package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.CotaNaoRecebeSegmentoDTO;
import br.com.abril.nds.dto.SegmentoNaoRecebeCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroSegmentoNaoRecebidoDTO;

/**
 * @author InfoA2 - Samuel Mendes
 * 
 *         <h1>Interface que define as regras de implementação referentes a
 *         entidade SegmentoNaoRecebido
 *         {@link br.com.abril.nds.model.distribuicao.SegmentoNaoRecebido}</h1>
 * 
 */
public interface SegmentoNaoRecebidoRepository {

	List<CotaNaoRecebeSegmentoDTO> buscarCotasNaoRecebemSegmento(FiltroSegmentoNaoRecebidoDTO filtro);
	
	List<SegmentoNaoRecebeCotaDTO> buscarSegmentosNaoRecebemCota(FiltroSegmentoNaoRecebidoDTO filtro);
}
