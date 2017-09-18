package com.example.auth.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.example.MyAppConst;
import com.example.MyAppMain.LoginState;

/**
 * Set/Get login state from HttpSession
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 *
 */
public class MyLoginState {

    private static MyLoginState instance = new MyLoginState();

    public static MyLoginState getInstance() {
        return instance;
    }

    private MyLoginState() {

    }

    /**
     * Returns login state of "MyApp"
     * 
     * @param req
     * @return
     */
    public LoginState getLoginState(HttpServletRequest req) {

        final HttpSession session = req.getSession();

        LoginState loginState = (LoginState) session.getAttribute(MyAppConst.SESSION_KEY_APP_LOGIN_STATE);
        if (loginState == null) {
            loginState = LoginState.STATE_00_NOT_LOGGED_IN;
        }

        return loginState;
    }

    public void setLoginState(HttpServletRequest req, LoginState loginState) {
        final HttpSession session = req.getSession();
        session.setAttribute(MyAppConst.SESSION_KEY_APP_LOGIN_STATE, loginState);
    }
}
