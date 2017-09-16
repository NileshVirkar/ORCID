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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 * @author Will Simpson
 * 
 */
public enum IndexingStatus {

    PENDING, DONE, REINDEX, IGNORE, FAILED;

    public static Object getNames(Collection<IndexingStatus> indexingStatuses) {
        List<String> names = new ArrayList<>();
        for (IndexingStatus indexingStatus : indexingStatuses) {
            names.add(indexingStatus.name());
        }
        return names;
    }

}
