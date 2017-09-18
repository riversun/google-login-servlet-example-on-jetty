package com.example.auth.app;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.MyAppConst;
import com.example.MyAppMain;
import com.example.MyAppMain.LoginState;

/**
 * Servlet for OAuth-front<br>
 * (Actual OAuth-flow is done by OAuthFilter)
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 *
 */
@SuppressWarnings("serial")
public class MyAuthServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(MyAuthServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.fine("");

        final HttpSession session = req.getSession();

        final LoginState loginState = MyLoginState.getInstance().getLoginState(req);

        LOGGER.fine("loginState=" + loginState);

        switch (loginState) {
        case STATE_00_NOT_LOGGED_IN:
            // - When the user is not logged in to "MyApp" yet,
            // jumping from the link of Login Page

            // [How MyAuthServlet works]
            //
            // First, if "/auth" is requested when Google's OAuth 2 has not ended,
            // this servlet will not be called because
            // "MyOAuthFilter" under this servlet is called first you know.

            // After OAuth2-flow has been properly completed in the "MyOAuthFilter",
            // since the URL "/auth" was automatically set as the callback URL of
            // OAuth2,so it comes back here "MyAuthServlet" again.

            // Why automatically return to "/auth", the "OAuthFilter" will automatically
            // set the URL(that is the requesting URL) as the return destination when
            // "/auth" is requested.

            LOGGER.fine("[NOT_LOGGED_IN_TO_APP] Do change loginState to " + LoginState.STATE_01_LOGGED_IN +
                    " because this servlet is called after successfully passing OAuthFilter.");

            // TODO:implement user management (match userId from OAuth2/OpenId connect and the userId from RDBMS)

            MyLoginState.getInstance().setLoginState(req, LoginState.STATE_01_LOGGED_IN);

            String redirectUrlAfterLogin = (String) session.getAttribute(MyAppConst.SESSION_KEY_REDIRECT_URL_AFTER_LOGIN);

            LOGGER.fine("[JUST_LOGGED_IN_TO_APP] Load REDIRECT_URL_AFTER_LOGIN from session URL='" + redirectUrlAfterLogin + "'");

            if (redirectUrlAfterLogin == null) {
                // - If user already logged in to "Google" and
                // intentionally requested "/auth" directly.

                redirectUrlAfterLogin = MyAppMain.PATH_APP_TOP;

                LOGGER.fine("[JUST_LOGGED_IN_TO_APP] Set redirectUrlAfterLogin to default path '" + redirectUrlAfterLogin + "' "
                        + "Maybe user already logged in to 'Google' and intentionally requested '/auth' directly.");

            }

            // Clear REDIRECT_URL_AFTER_LOGIN from the session
            session.setAttribute(MyAppConst.SESSION_KEY_REDIRECT_URL_AFTER_LOGIN, null);

            LOGGER.fine("[JUST_LOGGED_IN_TO_APP] sendRedirect to '" + redirectUrlAfterLogin + "'");

            resp.sendRedirect(redirectUrlAfterLogin);

            break;

        case STATE_01_LOGGED_IN:
            // - If the user intentionally requested "/auth" directly
            // even though login to "MyApp" has already been done

            final String redirectUrl = MyAppMain.PATH_APP_TOP;

            LOGGER.fine("[ALREADY_LOGGED_IN_TO_APP] sendRedirect to '" + redirectUrl + "'");

            resp.sendRedirect(redirectUrl);
            break;
        }

    }

}
