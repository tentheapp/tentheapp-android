package com.nvcomputers.ten.views.popular;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nvcomputers.ten.R;

import com.nvcomputers.ten.adapter.NewsPostAdapter;
import com.nvcomputers.ten.adapter.PopularTimerAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.output.NewsFeedOutput;
import com.nvcomputers.ten.model.output.PopularTimersResponse;
import com.nvcomputers.ten.presenter.PopularPostsPresenter;
import com.nvcomputers.ten.presenter.PopularTimersPresenter;
import com.nvcomputers.ten.utils.SharedPrefsHelper;
import com.nvcomputers.ten.utils.Utilities;
import com.nvcomputers.ten.views.core.BaseFragment;
import com.nvcomputers.ten.views.profile.NewProfileFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class PopularFragment extends BaseFragment implements AppCommonCallback, View.OnClickListener, PopularTimersPresenter.PopularTimersCallback, PopularPostsPresenter.PopularPostCallback, AdapterView.OnItemClickListener {

    private static final int PLUS_ONE_REQUEST_CODE = 0;
    private RecyclerView postsRecyclerview;
    private GridView gridViewFeeds;
    private LinearLayoutManager mLayoutManager;
    private SharedPrefsHelper sharedPrefsHelper;
    private ProgressDialog progress;
    private FrameLayout popularLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button timersButton2, timedButton1;
    private ImageView localButton, globalButton;
    public String Scope = "local", Type = "timed";
    Context context;
    private List<NewsFeedOutput.Posts> popularPostsOutput;
    private PopularTimersResponse.Users popularTimersOutput[];
    private PopularTimersResponse.Users[] users;
    private LinearLayoutManager manager;
    private NewsPostAdapter adapter;
    private ProgressBar popular_progress;

    public PopularFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_popular_tab;
    }

    @Override
    protected void initViews(View view) {
        //init views
        context = this.getActivity();
        getBaseActivity().setmCurrentFragment(this);
        popular_progress = (ProgressBar) view.findViewById(R.id.popular_progress);
        localButton = (ImageView) view.findViewById(R.id.local);
        globalButton = (ImageView) view.findViewById(R.id.global);
        timersButton2 = (Button) view.findViewById(R.id.timers);
        popularLayout = (FrameLayout) view.findViewById(R.id.popularLayout);
        timedButton1 = (Button) view.findViewById(R.id.timed);
        postsRecyclerview = (RecyclerView) view.findViewById(R.id.recyclerViewPosts);
        manager = new LinearLayoutManager(mContext);
        postsRecyclerview.setLayoutManager(manager);
        gridViewFeeds = (GridView) view.findViewById(R.id.gridViewFeeds);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!Utilities.checkInternet(context)) {
                    showToast(R.string.no_internet_msg);
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    swipeRefreshLayout.setRefreshing(true);
                    showSelectedPage();
                }
            }
        });
        timersButton2.setOnClickListener(this);
        popularLayout.setOnClickListener(this);
        timedButton1.setOnClickListener(this);
        localButton.setOnClickListener(this);
        globalButton.setOnClickListener(this);
        sharedPrefsHelper = new SharedPrefsHelper(mContext);

        gridViewFeeds.setOnItemClickListener(this);
        Scope = "local";
        Type = "timed";
        showSelectedPage();
    }

    private void loadPopularTimersData(String type, String locOrGlobalValue) {
        postsRecyclerview.setVisibility(View.GONE);
        gridViewFeeds.setVisibility(View.VISIBLE);
        if (!Utilities.checkInternet(context)) {
            showToast(R.string.no_internet_msg);
        } else {
            PopularTimersPresenter presenter = new PopularTimersPresenter(this, this);
            presenter.hitPopularTimersApi(type, locOrGlobalValue);
        }
    }

    private void loadPostsData(String type, String locOrGlobalValue) {
        postsRecyclerview.setVisibility(View.VISIBLE);
        gridViewFeeds.setVisibility(View.GONE);
        if (!Utilities.checkInternet(context)) {
            showToast(R.string.no_internet_msg);
        } else {
            PopularPostsPresenter presenter = new PopularPostsPresenter(this, this);
            presenter.hitPopularPostsApi(type, locOrGlobalValue);
        }
    }

    public void showSelectedPage() {
        popular_progress.setVisibility(View.VISIBLE);
        if (Type == "timer") {
            loadPopularTimersData(Type, Scope);
        } /*else if (Scope == "global" && Type == "timer") {
            loadPopularTimersData(Type, Scope);
        } else if (Scope == "local" && Type == "timed") {
            loadPostsData(Type, Scope);
        }*/ else if (Type == "timed") {
            loadPostsData(Type, Scope);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void dispose() {

    }

    @Override
    public void setProgressBar() {
        // progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void dismiss() {
        //  progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.popularLayout:
                Log.d("Popular Layout Clicked ", "");
                break;
            case R.id.local:
                localButton.setImageDrawable(context.getResources().getDrawable(R.drawable.localselected));
                globalButton.setImageDrawable(context.getResources().getDrawable(R.drawable.global));
                Scope = "local";
                showSelectedPage();
                break;
            case R.id.global:
                localButton.setImageDrawable(context.getResources().getDrawable(R.drawable.local));
                globalButton.setImageDrawable(context.getResources().getDrawable(R.drawable.globalselected));
                Scope = "global";
                showSelectedPage();
                break;
            //tab 1
            case R.id.timed:
                timedButton1.setBackground(context.getResources().getDrawable(R.drawable.left_segmented_control_selected));
                timedButton1.setTextColor(Color.WHITE);
                timersButton2.setBackground(context.getResources().getDrawable(R.drawable.right_segmented_control));
                timersButton2.setTextColor(Color.parseColor("#338a9e"));
                Type = "timed";
                showSelectedPage();
                break;
            //tab 2
            case R.id.timers:
                timedButton1.setBackground(context.getResources().getDrawable(R.drawable.left_segmented_control));
                timedButton1.setTextColor(Color.parseColor("#338a9e"));
                timersButton2.setBackground(context.getResources().getDrawable(R.drawable.right_segmented_control_selected));
                timersButton2.setTextColor(Color.WHITE);
                Type = "timer";
                showSelectedPage();
                break;


        }

    }

    //popular Posts Methods
    @Override
    public void onError(Call<NewsFeedOutput> call, Throwable t) {
        hideDialog();
        popular_progress.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(Response<NewsFeedOutput> response) {
        swipeRefreshLayout.setRefreshing(false);
        popular_progress.setVisibility(View.GONE);
        if (response != null) {
            NewsFeedOutput body = response.body();
            if (body != null) {
                List<NewsFeedOutput.Posts> posts = body.getPosts();
                if (posts != null && posts.size() > 0) {
                    popularPostsOutput = posts;
                    adapter = new NewsPostAdapter(this, posts, R.id.popularLayout, manager);
                    postsRecyclerview.setAdapter(adapter);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                } else {
                    swipeRefreshLayout.setVisibility(View.GONE);
                }
            }
        }
    }


    @Override
    public void reLoadList() {
        super.reLoadList();
        showSelectedPage();
    }

    @Override
    public void onTimersError(Call<PopularTimersResponse> call, Throwable t) {
        hideDialog();
        popular_progress.setVisibility(View.GONE);
    }

    @Override
    public void onTimersSuccess(Response<PopularTimersResponse> response) {
        popular_progress.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        if (response != null) {
            PopularTimersResponse body = response.body();
            if (body != null) {
                users = body.getUsers();
                if (users != null && users.length > 0) {
                    popularTimersOutput = users;
                    //set adapter for gridview
                    PopularTimerAdapter popularTimerAdapter = new PopularTimerAdapter(getActivity(), users);
                    gridViewFeeds.setAdapter(popularTimerAdapter);

                } else {
                    swipeRefreshLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String username = users[position].getUsername();
        Bundle b = new Bundle();
        b.putString("username", username);
        b.putBoolean("isfrompopularfragment", true);
        replaceChildFragment(R.id.popularLayout, new NewProfileFragment(), b);
    }

    public void scrollUp() {
        if (adapter != null && adapter.getItemCount() > 0) {
            postsRecyclerview.smoothScrollToPosition(0);
        }
    }
}
