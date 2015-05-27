/*
 * An XML document type.
 * Localname: obterFormaPagtoResponseElement
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.ObterFormaPagtoResponseElementDocument
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * A document containing one obterFormaPagtoResponseElement(@http://abril.ebo.pessoadetalhe.v1/types/) element.
 *
 * This is a complex type.
 */
public class ObterFormaPagtoResponseElementDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.ObterFormaPagtoResponseElementDocument
{
    
    public ObterFormaPagtoResponseElementDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName OBTERFORMAPAGTORESPONSEELEMENT$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "obterFormaPagtoResponseElement");
    
    
    /**
     * Gets the "obterFormaPagtoResponseElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterFormaPagtoResponse getObterFormaPagtoResponseElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterFormaPagtoResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterFormaPagtoResponse)get_store().find_element_user(OBTERFORMAPAGTORESPONSEELEMENT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "obterFormaPagtoResponseElement" element
     */
    public void setObterFormaPagtoResponseElement(v1.pessoadetalhe.ebo.abril.types.ObterFormaPagtoResponse obterFormaPagtoResponseElement)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterFormaPagtoResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterFormaPagtoResponse)get_store().find_element_user(OBTERFORMAPAGTORESPONSEELEMENT$0, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.ObterFormaPagtoResponse)get_store().add_element_user(OBTERFORMAPAGTORESPONSEELEMENT$0);
            }
            target.set(obterFormaPagtoResponseElement);
        }
    }
    
    /**
     * Appends and returns a new empty "obterFormaPagtoResponseElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterFormaPagtoResponse addNewObterFormaPagtoResponseElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterFormaPagtoResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterFormaPagtoResponse)get_store().add_element_user(OBTERFORMAPAGTORESPONSEELEMENT$0);
            return target;
        }
    }
}
