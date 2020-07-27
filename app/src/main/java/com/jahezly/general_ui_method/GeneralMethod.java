package com.jahezly.general_ui_method;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import com.jahezly.R;
import com.jahezly.tags.Tags;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class GeneralMethod {

    @BindingAdapter("error")
    public static void errorValidation(View view, String error) {
        if (view instanceof EditText) {
            EditText ed = (EditText) view;
            ed.setError(error);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setError(error);


        }
    }






    @BindingAdapter("image")
    public static void image(View view, String endPoint) {
        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).placeholder(R.drawable.logo).into(imageView);
            } else {
                Picasso.get().load(R.drawable.logo).into(imageView);

            }
        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;

            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).placeholder(R.drawable.logo).fit().into(imageView);
            } else {
                Picasso.get().load(R.drawable.logo).into(imageView);

            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).placeholder(R.drawable.logo).fit().into(imageView);
            } else {
                Picasso.get().load(R.drawable.logo).into(imageView);

            }
        }

    }


    @BindingAdapter("foodImage")
    public static void foodImage(View view, String endPoint) {

        Log.e("image",Tags.IMAGE_URL + endPoint);
        if (view instanceof CircleImageView) {


            CircleImageView imageView = (CircleImageView) view;
            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).into(imageView);
            } else {
                Picasso.get().load(R.drawable.logo).into(imageView);

            }
        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;

            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).fit().into(imageView);
            } else {
                Picasso.get().load(R.drawable.logo).into(imageView);

            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).fit().into(imageView);
            } else {
                Picasso.get().load(R.drawable.logo).into(imageView);

            }
        }

    }



    @BindingAdapter({"order_date","order_time"})
    public static void date_time(TextView textView,long date,long time)
    {
        Paper.init(textView.getContext());
        String lang = Paper.book().read("lang","ar");
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd-MM-yyyy", new Locale(lang));
        String d = dateFormat.format(new Date(date*1000));

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm aa", new Locale(lang));
        String t = dateFormat2.format(new Date(time*1000));


        textView.setText(d+" "+t);
    }


    @BindingAdapter({"date"})
    public static void date(TextView textView,long date)
    {
        Paper.init(textView.getContext());
        String lang = Paper.book().read("lang","ar");
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd-MM-yyyy", new Locale(lang));
        String d = dateFormat.format(new Date(date*1000));

        textView.setText(d);
    }

    @BindingAdapter({"time"})
    public static void time(TextView textView,long time)
    {
        Paper.init(textView.getContext());
        String lang = Paper.book().read("lang","ar");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm aa", new Locale(lang));
        String t = dateFormat2.format(new Date(time*1000));
        textView.setText(t);
    }


}










