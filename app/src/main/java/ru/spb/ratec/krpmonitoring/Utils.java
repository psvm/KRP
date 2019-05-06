package ru.spb.ratec.krpmonitoring;



import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

class Utils {
    static MonitorResponse parseXml(String xmlResponse) throws XmlPullParserException, IOException {
        MonitorResponse response = new MonitorResponse();
        ArrayList<Detector> detectorsState = new ArrayList<>();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(new StringReader(xmlResponse));
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {

            if (eventType == XmlPullParser.START_TAG && isEquals(xpp, Constants.XML_ID)) {
                response.setId(xpp.nextText());
            }
            if (eventType == XmlPullParser.START_TAG && isEquals(xpp, Constants.XML_DEVICE)) {
                response.setDevice(xpp.nextText());
            }
            if (eventType == XmlPullParser.START_TAG && isEquals(xpp, Constants.XML_DEVICE_STATE)) {
                response.setDeviceState(Integer.parseInt(xpp.nextText()));
            }
            if (eventType == XmlPullParser.START_TAG && isEquals(xpp, Constants.XML_NUMBER_OF_DETECTORS)) {
                response.setNumDet(Integer.parseInt(xpp.nextText()));
            }
            if (eventType == XmlPullParser.START_TAG && isEquals(xpp, Constants.XML_ALARM_LEVEL)) {
                response.setAlarmLevel(Integer.parseInt(xpp.nextText()));
            }
            if (eventType == XmlPullParser.START_TAG && isEquals(xpp, Constants.XML_ISOTOPE)) {
                response.setIsotope(xpp.nextText());
            }
            if (eventType == XmlPullParser.START_TAG && isEquals(xpp, Constants.XML_ALARMED_DETECTOR)) {
                response.setAlarmDet(Integer.parseInt(xpp.nextText()));
            }
            if (eventType == XmlPullParser.START_TAG && isEquals(xpp, Constants.XML_THRESHOLDS)) {
                response.setThreshold(xpp.nextText());
            }

            if (eventType == XmlPullParser.START_TAG && isEquals(xpp, Constants.XML_DETECTORS)) {

                int i = 1;
                Detector detector;
                while (!(eventType == XmlPullParser.END_TAG && isEquals(xpp, Constants.XML_DETECTORS))) {


                    String currentDetector = Constants.XML_DETECTORS_NUMBER_PREFIX.concat(Integer.toString(i));
                    if (eventType == XmlPullParser.START_TAG && isEquals(xpp, currentDetector)) {
                        detector = new Detector();

                        while (!(eventType == XmlPullParser.END_TAG && isEquals(xpp, currentDetector))) {

                            if ((eventType == XmlPullParser.START_TAG)
                                    && isEquals(xpp, Constants.XML_DETECTOR_STATE)) {
                                detector.setDetState(Integer.parseInt(xpp.nextText()));
                            }

                            if (eventType == XmlPullParser.START_TAG
                                    && isEquals(xpp, Constants.XML_BLOCKING_OF_SCALE_STABILITY)) {
                                detector.setBlock(Integer.parseInt(xpp.nextText()));
                            }

                            if (eventType == XmlPullParser.START_TAG
                                    && isEquals(xpp, Constants.XML_DETECTORS_LEVEL)) {
                                detector.setLevel(Double.parseDouble(xpp.nextText()));
                            }

                            if (eventType == XmlPullParser.START_TAG
                                    && isEquals(xpp, Constants.XML_LEVEL_OF_ALARM_ON_DETECTOR)) {
                                detector.setDetAlarmLevel(Integer.parseInt(xpp.nextText()));
                            }
                            eventType = xpp.next();
                        }
                        detectorsState.add(detector);
                        i++;
                    }
                    eventType = xpp.next();

                }

                response.setDetectorState(detectorsState);

            }

            eventType = xpp.next();


        }


        return response;
    }

    private static boolean isEquals(XmlPullParser xpp, String tag) {
        if (xpp.getName() == null) {
            return false;
        } else {
            return xpp.getName().equalsIgnoreCase(tag);
        }
    }
}
