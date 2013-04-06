package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ComponenteElementoDTO;

public interface ComponenteElementoRepository {

	List<ComponenteElementoDTO> buscaTiposDePontoDeVena();

	List<ComponenteElementoDTO> buscaGeradorDeFluxo();

	List<ComponenteElementoDTO> buscaBairros();

	List<ComponenteElementoDTO> buscaRegioes();

	List<ComponenteElementoDTO> buscaCotasAVista();

	List<ComponenteElementoDTO> buscaCotasNovas();

	List<ComponenteElementoDTO> buscaAreaDeInfluencia();

	List<ComponenteElementoDTO> buscaDistritos();

}
