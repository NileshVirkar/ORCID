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
package org.orcid.integration.blackbox.api.v3.tests;

import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.codehaus.jettison.json.JSONException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.orcid.integration.api.pub.PublicV3ApiClientImpl;
import org.orcid.integration.blackbox.api.BBBUtil;
import org.orcid.integration.blackbox.api.v3.dev1.BlackBoxBaseV3_0_dev1;
import org.orcid.integration.blackbox.api.v3.dev1.MemberV3Dev1ApiClientImpl;
import org.orcid.jaxb.model.message.ScopePathType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sun.jersey.api.client.ClientResponse;

/**
 * 
 * @author Angel Montenegro
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml" })
public class PersonTest extends BlackBoxBaseV3_0_dev1 {
    
    @Resource(name = "memberV3_0_dev1ApiClient")
    private MemberV3Dev1ApiClientImpl memberV3Dev1ApiClient;
    
    @Resource(name = "publicV3_0_dev1ApiClient")
    private PublicV3ApiClientImpl publicV3ApiClient;
    
    private static final String limitedEmail = "limited@test.orcid.org";

    private static boolean allSet = false;

    private static String researcherUrl1 = "http://test.orcid.org/1/" + System.currentTimeMillis();
    private static String researcherUrl2 = "http://test.orcid.org/2/" + System.currentTimeMillis();

    @BeforeClass
    public static void setUpUserInUi() throws Exception {
        signin();
        
        //Set the default visibility to public, so, all elements created are public by default
        changeDefaultUserVisibility(webDriver, org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC, false);
        
        showMyOrcidPage();

        openEditAddressModal();
        deleteAddresses();
        createAddress(org.orcid.jaxb.model.v3.dev1.common.Iso3166Country.US.name());
        saveEditAddressModal();

        openEditOtherNamesModal();
        deleteOtherNames();
        createOtherName("other-name-1");
        createOtherName("other-name-2");
        saveOtherNamesModal();

        openEditKeywordsModal();
        deleteKeywords();
        createKeyword("keyword-1");
        createKeyword("keyword-2");
        saveKeywordsModal();

        openEditResearcherUrlsModal();
        deleteResearcherUrls();
        createResearcherUrl(researcherUrl1);
        createResearcherUrl(researcherUrl2);
        saveResearcherUrlsModal();        
                
        // Set biography to public
        String bio = BBBUtil.getProperty("org.orcid.web.testUser1.bio");
        changeBiography(bio, org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC);

        // Set names to public
        changeNamesVisibility(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC);
        
        showAccountSettingsPage();
        openEditEmailsSectionOnAccountSettingsPage();
        updatePrimaryEmailVisibility(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC);
        removePopOver();
        if (emailExists(limitedEmail)) {
            updateEmailVisibility(limitedEmail, org.orcid.jaxb.model.v3.dev1.common.Visibility.LIMITED);
        } else {
            addEmail(limitedEmail, org.orcid.jaxb.model.v3.dev1.common.Visibility.LIMITED);
        }                
    }
    
    @Before
    public void before() throws InterruptedException, JSONException {
        if(allSet) {
            return;
        }
        
        showMyOrcidPage();
        
        if (hasExternalIdentifiers()) {
            openEditExternalIdentifiersModal();
            deleteExternalIdentifiers();
            saveExternalIdentifiersModal();
        }
        
        String accessToken = getAccessToken(getScopes(ScopePathType.ORCID_BIO_EXTERNAL_IDENTIFIERS_CREATE));
        createExternalIdentifier("A-0001", getUser1OrcidId(), accessToken);
        createExternalIdentifier("A-0002", getUser1OrcidId(), accessToken);

        showMyOrcidPage();
        
        openEditExternalIdentifiersModal();
        updateExternalIdentifierVisibility("A-0001", org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC);
        updateExternalIdentifierVisibility("A-0002", org.orcid.jaxb.model.v3.dev1.common.Visibility.LIMITED);
        saveExternalIdentifiersModal();
        
        allSet = true;
    }            

    @AfterClass
    public static void afterClass() {
        showMyOrcidPage();
        openEditAddressModal();
        deleteAddresses();
        saveEditAddressModal();

        openEditOtherNamesModal();
        deleteOtherNames();
        saveOtherNamesModal();

        openEditKeywordsModal();
        deleteKeywords();
        saveKeywordsModal();

        openEditResearcherUrlsModal();
        deleteResearcherUrls();
        saveResearcherUrlsModal();

        openEditExternalIdentifiersModal();
        deleteExternalIdentifiers();
        saveExternalIdentifiersModal();
        
        openEditResearcherUrlsModal();
        deleteResearcherUrls();
        saveResearcherUrlsModal();
        
        signout();
    }

    @Test
    public void testGetBioFromPublicAPI() {
        ClientResponse response = publicV3ApiClient.viewBiographyXML(getUser1OrcidId());
        assertNotNull(response);
        org.orcid.jaxb.model.v3.dev1.record.Biography bio = response.getEntity(org.orcid.jaxb.model.v3.dev1.record.Biography.class);
        assertNotNull(bio);
        assertEquals(getUser1Bio(), bio.getContent());
        assertEquals(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC, bio.getVisibility());
    }

    @Test
    public void testGetBioFromMemberAPI() throws Exception {
        String accessToken = getAccessToken();
        assertNotNull(accessToken);
        ClientResponse response = memberV3Dev1ApiClient.viewBiography(getUser1OrcidId(), accessToken);
        assertNotNull(response);
        org.orcid.jaxb.model.v3.dev1.record.Biography bio = response.getEntity(org.orcid.jaxb.model.v3.dev1.record.Biography.class);
        assertNotNull(bio);
        assertEquals(getUser1Bio(), bio.getContent());
        assertEquals(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC, bio.getVisibility());
    }

    @Test
    public void testViewPersonFromMemberAPI() throws InterruptedException, JSONException {
        String accessToken = getAccessToken();
        assertNotNull(accessToken);
        ClientResponse response = memberV3Dev1ApiClient.viewPerson(getUser1OrcidId(), accessToken);
        assertNotNull(response);
        assertEquals("invalid " + response, 200, response.getStatus());
        Thread.sleep(100);
        org.orcid.jaxb.model.v3.dev1.record.Person person = response.getEntity(org.orcid.jaxb.model.v3.dev1.record.Person.class);
        assertNotNull(person);
        assertNotNull(person.getAddresses());
        assertNotNull(person.getAddresses().getAddress());
        assertEquals(1, person.getAddresses().getAddress().size());
        assertNotNull(person.getAddresses().getAddress().get(0).getCountry());
        assertEquals(org.orcid.jaxb.model.v3.dev1.common.Iso3166Country.US, person.getAddresses().getAddress().get(0).getCountry().getValue());
        assertEquals(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC, person.getAddresses().getAddress().get(0).getVisibility());

        assertNotNull(person.getBiography());
        assertEquals(getUser1Bio(), person.getBiography().getContent());
        assertEquals(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC, person.getBiography().getVisibility());

        assertNotNull(person.getEmails());
        EmailTest.assertListContainsEmail(getUser1UserName(), org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC,
                person.getEmails());
        EmailTest.assertListContainsEmail(limitedEmail, org.orcid.jaxb.model.v3.dev1.common.Visibility.LIMITED, person.getEmails());

        assertNotNull(person.getExternalIdentifiers());
        assertNotNull(person.getExternalIdentifiers().getExternalIdentifiers());
        assertEquals(2, person.getExternalIdentifiers().getExternalIdentifiers().size());

        boolean foundPublic = false;
        boolean foundLimited = false;

        for (org.orcid.jaxb.model.v3.dev1.record.PersonExternalIdentifier e : person.getExternalIdentifiers().getExternalIdentifiers()) {
            if ("A-0001".equals(e.getValue())) {
                assertEquals(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC, e.getVisibility());
                foundPublic = true;
            } else if ("A-0002".equals(e.getValue())) {
                assertEquals(org.orcid.jaxb.model.v3.dev1.common.Visibility.LIMITED, e.getVisibility());
                foundLimited = true;
            }
        }

        assertTrue(foundPublic);
        assertTrue(foundLimited);

        assertNotNull(person.getKeywords());
        assertNotNull(person.getKeywords().getKeywords());
        assertEquals(2, person.getKeywords().getKeywords().size());
        assertThat(person.getKeywords().getKeywords().get(0).getContent(), anyOf(is("keyword-1"), is("keyword-2")));
        assertThat(person.getKeywords().getKeywords().get(1).getContent(), anyOf(is("keyword-1"), is("keyword-2")));
        assertEquals(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC, person.getKeywords().getKeywords().get(0).getVisibility());
        assertEquals(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC, person.getKeywords().getKeywords().get(1).getVisibility());
        assertNotNull(person.getOtherNames());
        assertNotNull(person.getOtherNames().getOtherNames());
        assertEquals(2, person.getOtherNames().getOtherNames().size());
        assertThat(person.getOtherNames().getOtherNames().get(0).getContent(), anyOf(is("other-name-1"), is("other-name-2")));
        assertThat(person.getOtherNames().getOtherNames().get(1).getContent(), anyOf(is("other-name-1"), is("other-name-2")));
        assertEquals(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC, person.getOtherNames().getOtherNames().get(0).getVisibility());
        assertEquals(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC, person.getOtherNames().getOtherNames().get(1).getVisibility());
        assertNotNull(person.getResearcherUrls());
        assertNotNull(person.getResearcherUrls().getResearcherUrls());
        assertEquals(2, person.getResearcherUrls().getResearcherUrls().size());
        assertThat(person.getResearcherUrls().getResearcherUrls().get(0).getUrl().getValue(), anyOf(is(researcherUrl1), is(researcherUrl2)));
        assertThat(person.getResearcherUrls().getResearcherUrls().get(1).getUrl().getValue(), anyOf(is(researcherUrl1), is(researcherUrl2)));
        assertEquals(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC, person.getResearcherUrls().getResearcherUrls().get(0).getVisibility());        
        assertEquals(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC, person.getResearcherUrls().getResearcherUrls().get(1).getVisibility());
        assertNotNull(person.getName());
        assertEquals(getUser1GivenName(), person.getName().getGivenNames().getContent());
        assertNotNull(person.getName().getFamilyName());
        assertEquals(getUser1FamilyNames(), person.getName().getFamilyName().getContent());
        assertNotNull(person.getName().getCreditName());
        assertEquals(getUser1CreditName(), person.getName().getCreditName().getContent());
        assertEquals(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC, person.getName().getVisibility());
    }

    @Test
    public void testViewPersonFromPublicAPI() {
        ClientResponse response = publicV3ApiClient.viewPersonXML(getUser1OrcidId());
        assertNotNull(response);
        org.orcid.jaxb.model.v3.dev1.record.Person person = response.getEntity(org.orcid.jaxb.model.v3.dev1.record.Person.class);
        assertNotNull(person);
        assertNotNull(person.getAddresses());
        assertNotNull(person.getAddresses().getAddress());
        assertEquals(1, person.getAddresses().getAddress().size());
        assertNotNull(person.getAddresses().getAddress().get(0).getCountry());
        assertEquals(org.orcid.jaxb.model.v3.dev1.common.Iso3166Country.US, person.getAddresses().getAddress().get(0).getCountry().getValue());
        assertEquals(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC, person.getAddresses().getAddress().get(0).getVisibility());

        assertNotNull(person.getBiography());
        assertEquals(getUser1Bio(), person.getBiography().getContent());
        assertEquals(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC, person.getBiography().getVisibility());

        assertNotNull(person.getEmails());
        EmailTest.assertListContainsEmail(getUser1UserName(), org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC,
                person.getEmails());

        assertNotNull(person.getExternalIdentifiers());
        assertNotNull(person.getExternalIdentifiers().getExternalIdentifiers());
        assertEquals(1, person.getExternalIdentifiers().getExternalIdentifiers().size());
        assertEquals("test", person.getExternalIdentifiers().getExternalIdentifiers().get(0).getType());
        assertEquals("A-0001", person.getExternalIdentifiers().getExternalIdentifiers().get(0).getValue());
        assertEquals(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC, person.getExternalIdentifiers().getExternalIdentifiers().get(0).getVisibility());

        assertNotNull(person.getKeywords());
        assertNotNull(person.getKeywords().getKeywords());
        assertEquals(2, person.getKeywords().getKeywords().size());
        assertThat(person.getKeywords().getKeywords().get(0).getContent(), anyOf(is("keyword-1"), is("keyword-2")));
        assertThat(person.getKeywords().getKeywords().get(1).getContent(), anyOf(is("keyword-1"), is("keyword-2")));
        assertEquals(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC, person.getKeywords().getKeywords().get(0).getVisibility());
        assertEquals(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC, person.getKeywords().getKeywords().get(1).getVisibility());
        assertNotNull(person.getOtherNames());
        assertNotNull(person.getOtherNames().getOtherNames());
        assertEquals(2, person.getOtherNames().getOtherNames().size());
        assertThat(person.getOtherNames().getOtherNames().get(0).getContent(), anyOf(is("other-name-1"), is("other-name-2")));
        assertThat(person.getOtherNames().getOtherNames().get(1).getContent(), anyOf(is("other-name-1"), is("other-name-2")));
        assertEquals(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC, person.getOtherNames().getOtherNames().get(0).getVisibility());
        assertEquals(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC, person.getOtherNames().getOtherNames().get(1).getVisibility());
        assertNotNull(person.getResearcherUrls());
        assertNotNull(person.getResearcherUrls().getResearcherUrls());
        assertEquals(2, person.getResearcherUrls().getResearcherUrls().size());
        assertThat(person.getResearcherUrls().getResearcherUrls().get(0).getUrl().getValue(), anyOf(is(researcherUrl1), is(researcherUrl2)));
        assertThat(person.getResearcherUrls().getResearcherUrls().get(1).getUrl().getValue(), anyOf(is(researcherUrl1), is(researcherUrl2)));
        assertEquals(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC, person.getResearcherUrls().getResearcherUrls().get(0).getVisibility());        
        assertEquals(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC, person.getResearcherUrls().getResearcherUrls().get(1).getVisibility());
        assertNotNull(person.getName());
        assertEquals(getUser1GivenName(), person.getName().getGivenNames().getContent());
        assertNotNull(person.getName().getFamilyName());
        assertEquals(getUser1FamilyNames(), person.getName().getFamilyName().getContent());
        assertNotNull(person.getName().getCreditName());
        assertEquals(getUser1CreditName(), person.getName().getCreditName().getContent());
        assertEquals(org.orcid.jaxb.model.v3.dev1.common.Visibility.PUBLIC, person.getName().getVisibility());
    }
    
    public String getAccessToken() throws InterruptedException, JSONException {
        return getAccessToken(getScopes(ScopePathType.PERSON_READ_LIMITED));
    }
}
