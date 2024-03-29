package backend;

import org.json.*;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.*;
import javax.net.ssl.HttpsURLConnection;

public class OxfordApi {
    private static final String OUT_FILE = "./src/main/java/backend/word.txt";

    /**
     * get OxfordApi URL for an english word.
     * @param word
     * @return
     */
    public static String getOxford(String word){
        String language = "en-gb";
        String fields = "definitions%2Cexamples%2Cpronunciations";
        String strictMatch = "false";
        String word_id = word.toLowerCase();
        return doInBackground("https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch);
    }

    /**
     * request URL that we got above.
     * @param params url
     * @return json data of requested word
     */
    private static String doInBackground(String... params) {
        final String APP_ID = "5a1fa363";
        final String APP_KEY = "e74771a4ef0d1911c7ee2a863829ed1a";

        try {
            URL url = new URL(params[0]);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("app_id",APP_ID);
            urlConnection.setRequestProperty("app_key",APP_KEY);

            // read the output from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            return stringBuilder.toString();

        }
        catch (IOException e) {
            //e.printStackTrace();
            return "API-ERROR";
        }
    }


    /**
     * parse json to get definition, examples, phoneticSpelling,... of a word and write down to word.txt(to test)
     * @param jsonString String in json syntax
     * @throws IOException exception
     */
    public static String[] parseJsonString(String jsonString) throws IOException {
        FileWriter out_file = new FileWriter(OUT_FILE);
        String[] oxfordResponse = {"Not Found", "Not Found"};

        if (jsonString.equals("API-ERROR")) {
            out_file.write("hhi");
            out_file.close();
            return oxfordResponse;
        }

        try {
            StringBuilder definitions = new StringBuilder();

            JSONObject jsonObj = new JSONObject(jsonString);
            //System.out.println(jsonObj);

            JSONObject firstEntry = jsonObj.getJSONArray("results").getJSONObject(0).getJSONArray("lexicalEntries").getJSONObject(0).getJSONArray("entries").getJSONObject(0);
            //System.out.println(firstEntry);

            JSONObject pronunciations = firstEntry.getJSONArray("pronunciations").getJSONObject(0);
            String phoneticSpelling = pronunciations.getString("phoneticSpelling");
            String audioFile = pronunciations.getString("audioFile");
            oxfordResponse[0] = audioFile;

            out_file.write(jsonString + '\n');
            out_file.write("Audio link       : " + audioFile + '\n');
            out_file.write("Phonetic Spelling: " +phoneticSpelling + '\n' + '\n');
            definitions.append("Phonetic Spelling: " +phoneticSpelling + '\n' + '\n');


            JSONArray senses = firstEntry.getJSONArray("senses");

            for (int i = 0; i < senses.length(); ++i) {
                JSONObject sense = senses.getJSONObject(i);
                String definition = sense.getJSONArray("definitions").getString(0);//.getJSONObject(0);
                String examples = sense.getJSONArray("examples").getJSONObject(0).getString("text");
                out_file.write("Definition: " + definition + '\n');
                out_file.write("Example   : " + examples + '\n' + '\n');
                definitions.append("Definition: " + definition + '\n' + "Example   : " + examples + '\n' + '\n');
            }
            oxfordResponse[1] = definitions.toString();
        }
        catch(JSONException e) {
            System.out.println("Can't parse json");
        }
        out_file.close();
        return oxfordResponse;
    }


    /**
     * try OxfordApi with a word.
     * @param args nothing
     * @throws IOException exception
     */
    public static void main(String[] args) throws IOException {
       String[] oxfordResponse = OxfordApi.parseJsonString(OxfordApi.getOxford("some"));
       System.out.println(oxfordResponse[0]);
       System.out.println(oxfordResponse[1]);
    }
}
