/**
 * =============================================================================
 *
 * ORCID (R) Open Source
 * http://orcid.org
 *
 * Copyright (c) 2012-2014 ORCID, Inc.
 * Licensed under an MIT-Style License (MIT)
 * http://orcid.org/open-source-license
 *
 * This copyright and license information (including a link to the full license)
 * shall be included in its entirety in all copies or substantial portion of
 * the software.
 *
 * =============================================================================
 */
package org.orcid.core.oauth;

import org.orcid.core.constants.OrcidOauth2Constants;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

/**
 * @author Angel Montenegro
 */
public class OrcidRefreshTokenTokenGranter implements TokenGranter {
    private final OrcidRefreshTokenChecker orcidRefreshTokenChecker;

    private final AuthorizationServerTokenServices tokenServices;    
    
    public OrcidRefreshTokenTokenGranter(OrcidRefreshTokenChecker orcidRefreshTokenChecker, AuthorizationServerTokenServices tokenServices) {
        this.orcidRefreshTokenChecker = orcidRefreshTokenChecker;
        this.tokenServices = tokenServices;        
    }

    @Override 
    public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
        if (!OrcidOauth2Constants.REFRESH_TOKEN.equals(grantType)) {
            return null;
        }
        
        Long requestTimeInMillis = System.currentTimeMillis();
        orcidRefreshTokenChecker.validateRequest(grantType, tokenRequest, requestTimeInMillis);
        
        //If no exception is thrown we are ready to create the new token
        String refreshToken = tokenRequest.getRequestParameters().get(OrcidOauth2Constants.REFRESH_TOKEN);               
        
        return tokenServices.refreshAccessToken(refreshToken, tokenRequest);
    }
}
