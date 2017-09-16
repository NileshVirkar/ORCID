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
package org.orcid.pojo.ajaxForm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.orcid.persistence.jpa.entities.CustomEmailEntity;
import org.orcid.persistence.jpa.entities.EmailType;

public class CustomEmailForm implements ErrorsInterface, Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> errors = new ArrayList<String>();
    private Text sender;
    private Text subject;
    private Text content;
    private Text emailType;
    private String clientId;
    private boolean isHtml;

    public CustomEmailEntity toCustomEmailEntity() {
        CustomEmailEntity entity = new CustomEmailEntity();
        if (this.sender != null)
            entity.setSender(this.sender.getValue());
        if (this.content != null)
            entity.setContent(this.content.getValue());
        if (this.subject != null)
            entity.setSubject(this.subject.getValue());
        if (this.emailType != null)
            entity.setEmailType(EmailType.valueOf(this.emailType.getValue()));
        if (this.isHtml)
            entity.setHtml(isHtml);
        return entity;
    }

    public static CustomEmailForm valueOf(CustomEmailEntity entity) {
        CustomEmailForm result = new CustomEmailForm();
        result.setSender(Text.valueOf(entity.getSender()));
        result.setSubject(Text.valueOf(entity.getSubject()));
        result.setContent(Text.valueOf(entity.getContent()));
        result.setEmailType(Text.valueOf(entity.getEmailType().name()));
        result.setHtml(entity.isHtml());
        result.setClientId(entity.getClientDetailsEntity().getClientId());
        return result;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public Text getSender() {
        return sender;
    }

    public void setSender(Text sender) {
        this.sender = sender;
    }

    public Text getSubject() {
        return subject;
    }

    public void setSubject(Text subject) {
        this.subject = subject;
    }

    public Text getContent() {
        return content;
    }

    public void setContent(Text content) {
        this.content = content;
    }

    public Text getEmailType() {
        return emailType;
    }

    public void setEmailType(Text emailType) {
        this.emailType = emailType;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public void setHtml(boolean isHtml) {
        this.isHtml = isHtml;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }        
}
