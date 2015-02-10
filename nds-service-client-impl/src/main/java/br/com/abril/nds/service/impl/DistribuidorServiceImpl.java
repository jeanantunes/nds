package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DistribuidorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.DistribuidorTipoNotaFiscal;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.Feriado;
import br.com.abril.nds.model.cadastro.NotaFiscalTipoEmissao.NotaFiscalTipoEmissaoEnum;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametrosRecolhimentoDistribuidor;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.cadastro.TipoContabilizacaoCE;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoImpressaoCE;
import br.com.abril.nds.model.cadastro.TipoImpressaoNENECADANFE;
import br.com.abril.nds.model.cadastro.TipoStatusGarantia;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.repository.DistribuicaoFornecedorRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FeriadoRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.SemanaUtil;

@Service
public class DistribuidorServiceImpl implements DistribuidorService {

	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private DistribuicaoFornecedorRepository distribuicaoFornecedorRepository; 
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Autowired
	private ConferenciaEncalheService conferenciaEncalheService;

	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	FeriadoRepository feriadoRepository;
	
	@Override
	@Transactional(readOnly = true)
	public boolean isDistribuidor(final Integer codigo) {
		return this.codigoDistribuidorDinap().equals(codigo.toString()) || this.codigoDistribuidorFC().equals(codigo.toString());
	}

	@Override
	@Transactional
	public Distribuidor obter() {
		return distribuidorRepository.obter();
	}
	
	@SuppressWarnings("unused")
	@Override
	@Transactional
	public Distribuidor obterParaNFe() {
		Distribuidor distribuidor = distribuidorRepository.obter();
		Object o = distribuidor.getNaturezasOperacoesNotasEnvio() != null ? distribuidor.getNaturezasOperacoesNotasEnvio().isEmpty() : null;
		Object o2 = distribuidor.getRegimeTributarioTributoAliquota() != null ? distribuidor.getRegimeTributarioTributoAliquota().isEmpty() : null;
		if(distribuidor.getTiposNotaFiscalDistribuidor() != null && !distribuidor.getTiposNotaFiscalDistribuidor().isEmpty() ) {
			for(DistribuidorTipoNotaFiscal dtnf : distribuidor.getTiposNotaFiscalDistribuidor()) {
				if(dtnf.getNaturezaOperacao() != null) {
					dtnf.getNaturezaOperacao().isEmpty();				
				}
			}
		}
		return distribuidor;
	}

	@Override
	@Transactional
	public void alterar(final Distribuidor distribuidor) {
		distribuidorRepository.alterar(distribuidor);
	}

