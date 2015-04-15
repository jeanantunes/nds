/*
 * An XML document type.
 * Localname: NFe
 * Namespace: http://www.portalfiscal.inf.br/nfe
 * Java type: br.inf.portalfiscal.nfe.NFeDocument
 *
 * Automatically generated - do not modify.
 */
package br.inf.portalfiscal.nfe.impl;
/**
 * A document containing one NFe(@http://www.portalfiscal.inf.br/nfe) element.
 *
 * This is a complex type.
 */
public class NFeDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.inf.portalfiscal.nfe.NFeDocument
{
    
    public NFeDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NFE$0 = 
        new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe", "NFe");
    
    
    /**
     * Gets the "NFe" element
     */
    public br.inf.portalfiscal.nfe.TNFe getNFe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TNFe target = null;
            target = (br.inf.portalfiscal.nfe.TNFe)get_store().find_element_user(NFE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "NFe" element
     */
    public void setNFe(br.inf.portalfiscal.nfe.TNFe nFe)
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TNFe target = null;
            target = (br.inf.portalfiscal.nfe.TNFe)get_store().find_element_user(NFE$0, 0);
            if (target == null)
            {
                target = (br.inf.portalfiscal.nfe.TNFe)get_store().add_element_user(NFE$0);
            }
            target.set(nFe);
        }
    }
    
    /**
     * Appends and returns a new empty "NFe" element
     */
    public br.inf.portalfiscal.nfe.TNFe addNewNFe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.inf.portalfiscal.nfe.TNFe target = null;
            target = (br.inf.portalfiscal.nfe.TNFe)get_store().add_element_user(NFE$0);
            return target;
        }
    }
}
