
package com.nsc.dem.webservice.client.edm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for receivePartServersInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="receivePartServersInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ftpContent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="intenterContent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pwd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "receivePartServersInfo", propOrder = {
    "ftpContent",
    "intenterContent",
    "pwd"
})
public class ReceivePartServersInfo {

    protected String ftpContent;
    protected String intenterContent;
    protected String pwd;

    /**
     * Gets the value of the ftpContent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFtpContent() {
        return ftpContent;
    }

    /**
     * Sets the value of the ftpContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFtpContent(String value) {
        this.ftpContent = value;
    }

    /**
     * Gets the value of the intenterContent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIntenterContent() {
        return intenterContent;
    }

    /**
     * Sets the value of the intenterContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIntenterContent(String value) {
        this.intenterContent = value;
    }

    /**
     * Gets the value of the pwd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * Sets the value of the pwd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPwd(String value) {
        this.pwd = value;
    }

}
