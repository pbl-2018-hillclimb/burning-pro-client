package jp.ac.titech.itpro.pbl.burning_pro;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * <p>
 * HttpリクエストでJSONを取得するためのクラス。
 * </p>

 * <p>
 * コネクションのパラメータの設定はビルダーパターンで行い、
 * {@link #requestJSONArray()} や {@link #requestJSONObject()}を呼んでリクエストを行う。
 * </p>
 *
 */
public class HttpRequestJSON {
    private HttpURLConnection connection;
    private static final int DEFAULT_CONNECT_TIMEOUT = 10000;
    private static final int DEFAULT_READ_TIMEOUT = 15000;


    /**
     * @param url リクエスト先のURL
     * @throws IOException コネクションオブジェクトの生成に失敗した場合
     */
    HttpRequestJSON(URL url) throws IOException {
        connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
        connection.setReadTimeout(DEFAULT_READ_TIMEOUT);
        connection.setDoInput(true);
    }

    /**
     * 接続を行う際のタイムアウトを設定する。
     * ミリ秒単位で設定を行う。デフォルトでは10000。
     * @param timeout タイムアウトまでの時間（ミリ秒単位）
     * @return 更新されたインスタンス
     */
    public HttpRequestJSON setConnectTimeout(int timeout) {
        connection.setConnectTimeout(timeout);
        return this;
    }

    /**
     * 読み込みの際のタイムアウトを設定する。
     * ミリ秒単位で設定を行う。デフォルトでは15000。
     * @param timeout タイムアウトまでの時間（ミリ秒単位）
     * @return 更新されたインスタンス
     */
    public HttpRequestJSON setReadTimeout(int timeout) {
        connection.setConnectTimeout(timeout);
        return this;
    }

    /**
     * getリクエストを投げ、返ってきたリクエストのInputStreamを文字列に変換する。
     * @throws IOException リクエストに失敗したり、InputStreamの読み込みに失敗した場合
     */
    @NonNull
    private String getResponseString() throws IOException{
        InputStreamReader reader = null;
        try {
            connection.connect();
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException();
            }
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

    /**
     * HTTPリクエストを投げてJSONのArrayを取得する。
     * @return リクエストで取得されたJSONのArray
     * @throws IOException リクエストに失敗した場合
     * @throws JSONException 得られた結果がJSONのArrayとして解釈できなかった場合
     */
    public JSONArray requestJSONArray() throws IOException, JSONException {
        return new JSONArray(getResponseString());
    }

    /**
     * HTTPリクエストを投げてJSONのObjectを取得する。
     * @return リクエストで取得されたJSONのObject
     * @throws IOException リクエストに失敗した場合
     * @throws JSONException 得られた結果がJSONのObjectとして解釈できなかった場合
     */
    public JSONObject requestJSONObject() throws IOException, JSONException {
        return new JSONObject(getResponseString());
    }

}
