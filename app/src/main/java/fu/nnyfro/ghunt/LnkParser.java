package fu.nnyfro.ghunt;


import android.util.Log;

public class LnkParser {
    String[] stringParts;
    String[] pairs;

    public void parseDeeplink1(String s){


            stringParts = s.split("://");
            s = stringParts[stringParts.length - 1];
            pairs = s.split("&");
            deleteTargetUrl();



            int dlEqualIndex = pairs[0].indexOf('=');
            MainActivity.dl = pairs[0].substring(dlEqualIndex + 1);




            for (int i = 1; i <= pairs.length - 1; i++) {
                int equalIndex = pairs[i].indexOf('=');
                String key = "sub_id_" + i;
                String value = pairs[i].substring(equalIndex + 1);
                MainActivity.deeplinkCollection.put(key, value);
                Log.i("test_", "parseDeeplink1: " +key+"="+value);
            }


    }

    public void deleteTargetUrl(){

        if (pairs.length>0){

            if (pairs[pairs.length - 1].contains("?")) {
                String arr[]  = pairs[pairs.length - 1].split("\\?");
                pairs[pairs.length - 1] = arr[0];
            }
        }
    }



    public String parseDeeplink2(String inputString){
        String[] pairs = inputString.split("_");
        String outputString = "";
        if (pairs.length>0) {


            MainActivity.dl = pairs[0];
            Log.i("test_", "dl="+MainActivity.dl );

            for (int i = 1; i <= pairs.length - 1; i++) {
                String key = "sub_id_" + i;
                String value = pairs[i];
                MainActivity.deeplinkCollection.put(key, value);
                Log.i("test_", "parseDeeplink1: " +key+"="+value);
            }

        }
        return outputString;
    }
}
