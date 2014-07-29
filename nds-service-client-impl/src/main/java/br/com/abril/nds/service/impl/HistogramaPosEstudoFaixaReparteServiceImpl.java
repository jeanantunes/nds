package br.com.abril.nds.service.impl;

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
	
	@Transactional
	@Override
	public List<Long> obterIdEdicoesBase(Long idEstudo) {
		return histogramaPosEstudoRepository.obterListaIdProdEdicoesBaseEstudo(idEstudo);
	}

	@Override
	@Transactional
	public List<HistogramaPosEstudoAnaliseFaixaReparteDTO> obterListaHistogramaPosEstudo(Integer[][] faixas, 
			                                                                             Integer estudoId, 
			                                                                             List<Long> listaIdEdicaoBase) {

		List<HistogramaPosEstudoAnaliseFaixaReparteDTO> listaHistoricoPosEstudo = 
				 this.histogramaPosEstudoRepository.obterHistogramaPosEstudo(estudoId, listaIdEdicaoBase, faixas);
		
		HistogramaPosEstudoAnaliseFaixaReparteDTO consolidado = new HistogramaPosEstudoAnaliseFaixaReparteDTO("Total");

		faixasIterator: for (int i = 0; i < faixas.length; i++) {
			
			String inicioFaixa = String.valueOf(faixas[i][0]);
			String fimFaixa = String.valueOf(faixas[i][1]);
			
			for(HistogramaPosEstudoAnaliseFaixaReparteDTO historico : listaHistoricoPosEstudo) {

				String faixaHistorico = historico.getFaixaReparte();
				
				if (faixaHistorico.startsWith(inicioFaixa) && faixaHistorico.endsWith(fimFaixa)) {
					
					consolidado.consolidar(historico);
					
					continue faixasIterator;
				}
			}

			listaHistoricoPosEstudo.add(new HistogramaPosEstudoAnaliseFaixaReparteDTO(inicioFaixa + " a " + fimFaixa));
		}
		
		listaHistoricoPosEstudo.add(consolidado);

		return listaHistoricoPosEstudo;
	}
}
