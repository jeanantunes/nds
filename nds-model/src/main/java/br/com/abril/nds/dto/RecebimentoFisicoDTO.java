package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class RecebimentoFisicoDTO implements Serializable {
	
	private static final long serialVersionUID = -580201558784688016L;

	private String nomeProduto;
	
	private Long edicao;
	
	private BigDecimal precoCapa;
	
	private Long repartePrevisto;
	
	private BigDecimal qtdFisico;
	
	private BigDecimal diferenca;
	
	private Long codigo;

	public RecebimentoFisicoDTO(Long codigo, String nomeProduto, Long edicao, BigDecimal precoCapa, Long repartePrevisto, BigDecimal qtdFisico, BigDecimal diferenca){
		this.codigo = codigo;
		this.nomeProduto = nomeProduto;
		this.edicao = edicao;
		this.precoCapa = precoCapa;
		this.repartePrevisto = repartePrevisto;
		this.qtdFisico = qtdFisico;
		this.diferenca = diferenca;		
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public Long getEdicao() {
		return edicao;
	}

	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}

	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}

	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}

	public Long getRepartePrevisto() {
		return repartePrevisto;
	}

	public void setRepartePrevisto(Long repartePrevisto) {
		this.repartePrevisto = repartePrevisto;
	}

	public BigDecimal getQtdFisico() {
		return qtdFisico;
	}

	public void setQtdFisico(BigDecimal qtdFisico) {
		this.qtdFisico = qtdFisico;
	}

	public BigDecimal getDiferenca() {
		return diferenca;
	}

	public void setDiferenca(BigDecimal diferenca) {
		this.diferenca = diferenca;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	
	
	

}