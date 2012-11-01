package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.MovimentoAprovacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroControleAprovacaoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.movimentacao.Movimento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.MovimentoRepository;
import br.com.abril.nds.service.ControleAprovacaoService;
import br.com.abril.nds.service.MovimentoEstoqueService;

/**
 * Classe de implementação de serviços referentes
 * ao controle de aprovações.
 * 
 * @author Discover Technology
 */
@Service
public class ControleAprovacaoServiceImpl implements ControleAprovacaoService {

	@Autowired
	protected MovimentoRepository movimentoRepository;

	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;
	
	@Override
	@Transactional(readOnly = true)
	public List<MovimentoAprovacaoDTO> obterMovimentosAprovacao(FiltroControleAprovacaoDTO filtro) {
		
		return this.movimentoRepository.obterMovimentosAprovacao(filtro);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Long obterTotalMovimentosAprovacao(FiltroControleAprovacaoDTO filtro) {
		
		return this.movimentoRepository.obterTotalMovimentosAprovacao(filtro);
	}
	
	@Override
	@Transactional
	public void aprovarMovimento(Long idMovimento, Usuario usuario) {
		
		Movimento movimento = this.movimentoRepository.buscarPorId(idMovimento);
		
		if (movimento != null) {
			
			this.realizarAprovacaoMovimento(movimento, usuario);
		}
	}
	
	@Override
	@Transactional
	public void rejeitarMovimento(Long idMovimento, String motivo, Usuario usuario) {
		
		Movimento movimento = this.movimentoRepository.buscarPorId(idMovimento);
		
		if (movimento != null) {
			
			movimento.setStatus(StatusAprovacao.REJEITADO);
			movimento.setAprovador(usuario);
			movimento.setMotivo(motivo);
			
			this.movimentoRepository.alterar(movimento);
		}
	}
	
	@Override
	@Transactional
	public void realizarAprovacaoMovimento(Movimento movimento, Usuario usuario) {
			
		movimento.setStatus(StatusAprovacao.APROVADO);
		movimento.setAprovador(usuario);
		
		this.movimentoRepository.merge(movimento);
		
		if (movimento instanceof MovimentoEstoque) {
			
			TipoMovimentoEstoque tipoMovimentoEstoque =
				(TipoMovimentoEstoque) movimento.getTipoMovimento();
				
			MovimentoEstoque movimentoEstoque = (MovimentoEstoque) movimento;
			
			this.movimentoEstoqueService.atualizarEstoqueProduto(tipoMovimentoEstoque,
															 	 movimentoEstoque);
		
		} else if (movimento instanceof MovimentoEstoqueCota) {
			
			TipoMovimentoEstoque tipoMovimentoEstoque =
				(TipoMovimentoEstoque) movimento.getTipoMovimento();
				
			MovimentoEstoqueCota movimentoEstoqueCota = (MovimentoEstoqueCota) movimento;

			this.movimentoEstoqueService.atualizarEstoqueProdutoCota(tipoMovimentoEstoque,
																 	 movimentoEstoqueCota);
		}
	}
	
}
