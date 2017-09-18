package com.example.auth.google_oauth;

import org.riversun.oauth2.google.OAuthCallbackServlet;

import com.example.MyAppConst;

/**
 * 
 * OAuth2 callabck servlet<br>
 * <br>
 * The servlet that receives the OAuth 2 callback. <br>
 * It returns the URL of OAuth 2 and implements persistence of refreshToken.
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
@SuppressWarnings("serial")
public class MyOAuthCallbackServlet extends OAuthCallbackServlet {

    @Override
    protected String getAuthRedirectUrl() {
        // Should return url of callback servlet(this servlet)
        return MyAppConst.OAUTH2_CALLBACK_URL;
    }

    @Override
    protected void saveRefreshTokenFor(String userId, String refreshToken) {
        // TODO
        // Write logic to save(persist) refresh_token for each user.
        // If not overridden it will be stored in memory
        super.saveRefreshTokenFor(userId, refreshToken);
    }

    @Override
    protected String loadRefreshTokenFor(String userId) {
        // TODO
        // Write logic to load refresh_token for each user
        // If not overridden it will be stored in memory
        return super.loadRefreshTokenFor(userId);
    }

}
