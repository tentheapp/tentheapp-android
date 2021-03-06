package com.nvcomputers.ten.api;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.output.ProfileResponse;
import com.nvcomputers.ten.utils.HttpUtils;
import com.nvcomputers.ten.views.core.BaseActivity;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

/**
 * This class is used to get the Accepted appointments response and return to VendorAppointmentsFragment
 *
 * @author jindaldipanshu
 * @version 1.0
 */

public class ProfileApi {
    private ProfileCallbacks profileCallbacks;
    private BaseActivity mActivity;

    public interface

    ProfileCallbacks extends AppCommonCallback {
        void onProfileResponse(ProfileResponse.Profile response);

        void profileError(String t);
    }


    public ProfileApi(BaseActivity activity, ProfileCallbacks profileCallbacks) {
        this.mActivity = activity;
        this.profileCallbacks = profileCallbacks;
    }

    public void responseCheck(String json) throws UnsupportedEncodingException { //input body-User//LoginInput loginInput
        //output body-LoginResponse
         profileCallbacks.setProgressBar();
        String url = GetRestAdapter.HOST_URL + "profile/" + json;
        // StringEntity params = new StringEntity(json);
        HttpUtils.get(mActivity, url, null, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onRetry(int retryNo) {
                super.onRetry(retryNo);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                profileCallbacks.dismiss();
                profileCallbacks.profileError("server error");
            }

            @Override
            public void onFinish() {
                super.onFinish();

                profileCallbacks.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                profileCallbacks.dismiss();
                profileCallbacks.profileError("server error");
            }

            @Override
            public void onUserException(Throwable error) {
                super.onUserException(error);
                profileCallbacks.dismiss();
                mActivity.showToast(error.getMessage());
            }

            @Override
            public void onCancel() {
                super.onCancel();
                profileCallbacks.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {JSONObject mainJsonObj = response.optJSONObject("profile");
                    ProfileResponse.Profile profileResponse = new ProfileResponse().new Profile();
                    profileResponse.setFollowingCount(mainJsonObj.optString("followingCount"));
                    profileResponse.setFollowersCount(mainJsonObj.optString("followersCount"));
                    profileResponse.setAllPostCount(mainJsonObj.optString("allPostCount"));
                    ProfileResponse.Profile.User user = new ProfileResponse().new Profile().new User();
                    JSONObject userObj = mainJsonObj.optJSONObject("user");
                    user.setDescription(userObj.optString("description"));
                    user.setUsername(userObj.optString("username"));
                    user.setContactName(userObj.optString("contactName"));
                    user.setFriended(userObj.optString("friended"));
                    user.setIdUser(userObj.optString("idUser"));
                    user.setFriended(userObj.optString("friended"));
                    user.setIsPrivate(userObj.optString("isPrivate"));
                    user.setIsNotify(userObj.optString("isNotify"));
                    user.setWebsite(userObj.optString("website"));
                    user.setBlocked(userObj.optString("blocked"));
                    profileResponse.setUser(user);
                    profileCallbacks.onProfileResponse(profileResponse);

                }
            }
        });

    }
}
