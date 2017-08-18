package com.nvcomputers.ten.views.profile;

import android.view.View;
import android.widget.Button;

import com.nvcomputers.ten.R;
import com.nvcomputers.ten.views.core.BaseFragment;

/**
 * Created by jindaldipanshu on 4/28/2017.
 */

public class DemoFragmnet extends BaseFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_demo;
    }

    @Override
    protected void initViews(View view) {

        Button pass = (Button) view.findViewById(R.id.btn_click);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("dsa");
                getBaseActivity().addFragment(R.id.main_frame_layout, new NewProfileFragment(), null);
            }
        });
//        password_next_btn
    }

    @Override
    public void dispose() {

    }

}
