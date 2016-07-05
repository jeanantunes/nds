package br.com.abril.nds.service.integracao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DistribuidorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.DistribuidorGridDistribuicao;
import br.com.abril.nds.model.cadastro.ParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.cadastro.ParametrosRecolhimentoDistribuidor;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.cadastro.TipoContabilizacaoCE;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoImpressaoCE;
import br.com.abril.nds.model.cadastro.TipoImpressaoNENECADANFE;
import br.com.abril.nds.model.cadastro.TipoParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.cadastro.TipoStatusGarantia;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;

public interface DistribuidorService {
	
	public Distribuidor obter();
	
	public boolean isDistribuidor(Integer codigo);
	
	public boolean distribuidorAceitaRecolhimentoParcialAtraso();
	
	public void alterar(Distribuidor distribuidor);

	public DistribuidorDTO obterDadosEmissao();

	List<String> obterNomeCNPJDistribuidor();

	int obterOrdinalUltimoDiaRecolhimento();
	
	List<ItemDTO<TipoGarantia, String>> getComboTiposGarantia();
	
	List<ItemDTO<TipoStatusGarantia, String>> getComboTiposStatusGarantia();

	Date obterDataOperacaoDistribuidor();

	boolean utilizaGarantiaPdv();

	boolean aceitaJuramentado();

	int qtdDiasEncalheAtrasadoAceitavel();

	boolean utilizaControleAprovacao();

	Boolean utilizaTermoAdesao();

	Boolean utilizaProcuracaoEntregadores();

	DiaSemana inicioSemanaRecolhimento();
	
	DiaSemana inicioSemanaLancamento();

	Boolean utilizaSugestaoIncrementoCodigo();

	String getEmail();

	ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor();
	
	String obterRazaoSocialDistribuidor();

	TipoImpressaoCE tipoImpressaoCE();

	Integer qntDiasVencinemtoVendaEncalhe();

	Integer negociacaoAteParcelas();

	Integer qtdDiasLimiteParaReprogLancamento();

	boolean obrigacaoFiscal(NaturezaOperacao naturezaOperacao, Distribuidor distribuidor);

	TipoImpressaoNENECADANFE tipoImpressaoNENECADANFE();

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
	
	boolean utilizaControleAprovacaoFaltaSobra();
	
	public List<Integer> getListaDiaOrdinalAceitaRecolhimento();
	
	List<Date> obterListaDataOperacional(
			Date dataAtual, 
			int qtndDiasUteis, 
			final List<Integer> diasSemanaDistribuidorOpera,
			final boolean posterior);
	
	/**
	 * Método que devolve uma lista de datas nas quais o recolhimento será permitido
	 * a partir da dataRecolhimento e fornecedores informados. 
	 * 
	 * O método utiliza as seguinte parametrizações feita em 'Parâmetros Distribuidor':
	 * 
	 * Dias de Recolhimento -> Ordinal dos dias em que é aceito o recolhimento (1º. 2º, ... 5º dias)
	 * Recolhimento 		-> Dias da semana em que é aceito o recolhimento por fornecedor.
	 * 
	 * 
	 * 
	 * @param dataRecolhimento
	 * @param idsFornecedor
	 * 
	 * @return List - Date
	 */
	List<Date> obterDatasAposFinalizacaoPrazoRecolhimento(Date dataRecolhimento, Long ...idsFornecedor);
	
	/**
	 * Obtem o dia de recolhimento do distribuidor para a data de Conferencia 
	 * divergente da data de Recolhimento prevista
	 * 
	 * @param dataConferencia
	 * @param dataRecolhimento
	 * @param numeroCota
	 * @param idProdutoEdicao
	 * @param idFornecedores
	 * @param isCotaOperacaoDiferenciada TODO
	 * @return Integer
	 */
	Integer obterDiaDeRecolhimentoDaData(
			Date dataConferencia, 
			Date dataRecolhimento, 
			Integer numeroCota, 
			Long idProdutoEdicao,
			List<Long> idFornecedores, Boolean isCotaOperacaoDiferenciada);
	
	/**
	 * Bloqueia os processos que alteram estudos ou balanceamentos de matriz
	 */
	void bloqueiaProcessosLancamentosEstudos();

	/**
	 * Desbloqueia os processos que alteram estudos ou balanceamentos de matriz
	 */
	void desbloqueiaProcessosLancamentosEstudos();

	/**
	 * Verifica se os processos que alteram estudos ou balanceamentos de matriz devem ser bloqueados
	 * Tambem desbloqueia caso a interface esteja sendo executada a mais de uma hora (pois pode ser que a interface tenha travado)
	 * @return boolean
	 */
	boolean verificaDesbloqueioProcessosLancamentosEstudos();
	
	boolean isConferenciaCegaRecebimentoFisico();
	
	boolean isConferenciaCegaFechamentoEncalhe();
	
	Set<NaturezaOperacao> obterNaturezasOperacoesNotasEnvio();

	Distribuidor obterParaNFe();

    Integer obterNumeroSemana(Date data);

	DistribuidorGridDistribuicao obterGridDistribuicaoDistribuidor();

	boolean verificarParametroDistribuidorEmissaoDocumentosImpressaoCheck(Distribuidor distribuidor, TipoParametrosDistribuidorEmissaoDocumento tipoDoc);

	boolean verificarParametroDistribuidorEmissaoDocumentosEmailCheck(Distribuidor distribuidor, TipoParametrosDistribuidorEmissaoDocumento tipoDoc);
}