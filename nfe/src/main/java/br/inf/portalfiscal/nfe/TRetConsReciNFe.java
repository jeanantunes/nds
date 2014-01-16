/*
 * XML Type:  TRetConsReciNFe
 * Namespace: http://www.portalfiscal.inf.br/nfe
 * Java type: br.inf.portalfiscal.nfe.TRetConsReciNFe
 *
 * Automatically generated - do not modify.
 */
package br.inf.portalfiscal.nfe;


/**
 * An XML TRetConsReciNFe(@http://www.portalfiscal.inf.br/nfe).
 *
 * This is a complex type.
 */
public interface TRetConsReciNFe extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(TRetConsReciNFe.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("tretconsrecinfe6489type");
    
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
     * Gets the "verAplic" element
     */
    java.lang.String getVerAplic();
    
    /**
     * Gets (as xml) the "verAplic" element
     */
    br.inf.portalfiscal.nfe.TVerAplic xgetVerAplic();
    
    /**
     * Sets the "verAplic" element
     */
    void setVerAplic(java.lang.String verAplic);
    
    /**
     * Sets (as xml) the "verAplic" element
     */
    void xsetVerAplic(br.inf.portalfiscal.nfe.TVerAplic verAplic);
    
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
     * Gets the "cStat" element
     */
    java.lang.String getCStat();
    
    /**
     * Gets (as xml) the "cStat" element
     */
    br.inf.portalfiscal.nfe.TStat xgetCStat();
    
    /**
     * Sets the "cStat" element
     */
    void setCStat(java.lang.String cStat);
    
    /**
     * Sets (as xml) the "cStat" element
     */
    void xsetCStat(br.inf.portalfiscal.nfe.TStat cStat);
    
    /**
     * Gets the "xMotivo" element
     */
    java.lang.String getXMotivo();
    
    /**
     * Gets (as xml) the "xMotivo" element
     */
    br.inf.portalfiscal.nfe.TMotivo xgetXMotivo();
    
    /**
     * Sets the "xMotivo" element
     */
    void setXMotivo(java.lang.String xMotivo);
    
    /**
     * Sets (as xml) the "xMotivo" element
     */
    void xsetXMotivo(br.inf.portalfiscal.nfe.TMotivo xMotivo);
    
    /**
     * Gets the "cUF" element
     */
    br.inf.portalfiscal.nfe.TCodUfIBGE.Enum getCUF();
    
    /**
     * Gets (as xml) the "cUF" element
     */
    br.inf.portalfiscal.nfe.TCodUfIBGE xgetCUF();
    
    /**
     * Sets the "cUF" element
     */
    void setCUF(br.inf.portalfiscal.nfe.TCodUfIBGE.Enum cuf);
    
    /**
     * Sets (as xml) the "cUF" element
     */
    void xsetCUF(br.inf.portalfiscal.nfe.TCodUfIBGE cuf);
    
    /**
     * Gets the "dhRecbto" element
     */
    java.lang.String getDhRecbto();
    
    /**
     * Gets (as xml) the "dhRecbto" element
     */
    br.inf.portalfiscal.nfe.TDateTimeUTC xgetDhRecbto();
    
    /**
     * Sets the "dhRecbto" element
     */
    void setDhRecbto(java.lang.String dhRecbto);
    
    /**
     * Sets (as xml) the "dhRecbto" element
     */
    void xsetDhRecbto(br.inf.portalfiscal.nfe.TDateTimeUTC dhRecbto);
    
    /**
     * Gets the "cMsg" element
     */
    java.lang.String getCMsg();
    
    /**
     * Gets (as xml) the "cMsg" element
     */
    br.inf.portalfiscal.nfe.TRetConsReciNFe.CMsg xgetCMsg();
    
    /**
     * True if has "cMsg" element
     */
    boolean isSetCMsg();
    
    /**
     * Sets the "cMsg" element
     */
    void setCMsg(java.lang.String cMsg);
    
    /**
     * Sets (as xml) the "cMsg" element
     */
    void xsetCMsg(br.inf.portalfiscal.nfe.TRetConsReciNFe.CMsg cMsg);
    
    /**
     * Unsets the "cMsg" element
     */
    void unsetCMsg();
    
    /**
     * Gets the "xMsg" element
     */
    java.lang.String getXMsg();
    
    /**
     * Gets (as xml) the "xMsg" element
     */
    br.inf.portalfiscal.nfe.TRetConsReciNFe.XMsg xgetXMsg();
    
    /**
     * True if has "xMsg" element
     */
    boolean isSetXMsg();
    
    /**
     * Sets the "xMsg" element
     */
    void setXMsg(java.lang.String xMsg);
    
    /**
     * Sets (as xml) the "xMsg" element
     */
    void xsetXMsg(br.inf.portalfiscal.nfe.TRetConsReciNFe.XMsg xMsg);
    
    /**
     * Unsets the "xMsg" element
     */
    void unsetXMsg();
    
    /**
     * Gets array of all "protNFe" elements
     */
    br.inf.portalfiscal.nfe.TProtNFe[] getProtNFeArray();
    
    /**
     * Gets ith "protNFe" element
     */
    br.inf.portalfiscal.nfe.TProtNFe getProtNFeArray(int i);
    
    /**
     * Returns number of "protNFe" element
     */
    int sizeOfProtNFeArray();
    
    /**
     * Sets array of all "protNFe" element
     */
    void setProtNFeArray(br.inf.portalfiscal.nfe.TProtNFe[] protNFeArray);
    
    /**
     * Sets ith "protNFe" element
     */
    void setProtNFeArray(int i, br.inf.portalfiscal.nfe.TProtNFe protNFe);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "protNFe" element
     */
    br.inf.portalfiscal.nfe.TProtNFe insertNewProtNFe(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "protNFe" element
     */
    br.inf.portalfiscal.nfe.TProtNFe addNewProtNFe();
    
    /**
     * Removes the ith "protNFe" element
     */
    void removeProtNFe(int i);
    
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
     * An XML cMsg(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TRetConsReciNFe$CMsg.
     */
    public interface CMsg extends org.apache.xmlbeans.XmlString
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(CMsg.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("cmsgb80felemtype");
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static br.inf.portalfiscal.nfe.TRetConsReciNFe.CMsg newValue(java.lang.Object obj) {
              return (br.inf.portalfiscal.nfe.TRetConsReciNFe.CMsg) type.newValue( obj ); }
            
            public static br.inf.portalfiscal.nfe.TRetConsReciNFe.CMsg newInstance() {
              return (br.inf.portalfiscal.nfe.TRetConsReciNFe.CMsg) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static br.inf.portalfiscal.nfe.TRetConsReciNFe.CMsg newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (br.inf.portalfiscal.nfe.TRetConsReciNFe.CMsg) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML xMsg(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TRetConsReciNFe$XMsg.
     */
    public interface XMsg extends br.inf.portalfiscal.nfe.TString
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(XMsg.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("xmsg7524elemtype");
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static br.inf.portalfiscal.nfe.TRetConsReciNFe.XMsg newValue(java.lang.Object obj) {
              return (br.inf.portalfiscal.nfe.TRetConsReciNFe.XMsg) type.newValue( obj ); }
            
            public static br.inf.portalfiscal.nfe.TRetConsReciNFe.XMsg newInstance() {
              return (br.inf.portalfiscal.nfe.TRetConsReciNFe.XMsg) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static br.inf.portalfiscal.nfe.TRetConsReciNFe.XMsg newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (br.inf.portalfiscal.nfe.TRetConsReciNFe.XMsg) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static br.inf.portalfiscal.nfe.TRetConsReciNFe newInstance() {
          return (br.inf.portalfiscal.nfe.TRetConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static br.inf.portalfiscal.nfe.TRetConsReciNFe newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (br.inf.portalfiscal.nfe.TRetConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static br.inf.portalfiscal.nfe.TRetConsReciNFe parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TRetConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TRetConsReciNFe parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TRetConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static br.inf.portalfiscal.nfe.TRetConsReciNFe parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TRetConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TRetConsReciNFe parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TRetConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TRetConsReciNFe parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TRetConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TRetConsReciNFe parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TRetConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TRetConsReciNFe parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TRetConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TRetConsReciNFe parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TRetConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TRetConsReciNFe parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TRetConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TRetConsReciNFe parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TRetConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TRetConsReciNFe parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TRetConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TRetConsReciNFe parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TRetConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TRetConsReciNFe parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TRetConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TRetConsReciNFe parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TRetConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.inf.portalfiscal.nfe.TRetConsReciNFe parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.inf.portalfiscal.nfe.TRetConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.inf.portalfiscal.nfe.TRetConsReciNFe parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.inf.portalfiscal.nfe.TRetConsReciNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
