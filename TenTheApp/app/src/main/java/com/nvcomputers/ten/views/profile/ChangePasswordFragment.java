package com.nvcomputers.ten.views.profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nvcomputers.ten.R;
import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.input.ChangePasswordInput;
import com.nvcomputers.ten.model.output.ChangePasswordResponse;
import com.nvcomputers.ten.presenter.ChangePasswordPresenter;
import com.nvcomputers.ten.utils.PreferenceKeys;
import com.nvcomputers.ten.utils.SharedPrefsHelper;
import com.nvcomputers.ten.utils.Utilities;
import com.nvcomputers.ten.views.TTApplication;
import com.nvcomputers.ten.views.core.BaseFragment;

import org.acra.collector.CrashReportData;

import retrofit2.Call;
import retrofit2.Response;

public class ChangePasswordFragment extends BaseFragment implements View.OnClickListener, ChangePasswordPresenter.ChangePasswordCallback, AppCommonCallback {
    private AppCompatActivity mActivity;
    private EditText oldPasswordText;
    private EditText newPasswordText;
    private EditText confirmPasswordText;
    private TextView passwordSaveBtn;
    private SharedPrefsHelper sharedPrefsHelper;
    private ProgressBar progressBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void initViews(View view) {
        mActivity = getBaseActivity();
        sharedPrefsHelper = new SharedPrefsHelper(getBaseActivity());
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        oldPasswordText = (EditText) findViewById(R.id.oldPasswordEt);
        newPasswordText = (EditText) findViewById(R.id.newPasswordEt);
        confirmPasswordText = (EditText) findViewById(R.id.confirmPasswordEt);
        passwordSaveBtn = (TextView) findViewById(R.id.password_save_btn);
        TextView backBtn = (TextView) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        view.findViewById(R.id.rl_change).setOnClickListener(this);
        passwordSaveBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_change:
                Utilities.hideKeypad(view);
                break;
            case R.id.backBtn:
                Utilities.hideKeypad(view);
                manualBackPressed();
                break;
            case R.id.password_save_btn:
                Utilities.hideKeypad(view);
                String password = sharedPrefsHelper.get(PreferenceKeys.PREF_PASSWORD, "");
                String oldPass = oldPasswordText.getText().toString().trim();
                String newPass = newPasswordText.getText().toString().trim();
                String confirmPass = confirmPasswordText.getText().toString().trim();
                if (oldPass.equals(null) || oldPass.equals("")) {
                    showToast("Please enter current password.");
                } else if (newPass.equals(null) || newPass.equals("")) {
                    showToast( "Please enter new password.");
                } else if (newPass.length() <= 5) {
                    showToast( "Password must be more than 5 characters");
                } else if (confirmPass.equals(null) || confirmPass.equals("")) {
                    showToast( "Please confirm your password.");
                } else if (!oldPass.equals(password)) {
                    showToast( "Incorrect password");
                } else if (!newPass.equals(confirmPass)) {
                    showToast("Passwords do not match");
                } else {
                    if (Utilities.checkInternet(mActivity)) {
                        showDialog();
                        ChangePasswordInput changePasswordInput = new ChangePasswordInput();
                        changePasswordInput.setPassword(newPass);
                        ChangePasswordPresenter changePasswordPresenter = new ChangePasswordPresenter(this, this);
                        changePasswordPresenter.responseCheck(changePasswordInput);

                    } else {
                        showToast(getString(R.string.no_internet_msg));
                    }
                }
                break;
        }


    }

    @Override
    public void dispose() {

    }

    @Override
    public void changePasswordError(Call<ChangePasswordResponse> call, Throwable t) {
        hideDialog();
        showToast( getString(R.string.server_error_msg));
    }

    @Override
    public void onchangePasswordSuccess(Response<ChangePasswordResponse> response) {
        hideDialog();
        ChangePasswordResponse body = response.body();
        if (body != null) {
            String status = body.getSuccess();
            if (status != null && status.equals("true")) {
                SharedPrefsHelper  sharedPrefsHelper=new SharedPrefsHelper(getBaseActivity());
                sharedPrefsHelper.save(PreferenceKeys.PREF_PASSWORD,newPasswordText.getText().toString().trim());
                TTApplication.password=newPasswordText.getText().toString().trim();
                GetRestAdapter.retrofitInterface=null;
                showToast("Password change successfully");
                manualBackPressed();
            } else {
                showToast(body.getMessage());
            }

        } else {
            showToast(getString(R.string.server_error_msg));
        }
    }

    @Override
    public void setProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismiss() {
        progressBar.setVisibility(View.GONE);
    }
}
