package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.NegociacaoDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;

public interface NegociacaoDividaRepository {

	
	List<NegociacaoDividaDTO> obterCotaPorNumero(FiltroConsultaNegociacaoDivida filtro);
	
}
