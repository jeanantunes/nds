/*
 * XML Type:  TLocal
 * Namespace: http://www.portalfiscal.inf.br/nfe
 * Java type: br.inf.portalfiscal.nfe.TLocal
 *
 * Automatically generated - do not modify.
 */
package br.inf.portalfiscal.nfe.impl;
/**
 * An XML TLocal(@http://www.portalfiscal.inf.br/nfe).
 *
 * This is a complex type.
 */
public class TLocalImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.inf.portalfiscal.nfe.TLocal
{
    
    public TLocalImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CNPJ$0 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "CNPJ");
    private static final javax.xml.namespace.QName CPF$2 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "CPF");
    private static final javax.xml.namespace.QName XLGR$4 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "xLgr");
    private static final javax.xml.namespace.QName NRO$6 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "nro");
    private static final javax.xml.namespace.QName XCPL$8 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "xCpl");
    private static final javax.xml.namespace.QName XBAIRRO$10 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "xBairro");
    private static final javax.xml.namespace.QName CMUN$12 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "cMun");
    private static final javax.xml.namespace.QName XMUN$14 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "xMun");
    private static final javax.xml.namespace.QName UF$16 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "UF");
    
    
    /**
     * Gets the "CNPJ" element
     */
    public java.lang.String getCNPJ()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CNPJ$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "CNPJ" element
     */
    public br.inf.portalfiscal.nfe.TCnpjOpc xgetCNPJ()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TCnpjOpc target = null;
            target = (br.inf.portalfiscal.nfe.TCnpjOpc)get_store().find_element_user(CNPJ$0, 0);
            return target;
        }
    }
    
    /**
     * True if has "CNPJ" element
     */
    public boolean isSetCNPJ()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CNPJ$0) != 0;
        }
    }
    
    /**
     * Sets the "CNPJ" element
     */
    public void setCNPJ(java.lang.String cnpj)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CNPJ$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CNPJ$0);
            }
            target.setStringValue(cnpj);
        }
    }
    
    /**
     * Sets (as xml) the "CNPJ" element
     */
    public void xsetCNPJ(br.inf.portalfiscal.nfe.TCnpjOpc cnpj)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TCnpjOpc target = null;
            target = (br.inf.portalfiscal.nfe.TCnpjOpc)get_store().find_element_user(CNPJ$0, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TCnpjOpc)get_store().add_element_user(CNPJ$0);
            }
            target.set(cnpj);
        }
    }
    
    /**
     * Unsets the "CNPJ" element
     */
    public void unsetCNPJ()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CNPJ$0, 0);
        }
    }
    
    /**
     * Gets the "CPF" element
     */
    public java.lang.String getCPF()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CPF$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "CPF" element
     */
    public br.inf.portalfiscal.nfe.TCpf xgetCPF()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TCpf target = null;
            target = (br.inf.portalfiscal.nfe.TCpf)get_store().find_element_user(CPF$2, 0);
            return target;
        }
    }
    
    /**
     * True if has "CPF" element
     */
    public boolean isSetCPF()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CPF$2) != 0;
        }
    }
    
    /**
     * Sets the "CPF" element
     */
    public void setCPF(java.lang.String cpf)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CPF$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CPF$2);
            }
            target.setStringValue(cpf);
        }
    }
    
    /**
     * Sets (as xml) the "CPF" element
     */
    public void xsetCPF(br.inf.portalfiscal.nfe.TCpf cpf)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TCpf target = null;
            target = (br.inf.portalfiscal.nfe.TCpf)get_store().find_element_user(CPF$2, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TCpf)get_store().add_element_user(CPF$2);
            }
            target.set(cpf);
        }
    }
    
    /**
     * Unsets the "CPF" element
     */
    public void unsetCPF()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CPF$2, 0);
        }
    }
    
    /**
     * Gets the "xLgr" element
     */
    public java.lang.String getXLgr()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XLGR$4, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "xLgr" element
     */
    public br.inf.portalfiscal.nfe.TLocal.XLgr xgetXLgr()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TLocal.XLgr target = null;
            target = (br.inf.portalfiscal.nfe.TLocal.XLgr)get_store().find_element_user(XLGR$4, 0);
            return target;
        }
    }
    
    /**
     * Sets the "xLgr" element
     */
    public void setXLgr(java.lang.String xLgr)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XLGR$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(XLGR$4);
            }
            target.setStringValue(xLgr);
        }
    }
    
    /**
     * Sets (as xml) the "xLgr" element
     */
    public void xsetXLgr(br.inf.portalfiscal.nfe.TLocal.XLgr xLgr)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TLocal.XLgr target = null;
            target = (br.inf.portalfiscal.nfe.TLocal.XLgr)get_store().find_element_user(XLGR$4, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TLocal.XLgr)get_store().add_element_user(XLGR$4);
            }
            target.set(xLgr);
        }
    }
    
    /**
     * Gets the "nro" element
     */
    public java.lang.String getNro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NRO$6, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "nro" element
     */
    public br.inf.portalfiscal.nfe.TLocal.Nro xgetNro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TLocal.Nro target = null;
            target = (br.inf.portalfiscal.nfe.TLocal.Nro)get_store().find_element_user(NRO$6, 0);
            return target;
        }
    }
    
    /**
     * Sets the "nro" element
     */
    public void setNro(java.lang.String nro)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NRO$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NRO$6);
            }
            target.setStringValue(nro);
        }
    }
    
    /**
     * Sets (as xml) the "nro" element
     */
    public void xsetNro(br.inf.portalfiscal.nfe.TLocal.Nro nro)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TLocal.Nro target = null;
            target = (br.inf.portalfiscal.nfe.TLocal.Nro)get_store().find_element_user(NRO$6, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TLocal.Nro)get_store().add_element_user(NRO$6);
            }
            target.set(nro);
        }
    }
    
    /**
     * Gets the "xCpl" element
     */
    public java.lang.String getXCpl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XCPL$8, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "xCpl" element
     */
    public br.inf.portalfiscal.nfe.TLocal.XCpl xgetXCpl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TLocal.XCpl target = null;
            target = (br.inf.portalfiscal.nfe.TLocal.XCpl)get_store().find_element_user(XCPL$8, 0);
            return target;
        }
    }
    
    /**
     * True if has "xCpl" element
     */
    public boolean isSetXCpl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(XCPL$8) != 0;
        }
    }
    
    /**
     * Sets the "xCpl" element
     */
    public void setXCpl(java.lang.String xCpl)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XCPL$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(XCPL$8);
            }
            target.setStringValue(xCpl);
        }
    }
    
    /**
     * Sets (as xml) the "xCpl" element
     */
    public void xsetXCpl(br.inf.portalfiscal.nfe.TLocal.XCpl xCpl)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TLocal.XCpl target = null;
            target = (br.inf.portalfiscal.nfe.TLocal.XCpl)get_store().find_element_user(XCPL$8, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TLocal.XCpl)get_store().add_element_user(XCPL$8);
            }
            target.set(xCpl);
        }
    }
    
    /**
     * Unsets the "xCpl" element
     */
    public void unsetXCpl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(XCPL$8, 0);
        }
    }
    
    /**
     * Gets the "xBairro" element
     */
    public java.lang.String getXBairro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XBAIRRO$10, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "xBairro" element
     */
    public br.inf.portalfiscal.nfe.TLocal.XBairro xgetXBairro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TLocal.XBairro target = null;
            target = (br.inf.portalfiscal.nfe.TLocal.XBairro)get_store().find_element_user(XBAIRRO$10, 0);
            return target;
        }
    }
    
    /**
     * Sets the "xBairro" element
     */
    public void setXBairro(java.lang.String xBairro)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XBAIRRO$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(XBAIRRO$10);
            }
            target.setStringValue(xBairro);
        }
    }
    
    /**
     * Sets (as xml) the "xBairro" element
     */
    public void xsetXBairro(br.inf.portalfiscal.nfe.TLocal.XBairro xBairro)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TLocal.XBairro target = null;
            target = (br.inf.portalfiscal.nfe.TLocal.XBairro)get_store().find_element_user(XBAIRRO$10, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TLocal.XBairro)get_store().add_element_user(XBAIRRO$10);
            }
            target.set(xBairro);
        }
    }
    
    /**
     * Gets the "cMun" element
     */
    public java.lang.String getCMun()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CMUN$12, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "cMun" element
     */
    public br.inf.portalfiscal.nfe.TCodMunIBGE xgetCMun()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TCodMunIBGE target = null;
            target = (br.inf.portalfiscal.nfe.TCodMunIBGE)get_store().find_element_user(CMUN$12, 0);
            return target;
        }
    }
    
    /**
     * Sets the "cMun" element
     */
    public void setCMun(java.lang.String cMun)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CMUN$12, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CMUN$12);
            }
            target.setStringValue(cMun);
        }
    }
    
    /**
     * Sets (as xml) the "cMun" element
     */
    public void xsetCMun(br.inf.portalfiscal.nfe.TCodMunIBGE cMun)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TCodMunIBGE target = null;
            target = (br.inf.portalfiscal.nfe.TCodMunIBGE)get_store().find_element_user(CMUN$12, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TCodMunIBGE)get_store().add_element_user(CMUN$12);
            }
            target.set(cMun);
        }
    }
    
    /**
     * Gets the "xMun" element
     */
    public java.lang.String getXMun()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XMUN$14, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "xMun" element
     */
    public br.inf.portalfiscal.nfe.TLocal.XMun xgetXMun()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TLocal.XMun target = null;
            target = (br.inf.portalfiscal.nfe.TLocal.XMun)get_store().find_element_user(XMUN$14, 0);
            return target;
        }
    }
    
    /**
     * Sets the "xMun" element
     */
    public void setXMun(java.lang.String xMun)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XMUN$14, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(XMUN$14);
            }
            target.setStringValue(xMun);
        }
    }
    
    /**
     * Sets (as xml) the "xMun" element
     */
    public void xsetXMun(br.inf.portalfiscal.nfe.TLocal.XMun xMun)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TLocal.XMun target = null;
            target = (br.inf.portalfiscal.nfe.TLocal.XMun)get_store().find_element_user(XMUN$14, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TLocal.XMun)get_store().add_element_user(XMUN$14);
            }
            target.set(xMun);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(UF$16, 0);
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
            target = (br.inf.portalfiscal.nfe.TUf)get_store().find_element_user(UF$16, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(UF$16, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(UF$16);
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
            target = (br.inf.portalfiscal.nfe.TUf)get_store().find_element_user(UF$16, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TUf)get_store().add_element_user(UF$16);
            }
            target.set(uf);
        }
    }
    /**
     * An XML xLgr(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TLocal$XLgr.
     */
    public static class XLgrImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements br.inf.portalfiscal.nfe.TLocal.XLgr
    {
        
        public XLgrImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected XLgrImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
    /**
     * An XML nro(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TLocal$Nro.
     */
    public static class NroImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements br.inf.portalfiscal.nfe.TLocal.Nro
    {
        
        public NroImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected NroImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
    /**
     * An XML xCpl(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TLocal$XCpl.
     */
    public static class XCplImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements br.inf.portalfiscal.nfe.TLocal.XCpl
    {
        
        public XCplImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected XCplImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
    /**
     * An XML xBairro(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TLocal$XBairro.
     */
    public static class XBairroImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements br.inf.portalfiscal.nfe.TLocal.XBairro
    {
        
        public XBairroImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected XBairroImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
    /**
     * An XML xMun(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TLocal$XMun.
     */
    public static class XMunImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements br.inf.portalfiscal.nfe.TLocal.XMun
    {
        
        public XMunImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected XMunImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
}
