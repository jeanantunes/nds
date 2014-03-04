/*
 * An XML document type.
 * Localname: obterContratanteResponseElement
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.ObterContratanteResponseElementDocument
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * A document containing one obterContratanteResponseElement(@http://abril.ebo.pessoadetalhe.v1/types/) element.
 *
 * This is a complex type.
 */
public class ObterContratanteResponseElementDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.ObterContratanteResponseElementDocument
{
    
    public ObterContratanteResponseElementDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName OBTERCONTRATANTERESPONSEELEMENT$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "obterContratanteResponseElement");
    
    
    /**
     * Gets the "obterContratanteResponseElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterContratanteResponse getObterContratanteResponseElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterContratanteResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterContratanteResponse)get_store().find_element_user(OBTERCONTRATANTERESPONSEELEMENT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "obterContratanteResponseElement" element
     */
    public void setObterContratanteResponseElement(v1.pessoadetalhe.ebo.abril.types.ObterContratanteResponse obterContratanteResponseElement)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterContratanteResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterContratanteResponse)get_store().find_element_user(OBTERCONTRATANTERESPONSEELEMENT$0, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.ObterContratanteResponse)get_store().add_element_user(OBTERCONTRATANTERESPONSEELEMENT$0);
            }
            target.set(obterContratanteResponseElement);
        }
    }
    
    /**
     * Appends and returns a new empty "obterContratanteResponseElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterContratanteResponse addNewObterContratanteResponseElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterContratanteResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterContratanteResponse)get_store().add_element_user(OBTERCONTRATANTERESPONSEELEMENT$0);
            return target;
        }
    }
}
