/*
 * An XML document type.
 * Localname: obterContratanteElement
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.ObterContratanteElementDocument
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * A document containing one obterContratanteElement(@http://abril.ebo.pessoadetalhe.v1/types/) element.
 *
 * This is a complex type.
 */
public class ObterContratanteElementDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.ObterContratanteElementDocument
{
    
    public ObterContratanteElementDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName OBTERCONTRATANTEELEMENT$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "obterContratanteElement");
    
    
    /**
     * Gets the "obterContratanteElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterContratante getObterContratanteElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterContratante target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterContratante)get_store().find_element_user(OBTERCONTRATANTEELEMENT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "obterContratanteElement" element
     */
    public void setObterContratanteElement(v1.pessoadetalhe.ebo.abril.types.ObterContratante obterContratanteElement)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterContratante target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterContratante)get_store().find_element_user(OBTERCONTRATANTEELEMENT$0, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.ObterContratante)get_store().add_element_user(OBTERCONTRATANTEELEMENT$0);
            }
            target.set(obterContratanteElement);
        }
    }
    
    /**
     * Appends and returns a new empty "obterContratanteElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterContratante addNewObterContratanteElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterContratante target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterContratante)get_store().add_element_user(OBTERCONTRATANTEELEMENT$0);
            return target;
        }
    }
}
