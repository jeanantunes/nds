package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaFaturamentoDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ImportacaoException;
import br.com.abril.nds.exception.ValidacaoException;
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
import br.com.abril.nds.model.estoque.ValoresAplicados;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.HistoricoMovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.HistoricoMovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
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
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	@Override
	@Transactional
	public List<MovimentoFinanceiroCota> gerarMovimentosFinanceirosDebitoCredito(MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO) {

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
				movimentoFinanceiroCota.setAprovador(movimentoFinanceiroCotaDTO.getUsuario());
				movimentoFinanceiroCota.setDataAprovacao(movimentoFinanceiroCotaDTO.getDataAprovacao());
				movimentoFinanceiroCota.setStatus(StatusAprovacao.APROVADO);
			} else {

				movimentoFinanceiroCota.setStatus(StatusAprovacao.PENDENTE);
			}

			movimentoFinanceiroCota.setCota(movimentoFinanceiroCotaDTO.getCota());
			movimentoFinanceiroCota.setTipoMovimento(tipoMovimentoFinanceiro);
			movimentoFinanceiroCota.setData(movimentoFinanceiroCotaDTO.getDataVencimento());
			movimentoFinanceiroCota.setDataCriacao(movimentoFinanceiroCotaDTO.getDataCriacao());
			movimentoFinanceiroCota.setUsuario(movimentoFinanceiroCotaDTO.getUsuario());
			movimentoFinanceiroCota.setValor(movimentoFinanceiroCotaDTO.getValor());
			movimentoFinanceiroCota.setLancamentoManual(movimentoFinanceiroCotaDTO.isLancamentoManual());
			movimentoFinanceiroCota.setBaixaCobranca(movimentoFinanceiroCotaDTO.getBaixaCobranca());
			movimentoFinanceiroCota.setObservacao(movimentoFinanceiroCotaDTO.getObservacao());
			movimentoFinanceiroCota.setMovimentos(movimentosEstoqueCota);
			movimentoFinanceiroCota.setFornecedor(movimentoFinanceiroCotaDTO.getFornecedor());
			
			movimentoFinanceiroCotaMerged = this.movimentoFinanceiroCotaRepository.merge(movimentoFinanceiroCota);

			gerarHistoricoMovimentoFinanceiroCota(movimentoFinanceiroCotaMerged, movimentoFinanceiroCotaDTO.getTipoEdicao());
			
			if (movimentosEstoqueCota != null){
				
				for (MovimentoEstoqueCota est : movimentosEstoqueCota){
					
					est.setMovimentoFinanceiroCota(movimentoFinanceiroCotaMerged);
					
					this.movimentoEstoqueCotaRepository.merge(est);
				}
			}
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
		
		filtroDebitoCreditoDTO.setGrupoMovimentosFinanceirosDebitosCreditos(this.getGrupoMovimentosFinanceirosDebitosCreditos());
		
		return this.movimentoFinanceiroCotaRepository.obterMovimentosFinanceiroCota(
					filtroDebitoCreditoDTO
				);
	}
	
	public List<GrupoMovimentoFinaceiro> getGrupoMovimentosFinanceirosDebitosCreditos() {
		List<GrupoMovimentoFinaceiro> gruposMovimentosFinanceiros = new ArrayList<GrupoMovimentoFinaceiro>();
		
		gruposMovimentosFinanceiros.add(GrupoMovimentoFinaceiro.CREDITO);
		gruposMovimentosFinanceiros.add(GrupoMovimentoFinaceiro.DEBITO);
		gruposMovimentosFinanceiros.add(GrupoMovimentoFinaceiro.DEBITO_SOBRE_FATURAMENTO);
		gruposMovimentosFinanceiros.add(GrupoMovimentoFinaceiro.CREDITO_SOBRE_FATURAMENTO);
		gruposMovimentosFinanceiros.add(GrupoMovimentoFinaceiro.COMPRA_NUMEROS_ATRAZADOS);
		return gruposMovimentosFinanceiros;
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
			
			Fornecedor fornecedor = null;
			
			if (movimentoEstoqueCota != null &&
					movimentoEstoqueCota.getProdutoEdicao() != null &&
					movimentoEstoqueCota.getProdutoEdicao().getProduto() != null){
				
				fornecedor = movimentoEstoqueCota.getProdutoEdicao().getProduto().getFornecedor();
			}
			
			if (fornecedor == null &&
					movimentoEstoqueCota.getEstoqueProdutoCota() != null &&
					movimentoEstoqueCota.getEstoqueProdutoCota().getProdutoEdicao() != null &&
					movimentoEstoqueCota.getEstoqueProdutoCota().getProdutoEdicao().getProduto() != null){
				
				fornecedor = movimentoEstoqueCota.getEstoqueProdutoCota().getProdutoEdicao().getProduto().getFornecedor();
			}
			
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

	@Transactional(readOnly=true)
	@Override
	public boolean existeOutrosMovimentosFinanceiroCota(FiltroDebitoCreditoDTO filtroDebitoCredito, Long idMovimentoFinanceiroAtual) {
		List<Long> ids = this.movimentoFinanceiroCotaRepository.obterIdsMovimentosFinanceiroCota(filtroDebitoCredito);
		// Além do registro atual, existem outros, logo deve ser true
		if (ids.size() > 1) {
			return true;
		}
		
		// Se existe apenas um objeto e o id não é o mesmo
		if (ids.size() == 1 && !ids.get(0).equals(idMovimentoFinanceiroAtual) ) {
			return true;
		}
		
		return false;
	}
	
	@Transactional
	@Override
	public void removerPostergadosDia(Long idCota, List<TipoMovimentoFinanceiro> tiposMovimentoPostergado) {
		
		List<MovimentoFinanceiroCota> movs = 
				this.movimentoFinanceiroCotaRepository.obterMovimentosFinanceirosCotaPorTipoMovimento(
						idCota, null, tiposMovimentoPostergado, new Date());
		
		for (MovimentoFinanceiroCota mfc : movs){
			
			if (mfc.getMovimentos() != null){
				
				for (MovimentoEstoqueCota mec : mfc.getMovimentos()){
					
					mec.setMovimentoFinanceiroCota(null);
					this.movimentoEstoqueCotaRepository.merge(mec);
				}
			}
			
			this.movimentoFinanceiroCotaRepository.remover(mfc);
		}
	}

	
	/**
	 * Gera Movimento Financeiro para a cota (Chamada em Reparte ou Encalhe)
	 * @param cota
	 * @param fornecedor
	 * @param movimentosEstoqueCota
	 * @param movimentosEstorno
	 * @param tipoMovimentoFinanceiro
	 * @param valor
	 * @param dataOperacao
	 * @param usuario
	 */
    private void gerarMovimentoFinanceiro(Cota cota, 
    		                              Fornecedor fornecedor,
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
		movimentoFinanceiroCotaDTO.setFornecedor(fornecedor);
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
    }
    
    /**
     * Distingue Movimentos de Estoque da Cota por Fornecedor; 
     * Separa a lista de Movimentos de Estoque em outras listas;
     * Cada lista separada possui Movimentos de Estoque de um único Fornecedor.
     * @param movimentosEstoqueCota
     * @return Map<Long,List<MovimentoEstoqueCota>>
     */
    private Map<Long,List<MovimentoEstoqueCota>> agrupaMovimentosEstoqueCotaPorFornecedor(List<MovimentoEstoqueCota> movimentosEstoqueCota){
    	
    	Map<Long,List<MovimentoEstoqueCota>> movEstAgrup = new HashMap<Long,List<MovimentoEstoqueCota>>();

    	List<MovimentoEstoqueCota> mecs = new ArrayList<MovimentoEstoqueCota>();
    	
    	for (MovimentoEstoqueCota mec:movimentosEstoqueCota){
    		
    		Fornecedor fornecedor = mec.getProdutoEdicao()!=null?mec.getProdutoEdicao().getProduto()!=null?mec.getProdutoEdicao().getProduto()!=null?mec.getProdutoEdicao().getProduto().getFornecedor():null:null:null;
    		
    		if (fornecedor==null){
    			
    			throw new ValidacaoException(
    					TipoMensagem.WARNING, "Fornecedor não encontrado na geração de Movimento Financeiro para o Movimentos de Estoque ["+mec.getId()+"] !");
    		}
    		
    		mecs = movEstAgrup.get(fornecedor.getId());
    		
    		mecs = mecs==null?new ArrayList<MovimentoEstoqueCota>():mecs;
    		
    		mecs.add(mec);
			
			movEstAgrup.put(fornecedor.getId(), mecs);
    	}
    	
    	return movEstAgrup;
    }
    
    /**
     * Obtém movimentos de envio de reparte à cota que ainda não geraram financeiro
     * Agrupados por fornecedor
     * @param idCota
     * @param dataOperacao
     * @return Map<Long,List<MovimentoEstoqueCota>>
     */
    private Map<Long,List<MovimentoEstoqueCota>> obterMovimentosEstoqueReparte(Long idCota, Date dataOperacao){
		
		List<MovimentoEstoqueCota> movimentosEstoqueCotaOperacaoEnvioReparte = 
				        movimentoEstoqueCotaRepository.obterMovimentosPendentesGerarFinanceiro(
				        idCota, 
				        dataOperacao);
		
		Map<Long,List<MovimentoEstoqueCota>> movimentosReparteAgrupadosPorFornecedor = this.agrupaMovimentosEstoqueCotaPorFornecedor(movimentosEstoqueCotaOperacaoEnvioReparte);
		
		return movimentosReparteAgrupadosPorFornecedor;
	}
	
    /**
     * Obtém movimentos da conferência de encalhe
     * Agrupados por fornecedor
     * @param idCota
     * @param dataOperacao
     * @return Map<Long,List<MovimentoEstoqueCota>>
     */
    private Map<Long,List<MovimentoEstoqueCota>> obterMovimentosEstoqueEncalhe(Long idCota, Long idControleConferenciaEncalheCota){

		List<MovimentoEstoqueCota> movimentosEstoqueCotaOperacaoConferenciaEncalhe = 
				         movimentoEstoqueCotaRepository.obterListaMovimentoEstoqueCotaParaOperacaoConferenciaEncalhe(idControleConferenciaEncalheCota);
		
		Map<Long,List<MovimentoEstoqueCota>> movimentosEncalheAgrupadosPorFornecedor = this.agrupaMovimentosEstoqueCotaPorFornecedor(movimentosEstoqueCotaOperacaoConferenciaEncalhe);
		
		return movimentosEncalheAgrupadosPorFornecedor;
	}

    /**
     * Obtém movimentos estornados que não geram financeiro
     * Agrupados por fornecedor
     * @param idCota
     * @param dataOperacao
     * @return Map<Long,List<MovimentoEstoqueCota>>
     */
    private Map<Long,List<MovimentoEstoqueCota>> obterMovimentosEstoqueEstorno(Long idCota){
	
		List<MovimentoEstoqueCota> movimentosEstoqueCotaOperacaoEstorno = 
				         movimentoEstoqueCotaRepository.obterMovimentosEstornados(idCota);
		
		Map<Long,List<MovimentoEstoqueCota>> movimentosEstornoAgrupadosPorFornecedor = this.agrupaMovimentosEstoqueCotaPorFornecedor(movimentosEstoqueCotaOperacaoEstorno);

		return movimentosEstornoAgrupadosPorFornecedor;
    }
    
    /**
     * Obtém movimentos de envio de reparte à cota x fornecedor que ainda não geraram financeiro
     * @param idFornecedor
     * @param idCota
     * @param dataOperacao
     * @return List<MovimentoEstoqueCota>
     */
    private List<MovimentoEstoqueCota> obterMovimentosEstoqueReparte(Long idFornecedor, Long idCota, Date dataOperacao){

		Map<Long,List<MovimentoEstoqueCota>> movimentosReparteAgrupadosPorFornecedor = this.obterMovimentosEstoqueReparte(idCota, dataOperacao);
		
		return movimentosReparteAgrupadosPorFornecedor.get(idFornecedor);
	}
   
    /**
     * Retorna o somatório dos valores dos Movimentos de Estoque
     * @param movimentos
     * @return BigDecimal
     */
    private BigDecimal obterValorMovimentosEstoqueCota(List<MovimentoEstoqueCota> movimentos){
    	
    	BigDecimal total = BigDecimal.ZERO;
    	
    	if (movimentos==null){
    		
    		return total;
    	}
    	
    	for (MovimentoEstoqueCota m:movimentos){
    		
    		BigInteger qtd = m.getQtde();
    		
    		BigDecimal valor = m.getValoresAplicados()!=null?m.getValoresAplicados().getPrecoComDesconto()!=null?m.getValoresAplicados().getPrecoComDesconto():m.getProdutoEdicao().getPrecoVenda():m.getProdutoEdicao().getPrecoVenda();
    		
    		total = total.add(valor.multiply(new BigDecimal(qtd)));	
    	}
    	
    	return total;
    }
    
    /**
	 * Gera Financeiro para Movimentos de Estoque da Cota referentes à Envio de Reparte.
	 * @param cota
	 * @param fornecedor
	 * @param movimentosEstoqueCota - movimentos
	 * @param movimentosEstoqueCota - movimentos de estorno
	 * @param dataOperacao
	 * @param usuario
	 */
	private void gerarMovimentoFinanceiroCotaReparte(Cota cota,
			                                         Fornecedor fornecedor,
			                                         List<MovimentoEstoqueCota> movimentosEstoqueCota,
			                                         List<MovimentoEstoqueCota> movimentosEstorno,
										             Date dataOperacao,
										             Usuario usuario,
										             FormaComercializacao formaComercializacaoProduto){
		
        BigDecimal precoVendaItem = BigDecimal.ZERO;
		BigInteger quantidadeItem = BigInteger.ZERO;
		BigDecimal totalItem = BigDecimal.ZERO;		
		BigDecimal totalContaFirme = BigDecimal.ZERO;	
		BigDecimal totalGeral = BigDecimal.ZERO;
		BigDecimal totalEstorno = BigDecimal.ZERO;

		totalEstorno = this.obterValorMovimentosEstoqueCota(movimentosEstorno);

		List<MovimentoEstoqueCota> movimentosProdutosContaFirme = new ArrayList<MovimentoEstoqueCota>();
		
		if(movimentosEstoqueCota!=null){
		
			for (MovimentoEstoqueCota item : movimentosEstoqueCota){
				
				ProdutoEdicao produtoEdicao = item.getProdutoEdicao();
				
	            Produto produto = produtoEdicao.getProduto();
	            
				FormaComercializacao formaComercializacao = produto.getFormaComercializacao();
				
				ValoresAplicados va = item.getValoresAplicados();
				
				if (va != null && va.getPrecoComDesconto() != null){
					
					precoVendaItem = va.getPrecoComDesconto();
				} else {
					
					precoVendaItem = (produtoEdicao!=null && produtoEdicao.getPrecoVenda()!=null)?produtoEdicao.getPrecoVenda():BigDecimal.ZERO;
				}
				
				quantidadeItem = (item.getQtde()!=null)?item.getQtde():BigInteger.ZERO;
				
				totalItem = precoVendaItem.multiply(new BigDecimal(quantidadeItem.longValue()));
						
				totalGeral = totalGeral.add(totalItem);
				
				if ((formaComercializacao == null ) || formaComercializacao.equals(formaComercializacaoProduto)){
	
					totalContaFirme = totalContaFirme.add(totalItem);
					
					movimentosProdutosContaFirme.add(item);
				}
				
			}
		}
		
        ParametroCobrancaCota parametro = cota.getParametroCobranca();
        
		TipoCota tipoCota = parametro!=null?parametro.getTipoCota():null;	
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE);
	
		if ((tipoCota==null) || tipoCota.equals(TipoCota.A_VISTA)){
		
			totalGeral = totalGeral.subtract(totalEstorno!=null?totalEstorno:BigDecimal.ZERO);
			
			this.gerarMovimentoFinanceiro(cota, 
					                      fornecedor,
				                          movimentosEstoqueCota, 
				                          movimentosEstorno,
				                          tipoMovimentoFinanceiro,
				                          totalGeral,
				                          dataOperacao,
				                          usuario);

		}
		else if (tipoCota.equals(TipoCota.CONSIGNADO)){
			
			if ((movimentosProdutosContaFirme!=null && movimentosProdutosContaFirme.size()>0)&& 
				(totalContaFirme!=null && totalContaFirme.compareTo(BigDecimal.ZERO) > 0)){
			    
				this.gerarMovimentoFinanceiro(cota, 
						                      fornecedor,
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
	 * Gera movimentos financeiros Cota x Fornecedor
	 * @param cota
	 * @param fornecedor
	 * @param idControleConferenciaEncalheCota
	 * @param formaComercializacaoProduto
	 * @param controleConferenciaEncalheCota
	 * @param movimentosEstoqueCotaOperacaoEnvioReparte
	 * @param movimentosEstoqueCotaOperacaoConferenciaEncalhe
	 * @param movimentosEstoqueCotaOperacaoEstorno
	 */
	private void gerarMovimentoFinanceiroCotaRecolhimentoPorFornecedor(Cota cota,
			                                                           Fornecedor fornecedor,
															           Long idControleConferenciaEncalheCota,
			                                                           FormaComercializacao formaComercializacaoProduto,
			                                                           ControleConferenciaEncalheCota controleConferenciaEncalheCota,
			                                                           List<MovimentoEstoqueCota> movimentosEstoqueCotaOperacaoEnvioReparte,
			                                                           List<MovimentoEstoqueCota> movimentosEstoqueCotaOperacaoConferenciaEncalhe,
			                                                           List<MovimentoEstoqueCota> movimentosEstoqueCotaOperacaoEstorno
			                                                           ){
		
		ParametroCobrancaCota parametro = cota.getParametroCobranca();
		TipoCota tipoCota = parametro!=null?parametro.getTipoCota():null;
		
		//GERA MOVIMENTOS FINANCEIROS PARA A COTA A VISTA E PRODUTOS CONTA FIRME
		if(movimentosEstoqueCotaOperacaoEnvioReparte!=null && movimentosEstoqueCotaOperacaoEnvioReparte.size()>0){
			
			this.gerarMovimentoFinanceiroCotaReparte(cota, 
					                                 fornecedor,
					                                 movimentosEstoqueCotaOperacaoEnvioReparte, 
					                                 movimentosEstoqueCotaOperacaoEstorno,
					                                 controleConferenciaEncalheCota.getDataOperacao(),
					                                 controleConferenciaEncalheCota.getUsuario(),
					                                 formaComercializacaoProduto);
		}
		
		
		//ATUALIZA MOVIMENTOS PENDENTES DE GERAR FINANCEIRO APÓS GERAR MOVIMENTOS FINANCEIROS DE REPARTE 
		movimentosEstoqueCotaOperacaoEnvioReparte = this.obterMovimentosEstoqueReparte(fornecedor.getId(), cota.getId(), controleConferenciaEncalheCota.getDataOperacao());
		
		
		BigDecimal valorTotalEnvioReparte;
		BigDecimal valorTotalEncalheOperacaoConferenciaEncalhe;
		BigDecimal valorTotalEstorno; 
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro;
		
		if ((tipoCota==null) || tipoCota.equals(TipoCota.A_VISTA)){
			
			//GERA CREDITOS

			valorTotalEncalheOperacaoConferenciaEncalhe = this.obterValorMovimentosEstoqueCota(movimentosEstoqueCotaOperacaoConferenciaEncalhe);

			if ((valorTotalEncalheOperacaoConferenciaEncalhe==null) || (valorTotalEncalheOperacaoConferenciaEncalhe.floatValue() <= 0)){
				
				return;
			}
			
			tipoMovimentoFinanceiro = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.ENVIO_ENCALHE);
						
			this.gerarMovimentoFinanceiro(cota, 
					                      fornecedor,
				                          movimentosEstoqueCotaOperacaoConferenciaEncalhe,
				                          null,
				                          tipoMovimentoFinanceiro,
				                          valorTotalEncalheOperacaoConferenciaEncalhe,
				                          controleConferenciaEncalheCota.getDataOperacao(),
				                          controleConferenciaEncalheCota.getUsuario());

		}
		else if (tipoCota.equals(TipoCota.CONSIGNADO)){
			
			//GERA DEBITOS DO CONSIGNADO

			
            valorTotalEstorno = this.obterValorMovimentosEstoqueCota(movimentosEstoqueCotaOperacaoEstorno);

            valorTotalEncalheOperacaoConferenciaEncalhe = this.obterValorMovimentosEstoqueCota(movimentosEstoqueCotaOperacaoConferenciaEncalhe);

            valorTotalEnvioReparte = this.obterValorMovimentosEstoqueCota(movimentosEstoqueCotaOperacaoEnvioReparte);

            
			BigDecimal valorConsignadoPagar = valorTotalEnvioReparte.subtract(valorTotalEstorno);
			
			
			if (valorTotalEncalheOperacaoConferenciaEncalhe.compareTo(valorConsignadoPagar) > 0){
				
				//CONFERENCIA MAIOR QUE REPARTE, GERA CREDITO DA DIFERENÇA
				
				BigDecimal valorCredito = BigDecimal.ZERO;
				
				valorCredito = valorTotalEncalheOperacaoConferenciaEncalhe.subtract(valorConsignadoPagar);

				if (movimentosEstoqueCotaOperacaoConferenciaEncalhe!=null){

					for(MovimentoEstoqueCota mec : movimentosEstoqueCotaOperacaoConferenciaEncalhe) {
						
						if (mec.getProdutoEdicao() == null || mec.getProdutoEdicao().getProduto() == null){
							
							Long id = this.movimentoEstoqueCotaRepository.obterIdProdutoEdicaoPorControleConferenciaEncalhe(idControleConferenciaEncalheCota);
							
							mec.setProdutoEdicao(this.produtoEdicaoRepository.buscarPorId(id));
						}
					}
					
	                tipoMovimentoFinanceiro = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.ENVIO_ENCALHE);
					
					this.gerarMovimentoFinanceiro(cota, 
							                      fornecedor,
						                          movimentosEstoqueCotaOperacaoConferenciaEncalhe, 
						                          movimentosEstoqueCotaOperacaoEstorno,
						                          tipoMovimentoFinanceiro,
						                          valorCredito,
						                          controleConferenciaEncalheCota.getDataOperacao(),
						                          controleConferenciaEncalheCota.getUsuario());
				
				}
			}
			else{
				
				//CONFERENCIA MENOR QUE REPARTE, GERA DEBITO DA DIFERENÇA
			
				valorConsignadoPagar = valorConsignadoPagar.subtract(valorTotalEncalheOperacaoConferenciaEncalhe);
				
				if ((valorConsignadoPagar!=null) && (valorConsignadoPagar.floatValue() > 0)){
				
		            tipoMovimentoFinanceiro = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.DEBITO_SOBRE_FATURAMENTO);
					
					this.gerarMovimentoFinanceiro(cota, 
							                      fornecedor,
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
	
	/**
	 * Gera movimento financeiro para cota na Conferencia de Encalhe
	 * @param controleConferenciaEncalheCota
	 * @param formaComercializacaoProduto
	 */
	@Transactional
	@Override
    public void gerarMovimentoFinanceiroCotaRecolhimento(ControleConferenciaEncalheCota controleConferenciaEncalheCota,
    													 FormaComercializacao formaComercializacaoProduto){

		
		Cota  cota = controleConferenciaEncalheCota.getCota();
		
		Long idControleConferenciaEncalheCota = controleConferenciaEncalheCota.getId();
		

		//MOVIMENTOS DA CONFERENCIA DE ENCALHE AGUPADOS POR FORNECEDOR
		Map<Long,List<MovimentoEstoqueCota>> movimentosEncalheAgrupadosPorFornecedor = this.obterMovimentosEstoqueEncalhe(cota.getId(), idControleConferenciaEncalheCota);
		
		//MOVIMENTOS DE ENVIO DE REPARTE À COTA QUE AINDA NÃO GERARAM FINANCEIRO AGUPADOS POR FORNECEDOR
		Map<Long,List<MovimentoEstoqueCota>> movimentosReparteAgrupadosPorFornecedor = this.obterMovimentosEstoqueReparte(cota.getId(), controleConferenciaEncalheCota.getDataOperacao());

		//MOVIMENTOS ESTORNADOS QUE ENTRAM COMO CREDITO À COTA AGUPADOS POR FORNECEDOR
		Map<Long,List<MovimentoEstoqueCota>> movimentosEstornoAgrupadosPorFornecedor = this.obterMovimentosEstoqueEstorno(cota.getId());

		
		//TODOS OS FORNECEDORES ENVOLVIDOS
		Set<Long> fornecedoresId = new HashSet<Long>();
		fornecedoresId.addAll(movimentosReparteAgrupadosPorFornecedor.keySet());
		fornecedoresId.addAll(movimentosEncalheAgrupadosPorFornecedor.keySet());
		fornecedoresId.addAll(movimentosEstornoAgrupadosPorFornecedor.keySet());
		

		for (Long fornecedorId:fornecedoresId){
			
			Fornecedor fornecedor = this.fornecedorRepository.buscarPorId(fornecedorId);
			
	        this.gerarMovimentoFinanceiroCotaRecolhimentoPorFornecedor(cota, 
	        		                                                   fornecedor, 
	        		                                                   idControleConferenciaEncalheCota, 
	        		                                                   formaComercializacaoProduto, 
	        		                                                   controleConferenciaEncalheCota, 
	        		                                                   movimentosReparteAgrupadosPorFornecedor.get(fornecedorId), 
	        		                                                   movimentosEncalheAgrupadosPorFornecedor.get(fornecedorId), 
	        		                                                   movimentosEstornoAgrupadosPorFornecedor.get(fornecedorId));
		}   
	}
}