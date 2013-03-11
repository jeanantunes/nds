package br.com.abril.nds.process.calculoreparte;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.dao.EstudoDAO;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicaoBase;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.ajustefinalreparte.AjusteFinalReparte;
import br.com.abril.nds.process.ajustefinalreparte.GravarReparteFinalCota;
import br.com.abril.nds.process.ajustefinalreparte.ReparteComplementarPorCota;
import br.com.abril.nds.process.definicaobases.DefinicaoBases;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 * 		- {@link MinimoMaximo}
 * 		- {@link GravarReparteJuramentado}
 * 		- {@link AjusteFinalReparte}
 * 		- {@link ReparteComplementarPorCota}
 * 		- {@link GravarReparteFinalCota}
 * Processo Pai:
 * 		- N/A
 * 
 * Processo Anterior: {@link DefinicaoBases}
 * Próximo Processo: N/A
 * </p>
 */
@Component
public class CalcularReparte extends ProcessoAbstrato {

    @Autowired
    private MinimoMaximo minimoMaximo;
    
    @Autowired
    private EstudoDAO estudoDAO;
    
    @Autowired
    private GravarReparteJuramentado gravarReparteJuramentado;
    
    BigDecimal excedenteDistribuir = BigDecimal.ZERO;
    BigDecimal percentualExcedente = BigDecimal.ZERO;

    @Override
    public void executarProcesso() throws Exception {

	// Cálculo da variável AjusteReparte
	calcularAjusteReparte();
	// Cálculos do percentual de excedente
	calcularPercentualExcedente();

	minimoMaximo.setEstudo(getEstudo());
	minimoMaximo.executar();

	// Ajustar reparte calculado ao pacote padrão ou simplesmente arredondar reparte calculado
	ajustarReparteCalculado();

	gravarReparteJuramentado.setEstudo(getEstudo());
	gravarReparteJuramentado.executar();
    }

    public void calcularAjusteReparte() {
	if (getEstudo().getReparteDistribuir().compareTo(getEstudo().getSomatoriaVendaMedia()) > 0) {
	    excedenteDistribuir = getEstudo().getReparteDistribuir().subtract(getEstudo().getSomatoriaVendaMedia());
	    boolean temEdicaoBaseFechada = temEdicaoBaseFechada();

	    BigDecimal ajusteReparte = BigDecimal.ZERO;
	    if (temEdicaoBaseFechada) {
		// Variável AjusteReparte modificada no faq FAQF2-53
		// AjusteReparte = Excedente * 1%
		// ou 1 exemplar (o que for maior, desde que 1 exemplar não ultrapasse a 10% do excedente)
		// ou 1 pacote-padrão se for distribuição por múltiplos
		if (getEstudo().isDistribuicaoPorMultiplos()) {
		    ajusteReparte = getEstudo().getPacotePadrao();
		} else {
		    BigDecimal percentual = BigDecimal.ZERO;
		    if (getEstudo().getExcedente().compareTo(BigDecimal.ZERO) > 0) {
			percentual = BigDecimal.valueOf(100).divide(getEstudo().getExcedente(), 2, BigDecimal.ROUND_HALF_UP);
		    }
		    if (percentual.compareTo(BigDecimal.valueOf(10)) < 0) {
			if (BigDecimal.ONE.compareTo(getEstudo().getExcedente().multiply(BigDecimal.valueOf(0.01))) > 0) {
			    ajusteReparte = BigDecimal.ONE;
			} else {
			    ajusteReparte = getEstudo().getExcedente().multiply(BigDecimal.valueOf(0.01));
			}
		    } else {
			ajusteReparte = getEstudo().getExcedente().multiply(BigDecimal.valueOf(0.01));
		    }
		}
		// ExcedenteDistribuir = ExcedenteDistribuir - AjusteReparte
		excedenteDistribuir = excedenteDistribuir.subtract(ajusteReparte);
	    }
	}
    }

