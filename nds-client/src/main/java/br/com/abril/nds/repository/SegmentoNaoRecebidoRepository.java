package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaNaoRecebeSegmentoDTO;
import br.com.abril.nds.dto.SegmentoNaoRecebeCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroSegmentoNaoRecebidoDTO;
import br.com.abril.nds.model.distribuicao.SegmentoNaoRecebido;

/**
 * @author InfoA2 - Samuel Mendes
 * 
 *         <h1>Interface que define as regras de implementação referentes a
 *         entidade SegmentoNaoRecebido
 *         {@link br.com.abril.nds.model.distribuicao.SegmentoNaoRecebido}</h1>
 * 
 */
public interface SegmentoNaoRecebidoRepository extends Repository<SegmentoNaoRecebido, Long> {

	List<CotaNaoRecebeSegmentoDTO> obterCotasNaoRecebemSegmento(FiltroSegmentoNaoRecebidoDTO filtro);
	
	List<SegmentoNaoRecebeCotaDTO> obterSegmentosNaoRecebemCota(FiltroSegmentoNaoRecebidoDTO filtro);

	List<CotaDTO> obterCotasNaoEstaoNoSegmento(FiltroSegmentoNaoRecebidoDTO filtro);
}
