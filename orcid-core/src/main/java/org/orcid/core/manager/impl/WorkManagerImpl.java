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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.orcid.core.exception.OrcidDuplicatedActivityException;
import org.orcid.core.locale.LocaleManager;
import org.orcid.core.manager.NotificationManager;
import org.orcid.core.manager.OrcidSecurityManager;
import org.orcid.core.manager.ProfileEntityCacheManager;
import org.orcid.core.manager.SourceManager;
import org.orcid.core.manager.WorkManager;
import org.orcid.core.manager.read_only.impl.WorkManagerReadOnlyImpl;
import org.orcid.core.manager.validator.ActivityValidator;
import org.orcid.core.manager.validator.ExternalIDValidator;
import org.orcid.core.utils.DisplayIndexCalculatorHelper;
import org.orcid.jaxb.model.common_v2.Visibility;
import org.orcid.jaxb.model.error_v2.OrcidError;
import org.orcid.jaxb.model.notification.amended_v2.AmendedSection;
import org.orcid.jaxb.model.notification.permission_v2.Item;
import org.orcid.jaxb.model.notification.permission_v2.ItemType;
import org.orcid.jaxb.model.record_v2.BulkElement;
import org.orcid.jaxb.model.record_v2.ExternalID;
import org.orcid.jaxb.model.record_v2.Relationship;
import org.orcid.jaxb.model.record_v2.Work;
import org.orcid.jaxb.model.record_v2.WorkBulk;
import org.orcid.persistence.jpa.entities.ProfileEntity;
import org.orcid.persistence.jpa.entities.SourceEntity;
import org.orcid.persistence.jpa.entities.WorkEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;

