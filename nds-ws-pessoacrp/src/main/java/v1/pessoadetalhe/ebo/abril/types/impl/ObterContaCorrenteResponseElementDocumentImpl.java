/*
 * An XML document type.
 * Localname: obterContaCorrenteResponseElement
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.ObterContaCorrenteResponseElementDocument
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * A document containing one obterContaCorrenteResponseElement(@http://abril.ebo.pessoadetalhe.v1/types/) element.
 *
 * This is a complex type.
 */
public class ObterContaCorrenteResponseElementDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.ObterContaCorrenteResponseElementDocument
{
    
    public ObterContaCorrenteResponseElementDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName OBTERCONTACORRENTERESPONSEELEMENT$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "obterContaCorrenteResponseElement");
    
    
    /**
     * Gets the "obterContaCorrenteResponseElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterContaCorrenteResponse getObterContaCorrenteResponseElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterContaCorrenteResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterContaCorrenteResponse)get_store().find_element_user(OBTERCONTACORRENTERESPONSEELEMENT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "obterContaCorrenteResponseElement" element
     */
    public void setObterContaCorrenteResponseElement(v1.pessoadetalhe.ebo.abril.types.ObterContaCorrenteResponse obterContaCorrenteResponseElement)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterContaCorrenteResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterContaCorrenteResponse)get_store().find_element_user(OBTERCONTACORRENTERESPONSEELEMENT$0, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.ObterContaCorrenteResponse)get_store().add_element_user(OBTERCONTACORRENTERESPONSEELEMENT$0);
            }
            target.set(obterContaCorrenteResponseElement);
        }
    }
    
    /**
     * Appends and returns a new empty "obterContaCorrenteResponseElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterContaCorrenteResponse addNewObterContaCorrenteResponseElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterContaCorrenteResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterContaCorrenteResponse)get_store().add_element_user(OBTERCONTACORRENTERESPONSEELEMENT$0);
            return target;
        }
    }
}
