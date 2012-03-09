package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.RateioDiferenca;
import br.com.abril.nds.model.movimentacao.DominioTipoMovimento;
import br.com.abril.nds.model.movimentacao.MovimentoEstoque;
import br.com.abril.nds.model.movimentacao.MovimentoEstoqueCota;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.repository.MovimentoCotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.repository.RateioDiferencaRepository;
import br.com.abril.nds.repository.TipoMovimentoRepository;
import br.com.abril.nds.service.DiferencaEstoqueService;
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
	private MovimentoCotaRepository movimentoCotaRepository;
	
	@Autowired
	private RateioDiferencaRepository rateioDiferencaRepository;
	
	@Autowired
	private TipoMovimentoRepository tipoMovimentoRepository;
	
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
		
		return this.diferencaEstoqueRepository.obterDiferencas(filtro);
	}
	
	@Transactional(readOnly = true)
	public Long obterTotalDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro) {
		
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
				
				TipoMovimento tipoMovimento = new TipoMovimento();
				tipoMovimento.setAprovacaoAutomatica(false);
				tipoMovimento.setDescricao(MOTIVO);
				
				switch (diferenca.getTipoDiferenca().getTipoMovimentoEstoque()){
					case SOBRA_DE:
						tipoMovimento.setTipoMovimento(DominioTipoMovimento.FALTA_DE);
						qtd = qtd.negate();
						break;
					case SOBRA_EM:
						tipoMovimento.setTipoMovimento(DominioTipoMovimento.FALTA_EM);
						qtd = qtd.negate();
						break;
					case FALTA_DE:
						tipoMovimento.setTipoMovimento(DominioTipoMovimento.SOBRA_DE);
						break;
					case FALTA_EM:
						tipoMovimento.setTipoMovimento(DominioTipoMovimento.SOBRA_EM);
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
}