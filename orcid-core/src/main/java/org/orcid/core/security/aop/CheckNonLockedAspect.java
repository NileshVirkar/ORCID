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
package org.orcid.core.security.aop;

import javax.annotation.Resource;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.orcid.core.manager.OrcidProfileManager;
import org.orcid.core.manager.ProfileEntityManager;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * @author Angel Montenegro
 * */
@Aspect
@Component
@Order(100)
public class CheckNonLockedAspect {

    @Resource
    ProfileEntityManager profileEntityManager;
    
    @Resource
    OrcidProfileManager orcidProfileManager;
    
    @Before("@annotation(nonLocked) && (args(orcid))")
    public void checkPermissionsWithAll(NonLocked nonLocked, String orcid) {        
        if(orcidProfileManager.isLocked(orcid)) {            
            throw new LockedException("The given account " + orcid + " is locked", orcid);
        }
    }

}
