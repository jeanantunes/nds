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
	
	private BigDecimal transferencias;
	
	private BigDecimal distribuidos;
	
	private BigDecimal valorTotalReparte;
	
	private Integer qtdReparte;
	
	private Integer qtdSobras;
	
	private Integer qtdFaltas;
	
	private Integer qtdTransferido;
	
	private Integer qtdADistribuir;
	
	private Integer qtdDistribuido;
	
	private Integer qtdSobraDiferenca;
	
	private Integer qtdDiferenca;
	
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

	public BigDecimal getTransferencias() {
		return transferencias;
	}

	public void setTransferencias(BigDecimal transferencias) {
		this.transferencias = transferencias;
	}

	public BigDecimal getDistribuidos() {
		return distribuidos;
	}

	public void setDistribuidos(BigDecimal distribuidos) {
		this.distribuidos = distribuidos;
	}

	public Integer getQtdReparte() {
		return qtdReparte;
	}

	public void setQtdReparte(Integer qtdReparte) {
		this.qtdReparte = qtdReparte;
	}

	public Integer getQtdSobras() {
		return qtdSobras;
	}

	public void setQtdSobras(Integer qtdSobras) {
		this.qtdSobras = qtdSobras;
	}

	public Integer getQtdFaltas() {
		return qtdFaltas;
	}

	public void setQtdFaltas(Integer qtdFaltas) {
		this.qtdFaltas = qtdFaltas;
	}

	public Integer getQtdTransferido() {
		return qtdTransferido;
	}

	public void setQtdTransferido(Integer qtdTransferido) {
		this.qtdTransferido = qtdTransferido;
	}

	public Integer getQtdADistribuir() {
		return qtdADistribuir;
	}

	public void setQtdADistribuir(Integer qtdADistribuir) {
		this.qtdADistribuir = qtdADistribuir;
	}

	public Integer getQtdDistribuido() {
		return qtdDistribuido;
	}

	public void setQtdDistribuido(Integer qtdDistribuido) {
		this.qtdDistribuido = qtdDistribuido;
	}

	public Integer getQtdSobraDiferenca() {
		return qtdSobraDiferenca;
	}

	public void setQtdSobraDiferenca(Integer qtdSobraDiferenca) {
		this.qtdSobraDiferenca = qtdSobraDiferenca;
	}

	public Integer getQtdDiferenca() {
		return qtdDiferenca;
	}

	public void setQtdDiferenca(Integer qtdDiferenca) {
		this.qtdDiferenca = qtdDiferenca;
	}
	
}
