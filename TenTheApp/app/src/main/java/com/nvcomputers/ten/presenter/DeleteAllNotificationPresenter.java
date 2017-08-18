package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.input.DeleteAllNotificationInput;
import com.nvcomputers.ten.model.output.DeleteAllNotificationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Thaparsneh on 7/4/2017.
 */

public class DeleteAllNotificationPresenter {
    private DeleteNotificationsCallback deleteNotificationsCallback;
    private AppCommonCallback screen;

    public DeleteAllNotificationPresenter(AppCommonCallback callback, DeleteNotificationsCallback deleteNotificationsCallback) {
        this.screen = callback;
        this.deleteNotificationsCallback = deleteNotificationsCallback;
    }

    public interface DeleteNotificationsCallback {
        void deleteNotificationsError(Call<DeleteAllNotificationResponse> call, Throwable t);

        void onDeleteNotificationSuccess(Response<DeleteAllNotificationResponse> response);
    }

    public void responseCheck(DeleteAllNotificationInput idNotification) {
        //screen.setProgressBar();
        Call<DeleteAllNotificationResponse> response = GetRestAdapter.getRestAdapter(true).deleteAllNotifications(idNotification);
        response.enqueue(new Callback<DeleteAllNotificationResponse>() {
            @Override
            public void onResponse(Call<DeleteAllNotificationResponse> call, Response<DeleteAllNotificationResponse> response) {
                //screen.dismiss();
                deleteNotificationsCallback.onDeleteNotificationSuccess(response);
            }

            @Override
            public void onFailure(Call<DeleteAllNotificationResponse> call, Throwable t) {
                //screen.dismiss();
                deleteNotificationsCallback.deleteNotificationsError(call, t);
            }
        });
    }
}

