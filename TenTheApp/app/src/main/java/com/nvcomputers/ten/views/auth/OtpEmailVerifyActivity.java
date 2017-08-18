package com.nvcomputers.ten.views.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nvcomputers.ten.R;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.interfaces.ResultCallbacks;
import com.nvcomputers.ten.model.input.EmailSignUpUserInput;
import com.nvcomputers.ten.model.input.OtpEmailVerifyInput;
import com.nvcomputers.ten.model.input.ResendEmailOtpInput;
import com.nvcomputers.ten.model.output.OtpEmailVerifyResponse;
import com.nvcomputers.ten.model.output.ResendEmailOtpResponse;
import com.nvcomputers.ten.model.output.SignUpUserResponse;
import com.nvcomputers.ten.presenter.EmailSignUpPresenter;
import com.nvcomputers.ten.presenter.OtpEmailVerifyPresenter;
import com.nvcomputers.ten.presenter.ResendEmailOtpPresenter;
import com.nvcomputers.ten.utils.IntentHandler;
import com.nvcomputers.ten.utils.PreferenceKeys;
import com.nvcomputers.ten.utils.SharedPrefsHelper;
import com.nvcomputers.ten.utils.Utilities;
import com.nvcomputers.ten.views.contacts.ContactsActivity;
import com.nvcomputers.ten.views.core.BaseActivity;
import com.nvcomputers.ten.views.home.LandingActivity;

import retrofit2.Call;
import retrofit2.Response;

public class OtpEmailVerifyActivity extends BaseActivity implements View.OnClickListener,TextWatcher, AppCommonCallback, ResultCallbacks.OtpEmailVerifyCallback, ResultCallbacks.ResendEmailOtpCallback, EmailSignUpPresenter.SignupCallback {

