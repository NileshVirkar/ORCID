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
package org.orcid.persistence.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.orcid.jaxb.model.common_v2.Visibility;
import org.orcid.jaxb.model.record_v2.AffiliationType;
import org.orcid.persistence.dao.OrgAffiliationRelationDao;
import org.orcid.persistence.jpa.entities.OrgAffiliationRelationEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

public class OrgAffiliationRelationDaoImpl extends GenericDaoImpl<OrgAffiliationRelationEntity, Long> implements OrgAffiliationRelationDao {

    public OrgAffiliationRelationDaoImpl() {
        super(OrgAffiliationRelationEntity.class);
    }

    /**
     * Removes the relationship that exists between a affiliation and a profile.
     * 
     * @param orgAffiliationRelationId
     *            The id of the orgAffiliationRelation that will be removed from the client
     *            profile
     * @param userOrcid
     *            The client orcid
     * @return true if the relationship was deleted
     * */
    @Override
    @Transactional
    public boolean removeOrgAffiliationRelation(String userOrcid, Long orgAffiliationRelationId) {
        Query query = entityManager.createQuery("delete from OrgAffiliationRelationEntity where profile.id=:userOrcid and id=:orgAffiliationRelationId");
        query.setParameter("userOrcid", userOrcid);
        query.setParameter("orgAffiliationRelationId", orgAffiliationRelationId);
        return query.executeUpdate() > 0 ? true : false;
    }

    /**
     * Updates the visibility of an existing profile affiliation relationship
     * 
     * @param clientOrcid
     *            The client orcid
     * 
     * @param orgAffiliationRelationId
     *            The id of the orgAffiliationRelation that will be updated
     * 
     * @param visibility
     *            The new visibility value for the profile affiliation relationship
     * 
     * @return true if the relationship was updated
     * */
    @Override
    @Transactional
    public boolean updateVisibilityOnOrgAffiliationRelation(String userOrcid, Long orgAffiliationRelationId, Visibility visibility) {
        Query query = entityManager
                .createQuery("update OrgAffiliationRelationEntity set visibility=:visibility, lastModified=now() where profile.id=:userOrcid and id=:orgAffiliationRelationId");
        query.setParameter("userOrcid", userOrcid);
        query.setParameter("orgAffiliationRelationId", orgAffiliationRelationId);
        query.setParameter("visibility", visibility);
        return query.executeUpdate() > 0 ? true : false;
    }

    /**
     * Get the affiliation associated with the client orcid and the orgAffiliationRelationId
     * 
     * @param userOrcid
     *            The user orcid
     * 
     * @param orgAffiliationRelationId
     *            The id of the orgAffiliationRelation that will be updated
     * 
     * @return the profileOrgAffiliationRelation object
     * */
    @Override
    @Transactional
    public OrgAffiliationRelationEntity getOrgAffiliationRelation(String userOrcid, Long orgAffiliationRelationId) {
        Query query = entityManager.createQuery("from OrgAffiliationRelationEntity where profile.id=:userOrcid and id=:orgAffiliationRelationId");
        query.setParameter("userOrcid", userOrcid);
        query.setParameter("orgAffiliationRelationId",orgAffiliationRelationId);
        return (OrgAffiliationRelationEntity) query.getSingleResult();
    }

    /**
     * Creates a new profile entity relationship between the provided affiliation and
     * the given profile.
     * 
     * @param orcid
     *            The profile id
     * 
     * @param orgAffiliationRelationId
     *            The orgAffiliationRelation id
     * 
     * @param visibility
     *            The orgAffiliationRelation visibility
     * 
     * @return true if the profile orgAffiliationRelation relationship was created
     * */
    @Override
    @Transactional
    public boolean addOrgAffiliationRelation(String clientOrcid, long orgAffiliationRelationId, Visibility visibility) {
        Query query = entityManager
                .createNativeQuery("INSERT INTO org_affiliation_relation(orcid, id, date_created, last_modified, added_to_profile_date, visibility, source_id) values(:orcid, :orgAffiliationRelationId, now(), now(), now(), :visibility, :sourceId)");
        query.setParameter("orcid", clientOrcid);
        query.setParameter("orgAffiliationRelationId", orgAffiliationRelationId);
        query.setParameter("visibility", visibility.name());
        query.setParameter("sourceId", clientOrcid);

        return query.executeUpdate() > 0 ? true : false;
    }
    
