package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
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
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
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
import br.com.abril.nds.util.Constantes;
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
		
		Cota cota = this.cotaRepository.obterPorNumerDaCota(debitoCredito.getNumeroCota());
		
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
		
		Long indice=0l;
		for (Cota itemCota:cotas){

			percFat = null;
			
			indice++;

			if((percentual!=null)&&( baseCalculo!=null)&&( dataPeriodoInicial!=null)&&(dataPeriodoFinal!=null)){
				
				cotasFaturamentos = this.movimentoFinanceiroCotaService.obterFaturamentoCotasPeriodo(cotas, baseCalculo, dataPeriodoInicial, dataPeriodoFinal);
				
				if (cotasFaturamentos!=null && cotasFaturamentos.get(itemCota.getId())!=null){
					
					if(cotasFaturamentos.get(itemCota.getId()).doubleValue() > 0 ){
				        percFat = ((cotasFaturamentos.get(itemCota.getId()).divide(percTotal)).multiply(percentual));
					}
					
				}
			}
			
			listaDC.add(new DebitoCreditoDTO(indice,null,null,itemCota.getNumeroCota(),itemCota.getPessoa().getNome(),null,null,(percFat!=null?CurrencyUtil.formatarValor(percFat.doubleValue()):null),null));	
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
		
			cota = this.cotaRepository.obterPorNumerDaCota(numeroCota);
			
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
	
    private void adicionarDebitoCreditoDeConsolidado(List<DebitoCreditoCotaDTO> listaDebitoCredito, BigDecimal valor, String descricaoCredito, String descricaoDebito, Date dataVencimento, Date dataLancamento) {
		
		if(valor == null || BigDecimal.ZERO.compareTo(valor) == 0) {
			return;
		}
		
		DebitoCreditoCotaDTO debitoCredito = new DebitoCreditoCotaDTO();
		
		if(BigDecimal.ZERO.compareTo(valor) < 0) {
			
			debitoCredito.setObservacoes(descricaoCredito);
			debitoCredito.setTipoLancamentoEnum(OperacaoFinaceira.CREDITO);
			debitoCredito.setTipoMovimento(Constantes.COMPOSICAO_COBRANCA_CREDITO);
		
		} else {
			
			debitoCredito.setObservacoes(descricaoDebito);
			debitoCredito.setTipoLancamentoEnum(OperacaoFinaceira.DEBITO);
			debitoCredito.setTipoMovimento(Constantes.COMPOSICAO_COBRANCA_DEBITO);
		
		}
		
		debitoCredito.setDataVencimento(dataVencimento);
		
		debitoCredito.setDataLancamento(dataLancamento);
		
		debitoCredito.setValor(valor.abs());
		
		listaDebitoCredito.add(debitoCredito);
		
	}
	
	/**
	 * Obtem lista de Débitos e Créditos quem não pertencem à reparte ou encalhe
	 * @param cota
	 * @param dataOperacao
	 * @return List<DebitoCreditoCotaDTO>
	 */
	@Override
	@Transactional(readOnly = true)
	public List<DebitoCreditoCotaDTO> obterListaDebitoCreditoCotaDTO(Cota cota, Date dataOperacao){
		
		List<DebitoCreditoCotaDTO> listaDebitoCreditoCompleta = new ArrayList<DebitoCreditoCotaDTO>();
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiroEnvioEncalhe = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.ENVIO_ENCALHE);
		TipoMovimentoFinanceiro tipoMovimentoFinanceiroRecebimentoReparte = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE);
		List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados = new ArrayList<TipoMovimentoFinanceiro>();
		tiposMovimentoFinanceiroIgnorados.add(tipoMovimentoFinanceiroEnvioEncalhe);
		tiposMovimentoFinanceiroIgnorados.add(tipoMovimentoFinanceiroRecebimentoReparte);
		

		//DEBITOS E CREDITOS DA COTA NA DATA DE OPERACAO
		List<DebitoCreditoCotaDTO> listaDebitoCreditoCotaNaoConsolidado = 
				movimentoFinanceiroCotaRepository.obterDebitoCreditoCotaDataOperacao(cota.getNumeroCota(), 
													dataOperacao, 
													tiposMovimentoFinanceiroIgnorados);

		listaDebitoCreditoCompleta.addAll(listaDebitoCreditoCotaNaoConsolidado);
		
		//NEGOCIACOES AVULSAS DA COTA
		List<DebitoCreditoCotaDTO> listaDebitoNegociacaoNaoAvulsaMaisEncargos = 
				movimentoFinanceiroCotaRepository.obterValorFinanceiroNaoConsolidadoDeNegociacaoNaoAvulsaMaisEncargos(cota.getNumeroCota());
		
		if(listaDebitoNegociacaoNaoAvulsaMaisEncargos != null && !listaDebitoNegociacaoNaoAvulsaMaisEncargos.isEmpty()) {
			
			for(DebitoCreditoCotaDTO negociacao : listaDebitoNegociacaoNaoAvulsaMaisEncargos) {
				
				negociacao.setTipoLancamentoEnum(OperacaoFinaceira.DEBITO);
				
				negociacao.setObservacoes("Negociação Avulsa e Encargos.");
				
				listaDebitoCreditoCompleta.add(negociacao);
			}
		}

		
		//DÉBIDO OU CRÉDITO DO CONSOLIDADO
		List<DebitoCreditoCotaDTO> outrosDebitoCreditoDoConsolidado = obterOutrosDebitoCreditoDeConsolidado(cota.getId(), dataOperacao);
		
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
		
		
		List<DebitoCreditoCotaDTO> listaDebitoCreditoCompleta = this.obterListaDebitoCreditoCotaDTO(cota, dataOperacao);
		
		infoConfereciaEncalheCota.setListaDebitoCreditoCota(listaDebitoCreditoCompleta);		
	}
	
	/**
	 * Obtém débito ou crédito do consolidado da cota
	 * 
	 * @param idCota
	 * @param dataOperacao
	 * 
	 * @return DebitoCreditoCotaDTO
	 */
	private List<DebitoCreditoCotaDTO> obterOutrosDebitoCreditoDeConsolidado(Long idCota, Date dataOperacao) {

		List<DebitoCreditoCotaDTO> listaDebitoCredito = new ArrayList<DebitoCreditoCotaDTO>();
		
		ConsolidadoFinanceiroCota consolidado = this.consolidadoFinanceiroRepository.buscarPorCotaEData(idCota, dataOperacao);
		
		if (consolidado == null) {
			
			return null;
		}
		
		Date dataConsolidadoPostergado = this.consolidadoFinanceiroRepository.obterDataAnteriorImediataPostergacao(consolidado);

		if (dataConsolidadoPostergado != null) {
			
			String dataConsolidadoPostergadoFormatada = DateUtil.formatarData(dataConsolidadoPostergado,"dd/MM/yy");
			
			adicionarDebitoCreditoDeConsolidado(
					listaDebitoCredito,
					consolidado.getValorPostergado(), 
					"Credito Post. " + dataConsolidadoPostergadoFormatada,
					"Pgto. Post. " + dataConsolidadoPostergadoFormatada,
					consolidado.getDataConsolidado(), 
					DateUtil.parseDataPTBR(DateUtil.formatarData(consolidado.getDataConsolidado(),"dd/MM/yy")));
		}
						
		adicionarDebitoCreditoDeConsolidado(
				listaDebitoCredito,
				consolidado.getDebitoCredito(), 
				OperacaoFinaceira.CREDITO.getDescricao(),
				OperacaoFinaceira.DEBITO.getDescricao(),
				consolidado.getDataConsolidado(), 
				DateUtil.parseDataPTBR(DateUtil.formatarData(consolidado.getDataConsolidado(),"dd/MM/yy")));

		adicionarDebitoCreditoDeConsolidado(
				listaDebitoCredito,
				consolidado.getEncargos(),
				"Encargos", "Encargos",
				consolidado.getDataConsolidado(), 
				DateUtil.parseDataPTBR(DateUtil.formatarData(consolidado.getDataConsolidado(),"dd/MM/yy")));

		adicionarDebitoCreditoDeConsolidado(
				listaDebitoCredito,
				consolidado.getPendente(),
				"Pendente", "Pendente",
				consolidado.getDataConsolidado(), 
				DateUtil.parseDataPTBR(DateUtil.formatarData(consolidado.getDataConsolidado(),"dd/MM/yy")));
		
		adicionarDebitoCreditoDeConsolidado(
				listaDebitoCredito,
				consolidado.getVendaEncalhe(),
				"Venda Encalhe", "Venda Encalhe",
				consolidado.getDataConsolidado(), 
				DateUtil.parseDataPTBR(DateUtil.formatarData(consolidado.getDataConsolidado(),"dd/MM/yy")));
		
		return listaDebitoCredito;
 	}
}
