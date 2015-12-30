
package com.nsc.dem.webservice.client.edm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for saveServersInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="saveServersInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="unitCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unitName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ftpIp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ftpLonginName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ftpLoginPass" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ftpPort" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="wsAdd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "saveServersInfo", propOrder = {
    "unitCode",
    "unitName",
    "ftpIp",
    "ftpLonginName",
    "ftpLoginPass",
    "ftpPort",
    "wsAdd",
    "pwd"
})
public class SaveServersInfo {

    protected String unitCode;
    protected String unitName;
    protected String ftpIp;
    protected String ftpLonginName;
    protected String ftpLoginPass;
    protected String ftpPort;
    protected String wsAdd;
    protected String pwd;

    /**
     * Gets the value of the unitCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitCode() {
        return unitCode;
    }

    /**
     * Sets the value of the unitCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitCode(String value) {
        this.unitCode = value;
    }

    /**
     * Gets the value of the unitName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * Sets the value of the unitName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitName(String value) {
        this.unitName = value;
    }

    /**
     * Gets the value of the ftpIp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFtpIp() {
        return ftpIp;
    }

    /**
     * Sets the value of the ftpIp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFtpIp(String value) {
        this.ftpIp = value;
    }

    /**
     * Gets the value of the ftpLonginName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFtpLonginName() {
        return ftpLonginName;
    }

    /**
     * Sets the value of the ftpLonginName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFtpLonginName(String value) {
        this.ftpLonginName = value;
    }

    /**
     * Gets the value of the ftpLoginPass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFtpLoginPass() {
        return ftpLoginPass;
    }

    /**
     * Sets the value of the ftpLoginPass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFtpLoginPass(String value) {
        this.ftpLoginPass = value;
    }

    /**
     * Gets the value of the ftpPort property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFtpPort() {
        return ftpPort;
    }

    /**
     * Sets the value of the ftpPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFtpPort(String value) {
        this.ftpPort = value;
    }

    /**
     * Gets the value of the wsAdd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWsAdd() {
        return wsAdd;
    }

    /**
     * Sets the value of the wsAdd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWsAdd(String value) {
        this.wsAdd = value;
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
