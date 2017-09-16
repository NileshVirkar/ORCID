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
	<title>
        <@emailMacros.msg "email.admin_delegate_request.title.1" /><@emailMacros.space />${trustedOrcidName}<@emailMacros.space /><@emailMacros.msg "email.admin_delegate_request.title.2" />
    </title>
	</head>
	<body>
		<div style="padding: 20px; padding-top: 0px;">
			<img src="https://orcid.org/sites/all/themes/orcid/img/orcid-logo.png" alt="ORCID.org"/>
		    <hr />
		  	<span style="font-family: arial, helvetica, sans-serif; font-size: 15px; color: #666666; font-weight: bold;">
			   <@emailMacros.msg "email.common.dear" /><@emailMacros.space />${emailNameForDelegate}<@emailMacros.msg "email.common.dear.comma" />
		    </span>
		    <p style="font-family: arial, helvetica, sans-serif; font-size: 15px; color: #666666;">
				<@emailMacros.msg "email.admin_delegate_request.you_have.1" /><@emailMacros.space />${trustedOrcidName}
                <@emailMacros.space /><@emailMacros.msg "email.admin_delegate_request.you_have.2" />${baseUri}/${trustedOrcidValue}<@emailMacros.msg "email.admin_delegate_request.you_have.3" />
                ${trustedOrcidValue}<@emailMacros.msg "email.admin_delegate_request.you_have.4" />${baseUri}/${managedOrcidValue}<@emailMacros.msg "email.admin_delegate_request.you_have.5" />${managedOrcidValue}<@emailMacros.msg "email.admin_delegate_request.you_have.6" />
		    </p>		   
		    <p style="font-family: arial, helvetica, sans-serif; font-size: 15px; color: #666666;">
		    	<@emailMacros.space /><@emailMacros.msg "email.admin_delegate_request.you_have.7" />
			</p>
		    <p style="font-family: arial, helvetica, sans-serif; font-size: 15px; color: #666666;">
		    	<@emailMacros.msg "email.admin_delegate_request.you_have.8" /><br /><a href="${link}" target="orcid.blank">${link}</a>
		    </p>		     
		    <p style="font-family: arial, helvetica, sans-serif; font-size: 15px; color: #666666;">
                <@emailMacros.msg "email.admin_delegate_request.for_a_tutorial" />
		    </p>
		    <p style="font-family: arial, helvetica, sans-serif; font-size: 15px; color: #666666; white-space: pre;">
<@emailMacros.msg "email.admin_delegate_request.kind_regards" />
<a href="${baseUri}/home?lang=${locale}">${baseUri}/</a>		
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
