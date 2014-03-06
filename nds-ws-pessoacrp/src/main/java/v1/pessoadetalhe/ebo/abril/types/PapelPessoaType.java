/*
 * XML Type:  PapelPessoaType
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.PapelPessoaType
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types;


/**
 * An XML PapelPessoaType(@http://abril.ebo.pessoadetalhe.v1/types/).
 *
 * This is a complex type.
 */
public interface PapelPessoaType extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(PapelPessoaType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9F21EE032D2D88E156BC5787EE2C44DE").resolveHandle("papelpessoatype590atype");
    
    /**
     * Gets the "codigo" element
     */
    long getCodigo();
    
    /**
     * Gets (as xml) the "codigo" element
     */
    org.apache.xmlbeans.XmlLong xgetCodigo();
    
    /**
     * Tests for nil "codigo" element
     */
    boolean isNilCodigo();
    
    /**
     * Sets the "codigo" element
     */
    void setCodigo(long codigo);
    
    /**
     * Sets (as xml) the "codigo" element
     */
    void xsetCodigo(org.apache.xmlbeans.XmlLong codigo);
    
    /**
     * Nils the "codigo" element
     */
    void setNilCodigo();
    
    /**
     * Gets the "nome" element
     */
    java.lang.String getNome();
    
    /**
     * Gets (as xml) the "nome" element
     */
    org.apache.xmlbeans.XmlString xgetNome();
    
    /**
     * Tests for nil "nome" element
     */
    boolean isNilNome();
    
    /**
     * Sets the "nome" element
     */
    void setNome(java.lang.String nome);
    
    /**
     * Sets (as xml) the "nome" element
     */
    void xsetNome(org.apache.xmlbeans.XmlString nome);
    
    /**
     * Nils the "nome" element
     */
    void setNilNome();
    
    /**
     * Gets the "codigoCategoria" element
     */
    long getCodigoCategoria();
    
    /**
     * Gets (as xml) the "codigoCategoria" element
     */
    org.apache.xmlbeans.XmlLong xgetCodigoCategoria();
    
    /**
     * Tests for nil "codigoCategoria" element
     */
    boolean isNilCodigoCategoria();
    
    /**
     * Sets the "codigoCategoria" element
     */
    void setCodigoCategoria(long codigoCategoria);
    
    /**
     * Sets (as xml) the "codigoCategoria" element
     */
    void xsetCodigoCategoria(org.apache.xmlbeans.XmlLong codigoCategoria);
    
    /**
     * Nils the "codigoCategoria" element
     */
    void setNilCodigoCategoria();
    
    /**
     * Gets the "nomeCategoria" element
     */
    java.lang.String getNomeCategoria();
    
    /**
     * Gets (as xml) the "nomeCategoria" element
     */
    org.apache.xmlbeans.XmlString xgetNomeCategoria();
    
    /**
     * Tests for nil "nomeCategoria" element
     */
    boolean isNilNomeCategoria();
    
    /**
     * Sets the "nomeCategoria" element
     */
    void setNomeCategoria(java.lang.String nomeCategoria);
    
    /**
     * Sets (as xml) the "nomeCategoria" element
     */
    void xsetNomeCategoria(org.apache.xmlbeans.XmlString nomeCategoria);
    
    /**
     * Nils the "nomeCategoria" element
     */
    void setNilNomeCategoria();
    
    /**
     * Gets the "codigoPessoaDetalhe" element
     */
    long getCodigoPessoaDetalhe();
    
    /**
     * Gets (as xml) the "codigoPessoaDetalhe" element
     */
    org.apache.xmlbeans.XmlLong xgetCodigoPessoaDetalhe();
    
    /**
     * Tests for nil "codigoPessoaDetalhe" element
     */
    boolean isNilCodigoPessoaDetalhe();
    
    /**
     * Sets the "codigoPessoaDetalhe" element
     */
    void setCodigoPessoaDetalhe(long codigoPessoaDetalhe);
    
    /**
     * Sets (as xml) the "codigoPessoaDetalhe" element
     */
    void xsetCodigoPessoaDetalhe(org.apache.xmlbeans.XmlLong codigoPessoaDetalhe);
    
    /**
     * Nils the "codigoPessoaDetalhe" element
     */
    void setNilCodigoPessoaDetalhe();
    
    /**
     * Gets the "codigoErro" element
     */
    int getCodigoErro();
    
    /**
     * Gets (as xml) the "codigoErro" element
     */
    org.apache.xmlbeans.XmlInt xgetCodigoErro();
    
    /**
     * Tests for nil "codigoErro" element
     */
    boolean isNilCodigoErro();
    
    /**
     * Sets the "codigoErro" element
     */
    void setCodigoErro(int codigoErro);
    
    /**
     * Sets (as xml) the "codigoErro" element
     */
    void xsetCodigoErro(org.apache.xmlbeans.XmlInt codigoErro);
    
    /**
     * Nils the "codigoErro" element
     */
    void setNilCodigoErro();
    
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
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static v1.pessoadetalhe.ebo.abril.types.PapelPessoaType newInstance() {
          return (v1.pessoadetalhe.ebo.abril.types.PapelPessoaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PapelPessoaType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (v1.pessoadetalhe.ebo.abril.types.PapelPessoaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static v1.pessoadetalhe.ebo.abril.types.PapelPessoaType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.PapelPessoaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PapelPessoaType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.PapelPessoaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static v1.pessoadetalhe.ebo.abril.types.PapelPessoaType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.PapelPessoaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PapelPessoaType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.PapelPessoaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PapelPessoaType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.PapelPessoaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PapelPessoaType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.PapelPessoaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PapelPessoaType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.PapelPessoaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PapelPessoaType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.PapelPessoaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PapelPessoaType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.PapelPessoaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PapelPessoaType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.PapelPessoaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PapelPessoaType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.PapelPessoaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PapelPessoaType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.PapelPessoaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PapelPessoaType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.PapelPessoaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PapelPessoaType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.PapelPessoaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static v1.pessoadetalhe.ebo.abril.types.PapelPessoaType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (v1.pessoadetalhe.ebo.abril.types.PapelPessoaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static v1.pessoadetalhe.ebo.abril.types.PapelPessoaType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (v1.pessoadetalhe.ebo.abril.types.PapelPessoaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
