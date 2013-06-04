
package com.example.pic_trip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import DAO.TravelDAO;
import ElementObject.Travel;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class Menu extends FragmentActivity implements ActionBar.TabListener {

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    static Intent intent;
    
    static Intent toReload;
    
    static Context context = null;
    
    private static TravelDAO travelDAO = null;
    
    private static int IDGlobal;

    public static Context getContext() {
		  if (Menu.context != null) {
			  return Menu.context;
		  }
		  return null;
	  }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        intent = new Intent(Menu.this, GridMenuActivity.class);
        
        toReload = getIntent();
        
        Menu.context = getApplicationContext();
        travelDAO = new TravelDAO(Menu.getContext());
        
        //travelDAO.deleteAll();
        
        setContentView(R.layout.menu);
        
        context = getBaseContext();

        // Create the adapter that will return a fragment for each of the three primary sections of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical parent.
        //actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {        	
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the Tab.
            	// calcul pour l'effet carrousel (2 page dummy)
            	if (position < 1) {
            		position = 4;
            	}
            	else if (position > 4) {
            		position = 1;
            	}
            	position--;
            	actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar. (moins les 2 dummy pages pour l effet carrousel)
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount() - 2; i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the listener for when this tab is selected.
            actionBar.addTab(actionBar.newTab().setText(mAppSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
        }
        
        //on le place sur la liste des voyages (par defaut)
        mViewPager.setCurrentItem(2);
 
        //on cache la recherche avancée de la gallery
 /*       final Spinner tripSearch = (Spinner) findViewById(R.id.GalleryTripSpinner);
    	final Spinner citySearch = (Spinner) findViewById(R.id.GalleryCitySpinner);
    	final EditText startDateSearch = (EditText) findViewById(R.id.GalleryStartDateEditText);
    	final EditText endDateSearch = (EditText) findViewById(R.id.GalleryEndDateEditText);
    	
		tripSearch.setVisibility(View.GONE);
		citySearch.setVisibility(View.GONE);
		startDateSearch.setVisibility(View.GONE);
		endDateSearch.setVisibility(View.GONE); */
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    	
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition()+1); // +1 pour la page dummy en 0 (effet carrousel)
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    	
    }
    
    public void createTrip(View button) throws ParseException {  
    	final EditText titleField = (EditText) findViewById(R.id.EditTextTripTitle);
    	String title = titleField.getText().toString();
    	final EditText startDateField = (EditText) findViewById(R.id.EditTripStartDate);
    	String startDate = startDateField.getText().toString();
    	final EditText endDateField = (EditText) findViewById(R.id.EditTripEndDate);
    	String endDate = endDateField.getText().toString();
    	final EditText descField = (EditText) findViewById(R.id.EditTextTripDesc);
    	String desc = descField.getText().toString();
    	
    	boolean valid = true;
    	
    	if (title.matches("")) {
    		titleField.setBackgroundResource(R.color.LightPink);
    		valid = false;
    	} else {
    		titleField.setBackgroundResource(R.color.White);
    	}
    	if (valid) {
    		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    		Date debut = (Date) df.parse(startDate);
    		Date fin = (Date) df.parse(endDate);
    		Travel t = new Travel(title, debut.getTime(), fin.getTime(), desc);
    		t.save();
    		AlertDialog.Builder adb = new AlertDialog.Builder(this);
    		adb.setTitle("New Trip");
    		adb.setMessage("Vous venez de créer : " + title);
    		adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	ArrayList<Travel> list =  travelDAO.getAll();
					int id = list.get(list.size() - 1).getId();
	            	Intent intent = new Intent(Menu.this, MainActivity.class);
	            	intent.putExtra("id", id);
	            	intent.putExtra("type", 1);
	            	startActivity(intent);
	            }
    		});
    		adb.show();
    	}	
    }
    
    public void toggleGalleryResearch(View button) {  
    	final Spinner tripSearch = (Spinner) findViewById(R.id.GalleryTripSpinner);
    	final Spinner citySearch = (Spinner) findViewById(R.id.GalleryCitySpinner);
    	final EditText startDateSearch = (EditText) findViewById(R.id.GalleryStartDateEditText);
    	final EditText endDateSearch = (EditText) findViewById(R.id.GalleryEndDateEditText);
    	final Button buttonSearch = (Button) findViewById(R.id.GallerySearchButton);
    	
    	if (buttonSearch.getText().toString().matches(getResources().getString(R.string.gallery_button_show))) {
    		tripSearch.setVisibility(View.VISIBLE);
    		citySearch.setVisibility(View.VISIBLE);
    		startDateSearch.setVisibility(View.VISIBLE);
    		endDateSearch.setVisibility(View.VISIBLE);   
    		buttonSearch.setText(R.string.gallery_button_hide);
    	}
    	else {
    		tripSearch.setVisibility(View.GONE);
    		citySearch.setVisibility(View.GONE);
    		startDateSearch.setVisibility(View.GONE);
    		endDateSearch.setVisibility(View.GONE);
    		buttonSearch.setText(R.string.gallery_button_show);
    	}
    }
    
    public void saveOptions(View button) {
    	final CheckBox gps = (CheckBox) findViewById(R.id.OptionsCheckBox);
    	
    	EditText tps = (EditText) findViewById(R.id.tpsTracking);
    	
    	int tpsToSend = Integer.parseInt(tps.getText().toString());
    	
    	boolean gpsActivate = gps.isChecked();
    	
    	Intent gps_ = new Intent(Menu.this, Gps.class);
    	
    	String tmp = "activé";

    	if (gpsActivate) {
    		gps_.putExtra("enable", true);
    		gps_.putExtra("temps", tpsToSend);
        	startActivity(gps_);
    	} else {
    		gps_.putExtra("enable", false);
        	startActivity(gps_);
        	tmp = "desactivé";
    	}
		new AlertDialog.Builder(this)
				.setTitle("Options")
				.setMessage("gps : " + tmp)
				.setPositiveButton("Ok", null)
				.show();
    }

	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) { //initialise le contenu de chaque page
            switch (i) {
        		case 0:
        			return new DummySectionFragment();
        		
        		case 1:
        			return new NewTripSectionFragment();
            
            	case 2:
            		return new TripListSectionFragment();

                case 3:
                    return new GallerySectionFragment();
                    

                case 4:
                    return new OptionsSectionFragment();

                default:
                	return new DummySectionFragment();
            }
        }

        @Override
        public int getCount() {
            return 6; //nombre d'onglets/pages (4 + 2 dummy pour l effet carrousel)
        }

        @Override
        public CharSequence getPageTitle(int position) {        	
        	String name;
        	
        	switch (position) {
	            case 0:
	                name = "Nouveau";
	                break;
		            
	            case 1:
	            	name = "Liste";
	                break;
		            
	            case 2:
	            	name = "Albums";
	                break;
		            
	            case 3:
	            	name = "Options";
	                break;                
	
	            default:
	            	name = "Onglet";
	        }
        	
            return name;
        }
    }    
    
    public static class NewTripSectionFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_newtrip, container, false);           

            return rootView;
        }        
    }
    
    public static class TripListSectionFragment extends Fragment {
    	
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_triplist, container, false);
            
            
          //creation liste voyages
            ListView tripList = (ListView) rootView.findViewById(R.id.TripListView);
            ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
     
            //On déclare la HashMap qui contiendra les informations pour un item
            HashMap<String, String> map;
            
    		ArrayList<Travel> list =  travelDAO.getAll();
    		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    		if(list != null) {
	    		for(Travel tr : list) {
	    			map = new HashMap<String, String>();
	    	        map.put("date_debut", sdf.format(new Date(tr.getDate_start())));
	    	        map.put("date_fin", sdf.format(new Date(tr.getDate_stop())));
	    	        map.put("title", tr.getName());
	    	        map.put("desc", tr.getDescription());
	    	        listItem.add(map);
	    		}
    		}
    		
            //Création d'un SimpleAdapter qui se chargera de mettre les items présent dans notre list (listItem) dans la vue triplist
            SimpleAdapter mSchedule = new SimpleAdapter(context, listItem, R.layout.trip_list_element, new String[] {"date_debut", "date_fin", "title", "desc"}, new int[] {R.id.TripDateDebut, R.id.TripDateFin, R.id.TripTitle, R.id.TripDesc});

            //On attribut à notre listView l'adapter que l'on vient de créer
            tripList.setAdapter(mSchedule);
            
            tripList.setLongClickable(true);
            
            tripList.setOnItemLongClickListener(new OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                        final int arg2, long arg3) {
                	
                	IDGlobal = arg2;
                	
                	createDialogForSupprTrip().show();
                	
                	return true;
		        }
		    });
		            
            tripList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					ArrayList<Travel> list =  travelDAO.getAll();
					int id = list.get(arg2).getId();
					intent.putExtra("id", id);
	            	startActivity(intent);
				}
			});
            
            return rootView;
        }
        
        private AlertDialog.Builder createDialogForSupprTrip() {
    		//On instancie notre layout en tant que View
    	        LayoutInflater factory = LayoutInflater.from(getActivity());
    	        final View alertDialogView = factory.inflate(R.layout.suppr_trip, null);
    	 
    	        //Création de l'AlertDialog
    	        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
    	 
    	        //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
    	        adb.setView(alertDialogView);
    	 
    	        //On donne un titre à l'AlertDialog
    	        adb.setTitle("Information");
    	 
    	        //On modifie l'icône de l'AlertDialog pour le fun ;)
    	        adb.setIcon(android.R.drawable.ic_dialog_alert);

    	        //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
    	        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int which) {
                    	ArrayList<Travel> list =  travelDAO.getAll();
                    	int id = list.get(IDGlobal).getId();
                    	travelDAO.deleteTravel(id);
                    	
                        getActivity().finish();
                        startActivity(toReload);
    	          } });
    	        return adb;
    	    }
    }
    
    public static class GallerySectionFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_gallery, container, false);

            return rootView;
        }
    }
    
    public static class OptionsSectionFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_options, container, false);

            return rootView;
        }
    }

    public static class DummySectionFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);
            
            return rootView;
        }
    }
}
