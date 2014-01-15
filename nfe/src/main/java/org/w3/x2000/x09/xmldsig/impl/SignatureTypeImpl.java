/*
 * XML Type:  SignatureType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SignatureType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;
/**
 * An XML SignatureType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public class SignatureTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.SignatureType
{
    private static final long serialVersionUID = 1L;
    
    public SignatureTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SIGNEDINFO$0 = 
        new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SignedInfo");
    private static final javax.xml.namespace.QName SIGNATUREVALUE$2 = 
        new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SignatureValue");
    private static final javax.xml.namespace.QName KEYINFO$4 = 
        new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "KeyInfo");
    private static final javax.xml.namespace.QName ID$6 = 
        new javax.xml.namespace.QName("", "Id");
    
    
    /**
     * Gets the "SignedInfo" element
     */
    public org.w3.x2000.x09.xmldsig.SignedInfoType getSignedInfo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignedInfoType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignedInfoType)get_store().find_element_user(SIGNEDINFO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "SignedInfo" element
     */
    public void setSignedInfo(org.w3.x2000.x09.xmldsig.SignedInfoType signedInfo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignedInfoType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignedInfoType)get_store().find_element_user(SIGNEDINFO$0, 0);
            if (target == null)
            {
                target = (org.w3.x2000.x09.xmldsig.SignedInfoType)get_store().add_element_user(SIGNEDINFO$0);
            }
            target.set(signedInfo);
        }
    }
    
    /**
     * Appends and returns a new empty "SignedInfo" element
     */
    public org.w3.x2000.x09.xmldsig.SignedInfoType addNewSignedInfo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignedInfoType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignedInfoType)get_store().add_element_user(SIGNEDINFO$0);
            return target;
        }
    }
    
    /**
     * Gets the "SignatureValue" element
     */
    public org.w3.x2000.x09.xmldsig.SignatureValueType getSignatureValue()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignatureValueType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignatureValueType)get_store().find_element_user(SIGNATUREVALUE$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "SignatureValue" element
     */
    public void setSignatureValue(org.w3.x2000.x09.xmldsig.SignatureValueType signatureValue)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignatureValueType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignatureValueType)get_store().find_element_user(SIGNATUREVALUE$2, 0);
            if (target == null)
            {
                target = (org.w3.x2000.x09.xmldsig.SignatureValueType)get_store().add_element_user(SIGNATUREVALUE$2);
            }
            target.set(signatureValue);
        }
    }
    
    /**
     * Appends and returns a new empty "SignatureValue" element
     */
    public org.w3.x2000.x09.xmldsig.SignatureValueType addNewSignatureValue()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignatureValueType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignatureValueType)get_store().add_element_user(SIGNATUREVALUE$2);
            return target;
        }
    }
    
    /**
     * Gets the "KeyInfo" element
     */
    public org.w3.x2000.x09.xmldsig.KeyInfoType getKeyInfo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.KeyInfoType target = null;
            target = (org.w3.x2000.x09.xmldsig.KeyInfoType)get_store().find_element_user(KEYINFO$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "KeyInfo" element
     */
    public void setKeyInfo(org.w3.x2000.x09.xmldsig.KeyInfoType keyInfo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.KeyInfoType target = null;
            target = (org.w3.x2000.x09.xmldsig.KeyInfoType)get_store().find_element_user(KEYINFO$4, 0);
            if (target == null)
            {
                target = (org.w3.x2000.x09.xmldsig.KeyInfoType)get_store().add_element_user(KEYINFO$4);
            }
            target.set(keyInfo);
        }
    }
    
    /**
     * Appends and returns a new empty "KeyInfo" element
     */
    public org.w3.x2000.x09.xmldsig.KeyInfoType addNewKeyInfo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.KeyInfoType target = null;
            target = (org.w3.x2000.x09.xmldsig.KeyInfoType)get_store().add_element_user(KEYINFO$4);
            return target;
        }
    }
    
    /**
     * Gets the "Id" attribute
     */
    public java.lang.String getId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ID$6);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Id" attribute
     */
    public org.apache.xmlbeans.XmlID xgetId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlID target = null;
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(ID$6);
            return target;
        }
    }
    
    /**
     * True if has "Id" attribute
     */
    public boolean isSetId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(ID$6) != null;
        }
    }
    
    /**
     * Sets the "Id" attribute
     */
    public void setId(java.lang.String id)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ID$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ID$6);
            }
            target.setStringValue(id);
        }
    }
    
    /**
     * Sets (as xml) the "Id" attribute
     */
    public void xsetId(org.apache.xmlbeans.XmlID id)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlID target = null;
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(ID$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlID)get_store().add_attribute_user(ID$6);
            }
            target.set(id);
        }
    }
    
    /**
     * Unsets the "Id" attribute
     */
    public void unsetId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(ID$6);
        }
    }
}
