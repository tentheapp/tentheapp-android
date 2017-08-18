package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.output.GroupedNotificationDetailResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Thaparsneh on 7/4/2017.
 */

public class GroupedNotificationDetailPresenter {
    private GroupedNotificationDetailCallback groupedNotificationDetailCallback;
    private AppCommonCallback screen;

    public GroupedNotificationDetailPresenter(AppCommonCallback callback, GroupedNotificationDetailCallback groupedNotificationDetailCallback) {
        this.screen = callback;
        this.groupedNotificationDetailCallback = groupedNotificationDetailCallback;
    }

    public interface GroupedNotificationDetailCallback {
        void groupedNotificationError(Call<GroupedNotificationDetailResponse> call, Throwable t);

        void onGroupedNotificationSuccess(Response<GroupedNotificationDetailResponse> response);
    }

    public void responseCheck(String userName, String password) {
        screen.setProgressBar();
        Call<GroupedNotificationDetailResponse> response = GetRestAdapter.getRestAdapter(true).groupedNotification(userName, password);
        response.enqueue(new Callback<GroupedNotificationDetailResponse>() {
            @Override
            public void onResponse(Call<GroupedNotificationDetailResponse> call, Response<GroupedNotificationDetailResponse> response) {
                screen.dismiss();
                groupedNotificationDetailCallback.onGroupedNotificationSuccess(response);
            }

            @Override
            public void onFailure(Call<GroupedNotificationDetailResponse> call, Throwable t) {
                screen.dismiss();
                groupedNotificationDetailCallback.groupedNotificationError(call, t);
            }
        });
    }
}
