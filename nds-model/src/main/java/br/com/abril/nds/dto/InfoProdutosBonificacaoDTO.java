package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.dto.filtro.FiltroDTO;

public class InfoProdutosBonificacaoDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = 2038396512399852785L;
	
	private String componente;
	private String nomeItem;
	private Integer qtdReparteMin;
	private BigDecimal bonificacao;
	
    public String getComponente() {
        return componente;
    }
    public void setComponente(String componente) {
        this.componente = componente;
    }
    public String getNomeItem() {
		return nomeItem;
	}
	public void setNomeItem(String nomeItem) {
		this.nomeItem = nomeItem;
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
