/*
 * XML Type:  TEnviNFe
 * Namespace: http://www.portalfiscal.inf.br/nfe
 * Java type: br.inf.portalfiscal.nfe.TEnviNFe
 *
 * Automatically generated - do not modify.
 */
package br.inf.portalfiscal.nfe.impl;
/**
 * An XML TEnviNFe(@http://www.portalfiscal.inf.br/nfe).
 *
 * This is a complex type.
 */
public class TEnviNFeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.inf.portalfiscal.nfe.TEnviNFe
{
    
    public TEnviNFeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName IDLOTE$0 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "idLote");
    private static final javax.xml.namespace.QName INDSINC$2 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "indSinc");
    private static final javax.xml.namespace.QName NFE$4 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "NFe");
    private static final javax.xml.namespace.QName VERSAO$6 = 
        new javax.xml.namespace.QName("", "versao");
    
    
    /**
     * Gets the "idLote" element
     */
    public java.lang.String getIdLote()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(IDLOTE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "idLote" element
     */
    public br.inf.portalfiscal.nfe.TIdLote xgetIdLote()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TIdLote target = null;
            target = (br.inf.portalfiscal.nfe.TIdLote)get_store().find_element_user(IDLOTE$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "idLote" element
     */
    public void setIdLote(java.lang.String idLote)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(IDLOTE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(IDLOTE$0);
            }
            target.setStringValue(idLote);
        }
    }
    
    /**
     * Sets (as xml) the "idLote" element
     */
    public void xsetIdLote(br.inf.portalfiscal.nfe.TIdLote idLote)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TIdLote target = null;
            target = (br.inf.portalfiscal.nfe.TIdLote)get_store().find_element_user(IDLOTE$0, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TIdLote)get_store().add_element_user(IDLOTE$0);
            }
            target.set(idLote);
        }
    }
    
    /**
     * Gets the "indSinc" element
     */
    public br.inf.portalfiscal.nfe.TEnviNFe.IndSinc.Enum getIndSinc()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(INDSINC$2, 0);
            if (target == null)
            {
                return null;
            }
            return (br.inf.portalfiscal.nfe.TEnviNFe.IndSinc.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "indSinc" element
     */
    public br.inf.portalfiscal.nfe.TEnviNFe.IndSinc xgetIndSinc()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TEnviNFe.IndSinc target = null;
            target = (br.inf.portalfiscal.nfe.TEnviNFe.IndSinc)get_store().find_element_user(INDSINC$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "indSinc" element
     */
    public void setIndSinc(br.inf.portalfiscal.nfe.TEnviNFe.IndSinc.Enum indSinc)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(INDSINC$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(INDSINC$2);
            }
            target.setEnumValue(indSinc);
        }
    }
    
    /**
     * Sets (as xml) the "indSinc" element
     */
    public void xsetIndSinc(br.inf.portalfiscal.nfe.TEnviNFe.IndSinc indSinc)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TEnviNFe.IndSinc target = null;
            target = (br.inf.portalfiscal.nfe.TEnviNFe.IndSinc)get_store().find_element_user(INDSINC$2, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TEnviNFe.IndSinc)get_store().add_element_user(INDSINC$2);
            }
            target.set(indSinc);
        }
    }
    
    /**
     * Gets array of all "NFe" elements
     */
    public br.inf.portalfiscal.nfe.TNFe[] getNFeArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(NFE$4, targetList);
            br.inf.portalfiscal.nfe.TNFe[] result = new br.inf.portalfiscal.nfe.TNFe[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "NFe" element
     */
    public br.inf.portalfiscal.nfe.TNFe getNFeArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TNFe target = null;
            target = (br.inf.portalfiscal.nfe.TNFe)get_store().find_element_user(NFE$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "NFe" element
     */
    public int sizeOfNFeArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(NFE$4);
        }
    }
    
    /**
     * Sets array of all "NFe" element
     */
    public void setNFeArray(br.inf.portalfiscal.nfe.TNFe[] nFeArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(nFeArray, NFE$4);
        }
    }
    
    /**
     * Sets ith "NFe" element
     */
    public void setNFeArray(int i, br.inf.portalfiscal.nfe.TNFe nFe)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TNFe target = null;
            target = (br.inf.portalfiscal.nfe.TNFe)get_store().find_element_user(NFE$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(nFe);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "NFe" element
     */
    public br.inf.portalfiscal.nfe.TNFe insertNewNFe(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TNFe target = null;
            target = (br.inf.portalfiscal.nfe.TNFe)get_store().insert_element_user(NFE$4, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "NFe" element
     */
    public br.inf.portalfiscal.nfe.TNFe addNewNFe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TNFe target = null;
            target = (br.inf.portalfiscal.nfe.TNFe)get_store().add_element_user(NFE$4);
            return target;
        }
    }
    
    /**
     * Removes the ith "NFe" element
     */
    public void removeNFe(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(NFE$4, i);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VERSAO$6);
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
            target = (br.inf.portalfiscal.nfe.TVerNFe)get_store().find_attribute_user(VERSAO$6);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VERSAO$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(VERSAO$6);
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
            target = (br.inf.portalfiscal.nfe.TVerNFe)get_store().find_attribute_user(VERSAO$6);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TVerNFe)get_store().add_attribute_user(VERSAO$6);
            }
            target.set(versao);
        }
    }
    /**
     * An XML indSinc(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TEnviNFe$IndSinc.
     */
    public static class IndSincImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements br.inf.portalfiscal.nfe.TEnviNFe.IndSinc
    {
        
        public IndSincImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected IndSincImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
}
