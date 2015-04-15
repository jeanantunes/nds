/*
 * XML Type:  obterProtocoloResponse
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.ObterProtocoloResponse
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * An XML obterProtocoloResponse(@http://abril.ebo.pessoadetalhe.v1/types/).
 *
 * This is a complex type.
 */
public class ObterProtocoloResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.ObterProtocoloResponse
{
    
    public ObterProtocoloResponseImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RESULT$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "result");
    
    
    /**
     * Gets the "result" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ProtocoloPessoaType getResult()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ProtocoloPessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ProtocoloPessoaType)get_store().find_element_user(RESULT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Tests for nil "result" element
     */
    public boolean isNilResult()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ProtocoloPessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ProtocoloPessoaType)get_store().find_element_user(RESULT$0, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "result" element
     */
    public void setResult(v1.pessoadetalhe.ebo.abril.types.ProtocoloPessoaType result)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ProtocoloPessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ProtocoloPessoaType)get_store().find_element_user(RESULT$0, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.ProtocoloPessoaType)get_store().add_element_user(RESULT$0);
            }
            target.set(result);
        }
    }
    
    /**
     * Appends and returns a new empty "result" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ProtocoloPessoaType addNewResult()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ProtocoloPessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ProtocoloPessoaType)get_store().add_element_user(RESULT$0);
            return target;
        }
    }
    
    /**
     * Nils the "result" element
     */
    public void setNilResult()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ProtocoloPessoaType target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ProtocoloPessoaType)get_store().find_element_user(RESULT$0, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.ProtocoloPessoaType)get_store().add_element_user(RESULT$0);
            }
            target.setNil();
        }
    }
}
