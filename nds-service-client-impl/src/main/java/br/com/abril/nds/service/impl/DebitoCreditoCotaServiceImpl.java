package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.BoletoAvulsoDTO;
import br.com.abril.nds.dto.DebitoCreditoDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.ParametroDistribuicaoEntregaCotaDTO;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.BaseCalculo;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ModalidadeCobranca;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.PeriodicidadeCobranca;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.movimentacao.DebitoCreditoCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BoxRepository;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.NotaEnvioRepository;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.DebitoCreditoCotaService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;

@Service
public class DebitoCreditoCotaServiceImpl implements DebitoCreditoCotaService {

	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;

	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private BoxRepository boxRepository;
	
	@Autowired
	private ConsolidadoFinanceiroRepository consolidadoFinanceiroRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
	
	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Autowired
	private NotaEnvioRepository notaEnvioRepository;
	
	@Autowired
	private ChamadaEncalheCotaRepository chamadaEncalheCotaRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PoliticaCobrancaRepository politicaCobrancaRepository;
	
	@Override
	@Transactional
	public MovimentoFinanceiroCotaDTO gerarMovimentoFinanceiroCotaDTO(
			DebitoCreditoDTO debitoCredito) {

		Long idMovimento = debitoCredito.getId();

		MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = new MovimentoFinanceiroCotaDTO();
		
		movimentoFinanceiroCotaDTO.setIdMovimentoFinanceiroCota(idMovimento);
		
		movimentoFinanceiroCotaDTO.setDataCriacao(
			DateUtil.removerTimestamp(debitoCredito.getDataCriacao() == null ? new Date() : debitoCredito.getDataCriacao()));

		Date dataVencimento = DateUtil.parseDataPTBR(debitoCredito.getDataVencimento());
		
		movimentoFinanceiroCotaDTO.setDataVencimento(dataVencimento);

		movimentoFinanceiroCotaDTO.setValor(new BigDecimal(debitoCredito.getValor()));

		movimentoFinanceiroCotaDTO.setObservacao(debitoCredito.getObservacao());

		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = this.tipoMovimentoFinanceiroRepository.buscarPorId(debitoCredito.getTipoMovimentoFinanceiro().getId());

		movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro(tipoMovimentoFinanceiro);
		
		Cota cota = this.cotaRepository.obterPorNumeroDaCota(debitoCredito.getNumeroCota());
		
		movimentoFinanceiroCotaDTO.setCota(cota);

		Usuario usuario = this.usuarioRepository.buscarPorId(debitoCredito.getIdUsuario());
		
		movimentoFinanceiroCotaDTO.setUsuario(usuario);
		
		movimentoFinanceiroCotaDTO.setLancamentoManual(true);
		
		Fornecedor fornecedor = cota.getParametroCobranca()!=null?cota.getParametroCobranca().getFornecedorPadrao():null;

		movimentoFinanceiroCotaDTO.setFornecedor(fornecedor);

		return movimentoFinanceiroCotaDTO;
	}

