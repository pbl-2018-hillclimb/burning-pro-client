package jp.ac.titech.itpro.pbl.burning_pro;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.app_name));
    }

    public void goToImprudence(View v) {
        Intent intent = new Intent(getApplication(), ImprudenceListActivity.class);
        startActivity(intent);
    }

    public void goToRegistration(View v) {
        Intent intent = new Intent(getApplication(), RegistrationActivity.class);
        startActivity(intent);
    }

}
