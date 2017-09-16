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
package org.orcid.pojo;



public class AdminChangePassword {
    private String password;

    private String orcidOrEmail;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOrcidOrEmail() {
        return orcidOrEmail;
    }

    public void setOrcidOrEmail(String orcidOrEmail) {        
        this.orcidOrEmail = orcidOrEmail;
    }

}
