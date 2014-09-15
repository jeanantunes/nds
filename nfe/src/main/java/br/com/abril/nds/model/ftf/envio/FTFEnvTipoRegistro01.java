package br.com.abril.nds.model.ftf.envio;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.ftfutil.FTFBaseDTO;
import br.com.abril.nds.ftfutil.FTFfield;
import br.com.abril.nds.model.ftf.FTFCommons;

public class FTFEnvTipoRegistro01 extends FTFBaseDTO implements FTFCommons {

	public List<FTFEnvTipoRegistro02> itemNFList;
	
	public List<FTFEnvTipoRegistro03> itemNFList03;
	
	private FTFEnvTipoRegistro06 regTipo06;
	
	private FTFEnvTipoRegistro08 regTipo08;
	
	@FTFfield(tamanho=1, tipo="char", ordem=1)
	private String tipoRegistro = "1";
	
	@FTFfield(tamanho = 2, tipo = "char", ordem = 2)
	private String codigoCentroEmissor;

	@FTFfield(tamanho = 14, tipo = "char", ordem = 3)
	private String cnpjEmpresaEmissora;
	
	@FTFfield(tamanho = 11, tipo = "char", ordem = 4)
	private String codLocal;

	@FTFfield(tamanho = 2, tipo = "char", ordem = 5)
	private String tipoPedido;

	@FTFfield(tamanho = 8, tipo = "char", ordem = 6)
	private String numeroDocOrigem;
	
	@FTFfield(tamanho=8, tipo="char", ordem=7)
	private String codSolicitante;
	
	@FTFfield(tamanho=40, tipo="char", ordem=8)
	private String nomeLocalEntregaNf;
	
	@FTFfield(tamanho=10, tipo="char", ordem=9)
	private String dataPedido;
	
	@FTFfield(tamanho=3, tipo="char", ordem=10)
	private String codCentroEmissor;
	
	@FTFfield(tamanho=14, tipo="char", ordem=11)
	private String cpfCnpjDestinatario;
	
	@FTFfield(tamanho=14, tipo="char", ordem=12)
	private String cpfCnpjEstabelecimentoEntrega;
	
	@FTFfield(tamanho=9, tipo="char", ordem=13)
	private String codDestinatarioSistemaOrigem;
	
	@FTFfield(tamanho=2, tipo="char", ordem=14)
	private String codCondicaoPagamento;
	
	@FTFfield(tamanho=12, tipo="char", ordem=15)
	private String numInscricaoEstadual;
	
	@FTFfield(tamanho=12, tipo="char", ordem=16)
	private String numDeclaracaoImportacao;
	
	@FTFfield(tamanho=15, tipo="char", ordem=17)
	private String valorDespesasAcessorias;
	
	@FTFfield(tamanho=15, tipo="char", ordem=18)
	private String valorVariacaoCambial;
	
	@FTFfield(tamanho=15, tipo="char", ordem=19)
	private String valorFrete;
	
	@FTFfield(tamanho=15, tipo="char", ordem=20)
	private String valorSeguro;
	
	@FTFfield(tamanho=15, tipo="char", ordem=21)
	private String valorOutrasDespesas;
	
	@FTFfield(tamanho=15, tipo="char", ordem=22)
	private String valorComplemento;
	
	@FTFfield(tamanho=15, tipo="char", ordem=23)
	private String valorIcmsComplemento;
	
	@FTFfield(tamanho=15, tipo="char", ordem=24)
	private String valorIpiComplemento;
	
	@FTFfield(tamanho=1, tipo="char", ordem=25)
	private String imprimePercentualDesconto;
	
	@FTFfield(tamanho=1, tipo="char", ordem=26)
	private String codMotivoCancelamento;
	
	@FTFfield(tamanho=1, tipo="char", ordem=27)
	private String indicadorRefaturamento;
	
	@FTFfield(tamanho=4, tipo="char", ordem=28)
	private String codMoeda;
	
	@FTFfield(tamanho=10, tipo="char", ordem=29)
	private String dataCancelamento;
	
	@FTFfield(tamanho=2, tipo="char", ordem=30)
	private String numSerieNfOrigem;
	
	@FTFfield(tamanho=6, tipo="char", ordem=31)
	private String numNfOrigem;
	
	@FTFfield(tamanho=14, tipo="char", ordem=32)
	private String cpfCnpjTransportadora;
	
	@FTFfield(tamanho=7, tipo="char", ordem=33)
	private String numPlacaVeiculo;
	
