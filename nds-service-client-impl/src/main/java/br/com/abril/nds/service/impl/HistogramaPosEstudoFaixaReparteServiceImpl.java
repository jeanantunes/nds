package br.com.abril.nds.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.HistogramaPosEstudoAnaliseFaixaReparteDTO;
import br.com.abril.nds.repository.HistogramaPosEstudoRepository;
import br.com.abril.nds.service.HistogramaPosEstudoFaixaReparteService;

@Service
public class HistogramaPosEstudoFaixaReparteServiceImpl implements HistogramaPosEstudoFaixaReparteService {

	@Autowired
	private HistogramaPosEstudoRepository histogramaPosEstudoRepository;
	
	@Transactional(readOnly = true)
	@Override
	public HistogramaPosEstudoAnaliseFaixaReparteDTO obterHistogramaPosEstudo(int faixaDe, int faixaAte, Integer estudoId, List<Long> listaIdEdicaoBase) {
		
		Integer[] faixa = {faixaDe, faixaAte};

		return histogramaPosEstudoRepository.obterHistogramaPosEstudo(estudoId, listaIdEdicaoBase, faixa);
	}
	
	@Transactional(readOnly=true)
	@Override
	public List<Long> obterIdEdicoesBase(Long idEstudo) {
		return histogramaPosEstudoRepository.obterListaIdProdEdicoesBaseEstudo(idEstudo);
	}

	@Override
	@Transactional(readOnly=true)
	public List<HistogramaPosEstudoAnaliseFaixaReparteDTO> obterListaHistogramaPosEstudo(Integer[][] faixas, 
			                                                                             Integer estudoId, 
			                                                                             List<Long> listaIdEdicaoBase) {

		List<HistogramaPosEstudoAnaliseFaixaReparteDTO> listaHistoricoPosEstudo = 
				 this.histogramaPosEstudoRepository.obterHistogramaPosEstudo(estudoId, listaIdEdicaoBase, faixas);
		
		HistogramaPosEstudoAnaliseFaixaReparteDTO consolidado = new HistogramaPosEstudoAnaliseFaixaReparteDTO("Total");

		faixasIterator: for (int i = 0; i < faixas.length; i++) {
			
			Integer reparteToOrder = faixas[i][0];
			
			String inicioFaixa = String.valueOf(faixas[i][0]);
			String fimFaixa = String.valueOf(faixas[i][1]);
			
			for(HistogramaPosEstudoAnaliseFaixaReparteDTO historico : listaHistoricoPosEstudo) {

				String faixaHistorico = historico.getFaixaReparte();
				
				if (inicioFaixa.contains(faixaHistorico.substring(0,2).trim()) && faixaHistorico.endsWith(fimFaixa)) {
					
					historico.setReparteToOrder(reparteToOrder);
					
					consolidado.consolidar(historico);
					
					continue faixasIterator;
				}
			}
			
			HistogramaPosEstudoAnaliseFaixaReparteDTO novaFaixa = 
					new HistogramaPosEstudoAnaliseFaixaReparteDTO(inicioFaixa + " a " + fimFaixa);

			novaFaixa.setReparteToOrder(reparteToOrder);
			
			listaHistoricoPosEstudo.add(novaFaixa);
		}
		
		listaHistoricoPosEstudo.add(consolidado);

		return this.ordenarHistograma(listaHistoricoPosEstudo);
	}
	
	private List<HistogramaPosEstudoAnaliseFaixaReparteDTO> ordenarHistograma(List<HistogramaPosEstudoAnaliseFaixaReparteDTO> listaHistoricoPosEstudo) {
		
		Collections.sort(listaHistoricoPosEstudo, new Comparator<HistogramaPosEstudoAnaliseFaixaReparteDTO>() {

			@Override
			public int compare(HistogramaPosEstudoAnaliseFaixaReparteDTO o1, HistogramaPosEstudoAnaliseFaixaReparteDTO o2) {

				if (o1.getReparteToOrder() == null || o2.getReparteToOrder() == null) {
					
					return 1;
				}
				
				return o1.getReparteToOrder().compareTo(o2.getReparteToOrder());
			}			
		});

		return listaHistoricoPosEstudo;
	}
}
