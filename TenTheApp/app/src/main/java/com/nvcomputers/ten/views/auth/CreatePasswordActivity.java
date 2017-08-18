package com.nvcomputers.ten.views.auth;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nvcomputers.ten.R;
import com.nvcomputers.ten.utils.PreferenceKeys;
import com.nvcomputers.ten.utils.SharedPrefsHelper;
import com.nvcomputers.ten.utils.Utilities;

public class CreatePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText passwordText, confirmPasswordText;
    private Button nextBtn;
    private ImageView passwordCheckImg, confirmPasswordCheckImg;
    private String password, confirmPassword;
    private TextView backBtn;
    private SharedPrefsHelper sharedPrefsHelper;
    private LinearLayout mainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);
        sharedPrefsHelper = new SharedPrefsHelper(CreatePasswordActivity.this);
        initUI();
    }

    private void initUI() {
        passwordText = (EditText) findViewById(R.id.password_edttxt);
        confirmPasswordText = (EditText) findViewById(R.id.confirm_password_edttxt);
        nextBtn = (Button) findViewById(R.id.password_next_btn);
        passwordCheckImg = (ImageView) findViewById(R.id.password_check_img);
        confirmPasswordCheckImg = (ImageView) findViewById(R.id.confirmpassword_check_img);
        mainLayout = (LinearLayout) findViewById(R.id.inner_layout);

        password = passwordText.getText().toString().trim();
        confirmPassword = confirmPasswordText.getText().toString().trim();

        backBtn = (TextView) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        mainLayout.setOnClickListener(this);
        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (passwordText.getText().toString().trim().length() == 0){
                    passwordCheckImg.setVisibility(View.INVISIBLE);
                    passwordCheckImg.setImageResource(R.drawable.cross_icon);
                }else

                if (passwordText.getText().toString().trim().length() > 5) {
                    passwordCheckImg.setVisibility(View.VISIBLE);
                    passwordCheckImg.setImageResource(R.drawable.check);//check
                } else {
                    passwordCheckImg.setVisibility(View.VISIBLE);
                    passwordCheckImg.setImageResource(R.drawable.cross_icon);//check
                }
            }
        });
        confirmPasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (confirmPasswordText.getText().toString().trim().length() == 0){
                    confirmPasswordCheckImg.setVisibility(View.INVISIBLE);
                    confirmPasswordCheckImg.setImageResource(R.drawable.cross_icon);
                }else
                if (confirmPasswordText.getText().toString().trim().length() > 5 &&
                        confirmPasswordText.getText().toString().trim().equals(passwordText.getText().toString().trim())) {
                    confirmPasswordCheckImg.setVisibility(View.VISIBLE);
                    confirmPasswordCheckImg.setImageResource(R.drawable.check);//check
                    nextBtn.setClickable(true);
                    nextBtn.setEnabled(true);
                } else {
                    confirmPasswordCheckImg.setVisibility(View.VISIBLE);
                    confirmPasswordCheckImg.setImageResource(R.drawable.cross_icon);//check
                }
            }

        });
        nextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                Utilities.hideKeypad(view);
                finish();
                break;
            case R.id.password_next_btn:
                Utilities.hideKeypad(view);
                verificationPopUp();
                break;
            case R.id.inner_layout:
                Utilities.hideKeypad(view);
                break;

        }

    }

    private void verificationPopUp() {
        password = passwordText.getText().toString().trim();
        sharedPrefsHelper.save(PreferenceKeys.PREF_PASSWORD, password);
        final Dialog dialouge_view = new Dialog(CreatePasswordActivity.this);
        dialouge_view.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialouge_view.setContentView(R.layout.custom_layout_choose_picker);
        // Set dialog title
        TextView headerText = (TextView) dialouge_view.findViewById(R.id.view1);
        headerText.setText("Choose with which option you want to register?");
        TextView selectEmail = (TextView) dialouge_view.findViewById(R.id.txt_view_via_email);
        TextView selectPhone = (TextView) dialouge_view.findViewById(R.id.txt_view_via_number);
        TextView cancelBtn = (TextView) dialouge_view.findViewById(R.id.cancel);
        dialouge_view.show();
        selectEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialouge_view.dismiss();
                Intent intent = new Intent(CreatePasswordActivity.this, EmailVerificationActivity.class);
                //intent.putExtra("action_for", "signup");
                startActivity(intent);
            }
        });


        selectPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialouge_view.dismiss();
                Intent intent = new Intent(CreatePasswordActivity.this, PhoneVerificationActivity.class);
                //intent.putExtra("action_for", "signup");
                startActivity(intent);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialouge_view.dismiss();
            }
        });
    }


}
