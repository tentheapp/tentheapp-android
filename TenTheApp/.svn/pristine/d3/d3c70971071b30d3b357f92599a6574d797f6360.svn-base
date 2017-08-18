package com.nvcomputers.ten.views.search;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nvcomputers.ten.R;
import com.nvcomputers.ten.adapter.NewsPostAdapter;
import com.nvcomputers.ten.adapter.SearchAdapter;
import com.nvcomputers.ten.model.output.NewsFeedOutput;
import com.nvcomputers.ten.model.output.SearchUsersResponse;
import com.nvcomputers.ten.presenter.SearchPresenter;
import com.nvcomputers.ten.utils.Utilities;
import com.nvcomputers.ten.views.core.BaseFragment;

import java.util.ArrayList;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;

/**
 * Created by jindaldipanshu on 5/3/2017.
 */

public class SearchFragment extends BaseFragment implements View.OnClickListener, EditText.OnEditorActionListener, SearchPresenter.SearchCallbacks, TextWatcher {
    private RecyclerView mSearchRecylerView;
    private EditText mSearchEditText;
    private TextView mUsersTextView, mTagsTextView;
    private boolean mHashtag;
    private SearchPresenter mSearchPresenter;
    private ArrayList<NewsFeedOutput.Posts> mSerarchedHashList = new ArrayList<>();
    private ArrayList<SearchUsersResponse.Users> msearchedUserList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initViews(View view) {

        mSearchRecylerView = (RecyclerView) findViewById(R.id.recycler_view_search);
        mSearchEditText = (EditText) findViewById(R.id.edit_text_search);
        mUsersTextView = (TextView) findViewById(R.id.text_view_users);
        mTagsTextView = (TextView) findViewById(R.id.text_view_tags);
        findViewById(R.id.frame_layout_search);
        findViewById(R.id.tv_back).setOnClickListener(this);
        findViewById(R.id.im_view_back).setOnClickListener(this);
        mUsersTextView.setOnClickListener(this);
        mTagsTextView.setOnClickListener(this);
        mSearchEditText.setOnEditorActionListener(this);

        mSearchEditText.addTextChangedListener(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mHashtag = true;
            mSearchEditText.setText(bundle.getString("hashTag"));
            mUsersTextView.setTypeface(null, Typeface.NORMAL);
            mTagsTextView.setTypeface(null, Typeface.BOLD);
            callSearchApi();
        }
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.search_parent);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // leave it empty
            }
        });

        mSearchRecylerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_DRAGGING) {
                    Utilities.hideKeypad(recyclerView);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public void dispose() {
        if (mSearchPresenter != null)
            mSearchPresenter = null;
    }

    @Override
    public void onClick(View view) {
        if (Utilities.checkInternet(mContext)) {
            int id = view.getId();
            Utilities.hideKeypad(view);
            if (id == R.id.im_view_back) {
                manualBackPressed();
            } else if (id == R.id.text_view_users) {
                mHashtag = false;
                mUsersTextView.setTypeface(null, Typeface.BOLD);
                mTagsTextView.setTypeface(null, Typeface.NORMAL);
                setAdapter(mSearchRecylerView, msearchedUserList);
            } else if (id == R.id.text_view_tags) {
                mHashtag = true;
                mTagsTextView.setTypeface(null, Typeface.BOLD);
                mUsersTextView.setTypeface(null, Typeface.NORMAL);
                setAdapter(mSearchRecylerView, mSerarchedHashList);
            }
        } else {
            Utilities.showMessage(mContext, getString(R.string.no_internet_msg));
        }

    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_SEND) {
                    /* handle action here */
            Utilities.hideKeypad(textView);
            mSearchEditText.clearFocus();
            callSearchApi();
            handled = true;
        }
        return handled;
    }

    private void callSearchApi() {
        setAdapter(mSearchRecylerView, null);
        String searh = mSearchEditText.getText().toString();
        if (!TextUtils.isEmpty(searh)) {
            if (!Utilities.checkInternet(getBaseActivity())) {
                showToast(R.string.no_internet_msg);
            } else {
                if (mSearchPresenter == null)
                    mSearchPresenter = new SearchPresenter(getBaseActivity(), this);
                //if (mHashtag) {
                mSearchPresenter.getHashTagResults(searh);
                //} else {
                mSearchPresenter.getUserResults(searh);
                //}
            }

        }

    }

    @Override
    public void setAdapter(RecyclerView recyclerView, ArrayList<?> mList) {
        super.setAdapter(recyclerView, mList);
        if (mHashtag) {
            NewsPostAdapter adapter = new NewsPostAdapter(this, mList, R.id.frame_layout_search, linearLayoutManager);
            mSearchRecylerView.setAdapter(adapter);
        } else {
            SearchAdapter searchAdapter = new SearchAdapter(this, mList);
            mSearchRecylerView.setAdapter(searchAdapter);
        }
    }

    @Override
    public void onUsersSearchResult(SearchUsersResponse searchUsersResponse) {
        if (searchUsersResponse.getUsers() != null && searchUsersResponse.getUsers().size() > 0) {
            msearchedUserList = searchUsersResponse.getUsers();
        } else {
            msearchedUserList = new ArrayList<>();
            //showToast(R.string.no_data_found_msg);
        }
        if (!mHashtag) {
            setAdapter(mSearchRecylerView, msearchedUserList);
        }
    }

    @Override
    public void onHashTagResult(NewsFeedOutput searchUsersResponse) {
        if (searchUsersResponse.getPosts() != null && searchUsersResponse.getPosts().size() > 0) {
            mSerarchedHashList = searchUsersResponse.getPosts();
            //setAdapter(mSearchRecylerView, mSerarchedHashList);
        } else {
            mSerarchedHashList = new ArrayList<>();
            //showToast(R.string.no_data_found_msg);
        }
        if (mHashtag) {
            setAdapter(mSearchRecylerView, mSerarchedHashList);
        }
    }

    @Override
    public void reLoadList() {
        super.reLoadList();
        callSearchApi();
    }

    String boforeText = "";

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        boforeText = charSequence.toString();
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (boforeText.length() < editable.length()) {
            callSearchApi();
        }
    }
}
