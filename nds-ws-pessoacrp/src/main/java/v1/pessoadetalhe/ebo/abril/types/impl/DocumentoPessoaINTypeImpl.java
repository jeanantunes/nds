/*
 * XML Type:  DocumentoPessoaINType
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaINType
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * An XML DocumentoPessoaINType(@http://abril.ebo.pessoadetalhe.v1/types/).
 *
 * This is a complex type.
 */
public class DocumentoPessoaINTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaINType
{
    
    public DocumentoPessoaINTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NUMERODOCUMENTO$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "numeroDocumento");
    private static final javax.xml.namespace.QName TIPODOCUMENTO$2 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "tipoDocumento");
    private static final javax.xml.namespace.QName SGLUFIE$4 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "sglUfIE");
    
    
    /**
     * Gets the "numeroDocumento" element
     */
    public java.lang.String getNumeroDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NUMERODOCUMENTO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "numeroDocumento" element
     */
    public org.apache.xmlbeans.XmlString xgetNumeroDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NUMERODOCUMENTO$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "numeroDocumento" element
     */
    public void setNumeroDocumento(java.lang.String numeroDocumento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NUMERODOCUMENTO$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NUMERODOCUMENTO$0);
            }
            target.setStringValue(numeroDocumento);
        }
    }
    
    /**
     * Sets (as xml) the "numeroDocumento" element
     */
    public void xsetNumeroDocumento(org.apache.xmlbeans.XmlString numeroDocumento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NUMERODOCUMENTO$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NUMERODOCUMENTO$0);
            }
            target.set(numeroDocumento);
        }
    }
    
    /**
     * Gets the "tipoDocumento" element
     */
    public long getTipoDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TIPODOCUMENTO$2, 0);
            if (target == null)
            {
                return 0L;
            }
            return target.getLongValue();
        }
    }
    
    /**
     * Gets (as xml) the "tipoDocumento" element
     */
    public org.apache.xmlbeans.XmlLong xgetTipoDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(TIPODOCUMENTO$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "tipoDocumento" element
     */
    public void setTipoDocumento(long tipoDocumento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TIPODOCUMENTO$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(TIPODOCUMENTO$2);
            }
            target.setLongValue(tipoDocumento);
        }
    }
    
    /**
     * Sets (as xml) the "tipoDocumento" element
     */
    public void xsetTipoDocumento(org.apache.xmlbeans.XmlLong tipoDocumento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(TIPODOCUMENTO$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_element_user(TIPODOCUMENTO$2);
            }
            target.set(tipoDocumento);
        }
    }
    
    /**
     * Gets the "sglUfIE" element
     */
    public java.lang.String getSglUfIE()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SGLUFIE$4, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "sglUfIE" element
     */
    public org.apache.xmlbeans.XmlString xgetSglUfIE()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SGLUFIE$4, 0);
            return target;
        }
    }
    
    /**
     * Sets the "sglUfIE" element
     */
    public void setSglUfIE(java.lang.String sglUfIE)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SGLUFIE$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SGLUFIE$4);
            }
            target.setStringValue(sglUfIE);
        }
    }
    
    /**
     * Sets (as xml) the "sglUfIE" element
     */
    public void xsetSglUfIE(org.apache.xmlbeans.XmlString sglUfIE)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SGLUFIE$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SGLUFIE$4);
            }
            target.set(sglUfIE);
        }
    }
}
