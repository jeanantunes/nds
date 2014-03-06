/*
 * An XML document type.
 * Localname: obterDadosFiscaisElement
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscaisElementDocument
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * A document containing one obterDadosFiscaisElement(@http://abril.ebo.pessoadetalhe.v1/types/) element.
 *
 * This is a complex type.
 */
public class ObterDadosFiscaisElementDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscaisElementDocument
{
    
    public ObterDadosFiscaisElementDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName OBTERDADOSFISCAISELEMENT$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "obterDadosFiscaisElement");
    
    
    /**
     * Gets the "obterDadosFiscaisElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscais getObterDadosFiscaisElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscais target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscais)get_store().find_element_user(OBTERDADOSFISCAISELEMENT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "obterDadosFiscaisElement" element
     */
    public void setObterDadosFiscaisElement(v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscais obterDadosFiscaisElement)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscais target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscais)get_store().find_element_user(OBTERDADOSFISCAISELEMENT$0, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscais)get_store().add_element_user(OBTERDADOSFISCAISELEMENT$0);
            }
            target.set(obterDadosFiscaisElement);
        }
    }
    
    /**
     * Appends and returns a new empty "obterDadosFiscaisElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscais addNewObterDadosFiscaisElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscais target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscais)get_store().add_element_user(OBTERDADOSFISCAISELEMENT$0);
            return target;
        }
    }
}
