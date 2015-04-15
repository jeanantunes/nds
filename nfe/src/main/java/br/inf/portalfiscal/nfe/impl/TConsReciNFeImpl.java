/*
 * XML Type:  TConsReciNFe
 * Namespace: http://www.portalfiscal.inf.br/nfe
 * Java type: br.inf.portalfiscal.nfe.TConsReciNFe
 *
 * Automatically generated - do not modify.
 */
package br.inf.portalfiscal.nfe.impl;
/**
 * An XML TConsReciNFe(@http://www.portalfiscal.inf.br/nfe).
 *
 * This is a complex type.
 */
public class TConsReciNFeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.inf.portalfiscal.nfe.TConsReciNFe
{
    
    public TConsReciNFeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TPAMB$0 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "tpAmb");
    private static final javax.xml.namespace.QName NREC$2 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "nRec");
    private static final javax.xml.namespace.QName VERSAO$4 = 
        new javax.xml.namespace.QName("", "versao");
    
    
    /**
     * Gets the "tpAmb" element
     */
    public br.inf.portalfiscal.nfe.TAmb.Enum getTpAmb()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TPAMB$0, 0);
            if (target == null)
            {
                return null;
            }
            return (br.inf.portalfiscal.nfe.TAmb.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "tpAmb" element
     */
    public br.inf.portalfiscal.nfe.TAmb xgetTpAmb()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TAmb target = null;
            target = (br.inf.portalfiscal.nfe.TAmb)get_store().find_element_user(TPAMB$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "tpAmb" element
     */
    public void setTpAmb(br.inf.portalfiscal.nfe.TAmb.Enum tpAmb)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TPAMB$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(TPAMB$0);
            }
            target.setEnumValue(tpAmb);
        }
    }
    
    /**
     * Sets (as xml) the "tpAmb" element
     */
    public void xsetTpAmb(br.inf.portalfiscal.nfe.TAmb tpAmb)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TAmb target = null;
            target = (br.inf.portalfiscal.nfe.TAmb)get_store().find_element_user(TPAMB$0, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TAmb)get_store().add_element_user(TPAMB$0);
            }
            target.set(tpAmb);
        }
    }
    
    /**
     * Gets the "nRec" element
     */
    public java.lang.String getNRec()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NREC$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "nRec" element
     */
    public br.inf.portalfiscal.nfe.TRec xgetNRec()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TRec target = null;
            target = (br.inf.portalfiscal.nfe.TRec)get_store().find_element_user(NREC$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "nRec" element
     */
    public void setNRec(java.lang.String nRec)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NREC$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NREC$2);
            }
            target.setStringValue(nRec);
        }
    }
    
    /**
     * Sets (as xml) the "nRec" element
     */
    public void xsetNRec(br.inf.portalfiscal.nfe.TRec nRec)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TRec target = null;
            target = (br.inf.portalfiscal.nfe.TRec)get_store().find_element_user(NREC$2, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TRec)get_store().add_element_user(NREC$2);
            }
            target.set(nRec);
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
