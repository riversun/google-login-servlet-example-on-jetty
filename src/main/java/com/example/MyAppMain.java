package com.example;

import java.io.IOException;
import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.example.app.MyAppServlet;
import com.example.auth.app.MyAuthServlet;
import com.example.auth.app.MyLoginFilter;
import com.example.auth.app.MyLoginServlet;
import com.example.auth.app.MyLogoutServlet;
import com.example.auth.google_oauth.MyOAuthCallbackServlet;
import com.example.auth.google_oauth.MyOAuthFilter;

/**
 * 
 * Launcher class for OAuth2/OpenId connect servlet<br>
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class MyAppMain {

    public static void main(String[] args) throws IOException {

        startServer();
    }

    // LoginState of "MyApp" not for "Google"
    public enum LoginState {
        STATE_00_NOT_LOGGED_IN, //
        STATE_01_LOGGED_IN//
    }

    public static String PATH_APP = "/app";

    public static String PATH_APP_TOP = PATH_APP + "/main";
    public static String PATH_AUTH_LOGIN = "/login";
    public static String PATH_AUTH_AUTH = "/auth";
    public static String PATH_AUTH_LOGOUT = "/logout";

    public static String PATH_BASE_OAUTH2_CALLBACK = "/callback";

    public static void startServer() {

        final int PORT = 8080;

        ServletContextHandler ctx = new ServletContextHandler(ServletContextHandler.SESSIONS);

        // Add filters for app(application logic) servlet
        ctx.addFilter(MyLoginFilter.class, PATH_APP + "/*", EnumSet.of(DispatcherType.INCLUDE, DispatcherType.REQUEST));
        ctx.addFilter(MyOAuthFilter.class, PATH_APP + "/*", EnumSet.of(DispatcherType.INCLUDE, DispatcherType.REQUEST));

        ctx.addFilter(MyOAuthFilter.class, PATH_AUTH_AUTH, EnumSet.of(DispatcherType.INCLUDE, DispatcherType.REQUEST));

        // Add OAuth2 callback servlet.
        ctx.addServlet(new ServletHolder(new MyOAuthCallbackServlet()), PATH_BASE_OAUTH2_CALLBACK);

        // Add My App level login related servlets
        ctx.addServlet(new ServletHolder(new MyLoginServlet()), PATH_AUTH_LOGIN);
        ctx.addServlet(new ServletHolder(new MyAuthServlet()), PATH_AUTH_AUTH);
        ctx.addServlet(new ServletHolder(new MyLogoutServlet()), PATH_AUTH_LOGOUT);

        // Add app servlets
        ctx.addServlet(new ServletHolder(new MyAppServlet()), PATH_APP_TOP);

        final ResourceHandler resourceHandler = new ResourceHandler();

        // Set location for static contents
        resourceHandler.setResourceBase(System.getProperty("user.dir") + "/htdocs");

        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[] { "index.html" });
        resourceHandler.setCacheControl("no-store,no-cache,must-revalidate");

        final HandlerList handlerList = new HandlerList();
        handlerList.addHandler(resourceHandler);
        handlerList.addHandler(ctx);

        final Server jettyServer = new Server();
        jettyServer.setHandler(handlerList);

        final HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSendServerVersion(false);

        final HttpConnectionFactory httpConnFactory = new HttpConnectionFactory(httpConfig);
        final ServerConnector httpConnector = new ServerConnector(jettyServer, httpConnFactory);
        httpConnector.setPort(PORT);
        jettyServer.setConnectors(new Connector[] { httpConnector });

        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
