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
package org.orcid.core.manager;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.orcid.core.BaseTest;
import org.orcid.jaxb.model.message.Iso3166Country;
import org.orcid.persistence.jpa.entities.AmbiguousOrgEntity;
import org.orcid.persistence.jpa.entities.OrgEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Will Simpson
 * 
 */
public class OrgManagerTest extends BaseTest {
    private static final List<String> DATA_FILES = Arrays.asList("/data/SecurityQuestionEntityData.xml", "/data/SubjectEntityData.xml", "/data/SourceClientDetailsEntityData.xml",
            "/data/ProfileEntityData.xml", "/data/OrgsEntityData.xml");

    @Resource
    private OrgManager orgManager;

    @BeforeClass
    public static void initDBUnitData() throws Exception {
        initDBUnitData(DATA_FILES);
    }

    @AfterClass
    public static void removeDBUnitData() throws Exception {
        Collections.reverse(DATA_FILES);
        removeDBUnitData(DATA_FILES);
    }

    @Test
    public void getAmbiguousOrgs() {
        List<AmbiguousOrgEntity> orgs = orgManager.getAmbiguousOrgs(0, Integer.MAX_VALUE);
        assertNotNull(orgs);
        assertEquals(2, orgs.size());
    }

    @Test
    public void testWriteAmbiguousOrgs() throws IOException {
        StringWriter writer = new StringWriter();

        orgManager.writeAmbiguousOrgs(writer);
        String result = writer.toString();

        String expected = IOUtils.toString(getClass().getResource("expected_ambiguous_orgs.csv"));
        assertEquals(expected, result);
    }

    @Test
    public void testGetOrgs() {
        List<OrgEntity> orgs = orgManager.getOrgs("an", 0, 10);
        assertNotNull(orgs);
        assertEquals(2, orgs.size());
        assertEquals("An Institution", orgs.get(0).resolveName());
        assertEquals("Another Institution", orgs.get(1).resolveName());
    }

    @Test
    @Transactional
    public void testCreateUpdateWhenAlreadyExists() {
        OrgEntity inputOrg = new OrgEntity();
        inputOrg.setName("An institution");
        inputOrg.setCity("London");
        inputOrg.setCountry(Iso3166Country.GB);

        OrgEntity resultOrg = orgManager.createUpdate(inputOrg);

        assertNotNull(resultOrg);
        assertEquals(inputOrg.getName(), resultOrg.getName());
        assertEquals(inputOrg.getCity(), resultOrg.getCity());
        assertEquals(inputOrg.getRegion(), resultOrg.getRegion());
        assertEquals(inputOrg.getCountry(), resultOrg.getCountry());
        assertEquals(1, resultOrg.getId().longValue());
    }

    @Test
    @Transactional
    public void testCreateUpdateWhenDoesNotAlreadyExists() {
        OrgEntity inputOrg = new OrgEntity();
        inputOrg.setName("Le Institution");
        inputOrg.setCity("Paris");
        inputOrg.setCountry(Iso3166Country.FR);

        OrgEntity resultOrg = orgManager.createUpdate(inputOrg);

        assertNotNull(resultOrg);
        assertEquals(inputOrg.getName(), resultOrg.getName());
        assertEquals(inputOrg.getCity(), resultOrg.getCity());
        assertEquals(inputOrg.getRegion(), resultOrg.getRegion());
        assertEquals(inputOrg.getCountry(), resultOrg.getCountry());
        assertFalse(resultOrg.getId().equals(1));
    }

}
