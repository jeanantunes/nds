package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.cadastro.TipoCobranca;

public class GeraDividaDTO implements Serializable{
		
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	private String box;
	private String rota;
	private String roteiro;
	private Integer numeroCota;
	private String nomeCota;
	private Date dataVencimento;
	private Date dataEmissao;
	private BigDecimal valor;
	private TipoCobranca tipoCobranca;
	private String suportaEmail;
	private Integer vias;
	private String nossoNumero;
	
	
	public GeraDividaDTO() {}
	
	public GeraDividaDTO(String box,String rota, String roteiro, 
						 Integer numeroCota,String nomeCota, Date dataVencimento,
						 Date dataEmissao,BigDecimal valor) {
		
		this.box = box;
		this.rota = rota;
		this.roteiro = roteiro; 
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.dataVencimento = dataVencimento;
		this.dataEmissao = dataEmissao;
		this.valor = valor;
		this.tipoCobranca = tipoCobranca;
	}
	
	
	
	/**
	 * @return the nossoNumero
	 */
	public String getNossoNumero() {
		return nossoNumero;
	}

	/**
	 * @param nossoNumero the nossoNumero to set
	 */
	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	/**
	 * @return the vias
	 */
	public Integer getVias() {
		return vias;
	}
	/**
	 * @param vias the vias to set
	 */
	public void setVias(Integer vias) {
		this.vias = vias;
	}
	/**
	 * @return the box
	 */
	public String getBox() {
		return box;
	}
	/**
	 * @param box the box to set
	 */
	public void setBox(String box) {
		this.box = box;
	}
	/**
	 * @return the rota
	 */
	public String getRota() {
		return rota;
	}
	/**
	 * @param rota the rota to set
	 */
	public void setRota(String rota) {
		this.rota = rota;
	}
	/**
	 * @return the roteiro
	 */
	public String getRoteiro() {
		return roteiro;
	}
	/**
	 * @param roteiro the roteiro to set
	 */
	public void setRoteiro(String roteiro) {
		this.roteiro = roteiro;
	}
	/**
	 * @return the numeroCota
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}
	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	/**
	 * @return the nomeCota
	 */
	public String getNomeCota() {
		return nomeCota;
	}
	/**
	 * @param nomeCota the nomeCota to set
	 */
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
	/**
	 * @return the dataVencimento
	 */
	public Date getDataVencimento() {
		return dataVencimento;
	}
	/**
	 * @param dataVencimento the dataVencimento to set
	 */
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	/**
	 * @return the dataEmissao
	 */
	public Date getDataEmissao() {
		return dataEmissao;
	}
	/**
	 * @param dataEmissao the dataEmissao to set
	 */
	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}
	/**
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return valor;
	}
	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	/**
	 * @return the tipoCobranca
	 */
	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}
	/**
	 * @param tipoCobranca the tipoCobranca to set
	 */
	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}
	/**
	 * @return the suportaEmail
	 */
	public String getSuportaEmail() {
		return suportaEmail;
	}
	/**
	 * @param suportaEmail the suportaEmail to set
	 */
	public void setSuportaEmail(String suportaEmail) {
		this.suportaEmail = suportaEmail;
	}
	
	
	 
}
