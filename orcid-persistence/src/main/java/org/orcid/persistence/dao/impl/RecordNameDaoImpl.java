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

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.orcid.persistence.dao.RecordNameDao;
import org.orcid.persistence.jpa.entities.RecordNameEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Angel Montenegro
 * 
 */
public class RecordNameDaoImpl extends GenericDaoImpl<RecordNameEntity, Long> implements RecordNameDao {

    public RecordNameDaoImpl() {
        super(RecordNameEntity.class);
    }

    @Override
    @Cacheable(value = "record-name", key = "#orcid.concat('-').concat(#lastModified)")
    public RecordNameEntity getRecordName(String orcid, long lastModified) {
        Query query = entityManager.createQuery("FROM RecordNameEntity WHERE profile.id = :orcid");
        query.setParameter("orcid", orcid);
        return (RecordNameEntity) query.getSingleResult();
    }

    @Override
    public RecordNameEntity findByCreditName(String creditName) {
        Query query = entityManager.createQuery("FROM RecordNameEntity WHERE creditName = :creditName");
        query.setParameter("creditName", creditName);
        @SuppressWarnings("unchecked")
        List<RecordNameEntity> names = (List<RecordNameEntity>) query.getResultList();
        if(names == null || names.isEmpty()) {
            return null;
        }
        //Return the first result
        return names.get(0);
    }
    
    @Override
    @Transactional
    public boolean updateRecordName(RecordNameEntity recordName) {
        Query query = entityManager.createNativeQuery(
                "update record_name set credit_name = :creditName, family_name = :familyName, given_names = :givenNames, visibility = :visibility, last_modified = now() where orcid = :orcid");
        query.setParameter("creditName", recordName.getCreditName());
        query.setParameter("givenNames", recordName.getGivenNames());
        query.setParameter("familyName", recordName.getFamilyName());
        query.setParameter("visibility", StringUtils.upperCase(recordName.getVisibility().value()));
        query.setParameter("orcid", recordName.getProfile().getId());
        return query.executeUpdate() > 0;
    }
    
    @Override
    @Transactional
    public void createRecordName(RecordNameEntity recordName) {
        entityManager.persist(recordName);
    }

    @Override
    public boolean exists(String orcid) {
        Query query = entityManager.createNativeQuery("select count(*) from record_name where orcid=:orcid");
        query.setParameter("orcid", orcid);
        Long result = ((BigInteger)query.getSingleResult()).longValue();
        return (result != null && result > 0);
    }   
    
    @Override
    public Date getLastModified(String orcid) {
        TypedQuery<Date> query = entityManager.createQuery("SELECT lastModified FROM RecordNameEntity WHERE profile.id = :orcid", Date.class);
        query.setParameter("orcid", orcid);
        return query.getSingleResult();
    }
}
