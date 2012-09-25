package br.com.abril.nds.dto;

import java.io.Serializable;

public class PdvRoteirizacaoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 201200000L;
	
	private Long id;
	
	private String pdv;
	
	private String origem;
	
	private String endereco;
	
	private String cota;
	
	private String nome;
	
	private Integer ordem;
	
	private Boolean selecionado;
	
	
	public PdvRoteirizacaoDTO(){
		
	}

	/**
	 * @param id
	 * @param pdv
	 * @param origem
	 * @param endereco
	 * @param cota
	 * @param nome
	 * @param ordem
	 * @param selecionado
	 */
	public PdvRoteirizacaoDTO(Long id, String pdv, String origem,
			String endereco, String cota, String nome, Integer ordem,
			Boolean selecionado) {
		super();
		this.id = id;
		this.pdv = pdv;
		this.origem = origem;
		this.endereco = endereco;
		this.cota = cota;
		this.nome = nome;
		this.ordem = ordem;
		this.selecionado = selecionado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPdv() {
		return pdv;
	}

	public void setPdv(String pdv) {
		this.pdv = pdv;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getCota() {
		return cota;
	}

	public void setCota(String cota) {
		this.cota = cota;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public Boolean getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}
	

}
