/*
 * XML Type:  EnderecoPessoaType
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * An XML EnderecoPessoaType(@http://abril.ebo.pessoadetalhe.v1/types/).
 *
 * This is a complex type.
 */
public class EnderecoPessoaTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType
{
    
    public EnderecoPessoaTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CODIGOPESSOADETALHE$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codigoPessoaDetalhe");
    private static final javax.xml.namespace.QName ENDERECO$2 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "endereco");
    private static final javax.xml.namespace.QName COMPLEMENTO$4 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "complemento");
    private static final javax.xml.namespace.QName NUMERO$6 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "numero");
    private static final javax.xml.namespace.QName TIPOENDERECO$8 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "tipoEndereco");
    private static final javax.xml.namespace.QName NOMETIPOENDERECO$10 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "nomeTipoEndereco");
    private static final javax.xml.namespace.QName CODIGOLOCALIDADE$12 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codigoLocalidade");
    private static final javax.xml.namespace.QName NOMELOCALIDADE$14 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "nomeLocalidade");
    private static final javax.xml.namespace.QName CODIGOPAIS$16 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codigoPais");
    private static final javax.xml.namespace.QName NOMEPAIS$18 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "nomePais");
    private static final javax.xml.namespace.QName TIPOLOGRADOURO$20 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "tipoLogradouro");
    private static final javax.xml.namespace.QName NOMETIPOLOGRADOURO$22 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "nomeTipoLogradouro");
    private static final javax.xml.namespace.QName CEP$24 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "cep");
    private static final javax.xml.namespace.QName SIGLAUF$26 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "siglaUf");
    private static final javax.xml.namespace.QName NOMEUF$28 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "nomeUf");
    private static final javax.xml.namespace.QName CODIGOUF$30 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codigoUf");
    private static final javax.xml.namespace.QName CODIGOIBGE$32 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codigoIbge");
    private static final javax.xml.namespace.QName BAIRRO$34 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "bairro");
    private static final javax.xml.namespace.QName DATAINCLUSAO$36 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "dataInclusao");
    private static final javax.xml.namespace.QName DATAALTERACAO$38 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "dataAlteracao");
    private static final javax.xml.namespace.QName USUARIOINCLUSAO$40 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "usuarioInclusao");
    private static final javax.xml.namespace.QName USUARIOALTERACAO$42 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "usuarioAlteracao");
    private static final javax.xml.namespace.QName DATAEXCLUSAO$44 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "dataExclusao");
    private static final javax.xml.namespace.QName USUARIOEXCLUSAO$46 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "usuarioExclusao");
    private static final javax.xml.namespace.QName CODIGOERRO$48 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codigoErro");
    private static final javax.xml.namespace.QName DESCRICAOERRO$50 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "descricaoErro");
    
    
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
     * Tests for nil "codigoPessoaDetalhe" element
     */
    public boolean isNilCodigoPessoaDetalhe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGOPESSOADETALHE$0, 0);
            if (target == null) return false;
            return target.isNil();
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
     * Nils the "codigoPessoaDetalhe" element
     */
    public void setNilCodigoPessoaDetalhe()
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
            target.setNil();
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ENDERECO$2, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ENDERECO$2, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "endereco" element
     */
    public boolean isNilEndereco()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ENDERECO$2, 0);
            if (target == null) return false;
            return target.isNil();
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ENDERECO$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ENDERECO$2);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ENDERECO$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(ENDERECO$2);
            }
            target.set(endereco);
        }
    }
    
    /**
     * Nils the "endereco" element
     */
    public void setNilEndereco()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ENDERECO$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(ENDERECO$2);
            }
            target.setNil();
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(COMPLEMENTO$4, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(COMPLEMENTO$4, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(COMPLEMENTO$4, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(COMPLEMENTO$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(COMPLEMENTO$4);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(COMPLEMENTO$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(COMPLEMENTO$4);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(COMPLEMENTO$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(COMPLEMENTO$4);
            }
            target.setNil();
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
     * Tests for nil "numero" element
     */
    public boolean isNilNumero()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NUMERO$6, 0);
            if (target == null) return false;
            return target.isNil();
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
     * Nils the "numero" element
     */
    public void setNilNumero()
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
            target.setNil();
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
     * Tests for nil "tipoEndereco" element
     */
    public boolean isNilTipoEndereco()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TIPOENDERECO$8, 0);
            if (target == null) return false;
            return target.isNil();
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
     * Nils the "tipoEndereco" element
     */
    public void setNilTipoEndereco()
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
            target.setNil();
        }
    }
    
    /**
     * Gets the "nomeTipoEndereco" element
     */
    public java.lang.String getNomeTipoEndereco()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NOMETIPOENDERECO$10, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "nomeTipoEndereco" element
     */
    public org.apache.xmlbeans.XmlString xgetNomeTipoEndereco()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMETIPOENDERECO$10, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "nomeTipoEndereco" element
     */
    public boolean isNilNomeTipoEndereco()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMETIPOENDERECO$10, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "nomeTipoEndereco" element
     */
    public void setNomeTipoEndereco(java.lang.String nomeTipoEndereco)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NOMETIPOENDERECO$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NOMETIPOENDERECO$10);
            }
            target.setStringValue(nomeTipoEndereco);
        }
    }
    
    /**
     * Sets (as xml) the "nomeTipoEndereco" element
     */
    public void xsetNomeTipoEndereco(org.apache.xmlbeans.XmlString nomeTipoEndereco)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMETIPOENDERECO$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NOMETIPOENDERECO$10);
            }
            target.set(nomeTipoEndereco);
        }
    }
    
    /**
     * Nils the "nomeTipoEndereco" element
     */
    public void setNilNomeTipoEndereco()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMETIPOENDERECO$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NOMETIPOENDERECO$10);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "codigoLocalidade" element
     */
    public java.lang.String getCodigoLocalidade()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOLOCALIDADE$12, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOLOCALIDADE$12, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "codigoLocalidade" element
     */
    public boolean isNilCodigoLocalidade()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOLOCALIDADE$12, 0);
            if (target == null) return false;
            return target.isNil();
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOLOCALIDADE$12, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CODIGOLOCALIDADE$12);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOLOCALIDADE$12, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CODIGOLOCALIDADE$12);
            }
            target.set(codigoLocalidade);
        }
    }
    
    /**
     * Nils the "codigoLocalidade" element
     */
    public void setNilCodigoLocalidade()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOLOCALIDADE$12, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CODIGOLOCALIDADE$12);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "nomeLocalidade" element
     */
    public java.lang.String getNomeLocalidade()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NOMELOCALIDADE$14, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "nomeLocalidade" element
     */
    public org.apache.xmlbeans.XmlString xgetNomeLocalidade()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMELOCALIDADE$14, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "nomeLocalidade" element
     */
    public boolean isNilNomeLocalidade()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMELOCALIDADE$14, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "nomeLocalidade" element
     */
    public void setNomeLocalidade(java.lang.String nomeLocalidade)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NOMELOCALIDADE$14, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NOMELOCALIDADE$14);
            }
            target.setStringValue(nomeLocalidade);
        }
    }
    
    /**
     * Sets (as xml) the "nomeLocalidade" element
     */
    public void xsetNomeLocalidade(org.apache.xmlbeans.XmlString nomeLocalidade)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMELOCALIDADE$14, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NOMELOCALIDADE$14);
            }
            target.set(nomeLocalidade);
        }
    }
    
    /**
     * Nils the "nomeLocalidade" element
     */
    public void setNilNomeLocalidade()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMELOCALIDADE$14, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NOMELOCALIDADE$14);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "codigoPais" element
     */
    public java.lang.String getCodigoPais()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOPAIS$16, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "codigoPais" element
     */
    public org.apache.xmlbeans.XmlString xgetCodigoPais()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOPAIS$16, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "codigoPais" element
     */
    public boolean isNilCodigoPais()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOPAIS$16, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "codigoPais" element
     */
    public void setCodigoPais(java.lang.String codigoPais)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOPAIS$16, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CODIGOPAIS$16);
            }
            target.setStringValue(codigoPais);
        }
    }
    
    /**
     * Sets (as xml) the "codigoPais" element
     */
    public void xsetCodigoPais(org.apache.xmlbeans.XmlString codigoPais)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOPAIS$16, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CODIGOPAIS$16);
            }
            target.set(codigoPais);
        }
    }
    
    /**
     * Nils the "codigoPais" element
     */
    public void setNilCodigoPais()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOPAIS$16, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CODIGOPAIS$16);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "nomePais" element
     */
    public java.lang.String getNomePais()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NOMEPAIS$18, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "nomePais" element
     */
    public org.apache.xmlbeans.XmlString xgetNomePais()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEPAIS$18, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "nomePais" element
     */
    public boolean isNilNomePais()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEPAIS$18, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "nomePais" element
     */
    public void setNomePais(java.lang.String nomePais)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NOMEPAIS$18, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NOMEPAIS$18);
            }
            target.setStringValue(nomePais);
        }
    }
    
    /**
     * Sets (as xml) the "nomePais" element
     */
    public void xsetNomePais(org.apache.xmlbeans.XmlString nomePais)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEPAIS$18, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NOMEPAIS$18);
            }
            target.set(nomePais);
        }
    }
    
    /**
     * Nils the "nomePais" element
     */
    public void setNilNomePais()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEPAIS$18, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NOMEPAIS$18);
            }
            target.setNil();
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TIPOLOGRADOURO$20, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TIPOLOGRADOURO$20, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "tipoLogradouro" element
     */
    public boolean isNilTipoLogradouro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TIPOLOGRADOURO$20, 0);
            if (target == null) return false;
            return target.isNil();
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TIPOLOGRADOURO$20, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(TIPOLOGRADOURO$20);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TIPOLOGRADOURO$20, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(TIPOLOGRADOURO$20);
            }
            target.set(tipoLogradouro);
        }
    }
    
    /**
     * Nils the "tipoLogradouro" element
     */
    public void setNilTipoLogradouro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TIPOLOGRADOURO$20, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(TIPOLOGRADOURO$20);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "nomeTipoLogradouro" element
     */
    public java.lang.String getNomeTipoLogradouro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NOMETIPOLOGRADOURO$22, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "nomeTipoLogradouro" element
     */
    public org.apache.xmlbeans.XmlString xgetNomeTipoLogradouro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMETIPOLOGRADOURO$22, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "nomeTipoLogradouro" element
     */
    public boolean isNilNomeTipoLogradouro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMETIPOLOGRADOURO$22, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "nomeTipoLogradouro" element
     */
    public void setNomeTipoLogradouro(java.lang.String nomeTipoLogradouro)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NOMETIPOLOGRADOURO$22, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NOMETIPOLOGRADOURO$22);
            }
            target.setStringValue(nomeTipoLogradouro);
        }
    }
    
    /**
     * Sets (as xml) the "nomeTipoLogradouro" element
     */
    public void xsetNomeTipoLogradouro(org.apache.xmlbeans.XmlString nomeTipoLogradouro)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMETIPOLOGRADOURO$22, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NOMETIPOLOGRADOURO$22);
            }
            target.set(nomeTipoLogradouro);
        }
    }
    
    /**
     * Nils the "nomeTipoLogradouro" element
     */
    public void setNilNomeTipoLogradouro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMETIPOLOGRADOURO$22, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NOMETIPOLOGRADOURO$22);
            }
            target.setNil();
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CEP$24, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CEP$24, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "cep" element
     */
    public boolean isNilCep()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CEP$24, 0);
            if (target == null) return false;
            return target.isNil();
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CEP$24, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CEP$24);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CEP$24, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CEP$24);
            }
            target.set(cep);
        }
    }
    
    /**
     * Nils the "cep" element
     */
    public void setNilCep()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CEP$24, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CEP$24);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "siglaUf" element
     */
    public java.lang.String getSiglaUf()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SIGLAUF$26, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "siglaUf" element
     */
    public org.apache.xmlbeans.XmlString xgetSiglaUf()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SIGLAUF$26, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "siglaUf" element
     */
    public boolean isNilSiglaUf()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SIGLAUF$26, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "siglaUf" element
     */
    public void setSiglaUf(java.lang.String siglaUf)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SIGLAUF$26, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SIGLAUF$26);
            }
            target.setStringValue(siglaUf);
        }
    }
    
    /**
     * Sets (as xml) the "siglaUf" element
     */
    public void xsetSiglaUf(org.apache.xmlbeans.XmlString siglaUf)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SIGLAUF$26, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SIGLAUF$26);
            }
            target.set(siglaUf);
        }
    }
    
    /**
     * Nils the "siglaUf" element
     */
    public void setNilSiglaUf()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SIGLAUF$26, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SIGLAUF$26);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "nomeUf" element
     */
    public java.lang.String getNomeUf()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NOMEUF$28, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "nomeUf" element
     */
    public org.apache.xmlbeans.XmlString xgetNomeUf()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEUF$28, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "nomeUf" element
     */
    public boolean isNilNomeUf()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEUF$28, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "nomeUf" element
     */
    public void setNomeUf(java.lang.String nomeUf)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NOMEUF$28, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NOMEUF$28);
            }
            target.setStringValue(nomeUf);
        }
    }
    
    /**
     * Sets (as xml) the "nomeUf" element
     */
    public void xsetNomeUf(org.apache.xmlbeans.XmlString nomeUf)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEUF$28, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NOMEUF$28);
            }
            target.set(nomeUf);
        }
    }
    
    /**
     * Nils the "nomeUf" element
     */
    public void setNilNomeUf()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEUF$28, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NOMEUF$28);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "codigoUf" element
     */
    public long getCodigoUf()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOUF$30, 0);
            if (target == null)
            {
                return 0L;
            }
            return target.getLongValue();
        }
    }
    
    /**
     * Gets (as xml) the "codigoUf" element
     */
    public org.apache.xmlbeans.XmlLong xgetCodigoUf()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGOUF$30, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "codigoUf" element
     */
    public boolean isNilCodigoUf()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGOUF$30, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "codigoUf" element
     */
    public void setCodigoUf(long codigoUf)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOUF$30, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CODIGOUF$30);
            }
            target.setLongValue(codigoUf);
        }
    }
    
    /**
     * Sets (as xml) the "codigoUf" element
     */
    public void xsetCodigoUf(org.apache.xmlbeans.XmlLong codigoUf)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGOUF$30, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_element_user(CODIGOUF$30);
            }
            target.set(codigoUf);
        }
    }
    
    /**
     * Nils the "codigoUf" element
     */
    public void setNilCodigoUf()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGOUF$30, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_element_user(CODIGOUF$30);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "codigoIbge" element
     */
    public long getCodigoIbge()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOIBGE$32, 0);
            if (target == null)
            {
                return 0L;
            }
            return target.getLongValue();
        }
    }
    
    /**
     * Gets (as xml) the "codigoIbge" element
     */
    public org.apache.xmlbeans.XmlLong xgetCodigoIbge()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGOIBGE$32, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "codigoIbge" element
     */
    public boolean isNilCodigoIbge()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGOIBGE$32, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "codigoIbge" element
     */
    public void setCodigoIbge(long codigoIbge)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOIBGE$32, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CODIGOIBGE$32);
            }
            target.setLongValue(codigoIbge);
        }
    }
    
    /**
     * Sets (as xml) the "codigoIbge" element
     */
    public void xsetCodigoIbge(org.apache.xmlbeans.XmlLong codigoIbge)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGOIBGE$32, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_element_user(CODIGOIBGE$32);
            }
            target.set(codigoIbge);
        }
    }
    
    /**
     * Nils the "codigoIbge" element
     */
    public void setNilCodigoIbge()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGOIBGE$32, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_element_user(CODIGOIBGE$32);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(BAIRRO$34, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(BAIRRO$34, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "bairro" element
     */
    public boolean isNilBairro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(BAIRRO$34, 0);
            if (target == null) return false;
            return target.isNil();
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(BAIRRO$34, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(BAIRRO$34);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(BAIRRO$34, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(BAIRRO$34);
            }
            target.set(bairro);
        }
    }
    
    /**
     * Nils the "bairro" element
     */
    public void setNilBairro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(BAIRRO$34, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(BAIRRO$34);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "dataInclusao" element
     */
    public java.lang.String getDataInclusao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAINCLUSAO$36, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "dataInclusao" element
     */
    public org.apache.xmlbeans.XmlString xgetDataInclusao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAINCLUSAO$36, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "dataInclusao" element
     */
    public boolean isNilDataInclusao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAINCLUSAO$36, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "dataInclusao" element
     */
    public void setDataInclusao(java.lang.String dataInclusao)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAINCLUSAO$36, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DATAINCLUSAO$36);
            }
            target.setStringValue(dataInclusao);
        }
    }
    
    /**
     * Sets (as xml) the "dataInclusao" element
     */
    public void xsetDataInclusao(org.apache.xmlbeans.XmlString dataInclusao)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAINCLUSAO$36, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAINCLUSAO$36);
            }
            target.set(dataInclusao);
        }
    }
    
    /**
     * Nils the "dataInclusao" element
     */
    public void setNilDataInclusao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAINCLUSAO$36, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAINCLUSAO$36);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "dataAlteracao" element
     */
    public java.lang.String getDataAlteracao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAALTERACAO$38, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "dataAlteracao" element
     */
    public org.apache.xmlbeans.XmlString xgetDataAlteracao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAALTERACAO$38, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "dataAlteracao" element
     */
    public boolean isNilDataAlteracao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAALTERACAO$38, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "dataAlteracao" element
     */
    public void setDataAlteracao(java.lang.String dataAlteracao)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAALTERACAO$38, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DATAALTERACAO$38);
            }
            target.setStringValue(dataAlteracao);
        }
    }
    
    /**
     * Sets (as xml) the "dataAlteracao" element
     */
    public void xsetDataAlteracao(org.apache.xmlbeans.XmlString dataAlteracao)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAALTERACAO$38, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAALTERACAO$38);
            }
            target.set(dataAlteracao);
        }
    }
    
    /**
     * Nils the "dataAlteracao" element
     */
    public void setNilDataAlteracao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAALTERACAO$38, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAALTERACAO$38);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "usuarioInclusao" element
     */
    public java.lang.String getUsuarioInclusao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USUARIOINCLUSAO$40, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "usuarioInclusao" element
     */
    public org.apache.xmlbeans.XmlString xgetUsuarioInclusao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOINCLUSAO$40, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "usuarioInclusao" element
     */
    public boolean isNilUsuarioInclusao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOINCLUSAO$40, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "usuarioInclusao" element
     */
    public void setUsuarioInclusao(java.lang.String usuarioInclusao)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USUARIOINCLUSAO$40, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(USUARIOINCLUSAO$40);
            }
            target.setStringValue(usuarioInclusao);
        }
    }
    
    /**
     * Sets (as xml) the "usuarioInclusao" element
     */
    public void xsetUsuarioInclusao(org.apache.xmlbeans.XmlString usuarioInclusao)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOINCLUSAO$40, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USUARIOINCLUSAO$40);
            }
            target.set(usuarioInclusao);
        }
    }
    
    /**
     * Nils the "usuarioInclusao" element
     */
    public void setNilUsuarioInclusao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOINCLUSAO$40, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USUARIOINCLUSAO$40);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "usuarioAlteracao" element
     */
    public java.lang.String getUsuarioAlteracao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USUARIOALTERACAO$42, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "usuarioAlteracao" element
     */
    public org.apache.xmlbeans.XmlString xgetUsuarioAlteracao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOALTERACAO$42, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "usuarioAlteracao" element
     */
    public boolean isNilUsuarioAlteracao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOALTERACAO$42, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "usuarioAlteracao" element
     */
    public void setUsuarioAlteracao(java.lang.String usuarioAlteracao)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USUARIOALTERACAO$42, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(USUARIOALTERACAO$42);
            }
            target.setStringValue(usuarioAlteracao);
        }
    }
    
    /**
     * Sets (as xml) the "usuarioAlteracao" element
     */
    public void xsetUsuarioAlteracao(org.apache.xmlbeans.XmlString usuarioAlteracao)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOALTERACAO$42, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USUARIOALTERACAO$42);
            }
            target.set(usuarioAlteracao);
        }
    }
    
    /**
     * Nils the "usuarioAlteracao" element
     */
    public void setNilUsuarioAlteracao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOALTERACAO$42, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USUARIOALTERACAO$42);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "dataExclusao" element
     */
    public java.lang.String getDataExclusao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAEXCLUSAO$44, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "dataExclusao" element
     */
    public org.apache.xmlbeans.XmlString xgetDataExclusao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAEXCLUSAO$44, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "dataExclusao" element
     */
    public boolean isNilDataExclusao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAEXCLUSAO$44, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "dataExclusao" element
     */
    public void setDataExclusao(java.lang.String dataExclusao)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAEXCLUSAO$44, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DATAEXCLUSAO$44);
            }
            target.setStringValue(dataExclusao);
        }
    }
    
    /**
     * Sets (as xml) the "dataExclusao" element
     */
    public void xsetDataExclusao(org.apache.xmlbeans.XmlString dataExclusao)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAEXCLUSAO$44, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAEXCLUSAO$44);
            }
            target.set(dataExclusao);
        }
    }
    
    /**
     * Nils the "dataExclusao" element
     */
    public void setNilDataExclusao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAEXCLUSAO$44, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAEXCLUSAO$44);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "usuarioExclusao" element
     */
    public java.lang.String getUsuarioExclusao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USUARIOEXCLUSAO$46, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "usuarioExclusao" element
     */
    public org.apache.xmlbeans.XmlString xgetUsuarioExclusao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOEXCLUSAO$46, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "usuarioExclusao" element
     */
    public boolean isNilUsuarioExclusao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOEXCLUSAO$46, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "usuarioExclusao" element
     */
    public void setUsuarioExclusao(java.lang.String usuarioExclusao)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USUARIOEXCLUSAO$46, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(USUARIOEXCLUSAO$46);
            }
            target.setStringValue(usuarioExclusao);
        }
    }
    
    /**
     * Sets (as xml) the "usuarioExclusao" element
     */
    public void xsetUsuarioExclusao(org.apache.xmlbeans.XmlString usuarioExclusao)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOEXCLUSAO$46, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USUARIOEXCLUSAO$46);
            }
            target.set(usuarioExclusao);
        }
    }
    
    /**
     * Nils the "usuarioExclusao" element
     */
    public void setNilUsuarioExclusao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOEXCLUSAO$46, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USUARIOEXCLUSAO$46);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "codigoErro" element
     */
    public int getCodigoErro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOERRO$48, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "codigoErro" element
     */
    public org.apache.xmlbeans.XmlInt xgetCodigoErro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(CODIGOERRO$48, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "codigoErro" element
     */
    public boolean isNilCodigoErro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(CODIGOERRO$48, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "codigoErro" element
     */
    public void setCodigoErro(int codigoErro)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOERRO$48, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CODIGOERRO$48);
            }
            target.setIntValue(codigoErro);
        }
    }
    
    /**
     * Sets (as xml) the "codigoErro" element
     */
    public void xsetCodigoErro(org.apache.xmlbeans.XmlInt codigoErro)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(CODIGOERRO$48, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(CODIGOERRO$48);
            }
            target.set(codigoErro);
        }
    }
    
    /**
     * Nils the "codigoErro" element
     */
    public void setNilCodigoErro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(CODIGOERRO$48, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(CODIGOERRO$48);
            }
            target.setNil();
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DESCRICAOERRO$50, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOERRO$50, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOERRO$50, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DESCRICAOERRO$50, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DESCRICAOERRO$50);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOERRO$50, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DESCRICAOERRO$50);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOERRO$50, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DESCRICAOERRO$50);
            }
            target.setNil();
        }
    }
}
