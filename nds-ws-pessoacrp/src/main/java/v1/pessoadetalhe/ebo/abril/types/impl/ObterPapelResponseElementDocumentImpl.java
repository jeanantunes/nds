/*
 * An XML document type.
 * Localname: obterPapelResponseElement
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.ObterPapelResponseElementDocument
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * A document containing one obterPapelResponseElement(@http://abril.ebo.pessoadetalhe.v1/types/) element.
 *
 * This is a complex type.
 */
public class ObterPapelResponseElementDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.ObterPapelResponseElementDocument
{
    
    public ObterPapelResponseElementDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName OBTERPAPELRESPONSEELEMENT$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "obterPapelResponseElement");
    
    
    /**
     * Gets the "obterPapelResponseElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterPapelResponse getObterPapelResponseElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterPapelResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterPapelResponse)get_store().find_element_user(OBTERPAPELRESPONSEELEMENT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "obterPapelResponseElement" element
     */
    public void setObterPapelResponseElement(v1.pessoadetalhe.ebo.abril.types.ObterPapelResponse obterPapelResponseElement)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterPapelResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterPapelResponse)get_store().find_element_user(OBTERPAPELRESPONSEELEMENT$0, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.ObterPapelResponse)get_store().add_element_user(OBTERPAPELRESPONSEELEMENT$0);
            }
            target.set(obterPapelResponseElement);
        }
    }
    
    /**
     * Appends and returns a new empty "obterPapelResponseElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterPapelResponse addNewObterPapelResponseElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterPapelResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterPapelResponse)get_store().add_element_user(OBTERPAPELRESPONSEELEMENT$0);
            return target;
        }
    }
}
