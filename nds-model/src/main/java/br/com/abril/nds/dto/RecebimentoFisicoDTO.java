package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.planejamento.TipoLancamento;

public class RecebimentoFisicoDTO implements Serializable {
	
	private static final long serialVersionUID = -580201558784688016L;

	private int lineId;
	
	private Long idItemNota;
	
	private Long idItemRecebimentoFisico;
	
	private String codigoProduto;
	
	private String nomeProduto;
	
	private Long edicao;
	
	private BigDecimal precoCapa;
	
	private BigDecimal precoDesconto;
	
	private int pacotePadrao;
	
	private Long peso;
	
	/*
	 * Campo referente a quantidade deste item na nota.
	 */
	private BigInteger repartePrevisto;
	
	/*
	 * Campo referente a quantidade recebimento fisico.
	 * 
	 * O calculo do valor deste campo é igual a:
	 * 	(qtdPacote * peb) + qtdExemplar
	 */
	private BigInteger qtdFisico;
	
	/*
	 * Quantidade de pacotes. 
	 */
	private BigInteger qtdPacote;
	
	/*
	 * Campo referente a quantidade de quebra.
	 */
	private BigInteger qtdExemplar;
	
	private BigInteger diferenca;
	
	private BigDecimal valorTotal;
	
	private TipoDiferenca tipoDiferenca;
	
	private Origem origemItemNota;
	
	private Long idProdutoEdicao;
			
	private Date dataLancamento;
	
	private Date dataRecolhimento;
	
	private TipoLancamento tipoLancamento;	
	
	public RecebimentoFisicoDTO() {}
	
	public RecebimentoFisicoDTO(
			
			Long idItemNota,
			Long idItemRecebimentoFisico,
			String codigoProduto, 
			String nomeProduto, 
			Long edicao,
			Long idProdutoEdicao,
			BigDecimal precoCapa, 
			BigInteger repartePrevisto, 
			BigInteger qtdFisico,
			int pacotePadrao,
			Long peso,
			Date dataLancamento,
			Date dataRecolhimento,
			TipoLancamento tipoLancamento,			
			BigInteger diferenca, 
			TipoDiferenca tipoDiferenca,
			Origem origemItemNota){
		
		this.idItemNota = idItemNota;
		this.idItemRecebimentoFisico = idItemRecebimentoFisico;
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.edicao = edicao;
		this.idProdutoEdicao = idProdutoEdicao;
		this.precoCapa = precoCapa;
		this.repartePrevisto = repartePrevisto;
		this.qtdFisico = qtdFisico;
		this.pacotePadrao = pacotePadrao;
		this.peso = peso;
		this.dataLancamento = dataLancamento;
		this.dataRecolhimento = dataRecolhimento;
		this.tipoLancamento = tipoLancamento;		
		this.diferenca = diferenca;
		this.tipoDiferenca = tipoDiferenca;
		this.origemItemNota = origemItemNota;
		
	}

	public Long getIdItemNota() {
		return idItemNota;
	}

	public void setIdItemNota(Long idItemNota) {
		this.idItemNota = idItemNota;
	}

	public Long getIdItemRecebimentoFisico() {
		return idItemRecebimentoFisico;
	}

	public void setIdItemRecebimentoFisico(Long idItemRecebimentoFisico) {
		this.idItemRecebimentoFisico = idItemRecebimentoFisico;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
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

	public BigDecimal getPrecoDesconto() {
		return precoDesconto;
	}

	public void setPrecoDesconto(BigDecimal precoDesconto) {
		this.precoDesconto = precoDesconto;
	}

	public BigInteger getRepartePrevisto() {
		return repartePrevisto;
	}

	public void setRepartePrevisto(BigInteger repartePrevisto) {
		this.repartePrevisto = repartePrevisto;
	}

	public BigInteger getQtdFisico() {
		return qtdFisico;
	}

	public void setQtdFisico(BigInteger qtdFisico) {
		this.qtdFisico = qtdFisico;
	}

	public BigInteger getQtdPacote() {
		return qtdPacote;
	}

	public void setQtdPacote(BigInteger qtdPacote) {
		this.qtdPacote = qtdPacote;
	}

	public BigInteger getQtdExemplar() {
		return qtdExemplar;
	}

	public void setQtdExemplar(BigInteger qtdExemplar) {
		this.qtdExemplar = qtdExemplar;
	}

	public BigInteger getDiferenca() {
		return diferenca;
	}

	public void setDiferenca(BigInteger diferenca) {
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
	

	public Origem getOrigemItemNota() {
		return origemItemNota;
	}

	public void setOrigemItemNota(Origem origemItemNota) {
		this.origemItemNota = origemItemNota;
	}

	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

	public TipoLancamento getTipoLancamento() {
		return tipoLancamento;
	}

	public void setTipoLancamento(TipoLancamento tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

	public int getPacotePadrao() {
		return pacotePadrao;
	}

	public void setPacotePadrao(int pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}

	public int getLineId() {
		return lineId;
	}

	public void setLineId(int lineId) {
		this.lineId = lineId;
	}
	
	public Long getPeso() {
		return peso;
	}

	public void setPeso(Long peso) {
		this.peso = peso;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lineId;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof RecebimentoFisicoDTO)) {
			return false;
		}
		RecebimentoFisicoDTO other = (RecebimentoFisicoDTO) obj;
		if (lineId != other.lineId) {
			return false;
		}
		return true;
	}

	
	

}