	@Override
	@Transactional
	public DistribuidorDTO obterDadosEmissao() {
		
		final Distribuidor distribuidor = this.obter();
		
		final DistribuidorDTO dto = new DistribuidorDTO();
		
		dto.setRazaoSocial(distribuidor.getJuridica().getRazaoSocial().toUpperCase());
		final Endereco endereco = distribuidor.getEnderecoDistribuidor().getEndereco(); 
		dto.setEndereco(endereco.getLogradouro().toUpperCase()  + " " + endereco.getNumero());
		dto.setCnpj(distribuidor.getJuridica().getCnpj());
		dto.setCidade(endereco.getCidade().toUpperCase());
		dto.setUf(endereco.getUf().toUpperCase());
		dto.setCep(endereco.getCep());
		dto.setInscricaoEstatual(distribuidor.getJuridica().getInscricaoEstadual());
		
		return dto;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<String> obterNomeCNPJDistribuidor(){
		
		return this.distribuidorRepository.obterNomeCNPJDistribuidor();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ItemDTO<TipoGarantia, String>> getComboTiposGarantia() {
		
		final List<ItemDTO<TipoGarantia,String>> comboTiposGarantia =  new ArrayList<ItemDTO<TipoGarantia,String>>();
		for (final TipoGarantia itemTipoGarantia: TipoGarantia.values()){
			comboTiposGarantia.add(new ItemDTO<TipoGarantia,String>(itemTipoGarantia, itemTipoGarantia.getDescTipoGarantia()));
		}
		return comboTiposGarantia;
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<ItemDTO<TipoStatusGarantia, String>> getComboTiposStatusGarantia() {
		
		final List<ItemDTO<TipoStatusGarantia,String>> comboTiposStatusGarantia =  new ArrayList<ItemDTO<TipoStatusGarantia,String>>();
		for (final TipoStatusGarantia itemTipoStatusGarantia: TipoStatusGarantia.values()){
			comboTiposStatusGarantia.add(new ItemDTO<TipoStatusGarantia,String>(itemTipoStatusGarantia, itemTipoStatusGarantia.getDescTipoStatusGarantia()));
		}
		return comboTiposStatusGarantia;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Date obterDataOperacaoDistribuidor(){
		
		return this.distribuidorRepository.obterDataOperacaoDistribuidor();
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean utilizaGarantiaPdv(){
		
		return this.distribuidorRepository.utilizaGarantiaPdv();
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean distribuidorAceitaRecolhimentoParcialAtraso() {
		
		return this.distribuidorRepository.buscarDistribuidorAceitaRecolhimentoParcialAtraso();
		
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean aceitaJuramentado(){
		
		return this.distribuidorRepository.aceitaJuramentado();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int qtdDiasEncalheAtrasadoAceitavel(){
		
		return this.distribuidorRepository.qtdDiasEncalheAtrasadoAceitavel();
	}

	@Override
	@Transactional(readOnly = true)
	public boolean utilizaControleAprovacao() {
		
		return this.distribuidorRepository.utilizaControleAprovacao();
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean utilizaControleAprovacaoFaltaSobra() {
		
		return this.distribuidorRepository.utilizaControleAprovacaoFaltaSobra();
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean utilizaTermoAdesao() {
		
		return this.distribuidorRepository.utilizaTermoAdesao();
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean utilizaProcuracaoEntregadores() {
		
		return this.distribuidorRepository.utilizaProcuracaoEntregadores();
	}

	@Override
	@Transactional(readOnly = true)
	public DiaSemana inicioSemanaRecolhimento() {
		
		return this.distribuidorRepository.buscarInicioSemanaRecolhimento();
	}
	
	@Override
	@Transactional(readOnly = true)
	public DiaSemana inicioSemanaLancamento() {
		
		return this.distribuidorRepository.buscarInicioSemanaLancamento();
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean utilizaSugestaoIncrementoCodigo() {
		
		return this.distribuidorRepository.utilizaSugestaoIncrementoCodigo();
	}

	@Override
	@Transactional(readOnly = true)
	public String getEmail() {
		
		return this.distribuidorRepository.getEmail();
	}

	@Override
	@Transactional(readOnly = true)
	public ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor() {
		
		return this.distribuidorRepository.parametrosRecolhimentoDistribuidor();
	}

	@Override
	@Transactional(readOnly = true)
	public String obterRazaoSocialDistribuidor() {
		
		return this.distribuidorRepository.obterRazaoSocialDistribuidor();
	}

	@Override
	@Transactional(readOnly = true)
	public TipoImpressaoCE tipoImpressaoCE() {
		
		return this.distribuidorRepository.tipoImpressaoCE();
	}

	@Override
	@Transactional(readOnly = true)
	public Integer qntDiasVencinemtoVendaEncalhe() {
		
		return this.distribuidorRepository.qntDiasVencinemtoVendaEncalhe();
	}

	@Override
	@Transactional(readOnly = true)
	public Integer negociacaoAteParcelas() {
		
		return this.distribuidorRepository.negociacaoAteParcelas();
	}

	@Override
	@Transactional(readOnly = true)
	public Integer qtdDiasLimiteParaReprogLancamento() {
		
		final Integer qtd = this.distribuidorRepository.qtdDiasLimiteParaReprogLancamento();
		
		return qtd == null ? 0 : qtd;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean obrigacaoFiscal(NaturezaOperacao naturezaOperacao, Distribuidor distribuidor) {
		
		for(DistribuidorTipoNotaFiscal dtnf :  distribuidor.getTiposNotaFiscalDistribuidor()) {
			if(dtnf.getNaturezaOperacao().contains(naturezaOperacao)) {
				if(dtnf.getTipoEmissao().getTipoEmissao().equals(NotaFiscalTipoEmissaoEnum.DESOBRIGA_EMISSAO)) {
					return false;
				}
		
			}
		}	
		return true;
	}

	@Override
	@Transactional(readOnly = true)
	public TipoImpressaoNENECADANFE tipoImpressaoNENECADANFE() {
		
		return this.distribuidorRepository.tipoImpressaoNENECADANFE();
	}

	@Override
	@Transactional(readOnly = true)
	public String cidadeDistribuidor() {
		
		return this.distribuidorRepository.cidadeDistribuidor();
	}

	@Override
	@Transactional(readOnly = true)
	public String codigoDistribuidorDinap() {
		
		return this.distribuidorRepository.codigoDistribuidorDinap();
	}

	@Override
	@Transactional(readOnly = true)
	public String codigoDistribuidorFC() {
		
		return this.distribuidorRepository.codigoDistribuidorFC();
	}

	@Override
	@Transactional(readOnly = true)
	public Integer diasNegociacao() {
		
		return this.distribuidorRepository.diasNegociacao();
	}

	@Override
	@Transactional(readOnly = true)
	public TipoContabilizacaoCE tipoContabilizacaoCE() {
		
		return this.distribuidorRepository.tipoContabilizacaoCE();
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean preenchimentoAutomaticoPDV() {
		
		return this.distribuidorRepository.preenchimentoAutomaticoPDV();
	}

	@Override
	@Transactional(readOnly = true)
	public Long qntDiasReutilizacaoCodigoCota() {
		
		return this.distribuidorRepository.qntDiasReutilizacaoCodigoCota();
	}

	@Override
	@Transactional(readOnly = true)
	public Set<PoliticaCobranca> politicasCobranca() {
		
		return this.distribuidorRepository.politicasCobranca();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Set<PoliticaCobranca> politicasCobrancaAtivas() {
		
		return this.distribuidorRepository.politicasCobrancaAtivas();
	}

	@Override
	@Transactional(readOnly = true)
	public String assuntoEmailCobranca() {
		
		return this.distribuidorRepository.assuntoEmailCobranca();
	}

	@Override
	@Transactional(readOnly = true)
	public String mensagemEmailCobranca() {
		
		return this.distribuidorRepository.mensagemEmailCobranca();
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean regimeEspecial() {
		
		return this.distribuidorRepository.regimeEspecial();
	}

	@Override
	@Transactional(readOnly = true)
	public TipoAtividade tipoAtividade() {
		
		return this.distribuidorRepository.tipoAtividade();
	}

	@Override
	@Transactional(readOnly = true)
	public Integer fatorRelancamentoParcial() {
		
		return this.distribuidorRepository.fatorRelancamentoParcial();
	}

	@Override
	@Transactional(readOnly = true)
	public Long obterId() {
		
		return this.distribuidorRepository.obterId();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int obterOrdinalUltimoDiaRecolhimento() {
		
		final ParametrosRecolhimentoDistribuidor parametroRecolhimento = 
				this.distribuidorRepository.parametrosRecolhimentoDistribuidor();
		
		int ordinal = 0;
		
		ordinal = parametroRecolhimento.isDiaRecolhimentoPrimeiro() ? 1 : ordinal;
		ordinal = parametroRecolhimento.isDiaRecolhimentoSegundo()  ? 2 : ordinal;
		ordinal = parametroRecolhimento.isDiaRecolhimentoTerceiro() ? 3 : ordinal;
		ordinal = parametroRecolhimento.isDiaRecolhimentoQuarto()   ? 4 : ordinal;
		ordinal = parametroRecolhimento.isDiaRecolhimentoQuinto()   ? 5 : ordinal;
		
		return ordinal;
	}
	
	@Transactional
	public List<Integer> getListaDiaOrdinalAceitaRecolhimento() {
		
		final ParametrosRecolhimentoDistribuidor parametroRecolhimento = 
				this.distribuidorRepository.parametrosRecolhimentoDistribuidor();
		
		final List<Integer> listaDiaOrdinalAceitaRecolhimento = new ArrayList<>();
		
		if(parametroRecolhimento.isDiaRecolhimentoPrimeiro()){
			listaDiaOrdinalAceitaRecolhimento.add(1);
		}
		
		if(parametroRecolhimento.isDiaRecolhimentoSegundo()){
			listaDiaOrdinalAceitaRecolhimento.add(2);
		}
		
		if(parametroRecolhimento.isDiaRecolhimentoTerceiro()){
			listaDiaOrdinalAceitaRecolhimento.add(3);
		}
		
		if(parametroRecolhimento.isDiaRecolhimentoQuarto()){
			listaDiaOrdinalAceitaRecolhimento.add(4);
		}
		
		if(parametroRecolhimento.isDiaRecolhimentoQuinto()){
			listaDiaOrdinalAceitaRecolhimento.add(5);
		}
		
		return listaDiaOrdinalAceitaRecolhimento;
		
	}
	

	/**
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.integracao.DistribuidorService#obterDatasAposFinalizacaoPrazoRecolhimento(java.util.Date, java.lang.Long[])
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Date> obterDatasAposFinalizacaoPrazoRecolhimento(final Date dataRecolhimento, final Long ...idsFornecedor) {
		
		final ParametrosRecolhimentoDistribuidor parametroRecolhimento = this.distribuidorRepository.parametrosRecolhimentoDistribuidor();
		
		final List<Integer> diasSemanaDistribuidorOpera = 
				this.distribuicaoFornecedorRepository.obterCodigosDiaDistribuicaoFornecedor(OperacaoDistribuidor.RECOLHIMENTO,idsFornecedor);
		
		final List<Date> datas = new ArrayList<>();
		
		final Map<Integer, Date> mapDataRecolhimentoValida = obterDatasValidaParaRecolhimento(dataRecolhimento,diasSemanaDistribuidorOpera);
		
		if(parametroRecolhimento.isDiaRecolhimentoPrimeiro()){
			
			datas.add(mapDataRecolhimentoValida.get(1));
		}
		
		if(parametroRecolhimento.isDiaRecolhimentoSegundo()){
			
			datas.add(mapDataRecolhimentoValida.get(2));
		}
		
		if(parametroRecolhimento.isDiaRecolhimentoTerceiro()){
			
			datas.add(mapDataRecolhimentoValida.get(3));
		}
		
		if(parametroRecolhimento.isDiaRecolhimentoQuarto()){
			
			datas.add(mapDataRecolhimentoValida.get(4));
		}
		
		if(parametroRecolhimento.isDiaRecolhimentoQuinto()){
			
			datas.add(mapDataRecolhimentoValida.get(5));
		}
		
		return datas;
	}
	
	/**
	 * Obtem o dia de recolhimento do distribuidor para a data de 
	 * Conferencia divergente da data de Recolhimento prevista
	 * @param dataRecolhimento
	 * @param numeroCota
	 * @param dataOperacaoConferencia
	 * @param produtoEdicaoId
	 */
	@Override
	@Transactional(readOnly = true)
	public Integer obterDiaDeRecolhimentoDaData(
			final Date dataOperacaoConferencia, 
			final Date dataRecolhimento, 
			final Integer numeroCota, 
			final Long produtoEdicaoId,
			final List<Long> idFornecedores,
			Boolean isCotaOperacaoDiferenciada) {
		
		boolean indCotaOperacaoDiferenciada = isCotaOperacaoDiferenciada != null ? isCotaOperacaoDiferenciada : cotaService.isCotaOperacaoDiferenciada(numeroCota, dataOperacaoConferencia);
		
		Date dataPrimeiroDiaRecolhimento = null;
		
		if(indCotaOperacaoDiferenciada) {
			dataPrimeiroDiaRecolhimento = conferenciaEncalheService.obterDataPrimeiroDiaEncalheOperacaoDiferenciada(numeroCota, dataRecolhimento);
		} else {
			dataPrimeiroDiaRecolhimento = dataRecolhimento;
		}
		
		if(!indCotaOperacaoDiferenciada) {

			if (dataRecolhimento.compareTo(dataOperacaoConferencia) > 0) {
				return null;
			} else if (dataRecolhimento.compareTo(dataOperacaoConferencia) == 0) {
				return 1;
			} 
			
		}
		
		Long[] listaIdsFornecedores = null;
		
		if(produtoEdicaoId != null) {
			
			final ProdutoEdicao produtoEdicao = produtoEdicaoService.buscarPorID(produtoEdicaoId);
			
			listaIdsFornecedores = this.conferenciaEncalheService.obterIdsFornecedorDoProduto(produtoEdicao);
		
		} else if(idFornecedores != null && !idFornecedores.isEmpty()){
			
			listaIdsFornecedores = idFornecedores.toArray(new Long[idFornecedores.size()]);
			
		} else {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sem informações de fornecedores, não foi possível obter informações de recolhimento.");
			
		}

		final List<Integer> diasSemanaDistribuidorOpera = this.distribuicaoFornecedorRepository.obterCodigosDiaDistribuicaoFornecedor(OperacaoDistribuidor.RECOLHIMENTO,
				                                                                                                                listaIdsFornecedores);
		
		final Map<Integer, Date> mapDataRecolhimentoValida = obterDatasValidaParaRecolhimento(dataPrimeiroDiaRecolhimento, diasSemanaDistribuidorOpera);
		
		for(final Entry<Integer, Date> entry : mapDataRecolhimentoValida.entrySet()) {
			
			if(entry.getValue().compareTo(dataOperacaoConferencia) == 0) {
				
				return entry.getKey();
			}
		}

		return 0;
	}
	
	
	
	
	
	
	/**
	 * Retorna um mapa com as data de recolhimento possíveis tendo como chave
	 * os valores de 1 a 5 representando a sequencia ordinal dos dias de recolhimento 
	 * como primerio até quinto dia.
	 *  
	 * 
	 * @param dataRecolhimento
	 * @param diasSemanaDistribuidorOpera
	 * 
	 * @return Map<Integer,Date>
	 */
	private Map<Integer,Date> obterDatasValidaParaRecolhimento(final Date dataRecolhimento, final List<Integer> diasSemanaDistribuidorOpera) {
		
		final Map<Integer,Date> mapDataRecolhimentoValida = new HashMap<>();
		
		Date dataRecolhimentoValida = null;
		
		int diasRecolhimento = 1;
		
		List<Feriado> feriados = feriadoRepository.buscarTodos();
		
		while(diasRecolhimento <= 5) {
			
			if(diasRecolhimento > 1) {
				
				dataRecolhimentoValida = processarDataRecolhimento(DateUtil.adicionarDias(mapDataRecolhimentoValida.get(diasRecolhimento-1),1), diasSemanaDistribuidorOpera, true, feriados);
				
				mapDataRecolhimentoValida.put(diasRecolhimento, dataRecolhimentoValida);
			} else {
				
				// A data de recolhimento referente ao primeiro dia
				// equivale a própria data de recolhimento planejada
				// para o produto edição.
				dataRecolhimentoValida = dataRecolhimento;
				
				mapDataRecolhimentoValida.put(diasRecolhimento, dataRecolhimentoValida);
			}
			
			diasRecolhimento++ ;
		}
		
		return mapDataRecolhimentoValida;
	}
	
	private Date processarDataRecolhimento(final Date novaData, final List<Integer> diasSemanaDistribuidorOpera, final boolean adicionaDias, List<Feriado> feriados) {
		
		Date dataAProcessar = obterDataValidaParaRecolhimento(novaData, diasSemanaDistribuidorOpera, feriados);
		
		final int qtdDias = adicionaDias ? 1 : -1;
		
		if(dataAProcessar == null){
			
			dataAProcessar = processarDataRecolhimento(DateUtil.adicionarDias(novaData, qtdDias), diasSemanaDistribuidorOpera, adicionaDias, feriados);
		}
		
		return dataAProcessar;
	}
	
	/**
	 * Método que obtém uma lista de datas operacionais a partir da data atual
	 * (datas posteriores ou anteriores a data atual de acordo com o parâmetro posterior).
	 * @param dataAtual
	 * @param qtndDiasUteis
	 * @param diasSemanaDistribuidorOpera
	 * @param posterior
	 * 
	 * @return List - Date
	 */
	@Override
	public List<Date> obterListaDataOperacional(Date dataAtual, int qtndDiasUteis, final List<Integer> diasSemanaDistribuidorOpera, final boolean posterior) {
		
		final List<Date> listaDatasRecolhimento = new ArrayList<>();

		final int qtdDias =  posterior ? 1 : -1;
		
		List<Feriado> feriados = feriadoRepository.buscarTodos();
		
		do {
			dataAtual = processarDataRecolhimento(DateUtil.adicionarDias(dataAtual, qtdDias), diasSemanaDistribuidorOpera, posterior, feriados);
			listaDatasRecolhimento.add(dataAtual);
		} while(--qtndDiasUteis> 0);
		
		return listaDatasRecolhimento;
		
	}
	
	private Date obterDataValidaParaRecolhimento(final Date novaData, final List<Integer> diasSemanaDistribuidorOpera, List<Feriado> feriados) {
		
		final int codigoDiaCorrente = SemanaUtil.obterDiaDaSemana(novaData);
		
		if( diasSemanaDistribuidorOpera.contains(codigoDiaCorrente) &&
				!calendarioService.isFeriadoSemOperacao(feriados, novaData) &&
				!calendarioService.isFeriadoMunicipalSemOperacao(feriados, novaData) ){
			
			return novaData;
		}
		
		return null;
	}

	@Override
	@Transactional
	public void bloqueiaProcessosLancamentosEstudos() {
		// Bloqueia no sistema os processoss relacionados a estudo e lancamentos
		final Distribuidor distribuidor = this.obter();
		distribuidor.setInterfacesMatrizExecucao(true);
		distribuidor.setDataInicioInterfacesMatrizExecucao(new Date());
		this.alterar(distribuidor);
	}

	@Override
	@Transactional
	public void desbloqueiaProcessosLancamentosEstudos() {
		// Bloqueia no sistema os processoss relacionados a estudo e lancamentos
		final Distribuidor distribuidor = this.obter();
		distribuidor.setInterfacesMatrizExecucao(false);
		distribuidor.setDataInicioInterfacesMatrizExecucao(null);
		this.alterar(distribuidor);
	}

	@Override
	@Transactional
	public boolean verificaDesbloqueioProcessosLancamentosEstudos() {
		final Distribuidor distribuidor = this.obter();
		final Date dataInicioExecucaoInterfaces = distribuidor.getDataInicioInterfacesMatrizExecucao();
		
		if (distribuidor.isInterfacesMatrizExecucao() && dataInicioExecucaoInterfaces != null) {

			final Date horaAtual = new Date();
			final Calendar calendarDuasHorasApos = Calendar.getInstance();
			calendarDuasHorasApos.clear();
			calendarDuasHorasApos.setTimeInMillis(dataInicioExecucaoInterfaces.getTime());
			calendarDuasHorasApos.add(Calendar.HOUR, 2);
			
			//Caso tenha passado uma hora e o sistema nao tenha liberado, desbloqueia automaticamente as funcionalidades (pois provavelmente a interface travou)
			if (horaAtual.after(calendarDuasHorasApos.getTime())) {
				distribuidor.setInterfacesMatrizExecucao(false);
				this.alterar(distribuidor);
			}

		}
		
		return distribuidor.isInterfacesMatrizExecucao();
	}

	@Override
	@Transactional(readOnly=true)
	public boolean isConferenciaCegaRecebimentoFisico() {
		
		return this.distribuidorRepository.isConferenciaCegaRecebimentoFisico();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public boolean isConferenciaCegaFechamentoEncalhe() {
		
		return this.distribuidorRepository.isConferenciaCegaFechamentoEncalhe();
	}

	@Override
	@Transactional
	public Set<NaturezaOperacao> obterNaturezasOperacoesNotasEnvio() {
		
		if(this.distribuidorRepository.obter().getNaturezasOperacoesNotasEnvio() != null) {
			this.distribuidorRepository.obter().getNaturezasOperacoesNotasEnvio().isEmpty();
		}
		return this.distribuidorRepository.obter().getNaturezasOperacoesNotasEnvio();
    }
	
	@Override
	@Transactional(readOnly=true)
	public Integer obterNumeroSemana(Date data){
	    
	    if (data == null){
	        
	        data = this.obterDataOperacaoDistribuidor();
	    }
	    
	    final DiaSemana diaSemana = this.inicioSemanaRecolhimento();
        
        if (diaSemana == null) {
            
            throw new ValidacaoException(TipoMensagem.ERROR, "Dados do distribuidor inexistentes: início semana");
        }
        
        return SemanaUtil.obterAnoNumeroSemana(data, diaSemana.getCodigoDiaSemana());
	}
}
