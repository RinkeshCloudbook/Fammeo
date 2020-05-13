package com.fammeo.app.util;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;

import com.fammeo.app.R;
import com.fammeo.app.app.App;
import com.fammeo.app.constants.Constants;
import com.fammeo.app.model.Item;

public class Api extends Application implements Constants {

    Context context;

    public Api(Context context) {

        this.context = context;
    }








    public void postShare(Item item) {

        String shareText = "";

        if (item.getPost().length() > 0) {

            shareText = item.getPost();

        } else {

            if (item.getImgUrl() == null || item.getImgUrl().length() == 0) {

                shareText = item.getLink();
            }
        }


        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);

        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, (String) context.getString(R.string.app_name));
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);

        if (item.getImgUrl().length() > 0) {

            ImageView image;
            ImageLoader imageLoader = App.getInstance().getImageLoader();

            image = new ImageView(context);

            if (imageLoader == null) {

                imageLoader = App.getInstance().getImageLoader();
            }

            imageLoader.get(item.getImgUrl(), ImageLoader.getImageListener(image, R.drawable.profile_default_photo, R.drawable.profile_default_photo));

            Drawable mDrawable = image.getDrawable();
            Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();

            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), mBitmap, "Image Description", null);

            Uri uri = Uri.parse(path);

            shareIntent.putExtra(android.content.Intent.EXTRA_STREAM, uri);
        }

        shareIntent.setType("text/plain");

        context.startActivity(Intent.createChooser(shareIntent, "Share post"));
    }

}
