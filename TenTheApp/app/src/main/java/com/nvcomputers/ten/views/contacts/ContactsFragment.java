package com.nvcomputers.ten.views.contacts;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nvcomputers.ten.R;
import com.nvcomputers.ten.adapter.InviteTenUsersAdapter;
import com.nvcomputers.ten.adapter.SearchTenUsersAdapter;
import com.nvcomputers.ten.adapter.TenUsersAdapter;
import com.nvcomputers.ten.api.ContactsApi;
import com.nvcomputers.ten.model.input.ContactsSyncInput;
import com.nvcomputers.ten.model.output.ContactsModel;
import com.nvcomputers.ten.model.output.TenUsersResponse;
import com.nvcomputers.ten.presenter.ContactsPresenter;
import com.nvcomputers.ten.sort.TenUsersSort;
import com.nvcomputers.ten.utils.Utilities;
import com.nvcomputers.ten.views.core.BaseFragment;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jindaldipanshu on 5/1/2017.
 */

public class ContactsFragment extends BaseFragment implements ContactsPresenter.ContactsCallback, ContactsApi.UsersCallback, View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 5;
    private TextView mSearchTextView;
    private RecyclerView mTenUsersRecyclerView, mNotTenUsersRecyclerView, mSearchRecyclerView;
    public ArrayList<TenUsersResponse.User> mTenUserList, mInViteUserList, mSearchUserList;
    private SearchView mContactsSearchView;
    private SearchView.OnQueryTextListener listener;
    private RelativeLayout mTenUserLayout;
    private ArrayList<TenUsersResponse.User> filterList;
    private ContactsPresenter contactsPresenter;
    private ProgressBar mTenUserProgressbar, mNotTenUsersProgressbar;
    private ContactsApi mContactsApi;
    private Toolbar mAddressToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.fragments_contacts;
    }


    @Override
    protected void initViews(View view) {

        findViewById(R.id.frame_layout_contacts);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_settings).setOnClickListener(this);
        findViewById(R.id.tv_done).setOnClickListener(this);
        findViewById(R.id.frag_contact_parent).setOnClickListener(this);
        mTenUserProgressbar = (ProgressBar) findViewById(R.id.progress_bar_ten_users);
        mNotTenUsersProgressbar = (ProgressBar) findViewById(R.id.progress_bar_not_ten_users);
        mSearchTextView = (TextView) findViewById(R.id.text_view_search);
        mSearchTextView.setVisibility(View.VISIBLE);
        mContactsSearchView = (SearchView) findViewById(R.id.search_view_Address);
        mTenUsersRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_ten_users);
        mNotTenUsersRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_not_ten_users);
        mSearchRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_search);
        mSearchRecyclerView.setVisibility(View.GONE);
        mTenUserLayout = (RelativeLayout) findViewById(R.id.rl_ten);
        mTenUserLayout.setVisibility(View.VISIBLE);
        mTenUserList = new ArrayList<>();
        mInViteUserList = new ArrayList<>();
        mSearchUserList = new ArrayList<>();


        mContactsSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mTenUserLayout.setVisibility(View.VISIBLE);
                mSearchRecyclerView.setVisibility(View.GONE);
                mSearchTextView.setVisibility(View.VISIBLE);
                callApi();
                return false;
            }
        });

        listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList = new ArrayList<>();
                if (query != null) {
                    filterList = getSearchList(query);
                    if (filterList != null && filterList.size() > 0) {
                        Collections.sort(filterList, new TenUsersSort());

                        setAdapter(mSearchRecyclerView, filterList);
                    } else {
                        setAdapter(mSearchRecyclerView, null);
                    }
                } else {
                    if (mSearchUserList != null && mSearchUserList.size() > 0) {
                        Collections.sort(mSearchUserList, new TenUsersSort());
                    }
                    setAdapter(mSearchRecyclerView, mSearchUserList);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("onQueryTextChange", newText);
                return true;
            }
        };
        mContactsSearchView.setOnQueryTextListener(listener);

        mContactsSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchTextView.setVisibility(View.GONE);
                mTenUserLayout.setVisibility(View.GONE);
                mSearchRecyclerView.setVisibility(View.VISIBLE);
                setSearchUserList();
            }
        });

        checkRunTimePersmission();
    }

    private void checkRunTimePersmission() {
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        if (sdkVersion >= 23) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(getBaseActivity(),
                    Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getBaseActivity(),
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            } else {
                getContacts();
            }
        } else {
            getContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getContacts();
                } else {
                    showToast("Please allow to read contacts");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void getContacts() {
        mTenUserProgressbar.setVisibility(View.VISIBLE);
        mNotTenUsersProgressbar.setVisibility(View.VISIBLE);

        contactsPresenter = new ContactsPresenter(getBaseActivity(), this);
        showDialog();
        Thread thread = new Thread() {
            public void run() {

                contactsPresenter.getContacts();
            }
        };
        thread.start();
    }


    public void callApi() {
        if (!Utilities.checkInternet(getBaseActivity())) {
            showToast(R.string.no_internet_msg);
        } else {
            if (mContactsApi == null) {
                mContactsApi = new ContactsApi(getBaseActivity(), this);
            }
            mContactsApi.getTenUsers();
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public void setAdapter(RecyclerView recyclerView, ArrayList<?> mList) {
        super.setAdapter(recyclerView, mList);
        if (recyclerView == mTenUsersRecyclerView) {
            TenUsersAdapter tenUsersAdapter = new TenUsersAdapter(this, mList, this);
            recyclerView.setAdapter(tenUsersAdapter);
        } else if (recyclerView == mNotTenUsersRecyclerView) {
            InviteTenUsersAdapter tenUsersAdapter = new InviteTenUsersAdapter(getBaseActivity(), mList, this);
            recyclerView.setAdapter(tenUsersAdapter);
        } else if (recyclerView == mSearchRecyclerView) {
            SearchTenUsersAdapter searchTenUsersAdapter = new SearchTenUsersAdapter(this, mList);
            recyclerView.setAdapter(searchTenUsersAdapter);
        }
    }

    public void notifyInviteList(int position) {
        mInViteUserList.get(position).setStatus("1");
    }

    public void notifyTenUsersList(int position, int friended) {
        mTenUserList.get(position).setFriended(getString(friended));
    }

    @Override
    public void setTenUser(ArrayList<?> mList) {
        mTenUserProgressbar.setVisibility(View.INVISIBLE);
        mTenUserList = (ArrayList<TenUsersResponse.User>) mList;
        if (mTenUserList != null && mTenUserList.size() > 0) {
            Collections.sort(mTenUserList, new TenUsersSort());
        }
        setAdapter(mTenUsersRecyclerView, mTenUserList);
    }

    @Override
    public void setTenUsersError(String message) {
        mTenUserProgressbar.setVisibility(View.INVISIBLE);
        showToast(message);
    }

    @Override
    public void setInviteUsersError(String message) {
        mNotTenUsersProgressbar.setVisibility(View.INVISIBLE);
        showToast(message);
    }

    @Override
    public void setInViteUser(ArrayList<?> mList) {
        mNotTenUsersProgressbar.setVisibility(View.INVISIBLE);
        mInViteUserList = (ArrayList<TenUsersResponse.User>) mList;
        if (mInViteUserList != null && mInViteUserList.size() > 0) {
            Collections.sort(mInViteUserList, new TenUsersSort());
        }

        setAdapter(mNotTenUsersRecyclerView, mInViteUserList);
    }

    public void setSearchUserList() {
        ArrayList<TenUsersResponse.User> tempList = (ArrayList<TenUsersResponse.User>) mTenUserList.clone();
        mSearchUserList = tempList;
        for (int i = 0; i < mInViteUserList.size(); i++) {
            TenUsersResponse.User user = new TenUsersResponse().new User();
            user.setFriended("");
            user.setUsername(mInViteUserList.get(i).getUsername());
            user.setPhone(mInViteUserList.get(i).getPhone());
            user.setStatus(mInViteUserList.get(i).getStatus());
            user.setIdUser(mInViteUserList.get(i).getIdUser());
            mSearchUserList.add(user);
        }
        if (mSearchUserList != null && mSearchUserList.size() > 0) {
            Collections.sort(mSearchUserList, new TenUsersSort());
        }
        setAdapter(mSearchRecyclerView, mSearchUserList);
    }

    /**
     * This method is used to get Search users list
     *
     * @param input:type input
     * @return ArrayList<TenUsersResponse.User>
     */
    private ArrayList<TenUsersResponse.User> getSearchList(String input) {
        ArrayList<TenUsersResponse.User> mFilterUserList = new ArrayList<>();
        if (mSearchUserList != null && mSearchUserList.size() > 0) {
            for (int i = 0; i < mSearchUserList.size(); i++) {
                String usernm = mSearchUserList.get(i).getUsername().toLowerCase();
                String number = mSearchUserList.get(i).getPhone();

                if (usernm.contains(input.toLowerCase()) || number.contains(input)) {
                    TenUsersResponse.User user = new TenUsersResponse().new User();
                    user.setFriended(mSearchUserList.get(i).getFriended());
                    user.setUsername(mSearchUserList.get(i).getUsername());
                    user.setContactName(mSearchUserList.get(i).getContactName());
                    user.setPhone(mSearchUserList.get(i).getPhone());
                    user.setStatus(mSearchUserList.get(i).getStatus());
                    user.setIdUser(mSearchUserList.get(i).getIdUser());
                    mFilterUserList.add(user);
                }
            }
        }
        return mFilterUserList;
    }


    @Override
    public void allContacts(ArrayList<ContactsModel> contactsModels) {
        String phonenNumber = "";
        String userName = "";
        for (int i = 0; i < contactsModels.size(); i++) {
            String number = contactsModels.get(i).getContactNumber();
            String name = contactsModels.get(i).getContactName();
            if (TextUtils.isEmpty(number)) {
                continue;
            }

            if (number.contains(";")) {
                String numberToken[] = number.split(";");
                for (int j = 0; j < numberToken.length; j++) {
                    phonenNumber = numberToken[j] + "," + phonenNumber;
                    if (!TextUtils.isEmpty(name)) {
                        userName = name + "," + userName;
                    } else {
                        userName = phonenNumber + "," + userName;
                    }
                }
            } else {
                phonenNumber = phonenNumber + "," + number;
                if (!TextUtils.isEmpty(name)) {
                    userName = name + "," + userName;
                } else {
                    userName = phonenNumber + "," + userName;
                }
            }
        }

        String num[] = userName.split(",");
        String nm[] = phonenNumber.split(",");
        Log.e("name--", "--length-" + num.length + "-name-" + userName);
        Log.e("number--", "--length-" + nm.length + "-phonenNumber-" + phonenNumber);
        if (!Utilities.checkInternet(getBaseActivity())) {
            showToast(R.string.no_internet_msg);
        } else {
            mContactsApi = new ContactsApi(getBaseActivity(), this);
            ContactsSyncInput contactsSyncInput = new ContactsSyncInput();
            contactsSyncInput.setPhone(phonenNumber);
            contactsSyncInput.setUsername(userName);
            mContactsApi.syncContacts(contactsSyncInput);
        }
    }

    @Override
    public void contactsError(String error) {
        mContactsApi = new ContactsApi(getBaseActivity(), this);
        callApi();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_done) {
            manualBackPressed();
        } else if (id == R.id.iv_back || id == R.id.tv_settings) {
            manualBackPressed();
        }

    }
}
