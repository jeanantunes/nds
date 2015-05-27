/*
 * An XML document type.
 * Localname: obterContaCorrenteElement
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.ObterContaCorrenteElementDocument
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * A document containing one obterContaCorrenteElement(@http://abril.ebo.pessoadetalhe.v1/types/) element.
 *
 * This is a complex type.
 */
public class ObterContaCorrenteElementDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.ObterContaCorrenteElementDocument
{
    
    public ObterContaCorrenteElementDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName OBTERCONTACORRENTEELEMENT$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "obterContaCorrenteElement");
    
    
    /**
     * Gets the "obterContaCorrenteElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterContaCorrente getObterContaCorrenteElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterContaCorrente target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterContaCorrente)get_store().find_element_user(OBTERCONTACORRENTEELEMENT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "obterContaCorrenteElement" element
     */
    public void setObterContaCorrenteElement(v1.pessoadetalhe.ebo.abril.types.ObterContaCorrente obterContaCorrenteElement)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterContaCorrente target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterContaCorrente)get_store().find_element_user(OBTERCONTACORRENTEELEMENT$0, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.ObterContaCorrente)get_store().add_element_user(OBTERCONTACORRENTEELEMENT$0);
            }
            target.set(obterContaCorrenteElement);
        }
    }
    
    /**
     * Appends and returns a new empty "obterContaCorrenteElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.ObterContaCorrente addNewObterContaCorrenteElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.ObterContaCorrente target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.ObterContaCorrente)get_store().add_element_user(OBTERCONTACORRENTEELEMENT$0);
            return target;
        }
    }
}
