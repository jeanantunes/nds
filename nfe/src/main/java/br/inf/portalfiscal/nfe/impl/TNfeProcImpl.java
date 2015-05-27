/*
 * XML Type:  TNfeProc
 * Namespace: http://www.portalfiscal.inf.br/nfe
 * Java type: br.inf.portalfiscal.nfe.TNfeProc
 *
 * Automatically generated - do not modify.
 */
package br.inf.portalfiscal.nfe.impl;
/**
 * An XML TNfeProc(@http://www.portalfiscal.inf.br/nfe).
 *
 * This is a complex type.
 */
public class TNfeProcImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.inf.portalfiscal.nfe.TNfeProc
{
    
    public TNfeProcImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NFE$0 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "NFe");
    private static final javax.xml.namespace.QName PROTNFE$2 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "protNFe");
    private static final javax.xml.namespace.QName VERSAO$4 = 
        new javax.xml.namespace.QName("", "versao");
    
    
    /**
     * Gets the "NFe" element
     */
    public br.inf.portalfiscal.nfe.TNFe getNFe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TNFe target = null;
            target = (br.inf.portalfiscal.nfe.TNFe)get_store().find_element_user(NFE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "NFe" element
     */
    public void setNFe(br.inf.portalfiscal.nfe.TNFe nFe)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TNFe target = null;
            target = (br.inf.portalfiscal.nfe.TNFe)get_store().find_element_user(NFE$0, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TNFe)get_store().add_element_user(NFE$0);
            }
            target.set(nFe);
        }
    }
    
    /**
     * Appends and returns a new empty "NFe" element
     */
    public br.inf.portalfiscal.nfe.TNFe addNewNFe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TNFe target = null;
            target = (br.inf.portalfiscal.nfe.TNFe)get_store().add_element_user(NFE$0);
            return target;
        }
    }
    
    /**
     * Gets the "protNFe" element
     */
    public br.inf.portalfiscal.nfe.TProtNFe getProtNFe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TProtNFe target = null;
            target = (br.inf.portalfiscal.nfe.TProtNFe)get_store().find_element_user(PROTNFE$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "protNFe" element
     */
    public void setProtNFe(br.inf.portalfiscal.nfe.TProtNFe protNFe)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TProtNFe target = null;
            target = (br.inf.portalfiscal.nfe.TProtNFe)get_store().find_element_user(PROTNFE$2, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TProtNFe)get_store().add_element_user(PROTNFE$2);
            }
            target.set(protNFe);
        }
    }
    
    /**
     * Appends and returns a new empty "protNFe" element
     */
    public br.inf.portalfiscal.nfe.TProtNFe addNewProtNFe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TProtNFe target = null;
            target = (br.inf.portalfiscal.nfe.TProtNFe)get_store().add_element_user(PROTNFE$2);
            return target;
        }
    }
    
    /**
     * Gets the "versao" attribute
     */
    public java.lang.String getVersao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VERSAO$4);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "versao" attribute
     */
    public br.inf.portalfiscal.nfe.TVerNFe xgetVersao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TVerNFe target = null;
            target = (br.inf.portalfiscal.nfe.TVerNFe)get_store().find_attribute_user(VERSAO$4);
            return target;
        }
    }
    
    /**
     * Sets the "versao" attribute
     */
    public void setVersao(java.lang.String versao)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VERSAO$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(VERSAO$4);
            }
            target.setStringValue(versao);
        }
    }
    
    /**
     * Sets (as xml) the "versao" attribute
     */
    public void xsetVersao(br.inf.portalfiscal.nfe.TVerNFe versao)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TVerNFe target = null;
            target = (br.inf.portalfiscal.nfe.TVerNFe)get_store().find_attribute_user(VERSAO$4);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TVerNFe)get_store().add_attribute_user(VERSAO$4);
            }
            target.set(versao);
        }
    }
}
