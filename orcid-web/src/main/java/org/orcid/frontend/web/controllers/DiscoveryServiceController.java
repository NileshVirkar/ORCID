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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author Will Simpson
 *
 */
@Controller
@RequestMapping("/disco")
public class DiscoveryServiceController extends BaseController {

    @Value("${org.orcid.shibboleth.enabled:false}")
    private boolean enabled;

    @RequestMapping
    public String discoveryServiceHome() {
        return "disco";
    }

}
