package com.nvcomputers.ten.views.auth;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nvcomputers.ten.R;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.interfaces.ResultCallbacks;
import com.nvcomputers.ten.model.input.EmailVerificationInput;
import com.nvcomputers.ten.model.input.ForgotPasswordEmailInput;
import com.nvcomputers.ten.model.output.EmailVerificationResponse;
import com.nvcomputers.ten.model.output.ForgotPasswordResponse;
import com.nvcomputers.ten.presenter.ForgotPasswordEmailPresenter;
import com.nvcomputers.ten.presenter.VerifyEmailPresenter;
import com.nvcomputers.ten.utils.Utilities;
import com.nvcomputers.ten.views.core.BaseActivity;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by kambojRavi on 4/21/2017.
 */

public class EmailVerificationActivity extends BaseActivity implements View.OnClickListener, AppCommonCallback, ResultCallbacks.EmailVerificationCallback, /*ForgotPasswordEmailPresenter.ForgotPassEmailCallback,*/ TextWatcher {

    private EditText mEmailAddressEditText;
    private Button mSendButton; //, mTryPhoneBtn
    private ImageView mCrossImageView;
    private String mUserEmailAddress;
  //  private ProgressBar progressBar;
    private TextView backBtn;
    private Intent intent;
    //private String actionFor;
    private View mView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_email;
    }

    @Override
    protected void initViews() {
        mView=(View)findViewById(R.id.view);
        mView.setVisibility(View.GONE);

        intent = getIntent();
        //actionFor = intent.getStringExtra("action_for");
      //  progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mEmailAddressEditText = (EditText) findViewById(R.id.edit_text_email);
        mSendButton = (Button) findViewById(R.id.sendbtn);
      //  mTryPhoneBtn = (Button) findViewById(R.id.try_phone);
        mCrossImageView = (ImageView) findViewById(R.id.image_view_tick);
        mSendButton.setOnClickListener(this);
       // mTryPhoneBtn.setOnClickListener(this);
        backBtn = (TextView) findViewById(R.id.text_view_back);
        backBtn.setOnClickListener(this);
        findViewById(R.id.parentView).setOnClickListener(this);
        mEmailAddressEditText.addTextChangedListener(this);
        mSendButton.setClickable(false);
        mSendButton.setEnabled(false);
    }

    @Override
    public void dispose() {
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.parentView:
                Utilities.hideKeypad(view);
                break;
            case R.id.sendbtn:
                Utilities.hideKeypad(view);
                validation();
                break;
           /* case R.id.try_phone:
                Utilities.hideKeypad(view);
                Intent intent = new Intent(EmailVerificationActivity.this, PhoneVerificationActivity.class);
                intent.putExtra("action_for",actionFor);
                startActivity(intent);
                finish();
                break;*/
            case R.id.text_view_back:
                Utilities.hideKeypad(view);
                finish();
                break;
        }
    }

    private void validation() {
        mUserEmailAddress = mEmailAddressEditText.getText().toString().trim();
        boolean cancel = false;
        View focusView = null;
        if (mUserEmailAddress.equals(null) || mUserEmailAddress.equals("")) {
            mEmailAddressEditText.setError("Enter your email address");
            focusView = mEmailAddressEditText;
            cancel = true;
        } else if (!Utilities.isValidEmail(mUserEmailAddress)) {
            mEmailAddressEditText.setError("Please enter valid email id");
            focusView = mEmailAddressEditText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            if (!Utilities.checkInternet(this)) {
                Utilities.showMessage(EmailVerificationActivity.this, getString(R.string.no_internet_msg));

            } else {
                /*if (actionFor.equals("forgot_password")) {
                    mView.setVisibility(View.VISIBLE);
                    ForgotPasswordEmailInput forgotPasswordEmailInput = new ForgotPasswordEmailInput();
                    forgotPasswordEmailInput.setEmail(mUserEmailAddress);
                    ForgotPasswordEmailPresenter forgotPasswordEmailPresenter = new ForgotPasswordEmailPresenter(this, this);
                    forgotPasswordEmailPresenter.responseCheck(forgotPasswordEmailInput);
                } else {*/
                    mView.setVisibility(View.VISIBLE);
                    EmailVerificationInput emailVerificationInput = new EmailVerificationInput();
                    emailVerificationInput.setEmail(mUserEmailAddress);
                    VerifyEmailPresenter verifyEmailPresenter = new VerifyEmailPresenter(this, this);
                    verifyEmailPresenter.responseCheck(emailVerificationInput);
                //}

            }
        }
    }

    @Override
    public void setProgressBar() {
        showDialog();
       // progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismiss() {
        hideDialog();
     //   progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void emailVerifyError(Call<EmailVerificationResponse> call, Throwable t) {
        Utilities.showMessage(this, getString(R.string.server_error_msg));
    }

    @Override
    public void onEmailVerifySuccess(Response<EmailVerificationResponse> response) {
        EmailVerificationResponse body = response.body();
        if (body != null) {
            String code = body.getResponse().getCode();
            if (code.equals("201")) {
                String otp = body.getResponse().getResult().getOtp();
                Log.d("---","otp-"+otp);
                //String email = body.getResponse().getResult().getEmail();
                Intent intent = new Intent(EmailVerificationActivity.this, OtpEmailVerifyActivity.class);
                intent.putExtra("email_id", mUserEmailAddress);
                intent.putExtra("action_for", "signUp");
                startActivity(intent);
                mView.setVisibility(View.GONE);
            } else {
                Utilities.showMessage(this, body.getResponse().getMessage());
                mView.setVisibility(View.GONE);
            }
        } else {
            Utilities.showMessage(this, getString(R.string.server_error));
            mView.setVisibility(View.GONE);
        }
    }

   /* @Override
    public void forgotPassEmailError(Call<ForgotPasswordResponse> call, Throwable t) {
        mView.setVisibility(View.GONE);
        Utilities.showMessage(this, getString(R.string.server_error_msg));
    }


    @Override
    public void onForgotPassEmailSuccess(Response<ForgotPasswordResponse> response) {
        ForgotPasswordResponse body = response.body();
        if (body != null) {
            String code = body.getSuccess();
            if (code.equals("true")) {
                Intent intent = new Intent(EmailVerificationActivity.this, ForgotPasswordActivity.class);
                intent.putExtra("email_id", mUserEmailAddress);
                intent.putExtra("action_with", "emailId");
                startActivity(intent);
                mView.setVisibility(View.GONE);
            } else {
                Utilities.showMessage(this, "Invalid Email address");
                mView.setVisibility(View.GONE);
            }
        } else {
            Utilities.showMessage(this, getString(R.string.server_error_msg));
            mView.setVisibility(View.GONE);
        }
    }*/

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (mEmailAddressEditText.getText().toString().trim().length() == 0) {
            mCrossImageView.setVisibility(View.INVISIBLE);
            // imageview_username.Tag = 1;
            mCrossImageView.setImageResource(R.drawable.cross_icon);//check
            mSendButton.setClickable(false);
            mSendButton.setEnabled(false);
        }else

        if (mEmailAddressEditText.getText().toString().trim().length() > 1 && Utilities.isValidEmail(mEmailAddressEditText.getText().toString().trim())) {
            mCrossImageView.setVisibility(View.VISIBLE);
            // imageview_username.Tag = 1;
            mCrossImageView.setImageResource(R.drawable.check);//check
            mSendButton.setClickable(true);
            mSendButton.setEnabled(true);
        } else {
            mCrossImageView.setVisibility(View.VISIBLE);
            mCrossImageView.setImageResource(R.drawable.cross_icon);
            mSendButton.setClickable(false);
            mSendButton.setEnabled(false);//check
        }
    }
}
