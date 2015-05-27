/*
 * An XML document type.
 * Localname: obterPapelElement
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.ObterPapelElementDocument
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * A document containing one obterPapelElement(@http://abril.ebo.pessoadetalhe.v1/types/) element.
 *
 * This is a complex type.
 */
public class ObterPapelElementDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.ObterPapelElementDocument
{
    
    public ObterPapelElementDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName OBTERPAPELELEMENT$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "obterPapelElement");
    
    
    /**
     * Gets the "obterPapelElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterPapel getObterPapelElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterPapel target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterPapel)get_store().find_element_user(OBTERPAPELELEMENT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "obterPapelElement" element
     */
    public void setObterPapelElement(v1.pessoadetalhe.ebo.abril.types.ObterPapel obterPapelElement)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterPapel target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterPapel)get_store().find_element_user(OBTERPAPELELEMENT$0, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.ObterPapel)get_store().add_element_user(OBTERPAPELELEMENT$0);
            }
            target.set(obterPapelElement);
        }
    }
    
    /**
     * Appends and returns a new empty "obterPapelElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterPapel addNewObterPapelElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterPapel target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterPapel)get_store().add_element_user(OBTERPAPELELEMENT$0);
            return target;
        }
    }
}
