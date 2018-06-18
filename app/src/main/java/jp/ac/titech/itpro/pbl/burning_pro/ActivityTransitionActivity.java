package jp.ac.titech.itpro.pbl.burning_pro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityTransitionActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transition);
	}

	public void transitionTest(View v) {
		Intent intent = new Intent(getApplication(), ActivityTransitionActivity.class);
		startActivity(intent);
	}

	public void transitionTest2(View v) {
		setContentView(R.layout.activity_transition);
	}

	public void transitionTest3(View v) {
		Intent intent = new Intent(getApplication(), MainActivity.class);
		startActivity(intent);
	}

	public void transitionTest4(View v) {
		setContentView(R.layout.activity_main);
	}

}
