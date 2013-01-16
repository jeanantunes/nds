package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.CotaNaoRecebeSegmentoDTO;
import br.com.abril.nds.dto.filtro.FiltroSegmentoNaoRecebidoDTO;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;

public interface SegmentoNaoRecebidoService {

	List<TipoSegmentoProduto> obterTipoSegmentoProduto();
	
	List<CotaNaoRecebeSegmentoDTO> obterCotasNaoRecebemSegmento(FiltroSegmentoNaoRecebidoDTO filtro);

}
