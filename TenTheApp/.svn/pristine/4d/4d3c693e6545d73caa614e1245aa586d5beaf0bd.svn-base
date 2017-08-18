package com.nvcomputers.ten.views.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.nvcomputers.ten.GCMRegistrationIntentService;
import com.nvcomputers.ten.R;
import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.input.EditProfileInput;
import com.nvcomputers.ten.model.output.EnableDisableResponse;
import com.nvcomputers.ten.model.output.FollowingResponse;
import com.nvcomputers.ten.model.output.LogoutResponse;
import com.nvcomputers.ten.model.output.ProfileResponse;
import com.nvcomputers.ten.model.output.UpdateTokenResponse;
import com.nvcomputers.ten.presenter.EditProfilePresenter;
import com.nvcomputers.ten.presenter.FollowersPresenter;
import com.nvcomputers.ten.presenter.GetUserProfilePresenter;
import com.nvcomputers.ten.presenter.LogoutPresenter;
import com.nvcomputers.ten.presenter.UpdateTokenPresenter;
import com.nvcomputers.ten.utils.PreferenceKeys;
import com.nvcomputers.ten.utils.SharedPrefsHelper;
import com.nvcomputers.ten.utils.Utilities;
import com.nvcomputers.ten.views.TTApplication;
import com.nvcomputers.ten.views.auth.Splash2Activity;
import com.nvcomputers.ten.views.core.BaseActivity;
import com.nvcomputers.ten.views.core.CarouselPagerFragment;
import com.nvcomputers.ten.views.profile.WebViewActivity;

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LandingActivity extends BaseActivity implements View.OnClickListener, AppCommonCallback, LogoutPresenter.LogoutCallback,
        NavigationView.OnNavigationItemSelectedListener, UpdateTokenPresenter.UpdateTokenCallback, FollowersPresenter.FollowersCallback, GetUserProfilePresenter.GetUserProfileCallback, EditProfilePresenter.EditProfileCallbacks {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 700;
    private TextView text_tab1, text_tab2, text_tab3, text_tab4, text_tab5;
    private SharedPrefsHelper mPrefHelper;
    private CarouselPagerFragment carouselFragment;
    private boolean doubleBackToExitPressedOnce = false;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    private String TAG = "LandingActivity";
    private FollowersPresenter mFollowersPresenter;
    public CircleImageView badgeIcon;
    public static LandingActivity landingActivity;
    private LinearLayout bottomTabLayout;
    private boolean isFromNotification;
    private String count, offset;
    private Toolbar supportActionBar;
    private TextView synchContactOption, helpOption, logoutOption;
    public Switch privateOption;
    private Switch notificationOption;
    private TextView changepasswordOption;
    private boolean isPrivate;
    private boolean checked;
    private ImageView drawerBackBtn;
    public String mIsPrivate;
    public DrawerLayout drawer;
    private SharedPrefsHelper sharedPrefsHelper;
    private boolean isNotify;
    private String mIsNotify;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        landingActivity = null;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;

    }

    @Override
    public void onBackPressed() {
        if (!carouselFragment.onBackPressed()) {
            // container Fragment or its associates couldn't handle the back pressed task
            // delegating the task to super class
            //super.onBackPressed();
            // ---it will finish home activity----
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            // carousel handled the back pressed task
            // do not call super
            // --- child fragment will pop here---
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_landing;
    }

    @Override
    protected void initViews() {

        landingActivity = this;
        mPrefHelper = new SharedPrefsHelper(this);
        TTApplication.userName = mPrefHelper.get(PreferenceKeys.PREF_USER_NAME, "");
        TTApplication.password = mPrefHelper.get(PreferenceKeys.PREF_PASSWORD, "");
        GetRestAdapter.retrofitInterface = null;

        isFromNotification = getIntent().getBooleanExtra("", false);

        bottomTabLayout = (LinearLayout) findViewById(R.id.bottomTabLayout);
        text_tab1 = (TextView) findViewById(R.id.text_tab1);
        text_tab2 = (TextView) findViewById(R.id.text_tab2);
        text_tab3 = (TextView) findViewById(R.id.text_tab3);
        text_tab4 = (TextView) findViewById(R.id.text_tab4);
        text_tab5 = (TextView) findViewById(R.id.text_tab5);
        badgeIcon = (CircleImageView) findViewById(R.id.badge_icon);
        text_tab1.setOnClickListener(this);
        text_tab2.setOnClickListener(this);
        text_tab3.setOnClickListener(this);
        text_tab4.setOnClickListener(this);
        text_tab5.setOnClickListener(this);
        text_tab1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tabbar0selected, 0, 0);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        drawerBackBtn = (ImageView) header.findViewById(R.id.drawerbackbtn);
        drawerBackBtn.setOnClickListener(this);
        privateOption = (Switch) header.findViewById(R.id.privateBtn);
        notificationOption = (Switch) header.findViewById(R.id.notificationBtn);
        synchContactOption = (TextView) header.findViewById(R.id.syncContactText);
        helpOption = (TextView) header.findViewById(R.id.helpText);
        logoutOption = (TextView) header.findViewById(R.id.logoutText);
        changepasswordOption = (TextView) header.findViewById(R.id.passwordText);
        changepasswordOption.setOnClickListener(this);
        synchContactOption.setOnClickListener(this);
        helpOption.setOnClickListener(this);
        logoutOption.setOnClickListener(this);
        hitProfileApi();
        privateOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                isPrivate = b;

            }
        });
        notificationOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                isNotify = b;

            }
        });

