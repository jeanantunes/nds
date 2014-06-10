package br.com.abril.nds.client.vo;

import br.com.abril.nds.model.cadastro.GrupoProduto;

public class TipoProdutoVO {

	private String id;
	private String idNcm;
	private String codigo;
	private String descricao;
	private String codigoNCM;
	private String codigoNBM;
	private GrupoProduto grupoProduto;
	
	
	
	/**
	 * @param id
	 * @param codigo
	 * @param descricao
	 * @param codigoNCM
	 * @param codigoNBM
	 */
	public TipoProdutoVO(String id,String idNcm, String codigo, String descricao,
			String codigoNCM, String codigoNBM, GrupoProduto grupoProduto) {
		super();
		this.id = id;
		this.idNcm = idNcm;
		this.codigo = codigo;
		this.descricao = descricao;
		this.codigoNCM = codigoNCM;
		this.codigoNBM = codigoNBM;
		this.grupoProduto = grupoProduto;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}
	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	/**
	 * @return the codigoNCM
	 */
	public String getCodigoNCM() {
		return codigoNCM;
	}
	/**
	 * @param codigoNCM the codigoNCM to set
	 */
	public void setCodigoNCM(String codigoNCM) {
		this.codigoNCM = codigoNCM;
	}
	/**
	 * @return the codigoNBM
	 */
	public String getCodigoNBM() {
		return codigoNBM;
	}
	/**
	 * @param codigoNBM the codigoNBM to set
	 */
	public void setCodigoNBM(String codigoNBM) {
		this.codigoNBM = codigoNBM;
	}

	/**
	 * @return the grupoProduto
	 */
	public GrupoProduto getGrupoProduto() {
		return grupoProduto;
	}

	/**
	 * @param grupoProduto the grupoProduto to set
	 */
	public void setGrupoProduto(GrupoProduto grupoProduto) {
		this.grupoProduto = grupoProduto;
	}

	/**
	 * @return the idNcm
	 */
	public String getIdNcm() {
		return idNcm;
	}

	/**
	 * @param idNcm the idNcm to set
	 */
	public void setIdNcm(String idNcm) {
		this.idNcm = idNcm;
	}
	
}
