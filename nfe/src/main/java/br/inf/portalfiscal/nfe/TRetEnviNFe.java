/*
 * XML Type:  TRetEnviNFe
 * Namespace: http://www.portalfiscal.inf.br/nfe
 * Java type: br.inf.portalfiscal.nfe.TRetEnviNFe
 *
 * Automatically generated - do not modify.
 */
package br.inf.portalfiscal.nfe;


/**
 * An XML TRetEnviNFe(@http://www.portalfiscal.inf.br/nfe).
 *
 * This is a complex type.
 */
public interface TRetEnviNFe extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(TRetEnviNFe.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("tretenvinfea27btype");
    
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
     * Gets the "infRec" element
     */
    br.inf.portalfiscal.nfe.TRetEnviNFe.InfRec getInfRec();
    
    /**
     * True if has "infRec" element
     */
    boolean isSetInfRec();
    
    /**
     * Sets the "infRec" element
     */
    void setInfRec(br.inf.portalfiscal.nfe.TRetEnviNFe.InfRec infRec);
    
    /**
     * Appends and returns a new empty "infRec" element
     */
    br.inf.portalfiscal.nfe.TRetEnviNFe.InfRec addNewInfRec();
    
    /**
     * Unsets the "infRec" element
     */
    void unsetInfRec();
    
    /**
     * Gets the "protNFe" element
     */
    br.inf.portalfiscal.nfe.TProtNFe getProtNFe();
    
    /**
     * True if has "protNFe" element
     */
    boolean isSetProtNFe();
    
    /**
     * Sets the "protNFe" element
     */
    void setProtNFe(br.inf.portalfiscal.nfe.TProtNFe protNFe);
    
    /**
     * Appends and returns a new empty "protNFe" element
     */
    br.inf.portalfiscal.nfe.TProtNFe addNewProtNFe();
    
    /**
     * Unsets the "protNFe" element
     */
    void unsetProtNFe();
    
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
     * An XML infRec(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is a complex type.
     */
    public interface InfRec extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(InfRec.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("infrecee50elemtype");
        
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
         * Gets the "tMed" element
         */
        java.lang.String getTMed();
        
        /**
         * Gets (as xml) the "tMed" element
         */
        br.inf.portalfiscal.nfe.TMed xgetTMed();
        
        /**
         * Sets the "tMed" element
         */
        void setTMed(java.lang.String tMed);
        
        /**
         * Sets (as xml) the "tMed" element
         */
        void xsetTMed(br.inf.portalfiscal.nfe.TMed tMed);
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static br.inf.portalfiscal.nfe.TRetEnviNFe.InfRec newInstance() {
              return (br.inf.portalfiscal.nfe.TRetEnviNFe.InfRec) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static br.inf.portalfiscal.nfe.TRetEnviNFe.InfRec newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (br.inf.portalfiscal.nfe.TRetEnviNFe.InfRec) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static br.inf.portalfiscal.nfe.TRetEnviNFe newInstance() {
          return (br.inf.portalfiscal.nfe.TRetEnviNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static br.inf.portalfiscal.nfe.TRetEnviNFe newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (br.inf.portalfiscal.nfe.TRetEnviNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static br.inf.portalfiscal.nfe.TRetEnviNFe parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TRetEnviNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TRetEnviNFe parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TRetEnviNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static br.inf.portalfiscal.nfe.TRetEnviNFe parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TRetEnviNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TRetEnviNFe parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TRetEnviNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TRetEnviNFe parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TRetEnviNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TRetEnviNFe parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TRetEnviNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TRetEnviNFe parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TRetEnviNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TRetEnviNFe parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TRetEnviNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TRetEnviNFe parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TRetEnviNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TRetEnviNFe parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TRetEnviNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TRetEnviNFe parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TRetEnviNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TRetEnviNFe parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TRetEnviNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TRetEnviNFe parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TRetEnviNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TRetEnviNFe parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TRetEnviNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.inf.portalfiscal.nfe.TRetEnviNFe parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.inf.portalfiscal.nfe.TRetEnviNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.inf.portalfiscal.nfe.TRetEnviNFe parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.inf.portalfiscal.nfe.TRetEnviNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
