package jp.ac.titech.itpro.pbl.burning_pro;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/*
* Implemented by builder pattern
*
* usage :
* new TweetIntentBuilder("tweet text").openTwitter();
*
* new TweetIntentBuilder("tweet text")
*		.url("https://example.com")
*		.openTwitter();
*
* new TweetIntentBuilder("tweet text")
*		.url("https://example.com")
*		.hashtag("hashtag1")
*		.hashtag("hashtag2")
*		.openTwitter();
*
* */

public class TweetWebIntent {
	private final String text;
	private final String intent_head = "https://twitter.com/intent/tweet?";

	private String url = null;
	private ArrayList<String> hashtags = new ArrayList<String>();

	public TweetWebIntent(String text) {
		this.text = text;
	}

	public TweetWebIntent url(String val) {
		url = val;
		return this;
	}

	public TweetWebIntent hashtag(String val) {
		hashtags.add(val);
		return this;
	}

	private Uri buildUri() throws UnsupportedEncodingException {
		String intent_text = URLEncoder.encode(text, "UTF-8");
		String tweet_intent_url =
			String.format("https://twitter.com/intent/tweet?text=%s", intent_text);

		if(url != null) {
			String intent_url = URLEncoder.encode(url, "UTF-8");
			tweet_intent_url += String.format("&url=%s", intent_url);
		}

		if(!hashtags.isEmpty()){
			tweet_intent_url += "&hashtags=";
			boolean foundAny = false;
			for(String tag : hashtags) {
				if (foundAny)
					tweet_intent_url += ",";
				else
					foundAny = true;
				tweet_intent_url += URLEncoder.encode(tag, "UTF-8");
			}
		}

		return Uri.parse(tweet_intent_url);
	}

	public void openTwitter(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			Uri uri = buildUri();
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);

			if (intent.resolveActivity(pm) != null) {
				context.startActivity(intent);
			} else {
				Toast.makeText(context,
					R.string.toast_no_view_activity, Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
