package com.example.app;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.riversun.oauth2.google.OAuthSession;

import com.example.MyAppMain;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;

/**
 * App servlet<br>
 * A servlet that implements application-specific logic
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
@SuppressWarnings("serial")
public class MyAppServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html; charset=UTF-8");

        GoogleCredential credential = OAuthSession.getInstance().createCredential(req);

        // Get userId
        String userId = OAuthSession.getInstance().getUserId(req);

        // Get userInfo
        Oauth2 oauth2 = new Oauth2.Builder(
                new com.google.api.client.http.javanet.NetHttpTransport(),
                new com.google.api.client.json.jackson2.JacksonFactory(),
                credential).build();

        // Get userInfo
        Userinfoplus userInfo = oauth2.userinfo().get().execute();

        final PrintWriter out = resp.getWriter();

        out.println("<html><body><a href=\"" + MyAppMain.PATH_AUTH_LOGOUT + "\">[Logout]</a>");
        out.println("<br>");
        out.println("<br>");
        out.println("<div style='width: 600px; word-break: break-all; word-wrap: break-word;'");
        out.println("<br>");
        out.println("<b>OAuth2/OpenId connect result</b>");
        out.println("<br>");
        out.println("<hr>");
        out.println("userId=" + userId);
        out.println("<hr>");
        out.println("userInfo=" + userInfo);
        out.println("<hr>");
        out.print("</div>");

        out.close();

    }

}