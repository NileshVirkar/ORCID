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

import java.util.List;

import org.orcid.jaxb.model.common_v2.Visibility;
import org.orcid.jaxb.model.record_v2.AffiliationType;
import org.orcid.persistence.jpa.entities.OrgAffiliationRelationEntity;

public interface OrgAffiliationRelationDao extends GenericDao<OrgAffiliationRelationEntity, Long> {

    /**
     * Removes the relationship that exists between a affiliation and a profile.
     * 
     * @param orgAffiliationRelationId
     *            The id of the orgAffilationRelation that will be removed from the client
     *            profile
     * @param userOrcid
     *            The user orcid
     * @return true if the relationship was deleted
     * */
    boolean removeOrgAffiliationRelation(String userOrcid, Long orgAffiliationRelationId);

    /**
     * Updates the visibility of an existing profile affiliation relationship
     * 
     * @param userOrcid
     *            The client orcid
     * 
     * @param orgAffiliationRelationId
     *            The id of the orgAffilationRelation that will be updated
     * 
     * @param visibility
     *            The new visibility value for the profile orgAffilationRelation relationship
     * 
     * @return true if the relationship was updated
     * */
    boolean updateVisibilityOnOrgAffiliationRelation(String userOrcid, Long orgAffiliationRelationId, Visibility visibility);

    /**
     * Get the affiliation associated with the client orcid and the orgAffiliationRelationId
     * 
     * @param userOrcid
     *            The user orcid
     * 
     * @param orgAffiliationRelationId
     *            The id of the orgAffilationRelation that will be updated
     * 
     * @return the orgAffiliationRelation object
     * */
    OrgAffiliationRelationEntity getOrgAffiliationRelation(String userOrcid, Long orgAffiliationRelationId);

    /**
     * Creates a new profile entity relationship between the provided orgAffilationRelation and
     * the given profile.
     * 
     * @param orcid
     *            The profile id
     * 
     * @param orgAffiliationRelationId
     *            The orgAffilationRelation id
     * 
     * @param visibility
     *            The orgAffilationRelation visibility
     * 
     * @return true if the profile orgAffilationRelation relationship was created
     * */
    boolean addOrgAffiliationRelation(String clientOrcid, long orgAffiliationRelationId, Visibility visibility);

    /**
     * Updates an existing OrgAffiliationRelationEntity
     * 
     * @param OrgAffiliationRelationEntity
     *          The entity to update
     * @return the updated OrgAffiliationRelationEntity
     * */
    OrgAffiliationRelationEntity updateOrgAffiliationRelationEntity(OrgAffiliationRelationEntity orgAffiliationRelationEntity);
    
    void removeOrgAffiliationByClientSourceId(String clientSourceId);
    
    /**
     * Get all affiliations that belongs to a user and matches given type
     * @param userOrcid
     *          The owner of the affiliation
     * @param type
     *          The affiliation type
     * @return a list of all affiliations that belongs to the given user and matches the given type                 
     * */
    List<OrgAffiliationRelationEntity> getByUserAndType(String userOrcid, AffiliationType type);        
    
    /**
     * Get all affiliations that belongs to the given user
     * @param orcid: the user id
     * @return the list of affiliations that belongs to the user
     * */
    List<OrgAffiliationRelationEntity> getByUser(String orcid);
    
    /**
     * Removes all affiliations that belongs to a given record. Careful!
     * 
     * @param orcid
     *            The ORCID iD of the record from which all funding will be
     *            removed.
     */
    void removeAllAffiliations(String orcid);

    List<OrgAffiliationRelationEntity> getEducationSummaries(String orcid, long lastModified);

    List<OrgAffiliationRelationEntity> getEmploymentSummaries(String orcid, long lastModified);
}
