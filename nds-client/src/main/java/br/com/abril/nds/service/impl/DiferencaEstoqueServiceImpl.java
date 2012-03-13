package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RateioDiferenca;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.movimentacao.GrupoMovimentoEstoque;
import br.com.abril.nds.model.movimentacao.MovimentoEstoque;
import br.com.abril.nds.model.movimentacao.MovimentoEstoqueCota;
import br.com.abril.nds.model.movimentacao.TipoMovimentoEstoque;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.repository.RateioDiferencaRepository;
import br.com.abril.nds.repository.RecebimentoFisicoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.FeriadoService;
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
	private MovimentoEstoqueCotaRepository movimentoCotaRepository;
	
	@Autowired
	private RateioDiferencaRepository rateioDiferencaRepository;
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoRepository;
	
	@Autowired
	private EstoqueProdutoCotaRepository estoqueProdutoCotaRepository;
	
	@Autowired
	private EstoqueProdutoRespository estoqueProdutoRespository;
	
	@Autowired
	private MovimentoEstoqueRepository movimentoEstoqueRepository;
	
	@Autowired
	private EstudoCotaRepository estudoCotaRepository;
	
	@Autowired
	private EstudoRepository estudoRepository;
	
	@Autowired
	private ParametroSistemaRepository parametroSistemaRepository;
	
	@Autowired
	private RecebimentoFisicoRepository recebimentoFisicoRepository;
	
	@Autowired
	private FeriadoService feriadoService;
	
	private static final String MOTIVO = "Exclusão diferença";
	
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
		
		Date dataInicialLancamento = feriadoService.subtrairDiasUteis(new Date(), 7);
		
		filtro.setDataLimiteLancamentoDistribuidor(dataInicialLancamento);
		
		return this.diferencaEstoqueRepository.obterDiferencas(filtro);
	}
	
	@Transactional(readOnly = true)
	public Long obterTotalDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro) {
		
		Date dataInicialLancamento = feriadoService.subtrairDiasUteis(new Date(), 7);
		
		filtro.setDataLimiteLancamentoDistribuidor(dataInicialLancamento);
		
		return this.diferencaEstoqueRepository.obterTotalDiferencas(filtro);
	}
	
	@Transactional(readOnly = true)
	public boolean verificarPossibilidadeExclusao(Long idDiferenca){
		if (idDiferenca == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Id da Diferença é obrigatório.");
		}
		
		Boolean diferenca = this.diferencaEstoqueRepository.buscarStatusDiferencaLancadaAutomaticamente(idDiferenca);
		
		return diferenca == null ? false : !diferenca;
	}
	
	
	//TODO: no momento esse método trata apenas as exclusões de diferenca, deve fazer todo o necessário para esta funcionalidade, morô
	@Transactional
	public void efetuarAlteracoes(Long idUsuario, Set<Long> idsDiferencaExclusao){
		this.excluirDiferenca(idUsuario, idsDiferencaExclusao);
	}
	
	private void excluirDiferenca(Long idUsuario, Set<Long> idsDiferenca){
		
		for (Long idDiferenca : idsDiferenca){
			
			Diferenca diferenca = this.diferencaEstoqueRepository.buscarPorId(idDiferenca);
			
			BigDecimal qtd = diferenca.getQtde();
			
			if (!diferenca.isAutomatica()){
				
				TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
				tipoMovimento.setAprovacaoAutomatica(false);
				tipoMovimento.setDescricao(MOTIVO);
				
				switch (diferenca.getTipoDiferenca().getTipoMovimentoEstoque()){
					case SOBRA_DE:
						tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.FALTA_DE);
						qtd = qtd.negate();
						break;
					case SOBRA_EM:
						tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.FALTA_EM);
						qtd = qtd.negate();
						break;
					case FALTA_DE:
						tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.SOBRA_DE);
						break;
					case FALTA_EM:
						tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.SOBRA_EM);
						break;
				}
				
				Usuario usuario = new Usuario();
				usuario.setId(idUsuario);
				
				RateioDiferenca rateioDiferenca = 
						this.rateioDiferencaRepository.obterRateioDiferencaPorDiferenca(idDiferenca);
				
				if (rateioDiferenca != null){
					
					if (rateioDiferenca.getCota() == null){
						throw new ValidacaoException(TipoMensagem.ERROR, "Cota não encontrada.");
					}
					
					if (rateioDiferenca.getEstudoCota() == null){
						throw new ValidacaoException(TipoMensagem.ERROR, "Estudo Cota não encontrado.");
					}
					
					this.tipoMovimentoRepository.adicionar(tipoMovimento);
					
					EstoqueProdutoCota estoqueProdutoCota = 
							this.estoqueProdutoCotaRepository.buscarEstoquePorProdutEdicaoECota(
									diferenca.getProdutoEdicao().getId(), rateioDiferenca.getCota().getId());
					
					if (estoqueProdutoCota == null){
						throw new ValidacaoException(
								TipoMensagem.ERROR, 
								"Estoque Produto Cota não encontrado, parâmetros de busca: idProdutoEdicao: " + diferenca.getProdutoEdicao().getId() + 
								", idCota: " + rateioDiferenca.getCota().getId());
					}
					
					MovimentoEstoqueCota movimentoEstoqueCota = new MovimentoEstoqueCota();
					movimentoEstoqueCota.setCota(rateioDiferenca.getCota());
					movimentoEstoqueCota.setQtde(diferenca.getQtde());
					movimentoEstoqueCota.setDataInclusao(new Date());
					movimentoEstoqueCota.setAprovadoAutomaticamente(false);
					movimentoEstoqueCota.setMotivo(MOTIVO);
					movimentoEstoqueCota.setTipoMovimento(tipoMovimento);
					movimentoEstoqueCota.setUsuario(usuario);
					movimentoEstoqueCota.setCota(rateioDiferenca.getCota());
					movimentoEstoqueCota.setEstoqueProdutoCota(estoqueProdutoCota);
					movimentoEstoqueCota.setProdutoEdicao(diferenca.getProdutoEdicao());
					
					this.movimentoCotaRepository.adicionar(movimentoEstoqueCota);
					
					estoqueProdutoCota.setQtdeDevolvida(estoqueProdutoCota.getQtdeDevolvida().add(qtd));
					estoqueProdutoCota.setQtdeRecebida(estoqueProdutoCota.getQtdeRecebida().subtract(qtd));
					
					this.estoqueProdutoCotaRepository.alterar(estoqueProdutoCota);
					
					EstudoCota estudoCota = rateioDiferenca.getEstudoCota();
					estudoCota.setQtdeEfetiva(rateioDiferenca.getEstudoCota().getQtdeEfetiva().add(qtd));
					this.estudoCotaRepository.alterar(estudoCota);
					
					Estudo estudo = estudoCota.getEstudo();
					
					if (estudo == null){
						throw new ValidacaoException(TipoMensagem.ERROR, "Estudo não encontrado.");
					}
					
					estudo.setQtdeReparte(estudoCota.getQtdeEfetiva());
					
					this.estudoRepository.alterar(estudo);
				}
				
				EstoqueProduto estoqueProduto = 
						estoqueProdutoRespository.buscarEstoqueProdutoPorProdutoEdicao(
								diferenca.getProdutoEdicao().getId());
				
				if (estoqueProduto == null){
					throw new ValidacaoException(
							TipoMensagem.ERROR, 
							"Estoque Produto não encontrado, parâmetros de busca: idProdutoEdicao: " + diferenca.getProdutoEdicao().getId());
				}
				
				estoqueProduto.setQtde(estoqueProduto.getQtde().add(qtd));
				
				this.estoqueProdutoRespository.alterar(estoqueProduto);
				
				MovimentoEstoque movimentoEstoque = new MovimentoEstoque();
				movimentoEstoque.setAprovadoAutomaticamente(false);
				movimentoEstoque.setMotivo(MOTIVO);
				movimentoEstoque.setDataInclusao(new Date());
				movimentoEstoque.setTipoMovimento(tipoMovimento);
				movimentoEstoque.setUsuario(usuario);
				movimentoEstoque.setQtde(diferenca.getQtde());
				movimentoEstoque.setEstoqueProduto(estoqueProduto);
				movimentoEstoque.setProdutoEdicao(diferenca.getProdutoEdicao());
				
				this.movimentoEstoqueRepository.adicionar(movimentoEstoque);
				
				this.rateioDiferencaRepository.removerRateioDiferencaPorDiferenca(idDiferenca);
				
				this.diferencaEstoqueRepository.remover(diferenca);
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR, "Diferença com automática não pode ser excluída.");
			}
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
	
	@Transactional
	public void salvarNovaDiferenca(Diferenca diferenca) {
		
		if (diferenca == null) {
			
			throw new IllegalArgumentException("Diferença não pode ser nula");
		}
		
		this.diferencaEstoqueRepository.adicionar(diferenca);
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