/*
 * XML Type:  obterContratanteResponse
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.ObterContratanteResponse
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * An XML obterContratanteResponse(@http://abril.ebo.pessoadetalhe.v1/types/).
 *
 * This is a complex type.
 */
public class ObterContratanteResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.ObterContratanteResponse
{
    
    public ObterContratanteResponseImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RESULT$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "result");
    
    
    /**
     * Gets array of all "result" elements
     */
    public v1.pessoadetalhe.ebo.abril.types.ContratantePessoaType[] getResultArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(RESULT$0, targetList);
            v1.pessoadetalhe.ebo.abril.types.ContratantePessoaType[] result = new v1.pessoadetalhe.ebo.abril.types.ContratantePessoaType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "result" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ContratantePessoaType getResultArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ContratantePessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ContratantePessoaType)get_store().find_element_user(RESULT$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Tests for nil ith "result" element
     */
    public boolean isNilResultArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ContratantePessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ContratantePessoaType)get_store().find_element_user(RESULT$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target.isNil();
        }
    }
    
    /**
     * Returns number of "result" element
     */
    public int sizeOfResultArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(RESULT$0);
        }
    }
    
    /**
     * Sets array of all "result" element
     */
    public void setResultArray(v1.pessoadetalhe.ebo.abril.types.ContratantePessoaType[] resultArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(resultArray, RESULT$0);
        }
    }
    
    /**
     * Sets ith "result" element
     */
    public void setResultArray(int i, v1.pessoadetalhe.ebo.abril.types.ContratantePessoaType result)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ContratantePessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ContratantePessoaType)get_store().find_element_user(RESULT$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(result);
        }
    }
    
    /**
     * Nils the ith "result" element
     */
    public void setNilResultArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ContratantePessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ContratantePessoaType)get_store().find_element_user(RESULT$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.setNil();
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "result" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ContratantePessoaType insertNewResult(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ContratantePessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ContratantePessoaType)get_store().insert_element_user(RESULT$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "result" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ContratantePessoaType addNewResult()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ContratantePessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ContratantePessoaType)get_store().add_element_user(RESULT$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "result" element
     */
    public void removeResult(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(RESULT$0, i);
        }
    }
}
