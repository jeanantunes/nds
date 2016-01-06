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
    public List<ComponenteElementoDTO> buscaElementos(String tipo, Long estudo) {
        switch (tipo) {
            case "tipo_ponto_venda": return componenteElementoRepository.buscaTiposDePontoDeVena(estudo);
            case "gerador_de_fluxo": return componenteElementoRepository.buscaGeradorDeFluxo(estudo);
            case "bairro": return componenteElementoRepository.buscaBairros(estudo);
            case "regiao": return componenteElementoRepository.buscaRegioes(estudo);
            case "cotas_a_vista": return componenteElementoRepository.buscaCotasAVista();
            case "cotas_novas": return componenteElementoRepository.buscaCotasNovas();
            case "area_influencia": return componenteElementoRepository.buscaAreaDeInfluencia(estudo);
            case "distrito": return componenteElementoRepository.buscaDistritos(estudo);
            case "tipo_distribuicao_cota": return componenteElementoRepository.buscaTipoDistribuicaoCotas();
            case "legenda_cota": return componenteElementoRepository.buscaLegendaCotas();
            default: return new ArrayList<>();
        }
    }
}
