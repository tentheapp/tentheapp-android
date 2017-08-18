package com.nvcomputers.ten.model.output;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Thaparsneh on 5/5/2017.
 */

public class GetUserStatusResponse {
    private List<Notifications> notifications;

    public List<Notifications> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notifications> notifications) {
        this.notifications = notifications;
    }

    public class Notifications {
        private String message;

        private Post post;

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

//    public class Post {
//        private String text;
//
//        private String userReposter;
//
//        private String isReposted;
//
//        private String hasImage;
//
//        private String likeCount;
//
//        private String expiration;
//
//        private String videoUrl;
//
//        private String liked;
//
//        private String image;
//
//        private String repostCount;
//
//        private String commentCount;
//
//        private String commented;
//
//        private String originalDatestamp;
//
//        private UserPoster userPoster;
//
//        private String postTypeImage;
//
//        private String datestamp;
//
//        private String idPost;
//
//        public String getText() {
//            return text;
//        }
//
//        public void setText(String text) {
//            this.text = text;
//        }
//
//        public String
//
//        getUserReposter() {
//            return userReposter;
//        }
//
//        public void setUserReposter(String userReposter) {
//            this.userReposter = userReposter;
//        }
//
//        public String getIsReposted() {
//            return isReposted;
//        }
//
//        public void setIsReposted(String isReposted) {
//            this.isReposted = isReposted;
//        }
//
//        public String getHasImage() {
//            return hasImage;
//        }
//
//        public void setHasImage(String hasImage) {
//            this.hasImage = hasImage;
//        }
//
//        public String getLikeCount() {
//            return likeCount;
//        }
//
//        public void setLikeCount(String likeCount) {
//            this.likeCount = likeCount;
//        }
//
//        public String getExpiration() {
//            return expiration;
//        }
//
//        public void setExpiration(String expiration) {
//            this.expiration = expiration;
//        }
//
//        public String
//
//        getVideoUrl() {
//            return videoUrl;
//        }
//
//        public void setVideoUrl(String videoUrl) {
//            this.videoUrl = videoUrl;
//        }
//
//        public String getLiked() {
//            return liked;
//        }
//
//        public void setLiked(String liked) {
//            this.liked = liked;
//        }
//
//        public String
//
//        getImage() {
//            return image;
//        }
//
//        public void setImage(String image) {
//            this.image = image;
//        }
//
//        public String getRepostCount() {
//            return repostCount;
//        }
//
//        public void setRepostCount(String repostCount) {
//            this.repostCount = repostCount;
//        }
//
//        public String getCommentCount() {
//            return commentCount;
//        }
//
//        public void setCommentCount(String commentCount) {
//            this.commentCount = commentCount;
//        }
//
//        public String getCommented() {
//            return commented;
//        }
//
//        public void setCommented(String commented) {
//            this.commented = commented;
//        }
//
//        public String
//
//        getOriginalDatestamp() {
//            return originalDatestamp;
//        }
//
//        public void setOriginalDatestamp(String originalDatestamp) {
//            this.originalDatestamp = originalDatestamp;
//        }
//
//        public UserPoster getUserPoster() {
//            return userPoster;
//        }
//
//        public void setUserPoster(UserPoster userPoster) {
//            this.userPoster = userPoster;
//        }
//
//        public String getPostTypeImage() {
//            return postTypeImage;
//        }
//
//        public void setPostTypeImage(String postTypeImage) {
//            this.postTypeImage = postTypeImage;
//        }
//
//        public String getDatestamp() {
//            return datestamp;
//        }
//
//        public void setDatestamp(String datestamp) {
//            this.datestamp = datestamp;
//        }
//
//        public String getIdPost() {
//            return idPost;
//        }
//
//        public void setIdPost(String idPost) {
//            this.idPost = idPost;
//        }
//    }

    public static class Post implements Parcelable {

        private String text;

        private NewsFeedOutput.UserReposter userReposter;

        private boolean isReposted;

        private boolean hasImage;

        private int likeCount;

        private String expiration;

        private String videoUrl;

        private boolean liked;

        private String image;

        private int repostCount;

        private int commentCount;

        private boolean commented;

        private String originalDatestamp;

        private NewsFeedOutput.UserPoster userPoster;

        private boolean postTypeImage;

        private String datestamp;

        private String idPost;

        public Post() {
        }

        public Post(Parcel in) {
            text = in.readString();
            userReposter = in.readParcelable(NewsFeedOutput.UserReposter.class.getClassLoader());
            isReposted = in.readByte() != 0;
            hasImage = in.readByte() != 0;
            likeCount = in.readInt();
            expiration = in.readString();
            videoUrl = in.readString();
            liked = in.readByte() != 0;
            image = in.readString();
            repostCount = in.readInt();
            commentCount = in.readInt();
            commented = in.readByte() != 0;
            originalDatestamp = in.readString();
            userPoster = in.readParcelable(NewsFeedOutput.UserPoster.class.getClassLoader());
            postTypeImage = in.readByte() != 0;
            datestamp = in.readString();
            idPost = in.readString();
        }

        public static final Creator<NewsFeedOutput.Posts> CREATOR = new Creator<NewsFeedOutput.Posts>() {
            @Override
            public NewsFeedOutput.Posts createFromParcel(Parcel in) {
                return new NewsFeedOutput.Posts(in);
            }

            @Override
            public NewsFeedOutput.Posts[] newArray(int size) {
                return new NewsFeedOutput.Posts[size];
            }
        };

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public NewsFeedOutput.UserReposter getUserReposter() {
            return userReposter;
        }

        public void setUserReposter(NewsFeedOutput.UserReposter userReposter) {
            this.userReposter = userReposter;
        }

        public boolean getIsReposted() {
            return isReposted;
        }

        public void setIsReposted(boolean isReposted) {
            this.isReposted = isReposted;
        }

        public boolean getHasImage() {
            return hasImage;
        }

        public void setHasImage(boolean hasImage) {
            this.hasImage = hasImage;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
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

        public boolean getLiked() {
            return liked;
        }

        public void setLiked(boolean liked) {
            this.liked = liked;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getRepostCount() {
            return repostCount;
        }

        public void setRepostCount(int repostCount) {
            this.repostCount = repostCount;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public boolean getCommented() {
            return commented;
        }

        public void setCommented(boolean commented) {
            this.commented = commented;
        }

        public String getOriginalDatestamp() {
            return originalDatestamp;
        }

        public void setOriginalDatestamp(String originalDatestamp) {
            this.originalDatestamp = originalDatestamp;
        }

        public NewsFeedOutput.UserPoster getUserPoster() {
            return userPoster;
        }

        public void setUserPoster(NewsFeedOutput.UserPoster userPoster) {
            this.userPoster = userPoster;
        }

        public boolean getPostTypeImage() {
            return postTypeImage;
        }

        public void setPostTypeImage(boolean postTypeImage) {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(text);
            dest.writeParcelable(userReposter, flags);
            dest.writeByte((byte) (isReposted ? 1 : 0));
            dest.writeByte((byte) (hasImage ? 1 : 0));
            dest.writeInt(likeCount);
            dest.writeString(expiration);
            dest.writeString(videoUrl);
            dest.writeByte((byte) (liked ? 1 : 0));
            dest.writeString(image);
            dest.writeInt(repostCount);
            dest.writeInt(commentCount);
            dest.writeByte((byte) (commented ? 1 : 0));
            dest.writeString(originalDatestamp);
            dest.writeParcelable(userPoster, flags);
            dest.writeByte((byte) (postTypeImage ? 1 : 0));
            dest.writeString(datestamp);
            dest.writeString(idPost);
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


}
