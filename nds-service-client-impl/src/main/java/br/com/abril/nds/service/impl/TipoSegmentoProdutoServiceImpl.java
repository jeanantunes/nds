package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.repository.TipoSegmentoProdutoRepository;
import br.com.abril.nds.service.TipoSegmentoProdutoService;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Service
public class TipoSegmentoProdutoServiceImpl implements TipoSegmentoProdutoService {
	
	@Autowired
	private TipoSegmentoProdutoRepository tipoSegmentoProdutoRepo;
	
	@Transactional(readOnly = true)
	@Override
	public List<TipoSegmentoProduto> obterTipoSegmentoProduto() {
		return tipoSegmentoProdutoRepo.buscarTodos();
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<TipoSegmentoProduto> obterTipoSegmentoProdutoOrdenados(Ordenacao ordem) {
		return tipoSegmentoProdutoRepo.obterTipoSegmentoProdutoOrdenados(ordem);
	}

	@Transactional(readOnly = true)
	@Override
	public TipoSegmentoProduto obterTipoProdutoSegmentoPorId(Long id) {
		return tipoSegmentoProdutoRepo.buscarPorId(id);
	}
}
