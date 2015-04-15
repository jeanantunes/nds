/*
 * XML Type:  TIpi
 * Namespace: http://www.portalfiscal.inf.br/nfe
 * Java type: br.inf.portalfiscal.nfe.TIpi
 *
 * Automatically generated - do not modify.
 */
package br.inf.portalfiscal.nfe.impl;
/**
 * An XML TIpi(@http://www.portalfiscal.inf.br/nfe).
 *
 * This is a complex type.
 */
public class TIpiImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.inf.portalfiscal.nfe.TIpi
{
    
    public TIpiImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CLENQ$0 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "clEnq");
    private static final javax.xml.namespace.QName CNPJPROD$2 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "CNPJProd");
    private static final javax.xml.namespace.QName CSELO$4 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "cSelo");
    private static final javax.xml.namespace.QName QSELO$6 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "qSelo");
    private static final javax.xml.namespace.QName CENQ$8 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "cEnq");
    private static final javax.xml.namespace.QName IPITRIB$10 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "IPITrib");
    private static final javax.xml.namespace.QName IPINT$12 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "IPINT");
    
    
    /**
     * Gets the "clEnq" element
     */
    public java.lang.String getClEnq()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CLENQ$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "clEnq" element
     */
    public br.inf.portalfiscal.nfe.TIpi.ClEnq xgetClEnq()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TIpi.ClEnq target = null;
            target = (br.inf.portalfiscal.nfe.TIpi.ClEnq)get_store().find_element_user(CLENQ$0, 0);
            return target;
        }
    }
    
    /**
     * True if has "clEnq" element
     */
    public boolean isSetClEnq()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CLENQ$0) != 0;
        }
    }
    
    /**
     * Sets the "clEnq" element
     */
    public void setClEnq(java.lang.String clEnq)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CLENQ$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CLENQ$0);
            }
            target.setStringValue(clEnq);
        }
    }
    
    /**
     * Sets (as xml) the "clEnq" element
     */
    public void xsetClEnq(br.inf.portalfiscal.nfe.TIpi.ClEnq clEnq)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TIpi.ClEnq target = null;
            target = (br.inf.portalfiscal.nfe.TIpi.ClEnq)get_store().find_element_user(CLENQ$0, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TIpi.ClEnq)get_store().add_element_user(CLENQ$0);
            }
            target.set(clEnq);
        }
    }
    
    /**
     * Unsets the "clEnq" element
     */
    public void unsetClEnq()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CLENQ$0, 0);
        }
    }
    
    /**
     * Gets the "CNPJProd" element
     */
    public java.lang.String getCNPJProd()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CNPJPROD$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "CNPJProd" element
     */
    public br.inf.portalfiscal.nfe.TCnpj xgetCNPJProd()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TCnpj target = null;
            target = (br.inf.portalfiscal.nfe.TCnpj)get_store().find_element_user(CNPJPROD$2, 0);
            return target;
        }
    }
    
    /**
     * True if has "CNPJProd" element
     */
    public boolean isSetCNPJProd()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CNPJPROD$2) != 0;
        }
    }
    
    /**
     * Sets the "CNPJProd" element
     */
    public void setCNPJProd(java.lang.String cnpjProd)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CNPJPROD$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CNPJPROD$2);
            }
            target.setStringValue(cnpjProd);
        }
    }
    
    /**
     * Sets (as xml) the "CNPJProd" element
     */
    public void xsetCNPJProd(br.inf.portalfiscal.nfe.TCnpj cnpjProd)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TCnpj target = null;
            target = (br.inf.portalfiscal.nfe.TCnpj)get_store().find_element_user(CNPJPROD$2, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TCnpj)get_store().add_element_user(CNPJPROD$2);
            }
            target.set(cnpjProd);
        }
    }
    
    /**
     * Unsets the "CNPJProd" element
     */
    public void unsetCNPJProd()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CNPJPROD$2, 0);
        }
    }
    
    /**
     * Gets the "cSelo" element
     */
    public java.lang.String getCSelo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CSELO$4, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "cSelo" element
     */
    public br.inf.portalfiscal.nfe.TIpi.CSelo xgetCSelo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TIpi.CSelo target = null;
            target = (br.inf.portalfiscal.nfe.TIpi.CSelo)get_store().find_element_user(CSELO$4, 0);
            return target;
        }
    }
    
    /**
     * True if has "cSelo" element
     */
    public boolean isSetCSelo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CSELO$4) != 0;
        }
    }
    
    /**
     * Sets the "cSelo" element
     */
    public void setCSelo(java.lang.String cSelo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CSELO$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CSELO$4);
            }
            target.setStringValue(cSelo);
        }
    }
    
    /**
     * Sets (as xml) the "cSelo" element
     */
    public void xsetCSelo(br.inf.portalfiscal.nfe.TIpi.CSelo cSelo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TIpi.CSelo target = null;
            target = (br.inf.portalfiscal.nfe.TIpi.CSelo)get_store().find_element_user(CSELO$4, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TIpi.CSelo)get_store().add_element_user(CSELO$4);
            }
            target.set(cSelo);
        }
    }
    
    /**
     * Unsets the "cSelo" element
     */
    public void unsetCSelo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CSELO$4, 0);
        }
    }
    
    /**
     * Gets the "qSelo" element
     */
    public java.lang.String getQSelo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(QSELO$6, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "qSelo" element
     */
    public br.inf.portalfiscal.nfe.TIpi.QSelo xgetQSelo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TIpi.QSelo target = null;
            target = (br.inf.portalfiscal.nfe.TIpi.QSelo)get_store().find_element_user(QSELO$6, 0);
            return target;
        }
    }
    
    /**
     * True if has "qSelo" element
     */
    public boolean isSetQSelo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(QSELO$6) != 0;
        }
    }
    
    /**
     * Sets the "qSelo" element
     */
    public void setQSelo(java.lang.String qSelo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(QSELO$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(QSELO$6);
            }
            target.setStringValue(qSelo);
        }
    }
    
    /**
     * Sets (as xml) the "qSelo" element
     */
    public void xsetQSelo(br.inf.portalfiscal.nfe.TIpi.QSelo qSelo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TIpi.QSelo target = null;
            target = (br.inf.portalfiscal.nfe.TIpi.QSelo)get_store().find_element_user(QSELO$6, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TIpi.QSelo)get_store().add_element_user(QSELO$6);
            }
            target.set(qSelo);
        }
    }
    
    /**
     * Unsets the "qSelo" element
     */
    public void unsetQSelo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(QSELO$6, 0);
        }
    }
    
    /**
     * Gets the "cEnq" element
     */
    public java.lang.String getCEnq()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CENQ$8, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "cEnq" element
     */
    public br.inf.portalfiscal.nfe.TIpi.CEnq xgetCEnq()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TIpi.CEnq target = null;
            target = (br.inf.portalfiscal.nfe.TIpi.CEnq)get_store().find_element_user(CENQ$8, 0);
            return target;
        }
    }
    
    /**
     * Sets the "cEnq" element
     */
    public void setCEnq(java.lang.String cEnq)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CENQ$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CENQ$8);
            }
            target.setStringValue(cEnq);
        }
    }
    
    /**
     * Sets (as xml) the "cEnq" element
     */
    public void xsetCEnq(br.inf.portalfiscal.nfe.TIpi.CEnq cEnq)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TIpi.CEnq target = null;
            target = (br.inf.portalfiscal.nfe.TIpi.CEnq)get_store().find_element_user(CENQ$8, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TIpi.CEnq)get_store().add_element_user(CENQ$8);
            }
            target.set(cEnq);
        }
    }
    
    /**
     * Gets the "IPITrib" element
     */
    public br.inf.portalfiscal.nfe.TIpi.IPITrib getIPITrib()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TIpi.IPITrib target = null;
            target = (br.inf.portalfiscal.nfe.TIpi.IPITrib)get_store().find_element_user(IPITRIB$10, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "IPITrib" element
     */
    public boolean isSetIPITrib()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(IPITRIB$10) != 0;
        }
    }
    
    /**
     * Sets the "IPITrib" element
     */
    public void setIPITrib(br.inf.portalfiscal.nfe.TIpi.IPITrib ipiTrib)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TIpi.IPITrib target = null;
            target = (br.inf.portalfiscal.nfe.TIpi.IPITrib)get_store().find_element_user(IPITRIB$10, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TIpi.IPITrib)get_store().add_element_user(IPITRIB$10);
            }
            target.set(ipiTrib);
        }
    }
    
    /**
     * Appends and returns a new empty "IPITrib" element
     */
    public br.inf.portalfiscal.nfe.TIpi.IPITrib addNewIPITrib()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TIpi.IPITrib target = null;
            target = (br.inf.portalfiscal.nfe.TIpi.IPITrib)get_store().add_element_user(IPITRIB$10);
            return target;
        }
    }
    
    /**
     * Unsets the "IPITrib" element
     */
    public void unsetIPITrib()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(IPITRIB$10, 0);
        }
    }
    
    /**
     * Gets the "IPINT" element
     */
    public br.inf.portalfiscal.nfe.TIpi.IPINT getIPINT()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TIpi.IPINT target = null;
            target = (br.inf.portalfiscal.nfe.TIpi.IPINT)get_store().find_element_user(IPINT$12, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "IPINT" element
     */
    public boolean isSetIPINT()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(IPINT$12) != 0;
        }
    }
    
    /**
     * Sets the "IPINT" element
     */
    public void setIPINT(br.inf.portalfiscal.nfe.TIpi.IPINT ipint)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TIpi.IPINT target = null;
            target = (br.inf.portalfiscal.nfe.TIpi.IPINT)get_store().find_element_user(IPINT$12, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TIpi.IPINT)get_store().add_element_user(IPINT$12);
            }
            target.set(ipint);
        }
    }
    
    /**
     * Appends and returns a new empty "IPINT" element
     */
    public br.inf.portalfiscal.nfe.TIpi.IPINT addNewIPINT()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TIpi.IPINT target = null;
            target = (br.inf.portalfiscal.nfe.TIpi.IPINT)get_store().add_element_user(IPINT$12);
            return target;
        }
    }
    
    /**
     * Unsets the "IPINT" element
     */
    public void unsetIPINT()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(IPINT$12, 0);
        }
    }
    /**
     * An XML clEnq(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TIpi$ClEnq.
     */
    public static class ClEnqImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements br.inf.portalfiscal.nfe.TIpi.ClEnq
    {
        
        public ClEnqImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected ClEnqImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
    /**
     * An XML cSelo(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TIpi$CSelo.
     */
    public static class CSeloImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements br.inf.portalfiscal.nfe.TIpi.CSelo
    {
        
        public CSeloImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected CSeloImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
    /**
     * An XML qSelo(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TIpi$QSelo.
     */
    public static class QSeloImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements br.inf.portalfiscal.nfe.TIpi.QSelo
    {
        
        public QSeloImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected QSeloImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
    /**
     * An XML cEnq(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TIpi$CEnq.
     */
    public static class CEnqImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements br.inf.portalfiscal.nfe.TIpi.CEnq
    {
        
        public CEnqImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected CEnqImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
    /**
     * An XML IPITrib(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is a complex type.
     */
    public static class IPITribImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.inf.portalfiscal.nfe.TIpi.IPITrib
    {
        
        public IPITribImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName CST$0 = 
            new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "CST");
        private static final javax.xml.namespace.QName VBC$2 = 
            new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "vBC");
        private static final javax.xml.namespace.QName PIPI$4 = 
            new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "pIPI");
        private static final javax.xml.namespace.QName QUNID$6 = 
            new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "qUnid");
        private static final javax.xml.namespace.QName VUNID$8 = 
            new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "vUnid");
        private static final javax.xml.namespace.QName VIPI$10 = 
            new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "vIPI");
        
        
        /**
         * Gets the "CST" element
         */
        public br.inf.portalfiscal.nfe.TIpi.IPITrib.CST.Enum getCST()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CST$0, 0);
                if (target == null)
                {
                    return null;
                }
                return (br.inf.portalfiscal.nfe.TIpi.IPITrib.CST.Enum)target.getEnumValue();
            }
        }
        
        /**
         * Gets (as xml) the "CST" element
         */
        public br.inf.portalfiscal.nfe.TIpi.IPITrib.CST xgetCST()
        {
            synchronized (monitor())
            {
                check_orphaned();
                br.inf.portalfiscal.nfe.TIpi.IPITrib.CST target = null;
                target = (br.inf.portalfiscal.nfe.TIpi.IPITrib.CST)get_store().find_element_user(CST$0, 0);
                return target;
            }
        }
        
        /**
         * Sets the "CST" element
         */
        public void setCST(br.inf.portalfiscal.nfe.TIpi.IPITrib.CST.Enum cst)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CST$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CST$0);
                }
                target.setEnumValue(cst);
            }
        }
        
        /**
         * Sets (as xml) the "CST" element
         */
        public void xsetCST(br.inf.portalfiscal.nfe.TIpi.IPITrib.CST cst)
        {
            synchronized (monitor())
            {
                check_orphaned();
                br.inf.portalfiscal.nfe.TIpi.IPITrib.CST target = null;
                target = (br.inf.portalfiscal.nfe.TIpi.IPITrib.CST)get_store().find_element_user(CST$0, 0);
                if (target == null)
                {
                    target = (br.inf.portalfiscal.nfe.TIpi.IPITrib.CST)get_store().add_element_user(CST$0);
                }
                target.set(cst);
            }
        }
        
        /**
         * Gets the "vBC" element
         */
        public java.lang.String getVBC()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VBC$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "vBC" element
         */
        public br.inf.portalfiscal.nfe.TDec1302 xgetVBC()
        {
            synchronized (monitor())
            {
                check_orphaned();
                br.inf.portalfiscal.nfe.TDec1302 target = null;
                target = (br.inf.portalfiscal.nfe.TDec1302)get_store().find_element_user(VBC$2, 0);
                return target;
            }
        }
        
        /**
         * True if has "vBC" element
         */
        public boolean isSetVBC()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(VBC$2) != 0;
            }
        }
        
        /**
         * Sets the "vBC" element
         */
        public void setVBC(java.lang.String vbc)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VBC$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(VBC$2);
                }
                target.setStringValue(vbc);
            }
        }
        
        /**
         * Sets (as xml) the "vBC" element
         */
        public void xsetVBC(br.inf.portalfiscal.nfe.TDec1302 vbc)
        {
            synchronized (monitor())
            {
                check_orphaned();
                br.inf.portalfiscal.nfe.TDec1302 target = null;
                target = (br.inf.portalfiscal.nfe.TDec1302)get_store().find_element_user(VBC$2, 0);
                if (target == null)
                {
                    target = (br.inf.portalfiscal.nfe.TDec1302)get_store().add_element_user(VBC$2);
                }
                target.set(vbc);
            }
        }
        
        /**
         * Unsets the "vBC" element
         */
        public void unsetVBC()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(VBC$2, 0);
            }
        }
        
        /**
         * Gets the "pIPI" element
         */
        public java.lang.String getPIPI()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PIPI$4, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "pIPI" element
         */
        public br.inf.portalfiscal.nfe.TDec0302A04 xgetPIPI()
        {
            synchronized (monitor())
            {
                check_orphaned();
                br.inf.portalfiscal.nfe.TDec0302A04 target = null;
                target = (br.inf.portalfiscal.nfe.TDec0302A04)get_store().find_element_user(PIPI$4, 0);
                return target;
            }
        }
        
        /**
         * True if has "pIPI" element
         */
        public boolean isSetPIPI()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(PIPI$4) != 0;
            }
        }
        
        /**
         * Sets the "pIPI" element
         */
        public void setPIPI(java.lang.String pipi)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PIPI$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PIPI$4);
                }
                target.setStringValue(pipi);
            }
        }
        
        /**
         * Sets (as xml) the "pIPI" element
         */
        public void xsetPIPI(br.inf.portalfiscal.nfe.TDec0302A04 pipi)
        {
            synchronized (monitor())
            {
                check_orphaned();
                br.inf.portalfiscal.nfe.TDec0302A04 target = null;
                target = (br.inf.portalfiscal.nfe.TDec0302A04)get_store().find_element_user(PIPI$4, 0);
                if (target == null)
                {
                    target = (br.inf.portalfiscal.nfe.TDec0302A04)get_store().add_element_user(PIPI$4);
                }
                target.set(pipi);
            }
        }
        
        /**
         * Unsets the "pIPI" element
         */
        public void unsetPIPI()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(PIPI$4, 0);
            }
        }
        
        /**
         * Gets the "qUnid" element
         */
        public java.lang.String getQUnid()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(QUNID$6, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "qUnid" element
         */
        public br.inf.portalfiscal.nfe.TDec1204V xgetQUnid()
        {
            synchronized (monitor())
            {
                check_orphaned();
                br.inf.portalfiscal.nfe.TDec1204V target = null;
                target = (br.inf.portalfiscal.nfe.TDec1204V)get_store().find_element_user(QUNID$6, 0);
                return target;
            }
        }
        
        /**
         * True if has "qUnid" element
         */
        public boolean isSetQUnid()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(QUNID$6) != 0;
            }
        }
        
        /**
         * Sets the "qUnid" element
         */
        public void setQUnid(java.lang.String qUnid)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(QUNID$6, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(QUNID$6);
                }
                target.setStringValue(qUnid);
            }
        }
        
        /**
         * Sets (as xml) the "qUnid" element
         */
        public void xsetQUnid(br.inf.portalfiscal.nfe.TDec1204V qUnid)
        {
            synchronized (monitor())
            {
                check_orphaned();
                br.inf.portalfiscal.nfe.TDec1204V target = null;
                target = (br.inf.portalfiscal.nfe.TDec1204V)get_store().find_element_user(QUNID$6, 0);
                if (target == null)
                {
                    target = (br.inf.portalfiscal.nfe.TDec1204V)get_store().add_element_user(QUNID$6);
                }
                target.set(qUnid);
            }
        }
        
        /**
         * Unsets the "qUnid" element
         */
        public void unsetQUnid()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(QUNID$6, 0);
            }
        }
        
        /**
         * Gets the "vUnid" element
         */
        public java.lang.String getVUnid()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VUNID$8, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "vUnid" element
         */
        public br.inf.portalfiscal.nfe.TDec1104 xgetVUnid()
        {
            synchronized (monitor())
            {
                check_orphaned();
                br.inf.portalfiscal.nfe.TDec1104 target = null;
                target = (br.inf.portalfiscal.nfe.TDec1104)get_store().find_element_user(VUNID$8, 0);
                return target;
            }
        }
        
        /**
         * True if has "vUnid" element
         */
        public boolean isSetVUnid()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(VUNID$8) != 0;
            }
        }
        
        /**
         * Sets the "vUnid" element
         */
        public void setVUnid(java.lang.String vUnid)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VUNID$8, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(VUNID$8);
                }
                target.setStringValue(vUnid);
            }
        }
        
        /**
         * Sets (as xml) the "vUnid" element
         */
        public void xsetVUnid(br.inf.portalfiscal.nfe.TDec1104 vUnid)
        {
            synchronized (monitor())
            {
                check_orphaned();
                br.inf.portalfiscal.nfe.TDec1104 target = null;
                target = (br.inf.portalfiscal.nfe.TDec1104)get_store().find_element_user(VUNID$8, 0);
                if (target == null)
                {
                    target = (br.inf.portalfiscal.nfe.TDec1104)get_store().add_element_user(VUNID$8);
                }
                target.set(vUnid);
            }
        }
        
        /**
         * Unsets the "vUnid" element
         */
        public void unsetVUnid()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(VUNID$8, 0);
            }
        }
        
        /**
         * Gets the "vIPI" element
         */
        public java.lang.String getVIPI()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VIPI$10, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "vIPI" element
         */
        public br.inf.portalfiscal.nfe.TDec1302 xgetVIPI()
        {
            synchronized (monitor())
            {
                check_orphaned();
                br.inf.portalfiscal.nfe.TDec1302 target = null;
                target = (br.inf.portalfiscal.nfe.TDec1302)get_store().find_element_user(VIPI$10, 0);
                return target;
            }
        }
        
        /**
         * Sets the "vIPI" element
         */
        public void setVIPI(java.lang.String vipi)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VIPI$10, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(VIPI$10);
                }
                target.setStringValue(vipi);
            }
        }
        
        /**
         * Sets (as xml) the "vIPI" element
         */
        public void xsetVIPI(br.inf.portalfiscal.nfe.TDec1302 vipi)
        {
            synchronized (monitor())
            {
                check_orphaned();
                br.inf.portalfiscal.nfe.TDec1302 target = null;
                target = (br.inf.portalfiscal.nfe.TDec1302)get_store().find_element_user(VIPI$10, 0);
                if (target == null)
                {
                    target = (br.inf.portalfiscal.nfe.TDec1302)get_store().add_element_user(VIPI$10);
                }
                target.set(vipi);
            }
        }
        /**
         * An XML CST(@http://www.portalfiscal.inf.br/nfe).
         *
         * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TIpi$IPITrib$CST.
         */
        public static class CSTImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements br.inf.portalfiscal.nfe.TIpi.IPITrib.CST
        {
            
            public CSTImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType, false);
            }
            
            protected CSTImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
            {
                super(sType, b);
            }
        }
    }
    /**
     * An XML IPINT(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is a complex type.
     */
    public static class IPINTImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.inf.portalfiscal.nfe.TIpi.IPINT
    {
        
        public IPINTImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName CST$0 = 
            new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "CST");
        
        
        /**
         * Gets the "CST" element
         */
        public br.inf.portalfiscal.nfe.TIpi.IPINT.CST.Enum getCST()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CST$0, 0);
                if (target == null)
                {
                    return null;
                }
                return (br.inf.portalfiscal.nfe.TIpi.IPINT.CST.Enum)target.getEnumValue();
            }
        }
        
        /**
         * Gets (as xml) the "CST" element
         */
        public br.inf.portalfiscal.nfe.TIpi.IPINT.CST xgetCST()
        {
            synchronized (monitor())
            {
                check_orphaned();
                br.inf.portalfiscal.nfe.TIpi.IPINT.CST target = null;
                target = (br.inf.portalfiscal.nfe.TIpi.IPINT.CST)get_store().find_element_user(CST$0, 0);
                return target;
            }
        }
        
        /**
         * Sets the "CST" element
         */
        public void setCST(br.inf.portalfiscal.nfe.TIpi.IPINT.CST.Enum cst)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CST$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CST$0);
                }
                target.setEnumValue(cst);
            }
        }
        
        /**
         * Sets (as xml) the "CST" element
         */
        public void xsetCST(br.inf.portalfiscal.nfe.TIpi.IPINT.CST cst)
        {
            synchronized (monitor())
            {
                check_orphaned();
                br.inf.portalfiscal.nfe.TIpi.IPINT.CST target = null;
                target = (br.inf.portalfiscal.nfe.TIpi.IPINT.CST)get_store().find_element_user(CST$0, 0);
                if (target == null)
                {
                    target = (br.inf.portalfiscal.nfe.TIpi.IPINT.CST)get_store().add_element_user(CST$0);
                }
                target.set(cst);
            }
        }
        /**
         * An XML CST(@http://www.portalfiscal.inf.br/nfe).
         *
         * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TIpi$IPINT$CST.
         */
        public static class CSTImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements br.inf.portalfiscal.nfe.TIpi.IPINT.CST
        {
            
            public CSTImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType, false);
            }
            
            protected CSTImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
            {
                super(sType, b);
            }
        }
    }
}
