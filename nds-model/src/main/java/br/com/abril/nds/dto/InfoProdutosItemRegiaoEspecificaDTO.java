package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.dto.filtro.FiltroDTO;

/*
 * Classe utilizada no detalhamento do produto. 
 * 
 * Funcionalidade: Item da região específica.
 * 
 */

public class InfoProdutosItemRegiaoEspecificaDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = 2038396512399852785L;
	
	private String nomeItemRegiao;
	private Integer quantidade = 0;
	private Double bonificacao = (double) 0;
	
	public String getNomeItemRegiao() {
		return nomeItemRegiao;
	}
	public void setNomeItemRegiao(String nomeItemRegiao) {
		this.nomeItemRegiao = nomeItemRegiao;
	}
	public Integer getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Integer quantidade) {
		if(quantidade != null){
			this.quantidade = quantidade;
		}else{
			this.quantidade = 0;
		}
	}
	public Double getBonificacao() {
		return bonificacao;
	}
	public void setBonificacao(Double bonificacao) {
		if(bonificacao != null){
			this.bonificacao = bonificacao;
		}else{
			this.bonificacao = (double) 0;
		}
	}
}
