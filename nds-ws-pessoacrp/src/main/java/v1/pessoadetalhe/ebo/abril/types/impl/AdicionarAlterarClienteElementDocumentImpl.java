/*
 * An XML document type.
 * Localname: adicionarAlterarClienteElement
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteElementDocument
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * A document containing one adicionarAlterarClienteElement(@http://abril.ebo.pessoadetalhe.v1/types/) element.
 *
 * This is a complex type.
 */
public class AdicionarAlterarClienteElementDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteElementDocument
{
    
    public AdicionarAlterarClienteElementDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ADICIONARALTERARCLIENTEELEMENT$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "adicionarAlterarClienteElement");
    
    
    /**
     * Gets the "adicionarAlterarClienteElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarCliente getAdicionarAlterarClienteElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarCliente target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarCliente)get_store().find_element_user(ADICIONARALTERARCLIENTEELEMENT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "adicionarAlterarClienteElement" element
     */
    public void setAdicionarAlterarClienteElement(v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarCliente adicionarAlterarClienteElement)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarCliente target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarCliente)get_store().find_element_user(ADICIONARALTERARCLIENTEELEMENT$0, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarCliente)get_store().add_element_user(ADICIONARALTERARCLIENTEELEMENT$0);
            }
            target.set(adicionarAlterarClienteElement);
        }
    }
    
    /**
     * Appends and returns a new empty "adicionarAlterarClienteElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarCliente addNewAdicionarAlterarClienteElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarCliente target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarCliente)get_store().add_element_user(ADICIONARALTERARCLIENTEELEMENT$0);
            return target;
        }
    }
}
