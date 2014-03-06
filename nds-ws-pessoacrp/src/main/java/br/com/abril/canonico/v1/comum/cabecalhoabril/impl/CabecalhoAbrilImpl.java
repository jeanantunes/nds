/*
 * XML Type:  CabecalhoAbril
 * Namespace: http://canonico.abril.com.br/v1/Comum/CabecalhoAbril
 * Java type: br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril
 *
 * Automatically generated - do not modify.
 */
package br.com.abril.canonico.v1.comum.cabecalhoabril.impl;
/**
 * An XML CabecalhoAbril(@http://canonico.abril.com.br/v1/Comum/CabecalhoAbril).
 *
 * This is a complex type.
 */
public class CabecalhoAbrilImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril
{
    
    public CabecalhoAbrilImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName REQUESTMESSAGEID$0 = 
        new javax.xml.namespace.QName("", "requestMessageID");
    private static final javax.xml.namespace.QName SISTEMAORIGEM$2 = 
        new javax.xml.namespace.QName("", "sistemaOrigem");
    
    
    /**
     * Gets the "requestMessageID" element
     */
    public java.lang.String getRequestMessageID()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(REQUESTMESSAGEID$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "requestMessageID" element
     */
    public org.apache.xmlbeans.XmlString xgetRequestMessageID()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(REQUESTMESSAGEID$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "requestMessageID" element
     */
    public void setRequestMessageID(java.lang.String requestMessageID)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(REQUESTMESSAGEID$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(REQUESTMESSAGEID$0);
            }
            target.setStringValue(requestMessageID);
        }
    }
    
    /**
     * Sets (as xml) the "requestMessageID" element
     */
    public void xsetRequestMessageID(org.apache.xmlbeans.XmlString requestMessageID)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(REQUESTMESSAGEID$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(REQUESTMESSAGEID$0);
            }
            target.set(requestMessageID);
        }
    }
    
    /**
     * Gets the "sistemaOrigem" element
     */
    public java.lang.String getSistemaOrigem()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SISTEMAORIGEM$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "sistemaOrigem" element
     */
    public org.apache.xmlbeans.XmlString xgetSistemaOrigem()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SISTEMAORIGEM$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "sistemaOrigem" element
     */
    public void setSistemaOrigem(java.lang.String sistemaOrigem)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SISTEMAORIGEM$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SISTEMAORIGEM$2);
            }
            target.setStringValue(sistemaOrigem);
        }
    }
    
    /**
     * Sets (as xml) the "sistemaOrigem" element
     */
    public void xsetSistemaOrigem(org.apache.xmlbeans.XmlString sistemaOrigem)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SISTEMAORIGEM$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SISTEMAORIGEM$2);
            }
            target.set(sistemaOrigem);
        }
    }
}
