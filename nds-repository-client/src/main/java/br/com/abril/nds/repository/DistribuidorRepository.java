package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametroContratoCota;
import br.com.abril.nds.model.cadastro.ParametrosAprovacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.cadastro.ParametrosRecolhimentoDistribuidor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.cadastro.TipoContabilizacaoCE;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoImpressaoCE;
import br.com.abril.nds.model.cadastro.TipoImpressaoNENECADANFE;

public interface DistribuidorRepository extends Repository<Distribuidor, Long> {
	
	Distribuidor obter();
	
	List<DistribuicaoFornecedor> buscarDiasDistribuicaoFornecedor(
															Collection<Long> idsForncedores,
													    	OperacaoDistribuidor operacaoDistribuidor);
	
	public boolean buscarDistribuidorAceitaRecolhimentoParcialAtraso();
	
	/**
	 * Recupera o endereço principal do distribuidor.
	 * @return endereço principal do distribuidor.
	 */
	public abstract EnderecoDistribuidor obterEnderecoPrincipal();
	
	/**
	 * Recupera os tipos de garantias aceitas pelo distribuidor.
	 * @return
	 */
	public List<TipoGarantia> obtemTiposGarantiasAceitas();

	/**
	 * Recupera o telefone principal do distribuidor.
	 * @return telefone principal do distribuidor.
	 */
	public abstract TelefoneDistribuidor obterTelefonePrincipal();

	List<String> obterNomeCNPJDistribuidor();
	
	String obterInformacoesComplementaresProcuracao();

	String obterRazaoSocialDistribuidor();

	String obterInformacoesComplementaresTermoAdesao();

	DiaSemana buscarInicioSemanaRecolhimento();
	
	DiaSemana buscarInicioSemanaLancamento();
	
	Date obterDataOperacaoDistribuidor();

	BigDecimal obterDescontoCotaNegociacao();

	boolean utilizaGarantiaPdv();

	boolean aceitaJuramentado();

	int qtdDiasEncalheAtrasadoAceitavel();

	Integer obterNumeroDiasNovaCobranca();

	boolean utilizaControleAprovacao();

	Boolean utilizaTermoAdesao();

	Boolean utilizaProcuracaoEntregadores();

	Boolean utilizaSugestaoIncrementoCodigo();

	String getEmail();

	ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor();

	TipoImpressaoCE tipoImpressaoCE();

	Integer qntDiasVencinemtoVendaEncalhe();

	Boolean aceitaBaixaPagamentoVencido();

	Boolean aceitaBaixaPagamentoMaior();

	Boolean aceitaBaixaPagamentoMenor();

	Integer negociacaoAteParcelas();

	Integer qtdDiasLimiteParaReprogLancamento();

	boolean obrigacaoFiscal();

	TipoImpressaoNENECADANFE tipoImpressaoNENECADANFE();

	BigInteger capacidadeRecolhimento();

	String cidadeDistribuidor();

	String codigoDistribuidorDinap();

	String codigoDistribuidorFC();

	Integer diasNegociacao();

	TipoContabilizacaoCE tipoContabilizacaoCE();

	Boolean preenchimentoAutomaticoPDV();

	Long qntDiasReutilizacaoCodigoCota();

	Set<PoliticaCobranca> politicasCobranca();
	
	Set<PoliticaCobranca> politicasCobrancaAtivas();

	String assuntoEmailCobranca();

	String mensagemEmailCobranca();

	Boolean regimeEspecial();

	TipoAtividade tipoAtividade();

	Integer fatorRelancamentoParcial();

	Long obterId();

	String cnpj();

	List<ParametrosDistribuidorEmissaoDocumento> parametrosDistribuidorEmissaoDocumentos();

	Integer codigo();

	BigInteger capacidadeDistribuicao();

	PessoaJuridica juridica();

	ParametroContratoCota parametroContratoCota();

	ParametrosAprovacaoDistribuidor parametrosAprovacaoDistribuidor();
	
	boolean utilizaControleAprovacaoFaltaSobra();

	boolean isConferenciaCegaRecebimentoFisico();
	
	boolean naoAcumulaDividas();
	
	Integer numeroMaximoAcumuloDividas();
	
	List<DistribuicaoFornecedor> buscarDiasDistribuicaoFornecedor(
			OperacaoDistribuidor operacaoDistribuidor);

	boolean isConferenciaCegaFechamentoEncalhe();
}