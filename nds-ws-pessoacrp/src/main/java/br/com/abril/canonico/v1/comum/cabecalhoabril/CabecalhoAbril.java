/*
 * XML Type:  CabecalhoAbril
 * Namespace: http://canonico.abril.com.br/v1/Comum/CabecalhoAbril
 * Java type: br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril
 *
 * Automatically generated - do not modify.
 */
package br.com.abril.canonico.v1.comum.cabecalhoabril;


/**
 * An XML CabecalhoAbril(@http://canonico.abril.com.br/v1/Comum/CabecalhoAbril).
 *
 * This is a complex type.
 */
public interface CabecalhoAbril extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(CabecalhoAbril.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9F21EE032D2D88E156BC5787EE2C44DE").resolveHandle("cabecalhoabril0e9ftype");
    
    /**
     * Gets the "requestMessageID" element
     */
    java.lang.String getRequestMessageID();
    
    /**
     * Gets (as xml) the "requestMessageID" element
     */
    org.apache.xmlbeans.XmlString xgetRequestMessageID();
    
    /**
     * Sets the "requestMessageID" element
     */
    void setRequestMessageID(java.lang.String requestMessageID);
    
    /**
     * Sets (as xml) the "requestMessageID" element
     */
    void xsetRequestMessageID(org.apache.xmlbeans.XmlString requestMessageID);
    
    /**
     * Gets the "sistemaOrigem" element
     */
    java.lang.String getSistemaOrigem();
    
    /**
     * Gets (as xml) the "sistemaOrigem" element
     */
    org.apache.xmlbeans.XmlString xgetSistemaOrigem();
    
    /**
     * Sets the "sistemaOrigem" element
     */
    void setSistemaOrigem(java.lang.String sistemaOrigem);
    
    /**
     * Sets (as xml) the "sistemaOrigem" element
     */
    void xsetSistemaOrigem(org.apache.xmlbeans.XmlString sistemaOrigem);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril newInstance() {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
