package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.RateioCotaVO;
import br.com.abril.nds.client.vo.relatorioLancamentoFaltasSobrasVO;
import br.com.abril.nds.dto.DetalheDiferencaCotaDTO;
import br.com.abril.nds.dto.RateioDiferencaCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheDiferencaCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.LancamentoDiferenca;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.RateioDiferenca;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoDirecionamentoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.LancamentoDiferencaRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.repository.RateioDiferencaRepository;
import br.com.abril.nds.repository.RecebimentoFisicoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.MovimentoEstoqueCotaService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.VisaoEstoqueService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;

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
		
		Date dataInicialLancamento = calendarioService.subtrairDiasUteis(new Date(), 7);
		
		if(filtro.getNumeroCota() != null) {
			Cota cota = this.cotaRepository.obterPorNumerDaCota(filtro.getNumeroCota());
			filtro.setIdCota(cota.getId());
		}
		
		return this.diferencaEstoqueRepository.obterDiferencas(filtro, dataInicialLancamento);
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
	public Diferenca lancarDiferencaAutomatica(Diferenca diferenca) {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		diferenca.setStatusConfirmacao(StatusConfirmacao.CONFIRMADO);
		diferenca.setTipoDirecionamento(TipoDirecionamentoDiferenca.ESTOQUE);
		diferenca.setTipoEstoque(TipoEstoque.LANCAMENTO);
		diferenca.setAutomatica(true);
		diferenca.setDataMovimento(distribuidor.getDataOperacao());
		
		this.diferencaEstoqueRepository.adicionar(diferenca);
		
		return diferenca;
	}
	
	@Override
	@Transactional
	public void gerarMovimentoEstoqueDiferenca(Diferenca diferenca, Long idUsuario) {
		gerarMovimentoEstoque(diferenca, idUsuario);
	}
	
	@Transactional
	public void efetuarAlteracoes(Set<Diferenca> listaNovasDiferencas,
			 					  Map<Long, List<RateioCotaVO>> mapaRateioCotas,
								  FiltroLancamentoDiferencaEstoqueDTO filtroPesquisa,
								  Long idUsuario,Boolean isDiferencaNova) {
		
		salvarDiferenca(listaNovasDiferencas, mapaRateioCotas, idUsuario, isDiferencaNova, StatusConfirmacao.CONFIRMADO);
		
		List<Diferenca> listaDiferencas = null;
		
		if (isDiferencaNova == null || !isDiferencaNova) {
			
			filtroPesquisa.setPaginacao(null);
			filtroPesquisa.setOrdenacaoColuna(null);
			
			listaDiferencas = this.diferencaEstoqueRepository.obterDiferencasLancamento(filtroPesquisa);
		}
		else{
			
			listaDiferencas = new ArrayList<>(listaNovasDiferencas);
		}
		
		this.confirmarLancamentosDiferenca(listaDiferencas, idUsuario);
	} 
	
	@Transactional
	public void salvarLancamentosDiferenca(Set<Diferenca> listaNovasDiferencas,
										   Map<Long, List<RateioCotaVO>> mapaRateioCotas,
										   Long idUsuario,Boolean isDiferencaNova){
		
		
		salvarDiferenca(listaNovasDiferencas, mapaRateioCotas, idUsuario,isDiferencaNova, StatusConfirmacao.PENDENTE);
	}

	private void salvarDiferenca(Set<Diferenca> listaNovasDiferencas,
								Map<Long, List<RateioCotaVO>> mapaRateioCotas, 
								Long idUsuario, 
								Boolean isDiferencaNova,
								StatusConfirmacao statusConfirmacao) {
		
		Usuario usuario = usuarioService.buscar(idUsuario);
		
		for (Diferenca diferenca : listaNovasDiferencas) {

			Long idDiferencaTemporario = diferenca.getId();
			
			if (isDiferencaNova != null && isDiferencaNova) {
				
				diferenca.setId(null);
			}
			
			diferenca = this.diferencaEstoqueRepository.merge(diferenca);
			
			List<RateioCotaVO> listaRateioCotas = null;
			
			if (mapaRateioCotas != null && !mapaRateioCotas.isEmpty()) {
				
				listaRateioCotas = mapaRateioCotas.remove(idDiferencaTemporario);
				
				mapaRateioCotas.put(diferenca.getId(), listaRateioCotas);
			}
			
			if (!TipoDirecionamentoDiferenca.ESTOQUE.equals(diferenca.getTipoDirecionamento())) {
				
				this.processarRateioCotas(diferenca, mapaRateioCotas, idUsuario);
				
			} else {
				
				rateioDiferencaRepository.removerRateioDiferencaPorDiferenca(diferenca.getId());
			}
			
			diferenca.setStatusConfirmacao(statusConfirmacao);
			
			diferenca.setResponsavel(usuario);
			
			this.diferencaEstoqueRepository.merge(diferenca);
			
			if (TipoDirecionamentoDiferenca.COTA.equals(diferenca.getTipoDirecionamento())) {
				
				this.redirecionarDiferencaEstoque(listaRateioCotas, diferenca);
			}
			
		}
	}
	
	private void redirecionarDiferencaEstoque(List<RateioCotaVO> rateiosDiferenca,Diferenca diferenca){
		
		if(rateiosDiferenca == null || rateiosDiferenca.isEmpty()){
			return;
		}
		
		BigInteger qntTotalRateio = BigInteger.ZERO;
		
		for(RateioCotaVO rateio : rateiosDiferenca){
			qntTotalRateio = qntTotalRateio.add(rateio.getQuantidade());
		}
		
		if(qntTotalRateio.compareTo(diferenca.getQtde()) < 0){
			
			Distribuidor distribuidor = distribuidorService.obter();
			
			Diferenca diferencaEstoque = new Diferenca();
			
			diferencaEstoque.setAutomatica(diferenca.isAutomatica());
			diferencaEstoque.setDataMovimento(distribuidor.getDataOperacao());
			diferencaEstoque.setProdutoEdicao(diferenca.getProdutoEdicao());
			diferencaEstoque.setResponsavel(diferenca.getResponsavel());
			diferencaEstoque.setTipoDiferenca(diferenca.getTipoDiferenca());
			diferencaEstoque.setTipoEstoque(diferenca.getTipoEstoque());
			diferencaEstoque.setItemRecebimentoFisico(diferenca.getItemRecebimentoFisico());
			diferencaEstoque.setStatusConfirmacao(diferenca.getStatusConfirmacao());
			
			diferencaEstoque.setTipoDirecionamento(TipoDirecionamentoDiferenca.ESTOQUE);
			diferencaEstoque.setQtde( diferenca.getQtde().subtract(qntTotalRateio) );
			
			diferencaEstoqueRepository.merge(diferencaEstoque);
		}
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

	private void confirmarLancamentosDiferenca(List<Diferenca> listaDiferencas,Long idUsuario) {
		
		for (Diferenca diferenca : listaDiferencas) {
			
			MovimentoEstoque movimentoEstoque = this.gerarMovimentoEstoque(diferenca, idUsuario);
				
			List<MovimentoEstoqueCota> listaMovimentosEstoqueCota = null;
				
			if (diferenca.getRateios() != null && !diferenca.getRateios().isEmpty()) {
					
				listaMovimentosEstoqueCota = new ArrayList<MovimentoEstoqueCota>();
					
				for (RateioDiferenca rateioDiferenca : diferenca.getRateios()) {
						
					listaMovimentosEstoqueCota.add(
							this.gerarMovimentoEstoqueCota(
								diferenca, rateioDiferenca.getQtde(), rateioDiferenca.getCota(), idUsuario));
				}
			}

			diferenca.setStatusConfirmacao(StatusConfirmacao.CONFIRMADO);
				
			this.diferencaEstoqueRepository.merge(diferenca);
				
			StatusAprovacao statusAprovacao = obterStatusLancamento(diferenca);
				
			this.gerarLancamentoDiferenca(
					diferenca, statusAprovacao, movimentoEstoque, listaMovimentosEstoqueCota);
				
			this.processarTransferenciaEstoque(diferenca,idUsuario);
		}
	}

	private void processarTransferenciaEstoque(Diferenca diferenca, Long idUsuario) {
		
		if( TipoEstoque.LANCAMENTO.equals(diferenca.getTipoEstoque())){
			return;
		}
		
		TipoMovimentoEstoque tipoMovimentoEstoqueLancamento = null;
		TipoMovimentoEstoque tipoMovimentoEstoqueAlvo = null;
				
		if(TipoDiferenca.FALTA_DE.equals(diferenca.getTipoDiferenca()) 
				|| TipoDiferenca.FALTA_EM.equals(diferenca.getTipoDiferenca())){
			
			 tipoMovimentoEstoqueLancamento = 
					this.tipoMovimentoRepository.buscarTipoMovimentoEstoque(
							obterTipoMovimentoEstoqueTransferencia(TipoEstoque.LANCAMENTO, TipoOperacao.ENTRADA));
			 
			 tratarTipoMovimentoEstoque(tipoMovimentoEstoqueLancamento,"Tipo de movimento de entrada não encontrado!");
			
			 tipoMovimentoEstoqueAlvo = 
					this.tipoMovimentoRepository.buscarTipoMovimentoEstoque(
							obterTipoMovimentoEstoqueTransferencia(diferenca.getTipoEstoque(), TipoOperacao.SAIDA));
			
			 tratarTipoMovimentoEstoque(tipoMovimentoEstoqueLancamento,"Tipo de movimento de saida não encontrado!");
		}
		
		if(TipoDiferenca.SOBRA_DE.equals(diferenca.getTipoDiferenca()) 
				|| TipoDiferenca.SOBRA_EM.equals(diferenca.getTipoDiferenca())){
			
			 tipoMovimentoEstoqueLancamento = 
					this.tipoMovimentoRepository.buscarTipoMovimentoEstoque(
							obterTipoMovimentoEstoqueTransferencia(TipoEstoque.LANCAMENTO, TipoOperacao.SAIDA));
			
			 tratarTipoMovimentoEstoque(tipoMovimentoEstoqueLancamento,"Tipo de movimento de entrada não encontrado!");
			
			 tipoMovimentoEstoqueAlvo = 
					this.tipoMovimentoRepository.buscarTipoMovimentoEstoque(
							obterTipoMovimentoEstoqueTransferencia(diferenca.getTipoEstoque(), TipoOperacao.ENTRADA));
			
			 tratarTipoMovimentoEstoque(tipoMovimentoEstoqueLancamento,"Tipo de movimento de saida não encontrado!");
		}
		
		movimentoEstoqueService.gerarMovimentoEstoque(
				diferenca.getProdutoEdicao().getId(),idUsuario,diferenca.getQtde(),
				tipoMovimentoEstoqueLancamento,Origem.TRANSFERENCIA_LANCAMENTO_FALTA_E_SOBRA);
		
		movimentoEstoqueService.gerarMovimentoEstoque(
				diferenca.getProdutoEdicao().getId(),idUsuario,diferenca.getQtde(),
				tipoMovimentoEstoqueAlvo,Origem.TRANSFERENCIA_LANCAMENTO_FALTA_E_SOBRA);
	}
	
	private void tratarTipoMovimentoEstoque(TipoMovimentoEstoque tipoMovimento,String mensagem){
		
		if (tipoMovimento == null) {
			throw new ValidacaoException(TipoMensagem.WARNING,mensagem);
		}
	}
	
	private GrupoMovimentoEstoque obterTipoMovimentoEstoqueTransferencia(TipoEstoque tipoEstoque, TipoOperacao tipoOperacao){
		
		switch(tipoEstoque) {
		
		case LANCAMENTO:
			return isOperacaoEntrada(tipoOperacao) 
					? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO  
							   :GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_LANCAMENTO;
		case SUPLEMENTAR:
			return isOperacaoEntrada(tipoOperacao) 
					? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR 
							   :GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_SUPLEMENTAR;
		case RECOLHIMENTO:
			return isOperacaoEntrada(tipoOperacao) 
					? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_RECOLHIMENTO 
							   :GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_RECOLHIMENTO;
		case PRODUTOS_DANIFICADOS:
			return isOperacaoEntrada(tipoOperacao) 
					? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DANIFICADOS
							   :GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DANIFICADOS;
		
		case DEVOLUCAO_FORNECEDOR:
			return isOperacaoEntrada(tipoOperacao) 
					? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DEVOLUCAO_FORNECEDOR
							   :GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DEVOLUCAO_FORNECEDOR;
		default:
			throw new ValidacaoException(TipoMensagem.ERROR,"Erro para obter tipo de movimento estoque para lançamento de faltas e sobras!");
		}
		
	}
	
	private boolean isOperacaoEntrada(TipoOperacao tipoOperacao){
		return (TipoOperacao.ENTRADA.equals(tipoOperacao));
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
	
	private void processarRateioCotas(Diferenca diferenca,
									  Map<Long, List<RateioCotaVO>> mapaRateioCotas,
									  Long idUsuario) {
		
		if (mapaRateioCotas == null || mapaRateioCotas.isEmpty()) {
			
			return;
		}
			
		List<RateioCotaVO> listaRateioCotaVO = mapaRateioCotas.get(diferenca.getId());
		
		if (listaRateioCotaVO == null || listaRateioCotaVO.isEmpty()) {
			
			return;
		}
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		List<Long> rateiosAssociadosDiferenca = new ArrayList<Long>();
		
		for (RateioCotaVO rateioCotaVO : listaRateioCotaVO) {
			
			RateioDiferenca rateioDiferenca = null;
			
			if(rateioCotaVO.getIdRateio()!= null){
				
				rateioDiferenca = rateioDiferencaRepository.buscarPorId(rateioCotaVO.getIdRateio());
			}
			
			if(rateioDiferenca == null){
				
				rateioDiferenca = new RateioDiferenca();
				
				Cota cota = this.cotaRepository.obterPorNumerDaCota(rateioCotaVO.getNumeroCota());
				
				rateioDiferenca.setCota(cota);
				
				EstudoCota estudoCota = 
						this.estudoCotaRepository.obterEstudoCotaDeLancamentoComEstudoFechado(distribuidor.getDataOperacao(),
																							  diferenca.getProdutoEdicao().getId(), 
																							  rateioCotaVO.getNumeroCota());
				rateioDiferenca.setEstudoCota(estudoCota);
				
				rateioDiferenca.setDiferenca(diferenca);
			}
			
			rateioDiferenca.setQtde(rateioCotaVO.getQuantidade());
			
			rateioDiferenca.setDataNotaEnvio(rateioCotaVO.getDataEnvioNota());
			
			rateioDiferenca = this.rateioDiferencaRepository.merge(rateioDiferenca);
			
			rateiosAssociadosDiferenca.add(rateioDiferenca.getId());
		}
		
		if(!rateiosAssociadosDiferenca.isEmpty()){
			
			rateioDiferencaRepository.removerRateiosNaoAssociadosDiferenca(diferenca.getId(), rateiosAssociadosDiferenca);
		}
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
				
				continue;
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
	private MovimentoEstoque gerarMovimentoEstoque(Diferenca diferenca, Long idUsuario) {
		
		TipoMovimentoEstoque tipoMovimentoEstoque =
			this.tipoMovimentoRepository.buscarTipoMovimentoEstoque(
				diferenca.getTipoDiferenca().getTipoMovimentoEstoque());
		
		return this.movimentoEstoqueService.gerarMovimentoEstoque(
			diferenca.getProdutoEdicao().getId(), idUsuario,
				diferenca.getQtde(), tipoMovimentoEstoque);
	}
	
	/*
	 * Efetua a geração do movimento de estoque do rateio da diferença para cota.
	 */
	private MovimentoEstoqueCota gerarMovimentoEstoqueCota(Diferenca diferenca,
														   BigInteger quantidade,
														   Cota cota, 
														   Long idUsuario) {
		
		TipoMovimentoEstoque tipoMovimentoEstoque =
			this.tipoMovimentoRepository.buscarTipoMovimentoEstoque(
				diferenca.getTipoDiferenca().getTipoMovimentoEstoque());
		
		return this.movimentoEstoqueService.gerarMovimentoCota(
			null, diferenca.getProdutoEdicao().getId(),
				cota.getId(), idUsuario, quantidade, tipoMovimentoEstoque);
	}
	
	/*
	 * Efetua a geração de lançamento de diferença.
	 */
	private LancamentoDiferenca gerarLancamentoDiferenca(Diferenca diferenca,
														 StatusAprovacao statusAprovacao,
														 MovimentoEstoque movimentoEstoque,
														 List<MovimentoEstoqueCota> listaMovimentosEstoqueCota) {
		
		LancamentoDiferenca lancamentoDiferenca = new LancamentoDiferenca();
		
		lancamentoDiferenca.setDataProcessamento(new Date());
		lancamentoDiferenca.setDiferenca(diferenca);
		lancamentoDiferenca.setMovimentoEstoque(movimentoEstoque);
		lancamentoDiferenca.setMovimentoEstoqueCota(listaMovimentosEstoqueCota);
		lancamentoDiferenca.setStatus(statusAprovacao);
		
		return this.lancamentoDiferencaRepository.merge(lancamentoDiferenca);
	}

	@Override
	@Transactional(readOnly = true)
	public byte[] imprimirRelatorioFaltasSobras(Date dataMovimento) throws Exception {
		FiltroLancamentoDiferencaEstoqueDTO filtro = new FiltroLancamentoDiferencaEstoqueDTO();
		filtro.setDataMovimento(dataMovimento);
	
 	List<Diferenca> listaLancamentoDiferencas =this.obterDiferencasLancamento(filtro);
 	List<relatorioLancamentoFaltasSobrasVO> listaRelatorio =  new ArrayList<relatorioLancamentoFaltasSobrasVO>();
	Map<String, Object> parameters = new HashMap<String, Object>();

	for (Diferenca diferenca : listaLancamentoDiferencas){
		listaRelatorio.add(new relatorioLancamentoFaltasSobrasVO(diferenca));
	}
	
	parameters.put("DISTRIBUIDOR",distribuidorService.obter().getJuridica().getNome());
	JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaRelatorio); 
	URL url = Thread.currentThread().getContextClassLoader().getResource("/reports/faltas_sobras.jasper");
	String path = url.toURI().getPath();
	return JasperRunManager.runReportToPdf(path, parameters,ds);
	}
	
}