package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.model.output.NewsFeedOutput;

import java.util.ArrayList;

/**
 * Created by jindaldipanshu on 6/5/2017.
 */

public class SelectedPostPresenter {
    private ArrayList<NewsFeedOutput.Posts> mPostslist;


    public ArrayList<NewsFeedOutput.Posts> topBottomList(ArrayList<NewsFeedOutput.Posts> postsList, int postion) {
        this.mPostslist = postsList;
        ArrayList<NewsFeedOutput.Posts> mTopBtmList = new ArrayList<>();
        if (mPostslist != null && mPostslist.size() > 0) {
            String userId = mPostslist.get(postion).getUserPoster().getIdUser();
            ArrayList<String> postIds = new ArrayList<>();
            for (int i = 0; i < mPostslist.size(); i++) {
               // if (!userId.equals(mPostslist.get(i).getUserPoster().getIdUser())) {
                    String id = mPostslist.get(i).getUserPoster().getIdUser();
                    if (!postIds.contains(id)) {
                        postIds.add(id);
                        mTopBtmList.add(mPostslist.get(i));
                    }
              //  }
            }
        }
        return mTopBtmList;
    }


    public ArrayList<NewsFeedOutput.Posts> leftRightList(ArrayList<NewsFeedOutput.Posts> postsList, String userId) {
        this.mPostslist = postsList;
        ArrayList<NewsFeedOutput.Posts> leftrightlist = new ArrayList<>();
        if (mPostslist != null && mPostslist.size() > 0) {

            for (int i = 0; i < mPostslist.size(); i++) {
                if (userId.equals(mPostslist.get(i).getUserPoster().getIdUser())) {
                    leftrightlist.add(mPostslist.get(i));
                }
            }
        }
        return leftrightlist;
    }


}
