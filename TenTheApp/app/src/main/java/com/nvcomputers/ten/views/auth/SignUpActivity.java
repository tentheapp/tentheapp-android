package com.nvcomputers.ten.views.auth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nvcomputers.ten.R;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.interfaces.ResultCallbacks;
import com.nvcomputers.ten.model.input.RegisterUsernameInput;
import com.nvcomputers.ten.model.output.RegisterUsernameOutput;
import com.nvcomputers.ten.presenter.RegisterUsernamePresenter;
import com.nvcomputers.ten.utils.IntentHandler;
import com.nvcomputers.ten.utils.PreferenceKeys;
import com.nvcomputers.ten.utils.SharedPrefsHelper;
import com.nvcomputers.ten.utils.Utilities;
import com.nvcomputers.ten.views.core.BaseActivity;

import retrofit2.Call;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity implements View.OnClickListener,
        TextWatcher, AppCommonCallback, ResultCallbacks.VerifyUserNameCallback {
    private EditText userNameEt;
    private Button nextBtn;
    private ImageView imageview_username;
    //private ProgressBar progressBar;
    private TextView backBtn;
    private SharedPrefsHelper sharedPrefsHelper;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_username;
    }

    @Override
    protected void initViews() {

        sharedPrefsHelper = new SharedPrefsHelper(SignUpActivity.this);
        //    progressBar = (ProgressBar) findViewById(R.id.progressBar);
        final RelativeLayout parentlayout = (RelativeLayout) findViewById(R.id.parentView);
        parentlayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Utilities.hideKeypad(parentlayout);
                return false;
            }
        });
        userNameEt = (EditText) findViewById(R.id.username);
        nextBtn = (Button) findViewById(R.id.next);
        imageview_username = (ImageView) findViewById(R.id.imageview_username);
        nextBtn.setOnClickListener(this);
        userNameEt.addTextChangedListener(this);
        backBtn = (TextView) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                Utilities.hideKeypad(nextBtn);
                finish();
                break;
            case R.id.next:
                Utilities.hideKeypad(nextBtn);
                if (!Utilities.checkInternet(this)) {
                    Toast.makeText(this, getResources().getString(R.string.no_internet_msg), Toast.LENGTH_SHORT).show();
                } else {
                    String userName = userNameEt.getText().toString().trim();
                    RegisterUsernameInput registerUsernameInput = new RegisterUsernameInput();
                    registerUsernameInput.setUsername(userName);
                    RegisterUsernamePresenter registerUsernamePresenter = new RegisterUsernamePresenter(this, this);
                    registerUsernamePresenter.responseCheck(registerUsernameInput);
                }
                break;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (userNameEt.getText().toString().trim().length() == 0){
            imageview_username.setVisibility(View.INVISIBLE);
            imageview_username.setImageResource(R.drawable.cross_icon);
        }else
        if (userNameEt.getText().toString().trim().length() > 5 && !userNameEt.getText().toString().trim().contains(" ")) {
            imageview_username.setVisibility(View.VISIBLE);
            // imageview_username.Tag = 1;
            imageview_username.setImageResource(R.drawable.check);//check
            nextBtn.setClickable(true);
            nextBtn.setEnabled(true);
        } else {
            imageview_username.setVisibility(View.VISIBLE);
            imageview_username.setImageResource(R.drawable.cross_icon);//check
            nextBtn.setClickable(false);
            nextBtn.setEnabled(false);
        }
    }

    @Override
    public void setProgressBar() {
        showDialog();
        //  progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismiss() {
        hideDialog();
        //progressBar.setVisibility(View.GONE);
    }

    @Override
    public void usernameError(Call<RegisterUsernameOutput> call, Throwable t) {
        Utilities.showMessage(this, getString(R.string.server_error));
    }

    @Override
    public void onUserNameSuccess(Response<RegisterUsernameOutput> response) {
        RegisterUsernameOutput body = response.body();
        if (body != null) {
            String code = body.getResponse().getCode();
            if (code.equals("201")) {
                String userName = body.getResponse().getResult().getUsername();
                sharedPrefsHelper.save(PreferenceKeys.PREF_USER_NAME, userName);
                IntentHandler.switchActivity(SignUpActivity.this, CreatePasswordActivity.class);
            } else if (code.equals("400")) {
                Utilities.showMessage(this, body.getResponse().getMessage());
                imageview_username.setVisibility(View.VISIBLE);
                //userNameEt.setText("");

                imageview_username.setImageResource(R.drawable.cross_icon);
                imageview_username.setFocusable(true);
            } else {
                Utilities.showMessage(this, body.getResponse().getMessage());
            }
        } else {
            Utilities.showMessage(this, getString(R.string.server_error));
        }
    }

    @Override
    public void dispose() {

    }
}
