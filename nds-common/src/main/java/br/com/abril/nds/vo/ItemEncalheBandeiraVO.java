/**
 * NotaEncalheBandeiraVO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.abril.nds.vo;

import java.util.Calendar;
import java.util.Date;


public class ItemEncalheBandeiraVO  implements java.io.Serializable {
	
	private Integer produtoEdicaoId;
	
    private java.lang.Integer codPublicacao;

    private java.lang.Integer numEdicao;

    private java.lang.String anoSemanaRecolhimento;

    private java.lang.Integer qtdExemplares;

    private java.lang.Integer pacotePadrao;

    private java.lang.Integer exemplaresPacote;

    private java.lang.Integer qtdExemplaresIrregular;

    private java.lang.Integer indiceVolume;
    
    private Date dataRecolhimento;

    public ItemEncalheBandeiraVO() {
    }

    private static final long serialVersionUID = -1526643197367343770L;
	

    /**
     * Gets the codPublicacao value for this NotaEncalheBandeiraVO.
     * 
     * @return codPublicacao
     */
    public java.lang.Integer getCodPublicacao() {
        return codPublicacao;
    }


    /**
     * Sets the codPublicacao value for this NotaEncalheBandeiraVO.
     * 
     * @param codPublicacao
     */
    public void setCodPublicacao(java.lang.Integer codPublicacao) {
        this.codPublicacao = codPublicacao;
    }


    /**
     * Gets the numEdicao value for this NotaEncalheBandeiraVO.
     * 
     * @return numEdicao
     */
    public java.lang.Integer getNumEdicao() {
        return numEdicao;
    }


    /**
     * Sets the numEdicao value for this NotaEncalheBandeiraVO.
     * 
     * @param numEdicao
     */
    public void setNumEdicao(java.lang.Integer numEdicao) {
        this.numEdicao = numEdicao;
    }


    /**
     * Gets the anoSemanaRecolhimento value for this NotaEncalheBandeiraVO.
     * 
     * @return anoSemanaRecolhimento
     */
    public java.lang.String getAnoSemanaRecolhimento() {
    	
    	return this.anoSemanaRecolhimento;
    }


    /**
     * Sets the anoSemanaRecolhimento value for this NotaEncalheBandeiraVO.
     * 
     * @param anoSemanaRecolhimento
     */
    public void setAnoSemanaRecolhimento(java.lang.String anoSemanaRecolhimento) {
        this.anoSemanaRecolhimento = anoSemanaRecolhimento;
    }


    /**
     * Gets the qtdExemplares value for this NotaEncalheBandeiraVO.
     * 
     * @return qtdExemplares
     */
    public java.lang.Integer getQtdExemplares() {
        return qtdExemplares;
    }


    /**
     * Sets the qtdExemplares value for this NotaEncalheBandeiraVO.
     * 
     * @param qtdExemplares
     */
    public void setQtdExemplares(java.lang.Integer qtdExemplares) {
        this.qtdExemplares = qtdExemplares;
    }


    /**
     * Gets the pacotePadrao value for this NotaEncalheBandeiraVO.
     * 
     * @return pacotePadrao
     */
    public java.lang.Integer getPacotePadrao() {
        return pacotePadrao;
    }


    /**
     * Sets the pacotePadrao value for this NotaEncalheBandeiraVO.
     * 
     * @param pacotePadrao
     */
    public void setPacotePadrao(java.lang.Integer pacotePadrao) {
        this.pacotePadrao = pacotePadrao;
    }


    /**
     * Gets the exemplaresPacote value for this NotaEncalheBandeiraVO.
     * 
     * @return exemplaresPacote
     */
    public java.lang.Integer getExemplaresPacote() {
    	if ( qtdExemplares != null && pacotePadrao != null && pacotePadrao.intValue() > 0)
        return qtdExemplares/pacotePadrao;
    	return 1;
    }


    /**
     * Sets the exemplaresPacote value for this NotaEncalheBandeiraVO.
     * 
     * @param exemplaresPacote
     */
    public void setExemplaresPacote(java.lang.Integer exemplaresPacote) {
        this.exemplaresPacote = exemplaresPacote;
    }


    /**
     * Gets the qtdExemplaresIrregular value for this NotaEncalheBandeiraVO.
     * 
     * @return qtdExemplaresIrregular
     */
    public java.lang.Integer getQtdExemplaresIrregular() {
        return qtdExemplaresIrregular;
    }


    /**
     * Sets the qtdExemplaresIrregular value for this NotaEncalheBandeiraVO.
     * 
     * @param qtdExemplaresIrregular
     */
    public void setQtdExemplaresIrregular(java.lang.Integer qtdExemplaresIrregular) {
        this.qtdExemplaresIrregular = qtdExemplaresIrregular;
    }


    /**
     * Gets the indiceVolume value for this NotaEncalheBandeiraVO.
     * 
     * @return indiceVolume
     */
    public java.lang.Integer getIndiceVolume() {
        return indiceVolume;
    }


    /**
     * Sets the indiceVolume value for this NotaEncalheBandeiraVO.
     * 
     * @param indiceVolume
     */
    public void setIndiceVolume(java.lang.Integer indiceVolume) {
        this.indiceVolume = indiceVolume;
    }

    

    
    public Date getDataRecolhimento() {
		return dataRecolhimento;
	}


	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}


}
