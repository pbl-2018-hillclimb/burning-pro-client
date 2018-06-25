package jp.ac.titech.itpro.pbl.burning_pro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImprudentTweetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imprudent_tweet);

        Intent intent = getIntent();
        String phrase = intent.getExtras().getString("phrase");
        LinearLayout container = findViewById(R.id.ImprudentTweetArea);
        int to = 0, from = 0;
        String subPhrase;
        while (to != -1) {
            while (true) {
                to = phrase.indexOf("{", from);
                if (to > 0 && phrase.charAt(to - 1) == '\\')
                    continue;
                break;
            }
            if (to == -1 && from < phrase.length())
                subPhrase = phrase.substring(from);
            else if (to != -1 && from != to)
                subPhrase = phrase.substring(from, to);
            else subPhrase = null;
            if (subPhrase != null)
                addTextView(container, subPhrase.replace("\\{", "{"));
            if (to == -1) break;
            from = to + 1;
            while (true) {
                to = phrase.indexOf("}", from);
                if (to > 0 && phrase.charAt(to - 1) == '\\')
                    continue;
                break;
            }
            if (to == -1 && from < phrase.length())
                subPhrase = phrase.substring(from);
            else if (to != -1)
                subPhrase = phrase.substring(from, to);
            else subPhrase = null;
            addEditText(container, subPhrase.replace("\\}", "}"));
            from = to + 1;
        }
    }

    private void addTextView(LinearLayout container, String subPhrase) {
        TextView view = new TextView(this);
        view.setText(subPhrase);
        container.addView(view);
    }

    private void addEditText(LinearLayout container, String subPhrase) {
        EditText edit = new EditText(this);
        edit.setText(subPhrase);
        container.addView(edit);
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