	@Override
	@Transactional
	public MovimentoFinanceiroCotaDTO gerarMovimentoFinanceiroBoletoAvulsoDTO(BoletoAvulsoDTO debitoCredito) {

		Long idMovimento = debitoCredito.getId();

		MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = new MovimentoFinanceiroCotaDTO();
		
		movimentoFinanceiroCotaDTO.setIdMovimentoFinanceiroCota(idMovimento);
		
		movimentoFinanceiroCotaDTO.setDataCriacao(
			DateUtil.removerTimestamp(debitoCredito.getDataCriacao() == null ? new Date() : debitoCredito.getDataCriacao()));

		Date dataVencimento = DateUtil.parseDataPTBR(debitoCredito.getDataVencimento());
		
		movimentoFinanceiroCotaDTO.setDataVencimento(dataVencimento);

		movimentoFinanceiroCotaDTO.setValor(new BigDecimal(debitoCredito.getValor()));

		movimentoFinanceiroCotaDTO.setObservacao(debitoCredito.getObservacao());

		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = this.tipoMovimentoFinanceiroRepository.buscarPorId(debitoCredito.getTipoMovimentoFinanceiro().getId());

		movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro(tipoMovimentoFinanceiro);
		
		Cota cota = this.cotaRepository.obterPorNumeroDaCota(debitoCredito.getNumeroCota());
		
		movimentoFinanceiroCotaDTO.setCota(cota);

		Usuario usuario = this.usuarioRepository.buscarPorId(debitoCredito.getIdUsuario());
		
		movimentoFinanceiroCotaDTO.setUsuario(usuario);
		
		movimentoFinanceiroCotaDTO.setLancamentoManual(true);
		
		Fornecedor fornecedor = cota.getParametroCobranca()!=null?cota.getParametroCobranca().getFornecedorPadrao():null;

		movimentoFinanceiroCotaDTO.setFornecedor(fornecedor);

		return movimentoFinanceiroCotaDTO;
	}
	
	
	/**
	 * Obtém dados pré-configurados com informações das Cotas do Box, Rota e Roteiro. Para lançamentos de débitos e/ou créditos
	 * @param idBox
	 * @param idRoteiro
	 * @param idRota
	 * @param percentual
	 * @param baseCalculo
	 * @param dataPeriodoInicial
	 * @param dataPeriodoFinal
	 * @return List<DebitoCreditoDTO>
	 */
	@Override
	@Transactional(readOnly=true)
	public List<DebitoCreditoDTO> obterDadosLancamentoPorBoxRoteiroRota(Long idBox,Long idRoteiro,Long idRota,BigDecimal percentual,BaseCalculo baseCalculo,Date dataPeriodoInicial,Date dataPeriodoFinal) {
		
		List<DebitoCreditoDTO> listaDC = new ArrayList<DebitoCreditoDTO>();
		
		List<Cota> cotas = this.boxRepository.obterCotasPorBoxRoteiroRota(idBox, idRoteiro, idRota);
		
		BigDecimal percFat;
		
		BigDecimal percTotal = new BigDecimal(100);
		
		Map<Long,BigDecimal> cotasFaturamentos = null;
		
		if( (percentual != null) && (baseCalculo != null) && (dataPeriodoInicial != null) && (dataPeriodoFinal!=null) ){
		    
		    cotasFaturamentos = this.movimentoFinanceiroCotaService.obterFaturamentoCotasPeriodo(
                    cotas, baseCalculo, dataPeriodoInicial, dataPeriodoFinal);
		}
		
		for (int index = 0 ; index < cotas.size() ; index++){

		    Cota itemCota = cotas.get(index);
		    
			percFat = null;
			
			if (cotasFaturamentos != null){
			
			    BigDecimal fat = cotasFaturamentos.get(itemCota.getId());
			    
				if (fat != null){
					
					if(fat.compareTo(BigDecimal.ZERO) > 0 ){
				        percFat = fat.divide(percTotal).multiply(percentual);
					}
					
				}
			}
			
			listaDC.add(
			        new DebitoCreditoDTO(
			                Long.valueOf(index),
			                null,
			                null,
			                itemCota.getNumeroCota(),
			                itemCota.getPessoa().getNome(),
			                null,
			                null,
			                (percFat!=null?CurrencyUtil.formatarValor(percFat.setScale(2, RoundingMode.HALF_UP)):null),
			                null));	
		}
		
		return listaDC;
	}


	/**
	 * Obtém Quantidade de Cotas por Box, Rota e Roteiro
	 * @param idBox
	 * @param idRoteiro
	 * @param idRota
	 * @return Número de Cotas encontradas
	 */
	@Override
	@Transactional(readOnly=true)
	public int obterQuantidadeCotasPorBoxRoteiroRota(Long idBox,
			Long idRoteiro, Long idRota) {
		return this.boxRepository.obterQuantidadeCotasPorBoxRoteiroRota(idBox, idRoteiro, idRota).intValue();
	}


