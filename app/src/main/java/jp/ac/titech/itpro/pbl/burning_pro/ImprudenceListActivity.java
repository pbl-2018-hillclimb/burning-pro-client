package jp.ac.titech.itpro.pbl.burning_pro;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImprudenceListActivity extends AppCompatActivity {
    private ArrayList<JSONObject> phraseList;
    private ArrayAdapter<JSONObject> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imprudence_list);

        phraseList = new ArrayList<>();

        ListView listView = findViewById(R.id.imprudence_list_view);
        listAdapter =
            new ImprudenceListAdapter(this, 0, new ArrayList<JSONObject>());
        listView.setAdapter(listAdapter);

        AdapterView.OnItemClickListener clickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    JSONObject imprudence = phraseList.get(pos);
                    try {
                        String phraseBody =
                            imprudence.getJSONObject("phrase").getString("phrase");
                        Intent intent = new Intent(getApplication(), ImprudentTweetActivity.class);
                        intent.putExtra("phrase", phraseBody);
                        startActivity(intent);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            };
        listView.setOnItemClickListener(clickListener);

        updateList();
    }

    public void updateList(){
        new RequestTask(this).execute();
    }

    protected void showResult(JSONArray res){
        try {
            for (int i = 0; i < res.length(); ++i) {
                phraseList.add(res.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            phraseList.clear();
        }
        listAdapter.addAll(phraseList);
        listAdapter.notifyDataSetChanged();
    }

    private static class ImprudenceListAdapter extends ArrayAdapter<JSONObject> {
        private static final int MAX_TITLE_SIZE = 10;

        ImprudenceListAdapter
            (@NonNull Context context, int resource, @NonNull List<JSONObject> objects) {
            super(context, resource, objects);
        }

        // TODO : Add attribute "TITLE" to JSON
        private String getTitle(JSONObject imprudence){
            String title;
            try {
                title = imprudence.getJSONObject("phrase").getString("phrase");
                if(title != null && title.length() > MAX_TITLE_SIZE)
                    title = title.substring(0, MAX_TITLE_SIZE);
            } catch (JSONException e){
                e.printStackTrace();
                title = null;
            }
            return title;
        }

        private String getPerson(JSONObject imprudence) {
            String person;
            try {
                person = imprudence.getJSONObject("person").getString("display_name");
            } catch (JSONException e) {
                e.printStackTrace();
                person = null;
            }
            return person;
        }

        @Override
        public @NonNull
        View getView(int pos, @Nullable View view, @NonNull ViewGroup parent) {
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                view = inflater.inflate(android.R.layout.simple_list_item_2,
                    parent, false);
            }
            JSONObject imprudence = getItem(pos);
            if (imprudence != null) {
                TextView titleText = view.findViewById(android.R.id.text1);
                String title = getTitle(imprudence);
                if(title != null)
                    titleText.setText(title);

                TextView personText = view.findViewById(android.R.id.text2);
                String person = getPerson(imprudence);
                if(person != null)
                    personText.setText(person);
            }
            return view;
        }
    }

    private static class RequestTask extends AsyncTask<Void, Void, JSONArray> {
        private WeakReference<ImprudenceListActivity> activityRef;
        private String requestURL;

        RequestTask(ImprudenceListActivity activity) {
            activityRef = new WeakReference<>(activity);
            requestURL = "https://api.myjson.com/bins/142bdq";
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {
            JSONArray res = new JSONArray();
            try {
                // currently, this is testing url
                URL url = new URL(requestURL);
                res = new HttpRequestJSON(url).requestJSONArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(JSONArray res) {
            ImprudenceListActivity activity = activityRef.get();
            if(activity == null || activity.isFinishing())
                return;
            activity.showResult(res);
        }
    }

}
