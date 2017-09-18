package com.example.auth.app;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.MyAppConst;
import com.example.MyAppMain;
import com.example.MyAppMain.LoginState;

/**
 * Filter to perform application level login check
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class MyLoginFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(MyLoginFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        LOGGER.fine("");

        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse resp = (HttpServletResponse) response;
        final HttpSession session = req.getSession();

        final LoginState loginState = MyLoginState.getInstance().getLoginState(req);

        LOGGER.fine("loginState=" + loginState);

        switch (loginState) {
        case STATE_00_NOT_LOGGED_IN:
            // - If not logged in yet

            // A user is requesting a URL related with a servlet.
            // And this filter is set for that servlet.
            // So,the following "currentUrl" is the URL related with that servlet.
            final String currentUrl = getRequestingUrl(req);// The URL user is
                                                            // about to access.

            // After OAuth 2 processing is finished, you can explicitly specify the URL to be redirected
            // OAuthSession.getInstance().setRedirectUrlAfterOAuth(req,MyAppMain.PATH_AUTH_AUTH);

            LOGGER.fine("[NOT_LOGGED_IN_TO_APP] Store REDIRECT_URL_AFTER_LOGIN in the session URL='" + currentUrl + "'");

            // Store the URL(to be redirected after the application login processing) in the session
            session.setAttribute(MyAppConst.SESSION_KEY_REDIRECT_URL_AFTER_LOGIN, currentUrl);

            final String redirectUrl = MyAppMain.PATH_AUTH_LOGIN;
            LOGGER.fine("[NOT_LOGGED_IN_TO_APP] sendRedirect to '" + redirectUrl + "'");
            resp.sendRedirect(redirectUrl);

            break;

        case STATE_01_LOGGED_IN:
            // - If already logged in

            LOGGER.fine("[ALREADY_LOGGED_IN_TO_APP] Already logged in,do nothing.");

            // just continue as it is.
            chain.doFilter(request, response);
            break;
        }

    }

    /**
     * Returns the current requesting url
     * 
     * @param req
     * @return
     */
    private String getRequestingUrl(final HttpServletRequest req) {

        final String scheme = req.getScheme();
        final int currentPort = req.getServerPort();

        final StringBuilder sb = new StringBuilder();

        sb.append(scheme);
        sb.append("://");
        sb.append(req.getServerName());

        if (currentPort != 80 && currentPort != 443) {
            sb.append(":");
            sb.append(currentPort);
        }

        sb.append(req.getRequestURI());

        if (req.getQueryString() != null && !req.getQueryString().isEmpty()) {
            sb.append("?");
            sb.append(req.getQueryString());
        }

        final String currentUrl = sb.toString();

        return currentUrl;
    }

    @Override
    public void destroy() {

    }

}
