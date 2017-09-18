package com.example.auth.app;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.riversun.oauth2.google.OAuthSession;

import com.example.MyAppMain;
import com.example.MyAppMain.LoginState;

/**
 * Logout servlet
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 *
 */
@SuppressWarnings("serial")
public class MyLogoutServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(MyLogoutServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        final LoginState loginState = MyLoginState.getInstance().getLoginState(req);
        LOGGER.fine("loginState=" + loginState);

        switch (loginState) {
        case STATE_00_NOT_LOGGED_IN:
            // - When the user is not logged in to "MyApp" yet,
            // but user intentionally call "/logout".
            LOGGER.fine("[NOT_LOGGED_IN_TO_APP] user intentionally call 'logout'");
            break;
        case STATE_01_LOGGED_IN:
            // - If the user has already been logged-in

            LOGGER.fine("[ALREADY_LOGGED_IN_TO_APP] Do change loginState to " + LoginState.STATE_00_NOT_LOGGED_IN);

            // Set login state to not-logged-in
            MyLoginState.getInstance().setLoginState(req, LoginState.STATE_00_NOT_LOGGED_IN);

            // Clear the OAuth2 state(oauth-passed state)
            // so that the user will need to do OAuth-flow again the next time the user request "MyApp"
            OAuthSession.getInstance().clearOAuth2State(req);

            break;
        }

        final String redirectUrl = MyAppMain.PATH_AUTH_LOGIN;
        LOGGER.fine("sendRedirect to login page '" + redirectUrl + "'");
        resp.sendRedirect(redirectUrl);

    }

}
