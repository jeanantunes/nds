/*
 * XML Type:  adicionarAlterarClienteResponse
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types;


/**
 * An XML adicionarAlterarClienteResponse(@http://abril.ebo.pessoadetalhe.v1/types/).
 *
 * This is a complex type.
 */
public interface AdicionarAlterarClienteResponse extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(AdicionarAlterarClienteResponse.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9F21EE032D2D88E156BC5787EE2C44DE").resolveHandle("adicionaralterarclienteresponse6121type");
    
    /**
     * Gets the "codigoPessoaDetalhe" element
     */
    long getCodigoPessoaDetalhe();
    
    /**
     * Gets (as xml) the "codigoPessoaDetalhe" element
     */
    org.apache.xmlbeans.XmlLong xgetCodigoPessoaDetalhe();
    
    /**
     * Sets the "codigoPessoaDetalhe" element
     */
    void setCodigoPessoaDetalhe(long codigoPessoaDetalhe);
    
    /**
     * Sets (as xml) the "codigoPessoaDetalhe" element
     */
    void xsetCodigoPessoaDetalhe(org.apache.xmlbeans.XmlLong codigoPessoaDetalhe);
    
    /**
     * Gets the "codigoErro" element
     */
    java.math.BigInteger getCodigoErro();
    
    /**
     * Gets (as xml) the "codigoErro" element
     */
    org.apache.xmlbeans.XmlInteger xgetCodigoErro();
    
    /**
     * Sets the "codigoErro" element
     */
    void setCodigoErro(java.math.BigInteger codigoErro);
    
    /**
     * Sets (as xml) the "codigoErro" element
     */
    void xsetCodigoErro(org.apache.xmlbeans.XmlInteger codigoErro);
    
    /**
     * Gets the "descricaoErro" element
     */
    java.lang.String getDescricaoErro();
    
    /**
     * Gets (as xml) the "descricaoErro" element
     */
    org.apache.xmlbeans.XmlString xgetDescricaoErro();
    
    /**
     * Tests for nil "descricaoErro" element
     */
    boolean isNilDescricaoErro();
    
    /**
     * Sets the "descricaoErro" element
     */
    void setDescricaoErro(java.lang.String descricaoErro);
    
    /**
     * Sets (as xml) the "descricaoErro" element
     */
    void xsetDescricaoErro(org.apache.xmlbeans.XmlString descricaoErro);
    
    /**
     * Nils the "descricaoErro" element
     */
    void setNilDescricaoErro();
    
    /**
     * Gets the "documentos" element
     */
    v1.pessoadetalhe.ebo.abril.types.DocumentosErro getDocumentos();
    
    /**
     * True if has "documentos" element
     */
    boolean isSetDocumentos();
    
    /**
     * Sets the "documentos" element
     */
    void setDocumentos(v1.pessoadetalhe.ebo.abril.types.DocumentosErro documentos);
    
    /**
     * Appends and returns a new empty "documentos" element
     */
    v1.pessoadetalhe.ebo.abril.types.DocumentosErro addNewDocumentos();
    
    /**
     * Unsets the "documentos" element
     */
    void unsetDocumentos();
    
    /**
     * Gets the "enderecos" element
     */
    v1.pessoadetalhe.ebo.abril.types.EnderecosErro getEnderecos();
    
    /**
     * True if has "enderecos" element
     */
    boolean isSetEnderecos();
    
    /**
     * Sets the "enderecos" element
     */
    void setEnderecos(v1.pessoadetalhe.ebo.abril.types.EnderecosErro enderecos);
    
    /**
     * Appends and returns a new empty "enderecos" element
     */
    v1.pessoadetalhe.ebo.abril.types.EnderecosErro addNewEnderecos();
    
    /**
     * Unsets the "enderecos" element
     */
    void unsetEnderecos();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse newInstance() {
          return (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponse) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
