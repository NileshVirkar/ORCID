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
<#import "email_macros.ftl" as emailMacros />
<@emailMacros.msg "email.common.dear" /><@emailMacros.space />${emailName}<@emailMacros.msg "email.common.dear.comma" />

<@emailMacros.msg "email.api_record_creation.creaded_an_account.1" />${creatorName}<@emailMacros.space /><@emailMacros.msg "email.api_record_creation.creaded_an_account.2" />

<@emailMacros.msg "email.api_record_creation.what" />

<@emailMacros.msg "email.api_record_creation.within.1" /><@emailMacros.space />${creatorName}<@emailMacros.space /><@emailMacros.msg "email.api_record_creation.within.2" />

${verificationUrl}?lang=${locale}

<@emailMacros.msg "email.api_record_creation.what_happens" />

<@emailMacros.msg "email.api_record_creation.if_you_take_no.1" /><@emailMacros.space />${creatorName}<@emailMacros.space /><@emailMacros.msg "email.api_record_creation.if_you_take_no.2" />

<@emailMacros.msg "email.api_record_creation.what_is_orcid" />

<@emailMacros.msg "email.api_record_creation.launched.1" />${baseUri}/home?lang=${locale}<@emailMacros.msg "email.api_record_creation.launched.2" />

<@emailMacros.msg "email.api_record_creation.read_privacy.1" /><@emailMacros.space />${baseUri}/privacy-policy/?lang=${locale}<@emailMacros.msg "email.api_record_creation.read_privacy.2" />
<@emailMacros.msg "email.common.if_you_have_any1" /><@emailMacros.knowledgeBaseUri /><@emailMacros.msg "email.common.if_you_have_any2" />

<@emailMacros.msg "email.common.kind_regards" />
${baseUri}

<@emailMacros.msg "email.api_record_creation.you_have_received.1" />${baseUri}/home?lang=${locale}<@emailMacros.msg "email.api_record_creation.you_have_received.2" />
<#include "email_footer.ftl"/>
