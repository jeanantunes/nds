/*
 * XML Type:  TProtNFe
 * Namespace: http://www.portalfiscal.inf.br/nfe
 * Java type: br.inf.portalfiscal.nfe.TProtNFe
 *
 * Automatically generated - do not modify.
 */
package br.inf.portalfiscal.nfe.impl;
/**
 * An XML TProtNFe(@http://www.portalfiscal.inf.br/nfe).
 *
 * This is a complex type.
 */
public class TProtNFeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.inf.portalfiscal.nfe.TProtNFe
{
    
    public TProtNFeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName INFPROT$0 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "infProt");
    private static final javax.xml.namespace.QName SIGNATURE$2 = 
        new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "Signature");
    private static final javax.xml.namespace.QName VERSAO$4 = 
        new javax.xml.namespace.QName("", "versao");
    
    
    /**
     * Gets the "infProt" element
     */
    public br.inf.portalfiscal.nfe.TProtNFe.InfProt getInfProt()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TProtNFe.InfProt target = null;
            target = (br.inf.portalfiscal.nfe.TProtNFe.InfProt)get_store().find_element_user(INFPROT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "infProt" element
     */
    public void setInfProt(br.inf.portalfiscal.nfe.TProtNFe.InfProt infProt)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TProtNFe.InfProt target = null;
            target = (br.inf.portalfiscal.nfe.TProtNFe.InfProt)get_store().find_element_user(INFPROT$0, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TProtNFe.InfProt)get_store().add_element_user(INFPROT$0);
            }
            target.set(infProt);
        }
    }
    
    /**
     * Appends and returns a new empty "infProt" element
     */
    public br.inf.portalfiscal.nfe.TProtNFe.InfProt addNewInfProt()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TProtNFe.InfProt target = null;
            target = (br.inf.portalfiscal.nfe.TProtNFe.InfProt)get_store().add_element_user(INFPROT$0);
            return target;
        }
    }
    
    /**
     * Gets the "Signature" element
     */
    public org.w3.x2000.x09.xmldsig.SignatureType getSignature()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignatureType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignatureType)get_store().find_element_user(SIGNATURE$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Signature" element
     */
    public boolean isSetSignature()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SIGNATURE$2) != 0;
        }
    }
    
    /**
     * Sets the "Signature" element
     */
    public void setSignature(org.w3.x2000.x09.xmldsig.SignatureType signature)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignatureType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignatureType)get_store().find_element_user(SIGNATURE$2, 0);
            if (target == null)
            {
                target = (org.w3.x2000.x09.xmldsig.SignatureType)get_store().add_element_user(SIGNATURE$2);
            }
            target.set(signature);
        }
    }
    
    /**
     * Appends and returns a new empty "Signature" element
     */
    public org.w3.x2000.x09.xmldsig.SignatureType addNewSignature()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignatureType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignatureType)get_store().add_element_user(SIGNATURE$2);
            return target;
        }
    }
    
    /**
     * Unsets the "Signature" element
     */
    public void unsetSignature()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SIGNATURE$2, 0);
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
    /**
     * An XML infProt(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is a complex type.
     */
    public static class InfProtImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.inf.portalfiscal.nfe.TProtNFe.InfProt
    {
        
        public InfProtImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName TPAMB$0 = 
            new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "tpAmb");
        private static final javax.xml.namespace.QName VERAPLIC$2 = 
            new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "verAplic");
        private static final javax.xml.namespace.QName CHNFE$4 = 
            new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "chNFe");
        private static final javax.xml.namespace.QName DHRECBTO$6 = 
            new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "dhRecbto");
        private static final javax.xml.namespace.QName NPROT$8 = 
            new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "nProt");
        private static final javax.xml.namespace.QName DIGVAL$10 = 
            new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "digVal");
        private static final javax.xml.namespace.QName CSTAT$12 = 
            new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "cStat");
        private static final javax.xml.namespace.QName XMOTIVO$14 = 
            new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "xMotivo");
        private static final javax.xml.namespace.QName ID$16 = 
            new javax.xml.namespace.QName("", "Id");
        
        
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
         * Gets the "chNFe" element
         */
        public java.lang.String getChNFe()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CHNFE$4, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "chNFe" element
         */
        public br.inf.portalfiscal.nfe.TChNFe xgetChNFe()
        {
            synchronized (monitor())
            {
                check_orphaned();
                br.inf.portalfiscal.nfe.TChNFe target = null;
                target = (br.inf.portalfiscal.nfe.TChNFe)get_store().find_element_user(CHNFE$4, 0);
                return target;
            }
        }
        
        /**
         * Sets the "chNFe" element
         */
        public void setChNFe(java.lang.String chNFe)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CHNFE$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CHNFE$4);
                }
                target.setStringValue(chNFe);
            }
        }
        
        /**
         * Sets (as xml) the "chNFe" element
         */
        public void xsetChNFe(br.inf.portalfiscal.nfe.TChNFe chNFe)
        {
            synchronized (monitor())
            {
                check_orphaned();
                br.inf.portalfiscal.nfe.TChNFe target = null;
                target = (br.inf.portalfiscal.nfe.TChNFe)get_store().find_element_user(CHNFE$4, 0);
                if (target == null)
                {
                    target = (br.inf.portalfiscal.nfe.TChNFe)get_store().add_element_user(CHNFE$4);
                }
                target.set(chNFe);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DHRECBTO$6, 0);
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
                target = (br.inf.portalfiscal.nfe.TDateTimeUTC)get_store().find_element_user(DHRECBTO$6, 0);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DHRECBTO$6, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DHRECBTO$6);
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
                target = (br.inf.portalfiscal.nfe.TDateTimeUTC)get_store().find_element_user(DHRECBTO$6, 0);
                if (target == null)
                {
                    target = (br.inf.portalfiscal.nfe.TDateTimeUTC)get_store().add_element_user(DHRECBTO$6);
                }
                target.set(dhRecbto);
            }
        }
        
        /**
         * Gets the "nProt" element
         */
        public java.lang.String getNProt()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NPROT$8, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "nProt" element
         */
        public br.inf.portalfiscal.nfe.TProt xgetNProt()
        {
            synchronized (monitor())
            {
                check_orphaned();
                br.inf.portalfiscal.nfe.TProt target = null;
                target = (br.inf.portalfiscal.nfe.TProt)get_store().find_element_user(NPROT$8, 0);
                return target;
            }
        }
        
        /**
         * True if has "nProt" element
         */
        public boolean isSetNProt()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(NPROT$8) != 0;
            }
        }
        
        /**
         * Sets the "nProt" element
         */
        public void setNProt(java.lang.String nProt)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NPROT$8, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NPROT$8);
                }
                target.setStringValue(nProt);
            }
        }
        
        /**
         * Sets (as xml) the "nProt" element
         */
        public void xsetNProt(br.inf.portalfiscal.nfe.TProt nProt)
        {
            synchronized (monitor())
            {
                check_orphaned();
                br.inf.portalfiscal.nfe.TProt target = null;
                target = (br.inf.portalfiscal.nfe.TProt)get_store().find_element_user(NPROT$8, 0);
                if (target == null)
                {
                    target = (br.inf.portalfiscal.nfe.TProt)get_store().add_element_user(NPROT$8);
                }
                target.set(nProt);
            }
        }
        
        /**
         * Unsets the "nProt" element
         */
        public void unsetNProt()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(NPROT$8, 0);
            }
        }
        
        /**
         * Gets the "digVal" element
         */
        public byte[] getDigVal()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DIGVAL$10, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getByteArrayValue();
            }
        }
        
        /**
         * Gets (as xml) the "digVal" element
         */
        public org.w3.x2000.x09.xmldsig.DigestValueType xgetDigVal()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.w3.x2000.x09.xmldsig.DigestValueType target = null;
                target = (org.w3.x2000.x09.xmldsig.DigestValueType)get_store().find_element_user(DIGVAL$10, 0);
                return target;
            }
        }
        
        /**
         * True if has "digVal" element
         */
        public boolean isSetDigVal()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(DIGVAL$10) != 0;
            }
        }
        
        /**
         * Sets the "digVal" element
         */
        public void setDigVal(byte[] digVal)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DIGVAL$10, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DIGVAL$10);
                }
                target.setByteArrayValue(digVal);
            }
        }
        
        /**
         * Sets (as xml) the "digVal" element
         */
        public void xsetDigVal(org.w3.x2000.x09.xmldsig.DigestValueType digVal)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.w3.x2000.x09.xmldsig.DigestValueType target = null;
                target = (org.w3.x2000.x09.xmldsig.DigestValueType)get_store().find_element_user(DIGVAL$10, 0);
                if (target == null)
                {
                    target = (org.w3.x2000.x09.xmldsig.DigestValueType)get_store().add_element_user(DIGVAL$10);
                }
                target.set(digVal);
            }
        }
        
        /**
         * Unsets the "digVal" element
         */
        public void unsetDigVal()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(DIGVAL$10, 0);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CSTAT$12, 0);
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
                target = (br.inf.portalfiscal.nfe.TStat)get_store().find_element_user(CSTAT$12, 0);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CSTAT$12, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CSTAT$12);
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
                target = (br.inf.portalfiscal.nfe.TStat)get_store().find_element_user(CSTAT$12, 0);
                if (target == null)
                {
                    target = (br.inf.portalfiscal.nfe.TStat)get_store().add_element_user(CSTAT$12);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XMOTIVO$14, 0);
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
                target = (br.inf.portalfiscal.nfe.TMotivo)get_store().find_element_user(XMOTIVO$14, 0);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XMOTIVO$14, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(XMOTIVO$14);
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
                target = (br.inf.portalfiscal.nfe.TMotivo)get_store().find_element_user(XMOTIVO$14, 0);
                if (target == null)
                {
                    target = (br.inf.portalfiscal.nfe.TMotivo)get_store().add_element_user(XMOTIVO$14);
                }
                target.set(xMotivo);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ID$16);
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
                target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(ID$16);
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
                return get_store().find_attribute_user(ID$16) != null;
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ID$16);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ID$16);
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
                target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(ID$16);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlID)get_store().add_attribute_user(ID$16);
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
                get_store().remove_attribute(ID$16);
            }
        }
    }
}
