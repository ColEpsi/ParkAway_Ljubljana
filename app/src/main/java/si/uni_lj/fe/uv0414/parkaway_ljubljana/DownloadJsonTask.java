package si.uni_lj.fe.uv0414.parkaway_ljubljana;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadJsonTask extends AsyncTask<String, Void, String> {
    private static final String DEBUG_TAG = "DownloadJsonTask";
    String data;

    private DownloadCallback caller;

    // Tole je konstruktor. Ob ustvarjanju objekta iz tega razreda mora klicatelj (uporabnik tega razreda)
    // posredovati sklic nase, tako da ga lahko ta razred obvešča o napredku.
    // Klicatelj je lahko vsak razred, ki deduje (izdela) vmesnik DownloadCallback
    DownloadJsonTask(DownloadCallback caller){
        this.caller=caller;
    }

    @Override
    protected String doInBackground(String... urls) {

        // params comes from the execute() call: params[0] is the url.
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        try {
            caller.updateFromDownload(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String downloadUrl(String myurl) throws IOException {
        try {
            URL url = new URL(myurl);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null){
                line = bufferedReader.readLine();
                data += line;
            }
            data = data.substring(4);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
