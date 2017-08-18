package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.model.input.EditProfileInput;
import com.nvcomputers.ten.model.output.EditProfileResponse;
import com.nvcomputers.ten.views.home.LandingActivity;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by jindaldipanshu on 5/2/2017.
 */

public class EditProfilePresenter {

    private final LandingActivity editProfileFragment;
    private final EditProfileCallbacks editProfileCallbacks;

    public interface EditProfileCallbacks {

    }

    public EditProfilePresenter(LandingActivity editProfileFragment, EditProfileCallbacks editProfileCallbacks) {
        this.editProfileFragment = editProfileFragment;
        this.editProfileCallbacks = editProfileCallbacks;
    }

    public void updateProfile(EditProfileInput editProfileInput) {
        editProfileFragment.showDialog();
        Call<EditProfileResponse> response = GetRestAdapter.getRestAdapter(true).updateProfile(editProfileInput);
        response.enqueue(new Callback<EditProfileResponse>() {
            @Override
            public void onResponse(Call<EditProfileResponse> call, retrofit2.Response<EditProfileResponse> response) {
                editProfileFragment.hideDialog();
                if (response != null && response.body() != null) {
                    if (response.body().getSuccess().equals("true")) {
                        editProfileFragment.showToast("Profile Updated");
                        //editProfileFragment.update();
                    } else {
                        editProfileFragment.showToast("Please wait until all active posts expire.");
                    }
                } else {
                    editProfileFragment.showToast("Please wait until all active posts expire.");
                }
            }

            @Override
            public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                editProfileFragment.hideDialog();
                editProfileFragment.showToast(t.getMessage());
            }
        });
    }
}
