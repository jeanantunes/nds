/*
 * XML Type:  TEnderEmi
 * Namespace: http://www.portalfiscal.inf.br/nfe
 * Java type: br.inf.portalfiscal.nfe.TEnderEmi
 *
 * Automatically generated - do not modify.
 */
package br.inf.portalfiscal.nfe.impl;
/**
 * An XML TEnderEmi(@http://www.portalfiscal.inf.br/nfe).
 *
 * This is a complex type.
 */
public class TEnderEmiImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.inf.portalfiscal.nfe.TEnderEmi
{
    private static final long serialVersionUID = 1L;
    
    public TEnderEmiImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName XLGR$0 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "xLgr");
    private static final javax.xml.namespace.QName NRO$2 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "nro");
    private static final javax.xml.namespace.QName XCPL$4 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "xCpl");
    private static final javax.xml.namespace.QName XBAIRRO$6 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "xBairro");
    private static final javax.xml.namespace.QName CMUN$8 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "cMun");
    private static final javax.xml.namespace.QName XMUN$10 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "xMun");
    private static final javax.xml.namespace.QName UF$12 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "UF");
    private static final javax.xml.namespace.QName CEP$14 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "CEP");
    private static final javax.xml.namespace.QName CPAIS$16 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "cPais");
    private static final javax.xml.namespace.QName XPAIS$18 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "xPais");
    private static final javax.xml.namespace.QName FONE$20 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "fone");
    
    
    /**
     * Gets the "xLgr" element
     */
    public java.lang.String getXLgr()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XLGR$0, 0);
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
    public br.inf.portalfiscal.nfe.TEnderEmi.XLgr xgetXLgr()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TEnderEmi.XLgr target = null;
            target = (br.inf.portalfiscal.nfe.TEnderEmi.XLgr)get_store().find_element_user(XLGR$0, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XLGR$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(XLGR$0);
            }
            target.setStringValue(xLgr);
        }
    }
    
    /**
     * Sets (as xml) the "xLgr" element
     */
    public void xsetXLgr(br.inf.portalfiscal.nfe.TEnderEmi.XLgr xLgr)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TEnderEmi.XLgr target = null;
            target = (br.inf.portalfiscal.nfe.TEnderEmi.XLgr)get_store().find_element_user(XLGR$0, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TEnderEmi.XLgr)get_store().add_element_user(XLGR$0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NRO$2, 0);
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
    public br.inf.portalfiscal.nfe.TEnderEmi.Nro xgetNro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TEnderEmi.Nro target = null;
            target = (br.inf.portalfiscal.nfe.TEnderEmi.Nro)get_store().find_element_user(NRO$2, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NRO$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NRO$2);
            }
            target.setStringValue(nro);
        }
    }
    
    /**
     * Sets (as xml) the "nro" element
     */
    public void xsetNro(br.inf.portalfiscal.nfe.TEnderEmi.Nro nro)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TEnderEmi.Nro target = null;
            target = (br.inf.portalfiscal.nfe.TEnderEmi.Nro)get_store().find_element_user(NRO$2, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TEnderEmi.Nro)get_store().add_element_user(NRO$2);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XCPL$4, 0);
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
    public br.inf.portalfiscal.nfe.TEnderEmi.XCpl xgetXCpl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TEnderEmi.XCpl target = null;
            target = (br.inf.portalfiscal.nfe.TEnderEmi.XCpl)get_store().find_element_user(XCPL$4, 0);
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
            return get_store().count_elements(XCPL$4) != 0;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XCPL$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(XCPL$4);
            }
            target.setStringValue(xCpl);
        }
    }
    
    /**
     * Sets (as xml) the "xCpl" element
     */
    public void xsetXCpl(br.inf.portalfiscal.nfe.TEnderEmi.XCpl xCpl)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TEnderEmi.XCpl target = null;
            target = (br.inf.portalfiscal.nfe.TEnderEmi.XCpl)get_store().find_element_user(XCPL$4, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TEnderEmi.XCpl)get_store().add_element_user(XCPL$4);
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
            get_store().remove_element(XCPL$4, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XBAIRRO$6, 0);
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
    public br.inf.portalfiscal.nfe.TEnderEmi.XBairro xgetXBairro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TEnderEmi.XBairro target = null;
            target = (br.inf.portalfiscal.nfe.TEnderEmi.XBairro)get_store().find_element_user(XBAIRRO$6, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XBAIRRO$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(XBAIRRO$6);
            }
            target.setStringValue(xBairro);
        }
    }
    
    /**
     * Sets (as xml) the "xBairro" element
     */
    public void xsetXBairro(br.inf.portalfiscal.nfe.TEnderEmi.XBairro xBairro)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TEnderEmi.XBairro target = null;
            target = (br.inf.portalfiscal.nfe.TEnderEmi.XBairro)get_store().find_element_user(XBAIRRO$6, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TEnderEmi.XBairro)get_store().add_element_user(XBAIRRO$6);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CMUN$8, 0);
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
            target = (br.inf.portalfiscal.nfe.TCodMunIBGE)get_store().find_element_user(CMUN$8, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CMUN$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CMUN$8);
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
            target = (br.inf.portalfiscal.nfe.TCodMunIBGE)get_store().find_element_user(CMUN$8, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TCodMunIBGE)get_store().add_element_user(CMUN$8);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XMUN$10, 0);
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
    public br.inf.portalfiscal.nfe.TEnderEmi.XMun xgetXMun()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TEnderEmi.XMun target = null;
            target = (br.inf.portalfiscal.nfe.TEnderEmi.XMun)get_store().find_element_user(XMUN$10, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XMUN$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(XMUN$10);
            }
            target.setStringValue(xMun);
        }
    }
    
    /**
     * Sets (as xml) the "xMun" element
     */
    public void xsetXMun(br.inf.portalfiscal.nfe.TEnderEmi.XMun xMun)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TEnderEmi.XMun target = null;
            target = (br.inf.portalfiscal.nfe.TEnderEmi.XMun)get_store().find_element_user(XMUN$10, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TEnderEmi.XMun)get_store().add_element_user(XMUN$10);
            }
            target.set(xMun);
        }
    }
    
    /**
     * Gets the "UF" element
     */
    public br.inf.portalfiscal.nfe.TUfEmi.Enum getUF()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(UF$12, 0);
            if (target == null)
            {
                return null;
            }
            return (br.inf.portalfiscal.nfe.TUfEmi.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "UF" element
     */
    public br.inf.portalfiscal.nfe.TUfEmi xgetUF()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TUfEmi target = null;
            target = (br.inf.portalfiscal.nfe.TUfEmi)get_store().find_element_user(UF$12, 0);
            return target;
        }
    }
    
    /**
     * Sets the "UF" element
     */
    public void setUF(br.inf.portalfiscal.nfe.TUfEmi.Enum uf)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(UF$12, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(UF$12);
            }
            target.setEnumValue(uf);
        }
    }
    
    /**
     * Sets (as xml) the "UF" element
     */
    public void xsetUF(br.inf.portalfiscal.nfe.TUfEmi uf)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TUfEmi target = null;
            target = (br.inf.portalfiscal.nfe.TUfEmi)get_store().find_element_user(UF$12, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TUfEmi)get_store().add_element_user(UF$12);
            }
            target.set(uf);
        }
    }
    
    /**
     * Gets the "CEP" element
     */
    public java.lang.String getCEP()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CEP$14, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "CEP" element
     */
    public br.inf.portalfiscal.nfe.TEnderEmi.CEP xgetCEP()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TEnderEmi.CEP target = null;
            target = (br.inf.portalfiscal.nfe.TEnderEmi.CEP)get_store().find_element_user(CEP$14, 0);
            return target;
        }
    }
    
    /**
     * Sets the "CEP" element
     */
    public void setCEP(java.lang.String cep)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CEP$14, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CEP$14);
            }
            target.setStringValue(cep);
        }
    }
    
    /**
     * Sets (as xml) the "CEP" element
     */
    public void xsetCEP(br.inf.portalfiscal.nfe.TEnderEmi.CEP cep)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TEnderEmi.CEP target = null;
            target = (br.inf.portalfiscal.nfe.TEnderEmi.CEP)get_store().find_element_user(CEP$14, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TEnderEmi.CEP)get_store().add_element_user(CEP$14);
            }
            target.set(cep);
        }
    }
    
    /**
     * Gets the "cPais" element
     */
    public br.inf.portalfiscal.nfe.TEnderEmi.CPais.Enum getCPais()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CPAIS$16, 0);
            if (target == null)
            {
                return null;
            }
            return (br.inf.portalfiscal.nfe.TEnderEmi.CPais.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "cPais" element
     */
    public br.inf.portalfiscal.nfe.TEnderEmi.CPais xgetCPais()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TEnderEmi.CPais target = null;
            target = (br.inf.portalfiscal.nfe.TEnderEmi.CPais)get_store().find_element_user(CPAIS$16, 0);
            return target;
        }
    }
    
    /**
     * True if has "cPais" element
     */
    public boolean isSetCPais()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CPAIS$16) != 0;
        }
    }
    
    /**
     * Sets the "cPais" element
     */
    public void setCPais(br.inf.portalfiscal.nfe.TEnderEmi.CPais.Enum cPais)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CPAIS$16, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CPAIS$16);
            }
            target.setEnumValue(cPais);
        }
    }
    
    /**
     * Sets (as xml) the "cPais" element
     */
    public void xsetCPais(br.inf.portalfiscal.nfe.TEnderEmi.CPais cPais)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TEnderEmi.CPais target = null;
            target = (br.inf.portalfiscal.nfe.TEnderEmi.CPais)get_store().find_element_user(CPAIS$16, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TEnderEmi.CPais)get_store().add_element_user(CPAIS$16);
            }
            target.set(cPais);
        }
    }
    
    /**
     * Unsets the "cPais" element
     */
    public void unsetCPais()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CPAIS$16, 0);
        }
    }
    
    /**
     * Gets the "xPais" element
     */
    public br.inf.portalfiscal.nfe.TEnderEmi.XPais.Enum getXPais()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XPAIS$18, 0);
            if (target == null)
            {
                return null;
            }
            return (br.inf.portalfiscal.nfe.TEnderEmi.XPais.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "xPais" element
     */
    public br.inf.portalfiscal.nfe.TEnderEmi.XPais xgetXPais()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TEnderEmi.XPais target = null;
            target = (br.inf.portalfiscal.nfe.TEnderEmi.XPais)get_store().find_element_user(XPAIS$18, 0);
            return target;
        }
    }
    
    /**
     * True if has "xPais" element
     */
    public boolean isSetXPais()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(XPAIS$18) != 0;
        }
    }
    
    /**
     * Sets the "xPais" element
     */
    public void setXPais(br.inf.portalfiscal.nfe.TEnderEmi.XPais.Enum xPais)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XPAIS$18, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(XPAIS$18);
            }
            target.setEnumValue(xPais);
        }
    }
    
    /**
     * Sets (as xml) the "xPais" element
     */
    public void xsetXPais(br.inf.portalfiscal.nfe.TEnderEmi.XPais xPais)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TEnderEmi.XPais target = null;
            target = (br.inf.portalfiscal.nfe.TEnderEmi.XPais)get_store().find_element_user(XPAIS$18, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TEnderEmi.XPais)get_store().add_element_user(XPAIS$18);
            }
            target.set(xPais);
        }
    }
    
    /**
     * Unsets the "xPais" element
     */
    public void unsetXPais()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(XPAIS$18, 0);
        }
    }
    
    /**
     * Gets the "fone" element
     */
    public java.lang.String getFone()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(FONE$20, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "fone" element
     */
    public br.inf.portalfiscal.nfe.TEnderEmi.Fone xgetFone()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TEnderEmi.Fone target = null;
            target = (br.inf.portalfiscal.nfe.TEnderEmi.Fone)get_store().find_element_user(FONE$20, 0);
            return target;
        }
    }
    
    /**
     * True if has "fone" element
     */
    public boolean isSetFone()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(FONE$20) != 0;
        }
    }
    
    /**
     * Sets the "fone" element
     */
    public void setFone(java.lang.String fone)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(FONE$20, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(FONE$20);
            }
            target.setStringValue(fone);
        }
    }
    
    /**
     * Sets (as xml) the "fone" element
     */
    public void xsetFone(br.inf.portalfiscal.nfe.TEnderEmi.Fone fone)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TEnderEmi.Fone target = null;
            target = (br.inf.portalfiscal.nfe.TEnderEmi.Fone)get_store().find_element_user(FONE$20, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TEnderEmi.Fone)get_store().add_element_user(FONE$20);
            }
            target.set(fone);
        }
    }
    
    /**
     * Unsets the "fone" element
     */
    public void unsetFone()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(FONE$20, 0);
        }
    }
    /**
     * An XML xLgr(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TEnderEmi$XLgr.
     */
    public static class XLgrImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements br.inf.portalfiscal.nfe.TEnderEmi.XLgr
    {
        private static final long serialVersionUID = 1L;
        
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
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TEnderEmi$Nro.
     */
    public static class NroImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements br.inf.portalfiscal.nfe.TEnderEmi.Nro
    {
        private static final long serialVersionUID = 1L;
        
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
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TEnderEmi$XCpl.
     */
    public static class XCplImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements br.inf.portalfiscal.nfe.TEnderEmi.XCpl
    {
        private static final long serialVersionUID = 1L;
        
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
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TEnderEmi$XBairro.
     */
    public static class XBairroImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements br.inf.portalfiscal.nfe.TEnderEmi.XBairro
    {
        private static final long serialVersionUID = 1L;
        
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
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TEnderEmi$XMun.
     */
    public static class XMunImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements br.inf.portalfiscal.nfe.TEnderEmi.XMun
    {
        private static final long serialVersionUID = 1L;
        
        public XMunImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected XMunImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
    /**
     * An XML CEP(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TEnderEmi$CEP.
     */
    public static class CEPImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements br.inf.portalfiscal.nfe.TEnderEmi.CEP
    {
        private static final long serialVersionUID = 1L;
        
        public CEPImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected CEPImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
    /**
     * An XML cPais(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TEnderEmi$CPais.
     */
    public static class CPaisImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements br.inf.portalfiscal.nfe.TEnderEmi.CPais
    {
        private static final long serialVersionUID = 1L;
        
        public CPaisImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected CPaisImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
    /**
     * An XML xPais(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TEnderEmi$XPais.
     */
    public static class XPaisImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements br.inf.portalfiscal.nfe.TEnderEmi.XPais
    {
        private static final long serialVersionUID = 1L;
        
        public XPaisImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected XPaisImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
    /**
     * An XML fone(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TEnderEmi$Fone.
     */
    public static class FoneImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements br.inf.portalfiscal.nfe.TEnderEmi.Fone
    {
        private static final long serialVersionUID = 1L;
        
        public FoneImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected FoneImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
}
