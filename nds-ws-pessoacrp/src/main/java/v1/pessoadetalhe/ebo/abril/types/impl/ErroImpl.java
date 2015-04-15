/*
 * XML Type:  Erro
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.Erro
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * An XML Erro(@http://abril.ebo.pessoadetalhe.v1/types/).
 *
 * This is a complex type.
 */
public class ErroImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.Erro
{
    
    public ErroImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CODIGOERRO$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codigoErro");
    private static final javax.xml.namespace.QName DESCRICAOERRO$2 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "descricaoErro");
    
    
    /**
     * Gets the "codigoErro" element
     */
    public java.math.BigInteger getCodigoErro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOERRO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getBigIntegerValue();
        }
    }
    
    /**
     * Gets (as xml) the "codigoErro" element
     */
    public org.apache.xmlbeans.XmlInteger xgetCodigoErro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInteger target = null;
            target = (org.apache.xmlbeans.XmlInteger)get_store().find_element_user(CODIGOERRO$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "codigoErro" element
     */
    public void setCodigoErro(java.math.BigInteger codigoErro)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOERRO$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CODIGOERRO$0);
            }
            target.setBigIntegerValue(codigoErro);
        }
    }
    
    /**
     * Sets (as xml) the "codigoErro" element
     */
    public void xsetCodigoErro(org.apache.xmlbeans.XmlInteger codigoErro)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInteger target = null;
            target = (org.apache.xmlbeans.XmlInteger)get_store().find_element_user(CODIGOERRO$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInteger)get_store().add_element_user(CODIGOERRO$0);
            }
            target.set(codigoErro);
        }
    }
    
    /**
     * Gets the "descricaoErro" element
     */
    public java.lang.String getDescricaoErro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DESCRICAOERRO$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "descricaoErro" element
     */
    public org.apache.xmlbeans.XmlString xgetDescricaoErro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOERRO$2, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "descricaoErro" element
     */
    public boolean isNilDescricaoErro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOERRO$2, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "descricaoErro" element
     */
    public void setDescricaoErro(java.lang.String descricaoErro)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DESCRICAOERRO$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DESCRICAOERRO$2);
            }
            target.setStringValue(descricaoErro);
        }
    }
    
    /**
     * Sets (as xml) the "descricaoErro" element
     */
    public void xsetDescricaoErro(org.apache.xmlbeans.XmlString descricaoErro)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOERRO$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DESCRICAOERRO$2);
            }
            target.set(descricaoErro);
        }
    }
    
    /**
     * Nils the "descricaoErro" element
     */
    public void setNilDescricaoErro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOERRO$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DESCRICAOERRO$2);
            }
            target.setNil();
        }
    }
}
