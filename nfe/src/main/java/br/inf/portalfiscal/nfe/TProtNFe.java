/*
 * XML Type:  TProtNFe
 * Namespace: http://www.portalfiscal.inf.br/nfe
 * Java type: br.inf.portalfiscal.nfe.TProtNFe
 *
 * Automatically generated - do not modify.
 */
package br.inf.portalfiscal.nfe;


/**
 * An XML TProtNFe(@http://www.portalfiscal.inf.br/nfe).
 *
 * This is a complex type.
 */
public interface TProtNFe extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(TProtNFe.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("tprotnfe23bbtype");
    
    /**
     * Gets the "infProt" element
     */
    br.inf.portalfiscal.nfe.TProtNFe.InfProt getInfProt();
    
    /**
     * Sets the "infProt" element
     */
    void setInfProt(br.inf.portalfiscal.nfe.TProtNFe.InfProt infProt);
    
    /**
     * Appends and returns a new empty "infProt" element
     */
    br.inf.portalfiscal.nfe.TProtNFe.InfProt addNewInfProt();
    
    /**
     * Gets the "Signature" element
     */
    org.w3.x2000.x09.xmldsig.SignatureType getSignature();
    
    /**
     * True if has "Signature" element
     */
    boolean isSetSignature();
    
    /**
     * Sets the "Signature" element
     */
    void setSignature(org.w3.x2000.x09.xmldsig.SignatureType signature);
    
    /**
     * Appends and returns a new empty "Signature" element
     */
    org.w3.x2000.x09.xmldsig.SignatureType addNewSignature();
    
    /**
     * Unsets the "Signature" element
     */
    void unsetSignature();
    
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
     * An XML infProt(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is a complex type.
     */
    public interface InfProt extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(InfProt.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("infprote40felemtype");
        
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
         * Gets the "chNFe" element
         */
        java.lang.String getChNFe();
        
        /**
         * Gets (as xml) the "chNFe" element
         */
        br.inf.portalfiscal.nfe.TChNFe xgetChNFe();
        
        /**
         * Sets the "chNFe" element
         */
        void setChNFe(java.lang.String chNFe);
        
        /**
         * Sets (as xml) the "chNFe" element
         */
        void xsetChNFe(br.inf.portalfiscal.nfe.TChNFe chNFe);
        
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
         * Gets the "nProt" element
         */
        java.lang.String getNProt();
        
        /**
         * Gets (as xml) the "nProt" element
         */
        br.inf.portalfiscal.nfe.TProt xgetNProt();
        
        /**
         * True if has "nProt" element
         */
        boolean isSetNProt();
        
        /**
         * Sets the "nProt" element
         */
        void setNProt(java.lang.String nProt);
        
        /**
         * Sets (as xml) the "nProt" element
         */
        void xsetNProt(br.inf.portalfiscal.nfe.TProt nProt);
        
        /**
         * Unsets the "nProt" element
         */
        void unsetNProt();
        
        /**
         * Gets the "digVal" element
         */
        byte[] getDigVal();
        
        /**
         * Gets (as xml) the "digVal" element
         */
        org.w3.x2000.x09.xmldsig.DigestValueType xgetDigVal();
        
        /**
         * True if has "digVal" element
         */
        boolean isSetDigVal();
        
        /**
         * Sets the "digVal" element
         */
        void setDigVal(byte[] digVal);
        
        /**
         * Sets (as xml) the "digVal" element
         */
        void xsetDigVal(org.w3.x2000.x09.xmldsig.DigestValueType digVal);
        
        /**
         * Unsets the "digVal" element
         */
        void unsetDigVal();
        
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
         * Gets the "Id" attribute
         */
        java.lang.String getId();
        
        /**
         * Gets (as xml) the "Id" attribute
         */
        org.apache.xmlbeans.XmlID xgetId();
        
        /**
         * True if has "Id" attribute
         */
        boolean isSetId();
        
        /**
         * Sets the "Id" attribute
         */
        void setId(java.lang.String id);
        
        /**
         * Sets (as xml) the "Id" attribute
         */
        void xsetId(org.apache.xmlbeans.XmlID id);
        
        /**
         * Unsets the "Id" attribute
         */
        void unsetId();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static br.inf.portalfiscal.nfe.TProtNFe.InfProt newInstance() {
              return (br.inf.portalfiscal.nfe.TProtNFe.InfProt) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static br.inf.portalfiscal.nfe.TProtNFe.InfProt newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (br.inf.portalfiscal.nfe.TProtNFe.InfProt) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static br.inf.portalfiscal.nfe.TProtNFe newInstance() {
          return (br.inf.portalfiscal.nfe.TProtNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static br.inf.portalfiscal.nfe.TProtNFe newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (br.inf.portalfiscal.nfe.TProtNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static br.inf.portalfiscal.nfe.TProtNFe parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TProtNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TProtNFe parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TProtNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static br.inf.portalfiscal.nfe.TProtNFe parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TProtNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TProtNFe parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TProtNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TProtNFe parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TProtNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TProtNFe parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TProtNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TProtNFe parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TProtNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TProtNFe parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TProtNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TProtNFe parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TProtNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TProtNFe parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TProtNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TProtNFe parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TProtNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TProtNFe parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TProtNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TProtNFe parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TProtNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TProtNFe parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TProtNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.inf.portalfiscal.nfe.TProtNFe parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.inf.portalfiscal.nfe.TProtNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.inf.portalfiscal.nfe.TProtNFe parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.inf.portalfiscal.nfe.TProtNFe) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
