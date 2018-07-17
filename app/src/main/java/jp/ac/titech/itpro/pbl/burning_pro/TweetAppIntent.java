package jp.ac.titech.itpro.pbl.burning_pro;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Parcelable;
import android.support.v4.app.ShareCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Twitter App Intent用のクラス
 * </p>
 *
 * <p>
 * ビルダーパターンでパラメータを追加して、 {@link #openTwitter(Activity, boolean)} を呼ぶことでアプリケーションに飛ぶ。
 * </p>
 *
 * 使用例 :
 * <pre>
 *     {@code
 *     new TweetAppIntent("tweet text")
 *         .url("https://example.com")
 *         .hashtag("hashtag1")
 *         .hashtag("hashtag2")
 *         .openTwitter();
 *     }
 * </pre>
 */

public class TweetAppIntent {
    private final String text;

    private String url = null;
    private ArrayList<String> hashtags = new ArrayList<>();

    /**
     * @param text ツイートの文面
     */
    public TweetAppIntent(String text) {
        this.text = text;
    }

    /**
     * URLをツイートに追加する。
     *
     * @param url URLの文字列
     * @return 更新されたインスタンス
     */
    public TweetAppIntent url(String url) {
        this.url = url;
        return this;
    }

    /**
     * ハッシュタグをポストに追加する。
     *
     * 一つのポストには複数のハッシュタグがつけられる。
     *
     * @param hashtag ハッシュタグの文字列。 '#'は付けないで指定する。
     * @return 更新されたインスタンス
     */
    public TweetAppIntent hashtag(String hashtag) {
        this.hashtags.add(hashtag);
        return this;
    }

    /**
     * 指定されたパラメータでアプリを開く
     *
     * @param activity フォームを開こうとしているアクティビティ
     * @param anyApp   trueなら任意のアプリを選択できる。falseならtwiを含むアプリから選択される。アプリが存在しない場合はTwitterのWebページに飛ばされる。
     */
    public void openTwitter(Activity activity, boolean anyApp) {
        try {
            if (anyApp) {
                ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(activity);
                builder.setChooserTitle("Choose App");
                builder.setText(this.text);
                builder.setType("text/plain");
                builder.startChooser();
            } else {
                List<Intent> shareIntentList = new ArrayList<Intent>();
                for (ResolveInfo info : activity.getPackageManager().queryIntentActivities(new Intent(Intent.ACTION_SEND).setType("text/plain"), 0)) {
                    Log.e("TAI", String.format("Package:%s", info.activityInfo.packageName));
                    if (info.activityInfo.packageName.toLowerCase().contains("twi")) {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND).setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                        shareIntent.setPackage(info.activityInfo.packageName);
                        shareIntentList.add(shareIntent);
                    }
                }
                if (shareIntentList.isEmpty()) {
                    new TweetWebIntent(text).openTwitter(activity);
                } else {
                    Intent chooserIntent = Intent.createChooser(shareIntentList.remove(0), "Choose Twitter App");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, shareIntentList.toArray(new Parcelable[]{}));
                    activity.startActivity(chooserIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
