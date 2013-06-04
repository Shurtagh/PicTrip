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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import DAO.PointDAO;
import ElementObject.Point;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grantlandchew.view.VerticalPager;

public class JournalActivity extends FragmentActivity {


    JournalPagerAdapter mJournalPagerAdapter;

    ViewPager mViewPager;
    
    public static int tripId;
    
    public static PointDAO pointDAO;
    
    public static Point videoPoint;
    
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
        	//String temp_travel_id = this.getArguments().getString(DayJournalFragment.ARG_OBJECT);
        	String temp_date = this.getArguments().getString(DayJournalFragment.ARG_DATE);
        	
        	//affecte la vue
        	View rootView = inflater.inflate(R.layout.fragment_journal_day, container, false);
        	
        	//recupère le VerticalPager de la journée
        	VerticalPager verticalPager = (VerticalPager)rootView.findViewById(R.id.VerticalPager);
        	        	
        	
        	//si c'est une vidéo
        	if(temp_date.equals(JournalPagerAdapter.VIDEO_NAME)){
        		
        		ArrayList<Point> videos = JournalActivity.pointDAO.getAllVideosOfTravel(tripId);
        		
        		if(videos != null){
        			// on affiche les photos
        			for (Point video : videos) {
        				
        				videoPoint = video;
        				
        				RelativeLayout list = new RelativeLayout(this.getActivity());
        	            
        				/*VideoView videoHolder = new VideoView(this.getActivity());
        	            videoHolder.setMediaController(new MediaController(this.getActivity()));
        	            videoHolder.setVideoURI(Uri.parse(video.getUri()));
        	            videoHolder.requestFocus();
        	            list.addView(videoHolder);*/
        				
        				ImageView image = new ImageView(this.getActivity());
        				
        				MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    	    			retriever.setDataSource(video.getUri());
    	    			Bitmap bitmap=retriever.getFrameAtTime(1000);
    	    			image.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 200, 320, false));
        				
        				image.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
									Intent intent = new Intent();
			 	        			intent.setAction(Intent.ACTION_VIEW);
			        				intent.setDataAndType(Uri.parse("file://" + videoPoint.getUri()), "video/*");
			        				startActivity(intent);
							}
						});
        				
        				list.addView(image);
        				
        	            TextView hour = new TextView(this.getActivity());
    					hour.setText(video.getComment());
    					hour.setTextSize(20);
    					list.addView(hour);
        	            
        	            verticalPager.addView(list);
        				
        			}
        		} else {
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
        		}
        	}else{
        		
        		//sinon affichage des photos du jours
        		//récupère les photos
        		ArrayList<Point> photos = JournalActivity.pointDAO.getAllPhotosOfDay(temp_date);
        		
        		//pour chaque photo
        		if(photos != null){
        			// on affiche les photos
        			for (Point photo : photos) {
        				RelativeLayout list = new RelativeLayout(this.getActivity());
        				ImageView image = new ImageView(this.getActivity());
        				if(photo.getUri() != null) {
        					image.setImageURI(Uri.parse(photo.getUri()));
        					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(800, 800);
        					image.setLayoutParams(layoutParams);
        					image.setId(1);
        					list.addView(image);
        					
        					TextView comment = new TextView(this.getActivity());
        					comment.setText(photo.getComment());
        					comment.setTextSize(20);
        					
        					RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
        					        RelativeLayout.LayoutParams.WRAP_CONTENT);
        					relativeLayoutParams.addRule(RelativeLayout.BELOW,
        					        image.getId());
        					relativeLayoutParams.addRule(RelativeLayout.ALIGN_LEFT,
        					        image.getId());
        					
        					list.addView(comment, relativeLayoutParams);
        					
        					TextView hour = new TextView(this.getActivity());
        					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.FRANCE);
        					String value = sdf.format(new Date(photo.getDate_add()));
        					
        					hour.setText(value);
        					hour.setTextSize(20);
        					list.addView(hour);
        					
        					verticalPager.addView(list);
        				}
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
        		
            Bundle args = getArguments();
            return rootView;
        }
    }
}
