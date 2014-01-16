/*
 * XML Type:  SignedInfoType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SignedInfoType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;


/**
 * An XML SignedInfoType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public interface SignedInfoType extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(SignedInfoType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("signedinfotype54dbtype");
    
    /**
     * Gets the "CanonicalizationMethod" element
     */
    org.w3.x2000.x09.xmldsig.SignedInfoType.CanonicalizationMethod getCanonicalizationMethod();
    
    /**
     * Sets the "CanonicalizationMethod" element
     */
    void setCanonicalizationMethod(org.w3.x2000.x09.xmldsig.SignedInfoType.CanonicalizationMethod canonicalizationMethod);
    
    /**
     * Appends and returns a new empty "CanonicalizationMethod" element
     */
    org.w3.x2000.x09.xmldsig.SignedInfoType.CanonicalizationMethod addNewCanonicalizationMethod();
    
    /**
     * Gets the "SignatureMethod" element
     */
    org.w3.x2000.x09.xmldsig.SignedInfoType.SignatureMethod getSignatureMethod();
    
    /**
     * Sets the "SignatureMethod" element
     */
    void setSignatureMethod(org.w3.x2000.x09.xmldsig.SignedInfoType.SignatureMethod signatureMethod);
    
    /**
     * Appends and returns a new empty "SignatureMethod" element
     */
    org.w3.x2000.x09.xmldsig.SignedInfoType.SignatureMethod addNewSignatureMethod();
    
    /**
     * Gets the "Reference" element
     */
    org.w3.x2000.x09.xmldsig.ReferenceType getReference();
    
    /**
     * Sets the "Reference" element
     */
    void setReference(org.w3.x2000.x09.xmldsig.ReferenceType reference);
    
    /**
     * Appends and returns a new empty "Reference" element
     */
    org.w3.x2000.x09.xmldsig.ReferenceType addNewReference();
    
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
     * An XML CanonicalizationMethod(@http://www.w3.org/2000/09/xmldsig#).
     *
     * This is a complex type.
     */
    public interface CanonicalizationMethod extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(CanonicalizationMethod.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("canonicalizationmethoda908elemtype");
        
        /**
         * Gets the "Algorithm" attribute
         */
        java.lang.String getAlgorithm();
        
        /**
         * Gets (as xml) the "Algorithm" attribute
         */
        org.apache.xmlbeans.XmlAnyURI xgetAlgorithm();
        
        /**
         * Sets the "Algorithm" attribute
         */
        void setAlgorithm(java.lang.String algorithm);
        
        /**
         * Sets (as xml) the "Algorithm" attribute
         */
        void xsetAlgorithm(org.apache.xmlbeans.XmlAnyURI algorithm);
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static org.w3.x2000.x09.xmldsig.SignedInfoType.CanonicalizationMethod newInstance() {
              return (org.w3.x2000.x09.xmldsig.SignedInfoType.CanonicalizationMethod) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static org.w3.x2000.x09.xmldsig.SignedInfoType.CanonicalizationMethod newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (org.w3.x2000.x09.xmldsig.SignedInfoType.CanonicalizationMethod) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML SignatureMethod(@http://www.w3.org/2000/09/xmldsig#).
     *
     * This is a complex type.
     */
    public interface SignatureMethod extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(SignatureMethod.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("signaturemethod2460elemtype");
        
        /**
         * Gets the "Algorithm" attribute
         */
        java.lang.String getAlgorithm();
        
        /**
         * Gets (as xml) the "Algorithm" attribute
         */
        org.apache.xmlbeans.XmlAnyURI xgetAlgorithm();
        
        /**
         * Sets the "Algorithm" attribute
         */
        void setAlgorithm(java.lang.String algorithm);
        
        /**
         * Sets (as xml) the "Algorithm" attribute
         */
        void xsetAlgorithm(org.apache.xmlbeans.XmlAnyURI algorithm);
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static org.w3.x2000.x09.xmldsig.SignedInfoType.SignatureMethod newInstance() {
              return (org.w3.x2000.x09.xmldsig.SignedInfoType.SignatureMethod) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static org.w3.x2000.x09.xmldsig.SignedInfoType.SignatureMethod newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (org.w3.x2000.x09.xmldsig.SignedInfoType.SignatureMethod) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.w3.x2000.x09.xmldsig.SignedInfoType newInstance() {
          return (org.w3.x2000.x09.xmldsig.SignedInfoType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.w3.x2000.x09.xmldsig.SignedInfoType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.w3.x2000.x09.xmldsig.SignedInfoType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.w3.x2000.x09.xmldsig.SignedInfoType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.w3.x2000.x09.xmldsig.SignedInfoType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.w3.x2000.x09.xmldsig.SignedInfoType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.w3.x2000.x09.xmldsig.SignedInfoType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.w3.x2000.x09.xmldsig.SignedInfoType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.w3.x2000.x09.xmldsig.SignedInfoType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.w3.x2000.x09.xmldsig.SignedInfoType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.w3.x2000.x09.xmldsig.SignedInfoType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.w3.x2000.x09.xmldsig.SignedInfoType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.w3.x2000.x09.xmldsig.SignedInfoType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.w3.x2000.x09.xmldsig.SignedInfoType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.w3.x2000.x09.xmldsig.SignedInfoType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.w3.x2000.x09.xmldsig.SignedInfoType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.w3.x2000.x09.xmldsig.SignedInfoType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.w3.x2000.x09.xmldsig.SignedInfoType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.w3.x2000.x09.xmldsig.SignedInfoType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.w3.x2000.x09.xmldsig.SignedInfoType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.w3.x2000.x09.xmldsig.SignedInfoType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.w3.x2000.x09.xmldsig.SignedInfoType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.w3.x2000.x09.xmldsig.SignedInfoType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.w3.x2000.x09.xmldsig.SignedInfoType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.w3.x2000.x09.xmldsig.SignedInfoType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.w3.x2000.x09.xmldsig.SignedInfoType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.w3.x2000.x09.xmldsig.SignedInfoType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.w3.x2000.x09.xmldsig.SignedInfoType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.w3.x2000.x09.xmldsig.SignedInfoType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.w3.x2000.x09.xmldsig.SignedInfoType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.w3.x2000.x09.xmldsig.SignedInfoType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.w3.x2000.x09.xmldsig.SignedInfoType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.w3.x2000.x09.xmldsig.SignedInfoType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.w3.x2000.x09.xmldsig.SignedInfoType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.w3.x2000.x09.xmldsig.SignedInfoType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
