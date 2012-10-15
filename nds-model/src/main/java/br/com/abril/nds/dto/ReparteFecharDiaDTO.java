package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ReparteFecharDiaDTO implements Serializable {

	private static final long serialVersionUID = 2126291233836764519L;
	
	private String codigo;
	
	private String nomeProduto;
	
	private Long numeroEdicao;
	
	private BigDecimal precoVenda;
	
	private BigDecimal sobras;
	
	private BigDecimal faltas;
	
	private BigDecimal valorTotalReparte;
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public BigDecimal getValorTotalReparte() {
		return valorTotalReparte;
	}

	public void setValorTotalReparte(BigDecimal valorTotalReparte) {
		this.valorTotalReparte = valorTotalReparte;
	}
	
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	public BigDecimal getSobras() {
		return sobras;
	}

	public void setSobras(BigDecimal sobras) {
		this.sobras = sobras;
	}

	public BigDecimal getFaltas() {
		return faltas;
	}

	public void setFaltas(BigDecimal faltas) {
		this.faltas = faltas;
	}
	
	
}
