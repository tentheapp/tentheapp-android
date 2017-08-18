package com.nvcomputers.ten.model.output;

/**
 * Created by jindaldipanshu on 4/26/2017.
 */

public class ProfileResponse {
    private Profile profile;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }


    public class Profile {

        private String followingCount;

        private String likers;

        private ActivePosts[] activePosts;

        private String likeCount;

        private String allPostCount;

        private BestTimedPost bestTimedPost;

        private String followersCount;

        private String commentCount;

        private User user;

        public String getFollowingCount() {
            return followingCount;
        }

        public void setFollowingCount(String followingCount) {
            this.followingCount = followingCount;
        }

        public String getLikers() {
            return likers;
        }

        public void setLikers(String likers) {
            this.likers = likers;
        }

        public ActivePosts[] getActivePosts() {
            return activePosts;
        }

        public void setActivePosts(ActivePosts[] activePosts) {
            this.activePosts = activePosts;
        }

        public String getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(String likeCount) {
            this.likeCount = likeCount;
        }

        public String getAllPostCount() {
            return allPostCount;
        }

        public void setAllPostCount(String allPostCount) {
            this.allPostCount = allPostCount;
        }

        public BestTimedPost getBestTimedPost() {
            return bestTimedPost;
        }

        public void setBestTimedPost(BestTimedPost bestTimedPost) {
            this.bestTimedPost = bestTimedPost;
        }

        public String getFollowersCount() {
            return followersCount;
        }

        public void setFollowersCount(String followersCount) {
            this.followersCount = followersCount;
        }

        public String getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(String commentCount) {
            this.commentCount = commentCount;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        @Override
        public String toString() {
            return "ClassPojo [followingCount = " + followingCount + ", likers = " + likers + ", activePosts = " + activePosts + ", likeCount = " + likeCount + ", allPostCount = " + allPostCount + ", bestTimedPost = " + bestTimedPost + ", followersCount = " + followersCount + ", commentCount = " + commentCount + ", user = " + user + "]";
        }


        public class User {
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

            public String getCountryId() {
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

            public String getImageSrc() {
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

            public String getContactName() {
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

            public String getIdUserFriend() {
                return idUserFriend;
            }

            public void setIdUserFriend(String idUserFriend) {
                this.idUserFriend = idUserFriend;
            }

            public String getOtp() {
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

        public class BestTimedPost {
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

            private String commentCount;

            private String commented;

            private String originalDatestamp;

            private UserPoster userPoster;

            private String postTypeImage;

            private String datestamp;

            private String idPost;

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

            @Override
            public String toString() {
                return "ClassPojo [text = " + text + ", userReposter = " + userReposter + ", isReposted = " + isReposted + ", hasImage = " + hasImage + ", likeCount = " + likeCount + ", expiration = " + expiration + ", videoUrl = " + videoUrl + ", liked = " + liked + ", image = " + image + ", repostCount = " + repostCount + ", commentCount = " + commentCount + ", commented = " + commented + ", originalDatestamp = " + originalDatestamp + ", userPoster = " + userPoster + ", postTypeImage = " + postTypeImage + ", datestamp = " + datestamp + ", idPost = " + idPost + "]";
            }
        }

        public class ActivePosts {
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

            private String commentCount;

            private String commented;

            private String originalDatestamp;

            private UserPoster userPoster;

            private String postTypeImage;

            private String datestamp;

            private String idPost;

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

            @Override
            public String toString() {
                return "ClassPojo [text = " + text + ", userReposter = " + userReposter + ", isReposted = " + isReposted + ", hasImage = " + hasImage + ", likeCount = " + likeCount + ", expiration = " + expiration + ", videoUrl = " + videoUrl + ", liked = " + liked + ", image = " + image + ", repostCount = " + repostCount + ", commentCount = " + commentCount + ", commented = " + commented + ", originalDatestamp = " + originalDatestamp + ", userPoster = " + userPoster + ", postTypeImage = " + postTypeImage + ", datestamp = " + datestamp + ", idPost = " + idPost + "]";
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

            @Override
            public String toString() {
                return "ClassPojo [username = " + username + ", imageSrc = " + imageSrc + ", isPrivate = " + isPrivate + ", idUser = " + idUser + "]";
            }
        }

    }

}
