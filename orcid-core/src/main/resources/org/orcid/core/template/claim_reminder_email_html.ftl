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
<#escape x as x?html>
<!DOCTYPE html>
<html>
	<head>
	<title>${subject}</title>
	</head>
	<body>
		<div style="padding: 20px; padding-top: 0px;">
			<img src="https://orcid.org/sites/all/themes/orcid/img/orcid-logo.png" alt="ORCID.org"/>
		    <hr />
		  	<span style="font-family: arial, helvetica, sans-serif; font-size: 15px; color: #666666; font-weight: bold;">
		      <@emailMacros.msg "email.common.dear" /><@emailMacros.space />${emailName}<@emailMacros.msg "email.common.dear.comma" />
		    </span>
		    <p style="font-family: arial, helvetica, sans-serif; font-size: 15px; color: #666666;">
                <@emailMacros.msg "email.claim_reminder.this_is_a_reminder.1" /><@emailMacros.space />${creatorName}<@emailMacros.space /><@emailMacros.msg "email.claim_reminder.this_is_a_reminder.2" /><@emailMacros.space />${daysUntilActivation}<@emailMacros.space /><@emailMacros.msg "email.claim_reminder.this_is_a_reminder.3" /><@emailMacros.space />${creatorName}<@emailMacros.space /><@emailMacros.msg "email.claim_reminder.this_is_a_reminder.4" />
		    </p>
		    <p style="font-family: arial, helvetica, sans-serif; font-size: 15px; color: #666666;">
		        <@emailMacros.msg "email.claim_reminder.what_do_you" />
 		    </p>
		    <p style="font-family: arial, helvetica, sans-serif; font-size: 15px; color: #666666;">		  
				<@emailMacros.msg "email.claim_reminder.within_the_next.1" /><@emailMacros.space />${daysUntilActivation}<@emailMacros.space /><@emailMacros.msg "email.claim_reminder.within_the_next.2" />${creatorName}<@emailMacros.space /><@emailMacros.msg "email.claim_reminder.within_the_next.3" />
		    </p>
		    <p style="font-family: arial, helvetica, sans-serif; font-size: 15px; color: #666666;">
		  		<a href="${verificationUrl}?lang=${locale}">${verificationUrl}</a>
		    </p>		    
		    <p style="font-family: arial, helvetica, sans-serif; font-size: 15px; color: #666666;">
		        <@emailMacros.msg "email.api_record_creation.what_is_orcid" />
		    </p>
		    <p style="font-family: arial, helvetica, sans-serif; font-size: 15px; color: #666666;">
		        <@emailMacros.msg "email.api_record_creation.launched.1" /><a href="${baseUri}/home?lang=${locale}">${baseUri}</a><@emailMacros.msg "email.api_record_creation.launched.2" />
		    </p>
		    <p style="font-family: arial, helvetica, sans-serif; font-size: 15px; color: #666666;">
		        <@emailMacros.msg "email.api_record_creation.read_privacy.1" /><@emailMacros.space /><a href="${baseUri}/privacy-policy/?lang=${locale}">${baseUri}/privacy-policy/</a><@emailMacros.msg "email.api_record_creation.read_privacy.2" /><@emailMacros.msg "email.common.if_you_have_any1" /><a href="<@emailMacros.knowledgeBaseUri />"><@emailMacros.knowledgeBaseUri /></a><@emailMacros.msg "email.common.if_you_have_any2" />
		    </p>
		  	<p style="font-family: arial,  helvetica, sans-serif;font-size: 15px;color: #666666; white-space: pre;">
<@emailMacros.msg "email.common.kind_regards" />
			</p>
			<p style="font-family: arial,  helvetica, sans-serif;font-size: 15px;color: #666666;">
				<@emailMacros.msg "email.common.you_have_received_this_email" />
			</p>
			<p style="font-family: arial,  helvetica, sans-serif;font-size: 15px;color: #666666;">
			   <#include "email_footer_html.ftl"/>
			</p>
		 </div>
	 </body>
 </html>
 </#escape>
