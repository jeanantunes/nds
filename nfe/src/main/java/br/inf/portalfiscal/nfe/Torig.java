/*
 * XML Type:  Torig
 * Namespace: http://www.portalfiscal.inf.br/nfe
 * Java type: br.inf.portalfiscal.nfe.Torig
 *
 * Automatically generated - do not modify.
 */
package br.inf.portalfiscal.nfe;


/**
 * An XML Torig(@http://www.portalfiscal.inf.br/nfe).
 *
 * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.Torig.
 */
public interface Torig extends org.apache.xmlbeans.XmlString
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Torig.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("torigf1aatype");
    
    org.apache.xmlbeans.StringEnumAbstractBase enumValue();
    void set(org.apache.xmlbeans.StringEnumAbstractBase e);
    
    static final Enum X_0 = Enum.forString("0");
    static final Enum X_1 = Enum.forString("1");
    static final Enum X_2 = Enum.forString("2");
    static final Enum X_3 = Enum.forString("3");
    static final Enum X_4 = Enum.forString("4");
    static final Enum X_5 = Enum.forString("5");
    static final Enum X_6 = Enum.forString("6");
    static final Enum X_7 = Enum.forString("7");
    static final Enum X_8 = Enum.forString("8");
    
    static final int INT_X_0 = Enum.INT_X_0;
    static final int INT_X_1 = Enum.INT_X_1;
    static final int INT_X_2 = Enum.INT_X_2;
    static final int INT_X_3 = Enum.INT_X_3;
    static final int INT_X_4 = Enum.INT_X_4;
    static final int INT_X_5 = Enum.INT_X_5;
    static final int INT_X_6 = Enum.INT_X_6;
    static final int INT_X_7 = Enum.INT_X_7;
    static final int INT_X_8 = Enum.INT_X_8;
    
    /**
     * Enumeration value class for br.inf.portalfiscal.nfe.Torig.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_X_0
     * Enum.forString(s); // returns the enum value for a string
     * Enum.forInt(i); // returns the enum value for an int
     * </pre>
     * Enumeration objects are immutable singleton objects that
     * can be compared using == object equality. They have no
     * public constructor. See the constants defined within this
     * class for all the valid values.
     */
    static final class Enum extends org.apache.xmlbeans.StringEnumAbstractBase
    {
        /**
         * Returns the enum value for a string, or null if none.
         */
        public static Enum forString(java.lang.String s)
            { return (Enum)table.forString(s); }
        /**
         * Returns the enum value corresponding to an int, or null if none.
         */
        public static Enum forInt(int i)
            { return (Enum)table.forInt(i); }
        
        private Enum(java.lang.String s, int i)
            { super(s, i); }
        
        static final int INT_X_0 = 1;
        static final int INT_X_1 = 2;
        static final int INT_X_2 = 3;
        static final int INT_X_3 = 4;
        static final int INT_X_4 = 5;
        static final int INT_X_5 = 6;
        static final int INT_X_6 = 7;
        static final int INT_X_7 = 8;
        static final int INT_X_8 = 9;
        
        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table
        (
            new Enum[]
            {
                new Enum("0", INT_X_0),
                new Enum("1", INT_X_1),
                new Enum("2", INT_X_2),
                new Enum("3", INT_X_3),
                new Enum("4", INT_X_4),
                new Enum("5", INT_X_5),
                new Enum("6", INT_X_6),
                new Enum("7", INT_X_7),
                new Enum("8", INT_X_8),
            }
        );
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() { return forInt(intValue()); } 
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static br.inf.portalfiscal.nfe.Torig newValue(java.lang.Object obj) {
          return (br.inf.portalfiscal.nfe.Torig) type.newValue( obj ); }
        
        public static br.inf.portalfiscal.nfe.Torig newInstance() {
          return (br.inf.portalfiscal.nfe.Torig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static br.inf.portalfiscal.nfe.Torig newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (br.inf.portalfiscal.nfe.Torig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static br.inf.portalfiscal.nfe.Torig parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.Torig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static br.inf.portalfiscal.nfe.Torig parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.Torig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static br.inf.portalfiscal.nfe.Torig parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.Torig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static br.inf.portalfiscal.nfe.Torig parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.Torig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static br.inf.portalfiscal.nfe.Torig parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.Torig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static br.inf.portalfiscal.nfe.Torig parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.Torig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static br.inf.portalfiscal.nfe.Torig parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.Torig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static br.inf.portalfiscal.nfe.Torig parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.Torig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static br.inf.portalfiscal.nfe.Torig parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.Torig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static br.inf.portalfiscal.nfe.Torig parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.Torig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static br.inf.portalfiscal.nfe.Torig parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.Torig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static br.inf.portalfiscal.nfe.Torig parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.Torig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static br.inf.portalfiscal.nfe.Torig parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.Torig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static br.inf.portalfiscal.nfe.Torig parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.Torig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.inf.portalfiscal.nfe.Torig parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.inf.portalfiscal.nfe.Torig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.inf.portalfiscal.nfe.Torig parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.inf.portalfiscal.nfe.Torig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
