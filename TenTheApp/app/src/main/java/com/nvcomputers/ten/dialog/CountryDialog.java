package com.nvcomputers.ten.dialog;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.nvcomputers.ten.R;
import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.model.output.CountryCodeOutput;
import com.nvcomputers.ten.views.auth.PhoneVerificationActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kambojRavi on 4/24/2017.
 */

public class CountryDialog {
    PhoneVerificationActivity mActivity;
    private RecyclerView mCountryRecyclerView;
    private AlertDialog mCountryDialog;

    /**
     * parametrized constructor 1
     *
     * @param activity:context of {@link PhoneVerificationActivity}
     */
    public CountryDialog(PhoneVerificationActivity activity) {
        this.mActivity = activity;
    }


    /**
     * Thsi method is used to show country Dialog
     */
    public void showCountryDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.country_code_listview, null);
        dialogBuilder.setView(dialogView);
        mCountryRecyclerView = (RecyclerView) dialogView.findViewById(R.id.recycler_view_country_code);
        mCountryDialog = dialogBuilder.create();
        mCountryDialog.show();
        responseCheck();
    }

    public void hideCountryDialog() {
        mCountryDialog.dismiss();
    }

    public void responseCheck() {
        mActivity.showDialog();
     //   GetRestAdapter.retrofitInterface=null;
        Call<CountryCodeOutput> response = GetRestAdapter.getRestAdapter(false).getCountryCode();
        response.enqueue(new Callback<CountryCodeOutput>() {
            @Override
            public void onResponse(Call<CountryCodeOutput> call, Response<CountryCodeOutput> respone) {
                mActivity.hideDialog();
                if (respone != null && respone.body() != null && respone.body().getResponse() != null &&
                        respone.body().getResponse().getCode() != null) {

                    if (respone.body().getResponse().getCode().equals("201")) {
                        if (respone.body().getResponse().getResult() != null) {
                            mActivity.setAdapter(mCountryRecyclerView, respone.body().getResponse().getResult().getCountry());
                        } else {
                            mActivity.showToast(mActivity.getString(R.string.no_data_found_msg));
                        }

                    } else {
                        mActivity.showToast(mActivity.getString(R.string.no_data_found_msg));
                    }

                } else {
                    mActivity.showToast(mActivity.getString(R.string.server_error_msg));
                }
            }

            @Override
            public void onFailure(Call<CountryCodeOutput> call, Throwable t) {
                mActivity.hideDialog();
                mActivity.showToast(t.getMessage());
            }
        });
    }


}
