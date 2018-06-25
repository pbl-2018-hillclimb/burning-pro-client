package jp.ac.titech.itpro.pbl.burning_pro;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * <p>
 * Twitter Web Intent用のクラス。
 * </p>
 *
 * <p>
 * ビルダーパターンでパラメータを追加して、{@link #openTwitter(Context)} を呼ぶことでツイート用フォームを開く。
 * </p>
 *
 * 使用例 :
 * <pre>
 *     {@code
 *     new TweetIntentBuilder("tweet text")
 *         .url("https://example.com")
 *         .hashtag("hashtag1")
 *         .hashtag("hashtag2")
 *         .openTwitter();
 *     }
 *</pre>
 */

public class TweetWebIntent {
    private final String text;
    private final String intent_head = "https://twitter.com/intent/tweet?";

    private String url = null;
    private ArrayList<String> hashtags = new ArrayList<String>();

    /**
     * @param text ツイートの文面
     */
    public TweetWebIntent(String text) {
        this.text = text;
    }


    /**
     * URLをツイートに追加する。
     *
     * 指定できるのは一つのポストにつき一つまでで、複数回呼んだ場合は最後のものだけが有効になる。
     *
     * @param url URLの文字列
     * @return 更新されたインスタンス
     */
    public TweetWebIntent url(String url) {
        this.url = url;
        return this;
    }

    /**
     * ハッシュタグをポストに追加する。
     *
     * 一つのポストには複数のハッシュタグがつけられる。
     *
     * @param hashtag ハッシュタグの文字列。'#'は付けないで指定する。
     * @return 更新されたインスタンス
     */
    public TweetWebIntent hashtag(String hashtag) {
        this.hashtags.add(hashtag);
        return this;
    }

    /**
     * 指定されたパラメータからWeb Intent用のURIを生成する。
     *
     * @throws UnsupportedEncodingException UTF-8によるエンコードがサポートされていなかった場合
     */
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


    /**
     * 指定されたパラメータでツイート用フォームを開く。
     *
     * @param context フォームを開こうとしているコンテキスト（アクティビティなど）
     */
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
