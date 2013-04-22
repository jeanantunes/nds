package br.com.abril.nds.integracao.ems0108.inbound;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatDecimal;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0108Input implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date dataMovimento;
	private String codigoPublicacao;
	private Long edicaoLancamento;
	private Long edicaoRecolhimento;
	private String dataLancamentoRecolhimentoProduto;
	private String condRelancamento;
	private String condImprimeBoleto;
	private String condEncalheRetido;
	private String flagAtual;
	private String tipoRecolhimento;
	private String condCobrancaTotal;
	private String condProdutoEspecial;
	private Long pesoProduto;
	private String codigoBarrasFisicoProduto;
	private String codigoBarrasCRP;
	private Integer codigoEdicaoOrigem; 
	private String sloganProduto;
	private Integer PEB;
	private Integer pacotePadrao;
	private String formaComercializacao; 
	private Integer tipoDesconto;
	private BigDecimal percentualDesconto; 
	private String periodicidade;
	private String tributacaoFiscal; 

	@Field(offset = 1, length = 8)
	public Date getDataMovimento() {
		return dataMovimento;
	}

	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
	}

	@Field(offset = 9, length = 8)
	public String getCodigoPublicacao() {
		return codigoPublicacao;
	}

	public void setCodigoPublicacao(String codigoPublicacao) {
		this.codigoPublicacao = codigoPublicacao;
	}

	@Field(offset = 17, length = 4)
	public Long getEdicaoLancamento() {
		return edicaoLancamento;
	}

	public void setEdicaoLancamento(Long edicao) {
		this.edicaoLancamento = edicao;
	}

	@Field(offset = 21, length = 4)
	public Long getEdicaoRecolhimento() {
		return edicaoRecolhimento;
	}

	public void setEdicaoRecolhimento(Long edicaoRecolhimento) {
		this.edicaoRecolhimento = edicaoRecolhimento;
	}

	@Field(offset = 25, length = 8)
	//@FixedFormatPattern("yyyyMMdd")
	public String getDataLancamentoRecolhimentoProduto() {
		return dataLancamentoRecolhimentoProduto;
	}

	public void setDataLancamentoRecolhimentoProduto(
			String dataLancamentoRecolhimentoProduto) {
		this.dataLancamentoRecolhimentoProduto = dataLancamentoRecolhimentoProduto;
	}

	@Field(offset = 33, length = 1)
	public String getCondRelancamento() {
		return condRelancamento;
	}

	public void setCondRelancamento(String condRelancamento) {
		this.condRelancamento = condRelancamento;
	}

	@Field(offset = 34, length = 1)
	public String getCondImprimeBoleto() {
		return condImprimeBoleto;
	}

	public void setCondImprimeBoleto(String condImprimeBoleto) {
		this.condImprimeBoleto = condImprimeBoleto;
	}

	@Field(offset = 35, length = 1)
	public String getCondEncalheRetido() {
		return condEncalheRetido;
	}

	public void setCondEncalheRetido(String condEncalheRetido) {
		this.condEncalheRetido = condEncalheRetido;
	}

	@Field(offset = 36, length = 1)
	public String getFlagAtual() {
		return flagAtual;
	}

	public void setFlagAtual(String flagAtual) {
		this.flagAtual = flagAtual;
	}

	@Field(offset = 37, length = 1)
	public String getTipoRecolhimento() {
		return tipoRecolhimento;
	}

	public void setTipoRecolhimento(String tipoRecolhimento) {
		this.tipoRecolhimento = tipoRecolhimento;
	}

	@Field(offset = 38, length = 1)
	public String getCondCobrancaTotal() {
		return condCobrancaTotal;
	}

	public void setCondCobrancaTotal(String condCobrancaTotal) {
		this.condCobrancaTotal = condCobrancaTotal;
	}

	@Field(offset = 39, length = 1)
	public String getCondProdutoEspecial() {
		return condProdutoEspecial;
	}

	public void setCondProdutoEspecial(String condProdutoEspecial) {
		this.condProdutoEspecial = condProdutoEspecial;
	}

	@Field(offset = 40, length = 5)
	@FixedFormatDecimal(decimals = 2, useDecimalDelimiter = false)
	public Long getPesoProduto() {
		return pesoProduto;
	}

	public void setPesoProduto(Long pesoProduto) {
		this.pesoProduto = pesoProduto;
	}

	@Field(offset = 45, length = 18)
	public String getCodigoBarrasFisicoProduto() {
		return codigoBarrasFisicoProduto;
	}

	public void setCodigoBarrasFisicoProduto(String codigoBarrasFisicoProduto) {
		this.codigoBarrasFisicoProduto = codigoBarrasFisicoProduto;
	}

	
	@Field(offset = 63, length = 18)
	public String getCodigoBarrasCRP() {
		return codigoBarrasCRP;
	}

	public void setCodigoBarrasCRP(String codigoBarrasCRP) {
		this.codigoBarrasCRP = codigoBarrasCRP;
	}

	@Field(offset = 81, length = 4)
	public Integer getCodigoEdicaoOrigem() {
		return codigoEdicaoOrigem;
	}

	public void setCodigoEdicaoOrigem(Integer codigoEdicaoOrigem) {
		this.codigoEdicaoOrigem = codigoEdicaoOrigem;
	}

	@Field(offset = 85, length = 50)
	public String getSloganProduto() {
		return sloganProduto;
	}

	public void setSloganProduto(String sloganProduto) {
		this.sloganProduto = sloganProduto;
	}

	@Field(offset = 135, length = 3)
	public Integer getPEB() {
		return PEB;
	}

	public void setPEB(Integer PEB) {
		this.PEB = PEB;
	}

	@Field(offset = 138, length = 8)
	public Integer getPacotePadrao() {
		return pacotePadrao;
	}

	public void setPacotePadrao(Integer pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}

	@Field(offset = 146, length = 3)
	public String getFormaComercializacao() {
		return formaComercializacao;
	}

	public void setFormaComercializacao(String formaComercializacao) {
		this.formaComercializacao = formaComercializacao;
	}

	@Field(offset = 149, length = 2)
	public Integer getTipoDesconto() {
		return tipoDesconto;
	}

	public void setTipoDesconto(Integer tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}

	@Field(offset = 151, length = 7)
	@FixedFormatDecimal(decimals = 4, useDecimalDelimiter = true)
	public BigDecimal getPercentualDesconto() {
		return percentualDesconto;
	}

	public void setPercentualDesconto(BigDecimal percentualDesconto) {
		this.percentualDesconto = percentualDesconto;
	}

	@Field(offset = 158, length = 3)
	public String getPeriodicidade() {
		return periodicidade;
	}

	public void setPeriodicidade(String periodicidade) {
		this.periodicidade = periodicidade;
	}

	@Field(offset = 161, length = 1)
	public String getTributacaoFiscal() {
		return tributacaoFiscal;
	}

	public void setTributacaoFiscal(String tributacaoFiscal) {
		this.tributacaoFiscal = tributacaoFiscal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((codigoBarrasFisicoProduto == null) ? 0
						: codigoBarrasFisicoProduto.hashCode());
		result = prime
				* result
				+ ((codigoPublicacao == null) ? 0 : codigoPublicacao.hashCode());
		result = prime
				* result
				+ ((condCobrancaTotal == null) ? 0 : condCobrancaTotal
						.hashCode());
		result = prime
				* result
				+ ((condEncalheRetido == null) ? 0 : condEncalheRetido
						.hashCode());
		result = prime
				* result
				+ ((condImprimeBoleto == null) ? 0 : condImprimeBoleto
						.hashCode());
		result = prime
				* result
				+ ((condProdutoEspecial == null) ? 0 : condProdutoEspecial
						.hashCode());
		result = prime
				* result
				+ ((condRelancamento == null) ? 0 : condRelancamento.hashCode());
		result = prime
				* result
				+ ((dataLancamentoRecolhimentoProduto == null) ? 0
						: dataLancamentoRecolhimentoProduto.hashCode());
		result = prime * result
				+ ((dataMovimento == null) ? 0 : dataMovimento.hashCode());
		result = prime * result + ((edicaoLancamento == null) ? 0 : edicaoLancamento.hashCode());
		result = prime
				* result
				+ ((edicaoRecolhimento == null) ? 0 : edicaoRecolhimento
						.hashCode());
		result = prime * result
				+ ((flagAtual == null) ? 0 : flagAtual.hashCode());
		result = prime * result
				+ ((pesoProduto == null) ? 0 : pesoProduto.hashCode());
		result = prime
				* result
				+ ((tipoRecolhimento == null) ? 0 : tipoRecolhimento.hashCode());
		
		result = prime
				* result
				+ ((codigoBarrasCRP == null) ? 0 : codigoBarrasCRP.hashCode());
		result = prime
				* result
				+ ((codigoEdicaoOrigem == null) ? 0 : codigoEdicaoOrigem.hashCode());
		result = prime
				* result
				+ ((sloganProduto == null) ? 0 : sloganProduto.hashCode());
		result = prime
				* result
				+ ((PEB == null) ? 0 : PEB.hashCode());
		result = prime
				* result
				+ ((pacotePadrao == null) ? 0 : pacotePadrao.hashCode());
		result = prime
				* result
				+ ((formaComercializacao == null) ? 0 : formaComercializacao.hashCode());
		result = prime
				* result
				+ ((tipoDesconto == null) ? 0 : tipoDesconto.hashCode());
		result = prime
				* result
				+ ((percentualDesconto == null) ? 0 : percentualDesconto.hashCode());
		result = prime
				* result
				+ ((periodicidade == null) ? 0 : periodicidade.hashCode());
		result = prime
				* result
				+ ((tributacaoFiscal == null) ? 0 : tributacaoFiscal.hashCode());
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EMS0108Input other = (EMS0108Input) obj;
		if (codigoBarrasFisicoProduto == null) {
			if (other.codigoBarrasFisicoProduto != null)
				return false;
		} else if (!codigoBarrasFisicoProduto
				.equals(other.codigoBarrasFisicoProduto))
			return false;
		if (codigoPublicacao == null) {
			if (other.codigoPublicacao != null)
				return false;
		} else if (!codigoPublicacao.equals(other.codigoPublicacao))
			return false;
		if (condCobrancaTotal == null) {
			if (other.condCobrancaTotal != null)
				return false;
		} else if (!condCobrancaTotal.equals(other.condCobrancaTotal))
			return false;
		if (condEncalheRetido == null) {
			if (other.condEncalheRetido != null)
				return false;
		} else if (!condEncalheRetido.equals(other.condEncalheRetido))
			return false;
		if (condImprimeBoleto == null) {
			if (other.condImprimeBoleto != null)
				return false;
		} else if (!condImprimeBoleto.equals(other.condImprimeBoleto))
			return false;
		if (condProdutoEspecial == null) {
			if (other.condProdutoEspecial != null)
				return false;
		} else if (!condProdutoEspecial.equals(other.condProdutoEspecial))
			return false;
		if (condRelancamento == null) {
			if (other.condRelancamento != null)
				return false;
		} else if (!condRelancamento.equals(other.condRelancamento))
			return false;
		if (dataLancamentoRecolhimentoProduto == null) {
			if (other.dataLancamentoRecolhimentoProduto != null)
				return false;
		} else if (!dataLancamentoRecolhimentoProduto
				.equals(other.dataLancamentoRecolhimentoProduto))
			return false;
		if (dataMovimento == null) {
			if (other.dataMovimento != null)
				return false;
		} else if (!dataMovimento.equals(other.dataMovimento))
			return false;
		if (edicaoLancamento == null) {
			if (other.edicaoLancamento != null)
				return false;
		} else if (!edicaoLancamento.equals(other.edicaoLancamento))
			return false;
		if (edicaoRecolhimento == null) {
			if (other.edicaoRecolhimento != null)
				return false;
		} else if (!edicaoRecolhimento.equals(other.edicaoRecolhimento))
			return false;
		if (flagAtual == null) {
			if (other.flagAtual != null)
				return false;
		} else if (!flagAtual.equals(other.flagAtual))
			return false;
		if (pesoProduto == null) {
			if (other.pesoProduto != null)
				return false;
		} else if (!pesoProduto.equals(other.pesoProduto))
			return false;
		if (tipoRecolhimento == null) {
			if (other.tipoRecolhimento != null)
				return false;
		} else if (!tipoRecolhimento.equals(other.tipoRecolhimento))
			return false;

		if (codigoBarrasCRP == null) {
			if (other.codigoBarrasCRP != null)
				return false;
		} else if (!codigoBarrasCRP.equals(other.codigoBarrasCRP))
			return false;

		if (codigoEdicaoOrigem == null) {
			if (other.codigoEdicaoOrigem != null)
				return false;
		} else if (!codigoEdicaoOrigem.equals(other.codigoEdicaoOrigem))
			return false;

		if (PEB == null) {
			if (other.PEB != null)
				return false;
		} else if (!PEB.equals(other.PEB))
			return false;

		if (pacotePadrao == null) {
			if (other.pacotePadrao != null)
				return false;
		} else if (!pacotePadrao.equals(other.pacotePadrao))
			return false;

		if (formaComercializacao == null) {
			if (other.formaComercializacao != null)
				return false;
		} else if (!formaComercializacao.equals(other.formaComercializacao))
			return false;

		if (tipoDesconto == null) {
			if (other.tipoDesconto != null)
				return false;
		} else if (!tipoDesconto.equals(other.tipoDesconto))
			return false;

		if (percentualDesconto == null) {
			if (other.percentualDesconto != null)
				return false;
		} else if (!percentualDesconto.equals(other.percentualDesconto))
			return false;

		if (periodicidade == null) {
			if (other.periodicidade != null)
				return false;
		} else if (!periodicidade.equals(other.periodicidade))
			return false;

		if (tributacaoFiscal == null) {
			if (other.tributacaoFiscal != null)
				return false;
		} else if (!tributacaoFiscal.equals(other.tributacaoFiscal))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "EMS0108Input [dataMovimento=" + dataMovimento
				+ ", codigoPublicacao=" + codigoPublicacao + ", edicao="
				+ edicaoLancamento + ", edicaoRecolhimento=" + edicaoRecolhimento
				+ ", dataLancamentoRecolhimentoProduto="
				+ dataLancamentoRecolhimentoProduto + ", condRelancamento="
				+ condRelancamento + ", condImprimeBoleto=" + condImprimeBoleto
				+ ", condEncalheRetido=" + condEncalheRetido + ", flagAtual="
				+ flagAtual + ", tipoRecolhimento=" + tipoRecolhimento
				+ ", condCobrancaTotal=" + condCobrancaTotal
				+ ", condProdutoEspecial=" + condProdutoEspecial
				+ ", pesoProduto=" + pesoProduto
				+ ", codigoBarrasFisicoProduto=" + codigoBarrasFisicoProduto
				+ ", codigoBarrasCRP=" + codigoBarrasCRP
				+ ", codigoEdicaoOrigem=" + codigoEdicaoOrigem
				+ ", PEB=" + PEB
				+ ", pacotePadrao=" + pacotePadrao
				+ ", formaComercializacao=" + formaComercializacao
				+ ", tipoDesconto=" + tipoDesconto
				+ ", percentualDesconto=" + percentualDesconto
				+ ", periodicidade=" + periodicidade
				+ ", tributacaoFiscal=" + tributacaoFiscal
				+ "]";
		
	}
}