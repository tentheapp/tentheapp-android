package com.nvcomputers.ten.model.output;

import java.util.ArrayList;


public class TopLikersResponse {
    private ArrayList<Users> users;

    public ArrayList<Users> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Users> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "ClassPojo [users = " + users + "]";
    }


    public class Users {
        private String username;

        private String imageSrc;

        private String isPrivate;

        private String idUser;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getImageSrc() {
            return imageSrc;
        }

        public void setImageSrc(String imageSrc) {
            this.imageSrc = imageSrc;
        }

        public String getIsPrivate() {
            return isPrivate;
        }

        public void setIsPrivate(String isPrivate) {
            this.isPrivate = isPrivate;
        }

        public String getIdUser() {
            return idUser;
        }

        public void setIdUser(String idUser) {
            this.idUser = idUser;
        }

        @Override
        public String toString() {
            return "ClassPojo [username = " + username + ", imageSrc = " + imageSrc + ", isPrivate = " + isPrivate + ", idUser = " + idUser + "]";
        }
    }


}
