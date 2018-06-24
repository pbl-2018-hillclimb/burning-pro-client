package jp.ac.titech.itpro.pbl.burning_pro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestJSON {
    private HttpURLConnection connection;
    private static final int DEFAULT_CONNECT_TIMEOUT = 10000;
    private static final int DEFAULT_READ_TIMEOUT = 15000;


    HttpRequestJSON(URL url) throws IOException {
        connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
        connection.setReadTimeout(DEFAULT_READ_TIMEOUT);
        connection.setDoInput(true);
    }


    public HttpRequestJSON setConnectTimeout(int timeout) {
        connection.setConnectTimeout(timeout);
        return this;
    }

    public HttpRequestJSON setReadTimeout(int timeout) {
        connection.setConnectTimeout(timeout);
        return this;
    }

    private String getResponseString() throws IOException{
        InputStreamReader reader = null;
        try {
            connection.connect();
            reader = new InputStreamReader(connection.getInputStream(), "UTF-8");
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[512];
            int len;
            while (true) {
                len = reader.read(buf);
                if (len < 0)
                    break;
                sb.append(buf, 0, len);
            }
            return sb.toString();
        } finally {
            connection.disconnect();
            if(reader != null)
                reader.close();
        }
    }

    public JSONArray requestJSONArray() throws IOException, JSONException {
        return new JSONArray(getResponseString());
    }

    public JSONObject requestJSONObject() throws IOException, JSONException {
        return new JSONObject(getResponseString());
    }

}
