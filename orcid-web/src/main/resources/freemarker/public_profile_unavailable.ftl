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
<@public>
<div class="row workspace-top public-profile">
    <div class="col-md-3 left-aside">
        <div class="workspace-left workspace-profile">
        	<div class="id-banner">
	            <h2 class="full-name">					
					${(displayName)!}	                	               
	            </h2>					           
	            <div class="oid">
					<div class="id-banner-header">
						<span><@orcid.msg 'common.orcid_id' /></span>
					</div>
					<div class="orcid-id-container">
						<div class="orcid-id-info">
					    	<span class="mini-orcid-icon"></span>
					    	<!-- Reference: orcid.js:removeProtocolString() -->
				       		<span id="orcid-id" class="orcid-id shortURI">${baseDomainRmProtocall}/${(effectiveUserOrcid)!}</span>
						</div>						
					</div>
				</div>
	        </div>
        </div>
    </div>
    
    <div class="col-md-9 right-aside">
        <div class="workspace-right">
        	<#if (locked)?? && locked>
        		<div class="alert alert-error readme">
		        	<p><b id="error_locked"><@orcid.msg 'public-layout.locked'/></b></p>
		        </div>        		
        	<#elseif (deprecated)??>
	        	<div class="alert alert-error readme">
	        		<p><b><@orcid.msg 'public_profile.deprecated_account.1'/>&nbsp;<a href="${baseUriHttp}/${primaryRecord}">${baseUriHttp}/${primaryRecord}</a>&nbsp;<@orcid.msg 'public_profile.deprecated_account.2'/></b></p>
	        	</div>
	        <#elseif (deactivated)??>
	        	<p class="margin-top-box"><b><@orcid.msg 'public_profile.empty_profile'/></b></p>
			</#if>	        	           
        </div>
    </div>
</div>
</@public>