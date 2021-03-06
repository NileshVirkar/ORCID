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
package org.orcid.integration.blackbox.client;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 
 * @author Will Simpson
 *
 */
public class LocalXPath {
    
    private WebElement baseElement;

    LocalXPath(WebElement baseElement) {
        this.baseElement = baseElement;
    }

    public WebElement findElement(String xpath) {
        By by = By.xpath(xpath);
        return baseElement.findElement(by);
    }

    List<WebElement> findElements(String xpath) {
        By by = By.xpath(xpath);
        return baseElement.findElements(by);
    }

    public WebElement waitToBeClickable(String xpath) {
        By by = By.xpath(xpath);
        WebElement webElement = baseElement.findElement(by);
        return webElement;
    }

    public void click(String xpath) {
        WebElement webElement = waitToBeClickable(xpath);
        webElement.click();
    }
}