	@FTFfield(tamanho=10, tipo="char", ordem=34)
	private String tipoEmbalagem;
	
	@FTFfield(tamanho=4, tipo="char", ordem=35)
	private String quantidadeVolumes;
	
	@FTFfield(tamanho=10, tipo="char", ordem=36)
	private String pesoLiquido;
	
	@FTFfield(tamanho=10, tipo="char", ordem=37)
	private String pesoBruto;
	
	@FTFfield(tamanho=10, tipo="char", ordem=38)
	private String dataSaida;
	
	@FTFfield(tamanho=5, tipo="char", ordem=39)
	private String numContaBanco;
	
	@FTFfield(tamanho=2, tipo="char", ordem=40)
	private String formaPagamento;
	
	@FTFfield(tamanho=1, tipo="char", ordem=41)
	private String envioBoleto;
	
	@FTFfield(tamanho=1, tipo="char", ordem=42)
	private String formaFaturamento;
	
	@FTFfield(tamanho=4, tipo="char", ordem=43)
	private String percentualComissaoIntermediaio;
	
	@FTFfield(tamanho=8, tipo="char", ordem=44)
	private String pedidoInsercao;
	
	@FTFfield(tamanho=14, tipo="char", ordem=45)
	private String cpfCnpjIntermediario;
	
	@FTFfield(tamanho=9, tipo="char", ordem=46)
	private String codIntermediarioSistemaOrigem;
	
	@FTFfield(tamanho=15, tipo="char", ordem=47)
	private String valorBruto;
	
	@FTFfield(tamanho=6, tipo="char", ordem=48)
	private String numeroFaturaAssociada;
	
	@FTFfield(tamanho=1, tipo="char", ordem=49)
	private String debitoCartaoCredito;
	
	@FTFfield(tamanho=17, tipo="char", ordem=50)
	private String numCartaoCredito;
	
	@FTFfield(tamanho=15, tipo="char", ordem=51)
	private String numContratoPermuta;
	
	@FTFfield(tamanho=255, tipo="char", ordem=52)
	private String textoObservacoes;
	
	@FTFfield(tamanho=15, tipo="char", ordem=53)
	private String numFaturaCliente;
	
	@FTFfield(tamanho=15, tipo="char", ordem=54)
	private String valorDescontoComercial;
	
	@FTFfield(tamanho=11, tipo="char", ordem=55)
	private String codUnidadeOperacionalEmpresaEmissora;
	
	@FTFfield(tamanho=1, tipo="char", ordem=56)
	private String indicadorResponsavelPeloFrete;
	
	@FTFfield(tamanho=1, tipo="char", ordem=57)
	private String statusPedido;
	
	@FTFfield(tamanho=30, tipo="char", ordem=58)
	private String numPedidoCliente;
	
	@FTFfield(tamanho=10, tipo="char", ordem=59)
	private String codLinhaDistribuicao;
	
	@FTFfield(tamanho=4, tipo="char", ordem=60)
	private String codResponsavelPeloFrete;
	
	@FTFfield(tamanho=10, tipo="char", ordem=61)
	private String numSimulacao;
	
	@FTFfield(tamanho=1, tipo="char", ordem=62)
	private String tipoOperacao;
	
	@FTFfield(tamanho=10, tipo="char", ordem=63)
	private String numAprovacao;
	
	@FTFfield(tamanho=2, tipo="char", ordem=64)
	private String situacaoTitulo;
	
	@FTFfield(tamanho=11, tipo="char", ordem=65)
	private String codUnidadeOperacionalEmpresaDestinataria;
	
	@FTFfield(tamanho=5, tipo="numeric", ordem=66)
	private String percentualDescontoFidelidade;
	
	@FTFfield(tamanho=30, tipo="char", ordem=67)
	private String pracaEntrega;
	
	@FTFfield(tamanho=9, tipo="numeric", ordem=68)
	private String codAssinanteCdms;
	
	@FTFfield(tamanho=3, tipo="numeric", ordem=69)
	private String codProjetoSize3;
	
	@FTFfield(tamanho=3, tipo="numeric", ordem=70)
	private String numAssinatura;
	
	@FTFfield(tamanho=14, tipo="char", ordem=71)
	private String cpfCnpjEstabelecimentoColeta;
	
	@FTFfield(tamanho=14, tipo="char", ordem=72)
	private String cnpjTransportadoraSuframa;
	
