package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ComponenteElementoDTO;

public interface ComponenteElementoService {

	List<ComponenteElementoDTO> buscaElementos(String tipo, Long estudo);

}
