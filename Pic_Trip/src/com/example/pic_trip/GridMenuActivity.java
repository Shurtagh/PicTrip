package com.example.pic_trip;

import java.util.ArrayList;

import DAO.PointDAO;
import ElementObject.Point;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GridMenuActivity extends Activity {
	
	private int tripId;
	private PointDAO pointDAO;
	private Builder adb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_menu);
		
		tripId = getIntent().getIntExtra("id", -1);
		
		pointDAO = new PointDAO(getApplicationContext());
		
		adb =  new AlertDialog.Builder(this)
		.setTitle("Information")
		.setMessage("Aucune photo pour ce voyage.")
		.setPositiveButton("Ok", null);
		
		
		//r�cup�ration des boutons
		Button btnEditer = (Button)findViewById(R.id.btnEditer);
		Button btnJournal = (Button)findViewById(R.id.btnJournal);
		Button btnTrace = (Button)findViewById(R.id.btnTrace);
		Button btnExplore = (Button)findViewById(R.id.btnExplore);
		
		//ajout des listenners
		//Editer
		btnEditer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//lancement de l'activit�
				Intent intent = new Intent(GridMenuActivity.this, MainActivity.class);
				intent.putExtra("id", tripId);
				intent.putExtra("type", 1);
            	startActivity(intent);
			}
		});
		
		//Journal
		btnJournal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//lancement de l'activit�
				
				ArrayList<Point> points = pointDAO.getAllPhotosOfTravel(tripId);
				if(points != null) {
					Intent intent = new Intent(GridMenuActivity.this, JournalActivity.class);
					intent.putExtra("id", tripId);
					startActivity(intent);
				} else {
					adb.show();
				}
			}
		});
		
		//Trac� interactif
		btnTrace.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//lancement de l'activit�
				Intent intent = new Intent(GridMenuActivity.this, MainActivity.class);
				intent.putExtra("id", tripId);
				intent.putExtra("type", 2);
            	startActivity(intent);
			}
		});
		
		//Exploration
		btnExplore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//lancement de l'activit�
				Intent intent = new Intent(GridMenuActivity.this, MainActivity.class);
				intent.putExtra("id", tripId);
				intent.putExtra("type", 3);
            	startActivity(intent);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
