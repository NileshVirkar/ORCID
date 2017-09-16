<#--

    =============================================================================

    ORCID (R) Open Source
    http://orcid.org

    Copyright (c) 2012-2014 ORCID, Inc.
    Licensed under an MIT-Style License (MIT)
    http://orcid.org/open-source-license

    This copyright and license information (including a link to the full license)
    shall be included in its entirety in all copies or substantial portion of
    the software.

    =============================================================================

-->
<@base>
<div class="container">
<div class="row">
    <div class="col-md-offset-3 col-md-6 col-sm-offset-3 col-sm-6 col-xs-12">
        <div class="alert">${springMacroRequestContext.getMessage("session_expired.labelsessionexpired")} <a href="<@orcid.rootPath '/signin'/>">${springMacroRequestContext.getMessage("header.signin")}</a> ${springMacroRequestContext.getMessage("session_expired.labeltryagain")}</p>
    </div>
</div>
</div>
</@base>