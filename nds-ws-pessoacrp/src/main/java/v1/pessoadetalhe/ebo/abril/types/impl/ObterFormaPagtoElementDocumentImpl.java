/*
 * An XML document type.
 * Localname: obterFormaPagtoElement
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.ObterFormaPagtoElementDocument
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * A document containing one obterFormaPagtoElement(@http://abril.ebo.pessoadetalhe.v1/types/) element.
 *
 * This is a complex type.
 */
public class ObterFormaPagtoElementDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.ObterFormaPagtoElementDocument
{
    
    public ObterFormaPagtoElementDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName OBTERFORMAPAGTOELEMENT$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "obterFormaPagtoElement");
    
    
    /**
     * Gets the "obterFormaPagtoElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterFormaPagto getObterFormaPagtoElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterFormaPagto target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterFormaPagto)get_store().find_element_user(OBTERFORMAPAGTOELEMENT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "obterFormaPagtoElement" element
     */
    public void setObterFormaPagtoElement(v1.pessoadetalhe.ebo.abril.types.ObterFormaPagto obterFormaPagtoElement)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterFormaPagto target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterFormaPagto)get_store().find_element_user(OBTERFORMAPAGTOELEMENT$0, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.ObterFormaPagto)get_store().add_element_user(OBTERFORMAPAGTOELEMENT$0);
            }
            target.set(obterFormaPagtoElement);
        }
    }
    
    /**
     * Appends and returns a new empty "obterFormaPagtoElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterFormaPagto addNewObterFormaPagtoElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterFormaPagto target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterFormaPagto)get_store().add_element_user(OBTERFORMAPAGTOELEMENT$0);
            return target;
        }
    }
}
