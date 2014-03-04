/*
 * XML Type:  adicionarAlterarCliente
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarCliente
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * An XML adicionarAlterarCliente(@http://abril.ebo.pessoadetalhe.v1/types/).
 *
 * This is a complex type.
 */
public class AdicionarAlterarClienteImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarCliente
{
    
    public AdicionarAlterarClienteImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PESSOA$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "pessoa");
    private static final javax.xml.namespace.QName PESSDET$2 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "PessDet");
    private static final javax.xml.namespace.QName PAPEL$4 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "papel");
    private static final javax.xml.namespace.QName DOCUMENTO$6 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "documento");
    private static final javax.xml.namespace.QName ENDERECO$8 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "endereco");
    
    
    /**
     * Gets the "pessoa" element
     */
    public v1.pessoadetalhe.ebo.abril.types.PessoaINType getPessoa()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.PessoaINType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.PessoaINType)get_store().find_element_user(PESSOA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "pessoa" element
     */
    public void setPessoa(v1.pessoadetalhe.ebo.abril.types.PessoaINType pessoa)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.PessoaINType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.PessoaINType)get_store().find_element_user(PESSOA$0, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.PessoaINType)get_store().add_element_user(PESSOA$0);
            }
            target.set(pessoa);
        }
    }
    
    /**
     * Appends and returns a new empty "pessoa" element
     */
    public v1.pessoadetalhe.ebo.abril.types.PessoaINType addNewPessoa()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.PessoaINType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.PessoaINType)get_store().add_element_user(PESSOA$0);
            return target;
        }
    }
    
    /**
     * Gets the "PessDet" element
     */
    public v1.pessoadetalhe.ebo.abril.types.PessoaDetalheINType getPessDet()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.PessoaDetalheINType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.PessoaDetalheINType)get_store().find_element_user(PESSDET$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "PessDet" element
     */
    public void setPessDet(v1.pessoadetalhe.ebo.abril.types.PessoaDetalheINType pessDet)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.PessoaDetalheINType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.PessoaDetalheINType)get_store().find_element_user(PESSDET$2, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.PessoaDetalheINType)get_store().add_element_user(PESSDET$2);
            }
            target.set(pessDet);
        }
    }
    
    /**
     * Appends and returns a new empty "PessDet" element
     */
    public v1.pessoadetalhe.ebo.abril.types.PessoaDetalheINType addNewPessDet()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.PessoaDetalheINType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.PessoaDetalheINType)get_store().add_element_user(PESSDET$2);
            return target;
        }
    }
    
    /**
     * Gets the "papel" element
     */
    public v1.pessoadetalhe.ebo.abril.types.PapelPessoaINType getPapel()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.PapelPessoaINType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.PapelPessoaINType)get_store().find_element_user(PAPEL$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "papel" element
     */
    public void setPapel(v1.pessoadetalhe.ebo.abril.types.PapelPessoaINType papel)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.PapelPessoaINType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.PapelPessoaINType)get_store().find_element_user(PAPEL$4, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.PapelPessoaINType)get_store().add_element_user(PAPEL$4);
            }
            target.set(papel);
        }
    }
    
    /**
     * Appends and returns a new empty "papel" element
     */
    public v1.pessoadetalhe.ebo.abril.types.PapelPessoaINType addNewPapel()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.PapelPessoaINType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.PapelPessoaINType)get_store().add_element_user(PAPEL$4);
            return target;
        }
    }
    
    /**
     * Gets array of all "documento" elements
     */
    public v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaINType[] getDocumentoArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(DOCUMENTO$6, targetList);
            v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaINType[] result = new v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaINType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "documento" element
     */
    public v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaINType getDocumentoArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaINType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaINType)get_store().find_element_user(DOCUMENTO$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "documento" element
     */
    public int sizeOfDocumentoArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(DOCUMENTO$6);
        }
    }
    
    /**
     * Sets array of all "documento" element
     */
    public void setDocumentoArray(v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaINType[] documentoArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(documentoArray, DOCUMENTO$6);
        }
    }
    
    /**
     * Sets ith "documento" element
     */
    public void setDocumentoArray(int i, v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaINType documento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaINType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaINType)get_store().find_element_user(DOCUMENTO$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(documento);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "documento" element
     */
    public v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaINType insertNewDocumento(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaINType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaINType)get_store().insert_element_user(DOCUMENTO$6, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "documento" element
     */
    public v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaINType addNewDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaINType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaINType)get_store().add_element_user(DOCUMENTO$6);
            return target;
        }
    }
    
    /**
     * Removes the ith "documento" element
     */
    public void removeDocumento(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(DOCUMENTO$6, i);
        }
    }
    
    /**
     * Gets array of all "endereco" elements
     */
    public v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType[] getEnderecoArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(ENDERECO$8, targetList);
            v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType[] result = new v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "endereco" element
     */
    public v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType getEnderecoArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType)get_store().find_element_user(ENDERECO$8, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "endereco" element
     */
    public int sizeOfEnderecoArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(ENDERECO$8);
        }
    }
    
    /**
     * Sets array of all "endereco" element
     */
    public void setEnderecoArray(v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType[] enderecoArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(enderecoArray, ENDERECO$8);
        }
    }
    
    /**
     * Sets ith "endereco" element
     */
    public void setEnderecoArray(int i, v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType endereco)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType)get_store().find_element_user(ENDERECO$8, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(endereco);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "endereco" element
     */
    public v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType insertNewEndereco(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType)get_store().insert_element_user(ENDERECO$8, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "endereco" element
     */
    public v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType addNewEndereco()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType)get_store().add_element_user(ENDERECO$8);
            return target;
        }
    }
    
    /**
     * Removes the ith "endereco" element
     */
    public void removeEndereco(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(ENDERECO$8, i);
        }
    }
}
