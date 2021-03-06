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

public interface StatisticsGeneratorDao {

    public long getLiveIds();
    
    public long getAccountsWithEducation();
    
    public long getAccountsWithEmployment();
    
    public long getAccountsWithExternalId();
    
    public long getAccountsWithFunding();
    
    public long getAccountsWithPeerReview();
    
    public long getAccountsWithPersonId();

    public long getAccountsWithVerifiedEmails();

    public long getAccountsWithWorks();

    public long getNumberOfWorks();

    public long getNumberOfUniqueDOIs();

    long getNumberOfEmployment();

    long getNumberOfEducation();

    long getNumberOfFunding();
    
    public long getNumberOfPeerReview();
    
    public long getNumberOfPersonId();

    long getNumberOfEmploymentUniqueOrg();

    long getNumberOfEducationUniqueOrg();

    long getNumberOfFundingUniqueOrg();
}
