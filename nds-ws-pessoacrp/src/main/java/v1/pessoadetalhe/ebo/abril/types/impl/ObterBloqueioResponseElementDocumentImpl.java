/*
 * An XML document type.
 * Localname: obterBloqueioResponseElement
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * A document containing one obterBloqueioResponseElement(@http://abril.ebo.pessoadetalhe.v1/types/) element.
 *
 * This is a complex type.
 */
public class ObterBloqueioResponseElementDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument
{
    
    public ObterBloqueioResponseElementDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName OBTERBLOQUEIORESPONSEELEMENT$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "obterBloqueioResponseElement");
    
    
    /**
     * Gets the "obterBloqueioResponseElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponse getObterBloqueioResponseElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponse)get_store().find_element_user(OBTERBLOQUEIORESPONSEELEMENT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "obterBloqueioResponseElement" element
     */
    public void setObterBloqueioResponseElement(v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponse obterBloqueioResponseElement)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponse)get_store().find_element_user(OBTERBLOQUEIORESPONSEELEMENT$0, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponse)get_store().add_element_user(OBTERBLOQUEIORESPONSEELEMENT$0);
            }
            target.set(obterBloqueioResponseElement);
        }
    }
    
    /**
     * Appends and returns a new empty "obterBloqueioResponseElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponse addNewObterBloqueioResponseElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponse)get_store().add_element_user(OBTERBLOQUEIORESPONSEELEMENT$0);
            return target;
        }
    }
}
