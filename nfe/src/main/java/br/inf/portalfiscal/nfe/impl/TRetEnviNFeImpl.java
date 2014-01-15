/*
 * XML Type:  TRetEnviNFe
 * Namespace: http://www.portalfiscal.inf.br/nfe
 * Java type: br.inf.portalfiscal.nfe.TRetEnviNFe
 *
 * Automatically generated - do not modify.
 */
package br.inf.portalfiscal.nfe.impl;
/**
 * An XML TRetEnviNFe(@http://www.portalfiscal.inf.br/nfe).
 *
 * This is a complex type.
 */
public class TRetEnviNFeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.inf.portalfiscal.nfe.TRetEnviNFe
{
    
    public TRetEnviNFeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TPAMB$0 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "tpAmb");
    private static final javax.xml.namespace.QName VERAPLIC$2 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "verAplic");
    private static final javax.xml.namespace.QName CSTAT$4 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "cStat");
    private static final javax.xml.namespace.QName XMOTIVO$6 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "xMotivo");
    private static final javax.xml.namespace.QName CUF$8 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "cUF");
    private static final javax.xml.namespace.QName DHRECBTO$10 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "dhRecbto");
    private static final javax.xml.namespace.QName INFREC$12 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "infRec");
    private static final javax.xml.namespace.QName PROTNFE$14 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "protNFe");
    private static final javax.xml.namespace.QName VERSAO$16 = 
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
     * Gets the "verAplic" element
     */
    public java.lang.String getVerAplic()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VERAPLIC$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "verAplic" element
     */
    public br.inf.portalfiscal.nfe.TVerAplic xgetVerAplic()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TVerAplic target = null;
            target = (br.inf.portalfiscal.nfe.TVerAplic)get_store().find_element_user(VERAPLIC$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "verAplic" element
     */
    public void setVerAplic(java.lang.String verAplic)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VERAPLIC$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(VERAPLIC$2);
            }
            target.setStringValue(verAplic);
        }
    }
    
    /**
     * Sets (as xml) the "verAplic" element
     */
    public void xsetVerAplic(br.inf.portalfiscal.nfe.TVerAplic verAplic)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TVerAplic target = null;
            target = (br.inf.portalfiscal.nfe.TVerAplic)get_store().find_element_user(VERAPLIC$2, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TVerAplic)get_store().add_element_user(VERAPLIC$2);
            }
            target.set(verAplic);
        }
    }
    
    /**
     * Gets the "cStat" element
     */
    public java.lang.String getCStat()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CSTAT$4, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "cStat" element
     */
    public br.inf.portalfiscal.nfe.TStat xgetCStat()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TStat target = null;
            target = (br.inf.portalfiscal.nfe.TStat)get_store().find_element_user(CSTAT$4, 0);
            return target;
        }
    }
    
    /**
     * Sets the "cStat" element
     */
    public void setCStat(java.lang.String cStat)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CSTAT$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CSTAT$4);
            }
            target.setStringValue(cStat);
        }
    }
    
    /**
     * Sets (as xml) the "cStat" element
     */
    public void xsetCStat(br.inf.portalfiscal.nfe.TStat cStat)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TStat target = null;
            target = (br.inf.portalfiscal.nfe.TStat)get_store().find_element_user(CSTAT$4, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TStat)get_store().add_element_user(CSTAT$4);
            }
            target.set(cStat);
        }
    }
    
    /**
     * Gets the "xMotivo" element
     */
    public java.lang.String getXMotivo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XMOTIVO$6, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "xMotivo" element
     */
    public br.inf.portalfiscal.nfe.TMotivo xgetXMotivo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TMotivo target = null;
            target = (br.inf.portalfiscal.nfe.TMotivo)get_store().find_element_user(XMOTIVO$6, 0);
            return target;
        }
    }
    
    /**
     * Sets the "xMotivo" element
     */
    public void setXMotivo(java.lang.String xMotivo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XMOTIVO$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(XMOTIVO$6);
            }
            target.setStringValue(xMotivo);
        }
    }
    
    /**
     * Sets (as xml) the "xMotivo" element
     */
    public void xsetXMotivo(br.inf.portalfiscal.nfe.TMotivo xMotivo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TMotivo target = null;
            target = (br.inf.portalfiscal.nfe.TMotivo)get_store().find_element_user(XMOTIVO$6, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TMotivo)get_store().add_element_user(XMOTIVO$6);
            }
            target.set(xMotivo);
        }
    }
    
    /**
     * Gets the "cUF" element
     */
    public br.inf.portalfiscal.nfe.TCodUfIBGE.Enum getCUF()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CUF$8, 0);
            if (target == null)
            {
                return null;
            }
            return (br.inf.portalfiscal.nfe.TCodUfIBGE.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "cUF" element
     */
    public br.inf.portalfiscal.nfe.TCodUfIBGE xgetCUF()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TCodUfIBGE target = null;
            target = (br.inf.portalfiscal.nfe.TCodUfIBGE)get_store().find_element_user(CUF$8, 0);
            return target;
        }
    }
    
    /**
     * Sets the "cUF" element
     */
    public void setCUF(br.inf.portalfiscal.nfe.TCodUfIBGE.Enum cuf)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CUF$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CUF$8);
            }
            target.setEnumValue(cuf);
        }
    }
    
    /**
     * Sets (as xml) the "cUF" element
     */
    public void xsetCUF(br.inf.portalfiscal.nfe.TCodUfIBGE cuf)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TCodUfIBGE target = null;
            target = (br.inf.portalfiscal.nfe.TCodUfIBGE)get_store().find_element_user(CUF$8, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TCodUfIBGE)get_store().add_element_user(CUF$8);
            }
            target.set(cuf);
        }
    }
    
    /**
     * Gets the "dhRecbto" element
     */
    public java.lang.String getDhRecbto()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DHRECBTO$10, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "dhRecbto" element
     */
    public br.inf.portalfiscal.nfe.TDateTimeUTC xgetDhRecbto()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TDateTimeUTC target = null;
            target = (br.inf.portalfiscal.nfe.TDateTimeUTC)get_store().find_element_user(DHRECBTO$10, 0);
            return target;
        }
    }
    
    /**
     * Sets the "dhRecbto" element
     */
    public void setDhRecbto(java.lang.String dhRecbto)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DHRECBTO$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DHRECBTO$10);
            }
            target.setStringValue(dhRecbto);
        }
    }
    
    /**
     * Sets (as xml) the "dhRecbto" element
     */
    public void xsetDhRecbto(br.inf.portalfiscal.nfe.TDateTimeUTC dhRecbto)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TDateTimeUTC target = null;
            target = (br.inf.portalfiscal.nfe.TDateTimeUTC)get_store().find_element_user(DHRECBTO$10, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TDateTimeUTC)get_store().add_element_user(DHRECBTO$10);
            }
            target.set(dhRecbto);
        }
    }
    
    /**
     * Gets the "infRec" element
     */
    public br.inf.portalfiscal.nfe.TRetEnviNFe.InfRec getInfRec()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TRetEnviNFe.InfRec target = null;
            target = (br.inf.portalfiscal.nfe.TRetEnviNFe.InfRec)get_store().find_element_user(INFREC$12, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "infRec" element
     */
    public boolean isSetInfRec()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(INFREC$12) != 0;
        }
    }
    
    /**
     * Sets the "infRec" element
     */
    public void setInfRec(br.inf.portalfiscal.nfe.TRetEnviNFe.InfRec infRec)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TRetEnviNFe.InfRec target = null;
            target = (br.inf.portalfiscal.nfe.TRetEnviNFe.InfRec)get_store().find_element_user(INFREC$12, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TRetEnviNFe.InfRec)get_store().add_element_user(INFREC$12);
            }
            target.set(infRec);
        }
    }
    
    /**
     * Appends and returns a new empty "infRec" element
     */
    public br.inf.portalfiscal.nfe.TRetEnviNFe.InfRec addNewInfRec()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TRetEnviNFe.InfRec target = null;
            target = (br.inf.portalfiscal.nfe.TRetEnviNFe.InfRec)get_store().add_element_user(INFREC$12);
            return target;
        }
    }
    
    /**
     * Unsets the "infRec" element
     */
    public void unsetInfRec()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(INFREC$12, 0);
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
            target = (br.inf.portalfiscal.nfe.TProtNFe)get_store().find_element_user(PROTNFE$14, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "protNFe" element
     */
    public boolean isSetProtNFe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PROTNFE$14) != 0;
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
            target = (br.inf.portalfiscal.nfe.TProtNFe)get_store().find_element_user(PROTNFE$14, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TProtNFe)get_store().add_element_user(PROTNFE$14);
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
            target = (br.inf.portalfiscal.nfe.TProtNFe)get_store().add_element_user(PROTNFE$14);
            return target;
        }
    }
    
    /**
     * Unsets the "protNFe" element
     */
    public void unsetProtNFe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PROTNFE$14, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VERSAO$16);
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
            target = (br.inf.portalfiscal.nfe.TVerNFe)get_store().find_attribute_user(VERSAO$16);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VERSAO$16);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(VERSAO$16);
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
            target = (br.inf.portalfiscal.nfe.TVerNFe)get_store().find_attribute_user(VERSAO$16);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TVerNFe)get_store().add_attribute_user(VERSAO$16);
            }
            target.set(versao);
        }
    }
    /**
     * An XML infRec(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is a complex type.
     */
    public static class InfRecImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.inf.portalfiscal.nfe.TRetEnviNFe.InfRec
    {
        
        public InfRecImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName NREC$0 = 
            new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "nRec");
        private static final javax.xml.namespace.QName TMED$2 = 
            new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "tMed");
        
        
        /**
         * Gets the "nRec" element
         */
        public java.lang.String getNRec()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NREC$0, 0);
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
                target = (br.inf.portalfiscal.nfe.TRec)get_store().find_element_user(NREC$0, 0);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NREC$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NREC$0);
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
                target = (br.inf.portalfiscal.nfe.TRec)get_store().find_element_user(NREC$0, 0);
                if (target == null)
                {
                    target = (br.inf.portalfiscal.nfe.TRec)get_store().add_element_user(NREC$0);
                }
                target.set(nRec);
            }
        }
        
        /**
         * Gets the "tMed" element
         */
        public java.lang.String getTMed()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TMED$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "tMed" element
         */
        public br.inf.portalfiscal.nfe.TMed xgetTMed()
        {
            synchronized (monitor())
            {
                check_orphaned();
                br.inf.portalfiscal.nfe.TMed target = null;
                target = (br.inf.portalfiscal.nfe.TMed)get_store().find_element_user(TMED$2, 0);
                return target;
            }
        }
        
        /**
         * Sets the "tMed" element
         */
        public void setTMed(java.lang.String tMed)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TMED$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(TMED$2);
                }
                target.setStringValue(tMed);
            }
        }
        
        /**
         * Sets (as xml) the "tMed" element
         */
        public void xsetTMed(br.inf.portalfiscal.nfe.TMed tMed)
        {
            synchronized (monitor())
            {
                check_orphaned();
                br.inf.portalfiscal.nfe.TMed target = null;
                target = (br.inf.portalfiscal.nfe.TMed)get_store().find_element_user(TMED$2, 0);
                if (target == null)
                {
                    target = (br.inf.portalfiscal.nfe.TMed)get_store().add_element_user(TMED$2);
                }
                target.set(tMed);
            }
        }
    }
}
