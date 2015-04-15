/*
 * XML Type:  TUfEmi
 * Namespace: http://www.portalfiscal.inf.br/nfe
 * Java type: br.inf.portalfiscal.nfe.TUfEmi
 *
 * Automatically generated - do not modify.
 */
package br.inf.portalfiscal.nfe;


/**
 * An XML TUfEmi(@http://www.portalfiscal.inf.br/nfe).
 *
 * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TUfEmi.
 */
public interface TUfEmi extends org.apache.xmlbeans.XmlString
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(TUfEmi.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("tufemid6b1type");
    
    org.apache.xmlbeans.StringEnumAbstractBase enumValue();
    void set(org.apache.xmlbeans.StringEnumAbstractBase e);
    
    static final Enum AC = Enum.forString("AC");
    static final Enum AL = Enum.forString("AL");
    static final Enum AM = Enum.forString("AM");
    static final Enum AP = Enum.forString("AP");
    static final Enum BA = Enum.forString("BA");
    static final Enum CE = Enum.forString("CE");
    static final Enum DF = Enum.forString("DF");
    static final Enum ES = Enum.forString("ES");
    static final Enum GO = Enum.forString("GO");
    static final Enum MA = Enum.forString("MA");
    static final Enum MG = Enum.forString("MG");
    static final Enum MS = Enum.forString("MS");
    static final Enum MT = Enum.forString("MT");
    static final Enum PA = Enum.forString("PA");
    static final Enum PB = Enum.forString("PB");
    static final Enum PE = Enum.forString("PE");
    static final Enum PI = Enum.forString("PI");
    static final Enum PR = Enum.forString("PR");
    static final Enum RJ = Enum.forString("RJ");
    static final Enum RN = Enum.forString("RN");
    static final Enum RO = Enum.forString("RO");
    static final Enum RR = Enum.forString("RR");
    static final Enum RS = Enum.forString("RS");
    static final Enum SC = Enum.forString("SC");
    static final Enum SE = Enum.forString("SE");
    static final Enum SP = Enum.forString("SP");
    static final Enum TO = Enum.forString("TO");
    
    static final int INT_AC = Enum.INT_AC;
    static final int INT_AL = Enum.INT_AL;
    static final int INT_AM = Enum.INT_AM;
    static final int INT_AP = Enum.INT_AP;
    static final int INT_BA = Enum.INT_BA;
    static final int INT_CE = Enum.INT_CE;
    static final int INT_DF = Enum.INT_DF;
    static final int INT_ES = Enum.INT_ES;
    static final int INT_GO = Enum.INT_GO;
    static final int INT_MA = Enum.INT_MA;
    static final int INT_MG = Enum.INT_MG;
    static final int INT_MS = Enum.INT_MS;
    static final int INT_MT = Enum.INT_MT;
    static final int INT_PA = Enum.INT_PA;
    static final int INT_PB = Enum.INT_PB;
    static final int INT_PE = Enum.INT_PE;
    static final int INT_PI = Enum.INT_PI;
    static final int INT_PR = Enum.INT_PR;
    static final int INT_RJ = Enum.INT_RJ;
    static final int INT_RN = Enum.INT_RN;
    static final int INT_RO = Enum.INT_RO;
    static final int INT_RR = Enum.INT_RR;
    static final int INT_RS = Enum.INT_RS;
    static final int INT_SC = Enum.INT_SC;
    static final int INT_SE = Enum.INT_SE;
    static final int INT_SP = Enum.INT_SP;
    static final int INT_TO = Enum.INT_TO;
    
    /**
     * Enumeration value class for br.inf.portalfiscal.nfe.TUfEmi.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_AC
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
        
        static final int INT_AC = 1;
        static final int INT_AL = 2;
        static final int INT_AM = 3;
        static final int INT_AP = 4;
        static final int INT_BA = 5;
        static final int INT_CE = 6;
        static final int INT_DF = 7;
        static final int INT_ES = 8;
        static final int INT_GO = 9;
        static final int INT_MA = 10;
        static final int INT_MG = 11;
        static final int INT_MS = 12;
        static final int INT_MT = 13;
        static final int INT_PA = 14;
        static final int INT_PB = 15;
        static final int INT_PE = 16;
        static final int INT_PI = 17;
        static final int INT_PR = 18;
        static final int INT_RJ = 19;
        static final int INT_RN = 20;
        static final int INT_RO = 21;
        static final int INT_RR = 22;
        static final int INT_RS = 23;
        static final int INT_SC = 24;
        static final int INT_SE = 25;
        static final int INT_SP = 26;
        static final int INT_TO = 27;
        
        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table
        (
            new Enum[]
            {
                new Enum("AC", INT_AC),
                new Enum("AL", INT_AL),
                new Enum("AM", INT_AM),
                new Enum("AP", INT_AP),
                new Enum("BA", INT_BA),
                new Enum("CE", INT_CE),
                new Enum("DF", INT_DF),
                new Enum("ES", INT_ES),
                new Enum("GO", INT_GO),
                new Enum("MA", INT_MA),
                new Enum("MG", INT_MG),
                new Enum("MS", INT_MS),
                new Enum("MT", INT_MT),
                new Enum("PA", INT_PA),
                new Enum("PB", INT_PB),
                new Enum("PE", INT_PE),
                new Enum("PI", INT_PI),
                new Enum("PR", INT_PR),
                new Enum("RJ", INT_RJ),
                new Enum("RN", INT_RN),
                new Enum("RO", INT_RO),
                new Enum("RR", INT_RR),
                new Enum("RS", INT_RS),
                new Enum("SC", INT_SC),
                new Enum("SE", INT_SE),
                new Enum("SP", INT_SP),
                new Enum("TO", INT_TO),
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
        public static br.inf.portalfiscal.nfe.TUfEmi newValue(java.lang.Object obj) {
          return (br.inf.portalfiscal.nfe.TUfEmi) type.newValue( obj ); }
        
        public static br.inf.portalfiscal.nfe.TUfEmi newInstance() {
          return (br.inf.portalfiscal.nfe.TUfEmi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static br.inf.portalfiscal.nfe.TUfEmi newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (br.inf.portalfiscal.nfe.TUfEmi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static br.inf.portalfiscal.nfe.TUfEmi parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TUfEmi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TUfEmi parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TUfEmi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static br.inf.portalfiscal.nfe.TUfEmi parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TUfEmi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TUfEmi parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TUfEmi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TUfEmi parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TUfEmi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TUfEmi parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TUfEmi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TUfEmi parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TUfEmi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TUfEmi parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TUfEmi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TUfEmi parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TUfEmi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TUfEmi parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TUfEmi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TUfEmi parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TUfEmi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TUfEmi parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TUfEmi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TUfEmi parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TUfEmi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TUfEmi parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TUfEmi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.inf.portalfiscal.nfe.TUfEmi parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.inf.portalfiscal.nfe.TUfEmi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.inf.portalfiscal.nfe.TUfEmi parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.inf.portalfiscal.nfe.TUfEmi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