    private EditText otpTxt;
    private Button resendOtpbtn, submitOtpBtn;
    private Intent intent;
    private String emailId;
    //private ProgressBar progressBar;
    private AlertDialog mSyncdialog;
    private TextView backBtn;
    private SharedPrefsHelper sharedPrefsHelper;
    private String actionFor;
    private ImageView mCrossImageView;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_otp_verify;
    }

    @Override
    protected void initViews() {
        sharedPrefsHelper = new SharedPrefsHelper(OtpEmailVerifyActivity.this);
        intent = getIntent();
        emailId = intent.getStringExtra("email_id");
        actionFor = intent.getStringExtra("action_for");

        findViewById(R.id.parentView).setOnClickListener(this);

      //  progressBar = (ProgressBar) findViewById(R.id.progressBar);
        otpTxt = (EditText) findViewById(R.id.email_otp_code);
        resendOtpbtn = (Button) findViewById(R.id.resendBtn);
        submitOtpBtn = (Button) findViewById(R.id.submitBtn);
        mCrossImageView = (ImageView) findViewById(R.id.imageview_code);
        resendOtpbtn.setOnClickListener(this);
        submitOtpBtn.setOnClickListener(this);
        backBtn = (TextView) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        if (actionFor.equals("signUp")) {
            resendOtpbtn.setVisibility(View.VISIBLE);
        } else {
            resendOtpbtn.setVisibility(View.GONE);
        }
        otpTxt.addTextChangedListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.parentView:
                 Utilities.hideKeypad(view);
                break;
            case R.id.resendBtn:
                Utilities.hideKeypad(resendOtpbtn);
                resendOtpApi();
                break;
            case R.id.submitBtn:
                Utilities.hideKeypad(submitOtpBtn);
                verifyOtpApi();
                break;
            case R.id.backBtn:
                finish();
                break;
        }

    }

    private void verifyOtpApi() {
        String otpCode = otpTxt.getText().toString().trim();
        if (otpCode.equals(null) || otpCode.equals("")) {
            Utilities.showSmallToast(this, "Please enter OTP code");
        } else {
            if (Utilities.checkInternet(OtpEmailVerifyActivity.this)) {
                OtpEmailVerifyInput otpEmailVerifyInput = new OtpEmailVerifyInput();
                otpEmailVerifyInput.setEmail(emailId);
                otpEmailVerifyInput.setOtp(otpCode);
                OtpEmailVerifyPresenter otpEmailVerifyPresenter = new OtpEmailVerifyPresenter(this, this);
                otpEmailVerifyPresenter.responseCheck(otpEmailVerifyInput);

            } else {
                Utilities.showMessage(OtpEmailVerifyActivity.this, getString(R.string.no_internet_msg));
            }
        }

    }

    private void resendOtpApi() {
        if (Utilities.checkInternet(OtpEmailVerifyActivity.this)) {
            ResendEmailOtpInput resendEmailOtpInput = new ResendEmailOtpInput();
            resendEmailOtpInput.setEmail(emailId);
            ResendEmailOtpPresenter resendEmailOtpPresenter = new ResendEmailOtpPresenter(this, this);
            resendEmailOtpPresenter.responseCheck(resendEmailOtpInput);
        } else {
            Utilities.showMessage(OtpEmailVerifyActivity.this, getString(R.string.no_internet_msg));
        }
    }

    @Override
    public void setProgressBar() {
        showDialog();
        //progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismiss() {
         hideDialog();
        //progressBar.setVisibility(View.GONE);
    }

    @Override
    public void emailOtpError(Call<OtpEmailVerifyResponse> call, Throwable t) {
        Utilities.showMessage(this, getString(R.string.server_error));
    }

    @Override
    public void onEmailOtpSuccess(Response<OtpEmailVerifyResponse> response) {
        OtpEmailVerifyResponse body = response.body();
        if (body != null) {
            String code = body.getResponse().getCode();
            if (code.equals("201")) {
                EmailSignUpUserInput emailSignUpUserInput = new EmailSignUpUserInput();
                emailSignUpUserInput.setUsername(sharedPrefsHelper.get(PreferenceKeys.PREF_USER_NAME, ""));
                emailSignUpUserInput.setPassword(sharedPrefsHelper.get(PreferenceKeys.PREF_PASSWORD, ""));
                emailSignUpUserInput.setEmail(emailId);
                EmailSignUpPresenter signUpPresenter = new EmailSignUpPresenter(this, this);
                signUpPresenter.responseCheck(emailSignUpUserInput);
            } else if(code.equals("400")) {
                mCrossImageView.setVisibility(View.VISIBLE);
                mCrossImageView.setImageResource(R.drawable.cross_icon);
                submitOtpBtn.setClickable(false);
                submitOtpBtn.setEnabled(false);
                if(body.getResponse().getMessage().equals("Invalid OTP code")){
                    Utilities.showMessage(this, "Invalid verification code");
                }else {
                    Utilities.showMessage(this, body.getResponse().getMessage());
                }
            }
            else {

                Utilities.showMessage(this, body.getResponse().getMessage());
            }
        } else {
            Utilities.showMessage(this, getString(R.string.server_error));
        }
    }

    private void contactSyncDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.sync_contacts));
        builder.setMessage(getString(R.string.sync_contacts_msg));
        builder.setPositiveButton(getString(R.string.sync), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mSyncdialog.dismiss();
                IntentHandler.switchActivity(OtpEmailVerifyActivity.this, ContactsActivity.class);
                Intent intent=new Intent(OtpEmailVerifyActivity.this,ContactsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("from_otp", true);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton(getString(R.string.skip), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mSyncdialog.dismiss();
                Intent intent=new Intent(OtpEmailVerifyActivity.this,LandingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });

        mSyncdialog = builder.create();
        mSyncdialog.show();
    }

    @Override
    public void resendEmailOtpError(Call<ResendEmailOtpResponse> call, Throwable t) {
        Utilities.showMessage(this, getString(R.string.server_error));
    }

    @Override
    public void onResendEmailOtpSuccess(Response<ResendEmailOtpResponse> response) {
        ResendEmailOtpResponse body = response.body();
        if (body != null) {
            String code = body.getResponse().getCode();
            if (code.equals("201")) {
                Utilities.showMessage(this, "OTP code is sent to your email");
            } else {
                Utilities.showMessage(this, body.getResponse().getMessage());
            }
        } else {
            Utilities.showMessage(this, getString(R.string.server_error));
        }
    }

    @Override
    public void signUpError(Call<SignUpUserResponse> call, Throwable t) {
        Utilities.showMessage(this, getString(R.string.server_error));
    }

    @Override
    public void onSignUpSuccess(Response<SignUpUserResponse> response) {
        SignUpUserResponse body = response.body();
        if (body != null) {
            String code = body.getResponse().getCode();
            if (code.equals("201")) {
                Utilities.showSmallToast(this, "Register Successfully.");
                SignUpUserResponse.Result data = body.getResponse().getResult();
                sharedPrefsHelper.save(PreferenceKeys.PREF_USER_NAME, data.getUsername());
                sharedPrefsHelper.save(PreferenceKeys.PREF_USER_ID, data.getIdUser());
                sharedPrefsHelper.save(PreferenceKeys.PREF_EMAIL_ID, data.getEmail());
                contactSyncDialog();
            } else {
                Utilities.showMessage(this, body.getResponse().getMessage());
            }
        } else {
            Utilities.showMessage(this, getString(R.string.server_error));
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (otpTxt.getText().toString().trim().length() == 0) {
            mCrossImageView.setVisibility(View.INVISIBLE);
            // imageview_username.Tag = 1;
            mCrossImageView.setImageResource(R.drawable.cross_icon);//check
            submitOtpBtn.setClickable(false);
            submitOtpBtn.setEnabled(false);
        }else  if (otpTxt.getText().toString().trim().length() == 4) {
            mCrossImageView.setVisibility(View.VISIBLE);
            // imageview_username.Tag = 1;
            mCrossImageView.setImageResource(R.drawable.check);//check
            submitOtpBtn.setClickable(true);
            submitOtpBtn.setEnabled(true);
        } else {
            mCrossImageView.setVisibility(View.VISIBLE);
            mCrossImageView.setImageResource(R.drawable.cross_icon);
            submitOtpBtn.setClickable(false);
            submitOtpBtn.setEnabled(false);//check
        }
    }

    @Override
    public void dispose() {

    }
}
