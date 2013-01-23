package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaNaoRecebeSegmentoDTO;
import br.com.abril.nds.dto.SegmentoNaoRecebeCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroSegmentoNaoRecebidoDTO;
import br.com.abril.nds.model.distribuicao.SegmentoNaoRecebido;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;

public interface SegmentoNaoRecebidoService {

	List<TipoSegmentoProduto> obterTipoSegmentoProduto();
	
	List<CotaNaoRecebeSegmentoDTO> obterCotasNaoRecebemSegmento(FiltroSegmentoNaoRecebidoDTO filtro);

	void excluirSegmentoNaoRecebido(Long segmentoNaoRecebidoId);
		
	TipoSegmentoProduto obterTipoProdutoPorId(Long id);
	
	void inserirCotasSegmentoNaoRecebido(List<SegmentoNaoRecebido> segmentoNaoRecebido);
	
	List<CotaDTO> obterCotasNaoEstaoNoSegmento(FiltroSegmentoNaoRecebidoDTO filtro);

	List<SegmentoNaoRecebeCotaDTO> obterSegmentosNaoRecebemCota(FiltroSegmentoNaoRecebidoDTO filtro);
}
