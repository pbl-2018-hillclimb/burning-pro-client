package jp.ac.titech.itpro.pbl.burning_pro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToImprudentTweetActivity(View v) {
        Intent intent = new Intent(getApplication(), ImprudentTweetActivity.class);
        intent.putExtra("phrase", "{大学生}なんだから自分で判断して{休み}たかったら{休めば}いいのではと思ってしまいます．");
        startActivity(intent);
    }
}
