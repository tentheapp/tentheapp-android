package com.nvcomputers.ten.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.nvcomputers.ten.model.output.NewsFeedOutput;
import com.nvcomputers.ten.utils.PreferenceKeys;
import com.nvcomputers.ten.views.home.PostDetailPagerItem;

import java.util.ArrayList;


/**
 * Created by shahabuddin on 6/6/14.
 */
public class DetailViewPagerAdapter extends FragmentStatePagerAdapter {
    private final int count;
    private final ArrayList<NewsFeedOutput.Posts> mList;
    private final FragmentManager fragmentManager;
    public static SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    //private final Resources resources;

    /**
     * Create pager adapter
     *
     * @param fm
     */
    public DetailViewPagerAdapter(ArrayList<NewsFeedOutput.Posts> list, FragmentManager fm) {
        super(fm);
        this.count = list.size();
        this.mList = list;
        this.fragmentManager = fm;

    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new PostDetailPagerItem(fragmentManager);
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putParcelableArrayList(PreferenceKeys.PREF_HOME_PAGE_DATA, mList);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return count;
    }

    /*@Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {
            case 0:
                return resources.getString(R.string.page_1);
            case 1:
                return resources.getString(R.string.page_2);
            case 2:
                return resources.getString(R.string.page_3);
            case 3:
                return resources.getString(R.string.page_4);
            case 4:
                return resources.getString(R.string.page_5);
            default:
                return null;
        }
    }*/

    /**
     * On each Fragment instantiation we are saving the reference of that Fragment in a Map
     * It will help us to retrieve the Fragment by position
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
        } catch (Exception e) {
            e.printStackTrace();
        }

       /* Fragment fragment = new PostDetailPagerItem(fragmentManager);
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putParcelableArrayList(PreferenceKeys.PREF_HOME_PAGE_DATA, mList);
        fragment.setArguments(bundle);*/
        //registeredFragments.put(position, fragment);
        return fragment;
    }

    /**
     * Remove the saved reference from our Map on the Fragment destroy
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    /**
     * Get the Fragment by position
     *
     * @param position tab position of the fragment
     * @return
     */
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
