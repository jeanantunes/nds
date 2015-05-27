/*
 * XML Type:  TConsReciNFe
 * Namespace: http://www.portalfiscal.inf.br/nfe
 * Java type: br.inf.portalfiscal.nfe.TConsReciNFe
 *
 * Automatically generated - do not modify.
 */
package br.inf.portalfiscal.nfe;


/**
 * An XML TConsReciNFe(@http://www.portalfiscal.inf.br/nfe).
 *
 * This is a complex type.
 */
public interface TConsReciNFe extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(TConsReciNFe.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("tconsrecinfe453etype");
    
    /**
     * Gets the "tpAmb" element
     */
    br.inf.portalfiscal.nfe.TAmb.Enum getTpAmb();
    
    /**
     * Gets (as xml) the "tpAmb" element
     */
    br.inf.portalfiscal.nfe.TAmb xgetTpAmb();
    
    /**
     * Sets the "tpAmb" element
     */
    void setTpAmb(br.inf.portalfiscal.nfe.TAmb.Enum tpAmb);
    
    /**
     * Sets (as xml) the "tpAmb" element
     */
    void xsetTpAmb(br.inf.portalfiscal.nfe.TAmb tpAmb);
    
    /**
     * Gets the "nRec" element
     */
    java.lang.String getNRec();
    
    /**
     * Gets (as xml) the "nRec" element
     */
    br.inf.portalfiscal.nfe.TRec xgetNRec();
    
    /**
     * Sets the "nRec" element
     */
    void setNRec(java.lang.String nRec);
    
    /**
     * Sets (as xml) the "nRec" element
     */
    void xsetNRec(br.inf.portalfiscal.nfe.TRec nRec);
    
    /**
     * Gets the "versao" attribute
     */
    java.lang.String getVersao();
    
    /**
     * Gets (as xml) the "versao" attribute
     */
    br.inf.portalfiscal.nfe.TVerNFe xgetVersao();
    
    /**
     * Sets the "versao" attribute
     */
    void setVersao(java.lang.String versao);
    
    /**
     * Sets (as xml) the "versao" attribute
     */
    void xsetVersao(br.inf.portalfiscal.nfe.TVerNFe versao);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static br.inf.portalfiscal.nfe.TConsReciNFe newInstance() {
          return (br.inf.portalfiscal.nfe.TConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static br.inf.portalfiscal.nfe.TConsReciNFe newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (br.inf.portalfiscal.nfe.TConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static br.inf.portalfiscal.nfe.TConsReciNFe parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TConsReciNFe parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static br.inf.portalfiscal.nfe.TConsReciNFe parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TConsReciNFe parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TConsReciNFe parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TConsReciNFe parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TConsReciNFe parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TConsReciNFe parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TConsReciNFe parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TConsReciNFe parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TConsReciNFe parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TConsReciNFe parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TConsReciNFe parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TConsReciNFe parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.inf.portalfiscal.nfe.TConsReciNFe parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.inf.portalfiscal.nfe.TConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.inf.portalfiscal.nfe.TConsReciNFe parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.inf.portalfiscal.nfe.TConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
