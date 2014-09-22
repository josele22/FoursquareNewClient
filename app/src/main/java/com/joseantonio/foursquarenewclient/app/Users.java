package com.joseantonio.foursquarenewclient.app;

/**
 * Created by josetorres on 30/05/14.
 */

public class Users {

    private String login;

    public Users(String login) {
        this.login=login;
    }

    public Users(){};


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
