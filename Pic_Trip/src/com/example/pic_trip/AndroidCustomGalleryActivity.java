package com.example.pic_trip;
 
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
 
public class AndroidCustomGalleryActivity extends Activity {
    private int count;
    private Bitmap[] thumbnails;
    private boolean[] thumbnailsselection;
    private String[] arrPath;
    private long[] dates_taken;
    private ImageAdapter imageAdapter;
    float[][] tab = new float[200][2];
    MultipleImages list = new MultipleImages();
    ArrayList<ObjetImage> imagesToSend = list.getImages();
    ArrayList<ObjetImage> imagesReceive;
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        imagesReceive = (ArrayList<ObjetImage>)getIntent().getSerializableExtra("pathes");
        
        setContentView(R.layout.gallery);
 
        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media.LONGITUDE, MediaStore.Images.Media.DATE_TAKEN };
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        Cursor imagecursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);
        this.count = imagecursor.getCount();
        this.thumbnails = new Bitmap[this.count];
        this.arrPath = new String[this.count];
        this.dates_taken = new long[this.count];
        this.thumbnailsselection = new boolean[this.count];
        for (int i = 0; i < this.count; i++) {
            imagecursor.moveToPosition(i);
            int id = imagecursor.getInt(image_column_index);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            thumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(
                    getApplicationContext().getContentResolver(), id,
                    MediaStore.Images.Thumbnails.MICRO_KIND, null);
            arrPath[i]= imagecursor.getString(dataColumnIndex);
            
            int latitudeIndex = imagecursor.getColumnIndex(columns[2]);
            float latitudeToDisplay = imagecursor.getFloat(latitudeIndex);
            tab[i][0] = latitudeToDisplay;
            
            int longitudeIndex = imagecursor.getColumnIndex(columns[3]);
            float longitudeToDisplay = imagecursor.getFloat(longitudeIndex);
            tab[i][1] = longitudeToDisplay;
            
            int date_index = imagecursor.getColumnIndex(columns[4]);
            long date = imagecursor.getLong(date_index);
            dates_taken[i] = date;
        }
        GridView imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
        imageAdapter = new ImageAdapter();
        imagegrid.setAdapter(imageAdapter);
        imagecursor.close();
 
        final Button selectBtn = (Button) findViewById(R.id.selectBtn);
        selectBtn.setOnClickListener(new OnClickListener() {
 
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	final int len = thumbnailsselection.length;
            	int nb = 0;
            	for (int i=0; i<len; i++) {
                    if (thumbnailsselection[i]) {
                    	nb++;
                    }
                }
            	for (int i=0; i<len; i++)
                {
                    if (thumbnailsselection[i]){
                    	String snippetToSend = null;
                    	for(int k=0;k<imagesReceive.size();k++) {
                        	if(imagesReceive.get(k).getImagePath() != null) {
                        		if(imagesReceive.get(k).getImagePath().equals(arrPath[i])) {
                        			snippetToSend = imagesReceive.get(k).getSnippet();
                    			}
                        	}
                        }
                    	System.out.println(tab[i][0] + " " + tab[i][1] + " " + arrPath[i] + " " + snippetToSend + " " + dates_taken[i]);
                    	imagesToSend.add(new ObjetImage(tab[i][0], tab[i][1], arrPath[i], snippetToSend, dates_taken[i]));
                    }
                }
            	Intent intent = new Intent(getBaseContext(), MainActivity.class);
            	
            	intent.putExtra("result", imagesToSend);
            	intent.putExtra("interactif", false);
                setResult(RESULT_OK, intent);
                finish();
                
            }
        });
        
        final Button interactifBtn = (Button) findViewById(R.id.interactif);
        interactifBtn.setOnClickListener(new OnClickListener() {
 
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	final int len = thumbnailsselection.length;
            	int nb = 0;
            	for (int i=0; i<len; i++) {
                    if (thumbnailsselection[i]) {
                    	nb++;
                    }
                }
                for (int i=0; i<len; i++)
                {
                    if (thumbnailsselection[i]){
                    	String snippetToSend = null;
                    	for(int k=0;k<imagesReceive.size();k++) {
                        	if(imagesReceive.get(k).getImagePath() != null) {
                        		if(imagesReceive.get(k).getImagePath().equals(arrPath[i])) {
                        			snippetToSend = imagesReceive.get(k).getSnippet();
                    			}
                        	}
                        }
                    	imagesToSend.add(new ObjetImage(tab[i][0], tab[i][1], arrPath[i], snippetToSend, dates_taken[i]));
                    }
                }
            	Intent intent = new Intent(getBaseContext(), MainActivity.class);
            	
            	intent.putExtra("result", imagesToSend);
            	intent.putExtra("interactif", true);
                setResult(RESULT_OK, intent);
                finish();
                
            }
        });
    }
 
    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
 
        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
 
        public int getCount() {
            return count;
        }
 
        public Object getItem(int position) {
            return position;
        }
 
        public long getItemId(int position) {
            return position;
        }
 
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.galleryitem, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);
 
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.checkbox.setId(position);
            holder.imageview.setId(position);
            holder.checkbox.setOnClickListener(new OnClickListener() {
 
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    if (thumbnailsselection[id]){
                        cb.setChecked(false);
                        thumbnailsselection[id] = false;
                        for(int i=0;i<imagesReceive.size();i++) {
                        	if(imagesReceive.get(i).getImagePath() != null) {
                        		if(imagesReceive.get(i).getImagePath().equals(arrPath[id])) {
                    				imagesReceive.remove(i);
                    			}
                        	}
                        }
                    } else {
                        cb.setChecked(true);
                        thumbnailsselection[id] = true;
                    }
                }
            });
            holder.imageview.setOnClickListener(new OnClickListener() {
 
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int id = v.getId();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("file://" + arrPath[id]), "image/*");
                    startActivity(intent);
                }
            });
            holder.imageview.setImageBitmap(thumbnails[position]);
            holder.checkbox.setChecked(thumbnailsselection[position]);
            
            for(int i=0;i<imagesReceive.size();i++) {
            	if(imagesReceive.get(i).getImagePath() != null) {
            		if(imagesReceive.get(i).getImagePath().equals(arrPath[position])) {
            			holder.checkbox.setChecked(true);
        				thumbnailsselection[position] = true;
        			}
            	}
            }
            holder.id = position;
            return convertView;
        }
    }
    class ViewHolder {
        ImageView imageview;
        CheckBox checkbox;
        int id;
    }
}