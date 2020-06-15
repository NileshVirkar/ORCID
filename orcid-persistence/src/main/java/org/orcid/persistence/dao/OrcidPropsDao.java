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

/**
 * 
 * @author Angel Montenegro
 * 
 */
public interface OrcidPropsDao {

    boolean create(String key, String value);
    boolean update(String key, String value);
    boolean exists(String key);
    String getValue(String key);    
}