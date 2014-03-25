package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.util.ComponentesPDV;

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
	private Integer bonificacao;
	
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
	public Integer getBonificacao() {
		return bonificacao;
	}
	public void setBonificacao(Integer bonificacao) {
		this.bonificacao = bonificacao;
	}
}
