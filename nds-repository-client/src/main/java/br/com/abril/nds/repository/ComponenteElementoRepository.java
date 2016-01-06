package br.com.abril.nds.repository;

import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.dto.ComponenteElementoDTO;

public interface ComponenteElementoRepository {

	List<ComponenteElementoDTO> buscaTiposDePontoDeVena(Long estudo);

	List<ComponenteElementoDTO> buscaGeradorDeFluxo(Long estudo);

	List<ComponenteElementoDTO> buscaBairros(Long estudo);

	List<ComponenteElementoDTO> buscaRegioes(Long estudo);

	List<ComponenteElementoDTO> buscaCotasAVista();

	List<ComponenteElementoDTO> buscaCotasNovas();

	List<ComponenteElementoDTO> buscaAreaDeInfluencia(Long estudo);

	List<ComponenteElementoDTO> buscaDistritos(Long estudo);

    List<ComponenteElementoDTO> buscaTipoDistribuicaoCotas();

	List<ComponenteElementoDTO> buscaLegendaCotas();
}
