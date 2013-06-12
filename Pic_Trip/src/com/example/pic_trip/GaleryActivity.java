package com.example.pic_trip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import DAO.PointDAO;
import ElementObject.Point;
import ElementObject.Travel;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class GaleryActivity extends Activity {
	
	private GridView gallery;
	private int mtripId;
    private static PointDAO pointDAO;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_section_gallery);

		mtripId = getIntent().getIntExtra("id", -1);
        
        gallery = (GridView) findViewById(R.id.GalleryGridView);        
        gallery.setAdapter(new AlbumAdapter(this, mtripId));	            
        gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				/*Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + ((ImageView) arg1).getPath()), "image/*");
                startActivity(intent);*/
			}
		});
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
}