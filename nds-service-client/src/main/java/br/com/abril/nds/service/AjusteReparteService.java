package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.AjusteReparteDTO;
import br.com.abril.nds.model.distribuicao.AjusteReparte;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;


public interface AjusteReparteService {

	void salvarAjuste(AjusteReparte ajuste);

	List<AjusteReparteDTO> buscarCotasEmAjuste(AjusteReparteDTO dto);
	
	List<AjusteReparteDTO> buscarPorIdCota(Long numCota);
	
	void alterarAjuste(AjusteReparte ajuste);
	
	void excluirAjuste(Long idAjuste);
	
	int qtdAjusteSegmento (Long idCota);
	
	AjusteReparteDTO buscarPorIdAjuste(Long id);
	
	List<TipoSegmentoProduto> buscarTodosSegmentos();
	
	TipoSegmentoProduto buscarSegmentoPorID (Long id);
	
	Integer buscarVendaMedia ();
	
}
