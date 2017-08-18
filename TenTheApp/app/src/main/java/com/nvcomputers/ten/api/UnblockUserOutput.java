package com.nvcomputers.ten.api;

/**
 * Created by KaurGurleen on 5/4/2017.
 */

class UnblockUserOutput {
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
