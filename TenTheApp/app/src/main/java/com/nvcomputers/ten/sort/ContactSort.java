package com.nvcomputers.ten.sort;

import com.nvcomputers.ten.model.output.ContactsModel;

import java.util.Comparator;

/**
 * Created by jindaldipanshu on 6/29/2017.
 */

public class ContactSort implements Comparator<ContactsModel> {

    @Override
    public int compare(ContactsModel contactsModel, ContactsModel t1) {
        return contactsModel.getContactName().compareTo(t1.getContactName());
    }
}
