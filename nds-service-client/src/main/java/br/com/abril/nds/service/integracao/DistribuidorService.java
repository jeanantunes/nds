package br.com.abril.nds.service.integracao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.DistribuidorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ObrigacaoFiscal;
import br.com.abril.nds.model.cadastro.ParametrosRecolhimentoDistribuidor;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.cadastro.TipoContabilizacaoCE;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoImpressaoCE;
import br.com.abril.nds.model.cadastro.TipoImpressaoNENECADANFE;
import br.com.abril.nds.model.cadastro.TipoStatusGarantia;

public interface DistribuidorService {
	
	public Distribuidor obter ();
	
	public boolean isDistribuidor(Integer codigo);
	
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

	DiaSemana inicioSemana();

	Boolean utilizaSugestaoIncrementoCodigo();

	String getEmail();

	ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor();
	
	String obterRazaoSocialDistribuidor();

	TipoImpressaoCE tipoImpressaoCE();

	Integer qntDiasVencinemtoVendaEncalhe();

	Integer negociacaoAteParcelas();

	Integer qtdDiasLimiteParaReprogLancamento();

	ObrigacaoFiscal obrigacaoFiscal();

	TipoImpressaoNENECADANFE tipoImpressaoNENECADANFE();

	String cidadeDistribuidor();

	String codigoDistribuidorDinap();

	String codigoDistribuidorFC();

	Integer diasNegociacao();

	TipoContabilizacaoCE tipoContabilizacaoCE();

	Boolean preenchimentoAutomaticoPDV();

	Long qntDiasReutilizacaoCodigoCota();

	Set<PoliticaCobranca> politicasCobranca();

	String assuntoEmailCobranca();

	String mensagemEmailCobranca();

	Boolean regimeEspecial();

	TipoAtividade tipoAtividade();

	Integer fatorRelancamentoParcial();

	Long obterId();
}