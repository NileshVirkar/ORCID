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
package org.orcid.persistence.dao;

import org.orcid.persistence.jpa.entities.GivenPermissionToEntity;

/**
 * 
 * @author Will Simpson
 * 
 */
public interface GivenPermissionToDao extends GenericDao<GivenPermissionToEntity, Long> {

    GivenPermissionToEntity findByGiverAndReceiverOrcid(String giverOrcid, String receiverOrcid);

    void remove(String giverOrcid, String receiverOrcid);

}
