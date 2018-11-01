package jp.ac.titech.itpro.pbl.burning_pro;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

/** 非同期でサーバーにデータをPOSTで送信 */
public class PostRequestTask extends AsyncTask<JSONObject, Void, String> {
    private static final int DEFAULT_CONNECT_TIMEOUT = 10000;
    private static final int DEFAULT_READ_TIMEOUT = 15000;

    private HttpURLConnection connection = null;
    private String result = "";
    @Override
    protected String doInBackground(JSONObject... jsons) {
        try {
            URL postURL = new URL(RegistrationActivity.getContext().getString(R.string.registration_url));
            connection = (HttpURLConnection) postURL.openConnection();

            connection.setRequestMethod("POST");
            connection.setInstanceFollowRedirects(false);
            connection.setRequestProperty("Accept-Language", "jp");
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
            connection.setReadTimeout(DEFAULT_READ_TIMEOUT);

            connection.connect();

            OutputStream os = connection.getOutputStream();
            PrintStream ps = new PrintStream(os);
            Log.d("test", jsons[0].toString());
            ps.print(jsons[0].toString());

            ps.close();
            os.close();

            // レスポンスを取得
            final int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                Toast.makeText(RegistrationActivity.getContext(), "登録に失敗しました", Toast.LENGTH_LONG).show();
                result = "HTTP_OK";
            }
            else{
                Toast.makeText(RegistrationActivity.getContext(), "登録に失敗しました", Toast.LENGTH_LONG).show();
                result = "status="+String.valueOf(status);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }
}
