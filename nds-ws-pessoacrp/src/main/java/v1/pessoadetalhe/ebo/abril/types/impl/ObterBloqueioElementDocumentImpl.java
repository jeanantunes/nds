/*
 * An XML document type.
 * Localname: obterBloqueioElement
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.ObterBloqueioElementDocument
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * A document containing one obterBloqueioElement(@http://abril.ebo.pessoadetalhe.v1/types/) element.
 *
 * This is a complex type.
 */
public class ObterBloqueioElementDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.ObterBloqueioElementDocument
{
    
    public ObterBloqueioElementDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName OBTERBLOQUEIOELEMENT$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "obterBloqueioElement");
    
    
    /**
     * Gets the "obterBloqueioElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterBloqueio getObterBloqueioElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterBloqueio target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterBloqueio)get_store().find_element_user(OBTERBLOQUEIOELEMENT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "obterBloqueioElement" element
     */
    public void setObterBloqueioElement(v1.pessoadetalhe.ebo.abril.types.ObterBloqueio obterBloqueioElement)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterBloqueio target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterBloqueio)get_store().find_element_user(OBTERBLOQUEIOELEMENT$0, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.ObterBloqueio)get_store().add_element_user(OBTERBLOQUEIOELEMENT$0);
            }
            target.set(obterBloqueioElement);
        }
    }
    
    /**
     * Appends and returns a new empty "obterBloqueioElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterBloqueio addNewObterBloqueioElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterBloqueio target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterBloqueio)get_store().add_element_user(OBTERBLOQUEIOELEMENT$0);
            return target;
        }
    }
}
