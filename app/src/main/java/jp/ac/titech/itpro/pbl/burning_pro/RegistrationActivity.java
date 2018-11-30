package jp.ac.titech.itpro.pbl.burning_pro;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity implements PostRequestTask.CallbackTask {

    private static Context context;
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

    PostRequestTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setTitle(getString(R.string.registration_label));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        context = this;
        // UIの取得
        inputPhrase = findViewById(R.id.inputPhrase);
        inputPerson = findViewById(R.id.inputPerson);
        inputURL = findViewById(R.id.inputURL);
        deleted_yes = findViewById(R.id.deleted_yes);
        checkable = findViewById(R.id.inputDateClickable);
        inputDate = findViewById(R.id.inputDate);

        task = new PostRequestTask();
        task.setOnCallback(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Context getContext(){
        return context;
    }

    /** チェックボックスに応じて，元ネタ投稿日時のUIを有効/無効にする */
    public void onCheckboxClicked(View view) {
        inputDate.setEnabled(checkable.isChecked());
    }

    /** フォームの送信ボタン押下時 */
    public void sendPhrase(View v) {
        // 空白処理（*発言，*発言者のみ）
        if (checkNull(inputPhrase) || checkNull(inputPerson)) {
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
                // UIデータのJSONを作成
                JSONObject sentJSON = createJSON();
                // JSONをサーバーにPOSTリクエストで送信
                task.execute(sentJSON);
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
     */
    public JSONObject createJSON(){
        JSONObject json = new JSONObject();
        try {
            json.put("phrase", inputPhrase.getText().toString());
            json.put("person", inputPerson.getText().toString());
            json.put("url", inputURL.getText().toString());
            json.put("deleted", deleted_yes.isChecked());
            String date = "";
            if (checkable.isChecked()) {
                Calendar date_c = Calendar.getInstance();
                date_c.set(Calendar.YEAR, inputDate.getYear());
                date_c.set(Calendar.MONTH, inputDate.getMonth());
                date_c.set(Calendar.DAY_OF_MONTH, inputDate.getDayOfMonth());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
                date = sdf.format(date_c.getTime());
            }
            json.put("published_at", date);
        } catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public void callback(Boolean result) {
        if (result) {
            Toast.makeText(RegistrationActivity.getContext(), "登録に成功しました", Toast.LENGTH_LONG).show();
            // ページ遷移
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(RegistrationActivity.getContext(), "登録に失敗しました", Toast.LENGTH_LONG).show();
        }
    }
}
