/*
 * XML Type:  PessoaDto
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.PessoaDto
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * An XML PessoaDto(@http://abril.ebo.pessoadetalhe.v1/types/).
 *
 * This is a complex type.
 */
public class PessoaDtoImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.PessoaDto
{
    
    public PessoaDtoImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PESSOA$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "pessoa");
    private static final javax.xml.namespace.QName PESSOADETALHE$2 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "pessoaDetalhe");
    private static final javax.xml.namespace.QName DOCUMENTO$4 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "documento");
    private static final javax.xml.namespace.QName ENDERECO$6 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "endereco");
    
    
    /**
     * Gets the "pessoa" element
     */
    public v1.pessoadetalhe.ebo.abril.types.PessoaType getPessoa()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.PessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.PessoaType)get_store().find_element_user(PESSOA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Tests for nil "pessoa" element
     */
    public boolean isNilPessoa()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.PessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.PessoaType)get_store().find_element_user(PESSOA$0, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "pessoa" element
     */
    public void setPessoa(v1.pessoadetalhe.ebo.abril.types.PessoaType pessoa)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.PessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.PessoaType)get_store().find_element_user(PESSOA$0, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.PessoaType)get_store().add_element_user(PESSOA$0);
            }
            target.set(pessoa);
        }
    }
    
    /**
     * Appends and returns a new empty "pessoa" element
     */
    public v1.pessoadetalhe.ebo.abril.types.PessoaType addNewPessoa()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.PessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.PessoaType)get_store().add_element_user(PESSOA$0);
            return target;
        }
    }
    
    /**
     * Nils the "pessoa" element
     */
    public void setNilPessoa()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.PessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.PessoaType)get_store().find_element_user(PESSOA$0, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.PessoaType)get_store().add_element_user(PESSOA$0);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets the "pessoaDetalhe" element
     */
    public v1.pessoadetalhe.ebo.abril.types.PessoaDetalheType getPessoaDetalhe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.PessoaDetalheType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.PessoaDetalheType)get_store().find_element_user(PESSOADETALHE$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Tests for nil "pessoaDetalhe" element
     */
    public boolean isNilPessoaDetalhe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.PessoaDetalheType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.PessoaDetalheType)get_store().find_element_user(PESSOADETALHE$2, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "pessoaDetalhe" element
     */
    public void setPessoaDetalhe(v1.pessoadetalhe.ebo.abril.types.PessoaDetalheType pessoaDetalhe)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.PessoaDetalheType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.PessoaDetalheType)get_store().find_element_user(PESSOADETALHE$2, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.PessoaDetalheType)get_store().add_element_user(PESSOADETALHE$2);
            }
            target.set(pessoaDetalhe);
        }
    }
    
    /**
     * Appends and returns a new empty "pessoaDetalhe" element
     */
    public v1.pessoadetalhe.ebo.abril.types.PessoaDetalheType addNewPessoaDetalhe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.PessoaDetalheType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.PessoaDetalheType)get_store().add_element_user(PESSOADETALHE$2);
            return target;
        }
    }
    
    /**
     * Nils the "pessoaDetalhe" element
     */
    public void setNilPessoaDetalhe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.PessoaDetalheType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.PessoaDetalheType)get_store().find_element_user(PESSOADETALHE$2, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.PessoaDetalheType)get_store().add_element_user(PESSOADETALHE$2);
            }
            target.setNil();
        }
    }
    
    /**
     * Gets array of all "documento" elements
     */
    public v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType[] getDocumentoArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(DOCUMENTO$4, targetList);
            v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType[] result = new v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "documento" element
     */
    public v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType getDocumentoArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType)get_store().find_element_user(DOCUMENTO$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Tests for nil ith "documento" element
     */
    public boolean isNilDocumentoArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType)get_store().find_element_user(DOCUMENTO$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target.isNil();
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
            return get_store().count_elements(DOCUMENTO$4);
        }
    }
    
    /**
     * Sets array of all "documento" element
     */
    public void setDocumentoArray(v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType[] documentoArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(documentoArray, DOCUMENTO$4);
        }
    }
    
    /**
     * Sets ith "documento" element
     */
    public void setDocumentoArray(int i, v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType documento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType)get_store().find_element_user(DOCUMENTO$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(documento);
        }
    }
    
    /**
     * Nils the ith "documento" element
     */
    public void setNilDocumentoArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType)get_store().find_element_user(DOCUMENTO$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.setNil();
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "documento" element
     */
    public v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType insertNewDocumento(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType)get_store().insert_element_user(DOCUMENTO$4, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "documento" element
     */
    public v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType addNewDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType)get_store().add_element_user(DOCUMENTO$4);
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
            get_store().remove_element(DOCUMENTO$4, i);
        }
    }
    
    /**
     * Gets array of all "endereco" elements
     */
    public v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType[] getEnderecoArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(ENDERECO$6, targetList);
            v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType[] result = new v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "endereco" element
     */
    public v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType getEnderecoArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType)get_store().find_element_user(ENDERECO$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Tests for nil ith "endereco" element
     */
    public boolean isNilEnderecoArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType)get_store().find_element_user(ENDERECO$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target.isNil();
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
            return get_store().count_elements(ENDERECO$6);
        }
    }
    
    /**
     * Sets array of all "endereco" element
     */
    public void setEnderecoArray(v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType[] enderecoArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(enderecoArray, ENDERECO$6);
        }
    }
    
    /**
     * Sets ith "endereco" element
     */
    public void setEnderecoArray(int i, v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType endereco)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType)get_store().find_element_user(ENDERECO$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(endereco);
        }
    }
    
    /**
     * Nils the ith "endereco" element
     */
    public void setNilEnderecoArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType)get_store().find_element_user(ENDERECO$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.setNil();
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "endereco" element
     */
    public v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType insertNewEndereco(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType)get_store().insert_element_user(ENDERECO$6, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "endereco" element
     */
    public v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType addNewEndereco()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType)get_store().add_element_user(ENDERECO$6);
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
            get_store().remove_element(ENDERECO$6, i);
        }
    }
}
