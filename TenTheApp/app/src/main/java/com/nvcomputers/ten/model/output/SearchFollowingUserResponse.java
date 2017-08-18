package com.nvcomputers.ten.model.output;

import java.util.List;

/**
 * Created by Thaparsneh on 5/23/2017.
 */

public class SearchFollowingUserResponse {
    private List<Users> users;

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public class Users {
        private String countryId;

        private String phone;

        private String imageSrc;

        private String website;

        private String status;

        private String username;

        private String friended;

        private String email;

        private String isPrivate;

        private String contactName;

        private String description;

        private String idUser;

        private String blocked;

        private String idUserFriend;

        private String otp;

        private String isNotify;

        public String

        getCountryId() {
            return countryId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String

        getImageSrc() {
            return imageSrc;
        }

        public void setImageSrc(String imageSrc) {
            this.imageSrc = imageSrc;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFriended() {
            return friended;
        }

        public void setFriended(String friended) {
            this.friended = friended;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getIsPrivate() {
            return isPrivate;
        }

        public void setIsPrivate(String isPrivate) {
            this.isPrivate = isPrivate;
        }

        public String

        getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIdUser() {
            return idUser;
        }

        public void setIdUser(String idUser) {
            this.idUser = idUser;
        }

        public String getBlocked() {
            return blocked;
        }

        public void setBlocked(String blocked) {
            this.blocked = blocked;
        }

        public String

        getIdUserFriend() {
            return idUserFriend;
        }

        public void setIdUserFriend(String idUserFriend) {
            this.idUserFriend = idUserFriend;
        }

        public String

        getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public String getIsNotify() {
            return isNotify;
        }

        public void setIsNotify(String isNotify) {
            this.isNotify = isNotify;
        }


    }

}
