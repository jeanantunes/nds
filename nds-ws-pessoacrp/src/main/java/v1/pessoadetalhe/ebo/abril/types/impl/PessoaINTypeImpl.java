/*
 * XML Type:  PessoaINType
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.PessoaINType
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * An XML PessoaINType(@http://abril.ebo.pessoadetalhe.v1/types/).
 *
 * This is a complex type.
 */
public class PessoaINTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.PessoaINType
{
    
    public PessoaINTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CODIGOTIPOPESSOA$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codigoTipoPessoa");
    private static final javax.xml.namespace.QName NOME$2 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "nome");
    
    
    /**
     * Gets the "codigoTipoPessoa" element
     */
    public long getCodigoTipoPessoa()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOTIPOPESSOA$0, 0);
            if (target == null)
            {
                return 0L;
            }
            return target.getLongValue();
        }
    }
    
    /**
     * Gets (as xml) the "codigoTipoPessoa" element
     */
    public org.apache.xmlbeans.XmlLong xgetCodigoTipoPessoa()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGOTIPOPESSOA$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "codigoTipoPessoa" element
     */
    public void setCodigoTipoPessoa(long codigoTipoPessoa)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOTIPOPESSOA$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CODIGOTIPOPESSOA$0);
            }
            target.setLongValue(codigoTipoPessoa);
        }
    }
    
    /**
     * Sets (as xml) the "codigoTipoPessoa" element
     */
    public void xsetCodigoTipoPessoa(org.apache.xmlbeans.XmlLong codigoTipoPessoa)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGOTIPOPESSOA$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_element_user(CODIGOTIPOPESSOA$0);
            }
            target.set(codigoTipoPessoa);
        }
    }
    
    /**
     * Gets the "nome" element
     */
    public java.lang.String getNome()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NOME$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "nome" element
     */
    public org.apache.xmlbeans.XmlString xgetNome()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOME$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "nome" element
     */
    public void setNome(java.lang.String nome)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NOME$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NOME$2);
            }
            target.setStringValue(nome);
        }
    }
    
    /**
     * Sets (as xml) the "nome" element
     */
    public void xsetNome(org.apache.xmlbeans.XmlString nome)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOME$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NOME$2);
            }
            target.set(nome);
        }
    }
}
