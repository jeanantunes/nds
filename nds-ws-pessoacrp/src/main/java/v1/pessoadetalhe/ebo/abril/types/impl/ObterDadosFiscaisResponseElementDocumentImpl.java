/*
 * An XML document type.
 * Localname: obterDadosFiscaisResponseElement
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscaisResponseElementDocument
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * A document containing one obterDadosFiscaisResponseElement(@http://abril.ebo.pessoadetalhe.v1/types/) element.
 *
 * This is a complex type.
 */
public class ObterDadosFiscaisResponseElementDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscaisResponseElementDocument
{
    
    public ObterDadosFiscaisResponseElementDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName OBTERDADOSFISCAISRESPONSEELEMENT$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "obterDadosFiscaisResponseElement");
    
    
    /**
     * Gets the "obterDadosFiscaisResponseElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscaisResponse getObterDadosFiscaisResponseElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscaisResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscaisResponse)get_store().find_element_user(OBTERDADOSFISCAISRESPONSEELEMENT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "obterDadosFiscaisResponseElement" element
     */
    public void setObterDadosFiscaisResponseElement(v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscaisResponse obterDadosFiscaisResponseElement)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscaisResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscaisResponse)get_store().find_element_user(OBTERDADOSFISCAISRESPONSEELEMENT$0, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscaisResponse)get_store().add_element_user(OBTERDADOSFISCAISRESPONSEELEMENT$0);
            }
            target.set(obterDadosFiscaisResponseElement);
        }
    }
    
    /**
     * Appends and returns a new empty "obterDadosFiscaisResponseElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscaisResponse addNewObterDadosFiscaisResponseElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscaisResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscaisResponse)get_store().add_element_user(OBTERDADOSFISCAISRESPONSEELEMENT$0);
            return target;
        }
    }
}
