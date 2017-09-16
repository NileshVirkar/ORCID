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
package org.orcid.core.constants;

public class OrcidOauth2Constants {
    
    public static final String TOKEN_VERSION = "tokenVersion";
    public static final String NON_PERSISTENT_TOKEN = "0";
    public static final String PERSISTENT_TOKEN = "1";    
    public static final String GRANT_PERSISTENT_TOKEN = "grantPersistentToken";
    public static final String PERSISTENT = "persistent";
    public static final String IS_PERSISTENT = "isPersistent";
    public static final String GRANT_TYPE = "grant_type";
    public static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
    public static final String DATE_CREATED = "date_created";
    public static final String CLIENT_ID = "client_id";
    public static final String ORCID = "orcid";
    public static final String NAME = "name";
    public static final String CLIENT_ID_PARAM = "client_id";
    public static final String SCOPE_PARAM = "scope";
    public static final String STATE_PARAM = "state";
    public static final String RESPONSE_TYPE_PARAM = "response_type";
    public static final String REDIRECT_URI_PARAM = "redirect_uri";
    public static final String JUST_REGISTERED = "justRegistered";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String AUTHORIZATION = "authorization";
    public static final String REVOKE_OLD = "revoke_old";
    public static final String EXPIRES_IN = "expires_in";
    //openid connect
    public static final String NONCE = "nonce";
    public static final String MAX_AGE = "max_age";
    public static final String ID_TOKEN = "id_token";
    public static final String PROMPT = "prompt";
    public static final Object PROMPT_CONFIRM = "confirm";    
    public static final Object PROMPT_LOGIN = "login";
    public static final Object PROMPT_NONE = "none";    
    public static final String AUTH_TIME = "auth_time";

    //OAuth 2 screens
    public static final String OAUTH_2SCREENS = "OAUTH_2SCREENS";
    public static final String OAUTH_QUERY_STRING = "queryString";
    public static final String IMPLICIT_GRANT_TYPE = "implicit";
    public static final String IMPLICIT_TOKEN_RESPONSE_TYPE = "token";
}
