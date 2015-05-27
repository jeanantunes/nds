/*
 * An XML document type.
 * Localname: adicionarAlterarClienteResponseElement
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponseElementDocument
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types.impl;
/**
 * A document containing one adicionarAlterarClienteResponseElement(@http://abril.ebo.pessoadetalhe.v1/types/) element.
 *
 * This is a complex type.
 */
public class AdicionarAlterarClienteResponseElementDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponseElementDocument
{
    
    public AdicionarAlterarClienteResponseElementDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ADICIONARALTERARCLIENTERESPONSEELEMENT$0 = 
        new javax.xml.namespace.QName("http://abril.ebo.pessoadetalhe.v1/types/", "adicionarAlterarClienteResponseElement");
    
    
    /**
     * Gets the "adicionarAlterarClienteResponseElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse getAdicionarAlterarClienteResponseElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse)get_store().find_element_user(ADICIONARALTERARCLIENTERESPONSEELEMENT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "adicionarAlterarClienteResponseElement" element
     */
    public void setAdicionarAlterarClienteResponseElement(v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse adicionarAlterarClienteResponseElement)
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse)get_store().find_element_user(ADICIONARALTERARCLIENTERESPONSEELEMENT$0, 0);
            if (target == null)
            {
                target = (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse)get_store().add_element_user(ADICIONARALTERARCLIENTERESPONSEELEMENT$0);
            }
            target.set(adicionarAlterarClienteResponseElement);
        }
    }
    
    /**
     * Appends and returns a new empty "adicionarAlterarClienteResponseElement" element
     */
    public v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse addNewAdicionarAlterarClienteResponseElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse target = null;
            target = (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse)get_store().add_element_user(ADICIONARALTERARCLIENTERESPONSEELEMENT$0);
            return target;
        }
    }
}
