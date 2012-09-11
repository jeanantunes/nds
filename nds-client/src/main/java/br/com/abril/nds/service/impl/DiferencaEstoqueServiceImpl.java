package br.com.abril.nds.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.RateioCotaVO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.RateioDiferenca;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.repository.RateioDiferencaRepository;
import br.com.abril.nds.repository.RecebimentoFisicoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.MovimentoEstoqueService;
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
	private CotaRepository cotaRepository;
	
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
	public Diferenca lancarDiferenca(Diferenca diferenca) {
		
		this.processarMovimentoEstoque(diferenca, diferenca.getResponsavel().getId());
		
		this.diferencaEstoqueRepository.adicionar(diferenca);
		
		return diferenca;
	}
	
	@Transactional
	public void efetuarAlteracoes(Set<Diferenca> listaNovasDiferencas,
			 					  Map<Long, List<RateioCotaVO>> mapaRateioCotas,
								  FiltroLancamentoDiferencaEstoqueDTO filtroPesquisa,
								  Long idUsuario) {

		if (listaNovasDiferencas != null 
				&& !listaNovasDiferencas.isEmpty()) {
			
			this.confirmarNovosLancamentosDiferenca(
				listaNovasDiferencas, mapaRateioCotas, idUsuario);
			
		} else {
			
			this.confirmarLancamentosDiferenca(
				mapaRateioCotas, filtroPesquisa, idUsuario);
		}
	}
	
	private void confirmarNovosLancamentosDiferenca(Set<Diferenca> listaNovasDiferencas,
			 										Map<Long, List<RateioCotaVO>> mapaRateioCotas,
													Long idUsuario) {
		
		for (Diferenca diferenca : listaNovasDiferencas) {

			Long idDiferencaTemporario = diferenca.getId();
			
			this.diferencaEstoqueRepository.adicionar(diferenca);
			
			if (mapaRateioCotas != null && !mapaRateioCotas.isEmpty()) {
				
				List<RateioCotaVO> listaRateioCotas = mapaRateioCotas.remove(idDiferencaTemporario);
				
				mapaRateioCotas.put(diferenca.getId(), listaRateioCotas);
			}
			
			diferenca.setMovimentoEstoque(null);
			
			this.processarMovimentoEstoque(diferenca, idUsuario);
			
			this.processarRateioCotas(diferenca, mapaRateioCotas, idUsuario);
			
			diferenca.setStatusConfirmacao(StatusConfirmacao.CONFIRMADO);
			
			this.diferencaEstoqueRepository.alterar(diferenca);
		}
	}
	
	private void confirmarLancamentosDiferenca(Map<Long, List<RateioCotaVO>> mapaRateioCotas,
											   FiltroLancamentoDiferencaEstoqueDTO filtroPesquisa,
											   Long idUsuario) {
		
		filtroPesquisa.setPaginacao(null);
		
		filtroPesquisa.setOrdenacaoColuna(null);
		
		List<Diferenca> listaDiferencas =
			this.diferencaEstoqueRepository.obterDiferencasLancamento(filtroPesquisa);
		
		for (Diferenca diferenca : listaDiferencas) {
			
			this.processarRateioCotas(diferenca, mapaRateioCotas, idUsuario);
			
			diferenca.setStatusConfirmacao(StatusConfirmacao.CONFIRMADO);
			
			this.diferencaEstoqueRepository.merge(diferenca);
		}
	}
	
	private void processarMovimentoEstoque(Diferenca diferenca, 
								    	   Long idUsuario) {
		
		TipoMovimentoEstoque tipoMovimentoEstoque =
			this.tipoMovimentoRepository.buscarTipoMovimentoEstoque(
				diferenca.getTipoDiferenca().getTipoMovimentoEstoque());
		
		MovimentoEstoque movimentoEstoque =
			this.movimentoEstoqueService.gerarMovimentoEstoque(
				new Date(), diferenca.getProdutoEdicao().getId(), idUsuario,
					diferenca.getQtde(), tipoMovimentoEstoque);
		
		diferenca.setMovimentoEstoque(movimentoEstoque);
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
		
		for (RateioCotaVO rateioCotaVO : listaRateioCotaVO) {
			
			RateioDiferenca rateioDiferenca = new RateioDiferenca();

			rateioDiferenca.setDiferenca(diferenca);
			rateioDiferenca.setQtde(rateioCotaVO.getQuantidade());
			
			Cota cota = this.cotaRepository.obterPorNumerDaCota(rateioCotaVO.getNumeroCota());
			
			rateioDiferenca.setCota(cota);
			
			EstudoCota estudoCota =
				this.estudoCotaRepository.obterEstudoCotaDeLancamentoComEstudoFechado(
					diferenca.getMovimentoEstoque().getData(), 
						diferenca.getProdutoEdicao().getId(), rateioCotaVO.getNumeroCota());
			
			rateioDiferenca.setEstudoCota(estudoCota);
			
			this.rateioDiferencaRepository.adicionar(rateioDiferenca);
			
			this.movimentoEstoqueService.gerarMovimentoCota(
				diferenca.getMovimentoEstoque().getData(),
					diferenca.getProdutoEdicao().getId(),
						cota.getId(), idUsuario, rateioCotaVO.getQuantidade(), 
							(TipoMovimentoEstoque) diferenca.getMovimentoEstoque().getTipoMovimento());
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
	
}