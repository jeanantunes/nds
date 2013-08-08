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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.DiferencaVO;
import br.com.abril.nds.client.vo.RateioCotaVO;
import br.com.abril.nds.client.vo.RelatorioLancamentoFaltasSobrasVO;
import br.com.abril.nds.dto.DetalheDiferencaCotaDTO;
import br.com.abril.nds.dto.ImpressaoDiferencaEstoqueDTO;
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
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
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
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.LancamentoDiferencaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.repository.RateioDiferencaRepository;
import br.com.abril.nds.repository.RecebimentoFisicoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.MovimentoEstoqueCotaService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.JasperUtil;

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
	private MovimentoEstoqueRepository movimentoEstoqueRepository;
	
	@Autowired
	private ParametrosDistribuidorService parametrosDistribuidorService;
	
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
			Cota cota = this.cotaRepository.obterPorNumerDaCota(filtro.getNumeroCota());
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
		
		processarDiferenca(diferenca, TipoEstoque.LANCAMENTO,StatusConfirmacao.CONFIRMADO);
		
		StatusAprovacao statusAprovacao = StatusAprovacao.GANHO;
		
		if(TipoDiferenca.FALTA_DE.equals(diferenca.getTipoDiferenca())
				||TipoDiferenca.FALTA_EM.equals(diferenca.getTipoDiferenca())){
			
			statusAprovacao = StatusAprovacao.PERDA;	
		}
		
		Usuario usuario = usuarioService.getUsuarioLogado();
		
		MovimentoEstoque movimentoEstoque = this.gerarMovimentoEstoque(diferenca, usuario.getId(), true, true);
		movimentoEstoque.setStatus(statusAprovacao);
		movimentoEstoqueRepository.alterar(movimentoEstoque);
	}
	
	@Transactional
	public Diferenca lancarDiferenca(Diferenca diferenca, TipoEstoque tipoEstoque) {
		
		diferenca = processarDiferenca(diferenca, tipoEstoque,StatusConfirmacao.PENDENTE);
		
		return diferenca;
	}

	
	@Transactional
	public Diferenca lancarDiferencaAutomatica(Diferenca diferenca, 
											   TipoEstoque tipoEstoque, 
											   StatusAprovacao statusAprovacao) {
		
		processarDiferenca(diferenca, tipoEstoque, StatusConfirmacao.CONFIRMADO);
		
		this.confirmarLancamentosDiferenca(Arrays.asList(diferenca), statusAprovacao,true);
		
		return diferenca;
	}

	private Diferenca processarDiferenca(Diferenca diferenca, TipoEstoque tipoEstoque,StatusConfirmacao statusConfirmacao) {
		
		diferenca.setStatusConfirmacao(statusConfirmacao);
		diferenca.setTipoDirecionamento(TipoDirecionamentoDiferenca.ESTOQUE);
		diferenca.setAutomatica(true);
		diferenca.setDataMovimento(this.distribuidorService.obterDataOperacaoDistribuidor());
		diferenca.setTipoEstoque(tipoEstoque);
		
		return this.diferencaEstoqueRepository.merge(diferenca);
	}
	
	@Override
	@Transactional
	public void gerarMovimentoEstoqueDiferenca(Diferenca diferenca, Long idUsuario) {
		gerarMovimentoEstoque(diferenca, idUsuario,true, true);
	}
	
	@Transactional
	public void efetuarAlteracoes(Set<Diferenca> listaNovasDiferencas,
			 					  Map<Long, List<RateioCotaVO>> mapaRateioCotas,
								  FiltroLancamentoDiferencaEstoqueDTO filtroPesquisa,
								  Long idUsuario,Boolean isDiferencaNova) {
		
		listaNovasDiferencas = salvarDiferenca(listaNovasDiferencas, mapaRateioCotas, idUsuario, isDiferencaNova, StatusConfirmacao.PENDENTE);
		
		boolean isAprovacaoMovimentoDiferencaAutomatico = distribuidorService.utilizaControleAprovacaoFaltaSobra();
		
		this.confirmarLancamentosDiferenca(new ArrayList<>(listaNovasDiferencas), null,!isAprovacaoMovimentoDiferencaAutomatico);
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
		
		Set<Diferenca>diferencasAtualizadas = new HashSet<>();
		
		for (Diferenca diferenca : listaNovasDiferencas) {

			Long idDiferencaTemporario = diferenca.getId();
			
			if (isDiferencaNova != null && isDiferencaNova) {
				
				diferenca.setId(null);
				
				diferenca = this.diferencaEstoqueRepository.merge(diferenca);
			}
			
			List<RateioCotaVO> listaRateioCotas = null;
			
			if (mapaRateioCotas != null && !mapaRateioCotas.isEmpty()) {
				
				listaRateioCotas = mapaRateioCotas.remove(idDiferencaTemporario);
				
				mapaRateioCotas.put(diferenca.getId(), listaRateioCotas);
			}
			
			if (!TipoDirecionamentoDiferenca.ESTOQUE.equals(diferenca.getTipoDirecionamento())) {
				
				List<RateioDiferenca> rateiosCota = this.processarRateioCotas(diferenca, mapaRateioCotas, idUsuario); 
				
				if(rateiosCota!= null){
					diferenca.setRateios(rateiosCota);
				}
				else{
					diferenca.setRateios(rateioDiferencaRepository.obterRateiosPorDiferenca(diferenca.getId()));
				}
				diferenca.setExistemRateios(true);
				
			} else {
				
				rateioDiferencaRepository.removerRateioDiferencaPorDiferenca(diferenca.getId());
				diferenca.setExistemRateios(false);
			}
			
			diferenca.setStatusConfirmacao(statusConfirmacao);
			
			diferenca.setResponsavel(usuario);
			
			diferenca = this.diferencaEstoqueRepository.merge(diferenca);
			
			diferencasAtualizadas.add(diferenca);
		}
		
		return diferencasAtualizadas;
	}
	
	private void direcionarItensEstoque(Diferenca diferenca,BigInteger qntDiferenca, boolean validarTransfEstoqueDiferenca){

		Diferenca diferencaRedirecionada = this.redirecionarDiferencaEstoque(qntDiferenca, diferenca);
	
		StatusAprovacao statusAprovacao = obterStatusLancamento(diferencaRedirecionada);
		
		MovimentoEstoque movimentoEstoque = 
				this.gerarMovimentoEstoque(diferencaRedirecionada, diferencaRedirecionada.getResponsavel().getId(),diferencaRedirecionada.isAutomatica(),
										   validarTransfEstoqueDiferenca);
		
		LancamentoDiferenca lancamentoDiferenca =  
				this.gerarLancamentoDiferenca(statusAprovacao, movimentoEstoque, null);
		
		diferencaRedirecionada.setLancamentoDiferenca(lancamentoDiferenca);
		
		diferencaRedirecionada = diferencaEstoqueRepository.merge(diferencaRedirecionada);
		
		this.processarTransferenciaEstoque(diferencaRedirecionada,diferencaRedirecionada.getResponsavel().getId());
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
								   Long idUsuario) {
		
		Usuario usuario = usuarioService.buscar(idUsuario);
		
		filtroPesquisa.setPaginacao(null);
		filtroPesquisa.setOrdenacaoColuna(null);
		
		List<Diferenca> listaDiferencas =
			this.diferencaEstoqueRepository.obterDiferencasLancamento(filtroPesquisa);
		
		for (Diferenca diferenca : listaDiferencas) {
			
			diferenca.setStatusConfirmacao(StatusConfirmacao.CANCELADO);
			diferenca.setResponsavel(usuario);
			
			this.diferencaEstoqueRepository.merge(diferenca);
		}
	}

	private void confirmarLancamentosDiferenca(List<Diferenca> listaDiferencas, StatusAprovacao statusAprovacao,boolean isMovimentoDiferencaAutomatico) {
		
		Usuario usuario = usuarioService.getUsuarioLogado();
		
		for (Diferenca diferenca : listaDiferencas) {
			
			diferenca.setStatusConfirmacao(StatusConfirmacao.CONFIRMADO);
			
			List<MovimentoEstoqueCota> listaMovimentosEstoqueCota = null;
			MovimentoEstoque movimentoEstoque = null;
			
			boolean validarTransfEstoqueDiferenca = diferenca.getTipoEstoque().equals(TipoEstoque.LANCAMENTO);
			
			if (diferenca.getRateios() != null && !diferenca.getRateios().isEmpty()) {
					
				listaMovimentosEstoqueCota = new ArrayList<MovimentoEstoqueCota>();
				
				BigInteger qntTotalRateio = BigInteger.ZERO;
				
				for (RateioDiferenca rateioDiferenca : diferenca.getRateios()) {
					
					qntTotalRateio = qntTotalRateio.add(rateioDiferenca.getQtde());
					
					listaMovimentosEstoqueCota.add(
						this.gerarMovimentoEstoqueCota(diferenca, rateioDiferenca, 
							usuario.getId(), isMovimentoDiferencaAutomatico));
				}
				
				if (diferenca.getTipoDiferenca().isSobra()) {
					
					this.direcionarItensEstoque(diferenca, qntTotalRateio, validarTransfEstoqueDiferenca);
				}
				
				//Verifica se ha direcionamento de produtos para o estoque do distribuidor
				if(diferenca.getQtde().compareTo(qntTotalRateio)>0){
					
					this.direcionarItensEstoque(diferenca, diferenca.getQtde().subtract(qntTotalRateio), validarTransfEstoqueDiferenca);
					
					diferenca.setQtde(qntTotalRateio);
				}
				
			} else {
				
				movimentoEstoque = this.gerarMovimentoEstoque(diferenca, usuario.getId(),
															  isMovimentoDiferencaAutomatico,
															  validarTransfEstoqueDiferenca);
			}

			if (statusAprovacao == null) {
			
				statusAprovacao = obterStatusLancamento(diferenca);
			}
			
			LancamentoDiferenca lancamentoDiferenca =  
				this.gerarLancamentoDiferenca(statusAprovacao, movimentoEstoque, listaMovimentosEstoqueCota);
			
			diferenca.setLancamentoDiferenca(lancamentoDiferenca);
			
			diferenca = this.diferencaEstoqueRepository.merge(diferenca);
					
			this.processarTransferenciaEstoque(diferenca,usuario.getId());
			
			diferencaEstoqueRepository.flush();
		}
	}
	
	private void processarTransferenciaEstoque(Diferenca diferenca, Long idUsuario) {
		
		if (TipoEstoque.LANCAMENTO.equals(diferenca.getTipoEstoque())) {
			
			return;
		}
		
		if (diferenca.getRateios() != null && !diferenca.getRateios().isEmpty()) {
			
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
				tipoMovimentoEstoqueLancamento, Origem.TRANSFERENCIA_LANCAMENTO_FALTA_E_SOBRA);
		
		this.movimentoEstoqueService.gerarMovimentoEstoque(
			diferenca.getProdutoEdicao().getId(), idUsuario, diferenca.getQtde(),
				tipoMovimentoEstoqueAlvo, Origem.TRANSFERENCIA_LANCAMENTO_FALTA_E_SOBRA);
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
		
		StatusAprovacao statusAprovacao  = StatusAprovacao.PENDENTE;
		
		if (!this.validarDataLancamentoDiferenca(
				diferenca.getDataMovimento(), diferenca.getProdutoEdicao().getId(), diferenca.getTipoDiferenca())) {
			
			if( TipoDiferenca.FALTA_DE.equals(diferenca.getTipoDiferenca()) 
					|| TipoDiferenca.FALTA_EM.equals(diferenca.getTipoDiferenca())){
				
				statusAprovacao = StatusAprovacao.PERDA; 
				
			}else{
				
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
			
			Cota cota = this.cotaRepository.obterPorNumerDaCota(rateioCotaVO.getNumeroCota());
			
			rateioDiferenca.setCota(cota);
			
			rateioDiferenca.setQtde(rateioCotaVO.getQuantidade());
			
			rateioDiferenca.setDataNotaEnvio(rateioCotaVO.getDataEnvioNota());
			
			rateioDiferenca.setDataMovimento(rateioCotaVO.getDataMovimento());
			
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
			
			Calendar dataConfirmacaoRecebimentoFisico = Calendar.getInstance();
			
			if (itemRecebimentoFisico.getRecebimentoFisico().getDataConfirmacao() == null) {
				
				itemRecebimentoFisico.getRecebimentoFisico().setDataConfirmacao(Calendar.getInstance().getTime());
			}
			
			dataConfirmacaoRecebimentoFisico.setTime(
				DateUtil.removerTimestamp(
					itemRecebimentoFisico.getRecebimentoFisico().getDataConfirmacao()));
			
			dataConfirmacaoRecebimentoFisico.add(Calendar.DAY_OF_MONTH, numeroDiasPermitidoLancamento);
			
			Calendar calendarLancamentoDiferenca = Calendar.getInstance();
			
			calendarLancamentoDiferenca.setTime(dataLancamentoDiferenca);
			
			if (dataConfirmacaoRecebimentoFisico.equals(calendarLancamentoDiferenca)
					|| dataConfirmacaoRecebimentoFisico.after(calendarLancamentoDiferenca)) {
				
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
												   boolean isAprovacaoAutomatica, boolean validarTransfEstoqueDiferenca) {
		
		TipoMovimentoEstoque tipoMovimentoEstoque =
			this.tipoMovimentoRepository.buscarTipoMovimentoEstoque(
				diferenca.getTipoDiferenca().getTipoMovimentoEstoque());
		
		if(tipoMovimentoEstoque == null)
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de Movimento de Estoque não encontrado.");
		
		return this.movimentoEstoqueService.gerarMovimentoEstoqueDiferenca(
			diferenca.getProdutoEdicao().getId(), idUsuario,
				diferenca.getQtde(), tipoMovimentoEstoque, isAprovacaoAutomatica, validarTransfEstoqueDiferenca);
	}
	
	/*
	 * Efetua a geração do movimento de estoque do rateio da diferença para cota.
	 */
	private MovimentoEstoqueCota gerarMovimentoEstoqueCota(Diferenca diferenca,
														   RateioDiferenca rateioDiferenca, 
														   Long idUsuario,
														   boolean isAprovacaoAutomatica) {

		TipoMovimentoEstoque tipoMovimentoEstoqueCota =
			this.tipoMovimentoRepository.buscarTipoMovimentoEstoque(
				diferenca.getTipoDiferenca().getGrupoMovimentoEstoqueCota());
		
		Long estudoCotaId = (rateioDiferenca.getEstudoCota() != null) 
								? rateioDiferenca.getEstudoCota().getId() : null;
		
		return this.movimentoEstoqueService.gerarMovimentoCotaDiferenca(
					null, diferenca.getProdutoEdicao().getId(), rateioDiferenca.getCota().getId(),
						idUsuario, rateioDiferenca.getQtde(), tipoMovimentoEstoqueCota,estudoCotaId,isAprovacaoAutomatica);
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
			
			Long idDiferenca = dadoImpressao.getIdDiferenca();
			
			if (idDiferenca != null) {
				
				List<RateioDiferencaDTO> rateios = 
					this.rateioDiferencaRepository.obterRateiosParaImpressaoPorDiferenca(
						idDiferenca);
				
				if (rateios != null) {
					
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
							
							dadoImpressaoComRateio.setIdDiferenca(idDiferenca);
					 		dadoImpressaoComRateio.setProdutoEdicao(dadoImpressao.getProdutoEdicao());
					 		dadoImpressaoComRateio.setQtdeFaltas(dadoImpressao.getQtdeFaltas());
					 		dadoImpressaoComRateio.setQtdeSobras(dadoImpressao.getQtdeSobras());
					 		
					 		dadoImpressaoComRateio.setRateios(
					 			rateios.subList(indice, indice += qtdeRateiosPorLinha));
							
							listaRelatorio.add(
								new RelatorioLancamentoFaltasSobrasVO(dadoImpressaoComRateio));
						}
					}
				}				
			}
            //Dados impressão sem rateio
			else{
				
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
	public HashMap<Long, Set<Diferenca>> verificarDiferencasIguais(
			Set<Diferenca> listaDiferencas,
			Diferenca diferenca) {
		
		Long idDiferenca = null;
	
		if(listaDiferencas != null && ! listaDiferencas.isEmpty()) {
			
			Iterator<Diferenca> iterator = listaDiferencas.iterator();
			
			while( iterator.hasNext() ) {
				
				Diferenca diferencaCadastrada = iterator.next();
					
				if(diferencaCadastrada.getProdutoEdicao().equals(diferenca.getProdutoEdicao()) 
						&& diferencaCadastrada.getTipoDiferenca().equals(diferenca.getTipoDiferenca())
						&& diferencaCadastrada.getTipoDirecionamento().equals(diferenca.getTipoDirecionamento())) {
					
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
	public Set<DiferencaVO> verificarDiferencasVOIguais(
			Set<DiferencaVO> listaNovaDiferencaVO, DiferencaVO diferencaVO) {
		
		if(listaNovaDiferencaVO != null && ! listaNovaDiferencaVO.isEmpty()) {
			
			Iterator<DiferencaVO> iterator = listaNovaDiferencaVO.iterator();
			while( iterator.hasNext() ) {
				DiferencaVO diferencaVoCadastrada = iterator.next();
				
				if( diferencaVoCadastrada.getCodigoProduto().equals(diferencaVO.getCodigoProduto())  
						&& diferencaVoCadastrada.getNumeroEdicao().equals(diferencaVO.getNumeroEdicao()) 
						&& diferencaVoCadastrada.getTipoDiferenca().equals(diferencaVO.getTipoDiferenca())
						&& diferencaVoCadastrada.getTipoDirecionamento().equals(diferencaVO.getTipoDirecionamento())) {
				
					BigInteger quantidade = diferencaVoCadastrada.getQuantidade();
					
					diferencaVoCadastrada.setQuantidade(quantidade.add(diferencaVO.getQuantidade()));
					
					diferencaVO = null;
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
	
}