package jp.ac.titech.itpro.pbl.burning_pro;

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

import org.json.JSONObject;

import java.util.ArrayList;

public class ImprudenceListActivity extends AppCompatActivity {
    private ArrayAdapter<JSONObject> imprudenceListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imprudence_list);

        ListView imprudenceListView = findViewById(R.id.imprudence_list_view);
        imprudenceListAdapter =
            new ArrayAdapter<JSONObject>(this, 0, new ArrayList<JSONObject>()){
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
                        TextView sensorNameText = view.findViewById(android.R.id.text1);
                        TextView sensorTypeText = view.findViewById(android.R.id.text2);
                        sensorNameText.setText("Title");
                        sensorTypeText.setText("Person");
                    }
                    return view;
                }
            };
        imprudenceListView.setAdapter(imprudenceListAdapter);
    }
}
