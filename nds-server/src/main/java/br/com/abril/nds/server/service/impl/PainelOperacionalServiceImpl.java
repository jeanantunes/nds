package br.com.abril.nds.server.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.server.model.OperacaoDistribuidor;
import br.com.abril.nds.server.model.FormatoIndicador;
import br.com.abril.nds.server.model.Indicador;
import br.com.abril.nds.server.model.TipoIndicador;
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
			
			if (ultimoDistribuidor.getIndicadores() == null){
				
				ultimoDistribuidor.setIndicadores(new ArrayList<Indicador>());
			}
			
			switch(indicador.getFormatoIndicador()){
				case MONETARIO:
					DecimalFormat df = new DecimalFormat(FormatoIndicador.MONETARIO.getFormato());
					indicador.setValor(df.format(indicador.getValor()));
				break;
			}
			
			Long qtdTotalJornaleiros = null;
			Double cobrancaDia = null;
			switch (indicador.getTipoIndicador()) {
				case JORNALEIROS:
					
					qtdTotalJornaleiros = Long.parseLong(indicador.getValor());
				break;
				case COBRANCA:
					
					cobrancaDia = Double.parseDouble(indicador.getValor());
				break;
			}
			
			switch (indicador.getGrupoIndicador()){
				case JORNALEIRO:
					
					if (!indicador.getTipoIndicador().equals(TipoIndicador.JORNALEIROS) &&
							qtdTotalJornaleiros != null){
						
						double pct = Long.parseLong(indicador.getValor()) * qtdTotalJornaleiros / 100;
						
						indicador.setValor(indicador.getValor() + " - (" + pct + ")");
					}
				break;
				
				case FINANCEIRO:
					
					if (TipoIndicador.COBRANCA_POSTERGADA.equals(indicador.getTipoIndicador())){
						
						double pctc = Long.parseLong(indicador.getValor()) * cobrancaDia / 100;
						
						indicador.setValor(indicador.getValor() + " - (" + pctc + ")");
					}
				break;
			}
			
			if (FormatoIndicador.MONETARIO.equals(indicador.getFormatoIndicador())){
				
				indicador.setValor("R$ " + indicador.getValor());
			}
			
			ultimoDistribuidor.getIndicadores().add(indicador);
		}
		
		if (ultimoDistribuidor != null){
			
			distribuidores.add(ultimoDistribuidor);
		}
		
		return distribuidores;
	}
}