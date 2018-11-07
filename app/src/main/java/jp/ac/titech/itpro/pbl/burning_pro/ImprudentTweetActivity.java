package jp.ac.titech.itpro.pbl.burning_pro;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.CharSequence;
import java.util.ArrayList;
import java.util.List;

/**
 * "phrase"にテンプレートツイート文字列を持つようなIntentを作成し、startActivityを呼び出して利用する。
 * 編集可能にしたい文字列は"{","}"で囲み、中にヒントとして表示したい文字列を入れる。
 * 文字列に"{","}"を含めたい場合は"{{","}}"とエスケープする
 * ex. "これは固定の文字列で、{これはヒント}です。{{これも固定の文字列}}です。"
 */
public class ImprudentTweetActivity extends AppCompatActivity {
    private final static String burningHashTag = "burningpro";
    private final static String BUNDLE_TEXTBOX_CONTENTS = "textbox_contents";

    private ArrayList<EditText> textboxes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imprudent_tweet);
        setTitle(getString(R.string.imprudence_tweet_label));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String phrase = intent.getExtras().getString("phrase");
        LinearLayout container = findViewById(R.id.ImprudentTweetArea);
        ArrayList<String> textboxContents = null;
        if (savedInstanceState != null) {
            textboxContents = savedInstanceState.getStringArrayList(BUNDLE_TEXTBOX_CONTENTS);
        }
        createPhrasesViews(container, phrase, textboxContents);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        ArrayList<String> textboxContents = new ArrayList<>();
        for (EditText edit: textboxes) {
            textboxContents.add(edit.getText().toString());
        }
        savedInstanceState.putStringArrayList(BUNDLE_TEXTBOX_CONTENTS, textboxContents);
    }

    /** 発言を表示する widget を与えられた発言文字列から動的に生成し、可能で
     *  あれば入力内容の再現も行う。
     */
    private void createPhrasesViews(ViewGroup container, String phrase, List<String> textboxContents) {
        /*
            phrase[from,to)が次のTextView/EditTextの区間
            次の"{","}"を検索し始めるのがstart
         */
        int to = 0, from = 0, start = 0;
        String subPhrase;
        int i = 0;
        while (to != -1) {
            while (true) {
                to = phrase.indexOf("{", start);
                if (to == -1) break;
                if (to + 1 < phrase.length() && phrase.charAt(to + 1) == '{') {
                    start = to + 2;
                    continue;
                }
                break;
            }
            if (to == -1 && from < phrase.length())
                subPhrase = phrase.substring(from);
            else if (to != -1 && from != to)
                subPhrase = phrase.substring(from, to);
            else subPhrase = null;
            if (subPhrase != null)
                addTextView(container, subPhrase.replace("{{", "{").replace("}}", "}"));
            if (to == -1) break;
            from = start = to + 1;
            while (true) {
                to = phrase.indexOf("}", start);
                if (to == -1) break;
                if (to + 1 < phrase.length() && phrase.charAt(to + 1) == '}') {
                    start = to + 2;
                    continue;
                }
                break;
            }
            if (to == -1 && from < phrase.length())
                subPhrase = phrase.substring(from);
            else if (to != -1)
                subPhrase = phrase.substring(from, to);
            else subPhrase = null;
            textboxes.add(addEditText(container, subPhrase.replace("{{", "{").replace("}}", "}")));
            if (textboxContents != null) {
                int textboxIndex = textboxes.size() - 1;
                textboxes.get(textboxIndex).setText(textboxContents.get(textboxIndex));
            }
            from = start = to + 1;
        }
    }

    private void addTextView(ViewGroup container, String subPhrase) {
        TextView view = new TextView(this);
        view.setText(subPhrase);
        container.addView(view);
    }

    /** 新しく追加された `EditText` を返す。 */
    private EditText addEditText(ViewGroup container, String subPhrase) {
        EditText edit = new EditText(this);
        edit.setHint(subPhrase);
        container.addView(edit);
        return edit;
    }

    private String getTweetText() {
        StringBuilder phrase = new StringBuilder();
        LinearLayout container = findViewById(R.id.ImprudentTweetArea);
        for (int i = 0; i < container.getChildCount(); ++i) {
            // `EditText` is a subclass of `TextView`.
            TextView element = (TextView) container.getChildAt(i);
            CharSequence subPhrase = element.getText();
            // `CharSequence` does not have `isEmpty()`.
            if (subPhrase.length() == 0 && element instanceof EditText) {
                subPhrase = ((EditText) element).getHint();
            }
            phrase.append(subPhrase);
        }
        return phrase.toString();
    }

    public void tweet(View view) {
        new TweetAppIntent(getTweetText())
            .hashtag(burningHashTag)
            .openTwitter(this, false);
    }

    public void share(View view) {
        new TweetAppIntent(getTweetText())
            .hashtag(burningHashTag)
            .openTwitter(this, true);
    }
}
