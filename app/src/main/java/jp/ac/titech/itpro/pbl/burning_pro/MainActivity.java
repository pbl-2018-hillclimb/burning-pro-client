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

    public void transitionTest(View v) {
        Intent intent = new Intent(getApplication(), ImprudentTweetActivity.class);
        startActivity(intent);
    }
}
