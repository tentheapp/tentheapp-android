package com.nvcomputers.ten.model.output;

/**
 * Created by jindaldipanshu on 5/2/2017.
 */

public class EditProfileResponse {
    private String success;

    public String getSuccess ()
    {
        return success;
    }

    public void setSuccess (String success)
    {
        this.success = success;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [success = "+success+"]";
    }
}
