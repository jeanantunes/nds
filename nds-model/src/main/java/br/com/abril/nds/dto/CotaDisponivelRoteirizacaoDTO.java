package br.com.abril.nds.dto;

import java.io.Serializable;

public class CotaDisponivelRoteirizacaoDTO  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8162310820661893456L;

	private Long idRoteirizacao;
	
	private Long idPontoVenda;
	
	private String pontoVenda;
	
	private String origemEndereco;
	
	private String endereco;
	
	private Integer numeroCota;
	
	private String nome ;
	
	private Integer ordem;

	public String getPontoVenda() {
		return pontoVenda;
	}

	public void setPontoVenda(String pontoVenda) {
		this.pontoVenda = pontoVenda;
	}

	public String getOrigemEndereco() {
		return origemEndereco;
	}

	public void setOrigemEndereco(String origemEndereco) {
		this.origemEndereco = origemEndereco;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getIdPontoVenda() {
		return idPontoVenda;
	}

	public void setIdPontoVenda(Long idPontoVenda) {
		this.idPontoVenda = idPontoVenda;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public Long getIdRoteirizacao() {
		return idRoteirizacao;
	}

	public void setIdRoteirizacao(Long idRoteirizacao) {
		this.idRoteirizacao = idRoteirizacao;
	}


	
}
