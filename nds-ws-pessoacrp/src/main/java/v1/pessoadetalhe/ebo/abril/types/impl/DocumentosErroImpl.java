/*
 * XML Type:  DocumentosErro
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.DocumentosErro
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * An XML DocumentosErro(@http://abril.ebo.pessoadetalhe.v1/types/).
 *
 * This is a complex type.
 */
public class DocumentosErroImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.DocumentosErro
{
    
    public DocumentosErroImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DOCUMENTO$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "documento");
    
    
    /**
     * Gets array of all "documento" elements
     */
    public v1.pessoadetalhe.ebo.abril.types.DocumentoErro[] getDocumentoArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(DOCUMENTO$0, targetList);
            v1.pessoadetalhe.ebo.abril.types.DocumentoErro[] result = new v1.pessoadetalhe.ebo.abril.types.DocumentoErro[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "documento" element
     */
    public v1.pessoadetalhe.ebo.abril.types.DocumentoErro getDocumentoArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.DocumentoErro target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.DocumentoErro)get_store().find_element_user(DOCUMENTO$0, i);
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
            return get_store().count_elements(DOCUMENTO$0);
        }
    }
    
    /**
     * Sets array of all "documento" element
     */
    public void setDocumentoArray(v1.pessoadetalhe.ebo.abril.types.DocumentoErro[] documentoArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(documentoArray, DOCUMENTO$0);
        }
    }
    
    /**
     * Sets ith "documento" element
     */
    public void setDocumentoArray(int i, v1.pessoadetalhe.ebo.abril.types.DocumentoErro documento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.DocumentoErro target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.DocumentoErro)get_store().find_element_user(DOCUMENTO$0, i);
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
    public v1.pessoadetalhe.ebo.abril.types.DocumentoErro insertNewDocumento(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.DocumentoErro target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.DocumentoErro)get_store().insert_element_user(DOCUMENTO$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "documento" element
     */
    public v1.pessoadetalhe.ebo.abril.types.DocumentoErro addNewDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.DocumentoErro target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.DocumentoErro)get_store().add_element_user(DOCUMENTO$0);
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
            get_store().remove_element(DOCUMENTO$0, i);
        }
    }
}
