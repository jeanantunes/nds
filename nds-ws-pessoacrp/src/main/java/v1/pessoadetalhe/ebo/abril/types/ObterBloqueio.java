/*
 * XML Type:  obterBloqueio
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.ObterBloqueio
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types;


/**
 * An XML obterBloqueio(@http://abril.ebo.pessoadetalhe.v1/types/).
 *
 * This is a complex type.
 */
public interface ObterBloqueio extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ObterBloqueio.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9F21EE032D2D88E156BC5787EE2C44DE").resolveHandle("obterbloqueioe201type");
    
    /**
     * Gets the "codTipoDoc" element
     */
    int getCodTipoDoc();
    
    /**
     * Gets (as xml) the "codTipoDoc" element
     */
    org.apache.xmlbeans.XmlInt xgetCodTipoDoc();
    
    /**
     * Sets the "codTipoDoc" element
     */
    void setCodTipoDoc(int codTipoDoc);
    
    /**
     * Sets (as xml) the "codTipoDoc" element
     */
    void xsetCodTipoDoc(org.apache.xmlbeans.XmlInt codTipoDoc);
    
    /**
     * Gets the "numDoc" element
     */
    java.lang.String getNumDoc();
    
    /**
     * Gets (as xml) the "numDoc" element
     */
    org.apache.xmlbeans.XmlString xgetNumDoc();
    
    /**
     * Sets the "numDoc" element
     */
    void setNumDoc(java.lang.String numDoc);
    
    /**
     * Sets (as xml) the "numDoc" element
     */
    void xsetNumDoc(org.apache.xmlbeans.XmlString numDoc);
    
    /**
     * Gets the "codSistema" element
     */
    java.lang.String getCodSistema();
    
    /**
     * Gets (as xml) the "codSistema" element
     */
    org.apache.xmlbeans.XmlString xgetCodSistema();
    
    /**
     * Sets the "codSistema" element
     */
    void setCodSistema(java.lang.String codSistema);
    
    /**
     * Sets (as xml) the "codSistema" element
     */
    void xsetCodSistema(org.apache.xmlbeans.XmlString codSistema);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueio newInstance() {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueio) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueio newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueio) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueio parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueio) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueio parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueio) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueio parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueio) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueio parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueio) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueio parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueio) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueio parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueio) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueio parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueio) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueio parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueio) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueio parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueio) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueio parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueio) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueio parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueio) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueio parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueio) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueio parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueio) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueio parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueio) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueio parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueio) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueio parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueio) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
