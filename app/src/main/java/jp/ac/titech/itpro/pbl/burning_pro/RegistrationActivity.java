package jp.ac.titech.itpro.pbl.burning_pro;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

public class RegistrationActivity extends AppCompatActivity {

    private static Context context;
    // *タイトル
    EditText inputTitle;
    // *発言
    EditText inputPhrase;
    // *発言者
    EditText inputPerson;
    // 元ネタURL
    EditText inputURL;
    // *元ネタ削除済み？
    RadioButton deleted_yes;
    // 元ネタ投稿日時を入力するか
    CheckBox checkable;
    // 元ネタ投稿日時
    DatePicker inputDate;
    // タグ
    EditText inputTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        context = this;
        // UIの取得
        inputTitle = findViewById(R.id.inputTitle);
        inputPhrase = findViewById(R.id.inputPhrase);
        inputPerson = findViewById(R.id.inputPerson);
        inputURL = findViewById(R.id.inputURL);
        deleted_yes = findViewById(R.id.deleted_yes);
        checkable = findViewById(R.id.inputDateClickable);
        inputDate = findViewById(R.id.inputDate);
        inputTag = findViewById(R.id.inputTag);
    }

    public static Context getContext(){
        return context;
    }

    /** チェックボックスに応じて，元ネタ投稿日時のUIを有効/無効にする */
    public void onCheckboxClicked(View view) {
        if (checkable.isChecked()) {
            inputDate.setEnabled(true);
        } else {
            inputDate.setEnabled(false);
        }
    }

    /** フォームの送信ボタン押下時 */
    public void sendPhrase(View v) {
        // 空白処理（*タイトル，*発言，*発言者のみ）
        if (checkNull(inputTitle) || checkNull(inputPhrase) || checkNull(inputPerson)) {
            return;
        }
        // 確認ダイアログの作成
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("確認");
        alertDialog.setMessage("本当に登録しますか？");
        // POSTリクエストをサーバに送る
        alertDialog.setPositiveButton("送信する", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // *タイトル，*発言，*発言者，元ネタURL，*元ネタ削除済み？の取得
                String title = inputTitle.getText().toString();
                String phrase = inputPhrase.getText().toString();
                String person = inputPerson.getText().toString();
                String url = inputURL.getText().toString();
                Boolean deleted = deleted_yes.isChecked();
                // 元ネタ投稿日時の取得（※日付のフォーマットが統一されたら以下を実装）
                // 現状，元ネタ投稿日時は""のみを送信
                String date = "";
                if (checkable.isChecked()) {
                    /*
                    Calendar date_c = Calendar.getInstance();
                    date_c.set(Calendar.YEAR, inputDate.getYear());
                    date_c.set(Calendar.MONTH, inputDate.getMonth());
                    date_c.set(Calendar.DAY_OF_MONTH, inputDate.getDayOfMonth());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                    date = sdf.format(date_c.getTime());
                    */
                }
                // タグの取得
                String tags = inputTag.getText().toString();
                // 取得したデータのJSONを作成
                JSONObject sentJSON = createJSON(title, phrase, person, url, deleted, date, tags);
                // JSONをサーバーにPOSTリクエストで送信
                new PostRequestTask().execute(sentJSON);
                // ページ遷移
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
            }
        });
        // キャンセル
        alertDialog.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 何もしないで閉じる
            }
        });
        alertDialog.create().show();
    }

    /**
     * @param editText 空白入力をチェックするUI
     */
    public Boolean checkNull(EditText editText){
        String text = editText.getText().toString();
        if (TextUtils.isEmpty(text) || text.trim().equals("")){
            editText.setError("文字を入力してください");
            return true;
        }
        return false;
    }

    /**
     * サーバに送るJSONを作成
     * @param title *タイトル
     * @param phrase *発言
     * @param person *発言者
     * @param url 元ネタURL
     * @param deleted *元ネタ削除済み？
     * @param date 元ネタ投稿日時
     * @param tags タグ
     */
    public JSONObject createJSON(String title, String phrase, String person, String url,
                           Boolean deleted, String date, String tags){
        JSONObject json = new JSONObject();
        try {
            json.put("title", title);
            json.put("phrase", phrase);
            json.put("person", person);
            json.put("url", url);
            json.put("deleted", deleted);
            json.put("published_at", date);
            json.put("tags", tags);
        } catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }
}
