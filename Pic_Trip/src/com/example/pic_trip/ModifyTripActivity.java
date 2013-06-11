package com.example.pic_trip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import DAO.TravelDAO;
import ElementObject.Travel;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class ModifyTripActivity extends Activity {
	
	TextView dateDebut;
	TextView dateFin;
	Date start;
	Date fin;
	int tripId;
	TravelDAO travelDAO;
	Intent intent;
	int id;
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_trip);
        
        tripId = getIntent().getIntExtra("id", -1);
        
        intent = new Intent(ModifyTripActivity.this, Menu.class);
        
        travelDAO = new TravelDAO(Menu.getContext());
        
        ArrayList<Travel> list =  travelDAO.getAll();
        id = list.get(tripId).getId();
        
        Travel voyage = travelDAO.getById(id);
        
        Button enregistrer = (Button)findViewById(R.id.ButtonSendForm);
        
        enregistrer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final EditText titleField = (EditText) findViewById(R.id.EditTextTripTitle);
		    	String title = titleField.getText().toString();
		    	final EditText descField = (EditText) findViewById(R.id.EditTextTripDesc);
		    	String desc = descField.getText().toString();
		    	final TextView dateDebut = (TextView) findViewById(R.id.TripDateDebutValue);
		    	final TextView dateFin = (TextView) findViewById(R.id.TripDateFinValue);
		    	
		    	boolean valid = true;
		    	
		    	if (title.matches("")) {
		    		titleField.setBackgroundResource(R.color.LightPink);
		    		valid = false;
		    	} else {
		    		titleField.setBackgroundResource(R.color.White);
		    	}
		    	if (valid) {
		    		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		    		Date debut = null;
		    		Date fin = null;
					try {
						debut = (Date) df.parse(dateDebut.getText().toString());
						fin = (Date) df.parse(dateFin.getText().toString());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		Travel t = new Travel(id, title, debut.getTime(), fin.getTime(), desc);
		    		travelDAO.updateTravel(t);
		    		startActivity(intent);
		    	}
			}
		});
        
        
        EditText titre = (EditText)findViewById(R.id.EditTextTripTitle);
        
        if(voyage.getName() != null) {
        	titre.setText(voyage.getName());
        }
        
        EditText description = (EditText)findViewById(R.id.EditTextTripDesc);
        
        if(voyage.getDescription() != null) {
        	description.setText(voyage.getDescription());
        }
        
        Long date_start = voyage.getDate_start();
        Long date_fin = voyage.getDate_stop();
        
        start = new Date(date_start);
        fin = new Date(date_fin);
        
        dateDebut = (TextView) findViewById(R.id.TripDateDebutValue);
        dateFin = (TextView) findViewById(R.id.TripDateFinValue);
        
        Calendar c = Calendar.getInstance();
        c.setTime(fin);
        int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		dateFin.setText(String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year));
		c.setTime(start);
        year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		dateDebut.setText(String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year));
        
        dateDebut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				OnDateSetListener DebutDateChange = new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
						dateDebut.setText(String.valueOf(arg3) + "/" + String.valueOf(arg2 + 1) + "/" + String.valueOf(arg1));
					}
					
				};
		        Calendar c = Calendar.getInstance();
		        c.setTime(start);
		        int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH);
				int day = c.get(Calendar.DAY_OF_MONTH);
				dateDebut.setText(String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year));
				DatePickerDialog dialog = new DatePickerDialog(arg0.getContext(), DebutDateChange, year, month, day);
				dialog.show();
			}
		});
        
        dateFin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				OnDateSetListener DebutDateChange = new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
						dateFin.setText(String.valueOf(arg3) + "/" + String.valueOf(arg2 + 1) + "/" + String.valueOf(arg1));
					}
					
				};
				Calendar c = Calendar.getInstance();
		        c.setTime(fin);
		        int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH);
				int day = c.get(Calendar.DAY_OF_MONTH);
				dateFin.setText(String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year));
				DatePickerDialog dialog = new DatePickerDialog(arg0.getContext(), DebutDateChange, year, month, day);
				dialog.show();
			}
		});
    }
    
}