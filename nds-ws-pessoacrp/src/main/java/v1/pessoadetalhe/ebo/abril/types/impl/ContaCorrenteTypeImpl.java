/*
 * XML Type:  ContaCorrenteType
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.ContaCorrenteType
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * An XML ContaCorrenteType(@http://abril.ebo.pessoadetalhe.v1/types/).
 *
 * This is a complex type.
 */
public class ContaCorrenteTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.ContaCorrenteType
{
    
    public ContaCorrenteTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CODIGO$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codigo");
    private static final javax.xml.namespace.QName CODIGOAGENCIA$2 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codigoAgencia");
    private static final javax.xml.namespace.QName DIGITOAGENCIA$4 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "digitoAgencia");
    private static final javax.xml.namespace.QName NOMEAGENCIA$6 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "nomeAgencia");
    private static final javax.xml.namespace.QName NUMEROCONTA$8 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "numeroConta");
    private static final javax.xml.namespace.QName DIGITOCONTA$10 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "digitoConta");
    private static final javax.xml.namespace.QName CODIGOBANCO$12 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codigoBanco");
    private static final javax.xml.namespace.QName NOMEBANCO$14 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "nomeBanco");
    private static final javax.xml.namespace.QName CODIGOPESSOADETALHE$16 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codigoPessoaDetalhe");
    private static final javax.xml.namespace.QName INDCONTAPRINCIPAL$18 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "indContaPrincipal");
    private static final javax.xml.namespace.QName USUARIOINCLUSAO$20 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "usuarioInclusao");
    private static final javax.xml.namespace.QName USUARIOALTERACAO$22 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "usuarioAlteracao");
    private static final javax.xml.namespace.QName DATAINCLUSAO$24 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "dataInclusao");
    private static final javax.xml.namespace.QName DATAALTERACAO$26 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "dataAlteracao");
    private static final javax.xml.namespace.QName DATAEXCLUSAO$28 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "dataExclusao");
    private static final javax.xml.namespace.QName USUARIOEXCLUSAO$30 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "usuarioExclusao");
    private static final javax.xml.namespace.QName CODIGOERRO$32 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codigoErro");
    private static final javax.xml.namespace.QName DESCRICAOERRO$34 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "descricaoErro");
    
    
    /**
     * Gets the "codigo" element
     */
    public long getCodigo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGO$0, 0);
            if (target == null)
            {
                return 0L;
            }
            return target.getLongValue();
        }
    }
    
    /**
     * Gets (as xml) the "codigo" element
     */
    public org.apache.xmlbeans.XmlLong xgetCodigo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGO$0, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "codigo" element
     */
    public boolean isNilCodigo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGO$0, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "codigo" element
     */
    public void setCodigo(long codigo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGO$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CODIGO$0);
            }
            target.setLongValue(codigo);
        }
    }
    
    /**
     * Sets (as xml) the "codigo" element
     */
    public void xsetCodigo(org.apache.xmlbeans.XmlLong codigo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGO$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_element_user(CODIGO$0);
            }
            target.set(codigo);
        }
    }
    
    /**
     * Nils the "codigo" element
     */
    public void setNilCodigo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGO$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_element_user(CODIGO$0);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "codigoAgencia" element
     */
    public java.lang.String getCodigoAgencia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOAGENCIA$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "codigoAgencia" element
     */
    public org.apache.xmlbeans.XmlString xgetCodigoAgencia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOAGENCIA$2, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "codigoAgencia" element
     */
    public boolean isNilCodigoAgencia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOAGENCIA$2, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "codigoAgencia" element
     */
    public void setCodigoAgencia(java.lang.String codigoAgencia)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOAGENCIA$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CODIGOAGENCIA$2);
            }
            target.setStringValue(codigoAgencia);
        }
    }
    
    /**
     * Sets (as xml) the "codigoAgencia" element
     */
    public void xsetCodigoAgencia(org.apache.xmlbeans.XmlString codigoAgencia)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOAGENCIA$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CODIGOAGENCIA$2);
            }
            target.set(codigoAgencia);
        }
    }
    
    /**
     * Nils the "codigoAgencia" element
     */
    public void setNilCodigoAgencia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOAGENCIA$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CODIGOAGENCIA$2);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "digitoAgencia" element
     */
    public java.lang.String getDigitoAgencia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DIGITOAGENCIA$4, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "digitoAgencia" element
     */
    public org.apache.xmlbeans.XmlString xgetDigitoAgencia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DIGITOAGENCIA$4, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "digitoAgencia" element
     */
    public boolean isNilDigitoAgencia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DIGITOAGENCIA$4, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "digitoAgencia" element
     */
    public void setDigitoAgencia(java.lang.String digitoAgencia)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DIGITOAGENCIA$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DIGITOAGENCIA$4);
            }
            target.setStringValue(digitoAgencia);
        }
    }
    
    /**
     * Sets (as xml) the "digitoAgencia" element
     */
    public void xsetDigitoAgencia(org.apache.xmlbeans.XmlString digitoAgencia)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DIGITOAGENCIA$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DIGITOAGENCIA$4);
            }
            target.set(digitoAgencia);
        }
    }
    
    /**
     * Nils the "digitoAgencia" element
     */
    public void setNilDigitoAgencia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DIGITOAGENCIA$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DIGITOAGENCIA$4);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "nomeAgencia" element
     */
    public java.lang.String getNomeAgencia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NOMEAGENCIA$6, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "nomeAgencia" element
     */
    public org.apache.xmlbeans.XmlString xgetNomeAgencia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEAGENCIA$6, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "nomeAgencia" element
     */
    public boolean isNilNomeAgencia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEAGENCIA$6, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "nomeAgencia" element
     */
    public void setNomeAgencia(java.lang.String nomeAgencia)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NOMEAGENCIA$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NOMEAGENCIA$6);
            }
            target.setStringValue(nomeAgencia);
        }
    }
    
    /**
     * Sets (as xml) the "nomeAgencia" element
     */
    public void xsetNomeAgencia(org.apache.xmlbeans.XmlString nomeAgencia)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEAGENCIA$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NOMEAGENCIA$6);
            }
            target.set(nomeAgencia);
        }
    }
    
    /**
     * Nils the "nomeAgencia" element
     */
    public void setNilNomeAgencia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEAGENCIA$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NOMEAGENCIA$6);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "numeroConta" element
     */
    public java.lang.String getNumeroConta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NUMEROCONTA$8, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "numeroConta" element
     */
    public org.apache.xmlbeans.XmlString xgetNumeroConta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NUMEROCONTA$8, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "numeroConta" element
     */
    public boolean isNilNumeroConta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NUMEROCONTA$8, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "numeroConta" element
     */
    public void setNumeroConta(java.lang.String numeroConta)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NUMEROCONTA$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NUMEROCONTA$8);
            }
            target.setStringValue(numeroConta);
        }
    }
    
    /**
     * Sets (as xml) the "numeroConta" element
     */
    public void xsetNumeroConta(org.apache.xmlbeans.XmlString numeroConta)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NUMEROCONTA$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NUMEROCONTA$8);
            }
            target.set(numeroConta);
        }
    }
    
    /**
     * Nils the "numeroConta" element
     */
    public void setNilNumeroConta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NUMEROCONTA$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NUMEROCONTA$8);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "digitoConta" element
     */
    public java.lang.String getDigitoConta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DIGITOCONTA$10, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "digitoConta" element
     */
    public org.apache.xmlbeans.XmlString xgetDigitoConta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DIGITOCONTA$10, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "digitoConta" element
     */
    public boolean isNilDigitoConta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DIGITOCONTA$10, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "digitoConta" element
     */
    public void setDigitoConta(java.lang.String digitoConta)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DIGITOCONTA$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DIGITOCONTA$10);
            }
            target.setStringValue(digitoConta);
        }
    }
    
    /**
     * Sets (as xml) the "digitoConta" element
     */
    public void xsetDigitoConta(org.apache.xmlbeans.XmlString digitoConta)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DIGITOCONTA$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DIGITOCONTA$10);
            }
            target.set(digitoConta);
        }
    }
    
    /**
     * Nils the "digitoConta" element
     */
    public void setNilDigitoConta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DIGITOCONTA$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DIGITOCONTA$10);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "codigoBanco" element
     */
    public java.lang.String getCodigoBanco()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOBANCO$12, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "codigoBanco" element
     */
    public org.apache.xmlbeans.XmlString xgetCodigoBanco()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOBANCO$12, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "codigoBanco" element
     */
    public boolean isNilCodigoBanco()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOBANCO$12, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "codigoBanco" element
     */
    public void setCodigoBanco(java.lang.String codigoBanco)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOBANCO$12, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CODIGOBANCO$12);
            }
            target.setStringValue(codigoBanco);
        }
    }
    
    /**
     * Sets (as xml) the "codigoBanco" element
     */
    public void xsetCodigoBanco(org.apache.xmlbeans.XmlString codigoBanco)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOBANCO$12, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CODIGOBANCO$12);
            }
            target.set(codigoBanco);
        }
    }
    
    /**
     * Nils the "codigoBanco" element
     */
    public void setNilCodigoBanco()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOBANCO$12, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CODIGOBANCO$12);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "nomeBanco" element
     */
    public java.lang.String getNomeBanco()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NOMEBANCO$14, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "nomeBanco" element
     */
    public org.apache.xmlbeans.XmlString xgetNomeBanco()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEBANCO$14, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "nomeBanco" element
     */
    public boolean isNilNomeBanco()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEBANCO$14, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "nomeBanco" element
     */
    public void setNomeBanco(java.lang.String nomeBanco)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NOMEBANCO$14, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NOMEBANCO$14);
            }
            target.setStringValue(nomeBanco);
        }
    }
    
    /**
     * Sets (as xml) the "nomeBanco" element
     */
    public void xsetNomeBanco(org.apache.xmlbeans.XmlString nomeBanco)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEBANCO$14, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NOMEBANCO$14);
            }
            target.set(nomeBanco);
        }
    }
    
    /**
     * Nils the "nomeBanco" element
     */
    public void setNilNomeBanco()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEBANCO$14, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NOMEBANCO$14);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "codigoPessoaDetalhe" element
     */
    public long getCodigoPessoaDetalhe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOPESSOADETALHE$16, 0);
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
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGOPESSOADETALHE$16, 0);
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
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGOPESSOADETALHE$16, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOPESSOADETALHE$16, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CODIGOPESSOADETALHE$16);
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
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGOPESSOADETALHE$16, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_element_user(CODIGOPESSOADETALHE$16);
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
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGOPESSOADETALHE$16, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_element_user(CODIGOPESSOADETALHE$16);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "indContaPrincipal" element
     */
    public int getIndContaPrincipal()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(INDCONTAPRINCIPAL$18, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "indContaPrincipal" element
     */
    public org.apache.xmlbeans.XmlInt xgetIndContaPrincipal()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(INDCONTAPRINCIPAL$18, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "indContaPrincipal" element
     */
    public boolean isNilIndContaPrincipal()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(INDCONTAPRINCIPAL$18, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "indContaPrincipal" element
     */
    public void setIndContaPrincipal(int indContaPrincipal)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(INDCONTAPRINCIPAL$18, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(INDCONTAPRINCIPAL$18);
            }
            target.setIntValue(indContaPrincipal);
        }
    }
    
    /**
     * Sets (as xml) the "indContaPrincipal" element
     */
    public void xsetIndContaPrincipal(org.apache.xmlbeans.XmlInt indContaPrincipal)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(INDCONTAPRINCIPAL$18, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(INDCONTAPRINCIPAL$18);
            }
            target.set(indContaPrincipal);
        }
    }
    
    /**
     * Nils the "indContaPrincipal" element
     */
    public void setNilIndContaPrincipal()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(INDCONTAPRINCIPAL$18, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(INDCONTAPRINCIPAL$18);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USUARIOINCLUSAO$20, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOINCLUSAO$20, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOINCLUSAO$20, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USUARIOINCLUSAO$20, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(USUARIOINCLUSAO$20);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOINCLUSAO$20, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USUARIOINCLUSAO$20);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOINCLUSAO$20, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USUARIOINCLUSAO$20);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USUARIOALTERACAO$22, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOALTERACAO$22, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOALTERACAO$22, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USUARIOALTERACAO$22, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(USUARIOALTERACAO$22);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOALTERACAO$22, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USUARIOALTERACAO$22);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOALTERACAO$22, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USUARIOALTERACAO$22);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAINCLUSAO$24, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAINCLUSAO$24, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAINCLUSAO$24, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAINCLUSAO$24, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DATAINCLUSAO$24);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAINCLUSAO$24, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAINCLUSAO$24);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAINCLUSAO$24, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAINCLUSAO$24);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAALTERACAO$26, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAALTERACAO$26, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAALTERACAO$26, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAALTERACAO$26, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DATAALTERACAO$26);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAALTERACAO$26, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAALTERACAO$26);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAALTERACAO$26, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAALTERACAO$26);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAEXCLUSAO$28, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAEXCLUSAO$28, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAEXCLUSAO$28, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAEXCLUSAO$28, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DATAEXCLUSAO$28);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAEXCLUSAO$28, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAEXCLUSAO$28);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAEXCLUSAO$28, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAEXCLUSAO$28);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USUARIOEXCLUSAO$30, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOEXCLUSAO$30, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOEXCLUSAO$30, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USUARIOEXCLUSAO$30, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(USUARIOEXCLUSAO$30);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOEXCLUSAO$30, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USUARIOEXCLUSAO$30);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOEXCLUSAO$30, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USUARIOEXCLUSAO$30);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOERRO$32, 0);
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
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(CODIGOERRO$32, 0);
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
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(CODIGOERRO$32, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOERRO$32, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CODIGOERRO$32);
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
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(CODIGOERRO$32, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(CODIGOERRO$32);
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
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(CODIGOERRO$32, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(CODIGOERRO$32);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DESCRICAOERRO$34, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOERRO$34, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOERRO$34, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * True if has "descricaoErro" element
     */
    public boolean isSetDescricaoErro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(DESCRICAOERRO$34) != 0;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DESCRICAOERRO$34, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DESCRICAOERRO$34);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOERRO$34, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DESCRICAOERRO$34);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOERRO$34, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DESCRICAOERRO$34);
            }
            target.setNil();
        }
    }
    
    /**
     * Unsets the "descricaoErro" element
     */
    public void unsetDescricaoErro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(DESCRICAOERRO$34, 0);
        }
    }
}
