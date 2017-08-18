package com.nvcomputers.ten.model.output;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by rkumar4 on 4/25/2017.
 */

public class NewsFeedOutput {
    private ArrayList<Posts> posts;

    public ArrayList<Posts> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Posts> posts) {
        this.posts = posts;
    }


    public static class Posts implements Parcelable {

        private String text;

        private UserReposter userReposter;

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

        private UserPoster userPoster;

        private boolean postTypeImage;

        private String datestamp;

        private String idPost;

        public Posts() {
        }

        public Posts(Parcel in) {
            text = in.readString();
            userReposter = in.readParcelable(UserReposter.class.getClassLoader());
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
            userPoster = in.readParcelable(UserPoster.class.getClassLoader());
            postTypeImage = in.readByte() != 0;
            datestamp = in.readString();
            idPost = in.readString();
        }

        public static  final Creator<Posts> CREATOR = new Creator<Posts>() {
            @Override
            public Posts createFromParcel(Parcel in) {
                return new Posts(in);
            }

            @Override
            public Posts[] newArray(int size) {
                return new Posts[size];
            }
        };

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public UserReposter getUserReposter() {
            return userReposter;
        }

        public void setUserReposter(UserReposter userReposter) {
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

        public UserPoster getUserPoster() {
            return userPoster;
        }

        public void setUserPoster(UserPoster userPoster) {
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

    public static class UserPoster implements Parcelable {

        private String username;

        private String imageSrc;

        private boolean isPrivate;

        //private boolean isMe;

        private String idUser;

        public UserPoster() {

        }

        protected UserPoster(Parcel in) {
            username = in.readString();
            imageSrc = in.readString();
            isPrivate = in.readByte() != 0;
            idUser = in.readString();
        }

        public static final Creator<UserPoster> CREATOR = new Creator<UserPoster>() {
            @Override
            public UserPoster createFromParcel(Parcel in) {
                return new UserPoster(in);
            }

            @Override
            public UserPoster[] newArray(int size) {
                return new UserPoster[size];
            }
        };

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

        public boolean getIsPrivate() {
            return isPrivate;
        }

        public void setIsPrivate(boolean isPrivate) {
            this.isPrivate = isPrivate;
        }

        public String getIdUser() {
            return idUser;
        }

        public void setIdUser(String idUser) {
            this.idUser = idUser;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(username);
            dest.writeString(imageSrc);
            dest.writeByte((byte) (isPrivate ? 1 : 0));
            dest.writeString(idUser);
        }

       /* public boolean isMe() {
            return isMe;
        }

        public void setMe(boolean me) {
            isMe = me;
        }*/
    }

    public static class UserReposter implements Parcelable {
        private String username;
        private String imageSrc;
        private boolean isPrivate;
        private String idUser;

        public UserReposter() {
        }

        protected UserReposter(Parcel in) {
            username = in.readString();
            imageSrc = in.readString();
            isPrivate = in.readByte() != 0;
            idUser = in.readString();
        }

        public static final Creator<UserReposter> CREATOR = new Creator<UserReposter>() {
            @Override
            public UserReposter createFromParcel(Parcel in) {
                return new UserReposter(in);
            }

            @Override
            public UserReposter[] newArray(int size) {
                return new UserReposter[size];
            }
        };

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

        public boolean getIsPrivate() {
            return isPrivate;
        }

        public void setIsPrivate(boolean isPrivate) {
            this.isPrivate = isPrivate;
        }

        public String getIdUser() {
            return idUser;
        }

        public void setIdUser(String idUser) {
            this.idUser = idUser;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(username);
            dest.writeString(imageSrc);
            dest.writeByte((byte) (isPrivate ? 1 : 0));
            dest.writeString(idUser);
        }
    }
}
