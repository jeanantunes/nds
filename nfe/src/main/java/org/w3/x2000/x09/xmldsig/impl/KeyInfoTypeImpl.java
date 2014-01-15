/*
 * XML Type:  KeyInfoType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.KeyInfoType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;
/**
 * An XML KeyInfoType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public class KeyInfoTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.KeyInfoType
{
    
    public KeyInfoTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName X509DATA$0 = 
        new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "X509Data");
    private static final javax.xml.namespace.QName ID$2 = 
        new javax.xml.namespace.QName("", "Id");
    
    
    /**
     * Gets the "X509Data" element
     */
    public org.w3.x2000.x09.xmldsig.X509DataType getX509Data()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.X509DataType target = null;
            target = (org.w3.x2000.x09.xmldsig.X509DataType)get_store().find_element_user(X509DATA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "X509Data" element
     */
    public void setX509Data(org.w3.x2000.x09.xmldsig.X509DataType x509Data)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.X509DataType target = null;
            target = (org.w3.x2000.x09.xmldsig.X509DataType)get_store().find_element_user(X509DATA$0, 0);
            if (target == null)
            {
                target = (org.w3.x2000.x09.xmldsig.X509DataType)get_store().add_element_user(X509DATA$0);
            }
            target.set(x509Data);
        }
    }
    
    /**
     * Appends and returns a new empty "X509Data" element
     */
    public org.w3.x2000.x09.xmldsig.X509DataType addNewX509Data()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.X509DataType target = null;
            target = (org.w3.x2000.x09.xmldsig.X509DataType)get_store().add_element_user(X509DATA$0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ID$2);
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
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(ID$2);
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
            return get_store().find_attribute_user(ID$2) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ID$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ID$2);
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
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(ID$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlID)get_store().add_attribute_user(ID$2);
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
            get_store().remove_attribute(ID$2);
        }
    }
}
