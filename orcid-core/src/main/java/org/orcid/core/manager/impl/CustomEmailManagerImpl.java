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

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.orcid.core.manager.CustomEmailManager;
import org.orcid.persistence.dao.CustomEmailDao;
import org.orcid.persistence.jpa.entities.CustomEmailEntity;
import org.orcid.persistence.jpa.entities.EmailType;

public class CustomEmailManagerImpl implements CustomEmailManager {

    @Resource
    private CustomEmailDao customEmailDao;
    
    /**
     * Get a list of all custom emails created by a specific client
     * @param clientDetailsId
     * @return a list containing all custom emails associated with a client
     * */
    @Override
    public List<CustomEmailEntity> getCustomEmails(String clientDetailsId) {
        return customEmailDao.getCustomEmails(clientDetailsId);
    }
    
    /**
     * Finds a custom email given his client id and the email type
     * @param clientDetailsId
     * @param emailType
     * @return a CustomEmailEntity object if the email is found, null otherwise
     * */
    @Override    
    public CustomEmailEntity getCustomEmail(String clientDetailsId, EmailType emailType) {
        Date lastModified = customEmailDao.getLastModified(clientDetailsId, emailType);
        return customEmailDao.findByClientIdAndEmailType(clientDetailsId, emailType, lastModified.getTime());
    }

    /**
     * Creates a custom email on database
     * @param clientDetailsId
     * @param emailType
     * @param sender
     * @param subject
     * @param content
     * @return true if it was able to create the custom email      
     * */
    @Override
    public boolean createCustomEmail(String clientDetailsId, EmailType emailType, String sender, String subject, String content, boolean isHtml) {
        return customEmailDao.createCustomEmail(clientDetailsId, emailType, sender, subject, content, isHtml);
    }

    /**
     * Updated an existing custom email
     * @param clientDetailsId
     * @param emailType
     * @param sender
     * @param subject
     * @param content
     * @return true if it was able to update the custom email
     * */
    @Override
    public boolean updateCustomEmail(String clientDetailsId, EmailType emailType, String sender, String subject, String content, boolean isHtml) {
        if(customEmailDao.exists(clientDetailsId, emailType)) {
            return customEmailDao.updateCustomEmail(clientDetailsId, emailType, sender, subject, content, isHtml);
        }
        return false;
    }

    /**
     * Deletes a custom email
     * @param clientDetailsId
     * @param emailType 
     * @return true if it was able to delete the custom email
     * */
    @Override
    public boolean deleteCustomEmail(String clientDetailsId, EmailType emailType) {
        return customEmailDao.deleteCustomEmail(clientDetailsId, emailType);
    }
}
