/*
 * XML Type:  PessoaDto
 * Namespace: http://abril.ebo.pessoadetalhe.v1/types/
 * Java type: v1.pessoadetalhe.ebo.abril.types.PessoaDto
 *
 * Automatically generated - do not modify.
 */
package v1.pessoadetalhe.ebo.abril.types;


/**
 * An XML PessoaDto(@http://abril.ebo.pessoadetalhe.v1/types/).
 *
 * This is a complex type.
 */
public interface PessoaDto extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(PessoaDto.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9F21EE032D2D88E156BC5787EE2C44DE").resolveHandle("pessoadto77d9type");
    
    /**
     * Gets the "pessoa" element
     */
    v1.pessoadetalhe.ebo.abril.types.PessoaType getPessoa();
    
    /**
     * Tests for nil "pessoa" element
     */
    boolean isNilPessoa();
    
    /**
     * Sets the "pessoa" element
     */
    void setPessoa(v1.pessoadetalhe.ebo.abril.types.PessoaType pessoa);
    
    /**
     * Appends and returns a new empty "pessoa" element
     */
    v1.pessoadetalhe.ebo.abril.types.PessoaType addNewPessoa();
    
    /**
     * Nils the "pessoa" element
     */
    void setNilPessoa();
    
    /**
     * Gets the "pessoaDetalhe" element
     */
    v1.pessoadetalhe.ebo.abril.types.PessoaDetalheType getPessoaDetalhe();
    
    /**
     * Tests for nil "pessoaDetalhe" element
     */
    boolean isNilPessoaDetalhe();
    
    /**
     * Sets the "pessoaDetalhe" element
     */
    void setPessoaDetalhe(v1.pessoadetalhe.ebo.abril.types.PessoaDetalheType pessoaDetalhe);
    
    /**
     * Appends and returns a new empty "pessoaDetalhe" element
     */
    v1.pessoadetalhe.ebo.abril.types.PessoaDetalheType addNewPessoaDetalhe();
    
    /**
     * Nils the "pessoaDetalhe" element
     */
    void setNilPessoaDetalhe();
    
    /**
     * Gets array of all "documento" elements
     */
    v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType[] getDocumentoArray();
    
    /**
     * Gets ith "documento" element
     */
    v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType getDocumentoArray(int i);
    
    /**
     * Tests for nil ith "documento" element
     */
    boolean isNilDocumentoArray(int i);
    
    /**
     * Returns number of "documento" element
     */
    int sizeOfDocumentoArray();
    
    /**
     * Sets array of all "documento" element
     */
    void setDocumentoArray(v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType[] documentoArray);
    
    /**
     * Sets ith "documento" element
     */
    void setDocumentoArray(int i, v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType documento);
    
    /**
     * Nils the ith "documento" element
     */
    void setNilDocumentoArray(int i);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "documento" element
     */
    v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType insertNewDocumento(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "documento" element
     */
    v1.pessoadetalhe.ebo.abril.types.DocumentoPessoaType addNewDocumento();
    
    /**
     * Removes the ith "documento" element
     */
    void removeDocumento(int i);
    
    /**
     * Gets array of all "endereco" elements
     */
    v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType[] getEnderecoArray();
    
    /**
     * Gets ith "endereco" element
     */
    v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType getEnderecoArray(int i);
    
    /**
     * Tests for nil ith "endereco" element
     */
    boolean isNilEnderecoArray(int i);
    
    /**
     * Returns number of "endereco" element
     */
    int sizeOfEnderecoArray();
    
    /**
     * Sets array of all "endereco" element
     */
    void setEnderecoArray(v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType[] enderecoArray);
    
    /**
     * Sets ith "endereco" element
     */
    void setEnderecoArray(int i, v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType endereco);
    
    /**
     * Nils the ith "endereco" element
     */
    void setNilEnderecoArray(int i);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "endereco" element
     */
    v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType insertNewEndereco(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "endereco" element
     */
    v1.pessoadetalhe.ebo.abril.types.EnderecoPessoaType addNewEndereco();
    
    /**
     * Removes the ith "endereco" element
     */
    void removeEndereco(int i);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static v1.pessoadetalhe.ebo.abril.types.PessoaDto newInstance() {
          return (v1.pessoadetalhe.ebo.abril.types.PessoaDto) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PessoaDto newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (v1.pessoadetalhe.ebo.abril.types.PessoaDto) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static v1.pessoadetalhe.ebo.abril.types.PessoaDto parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.PessoaDto) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PessoaDto parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.PessoaDto) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static v1.pessoadetalhe.ebo.abril.types.PessoaDto parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.PessoaDto) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PessoaDto parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.PessoaDto) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PessoaDto parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.PessoaDto) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PessoaDto parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.PessoaDto) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PessoaDto parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.PessoaDto) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PessoaDto parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.PessoaDto) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PessoaDto parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.PessoaDto) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PessoaDto parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (v1.pessoadetalhe.ebo.abril.types.PessoaDto) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PessoaDto parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.PessoaDto) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PessoaDto parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.PessoaDto) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PessoaDto parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.PessoaDto) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static v1.pessoadetalhe.ebo.abril.types.PessoaDto parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (v1.pessoadetalhe.ebo.abril.types.PessoaDto) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static v1.pessoadetalhe.ebo.abril.types.PessoaDto parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (v1.pessoadetalhe.ebo.abril.types.PessoaDto) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static v1.pessoadetalhe.ebo.abril.types.PessoaDto parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (v1.pessoadetalhe.ebo.abril.types.PessoaDto) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
