package com.nvcomputers.ten.views.notification;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nvcomputers.ten.R;
import com.nvcomputers.ten.adapter.NotificationListAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.input.DeleteAllNotificationInput;
import com.nvcomputers.ten.model.output.DeleteAllNotificationResponse;
import com.nvcomputers.ten.model.output.NewsFeedOutput;
import com.nvcomputers.ten.model.output.NotificationLitResponse;
import com.nvcomputers.ten.model.output.ReadNotificationResponse;
import com.nvcomputers.ten.model.output.RequestListResponse;
import com.nvcomputers.ten.presenter.DeleteAllNotificationPresenter;
import com.nvcomputers.ten.presenter.NotificationListPresenter;
import com.nvcomputers.ten.presenter.ReadNotificationPresenter;
import com.nvcomputers.ten.presenter.RequestListPresenter;
import com.nvcomputers.ten.utils.PreferenceKeys;
import com.nvcomputers.ten.utils.SharedPrefsHelper;
import com.nvcomputers.ten.utils.Utilities;
import com.nvcomputers.ten.views.core.BaseFragment;
import com.nvcomputers.ten.views.home.PostDetailFragment;
import com.nvcomputers.ten.views.profile.NewProfileFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Response;

public class NotificationFragment extends BaseFragment implements AppCommonCallback, NotificationListPresenter.NotificationListCallback, ReadNotificationPresenter.ReadNotificationCallback, View.OnClickListener, RequestListPresenter.RequestListCallback, DeleteAllNotificationPresenter.DeleteNotificationsCallback {
    private ImageView deleteBtn;
    private RecyclerView notificationRecyclerview;
    private LinearLayoutManager mLayoutManager;
    private SharedPrefsHelper sharedPrefsHelper;
    private ProgressBar progressBar;
    private NotificationListAdapter notificationListAdapter;
    private List<NotificationLitResponse.Notifications> mArrayList;
    private ProgressDialog progress;
    //private int counter = 0;
    private ImageView requestBtn;
    private List<RequestListResponse.Users> mRequestList;
    private int offset = 0, count = 0;
    int lastSavedPosition = 0;
    boolean noMoreData = false;
    int currentPageNumber = 1;
    private TextView noNotificationText;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Timer timer;
    private boolean isLoading;

    public NotificationFragment() {
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_notification;
    }

