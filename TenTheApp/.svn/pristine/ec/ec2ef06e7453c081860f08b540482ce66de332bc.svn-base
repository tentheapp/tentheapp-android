package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.output.NotificationLitResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Thaparsneh on 4/26/2017.
 */

public class NotificationListPresenter {
    private NotificationListCallback notificationListCallback;
    private AppCommonCallback screen;

    public NotificationListPresenter(AppCommonCallback callback, NotificationListCallback notificationListCallback) {
        this.screen = callback;
        this.notificationListCallback = notificationListCallback;
    }

    public interface NotificationListCallback {
        void notificationError(Call<NotificationLitResponse> call, Throwable t);

        void onNotificationSuccess(Response<NotificationLitResponse> response);
    }

    public void responseCheck(String userName, String password, String offset, String count) {
        screen.setProgressBar();
        Call<NotificationLitResponse> response = GetRestAdapter.getRestAdapter(true).notificationList(userName, password, offset, count);
        response.enqueue(new Callback<NotificationLitResponse>() {
            @Override
            public void onResponse(Call<NotificationLitResponse> call, Response<NotificationLitResponse> response) {
                screen.dismiss();
                notificationListCallback.onNotificationSuccess(response);
            }

            @Override
            public void onFailure(Call<NotificationLitResponse> call, Throwable t) {
                screen.dismiss();
                notificationListCallback.notificationError(call, t);
            }
        });
    }
}
