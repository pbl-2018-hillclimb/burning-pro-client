package jp.ac.titech.itpro.pbl.burning_pro;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * 不謹慎発言の一覧を表示するActivityのクラス。
 */
public class ImprudenceListActivity extends AppCompatActivity {
    private HashMap<String, ArrayList<PhraseEntry>> phraseList;
    private ImprudenceListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imprudence_list);
        setTitle(getString(R.string.imprudence_list_label));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        phraseList = new HashMap<>();

        ExpandableListView listView = findViewById(R.id.imprudence_list_view);
        listAdapter = new ImprudenceListAdapter(this);
        listView.setAdapter(listAdapter);

        // ExpandableListView の子要素 (サブアイテム) のタッチイベントの Listener
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                ImprudenceListAdapter adapter = (ImprudenceListAdapter) parent.getExpandableListAdapter();
                PhraseEntry entry = adapter.getChild(groupPosition, childPosition);
                String phraseBody = entry.phrase.phrase;
                Intent intent = new Intent(getApplication(), ImprudentTweetActivity.class);
                intent.putExtra("phrase", phraseBody);
                startActivity(intent);
                // The click would be always handled.
                return true;
            }
        });

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

    /**
     * 不謹慎発言のリストの更新を行う。
     */
    public void updateList() {
        new RequestTask(this).execute();
    }

    /**
     * 取得したJSONからViewの更新を行う。
     */
    protected void refreshView(JSONArray res) {
        phraseList.clear();
        listAdapter.clear();
        try {
            for (int i = 0; i < res.length(); ++i) {
                JSONObject obj = res.getJSONObject(i);
                String displayName = obj.getJSONObject("person").getString("display_name");
                if (!phraseList.containsKey(displayName)) {
                    phraseList.put(displayName, new ArrayList<PhraseEntry>());
                }
                List entries = phraseList.get(displayName);
                entries.add(new PhraseEntry(obj));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            phraseList.clear();
        }
        listAdapter.setEntries(phraseList);
        ExpandableListView listView = findViewById(R.id.imprudence_list_view);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            listView.expandGroup(i);
        }
        listAdapter.notifyDataSetChanged();
    }

    /**
     * ExpandableListViewの要素を操作するAdapter。
     */
    private static class ImprudenceListAdapter extends BaseExpandableListAdapter {
        private List<String> groups;
        private List<List<PhraseEntry>> children;
        private Context context = null;

        /**
         * Constructor
         */
        public ImprudenceListAdapter(@NonNull Context ctx) {
            this.groups = new ArrayList<>();
            this.children = new ArrayList<>();
            this.context = ctx;
        }

        public ImprudenceListAdapter(@NonNull Context ctx, @NonNull List<String> groups, @NonNull List<List<PhraseEntry>> children) {
            this.groups = groups;
            this.children = children;
            this.context = ctx;
        }

        public void clear() {
            groups.clear();
            children.clear();
        }

        public void setEntries(@NonNull HashMap<String, ArrayList<PhraseEntry>> entries) {
            clear();

            List<String> groups = new ArrayList<>();
            groups.addAll(entries.keySet());
            groups.sort(null);
            this.groups.addAll(groups);
            for (String groupName : groups) {
                this.children.add(entries.get(groupName));
            }
        }

        @Override
        public PhraseEntry getChild(int groupPosition, int childPosition) {
            return children.get(groupPosition).get(childPosition);
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return children.get(groupPosition).size();
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public String getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return groups.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        @NonNull
        public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(android.R.layout.simple_list_item_2,
                    parent, false);
            }
            String groupName = getGroup(groupPosition);
            if (groupName != null) {
                TextView personText = view.findViewById(android.R.id.text1);
                personText.setText(groupName);
                personText.setTypeface(Typeface.DEFAULT_BOLD);

                int numPhrases = getChildrenCount(groupPosition);
                TextView countText = view.findViewById(android.R.id.text2);
                countText.setText(numPhrases + " 件の発言");
            }
            return view;
        }

        @Override
        @NonNull
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(android.R.layout.simple_list_item_1,
                    parent, false);
            }
            PhraseEntry imprudence = getChild(groupPosition, childPosition);
            if (imprudence != null) {
                TextView titleText = view.findViewById(android.R.id.text1);
                titleText.setText(imprudence.phrase.title);
            }
            return view;
        }
    }

    /**
     * 通信を行う非同期処理。
     */
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