    @Override
    protected void initViews(View view) {
        //init views
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        sharedPrefsHelper = new SharedPrefsHelper(mContext);
        deleteBtn = (ImageView) view.findViewById(R.id.deleteBtn);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        // swipeRefreshLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(0);
                hitRequestListApi();
            }
        });
        notificationRecyclerview = (RecyclerView) view.findViewById(R.id.notification_rv);
        requestBtn = (ImageView) view.findViewById(R.id.requestBtn);
        //set recycler view orientation & layout
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        notificationRecyclerview.setLayoutManager(mLayoutManager);
        noNotificationText = (TextView) view.findViewById(R.id.noNotificationText);
        //set click listeners
        deleteBtn.setOnClickListener(this);
        requestBtn.setOnClickListener(this);
        //set button filter
        deleteBtn.setColorFilter(R.color.tenBlue);
        //get list of notificaitons
        mArrayList = new ArrayList<>();
        if (Utilities.checkInternet(mContext)) {
            loadData(0);
            hitRequestListApi();
        } else {
            showToast(R.string.no_internet_msg);
        }
    }

    public void loadData(int listCount) {
        if (!isLoading) {
            isLoading = true;
            if (listCount == 0) {
                currentPageNumber = 1;
                lastSavedPosition = 0;
                noMoreData = false;
                mArrayList = new ArrayList<>();
                hitNotificationApi(listCount);
            } else {
                lastSavedPosition = listCount - 1;
                if (listCount >= 10 && !noMoreData && listCount % 10 == 0) {
                    int value = listCount / 10;
                    currentPageNumber = value + 1;
                    hitNotificationApi(listCount);
                } else {
                    progressBar.setVisibility(View.GONE);
                    isLoading = false;
                    //ProgressUtility.dismissProgress();
                }
            }
        }
    }

    public void hitRequestListApi() {
        String username = sharedPrefsHelper.get(PreferenceKeys.PREF_USER_NAME, "");
        String password = sharedPrefsHelper.get(PreferenceKeys.PREF_PASSWORD, "");
        RequestListPresenter requestListPresenter = new RequestListPresenter(this, this);
        requestListPresenter.responseCheck(username, password);
    }

    private void hitNotificationApi(int offset) {

        String offSet = String.valueOf(offset);
        String username = sharedPrefsHelper.get(PreferenceKeys.PREF_USER_NAME, "");
        String password = sharedPrefsHelper.get(PreferenceKeys.PREF_PASSWORD, "");
        NotificationListPresenter notificationListPresenter = new NotificationListPresenter(this, this);
        notificationListPresenter.responseCheck(username, password, offSet, "10");

    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("onResume--", "notification--");
        if (Utilities.checkInternet(mContext)) {
            loadData(0);
            hitRequestListApi();
        } else {
            showToast(R.string.no_internet_msg);
        }
    }


    @Override
    public void dispose() {
        cancelTimer();
    }

    @Override
    public void setProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismiss() {
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void notificationError(Call<NotificationLitResponse> call, Throwable t) {
        deleteBtn.setVisibility(View.GONE);
        noNotificationText.setVisibility(View.VISIBLE);
        // Utilities.showMessage(mContext, getString(R.string.server_error_msg));
        swipeRefreshLayout.setRefreshing(false);
        isLoading = false;
    }

    @Override
    public void onNotificationSuccess(Response<NotificationLitResponse> response) {
        isLoading = false;
        NotificationLitResponse body = response.body();
        swipeRefreshLayout.setRefreshing(false);
        if (body != null) {
            if (body.getNotifications() != null && body.getNotifications().size() > 0)
                mArrayList.addAll(body.getNotifications());
            if (mArrayList != null && mArrayList.size() > 0) {
                deleteBtn.setVisibility(View.VISIBLE);
                notificationListAdapter = new NotificationListAdapter(this, mArrayList, linearLayoutManager);
                notificationRecyclerview.setAdapter(notificationListAdapter);
                noNotificationText.setVisibility(View.GONE);
            } else {
                deleteBtn.setVisibility(View.GONE);
                noNotificationText.setVisibility(View.VISIBLE);
                mArrayList = new ArrayList<>();
                //Utilities.showSmallToast(getActivity(), "No notification");
                notificationListAdapter = new NotificationListAdapter(this, mArrayList, linearLayoutManager);
                notificationRecyclerview.setAdapter(notificationListAdapter);
            }
        } else {
            deleteBtn.setVisibility(View.GONE);
            noNotificationText.setVisibility(View.VISIBLE);
            // Utilities.showMessage(mContext, getString(R.string.server_error_msg));
        }
        cancelTimer();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //checkPostExpiration(body);
                if (mArrayList != null && mArrayList.size() > 0) {
                    updateListAdatper();
                }
            }
        }, 10000, 10000);
    }

    private void updateListAdatper() {
        if (notificationListAdapter == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notificationListAdapter.updateList();
            }
        });
    }

    private void cancelTimer() {
        if (timer != null) {
            try {
                timer.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.requestBtn:
                replaceChildFragment(R.id.notification_frame_layout, new RequestListFragment(), null);
                break;
            case R.id.deleteBtn:
                if (Utilities.checkInternet(mContext)) {
                    if (mArrayList != null && mArrayList.size() > 0) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle(R.string.clear_notification);
                        builder.setMessage(R.string.clear_notification_message);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                markAllDelete();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();
                    } else {
                        Utilities.showMessage(getActivity(), "No notification to delete");
                    }

                } else {
                    Utilities.showMessage(mContext, getString(R.string.no_internet_msg));
                }
                break;
        }

    }

    private void markAllDelete() {
        progress = new ProgressDialog(mContext);
        progress.setIndeterminate(true);
        progress.setMessage(getString(R.string.clearAll));
        progress.setCancelable(false);
        progress.show();
        //counter = mArrayList.size();
        /*for (int i = mArrayList.size() - 1; i >= 0; i--) {
            String notificationId = mArrayList.get(i).getIdNotification();
            ReadNotificationPresenter readNotificationPresenter = new ReadNotificationPresenter(this, this);
            readNotificat'

            ionPresenter.responseCheck(notificationId);
        }*/
        List<String> notificationList = new ArrayList<>();
        for (int i = 0; i < mArrayList.size(); i++) {
            String notificationId = mArrayList.get(i).getIdNotification();
            if (notificationId.equals("0")) {
                String[] groupIds = mArrayList.get(i).getNotificationIds();
                for (int j = 0; j < groupIds.length; j++) {
                    String singleNotificaton = groupIds[j];
                    notificationList.add(singleNotificaton);
                }
            } else {
                notificationList.add(notificationId);
            }
        }
        String[] notificationArray = new String[notificationList.size()];
        for (int i = 0; i < notificationList.size(); i++) {
            notificationArray[i] = notificationList.get(i);
        }
        DeleteAllNotificationInput deleteAllNotificationInput = new DeleteAllNotificationInput();
        deleteAllNotificationInput.setNotificationIds(notificationArray);
        DeleteAllNotificationPresenter deleteAllNotificationPresenter = new DeleteAllNotificationPresenter(this, this);
        deleteAllNotificationPresenter.responseCheck(deleteAllNotificationInput);

    }

    @Override
    public void readNotificationError(Call<ReadNotificationResponse> call, Throwable t) {
        try {
            //counter--;
            //if (counter == 0) {
            progress.dismiss();
            //}
        } catch (Exception e) {
            e.printStackTrace();
            progress.dismiss();
        }
    }

    @Override
    public void onReadNotificationSuccess(Response<ReadNotificationResponse> response) {
        try {
            loadData(0);
            progress.dismiss();
            /*counter--;
            if (counter == 0) {

                mArrayList = new ArrayList<>();
                notificationListAdapter = new NotificationListAdapter(this, mArrayList, linearLayoutManager);
                notificationRecyclerview.setAdapter(notificationListAdapter);
                hitNotificationApi(0);
            } else {
                notificationListAdapter = new NotificationListAdapter(this, mArrayList, linearLayoutManager);
                notificationRecyclerview.setAdapter(notificationListAdapter);
                hitNotificationApi(0);
            }*/
        } catch (Exception e) {
            //progress.dismiss();
            e.printStackTrace();
        }
    }

    public void moveToProfile(String username, String notificationId) {
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        replaceChildFragment(R.id.notification_frame_layout, new NewProfileFragment(), bundle);
        //markReadNotification(notificationId);
    }

    public void screenNaigations(ArrayList<NewsFeedOutput.Posts> notificationPostList, Bundle bundle, int clickedPosition) {
        String type = bundle.getString("type");
        String notificationId = bundle.getString("notificationId");
        markReadNotification(notificationId);
        if (type.contains("COMMENT")) {
            Bundle bundle1 = new Bundle();
            bundle1.putParcelableArrayList(PreferenceKeys.PREF_HOME_PAGE_DATA, notificationPostList);
            bundle1.putInt("position", clickedPosition);
            replaceChildFragment(R.id.notification_frame_layout, new PostDetailFragment(), bundle1);
        } else if (type.contains("LIKE")) {
            Bundle bundle1 = new Bundle();
            bundle1.putParcelableArrayList(PreferenceKeys.PREF_HOME_PAGE_DATA, notificationPostList);
            bundle1.putInt("position", clickedPosition);
            replaceChildFragment(R.id.notification_frame_layout, new PostDetailFragment(), bundle1);
        } else if (type.contains("FRIENDREQUEST")) {
            replaceChildFragment(R.id.notification_frame_layout, new NewProfileFragment(), bundle);
        } else if (type.contains("FOLLWERSPOST")) {
            Bundle bundle1 = new Bundle();
            bundle1.putParcelableArrayList(PreferenceKeys.PREF_HOME_PAGE_DATA, notificationPostList);
            bundle1.putInt("position", clickedPosition);
            replaceChildFragment(R.id.notification_frame_layout, new PostDetailFragment(), bundle1);
        } else if (type.contains("MENTIONPOST")) {
            Bundle bundle1 = new Bundle();
            bundle1.putParcelableArrayList(PreferenceKeys.PREF_HOME_PAGE_DATA, notificationPostList);
            bundle1.putInt("position", clickedPosition);
            replaceChildFragment(R.id.notification_frame_layout, new PostDetailFragment(), bundle1);
        } else {
            replaceChildFragment(R.id.notification_frame_layout, new NewProfileFragment(), bundle);
        }

    }

    private void markReadNotification(String notificationId) {
        ReadNotificationPresenter readNotificationPresenter = new ReadNotificationPresenter(this, this);
        readNotificationPresenter.responseCheck(notificationId);
    }

    @Override
    public void requestListError(Call<RequestListResponse> call, Throwable t) {

    }

    @Override
    public void onRequestListSuccess(Response<RequestListResponse> response) {
        RequestListResponse body = response.body();
        if (body != null) {
            List<RequestListResponse.Users> userData = body.getUsers();
            if (userData != null || userData.size() >= 0) {
                mRequestList = userData;
                if (mRequestList != null && mRequestList.size() > 0) {
                    requestBtn.setVisibility(View.VISIBLE);
                } else {
                    requestBtn.setVisibility(View.GONE);
                }
            } else {
                Utilities.showMessage(getActivity(), getString(R.string.server_error_msg));
            }
        }
    }

    public void scrollUp() {
        if (notificationListAdapter != null && notificationListAdapter.getItemCount() > 0) {
            notificationRecyclerview.smoothScrollToPosition(0);
        }
    }

    @Override
    public void deleteNotificationsError(Call<DeleteAllNotificationResponse> call, Throwable t) {
        progress.dismiss();
        Utilities.showMessage(mContext, getString(R.string.server_error_msg));
    }

    @Override
    public void onDeleteNotificationSuccess(Response<DeleteAllNotificationResponse> response) {
        progress.dismiss();
        DeleteAllNotificationResponse body = response.body();
        if (body != null) {
            String status = body.getSuccess();
            if (status.equals("true")) {
                loadData(0);
            } else {
                Utilities.showMessage(mContext, getString(R.string.server_error_msg));
            }
        } else {
            Utilities.showMessage(mContext, getString(R.string.server_error_msg));
        }
    }
}
