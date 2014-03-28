package br.com.abril.nds.model.ftf.envio;

import br.com.abril.nds.ftfutil.FTFBaseDTO;
import br.com.abril.nds.ftfutil.FTFfield;
import br.com.abril.nds.model.ftf.FTFCommons;

public class FTFEnvTipoRegistro05 extends FTFBaseDTO implements FTFCommons {

	@FTFfield(tamanho=1, tipo="char", ordem=1)
	private String tipoRegistro = "5";
	
	@FTFfield(tamanho = 2, tipo = "char", ordem = 2)
	private String codEstabelecimentoEmissor;

	@FTFfield(tamanho = 14, tipo = "char", ordem = 3)
	private String cnpjEmpresaEmissora;

	@FTFfield(tamanho = 11, tipo = "char", ordem = 4)
	private String codLocal;

	@FTFfield(tamanho = 2, tipo = "char", ordem = 5)
	private String tipoPedido;

	@FTFfield(tamanho = 8, tipo = "char", ordem = 6)
	private String numeroDocOrigem;
	
	@FTFfield(tamanho=2, tipo="numeric", ordem=7)
	private String numParcela;
	
	@FTFfield(tamanho=3, tipo="numeric", ordem=8)
	private String qtdeDiasParaPagamento;
	
	@FTFfield(tamanho=15, tipo="numeric", ordem=9)
	private String valorParcela;
	
	@FTFfield(tamanho=10, tipo="char", ordem=10)
	private String dataVencimentoParcela;
	
	@FTFfield(tamanho=6, tipo="numeric", ordem=11)
	private String percentualDescontoCondicional;
	
	@FTFfield(tamanho=10, tipo="char", ordem=12)
	private String dataLimiteDescontoFinanceiro;
	
	@FTFfield(tamanho=4, tipo="numeric", ordem=13)
	private String bancoCobranca;
	
	@FTFfield(tamanho=16, tipo="char", ordem=14)
	private String agenciaCobranca;
	
	@FTFfield(tamanho=2, tipo="char", ordem=15)
	private String digitoAgencia;
	
	/**
	 * Data impressa no relatório de Cartões debitados.
	 */
	@FTFfield(tamanho=10, tipo="char", ordem=16)
	private String dataRelatorioCartaoDebitado;

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getCodEstabelecimentoEmissor() {
		return codEstabelecimentoEmissor;
	}

	public String getCnpjEmpresaEmissora() {
		return cnpjEmpresaEmissora;
	}

	public String getCodLocal() {
		return codLocal;
	}

	public String getTipoPedido() {
		return tipoPedido;
	}

	public String getNumeroDocOrigem() {
		return numeroDocOrigem;
	}

	public String getNumParcela() {
		return numParcela;
	}

	public void setNumParcela(String numParcela) {
		this.numParcela = numParcela;
	}

	public String getQtdeDiasParaPagamento() {
		return qtdeDiasParaPagamento;
	}

	public void setQtdeDiasParaPagamento(String qtdeDiasParaPagamento) {
		this.qtdeDiasParaPagamento = qtdeDiasParaPagamento;
	}

	public String getValorParcela() {
		return valorParcela;
	}

	public void setValorParcela(String valorParcela) {
		this.valorParcela = valorParcela;
	}

	public String getDataVencimentoParcela() {
		return dataVencimentoParcela;
	}

	public void setDataVencimentoParcela(String dataVencimentoParcela) {
		this.dataVencimentoParcela = dataVencimentoParcela;
	}

	public String getPercentualDescontoCondicional() {
		return percentualDescontoCondicional;
	}

	public void setPercentualDescontoCondicional(
			String percentualDescontoCondicional) {
		this.percentualDescontoCondicional = percentualDescontoCondicional;
	}

	public String getDataLimiteDescontoFinanceiro() {
		return dataLimiteDescontoFinanceiro;
	}

	public void setDataLimiteDescontoFinanceiro(String dataLimiteDescontoFinanceiro) {
		this.dataLimiteDescontoFinanceiro = dataLimiteDescontoFinanceiro;
	}

	public String getBancoCobranca() {
		return bancoCobranca;
	}

	public void setBancoCobranca(String bancoCobranca) {
		this.bancoCobranca = bancoCobranca;
	}

	public String getAgenciaCobranca() {
		return agenciaCobranca;
	}

	public void setAgenciaCobranca(String agenciaCobranca) {
		this.agenciaCobranca = agenciaCobranca;
	}

	public String getDigitoAgencia() {
		return digitoAgencia;
	}

	public void setDigitoAgencia(String digitoAgencia) {
		this.digitoAgencia = digitoAgencia;
	}

	public String getDataRelatorioCartaoDebitado() {
		return dataRelatorioCartaoDebitado;
	}

	public void setDataRelatorioCartaoDebitado(String dataRelatorioCartaoDebitado) {
		this.dataRelatorioCartaoDebitado = dataRelatorioCartaoDebitado;
	}
	
	@Override
	public void setCodEstabelecimentoEmissor(String codEstabelecimentoEmissor) {
		this.codEstabelecimentoEmissor = codEstabelecimentoEmissor;
	}

	@Override
	public void setCnpjEmpresaEmissora(String cnpjEmpresaEmissora) {
		this.cnpjEmpresaEmissora = cnpjEmpresaEmissora;
	}

	@Override
	public void setCodLocal(String codLocal) {
		this.codLocal = codLocal;
	}

	@Override
	public void setTipoPedido(String tipoPedido) {
		this.tipoPedido = tipoPedido;
	}

	@Override
	public void setNumeroDocOrigem(String numeroDocOrigem) {
		this.numeroDocOrigem = numeroDocOrigem;
	}
}
