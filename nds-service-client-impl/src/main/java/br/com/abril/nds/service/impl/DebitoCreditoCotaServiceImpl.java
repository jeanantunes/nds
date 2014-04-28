package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.DebitoCreditoDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.model.cadastro.BaseCalculo;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BoxRepository;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.DebitoCreditoCotaService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
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

		TipoMovimentoFinanceiro tipoMovimentoFinanceiro =
				this.tipoMovimentoFinanceiroRepository.buscarPorId(debitoCredito.getTipoMovimentoFinanceiro().getId());

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
    public List<DebitoCreditoCotaDTO> obterListaResumoCobranca(Cota cota, Date dataOperacao) {
    
    	return this.consolidadoFinanceiroRepository.obterConsolidadosDataOperacaoSlip(
    	        cota.getId(), dataOperacao);
    }
    
	/**
	 * Obtem lista de Débitos e Créditos quem não pertencem à reparte ou encalhe
	 * @param cota
	 * @param dataOperacao
	 * @return List<DebitoCreditoCotaDTO>
	 */
	@Override
	@Transactional(readOnly = true)
	public List<DebitoCreditoCotaDTO> obterListaDebitoCreditoCotaDTO(final Cota cota, final Date dataOperacao,
	        final Long idFornecedor){
		
		final List<DebitoCreditoCotaDTO> listaDebitoCreditoCompleta = new ArrayList<DebitoCreditoCotaDTO>();
		
		final List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados = 
		        this.tipoMovimentoFinanceiroRepository.buscarTiposMovimentoFinanceiro(
		                Arrays.asList(
		                        GrupoMovimentoFinaceiro.ENVIO_ENCALHE,
		                        GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE,
		                        GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO,
		                        GrupoMovimentoFinaceiro.NEGOCIACAO_COMISSAO));

		//DEBITOS E CREDITOS DA COTA NA DATA DE OPERACAO
		final List<DebitoCreditoCotaDTO> listaDebitoCreditoCotaNaoConsolidado = 
				movimentoFinanceiroCotaRepository.obterDebitoCreditoCotaDataOperacao(cota.getNumeroCota(), 
													dataOperacao, 
													tiposMovimentoFinanceiroIgnorados,
													idFornecedor);

		listaDebitoCreditoCompleta.addAll(listaDebitoCreditoCotaNaoConsolidado);
		
		//NEGOCIACOES AVULSAS DA COTA
		final List<DebitoCreditoCotaDTO> listaDebitoNegociacaoNaoAvulsaMaisEncargos = 
				movimentoFinanceiroCotaRepository.obterValorFinanceiroNaoConsolidadoDeNegociacaoNaoAvulsaMaisEncargos(
				        cota.getNumeroCota(), dataOperacao, idFornecedor);
		
		if(listaDebitoNegociacaoNaoAvulsaMaisEncargos != null && !listaDebitoNegociacaoNaoAvulsaMaisEncargos.isEmpty()) {
			
		    listaDebitoCreditoCompleta.addAll(listaDebitoNegociacaoNaoAvulsaMaisEncargos);
		}

		
		//DÉBIDO OU CRÉDITO DO CONSOLIDADO
		final List<DebitoCreditoCotaDTO> outrosDebitoCreditoDoConsolidado =
		        this.consolidadoFinanceiroRepository.buscarMovFinanPorCotaEData(
		                cota.getId(), dataOperacao, idFornecedor);
		
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
	 * @param dataOperacao
	 */
	@Transactional
	@Override
	public void carregarDadosDebitoCreditoDaCota(InfoConferenciaEncalheCota infoConfereciaEncalheCota, 
												  Cota cota,
												  Date dataOperacao) {
		
		
		List<DebitoCreditoCotaDTO> listaDebitoCreditoCompleta = this.obterListaDebitoCreditoCotaDTO(cota, dataOperacao, null);
		
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
}
