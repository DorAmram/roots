package com.example.user.biographics_tests;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class VideoAdapter extends BaseAdapter {
    private Context mContext;
    private Story story;

    public VideoAdapter(Context c, Story s) {
        mContext = c;
        story = s;
    }

    public int getCount() {
        return story.videosSize();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) { // if it's not recycled, initialize some
            // attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(250, 250));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }
        Uri uri = Uri.parse("file:"+story.getVideoThumbAt(position));
        try {
            Picasso.with(mContext)
                    .load(uri)//.resize()
                    .into(imageView);
            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            //Bitmap output = Bitmap.createScaledBitmap(bitmap, 85, 85, false);
            //imageView.setImageBitmap(bitmap);
            return imageView;
        }catch(Exception e){}//TODO informative massage
        return null;
    }


}