public class WorkManagerImpl extends WorkManagerReadOnlyImpl implements WorkManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkManagerImpl.class);

    @Resource
    private SourceManager sourceManager;

    @Resource
    private OrcidSecurityManager orcidSecurityManager;

    @Resource
    private ProfileEntityCacheManager profileEntityCacheManager;
    
    @Resource
    private NotificationManager notificationManager;
    
    @Resource 
    private ExternalIDValidator externalIDValidator;

    @Resource 
    private ActivityValidator activityValidator;
    
    @Resource
    private MessageSource messageSource;
    
    @Resource
    private LocaleManager localeManager;
    
    @Value("${org.orcid.core.works.bulk.max:100}")
    private Long maxBulkSize;
    
    /**
     * Updates the visibility of an existing work
     * 
     * @param workId
     *            The id of the work that will be updated
     * @param visibility
     *            The new visibility value for the profile work relationship
     * @return true if the relationship was updated
     * */
    public boolean updateVisibilities(String orcid, List<Long> workIds, Visibility visibility) {
        return workDao.updateVisibilities(orcid, workIds, visibility);
    }

    /**
     * Removes a work.
     * 
     * @param workId
     *            The id of the work that will be removed from the client
     *            profile
     * @param clientOrcid
     *            The client orcid
     * @return true if the work was deleted
     * */
    public boolean removeWorks(String clientOrcid, List<Long> workIds) {
        return workDao.removeWorks(clientOrcid, workIds);
    }

    @Override
    public void removeAllWorks(String orcid) {
        workDao.removeWorks(orcid);
    }
    
    /**
     * Sets the display index of the new work
     * 
     * @param orcid
     *            The work owner
     * @param workId
     *            The work id
     * @return true if the work index was correctly set
     * */
    public boolean updateToMaxDisplay(String orcid, Long workId) {
        return workDao.updateToMaxDisplay(orcid, workId);
    }

    @Override
    @Transactional
    public Work createWork(String orcid, Work work, boolean isApiRequest) {
        SourceEntity sourceEntity = sourceManager.retrieveSourceEntity();
        
        if (isApiRequest) {
            activityValidator.validateWork(work, sourceEntity, true, isApiRequest, null);
            // If it is the user adding the peer review, allow him to add
            // duplicates
            if (!sourceEntity.getSourceId().equals(orcid)) {
                List<Work> existingWorks = this.findWorks(orcid);       
                if (existingWorks != null) {
                    for (Work existing : existingWorks) {
                        activityValidator.checkExternalIdentifiersForDuplicates(work.getExternalIdentifiers(), existing.getExternalIdentifiers(), existing.getSource(),
                                sourceEntity);
                    }
                }
            }
        } else {
            // validate external ID vocab
            externalIDValidator.validateWorkOrPeerReview(work.getExternalIdentifiers());
        }

        WorkEntity workEntity = jpaJaxbWorkAdapter.toWorkEntity(work);
        ProfileEntity profile = profileEntityCacheManager.retrieve(orcid);
        workEntity.setOrcid(orcid);
        workEntity.setAddedToProfileDate(new Date());
        
        // Set source id 
        if(sourceEntity.getSourceProfile() != null) {
            workEntity.setSourceId(sourceEntity.getSourceProfile().getId());
        }
        
        if(sourceEntity.getSourceClient() != null) {
            workEntity.setClientSourceId(sourceEntity.getSourceClient().getId());
        } 
        
        setIncomingWorkPrivacy(workEntity, profile);        
        DisplayIndexCalculatorHelper.setDisplayIndexOnNewEntity(workEntity, isApiRequest);        
        workDao.persist(workEntity);
        workDao.flush();
        notificationManager.sendAmendEmail(orcid, AmendedSection.WORK, createItem(workEntity));
        return jpaJaxbWorkAdapter.toWork(workEntity);
    }

    
    /**
     * Add a list of works to the given profile
     * 
     * @param works
     *            The list of works that want to be added
     * @param orcid
     *            The id of the user we want to add the works to
     * 
     * @return the work bulk with the put codes of the new works or the error
     *         that indicates why a work can't be added
     */
    @Override
    @Transactional
    public WorkBulk createWorks(String orcid, WorkBulk workBulk) {        
        SourceEntity sourceEntity = sourceManager.retrieveSourceEntity();
        Set<ExternalID> existingExternalIdentifiers = buildExistingExternalIdsSet(orcid, sourceEntity.getSourceId());
        
        if(workBulk.getBulk() != null && !workBulk.getBulk().isEmpty()) {
            List<BulkElement> bulk = workBulk.getBulk();
            
            //Check bulk size
            if(bulk.size() > maxBulkSize) {
                Locale locale = localeManager.getLocale();                
                throw new IllegalArgumentException(messageSource.getMessage("apiError.validation_too_many_elements_in_bulk.exception", new Object[]{maxBulkSize}, locale));                
            }
                                    
            for(int i = 0; i < bulk.size(); i++) {
                if(Work.class.isAssignableFrom(bulk.get(i).getClass())){
                    Work work = (Work) bulk.get(i);
                    try {
                        //Validate the work
                        activityValidator.validateWork(work, sourceEntity, true, true, null);
                        //Validate it is not duplicated
                        if(work.getExternalIdentifiers() != null) {
                            for(ExternalID extId : work.getExternalIdentifiers().getExternalIdentifier()) {
                                if(existingExternalIdentifiers.contains(extId)) {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("clientName", sourceEntity.getSourceName());
                                    throw new OrcidDuplicatedActivityException(params);
                                }
                            }
                        }
                        
                        //Save the work
                        WorkEntity workEntity = jpaJaxbWorkAdapter.toWorkEntity(work);
                        ProfileEntity profile = profileEntityCacheManager.retrieve(orcid);
                        workEntity.setOrcid(orcid);
                        workEntity.setAddedToProfileDate(new Date());
                        
                        // Set source id 
                        if(sourceEntity.getSourceProfile() != null) {
                            workEntity.setSourceId(sourceEntity.getSourceProfile().getId());
                        }
                        
                        if(sourceEntity.getSourceClient() != null) {
                            workEntity.setClientSourceId(sourceEntity.getSourceClient().getId());
                        } 
                        
                        setIncomingWorkPrivacy(workEntity, profile);        
                        DisplayIndexCalculatorHelper.setDisplayIndexOnNewEntity(workEntity, true);        
                        workDao.persist(workEntity);                    
                        
                        //Update the element in the bulk
                        Work updatedWork = jpaJaxbWorkAdapter.toWork(workEntity);
                        
                        bulk.set(i, updatedWork);
                        
                        //Add the work extIds to the list of existing external identifiers
                        addExternalIdsToExistingSet(updatedWork, existingExternalIdentifiers);
                    } catch(Exception e) {
                        //Get the exception 
                        OrcidError orcidError = orcidCoreExceptionMapper.getOrcidError(e);
                        bulk.set(i, orcidError);
                    }                                        
                }
            }
            
            workDao.flush();            
        }
        
        return workBulk;
    }
    
    /**
     * Return the list of existing external identifiers for the given user where the source matches the given sourceId
     * 
     * @param orcid
     *          The user we want to add the works to
     * @param sourceId
     *          The client id we are evaluating
     * @return A set of all the existing external identifiers that belongs to the given user and to the given source id                  
     * */
    private Set<ExternalID> buildExistingExternalIdsSet(String orcid, String sourceId) {
        Set<ExternalID> existingExternalIds = new HashSet<ExternalID>();
        List<Work> existingWorks = this.findWorks(orcid);    
        for(Work work : existingWorks) {
            //If it is the same source
            if(work.retrieveSourcePath().equals(sourceId)) {
                if(work.getExternalIdentifiers() != null && work.getExternalIdentifiers().getExternalIdentifier() != null) {
                    for(ExternalID extId : work.getExternalIdentifiers().getExternalIdentifier()) {
                        //Don't include PART_OF external ids
                        if(!Relationship.PART_OF.equals(extId.getRelationship())) {
                            existingExternalIds.add(extId);
                        }                        
                    }
                }
            }
        }
        
        return existingExternalIds;
    }
    
    private void addExternalIdsToExistingSet(Work work, Set<ExternalID> existingExternalIDs) {
        if(work != null && work.getExternalIdentifiers() != null && work.getExternalIdentifiers().getExternalIdentifier() != null) {
            for(ExternalID extId : work.getExternalIdentifiers().getExternalIdentifier()) {
                //Don't include PART_OF external ids
                if(!Relationship.PART_OF.equals(extId.getRelationship())) {
                    existingExternalIDs.add(extId);
                }
            }
        }
    }
    
    @Override
    @Transactional
    public Work updateWork(String orcid, Work work, boolean isApiRequest) {
        WorkEntity workEntity = workDao.getWork(orcid, work.getPutCode());
        Visibility originalVisibility = workEntity.getVisibility();
        SourceEntity sourceEntity = sourceManager.retrieveSourceEntity();
        
        //Save the original source
        String existingSourceId = workEntity.getSourceId();
        String existingClientSourceId = workEntity.getClientSourceId();
        
        if (isApiRequest) {
            activityValidator.validateWork(work, sourceEntity, false, isApiRequest, workEntity.getVisibility());                        
            List<Work> existingWorks = this.findWorks(orcid);       
            
            for (Work existing : existingWorks) {
                // Dont compare the updated peer review with the DB version
                if (!existing.getPutCode().equals(work.getPutCode())) {
                    activityValidator.checkExternalIdentifiersForDuplicates(work.getExternalIdentifiers(), existing.getExternalIdentifiers(), existing.getSource(), sourceEntity);
                }
            }
        }else{
            //validate external ID vocab
            externalIDValidator.validateWorkOrPeerReview(work.getExternalIdentifiers());            
        }
                        
        orcidSecurityManager.checkSource(workEntity);
        jpaJaxbWorkAdapter.toWorkEntity(work, workEntity);
        workEntity.setVisibility(originalVisibility);
        
        //Be sure it doesn't overwrite the source
        workEntity.setSourceId(existingSourceId);
        workEntity.setClientSourceId(existingClientSourceId);
        
        workDao.merge(workEntity);
        workDao.flush();
        notificationManager.sendAmendEmail(orcid, AmendedSection.WORK, createItem(workEntity));
        return jpaJaxbWorkAdapter.toWork(workEntity);
    }

    @Override
    public boolean checkSourceAndRemoveWork(String orcid, Long workId) {
        boolean result = true;
        WorkEntity workEntity = workDao.getWork(orcid, workId);
        orcidSecurityManager.checkSource(workEntity);
        try {
            Item item = createItem(workEntity);
            workDao.removeWork(orcid, workId);
            workDao.flush();
            notificationManager.sendAmendEmail(orcid, AmendedSection.WORK, item);
        } catch (Exception e) {
            LOGGER.error("Unable to delete work with ID: " + workId);
            result = false;
        }
        return result;
    }

    private void setIncomingWorkPrivacy(WorkEntity workEntity, ProfileEntity profile) {
        Visibility incomingWorkVisibility = workEntity.getVisibility();
        Visibility defaultWorkVisibility = profile.getActivitiesVisibilityDefault();
        if (profile.getClaimed()) {
            workEntity.setVisibility(defaultWorkVisibility);            
        } else if (incomingWorkVisibility == null) {
            workEntity.setVisibility(org.orcid.jaxb.model.common_v2.Visibility.PRIVATE);
        }
    }

    private Item createItem(WorkEntity workEntity) {
        Item item = new Item();
        item.setItemName(workEntity.getTitle());
        item.setItemType(ItemType.WORK);
        item.setPutCode(String.valueOf(workEntity.getId()));
        return item;
    }
}