	@FTFfield(tamanho=2, tipo="char", ordem=73)
	private String ufPlacaVeiculoTransportadora;

	@FTFfield(tamanho=60, tipo="char", ordem=74)
	private String emailDestinatarioNota;
	
	@FTFfield(tamanho=7, tipo="numeric", ordem=75)
	private String codProjetoSize7;
	
	@FTFfield(tamanho=3, tipo="numeric", ordem=76)
	private String sequenciaAssinatura;
	
	@FTFfield(tamanho=10, tipo="numeric", ordem=77)
	private String codContrato;
	
	@FTFfield(tamanho=38, tipo="char", ordem=78)
	private String codLinhaContrato;
	
	@FTFfield(tamanho=8, tipo="char", ordem=79)
	private String codSistemaOrigem;
	
	@FTFfield(tamanho=255, tipo="char", ordem=80)
	private String mensagemFiscalImpressaNota;
	
	/**
	 * <h1> Hora/Minuto/Segundo da saída da nota/mercadoria </h1>
	 */
	@FTFfield(tamanho=6, tipo="char", ordem=81)
	private String horaSaidaNotaMercadoria;
	
	@FTFfield(tamanho=4, tipo="char", ordem=82)
	private String aliquotaIssImpostoDevido;
	
	@FTFfield(tamanho=60, tipo="char", ordem=83)
	private String nomeMunicipioServicoPrestado;
	
	@FTFfield(tamanho=22, tipo="char", ordem=84)
	private String numNotaEmpenho;
	
	@FTFfield(tamanho=9, tipo="numeric", ordem=85)
	private String codViagem;
	
	@FTFfield(tamanho=12, tipo="numeric", ordem=86)
	private String codFaturaAssociada;

	@FTFfield(tamanho=10, tipo="numeric", ordem=87)
	private String codigoPessoaEmissor;

	@FTFfield(tamanho=10, tipo="numeric", ordem=88)
	private String codigoPessoaDetalheDestinatario;
	
	@FTFfield(tamanho=10, tipo="numeric", ordem=89)
	private String codigoCGLServicoPrestado;
	
	@FTFfield(tamanho=10, tipo="char", ordem=90)
	private String dataInicioPrestacaoServicoIntangivel;
	
	@FTFfield(tamanho=10, tipo="char", ordem=91)
	private String dataConclusaoPrestacaoServicoIntangivel;
	
	@FTFfield(tamanho=1, tipo="numeric", ordem=92)
	private String modoPrestacaoServico;
	
	@FTFfield(tamanho=1, tipo="numeric", ordem=93)
	private String identificaoVendaConsumidorFinal;
	
	@FTFfield(tamanho=1, tipo="numeric", ordem=94)
	private String identificaoVendaPresencialAtendimento;
	
	@FTFfield(tamanho=2, tipo="char", ordem=95)
	private String siglaUfEmbarque;
	
	@FTFfield(tamanho=60, tipo="char", ordem=96)
	private String localEmbarque;
	
	@FTFfield(tamanho=60, tipo="char", ordem=97)
	private String localDespacho;
	
	private String tipoPessoaDestinatario;
	
	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getCodSolicitante() {
		return codSolicitante;
	}

	public void setCodSolicitante(String codSolicitante) {
		this.codSolicitante = codSolicitante;
	}

	public String getNomeLocalEntregaNf() {
		return nomeLocalEntregaNf;
	}

	public void setNomeLocalEntregaNf(String nomeLocalEntregaNf) {
		this.nomeLocalEntregaNf = nomeLocalEntregaNf;
	}

	public String getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(String dataPedido) {
		this.dataPedido = dataPedido;
	}

	public String getCodCentroEmissor() {
		return codCentroEmissor;
	}

	public void setCodCentroEmissor(String codCentroEmissor) {
		this.codCentroEmissor = codCentroEmissor;
	}

	public String getCpfCnpjDestinatario() {
		return cpfCnpjDestinatario;
	}

	public void setCpfCnpjDestinatario(String cpfCnpjDestinatario) {
		this.cpfCnpjDestinatario = cpfCnpjDestinatario;
	}

	public String getCpfCnpjEstabelecimentoEntrega() {
		return cpfCnpjEstabelecimentoEntrega;
	}

	public void setCpfCnpjEstabelecimentoEntrega(
			String cpfCnpjEstabelecimentoEntrega) {
		this.cpfCnpjEstabelecimentoEntrega = cpfCnpjEstabelecimentoEntrega;
	}