//        privateOption.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checked) {
//                    checked = false;
//                } else {
//                    checked = true;
//                }
//                enableDisable();
//            }
//        });
        if (mSavedInstanceState == null) {
            // withholding the previously created fragment from being created again
            // On orientation change, it will prevent fragment recreation
            // its necessary to reserving the fragment stack inside each tab
            carouselFragment = new CarouselPagerFragment();
            replaceFragment(R.id.main_frame_layout, carouselFragment);
        } else {
            // restoring the previously created fragment
            // and getting the reference
            carouselFragment = (CarouselPagerFragment) getSupportFragmentManager().getFragments().get(0);
        }
        //Initializing our broadcast receiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    String gcmDeviceToken = intent.getStringExtra("token");
                    UpdateTokenPresenter updateTokenPresenter = new UpdateTokenPresenter(LandingActivity.this, LandingActivity.this);
                    updateTokenPresenter.responseCheck(gcmDeviceToken, "android");
                }
            }
        };
        // Registering BroadcastReceiver
        registerReceiver();
        //Checking play service is available or not
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, GCMRegistrationIntentService.class);
            startService(intent);

            hitGetFollowingApi();
        }
        if (isFromNotification) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            text_tab2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tabbar1selected, 0, 0);
                            carouselFragment.updatePage(1);
                            //carouselFragment.updateButtonUI();
                        }
                    });
                }
            }, 1000);

        }
    }

    private void hitProfileApi() {
        String mUserName = mPrefHelper.get(PreferenceKeys.PREF_USER_NAME, "");
        GetUserProfilePresenter getUserProfilePresenter = new GetUserProfilePresenter(this, this);
        getUserProfilePresenter.responseCheck(mUserName);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logoutText) {
            //hitLogoutApi();
        } else if (id == R.id.syncContactText) {

        } else if (id == R.id.helpText) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void enableDisable(/*boolean isChecked*/) {
        if (!Utilities.checkInternet(LandingActivity.this)) {
            showToast(R.string.no_internet_msg);
            //mEditProfileCheckBox.setChecked(isChecked);
        } else {
            Call<EnableDisableResponse> response = GetRestAdapter.getRestAdapter(true).enableDisableFollow();
            response.enqueue(new Callback<EnableDisableResponse>() {
                @Override
                public void onResponse(Call<EnableDisableResponse> call, retrofit2.Response<EnableDisableResponse> response) {
                    hideDialog();
                    if (response != null && response.body() != null) {
                        if (response.body().getStatus().equals("enabled")) {
                            privateOption.setChecked(true);
                            checked = true;
                        } else {

                            privateOption.setChecked(false);
                            checked = false;
                        }
                    } else {
                        showToast(getString(R.string.server_error_msg));
                    }
                }


                @Override
                public void onFailure(Call<EnableDisableResponse> call, Throwable t) {
                    hideDialog();
                    showToast(t.getMessage());
                }
            });
        }
    }

    public void setSupportActionBar(Toolbar supportActionBar) {
        this.supportActionBar = supportActionBar;
    }

    public void showBadgeIcon() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (badgeIcon.getVisibility() == View.VISIBLE) {
                    // Its visible
                } else {
                    badgeIcon.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void hideBadgeIcon() {
        if (badgeIcon.getVisibility() == View.VISIBLE) {
            badgeIcon.setVisibility(View.GONE);
        } else {

        }
    }

    private void hitGetFollowingApi() {
        String userId = mPrefHelper.get(PreferenceKeys.PREF_USER_ID, "");
        mFollowersPresenter = new FollowersPresenter(this, this);
        mFollowersPresenter.getFollowingResponse(userId, offset, count);
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
            isReceiverRegistered = true;
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        setDeaultValues();
        //boolean enabled=false;
        //int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED;

        switch (v.getId()) {
            case R.id.drawerbackbtn:
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.syncContactText:
                drawer.closeDrawer(GravityCompat.START);
                carouselFragment.updateProfileFragment(2);
                break;
            case R.id.passwordText:
                drawer.closeDrawer(GravityCompat.START);
                carouselFragment.updateProfileFragment(3);
                break;
            case R.id.helpText:
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(this, WebViewActivity.class);
                startActivity(intent);
                break;
            case R.id.logoutText:
                drawer.closeDrawer(GravityCompat.START);
                hitLogoutApi();
                break;
            case R.id.text_tab1:
                if (CarouselPagerFragment.currentPage == 0) {
                    carouselFragment.popItemsIfExist(0);
                }
                text_tab1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tabbar0selected, 0, 0);
                carouselFragment.updatePage(0);
                CarouselPagerFragment.currentPage = 0;
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                break;

            case R.id.text_tab2:
                if (CarouselPagerFragment.currentPage == 1) {
                    carouselFragment.popItemsIfExist(1);
                }
                text_tab2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tabbar1selected, 0, 0);
                hideBadgeIcon();
                carouselFragment.updatePage(1);
                CarouselPagerFragment.currentPage = 1;
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                break;


            case R.id.text_tab3:
                carouselFragment.updatePage(2);
                CarouselPagerFragment.currentPage = 2;
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                break;

            case R.id.text_tab4:
                if (CarouselPagerFragment.currentPage == 3) {
                    carouselFragment.popItemsIfExist(3);
                }
                text_tab4.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tabbar3selected, 0, 0);
                carouselFragment.updatePage(3);
                CarouselPagerFragment.currentPage = 3;
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                break;

            case R.id.text_tab5:
                if (CarouselPagerFragment.currentPage == 4) {
                    carouselFragment.popItemsIfExist(4);
                }
                text_tab5.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tabbar4selected, 0, 0);
                carouselFragment.updatePage(4);
                CarouselPagerFragment.currentPage = 4;
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                break;
        }

    }

    private void hitLogoutApi() {
        sharedPrefsHelper = new SharedPrefsHelper(this);
        String userName = sharedPrefsHelper.get(PreferenceKeys.PREF_USER_NAME, "");
        String password = sharedPrefsHelper.get(PreferenceKeys.PREF_PASSWORD, "");
        if (Utilities.checkInternet(this)) {
            LogoutPresenter logoutPresenter = new LogoutPresenter(this, this);
            logoutPresenter.responseCheck(userName, password);
        } else {
            showToast(R.string.no_internet_msg);
        }
    }

    private void setDeaultValues() {
        text_tab1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tabbar0, 0, 0);
        text_tab2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tabbar1, 0, 0);
        text_tab4.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tabbar3, 0, 0);
        text_tab5.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tabbar4, 0, 0);
    }

    @Override
    public void dispose() {

    }

    public void setHomePage() {
        text_tab1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tabbar0selected, 0, 0);
    }

    @Override
    public void setProgressBar() {

    }

    @Override
    public void dismiss() {

    }

    @Override
    public void updateTokenError(Call<UpdateTokenResponse> call, Throwable t) {

    }

    @Override
    public void onUpdateTokenSuccess(Response<UpdateTokenResponse> response) {

    }

    @Override
    public void onFollowersSuccess(FollowingResponse followingResponse) {

        if (followingResponse.getUsers() != null && followingResponse.getUsers().size() > 0) {
            String userNameList = "";
            for (int i = 0; i < followingResponse.getUsers().size(); i++) {
                userNameList = userNameList + "," + followingResponse.getUsers().get(i).getUsername();
            }
            mPrefHelper.save(PreferenceKeys.PREF_FOLLOWING_LIST, userNameList);
        }
    }

    @Override
    public void onFollowingError(Throwable t) {

    }

    public void hideBottomButtons() {
        bottomTabLayout.setVisibility(View.GONE);
    }

    public void showBottomButtons() {
        bottomTabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void logoutError(Call<LogoutResponse> call, Throwable t) {
        showToast(R.string.server_error_msg);
    }

    @Override
    public void onLogoutSuccess(Response<LogoutResponse> response) {
        clearPreferences();
        Intent intent = new Intent(this, Splash2Activity.class);
        startActivity(intent);
        finish();
    }

    private void clearPreferences() {
        sharedPrefsHelper.save(PreferenceKeys.PREF_USER_ID, "");
        sharedPrefsHelper.save(PreferenceKeys.PREF_COUNTRY_ID, "");
        sharedPrefsHelper.save(PreferenceKeys.PREF_USER_PHONE_NUMBER, "");
        sharedPrefsHelper.save(PreferenceKeys.PREF_PASSWORD, "");
        sharedPrefsHelper.save(PreferenceKeys.PREF_USER_NAME, "");
        sharedPrefsHelper.save(PreferenceKeys.PREF_USER_FIRST_NAME, "");
        sharedPrefsHelper.save(PreferenceKeys.PREF_EMAIL_ID, "");
        sharedPrefsHelper.save(PreferenceKeys.PREF_COUNTRY_ID, "");
        sharedPrefsHelper.save(PreferenceKeys.PREF_USER_IMAGE, "");
        sharedPrefsHelper.save(PreferenceKeys.PREF_HOME_PAGE_DATA, "");
        sharedPrefsHelper.save(PreferenceKeys.PREF_IMAGE_TAG, "");
        sharedPrefsHelper.save(PreferenceKeys.PREF_PROFILE_PAGE_DATA, "");

    }

    @Override
    public void userProfileError(Call<ProfileResponse> call, Throwable t) {

    }

    @Override
    public void onUserProfileSuccess(Response<ProfileResponse> response) {
        ProfileResponse body = response.body();
        if (body != null) {
            ProfileResponse.Profile profileData = body.getProfile();
            if (profileData != null) {
                mIsNotify = profileData.getUser().getIsNotify();
                mIsPrivate = profileData.getUser().getIsPrivate();
                updateProfileData();
            }

        } else {

        }
    }

    private void updateProfileData() {
        if (mIsPrivate != null && mIsPrivate.equals("false")) {
            privateOption.setChecked(false);
            // checked = false;
        } else {
            privateOption.setChecked(true);
            //checked = true;
        }

        if (mIsNotify != null && mIsNotify.equals("false")) {
            notificationOption.setChecked(false);
            // checked = false;
        } else {
            notificationOption.setChecked(true);
            //checked = true;
        }
    }

    private void updateProfileApi() {
        if (!Utilities.checkInternet(this)) {
            showToast(R.string.no_internet_msg);
        } else {
            SharedPrefsHelper sharedPrefsHelper = new SharedPrefsHelper(this);
            String userName = sharedPrefsHelper.get(PreferenceKeys.PREF_USER_NAME);
            String pass = sharedPrefsHelper.get(PreferenceKeys.PREF_PASSWORD);
            EditProfileInput editProfileInput = new EditProfileInput();
            editProfileInput.setWebsite("");
            editProfileInput.setUsername(userName);
            editProfileInput.setPassword(pass);
            editProfileInput.setIsPrivate(isPrivate + "");
            //editProfileInput.set(isNotify + "");
            editProfileInput.setDescription("");


            Gson gson = new Gson();
            String profileJson = gson.toJson(editProfileInput, EditProfileInput.class);
            Log.e("profile--", profileJson);

            EditProfilePresenter editProfilePresenter = new EditProfilePresenter(this, this);
            editProfilePresenter.updateProfile(editProfileInput);
        }
    }
}
