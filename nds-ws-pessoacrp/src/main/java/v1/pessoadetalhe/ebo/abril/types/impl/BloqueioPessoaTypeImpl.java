/*
 * XML Type:  BloqueioPessoaType
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.BloqueioPessoaType
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * An XML BloqueioPessoaType(@http://abril.ebo.pessoadetalhe.v1/types/).
 *
 * This is a complex type.
 */
public class BloqueioPessoaTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.BloqueioPessoaType
{
    
    public BloqueioPessoaTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CODIGO$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codigo");
    private static final javax.xml.namespace.QName NOME$2 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "nome");
    private static final javax.xml.namespace.QName CODIGOMOTIVO$4 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codigoMotivo");
    private static final javax.xml.namespace.QName NOMEMOTIVO$6 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "nomeMotivo");
    private static final javax.xml.namespace.QName DESCRICAOMOTIVO$8 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "descricaoMotivo");
    private static final javax.xml.namespace.QName DATAINICIOVIGENCIA$10 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "dataInicioVigencia");
    private static final javax.xml.namespace.QName DATAFIMVIGENCIA$12 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "dataFimVigencia");
    private static final javax.xml.namespace.QName DATAVALIDADE$14 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "dataValidade");
    private static final javax.xml.namespace.QName CODIGOPESSOADETALHE$16 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codigoPessoaDetalhe");
    private static final javax.xml.namespace.QName USUARIOINCLUSAO$18 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "usuarioInclusao");
    private static final javax.xml.namespace.QName USUARIOALTERACAO$20 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "usuarioAlteracao");
    private static final javax.xml.namespace.QName DATAINCLUSAO$22 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "dataInclusao");
    private static final javax.xml.namespace.QName DATAALTERACAO$24 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "dataAlteracao");
    private static final javax.xml.namespace.QName CODIGOERRO$26 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codigoErro");
    private static final javax.xml.namespace.QName DESCRICAOERRO$28 = 
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
     * Tests for nil "nome" element
     */
    public boolean isNilNome()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOME$2, 0);
            if (target == null) return false;
            return target.isNil();
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
    
    /**
     * Nils the "nome" element
     */
    public void setNilNome()
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
            target.setNil();
        }
    }
    
    /**
     * Gets the "codigoMotivo" element
     */
    public long getCodigoMotivo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOMOTIVO$4, 0);
            if (target == null)
            {
                return 0L;
            }
            return target.getLongValue();
        }
    }
    
    /**
     * Gets (as xml) the "codigoMotivo" element
     */
    public org.apache.xmlbeans.XmlLong xgetCodigoMotivo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGOMOTIVO$4, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "codigoMotivo" element
     */
    public boolean isNilCodigoMotivo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGOMOTIVO$4, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "codigoMotivo" element
     */
    public void setCodigoMotivo(long codigoMotivo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOMOTIVO$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CODIGOMOTIVO$4);
            }
            target.setLongValue(codigoMotivo);
        }
    }
    
    /**
     * Sets (as xml) the "codigoMotivo" element
     */
    public void xsetCodigoMotivo(org.apache.xmlbeans.XmlLong codigoMotivo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGOMOTIVO$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_element_user(CODIGOMOTIVO$4);
            }
            target.set(codigoMotivo);
        }
    }
    
    /**
     * Nils the "codigoMotivo" element
     */
    public void setNilCodigoMotivo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGOMOTIVO$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_element_user(CODIGOMOTIVO$4);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "nomeMotivo" element
     */
    public java.lang.String getNomeMotivo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NOMEMOTIVO$6, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "nomeMotivo" element
     */
    public org.apache.xmlbeans.XmlString xgetNomeMotivo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEMOTIVO$6, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "nomeMotivo" element
     */
    public boolean isNilNomeMotivo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEMOTIVO$6, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "nomeMotivo" element
     */
    public void setNomeMotivo(java.lang.String nomeMotivo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NOMEMOTIVO$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NOMEMOTIVO$6);
            }
            target.setStringValue(nomeMotivo);
        }
    }
    
    /**
     * Sets (as xml) the "nomeMotivo" element
     */
    public void xsetNomeMotivo(org.apache.xmlbeans.XmlString nomeMotivo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEMOTIVO$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NOMEMOTIVO$6);
            }
            target.set(nomeMotivo);
        }
    }
    
    /**
     * Nils the "nomeMotivo" element
     */
    public void setNilNomeMotivo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEMOTIVO$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NOMEMOTIVO$6);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "descricaoMotivo" element
     */
    public java.lang.String getDescricaoMotivo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DESCRICAOMOTIVO$8, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "descricaoMotivo" element
     */
    public org.apache.xmlbeans.XmlString xgetDescricaoMotivo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOMOTIVO$8, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "descricaoMotivo" element
     */
    public boolean isNilDescricaoMotivo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOMOTIVO$8, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "descricaoMotivo" element
     */
    public void setDescricaoMotivo(java.lang.String descricaoMotivo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DESCRICAOMOTIVO$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DESCRICAOMOTIVO$8);
            }
            target.setStringValue(descricaoMotivo);
        }
    }
    
    /**
     * Sets (as xml) the "descricaoMotivo" element
     */
    public void xsetDescricaoMotivo(org.apache.xmlbeans.XmlString descricaoMotivo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOMOTIVO$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DESCRICAOMOTIVO$8);
            }
            target.set(descricaoMotivo);
        }
    }
    
    /**
     * Nils the "descricaoMotivo" element
     */
    public void setNilDescricaoMotivo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOMOTIVO$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DESCRICAOMOTIVO$8);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "dataInicioVigencia" element
     */
    public java.lang.String getDataInicioVigencia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAINICIOVIGENCIA$10, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "dataInicioVigencia" element
     */
    public org.apache.xmlbeans.XmlString xgetDataInicioVigencia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAINICIOVIGENCIA$10, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "dataInicioVigencia" element
     */
    public boolean isNilDataInicioVigencia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAINICIOVIGENCIA$10, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "dataInicioVigencia" element
     */
    public void setDataInicioVigencia(java.lang.String dataInicioVigencia)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAINICIOVIGENCIA$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DATAINICIOVIGENCIA$10);
            }
            target.setStringValue(dataInicioVigencia);
        }
    }
    
    /**
     * Sets (as xml) the "dataInicioVigencia" element
     */
    public void xsetDataInicioVigencia(org.apache.xmlbeans.XmlString dataInicioVigencia)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAINICIOVIGENCIA$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAINICIOVIGENCIA$10);
            }
            target.set(dataInicioVigencia);
        }
    }
    
    /**
     * Nils the "dataInicioVigencia" element
     */
    public void setNilDataInicioVigencia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAINICIOVIGENCIA$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAINICIOVIGENCIA$10);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "dataFimVigencia" element
     */
    public java.lang.String getDataFimVigencia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAFIMVIGENCIA$12, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "dataFimVigencia" element
     */
    public org.apache.xmlbeans.XmlString xgetDataFimVigencia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAFIMVIGENCIA$12, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "dataFimVigencia" element
     */
    public boolean isNilDataFimVigencia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAFIMVIGENCIA$12, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "dataFimVigencia" element
     */
    public void setDataFimVigencia(java.lang.String dataFimVigencia)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAFIMVIGENCIA$12, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DATAFIMVIGENCIA$12);
            }
            target.setStringValue(dataFimVigencia);
        }
    }
    
    /**
     * Sets (as xml) the "dataFimVigencia" element
     */
    public void xsetDataFimVigencia(org.apache.xmlbeans.XmlString dataFimVigencia)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAFIMVIGENCIA$12, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAFIMVIGENCIA$12);
            }
            target.set(dataFimVigencia);
        }
    }
    
    /**
     * Nils the "dataFimVigencia" element
     */
    public void setNilDataFimVigencia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAFIMVIGENCIA$12, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAFIMVIGENCIA$12);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "dataValidade" element
     */
    public java.util.Calendar getDataValidade()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAVALIDADE$14, 0);
            if (target == null)
            {
                return null;
            }
            return target.getCalendarValue();
        }
    }
    
    /**
     * Gets (as xml) the "dataValidade" element
     */
    public org.apache.xmlbeans.XmlDateTime xgetDataValidade()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDateTime target = null;
            target = (org.apache.xmlbeans.XmlDateTime)get_store().find_element_user(DATAVALIDADE$14, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "dataValidade" element
     */
    public boolean isNilDataValidade()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDateTime target = null;
            target = (org.apache.xmlbeans.XmlDateTime)get_store().find_element_user(DATAVALIDADE$14, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "dataValidade" element
     */
    public void setDataValidade(java.util.Calendar dataValidade)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAVALIDADE$14, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DATAVALIDADE$14);
            }
            target.setCalendarValue(dataValidade);
        }
    }
    
    /**
     * Sets (as xml) the "dataValidade" element
     */
    public void xsetDataValidade(org.apache.xmlbeans.XmlDateTime dataValidade)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDateTime target = null;
            target = (org.apache.xmlbeans.XmlDateTime)get_store().find_element_user(DATAVALIDADE$14, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDateTime)get_store().add_element_user(DATAVALIDADE$14);
            }
            target.set(dataValidade);
        }
    }
    
    /**
     * Nils the "dataValidade" element
     */
    public void setNilDataValidade()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDateTime target = null;
            target = (org.apache.xmlbeans.XmlDateTime)get_store().find_element_user(DATAVALIDADE$14, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDateTime)get_store().add_element_user(DATAVALIDADE$14);
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
     * Gets the "usuarioInclusao" element
     */
    public java.lang.String getUsuarioInclusao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USUARIOINCLUSAO$18, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOINCLUSAO$18, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOINCLUSAO$18, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USUARIOINCLUSAO$18, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(USUARIOINCLUSAO$18);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOINCLUSAO$18, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USUARIOINCLUSAO$18);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOINCLUSAO$18, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USUARIOINCLUSAO$18);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USUARIOALTERACAO$20, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOALTERACAO$20, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOALTERACAO$20, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USUARIOALTERACAO$20, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(USUARIOALTERACAO$20);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOALTERACAO$20, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USUARIOALTERACAO$20);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOALTERACAO$20, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USUARIOALTERACAO$20);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAINCLUSAO$22, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAINCLUSAO$22, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAINCLUSAO$22, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAINCLUSAO$22, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DATAINCLUSAO$22);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAINCLUSAO$22, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAINCLUSAO$22);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAINCLUSAO$22, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAINCLUSAO$22);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAALTERACAO$24, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAALTERACAO$24, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAALTERACAO$24, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAALTERACAO$24, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DATAALTERACAO$24);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAALTERACAO$24, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAALTERACAO$24);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAALTERACAO$24, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAALTERACAO$24);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOERRO$26, 0);
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
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(CODIGOERRO$26, 0);
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
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(CODIGOERRO$26, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOERRO$26, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CODIGOERRO$26);
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
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(CODIGOERRO$26, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(CODIGOERRO$26);
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
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(CODIGOERRO$26, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(CODIGOERRO$26);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DESCRICAOERRO$28, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOERRO$28, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOERRO$28, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DESCRICAOERRO$28, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DESCRICAOERRO$28);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOERRO$28, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DESCRICAOERRO$28);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRICAOERRO$28, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DESCRICAOERRO$28);
            }
            target.setNil();
        }
    }
}
