package jp.ac.titech.itpro.pbl.burning_pro;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import java.net.URLEncoder;

public class TwitterManager {
	private final static String TAG = "TwitterManager";
	private Context context;
	private PackageManager packageManager;

	TwitterManager(Context context) {
		this.context = context;
		this.packageManager = context.getPackageManager();
	}

	public void openTwitter(String text) {
		try {
			String intent_head =
				context.getResources().getString(R.string.tweet_intent_head);
			String intent_text = URLEncoder.encode(text, "UTF-8");
			String tweet_intent_url =
				String.format("%stext=%s", intent_head, intent_text);

			Uri uri = Uri.parse(tweet_intent_url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);

			if (intent.resolveActivity(packageManager) != null) {
				context.startActivity(intent);
			} else {
				Toast.makeText(context,
					R.string.toast_no_view_activity, Toast.LENGTH_LONG).show();
			}

		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
