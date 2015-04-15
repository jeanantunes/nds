/*
 * XML Type:  EnderecoPessoaINType
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types;


/**
 * An XML EnderecoPessoaINType(@http://abril.ebo.pessoadetalhe.v1/types/).
 *
 * This is a complex type.
 */
public interface EnderecoPessoaINType extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(EnderecoPessoaINType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9F21EE032D2D88E156BC5787EE2C44DE").resolveHandle("enderecopessoaintypebb70type");
    
    /**
     * Gets the "codigoLocalidade" element
     */
    java.lang.String getCodigoLocalidade();
    
    /**
     * Gets (as xml) the "codigoLocalidade" element
     */
    org.apache.xmlbeans.XmlString xgetCodigoLocalidade();
    
    /**
     * Sets the "codigoLocalidade" element
     */
    void setCodigoLocalidade(java.lang.String codigoLocalidade);
    
    /**
     * Sets (as xml) the "codigoLocalidade" element
     */
    void xsetCodigoLocalidade(org.apache.xmlbeans.XmlString codigoLocalidade);
    
    /**
     * Gets the "tipoLogradouro" element
     */
    java.lang.String getTipoLogradouro();
    
    /**
     * Gets (as xml) the "tipoLogradouro" element
     */
    org.apache.xmlbeans.XmlString xgetTipoLogradouro();
    
    /**
     * Sets the "tipoLogradouro" element
     */
    void setTipoLogradouro(java.lang.String tipoLogradouro);
    
    /**
     * Sets (as xml) the "tipoLogradouro" element
     */
    void xsetTipoLogradouro(org.apache.xmlbeans.XmlString tipoLogradouro);
    
    /**
     * Gets the "endereco" element
     */
    java.lang.String getEndereco();
    
    /**
     * Gets (as xml) the "endereco" element
     */
    org.apache.xmlbeans.XmlString xgetEndereco();
    
    /**
     * Sets the "endereco" element
     */
    void setEndereco(java.lang.String endereco);
    
    /**
     * Sets (as xml) the "endereco" element
     */
    void xsetEndereco(org.apache.xmlbeans.XmlString endereco);
    
    /**
     * Gets the "numero" element
     */
    java.lang.String getNumero();
    
    /**
     * Gets (as xml) the "numero" element
     */
    org.apache.xmlbeans.XmlString xgetNumero();
    
    /**
     * Sets the "numero" element
     */
    void setNumero(java.lang.String numero);
    
    /**
     * Sets (as xml) the "numero" element
     */
    void xsetNumero(org.apache.xmlbeans.XmlString numero);
    
    /**
     * Gets the "tipoEndereco" element
     */
    java.lang.String getTipoEndereco();
    
    /**
     * Gets (as xml) the "tipoEndereco" element
     */
    org.apache.xmlbeans.XmlString xgetTipoEndereco();
    
    /**
     * Sets the "tipoEndereco" element
     */
    void setTipoEndereco(java.lang.String tipoEndereco);
    
    /**
     * Sets (as xml) the "tipoEndereco" element
     */
    void xsetTipoEndereco(org.apache.xmlbeans.XmlString tipoEndereco);
    
    /**
     * Gets the "complemento" element
     */
    java.lang.String getComplemento();
    
    /**
     * Gets (as xml) the "complemento" element
     */
    org.apache.xmlbeans.XmlString xgetComplemento();
    
    /**
     * Tests for nil "complemento" element
     */
    boolean isNilComplemento();
    
    /**
     * Sets the "complemento" element
     */
    void setComplemento(java.lang.String complemento);
    
    /**
     * Sets (as xml) the "complemento" element
     */
    void xsetComplemento(org.apache.xmlbeans.XmlString complemento);
    
    /**
     * Nils the "complemento" element
     */
    void setNilComplemento();
    
    /**
     * Gets the "bairro" element
     */
    java.lang.String getBairro();
    
    /**
     * Gets (as xml) the "bairro" element
     */
    org.apache.xmlbeans.XmlString xgetBairro();
    
    /**
     * Sets the "bairro" element
     */
    void setBairro(java.lang.String bairro);
    
    /**
     * Sets (as xml) the "bairro" element
     */
    void xsetBairro(org.apache.xmlbeans.XmlString bairro);
    
    /**
     * Gets the "cep" element
     */
    java.lang.String getCep();
    
    /**
     * Gets (as xml) the "cep" element
     */
    org.apache.xmlbeans.XmlString xgetCep();
    
    /**
     * Sets the "cep" element
     */
    void setCep(java.lang.String cep);
    
    /**
     * Sets (as xml) the "cep" element
     */
    void xsetCep(org.apache.xmlbeans.XmlString cep);
    
    /**
     * Gets the "enderecoCompleto" element
     */
    java.lang.String getEnderecoCompleto();
    
    /**
     * Gets (as xml) the "enderecoCompleto" element
     */
    org.apache.xmlbeans.XmlString xgetEnderecoCompleto();
    
    /**
     * Tests for nil "enderecoCompleto" element
     */
    boolean isNilEnderecoCompleto();
    
    /**
     * Sets the "enderecoCompleto" element
     */
    void setEnderecoCompleto(java.lang.String enderecoCompleto);
    
    /**
     * Sets (as xml) the "enderecoCompleto" element
     */
    void xsetEnderecoCompleto(org.apache.xmlbeans.XmlString enderecoCompleto);
    
    /**
     * Nils the "enderecoCompleto" element
     */
    void setNilEnderecoCompleto();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType newInstance() {
          return (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaINType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
