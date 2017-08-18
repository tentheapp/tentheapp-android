package com.nvcomputers.ten.presenter;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.nvcomputers.ten.model.output.ContactsModel;
import com.nvcomputers.ten.views.core.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jindaldipanshu on 4/24/2017.
 */

public class ContactsPresenter {

    private BaseActivity mContext;
    private ContactsCallback contactsCallback;

    public interface ContactsCallback {
        void allContacts(ArrayList<ContactsModel> contactsModels);

        void contactsError(String error);
    }

    public ContactsPresenter(BaseActivity activity, ContactsCallback contactsCallback) {
        this.mContext = activity;
        this.contactsCallback = contactsCallback;
    }

    public void getContacts() {
        ArrayList<ContactsModel> list = fetchUserContactsEmail();
        if (list != null && list.size() > 0) {
            contactsCallback.allContacts(list);
        } else {
            contactsCallback.contactsError("No data found");
        }
    }

    private ArrayList<ContactsModel> fetchUserContactsEmail() {
        ArrayList<HashMap<String, Object>> contacts = new ArrayList<HashMap<String, Object>>();
        ArrayList<ContactsModel> user_contacts = new ArrayList<ContactsModel>();
        final String[] projection = new String[]{ContactsContract.RawContacts.CONTACT_ID, ContactsContract.RawContacts.DELETED};

        final Cursor rawContacts = ((Activity) (mContext)).getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, projection, null, null, null);

        final int contactIdColumnIndex = rawContacts.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID);
        final int deletedColumnIndex = rawContacts.getColumnIndex(ContactsContract.RawContacts.DELETED);

        if (rawContacts.moveToFirst()) {
            while (!rawContacts.isAfterLast()) {
                final int contactId = rawContacts.getInt(contactIdColumnIndex);
                final boolean deleted = (rawContacts.getInt(deletedColumnIndex) == 1);

                ContactsModel contactsModel = new ContactsModel();
                if (!deleted) {
                    HashMap<String, Object> contactInfo = new HashMap<String, Object>() {
                        {
                            put("contactId", "");
                            put("name", "");
                            put("email", "");
                            put("phone", "");
                        }

                    };

                    contactsModel.setContactId("" + contactId);
                    contactsModel.setContactName("" + getName(contactId));
                    contactsModel.setContactNumber("" + getPhoneNumber(contactId));
                    contactsModel.setContactEmail("" + getPhoneNumber(contactId));
                    user_contacts.add(contactsModel);

                }
                rawContacts.moveToNext();
            }
        }

        rawContacts.close();
        return user_contacts;
    }

    private String getName(int contactId) {
        String name = "";
        final String[] projection = new String[]{ContactsContract.Contacts.DISPLAY_NAME};

        final Cursor contact = ((Activity) (mContext)).getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projection, ContactsContract.Contacts._ID + "=?", new String[]{String.valueOf(contactId)}, null);
        if (contact.moveToFirst()) {
            name = contact.getString(contact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        contact.close();
        return name;

    }

    private String getEmail(int contactId) {
        String emailStr = "";
        final String[] projection = new String[]{ContactsContract.CommonDataKinds.Email.DATA, // use
                // Email.ADDRESS
                // for API-Level
                // 11+
                ContactsContract.CommonDataKinds.Email.TYPE};

        final Cursor email = ((Activity) (mContext)).getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, projection, ContactsContract.Data.CONTACT_ID + "=?", new String[]{String.valueOf(contactId)}, null);

        if (email.moveToFirst()) {
            final int contactEmailColumnIndex = email.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);

            while (!email.isAfterLast()) {
                emailStr = emailStr + email.getString(contactEmailColumnIndex) + ";";
                email.moveToNext();
            }
        }
        email.close();
        return emailStr;

    }

//    private Bitmap getPhoto(int contactId) {
//        Bitmap photo = null;
//        final String[] projection = new String[] { ContactsContract.Contacts.PHOTO_ID };
//
//        final Cursor contact = managedQuery(ContactsContract.Contacts.CONTENT_URI, projection, ContactsContract.Contacts._ID + "=?", new String[] { String.valueOf(contactId) }, null);
//
//        if (contact.moveToFirst()) {
//            final String photoId = contact.getString(contact.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
//            if (photoId != null) {
//                photo = getBitmap(photoId);
//            } else {
//                photo = null;
//            }
//        }
//        contact.close();
//
//        return photo;
//    }


    private String getAddress(int contactId) {
        String postalData = "";
        String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
        String[] addrWhereParams = new String[]{String.valueOf(contactId), ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};

        Cursor addrCur = ((Activity) (mContext)).getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, addrWhere, addrWhereParams, null);

        if (addrCur.moveToFirst()) {
            postalData = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
        }
        addrCur.close();
        return postalData;
    }

    private String getPhoneNumber(int contactId) {

        String phoneNumber = "";
        final String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE,};
        final Cursor phone = ((Activity) (mContext)).getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, ContactsContract.Data.CONTACT_ID + "=?", new String[]{String.valueOf(contactId)}, null);

        if (phone.moveToFirst()) {
            final int contactNumberColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA);

            while (!phone.isAfterLast()) {
                phoneNumber = phoneNumber + phone.getString(contactNumberColumnIndex) + ";";
                phone.moveToNext();
            }

        }
        phone.close();
        return phoneNumber;
    }

}
