package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

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
	private Integer qtdReparteMin;
	private BigDecimal bonificacao;
	
	public String getNomeItemRegiao() {
		return nomeItemRegiao;
	}
	public void setNomeItemRegiao(String nomeItemRegiao) {
		this.nomeItemRegiao = nomeItemRegiao;
	}
	public Integer getQtdReparteMin() {
		return qtdReparteMin;
	}
	public void setQtdReparteMin(Integer qtdReparteMin) {
		this.qtdReparteMin = qtdReparteMin;
	}
	public BigDecimal getBonificacao() {
		return bonificacao;
	}
	public void setBonificacao(BigDecimal bonificacao) {
		this.bonificacao = bonificacao;
	}
}
