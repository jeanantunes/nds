/*
 * An XML document type.
 * Localname: obterBloqueioResponseElement
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types;


/**
 * A document containing one obterBloqueioResponseElement(@http://abril.ebo.pessoadetalhe.v1/types/) element.
 *
 * This is a complex type.
 */
public interface ObterBloqueioResponseElementDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ObterBloqueioResponseElementDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9F21EE032D2D88E156BC5787EE2C44DE").resolveHandle("obterbloqueioresponseelement3264doctype");
    
    /**
     * Gets the "obterBloqueioResponseElement" element
     */
    v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponse getObterBloqueioResponseElement();
    
    /**
     * Sets the "obterBloqueioResponseElement" element
     */
    void setObterBloqueioResponseElement(v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponse obterBloqueioResponseElement);
    
    /**
     * Appends and returns a new empty "obterBloqueioResponseElement" element
     */
    v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponse addNewObterBloqueioResponseElement();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument newInstance() {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