	/**
	 * Obtém dados pré-configurados com informações da Cota para lançamento de débito e/ou crédito
	 * @param numeroCota
	 * @param percentual
	 * @param baseCalculo
	 * @param dataPeriodoInicial
	 * @param dataPeriodoFinal
	 * @return DebitoCreditoDTO
	 */
	@Override
	@Transactional(readOnly=true)
	public DebitoCreditoDTO obterDadosLancamentoPorCota(Integer numeroCota,
			BigDecimal percentual, BaseCalculo baseCalculo,
			Date dataPeriodoInicial, Date dataPeriodoFinal, Long index) {
		
		DebitoCreditoDTO dc = new DebitoCreditoDTO();
		Long indice=index;
		Cota cota = null;
		
		if (numeroCota!=null){
		
			cota = this.cotaRepository.obterPorNumeroDaCota(numeroCota);
			
			if (cota!=null){
			
				BigDecimal percFat = null;
				
				BigDecimal percTotal = new BigDecimal(100);
				
				Map<Long,BigDecimal> cotasFaturamentos = null;
		
				if((percentual!=null)&&( baseCalculo!=null)&&( dataPeriodoInicial!=null)&&(dataPeriodoFinal!=null)){
					
					cotasFaturamentos = this.movimentoFinanceiroCotaService.obterFaturamentoCotasPeriodo(Arrays.asList(cota), baseCalculo, dataPeriodoInicial, dataPeriodoFinal);
					
					if (cotasFaturamentos!=null && cotasFaturamentos.get(cota.getId())!=null){
						
						if(cotasFaturamentos.get(cota.getId()).doubleValue() > 0 ){
					        percFat = ((cotasFaturamentos.get(cota.getId()).divide(percTotal)).multiply(percentual));
						}
						
					}
				}
				
				dc = new DebitoCreditoDTO(indice,null,null,cota.getNumeroCota(),cota.getPessoa().getNome(),null,null,(percFat!=null?CurrencyUtil.formatarValor(percFat.doubleValue()):null),null);
			}
		}

		return dc;
	}
	
	/**
	 * Obtem valor total de Débitos pendentes da Cota
	 * @param numeroCota
	 * @return BigDecimal
	 */
	@Override
	@Transactional
	public BigDecimal obterTotalDebitoCota(Integer numeroCota, Date dataOperacao){
		
		return this.movimentoFinanceiroCotaRepository.obterTotalDebitoCota(numeroCota, dataOperacao);
	}
	
	/**
	 * Obtem valor total de Créditos pendentes da Cota
	 * @param numeroCota
	 * @return BigDecimal
	 */
	@Override
	@Transactional
	public BigDecimal obterTotalCreditoCota(Integer numeroCota, Date dataOperacao){
		
		return this.movimentoFinanceiroCotaRepository.obterTotalCreditoCota(numeroCota, dataOperacao);
	}
   
    
    @Transactional(readOnly = true)
    public List<DebitoCreditoCota> obterListaResumoCobranca(Cota cota, Date dataOperacao) {
    
    	return this.consolidadoFinanceiroRepository.obterConsolidadosDataOperacaoSlip(
    	        cota.getId(), dataOperacao);
    }
    
