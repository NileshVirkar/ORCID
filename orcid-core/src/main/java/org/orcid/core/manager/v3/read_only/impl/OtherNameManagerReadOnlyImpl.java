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
package org.orcid.core.manager.v3.read_only.impl;

import java.util.List;

import javax.annotation.Resource;

import org.orcid.core.adapter.v3.JpaJaxbOtherNameAdapter;
import org.orcid.core.manager.v3.read_only.OtherNameManagerReadOnly;
import org.orcid.jaxb.model.v3.dev1.record.OtherName;
import org.orcid.jaxb.model.v3.dev1.record.OtherNames;
import org.orcid.persistence.dao.OtherNameDao;
import org.orcid.persistence.jpa.entities.OtherNameEntity;

public class OtherNameManagerReadOnlyImpl extends ManagerReadOnlyBaseImpl implements OtherNameManagerReadOnly {
       
    @Resource(name = "jpaJaxbOtherNameAdapterV3")
    protected JpaJaxbOtherNameAdapter jpaJaxbOtherNameAdapter;

    protected OtherNameDao otherNameDao;
    
    public void setOtherNameDao(OtherNameDao otherNameDao) {
        this.otherNameDao = otherNameDao;
    }

    @Override
    public OtherNames getOtherNames(String orcid) {
        List<OtherNameEntity> otherNameEntityList = otherNameDao.getOtherNames(orcid, getLastModified(orcid));
        return jpaJaxbOtherNameAdapter.toOtherNameList(otherNameEntityList);
    }
    
    @Override
    public OtherNames getPublicOtherNames(String orcid) {
        List<OtherNameEntity> otherNameEntityList = otherNameDao.getPublicOtherNames(orcid, getLastModified(orcid));
        return jpaJaxbOtherNameAdapter.toOtherNameList(otherNameEntityList);
    }
    
    @Override
    public OtherNames getMinimizedOtherNames(String orcid) {
        List<OtherNameEntity> otherNameEntityList = otherNameDao.getOtherNames(orcid, getLastModified(orcid));
        return jpaJaxbOtherNameAdapter.toMinimizedOtherNameList(otherNameEntityList);
    }
    

    @Override
    public OtherName getOtherName(String orcid, Long putCode) {        
        OtherNameEntity otherNameEntity = otherNameDao.getOtherName(orcid, putCode);
        return jpaJaxbOtherNameAdapter.toOtherName(otherNameEntity);
    }
}
