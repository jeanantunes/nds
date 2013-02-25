package br.com.abril.nds.process.calculoreparte;

import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.ProcessoAbstrato;

/**
 * Processo que tem como objetivo ajustar o reparte definido na cota entre o mínimo e o máximo configurado 
 * <p style="white-space: pre-wrap;">SubProcessos:
 * 		- N/A
 * Processo Pai:
 * 		- {@link CalcularReparte}
 * 
 * Processo Anterior: N/A
 * Próximo Processo: {@link GravarReparteJuramentado}
 * </p>
 */
public class MinimoMaximo extends ProcessoAbstrato {

	public MinimoMaximo(Estudo estudo) {
		super(estudo);
	}

	@Override
	public void executarProcesso() throws Exception {
		
		for (Cota cota : getEstudo().getCotas()) {
			if ((cota.getReparteMinimo() != null) && (cota.getReparteMaximo() != null)) {
    			if (cota.getReparteMinimo().compareTo(cota.getReparteMaximo()) > 0) {
    				throw new Exception(String.format("O reparte mínimo da cota %s está maior que o reparte máximo.", cota.getId()));
    			}
    			if (cota.getReparteCalculado().compareTo(cota.getReparteMinimo()) < 0) {
    				cota.setReparteCalculado(cota.getReparteMinimo());
    				
    				if (cota.isMix()) {
    					cota.setClassificacao(ClassificacaoCota.CotaMix);
    				} else {
    					cota.setClassificacao(ClassificacaoCota.MaximoMinimo);
    				}
    			} else if (cota.getReparteCalculado().longValue() > cota.getReparteMaximo().longValue()) {
    				cota.setReparteCalculado(cota.getReparteMaximo());
    	
    				if (cota.isMix()) {
    					cota.setClassificacao(ClassificacaoCota.CotaMix);
    				} else {
    					cota.setClassificacao(ClassificacaoCota.MaximoMinimo);
    				}
    			}
			}
		}
	}
}
