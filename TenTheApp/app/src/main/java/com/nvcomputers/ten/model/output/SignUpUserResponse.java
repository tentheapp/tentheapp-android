package com.nvcomputers.ten.model.output;

/**
 * Created by Thaparsneh on 4/25/2017.
 */

public class SignUpUserResponse {
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public class Response {
        private String message;

        private Result result;

        private String status;

        private String code;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public class Result {
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

        public String

        getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String

        getStatus() {
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

        public String

        getFriended() {
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

        public String

        getDescription() {
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

        @Override
        public String toString() {
            return "ClassPojo [countryId = " + countryId + ", phone = " + phone + ", imageSrc = " + imageSrc + ", website = " + website + ", status = " + status + ", username = " + username + ", friended = " + friended + ", email = " + email + ", isPrivate = " + isPrivate + ", contactName = " + contactName + ", description = " + description + ", idUser = " + idUser + ", blocked = " + blocked + ", idUserFriend = " + idUserFriend + ", otp = " + otp + ", isNotify = " + isNotify + "]";
        }
    }


}
