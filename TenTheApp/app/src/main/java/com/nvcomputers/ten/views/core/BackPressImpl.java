package com.nvcomputers.ten.views.core;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.nvcomputers.ten.adapter.ViewPagerAdapter;

/**
 * @author rkumar4
 * @version 1.0
 */
public class BackPressImpl implements OnBackPressListener {

    private Fragment parentFragment;

    public BackPressImpl(Fragment parentFragment) {
        this.parentFragment = parentFragment;
    }

    @Override
    public boolean onBackPressed() {

        if (parentFragment == null) return false;

        return popStackIfRequired();

        /*int childCount = parentFragment.getChildFragmentManager().getBackStackEntryCount();

        if (childCount == 0) {
            // it has no child Fragment
            // can not handle the onBackPressed task by itself
            return false;

        } else {
            // get the child Fragment
            FragmentManager childFragmentManager = parentFragment.getChildFragmentManager();
            OnBackPressListener childFragment = (OnBackPressListener) childFragmentManager.getFragments().get(0);

            // propagate onBackPressed method call to the child Fragment
            if (!childFragment.onBackPressed()) {
                // child Fragment was unable to handle the task
                // It could happen when the child Fragment is last last leaf of a chain
                // removing the child Fragment from stack
                childFragmentManager.popBackStackImmediate();

            }

            // either this Fragment or its child handled the task
            // either way we are successful and done here
            return true;
        }*/
    }

    @Override
    public boolean manualBackPressed() {
        if (parentFragment == null){
            //get manually fragement from saved list
            parentFragment = ViewPagerAdapter.registeredFragments.get(CarouselPagerFragment.currentPage);
        }

        return popStackIfRequired();
    }

    private boolean popStackIfRequired() {
        if (parentFragment==null) return  false;
        int childCount = parentFragment.getChildFragmentManager().getBackStackEntryCount();

        if (childCount == 0) {
            // it has no child Fragment
            // can not handle the onBackPressed task by itself
            return false;

        } else {
            if (parentFragment==null) return  false;
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // either this Fragment or its child handled the task
            // either way we are successful and done here
            return true;
        }
    }
}