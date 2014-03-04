/*
 * An XML document type.
 * Localname: obterProtocoloResponseElement
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.ObterProtocoloResponseElementDocument
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * A document containing one obterProtocoloResponseElement(@http://abril.ebo.pessoadetalhe.v1/types/) element.
 *
 * This is a complex type.
 */
public class ObterProtocoloResponseElementDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.ObterProtocoloResponseElementDocument
{
    
    public ObterProtocoloResponseElementDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName OBTERPROTOCOLORESPONSEELEMENT$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "obterProtocoloResponseElement");
    
    
    /**
     * Gets the "obterProtocoloResponseElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterProtocoloResponse getObterProtocoloResponseElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterProtocoloResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterProtocoloResponse)get_store().find_element_user(OBTERPROTOCOLORESPONSEELEMENT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "obterProtocoloResponseElement" element
     */
    public void setObterProtocoloResponseElement(v1.pessoadetalhe.ebo.abril.types.ObterProtocoloResponse obterProtocoloResponseElement)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterProtocoloResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterProtocoloResponse)get_store().find_element_user(OBTERPROTOCOLORESPONSEELEMENT$0, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.ObterProtocoloResponse)get_store().add_element_user(OBTERPROTOCOLORESPONSEELEMENT$0);
            }
            target.set(obterProtocoloResponseElement);
        }
    }
    
    /**
     * Appends and returns a new empty "obterProtocoloResponseElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterProtocoloResponse addNewObterProtocoloResponseElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterProtocoloResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterProtocoloResponse)get_store().add_element_user(OBTERPROTOCOLORESPONSEELEMENT$0);
            return target;
        }
    }
}
