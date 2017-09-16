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
package org.orcid.frontend.web.controllers;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.orcid.core.constants.OrcidOauth2Constants;
import org.orcid.core.manager.ClientDetailsEntityCacheManager;
import org.orcid.core.manager.ProfileEntityManager;
import org.orcid.core.manager.read_only.EmailManagerReadOnly;
import org.orcid.core.oauth.service.OrcidAuthorizationEndpoint;
import org.orcid.core.oauth.service.OrcidOAuth2RequestValidator;
import org.orcid.core.security.aop.LockedException;
import org.orcid.persistence.jpa.entities.ClientDetailsEntity;
import org.orcid.pojo.ajaxForm.PojoUtil;
import org.orcid.pojo.ajaxForm.RequestInfoForm;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller("loginController")
public class LoginController extends OauthControllerBase {
   
    @Resource
    protected ClientDetailsEntityCacheManager clientDetailsEntityCacheManager;
    
    @Resource
    protected OrcidOAuth2RequestValidator orcidOAuth2RequestValidator;
    
    @Resource
    protected OrcidAuthorizationEndpoint authorizationEndpoint;
    
    @Resource
    protected ProfileEntityManager profileEntityManager;
    
    @Resource
    protected EmailManagerReadOnly emailManagerReadOnly;
    
    @ModelAttribute("yesNo")
    public Map<String, String> retrieveYesNoMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("true", "Yes");
        map.put("false", "No");
        return map;
    }

    @RequestMapping(value = { "/signin", "/login" }, method = RequestMethod.GET)
    public ModelAndView loginGetHandler(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String query = request.getQueryString();
        if(!PojoUtil.isEmpty(query)) {
            if(query.contains("oauth")) {
                return handleOauthSignIn(request, response);
            }
        }
        // in case have come via a link that requires them to be signed out        
        return new ModelAndView("login");
    }

    // We should go back to regular spring sign out with CSRF protection
    @RequestMapping(value = { "/signout"}, method = RequestMethod.GET)
    public ModelAndView signout(HttpServletRequest request, HttpServletResponse response) {
        // in case have come via a link that requires them to be signed out
        logoutCurrentUser(request, response);    
        String redirectString = "redirect:" + orcidUrlManager.getBaseUrl()  + "/signin";
        ModelAndView mav = new ModelAndView(redirectString);
        return mav;
    }

    @RequestMapping("wrong-user")
    public String wrongUserHandler() {
        return "wrong_user";
    }

    @RequestMapping("/session-expired")
    public String sessionExpiredHandler() {
        return "session_expired";
    }
    
    private ModelAndView handleOauthSignIn(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String queryString = request.getQueryString();
        String redirectUri = null;

        // Get and save the request information form
        RequestInfoForm requestInfoForm = generateRequestInfoForm(queryString);
        request.getSession().setAttribute(REQUEST_INFO_FORM, requestInfoForm);
        // Save also the original query string
        request.getSession().setAttribute(OrcidOauth2Constants.OAUTH_QUERY_STRING, queryString);
        // Save a flag to indicate this is a request from the new
        request.getSession().setAttribute(OrcidOauth2Constants.OAUTH_2SCREENS, true);

        // Redirect URI
        redirectUri = requestInfoForm.getRedirectUrl();

        // Check that the client have the required permissions
        // Get client name
        String clientId = requestInfoForm.getClientId();
        if (PojoUtil.isEmpty(clientId)) {
            String redirectUriWithParams = redirectUri + "?error=invalid_client&error_description=invalid client_id";
            return new ModelAndView(new RedirectView(redirectUriWithParams));
        }
        // Validate client details
        ClientDetailsEntity clientDetails = clientDetailsEntityCacheManager.retrieve(clientId);
        try {
            orcidOAuth2RequestValidator.validateClientIsEnabled(clientDetails);
        } catch (LockedException e) {
            String redirectUriWithParams = redirectUri + "?error=client_locked&error_description=" + e.getMessage();
            return new ModelAndView(new RedirectView(redirectUriWithParams));
        }

        // validate client scopes
        try {
            authorizationEndpoint.validateScope(requestInfoForm.getScopesAsString(), clientDetails,requestInfoForm.getResponseType());
        } catch (InvalidScopeException e) {
            String redirectUriWithParams = redirectUri + "?error=invalid_scope&error_description=" + e.getMessage();
            return new ModelAndView(new RedirectView(redirectUriWithParams));
        }

        ModelAndView mav = new ModelAndView("login");
        // Check orcid and email params to decide if the login form should be
        // displayed by default
        boolean showLogin = false;
        if (PojoUtil.isEmpty(requestInfoForm.getUserOrcid()) && PojoUtil.isEmpty(requestInfoForm.getUserEmail())) {
            showLogin = true;
        } else if (!PojoUtil.isEmpty(requestInfoForm.getUserOrcid()) && profileEntityManager.orcidExists(requestInfoForm.getUserOrcid())) {
            mav.addObject("oauth_userId", requestInfoForm.getUserOrcid());
            showLogin = true;
        } else if (!PojoUtil.isEmpty(requestInfoForm.getUserEmail())) {
            mav.addObject("oauth_userId", requestInfoForm.getUserEmail());
            if(emailManagerReadOnly.emailExists(requestInfoForm.getUserEmail())) {
                showLogin = true;
            }            
        }
        // Check show_login param and change form 
        // takes precendence over orcid and email params
        if (queryString.toLowerCase().contains("show_login=true"))
            showLogin = true;
        else if (queryString.toLowerCase().contains("show_login=false"))
            showLogin = false;

        mav.addObject("showLogin", String.valueOf(showLogin));
        mav.addObject("hideUserVoiceScript", true);
        mav.addObject("oauth2Screens", true);
        return mav;
    }
}
