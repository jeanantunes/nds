package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.model.estoque.TipoDiferenca;

public class RecebimentoFisicoDTO implements Serializable {
	
	private static final long serialVersionUID = -580201558784688016L;

	private String codigo;
	
	private String nomeProduto;
	
	private Long edicao;
	
	private BigDecimal precoCapa;
	
	private BigDecimal repartePrevisto;
	
	private BigDecimal qtdFisico;
	
	private BigDecimal diferenca;
	
	private BigDecimal valorTotal;
	
	private TipoDiferenca tipoDiferenca;
	
	private Long idItemNota;

	public RecebimentoFisicoDTO() {
		
	}
	
	public RecebimentoFisicoDTO(String codigo, 
			String nomeProduto, 
			Long edicao, 
			BigDecimal precoCapa, 
			BigDecimal repartePrevisto, 
			BigDecimal qtdFisico, 
			BigDecimal diferenca, 
			TipoDiferenca tipoDiferenca,
			Long idItemNota){
		this.codigo = codigo;
		this.nomeProduto = nomeProduto;
		this.edicao = edicao;
		this.precoCapa = precoCapa;
		this.repartePrevisto = repartePrevisto;
		this.qtdFisico = qtdFisico;
		this.diferenca = diferenca;
		this.tipoDiferenca = tipoDiferenca;
		this.idItemNota = idItemNota;		
	}

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

	public BigDecimal getRepartePrevisto() {
		return repartePrevisto;
	}

	public void setRepartePrevisto(BigDecimal repartePrevisto) {
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

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public TipoDiferenca getTipoDiferenca() {
		return tipoDiferenca;
	}

	public void setTipoDiferenca(TipoDiferenca tipoDiferenca) {
		this.tipoDiferenca = tipoDiferenca;
	}

	public Long getIdItemNota() {
		return idItemNota;
	}

	public void setIdItemNota(Long idItemNota) {
		this.idItemNota = idItemNota;
	}
	

}