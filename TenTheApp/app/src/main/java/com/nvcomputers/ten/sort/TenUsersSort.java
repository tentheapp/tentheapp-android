package com.nvcomputers.ten.sort;

import com.nvcomputers.ten.model.output.ContactsModel;
import com.nvcomputers.ten.model.output.TenUsersResponse;

import java.util.Comparator;

/**
 * Created by jindaldipanshu on 6/29/2017.
 */

public class TenUsersSort implements Comparator<TenUsersResponse.User> {

    @Override
    public int compare(TenUsersResponse.User contactsModel, TenUsersResponse.User t1) {

        return contactsModel.getUsername().compareToIgnoreCase(t1.getUsername());
    }
}
