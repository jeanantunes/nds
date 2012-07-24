package br.com.abril.nds.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.server.model.OperacaoDistribuidor;
import br.com.abril.nds.server.model.Indicador;
import br.com.abril.nds.server.repository.IndicadorRepository;
import br.com.abril.nds.server.service.PainelOperacionalService;

@Service
public class PainelOperacionalServiceImpl implements PainelOperacionalService{

	@Autowired
	private IndicadorRepository indicadorRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<OperacaoDistribuidor> buscarIndicadoresPorDistribuidor() {
		
		List<Indicador> indicadores = this.indicadorRepository.buscarIndicadores();
		
		List<OperacaoDistribuidor> distribuidores = new ArrayList<OperacaoDistribuidor>();
		OperacaoDistribuidor ultimoDistribuidor = null;
		
		for (Indicador indicador : indicadores){
			
			if (!indicador.getDistribuidor().equals(ultimoDistribuidor)){
				
				if (ultimoDistribuidor != null){
					
					distribuidores.add(ultimoDistribuidor);
				}
				
				ultimoDistribuidor = indicador.getDistribuidor();
			}
			
			if (ultimoDistribuidor.getIndicadoresOrd() == null){
				
				ultimoDistribuidor.setIndicadoresOrd(new ArrayList<Indicador>());
			}
			
			ultimoDistribuidor.getIndicadoresOrd().add(indicador);
		}
		
		if (ultimoDistribuidor != null){
			
			distribuidores.add(ultimoDistribuidor);
		}
		
		return distribuidores;
	}
}