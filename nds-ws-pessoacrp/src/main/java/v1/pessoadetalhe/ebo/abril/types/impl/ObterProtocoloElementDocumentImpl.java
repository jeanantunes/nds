/*
 * An XML document type.
 * Localname: obterProtocoloElement
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.ObterProtocoloElementDocument
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * A document containing one obterProtocoloElement(@http://abril.ebo.pessoadetalhe.v1/types/) element.
 *
 * This is a complex type.
 */
public class ObterProtocoloElementDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.ObterProtocoloElementDocument
{
    
    public ObterProtocoloElementDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName OBTERPROTOCOLOELEMENT$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "obterProtocoloElement");
    
    
    /**
     * Gets the "obterProtocoloElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterProtocolo getObterProtocoloElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterProtocolo target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterProtocolo)get_store().find_element_user(OBTERPROTOCOLOELEMENT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "obterProtocoloElement" element
     */
    public void setObterProtocoloElement(v1.pessoadetalhe.ebo.abril.types.ObterProtocolo obterProtocoloElement)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterProtocolo target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterProtocolo)get_store().find_element_user(OBTERPROTOCOLOELEMENT$0, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.ObterProtocolo)get_store().add_element_user(OBTERPROTOCOLOELEMENT$0);
            }
            target.set(obterProtocoloElement);
        }
    }
    
    /**
     * Appends and returns a new empty "obterProtocoloElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterProtocolo addNewObterProtocoloElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterProtocolo target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterProtocolo)get_store().add_element_user(OBTERPROTOCOLOELEMENT$0);
            return target;
        }
    }
}
