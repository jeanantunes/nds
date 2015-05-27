/*
 * XML Type:  adicionarAlterarClienteResponse
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * An XML adicionarAlterarClienteResponse(@http://abril.ebo.pessoadetalhe.v1/types/).
 *
 * This is a complex type.
 */
public class AdicionarAlterarClienteResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse
{
    
    public AdicionarAlterarClienteResponseImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CODIGOPESSOADETALHE$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codigoPessoaDetalhe");
    private static final javax.xml.namespace.QName CODIGOERRO$2 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codigoErro");
    private static final javax.xml.namespace.QName DESCRICAOERRO$4 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "descricaoErro");
    private static final javax.xml.namespace.QName DOCUMENTOS$6 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "documentos");
    private static final javax.xml.namespace.QName ENDERECOS$8 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "enderecos");
    
    
    /**
     * Gets the "codigoPessoaDetalhe" element
     */
    public long getCodigoPessoaDetalhe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOPESSOADETALHE$0, 0);
            if (target == null)
            {
                return 0L;
            }
            return target.getLongValue();
        }
    }
    
    /**
     * Gets (as xml) the "codigoPessoaDetalhe" element
     */
    public org.apache.xmlbeans.XmlLong xgetCodigoPessoaDetalhe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGOPESSOADETALHE$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "codigoPessoaDetalhe" element
     */
    public void setCodigoPessoaDetalhe(long codigoPessoaDetalhe)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOPESSOADETALHE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CODIGOPESSOADETALHE$0);
            }
            target.setLongValue(codigoPessoaDetalhe);
        }
    }
    
    /**
     * Sets (as xml) the "codigoPessoaDetalhe" element
     */
    public void xsetCodigoPessoaDetalhe(org.apache.xmlbeans.XmlLong codigoPessoaDetalhe)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGOPESSOADETALHE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_element_user(CODIGOPESSOADETALHE$0);
            }
            target.set(codigoPessoaDetalhe);
        }
    }
    
    /**
     * Gets the "codigoErro" element
     */
    public java.math.BigInteger getCodigoErro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOERRO$2, 0);
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
            target = (org.apache.xmlbeans.XmlInteger)get_store().find_element_user(CODIGOERRO$2, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOERRO$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CODIGOERRO$2);
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
            target = (org.apache.xmlbeans.XmlInteger)get_store().find_element_user(CODIGOERRO$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInteger)get_store().add_element_user(CODIGOERRO$2);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DESCRICAOERRO$4, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOERRO$4, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOERRO$4, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DESCRICAOERRO$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DESCRICAOERRO$4);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOERRO$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DESCRICAOERRO$4);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOERRO$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DESCRICAOERRO$4);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "documentos" element
     */
    public v1.pessoadetalhe.ebo.abril.types.DocumentosErro getDocumentos()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.DocumentosErro target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.DocumentosErro)get_store().find_element_user(DOCUMENTOS$6, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "documentos" element
     */
    public boolean isSetDocumentos()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(DOCUMENTOS$6) != 0;
        }
    }
    
    /**
     * Sets the "documentos" element
     */
    public void setDocumentos(v1.pessoadetalhe.ebo.abril.types.DocumentosErro documentos)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.DocumentosErro target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.DocumentosErro)get_store().find_element_user(DOCUMENTOS$6, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.DocumentosErro)get_store().add_element_user(DOCUMENTOS$6);
            }
            target.set(documentos);
        }
    }
    
    /**
     * Appends and returns a new empty "documentos" element
     */
    public v1.pessoadetalhe.ebo.abril.types.DocumentosErro addNewDocumentos()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.DocumentosErro target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.DocumentosErro)get_store().add_element_user(DOCUMENTOS$6);
            return target;
        }
    }
    
    /**
     * Unsets the "documentos" element
     */
    public void unsetDocumentos()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(DOCUMENTOS$6, 0);
        }
    }
    
    /**
     * Gets the "enderecos" element
     */
    public v1.pessoadetalhe.ebo.abril.types.EnderecosErro getEnderecos()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.EnderecosErro target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.EnderecosErro)get_store().find_element_user(ENDERECOS$8, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "enderecos" element
     */
    public boolean isSetEnderecos()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(ENDERECOS$8) != 0;
        }
    }
    
    /**
     * Sets the "enderecos" element
     */
    public void setEnderecos(v1.pessoadetalhe.ebo.abril.types.EnderecosErro enderecos)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.EnderecosErro target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.EnderecosErro)get_store().find_element_user(ENDERECOS$8, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.EnderecosErro)get_store().add_element_user(ENDERECOS$8);
            }
            target.set(enderecos);
        }
    }
    
    /**
     * Appends and returns a new empty "enderecos" element
     */
    public v1.pessoadetalhe.ebo.abril.types.EnderecosErro addNewEnderecos()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.EnderecosErro target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.EnderecosErro)get_store().add_element_user(ENDERECOS$8);
            return target;
        }
    }
    
    /**
     * Unsets the "enderecos" element
     */
    public void unsetEnderecos()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(ENDERECOS$8, 0);
        }
    }
}
