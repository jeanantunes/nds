package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.AbastecimentoDTO;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO;

public interface MapaAbastecimentoService {

	List<AbastecimentoDTO> obterDadosAbastecimento(FiltroMapaAbastecimentoDTO filtro);
}
