package br.com.abril.nds.server.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.server.model.FormatoIndicador;
import br.com.abril.nds.server.model.Indicador;
import br.com.abril.nds.server.model.OperacaoDistribuidor;
import br.com.abril.nds.server.model.TipoIndicador;
import br.com.abril.nds.server.repository.IndicadorRepository;
import br.com.abril.nds.server.service.PainelOperacionalService;

@Service
public class PainelOperacionalServiceImpl implements PainelOperacionalService{

	@Autowired
	private IndicadorRepository indicadorRepository;
	
	private BigDecimal CEM = new BigDecimal(100);
	
	@Override
	@Transactional(readOnly = true)
	public List<OperacaoDistribuidor> buscarIndicadoresPorDistribuidor() {
		
		List<Indicador> indicadores = this.indicadorRepository.buscarIndicadores();
		
		List<OperacaoDistribuidor> distribuidores = new ArrayList<OperacaoDistribuidor>();
		OperacaoDistribuidor ultimoDistribuidor = null;
		
		BigDecimal qtdTotalJornaleiros = null;
		BigDecimal cobrancaDia = null;
		BigDecimal partLiq = null;
		
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
					
					indicador.setValor(new BigDecimal(indicador.getValor()).setScale(2, RoundingMode.HALF_EVEN).toString());
				break;
			}
			
			switch (indicador.getTipoIndicador()) {
				case JORNALEIROS:
					
					qtdTotalJornaleiros = new BigDecimal(indicador.getValor());
				break;
				case COBRANCA:
					
					cobrancaDia = new BigDecimal(indicador.getValor());
				break;
				case LIQUIDACAO:
					
					partLiq = new BigDecimal(indicador.getValor());
				break;
			}
			
			switch (indicador.getGrupoIndicador()){
				case JORNALEIRO:
					
					if (!indicador.getTipoIndicador().equals(TipoIndicador.JORNALEIROS) &&
							qtdTotalJornaleiros != null){
						
						BigDecimal pct = qtdTotalJornaleiros.multiply(new BigDecimal(indicador.getValor()).divide(CEM));
						
						indicador.setValor(indicador.getValor() + " - (" + pct.setScale(0) + "%)");
					}
				break;
				
				case FINANCEIRO:
					
					if (TipoIndicador.COBRANCA_POSTERGADA.equals(indicador.getTipoIndicador())){
						
						BigDecimal pctc = cobrancaDia.multiply(new BigDecimal(indicador.getValor()).divide(CEM));
						
						indicador.setValor(indicador.getValor() + " - (" + pctc.setScale(0, RoundingMode.HALF_EVEN) + "%)");
					} else if (TipoIndicador.INADIMPLENCIA.equals(indicador.getTipoIndicador())){
						
						BigDecimal valorInd = new BigDecimal(indicador.getValor());
						
						BigDecimal pctPartLiqui = valorInd.multiply(partLiq).divide(CEM);
						
						indicador.setValor(indicador.getValor() + " - (" + pctPartLiqui.setScale(0, RoundingMode.HALF_EVEN) + "%)");
					}
				break;
			}
			
			if (FormatoIndicador.MONETARIO.equals(indicador.getFormatoIndicador())){
				
				indicador.setValor("R$ " + indicador.getValor());
			}
			
			if (!TipoIndicador.LIQUIDACAO.equals(indicador.getTipoIndicador())){
			
				ultimoDistribuidor.getIndicadores().add(indicador);
			}
		}
		
		if (ultimoDistribuidor != null){
			
			distribuidores.add(ultimoDistribuidor);
		}
		
		return distribuidores;
	}
}