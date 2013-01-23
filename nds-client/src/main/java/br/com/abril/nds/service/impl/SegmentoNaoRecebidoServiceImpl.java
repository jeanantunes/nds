package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaNaoRecebeSegmentoDTO;
import br.com.abril.nds.dto.filtro.FiltroSegmentoNaoRecebidoDTO;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.repository.SegmentoNaoRecebidoRepository;
import br.com.abril.nds.repository.TipoSegmentoProdutoRepository;
import br.com.abril.nds.service.SegmentoNaoRecebidoService;

@Service
public class SegmentoNaoRecebidoServiceImpl implements SegmentoNaoRecebidoService {

	@Autowired
	private TipoSegmentoProdutoRepository tipoSegmentoProdutoRepo;
	
	@Autowired
	private SegmentoNaoRecebidoRepository segmentoNaoRecebidoRepo;
	
	@Transactional(readOnly = true)
	@Override
	public List<TipoSegmentoProduto> obterTipoSegmentoProduto() {
		// TODO Auto-generated method stub
		return tipoSegmentoProdutoRepo.buscarTodos() ;
	}

	@Transactional(readOnly = true)
	@Override
	public List<CotaNaoRecebeSegmentoDTO> obterCotasNaoRecebemSegmento(FiltroSegmentoNaoRecebidoDTO filtro) {
		return segmentoNaoRecebidoRepo.buscarCotasNaoRecebemSegmento(filtro);
	}

}
