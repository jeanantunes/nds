package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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
import br.com.abril.nds.exception.ImportacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.BaseCalculo;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.StatusEstoqueFinanceiro;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.HistoricoMovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.repository.HistoricoMovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.strategy.importacao.input.HistoricoFinanceiroInput;

@Service
public class MovimentoFinanceiroCotaServiceImpl implements
		MovimentoFinanceiroCotaService {

	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;

	@Autowired
	private HistoricoMovimentoFinanceiroCotaRepository historicoMovimentoFinanceiroCotaRepository;
	
	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private ConferenciaEncalheService conferenciaEncalheService;
	
	@Autowired
	private ConsolidadoFinanceiroRepository consolidadoFinanceiroRepository;  
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	@Transactional
	public List<MovimentoFinanceiroCota> gerarMovimentosFinanceirosDebitoCredito(
			MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO) {

		Map<Long, List<MovimentoEstoqueCota>> mapaMovimentoEstoqueCotaPorFornecedor = 
			this.gerarMapaMovimentoEstoqueCotaPorFornecedor(movimentoFinanceiroCotaDTO.getMovimentos());
		
		List<MovimentoFinanceiroCota> movimentosFinanceirosCota = new ArrayList<MovimentoFinanceiroCota>();
		
		MovimentoFinanceiroCota movimentoFinanceiroCota;
		
		if (mapaMovimentoEstoqueCotaPorFornecedor.isEmpty()) {
			
			movimentoFinanceiroCota = gerarMovimentoFinanceiroCota(movimentoFinanceiroCotaDTO, null);
			
			movimentosFinanceirosCota.add(movimentoFinanceiroCota);
	
		} else {
		
			for (Map.Entry<Long, List<MovimentoEstoqueCota>> entry : mapaMovimentoEstoqueCotaPorFornecedor.entrySet()) {
				
				movimentoFinanceiroCota = gerarMovimentoFinanceiroCota(movimentoFinanceiroCotaDTO, entry.getValue());
				
				movimentosFinanceirosCota.add(movimentoFinanceiroCota);
				
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
	
	/**
	 * Gera Movimento Financeiro para a cota (Chamada em Reparte ou Encalhe)
	 * @param cota
	 * @param movimentosEstoqueCota
	 * @param movimentosEstorno
	 * @param tipoMovimentoFinanceiro
	 * @param valor
	 * @param dataOperacao
	 * @param usuario
	 */
    private void gerarMovimentoFinanceiro(Cota cota, 
		    		                      List<MovimentoEstoqueCota> movimentosEstoqueCota,
		    		                      List<MovimentoEstoqueCota> movimentosEstorno,
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
		
		
		for(MovimentoEstoqueCota item:movimentosEstoqueCota){
			
			item.setStatusEstoqueFinanceiro(StatusEstoqueFinanceiro.FINANCEIRO_PROCESSADO);
			
			this.movimentoEstoqueCotaRepository.merge(item);
		}
		
		if (movimentosEstorno!=null){
	        for(MovimentoEstoqueCota item:movimentosEstorno){
				
				item.setStatusEstoqueFinanceiro(StatusEstoqueFinanceiro.FINANCEIRO_PROCESSADO);
				
				this.movimentoEstoqueCotaRepository.merge(item);
			}
		}

		this.gerarMovimentosFinanceirosDebitoCredito(movimentoFinanceiroCotaDTO);
    };
    
    /**
	 * Gera Financeiro para Movimentos de Estoque da Cota.
	 * @param cota
	 * @param movimentosEstoqueCota
	 * @param dataOperacao
	 * @param usuario
	 */
	private void gerarMovimentoFinanceiroCotaReparte(Cota cota,
			                                         List<MovimentoEstoqueCota> movimentosEstoqueCota,
			                                         List<MovimentoEstoqueCota> movimentosEstorno,
										             Date dataOperacao,
										             Usuario usuario){
		
        BigDecimal precoVendaItem = BigDecimal.ZERO;
		BigInteger quantidadeItem = BigInteger.ZERO;
		BigDecimal totalItem = BigDecimal.ZERO;		
		BigDecimal totalContaFirme = BigDecimal.ZERO;	
		BigDecimal totalGeral = BigDecimal.ZERO;
		BigDecimal totalEstorno = BigDecimal.ZERO;
		
		
		for(MovimentoEstoqueCota item : movimentosEstorno){

			ProdutoEdicao produtoEdicao = item.getProdutoEdicao();
			
			precoVendaItem = (produtoEdicao!=null && produtoEdicao.getPrecoVenda()!=null)?produtoEdicao.getPrecoVenda():BigDecimal.ZERO;
			quantidadeItem = (item.getQtde()!=null)?item.getQtde():BigInteger.ZERO;
			totalItem = precoVendaItem.multiply(new BigDecimal(quantidadeItem.longValue()));
					
			totalEstorno = totalEstorno.add(totalItem);
		}
		
		
		List<MovimentoEstoqueCota> movimentosProdutosContaFirme = new ArrayList<MovimentoEstoqueCota>();
		
		for (MovimentoEstoqueCota item : movimentosEstoqueCota){
			
			ProdutoEdicao produtoEdicao = item.getProdutoEdicao();
            Produto produto = produtoEdicao.getProduto();
			FormaComercializacao formaComercializacao = produto.getFormaComercializacao();
			
			precoVendaItem = (produtoEdicao!=null && produtoEdicao.getPrecoVenda()!=null)?produtoEdicao.getPrecoVenda():BigDecimal.ZERO;
			quantidadeItem = (item.getQtde()!=null)?item.getQtde():BigInteger.ZERO;
			totalItem = precoVendaItem.multiply(new BigDecimal(quantidadeItem.longValue()));
					
			totalGeral = totalGeral.add(totalItem);
			
			if ((formaComercializacao == null ) || formaComercializacao.equals(FormaComercializacao.CONTA_FIRME)){

				totalContaFirme = totalContaFirme.add(totalItem);
				
				movimentosProdutosContaFirme.add(item);
			}
			
		}
		
		
        ParametroCobrancaCota parametro = cota.getParametroCobranca();
        
		TipoCota tipoCota = parametro!=null?parametro.getTipoCota():null;	
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE);
	
		if ((tipoCota==null) || tipoCota.equals(TipoCota.A_VISTA)){
		
			totalGeral = totalGeral.subtract(totalEstorno!=null?totalEstorno:BigDecimal.ZERO);
			
			this.gerarMovimentoFinanceiro(cota, 
				                          movimentosEstoqueCota, 
				                          movimentosEstorno,
				                          tipoMovimentoFinanceiro,
				                          totalGeral,
				                          dataOperacao,
				                          usuario);

		}
		else if (tipoCota.equals(TipoCota.CONSIGNADO)){
			
			if ((movimentosProdutosContaFirme!=null && movimentosProdutosContaFirme.size()>0)&& 
				(totalContaFirme!=null && totalContaFirme.floatValue() > 0)){
			    
				this.gerarMovimentoFinanceiro(cota, 
					                          movimentosProdutosContaFirme, 
					                          null,
					                          tipoMovimentoFinanceiro,
					                          totalContaFirme,
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
		List<MovimentoEstoqueCota> movimentosEstoqueCotaOperacaoEnvioReparte = movimentoEstoqueCotaRepository.obterMovimentosPendentesGerarFinanceiro(cota.getId());
		List<MovimentoEstoqueCota> movimentosEstoqueCotaOperacaoEstorno = movimentoEstoqueCotaRepository.obterMovimentosEstornados(cota.getId());

		
		//GERA MOVIMENTOS FINANCEIROS PARA A COTA A VISTA E PRODUTOS CONTA FIRME
		if(movimentosEstoqueCotaOperacaoEnvioReparte!=null && movimentosEstoqueCotaOperacaoEnvioReparte.size()>0){
			
			this.gerarMovimentoFinanceiroCotaReparte(cota, 
					                                 movimentosEstoqueCotaOperacaoEnvioReparte, 
					                                 movimentosEstoqueCotaOperacaoEstorno,
					                                 controleConferenciaEncalheCota.getDataOperacao(),
					                                 controleConferenciaEncalheCota.getUsuario());
		}
		
		BigDecimal valorTotalEncalheOperacaoConferenciaEncalhe;
		BigDecimal valorTotalEstorno; 
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro;
		
		if ((tipoCota==null) || tipoCota.equals(TipoCota.A_VISTA)){
			
			//GERA CREDITOS
			
			movimentosEstoqueCotaOperacaoConferenciaEncalhe = 
					movimentoEstoqueCotaRepository.obterListaMovimentoEstoqueCotaParaOperacaoConferenciaEncalhe(idControleConferenciaEncalheCota);
			
			valorTotalEncalheOperacaoConferenciaEncalhe = this.conferenciaEncalheService.obterValorTotalConferenciaEncalhe(idControleConferenciaEncalheCota);
			
			if ((valorTotalEncalheOperacaoConferenciaEncalhe==null) || (valorTotalEncalheOperacaoConferenciaEncalhe.floatValue() <= 0)){
				
				return;
			}
			
			tipoMovimentoFinanceiro = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.ENVIO_ENCALHE);
						
			this.gerarMovimentoFinanceiro(cota, 
				                          movimentosEstoqueCotaOperacaoConferenciaEncalhe,
				                          null,
				                          tipoMovimentoFinanceiro,
				                          valorTotalEncalheOperacaoConferenciaEncalhe,
				                          controleConferenciaEncalheCota.getDataOperacao(),
				                          controleConferenciaEncalheCota.getUsuario());

		}
		else if (tipoCota.equals(TipoCota.CONSIGNADO)){
			
			//GERA DEBITOS DO CONSIGNADO
            
            valorTotalEstorno = this.movimentoEstoqueCotaRepository.obterValorTotalMovimentosEstornados(cota.getId()); 
            
            valorTotalEncalheOperacaoConferenciaEncalhe = this.conferenciaEncalheService.obterValorTotalConferenciaEncalhe(idControleConferenciaEncalheCota);
			
            movimentosEstoqueCotaOperacaoEnvioReparte = movimentoEstoqueCotaRepository.obterMovimentosPendentesGerarFinanceiro(cota.getId());
            
            BigDecimal valorTotalEnvioReparte = this.movimentoEstoqueCotaRepository.obterValorTotalMovimentosPendentesGerarFinanceiro(cota.getId()); 

            
			BigDecimal valorConsignadoPagar = BigDecimal.ZERO;
			
			
			valorTotalEnvioReparte = (valorTotalEnvioReparte!=null?valorTotalEnvioReparte:BigDecimal.ZERO);
			
			valorTotalEncalheOperacaoConferenciaEncalhe = (valorTotalEncalheOperacaoConferenciaEncalhe!=null?valorTotalEncalheOperacaoConferenciaEncalhe:BigDecimal.ZERO);
			
			valorTotalEstorno = (valorTotalEstorno!=null?valorTotalEstorno:BigDecimal.ZERO);
			
			valorConsignadoPagar = valorTotalEnvioReparte.subtract(valorTotalEstorno);
			
			
			if (valorTotalEncalheOperacaoConferenciaEncalhe.compareTo(valorConsignadoPagar) > 0){
				
				//CONFERENCIA MAIOR QUE REPARTE, GERA CREDITO DA DIFERENÇA
				
				BigDecimal valorCredito = BigDecimal.ZERO;
				
				valorCredito = valorTotalEncalheOperacaoConferenciaEncalhe.subtract(valorConsignadoPagar);
				
				movimentosEstoqueCotaOperacaoConferenciaEncalhe = 
						movimentoEstoqueCotaRepository.obterListaMovimentoEstoqueCotaParaOperacaoConferenciaEncalhe(idControleConferenciaEncalheCota);
				
                tipoMovimentoFinanceiro = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.ENVIO_ENCALHE);
				
				this.gerarMovimentoFinanceiro(cota, 
					                          movimentosEstoqueCotaOperacaoConferenciaEncalhe, 
					                          movimentosEstoqueCotaOperacaoEstorno,
					                          tipoMovimentoFinanceiro,
					                          valorCredito,
					                          controleConferenciaEncalheCota.getDataOperacao(),
					                          controleConferenciaEncalheCota.getUsuario());
			}
			else{
				
				//CONFERENCIA MENOR QUE REPARTE, GERA DEBITO DA DIFERENÇA
			
				valorConsignadoPagar = valorConsignadoPagar.subtract(valorTotalEncalheOperacaoConferenciaEncalhe);
				
				if ((valorConsignadoPagar!=null) && (valorConsignadoPagar.floatValue() > 0)){
				
		            tipoMovimentoFinanceiro = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.DEBITO_SOBRE_FATURAMENTO);
					
					this.gerarMovimentoFinanceiro(cota, 
						                          movimentosEstoqueCotaOperacaoEnvioReparte,
						                          movimentosEstoqueCotaOperacaoEstorno,
						                          tipoMovimentoFinanceiro,
						                          valorConsignadoPagar,
						                          controleConferenciaEncalheCota.getDataOperacao(),
						                          controleConferenciaEncalheCota.getUsuario());
				}
			}
		}
    	
	}

	@Override
	public void processarRegistrohistoricoFinanceiro(
			HistoricoFinanceiroInput valorInput) {
		
		Cota cota = validarHistoricoFinanceiroInput(valorInput);
		
		MovimentoFinanceiroCota movimento = new MovimentoFinanceiroCota();
		movimento.setCota(cota);
		movimento.setData(valorInput.getData());
		movimento.setDataAprovacao(new Date());
		movimento.setDataCriacao(new Date());
		movimento.setStatus(StatusAprovacao.APROVADO);
		movimento.setAprovadoAutomaticamente(true);
		movimento.setMotivo("VIRADA NDS");
		movimento.setAprovador(usuarioRepository.getUsuarioImportacao());
		movimento.setUsuario(usuarioRepository.getUsuarioImportacao());
		
		ConsolidadoFinanceiroCota cfc = consolidadoFinanceiroRepository.buscarPorCotaEData(cota, valorInput.getData());
		
		if (cfc == null) {
			cfc = new ConsolidadoFinanceiroCota();				
			cfc.setCota(cota);
			cfc.setDataConsolidado(valorInput.getData());
		} 		

		if (!valorInput.getValorPendente().equals(BigDecimal.ZERO)) {
			
			movimento.setValor(valorInput.getValorPendente());
			movimento.setTipoMovimento(tipoMovimentoFinanceiroRepository.buscarPorDescricao("Pendente"));	
			
			cfc.setPendente(valorInput.getValorPendente());
			
		}
		
		if (!valorInput.getValorPostergado().equals(BigDecimal.ZERO)) {
			
			movimento.setValor(valorInput.getValorPostergado());
			movimento.setTipoMovimento(tipoMovimentoFinanceiroRepository.buscarPorDescricao("Postergado"));		
	
			cfc.setValorPostergado(valorInput.getValorPostergado());
			
		}
		
		if (!valorInput.getValorFuturo().equals(BigDecimal.ZERO)) {
			
			movimento.setValor(valorInput.getValorFuturo());
			movimento.setTipoMovimento(
					tipoMovimentoFinanceiroRepository.buscarPorDescricao( 
							( valorInput.getValorFuturo().compareTo(BigDecimal.ZERO)  > 0 ?  "Crédito" : "Débito") 
					) 
			);
			
			cfc.setDebitoCredito(valorInput.getValorFuturo());
			
		}
		
		if (cfc.getId() == null) {						
			cfc.setTotal(movimento.getValor());
			cfc.getMovimentos().add(movimento);
			consolidadoFinanceiroRepository.adicionar(cfc);
		} else {
			cfc.getTotal().add( movimento.getValor() );
			cfc.getMovimentos().add(movimento);
			consolidadoFinanceiroRepository.alterar(cfc);				
		}
		
	}

	private Cota validarHistoricoFinanceiroInput(
			HistoricoFinanceiroInput valorInput) throws ImportacaoException {
		if (valorInput.getNumeroCota() == null) {
			throw new ImportacaoException("Cota não Informada."); 
		}
		
		if (
				valorInput.getValorFuturo() == null  
				|| valorInput.getValorPendente() == null  
				|| valorInput.getValorPostergado() == null  
			) {
			throw new ImportacaoException("Valor nulo."); 
		}
		
		if (!(
				valorInput.getValorFuturo().equals(BigDecimal.ZERO)  
				^ valorInput.getValorPendente().equals(BigDecimal.ZERO)  
				^ valorInput.getValorPostergado().equals(BigDecimal.ZERO)  
			)) {
			throw new ImportacaoException("Mais de um valor com valor."); 
		}
		
		if (
				valorInput.getValorFuturo().equals(BigDecimal.ZERO)  
				&& valorInput.getValorPendente().equals(BigDecimal.ZERO)  
				&& valorInput.getValorPostergado().equals(BigDecimal.ZERO)  
			) {
			throw new ImportacaoException("Todos os Valores Zerados.");
		}
		
		Cota cota = cotaService.obterCotaPDVPorNumeroDaCota(valorInput.getNumeroCota());		
		if (cota == null) {
			throw new ImportacaoException("Cota inexistente."); 
		}
		return cota;
	}
	
}