	/**
	 * Obtem lista de Débitos e Créditos quem não pertencem à reparte ou encalhe
	 * @param cota
	 * @param datas
	 * @return List<DebitoCreditoCotaDTO>
	 */
	@Override
	@Transactional(readOnly = true)
	public List<DebitoCreditoCota> obterListaDebitoCreditoCotaDTO(final Cota cota, final List<Date> datas,
	        final Long idFornecedor){
		
		final List<DebitoCreditoCota> listaDebitoCreditoCompleta = new ArrayList<DebitoCreditoCota>();
		
		if(datas == null || datas.isEmpty()) {
			return listaDebitoCreditoCompleta;
		}
		
		final List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados = 
		        this.tipoMovimentoFinanceiroRepository.buscarTiposMovimentoFinanceiro(
		                Arrays.asList(
		                        GrupoMovimentoFinaceiro.ENVIO_ENCALHE,
		                        GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE,
		                        GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO,
		                        GrupoMovimentoFinaceiro.NEGOCIACAO_COMISSAO));

		//DEBITOS E CREDITOS DA COTA NA DATA DE OPERACAO
		final List<DebitoCreditoCota> listaDebitoCreditoCotaNaoConsolidado = 
				movimentoFinanceiroCotaRepository.obterDebitoCreditoCotaDataOperacao(cota.getNumeroCota(), 
													datas, 
													tiposMovimentoFinanceiroIgnorados,
													idFornecedor);

		listaDebitoCreditoCompleta.addAll(listaDebitoCreditoCotaNaoConsolidado);
		
		//NEGOCIACOES AVULSAS DA COTA
		final List<DebitoCreditoCota> listaDebitoNegociacaoNaoAvulsaMaisEncargos = 
				movimentoFinanceiroCotaRepository.obterValorFinanceiroNaoConsolidadoDeNegociacaoNaoAvulsaMaisEncargos(
				        cota.getNumeroCota(), datas, idFornecedor);
		
		if(listaDebitoNegociacaoNaoAvulsaMaisEncargos != null && !listaDebitoNegociacaoNaoAvulsaMaisEncargos.isEmpty()) {
			
		    listaDebitoCreditoCompleta.addAll(listaDebitoNegociacaoNaoAvulsaMaisEncargos);
		}

		//DÉBIDO OU CRÉDITO DO CONSOLIDADO
		final List<DebitoCreditoCota> outrosDebitoCreditoDoConsolidado =
		        this.consolidadoFinanceiroRepository.buscarMovFinanPorCotaEData(cota.getId(), datas, idFornecedor);
		
		if(outrosDebitoCreditoDoConsolidado != null) {
			for(DebitoCreditoCota dc : outrosDebitoCreditoDoConsolidado) {
				if(GrupoMovimentoFinaceiro.TAXA_EXTRA.equals(dc.getTipoMovimento())) {
					dc.setObservacoes(distribuidorService.obter().getDescricaoTaxaExtra() +" ("+ DateUtil.formatarDataPTBR(dc.getDataLancamento()) +")");
				}
			}
		}
		
		if(outrosDebitoCreditoDoConsolidado!=null && !outrosDebitoCreditoDoConsolidado.isEmpty()) {
			listaDebitoCreditoCompleta.addAll(outrosDebitoCreditoDoConsolidado);
		}
		
		return listaDebitoCreditoCompleta;
	}
	
	/**
	 * Obtem Outros Valores
	 * 
	 * @param infoConfereciaEncalheCota
	 * @param cota
	 * @param datas
	 */
	@Transactional
	@Override
	public void carregarDadosDebitoCreditoDaCota(InfoConferenciaEncalheCota infoConfereciaEncalheCota, 
												  Cota cota,
												  List<Date> datas) {

		List<DebitoCreditoCota> listaDebitoCreditoCompleta = this.obterListaDebitoCreditoCotaDTO(cota, datas, null);
		
		infoConfereciaEncalheCota.setListaDebitoCreditoCota(listaDebitoCreditoCompleta);		
	}
	
	/**
	 * Verifica se o Movimento Financeiro pode ser Editado
	 * Nao consolidado
	 * Lancamento automatico
	 * Data do movimento maior que a data de operação
	 * 
	 * @param movimentoFinanceiroCota
	 * @return boolean
	 */
	@Override
	@Transactional(readOnly = true)
    public boolean isMovimentoEditavel(MovimentoFinanceiroCota movimentoFinanceiroCota) {
		
		boolean movimentoEditavel = true;
		
		if (this.movimentoFinanceiroCotaRepository.isMovimentoFinanceiroCotaConsolidado(movimentoFinanceiroCota.getId())) {
			
			movimentoEditavel = false;
		}
		
		if (!movimentoFinanceiroCota.isLancamentoManual()) {
			
			movimentoEditavel = false;
		}
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		dataOperacao = DateUtil.removerTimestamp(dataOperacao);
		
		if (dataOperacao.compareTo(movimentoFinanceiroCota.getData()) >= 0) {
			
			movimentoEditavel = false;
		}
		
		return movimentoEditavel;
	}
	
	
	@Override
	@Transactional
	public void processarDebitoDeDistribuicaoDeEntregaDaCota(final Date dataOperacao){
		
		final List<ParametroDistribuicaoEntregaCotaDTO> 
					cotasCandidatasADebito = cotaRepository.obterParametrosDistribuicaoEntregaCota();
		
		if(cotasCandidatasADebito == null || cotasCandidatasADebito.isEmpty()){
			return;
		}
		
		final TipoMovimentoFinanceiro tipoMovimentoEntregador = 
				this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
						GrupoMovimentoFinaceiro.DEBITO_COTA_TAXA_DE_ENTREGA_ENTREGADOR);
		
