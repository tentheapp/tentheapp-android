package com.nvcomputers.ten.model.output;

/**
 * Created by Thaparsneh on 7/4/2017.
 */

public class GroupedNotificationDetailResponse {
    private Notifications[] notifications;

    public Notifications[] getNotifications() {
        return notifications;
    }

    public void setNotifications(Notifications[] notifications) {
        this.notifications = notifications;
    }
    public class Notifications {
        private String message;

        private Post post;

        private String count;

        private String datestamp;

        private String idNotification;

        private String type;

        private User user;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Post getPost() {
            return post;
        }

        public void setPost(Post post) {
            this.post = post;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getDatestamp() {
            return datestamp;
        }

        public void setDatestamp(String datestamp) {
            this.datestamp = datestamp;
        }

        public String getIdNotification() {
            return idNotification;
        }

        public void setIdNotification(String idNotification) {
            this.idNotification = idNotification;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

    }
    public class User {
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

        public String

        getImageSrc() {
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
    }

    public class UserPoster {
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

    }

    public class Post {
        private String text;

        private String userReposter;

        private String isReposted;

        private String hasImage;

        private String likeCount;

        private String expiration;

        private String videoUrl;

        private String liked;

        private String image;

        private String repostCount;

        private String videoName;

        private String commentCount;

        private String commented;

        private String originalDatestamp;

        private UserPoster userPoster;

        private String postTypeImage;

        private String datestamp;

        private String idPost;

        private String videoPosterName;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getUserReposter() {
            return userReposter;
        }

        public void setUserReposter(String userReposter) {
            this.userReposter = userReposter;
        }

        public String getIsReposted() {
            return isReposted;
        }

        public void setIsReposted(String isReposted) {
            this.isReposted = isReposted;
        }

        public String getHasImage() {
            return hasImage;
        }

        public void setHasImage(String hasImage) {
            this.hasImage = hasImage;
        }

        public String getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(String likeCount) {
            this.likeCount = likeCount;
        }

        public String getExpiration() {
            return expiration;
        }

        public void setExpiration(String expiration) {
            this.expiration = expiration;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getLiked() {
            return liked;
        }

        public void setLiked(String liked) {
            this.liked = liked;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getRepostCount() {
            return repostCount;
        }

        public void setRepostCount(String repostCount) {
            this.repostCount = repostCount;
        }

        public String getVideoName() {
            return videoName;
        }

        public void setVideoName(String videoName) {
            this.videoName = videoName;
        }

        public String getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(String commentCount) {
            this.commentCount = commentCount;
        }

        public String getCommented() {
            return commented;
        }

        public void setCommented(String commented) {
            this.commented = commented;
        }

        public String getOriginalDatestamp() {
            return originalDatestamp;
        }

        public void setOriginalDatestamp(String originalDatestamp) {
            this.originalDatestamp = originalDatestamp;
        }

        public UserPoster getUserPoster() {
            return userPoster;
        }

        public void setUserPoster(UserPoster userPoster) {
            this.userPoster = userPoster;
        }

        public String getPostTypeImage() {
            return postTypeImage;
        }

        public void setPostTypeImage(String postTypeImage) {
            this.postTypeImage = postTypeImage;
        }

        public String getDatestamp() {
            return datestamp;
        }

        public void setDatestamp(String datestamp) {
            this.datestamp = datestamp;
        }

        public String getIdPost() {
            return idPost;
        }

        public void setIdPost(String idPost) {
            this.idPost = idPost;
        }

        public String getVideoPosterName() {
            return videoPosterName;
        }

        public void setVideoPosterName(String videoPosterName) {
            this.videoPosterName = videoPosterName;
        }

        @Override
        public String toString() {
            return "ClassPojo [text = " + text + ", userReposter = " + userReposter + ", isReposted = " + isReposted + ", hasImage = " + hasImage + ", likeCount = " + likeCount + ", expiration = " + expiration + ", videoUrl = " + videoUrl + ", liked = " + liked + ", image = " + image + ", repostCount = " + repostCount + ", videoName = " + videoName + ", commentCount = " + commentCount + ", commented = " + commented + ", originalDatestamp = " + originalDatestamp + ", userPoster = " + userPoster + ", postTypeImage = " + postTypeImage + ", datestamp = " + datestamp + ", idPost = " + idPost + ", videoPosterName = " + videoPosterName + "]";
        }
    }


}