    public void calcularPercentualExcedente() {
	// %Excedente = Excedente / SVendaMédiaFinal
	if (getEstudo().getSomatoriaVendaMedia().compareTo(BigDecimal.ZERO) > 0) {
	    percentualExcedente = getEstudo().getExcedente().divide(getEstudo().getSomatoriaVendaMedia(), 2, BigDecimal.ROUND_HALF_UP);
	}
	
	for (Cota cota : getEstudo().getCotas()) {
	    if (percentualExcedente.compareTo(BigDecimal.ZERO) < 0) {
		// RepCalculadoCota = ((RepDistribuir / SVendaMédiaFinal) * VendaMédiaFinalCota) + ReparteMínimo
		BigDecimal temp = getEstudo().getReparteDistribuir()
			.divide(getEstudo().getSomatoriaVendaMedia(), 2, BigDecimal.ROUND_HALF_UP);
		cota.setReparteCalculado(temp.multiply(cota.getVendaMedia()));
		// se o reparte mínimo for nulo, simplesmente não o adiciona deixando o cálculo conforme abaixo
		// RepCalculadoCota = ((RepDistribuir / SVendaMédiaFinal) * VendaMédiaFinalCota)
		if (cota.getReparteMinimo() != null) {
		    cota.setReparteCalculado(cota.getReparteCalculado().add(cota.getReparteMinimo()));
		}
	    } else {
		estudoDAO.carregarPercentuaisProporcao(getEstudo());
		
		if ((getEstudo().getPercentualProporcaoExcedentePDV() != null) && 
			(getEstudo().getPercentualProporcaoExcedenteVenda() != null)) {
		    // ExcedentePDV = ((ExcedenteDistribuir * %PropPDV) / SPDVEstudo) * PDVCota
		    BigDecimal temp = excedenteDistribuir.multiply(getEstudo().getPercentualProporcaoExcedentePDV());
		    BigDecimal excedentePDV = BigDecimal.ZERO;
		    if (getEstudo().getTotalPDVs().compareTo(BigDecimal.ZERO) > 0) {
			excedentePDV = temp.divide(getEstudo().getTotalPDVs(), 2, BigDecimal.ROUND_HALF_UP).multiply(
				cota.getQuantidadePDVs());
		    }

		    // ExcedenteVDA = ((ExcedenteDistribuir * %PropVenda) / SVendaMédiaFinal) * VendaMédiaFinalCota
		    temp = excedenteDistribuir.multiply(getEstudo().getPercentualProporcaoExcedenteVenda());
		    if (getEstudo().getPercentualProporcaoExcedenteVenda().compareTo(BigDecimal.ZERO) > 0 ) {
			temp = temp.divide(getEstudo().getPercentualProporcaoExcedenteVenda(), 2, BigDecimal.ROUND_FLOOR);
		    }
		    BigDecimal excedenteVenda = temp.multiply(cota.getVendaMedia());

		    // RepCalculadoCota = VMFCota + ExcedPDV + ExcedVda + ReparteMínimo
		    cota.setReparteCalculado(cota.getVendaMedia().add(excedentePDV).add(excedenteVenda).add(cota.getReparteMinimo()));
		}
	    }
	}
    }

    public void ajustarReparteCalculado() {
	if (getEstudo().isDistribuicaoPorMultiplos() && (getEstudo().getPacotePadrao() != null)) {
	    for (Cota cota : getEstudo().getCotas()) {
		// RepCalculadoCota = ARRED(RepCalculadoCota /Pacote-Padrão; 0) * Pacote-Padrão
		cota.setReparteCalculado(cota.getReparteCalculado().divide(getEstudo().getPacotePadrao(), 0, BigDecimal.ROUND_HALF_UP)
			.multiply(getEstudo().getPacotePadrao()));
	    }
	} else {
	    for (Cota cota : getEstudo().getCotas()) {
		// Arredondar RepCalculado Cota pelo método inglês
		cota.setReparteCalculado(cota.getReparteCalculado().divide(BigDecimal.ONE, 0, BigDecimal.ROUND_HALF_UP));
	    }
	}
    }

    private boolean temEdicaoBaseFechada() {
	for (ProdutoEdicaoBase edicao : getEstudo().getEdicoesBase()) {
	    if (!edicao.isEdicaoAberta()) {
		return true;
	    }
	}
	return false;
    }
}
