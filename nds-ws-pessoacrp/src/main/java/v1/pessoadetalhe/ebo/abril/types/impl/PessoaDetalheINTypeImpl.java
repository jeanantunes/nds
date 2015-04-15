/*
 * XML Type:  PessoaDetalheINType
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.PessoaDetalheINType
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * An XML PessoaDetalheINType(@http://abril.ebo.pessoadetalhe.v1/types/).
 *
 * This is a complex type.
 */
public class PessoaDetalheINTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.PessoaDetalheINType
{
    
    public PessoaDetalheINTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CODIGOTIPOCONTRIBUINTE$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codigoTipoContribuinte");
    private static final javax.xml.namespace.QName CODIGOSISTEMA$2 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codigoSistema");
    private static final javax.xml.namespace.QName NOMEABREVIADO$4 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "nomeAbreviado");
    private static final javax.xml.namespace.QName DDI$6 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "ddi");
    private static final javax.xml.namespace.QName DDD$8 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "ddd");
    private static final javax.xml.namespace.QName TELEFONE$10 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "telefone");
    private static final javax.xml.namespace.QName RAMAL$12 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "ramal");
    private static final javax.xml.namespace.QName FAX$14 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "fax");
    private static final javax.xml.namespace.QName EMAIL$16 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "email");
    private static final javax.xml.namespace.QName URL$18 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "url");
    private static final javax.xml.namespace.QName EMAILFATURAMENTO$20 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "emailFaturamento");
    private static final javax.xml.namespace.QName USUARIOINCLUSAO$22 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "usuarioInclusao");
    private static final javax.xml.namespace.QName USUARIOALTERACAO$24 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "usuarioAlteracao");
    private static final javax.xml.namespace.QName USUARIOEXCLUSAO$26 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "usuarioExclusao");
    private static final javax.xml.namespace.QName DATAINCLUSAO$28 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "dataInclusao");
    private static final javax.xml.namespace.QName DATAALTERACAO$30 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "dataAlteracao");
    private static final javax.xml.namespace.QName DATAEXCLUSAO$32 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "dataExclusao");
    private static final javax.xml.namespace.QName CODIGOPESSOAGL$34 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "codigoPessoaGl");
    private static final javax.xml.namespace.QName INDATIVIDADEINDUSTRIAL$36 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "indAtividadeIndustrial");
    private static final javax.xml.namespace.QName GRUPONATUREZAJURIDICA$38 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "grupoNaturezaJuridica");
    
    
    /**
     * Gets the "codigoTipoContribuinte" element
     */
    public long getCodigoTipoContribuinte()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOTIPOCONTRIBUINTE$0, 0);
            if (target == null)
            {
                return 0L;
            }
            return target.getLongValue();
        }
    }
    
    /**
     * Gets (as xml) the "codigoTipoContribuinte" element
     */
    public org.apache.xmlbeans.XmlLong xgetCodigoTipoContribuinte()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGOTIPOCONTRIBUINTE$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "codigoTipoContribuinte" element
     */
    public void setCodigoTipoContribuinte(long codigoTipoContribuinte)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOTIPOCONTRIBUINTE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CODIGOTIPOCONTRIBUINTE$0);
            }
            target.setLongValue(codigoTipoContribuinte);
        }
    }
    
    /**
     * Sets (as xml) the "codigoTipoContribuinte" element
     */
    public void xsetCodigoTipoContribuinte(org.apache.xmlbeans.XmlLong codigoTipoContribuinte)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(CODIGOTIPOCONTRIBUINTE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_element_user(CODIGOTIPOCONTRIBUINTE$0);
            }
            target.set(codigoTipoContribuinte);
        }
    }
    
    /**
     * Gets the "codigoSistema" element
     */
    public java.lang.String getCodigoSistema()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOSISTEMA$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "codigoSistema" element
     */
    public org.apache.xmlbeans.XmlString xgetCodigoSistema()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOSISTEMA$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "codigoSistema" element
     */
    public void setCodigoSistema(java.lang.String codigoSistema)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOSISTEMA$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CODIGOSISTEMA$2);
            }
            target.setStringValue(codigoSistema);
        }
    }
    
    /**
     * Sets (as xml) the "codigoSistema" element
     */
    public void xsetCodigoSistema(org.apache.xmlbeans.XmlString codigoSistema)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOSISTEMA$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CODIGOSISTEMA$2);
            }
            target.set(codigoSistema);
        }
    }
    
    /**
     * Gets the "nomeAbreviado" element
     */
    public java.lang.String getNomeAbreviado()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NOMEABREVIADO$4, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "nomeAbreviado" element
     */
    public org.apache.xmlbeans.XmlString xgetNomeAbreviado()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEABREVIADO$4, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "nomeAbreviado" element
     */
    public boolean isNilNomeAbreviado()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEABREVIADO$4, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "nomeAbreviado" element
     */
    public void setNomeAbreviado(java.lang.String nomeAbreviado)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NOMEABREVIADO$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NOMEABREVIADO$4);
            }
            target.setStringValue(nomeAbreviado);
        }
    }
    
    /**
     * Sets (as xml) the "nomeAbreviado" element
     */
    public void xsetNomeAbreviado(org.apache.xmlbeans.XmlString nomeAbreviado)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEABREVIADO$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NOMEABREVIADO$4);
            }
            target.set(nomeAbreviado);
        }
    }
    
    /**
     * Nils the "nomeAbreviado" element
     */
    public void setNilNomeAbreviado()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOMEABREVIADO$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NOMEABREVIADO$4);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "ddi" element
     */
    public java.lang.String getDdi()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DDI$6, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "ddi" element
     */
    public org.apache.xmlbeans.XmlString xgetDdi()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DDI$6, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "ddi" element
     */
    public boolean isNilDdi()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DDI$6, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "ddi" element
     */
    public void setDdi(java.lang.String ddi)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DDI$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DDI$6);
            }
            target.setStringValue(ddi);
        }
    }
    
    /**
     * Sets (as xml) the "ddi" element
     */
    public void xsetDdi(org.apache.xmlbeans.XmlString ddi)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DDI$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DDI$6);
            }
            target.set(ddi);
        }
    }
    
    /**
     * Nils the "ddi" element
     */
    public void setNilDdi()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DDI$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DDI$6);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "ddd" element
     */
    public java.lang.String getDdd()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DDD$8, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "ddd" element
     */
    public org.apache.xmlbeans.XmlString xgetDdd()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DDD$8, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "ddd" element
     */
    public boolean isNilDdd()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DDD$8, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "ddd" element
     */
    public void setDdd(java.lang.String ddd)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DDD$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DDD$8);
            }
            target.setStringValue(ddd);
        }
    }
    
    /**
     * Sets (as xml) the "ddd" element
     */
    public void xsetDdd(org.apache.xmlbeans.XmlString ddd)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DDD$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DDD$8);
            }
            target.set(ddd);
        }
    }
    
    /**
     * Nils the "ddd" element
     */
    public void setNilDdd()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DDD$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DDD$8);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "telefone" element
     */
    public java.lang.String getTelefone()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TELEFONE$10, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "telefone" element
     */
    public org.apache.xmlbeans.XmlString xgetTelefone()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TELEFONE$10, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "telefone" element
     */
    public boolean isNilTelefone()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TELEFONE$10, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "telefone" element
     */
    public void setTelefone(java.lang.String telefone)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TELEFONE$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(TELEFONE$10);
            }
            target.setStringValue(telefone);
        }
    }
    
    /**
     * Sets (as xml) the "telefone" element
     */
    public void xsetTelefone(org.apache.xmlbeans.XmlString telefone)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TELEFONE$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(TELEFONE$10);
            }
            target.set(telefone);
        }
    }
    
    /**
     * Nils the "telefone" element
     */
    public void setNilTelefone()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TELEFONE$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(TELEFONE$10);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "ramal" element
     */
    public java.lang.String getRamal()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RAMAL$12, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "ramal" element
     */
    public org.apache.xmlbeans.XmlString xgetRamal()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(RAMAL$12, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "ramal" element
     */
    public boolean isNilRamal()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(RAMAL$12, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "ramal" element
     */
    public void setRamal(java.lang.String ramal)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RAMAL$12, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(RAMAL$12);
            }
            target.setStringValue(ramal);
        }
    }
    
    /**
     * Sets (as xml) the "ramal" element
     */
    public void xsetRamal(org.apache.xmlbeans.XmlString ramal)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(RAMAL$12, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(RAMAL$12);
            }
            target.set(ramal);
        }
    }
    
    /**
     * Nils the "ramal" element
     */
    public void setNilRamal()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(RAMAL$12, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(RAMAL$12);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "fax" element
     */
    public java.lang.String getFax()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(FAX$14, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "fax" element
     */
    public org.apache.xmlbeans.XmlString xgetFax()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(FAX$14, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "fax" element
     */
    public boolean isNilFax()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(FAX$14, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "fax" element
     */
    public void setFax(java.lang.String fax)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(FAX$14, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(FAX$14);
            }
            target.setStringValue(fax);
        }
    }
    
    /**
     * Sets (as xml) the "fax" element
     */
    public void xsetFax(org.apache.xmlbeans.XmlString fax)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(FAX$14, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(FAX$14);
            }
            target.set(fax);
        }
    }
    
    /**
     * Nils the "fax" element
     */
    public void setNilFax()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(FAX$14, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(FAX$14);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "email" element
     */
    public java.lang.String getEmail()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EMAIL$16, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "email" element
     */
    public org.apache.xmlbeans.XmlString xgetEmail()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EMAIL$16, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "email" element
     */
    public boolean isNilEmail()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EMAIL$16, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "email" element
     */
    public void setEmail(java.lang.String email)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EMAIL$16, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(EMAIL$16);
            }
            target.setStringValue(email);
        }
    }
    
    /**
     * Sets (as xml) the "email" element
     */
    public void xsetEmail(org.apache.xmlbeans.XmlString email)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EMAIL$16, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(EMAIL$16);
            }
            target.set(email);
        }
    }
    
    /**
     * Nils the "email" element
     */
    public void setNilEmail()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EMAIL$16, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(EMAIL$16);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "url" element
     */
    public java.lang.String getUrl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(URL$18, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "url" element
     */
    public org.apache.xmlbeans.XmlString xgetUrl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(URL$18, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "url" element
     */
    public boolean isNilUrl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(URL$18, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "url" element
     */
    public void setUrl(java.lang.String url)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(URL$18, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(URL$18);
            }
            target.setStringValue(url);
        }
    }
    
    /**
     * Sets (as xml) the "url" element
     */
    public void xsetUrl(org.apache.xmlbeans.XmlString url)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(URL$18, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(URL$18);
            }
            target.set(url);
        }
    }
    
    /**
     * Nils the "url" element
     */
    public void setNilUrl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(URL$18, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(URL$18);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "emailFaturamento" element
     */
    public java.lang.String getEmailFaturamento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EMAILFATURAMENTO$20, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "emailFaturamento" element
     */
    public org.apache.xmlbeans.XmlString xgetEmailFaturamento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EMAILFATURAMENTO$20, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "emailFaturamento" element
     */
    public boolean isNilEmailFaturamento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EMAILFATURAMENTO$20, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "emailFaturamento" element
     */
    public void setEmailFaturamento(java.lang.String emailFaturamento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EMAILFATURAMENTO$20, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(EMAILFATURAMENTO$20);
            }
            target.setStringValue(emailFaturamento);
        }
    }
    
    /**
     * Sets (as xml) the "emailFaturamento" element
     */
    public void xsetEmailFaturamento(org.apache.xmlbeans.XmlString emailFaturamento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EMAILFATURAMENTO$20, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(EMAILFATURAMENTO$20);
            }
            target.set(emailFaturamento);
        }
    }
    
    /**
     * Nils the "emailFaturamento" element
     */
    public void setNilEmailFaturamento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EMAILFATURAMENTO$20, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(EMAILFATURAMENTO$20);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USUARIOINCLUSAO$22, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOINCLUSAO$22, 0);
            return target;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USUARIOINCLUSAO$22, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(USUARIOINCLUSAO$22);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOINCLUSAO$22, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USUARIOINCLUSAO$22);
            }
            target.set(usuarioInclusao);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USUARIOALTERACAO$24, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOALTERACAO$24, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOALTERACAO$24, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USUARIOALTERACAO$24, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(USUARIOALTERACAO$24);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOALTERACAO$24, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USUARIOALTERACAO$24);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOALTERACAO$24, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USUARIOALTERACAO$24);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USUARIOEXCLUSAO$26, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOEXCLUSAO$26, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOEXCLUSAO$26, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USUARIOEXCLUSAO$26, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(USUARIOEXCLUSAO$26);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOEXCLUSAO$26, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USUARIOEXCLUSAO$26);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USUARIOEXCLUSAO$26, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USUARIOEXCLUSAO$26);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAINCLUSAO$28, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAINCLUSAO$28, 0);
            return target;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAINCLUSAO$28, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DATAINCLUSAO$28);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAINCLUSAO$28, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAINCLUSAO$28);
            }
            target.set(dataInclusao);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAALTERACAO$30, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAALTERACAO$30, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAALTERACAO$30, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAALTERACAO$30, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DATAALTERACAO$30);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAALTERACAO$30, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAALTERACAO$30);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAALTERACAO$30, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAALTERACAO$30);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAEXCLUSAO$32, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAEXCLUSAO$32, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAEXCLUSAO$32, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAEXCLUSAO$32, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DATAEXCLUSAO$32);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAEXCLUSAO$32, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAEXCLUSAO$32);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAEXCLUSAO$32, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAEXCLUSAO$32);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "codigoPessoaGl" element
     */
    public java.lang.String getCodigoPessoaGl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOPESSOAGL$34, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "codigoPessoaGl" element
     */
    public org.apache.xmlbeans.XmlString xgetCodigoPessoaGl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOPESSOAGL$34, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "codigoPessoaGl" element
     */
    public boolean isNilCodigoPessoaGl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOPESSOAGL$34, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "codigoPessoaGl" element
     */
    public void setCodigoPessoaGl(java.lang.String codigoPessoaGl)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODIGOPESSOAGL$34, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CODIGOPESSOAGL$34);
            }
            target.setStringValue(codigoPessoaGl);
        }
    }
    
    /**
     * Sets (as xml) the "codigoPessoaGl" element
     */
    public void xsetCodigoPessoaGl(org.apache.xmlbeans.XmlString codigoPessoaGl)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOPESSOAGL$34, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CODIGOPESSOAGL$34);
            }
            target.set(codigoPessoaGl);
        }
    }
    
    /**
     * Nils the "codigoPessoaGl" element
     */
    public void setNilCodigoPessoaGl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODIGOPESSOAGL$34, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CODIGOPESSOAGL$34);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "indAtividadeIndustrial" element
     */
    public java.lang.String getIndAtividadeIndustrial()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(INDATIVIDADEINDUSTRIAL$36, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "indAtividadeIndustrial" element
     */
    public org.apache.xmlbeans.XmlString xgetIndAtividadeIndustrial()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(INDATIVIDADEINDUSTRIAL$36, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "indAtividadeIndustrial" element
     */
    public boolean isNilIndAtividadeIndustrial()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(INDATIVIDADEINDUSTRIAL$36, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "indAtividadeIndustrial" element
     */
    public void setIndAtividadeIndustrial(java.lang.String indAtividadeIndustrial)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(INDATIVIDADEINDUSTRIAL$36, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(INDATIVIDADEINDUSTRIAL$36);
            }
            target.setStringValue(indAtividadeIndustrial);
        }
    }
    
    /**
     * Sets (as xml) the "indAtividadeIndustrial" element
     */
    public void xsetIndAtividadeIndustrial(org.apache.xmlbeans.XmlString indAtividadeIndustrial)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(INDATIVIDADEINDUSTRIAL$36, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(INDATIVIDADEINDUSTRIAL$36);
            }
            target.set(indAtividadeIndustrial);
        }
    }
    
    /**
     * Nils the "indAtividadeIndustrial" element
     */
    public void setNilIndAtividadeIndustrial()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(INDATIVIDADEINDUSTRIAL$36, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(INDATIVIDADEINDUSTRIAL$36);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "grupoNaturezaJuridica" element
     */
    public java.lang.String getGrupoNaturezaJuridica()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(GRUPONATUREZAJURIDICA$38, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "grupoNaturezaJuridica" element
     */
    public org.apache.xmlbeans.XmlString xgetGrupoNaturezaJuridica()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(GRUPONATUREZAJURIDICA$38, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "grupoNaturezaJuridica" element
     */
    public boolean isNilGrupoNaturezaJuridica()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(GRUPONATUREZAJURIDICA$38, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "grupoNaturezaJuridica" element
     */
    public void setGrupoNaturezaJuridica(java.lang.String grupoNaturezaJuridica)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(GRUPONATUREZAJURIDICA$38, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(GRUPONATUREZAJURIDICA$38);
            }
            target.setStringValue(grupoNaturezaJuridica);
        }
    }
    
    /**
     * Sets (as xml) the "grupoNaturezaJuridica" element
     */
    public void xsetGrupoNaturezaJuridica(org.apache.xmlbeans.XmlString grupoNaturezaJuridica)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(GRUPONATUREZAJURIDICA$38, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(GRUPONATUREZAJURIDICA$38);
            }
            target.set(grupoNaturezaJuridica);
        }
    }
    
    /**
     * Nils the "grupoNaturezaJuridica" element
     */
    public void setNilGrupoNaturezaJuridica()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(GRUPONATUREZAJURIDICA$38, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(GRUPONATUREZAJURIDICA$38);
            }
            target.setNil();
        }
    }
}