    /**
     * Updates an existing OrgAffiliationRelationEntity
     * 
     * @param OrgAffiliationRelationEntity
     *          The entity to update
     * @return the updated OrgAffiliationRelationEntity
     * */
    public OrgAffiliationRelationEntity updateOrgAffiliationRelationEntity(OrgAffiliationRelationEntity orgAffiliationRelationEntity) {
        OrgAffiliationRelationEntity toUpdate = this.find(orgAffiliationRelationEntity.getId());
        mergeOrgAffiliationRelationEntity(toUpdate, orgAffiliationRelationEntity);
        toUpdate = this.merge(toUpdate);
        return toUpdate;
    }
    
    private void mergeOrgAffiliationRelationEntity(OrgAffiliationRelationEntity existing, OrgAffiliationRelationEntity updated) {
        existing.setDepartment(updated.getDepartment());
        existing.setEndDate(updated.getEndDate());        
        existing.setOrg(updated.getOrg());
        existing.setStartDate(updated.getStartDate());
        existing.setTitle(updated.getTitle());
        existing.setVisibility(updated.getVisibility());
        existing.setLastModified(new Date());
    }

    /**
     * Deletes all org affiliations where the source matches the give app id
     * @param clientSourceId the app id
     * */
    @Override
    @Transactional
    public void removeOrgAffiliationByClientSourceId(String clientSourceId) {
        Query query = entityManager.createNativeQuery("DELETE FROM org_affiliation_relation WHERE client_source_id=:clientSourceId");
        query.setParameter("clientSourceId", clientSourceId);
        query.executeUpdate();
    }
    
    @Override
    @Cacheable(value = "educations-summaries", key = "#userOrcid.concat('-').concat(#lastModified)")
    public List<OrgAffiliationRelationEntity> getEducationSummaries(String userOrcid, long lastModified) {
        return getByUserAndType(userOrcid, org.orcid.jaxb.model.record_v2.AffiliationType.EDUCATION);
    }
    
    @Override
    @Cacheable(value = "employments-summaries", key = "#userOrcid.concat('-').concat(#lastModified)")
    public List<OrgAffiliationRelationEntity> getEmploymentSummaries(String userOrcid, long lastModified) {
        return getByUserAndType(userOrcid, org.orcid.jaxb.model.record_v2.AffiliationType.EMPLOYMENT);
    }
    
    /**
     * Get all affiliations that belongs to a user and matches given type
     * @param userOrcid
     *          The owner of the affiliation
     * @param type
     *          The affiliation type
     * @return a list of all affiliations that belongs to the given user and matches the given type                 
     * */
    @Override
    public List<OrgAffiliationRelationEntity> getByUserAndType(String userOrcid, AffiliationType type) {
        TypedQuery<OrgAffiliationRelationEntity> query = entityManager.createQuery("from OrgAffiliationRelationEntity where profile.id=:userOrcid and affiliationType=:affiliationType", OrgAffiliationRelationEntity.class);
        query.setParameter("userOrcid", userOrcid);
        query.setParameter("affiliationType", type);
        return query.getResultList();
    }        
    
    /**
     * Get all affiliations that belongs to the given user
     * @param orcid: the user id
     * @return the list of affiliations that belongs to the user
     * */
    @Override
    public List<OrgAffiliationRelationEntity> getByUser(String orcid) {
        TypedQuery<OrgAffiliationRelationEntity> query = entityManager.createQuery("from OrgAffiliationRelationEntity where profile.id=:orcid order by dateCreated asc", OrgAffiliationRelationEntity.class);
        query.setParameter("orcid", orcid);
        return query.getResultList();
    }
    
    @Override
    @Transactional
    public void removeAllAffiliations(String orcid) {
        Query query = entityManager.createQuery("delete from OrgAffiliationRelationEntity where orcid = :orcid");
        query.setParameter("orcid", orcid);
        query.executeUpdate();
    }
}
