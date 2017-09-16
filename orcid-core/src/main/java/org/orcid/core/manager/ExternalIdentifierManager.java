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

import org.orcid.core.manager.read_only.ExternalIdentifierManagerReadOnly;
import org.orcid.jaxb.model.record_v2.PersonExternalIdentifier;
import org.orcid.jaxb.model.record_v2.PersonExternalIdentifiers;

public interface ExternalIdentifierManager extends ExternalIdentifierManagerReadOnly {
    /**
     * Add a new external identifier to a specific profile
     * 
     * @param orcid
     * @param externalIdentifier
     * @return true if the external identifier was successfully created on
     *         database
     */
    PersonExternalIdentifier createExternalIdentifier(String orcid, PersonExternalIdentifier externalIdentifier, boolean isApiRequest);

    /**
     * Updates an existing external identifier
     * 
     * @param orcid
     * @param externalIdentifier
     * @return the updated external identifier
     */
    PersonExternalIdentifier updateExternalIdentifier(String orcid, PersonExternalIdentifier externalIdentifier, boolean isApiRequest);

    /**
     * Deletes an external identifier
     * 
     * @param orcid
     * @param id
     */
    boolean deleteExternalIdentifier(String orcid, Long id, boolean checkSource);
    
    PersonExternalIdentifiers updateExternalIdentifiers(String orcid, PersonExternalIdentifiers externalIdentifiers);
    
    /**
     * Removes all external identifiers that belongs to a given record. Careful!
     * 
     * @param orcid
     *            The ORCID iD of the record from which all external identifiers will be
     *            removed.
     */
    void removeAllExternalIdentifiers(String orcid);
}
