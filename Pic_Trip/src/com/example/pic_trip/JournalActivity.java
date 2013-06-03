/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.pic_trip;

import java.util.ArrayList;

import DAO.PointDAO;
import ElementObject.Point;
import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grantlandchew.view.VerticalPager;

public class JournalActivity extends FragmentActivity {


    JournalPagerAdapter mJournalPagerAdapter;

    ViewPager mViewPager;
    
    int tripId;
    
    public static PointDAO pointDAO;
    
    public static String jourCourant;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal);
        
        pointDAO = new PointDAO(Menu.getContext());
        
        tripId = getIntent().getIntExtra("id", -1);
        
        mJournalPagerAdapter = new JournalPagerAdapter(getSupportFragmentManager(),tripId);
        
        final ActionBar actionBar = getActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        // Set up the ViewPager, attaching the adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mJournalPagerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                Intent upIntent = new Intent(this, MainActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is not part of the application's task, so create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder.from(this)
                            // If there are ancestor activities, they should be added here.
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                    // This activity is part of the application's task, so simply
                    // navigate up to the hierarchical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //structure du pager
    /**
     * Gestion des différentes journées
     */
    public static class JournalPagerAdapter extends FragmentStatePagerAdapter {
    	
    	ArrayList<String> jours;
    	int tripId;
    	public static String VIDEO_NAME = "Vidéos";

        public JournalPagerAdapter(FragmentManager fm, int tripId) {
            super(fm);
            
            //récupérer toutes les journée
            jours = pointDAO.getDistinctDayOfTravel(tripId);
            
            //ajout de la vidéo
            jours.add(JournalPagerAdapter.VIDEO_NAME);
        }
        
        //renvoyer la page numéro i (création) - chaque jour du voyage
        @Override
        public Fragment getItem(int i) {
        	
            Fragment fragment = new DayJournalFragment();
            Bundle args = new Bundle();
            args.putInt(DayJournalFragment.ARG_OBJECT, tripId); // Our object is just an integer :-P
            args.putString(DayJournalFragment.ARG_DATE, jours.get(i)); // Our object is just an integer :-P
            jourCourant = jours.get(i);
            fragment.setArguments(args);
            return fragment;
        }
        
        //nombre de page
        @Override
        public int getCount() {
        	//jours + 1 page vidéo
            return jours.size();
        }

        //titre de la page
        @Override
        public CharSequence getPageTitle(int position) {
            //return "Jour " + (position + 1); // + Mardi 15 Mars 2012
            return jours.get(position);
        }
    }
    

    /**
     * Affichage d'une journée
     */
    public static class DayJournalFragment extends Fragment {

        public static String ARG_OBJECT = "-1";
        public static String ARG_DATE = "";
        

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        	//récupérer les arguments
        	String temp_travel_id = this.getArguments().getString(DayJournalFragment.ARG_OBJECT);
        	String temp_date = this.getArguments().getString(DayJournalFragment.ARG_DATE);
        	
        	//affecte la vue
        	View rootView = inflater.inflate(R.layout.fragment_journal_day, container, false);
        	
        	//recupère le VerticalPager de la journée
        	VerticalPager verticalPager = (VerticalPager)rootView.findViewById(R.id.VerticalPager);
        	        	
        	
        	//si c'est une vidéo
        	if(temp_date.equals(JournalPagerAdapter.VIDEO_NAME)){
        		
        		//layout 
        		LinearLayout list = new LinearLayout(this.getActivity());
        		list.setHorizontalGravity(1);
        		list.setVerticalGravity(1);

        		//ajouter le commentaire au layout
    	        TextView comment = new TextView(this.getActivity());
        		comment.setText("Aucune vidéo n'est présente.");
        		comment.setTextSize(30);
    	        list.addView(comment);
    	        
        		//ajouter l'heure au layout
    	        //ajout du layout au pager
    	        verticalPager.addView(list);
    	        
        	}else{
        		
        		//sinon affichage des photos du jours
        		//récupère les photos
        		ArrayList<Point> photos = JournalActivity.pointDAO.getAllPhotosOfDay(jourCourant);
        		
        		System.out.println("YYYYYYYYYY " + jourCourant);
        		        		
        		//pour chaque photo
        		if(photos != null){
        			// on affiche les photos
        			for (Point photo : photos) {
        				LinearLayout list = new LinearLayout(this.getActivity());
        				ImageView image = new ImageView(this.getActivity());
        				image.setImageURI(Uri.parse(photo.getUri()));
        				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(800, 800);
        				image.setLayoutParams(layoutParams);
            	        list.addView(image);
            	        
            	        TextView comment = new TextView(this.getActivity());
                		comment.setText(photo.getComment());
                		comment.setTextSize(30);
            	        list.addView(comment);
            	        
            	        verticalPager.addView(list);
            	        
            	        
            			System.out.println(photo.getUri());
            		}
        		}else{
        			//layout 
    				LinearLayout list = new LinearLayout(this.getActivity());
    				list.setHorizontalGravity(1);
    				list.setVerticalGravity(1);
    				
    				//ajouter le commentaire au layout
    				TextView comment = new TextView(this.getActivity());
    				comment.setText("Aucune photo n'est présente.");
    				comment.setTextSize(30);
    				list.addView(comment);
    				
    				//ajouter l'heure au layout
    				//ajout du layout au pager
    				verticalPager.addView(list);
        		}
        	}
        		
        	//récupération des places de la journée en base
        	
        	//pour chaque place
//        	foreach (place : places) {
        		//créer un layout
   /*     		LinearLayout list = new LinearLayout(this.getActivity());
        		//ajouter les différents éléments (image, video, texte, heure, carte)
        		switch (type) {
        			case 1:
        				ImageView image = new ImageView(this.getActivity());
        				image.setImageResource(src);
            	        list.addView(image);
        				break;
        			case 2:
        				
        				break;
        			case 3:
        				
        				break;
        			case 4:
        				
        				break;
        			default:
        				
        		}
        		TextView comment = new TextView(this.getActivity());
        		comment.setText("commentaire du point");
        		comment.setTextSize(30);
    	        list.addView(comment);
        		//ajouter le layout au VerticalPager
    	        //verticalPager.addView(list);
//        	}
        	//fin de l'histoire*/
            Bundle args = getArguments();
            return rootView;
        }
    }
}
