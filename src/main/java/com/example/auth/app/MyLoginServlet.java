package com.example.auth.app;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.MyAppMain;
import com.example.MyAppMain.LoginState;

/**
 * Login servlet<br>
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
@SuppressWarnings("serial")
public class MyLoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(MyLoginServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.fine("");

        final LoginState loginState = MyLoginState.getInstance().getLoginState(req);
        LOGGER.fine("loginState=" + loginState);

        switch (loginState) {
        case STATE_00_NOT_LOGGED_IN:
            LOGGER.fine("[NOT_LOGGED_IN_TO_APP] Show 'Login with Google' link.");

            resp.setContentType("text/html; charset=UTF-8");
            final PrintWriter out = resp.getWriter();
            out.println("<html><body><a href=\""
                    + "."
                    + MyAppMain.PATH_AUTH_AUTH
                    + "\">[Login with Google]</a>");
            out.close();
            break;

        case STATE_01_LOGGED_IN:
            // - If the user intentionally requested "/login" directly
            // even though the user has already been logged-in to "MyApp"

            // Redirect to default path
            final String defaultAppPath = MyAppMain.PATH_APP_TOP;

            LOGGER.fine("[ALREADY_LOGGED_IN_TO_APP] Maybe requested '/login' directly, redirect to default path '" + defaultAppPath + "'");

            resp.sendRedirect(defaultAppPath);

            break;
        default:
            break;
        }

    }

}
