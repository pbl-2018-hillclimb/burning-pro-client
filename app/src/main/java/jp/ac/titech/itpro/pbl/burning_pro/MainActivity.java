package jp.ac.titech.itpro.pbl.burning_pro;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void tweetTest(View v) {
    	try {
			Uri tweetIntent = new TweetIntentBuilder("楽しい！ 人生！").build();
			TwitterManager.openTwitter(this, tweetIntent);
		} catch (Exception e) {
    		e.printStackTrace();
		}
	}

	public void tweetTest2(View v) {
		try {
			Uri tweetIntent =
				new TweetIntentBuilder("楽しい！ 人生！")
					.url("https://twitter.com/chakku_000")
					.build();
			TwitterManager.openTwitter(this, tweetIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void tweetTest3(View v) {
		try {
			Uri tweetIntent =
				new TweetIntentBuilder("楽しい！ 人生！")
					.url("https://twitter.com/chakku_000")
					.hashtag("炎上")
					.hashtag("我が人生")
					.build();
			TwitterManager.openTwitter(this, tweetIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
