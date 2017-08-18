package com.nvcomputers.ten.views.auth;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nvcomputers.ten.R;
import com.nvcomputers.ten.adapter.CountryCodeAdapter;
import com.nvcomputers.ten.dialog.CountryDialog;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.interfaces.ResultCallbacks;
import com.nvcomputers.ten.model.input.ForgotPasswordPhoneInput;
import com.nvcomputers.ten.model.input.PhoneVerifyInput;
import com.nvcomputers.ten.model.output.ForgotPasswordResponse;
import com.nvcomputers.ten.model.output.PhoneVerifyResponse;
import com.nvcomputers.ten.presenter.ForgotPasswordPhonePresenter;
import com.nvcomputers.ten.presenter.PhoneVerifyPresenter;
import com.nvcomputers.ten.utils.IntentHandler;
import com.nvcomputers.ten.utils.Utilities;
import com.nvcomputers.ten.views.core.BaseActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class PhoneVerificationActivity extends BaseActivity implements View.OnClickListener, AppCommonCallback, ResultCallbacks.PhoneVerifyCallback, /*ForgotPasswordPhonePresenter.ForgotPassPhoneCallback,*/ TextWatcher {

    private EditText phoneNumberEt;
    private Button mSendButton;//, mTryEmailBtn;
    private ImageView mCrossImageView;
    private TextView mPhoneCodeTextView;
    private CountryDialog countryDialog;
    private String phonenoCode = "+1", phoneNumber;
    private String mCountryId = "+1";
   // private ProgressBar progressBar;
    private TextView backBtn;
    //private String actionFor;
    //private LinearLayout mainLayout;
    private LinearLayout parentView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_phoneno;
    }

    @Override
    protected void initViews() {
        //actionFor = getIntent().getStringExtra("action_for");
   //     progressBar = (ProgressBar) findViewById(R.id.progressBar);
        phoneNumberEt = (EditText) findViewById(R.id.edit_text_phnnumber);
       // mTryEmailBtn = (Button) findViewById(R.id.button_try_email);
        mPhoneCodeTextView = (TextView) findViewById(R.id.text_view_frontcode);
        mCrossImageView = (ImageView) findViewById(R.id.image_view_tick);
       // mainLayout = (LinearLayout) findViewById(R.id.parentView);
        mPhoneCodeTextView.setOnClickListener(this);
        mSendButton =(Button)findViewById(R.id.sendbtn);
        mSendButton.setOnClickListener(this);
        backBtn = (TextView) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
     //   mTryEmailBtn.setOnClickListener(this);
      //  mainLayout.setOnClickListener(this);
      findViewById(R.id.ll_parent).setOnClickListener(this);
      //  parentView.setOnClickListener(this);
        phoneNumberEt.addTextChangedListener(this);
    }

    private void hitCodeApi() {
        if (!Utilities.checkInternet(PhoneVerificationActivity.this)) {
            showToast(R.string.no_internet_msg);
        } else {
            countryDialog = new CountryDialog(PhoneVerificationActivity.this);
            countryDialog.showCountryDialog();
        }
    }

    @Override
    public void setAdapter(RecyclerView recyclerView, ArrayList<?> mList) {
        super.setAdapter(recyclerView, mList);
        CountryCodeAdapter customAdapter = new CountryCodeAdapter(this, mList, countryDialog);
        recyclerView.setAdapter(customAdapter);
    }


    @Override
    public void dispose() {
        finish();
    }

    public void setCodeText(String phoneCode, String countryId) {
        phonenoCode = phoneCode;
        mPhoneCodeTextView.setText(phoneCode);
        mCountryId = countryId;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_parent:
               Utilities.hideKeypad(v);
                break;
            case R.id.sendbtn:
                Utilities.hideKeypad(v);
                numberValidation();
                break;
            case R.id.text_view_frontcode:
                Utilities.hideKeypad(v);
                hitCodeApi();
                break;
            case R.id.backBtn:
                Utilities.hideKeypad(v);
                finish();
                break;
        }
    }

    private void numberValidation() {
        phoneNumber = phoneNumberEt.getText().toString().trim();
        if (phoneNumber.equals(null) || phoneNumber.equals("")) {
            Utilities.showMessage(this, "Enter Phone Number");
        }
        else if (phoneNumber.trim().length()>5){
            if (Utilities.checkInternet(PhoneVerificationActivity.this)) {
                setProgressBar();
                    PhoneVerifyInput phoneVerifyInput = new PhoneVerifyInput();
                    phoneVerifyInput.setPhone(phoneNumber);
                    phoneVerifyInput.setCountryId(mCountryId);

                    PhoneVerifyPresenter phoneVerifyPresenter = new PhoneVerifyPresenter(this, this);
                    phoneVerifyPresenter.responseCheck(phoneVerifyInput);
                //}
            } else {
                Utilities.showSmallToast(this, getString(R.string.no_internet_msg));
            }
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
        //progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void phoneVerifyError(Call<PhoneVerifyResponse> call, Throwable t) {
        Utilities.showMessage(this, getString(R.string.server_error));
    }

    @Override
    public void onPhoneVerifySuccess(Response<PhoneVerifyResponse> response) {
        PhoneVerifyResponse body = response.body();
        dismiss();
        if (body != null) {
            String code = body.getResponse().getCode();
            if (code.equals("201")) {
                String otp = body.getResponse().getResult().getOtp();
                Log.e("otp--", otp);
                String phoneNo = body.getResponse().getResult().getPhone();
                Intent intent = new Intent(PhoneVerificationActivity.this, PhoneOtpVerifyActivity.class);
                intent.putExtra("phone_no", phoneNo);
                intent.putExtra("country_id", mCountryId);
                startActivity(intent);
            } else {
                Utilities.showMessage(this, body.getResponse().getMessage());
            }
        } else {
            Utilities.showMessage(this, getString(R.string.server_error_msg));
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
        if (phoneNumberEt.getText().toString().trim().length() == 0) {
            mCrossImageView.setVisibility(View.INVISIBLE);
            // imageview_username.Tag = 1;
            mCrossImageView.setImageResource(R.drawable.cross_icon);//check
        }else  if (phoneNumberEt.getText().toString().trim().length() > 5) {
            mCrossImageView.setVisibility(View.VISIBLE);
            // imageview_username.Tag = 1;
            mCrossImageView.setImageResource(R.drawable.check);//check
        } else {
            mCrossImageView.setVisibility(View.VISIBLE);
            mCrossImageView.setImageResource(R.drawable.cross_icon);
        }
    }
}