	public String getCodDestinatarioSistemaOrigem() {
		return codDestinatarioSistemaOrigem;
	}

	public void setCodDestinatarioSistemaOrigem(String codDestinatarioSistemaOrigem) {
		this.codDestinatarioSistemaOrigem = codDestinatarioSistemaOrigem;
	}

	public String getCodCondicaoPagamento() {
		return codCondicaoPagamento;
	}

	public void setCodCondicaoPagamento(String codCondicaoPagamento) {
		this.codCondicaoPagamento = codCondicaoPagamento;
	}

	public String getNumInscricaoEstadual() {
		return numInscricaoEstadual;
	}

	public void setNumInscricaoEstadual(String numInscricaoEstadual) {
		this.numInscricaoEstadual = numInscricaoEstadual;
	}

	public String getNumDeclaracaoImportacao() {
		return numDeclaracaoImportacao;
	}

	public void setNumDeclaracaoImportacao(String numDeclaracaoImportacao) {
		this.numDeclaracaoImportacao = numDeclaracaoImportacao;
	}

	public String getValorDespesasAcessorias() {
		return valorDespesasAcessorias;
	}

	public void setValorDespesasAcessorias(String valorDespesasAcessorias) {
		this.valorDespesasAcessorias = valorDespesasAcessorias;
	}

	public String getValorVariacaoCambial() {
		return valorVariacaoCambial;
	}

	public void setValorVariacaoCambial(String valorVariacaoCambial) {
		this.valorVariacaoCambial = valorVariacaoCambial;
	}

	public String getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(String valorFrete) {
		this.valorFrete = valorFrete;
	}

	public String getValorSeguro() {
		return valorSeguro;
	}

	public void setValorSeguro(String valorSeguro) {
		this.valorSeguro = valorSeguro;
	}

	public String getValorOutrasDespesas() {
		return valorOutrasDespesas;
	}

	public void setValorOutrasDespesas(String valorOutrasDespesas) {
		this.valorOutrasDespesas = valorOutrasDespesas;
	}

	public String getValorComplemento() {
		return valorComplemento;
	}

	public void setValorComplemento(String valorComplemento) {
		this.valorComplemento = valorComplemento;
	}

	public String getValorIcmsComplemento() {
		return valorIcmsComplemento;
	}

	public void setValorIcmsComplemento(String valorIcmsComplemento) {
		this.valorIcmsComplemento = valorIcmsComplemento;
	}

	public String getValorIpiComplemento() {
		return valorIpiComplemento;
	}

	public void setValorIpiComplemento(String valorIpiComplemento) {
		this.valorIpiComplemento = valorIpiComplemento;
	}

	public String getImprimePercentualDesconto() {
		return imprimePercentualDesconto;
	}

	public void setImprimePercentualDesconto(String imprimePercentualDesconto) {
		this.imprimePercentualDesconto = imprimePercentualDesconto;
	}

	public String getCodMotivoCancelamento() {
		return codMotivoCancelamento;
	}

	public void setCodMotivoCancelamento(String codMotivoCancelamento) {
		this.codMotivoCancelamento = codMotivoCancelamento;
	}

	public String getIndicadorRefaturamento() {
		return indicadorRefaturamento;
	}

	public void setIndicadorRefaturamento(String indicadorRefaturamento) {
		this.indicadorRefaturamento = indicadorRefaturamento;
	}

	public String getCodMoeda() {
		return codMoeda;
	}

	public void setCodMoeda(String codMoeda) {
		this.codMoeda = codMoeda;
	}

