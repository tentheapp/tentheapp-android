package com.nvcomputers.ten.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.nvcomputers.ten.R;
import com.nvcomputers.ten.model.output.PopularTimersResponse;
import com.nvcomputers.ten.utils.HttpUtils;
import com.nvcomputers.ten.utils.PreferenceKeys;
import com.nvcomputers.ten.utils.SharedPrefsHelper;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Thaparsneh on 4/27/2017.
 */

public class PopularTimerAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private SharedPrefsHelper sharedPref;
    Context context;
    PopularTimersResponse.Users[] mList;
    LayoutInflater inflter;
    private String idUser;

    public PopularTimerAdapter(Context applicationContext, PopularTimersResponse.Users[] mList) {
        this.context = applicationContext;
        this.mList = mList;
        inflter = (LayoutInflater.from(applicationContext));
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sharedPref = new SharedPrefsHelper(context);
    }


    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        idUser = mList[position].getIdUser();
        ViewHolder viewHolder = null;


        if (view == null) {
            view = inflter.inflate(R.layout.row_popular_timers, null);
            // viewHolder.linear_layout=(LinearLayout) view.findViewById(R.id.linear_layout);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

      /*  view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


             *//*Intent gotoUserProfile=new Intent(context, ProfileActivity.class);
                gotoUserProfile.putExtra("userImagView",mList[position].getImageSrc());
                gotoUserProfile.putExtra("username",mList[position].getUsername());
                gotoUserProfile.putExtra("userId",mList[position].getIdUser());
                context.startActivity(gotoUserProfile);*//*
                // TODO Auto-generated method stub
               // Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
            }
        });*/

        String imageTag = sharedPref.get(PreferenceKeys.PREF_IMAGE_TAG, "");
        Glide.with(context)
                .load(HttpUtils.getProfileImageURL(mList[position].getUsername()))
                .bitmapTransform(new RoundedCornersTransformation(context, 20, 15))
                .placeholder(R.drawable.myprofilelarge)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .signature(new StringSignature(imageTag))
                .into(viewHolder.timer_img);


        viewHolder.userNameTV.setText(mList[position].getUsername());

        return view;
    }

    @Override
    public int getCount() {
        return mList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    class ViewHolder {

        // private  LinearLayout linear_layout;
        private TextView userNameTV;
        public ImageView timer_img;

        public ViewHolder(View view) {
            userNameTV = (TextView) view.findViewById(R.id.timer_name);
            timer_img = (ImageView) view.findViewById(R.id.timers_img);
            // linear_layout=(LinearLayout)view.findViewById(R.id.linear_layout);
   /*    Intent gotoUserProfile=new Intent(context, ProfileActivity.class);
            gotoUserProfile.putExtra("userId",idUser);
            context.startActivity(gotoUserProfile);*/

        }
    }
}