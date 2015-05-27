/*
 * XML Type:  EnderecosErro
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.EnderecosErro
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * An XML EnderecosErro(@http://abril.ebo.pessoadetalhe.v1/types/).
 *
 * This is a complex type.
 */
public class EnderecosErroImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.EnderecosErro
{
    
    public EnderecosErroImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ENDERECO$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "endereco");
    
    
    /**
     * Gets array of all "endereco" elements
     */
    public v1.pessoadetalhe.ebo.abril.types.Erro[] getEnderecoArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(ENDERECO$0, targetList);
            v1.pessoadetalhe.ebo.abril.types.Erro[] result = new v1.pessoadetalhe.ebo.abril.types.Erro[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "endereco" element
     */
    public v1.pessoadetalhe.ebo.abril.types.Erro getEnderecoArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.Erro target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.Erro)get_store().find_element_user(ENDERECO$0, i);
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
            return get_store().count_elements(ENDERECO$0);
        }
    }
    
    /**
     * Sets array of all "endereco" element
     */
    public void setEnderecoArray(v1.pessoadetalhe.ebo.abril.types.Erro[] enderecoArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(enderecoArray, ENDERECO$0);
        }
    }
    
    /**
     * Sets ith "endereco" element
     */
    public void setEnderecoArray(int i, v1.pessoadetalhe.ebo.abril.types.Erro endereco)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.Erro target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.Erro)get_store().find_element_user(ENDERECO$0, i);
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
    public v1.pessoadetalhe.ebo.abril.types.Erro insertNewEndereco(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.Erro target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.Erro)get_store().insert_element_user(ENDERECO$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "endereco" element
     */
    public v1.pessoadetalhe.ebo.abril.types.Erro addNewEndereco()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.Erro target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.Erro)get_store().add_element_user(ENDERECO$0);
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
            get_store().remove_element(ENDERECO$0, i);
        }
    }
}
