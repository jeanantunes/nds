package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CaracteristicaDistribuicaoDTO;
import br.com.abril.nds.dto.CaracteristicaDistribuicaoSimplesDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaCaracteristicaDistribuicaoDetalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaCaracteristicaDistribuicaoSimplesDTO;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.repository.CaracteristicaDistribuicaoRepository;
import br.com.abril.nds.repository.TipoClassificacaoProdutoRepository;
import br.com.abril.nds.service.CaracteristicaDistribuicaoService;

@Service
public class CaracteristicaDistribuicaoServiceImpl implements CaracteristicaDistribuicaoService {
	
	@Autowired
	private TipoClassificacaoProdutoRepository tipoClassificacaoProdutoRepository;
	
	@Autowired
	private CaracteristicaDistribuicaoRepository claDistribuicaoRepository;
	
	
	@Override
	@Transactional
	public List<TipoClassificacaoProduto> obterClassificacoesProduto(){
		return tipoClassificacaoProdutoRepository.obterTodos();
	}

	@Override
	@Transactional
	public List<CaracteristicaDistribuicaoDTO> buscarComFiltroCompleto(
			FiltroConsultaCaracteristicaDistribuicaoDetalheDTO filtro) {
		return claDistribuicaoRepository.obterCaracteristicaDistribuicaoDetalhe(filtro);
	}

	@Override
	@Transactional
	public List<CaracteristicaDistribuicaoSimplesDTO> buscarComFiltroSimples(
			FiltroConsultaCaracteristicaDistribuicaoSimplesDTO filtro) {
		return claDistribuicaoRepository.obterCaracteristicaDistribuicaoSimples(filtro);
	}
	
}
