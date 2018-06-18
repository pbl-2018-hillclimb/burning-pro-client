package jp.ac.titech.itpro.pbl.burning_pro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void tweetTest(View v) {
		new TweetWebIntent("楽しい！ 人生！")
			.openTwitter(this);
	}

	public void tweetTest2(View v) {
		new TweetWebIntent("楽しい！ 人生！")
			.url("https://twitter.com/chakku_000")
			.openTwitter(this);
	}

	public void tweetTest3(View v) {
		new TweetWebIntent("楽しい！ 人生！")
			.url("https://twitter.com/chakku_000")
			.hashtag("炎上")
			.hashtag("我が人生")
			.openTwitter(this);
	}

	public void generateTest(View v) {
		TextView imprudence_text = findViewById(R.id.ImprudenceText);
    	imprudence_text.setText(new Imprudence().generateImprudenceText());
	}

}
