package br.com.abril.nds.server.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ancientprogramming.fixedformat4j.format.impl.StringFormatter;

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
		
		Integer qtdTotalJornaleiros = 0;
		BigDecimal cobrancaDia = BigDecimal.ZERO;
		BigDecimal partLiq = BigDecimal.ZERO;
		
		for (Indicador indicador : indicadores){
			
			if (!indicador.getDistribuidor().equals(ultimoDistribuidor)){
				
				if (ultimoDistribuidor != null){
					
					distribuidores.add(ultimoDistribuidor);
				}
				
				ultimoDistribuidor = indicador.getDistribuidor();
				
				ultimoDistribuidor.setIndicadores(null);
			}
			
			if (ultimoDistribuidor.getIndicadores() == null){
				
				ultimoDistribuidor.setIndicadores(new ArrayList<Indicador>());
			}
			
			switch(indicador.getFormatoIndicador()){
				case MONETARIO:
					
					if (indicador.getValor() != null){
						
						indicador.setValor(new BigDecimal(indicador.getValor()).setScale(2, RoundingMode.HALF_EVEN).toString());
					} else {
						
						indicador.setValor("0");
					}
				break;
			}
			
			switch (indicador.getTipoIndicador()) {
				case JORNALEIROS:
					
					qtdTotalJornaleiros = new Integer(indicador.getValor());
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
						
						if (indicador.getValor() != null){
							Float pct = (( new Float(indicador.getValor())/qtdTotalJornaleiros.floatValue() ) * 100);							
							indicador.setValor(String.format("%s -( %2.2f %% )", indicador.getValor(), pct));							
						} else {
							
							indicador.setValor("0");
						}
					}
				break;
				
				case FINANCEIRO:
					
					if (TipoIndicador.COBRANCA_POSTERGADA.equals(indicador.getTipoIndicador())){
						
						if (indicador.getValor() != null && !indicador.getValor().equals("0")){
							
							BigDecimal pctc =  new BigDecimal(indicador.getValor()).divide(cobrancaDia).multiply(CEM);
							
							indicador.setValor(indicador.getValor() + " - (" + pctc.toString() + "%)");
						} else {
							
							indicador.setValor("0");
						}
					} else if (TipoIndicador.INADIMPLENCIA.equals(indicador.getTipoIndicador())){
						
						if (indicador.getValor() != null && !indicador.getValor().equals("0")){
							BigDecimal pctPartLiqui = new BigDecimal(indicador.getValor()).divide(partLiq).multiply(CEM);
							
							indicador.setValor(indicador.getValor() + " - (" + pctPartLiqui.toString() + "%)");
						} else {
							
							indicador.setValor("0");
						}
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