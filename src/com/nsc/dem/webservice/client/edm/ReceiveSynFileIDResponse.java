
package com.nsc.dem.webservice.client.edm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for receiveSynFileIDResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="receiveSynFileIDResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="out" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "receiveSynFileIDResponse", propOrder = {
    "out"
})
public class ReceiveSynFileIDResponse {

    protected boolean out;

    /**
     * Gets the value of the out property.
     * 
     */
    public boolean isOut() {
        return out;
    }

    /**
     * Sets the value of the out property.
     * 
     */
    public void setOut(boolean value) {
        this.out = value;
    }

}
