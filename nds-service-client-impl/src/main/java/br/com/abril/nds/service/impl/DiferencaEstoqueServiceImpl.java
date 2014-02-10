package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.DiferencaVO;
import br.com.abril.nds.client.vo.RateioCotaVO;
import br.com.abril.nds.client.vo.RelatorioLancamentoFaltasSobrasVO;
import br.com.abril.nds.dto.DebitoCreditoDTO;
import br.com.abril.nds.dto.DetalheDiferencaCotaDTO;
import br.com.abril.nds.dto.ImpressaoDiferencaEstoqueDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.RateioDiferencaCotaDTO;
import br.com.abril.nds.dto.RateioDiferencaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheDiferencaCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.LancamentoDiferenca;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.RateioDiferenca;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoDirecionamentoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.model.integracao.StatusIntegracao;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.LancamentoDiferencaRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.repository.RateioDiferencaRepository;
import br.com.abril.nds.repository.RecebimentoFisicoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.DebitoCreditoCotaService;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.MovimentoEstoqueCotaService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.TipoMovimentoFinanceiroService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.VisaoEstoqueService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.JasperUtil;
import br.com.abril.nds.vo.ValidacaoVO;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.estoque.Diferenca}  
 * 
 * @author Discover Technology
 *
 */
@Service
public class DiferencaEstoqueServiceImpl implements DiferencaEstoqueService {

	@Autowired
	private DiferencaEstoqueRepository diferencaEstoqueRepository;
	
	@Autowired
	private LancamentoDiferencaRepository lancamentoDiferencaRepository;

	@Autowired
	private RateioDiferencaRepository rateioDiferencaRepository;
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoRepository;
	
	@Autowired
	private EstudoCotaRepository estudoCotaRepository;
	
	@Autowired
	private ParametroSistemaRepository parametroSistemaRepository;
	
	@Autowired
	private RecebimentoFisicoRepository recebimentoFisicoRepository;
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;
	
	@Autowired
	private MovimentoEstoqueCotaService movimentoEstoqueCotaService;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private VisaoEstoqueService visaoEstoqueService;
	
	@Autowired
	private MovimentoEstoqueRepository movimentoEstoqueRepository;
	
	@Autowired
	private ParametrosDistribuidorService parametrosDistribuidorService;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private DebitoCreditoCotaService debitoCreditoCotaService;
	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
	
	@Autowired
	private TipoMovimentoFinanceiroService tipoMovimentoFinanceiroService;
	
	@Autowired
	private EstoqueProdutoService estoqueProdutoService;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	
	@Transactional(readOnly = true)
	public List<Diferenca> obterDiferencasLancamento(FiltroLancamentoDiferencaEstoqueDTO filtro) {
		
		return this.diferencaEstoqueRepository.obterDiferencasLancamento(filtro);
	}
	
	@Transactional(readOnly = true)
	public Long obterTotalDiferencasLancamento(FiltroLancamentoDiferencaEstoqueDTO filtro) {
		
		return this.diferencaEstoqueRepository.obterTotalDiferencasLancamento(filtro);
	}

