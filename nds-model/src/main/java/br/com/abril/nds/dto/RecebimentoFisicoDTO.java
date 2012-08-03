package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.planejamento.TipoLancamento;

public class RecebimentoFisicoDTO implements Serializable {
	
	private static final long serialVersionUID = -580201558784688016L;

	private Long idItemNota;
	
	private Long idItemRecebimentoFisico;
	
	private String codigoProduto;
	
	private String nomeProduto;
	
	private Long edicao;
	
	private BigDecimal precoCapa;
	
	private BigDecimal precoDesconto;
	
	private BigDecimal repartePrevisto;
	
	private BigDecimal qtdFisico;
	
	private BigDecimal qtdPacote;
	
	private BigDecimal qtdExemplares;
	
	private BigDecimal diferenca;
	
	private BigDecimal valorTotal;
	
	private TipoDiferenca tipoDiferenca;
	
	private Origem origemItemNota;
	
	private Long idProdutoEdicao;
			
	private Date dataLancamento;
	
	private Date dataRecolhimento;
	
	private TipoLancamento tipoLancamento;	
	
	private int lineId;
	
	public RecebimentoFisicoDTO() {}
	
	public RecebimentoFisicoDTO(
			
			Long idItemNota,
			Long idItemRecebimentoFisico,
			String codigoProduto, 
			String nomeProduto, 
			Long edicao,
			Long idProdutoEdicao,
			BigDecimal precoCapa, 
			BigDecimal repartePrevisto, 
			BigDecimal qtdFisico,
			Date dataLancamento,
			Date dataRecolhimento,
			TipoLancamento tipoLancamento,			
			BigDecimal diferenca, 
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

	public BigDecimal getQtdPacote() {
		return qtdPacote;
	}

	public void setQtdPacote(BigDecimal qtdPacote) {
		this.qtdPacote = qtdPacote;
	}

	public BigDecimal getQtdExemplares() {
		return qtdExemplares;
	}

	public void setQtdExemplares(BigDecimal qtdExemplares) {
		this.qtdExemplares = qtdExemplares;
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

	public int getLineId() {
		return lineId;
	}

	public void setLineId(int lineId) {
		this.lineId = lineId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lineId;
		return result;
	}

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
