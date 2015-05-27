/*
 * XML Type:  SignedInfoType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SignedInfoType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;
/**
 * An XML SignedInfoType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public class SignedInfoTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.SignedInfoType
{
    
    public SignedInfoTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CANONICALIZATIONMETHOD$0 = 
        new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "CanonicalizationMethod");
    private static final javax.xml.namespace.QName SIGNATUREMETHOD$2 = 
        new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SignatureMethod");
    private static final javax.xml.namespace.QName REFERENCE$4 = 
        new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "Reference");
    private static final javax.xml.namespace.QName ID$6 = 
        new javax.xml.namespace.QName("", "Id");
    
    
    /**
     * Gets the "CanonicalizationMethod" element
     */
    public org.w3.x2000.x09.xmldsig.SignedInfoType.CanonicalizationMethod getCanonicalizationMethod()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignedInfoType.CanonicalizationMethod target = null;
            target = (org.w3.x2000.x09.xmldsig.SignedInfoType.CanonicalizationMethod)get_store().find_element_user(CANONICALIZATIONMETHOD$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "CanonicalizationMethod" element
     */
    public void setCanonicalizationMethod(org.w3.x2000.x09.xmldsig.SignedInfoType.CanonicalizationMethod canonicalizationMethod)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignedInfoType.CanonicalizationMethod target = null;
            target = (org.w3.x2000.x09.xmldsig.SignedInfoType.CanonicalizationMethod)get_store().find_element_user(CANONICALIZATIONMETHOD$0, 0);
            if (target == null)
            {
                target = (org.w3.x2000.x09.xmldsig.SignedInfoType.CanonicalizationMethod)get_store().add_element_user(CANONICALIZATIONMETHOD$0);
            }
            target.set(canonicalizationMethod);
        }
    }
    
    /**
     * Appends and returns a new empty "CanonicalizationMethod" element
     */
    public org.w3.x2000.x09.xmldsig.SignedInfoType.CanonicalizationMethod addNewCanonicalizationMethod()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignedInfoType.CanonicalizationMethod target = null;
            target = (org.w3.x2000.x09.xmldsig.SignedInfoType.CanonicalizationMethod)get_store().add_element_user(CANONICALIZATIONMETHOD$0);
            return target;
        }
    }
    
    /**
     * Gets the "SignatureMethod" element
     */
    public org.w3.x2000.x09.xmldsig.SignedInfoType.SignatureMethod getSignatureMethod()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignedInfoType.SignatureMethod target = null;
            target = (org.w3.x2000.x09.xmldsig.SignedInfoType.SignatureMethod)get_store().find_element_user(SIGNATUREMETHOD$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "SignatureMethod" element
     */
    public void setSignatureMethod(org.w3.x2000.x09.xmldsig.SignedInfoType.SignatureMethod signatureMethod)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignedInfoType.SignatureMethod target = null;
            target = (org.w3.x2000.x09.xmldsig.SignedInfoType.SignatureMethod)get_store().find_element_user(SIGNATUREMETHOD$2, 0);
            if (target == null)
            {
                target = (org.w3.x2000.x09.xmldsig.SignedInfoType.SignatureMethod)get_store().add_element_user(SIGNATUREMETHOD$2);
            }
            target.set(signatureMethod);
        }
    }
    
    /**
     * Appends and returns a new empty "SignatureMethod" element
     */
    public org.w3.x2000.x09.xmldsig.SignedInfoType.SignatureMethod addNewSignatureMethod()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignedInfoType.SignatureMethod target = null;
            target = (org.w3.x2000.x09.xmldsig.SignedInfoType.SignatureMethod)get_store().add_element_user(SIGNATUREMETHOD$2);
            return target;
        }
    }
    
    /**
     * Gets the "Reference" element
     */
    public org.w3.x2000.x09.xmldsig.ReferenceType getReference()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.ReferenceType target = null;
            target = (org.w3.x2000.x09.xmldsig.ReferenceType)get_store().find_element_user(REFERENCE$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Reference" element
     */
    public void setReference(org.w3.x2000.x09.xmldsig.ReferenceType reference)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.ReferenceType target = null;
            target = (org.w3.x2000.x09.xmldsig.ReferenceType)get_store().find_element_user(REFERENCE$4, 0);
            if (target == null)
            {
                target = (org.w3.x2000.x09.xmldsig.ReferenceType)get_store().add_element_user(REFERENCE$4);
            }
            target.set(reference);
        }
    }
    
    /**
     * Appends and returns a new empty "Reference" element
     */
    public org.w3.x2000.x09.xmldsig.ReferenceType addNewReference()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.ReferenceType target = null;
            target = (org.w3.x2000.x09.xmldsig.ReferenceType)get_store().add_element_user(REFERENCE$4);
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
    /**
     * An XML CanonicalizationMethod(@http://www.w3.org/2000/09/xmldsig#).
     *
     * This is a complex type.
     */
    public static class CanonicalizationMethodImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.SignedInfoType.CanonicalizationMethod
    {
        
        public CanonicalizationMethodImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName ALGORITHM$0 = 
            new javax.xml.namespace.QName("", "Algorithm");
        
        
        /**
         * Gets the "Algorithm" attribute
         */
        public java.lang.String getAlgorithm()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ALGORITHM$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(ALGORITHM$0);
                }
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "Algorithm" attribute
         */
        public org.apache.xmlbeans.XmlAnyURI xgetAlgorithm()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlAnyURI target = null;
                target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_attribute_user(ALGORITHM$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlAnyURI)get_default_attribute_value(ALGORITHM$0);
                }
                return target;
            }
        }
        
        /**
         * Sets the "Algorithm" attribute
         */
        public void setAlgorithm(java.lang.String algorithm)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ALGORITHM$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ALGORITHM$0);
                }
                target.setStringValue(algorithm);
            }
        }
        
        /**
         * Sets (as xml) the "Algorithm" attribute
         */
        public void xsetAlgorithm(org.apache.xmlbeans.XmlAnyURI algorithm)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlAnyURI target = null;
                target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_attribute_user(ALGORITHM$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlAnyURI)get_store().add_attribute_user(ALGORITHM$0);
                }
                target.set(algorithm);
            }
        }
    }
    /**
     * An XML SignatureMethod(@http://www.w3.org/2000/09/xmldsig#).
     *
     * This is a complex type.
     */
    public static class SignatureMethodImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.SignedInfoType.SignatureMethod
    {
        
        public SignatureMethodImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName ALGORITHM$0 = 
            new javax.xml.namespace.QName("", "Algorithm");
        
        
        /**
         * Gets the "Algorithm" attribute
         */
        public java.lang.String getAlgorithm()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ALGORITHM$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(ALGORITHM$0);
                }
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "Algorithm" attribute
         */
        public org.apache.xmlbeans.XmlAnyURI xgetAlgorithm()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlAnyURI target = null;
                target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_attribute_user(ALGORITHM$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlAnyURI)get_default_attribute_value(ALGORITHM$0);
                }
                return target;
            }
        }
        
        /**
         * Sets the "Algorithm" attribute
         */
        public void setAlgorithm(java.lang.String algorithm)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ALGORITHM$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ALGORITHM$0);
                }
                target.setStringValue(algorithm);
            }
        }
        
        /**
         * Sets (as xml) the "Algorithm" attribute
         */
        public void xsetAlgorithm(org.apache.xmlbeans.XmlAnyURI algorithm)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlAnyURI target = null;
                target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_attribute_user(ALGORITHM$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlAnyURI)get_store().add_attribute_user(ALGORITHM$0);
                }
                target.set(algorithm);
            }
        }
    }
}
