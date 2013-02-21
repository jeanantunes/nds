package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.client.vo.TotalizadorProdutoDistribuicaoVO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.StatusEstudo;
import br.com.abril.nds.repository.DistribuicaoRepository;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.MatrizDistribuicaoService;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class MatrizDistribuicaoServiceImpl implements MatrizDistribuicaoService {
	
	@Autowired
	protected CalendarioService calendarioService;
	
	@Autowired
	protected DistribuicaoRepository distribuicaoRepository;
	
	@Autowired
	private EstudoRepository estudoRepository;
	
	@Override
	@Transactional(readOnly = true)
	public TotalizadorProdutoDistribuicaoVO obterMatrizDistribuicao(FiltroLancamentoDTO filtro) {
	
		this.validarFiltro(filtro);
		
		List<ProdutoDistribuicaoVO> produtoDistribuicaoVOs = distribuicaoRepository.obterMatrizDistribuicao(filtro);
		
		return getProdutoDistribuicaoVOTotalizado(produtoDistribuicaoVOs);
	}
	
	private TotalizadorProdutoDistribuicaoVO getProdutoDistribuicaoVOTotalizado(List<ProdutoDistribuicaoVO> produtoDistribuicaoVOs) {
		
		Integer totalEstudoGerado = 0;
		Integer totalEstudoLiberado = 0;
		
		for (ProdutoDistribuicaoVO prodDistVO:produtoDistribuicaoVOs) {
			if (prodDistVO.getIdEstudo() != null) {
				if (prodDistVO.getLiberado().equals(StatusEstudo.LIBERADO.name())) {
					totalEstudoLiberado++;
				}
				else {
					totalEstudoGerado++;
				}
			}
		}
		
		TotalizadorProdutoDistribuicaoVO totProdDistVO = new TotalizadorProdutoDistribuicaoVO();
		totProdDistVO.setListProdutoDistribuicao(produtoDistribuicaoVOs);
		totProdDistVO.setTotalEstudosGerados(totalEstudoGerado);
		totProdDistVO.setTotalEstudosLiberados(totalEstudoLiberado);
		
		return totProdDistVO;
	}
	
	
	/**
	 * Valida o filtro informado.
	 */
	private void validarFiltro(FiltroLancamentoDTO filtro) {
		
		if (filtro == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"Os dados do filtro devem ser informados!");
			
		} else {
		
			List<String> mensagens = new ArrayList<String>();
			
			if (filtro.getData() == null) {
				
				mensagens.add("Os dados do filtro da tela devem ser informados!");
			}
			
			if (filtro.getIdsFornecedores() == null
					|| filtro.getIdsFornecedores().isEmpty()) {
				
				mensagens.add("Os dados do filtro da tela devem ser informados!");
			}
			
			if (!mensagens.isEmpty()) {
				
				ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.WARNING, mensagens);
				
				throw new ValidacaoException(validacaoVO);
			}
		}
	}
	
	
	@Override
	@Transactional
	public void reabrirEstudos(List<ProdutoDistribuicaoVO> produtosDistribuicao) {
		
		List<Long> idsEstudos = new ArrayList<Long>();
		
		for (ProdutoDistribuicaoVO vo:produtosDistribuicao) {
			if (vo.getIdEstudo() != null) {
				idsEstudos.add(vo.getIdEstudo().longValue());
			}
		}
		
		estudoRepository.alterarStatusEstudos(idsEstudos, StatusEstudo.GERADO);
	}
	
	@Override
	@Transactional
	public void excluirEstudos(List<ProdutoDistribuicaoVO> produtosDistribuicao) {
		
		for (ProdutoDistribuicaoVO vo:produtosDistribuicao) {
			
			if (vo.getIdEstudo() != null) {
				Estudo estudo = estudoRepository.buscarPorId(vo.getIdEstudo().longValue());
				estudoRepository.remover(estudo);
			}
		}
	}
}