	public String getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(String dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	public String getNumSerieNfOrigem() {
		return numSerieNfOrigem;
	}

	public void setNumSerieNfOrigem(String numSerieNfOrigem) {
		this.numSerieNfOrigem = numSerieNfOrigem;
	}

	public String getNumNfOrigem() {
		return numNfOrigem;
	}

	public void setNumNfOrigem(String numNfOrigem) {
		this.numNfOrigem = numNfOrigem;
	}

	public String getCpfCnpjTransportadora() {
		return cpfCnpjTransportadora;
	}

	public void setCpfCnpjTransportadora(String cpfCnpjTransportadora) {
		this.cpfCnpjTransportadora = cpfCnpjTransportadora;
	}

	public String getNumPlacaVeiculo() {
		return numPlacaVeiculo;
	}

	public void setNumPlacaVeiculo(String numPlacaVeiculo) {
		this.numPlacaVeiculo = numPlacaVeiculo;
	}

	public String getTipoEmbalagem() {
		return tipoEmbalagem;
	}

	public void setTipoEmbalagem(String tipoEmbalagem) {
		this.tipoEmbalagem = tipoEmbalagem;
	}

	public String getQuantidadeVolumes() {
		return quantidadeVolumes;
	}

	public void setQuantidadeVolumes(String quantidadeVolumes) {
		this.quantidadeVolumes = quantidadeVolumes;
	}

	public String getPesoLiquido() {
		return pesoLiquido;
	}

	public void setPesoLiquido(String pesoLiquido) {
		this.pesoLiquido = pesoLiquido;
	}

	public String getPesoBruto() {
		return pesoBruto;
	}

	public void setPesoBruto(String pesoBruto) {
		this.pesoBruto = pesoBruto;
	}

	public String getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(String dataSaida) {
		this.dataSaida = dataSaida;
	}

	public String getNumContaBanco() {
		return numContaBanco;
	}

	public void setNumContaBanco(String numContaBanco) {
		this.numContaBanco = numContaBanco;
	}

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public String getEnvioBoleto() {
		return envioBoleto;
	}

	public void setEnvioBoleto(String envioBoleto) {
		this.envioBoleto = envioBoleto;
	}

	public String getFormaFaturamento() {
		return formaFaturamento;
	}

	public void setFormaFaturamento(String formaFaturamento) {
		this.formaFaturamento = formaFaturamento;
	}

	public String getPercentualComissaoIntermediaio() {
		return percentualComissaoIntermediaio;
	}

	public void setPercentualComissaoIntermediaio(
			String percentualComissaoIntermediaio) {
		this.percentualComissaoIntermediaio = percentualComissaoIntermediaio;
	}

	public String getPedidoInsercao() {
		return pedidoInsercao;
	}

	public void setPedidoInsercao(String pedidoInsercao) {
		this.pedidoInsercao = pedidoInsercao;
	}

	public String getCpfCnpjIntermediario() {
		return cpfCnpjIntermediario;
	}

	public void setCpfCnpjIntermediario(String cpfCnpjIntermediario) {
		this.cpfCnpjIntermediario = cpfCnpjIntermediario;
	}

	public String getCodIntermediarioSistemaOrigem() {
		return codIntermediarioSistemaOrigem;
	}

	public void setCodIntermediarioSistemaOrigem(
			String codIntermediarioSistemaOrigem) {
		this.codIntermediarioSistemaOrigem = codIntermediarioSistemaOrigem;
	}

	public String getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(String valorBruto) {
		this.valorBruto = valorBruto;
	}

	public String getNumeroFaturaAssociada() {
		return numeroFaturaAssociada;
	}

	public void setNumeroFaturaAssociada(String numeroFaturaAssociada) {
		this.numeroFaturaAssociada = numeroFaturaAssociada;
	}

	public String getDebitoCartaoCredito() {
		return debitoCartaoCredito;
	}

	public void setDebitoCartaoCredito(String debitoCartaoCredito) {
		this.debitoCartaoCredito = debitoCartaoCredito;
	}

	public String getNumCartaoCredito() {
		return numCartaoCredito;
	}

	public void setNumCartaoCredito(String numCartaoCredito) {
		this.numCartaoCredito = numCartaoCredito;
	}

	public String getNumContratoPermuta() {
		return numContratoPermuta;
	}

	public void setNumContratoPermuta(String numContratoPermuta) {
		this.numContratoPermuta = numContratoPermuta;
	}

	public String getTextoObservacoes() {
		return textoObservacoes;
	}

	public void setTextoObservacoes(String textoObservacoes) {
		this.textoObservacoes = textoObservacoes;
	}

	public String getNumFaturaCliente() {
		return numFaturaCliente;
	}

	public void setNumFaturaCliente(String numFaturaCliente) {
		this.numFaturaCliente = numFaturaCliente;
	}

	public String getValorDescontoComercial() {
		return valorDescontoComercial;
	}

	public void setValorDescontoComercial(String valorDescontoComercial) {
		this.valorDescontoComercial = valorDescontoComercial;
	}

	public String getCodUnidadeOperacionalEmpresaEmissora() {
		return codUnidadeOperacionalEmpresaEmissora;
	}

	public void setCodUnidadeOperacionalEmpresaEmissora(
			String codUnidadeOperacionalEmpresaEmissora) {
		this.codUnidadeOperacionalEmpresaEmissora = codUnidadeOperacionalEmpresaEmissora;
	}

	public String getIndicadorResponsavelPeloFrete() {
		return indicadorResponsavelPeloFrete;
	}

	public void setIndicadorResponsavelPeloFrete(
			String indicadorResponsavelPeloFrete) {
		this.indicadorResponsavelPeloFrete = indicadorResponsavelPeloFrete;
	}

	public String getStatusPedido() {
		return statusPedido;
	}

	public void setStatusPedido(String statusPedido) {
		this.statusPedido = statusPedido;
	}

	public String getNumPedidoCliente() {
		return numPedidoCliente;
	}

	public void setNumPedidoCliente(String numPedidoCliente) {
		this.numPedidoCliente = numPedidoCliente;
	}

	public String getCodLinhaDistribuicao() {
		return codLinhaDistribuicao;
	}

	public void setCodLinhaDistribuicao(String codLinhaDistribuicao) {
		this.codLinhaDistribuicao = codLinhaDistribuicao;
	}

	public String getCodResponsavelPeloFrete() {
		return codResponsavelPeloFrete;
	}

	public void setCodResponsavelPeloFrete(String codResponsavelPeloFrete) {
		this.codResponsavelPeloFrete = codResponsavelPeloFrete;
	}

	public String getNumSimulacao() {
		return numSimulacao;
	}

	public void setNumSimulacao(String numSimulacao) {
		this.numSimulacao = numSimulacao;
	}

	public String getTipoOperacao() {
		return tipoOperacao;
	}

	public void setTipoOperacao(String tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	public String getNumAprovacao() {
		return numAprovacao;
	}

	public void setNumAprovacao(String numAprovacao) {
		this.numAprovacao = numAprovacao;
	}

	public String getSituacaoTitulo() {
		return situacaoTitulo;
	}

	public void setSituacaoTitulo(String situacaoTitulo) {
		this.situacaoTitulo = situacaoTitulo;
	}

	public String getCodUnidadeOperacionalEmpresaDestinataria() {
		return codUnidadeOperacionalEmpresaDestinataria;
	}

	public void setCodUnidadeOperacionalEmpresaDestinataria(
			String codUnidadeOperacionalEmpresaDestinataria) {
		this.codUnidadeOperacionalEmpresaDestinataria = codUnidadeOperacionalEmpresaDestinataria;
	}

	public String getPercentualDescontoFidelidade() {
		return percentualDescontoFidelidade;
	}

	public void setPercentualDescontoFidelidade(String percentualDescontoFidelidade) {
		this.percentualDescontoFidelidade = percentualDescontoFidelidade;
	}

	public String getPracaEntrega() {
		return pracaEntrega;
	}

	public void setPracaEntrega(String pracaEntrega) {
		this.pracaEntrega = pracaEntrega;
	}

	public String getCodAssinanteCdms() {
		return codAssinanteCdms;
	}

	public void setCodAssinanteCdms(String codAssinanteCdms) {
		this.codAssinanteCdms = codAssinanteCdms;
	}

	public String getCodProjetoSize3() {
		return codProjetoSize3;
	}

	public void setCodProjetoSize3(String codProjetoSize3) {
		this.codProjetoSize3 = codProjetoSize3;
	}

	public String getNumAssinatura() {
		return numAssinatura;
	}

	public void setNumAssinatura(String numAssinatura) {
		this.numAssinatura = numAssinatura;
	}

	public String getCpfCnpjEstabelecimentoColeta() {
		return cpfCnpjEstabelecimentoColeta;
	}

	public void setCpfCnpjEstabelecimentoColeta(String cpfCnpjEstabelecimentoColeta) {
		this.cpfCnpjEstabelecimentoColeta = cpfCnpjEstabelecimentoColeta;
	}

	public String getCnpjTransportadoraSuframa() {
		return cnpjTransportadoraSuframa;
	}

	public void setCnpjTransportadoraSuframa(String cnpjTransportadoraSuframa) {
		this.cnpjTransportadoraSuframa = cnpjTransportadoraSuframa;
	}

	public String getUfPlacaVeiculoTransportadora() {
		return ufPlacaVeiculoTransportadora;
	}

	public void setUfPlacaVeiculoTransportadora(String ufPlacaVeiculoTransportadora) {
		this.ufPlacaVeiculoTransportadora = ufPlacaVeiculoTransportadora;
	}

	public String getEmailDestinatarioNota() {
		return emailDestinatarioNota;
	}

	public void setEmailDestinatarioNota(String emailDestinatarioNota) {
		this.emailDestinatarioNota = emailDestinatarioNota;
	}

	public String getCodProjetoSize7() {
		return codProjetoSize7;
	}

	public void setCodProjetoSize7(String codProjetoSize7) {
		this.codProjetoSize7 = codProjetoSize7;
	}

	public String getSequenciaAssinatura() {
		return sequenciaAssinatura;
	}

	public void setSequenciaAssinatura(String sequenciaAssinatura) {
		this.sequenciaAssinatura = sequenciaAssinatura;
	}

	public String getCodContrato() {
		return codContrato;
	}

	public void setCodContrato(String codContrato) {
		this.codContrato = codContrato;
	}

	public String getCodLinhaContrato() {
		return codLinhaContrato;
	}

	public void setCodLinhaContrato(String codLinhaContrato) {
		this.codLinhaContrato = codLinhaContrato;
	}

	public String getCodSistemaOrigem() {
		return codSistemaOrigem;
	}

	public void setCodSistemaOrigem(String codSistemaOrigem) {
		this.codSistemaOrigem = codSistemaOrigem;
	}

	public String getMensagemFiscalImpressaNota() {
		return mensagemFiscalImpressaNota;
	}

	public void setMensagemFiscalImpressaNota(String mensagemFiscalImpressaNota) {
		this.mensagemFiscalImpressaNota = mensagemFiscalImpressaNota;
	}

	public String getHoraSaidaNotaMercadoria() {
		return horaSaidaNotaMercadoria;
	}

	public void setHoraSaidaNotaMercadoria(String horaSaidaNotaMercadoria) {
		this.horaSaidaNotaMercadoria = horaSaidaNotaMercadoria;
	}

	public String getAliquotaIssImpostoDevido() {
		return aliquotaIssImpostoDevido;
	}

	public void setAliquotaIssImpostoDevido(String aliquotaIssImpostoDevido) {
		this.aliquotaIssImpostoDevido = aliquotaIssImpostoDevido;
	}

	public String getNomeMunicipioServicoPrestado() {
		return nomeMunicipioServicoPrestado;
	}

	public void setNomeMunicipioServicoPrestado(String nomeMunicipioServicoPrestado) {
		this.nomeMunicipioServicoPrestado = nomeMunicipioServicoPrestado;
	}

	public String getNumNotaEmpenho() {
		return numNotaEmpenho;
	}

	public void setNumNotaEmpenho(String numNotaEmpenho) {
		this.numNotaEmpenho = numNotaEmpenho;
	}

	public String getCodViagem() {
		return codViagem;
	}

	public void setCodViagem(String codViagem) {
		this.codViagem = codViagem;
	}

	public String getCodFaturaAssociada() {
		return codFaturaAssociada;
	}

	public void setCodFaturaAssociada(String codFaturaAssociada) {
		this.codFaturaAssociada = codFaturaAssociada;
	}
	
	public String getCodigoCentroEmissor() {
		return codigoCentroEmissor;
	}

	public void setCodigoCentroEmissor(String codigoCentroEmissor) {
		codigoCentroEmissor = codigoCentroEmissor != null ? StringUtils.leftPad(codigoCentroEmissor, 2, '0') : StringUtils.leftPad(codigoCentroEmissor, ' ', '0');
		this.codigoCentroEmissor = codigoCentroEmissor;
	}

	public String getCnpjEmpresaEmissora() {
		return cnpjEmpresaEmissora;
	}

	public void setCnpjEmpresaEmissora(String cnpjEmpresaEmissora) {
		this.cnpjEmpresaEmissora = cnpjEmpresaEmissora != null ? cnpjEmpresaEmissora.replaceAll("\\D+","") : null;
	}

	public String getCodLocal() {
		return codLocal;
	}

	public void setCodLocal(String codLocal) {
		this.codLocal = codLocal != null ? StringUtils.leftPad(codLocal, 11, '0') : StringUtils.leftPad("", 11, '0');
	}

	public String getTipoPedido() {
		return tipoPedido;
	}

	public void setTipoPedido(String tipoPedido) {
		this.tipoPedido = tipoPedido != null ? StringUtils.leftPad(tipoPedido, 2, '0') : StringUtils.leftPad(tipoPedido, ' ', '0');
	}

	public String getNumeroDocOrigem() {
		return numeroDocOrigem;
	}

	public void setNumeroDocOrigem(String numeroDocOrigem) {
		this.numeroDocOrigem = numeroDocOrigem;
	}
	
	public String getCodigoPessoaEmissor() {
		return codigoPessoaEmissor;
	}

	public void setCodigoPessoaEmissor(String codigoPessoaEmissor) {
		this.codigoPessoaEmissor = codigoPessoaEmissor;
	}

	public String getCodigoPessoaDetalheDestinatario() {
		return codigoPessoaDetalheDestinatario;
	}

	public void setCodigoPessoaDetalheDestinatario(
			String codigoPessoaDetalheDestinatario) {
		this.codigoPessoaDetalheDestinatario = codigoPessoaDetalheDestinatario;
	}

	public String getCodigoCGLServicoPrestado() {
		return codigoCGLServicoPrestado;
	}

	public void setCodigoCGLServicoPrestado(String codigoCGLServicoPrestado) {
		this.codigoCGLServicoPrestado = codigoCGLServicoPrestado;
	}

	public String getDataInicioPrestacaoServicoIntangivel() {
		return dataInicioPrestacaoServicoIntangivel;
	}

	public void setDataInicioPrestacaoServicoIntangivel(
			String dataInicioPrestacaoServicoIntangivel) {
		this.dataInicioPrestacaoServicoIntangivel = dataInicioPrestacaoServicoIntangivel;
	}

	public String getDataConclusaoPrestacaoServicoIntangivel() {
		return dataConclusaoPrestacaoServicoIntangivel;
	}

	public void setDataConclusaoPrestacaoServicoIntangivel(
			String dataConclusaoPrestacaoServicoIntangivel) {
		this.dataConclusaoPrestacaoServicoIntangivel = dataConclusaoPrestacaoServicoIntangivel;
	}

	public String getModoPrestacaoServico() {
		return modoPrestacaoServico;
	}

	public void setModoPrestacaoServico(String modoPrestacaoServico) {
		this.modoPrestacaoServico = modoPrestacaoServico;
	}

	public String getIdentificaoVendaConsumidorFinal() {
		return identificaoVendaConsumidorFinal;
	}

	public void setIdentificaoVendaConsumidorFinal(
			String identificaoVendaConsumidorFinal) {
		this.identificaoVendaConsumidorFinal = identificaoVendaConsumidorFinal;
	}

	public String getIdentificaoVendaPresencialAtendimento() {
		return identificaoVendaPresencialAtendimento;
	}

	public void setIdentificaoVendaPresencialAtendimento(
			String identificaoVendaPresencialAtendimento) {
		this.identificaoVendaPresencialAtendimento = identificaoVendaPresencialAtendimento;
	}

	public String getSiglaUfEmbarque() {
		return siglaUfEmbarque;
	}

	public void setSiglaUfEmbarque(String siglaUfEmbarque) {
		this.siglaUfEmbarque = siglaUfEmbarque;
	}

	public String getLocalEmbarque() {
		return localEmbarque;
	}

	public void setLocalEmbarque(String localEmbarque) {
		this.localEmbarque = localEmbarque;
	}

	public String getLocalDespacho() {
		return localDespacho;
	}

	public void setLocalDespacho(String localDespacho) {
		this.localDespacho = localDespacho;
	}

	public List<FTFEnvTipoRegistro02> getItemNFList() {
		return itemNFList;
	}

	public FTFEnvTipoRegistro06 getRegTipo06() {
		return regTipo06;
	}

	public void setRegTipo06(FTFEnvTipoRegistro06 regTipo06) {
		this.regTipo06 = regTipo06;
	}

	public FTFEnvTipoRegistro08 getRegTipo08() {
		return regTipo08;
	}

	public void setRegTipo08(FTFEnvTipoRegistro08 regTipo08) {
		this.regTipo08 = regTipo08;
	}

	public void setItemNFList(List<FTFEnvTipoRegistro02> itemNFList) {
		this.itemNFList = itemNFList;
	}

	public List<FTFEnvTipoRegistro03> getItemNFList03() {
		return itemNFList03;
	}

	public void setItemNFList03(List<FTFEnvTipoRegistro03> itemNFList03) {
		this.itemNFList03 = itemNFList03;
	}

	public String getTipoPessoaDestinatario() {
		return tipoPessoaDestinatario;
	}

	public void setTipoPessoaDestinatario(String tipoPessoaDestinatario) {
		this.tipoPessoaDestinatario = tipoPessoaDestinatario;
	}
	
}