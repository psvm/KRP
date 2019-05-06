package ru.spb.ratec.krpmonitoring;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import static ru.spb.ratec.krpmonitoring.Constants.IP_ADDRESSES;

public class MainActivity extends AppCompatActivity {
    Handler updateHandler;
    Thread serverThread = null;
    TextView mMessage[] = new TextView[8];
    ProgressBar progressBar[] = new ProgressBar[8];
    Socket socket[] = new Socket[8];
    boolean isAlarmed = false;
    Button btnResetAlarm;
    //ToDo make ArrayList


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnResetAlarm = findViewById(R.id.alarm_button);
        mMessage[0] = findViewById(R.id.message_0);
        mMessage[1] = findViewById(R.id.message_1);
        mMessage[2] = findViewById(R.id.message_2);
        mMessage[3] = findViewById(R.id.message_3);
        mMessage[4] = findViewById(R.id.message_4);
        mMessage[5] = findViewById(R.id.message_5);
        mMessage[6] = findViewById(R.id.message_6);
        mMessage[7] = findViewById(R.id.message_7);

        progressBar[0] = findViewById(R.id.progress_level_0);
        progressBar[1] = findViewById(R.id.progress_level_1);
        progressBar[2] = findViewById(R.id.progress_level_2);
        progressBar[3] = findViewById(R.id.progress_level_3);
        progressBar[4] = findViewById(R.id.progress_level_4);
        progressBar[5] = findViewById(R.id.progress_level_5);
        progressBar[6] = findViewById(R.id.progress_level_6);
        progressBar[7] = findViewById(R.id.progress_level_7);


        updateHandler = new Handler();

        int countID = 0;
        int userID = 10001;
        int i = 0;
        for (String address: IP_ADDRESSES) {
            this.serverThread = new Thread(new ClientThread(address, i));
            this.serverThread.start();
            i++;
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            for(int i = 0; i < socket.length; i++) {
                if (socket[i] != null) {
                    socket[i].close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    class ClientThread implements Runnable {

        private final String address;
        private final int viewNumber;

        ClientThread(String address, int viewNumber) {
            this.address = address;
            this.viewNumber = viewNumber;
        }

        @Override
        public void run() {
            StringBuilder message = new StringBuilder();
            try {
                InetAddress serverAddress = InetAddress.getByName(address);

                boolean isAdd = false;
                boolean isConnected = true;
                BufferedReader input = createNewInputStream(serverAddress, viewNumber);
                while (!Thread.currentThread().isInterrupted()) {
                        final String read = input.readLine();
                        if (read == null) {

                        Log.i("Main Activity", serverAddress + "is not connected. Trying again");

                        isConnected = false;
                        updateHandler.post(new updateUIThread(null, isConnected, viewNumber));
                        //                           socket.connect(socketAddress);
                        if (socket[viewNumber] != null) {
                            socket[viewNumber].close();
                        }

 //                       input = createNewInputStream(serverAddress);
                        Log.i("Main Activity", "New input stream created");


                    } else {
                        if (read.equals("<CurrentState>")) {
                            isAdd = true;
                        }
                        if (isAdd) {
                            message.append(read + "\n");
                        }
                        if (read.equals("</CurrentState>")) {
                            isAdd = false;

                            MonitorResponse response = Utils.parseXml(message.toString());
                            if (!isAlarmed && (response.getAlarmLevel() > 0)){
                                isAlarmed = true;
                                updateHandler.post(new setAlarm(response));
                            }

                            updateHandler.post(new updateUIThread(response, isConnected, viewNumber));
                            message.delete(0, Integer.MAX_VALUE);
                        }

                    }
                }

            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                updateHandler.post(new updateUIThread(null, false, viewNumber));
                Log.i("Exception", "Time out");
              } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                Log.i("Exception", "message no parse state is " + message);
          } finally {
                try {
                    //                   updateHandler.post(new updateUIThread(Constants.IP_ADDRESS + ":" + Constants.PORT + " is not available"));
                    if (socket[viewNumber] != null) {
                        socket[viewNumber].close();
                    }
                    Log.i("onDestroy", "Socket closed");
                    serverThread = new Thread(new ClientThread(address, viewNumber));
                    serverThread.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    class updateUIThread implements Runnable {
        private MonitorResponse response;
        private boolean isConnected;
        private int viewNumber;

        updateUIThread(MonitorResponse response, boolean isConnected, int viewNumber) {
            this.response = response;
            this.isConnected = isConnected;
            this.viewNumber = viewNumber;
        }

        @Override
        public void run() {
            if (isConnected) {
                mMessage[viewNumber].setText(response.getId());
                double maxLevel = 0.0;
                if (response != null) {
                    for (Detector detector : response.getDetectorState()) {
                        if (detector.getLevel() > maxLevel) {
                            maxLevel = detector.getLevel();
                        }

                    }
                }
                progressBar[viewNumber].setProgress((int) (maxLevel * 200));
                Log.i("ProgressBar", Double.toString(response.getDetectorState().get(0).getLevel()));

            } else {
                progressBar[viewNumber].setProgress(100);
                mMessage[viewNumber].setText("Not connected");
            }
        }
    }

    class setAlarm implements Runnable{
        MonitorResponse response;
        setAlarm(MonitorResponse response) {
            this.response = response;
        }

        @Override
        public void run(){
            btnResetAlarm.setVisibility(View.VISIBLE);
            String text = "Alarm!" + " " + response.getId() + " " + response.getIsotope();
            btnResetAlarm.setText(text);
        }
    }

    public void resetAlarm(View view) {
        btnResetAlarm.setVisibility(View.INVISIBLE);
        isAlarmed = false;
    }

    private BufferedReader createNewInputStream(InetAddress serverAddress, int viewNumber) throws IOException {
        Log.i("createNewInputStream", "Trying to make a new connect");
        socket[viewNumber] = new Socket();
        socket[viewNumber].connect(new InetSocketAddress(serverAddress, Constants.PORT), 10000);
        socket[viewNumber].setSoTimeout(5000);
        Log.i("Socket connect:", "" + socket[viewNumber].isConnected());


        return new BufferedReader(new InputStreamReader(socket[viewNumber].getInputStream()));
    }
}

