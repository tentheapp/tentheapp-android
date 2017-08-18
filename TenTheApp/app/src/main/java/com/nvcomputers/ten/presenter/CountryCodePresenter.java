package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.dialog.CountryDialog;
import com.nvcomputers.ten.model.output.CountryCodeOutput;
import com.nvcomputers.ten.views.auth.PhoneVerificationActivity;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kthakur on 4/21/2017.
 */

public class CountryCodePresenter {

    private final CountryDialog dialog;
    PhoneVerificationActivity activity;

    public CountryCodePresenter(CountryDialog dialog,PhoneVerificationActivity activity) {
        this.dialog=dialog;
        this.activity = activity;
    }

    public interface CountryCodeInterface {

        public void onSuccess(Response<CountryCodeOutput> respone);

        public void onError(Call<CountryCodeOutput> call, Throwable t);

    }

    public void responseCheck()  {
        activity.showDialog();

        Call<CountryCodeOutput> response = GetRestAdapter.getRestAdapter(true).getCountryCode();
        response.enqueue(new Callback<CountryCodeOutput>() {
            @Override
            public void onResponse(Call<CountryCodeOutput> call, Response<CountryCodeOutput> response) {
                activity.hideDialog();

            }

            @Override
            public void onFailure(Call<CountryCodeOutput> call, Throwable t) {
                activity.hideDialog();

            }
        });
    }

}
