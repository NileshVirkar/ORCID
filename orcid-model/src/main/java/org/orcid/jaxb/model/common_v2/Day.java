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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.09 at 01:52:56 PM BST 
//

package org.orcid.jaxb.model.common_v2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.apache.commons.lang.StringUtils;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType( propOrder = { "value" })
@XmlRootElement(name = "day")
public class Day implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @XmlValue
    @ApiModelProperty(example = "1999")
    protected String value;

    public Day() {

    }

    public Day(Integer value) {
        this.value = tidyValue(value);
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = tidyValue(value);
    }

    private String tidyValue(Integer value) {
        return tidyValue(Integer.toString(value));
    }
    
    private String tidyValue(String value) {
        return StringUtils.leftPad(value, 2, '0');
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Day)) {
            return false;
        }

        Day day = (Day) o;

        if (value != null ? !value.equals(day.value) : day.value != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
