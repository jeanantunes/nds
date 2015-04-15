/*
 * XML Type:  TEndereco
 * Namespace: http://www.portalfiscal.inf.br/nfe
 * Java type: br.inf.portalfiscal.nfe.TEndereco
 *
 * Automatically generated - do not modify.
 */
package br.inf.portalfiscal.nfe;


/**
 * An XML TEndereco(@http://www.portalfiscal.inf.br/nfe).
 *
 * This is a complex type.
 */
public interface TEndereco extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(TEndereco.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("tenderecoe082type");
    
    /**
     * Gets the "xLgr" element
     */
    java.lang.String getXLgr();
    
    /**
     * Gets (as xml) the "xLgr" element
     */
    br.inf.portalfiscal.nfe.TEndereco.XLgr xgetXLgr();
    
    /**
     * Sets the "xLgr" element
     */
    void setXLgr(java.lang.String xLgr);
    
    /**
     * Sets (as xml) the "xLgr" element
     */
    void xsetXLgr(br.inf.portalfiscal.nfe.TEndereco.XLgr xLgr);
    
    /**
     * Gets the "nro" element
     */
    java.lang.String getNro();
    
    /**
     * Gets (as xml) the "nro" element
     */
    br.inf.portalfiscal.nfe.TEndereco.Nro xgetNro();
    
    /**
     * Sets the "nro" element
     */
    void setNro(java.lang.String nro);
    
    /**
     * Sets (as xml) the "nro" element
     */
    void xsetNro(br.inf.portalfiscal.nfe.TEndereco.Nro nro);
    
    /**
     * Gets the "xCpl" element
     */
    java.lang.String getXCpl();
    
    /**
     * Gets (as xml) the "xCpl" element
     */
    br.inf.portalfiscal.nfe.TEndereco.XCpl xgetXCpl();
    
    /**
     * True if has "xCpl" element
     */
    boolean isSetXCpl();
    
    /**
     * Sets the "xCpl" element
     */
    void setXCpl(java.lang.String xCpl);
    
    /**
     * Sets (as xml) the "xCpl" element
     */
    void xsetXCpl(br.inf.portalfiscal.nfe.TEndereco.XCpl xCpl);
    
    /**
     * Unsets the "xCpl" element
     */
    void unsetXCpl();
    
    /**
     * Gets the "xBairro" element
     */
    java.lang.String getXBairro();
    
    /**
     * Gets (as xml) the "xBairro" element
     */
    br.inf.portalfiscal.nfe.TEndereco.XBairro xgetXBairro();
    
    /**
     * Sets the "xBairro" element
     */
    void setXBairro(java.lang.String xBairro);
    
    /**
     * Sets (as xml) the "xBairro" element
     */
    void xsetXBairro(br.inf.portalfiscal.nfe.TEndereco.XBairro xBairro);
    
    /**
     * Gets the "cMun" element
     */
    java.lang.String getCMun();
    
    /**
     * Gets (as xml) the "cMun" element
     */
    br.inf.portalfiscal.nfe.TCodMunIBGE xgetCMun();
    
    /**
     * Sets the "cMun" element
     */
    void setCMun(java.lang.String cMun);
    
    /**
     * Sets (as xml) the "cMun" element
     */
    void xsetCMun(br.inf.portalfiscal.nfe.TCodMunIBGE cMun);
    
    /**
     * Gets the "xMun" element
     */
    java.lang.String getXMun();
    
    /**
     * Gets (as xml) the "xMun" element
     */
    br.inf.portalfiscal.nfe.TEndereco.XMun xgetXMun();
    
    /**
     * Sets the "xMun" element
     */
    void setXMun(java.lang.String xMun);
    
    /**
     * Sets (as xml) the "xMun" element
     */
    void xsetXMun(br.inf.portalfiscal.nfe.TEndereco.XMun xMun);
    
    /**
     * Gets the "UF" element
     */
    br.inf.portalfiscal.nfe.TUf.Enum getUF();
    
    /**
     * Gets (as xml) the "UF" element
     */
    br.inf.portalfiscal.nfe.TUf xgetUF();
    
    /**
     * Sets the "UF" element
     */
    void setUF(br.inf.portalfiscal.nfe.TUf.Enum uf);
    
    /**
     * Sets (as xml) the "UF" element
     */
    void xsetUF(br.inf.portalfiscal.nfe.TUf uf);
    
    /**
     * Gets the "CEP" element
     */
    java.lang.String getCEP();
    
    /**
     * Gets (as xml) the "CEP" element
     */
    br.inf.portalfiscal.nfe.TEndereco.CEP xgetCEP();
    
    /**
     * True if has "CEP" element
     */
    boolean isSetCEP();
    
    /**
     * Sets the "CEP" element
     */
    void setCEP(java.lang.String cep);
    
    /**
     * Sets (as xml) the "CEP" element
     */
    void xsetCEP(br.inf.portalfiscal.nfe.TEndereco.CEP cep);
    
    /**
     * Unsets the "CEP" element
     */
    void unsetCEP();
    
    /**
     * Gets the "cPais" element
     */
    br.inf.portalfiscal.nfe.Tpais.Enum getCPais();
    
    /**
     * Gets (as xml) the "cPais" element
     */
    br.inf.portalfiscal.nfe.Tpais xgetCPais();
    
    /**
     * True if has "cPais" element
     */
    boolean isSetCPais();
    
    /**
     * Sets the "cPais" element
     */
    void setCPais(br.inf.portalfiscal.nfe.Tpais.Enum cPais);
    
    /**
     * Sets (as xml) the "cPais" element
     */
    void xsetCPais(br.inf.portalfiscal.nfe.Tpais cPais);
    
    /**
     * Unsets the "cPais" element
     */
    void unsetCPais();
    
    /**
     * Gets the "xPais" element
     */
    java.lang.String getXPais();
    
    /**
     * Gets (as xml) the "xPais" element
     */
    br.inf.portalfiscal.nfe.TEndereco.XPais xgetXPais();
    
    /**
     * True if has "xPais" element
     */
    boolean isSetXPais();
    
    /**
     * Sets the "xPais" element
     */
    void setXPais(java.lang.String xPais);
    
    /**
     * Sets (as xml) the "xPais" element
     */
    void xsetXPais(br.inf.portalfiscal.nfe.TEndereco.XPais xPais);
    
    /**
     * Unsets the "xPais" element
     */
    void unsetXPais();
    
    /**
     * Gets the "fone" element
     */
    java.lang.String getFone();
    
    /**
     * Gets (as xml) the "fone" element
     */
    br.inf.portalfiscal.nfe.TEndereco.Fone xgetFone();
    
    /**
     * True if has "fone" element
     */
    boolean isSetFone();
    
    /**
     * Sets the "fone" element
     */
    void setFone(java.lang.String fone);
    
    /**
     * Sets (as xml) the "fone" element
     */
    void xsetFone(br.inf.portalfiscal.nfe.TEndereco.Fone fone);
    
    /**
     * Unsets the "fone" element
     */
    void unsetFone();
    
    /**
     * An XML xLgr(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TEndereco$XLgr.
     */
    public interface XLgr extends br.inf.portalfiscal.nfe.TString
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(XLgr.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("xlgr57c7elemtype");
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static br.inf.portalfiscal.nfe.TEndereco.XLgr newValue(java.lang.Object obj) {
              return (br.inf.portalfiscal.nfe.TEndereco.XLgr) type.newValue( obj ); }
            
            public static br.inf.portalfiscal.nfe.TEndereco.XLgr newInstance() {
              return (br.inf.portalfiscal.nfe.TEndereco.XLgr) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static br.inf.portalfiscal.nfe.TEndereco.XLgr newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (br.inf.portalfiscal.nfe.TEndereco.XLgr) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML nro(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TEndereco$Nro.
     */
    public interface Nro extends br.inf.portalfiscal.nfe.TString
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Nro.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("nro400belemtype");
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static br.inf.portalfiscal.nfe.TEndereco.Nro newValue(java.lang.Object obj) {
              return (br.inf.portalfiscal.nfe.TEndereco.Nro) type.newValue( obj ); }
            
            public static br.inf.portalfiscal.nfe.TEndereco.Nro newInstance() {
              return (br.inf.portalfiscal.nfe.TEndereco.Nro) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static br.inf.portalfiscal.nfe.TEndereco.Nro newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (br.inf.portalfiscal.nfe.TEndereco.Nro) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML xCpl(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TEndereco$XCpl.
     */
    public interface XCpl extends br.inf.portalfiscal.nfe.TString
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(XCpl.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("xcpl1f7felemtype");
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static br.inf.portalfiscal.nfe.TEndereco.XCpl newValue(java.lang.Object obj) {
              return (br.inf.portalfiscal.nfe.TEndereco.XCpl) type.newValue( obj ); }
            
            public static br.inf.portalfiscal.nfe.TEndereco.XCpl newInstance() {
              return (br.inf.portalfiscal.nfe.TEndereco.XCpl) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static br.inf.portalfiscal.nfe.TEndereco.XCpl newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (br.inf.portalfiscal.nfe.TEndereco.XCpl) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML xBairro(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TEndereco$XBairro.
     */
    public interface XBairro extends br.inf.portalfiscal.nfe.TString
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(XBairro.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("xbairro9c79elemtype");
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static br.inf.portalfiscal.nfe.TEndereco.XBairro newValue(java.lang.Object obj) {
              return (br.inf.portalfiscal.nfe.TEndereco.XBairro) type.newValue( obj ); }
            
            public static br.inf.portalfiscal.nfe.TEndereco.XBairro newInstance() {
              return (br.inf.portalfiscal.nfe.TEndereco.XBairro) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static br.inf.portalfiscal.nfe.TEndereco.XBairro newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (br.inf.portalfiscal.nfe.TEndereco.XBairro) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML xMun(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TEndereco$XMun.
     */
    public interface XMun extends br.inf.portalfiscal.nfe.TString
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(XMun.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("xmune278elemtype");
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static br.inf.portalfiscal.nfe.TEndereco.XMun newValue(java.lang.Object obj) {
              return (br.inf.portalfiscal.nfe.TEndereco.XMun) type.newValue( obj ); }
            
            public static br.inf.portalfiscal.nfe.TEndereco.XMun newInstance() {
              return (br.inf.portalfiscal.nfe.TEndereco.XMun) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static br.inf.portalfiscal.nfe.TEndereco.XMun newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (br.inf.portalfiscal.nfe.TEndereco.XMun) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML CEP(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TEndereco$CEP.
     */
    public interface CEP extends org.apache.xmlbeans.XmlString
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(CEP.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("cepaca8elemtype");
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static br.inf.portalfiscal.nfe.TEndereco.CEP newValue(java.lang.Object obj) {
              return (br.inf.portalfiscal.nfe.TEndereco.CEP) type.newValue( obj ); }
            
            public static br.inf.portalfiscal.nfe.TEndereco.CEP newInstance() {
              return (br.inf.portalfiscal.nfe.TEndereco.CEP) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static br.inf.portalfiscal.nfe.TEndereco.CEP newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (br.inf.portalfiscal.nfe.TEndereco.CEP) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML xPais(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TEndereco$XPais.
     */
    public interface XPais extends br.inf.portalfiscal.nfe.TString
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(XPais.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("xpais50c3elemtype");
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static br.inf.portalfiscal.nfe.TEndereco.XPais newValue(java.lang.Object obj) {
              return (br.inf.portalfiscal.nfe.TEndereco.XPais) type.newValue( obj ); }
            
            public static br.inf.portalfiscal.nfe.TEndereco.XPais newInstance() {
              return (br.inf.portalfiscal.nfe.TEndereco.XPais) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static br.inf.portalfiscal.nfe.TEndereco.XPais newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (br.inf.portalfiscal.nfe.TEndereco.XPais) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML fone(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TEndereco$Fone.
     */
    public interface Fone extends org.apache.xmlbeans.XmlString
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Fone.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("fonef426elemtype");
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static br.inf.portalfiscal.nfe.TEndereco.Fone newValue(java.lang.Object obj) {
              return (br.inf.portalfiscal.nfe.TEndereco.Fone) type.newValue( obj ); }
            
            public static br.inf.portalfiscal.nfe.TEndereco.Fone newInstance() {
              return (br.inf.portalfiscal.nfe.TEndereco.Fone) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static br.inf.portalfiscal.nfe.TEndereco.Fone newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (br.inf.portalfiscal.nfe.TEndereco.Fone) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static br.inf.portalfiscal.nfe.TEndereco newInstance() {
          return (br.inf.portalfiscal.nfe.TEndereco) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static br.inf.portalfiscal.nfe.TEndereco newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (br.inf.portalfiscal.nfe.TEndereco) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static br.inf.portalfiscal.nfe.TEndereco parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TEndereco) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TEndereco parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TEndereco) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static br.inf.portalfiscal.nfe.TEndereco parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TEndereco) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TEndereco parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TEndereco) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TEndereco parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TEndereco) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TEndereco parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TEndereco) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TEndereco parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TEndereco) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TEndereco parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TEndereco) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TEndereco parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TEndereco) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TEndereco parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TEndereco) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TEndereco parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TEndereco) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TEndereco parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TEndereco) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TEndereco parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TEndereco) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TEndereco parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TEndereco) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.inf.portalfiscal.nfe.TEndereco parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.inf.portalfiscal.nfe.TEndereco) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.inf.portalfiscal.nfe.TEndereco parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.inf.portalfiscal.nfe.TEndereco) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
