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
package org.orcid.core.manager.impl;

import javax.annotation.Resource;

import org.orcid.core.manager.BiographyManager;
import org.orcid.core.manager.ProfileEntityCacheManager;
import org.orcid.core.manager.read_only.impl.BiographyManagerReadOnlyImpl;
import org.orcid.jaxb.model.record_v2.Biography;
import org.orcid.pojo.ajaxForm.PojoUtil;

/**
 * 
 * @author Angel Montenegro
 * 
 */
public class BiographyManagerImpl extends BiographyManagerReadOnlyImpl implements BiographyManager {

    @Resource
    private ProfileEntityCacheManager profileEntityCacheManager;        
    
    @Override
    public boolean updateBiography(String orcid, Biography bio) {
        if (bio == null || bio.getVisibility() == null) {
            return false;
        }
        return biographyDao.updateBiography(orcid, bio.getContent(), bio.getVisibility());
    }

    @Override
    public void createBiography(String orcid, Biography bio) {
        if (bio == null || PojoUtil.isEmpty(bio.getContent()) || bio.getVisibility() == null) {
            return;
        }
        
        if(biographyDao.exists(orcid)) {
            throw new IllegalArgumentException("The biography for " + orcid + " already exists");
        }
        
        biographyDao.persistBiography(orcid, bio.getContent(), bio.getVisibility());
    }
}
