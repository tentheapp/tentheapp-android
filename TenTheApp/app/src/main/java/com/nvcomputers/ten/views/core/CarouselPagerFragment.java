package com.nvcomputers.ten.views.core;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nvcomputers.ten.R;
import com.nvcomputers.ten.adapter.ViewPagerAdapter;
import com.nvcomputers.ten.views.contacts.ContactsFragment;
import com.nvcomputers.ten.views.home.LandingActivity;
import com.nvcomputers.ten.views.home.PostListFragment;
import com.nvcomputers.ten.views.notification.NotificationFragment;
import com.nvcomputers.ten.views.popular.PopularFragment;
import com.nvcomputers.ten.views.post.AddPostFragment;
import com.nvcomputers.ten.views.profile.ChangePasswordFragment;
import com.nvcomputers.ten.views.profile.NewProfileFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class CarouselPagerFragment extends Fragment {

    /**
     * TabPagerIndicator
     * Please refer to ViewPagerIndicator library
     */
    // protected TabPageIndicator indicator;

    protected ViewPager pager;

    private ViewPagerAdapter adapter;
    public static int currentPage;
    private Context mContext;


    public CarouselPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_carousel, container, false);

        //indicator = (TabPageIndicator) rootView.findViewById(R.id.tpi_header);
        pager = (ViewPager) rootView.findViewById(R.id.vp_pages);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Note that we are passing childFragmentManager, not FragmentManager
        adapter = new ViewPagerAdapter(getResources(), getChildFragmentManager());

        pager.setAdapter(adapter);
        //indicator.setViewPager(pager);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("", "");
                Fragment curentFragment = ViewPagerAdapter.registeredFragments.get(position);
                if (curentFragment instanceof AddPostFragment) {
                    AddPostFragment postListFragment = (AddPostFragment) curentFragment;
                    postListFragment.updateUI();
                }
            }

            @Override
            public void onPageSelected(int position) {
                //currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("", "");
            }

        });

    }

    /**
     * Retrieve the currently visible Tab Fragment and propagate the onBackPressed callback
     *
     * @return true = if this fragment and/or one of its associates Fragment can handle the backPress
     */
    public boolean onBackPressed() {
        // currently visible tab Fragment

        OnBackPressListener currentFragment = (OnBackPressListener) adapter.getRegisteredFragment(pager.getCurrentItem());

        if (currentFragment != null) {
            // lets see if the currentFragment or any of its childFragment can handle onBackPressed
            return currentFragment.onBackPressed();
        }

        // this Fragment couldn't handle the onBackPressed call
        return false;
    }

    public void updatePage(int pageNumber) {
        Fragment curentFragment = ViewPagerAdapter.registeredFragments.get(pageNumber);
        if (pageNumber == currentPage) {
            if (curentFragment instanceof PostListFragment) {
                PostListFragment postListFragment = (PostListFragment) curentFragment;
                postListFragment.scrollUp();
            } else if (curentFragment instanceof NotificationFragment) {
                NotificationFragment notificationFragment = (NotificationFragment) curentFragment;
                notificationFragment.scrollUp();
            } else if (curentFragment instanceof PopularFragment) {
                PopularFragment popularFragment = (PopularFragment) curentFragment;
                popularFragment.scrollUp();
            } else if (curentFragment instanceof NewProfileFragment) {
                NewProfileFragment profileFragment = (NewProfileFragment) curentFragment;
                profileFragment.scrollUp();
            }
        }
        pager.setCurrentItem(pageNumber, false);


       /* */
        //popItemsIfExist();
        //popItemsIfExist();
    }

    public void updateProfileFragment(int action) {
        if (currentPage!=4){
            return;
        }
        Fragment curentFragment = ViewPagerAdapter.registeredFragments.get(currentPage);
        NewProfileFragment profileFragment=null;
        if (curentFragment instanceof NewProfileFragment) {
            profileFragment = (NewProfileFragment) curentFragment;
        }
        if (profileFragment==null){
            return;
        }
        switch (action) {
            case 1:
                //profileFragment.showPopUp();
                break;
            case 2:
                profileFragment.replaceChildFragment(R.id.profile_frame_layout, new ContactsFragment(), null);
                break;
            case 3:
                profileFragment.replaceChildFragment(R.id.profile_frame_layout, new ChangePasswordFragment(), null);
                break;
        }
    }

    public void popItemsIfExist(int currentPage) {
        Fragment parentFragment = ViewPagerAdapter.registeredFragments.get(currentPage);

        if (parentFragment == null) {
            return;
        }
        int childCount = parentFragment.getChildFragmentManager().getBackStackEntryCount();
        while (childCount > 0) {
            if (childCount == 0) {
                // it has no child Fragment
                // can not handle the onBackPressed task by itself
                return;

            } else {
                if (parentFragment == null) return;
                // get the child Fragment
                FragmentManager childFragmentManager = parentFragment.getChildFragmentManager();
                try {
                    OnBackPressListener childFragment = (OnBackPressListener) childFragmentManager.getFragments().get(0);

                    // propagate onBackPressed method call to the child Fragment
                    if (!childFragment.onBackPressed()) {
                        // child Fragment was unable to handle the task
                        // It could happen when the child Fragment is last last leaf of a chain
                        // removing the child Fragment from stack
                        childFragmentManager.popBackStackImmediate();
                        childCount = parentFragment.getChildFragmentManager().getBackStackEntryCount();
                        Log.d("", "");
                    } else {
                        childCount = parentFragment.getChildFragmentManager().getBackStackEntryCount();
                        Log.d("", "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // either this Fragment or its child handled the task
                // either way we are successful and done here
                //return true;
            }
        }
        Log.d("", "");
    }

    public void updateButtonUI() {
        LandingActivity landingActivity = (LandingActivity) mContext;
        if (landingActivity != null) {
            landingActivity.setHomePage();
        }
    }
}

