
package com.example.pic_trip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import DAO.PointDAO;
import DAO.TravelDAO;
import ElementObject.Point;
import ElementObject.Travel;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Menu extends FragmentActivity implements ActionBar.TabListener {

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    static Intent intent;
    
    static Intent toReload;
    
    static Context context = null;
    
    private static TravelDAO travelDAO = null;
    
    public static PointDAO pointDAO;
    
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
        pointDAO = new PointDAO(Menu.getContext());
        
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
    		Date debut = (Date) df.parse(dateDebut.getText().toString());
    		Date fin = (Date) df.parse(dateFin.getText().toString());
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
    	
    	DatePicker startDate;
    	DatePicker stopDate;
    	TextView dateDebut;
    	TextView dateFin;
    	
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_newtrip, container, false);           
            //Date picker
            //startDate = (DatePicker) rootView.findViewById(R.id.TripDateDebut);
            //stopDate = (DatePicker) rootView.findViewById(R.id.TripDateFin);
            
            dateDebut = (TextView) rootView.findViewById(R.id.TripDateDebutValue);
            dateFin = (TextView) rootView.findViewById(R.id.TripDateFinValue);
            
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
					int year = c.get(Calendar.YEAR);
					int month = c.get(Calendar.MONTH);
					int day = c.get(Calendar.DAY_OF_MONTH);
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
					int year = c.get(Calendar.YEAR);
					int month = c.get(Calendar.MONTH);
					int day = c.get(Calendar.DAY_OF_MONTH);
					DatePickerDialog dialog = new DatePickerDialog(arg0.getContext(), DebutDateChange, year, month, day);
					dialog.show();
				}
			});
            
            //affecte date dans datepicker
            //startDate.init(year, month, day, null);
            //stopDate.init(year, month, day, null);
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
    			int tripNumber = 1;
	    		for(Travel tr : list) {
	    			map = new HashMap<String, String>();
	    			map.put("numero", String.valueOf(tripNumber));
	    	        map.put("date_debut", "du " + sdf.format(new Date(tr.getDate_start())));
	    	        map.put("date_fin", " au " + sdf.format(new Date(tr.getDate_stop())));
	    	        map.put("title", tr.getName());
	    	        map.put("desc", tr.getDescription());
	    	        listItem.add(map);
	    	        tripNumber++;
	    		}
    		}
    		
            //Création d'un SimpleAdapter qui se chargera de mettre les items présent dans notre list (listItem) dans la vue triplist
            SimpleAdapter mSchedule = new SimpleAdapter(context, listItem, R.layout.trip_list_element, new String[] {"numero","date_debut", "date_fin", "title", "desc"}, new int[] {R.id.TripNumber, R.id.TripListDateDebut, R.id.TripListDateFin, R.id.TripTitle, R.id.TripDesc});

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
    	
    	private GridView gallery;
    	
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_gallery, container, false);
            
            //creation liste voyages
            gallery = (GridView) rootView.findViewById(R.id.GalleryGridView);
            
            gallery.setAdapter(new GalleryAdapter(this.getActivity()));
		            
            gallery.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					// TODO Auto-generated method stub
					/*ArrayList<Travel> list =  travelDAO.getAll();
					int id = list.get(arg2).getId();
					intent.putExtra("id", id);
	            	startActivity(intent);*/
		            gallery.setAdapter(new AlbumAdapter(Menu.getContext(), arg1.getId()));					
				}
			});
            
            return rootView;
        }
    }
    
    public static class GalleryAdapter extends BaseAdapter {    
    	
        private Context mContext;
        private ArrayList<Travel> triplist = new ArrayList<Travel>();
        
        public GalleryAdapter(Context c) {
            mContext = c;
            triplist =  travelDAO.getAll();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return triplist.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
        	Travel tr = triplist.get(position);
            View v;
            if (convertView == null) {
            	//on recupere le layout d'un album
                LayoutInflater li = LayoutInflater.from(mContext);
                v = li.inflate(R.layout.gallery_album, null);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                
                TextView title = (TextView)v.findViewById(R.id.GalleryTitle);
                title.setText(tr.getName());
                TextView date = (TextView)v.findViewById(R.id.GalleryDate);
                String dateValue = sdf.format(new Date(tr.getDate_start())) + "\n" + sdf.format(new Date(tr.getDate_stop()));
                date.setText(dateValue);
                
    		    ArrayList<Point> photos = Menu.pointDAO.getAllPhotosOfTravel(tr.getId());
    		    ArrayList<Uri> photosUri = new ArrayList<Uri>();
    		    for (int i = 0; i < 4; i++) {
    		    	if(photos != null && i < photos.size() && photos.get(i) != null) {
		    			photosUri.add(Uri.parse(photos.get(i).getUri()));    		    		
    		    	}    		    	
	    	    }
                ImageView photo1 = (ImageView)v.findViewById(R.id.GalleryPhoto1);
                if(photosUri.size() > 0) {
                	photo1.setImageURI(photosUri.get(0));                	
                }
                else {
                	photo1.setVisibility(View.INVISIBLE);
                }
                ImageView photo2 = (ImageView)v.findViewById(R.id.GalleryPhoto2);
                if (photosUri.size() > 1) {
                	photo2.setImageURI(photosUri.get(1));               	
                }
                else {
                	photo2.setVisibility(View.INVISIBLE);
                }
                
                ImageView photo3 = (ImageView)v.findViewById(R.id.GalleryPhoto3);
                if (photosUri.size() > 2) {
                	photo3.setImageURI(photosUri.get(2));                	
                }
                else {
                	photo3.setVisibility(View.INVISIBLE);
                }
                ImageView photo4 = (ImageView)v.findViewById(R.id.GalleryPhoto4);
                if (photosUri.size() > 3) {
                	photo4.setImageURI(photosUri.get(3));                	
                }
                else {
                	photo4.setVisibility(View.INVISIBLE);
                }

            }
            else
            {
                v = convertView;
            }
            v.setId(tr.getId());
            return v;
        }

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
    }

    public static class AlbumAdapter extends BaseAdapter {    
    	
        private Context mContext;
        private int mtripId;
        private ArrayList<Point> triplistpoints =  new ArrayList<Point>();
        
        public AlbumAdapter(Context c, int tripId) {
            mContext = c;
            mtripId = tripId;
            System.out.println("id travel :" + tripId);
            triplistpoints =  pointDAO.getAllPhotosOfTravel(mtripId);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return triplistpoints.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
        	View v;
            if (convertView == null) {
            	//on recupere le layout d'un album
                LayoutInflater li = LayoutInflater.from(mContext);
                v = li.inflate(R.layout.gallery_album_content, null);
                
    		    ArrayList<Point> photos = Menu.pointDAO.getAllPhotosOfTravel(mtripId);
    		    ImageView photo = (ImageView)v.findViewById(R.id.GalleryPhoto);
    		    photo.setImageURI(Uri.parse(photos.get(position).getUri()));
    		}
            else
            {
                v = convertView;
            }
            return v;
        }

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
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
