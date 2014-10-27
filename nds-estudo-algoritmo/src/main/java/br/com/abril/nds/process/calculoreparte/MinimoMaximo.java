package br.com.abril.nds.process.calculoreparte;

import org.springframework.stereotype.Component;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.vo.ValidacaoVO;

/**
 * Processo que tem como objetivo ajustar o reparte definido na cota entre o mínimo e o máximo configurado
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link CalcularReparte}
 * 
 * Processo Anterior: N/A Próximo Processo: {@link GravarReparteJuramentado}
 * </p>
 */
@Component
public class MinimoMaximo extends ProcessoAbstrato {

    @Override
    public void executar(EstudoTransient estudo) throws Exception {

	validarRepartesMinimos(estudo);

	for (CotaEstudo cota : estudo.getCotas()) {
	    if ((cota.getIntervaloMinimo() != null) && (cota.getIntervaloMaximo() != null)) {
			
	    	if (cota.getIntervaloMinimo().compareTo(cota.getIntervaloMaximo()) > 0) {
			    throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, String.format("O reparte mínimo da cota %s está maior que o reparte máximo.", cota.getId())));
			}
			
	    	if (cota.getReparteCalculado().compareTo(cota.getIntervaloMinimo()) < 0) {
			    if(!cota.getClassificacao().equals(ClassificacaoCota.CotaMixSemMinMax)){
			    	cota.setReparteCalculado(cota.getIntervaloMinimo(), estudo);
			    }
			    preencheClassificacao(cota);
			} else if (cota.getReparteCalculado().compareTo(cota.getIntervaloMaximo()) > 0) {
				if(!cota.getClassificacao().equals(ClassificacaoCota.CotaMixSemMinMax)){
			    	cota.setReparteCalculado(cota.getIntervaloMaximo(), estudo);
			    }
			    preencheClassificacao(cota);
			}
	    }
	}
    }

    private void preencheClassificacao(CotaEstudo cota) {
	if (cota.isMix()) {
	    cota.setClassificacao(ClassificacaoCota.CotaMix);
	} else {
	    cota.setClassificacao(ClassificacaoCota.MaximoMinimo);
	}
    }

    private void validarRepartesMinimos(EstudoTransient estudo) {
	for (CotaEstudo cota : estudo.getCotas()) {
	    if (cota.getReparteMinimoFinal().compareTo(cota.getReparteCalculado()) > 0) {
		cota.setReparteCalculado(cota.getReparteMinimoFinal(), estudo);
	    }
	}
	for (CotaEstudo cota : estudo.getCotasExcluidas()) {
	    if (cota.getReparteMinimoFinal().compareTo(cota.getReparteCalculado()) > 0) {
		cota.setReparteCalculado(cota.getReparteMinimoFinal(), estudo);
	    }
	}
    }
}
