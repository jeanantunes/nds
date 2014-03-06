/*
 * An XML document type.
 * Localname: cabecalhoAbril
 * Namespace: http://canonico.abril.com.br/v1/Comum/CabecalhoAbril
 * Java type: br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument
 *
 * Automatically generated - do not modify.
 */
package br.com.abril.canonico.v1.comum.cabecalhoabril;


/**
 * A document containing one cabecalhoAbril(@http://canonico.abril.com.br/v1/Comum/CabecalhoAbril) element.
 *
 * This is a complex type.
 */
public interface CabecalhoAbrilDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(CabecalhoAbrilDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9F21EE032D2D88E156BC5787EE2C44DE").resolveHandle("cabecalhoabril8eafdoctype");
    
    /**
     * Gets the "cabecalhoAbril" element
     */
    br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril getCabecalhoAbril();
    
    /**
     * Sets the "cabecalhoAbril" element
     */
    void setCabecalhoAbril(br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril cabecalhoAbril);
    
    /**
     * Appends and returns a new empty "cabecalhoAbril" element
     */
    br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril addNewCabecalhoAbril();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument newInstance() {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