	@Transactional(readOnly = true)
	public List<Diferenca> obterDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro) {
		
		if(filtro.getNumeroCota() != null) {
			Cota cota = this.cotaRepository.obterPorNumeroDaCota(filtro.getNumeroCota());
			filtro.setIdCota(cota.getId());
		}
		
		return this.diferencaEstoqueRepository.obterDiferencas(filtro);
	}
	
	@Transactional(readOnly = true)
	public Long obterTotalDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro) {
		
		Date dataInicialLancamento = calendarioService.subtrairDiasUteis(new Date(), 7);
		
		return this.diferencaEstoqueRepository.obterTotalDiferencas(filtro, dataInicialLancamento);
	}
	
	@Transactional(readOnly = true)
	public boolean verificarPossibilidadeExclusao(Long idDiferenca){
		if (idDiferenca == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Id da Diferença é obrigatório.");
		}
		
		Boolean diferenca = this.diferencaEstoqueRepository.buscarStatusDiferencaLancadaAutomaticamente(idDiferenca);
		
		return diferenca == null ? false : !diferenca;
	}
	
	@Transactional
	public void lancarDiferencaAutomaticaContagemDevolucao(Diferenca diferenca) {
		
		processarDiferenca(diferenca, TipoEstoque.LANCAMENTO, StatusConfirmacao.CONFIRMADO, true);
		
		StatusAprovacao statusAprovacao = StatusAprovacao.GANHO;
		
		if (TipoDiferenca.FALTA_DE.equals(diferenca.getTipoDiferenca())
				|| TipoDiferenca.FALTA_EM.equals(diferenca.getTipoDiferenca())){
			
			statusAprovacao = StatusAprovacao.PERDA;	
		}
		
		Usuario usuario = usuarioService.getUsuarioLogado();
		
		Lancamento ultimoLancamento = this.obterUltimoLancamentoProduto(diferenca);
		
		MovimentoEstoque movimentoEstoque = 
			this.gerarMovimentoEstoque(diferenca, usuario.getId(), 
				true, true, ultimoLancamento.getDataLancamentoDistribuidor(), null);
		
		movimentoEstoque.setStatus(statusAprovacao);
		
		movimentoEstoqueRepository.alterar(movimentoEstoque);
	}
	
	@Override
	@Transactional
	public Diferenca lancarDiferenca(Diferenca diferenca, TipoEstoque tipoEstoque) {
		
		boolean automatica = false;
		
		diferenca = processarDiferenca(diferenca, tipoEstoque,StatusConfirmacao.PENDENTE, automatica);
		
		return diferenca;
	}
	
	@Override
	@Transactional
	public Diferenca lancarDiferencaAutomatica(Diferenca diferenca, 
			                                   TipoEstoque tipoEstoque) {
		
		boolean automatica = true;
		
		diferenca = processarDiferenca(diferenca, tipoEstoque,StatusConfirmacao.PENDENTE, automatica);
		
		return diferenca;
	}
	
	@Override
	@Transactional
	public Diferenca lancarDiferencaAutomatica(Diferenca diferenca, 
											   TipoEstoque tipoEstoque, 
											   StatusAprovacao statusAprovacao,
											   Origem origem) {
		
		boolean automatica = true;
		
		diferenca = processarDiferenca(diferenca, tipoEstoque, StatusConfirmacao.CONFIRMADO, automatica);
		
		this.confirmarLancamentosDiferenca(Arrays.asList(diferenca), statusAprovacao, true, origem);
		
		return diferenca;
	}

	private Diferenca processarDiferenca(Diferenca diferenca, TipoEstoque tipoEstoque, StatusConfirmacao statusConfirmacao, boolean automatica) {
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		diferenca.setStatusConfirmacao(statusConfirmacao);
		diferenca.setTipoDirecionamento(TipoDirecionamentoDiferenca.ESTOQUE);
		diferenca.setAutomatica(automatica);
		diferenca.setDataMovimento(dataOperacao);
		diferenca.setTipoEstoque(tipoEstoque);
		
		return this.diferencaEstoqueRepository.merge(diferenca);
	}

	@Transactional
	public void efetuarAlteracoes(Set<Diferenca> listaNovasDiferencas,
			 					  Map<Long, List<RateioCotaVO>> mapaRateioCotas,
								  FiltroLancamentoDiferencaEstoqueDTO filtroPesquisa,
								  Long idUsuario,Boolean isDiferencaNova) {
		
		listaNovasDiferencas = salvarDiferenca(listaNovasDiferencas, mapaRateioCotas, idUsuario, isDiferencaNova, StatusConfirmacao.CONFIRMADO);
		
		boolean isAprovacaoMovimentoDiferencaAutomatico = distribuidorService.utilizaControleAprovacaoFaltaSobra();
		
		Origem origem  = (mapaRateioCotas!= null && mapaRateioCotas.size() > 0) ? Origem.TRANSFERENCIA_LANCAMENTO_FALTA_E_SOBRA_COTA  : null;
		
		this.confirmarLancamentosDiferenca(new ArrayList<>(listaNovasDiferencas), null, !isAprovacaoMovimentoDiferencaAutomatico, origem);
	} 
	
	@Transactional
	public void salvarLancamentosDiferenca(Set<Diferenca> listaNovasDiferencas,
										   Map<Long, List<RateioCotaVO>> mapaRateioCotas,
										   Long idUsuario,Boolean isDiferencaNova){
		
		
		salvarDiferenca(listaNovasDiferencas, mapaRateioCotas, idUsuario,isDiferencaNova, StatusConfirmacao.PENDENTE);
	}

	private Set<Diferenca> salvarDiferenca(Set<Diferenca> listaNovasDiferencas,
								Map<Long, List<RateioCotaVO>> mapaRateioCotas, 
								Long idUsuario, 
								Boolean isDiferencaNova,
								StatusConfirmacao statusConfirmacao) {
		
		Usuario usuario = usuarioService.buscar(idUsuario);
		
		Set<Diferenca> diferencasAtualizadas = new HashSet<>();
		
		for (Diferenca diferenca : listaNovasDiferencas) {

			Diferenca diferencaASalvar = (Diferenca) SerializationUtils.clone(diferenca);
			
			Long idDiferencaTemporario = diferencaASalvar.getId();
			
			if (isDiferencaNova != null && isDiferencaNova) {
				
				diferencaASalvar.setId(null);
				
				diferencaASalvar = this.diferencaEstoqueRepository.merge(diferencaASalvar);
			}
			
			List<RateioCotaVO> listaRateioCotas = null;
			
			if (mapaRateioCotas != null && !mapaRateioCotas.isEmpty()) {
				
				listaRateioCotas = mapaRateioCotas.remove(idDiferencaTemporario);
				
				mapaRateioCotas.put(diferencaASalvar.getId(), listaRateioCotas);
			}
			
			if (!TipoDirecionamentoDiferenca.ESTOQUE.equals(diferencaASalvar.getTipoDirecionamento())) {
				
				List<RateioDiferenca> rateiosCota = this.processarRateioCotas(diferencaASalvar, mapaRateioCotas, idUsuario); 
				
				if(rateiosCota!= null){
					diferencaASalvar.setRateios(rateiosCota);
				}
				else{
					diferencaASalvar.setRateios(rateioDiferencaRepository.obterRateiosPorDiferenca(diferencaASalvar.getId()));
				}
				diferencaASalvar.setExistemRateios(true);
				
			} else {
				
				rateioDiferencaRepository.removerRateioDiferencaPorDiferenca(diferencaASalvar.getId());
				diferencaASalvar.setExistemRateios(false);
			}
			
			diferencaASalvar.setStatusConfirmacao(statusConfirmacao);
			
			diferencaASalvar.setResponsavel(usuario);
			
			diferencaASalvar = this.diferencaEstoqueRepository.merge(diferencaASalvar);
			
			diferencasAtualizadas.add(diferencaASalvar);
		}
		
		return diferencasAtualizadas;
	}
	
	private void direcionarItensEstoque(Diferenca diferenca,
										BigInteger qntDiferenca, 
										boolean validarTransfEstoqueDiferenca,
										Date dataLancamento) {

		Diferenca diferencaRedirecionada = this.redirecionarDiferencaEstoque(qntDiferenca, diferenca);
	
		StatusAprovacao statusAprovacao = obterStatusLancamento(diferencaRedirecionada);
		
		MovimentoEstoque movimentoEstoque = 
			this.gerarMovimentoEstoque(
				diferencaRedirecionada, diferencaRedirecionada.getResponsavel().getId(),
					diferencaRedirecionada.isAutomatica(), 
						validarTransfEstoqueDiferenca, dataLancamento, null);
		
		LancamentoDiferenca lancamentoDiferenca =  
				this.gerarLancamentoDiferenca(statusAprovacao, movimentoEstoque, null);
		
		diferencaRedirecionada.setLancamentoDiferenca(lancamentoDiferenca);
		
		diferencaRedirecionada = diferencaEstoqueRepository.merge(diferencaRedirecionada);
		
		this.processarTransferenciaEstoque(diferencaRedirecionada,diferencaRedirecionada.getResponsavel().getId(), null);
	}
	
	private Diferenca redirecionarDiferencaEstoque(BigInteger qntDiferenca,Diferenca diferenca){
		
		Diferenca diferencaEstoque = new Diferenca();
		
		diferencaEstoque.setId(null);
		diferencaEstoque.setAutomatica(diferenca.isAutomatica());
		diferencaEstoque.setDataMovimento(this.distribuidorService.obterDataOperacaoDistribuidor());
		diferencaEstoque.setProdutoEdicao(diferenca.getProdutoEdicao());
		diferencaEstoque.setResponsavel(diferenca.getResponsavel());
		diferencaEstoque.setTipoDiferenca(diferenca.getTipoDiferenca());
		diferencaEstoque.setTipoEstoque(diferenca.getTipoEstoque());
		diferencaEstoque.setItemRecebimentoFisico(diferenca.getItemRecebimentoFisico());
		diferencaEstoque.setStatusConfirmacao(diferenca.getStatusConfirmacao());
		diferencaEstoque.setTipoDirecionamento(TipoDirecionamentoDiferenca.ESTOQUE);
		diferencaEstoque.setQtde(qntDiferenca);
		
		return diferencaEstoque;
	}
	
	@Transactional
	public void cancelarDiferencas(FiltroLancamentoDiferencaEstoqueDTO filtroPesquisa,
								   List<Long> idsDiferencasSelecionadas,
								   Long idUsuario) {
		
		Usuario usuario = usuarioService.buscar(idUsuario);
		
		filtroPesquisa.setPaginacao(null);
		filtroPesquisa.setOrdenacaoColuna(null);
		
		List<Diferenca> listaDiferencas =
			this.diferencaEstoqueRepository.obterDiferencasLancamento(filtroPesquisa);
		
		for (Diferenca diferenca : listaDiferencas) {
			
			if (idsDiferencasSelecionadas != null
					&& !idsDiferencasSelecionadas.isEmpty()) {
				
				if (!idsDiferencasSelecionadas.contains(diferenca.getId())) {
					
					continue;
				}
			}
			
			diferenca.setStatusConfirmacao(StatusConfirmacao.CANCELADO);
			diferenca.setResponsavel(usuario);
			
			this.diferencaEstoqueRepository.merge(diferenca);
		}
	}
	
	private Cota obterCotaDaDiferenca(Diferenca diferenca){
		
		if (diferenca.getRateios() == null) {
			
			return null;
		}
		
	    for (RateioDiferenca rd : diferenca.getRateios()){
	    	
	    	if (rd.getCota() != null) {
	    		
	    		return rd.getCota();
	    	}
	    }
		    
		return null;
	}

	private void confirmarLancamentosDiferenca(List<Diferenca> listaDiferencas, StatusAprovacao statusAprovacao, boolean isMovimentoDiferencaAutomatico, Origem origem) {

		Usuario usuario = usuarioService.getUsuarioLogado();
		
		for (Diferenca diferenca : listaDiferencas) {
			
			Lancamento ultimoLancamento = this.obterUltimoLancamentoProduto(diferenca);
			
			boolean produtoRecolhido = this.verificarRecolhimentoProdutoEdicao(ultimoLancamento, diferenca.getDataMovimento());
			
			diferenca.setStatusConfirmacao(StatusConfirmacao.CONFIRMADO);
			
			List<MovimentoEstoqueCota> listaMovimentosEstoqueCota = null;
			MovimentoEstoque movimentoEstoque = null;

			boolean validarTransfEstoqueDiferenca = TipoEstoque.LANCAMENTO.equals(diferenca.getTipoEstoque());
			
			if (diferenca.getRateios() != null && !diferenca.getRateios().isEmpty()) {
				
				listaMovimentosEstoqueCota = new ArrayList<MovimentoEstoqueCota>();
				
				BigInteger qntTotalRateio = BigInteger.ZERO;
				
				for (RateioDiferenca rateioDiferenca : diferenca.getRateios()) {
					
					qntTotalRateio = qntTotalRateio.add(rateioDiferenca.getQtde());
					
					MovimentoEstoqueCota movimentoEstoqueCota =
						this.gerarMovimentoEstoqueCota(diferenca, rateioDiferenca, 
							usuario.getId(), isMovimentoDiferencaAutomatico, 
								ultimoLancamento.getDataLancamentoDistribuidor());
					
					if (produtoRecolhido) {
						
						this.lancarDebitoCreditoCota(diferenca, rateioDiferenca, movimentoEstoqueCota, usuario);
					}
					
					listaMovimentosEstoqueCota.add(movimentoEstoqueCota);
				}
				
				if (diferenca.getTipoDiferenca().isSobra() 
						|| diferenca.getTipoDiferenca().isAlteracaoReparte()) {
					
					movimentoEstoque = this.gerarMovimentoEstoque(
						diferenca, diferenca.getResponsavel().getId(), diferenca.isAutomatica(),
							validarTransfEstoqueDiferenca, 
								ultimoLancamento.getDataLancamentoDistribuidor(), origem);
				}
				
				//Verifica se ha direcionamento de produtos para o estoque do distribuidor
				if (diferenca.getQtde().compareTo(qntTotalRateio) > 0) {
					
					this.direcionarItensEstoque(
						diferenca, diferenca.getQtde().subtract(qntTotalRateio), 
							validarTransfEstoqueDiferenca, ultimoLancamento.getDataLancamentoDistribuidor());
					
					diferenca.setQtde(qntTotalRateio);
				}
			} else {
				
				movimentoEstoque = 
					this.gerarMovimentoEstoque(diferenca, usuario.getId(),
						isMovimentoDiferencaAutomatico, validarTransfEstoqueDiferenca,
							ultimoLancamento.getDataLancamentoDistribuidor(), origem);
			}

			if (statusAprovacao == null) {
			
				statusAprovacao = obterStatusLancamento(diferenca);
			}
			
			LancamentoDiferenca lancamentoDiferenca = this.gerarLancamentoDiferenca(statusAprovacao, movimentoEstoque, listaMovimentosEstoqueCota);
			
			diferenca.setLancamentoDiferenca(lancamentoDiferenca);
			
			//TODO: Verificar com negocio a obrigatoriedade de ter Recebimento Fisico para lancar Diferenca
			ItemRecebimentoFisico itemRecebFisico = null;
			List<ItemRecebimentoFisico> itensRecebFisico = recebimentoFisicoRepository.obterItensRecebimentoFisicoDoProduto(diferenca.getProdutoEdicao().getId());
			if(itensRecebFisico != null && !itensRecebFisico.isEmpty()) {
				itemRecebFisico = itensRecebFisico.get(0);
				diferenca.setItemRecebimentoFisico(itemRecebFisico);
			}
			
			diferenca = this.diferencaEstoqueRepository.merge(diferenca);

			this.processarTransferenciaEstoque(diferenca, usuario.getId(), origem);

			diferencaEstoqueRepository.flush();
		}
	}
	
	private Boolean foraDoPrazoDoGFS(Diferenca diferenca) {
		
		return !this.validarDataLancamentoDiferenca(
					diferenca.getDataMovimento(), diferenca.getProdutoEdicao().getId(), 
						diferenca.getTipoDiferenca());
	}
	
	private Lancamento obterUltimoLancamentoProduto(Diferenca diferenca) {
		
		Lancamento ultimoLancamento = null;
		
		Cota cota = this.obterCotaDaDiferenca(diferenca);
		
		if (cota != null) {
		
			ultimoLancamento =
				this.lancamentoService.obterUltimoLancamentoDaEdicaoParaCota(
					diferenca.getProdutoEdicao().getId(), cota.getId());
			
		} else {	
			
			ultimoLancamento =
				this.lancamentoService.obterUltimoLancamentoDaEdicao(
					diferenca.getProdutoEdicao().getId());
		}
		
		return ultimoLancamento;		
	}
	
	private boolean verificarRecolhimentoProdutoEdicao(Lancamento lancamento, Date data) {
		
		if (lancamento == null || data == null) {
			
			return false;
		}
		
		return (lancamento.getDataRecolhimentoDistribuidor().compareTo(data) < 0);
	}
	
	private void lancarDebitoCreditoCota(Diferenca diferenca, 
										 RateioDiferenca rateioDiferenca,
										 MovimentoEstoqueCota movimentoEstoqueCota,
										 Usuario usuario) {
		
		DebitoCreditoDTO debitoCredito = new DebitoCreditoDTO();
		
		debitoCredito.setDataVencimento(
			DateUtil.formatarDataPTBR(
				this.calendarioService.adicionarDiasUteis(diferenca.getDataMovimento(), 1)));
		
		Cota cota = rateioDiferenca.getCota(); 
		
		debitoCredito.setNomeCota(cota.getPessoa().getNome());
		debitoCredito.setNumeroCota(cota.getNumeroCota());
		
		debitoCredito.setTipoMovimentoFinanceiro(
			this.obterTipoMovimentoFinanceiro(diferenca.getTipoDiferenca()));

		//null
		BigDecimal valor = 
			movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto()
				.multiply(new BigDecimal(rateioDiferenca.getQtde()));
		
		debitoCredito.setValor(valor.toString());

		debitoCredito.setIdUsuario(usuario.getId());
		
		MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = 
			this.debitoCreditoCotaService.gerarMovimentoFinanceiroCotaDTO(debitoCredito);
		
		movimentoFinanceiroCotaDTO.setTipoEdicao(TipoEdicao.INCLUSAO);
		
		this.movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(
			movimentoFinanceiroCotaDTO);
	}
	
	private TipoMovimentoFinanceiro obterTipoMovimentoFinanceiro(TipoDiferenca tipoDiferenca) {
		
		GrupoMovimentoFinaceiro grupoMovimentoFinaceiro = null;
		OperacaoFinaceira operacaoFinaceira = null;
		
		if (tipoDiferenca.isFalta()) {
			
			grupoMovimentoFinaceiro = GrupoMovimentoFinaceiro.CREDITO;
			operacaoFinaceira = OperacaoFinaceira.CREDITO;
			
		} else if (tipoDiferenca.isSobra()) {
			
			grupoMovimentoFinaceiro = GrupoMovimentoFinaceiro.DEBITO;
			operacaoFinaceira = OperacaoFinaceira.DEBITO;
		}
		
		return
			this.tipoMovimentoFinanceiroService
				.obterTipoMovimentoFincanceiroPorGrupoFinanceiroEOperacaoFinanceira(
						grupoMovimentoFinaceiro, operacaoFinaceira);
	}
	
	private void processarTransferenciaEstoque(Diferenca diferenca, Long idUsuario, Origem origem) {
		
		if (TipoEstoque.LANCAMENTO.equals(diferenca.getTipoEstoque())) {
			
			return;
		}
		
		if (diferenca.getRateios() != null && !diferenca.getRateios().isEmpty()) {
			
			return;
		}
		
		if (TipoEstoque.RECOLHIMENTO.equals(diferenca.getTipoEstoque())) {
			
			return;
		}
        
		
        if (TipoEstoque.GANHO.equals(diferenca.getTipoEstoque())) {
			
			return;
		}
        
        if (TipoEstoque.PERDA.equals(diferenca.getTipoEstoque())) {
			
			return;
		}
		
		TipoMovimentoEstoque tipoMovimentoEstoqueLancamento = null;
		
		TipoMovimentoEstoque tipoMovimentoEstoqueAlvo = null;
				
		if (OperacaoEstoque.SAIDA.equals(
				diferenca.getTipoDiferenca().getTipoMovimentoEstoque().getOperacaoEstoque())) {
			
			 tipoMovimentoEstoqueLancamento = 
				this.tipoMovimentoRepository.buscarTipoMovimentoEstoque(
					obterTipoMovimentoEstoqueTransferencia(
						TipoEstoque.LANCAMENTO, OperacaoEstoque.ENTRADA));
			 
			 tratarTipoMovimentoEstoque(
				tipoMovimentoEstoqueLancamento, "Tipo de movimento de entrada não encontrado!");
			
			 tipoMovimentoEstoqueAlvo = 
				this.tipoMovimentoRepository.buscarTipoMovimentoEstoque(
					obterTipoMovimentoEstoqueTransferencia(
						diferenca.getTipoEstoque(), OperacaoEstoque.SAIDA));
			
			 tratarTipoMovimentoEstoque(
				tipoMovimentoEstoqueAlvo, "Tipo de movimento de saída não encontrado!");
			 
		} else 
			if (OperacaoEstoque.ENTRADA.equals(
					diferenca.getTipoDiferenca().getTipoMovimentoEstoque().getOperacaoEstoque())) {
			
			 tipoMovimentoEstoqueLancamento = 
				this.tipoMovimentoRepository.buscarTipoMovimentoEstoque(
					obterTipoMovimentoEstoqueTransferencia(
						TipoEstoque.LANCAMENTO, OperacaoEstoque.SAIDA));
			
			 tratarTipoMovimentoEstoque(
				tipoMovimentoEstoqueLancamento, "Tipo de movimento de saída não encontrado!");
			
			 tipoMovimentoEstoqueAlvo = 
				this.tipoMovimentoRepository.buscarTipoMovimentoEstoque(
					obterTipoMovimentoEstoqueTransferencia(
						diferenca.getTipoEstoque(), OperacaoEstoque.ENTRADA));
			
			 tratarTipoMovimentoEstoque(
				tipoMovimentoEstoqueAlvo, "Tipo de movimento de entrada não encontrado!");
		}
		
		this.movimentoEstoqueService.gerarMovimentoEstoque(
			diferenca.getProdutoEdicao().getId(), idUsuario, diferenca.getQtde(),
				tipoMovimentoEstoqueLancamento, origem);
		
		this.movimentoEstoqueService.gerarMovimentoEstoque(
			diferenca.getProdutoEdicao().getId(), idUsuario, diferenca.getQtde(),
				tipoMovimentoEstoqueAlvo, origem);
	}
	
	private void tratarTipoMovimentoEstoque(TipoMovimentoEstoque tipoMovimento,String mensagem){
		
		if (tipoMovimento == null) {
			throw new ValidacaoException(TipoMensagem.WARNING,mensagem);
		}
	}
	
	private GrupoMovimentoEstoque obterTipoMovimentoEstoqueTransferencia(TipoEstoque tipoEstoque, OperacaoEstoque operacaoEstoque){
		
		switch(tipoEstoque) {
		
		case LANCAMENTO:
			return isOperacaoEntrada(operacaoEstoque) 
					? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO  
							   :GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_LANCAMENTO;
		case SUPLEMENTAR:
			return isOperacaoEntrada(operacaoEstoque) 
					? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR 
							   :GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_SUPLEMENTAR;
		case RECOLHIMENTO:
			return isOperacaoEntrada(operacaoEstoque) 
					? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_RECOLHIMENTO 
							   :GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_RECOLHIMENTO;
		case PRODUTOS_DANIFICADOS:
			return isOperacaoEntrada(operacaoEstoque) 
					? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DANIFICADOS
							   :GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DANIFICADOS;
		
		case DEVOLUCAO_FORNECEDOR:
			return isOperacaoEntrada(operacaoEstoque) 
					? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DEVOLUCAO_FORNECEDOR
							   :GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DEVOLUCAO_FORNECEDOR;
			
		case DEVOLUCAO_ENCALHE:
			return isOperacaoEntrada(operacaoEstoque) 
					? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DEVOLUCAO_ENCALHE
							   :GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DEVOLUCAO_ENCALHE;
			
		case GANHO:
			return GrupoMovimentoEstoque.GANHO_EM;	

		case PERDA:
			return GrupoMovimentoEstoque.PERDA_EM;
			
		default:
			throw new ValidacaoException(TipoMensagem.ERROR,"Erro para obter tipo de movimento estoque para lançamento de faltas e sobras!");
		}
		
	}
	
	private boolean isOperacaoEntrada(OperacaoEstoque operacaoEstoque){
		return (OperacaoEstoque.ENTRADA.equals(operacaoEstoque));
	}

	private StatusAprovacao obterStatusLancamento(Diferenca diferenca) {
		
		boolean utilizaControleAprovacao = parametrosDistribuidorService.getParametrosDistribuidor().getUtilizaControleAprovacao();
		
		StatusAprovacao statusAprovacao = null;
		if(!utilizaControleAprovacao) {
			statusAprovacao = StatusAprovacao.APROVADO;
		} else {
			statusAprovacao = StatusAprovacao.PENDENTE;
		}
		
		if (this.foraDoPrazoDoGFS(diferenca)) {
			
			if (diferenca.getTipoDiferenca().isFalta()) {
				
				statusAprovacao = StatusAprovacao.PERDA; 
				
			} else {
				
				statusAprovacao = StatusAprovacao.GANHO;
			}
		}
		
		return statusAprovacao;
	}
	
	private List<RateioDiferenca> processarRateioCotas(Diferenca diferenca,
									  Map<Long, List<RateioCotaVO>> mapaRateioCotas,
									  Long idUsuario) {
		
		if (mapaRateioCotas == null || mapaRateioCotas.isEmpty()) {
			
			return null;
		}
			
		List<RateioCotaVO> listaRateioCotaVO = mapaRateioCotas.get(diferenca.getId());
		
		if (listaRateioCotaVO == null || listaRateioCotaVO.isEmpty()) {
			
			return null;
		}
		
		List<Long> rateiosAssociadosDiferenca = new ArrayList<Long>();
		
		List<RateioDiferenca> rateiosProcessados = new ArrayList<>();
		
		for (RateioCotaVO rateioCotaVO : listaRateioCotaVO) {
			
			rateioCotaVO.setDataMovimento(diferenca.getDataMovimento());
		
			RateioDiferenca rateioDiferenca = null;
			
			if(rateioCotaVO.getIdRateio()!= null){
				
				rateioDiferenca = rateioDiferencaRepository.buscarPorId(rateioCotaVO.getIdRateio());
			}
			
			if(rateioDiferenca == null){
				
				rateioDiferenca = new RateioDiferenca();

				EstudoCota estudoCota = 
						this.estudoCotaRepository.obterEstudoCotaDeLancamentoComEstudoFechado(
								this.distribuidorService.obterDataOperacaoDistribuidor(),
								diferenca.getProdutoEdicao().getId(), 
								rateioCotaVO.getNumeroCota());
				
				rateioDiferenca.setEstudoCota(estudoCota);
				
				rateioDiferenca.setDiferenca(diferenca);
			}
			
			Cota cota = this.cotaRepository.obterPorNumeroDaCota(rateioCotaVO.getNumeroCota());
			
			rateioDiferenca.setCota(cota);
			
			rateioDiferenca.setQtde(rateioCotaVO.getQuantidade());
			
			rateioDiferenca.setDataNotaEnvio(rateioCotaVO.getDataEnvioNota());
			
			Lancamento ultimoLancamento =
				this.lancamentoService.obterUltimoLancamentoDaEdicao(
					diferenca.getProdutoEdicao().getId());
			
			rateioDiferenca.setDataMovimento(ultimoLancamento.getDataLancamentoDistribuidor());
			
			rateioDiferenca = this.rateioDiferencaRepository.merge(rateioDiferenca);
			
			rateiosAssociadosDiferenca.add(rateioDiferenca.getId());
			
			rateiosProcessados.add(rateioDiferenca);
		}
		
		if(!rateiosAssociadosDiferenca.isEmpty()){
			
			rateioDiferencaRepository.removerRateiosNaoAssociadosDiferenca(diferenca.getId(), rateiosAssociadosDiferenca);
		}
		
		return rateiosProcessados;
	}

	@Transactional(readOnly = true)
	public boolean validarDataLancamentoDiferenca(Date dataLancamentoDiferenca, 
												  Long idProdutoEdicao,
												  TipoDiferenca tipoDiferenca) {

		List<ItemRecebimentoFisico> listaItensRecebimentoFisico =
			this.recebimentoFisicoRepository.obterItensRecebimentoFisicoDoProduto(idProdutoEdicao);
		
		ParametroSistema parametroNumeroDiasPermitidoLancamento = 
			this.obterParametroNumeroDiasPermissaoLancamentoDiferenca(tipoDiferenca);
		
		Integer numeroDiasPermitidoLancamento = 0;
		
		if (parametroNumeroDiasPermitidoLancamento != null) {
			
			numeroDiasPermitidoLancamento = 
				Integer.parseInt(parametroNumeroDiasPermitidoLancamento.getValor());
		}
		
		for (ItemRecebimentoFisico itemRecebimentoFisico : listaItensRecebimentoFisico) {
			
			if (itemRecebimentoFisico.getRecebimentoFisico().getDataConfirmacao() == null) {
				
				itemRecebimentoFisico.getRecebimentoFisico().setDataConfirmacao(Calendar.getInstance().getTime());
			}
			
			Date dataConfirmacaoRecebimentoFisico =
				DateUtil.removerTimestamp(
					itemRecebimentoFisico.getRecebimentoFisico().getDataConfirmacao());
			
			long diferencaDias = DateUtil.obterDiferencaDias(
				dataConfirmacaoRecebimentoFisico, dataLancamentoDiferenca);
			
			if (diferencaDias == numeroDiasPermitidoLancamento
					|| diferencaDias < numeroDiasPermitidoLancamento) {
				
				return true;
			}
		}
		
		return false;
	}
	
	@Transactional(readOnly = true)
	public Diferenca obterDiferenca(Long id) {
		
		return this.diferencaEstoqueRepository.buscarPorId(id);
	}
	
	@Transactional(readOnly = true)
	public List<RateioCotaVO> obterRateiosCotaPorIdDiferenca(Long idDiferenca){
		
		List<RateioCotaVO> listaRetorno = new ArrayList<RateioCotaVO>();
		
		Diferenca diferenca = obterDiferenca(idDiferenca);
		
		if(diferenca!= null && diferenca.getRateios()!= null){
			
			for(RateioDiferenca rateio : diferenca.getRateios()){
				
				RateioCotaVO rateioVO = new RateioCotaVO();
				
				rateioVO.setIdRateio(rateio.getId());
				rateioVO.setIdDiferenca(rateio.getDiferenca().getId());
				rateioVO.setNomeCota(rateio.getCota().getPessoa().getNome());
				rateioVO.setNumeroCota(rateio.getCota().getNumeroCota());
				rateioVO.setQuantidade(rateio.getQtde());
				rateioVO.setDataEnvioNota(rateio.getDataNotaEnvio());
				
				Long reparteCota = 
						movimentoEstoqueCotaService.obterQuantidadeReparteProdutoCota
							(rateio.getDiferenca().getProdutoEdicao().getId(), rateio.getCota().getNumeroCota());
				
				rateioVO.setReparteCota(new BigInteger(reparteCota.toString()));
				
				listaRetorno.add(rateioVO);
			}
		}
		
		return listaRetorno;
	}
	
	/*
	 * Obtém o parâmetro de número de dias de permissão para lançamento de uma diferença.
	 * 
	 * @param tipoDiferenca - tipo de diferença
	 * 
	 * @return {@link ParametroSistema}
	 */
	private ParametroSistema obterParametroNumeroDiasPermissaoLancamentoDiferenca(TipoDiferenca tipoDiferenca) {
		
		ParametroSistema parametroNumeroDiasLancamento;
		
		switch (tipoDiferenca)  {
		
			case FALTA_DE:
				
				parametroNumeroDiasLancamento = 
					this.parametroSistemaRepository.buscarParametroPorTipoParametro(
						TipoParametroSistema.NUMERO_DIAS_PERMITIDO_LANCAMENTO_FALTA_DE); 
				
				break;
				
			case FALTA_EM:
				
				parametroNumeroDiasLancamento = 
					this.parametroSistemaRepository.buscarParametroPorTipoParametro(
						TipoParametroSistema.NUMERO_DIAS_PERMITIDO_LANCAMENTO_FALTA_EM);
				
				break;
				
			case SOBRA_DE:
				
				parametroNumeroDiasLancamento = 
					this.parametroSistemaRepository.buscarParametroPorTipoParametro(
						TipoParametroSistema.NUMERO_DIAS_PERMITIDO_LANCAMENTO_SOBRA_DE);
				
				break;
				
			case SOBRA_EM:
				
				parametroNumeroDiasLancamento = 
					this.parametroSistemaRepository.buscarParametroPorTipoParametro(
						TipoParametroSistema.NUMERO_DIAS_PERMITIDO_LANCAMENTO_SOBRA_EM);
				
				break;
				
			default:
				
				parametroNumeroDiasLancamento = null;
		}
		
		return parametroNumeroDiasLancamento;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public DetalheDiferencaCotaDTO obterDetalhesDiferencaCota(FiltroDetalheDiferencaCotaDTO filtro) {

		List<RateioDiferencaCotaDTO> detalhesDiferenca = rateioDiferencaRepository.obterRateioDiferencaCota(filtro);
		
		DetalheDiferencaCotaDTO detalheDiferencaCota = this.rateioDiferencaRepository.obterDetalhesDiferencaCota(filtro);

		if (detalheDiferencaCota == null) {

			detalheDiferencaCota = new DetalheDiferencaCotaDTO();
		}
		
		detalheDiferencaCota.setDetalhesDiferenca(detalhesDiferenca);
		
		return detalheDiferencaCota;
	}
	
	@Override
	@Transactional
	public void excluirLancamentoDiferenca(Long idDiferenca) {
		
		rateioDiferencaRepository.removerRateioDiferencaPorDiferenca(idDiferenca);
		
		Diferenca diferenca = this.diferencaEstoqueRepository.buscarPorId(idDiferenca);
		
		if (diferenca.getItemRecebimentoFisico() != null) {
			
			diferenca.getItemRecebimentoFisico().setDiferenca(null);
		}
		
		this.diferencaEstoqueRepository.remover(diferenca);
	}
	
	@Override
	@Transactional(readOnly = true)
	public BigInteger obterQuantidadeTotalDiferencas(String codigoProduto, Long numeroEdicao,
			  										 TipoEstoque tipoEstoque, Date dataMovimento) {
		
		return this.diferencaEstoqueRepository.obterQuantidadeTotalDiferencas(
						codigoProduto, numeroEdicao, tipoEstoque, dataMovimento);
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean existeDiferencaPorNota(Long idProdutoEdicao,
										  Date dataNotaEnvio,
										  Integer numeroCota) {
		
		return this.diferencaEstoqueRepository.existeDiferencaPorNota(
						idProdutoEdicao, dataNotaEnvio, numeroCota);
	}
	
	/*
	 * Efetua a geração da movimentação de estoque para diferença.
	 */
	private MovimentoEstoque gerarMovimentoEstoque(Diferenca diferenca, Long idUsuario,
												   boolean isAprovacaoAutomatica, 
												   boolean validarTransfEstoqueDiferenca,
												   Date dataLancamento, Origem origem) {
		
		GrupoMovimentoEstoque grupoMovimentoEstoque = null;
		
		TipoDiferenca tipoDiferenca = diferenca.getTipoDiferenca();
		
		StatusIntegracao statusIntegracao = null;
		
		if (tipoDiferenca.isAlteracaoReparte()) {
			
			statusIntegracao = StatusIntegracao.NAO_INTEGRAR;
		}
		
		if (!tipoDiferenca.isAlteracaoReparte() && this.foraDoPrazoDoGFS(diferenca)) {
			
			if(origem != null && origem.equals(Origem.TRANSFERENCIA_LANCAMENTO_FALTA_E_SOBRA_FECHAMENTO_ENCALHE)) {
				statusIntegracao = StatusIntegracao.ENCALHE;
			} else {
				statusIntegracao = StatusIntegracao.FORA_DO_PRAZO;
			}
			
			grupoMovimentoEstoque = obterGrupoMovimentoEstoqueForaDoPrazo(tipoDiferenca);
			
		} else {
			
			grupoMovimentoEstoque = tipoDiferenca.getTipoMovimentoEstoque();
		
		}
		
		TipoMovimentoEstoque tipoMovimentoEstoque = this.tipoMovimentoRepository.buscarTipoMovimentoEstoque(grupoMovimentoEstoque);
		
		if (tipoMovimentoEstoque == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de Movimento de Estoque não encontrado.");
		}
		
		return this.movimentoEstoqueService.gerarMovimentoEstoqueDiferenca(
			diferenca.getProdutoEdicao().getId(), idUsuario,
				diferenca.getQtde(), tipoMovimentoEstoque, 
					isAprovacaoAutomatica, validarTransfEstoqueDiferenca, dataLancamento, statusIntegracao, origem);
	}
	
	private GrupoMovimentoEstoque obterGrupoMovimentoEstoqueForaDoPrazo(TipoDiferenca tipoDiferenca) {
		
		GrupoMovimentoEstoque grupoMovimentoEstoque;
		
		if (tipoDiferenca.isDiferencaDe()) {
			
			if (tipoDiferenca.isFalta()) {
				
				grupoMovimentoEstoque = GrupoMovimentoEstoque.PERDA_DE;
				
			} else {
				
				grupoMovimentoEstoque = GrupoMovimentoEstoque.GANHO_DE;
			}
			
		} else {
			
			if (tipoDiferenca.isFalta() || tipoDiferenca.isPerda()) {
				
				grupoMovimentoEstoque = GrupoMovimentoEstoque.PERDA_EM;
				
			} else {
				
				grupoMovimentoEstoque = GrupoMovimentoEstoque.GANHO_EM;
			}
		}

		return grupoMovimentoEstoque;
	}
	
	/*
	 * Efetua a geração do movimento de estoque do rateio da diferença para cota.
	 */
	private MovimentoEstoqueCota gerarMovimentoEstoqueCota(Diferenca diferenca,
														   RateioDiferenca rateioDiferenca, 
														   Long idUsuario,
														   boolean isAprovacaoAutomatica,
														   Date dataLancamento) {

		TipoMovimentoEstoque tipoMovimentoEstoqueCota =
			this.tipoMovimentoRepository.buscarTipoMovimentoEstoque(
				diferenca.getTipoDiferenca().getGrupoMovimentoEstoqueCota());
		
		Long estudoCotaId = (rateioDiferenca.getEstudoCota() != null) 
								? rateioDiferenca.getEstudoCota().getId() : null;
		
		return this.movimentoEstoqueService.gerarMovimentoCotaDiferenca(
				dataLancamento, diferenca.getProdutoEdicao().getId(), rateioDiferenca.getCota().getId(),
					idUsuario, rateioDiferenca.getQtde(), tipoMovimentoEstoqueCota, estudoCotaId, isAprovacaoAutomatica);
	}
	
	/*
	 * Efetua a geração de lançamento de diferença.
	 */
	private LancamentoDiferenca gerarLancamentoDiferenca(StatusAprovacao statusAprovacao,
														 MovimentoEstoque movimentoEstoque,
														 List<MovimentoEstoqueCota> listaMovimentosEstoqueCota) {
		
		LancamentoDiferenca lancamentoDiferenca = new LancamentoDiferenca();
		
		lancamentoDiferenca.setDataProcessamento(new Date());
		lancamentoDiferenca.setMovimentoEstoque(movimentoEstoque);
		lancamentoDiferenca.setMovimentosEstoqueCota(listaMovimentosEstoqueCota);
		lancamentoDiferenca.setStatus(statusAprovacao);
		
		return this.lancamentoDiferencaRepository.merge(lancamentoDiferenca);
	}

	@Override
	@Transactional(readOnly = true)
	public byte[] imprimirRelatorioFaltasSobras(Date dataMovimento) throws Exception {
	 	
		List<ImpressaoDiferencaEstoqueDTO> dadosImpressao =
			this.diferencaEstoqueRepository.obterDadosParaImpressaoNaData(dataMovimento);
		
		if (dadosImpressao == null
				|| dadosImpressao.isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Não há dados para impressão nesta data");
		}
		
	 	List<RelatorioLancamentoFaltasSobrasVO> listaRelatorio =  
	 		new ArrayList<RelatorioLancamentoFaltasSobrasVO>();
	
	 	final int qtdeRateiosPorLinha = 5;
	 	
	 	for (ImpressaoDiferencaEstoqueDTO dadoImpressao : dadosImpressao) {
			
			List<RateioDiferencaDTO> rateios = 
					this.rateioDiferencaRepository.obterRateiosParaImpressaoPorDiferenca(
						dadoImpressao.getProdutoEdicao().getId(), dataMovimento);
			
			if (rateios != null && !rateios.isEmpty()) {
				
				if (rateios.size() <= qtdeRateiosPorLinha) {
					
					dadoImpressao.setRateios(rateios);
					
					listaRelatorio.add(
						new RelatorioLancamentoFaltasSobrasVO(dadoImpressao));
					
				} else {
					
					int qtdeLinhas = 
						(int) Math.ceil((double) rateios.size() / qtdeRateiosPorLinha);
					
					int indice = 0;
					
					for (int linha = 0; linha < qtdeLinhas; linha++) {
						
						ImpressaoDiferencaEstoqueDTO dadoImpressaoComRateio = 
					 		new ImpressaoDiferencaEstoqueDTO();
						
						if (linha == 0){
							dadoImpressaoComRateio.setIdDiferenca(dadoImpressao.getIdDiferenca());
					 		dadoImpressaoComRateio.setProdutoEdicao(dadoImpressao.getProdutoEdicao());
						}
				 		
				 		dadoImpressaoComRateio.setQtdeFaltas(dadoImpressao.getQtdeFaltas());
				 		dadoImpressaoComRateio.setQtdeSobras(dadoImpressao.getQtdeSobras());
				 		
				 		dadoImpressaoComRateio.setRateios(
				 			rateios.subList(indice, (indice += qtdeRateiosPorLinha) > rateios.size() ? rateios.size() : indice));
						
						listaRelatorio.add(
							new RelatorioLancamentoFaltasSobrasVO(dadoImpressaoComRateio));
					}
				}
			}
            //Dados impressão sem rateio
			else{
				
				dadoImpressao.setQtdeFaltas(null);
				dadoImpressao.setQtdeSobras(null);
				
				listaRelatorio.add(
						new RelatorioLancamentoFaltasSobrasVO(dadoImpressao));
			}
		}
		
		Map<String, Object> parametrosRelatorio = new HashMap<String, Object>();
		
		parametrosRelatorio.put("DISTRIBUIDOR", this.distribuidorService.obterRazaoSocialDistribuidor());
		parametrosRelatorio.put("DATA_MOVIMENTO", DateUtil.formatarDataPTBR(dataMovimento));
		parametrosRelatorio.put("LOGO_DISTRIBUIDOR", JasperUtil.getImagemRelatorio(this.parametrosDistribuidorService.getLogotipoDistribuidor()));
		
		JRBeanCollectionDataSource datasourceRelatorio = new JRBeanCollectionDataSource(listaRelatorio);
		
		URL urlRelatorio = 
			Thread.currentThread().getContextClassLoader().getResource("/reports/faltas_sobras.jasper");
		
		String caminhoRelatorio = urlRelatorio.toURI().getPath();
		
		return JasperRunManager.runReportToPdf(caminhoRelatorio, parametrosRelatorio, datasourceRelatorio);
	}
	
	@Override
	@Transactional(readOnly = true)
	public void validarDadosParaImpressaoNaData(String dataMovimentoFormatada) {
		
		if (dataMovimentoFormatada == null 
				|| dataMovimentoFormatada.trim().equals("")) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Informe uma data de movimento");
		}	
		
		Date dataMovimento = DateUtil.parseDataPTBR(dataMovimentoFormatada);
		
		Long quantidadeDadosImpressao = 
			this.diferencaEstoqueRepository.obterQuantidadeDadosParaImpressaoNaData(dataMovimento);
		
		if (quantidadeDadosImpressao == null 
				|| quantidadeDadosImpressao == 0L) {
		
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Não há dados para impressão nesta data");
		}
	}

	@Override
	@Transactional
	public HashMap<Long, Set<Diferenca>> verificarDiferencasIguais(
			Set<Diferenca> listaDiferencas,
			Diferenca diferenca) {
		
		Long idDiferenca = diferenca.getId();
	
		if(listaDiferencas != null && !listaDiferencas.isEmpty()) {
			
			Set<Diferenca> listaDiferencasClone = new HashSet<Diferenca>();
			listaDiferencasClone.addAll(listaDiferencas);
			Iterator<Diferenca> iterator = listaDiferencasClone.iterator();
			
			while( iterator.hasNext() ) {
				
				Diferenca diferencaCadastrada = iterator.next();
					
				if(diferencaCadastrada.getProdutoEdicao().equals(diferenca.getProdutoEdicao()) 
						&& diferencaCadastrada.getTipoDiferenca().equals(diferenca.getTipoDiferenca())
						&& diferencaCadastrada.getTipoDirecionamento().equals(diferenca.getTipoDirecionamento())
						&& diferencaCadastrada.getTipoEstoque().equals(diferenca.getTipoEstoque())) {
					
					idDiferenca = diferencaCadastrada.getId();
					
					BigInteger diferencaInicial = diferencaCadastrada.getQtde();
					BigDecimal valorTotalDiferenca = diferencaCadastrada.getValorTotalDiferenca();
					
					diferencaCadastrada.setQtde(diferencaInicial.add(diferenca.getQtde()));
					diferencaCadastrada.setValorTotalDiferenca(valorTotalDiferenca.add(diferenca.getValorTotalDiferenca()));
				} else {
					listaDiferencas.add(diferenca);
				}
			}
		} else {
			listaDiferencas.add(diferenca);
		}
		
		HashMap<Long, Set<Diferenca>> mapa = new HashMap<Long, Set<Diferenca>>();
		mapa.put(idDiferenca, listaDiferencas);
		
		return mapa;
	}

	@Override
	@Transactional
	public Set<DiferencaVO> verificarDiferencasVOIguais(
			Set<DiferencaVO> listaNovaDiferencaVO, DiferencaVO diferencaVO) {
		
		if(listaNovaDiferencaVO != null && ! listaNovaDiferencaVO.isEmpty()) {
			
			Set<DiferencaVO> listaDiferencasClone = new HashSet<DiferencaVO>();
			listaDiferencasClone.addAll(listaNovaDiferencaVO);
			Iterator<DiferencaVO> iterator = listaDiferencasClone.iterator();
			
			while( iterator.hasNext() ) {
				
				DiferencaVO diferencaVoCadastrada = iterator.next();
				
				if( diferencaVoCadastrada.getCodigoProduto().equals(diferencaVO.getCodigoProduto())  
						&& diferencaVoCadastrada.getNumeroEdicao().equals(diferencaVO.getNumeroEdicao()) 
						&& diferencaVoCadastrada.getTipoDiferenca().equals(diferencaVO.getTipoDiferenca())
						&& diferencaVoCadastrada.getTipoDirecionamento().equals(diferencaVO.getTipoDirecionamento())
						&& diferencaVoCadastrada.getTipoEstoque().equals(diferencaVO.getTipoEstoque())) {
				
					BigInteger quantidade = diferencaVoCadastrada.getQuantidade();
					
					diferencaVoCadastrada.setQuantidade(quantidade.add(diferencaVO.getQuantidade()));
				} else {
					listaNovaDiferencaVO.add(diferencaVO);
				}
			}
		} else {
			listaNovaDiferencaVO.add(diferencaVO);
		}
		return listaNovaDiferencaVO;
	}

	@Override
	@Transactional
	public Map<Long, List<RateioCotaVO>> insercaoDeNovosValoresNaMesmaCota(
			Map<Long, List<RateioCotaVO>> mapaRateiosCadastrados,
			List<RateioCotaVO> listaNovosRateios) {
		
			Set<Entry<Long, List<RateioCotaVO>>> entrySet = mapaRateiosCadastrados.entrySet();
			Iterator<Entry<Long, List<RateioCotaVO>>> iterator = entrySet.iterator();
			List<RateioCotaVO> listaRateiosCadastrados;
			
			while(iterator.hasNext()) {
				Entry<Long, List<RateioCotaVO>> entry = iterator.next();
				
				listaRateiosCadastrados = entry.getValue();				
				for(RateioCotaVO rateiosCadastrados : listaRateiosCadastrados ) {
					for(RateioCotaVO rateioCotaVO : listaNovosRateios) {
						if( rateiosCadastrados.getNumeroCota().equals(rateioCotaVO.getNumeroCota()) ) {
							
							BigInteger quantidade = rateiosCadastrados.getQuantidade();
							BigInteger reparteAtual = rateiosCadastrados.getReparteAtualCota();
							
							rateiosCadastrados.setQuantidade(quantidade.add(rateioCotaVO.getQuantidade()));
							rateiosCadastrados.setReparteAtualCota(reparteAtual.subtract(rateioCotaVO.getQuantidade()));
						}
					}
				}
			}
		return mapaRateiosCadastrados;
	}

	@Override
	@Transactional
	public Map<Long, List<RateioCotaVO>> verificarSeExisteListaNoMapa(
			Map<Long, List<RateioCotaVO>> mapaRateiosCadastrados, Long id,
			RateioCotaVO rateioCotaVO) {
		
		Set<Entry<Long, List<RateioCotaVO>>> entrySet = mapaRateiosCadastrados.entrySet();
		Iterator<Entry<Long, List<RateioCotaVO>>> iterator = entrySet.iterator();
		
		if(iterator.hasNext()) {
			while(iterator.hasNext()) {
				Entry<Long, List<RateioCotaVO>> keyValue = iterator.next();
				List<RateioCotaVO> listaValue = keyValue.getValue();
	
				for(RateioCotaVO value : listaValue) {
					if(rateioCotaVO.getNumeroCota().equals(value.getNumeroCota()) && 
							rateioCotaVO.getIdDiferenca().equals(value.getIdDiferenca()) ) {
							
							BigInteger quantidade = value.getQuantidade();
							BigInteger reparteAtual = value.getReparteAtualCota();
							
							value.setQuantidade(quantidade.add(rateioCotaVO.getQuantidade()));
							value.setReparteAtualCota(reparteAtual.subtract(rateioCotaVO.getQuantidade()));	
					}
				}
			}
		} else {
			
			List<RateioCotaVO> listaRateiosCadastrados = new ArrayList<RateioCotaVO>();
			listaRateiosCadastrados.add(rateioCotaVO);
			mapaRateiosCadastrados.put(id, listaRateiosCadastrados);
		
		}
		
		return mapaRateiosCadastrados;
	}

	@Override
	@Transactional
	public DiferencaVO verificarDiferencaComListaSessao(
			Set<DiferencaVO> listaNovasDiferencasVO, DiferencaVO diferencaVO,
			Long idDiferenca) {
		
		
		Iterator<DiferencaVO> iterator = listaNovasDiferencasVO.iterator();
		
		while(iterator.hasNext()) {
			DiferencaVO diferencaVoComparacao = iterator.next();
			if(diferencaVoComparacao.getId().equals(idDiferenca) && !diferencaVoComparacao.equals(diferencaVO)) {
				diferencaVO  = diferencaVoComparacao;
			}
		}
		return diferencaVO;
	}

	@Override
	@Transactional
	public Map<Long, List<RateioCotaVO>> incluirSeNaoExisteNoMapa(
			Map<Long, List<RateioCotaVO>> mapaRateiosCadastrados, Long id,
			RateioCotaVO rateioCotaVO) {
		
		List<RateioCotaVO> listaRateiosCadastrados = mapaRateiosCadastrados.get(id);
		
		if (listaRateiosCadastrados == null) {
		
			listaRateiosCadastrados = new ArrayList<RateioCotaVO>();
		}

		if (listaRateiosCadastrados.contains(rateioCotaVO)) {
			
			listaRateiosCadastrados.remove(rateioCotaVO);
		}
		
		listaRateiosCadastrados.add(rateioCotaVO);
		
		mapaRateiosCadastrados.put(id, listaRateiosCadastrados);
		
		return mapaRateiosCadastrados;
	}
	
	@Transactional(readOnly = true)
	public boolean validarProdutoEmRecolhimento(ProdutoEdicao produtoEdicao){
		
   		if(produtoEdicao == null){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Produto não encontrada."));
		}
		
		Lancamento lancamento = null;
		
		if(produtoEdicao.isParcial()){
			
			lancamento = lancamentoRepository.obterLancamentoParcialFinal(produtoEdicao.getId());
			
		}else{
			
			lancamento = lancamentoRepository.obterUltimoLancamentoDaEdicao(produtoEdicao.getId());
		}
		
		if(lancamento!= null 
				&& Arrays.asList(StatusLancamento.BALANCEADO_RECOLHIMENTO,
								 StatusLancamento.EM_RECOLHIMENTO,
								 StatusLancamento.RECOLHIDO,
								 StatusLancamento.FECHADO).contains(lancamento.getStatus())){
			
			return false;
		}
		
		return true;
		
	}
	
}