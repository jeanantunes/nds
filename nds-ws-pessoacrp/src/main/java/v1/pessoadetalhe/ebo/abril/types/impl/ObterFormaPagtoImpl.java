/*
 * XML Type:  obterFormaPagto
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.ObterFormaPagto
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * An XML obterFormaPagto(@http://abril.ebo.pessoadetalhe.v1/types/).
 *
 * This is a complex type.
 */
public class ObterFormaPagtoImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.ObterFormaPagto
{
    
    public ObterFormaPagtoImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CODTIPODOC$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codTipoDoc");
    private static final javax.xml.namespace.QName NUMDOC$2 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "numDoc");
    private static final javax.xml.namespace.QName CODSISTEMA$4 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codSistema");
    
    
    /**
     * Gets the "codTipoDoc" element
     */
    public int getCodTipoDoc()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODTIPODOC$0, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "codTipoDoc" element
     */
    public org.apache.xmlbeans.XmlInt xgetCodTipoDoc()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(CODTIPODOC$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "codTipoDoc" element
     */
    public void setCodTipoDoc(int codTipoDoc)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODTIPODOC$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CODTIPODOC$0);
            }
            target.setIntValue(codTipoDoc);
        }
    }
    
    /**
     * Sets (as xml) the "codTipoDoc" element
     */
    public void xsetCodTipoDoc(org.apache.xmlbeans.XmlInt codTipoDoc)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(CODTIPODOC$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(CODTIPODOC$0);
            }
            target.set(codTipoDoc);
        }
    }
    
    /**
     * Gets the "numDoc" element
     */
    public java.lang.String getNumDoc()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NUMDOC$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "numDoc" element
     */
    public org.apache.xmlbeans.XmlString xgetNumDoc()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NUMDOC$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "numDoc" element
     */
    public void setNumDoc(java.lang.String numDoc)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NUMDOC$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NUMDOC$2);
            }
            target.setStringValue(numDoc);
        }
    }
    
    /**
     * Sets (as xml) the "numDoc" element
     */
    public void xsetNumDoc(org.apache.xmlbeans.XmlString numDoc)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NUMDOC$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NUMDOC$2);
            }
            target.set(numDoc);
        }
    }
    
    /**
     * Gets the "codSistema" element
     */
    public java.lang.String getCodSistema()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODSISTEMA$4, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "codSistema" element
     */
    public org.apache.xmlbeans.XmlString xgetCodSistema()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODSISTEMA$4, 0);
            return target;
        }
    }
    
    /**
     * Sets the "codSistema" element
     */
    public void setCodSistema(java.lang.String codSistema)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODSISTEMA$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CODSISTEMA$4);
            }
            target.setStringValue(codSistema);
        }
    }
    
    /**
     * Sets (as xml) the "codSistema" element
     */
    public void xsetCodSistema(org.apache.xmlbeans.XmlString codSistema)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODSISTEMA$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CODSISTEMA$4);
            }
            target.set(codSistema);
        }
    }
}
