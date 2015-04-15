/*
 * An XML document type.
 * Localname: cabecalhoAbril
 * Namespace: http://canonico.abril.com.br/v1/Comum/CabecalhoAbril
 * Java type: br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument
 *
 * Automatically generated - do not modify.
 */
package br.com.abril.canonico.v1.comum.cabecalhoabril.impl;
/**
 * A document containing one cabecalhoAbril(@http://canonico.abril.com.br/v1/Comum/CabecalhoAbril) element.
 *
 * This is a complex type.
 */
public class CabecalhoAbrilDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument
{
    
    public CabecalhoAbrilDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CABECALHOABRIL$0 = 
        new javax.xml.namespace.QName("http://canonico.abril.com.br/v1/Comum/CabecalhoAbril", "cabecalhoAbril");
    
    
    /**
     * Gets the "cabecalhoAbril" element
     */
    public br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril getCabecalhoAbril()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril target = null;
            target = (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril)get_store().find_element_user(CABECALHOABRIL$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "cabecalhoAbril" element
     */
    public void setCabecalhoAbril(br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril cabecalhoAbril)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril target = null;
            target = (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril)get_store().find_element_user(CABECALHOABRIL$0, 0);
            if (target == null)
            {
                target = (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril)get_store().add_element_user(CABECALHOABRIL$0);
            }
            target.set(cabecalhoAbril);
        }
    }
    
    /**
     * Appends and returns a new empty "cabecalhoAbril" element
     */
    public br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril addNewCabecalhoAbril()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril target = null;
            target = (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril)get_store().add_element_user(CABECALHOABRIL$0);
            return target;
        }
    }
}
