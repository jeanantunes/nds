package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.dto.ComponenteElementoDTO;
import br.com.abril.nds.repository.ComponenteElementoRepository;
import br.com.abril.nds.service.ComponenteElementoService;

@Service
public class ComponenteElementoServiceImpl implements ComponenteElementoService {

	@Autowired
	private ComponenteElementoRepository componenteElementoRepository;

	@Override
	public List<ComponenteElementoDTO> buscaElementos(String tipo) {
		switch (tipo) {
		case "null": return new  ArrayList<>();
		case "tipo_ponto_venda": return componenteElementoRepository.buscaTiposDePontoDeVena();
		case "gerador_de_fluxo": return componenteElementoRepository.buscaGeradorDeFluxo();
		case "bairro": return componenteElementoRepository.buscaBairros();
		case "regiao": return componenteElementoRepository.buscaRegioes();
		case "cotas_a_vista":  return componenteElementoRepository.buscaCotasAVista();
		case "cotas_novas": return componenteElementoRepository.buscaCotasNovas();
		case "area_influencia": return componenteElementoRepository.buscaAreaDeInfluencia();
		case "distrito": return componenteElementoRepository.buscaDistritos();
		}
		
		return null;
		
	}
}
