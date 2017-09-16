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
package org.orcid.persistence.jpa.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author Angel Montenegro
 * 
 */
@MappedSuperclass
public abstract class SourceAwareEntity<T extends Serializable> extends BaseEntity<T> {
    private static final long serialVersionUID = -5397119397438830995L;
    protected String sourceId;
    protected String clientSourceId;    

    @Column(name = "source_id")
    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    @Column(name = "client_source_id")
    public String getClientSourceId() {
        return clientSourceId;
    }

    public void setClientSourceId(String clientSourceId) {
        this.clientSourceId = clientSourceId;
    }    
    
    /**
     * Get the element source id, helpful when we just need the id, not the complete source element
     * */
    @Transient
    public String getElementSourceId() {
        if(!StringUtils.isEmpty(clientSourceId)) {
            return clientSourceId;
        } else if(!StringUtils.isEmpty(sourceId)) {
            return sourceId;
        }
        return null;
    }    
}
