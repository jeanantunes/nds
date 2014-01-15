/*
 * XML Type:  TIpi
 * Namespace: http://www.portalfiscal.inf.br/nfe
 * Java type: br.inf.portalfiscal.nfe.TIpi
 *
 * Automatically generated - do not modify.
 */
package br.inf.portalfiscal.nfe;


/**
 * An XML TIpi(@http://www.portalfiscal.inf.br/nfe).
 *
 * This is a complex type.
 */
public interface TIpi extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(TIpi.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("tipicc7ftype");
    
    /**
     * Gets the "clEnq" element
     */
    java.lang.String getClEnq();
    
    /**
     * Gets (as xml) the "clEnq" element
     */
    br.inf.portalfiscal.nfe.TIpi.ClEnq xgetClEnq();
    
    /**
     * True if has "clEnq" element
     */
    boolean isSetClEnq();
    
    /**
     * Sets the "clEnq" element
     */
    void setClEnq(java.lang.String clEnq);
    
    /**
     * Sets (as xml) the "clEnq" element
     */
    void xsetClEnq(br.inf.portalfiscal.nfe.TIpi.ClEnq clEnq);
    
    /**
     * Unsets the "clEnq" element
     */
    void unsetClEnq();
    
    /**
     * Gets the "CNPJProd" element
     */
    java.lang.String getCNPJProd();
    
    /**
     * Gets (as xml) the "CNPJProd" element
     */
    br.inf.portalfiscal.nfe.TCnpj xgetCNPJProd();
    
    /**
     * True if has "CNPJProd" element
     */
    boolean isSetCNPJProd();
    
    /**
     * Sets the "CNPJProd" element
     */
    void setCNPJProd(java.lang.String cnpjProd);
    
    /**
     * Sets (as xml) the "CNPJProd" element
     */
    void xsetCNPJProd(br.inf.portalfiscal.nfe.TCnpj cnpjProd);
    
    /**
     * Unsets the "CNPJProd" element
     */
    void unsetCNPJProd();
    
    /**
     * Gets the "cSelo" element
     */
    java.lang.String getCSelo();
    
    /**
     * Gets (as xml) the "cSelo" element
     */
    br.inf.portalfiscal.nfe.TIpi.CSelo xgetCSelo();
    
    /**
     * True if has "cSelo" element
     */
    boolean isSetCSelo();
    
    /**
     * Sets the "cSelo" element
     */
    void setCSelo(java.lang.String cSelo);
    
    /**
     * Sets (as xml) the "cSelo" element
     */
    void xsetCSelo(br.inf.portalfiscal.nfe.TIpi.CSelo cSelo);
    
    /**
     * Unsets the "cSelo" element
     */
    void unsetCSelo();
    
    /**
     * Gets the "qSelo" element
     */
    java.lang.String getQSelo();
    
    /**
     * Gets (as xml) the "qSelo" element
     */
    br.inf.portalfiscal.nfe.TIpi.QSelo xgetQSelo();
    
    /**
     * True if has "qSelo" element
     */
    boolean isSetQSelo();
    
    /**
     * Sets the "qSelo" element
     */
    void setQSelo(java.lang.String qSelo);
    
    /**
     * Sets (as xml) the "qSelo" element
     */
    void xsetQSelo(br.inf.portalfiscal.nfe.TIpi.QSelo qSelo);
    
    /**
     * Unsets the "qSelo" element
     */
    void unsetQSelo();
    
    /**
     * Gets the "cEnq" element
     */
    java.lang.String getCEnq();
    
    /**
     * Gets (as xml) the "cEnq" element
     */
    br.inf.portalfiscal.nfe.TIpi.CEnq xgetCEnq();
    
    /**
     * Sets the "cEnq" element
     */
    void setCEnq(java.lang.String cEnq);
    
    /**
     * Sets (as xml) the "cEnq" element
     */
    void xsetCEnq(br.inf.portalfiscal.nfe.TIpi.CEnq cEnq);
    
    /**
     * Gets the "IPITrib" element
     */
    br.inf.portalfiscal.nfe.TIpi.IPITrib getIPITrib();
    
    /**
     * True if has "IPITrib" element
     */
    boolean isSetIPITrib();
    
    /**
     * Sets the "IPITrib" element
     */
    void setIPITrib(br.inf.portalfiscal.nfe.TIpi.IPITrib ipiTrib);
    
    /**
     * Appends and returns a new empty "IPITrib" element
     */
    br.inf.portalfiscal.nfe.TIpi.IPITrib addNewIPITrib();
    
    /**
     * Unsets the "IPITrib" element
     */
    void unsetIPITrib();
    
    /**
     * Gets the "IPINT" element
     */
    br.inf.portalfiscal.nfe.TIpi.IPINT getIPINT();
    
    /**
     * True if has "IPINT" element
     */
    boolean isSetIPINT();
    
    /**
     * Sets the "IPINT" element
     */
    void setIPINT(br.inf.portalfiscal.nfe.TIpi.IPINT ipint);
    
    /**
     * Appends and returns a new empty "IPINT" element
     */
    br.inf.portalfiscal.nfe.TIpi.IPINT addNewIPINT();
    
    /**
     * Unsets the "IPINT" element
     */
    void unsetIPINT();
    
    /**
     * An XML clEnq(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TIpi$ClEnq.
     */
    public interface ClEnq extends br.inf.portalfiscal.nfe.TString
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ClEnq.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("clenqbceaelemtype");
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static br.inf.portalfiscal.nfe.TIpi.ClEnq newValue(java.lang.Object obj) {
              return (br.inf.portalfiscal.nfe.TIpi.ClEnq) type.newValue( obj ); }
            
            public static br.inf.portalfiscal.nfe.TIpi.ClEnq newInstance() {
              return (br.inf.portalfiscal.nfe.TIpi.ClEnq) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static br.inf.portalfiscal.nfe.TIpi.ClEnq newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (br.inf.portalfiscal.nfe.TIpi.ClEnq) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML cSelo(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TIpi$CSelo.
     */
    public interface CSelo extends br.inf.portalfiscal.nfe.TString
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(CSelo.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("cselo8843elemtype");
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static br.inf.portalfiscal.nfe.TIpi.CSelo newValue(java.lang.Object obj) {
              return (br.inf.portalfiscal.nfe.TIpi.CSelo) type.newValue( obj ); }
            
            public static br.inf.portalfiscal.nfe.TIpi.CSelo newInstance() {
              return (br.inf.portalfiscal.nfe.TIpi.CSelo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static br.inf.portalfiscal.nfe.TIpi.CSelo newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (br.inf.portalfiscal.nfe.TIpi.CSelo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML qSelo(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TIpi$QSelo.
     */
    public interface QSelo extends org.apache.xmlbeans.XmlString
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(QSelo.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("qselo9fd1elemtype");
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static br.inf.portalfiscal.nfe.TIpi.QSelo newValue(java.lang.Object obj) {
              return (br.inf.portalfiscal.nfe.TIpi.QSelo) type.newValue( obj ); }
            
            public static br.inf.portalfiscal.nfe.TIpi.QSelo newInstance() {
              return (br.inf.portalfiscal.nfe.TIpi.QSelo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static br.inf.portalfiscal.nfe.TIpi.QSelo newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (br.inf.portalfiscal.nfe.TIpi.QSelo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML cEnq(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TIpi$CEnq.
     */
    public interface CEnq extends br.inf.portalfiscal.nfe.TString
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(CEnq.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("cenqa160elemtype");
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static br.inf.portalfiscal.nfe.TIpi.CEnq newValue(java.lang.Object obj) {
              return (br.inf.portalfiscal.nfe.TIpi.CEnq) type.newValue( obj ); }
            
            public static br.inf.portalfiscal.nfe.TIpi.CEnq newInstance() {
              return (br.inf.portalfiscal.nfe.TIpi.CEnq) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static br.inf.portalfiscal.nfe.TIpi.CEnq newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (br.inf.portalfiscal.nfe.TIpi.CEnq) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML IPITrib(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is a complex type.
     */
    public interface IPITrib extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(IPITrib.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("ipitrib3de4elemtype");
        
        /**
         * Gets the "CST" element
         */
        br.inf.portalfiscal.nfe.TIpi.IPITrib.CST.Enum getCST();
        
        /**
         * Gets (as xml) the "CST" element
         */
        br.inf.portalfiscal.nfe.TIpi.IPITrib.CST xgetCST();
        
        /**
         * Sets the "CST" element
         */
        void setCST(br.inf.portalfiscal.nfe.TIpi.IPITrib.CST.Enum cst);
        
        /**
         * Sets (as xml) the "CST" element
         */
        void xsetCST(br.inf.portalfiscal.nfe.TIpi.IPITrib.CST cst);
        
        /**
         * Gets the "vBC" element
         */
        java.lang.String getVBC();
        
        /**
         * Gets (as xml) the "vBC" element
         */
        br.inf.portalfiscal.nfe.TDec1302 xgetVBC();
        
        /**
         * True if has "vBC" element
         */
        boolean isSetVBC();
        
        /**
         * Sets the "vBC" element
         */
        void setVBC(java.lang.String vbc);
        
        /**
         * Sets (as xml) the "vBC" element
         */
        void xsetVBC(br.inf.portalfiscal.nfe.TDec1302 vbc);
        
        /**
         * Unsets the "vBC" element
         */
        void unsetVBC();
        
        /**
         * Gets the "pIPI" element
         */
        java.lang.String getPIPI();
        
        /**
         * Gets (as xml) the "pIPI" element
         */
        br.inf.portalfiscal.nfe.TDec0302A04 xgetPIPI();
        
        /**
         * True if has "pIPI" element
         */
        boolean isSetPIPI();
        
        /**
         * Sets the "pIPI" element
         */
        void setPIPI(java.lang.String pipi);
        
        /**
         * Sets (as xml) the "pIPI" element
         */
        void xsetPIPI(br.inf.portalfiscal.nfe.TDec0302A04 pipi);
        
        /**
         * Unsets the "pIPI" element
         */
        void unsetPIPI();
        
        /**
         * Gets the "qUnid" element
         */
        java.lang.String getQUnid();
        
        /**
         * Gets (as xml) the "qUnid" element
         */
        br.inf.portalfiscal.nfe.TDec1204V xgetQUnid();
        
        /**
         * True if has "qUnid" element
         */
        boolean isSetQUnid();
        
        /**
         * Sets the "qUnid" element
         */
        void setQUnid(java.lang.String qUnid);
        
        /**
         * Sets (as xml) the "qUnid" element
         */
        void xsetQUnid(br.inf.portalfiscal.nfe.TDec1204V qUnid);
        
        /**
         * Unsets the "qUnid" element
         */
        void unsetQUnid();
        
        /**
         * Gets the "vUnid" element
         */
        java.lang.String getVUnid();
        
        /**
         * Gets (as xml) the "vUnid" element
         */
        br.inf.portalfiscal.nfe.TDec1104 xgetVUnid();
        
        /**
         * True if has "vUnid" element
         */
        boolean isSetVUnid();
        
        /**
         * Sets the "vUnid" element
         */
        void setVUnid(java.lang.String vUnid);
        
        /**
         * Sets (as xml) the "vUnid" element
         */
        void xsetVUnid(br.inf.portalfiscal.nfe.TDec1104 vUnid);
        
        /**
         * Unsets the "vUnid" element
         */
        void unsetVUnid();
        
        /**
         * Gets the "vIPI" element
         */
        java.lang.String getVIPI();
        
        /**
         * Gets (as xml) the "vIPI" element
         */
        br.inf.portalfiscal.nfe.TDec1302 xgetVIPI();
        
        /**
         * Sets the "vIPI" element
         */
        void setVIPI(java.lang.String vipi);
        
        /**
         * Sets (as xml) the "vIPI" element
         */
        void xsetVIPI(br.inf.portalfiscal.nfe.TDec1302 vipi);
        
        /**
         * An XML CST(@http://www.portalfiscal.inf.br/nfe).
         *
         * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TIpi$IPITrib$CST.
         */
        public interface CST extends org.apache.xmlbeans.XmlString
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(CST.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("cstcc34elemtype");
            
            org.apache.xmlbeans.StringEnumAbstractBase enumValue();
            void set(org.apache.xmlbeans.StringEnumAbstractBase e);
            
            static final Enum X_00 = Enum.forString("00");
            static final Enum X_49 = Enum.forString("49");
            static final Enum X_50 = Enum.forString("50");
            static final Enum X_99 = Enum.forString("99");
            
            static final int INT_X_00 = Enum.INT_X_00;
            static final int INT_X_49 = Enum.INT_X_49;
            static final int INT_X_50 = Enum.INT_X_50;
            static final int INT_X_99 = Enum.INT_X_99;
            
            /**
             * Enumeration value class for br.inf.portalfiscal.nfe.TIpi$IPITrib$CST.
             * These enum values can be used as follows:
             * <pre>
             * enum.toString(); // returns the string value of the enum
             * enum.intValue(); // returns an int value, useful for switches
             * // e.g., case Enum.INT_X_00
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
                
                static final int INT_X_00 = 1;
                static final int INT_X_49 = 2;
                static final int INT_X_50 = 3;
                static final int INT_X_99 = 4;
                
                public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
                    new org.apache.xmlbeans.StringEnumAbstractBase.Table
                (
                    new Enum[]
                    {
                      new Enum("00", INT_X_00),
                      new Enum("49", INT_X_49),
                      new Enum("50", INT_X_50),
                      new Enum("99", INT_X_99),
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
                public static br.inf.portalfiscal.nfe.TIpi.IPITrib.CST newValue(java.lang.Object obj) {
                  return (br.inf.portalfiscal.nfe.TIpi.IPITrib.CST) type.newValue( obj ); }
                
                public static br.inf.portalfiscal.nfe.TIpi.IPITrib.CST newInstance() {
                  return (br.inf.portalfiscal.nfe.TIpi.IPITrib.CST) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static br.inf.portalfiscal.nfe.TIpi.IPITrib.CST newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (br.inf.portalfiscal.nfe.TIpi.IPITrib.CST) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static br.inf.portalfiscal.nfe.TIpi.IPITrib newInstance() {
              return (br.inf.portalfiscal.nfe.TIpi.IPITrib) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static br.inf.portalfiscal.nfe.TIpi.IPITrib newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (br.inf.portalfiscal.nfe.TIpi.IPITrib) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML IPINT(@http://www.portalfiscal.inf.br/nfe).
     *
     * This is a complex type.
     */
    public interface IPINT extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(IPINT.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("ipintaf33elemtype");
        
        /**
         * Gets the "CST" element
         */
        br.inf.portalfiscal.nfe.TIpi.IPINT.CST.Enum getCST();
        
        /**
         * Gets (as xml) the "CST" element
         */
        br.inf.portalfiscal.nfe.TIpi.IPINT.CST xgetCST();
        
        /**
         * Sets the "CST" element
         */
        void setCST(br.inf.portalfiscal.nfe.TIpi.IPINT.CST.Enum cst);
        
        /**
         * Sets (as xml) the "CST" element
         */
        void xsetCST(br.inf.portalfiscal.nfe.TIpi.IPINT.CST cst);
        
        /**
         * An XML CST(@http://www.portalfiscal.inf.br/nfe).
         *
         * This is an atomic type that is a restriction of br.inf.portalfiscal.nfe.TIpi$IPINT$CST.
         */
        public interface CST extends org.apache.xmlbeans.XmlString
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(CST.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s82BAE66DFF11F95F8CB4DEAB8E4975C8").resolveHandle("cst9183elemtype");
            
            org.apache.xmlbeans.StringEnumAbstractBase enumValue();
            void set(org.apache.xmlbeans.StringEnumAbstractBase e);
            
            static final Enum X_01 = Enum.forString("01");
            static final Enum X_02 = Enum.forString("02");
            static final Enum X_03 = Enum.forString("03");
            static final Enum X_04 = Enum.forString("04");
            static final Enum X_05 = Enum.forString("05");
            static final Enum X_51 = Enum.forString("51");
            static final Enum X_52 = Enum.forString("52");
            static final Enum X_53 = Enum.forString("53");
            static final Enum X_54 = Enum.forString("54");
            static final Enum X_55 = Enum.forString("55");
            
            static final int INT_X_01 = Enum.INT_X_01;
            static final int INT_X_02 = Enum.INT_X_02;
            static final int INT_X_03 = Enum.INT_X_03;
            static final int INT_X_04 = Enum.INT_X_04;
            static final int INT_X_05 = Enum.INT_X_05;
            static final int INT_X_51 = Enum.INT_X_51;
            static final int INT_X_52 = Enum.INT_X_52;
            static final int INT_X_53 = Enum.INT_X_53;
            static final int INT_X_54 = Enum.INT_X_54;
            static final int INT_X_55 = Enum.INT_X_55;
            
            /**
             * Enumeration value class for br.inf.portalfiscal.nfe.TIpi$IPINT$CST.
             * These enum values can be used as follows:
             * <pre>
             * enum.toString(); // returns the string value of the enum
             * enum.intValue(); // returns an int value, useful for switches
             * // e.g., case Enum.INT_X_01
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
                
                static final int INT_X_01 = 1;
                static final int INT_X_02 = 2;
                static final int INT_X_03 = 3;
                static final int INT_X_04 = 4;
                static final int INT_X_05 = 5;
                static final int INT_X_51 = 6;
                static final int INT_X_52 = 7;
                static final int INT_X_53 = 8;
                static final int INT_X_54 = 9;
                static final int INT_X_55 = 10;
                
                public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
                    new org.apache.xmlbeans.StringEnumAbstractBase.Table
                (
                    new Enum[]
                    {
                      new Enum("01", INT_X_01),
                      new Enum("02", INT_X_02),
                      new Enum("03", INT_X_03),
                      new Enum("04", INT_X_04),
                      new Enum("05", INT_X_05),
                      new Enum("51", INT_X_51),
                      new Enum("52", INT_X_52),
                      new Enum("53", INT_X_53),
                      new Enum("54", INT_X_54),
                      new Enum("55", INT_X_55),
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
                public static br.inf.portalfiscal.nfe.TIpi.IPINT.CST newValue(java.lang.Object obj) {
                  return (br.inf.portalfiscal.nfe.TIpi.IPINT.CST) type.newValue( obj ); }
                
                public static br.inf.portalfiscal.nfe.TIpi.IPINT.CST newInstance() {
                  return (br.inf.portalfiscal.nfe.TIpi.IPINT.CST) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static br.inf.portalfiscal.nfe.TIpi.IPINT.CST newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (br.inf.portalfiscal.nfe.TIpi.IPINT.CST) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static br.inf.portalfiscal.nfe.TIpi.IPINT newInstance() {
              return (br.inf.portalfiscal.nfe.TIpi.IPINT) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static br.inf.portalfiscal.nfe.TIpi.IPINT newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (br.inf.portalfiscal.nfe.TIpi.IPINT) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static br.inf.portalfiscal.nfe.TIpi newInstance() {
          return (br.inf.portalfiscal.nfe.TIpi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static br.inf.portalfiscal.nfe.TIpi newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (br.inf.portalfiscal.nfe.TIpi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static br.inf.portalfiscal.nfe.TIpi parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TIpi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TIpi parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TIpi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static br.inf.portalfiscal.nfe.TIpi parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TIpi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TIpi parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TIpi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TIpi parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TIpi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TIpi parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TIpi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TIpi parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TIpi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TIpi parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TIpi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TIpi parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TIpi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TIpi parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.inf.portalfiscal.nfe.TIpi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TIpi parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TIpi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TIpi parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TIpi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static br.inf.portalfiscal.nfe.TIpi parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TIpi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static br.inf.portalfiscal.nfe.TIpi parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.inf.portalfiscal.nfe.TIpi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.inf.portalfiscal.nfe.TIpi parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.inf.portalfiscal.nfe.TIpi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.inf.portalfiscal.nfe.TIpi parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.inf.portalfiscal.nfe.TIpi) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
