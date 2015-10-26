package br.com.abril.nds.process.ajustefinalreparte;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.calculoreparte.CalcularReparte;
import br.com.abril.nds.process.calculoreparte.GravarReparteJuramentado;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 * 	- N/A
 * Processo Pai:
 * 	- {@link CalcularReparte}
 * 
 * Processo Anterior: {@link GravarReparteJuramentado}
 * Próximo Processo: {@link ReparteComplementarPorCota}
 * </p>
 */
@Component
public class AjusteFinalReparte extends ProcessoAbstrato {

    @Autowired
    private ReparteComplementarPorCota reparteComplementarPorCota;

    @Autowired
    private GravarReparteFinalCota gravarReparteFinalCota;

    @Override
    public void executar(EstudoTransient estudo) throws Exception {

	// Variável AjusteReparte modificada no faq FAQF2-53
	// Se ReservaAjuste > 0
	// distribuicao para as cotas que possuem reparteCalculado < ultimaVenda
	if (estudo.getReservaAjuste() != null) {
	    for (CotaEstudo cota : estudo.getCotas()) {
	    	if (estudo.getReservaAjuste().compareTo(BigInteger.ZERO) <= 0) {
			    break;
			}
			// Se Repcalculado < Venda (Última edição fechada, sem correção)
			// Se Cota <> FX / MM / MX / RD / PR
			if (estudo.getReservaAjuste().compareTo(BigInteger.ZERO) == 1 &&
				(cota.getVendaEdicaoMaisRecenteFechada() != null && cota.getReparteCalculado().compareTo(cota.getVendaEdicaoMaisRecenteFechada()) < 0) &&
				(cota.getClassificacao().notIn(ClassificacaoCota.ReparteFixado,
					ClassificacaoCota.MaximoMinimo, ClassificacaoCota.CotaMix,
					ClassificacaoCota.RedutorAutomatico, ClassificacaoCota.BancaSoComEdicaoBaseAberta,
					ClassificacaoCota.BancaForaDaRegiaoDistribuicao))) {
			    // RepCalculado Cota = RepCalculado Cota + 1 (ou um pacote padrao)
			    setReparteCota(cota, estudo);
			}
	    }

	    // distribuicao para as cotas que ENTRARAM no estudo
	    if (estudo.getReservaAjuste().compareTo(BigInteger.ZERO) == 1) {
			for (CotaEstudo cota : estudo.getCotas()) {
			    if (estudo.getReservaAjuste().compareTo(BigInteger.ZERO) <= 0) {
			    	break;
			    }
			    if (estudo.getReservaAjuste().compareTo(BigInteger.ZERO) == 1 &&
				    (cota.getVendaEdicaoMaisRecenteFechada() != null && cota.getReparteCalculado().compareTo(cota.getVendaEdicaoMaisRecenteFechada()) == 0) &&
				    cota.getClassificacao().notIn(ClassificacaoCota.ReparteFixado, ClassificacaoCota.MaximoMinimo, ClassificacaoCota.CotaMix,
					    ClassificacaoCota.BancaForaDaRegiaoDistribuicao, ClassificacaoCota.RedutorAutomatico)) {
			    	setReparteCota(cota, estudo);
			    }
			}
	    }

	    BigInteger reparte = BigInteger.ONE;
	    
	    if (estudo.isDistribuicaoPorMultiplos() && estudo.getPacotePadrao() != null) {
	    	reparte = estudo.getPacotePadrao();
	    }
	   
	    BigInteger excecaoReservaAjuste = BigInteger.ZERO; 
	    
	    // distribuicao para TODAS as cotas em ordem decrescente de reparte
	    while (estudo.getReservaAjuste().compareTo(reparte) >= 0) {

			Comparator<CotaEstudo> orderCotaDesc = new Comparator<CotaEstudo>(){
			    @Override
			    public int compare(CotaEstudo c1, CotaEstudo c2) {
				return c2.getReparteCalculado().compareTo(c1.getReparteCalculado());
			    }
			};
	
			Collections.sort(estudo.getCotas(), orderCotaDesc);
	
			for (CotaEstudo cota : estudo.getCotas()) {
			    if (estudo.getReservaAjuste().compareTo(reparte) < 0) {
			    	break;
			    }
			    if (estudo.getReservaAjuste().compareTo(reparte) >= 0 &&
				    cota.getClassificacao().notIn(ClassificacaoCota.ReparteFixado, ClassificacaoCota.MaximoMinimo, ClassificacaoCota.RedutorAutomatico, ClassificacaoCota.CotaMix)) {
			    	setReparteCota(cota, estudo);
			    }
			}
			
			if(estudo.getCotas().isEmpty()){
				break;
			}
			
			if(estudo.getReservaAjuste().compareTo(excecaoReservaAjuste) == 0){
				estudo.setReparteDistribuir(estudo.getReparteDistribuir().add(estudo.getReservaAjuste()));
				estudo.setReservaAjuste(BigInteger.ZERO);
				break;
			}
			
			excecaoReservaAjuste = estudo.getReservaAjuste();
		
	    }
	}
	reparteComplementarPorCota.executar(estudo);

	gravarReparteFinalCota.executar(estudo);
    }

    private void setReparteCota(CotaEstudo cota, EstudoTransient estudo) {
	BigInteger reparte = BigInteger.ONE;
	if (estudo.isDistribuicaoPorMultiplos() && estudo.getPacotePadrao() != null) {
	    reparte = estudo.getPacotePadrao();
	}
	BigInteger valorFuturo = cota.getReparteCalculado().add(reparte);
	if ((cota.getIntervaloMaximo() == null || valorFuturo.compareTo(cota.getIntervaloMaximo()) <= 0) &&
		(valorFuturo.compareTo(cota.getIntervaloMinimo()) >= 0)) {
	    cota.setReparteCalculado(cota.getReparteCalculado().add(reparte));
	    estudo.setReservaAjuste(estudo.getReservaAjuste().subtract(reparte));
	}
    }
}
