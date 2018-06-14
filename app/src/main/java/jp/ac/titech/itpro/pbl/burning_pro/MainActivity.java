package jp.ac.titech.itpro.pbl.burning_pro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

	private TwitterManager twitterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        twitterManager = new TwitterManager(this);
    }


    public void tweetTest(View v) {
    	twitterManager.openTwitter("楽しい！ 人生！");
	}
}
