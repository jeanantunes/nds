package br.com.abril.nds.client.vo;

import java.io.Serializable;

public class PdvVO implements Serializable{
	
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private Long idPdv;
	
	private Long idCota;
	
	private String nomePdv;
	
	private String tipoPonto;
	
	private String contato;
	
	private String telefone;
	
	private String endereco;
	
	private String bairro;
	
	private String cidade;

	public PdvVO() {}
	
	public PdvVO(int id,Long idPdv,Long idCota,String nomePdv,String tipoPonto,
			String contato,String telefone,String endereco,String bairro,String cidade) {
		
		this.id = id;
		this.idPdv = idPdv;
		this.idCota = idCota;
		this.nomePdv = nomePdv;
		this.tipoPonto = tipoPonto;
		this.contato = contato;
		this.telefone = telefone;
		this.endereco = endereco;
		this.bairro = bairro;
		this.cidade = cidade;
	}
	
	/**
	 * @return the nomePdv
	 */
	public String getNomePdv() {
		return nomePdv;
	}

	/**
	 * @param nomePdv the nomePdv to set
	 */
	public void setNomePdv(String nomePdv) {
		this.nomePdv = nomePdv;
	}

	/**
	 * @return the tipoPonto
	 */
	public String getTipoPonto() {
		return tipoPonto;
	}

	/**
	 * @param tipoPonto the tipoPonto to set
	 */
	public void setTipoPonto(String tipoPonto) {
		this.tipoPonto = tipoPonto;
	}

	/**
	 * @return the contato
	 */
	public String getContato() {
		return contato;
	}

	/**
	 * @param contato the contato to set
	 */
	public void setContato(String contato) {
		this.contato = contato;
	}

	/**
	 * @return the telefone
	 */
	public String getTelefone() {
		return telefone;
	}

	/**
	 * @param telefone the telefone to set
	 */
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	/**
	 * @return the endreco
	 */
	public String getEndereco() {
		return endereco;
	}

	/**
	 * @param endreco the endreco to set
	 */
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	/**
	 * @return the bairro
	 */
	public String getBairro() {
		return bairro;
	}

	/**
	 * @param bairro the bairro to set
	 */
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	/**
	 * @return the cidade
	 */
	public String getCidade() {
		return cidade;
	}

	/**
	 * @param cidade the cidade to set
	 */
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	/**
	 * @return the idPdv
	 */
	public Long getIdPdv() {
		return idPdv;
	}

	/**
	 * @param idPdv the idPdv to set
	 */
	public void setIdPdv(Long idPdv) {
		this.idPdv = idPdv;
	}

	/**
	 * @return the idCota
	 */
	public Long getIdCota() {
		return idCota;
	}

	/**
	 * @param idCota the idCota to set
	 */
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	
	
}
