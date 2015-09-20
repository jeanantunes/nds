/**
 * ItemNotaEncalheBandeiraVO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.abril.icd.axis.vo;


public class ItemNotaEncalheBandeiraVO  implements java.io.Serializable {
    private java.lang.Integer codPublicacaoProdin;

    private java.lang.Integer numEdicaoProdin;

    private java.lang.String anoSemanaRecolhimento;

    private java.lang.Integer qtdExemplares;

    private java.lang.Integer pacotePadrao;

    private java.lang.Integer exemplaresPacote;

    private java.lang.Integer qtdExemplaresIrregular;

    private java.lang.Integer indiceVolume;

  

    private static final long serialVersionUID = -1526643197367343770L;
	
    public ItemNotaEncalheBandeiraVO(){}


    /**
     * Gets the codPublicacaoProdin value for this ItemNotaEncalheBandeiraVO.
     * 
     * @return codPublicacaoProdin
     */
    public java.lang.Integer getCodPublicacaoProdin() {
        return codPublicacaoProdin;
    }


    /**
     * Sets the codPublicacaoProdin value for this ItemNotaEncalheBandeiraVO.
     * 
     * @param codPublicacaoProdin
     */
    public void setCodPublicacaoProdin(java.lang.Integer codPublicacaoProdin) {
        this.codPublicacaoProdin = codPublicacaoProdin;
    }


    /**
     * Gets the numEdicaoProdin value for this ItemNotaEncalheBandeiraVO.
     * 
     * @return numEdicaoProdin
     */
    public java.lang.Integer getNumEdicaoProdin() {
        return numEdicaoProdin;
    }


    /**
     * Sets the numEdicaoProdin value for this ItemNotaEncalheBandeiraVO.
     * 
     * @param numEdicaoProdin
     */
    public void setNumEdicaoProdin(java.lang.Integer numEdicaoProdin) {
        this.numEdicaoProdin = numEdicaoProdin;
    }


    /**
     * Gets the anoSemanaRecolhimento value for this ItemNotaEncalheBandeiraVO.
     * 
     * @return anoSemanaRecolhimento
     */
    public java.lang.String getAnoSemanaRecolhimento() {
        return anoSemanaRecolhimento;
    }


    /**
     * Sets the anoSemanaRecolhimento value for this ItemNotaEncalheBandeiraVO.
     * 
     * @param anoSemanaRecolhimento
     */
    public void setAnoSemanaRecolhimento(java.lang.String anoSemanaRecolhimento) {
        this.anoSemanaRecolhimento = anoSemanaRecolhimento;
    }


    /**
     * Gets the qtdExemplares value for this ItemNotaEncalheBandeiraVO.
     * 
     * @return qtdExemplares
     */
    public java.lang.Integer getQtdExemplares() {
        return qtdExemplares;
    }


    /**
     * Sets the qtdExemplares value for this ItemNotaEncalheBandeiraVO.
     * 
     * @param qtdExemplares
     */
    public void setQtdExemplares(java.lang.Integer qtdExemplares) {
        this.qtdExemplares = qtdExemplares;
    }


    /**
     * Gets the pacotePadrao value for this ItemNotaEncalheBandeiraVO.
     * 
     * @return pacotePadrao
     */
    public java.lang.Integer getPacotePadrao() {
        return pacotePadrao;
    }


    /**
     * Sets the pacotePadrao value for this ItemNotaEncalheBandeiraVO.
     * 
     * @param pacotePadrao
     */
    public void setPacotePadrao(java.lang.Integer pacotePadrao) {
        this.pacotePadrao = pacotePadrao;
    }


    /**
     * Gets the exemplaresPacote value for this ItemNotaEncalheBandeiraVO.
     * 
     * @return exemplaresPacote
     */
    public java.lang.Integer getExemplaresPacote() {
        return exemplaresPacote;
    }


    /**
     * Sets the exemplaresPacote value for this ItemNotaEncalheBandeiraVO.
     * 
     * @param exemplaresPacote
     */
    public void setExemplaresPacote(java.lang.Integer exemplaresPacote) {
        this.exemplaresPacote = exemplaresPacote;
    }


    /**
     * Gets the qtdExemplaresIrregular value for this ItemNotaEncalheBandeiraVO.
     * 
     * @return qtdExemplaresIrregular
     */
    public java.lang.Integer getQtdExemplaresIrregular() {
        return qtdExemplaresIrregular;
    }


    /**
     * Sets the qtdExemplaresIrregular value for this ItemNotaEncalheBandeiraVO.
     * 
     * @param qtdExemplaresIrregular
     */
    public void setQtdExemplaresIrregular(java.lang.Integer qtdExemplaresIrregular) {
        this.qtdExemplaresIrregular = qtdExemplaresIrregular;
    }


    /**
     * Gets the indiceVolume value for this ItemNotaEncalheBandeiraVO.
     * 
     * @return indiceVolume
     */
    public java.lang.Integer getIndiceVolume() {
        return indiceVolume;
    }


    /**
     * Sets the indiceVolume value for this ItemNotaEncalheBandeiraVO.
     * 
     * @param indiceVolume
     */
    public void setIndiceVolume(java.lang.Integer indiceVolume) {
        this.indiceVolume = indiceVolume;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ItemNotaEncalheBandeiraVO)) return false;
        ItemNotaEncalheBandeiraVO other = (ItemNotaEncalheBandeiraVO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codPublicacaoProdin==null && other.getCodPublicacaoProdin()==null) || 
             (this.codPublicacaoProdin!=null &&
              this.codPublicacaoProdin.equals(other.getCodPublicacaoProdin()))) &&
            ((this.numEdicaoProdin==null && other.getNumEdicaoProdin()==null) || 
             (this.numEdicaoProdin!=null &&
              this.numEdicaoProdin.equals(other.getNumEdicaoProdin()))) &&
            ((this.anoSemanaRecolhimento==null && other.getAnoSemanaRecolhimento()==null) || 
             (this.anoSemanaRecolhimento!=null &&
              this.anoSemanaRecolhimento.equals(other.getAnoSemanaRecolhimento()))) &&
            ((this.qtdExemplares==null && other.getQtdExemplares()==null) || 
             (this.qtdExemplares!=null &&
              this.qtdExemplares.equals(other.getQtdExemplares()))) &&
            ((this.pacotePadrao==null && other.getPacotePadrao()==null) || 
             (this.pacotePadrao!=null &&
              this.pacotePadrao.equals(other.getPacotePadrao()))) &&
            ((this.exemplaresPacote==null && other.getExemplaresPacote()==null) || 
             (this.exemplaresPacote!=null &&
              this.exemplaresPacote.equals(other.getExemplaresPacote()))) &&
            ((this.qtdExemplaresIrregular==null && other.getQtdExemplaresIrregular()==null) || 
             (this.qtdExemplaresIrregular!=null &&
              this.qtdExemplaresIrregular.equals(other.getQtdExemplaresIrregular()))) &&
            ((this.indiceVolume==null && other.getIndiceVolume()==null) || 
             (this.indiceVolume!=null &&
              this.indiceVolume.equals(other.getIndiceVolume())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getCodPublicacaoProdin() != null) {
            _hashCode += getCodPublicacaoProdin().hashCode();
        }
        if (getNumEdicaoProdin() != null) {
            _hashCode += getNumEdicaoProdin().hashCode();
        }
        if (getAnoSemanaRecolhimento() != null) {
            _hashCode += getAnoSemanaRecolhimento().hashCode();
        }
        if (getQtdExemplares() != null) {
            _hashCode += getQtdExemplares().hashCode();
        }
        if (getPacotePadrao() != null) {
            _hashCode += getPacotePadrao().hashCode();
        }
        if (getExemplaresPacote() != null) {
            _hashCode += getExemplaresPacote().hashCode();
        }
        if (getQtdExemplaresIrregular() != null) {
            _hashCode += getQtdExemplaresIrregular().hashCode();
        }
        if (getIndiceVolume() != null) {
            _hashCode += getIndiceVolume().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ItemNotaEncalheBandeiraVO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://vo.axis.icd.abril.com.br", "ItemNotaEncalheBandeiraVO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codPublicacaoProdin");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codPublicacaoProdin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numEdicaoProdin");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numEdicaoProdin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anoSemanaRecolhimento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "anoSemanaRecolhimento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("qtdExemplares");
        elemField.setXmlName(new javax.xml.namespace.QName("", "qtdExemplares"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pacotePadrao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pacotePadrao"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exemplaresPacote");
        elemField.setXmlName(new javax.xml.namespace.QName("", "exemplaresPacote"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("qtdExemplaresIrregular");
        elemField.setXmlName(new javax.xml.namespace.QName("", "qtdExemplaresIrregular"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indiceVolume");
        elemField.setXmlName(new javax.xml.namespace.QName("", "indiceVolume"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
