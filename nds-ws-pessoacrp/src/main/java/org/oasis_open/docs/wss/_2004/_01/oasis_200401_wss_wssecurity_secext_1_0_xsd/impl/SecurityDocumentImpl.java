/*
 * An XML document type.
 * Localname: Security
 * Namespace: http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd
 * Java type: org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0_xsd.SecurityDocument
 *
 * Automatically generated - do not modify.
 */
package org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0_xsd.impl;
/**
 * A document containing one Security(@http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd) element.
 *
 * This is a complex type.
 */
public class SecurityDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0_xsd.SecurityDocument
{
    
    public SecurityDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SECURITY$0 = 
        new javax.xml.namespace.QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security");
    
    
    /**
     * Gets the "Security" element
     */
    public org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0_xsd.SecurityHeaderType getSecurity()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0_xsd.SecurityHeaderType target = null;
            target = (org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0_xsd.SecurityHeaderType)get_store().find_element_user(SECURITY$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Security" element
     */
    public void setSecurity(org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0_xsd.SecurityHeaderType security)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0_xsd.SecurityHeaderType target = null;
            target = (org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0_xsd.SecurityHeaderType)get_store().find_element_user(SECURITY$0, 0);
            if (target == null)
            {
                target = (org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0_xsd.SecurityHeaderType)get_store().add_element_user(SECURITY$0);
            }
            target.set(security);
        }
    }
    
    /**
     * Appends and returns a new empty "Security" element
     */
    public org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0_xsd.SecurityHeaderType addNewSecurity()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0_xsd.SecurityHeaderType target = null;
            target = (org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0_xsd.SecurityHeaderType)get_store().add_element_user(SECURITY$0);
            return target;
        }
    }
}
