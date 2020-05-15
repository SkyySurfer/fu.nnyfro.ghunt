package fu.nnyfro.ghunt;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

public class InstallRefferer {

    InstallReferrerClient referrerClient;
    final String utmSource = "utm_source", utmContent="utm_content";
    Map<String, String> collection = new TreeMap<>();

    public void buildInstallReferrer(Context context){
        referrerClient = InstallReferrerClient.newBuilder(context).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        ReferrerDetails response = null;
                        try {
                            response = referrerClient.getInstallReferrer();

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        String utmString = response.getInstallReferrer();

                        response.getReferrerClickTimestampSeconds();
                        response.getInstallBeginTimestampSeconds();
                        try {
                            utmString = java.net.URLDecoder.decode(utmString, "UTF-8");
                            Log.i("test_", "utmString " + utmString);
                            if (utmString != null){


                                if (utmString.contains(utmContent)&&!utmString.contains("organic")) {

                                    ThisApplication.hasDeeplink=true;

                                    Log.i("test_", "install refferer has deeplink");
                                    parseUtmString(utmString);
                                }

                            }



                        } catch (UnsupportedEncodingException e) {
                        }
                        ThisApplication.isInstallReferrerIsReady = true;
                        referrerClient.endConnection();
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        ThisApplication.isInstallReferrerIsReady = true;
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        ThisApplication.isInstallReferrerIsReady = true;
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
            }
        });

    }


    public void parseUtmString(String s){


        String[] pairs = s.split("&");

        for (int i = 0; i <= pairs.length - 1; i++) {
            int equalIndex = pairs[i].indexOf('=');
            String key = pairs[i].substring(0,equalIndex);
            String value = pairs[i].substring(equalIndex + 1);
            collection.put(key, value);
            Log.i("test_", key+"="+value);
        }

        if (collection.containsKey(utmSource)){
            MainActivity.source = collection.get(utmSource);
            Log.i("test_", "source: " +   MainActivity.source);
        }

        if (collection.containsKey(utmContent)){
            MainActivity.deeplink = collection.get(utmContent);
            Log.i("test_", "utm-content: " +   MainActivity.source);

        }


    }

}
