package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.abril.nds.dto.CotaFaturamentoDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.BaseCalculo;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.HistoricoMovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.planejamento.LancamentoParcial;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ConferenciaEncalheRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.HistoricoMovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.LancamentoParcialRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;

@Service
public class MovimentoFinanceiroCotaServiceImpl implements
		MovimentoFinanceiroCotaService {

	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;

	@Autowired
	private HistoricoMovimentoFinanceiroCotaRepository historicoMovimentoFinanceiroCotaRepository;
	
	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private ConferenciaEncalheRepository conferenciaEncalheRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private LancamentoParcialRepository lancamentoParcialRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Override
	@Transactional
	public List<MovimentoFinanceiroCota> gerarMovimentosFinanceirosDebitoCredito(
			MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO) {

		Map<Long, List<MovimentoEstoqueCota>> mapaMovimentoEstoqueCotaPorFornecedor = 
			this.gerarMapaMovimentoEstoqueCotaPorFornecedor(movimentoFinanceiroCotaDTO.getMovimentos());
		
		List<MovimentoFinanceiroCota> movimentosFinanceirosCota = new ArrayList<MovimentoFinanceiroCota>();
		
		if (mapaMovimentoEstoqueCotaPorFornecedor.isEmpty()) {
			
			movimentosFinanceirosCota.add(
				gerarMovimentoFinanceiroCota(movimentoFinanceiroCotaDTO, null));
			
		} else {
		
			for (Map.Entry<Long, List<MovimentoEstoqueCota>> entry : mapaMovimentoEstoqueCotaPorFornecedor.entrySet()) {
				
				movimentosFinanceirosCota.add(
					gerarMovimentoFinanceiroCota(movimentoFinanceiroCotaDTO, entry.getValue()));
			}
		}
		
		return movimentosFinanceirosCota;
	}

	/*
	 * Gera o movimento financeiro da cota.
	 */
	private MovimentoFinanceiroCota gerarMovimentoFinanceiroCota(MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO,
										  					 	 List<MovimentoEstoqueCota> movimentosEstoqueCota) {
		
		MovimentoFinanceiroCota movimentoFinanceiroCota = null;
		MovimentoFinanceiroCota movimentoFinanceiroCotaMerged = null;
		
		if (movimentoFinanceiroCotaDTO.getIdMovimentoFinanceiroCota() != null) {

			movimentoFinanceiroCota = this.movimentoFinanceiroCotaRepository.buscarPorId(
					movimentoFinanceiroCotaDTO.getIdMovimentoFinanceiroCota());
		
		} else {

			movimentoFinanceiroCota = new MovimentoFinanceiroCota();
		}

		TipoMovimentoFinanceiro tipoMovimentoFinanceiro =
					movimentoFinanceiroCotaDTO.getTipoMovimentoFinanceiro();

		if (tipoMovimentoFinanceiro != null) {

			if (tipoMovimentoFinanceiro.isAprovacaoAutomatica()) {

				movimentoFinanceiroCota.setAprovadoAutomaticamente(Boolean.TRUE);
				movimentoFinanceiroCota.setAprovador(
						movimentoFinanceiroCotaDTO.getUsuario());
				movimentoFinanceiroCota.setDataAprovacao(
						movimentoFinanceiroCotaDTO.getDataAprovacao());
				movimentoFinanceiroCota.setStatus(
						StatusAprovacao.APROVADO);

			} else {

				movimentoFinanceiroCota.setStatus(StatusAprovacao.PENDENTE);
			}

			movimentoFinanceiroCota.setCota(
					movimentoFinanceiroCotaDTO.getCota());
			movimentoFinanceiroCota.setTipoMovimento(
					tipoMovimentoFinanceiro);
			movimentoFinanceiroCota.setData(
					movimentoFinanceiroCotaDTO.getDataVencimento());
			movimentoFinanceiroCota.setDataCriacao(
					movimentoFinanceiroCotaDTO.getDataCriacao());
			movimentoFinanceiroCota.setUsuario(
					movimentoFinanceiroCotaDTO.getUsuario());
			movimentoFinanceiroCota.setValor(
					movimentoFinanceiroCotaDTO.getValor());
			movimentoFinanceiroCota.setLancamentoManual(
					movimentoFinanceiroCotaDTO.isLancamentoManual());
			movimentoFinanceiroCota.setBaixaCobranca(
					movimentoFinanceiroCotaDTO.getBaixaCobranca());
			movimentoFinanceiroCota.setObservacao(
					movimentoFinanceiroCotaDTO.getObservacao());
			
			movimentoFinanceiroCota.setMovimentos(movimentosEstoqueCota);
			
			movimentoFinanceiroCotaMerged = 
					this.movimentoFinanceiroCotaRepository.merge(movimentoFinanceiroCota);

			gerarHistoricoMovimentoFinanceiroCota(movimentoFinanceiroCotaMerged, movimentoFinanceiroCotaDTO.getTipoEdicao());

		}
		
		return movimentoFinanceiroCotaMerged;
	}
	
	
	/**
	 * @see br.com.abril.nds.service.MovimentoFinanceiroCotaService#obterMovimentosFinanceiroCota()
	 */
	@Override
	@Transactional
	public List<MovimentoFinanceiroCota> obterMovimentosFinanceiroCota(
			FiltroDebitoCreditoDTO filtroDebitoCreditoDTO) {

		return this.movimentoFinanceiroCotaRepository.obterMovimentosFinanceiroCota(
					filtroDebitoCreditoDTO
				);
	}

	/**
	 * @see br.com.abril.nds.service.MovimentoFinanceiroCotaService#obterContagemMovimentosFinanceiroCota(br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO)
	 */
	@Override
	@Transactional
	public Integer obterContagemMovimentosFinanceiroCota(
			FiltroDebitoCreditoDTO filtroDebitoCreditoDTO) {

		return this.movimentoFinanceiroCotaRepository.obterContagemMovimentosFinanceiroCota(
					filtroDebitoCreditoDTO
				);
	}

	/**
	 * @see br.com.abril.nds.service.MovimentoFinanceiroCotaService#removerMovimentoFinanceiroCota(java.lang.Long)
	 */
	@Override
	@Transactional
	public void removerMovimentoFinanceiroCota(Long idMovimento) {
		
		MovimentoFinanceiroCota movimentoFinanceiroCota = this.movimentoFinanceiroCotaRepository.buscarPorId(idMovimento);
		
		this.movimentoFinanceiroCotaRepository.remover(movimentoFinanceiroCota);
	}

	/**
	 * @see br.com.abril.nds.service.MovimentoFinanceiroCotaService#obterMovimentoFinanceiroCotaPorId(java.lang.Long)
	 */
	@Override
	@Transactional
	public MovimentoFinanceiroCota obterMovimentoFinanceiroCotaPorId(Long idMovimento) {
		
		return this.movimentoFinanceiroCotaRepository.buscarPorId(idMovimento);
	}
	
	/**
	 * @see br.com.abril.nds.service.MovimentoFinanceiroCotaService#obterSomatorioValorMovimentosFinanceiroCota(br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO)
	 */
	@Override
	@Transactional
	public BigDecimal obterSomatorioValorMovimentosFinanceiroCota(
			FiltroDebitoCreditoDTO filtroDebitoCreditoDTO) {

		return this.movimentoFinanceiroCotaRepository.obterSomatorioValorMovimentosFinanceiroCota(filtroDebitoCreditoDTO);
	}

	private void gerarHistoricoMovimentoFinanceiroCota(MovimentoFinanceiroCota movimentoFinanceiroCota, TipoEdicao tipoEdicao) {
		
		HistoricoMovimentoFinanceiroCota historicoMovimentoFinanceiroCota = 
				new HistoricoMovimentoFinanceiroCota();

		historicoMovimentoFinanceiroCota.setCota(movimentoFinanceiroCota.getCota());
		historicoMovimentoFinanceiroCota.setResponsavel(movimentoFinanceiroCota.getUsuario());
		historicoMovimentoFinanceiroCota.setTipoEdicao(tipoEdicao);
		historicoMovimentoFinanceiroCota.setTipoMovimento((TipoMovimentoFinanceiro) movimentoFinanceiroCota.getTipoMovimento());
		historicoMovimentoFinanceiroCota.setValor(movimentoFinanceiroCota.getValor());
		historicoMovimentoFinanceiroCota.setMovimentoFinanceiroCota(movimentoFinanceiroCota);
		historicoMovimentoFinanceiroCota.setDataEdicao(new Date());
		historicoMovimentoFinanceiroCota.setData(movimentoFinanceiroCota.getData());
		
		this.historicoMovimentoFinanceiroCotaRepository.adicionar(historicoMovimentoFinanceiroCota);
	}

    
	/**
	 * Obtém valores dos faturamentos bruto ou liquido das cotas no período
	 * @param cotas
	 * @param baseCalculo
	 * @param dataInicial
	 * @param dataFinal
	 * @return Map<Long,BigDecimal>: Faturamentos das cotas
	 */
	@Override
	@Transactional(readOnly=true)
	public Map<Long,BigDecimal> obterFaturamentoCotasPeriodo(List<Cota> cotas, BaseCalculo baseCalculo, Date dataInicial, Date dataFinal)
    {
		
		Map<Long,BigDecimal> res = null;
		
		List<CotaFaturamentoDTO> cotasFaturamento = this.movimentoFinanceiroCotaRepository.obterFaturamentoCotasPorPeriodo(cotas, dataInicial, dataFinal);

		if(cotasFaturamento!=null && cotasFaturamento.size()>0){
			res = new HashMap<Long,BigDecimal>();
			for(CotaFaturamentoDTO item:cotasFaturamento){
				if (baseCalculo == BaseCalculo.FATURAMENTO_BRUTO){
	        	    res.put(item.getIdCota(), item.getFaturamentoBruto());
				}
				if (baseCalculo == BaseCalculo.FATURAMENTO_LIQUIDO){
	        	    res.put(item.getIdCota(), item.getFaturamentoLiquido()); 
				}
	        }
			
	    }
				
		return res;
	}
	
	/*
	 * Gera um mapa de movimentos de estoque da cota por fornecedor. 
	 */
	private Map<Long, List<MovimentoEstoqueCota>> gerarMapaMovimentoEstoqueCotaPorFornecedor(
																List<MovimentoEstoqueCota> movimentosEstoqueCota) {
		
		Map<Long, List<MovimentoEstoqueCota>> mapaMovimentoEstoqueCotaPorFornecedor = 
			new HashMap<Long, List<MovimentoEstoqueCota>>();
		
		if (movimentosEstoqueCota == null
				|| movimentosEstoqueCota.isEmpty()) {
			
			return mapaMovimentoEstoqueCotaPorFornecedor;
		}
		
		for (MovimentoEstoqueCota movimentoEstoqueCota : movimentosEstoqueCota) {
			
			Fornecedor fornecedor =
				movimentoEstoqueCota.getProdutoEdicao().getProduto().getFornecedor();
			
			if (fornecedor != null) {
				
				List<MovimentoEstoqueCota> movimentosEstoqueCotaFornecedor = 
					mapaMovimentoEstoqueCotaPorFornecedor.get(fornecedor.getId());
				
				if (movimentosEstoqueCotaFornecedor == null) {
				
					movimentosEstoqueCotaFornecedor = new ArrayList<MovimentoEstoqueCota>();
				}
				
				movimentosEstoqueCotaFornecedor.add(movimentoEstoqueCota);
				
				mapaMovimentoEstoqueCotaPorFornecedor.put(fornecedor.getId(), movimentosEstoqueCotaFornecedor);
			}
		}
		
		return mapaMovimentoEstoqueCotaPorFornecedor;
	}
	
	
	//GERAÇÃO DE MOVIMENTOS FINANCEIROS PARA EXPEDIÇÃO E RECOLHIMENTO
	
	/**
	 * Obtém o valor total do reparte por movimento de estoque
	 * @param movimento
	 * @param dataOperacao
	 * @param dataRecolhimentoDistribuidor
	 * @return BigDecimal
	 */
	private BigDecimal obterValorTotalMovimentoEstoqueCota(MovimentoEstoqueCota movimento, 
														   Date dataOperacao,
														   Date dataRecolhimentoDistribuidor){

		ProdutoEdicao produtoEdicao 	= movimento.getProdutoEdicao();
		Long idProdutoEdicao = produtoEdicao.getId();
		BigDecimal valorReparte = BigDecimal.ZERO;
		boolean indParcialNaoFinal = false;
		Long idCota = movimento.getCota().getId();
		
		if(produtoEdicao.isParcial()) {
			
			LancamentoParcial lancamentoParcialFinal =  lancamentoParcialRepository.obterLancamentoParcial(idProdutoEdicao, dataOperacao);
			indParcialNaoFinal = (lancamentoParcialFinal == null);
		} 

		if(indParcialNaoFinal) {
			
			Date dataLancamento = lancamentoRepository.obterDataUltimoLancamentoParcial(idProdutoEdicao, dataOperacao);
			
			if(dataLancamento == null) {
				throw new IllegalStateException("Não foi possível validar a quantidade de " + 
												"reparte para o produtoEdicao de id: " + idProdutoEdicao);
			}
			
			valorReparte = (movimentoEstoqueCotaRepository.obterValorTotalMovimentoEstoqueCotaParaProdutoEdicaoNoPeriodo(
							idCota, 
							idProdutoEdicao, 
							dataLancamento, 
							dataRecolhimentoDistribuidor, 
							OperacaoEstoque.ENTRADA));
			
		} else {

			BigInteger qtdeDevolvida = BigInteger.ZERO;
			BigInteger qtdeRecebida = BigInteger.ZERO;
			BigDecimal valorUnitario = (produtoEdicao.getPrecoVenda()==null?BigDecimal.ZERO:produtoEdicao.getPrecoVenda());
			EstoqueProdutoCota estoqueProdutoCota = movimento.getEstoqueProdutoCota();
			
			if(estoqueProdutoCota != null) {
				
				qtdeDevolvida 	= (estoqueProdutoCota.getQtdeDevolvida() == null) ? BigInteger.ZERO : estoqueProdutoCota.getQtdeDevolvida();
				qtdeRecebida 	=  (estoqueProdutoCota.getQtdeRecebida() == null) ? BigInteger.ZERO : estoqueProdutoCota.getQtdeRecebida();

			}
			
			valorReparte = valorUnitario.multiply(new BigDecimal((qtdeRecebida.subtract(qtdeDevolvida)).longValue()));
		}
		
		return valorReparte;
	}
	
	/**
	 * Obtém o total de reparte para uma lista de movimentos de estoque para determinada cota.
	 * @param idCota
	 * @param idProdutoEdicao
	 * @param dataOperacao
	 * @param dataRecolhimentoDistribuidor
	 * @return BigInteger
	 */
	private BigDecimal obterValorTotalReparte(
			List<MovimentoEstoqueCota> movimentosEstoque,
			Long idCota, 
			Date dataOperacao,
			Date dataRecolhimentoDistribuidor) {
		
		BigDecimal valorReparteBigDecimal = BigDecimal.ZERO;
		BigDecimal valorTotalReparte = BigDecimal.ZERO;
			
		for (MovimentoEstoqueCota item : movimentosEstoque){
			
			valorReparteBigDecimal = this.obterValorTotalMovimentoEstoqueCota(item, dataOperacao, dataRecolhimentoDistribuidor);
			valorTotalReparte.add(valorReparteBigDecimal);
		}
		
		return valorTotalReparte;
	}
	
	/**
	 * Obtem valor total para geração de crédito na C.E.
	 * @param idControleConferenciaEncalheCota
	 * @return BigDecimal
	 */
	private BigDecimal obterValorTotalConferenciaEncalhe(Long idControleConferenciaEncalheCota, FormaComercializacao formaComercializacao, boolean consideraFormaComercializacaoNula){
		
		 Distribuidor distribuidor = distribuidorRepository.obter();
		 BigDecimal valorTotalEncalheOperacaoConferenciaEncalhe = 
					conferenciaEncalheRepository.obterValorTotalEncalheOperacaoConferenciaEncalhe(idControleConferenciaEncalheCota, distribuidor.getId(), formaComercializacao, consideraFormaComercializacaoNula);
			
		 if(valorTotalEncalheOperacaoConferenciaEncalhe == null) {
		     valorTotalEncalheOperacaoConferenciaEncalhe = BigDecimal.ZERO;
		 }
		
		 return valorTotalEncalheOperacaoConferenciaEncalhe;
	}
	
	/**
	 * Gera Movimento Financeiro para a cota (Chamada em Reparte ou Encalhe)
	 * @param cota
	 * @param movimentosEstoqueCota
	 * @param tipoMovimentoFinanceiro
	 * @param valor
	 * @param dataOperacao
	 * @param usuario
	 */
    private void gerarMovimentoFinanceiroCota(Cota cota, 
			    		                      List<MovimentoEstoqueCota> movimentosEstoqueCota,
			    		                      TipoMovimentoFinanceiro tipoMovimentoFinanceiro,
			    		                      BigDecimal valor,
			    		                      Date dataOperacao,
			    		                      Usuario usuario){
    	
    	MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = new MovimentoFinanceiroCotaDTO();
		
		movimentoFinanceiroCotaDTO.setCota(cota);
		movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro(tipoMovimentoFinanceiro);
		movimentoFinanceiroCotaDTO.setUsuario(usuario);
		movimentoFinanceiroCotaDTO.setValor(valor);
		movimentoFinanceiroCotaDTO.setDataOperacao(dataOperacao);
		movimentoFinanceiroCotaDTO.setBaixaCobranca(null);
		movimentoFinanceiroCotaDTO.setDataVencimento(dataOperacao);
		movimentoFinanceiroCotaDTO.setDataAprovacao(dataOperacao);
		movimentoFinanceiroCotaDTO.setDataCriacao(dataOperacao);
		movimentoFinanceiroCotaDTO.setObservacao(null);
		movimentoFinanceiroCotaDTO.setTipoEdicao(TipoEdicao.INCLUSAO);
		movimentoFinanceiroCotaDTO.setAprovacaoAutomatica(true);
		movimentoFinanceiroCotaDTO.setLancamentoManual(false);
		
		movimentoFinanceiroCotaDTO.setMovimentos(movimentosEstoqueCota);

		this.gerarMovimentosFinanceirosDebitoCredito(movimentoFinanceiroCotaDTO);
    };
    
    /**
	 * Gera movimento financeiro para cota na Expedição.
	 * @param movimentosEstoqueCota
	 * @param dataOperacao
	 * @param usuario
	 */
    @Transactional
	@Override
	public void gerarMovimentoFinanceiroCotaExpedicao(MovimentoEstoqueCota movimentosEstoqueCota,
										              Date dataOperacao,
										              Usuario usuario){
		
		Cota cota = movimentosEstoqueCota.getCota();
		
        ParametroCobrancaCota parametro = cota.getParametroCobranca();
        
		TipoCota tipoCota = parametro!=null?parametro.getTipoCota():null;
		
		EstoqueProdutoCota estoqueProdutoCota = movimentosEstoqueCota.getEstoqueProdutoCota();
		
		ProdutoEdicao produtoEdicao = (estoqueProdutoCota!=null?estoqueProdutoCota.getProdutoEdicao():null);
		
		BigDecimal precoVenda = (produtoEdicao!=null && produtoEdicao.getPrecoVenda()!=null)?produtoEdicao.getPrecoVenda():BigDecimal.ZERO;
		
		BigInteger quantidade = (estoqueProdutoCota!=null && estoqueProdutoCota.getQtdeRecebida()!=null)?estoqueProdutoCota.getQtdeRecebida():BigInteger.ZERO;
		
		BigDecimal total = precoVenda.multiply(new BigDecimal(quantidade.longValue()));	
		
		if (!total.equals(BigDecimal.ZERO)){
		
			return;
		}	
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE);
	
		//COTA TIPO A VISTA GERA CREDITO (DEBITO DO TOTAL DO REPARTE GERADO NA EXPEDICAO)
		if ((tipoCota==null) || tipoCota.equals(TipoCota.A_VISTA)){

			this.gerarMovimentoFinanceiroCota(cota, 
					                          Arrays.asList(movimentosEstoqueCota), 
					                          tipoMovimentoFinanceiro,
					                          total,
					                          dataOperacao,
					                          usuario);

		}
		//COTA TIPO CONSIGNADO GERA DEBITO DE PRODUTOD DO REPARTE COM FORMA DE NEGOCIACAO CONSIGNADO (REPARTE - ENCALHE)
		//COTA TIPO CONSIGNADO GERA CREDITO DE PRODUTO CONTA FIRME (DEBITO GERADO NA EXPEDICAO)
		else if (tipoCota.equals(TipoCota.CONSIGNADO)){
			
			Produto produto = produtoEdicao.getProduto();
			
			FormaComercializacao formaComercializacao = produto.getFormaComercializacao();
			
			if ((formaComercializacao == null ) || formaComercializacao.equals(FormaComercializacao.CONTA_FIRME)){
			    
				this.gerarMovimentoFinanceiroCota(cota, 
                          Arrays.asList(movimentosEstoqueCota), 
                          tipoMovimentoFinanceiro,
                          total,
                          dataOperacao,
                          usuario);
			}

		}
	}
	
	/**
	 * Gera movimento financeiro para cota na Conferencia de Encalhe
	 * @param controleConferenciaEncalheCota
	 */
	@Transactional
	@Override
    public void gerarMovimentoFinanceiroCotaRecolhimento(ControleConferenciaEncalheCota controleConferenciaEncalheCota){
		
		Cota  cota = controleConferenciaEncalheCota.getCota();
		
		ParametroCobrancaCota parametro = cota.getParametroCobranca();
		
		TipoCota tipoCota = parametro!=null?parametro.getTipoCota():null;
		
		Long idControleConferenciaEncalheCota = controleConferenciaEncalheCota.getId();
		
		List<MovimentoEstoqueCota> movimentosEstoqueCotaOperacaoConferenciaEncalhe;
		
		BigDecimal valorTotalEncalheOperacaoConferenciaEncalhe;
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro;
		
		//COTA TIPO A VISTA GERA CREDITO (DEBITO DO TOTAL DO REPARTE GERADO NA EXPEDICAO)
		if ((tipoCota==null) || tipoCota.equals(TipoCota.A_VISTA)){
			
			movimentosEstoqueCotaOperacaoConferenciaEncalhe = 
					movimentoEstoqueCotaRepository.obterListaMovimentoEstoqueCotaParaOperacaoConferenciaEncalhe(idControleConferenciaEncalheCota, null, false);
			
			valorTotalEncalheOperacaoConferenciaEncalhe = this.obterValorTotalConferenciaEncalhe(idControleConferenciaEncalheCota, null, false);
			
			if ((valorTotalEncalheOperacaoConferenciaEncalhe==null) || (valorTotalEncalheOperacaoConferenciaEncalhe.floatValue() <= 0f)){
				
				return;
			}
			
			tipoMovimentoFinanceiro = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.ENVIO_ENCALHE);
						
			this.gerarMovimentoFinanceiroCota(cota, 
					                          movimentosEstoqueCotaOperacaoConferenciaEncalhe, 
					                          tipoMovimentoFinanceiro,
					                          valorTotalEncalheOperacaoConferenciaEncalhe,
					                          controleConferenciaEncalheCota.getDataOperacao(),
					                          controleConferenciaEncalheCota.getUsuario());

		}
		//COTA TIPO CONSIGNADO GERA DEBITO DE PRODUTOD DO REPARTE COM FORMA DE NEGOCIACAO CONSIGNADO (REPARTE - ENCALHE)
		//COTA TIPO CONSIGNADO GERA CREDITO DE PRODUTO CONTA FIRME (DEBITO GERADO NA EXPEDICAO)
		else if (tipoCota.equals(TipoCota.CONSIGNADO)){
			
			
			
			
			//TODO: CASO A COTA NAO DEVOLVA O ENCALHE, VERIFICAR SE É O CASO DE GERAR A DIVIDA DO TOTAL, CONFORME REGRAS DEFINIDAS NOS PARAMETROS DA COTA (PRAZO,ETC) 
			
			
			
			
			//GERA CRÉDITOS REFERENTES AOS PRODUTOS CONTA FIRME QUE GERARAM DÉBITO NA EXPEDICAO, INDEPENDENTE DO TIPO DA COTA
			movimentosEstoqueCotaOperacaoConferenciaEncalhe = 
					movimentoEstoqueCotaRepository.obterListaMovimentoEstoqueCotaParaOperacaoConferenciaEncalhe(idControleConferenciaEncalheCota, FormaComercializacao.CONTA_FIRME, true);
			
			valorTotalEncalheOperacaoConferenciaEncalhe = this.obterValorTotalConferenciaEncalhe(idControleConferenciaEncalheCota, FormaComercializacao.CONTA_FIRME, true);

            if ((valorTotalEncalheOperacaoConferenciaEncalhe!=null) && (valorTotalEncalheOperacaoConferenciaEncalhe.floatValue() > 0f)){

				tipoMovimentoFinanceiro = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.ENVIO_ENCALHE);
				
				this.gerarMovimentoFinanceiroCota(cota, 
						                          movimentosEstoqueCotaOperacaoConferenciaEncalhe, 
						                          tipoMovimentoFinanceiro,
						                          valorTotalEncalheOperacaoConferenciaEncalhe,
						                          controleConferenciaEncalheCota.getDataOperacao(),
						                          controleConferenciaEncalheCota.getUsuario());

            }
            
			//GERA DEBITOS REFERENTES AOS PRODUTOS QUE NÃO GERARAM DÉBITO NA EXPEDICAO, DO TIPO DA COTA CONSIGNADO
			movimentosEstoqueCotaOperacaoConferenciaEncalhe = 
					movimentoEstoqueCotaRepository.obterListaMovimentoEstoqueCotaParaOperacaoConferenciaEncalhe(idControleConferenciaEncalheCota, FormaComercializacao.CONSIGNADO, false);
			
			valorTotalEncalheOperacaoConferenciaEncalhe = this.obterValorTotalConferenciaEncalhe(idControleConferenciaEncalheCota, FormaComercializacao.CONSIGNADO, false);

			BigDecimal valorTotalReparteOperacaoConferenciaEncalhe = this.obterValorTotalReparte(movimentosEstoqueCotaOperacaoConferenciaEncalhe, 
					                                                                             cota.getId(), 
					                                                                             controleConferenciaEncalheCota.getDataOperacao(), 
					                                                                             controleConferenciaEncalheCota.getDataOperacao()); 
				
			valorTotalReparteOperacaoConferenciaEncalhe = valorTotalReparteOperacaoConferenciaEncalhe.subtract(valorTotalEncalheOperacaoConferenciaEncalhe);
			
			if ((valorTotalReparteOperacaoConferenciaEncalhe!=null) && (valorTotalReparteOperacaoConferenciaEncalhe.floatValue() > 0f)){
			
	            tipoMovimentoFinanceiro = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.DEBITO_SOBRE_FATURAMENTO);
				
				this.gerarMovimentoFinanceiroCota(cota, 
						                          movimentosEstoqueCotaOperacaoConferenciaEncalhe, 
						                          tipoMovimentoFinanceiro,
						                          valorTotalReparteOperacaoConferenciaEncalhe,
						                          controleConferenciaEncalheCota.getDataOperacao(),
						                          controleConferenciaEncalheCota.getUsuario());
			}
		}
    	
	}
}
