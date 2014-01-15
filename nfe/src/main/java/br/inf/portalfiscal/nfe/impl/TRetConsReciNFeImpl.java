/*
 * XML Type:  TRetConsReciNFe
 * Namespace: http://www.portalfiscal.inf.br/nfe
 * Java type: br.inf.portalfiscal.nfe.TRetConsReciNFe
 *
 * Automatically generated - do not modify.
 */
package br.inf.portalfiscal.nfe.impl;
/**
 * An XML TRetConsReciNFe(@http://www.portalfiscal.inf.br/nfe).
 *
 * This is a complex type.
 */
public class TRetConsReciNFeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.inf.portalfiscal.nfe.TRetConsReciNFe
{
    
    public TRetConsReciNFeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TPAMB$0 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "tpAmb");
    private static final javax.xml.namespace.QName VERAPLIC$2 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "verAplic");
    private static final javax.xml.namespace.QName NREC$4 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "nRec");
    private static final javax.xml.namespace.QName CSTAT$6 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "cStat");
    private static final javax.xml.namespace.QName XMOTIVO$8 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "xMotivo");
    private static final javax.xml.namespace.QName CUF$10 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "cUF");
    private static final javax.xml.namespace.QName DHRECBTO$12 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "dhRecbto");
    private static final javax.xml.namespace.QName CMSG$14 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "cMsg");
    private static final javax.xml.namespace.QName XMSG$16 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "xMsg");
    private static final javax.xml.namespace.QName PROTNFE$18 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "protNFe");
    private static final javax.xml.namespace.QName VERSAO$20 = 
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
     * Gets the "nRec" element
     */
    public java.lang.String getNRec()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NREC$4, 0);
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
            target = (br.inf.portalfiscal.nfe.TRec)get_store().find_element_user(NREC$4, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NREC$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NREC$4);
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
            target = (br.inf.portalfiscal.nfe.TRec)get_store().find_element_user(NREC$4, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TRec)get_store().add_element_user(NREC$4);
            }
            target.set(nRec);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CSTAT$6, 0);
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
            target = (br.inf.portalfiscal.nfe.TStat)get_store().find_element_user(CSTAT$6, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CSTAT$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CSTAT$6);
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
            target = (br.inf.portalfiscal.nfe.TStat)get_store().find_element_user(CSTAT$6, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TStat)get_store().add_element_user(CSTAT$6);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XMOTIVO$8, 0);
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
            target = (br.inf.portalfiscal.nfe.TMotivo)get_store().find_element_user(XMOTIVO$8, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XMOTIVO$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(XMOTIVO$8);
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
            target = (br.inf.portalfiscal.nfe.TMotivo)get_store().find_element_user(XMOTIVO$8, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TMotivo)get_store().add_element_user(XMOTIVO$8);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CUF$10, 0);
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
            target = (br.inf.portalfiscal.nfe.TCodUfIBGE)get_store().find_element_user(CUF$10, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CUF$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CUF$10);
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
            target = (br.inf.portalfiscal.nfe.TCodUfIBGE)get_store().find_element_user(CUF$10, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TCodUfIBGE)get_store().add_element_user(CUF$10);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DHRECBTO$12, 0);
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
            target = (br.inf.portalfiscal.nfe.TDateTimeUTC)get_store().find_element_user(DHRECBTO$12, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DHRECBTO$12, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DHRECBTO$12);
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
            target = (br.inf.portalfiscal.nfe.TDateTimeUTC)get_store().find_element_user(DHRECBTO$12, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TDateTimeUTC)get_store().add_element_user(DHRECBTO$12);
            }
            target.set(dhRecbto);
        }
    }
    
    /**
     * Gets the "cMsg" element
     */
    public java.lang.String getCMsg()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CMSG$14, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "cMsg" element
     */
    public br.inf.portalfiscal.nfe.TRetConsReciNFe.CMsg xgetCMsg()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TRetConsReciNFe.CMsg target = null;
            target = (br.inf.portalfiscal.nfe.TRetConsReciNFe.CMsg)get_store().find_element_user(CMSG$14, 0);
            return target;
        }
    }
    
    /**
     * True if has "cMsg" element
     */
    public boolean isSetCMsg()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CMSG$14) != 0;
        }
    }
    
    /**
     * Sets the "cMsg" element
     */
    public void setCMsg(java.lang.String cMsg)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CMSG$14, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CMSG$14);
            }
            target.setStringValue(cMsg);
        }
    }
    
    /**
     * Sets (as xml) the "cMsg" element
     */
    public void xsetCMsg(br.inf.portalfiscal.nfe.TRetConsReciNFe.CMsg cMsg)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TRetConsReciNFe.CMsg target = null;
            target = (br.inf.portalfiscal.nfe.TRetConsReciNFe.CMsg)get_store().find_element_user(CMSG$14, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TRetConsReciNFe.CMsg)get_store().add_element_user(CMSG$14);
            }
            target.set(cMsg);
        }
    }
    
    /**
     * Unsets the "cMsg" element
     */
    public void unsetCMsg()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CMSG$14, 0);
        }
    }
    
    /**
     * Gets the "xMsg" element
     */
    public java.lang.String getXMsg()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XMSG$16, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "xMsg" element
     */
    public br.inf.portalfiscal.nfe.TRetConsReciNFe.XMsg xgetXMsg()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TRetConsReciNFe.XMsg target = null;
            target = (br.inf.portalfiscal.nfe.TRetConsReciNFe.XMsg)get_store().find_element_user(XMSG$16, 0);
            return target;
        }
    }
    
    /**
     * True if has "xMsg" element
     */
    public boolean isSetXMsg()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(XMSG$16) != 0;
        }
    }
    
    /**
     * Sets the "xMsg" element
     */
    public void setXMsg(java.lang.String xMsg)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(XMSG$16, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(XMSG$16);
            }
            target.setStringValue(xMsg);
        }
    }
    
    /**
     * Sets (as xml) the "xMsg" element
     */
    public void xsetXMsg(br.inf.portalfiscal.nfe.TRetConsReciNFe.XMsg xMsg)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TRetConsReciNFe.XMsg target = null;
            target = (br.inf.portalfiscal.nfe.TRetConsReciNFe.XMsg)get_store().find_element_user(XMSG$16, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TRetConsReciNFe.XMsg)get_store().add_element_user(XMSG$16);
            }
            target.set(xMsg);
        }
    }
    
    /**
     * Unsets the "xMsg" element
     */
    public void unsetXMsg()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(XMSG$16, 0);
        }
    }
    
    /**
     * Gets array of all "protNFe" elements
     */
    public br.inf.portalfiscal.nfe.TProtNFe[] getProtNFeArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(PROTNFE$18, targetList);
            br.inf.portalfiscal.nfe.TProtNFe[] result = new br.inf.portalfiscal.nfe.TProtNFe[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "protNFe" element
     */
    public br.inf.portalfiscal.nfe.TProtNFe getProtNFeArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TProtNFe target = null;
            target = (br.inf.portalfiscal.nfe.TProtNFe)get_store().find_element_user(PROTNFE$18, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "protNFe" element
     */
    public int sizeOfProtNFeArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PROTNFE$18);
        }
    }
    
    /**
     * Sets array of all "protNFe" element
     */
    public void setProtNFeArray(br.inf.portalfiscal.nfe.TProtNFe[] protNFeArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(protNFeArray, PROTNFE$18);
        }
    }
    
    /**
     * Sets ith "protNFe" element
     */
    public void setProtNFeArray(int i, br.inf.portalfiscal.nfe.TProtNFe protNFe)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TProtNFe target = null;
            target = (br.inf.portalfiscal.nfe.TProtNFe)get_store().find_element_user(PROTNFE$18, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(protNFe);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "protNFe" element
     */
    public br.inf.portalfiscal.nfe.TProtNFe insertNewProtNFe(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TProtNFe target = null;
            target = (br.inf.portalfiscal.nfe.TProtNFe)get_store().insert_element_user(PROTNFE$18, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "protNFe" element
     */
    public br.inf.portalfiscal.nfe.TProtNFe addNewProtNFe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TProtNFe target = null;
            target = (br.inf.portalfiscal.nfe.TProtNFe)get_store().add_element_user(PROTNFE$18);
            return target;
        }
    }
    
    /**
     * Removes the ith "protNFe" element
     */
    public void removeProtNFe(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PROTNFE$18, i);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VERSAO$20);
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
            target = (br.inf.portalfiscal.nfe.TVerNFe)get_store().find_attribute_user(VERSAO$20);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VERSAO$20);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(VERSAO$20);
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
            target = (br.inf.portalfiscal.nfe.TVerNFe)get_store().find_attribute_user(VERSAO$20);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TVerNFe)get_store().add_attribute_user(VERSAO$20);
            }
            target.set(versao);
        }
    }
    /**
     * An XML cMsg(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TRetConsReciNFe$CMsg.
     */
    public static class CMsgImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements br.inf.portalfiscal.nfe.TRetConsReciNFe.CMsg
    {
        
        public CMsgImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected CMsgImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
    /**
     * An XML xMsg(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TRetConsReciNFe$XMsg.
     */
    public static class XMsgImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements br.inf.portalfiscal.nfe.TRetConsReciNFe.XMsg
    {
        
        public XMsgImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected XMsgImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
}
