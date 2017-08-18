package com.nvcomputers.ten.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nvcomputers.ten.R;
import com.nvcomputers.ten.dialog.CountryDialog;
import com.nvcomputers.ten.model.output.CountryCodeOutput;
import com.nvcomputers.ten.views.auth.PhoneVerificationActivity;

import java.util.ArrayList;

/**
 * Created by kambojRavi on 4/21/2017.
 */

public class CountryCodeAdapter extends RecyclerView.Adapter<CountryCodeAdapter.ViewHolder> {

    Context context;
    ArrayList<CountryCodeOutput.Country> mList;

    private final LayoutInflater mInflator;

    private Typeface regular_fonts;
    private PhoneVerificationActivity mActivity;
    private CountryDialog countryDialog;

    public CountryCodeAdapter(PhoneVerificationActivity context, ArrayList<?> mList, CountryDialog countryDialog) {
        this.mList = (ArrayList<CountryCodeOutput.Country>) mList;
        this.mActivity = context;
        mInflator = LayoutInflater.from(mActivity);
        this.countryDialog = countryDialog;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.country_code_custom_inner_layout, null);
        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        String countryName = mList.get(position).getCountryName();
        String countryId = mList.get(position).getPhoneCode();

        if (TextUtils.isEmpty(countryId)) {
            viewHolder.mCountryCodeTextView.setText("N/A");
        } else {
            viewHolder.mCountryCodeTextView.setText("+"+countryId);
        }


        if (TextUtils.isEmpty(countryName)) {
            viewHolder.mCountryNameTextView.setText("N/A");
        } else {
            viewHolder.mCountryNameTextView.setText(countryName);
        }

    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final RelativeLayout mRowLayout;
        private TextView mCountryNameTextView, mCountryCodeTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            mRowLayout = (RelativeLayout) itemView.findViewById(R.id.phone_code_row);
            mCountryCodeTextView = (TextView) itemView.findViewById(R.id.country_code);
            mCountryNameTextView = (TextView) itemView.findViewById(R.id.country_name);
            mRowLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getLayoutPosition();
            String phoneCode = "+"+mList.get(clickedPosition).getPhoneCode();
            countryDialog.hideCountryDialog();
            String countryId = mList.get(clickedPosition).getCountryId();
            mActivity.setCodeText(phoneCode,countryId);

        }
    }


}
