package com.nvcomputers.ten.model.output;

import java.util.ArrayList;

/**
 * Created by KaurGurleen on 5/4/2017.
 */

public class GetAllPostCommentOutput {
    private ArrayList<Comments> comments;

    public ArrayList<Comments> getComments ()
    {
        return comments;
    }

    public void setComments (ArrayList<Comments> comments)
    {
        this.comments = comments;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [comments = "+comments+"]";
    }

    public class Comments
    {
        private String text;

        private String videoUrl;

        private String videoPosterUrl;

        private String datestamp;

        private User user;

        private String idComment;

        public String getText ()
        {
            return text;
        }

        public void setText (String text)
        {
            this.text = text;
        }

        public String getVideoUrl ()
    {
        return videoUrl;
    }

        public void setVideoUrl (String videoUrl)
        {
            this.videoUrl = videoUrl;
        }

        public String getVideoPosterUrl ()
    {
        return videoPosterUrl;
    }

        public void setVideoPosterUrl (String videoPosterUrl)
        {
            this.videoPosterUrl = videoPosterUrl;
        }

        public String getDatestamp ()
        {
            return datestamp;
        }

        public void setDatestamp (String datestamp)
        {
            this.datestamp = datestamp;
        }

        public User getUser ()
        {
            return user;
        }

        public void setUser (User user)
        {
            this.user = user;
        }

        public String getIdComment ()
        {
            return idComment;
        }

        public void setIdComment (String idComment)
        {
            this.idComment = idComment;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [text = "+text+", videoUrl = "+videoUrl+", videoPosterUrl = "+videoPosterUrl+", datestamp = "+datestamp+", user = "+user+", idComment = "+idComment+"]";
        }
    }

    public class User
    {
        private String username;

        private String imageSrc;

        private String isPrivate;

        private String idUser;

        public String getUsername ()
        {
            return username;
        }

        public void setUsername (String username)
        {
            this.username = username;
        }

        public String getImageSrc ()
    {
        return imageSrc;
    }

        public void setImageSrc (String imageSrc)
        {
            this.imageSrc = imageSrc;
        }

        public String getIsPrivate ()
        {
            return isPrivate;
        }

        public void setIsPrivate (String isPrivate)
        {
            this.isPrivate = isPrivate;
        }

        public String getIdUser ()
        {
            return idUser;
        }

        public void setIdUser (String idUser)
        {
            this.idUser = idUser;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [username = "+username+", imageSrc = "+imageSrc+", isPrivate = "+isPrivate+", idUser = "+idUser+"]";
        }
    }


}
