package jp.ac.titech.itpro.pbl.burning_pro;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

public class TwitterManager {

	public static void openTwitter(Context context, Uri uri) {
		PackageManager pm = context.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);

		if (intent.resolveActivity(pm) != null) {
			context.startActivity(intent);
		} else {
			Toast.makeText(context,
				R.string.toast_no_view_activity, Toast.LENGTH_LONG).show();
		}
	}

}
