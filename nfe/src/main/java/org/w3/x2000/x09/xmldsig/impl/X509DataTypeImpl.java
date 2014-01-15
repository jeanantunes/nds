/*
 * XML Type:  X509DataType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.X509DataType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;
/**
 * An XML X509DataType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public class X509DataTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.X509DataType
{
    
    public X509DataTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName X509CERTIFICATE$0 = 
        new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "X509Certificate");
    
    
    /**
     * Gets the "X509Certificate" element
     */
    public byte[] getX509Certificate()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(X509CERTIFICATE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getByteArrayValue();
        }
    }
    
    /**
     * Gets (as xml) the "X509Certificate" element
     */
    public org.apache.xmlbeans.XmlBase64Binary xgetX509Certificate()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(X509CERTIFICATE$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "X509Certificate" element
     */
    public void setX509Certificate(byte[] x509Certificate)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(X509CERTIFICATE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(X509CERTIFICATE$0);
            }
            target.setByteArrayValue(x509Certificate);
        }
    }
    
    /**
     * Sets (as xml) the "X509Certificate" element
     */
    public void xsetX509Certificate(org.apache.xmlbeans.XmlBase64Binary x509Certificate)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(X509CERTIFICATE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_element_user(X509CERTIFICATE$0);
            }
            target.set(x509Certificate);
        }
    }
}