		final TipoMovimentoFinanceiro tipoMovimentoTransportador = 
				this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
						GrupoMovimentoFinaceiro.DEBITO_COTA_TAXA_DE_ENTREGA_TRANSPORTADOR);
		
		final Usuario usuario = usuarioService.getUsuarioLogado();
		
		final Fornecedor fornecedorPadraoDistribuidor = 
				politicaCobrancaRepository.obterFornecedorPadrao();
		
		for(ParametroDistribuicaoEntregaCotaDTO parametro : cotasCandidatasADebito){
				
			if(this.isCotaNoPeriodoDeCarenciaCobranca(parametro, dataOperacao)){
				continue;
			}
			
			if(this.isPeriodicidadeDeCobrancaNaoPermitido(parametro,dataOperacao)){
				continue;
			}
			
			final boolean isGerarDebitoPorEntrega = parametro.isPorEntrega();
			
			if(isGerarDebitoPorEntrega){
				
				this.gerarDebitoPorEntrega(
						parametro,dataOperacao, tipoMovimentoEntregador, tipoMovimentoTransportador, usuario,fornecedorPadraoDistribuidor);
			}
			else{
				
				this.gerarDebitoPorPeriodicidade(
						parametro,dataOperacao, tipoMovimentoEntregador, tipoMovimentoTransportador, usuario,fornecedorPadraoDistribuidor);
			}
		}
	}
	
	/*
	 * Verifica se é permitido a geração de debito da cota na periodicidade definida nos parametros da cota
	 */
	private boolean isPeriodicidadeDeCobrancaNaoPermitido(final ParametroDistribuicaoEntregaCotaDTO parametro,
														  final Date dataOperacao) {
		
		final Date dataComparacao = DateUtil.adicionarDias(dataOperacao, 1); 
		
		final int diaDoMes = DateUtil.obterDiaDoMes(dataComparacao);
		
		if(PeriodicidadeCobranca.MENSAL.equals(parametro.getPeriodicidade())){
			
			return (diaDoMes != parametro.getDiaCobranca());
		}
		
		if(PeriodicidadeCobranca.QUINZENAL.equals(parametro.getPeriodicidade())){
			
			final Integer segundoDiaQuinzenalValido = this.obterSegundoDiaDaQuinzena(parametro);	
			
			return (diaDoMes != parametro.getDiaCobranca() 
						&& diaDoMes!= segundoDiaQuinzenalValido );
		}
		
		if(PeriodicidadeCobranca.SEMANAL.equals(parametro.getPeriodicidade())){
			
			final DiaSemana diaSemanaCobranca = DiaSemana.getByDate(dataComparacao);
			
			return !diaSemanaCobranca.equals(parametro.getDiaSemana());
		}
		
		return false;
	}
	
	/*
	 * Gera movimentos de debito para cota em função da periodicidade definida nos parametros da cota
	 */
	private void gerarDebitoPorPeriodicidade(final ParametroDistribuicaoEntregaCotaDTO parametro,
											 final Date dataOperacao,
											 final TipoMovimentoFinanceiro tipoMovimentoEntregador,
											 final TipoMovimentoFinanceiro tipoMovimentoTransportador,
											 final Usuario usuario,
											 final Fornecedor fornecedorPadraoDistribuidor) {
		
		BigDecimal valorDebito = BigDecimal.ZERO;
		
		if(ModalidadeCobranca.PERCENTUAL.equals(parametro.getModalidadeCobranca())){
			
			final Date periodoInicial = this.obterPeriodoInicial(parametro, dataOperacao);
			
			final Date periodoFinal = dataOperacao;
			
			valorDebito = this.obterValorSobrePercentualFaturamento(parametro, periodoInicial, periodoFinal);
		}
		else{
			
			valorDebito = parametro.getTaxaFixa();
		}
		
		this.gerarMovimentoDebitoEntregaCota(
				valorDebito, parametro, dataOperacao, tipoMovimentoEntregador,tipoMovimentoTransportador,usuario,fornecedorPadraoDistribuidor);
	}

    /*
     * Retorna o periodo inicial valido para obter os dados da cota para geração de debito.
     */
	@SuppressWarnings("deprecation")
	private Date obterPeriodoInicial(final ParametroDistribuicaoEntregaCotaDTO parametro,
									 final Date dataOperacao) {
		
		Date periodoInicial  = dataOperacao;
		
		if(PeriodicidadeCobranca.MENSAL.equals(parametro.getPeriodicidade())){
			
			periodoInicial  = DateUtil.subtrairMeses(dataOperacao, 1);
		}
		else if(PeriodicidadeCobranca.QUINZENAL.equals(parametro.getPeriodicidade())){
			
			final Integer segundoDiaQuinzenalValido = this.obterSegundoDiaDaQuinzena(parametro);	
			
			Calendar data = Calendar.getInstance();
			data.setTime(dataOperacao);
			
			if(dataOperacao.getDay() == parametro.getDiaCobranca()){
				
				data.set(Calendar.DAY_OF_MONTH,segundoDiaQuinzenalValido);
				data.add(Calendar.MONTH, -1);

				periodoInicial = data.getTime();
			}
			else{
				
				data.set(Calendar.DAY_OF_MONTH,parametro.getDiaCobranca());

				periodoInicial = data.getTime();  
			}
		
		}
		else if(PeriodicidadeCobranca.SEMANAL.equals(parametro.getPeriodicidade())){
			
			periodoInicial  = DateUtil.subtrairDias(dataOperacao, 7);
		}
		
		return periodoInicial;
	}
	
	/*
	 * Gera movimentos de debito para cota por cada entrega realizada em uma determinada periodicidade.
	 */
	private void gerarDebitoPorEntrega(final ParametroDistribuicaoEntregaCotaDTO parametro,
									   final Date dataOperacao,
									   final TipoMovimentoFinanceiro tipoMovimentoEntregador,
									   final TipoMovimentoFinanceiro tipoMovimentoTransportador,
									   final Usuario usuario,
									   final Fornecedor fornecedorPadraoDistribuidor) {
		
		final Date periodoInicial = this.obterPeriodoInicial(parametro, dataOperacao);
		
		final Date periodoFinal = dataOperacao;
		
		final Long quantidadeNotaEnvio = 
				notaEnvioRepository.quantidadeNotasEmitidasParaCota(
						parametro.getIdCota(), periodoInicial, periodoFinal);
		
		final Long quantidadeChamadaEncalhe = 
				chamadaEncalheCotaRepository.quantidadeChamadasEncalheParaCota(
						parametro.getIdCota(), periodoInicial, periodoFinal);
		
		final boolean naoExisteEntregaParaCota = (quantidadeNotaEnvio == 0L && quantidadeChamadaEncalhe== 0L);
		
		if(naoExisteEntregaParaCota){
			return;
		}
		
		final BigDecimal quantidadeEntregas = 
				(quantidadeNotaEnvio > quantidadeChamadaEncalhe) ? new BigDecimal(quantidadeNotaEnvio) : new BigDecimal(quantidadeChamadaEncalhe);
		
		BigDecimal valorDebito = BigDecimal.ZERO;
		
		if(ModalidadeCobranca.PERCENTUAL.equals(parametro.getModalidadeCobranca())){
			
			valorDebito = this.obterValorSobrePercentualFaturamento(parametro, periodoInicial, periodoFinal);
			
			valorDebito = valorDebito.multiply(quantidadeEntregas);
		}
		else{
			
			valorDebito = parametro.getTaxaFixa().multiply(quantidadeEntregas);
		}
		
		this.gerarMovimentoDebitoEntregaCota(
				valorDebito, parametro, dataOperacao, tipoMovimentoEntregador,tipoMovimentoTransportador,usuario,fornecedorPadraoDistribuidor);
	}
	
	/*
	 * Gera movimento de Débito para cota
	 */
	private void gerarMovimentoDebitoEntregaCota(final BigDecimal valorDebito,
												 final ParametroDistribuicaoEntregaCotaDTO parametro,
												 final Date dataOperacao,
												 final TipoMovimentoFinanceiro tipoMovimentoEntregador,
												 final TipoMovimentoFinanceiro tipoMovimentoTransportador,
												 final Usuario usuario,
												 final Fornecedor fornecedorPadraoDistribuidor){
		
		if(valorDebito.compareTo(BigDecimal.ZERO) == 0){
			return;
		}
		
		Date dataVencimento = calendarioService.adicionarDiasUteis(dataOperacao, 1);
		
		Fornecedor fornecedorPadraoCota = cotaRepository.obterFornecedorPadrao(parametro.getIdCota());
		
		if(fornecedorPadraoCota == null){
			
			fornecedorPadraoCota = fornecedorPadraoDistribuidor;
		}
		
		Cota cota = new Cota();
		cota.setId(parametro.getIdCota());
		cota.setParametroCobranca(new ParametroCobrancaCota());
		cota.getParametroCobranca().setFornecedorPadrao(fornecedorPadraoCota);
		
		final TipoMovimentoFinanceiro tipoMovimento = 
				(DescricaoTipoEntrega.ENTREGADOR.equals(parametro.getTipoEntrega()))
					? tipoMovimentoEntregador : tipoMovimentoTransportador;
		
		movimentoFinanceiroCotaService
			.gerarMovimentoFinanceiroDebitoCota(
					tipoMovimento, usuario, cota, dataVencimento, dataOperacao, valorDebito, null);		
	}
	
	/*
	 * Retorna o valor para debito calculado sobre o percentual de faturamento da cota em um periodo.
	 */
	private BigDecimal obterValorSobrePercentualFaturamento(final ParametroDistribuicaoEntregaCotaDTO parametro,
														    final Date dataPeriodoInicial,
														    final Date dataPeriodoFinal){
		
		if(parametro.getPercentualFaturamento().compareTo(BigDecimal.ZERO)==0){
			return BigDecimal.ZERO;
		}
		
		BigDecimal faturamentoCota = 
				movimentoFinanceiroCotaService.obterFaturamentoDaCotaNoPeriodo(
						parametro.getIdCota(),parametro.getBaseCalculo() ,dataPeriodoInicial, dataPeriodoFinal);
		
		if(faturamentoCota.compareTo(BigDecimal.ZERO) == 0){
			return faturamentoCota;
		}
		
		BigDecimal valorSobrePercentualDebito = 
				parametro.getPercentualFaturamento().divide(new BigDecimal(100)).multiply(faturamentoCota);
		
		return valorSobrePercentualDebito;
	}

	/*
	 * Verifica se a cota esta em um periodo de carencia, nesse periodo não sera gerado debito.
	 */
	private boolean isCotaNoPeriodoDeCarenciaCobranca(final ParametroDistribuicaoEntregaCotaDTO parametro,
													  final Date dataOperacao) {
		
		if(parametro.getInicioCarencia() == null || parametro.getFimCarencia() == null){
			return false;
		}
		
		final Date dataComparacao = DateUtil.adicionarDias(dataOperacao, 1);
		
		return DateUtil.validarDataEntrePeriodo(
				dataComparacao, parametro.getInicioCarencia(), parametro.getFimCarencia());
	}
	
	/*
	 * Retorna o segundo dia da quinzena 
	 */
	private Integer obterSegundoDiaDaQuinzena(final ParametroDistribuicaoEntregaCotaDTO parametro) {
		
		final Integer segundoDiaQuinzenalValido = 
				(parametro.getDiaCobranca() + 14) < 31 ? parametro.getDiaCobranca() + 14 : 31;
		
		return segundoDiaQuinzenalValido;
	}
}
