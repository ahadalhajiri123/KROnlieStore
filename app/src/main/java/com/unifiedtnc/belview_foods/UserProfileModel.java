package com.unifiedtnc.belview_foods;

public class UserProfileModel
{
public String uid;
public String name;
public String password;
public String email;

    public UserProfileModel( String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }


}
