/*
 * XML Type:  TVeiculo
 * Namespace: http://www.portalfiscal.inf.br/nfe
 * Java type: br.inf.portalfiscal.nfe.TVeiculo
 *
 * Automatically generated - do not modify.
 */
package br.inf.portalfiscal.nfe.impl;
/**
 * An XML TVeiculo(@http://www.portalfiscal.inf.br/nfe).
 *
 * This is a complex type.
 */
public class TVeiculoImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.inf.portalfiscal.nfe.TVeiculo
{
    
    public TVeiculoImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PLACA$0 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "placa");
    private static final javax.xml.namespace.QName UF$2 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "UF");
    private static final javax.xml.namespace.QName RNTC$4 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "RNTC");
    
    
    /**
     * Gets the "placa" element
     */
    public java.lang.String getPlaca()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PLACA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "placa" element
     */
    public br.inf.portalfiscal.nfe.TVeiculo.Placa xgetPlaca()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TVeiculo.Placa target = null;
            target = (br.inf.portalfiscal.nfe.TVeiculo.Placa)get_store().find_element_user(PLACA$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "placa" element
     */
    public void setPlaca(java.lang.String placa)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PLACA$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PLACA$0);
            }
            target.setStringValue(placa);
        }
    }
    
    /**
     * Sets (as xml) the "placa" element
     */
    public void xsetPlaca(br.inf.portalfiscal.nfe.TVeiculo.Placa placa)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TVeiculo.Placa target = null;
            target = (br.inf.portalfiscal.nfe.TVeiculo.Placa)get_store().find_element_user(PLACA$0, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TVeiculo.Placa)get_store().add_element_user(PLACA$0);
            }
            target.set(placa);
        }
    }
    
    /**
     * Gets the "UF" element
     */
    public br.inf.portalfiscal.nfe.TUf.Enum getUF()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(UF$2, 0);
            if (target == null)
            {
                return null;
            }
            return (br.inf.portalfiscal.nfe.TUf.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "UF" element
     */
    public br.inf.portalfiscal.nfe.TUf xgetUF()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TUf target = null;
            target = (br.inf.portalfiscal.nfe.TUf)get_store().find_element_user(UF$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "UF" element
     */
    public void setUF(br.inf.portalfiscal.nfe.TUf.Enum uf)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(UF$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(UF$2);
            }
            target.setEnumValue(uf);
        }
    }
    
    /**
     * Sets (as xml) the "UF" element
     */
    public void xsetUF(br.inf.portalfiscal.nfe.TUf uf)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TUf target = null;
            target = (br.inf.portalfiscal.nfe.TUf)get_store().find_element_user(UF$2, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TUf)get_store().add_element_user(UF$2);
            }
            target.set(uf);
        }
    }
    
    /**
     * Gets the "RNTC" element
     */
    public java.lang.String getRNTC()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RNTC$4, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "RNTC" element
     */
    public br.inf.portalfiscal.nfe.TVeiculo.RNTC xgetRNTC()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TVeiculo.RNTC target = null;
            target = (br.inf.portalfiscal.nfe.TVeiculo.RNTC)get_store().find_element_user(RNTC$4, 0);
            return target;
        }
    }
    
    /**
     * True if has "RNTC" element
     */
    public boolean isSetRNTC()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(RNTC$4) != 0;
        }
    }
    
    /**
     * Sets the "RNTC" element
     */
    public void setRNTC(java.lang.String rntc)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RNTC$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(RNTC$4);
            }
            target.setStringValue(rntc);
        }
    }
    
    /**
     * Sets (as xml) the "RNTC" element
     */
    public void xsetRNTC(br.inf.portalfiscal.nfe.TVeiculo.RNTC rntc)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TVeiculo.RNTC target = null;
            target = (br.inf.portalfiscal.nfe.TVeiculo.RNTC)get_store().find_element_user(RNTC$4, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TVeiculo.RNTC)get_store().add_element_user(RNTC$4);
            }
            target.set(rntc);
        }
    }
    
    /**
     * Unsets the "RNTC" element
     */
    public void unsetRNTC()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(RNTC$4, 0);
        }
    }
    /**
     * An XML placa(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TVeiculo$Placa.
     */
    public static class PlacaImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements br.inf.portalfiscal.nfe.TVeiculo.Placa
    {
        
        public PlacaImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected PlacaImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
    /**
     * An XML RNTC(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TVeiculo$RNTC.
     */
    public static class RNTCImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements br.inf.portalfiscal.nfe.TVeiculo.RNTC
    {
        
        public RNTCImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected RNTCImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
}
