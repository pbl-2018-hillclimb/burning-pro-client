package jp.ac.titech.itpro.pbl.burning_pro;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** 不謹慎発言の一覧を表示するActivityのクラス。 */
public class ImprudenceListActivity extends AppCompatActivity {
    private ArrayList<PhraseEntry> phraseList;
    private ArrayAdapter<PhraseEntry> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imprudence_list);
        setTitle(getString(R.string.imprudence_list_label));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        phraseList = new ArrayList<>();

        ListView listView = findViewById(R.id.imprudence_list_view);
        listAdapter =
            new ImprudenceListAdapter(this, 0, new ArrayList<PhraseEntry>());
        listView.setAdapter(listAdapter);

        // ListViewのタッチイベントのListener
        AdapterView.OnItemClickListener clickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    PhraseEntry imprudence = phraseList.get(pos);
                    String phraseBody = imprudence.phrase.phrase;
                    Intent intent = new Intent(getApplication(), ImprudentTweetActivity.class);
                    intent.putExtra("phrase", phraseBody);
                    startActivity(intent);
                }
            };
        listView.setOnItemClickListener(clickListener);

        updateList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.imprudence_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_refresh_phrases:
                updateList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** 不謹慎発言のリストの更新を行う。 */
    public void updateList(){
        new RequestTask(this).execute();
    }

    /** 取得したJSONからViewの更新を行う。 */
    protected void refreshView(JSONArray res){
        phraseList.clear();
        listAdapter.clear();
        try {
            for (int i = 0; i < res.length(); ++i) {
                JSONObject obj = res.getJSONObject(i);
                phraseList.add(new PhraseEntry(obj));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            phraseList.clear();
        }
        listAdapter.addAll(phraseList);
        listAdapter.notifyDataSetChanged();
    }

    /** ListViewの要素を操作するAdapter。 */
    private static class ImprudenceListAdapter extends ArrayAdapter<PhraseEntry> {

        ImprudenceListAdapter
            (@NonNull Context context, int resource, @NonNull List<PhraseEntry> objects) {
            super(context, resource, objects);
        }

        @Override @NonNull
        public View getView(int pos, @Nullable View view, @NonNull ViewGroup parent) {
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                view = inflater.inflate(android.R.layout.simple_list_item_2,
                    parent, false);
            }
            PhraseEntry imprudence = getItem(pos);
            if (imprudence != null) {
                TextView titleText = view.findViewById(android.R.id.text1);
                titleText.setText(imprudence.phrase.title);

                TextView personText = view.findViewById(android.R.id.text2);
                personText.setText(imprudence.person.displayName);
            }
            return view;
        }
    }

    /** 通信を行う非同期処理。 */
    private static class RequestTask extends AsyncTask<Void, Void, Optional<JSONArray>> {
        private WeakReference<ImprudenceListActivity> activityRef;
        private String requestURL;

        RequestTask(ImprudenceListActivity activity) {
            activityRef = new WeakReference<>(activity);
            requestURL = activity.getResources().getString(R.string.good_phrase_ap);
        }

        @Override
        protected Optional<JSONArray> doInBackground(Void... voids) {
            try {
                URL url = new URL(requestURL);
                JSONArray res = new HttpRequestJSON(url).requestJSONArray();
                return Optional.of(res);
            } catch (Exception e) {
                e.printStackTrace();
                return Optional.empty();
            }
        }

        @Override
        protected void onPostExecute(Optional<JSONArray> res) {
            ImprudenceListActivity activity = activityRef.get();
            if (activity == null || activity.isFinishing())
                return;
            if (res.isPresent()) {
                activity.refreshView(res.get());
            } else {
                String msg =
                    activity.getResources().getString(R.string.toast_fail_to_get_phrases);
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
            }
        }
    }
}
