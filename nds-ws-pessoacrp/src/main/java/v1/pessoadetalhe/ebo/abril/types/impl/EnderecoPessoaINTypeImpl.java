/*
 * XML Type:  EnderecoPessoaINType
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * An XML EnderecoPessoaINType(@http://abril.ebo.pessoadetalhe.v1/types/).
 *
 * This is a complex type.
 */
public class EnderecoPessoaINTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType
{
    
    public EnderecoPessoaINTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CODIGOLOCALIDADE$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codigoLocalidade");
    private static final javax.xml.namespace.QName TIPOLOGRADOURO$2 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "tipoLogradouro");
    private static final javax.xml.namespace.QName ENDERECO$4 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "endereco");
    private static final javax.xml.namespace.QName NUMERO$6 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "numero");
    private static final javax.xml.namespace.QName TIPOENDERECO$8 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "tipoEndereco");
    private static final javax.xml.namespace.QName COMPLEMENTO$10 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "complemento");
    private static final javax.xml.namespace.QName BAIRRO$12 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "bairro");
    private static final javax.xml.namespace.QName CEP$14 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "cep");
    private static final javax.xml.namespace.QName ENDERECOCOMPLETO$16 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "enderecoCompleto");
    
    
    /**
     * Gets the "codigoLocalidade" element
     */
    public java.lang.String getCodigoLocalidade()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOLOCALIDADE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "codigoLocalidade" element
     */
    public org.apache.xmlbeans.XmlString xgetCodigoLocalidade()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOLOCALIDADE$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "codigoLocalidade" element
     */
    public void setCodigoLocalidade(java.lang.String codigoLocalidade)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOLOCALIDADE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CODIGOLOCALIDADE$0);
            }
            target.setStringValue(codigoLocalidade);
        }
    }
    
    /**
     * Sets (as xml) the "codigoLocalidade" element
     */
    public void xsetCodigoLocalidade(org.apache.xmlbeans.XmlString codigoLocalidade)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOLOCALIDADE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CODIGOLOCALIDADE$0);
            }
            target.set(codigoLocalidade);
        }
    }
    
    /**
     * Gets the "tipoLogradouro" element
     */
    public java.lang.String getTipoLogradouro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TIPOLOGRADOURO$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "tipoLogradouro" element
     */
    public org.apache.xmlbeans.XmlString xgetTipoLogradouro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TIPOLOGRADOURO$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "tipoLogradouro" element
     */
    public void setTipoLogradouro(java.lang.String tipoLogradouro)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TIPOLOGRADOURO$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(TIPOLOGRADOURO$2);
            }
            target.setStringValue(tipoLogradouro);
        }
    }
    
    /**
     * Sets (as xml) the "tipoLogradouro" element
     */
    public void xsetTipoLogradouro(org.apache.xmlbeans.XmlString tipoLogradouro)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TIPOLOGRADOURO$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(TIPOLOGRADOURO$2);
            }
            target.set(tipoLogradouro);
        }
    }
    
    /**
     * Gets the "endereco" element
     */
    public java.lang.String getEndereco()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ENDERECO$4, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "endereco" element
     */
    public org.apache.xmlbeans.XmlString xgetEndereco()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ENDERECO$4, 0);
            return target;
        }
    }
    
    /**
     * Sets the "endereco" element
     */
    public void setEndereco(java.lang.String endereco)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ENDERECO$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ENDERECO$4);
            }
            target.setStringValue(endereco);
        }
    }
    
    /**
     * Sets (as xml) the "endereco" element
     */
    public void xsetEndereco(org.apache.xmlbeans.XmlString endereco)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ENDERECO$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(ENDERECO$4);
            }
            target.set(endereco);
        }
    }
    
    /**
     * Gets the "numero" element
     */
    public java.lang.String getNumero()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NUMERO$6, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "numero" element
     */
    public org.apache.xmlbeans.XmlString xgetNumero()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NUMERO$6, 0);
            return target;
        }
    }
    
    /**
     * Sets the "numero" element
     */
    public void setNumero(java.lang.String numero)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NUMERO$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NUMERO$6);
            }
            target.setStringValue(numero);
        }
    }
    
    /**
     * Sets (as xml) the "numero" element
     */
    public void xsetNumero(org.apache.xmlbeans.XmlString numero)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NUMERO$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NUMERO$6);
            }
            target.set(numero);
        }
    }
    
    /**
     * Gets the "tipoEndereco" element
     */
    public java.lang.String getTipoEndereco()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TIPOENDERECO$8, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "tipoEndereco" element
     */
    public org.apache.xmlbeans.XmlString xgetTipoEndereco()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TIPOENDERECO$8, 0);
            return target;
        }
    }
    
    /**
     * Sets the "tipoEndereco" element
     */
    public void setTipoEndereco(java.lang.String tipoEndereco)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TIPOENDERECO$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(TIPOENDERECO$8);
            }
            target.setStringValue(tipoEndereco);
        }
    }
    
    /**
     * Sets (as xml) the "tipoEndereco" element
     */
    public void xsetTipoEndereco(org.apache.xmlbeans.XmlString tipoEndereco)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TIPOENDERECO$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(TIPOENDERECO$8);
            }
            target.set(tipoEndereco);
        }
    }
    
    /**
     * Gets the "complemento" element
     */
    public java.lang.String getComplemento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(COMPLEMENTO$10, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "complemento" element
     */
    public org.apache.xmlbeans.XmlString xgetComplemento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(COMPLEMENTO$10, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "complemento" element
     */
    public boolean isNilComplemento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(COMPLEMENTO$10, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "complemento" element
     */
    public void setComplemento(java.lang.String complemento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(COMPLEMENTO$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(COMPLEMENTO$10);
            }
            target.setStringValue(complemento);
        }
    }
    
    /**
     * Sets (as xml) the "complemento" element
     */
    public void xsetComplemento(org.apache.xmlbeans.XmlString complemento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(COMPLEMENTO$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(COMPLEMENTO$10);
            }
            target.set(complemento);
        }
    }
    
    /**
     * Nils the "complemento" element
     */
    public void setNilComplemento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(COMPLEMENTO$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(COMPLEMENTO$10);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "bairro" element
     */
    public java.lang.String getBairro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(BAIRRO$12, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "bairro" element
     */
    public org.apache.xmlbeans.XmlString xgetBairro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(BAIRRO$12, 0);
            return target;
        }
    }
    
    /**
     * Sets the "bairro" element
     */
    public void setBairro(java.lang.String bairro)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(BAIRRO$12, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(BAIRRO$12);
            }
            target.setStringValue(bairro);
        }
    }
    
    /**
     * Sets (as xml) the "bairro" element
     */
    public void xsetBairro(org.apache.xmlbeans.XmlString bairro)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(BAIRRO$12, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(BAIRRO$12);
            }
            target.set(bairro);
        }
    }
    
    /**
     * Gets the "cep" element
     */
    public java.lang.String getCep()
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
     * Gets (as xml) the "cep" element
     */
    public org.apache.xmlbeans.XmlString xgetCep()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CEP$14, 0);
            return target;
        }
    }
    
    /**
     * Sets the "cep" element
     */
    public void setCep(java.lang.String cep)
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
     * Sets (as xml) the "cep" element
     */
    public void xsetCep(org.apache.xmlbeans.XmlString cep)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CEP$14, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CEP$14);
            }
            target.set(cep);
        }
    }
    
    /**
     * Gets the "enderecoCompleto" element
     */
    public java.lang.String getEnderecoCompleto()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ENDERECOCOMPLETO$16, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "enderecoCompleto" element
     */
    public org.apache.xmlbeans.XmlString xgetEnderecoCompleto()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ENDERECOCOMPLETO$16, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "enderecoCompleto" element
     */
    public boolean isNilEnderecoCompleto()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ENDERECOCOMPLETO$16, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "enderecoCompleto" element
     */
    public void setEnderecoCompleto(java.lang.String enderecoCompleto)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ENDERECOCOMPLETO$16, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ENDERECOCOMPLETO$16);
            }
            target.setStringValue(enderecoCompleto);
        }
    }
    
    /**
     * Sets (as xml) the "enderecoCompleto" element
     */
    public void xsetEnderecoCompleto(org.apache.xmlbeans.XmlString enderecoCompleto)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ENDERECOCOMPLETO$16, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(ENDERECOCOMPLETO$16);
            }
            target.set(enderecoCompleto);
        }
    }
    
    /**
     * Nils the "enderecoCompleto" element
     */
    public void setNilEnderecoCompleto()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ENDERECOCOMPLETO$16, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(ENDERECOCOMPLETO$16);
            }
            target.setNil();
        }
    }
}
