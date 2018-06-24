package jp.ac.titech.itpro.pbl.burning_pro;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private ArrayAdapter<JSONObject> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imprudence_list);

        ListView listView = findViewById(R.id.imprudence_list_view);
        listAdapter =
            new ImprudenceListAdapter(this, 0, new ArrayList<JSONObject>());
        listView.setAdapter(listAdapter);

        updateList();
    }

    public void updateList(){
        new RequestTask(this).execute();
    }

    protected void showResult(JSONArray res){
        try {
            for (int i = 0; i < res.length(); ++i) {
                listAdapter.add(res.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listAdapter.clear();
        }
    }

    private static class ImprudenceListAdapter extends ArrayAdapter<JSONObject> {
        private static final int MAX_TITLE_SIZE = 10;

        ImprudenceListAdapter
            (@NonNull Context context, int resource, @NonNull List<JSONObject> objects) {
            super(context, resource, objects);
        }

        private String getTitle(JSONObject imprudence){
            String title;
            try {
                title = imprudence.getJSONObject("phrase").getString("phrase");
                if(title.length() > MAX_TITLE_SIZE)
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
            requestURL = activity.getResources().getString(R.string.imprudence_request_url);
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {
            JSONArray res = new JSONArray();
            try {
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
