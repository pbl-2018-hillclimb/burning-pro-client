package jp.ac.titech.itpro.pbl.burning_pro;

import android.net.Uri;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/*
* Implemented by builder pattern
*
* usage :
* Uri tweetIntent = new TweetIntentBuilder("tweet text").build();
*
* Uri tweetIntent =
*	new TweetIntentBuilder("tweet text")
*		.url("https://example.com")
*		.build();
*
* Uri tweetIntent =
*	new TweetIntentBuilder("tweet text")
*		.url("https://example.com")
*		.hashtag("hashtag1")
*		.hashtag("hashtag2")
*		.build();
*
* */

public class TweetIntentBuilder {
	private final String text;
	private final String intent_head = "https://twitter.com/intent/tweet?";

	private String url = null;
	private ArrayList<String> hashtags = new ArrayList<String>();

	public TweetIntentBuilder(String text) {
		this.text = text;
	}

	public TweetIntentBuilder url(String val) {
		url = val;
		return this;
	}

	public TweetIntentBuilder hashtag(String val) {
		hashtags.add(val);
		return this;
	}

	public Uri build() throws UnsupportedEncodingException {
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
}
