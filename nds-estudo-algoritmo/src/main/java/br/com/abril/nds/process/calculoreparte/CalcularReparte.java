package br.com.abril.nds.process.calculoreparte;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.dao.EstudoDAO;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.PercentualExcedenteEstudo;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.ajustefinalreparte.AjusteFinalReparte;
import br.com.abril.nds.process.ajustefinalreparte.GravarReparteFinalCota;
import br.com.abril.nds.process.ajustefinalreparte.ReparteComplementarPorCota;
import br.com.abril.nds.process.definicaobases.DefinicaoBases;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - {@link MinimoMaximo} - {@link GravarReparteJuramentado} -
 * {@link AjusteFinalReparte} - {@link ReparteComplementarPorCota} -
 * {@link GravarReparteFinalCota} Processo Pai: - N/A
 * 
 * Processo Anterior: {@link DefinicaoBases} Próximo Processo: N/A
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

    @Override
    public void executar(EstudoTransient estudo) throws Exception {

	// Cálculo da variável AjusteReparte
	calcularAjusteReparte(estudo);
	// Cálculos do percentual de excedente
	calcularPercentualExcedente(estudo);

	minimoMaximo.executar(estudo);

	// Ajustar reparte calculado ao pacote padrão ou simplesmente arredondar
	// reparte calculado
	ajustarReparteCalculado(estudo);

	gravarReparteJuramentado.executar(estudo);

	calcularIndiceSobra(estudo);
    }

    public void calcularAjusteReparte(EstudoTransient estudo) {
	BigDecimal reparteDistribuir = new BigDecimal(estudo.getReparteDistribuir());
	if (reparteDistribuir.compareTo(estudo.getSomatoriaVendaMedia()) > 0) {
	    excedenteDistribuir = reparteDistribuir.subtract(estudo.getSomatoriaVendaMedia());
	    boolean temEdicaoBaseFechada = temEdicaoBaseFechada(estudo);

	    BigInteger ajusteReparte = BigInteger.ZERO;
	    if (temEdicaoBaseFechada) {
		// Variável AjusteReparte modificada no faq FAQF2-53
		// AjusteReparte = Excedente * 1%
		// ou 1 exemplar (o que for maior, desde que 1 exemplar não
		// ultrapasse a 10% do excedente)
		// ou 1 pacote-padrão se for distribuição por múltiplos
		if (estudo.isDistribuicaoPorMultiplos()) {
		    ajusteReparte = estudo.getPacotePadrao();
		} else {
		    BigDecimal percentual = BigDecimal.ZERO;
		    if (excedenteDistribuir.compareTo(BigDecimal.ZERO) > 0) {
			percentual = BigDecimal.valueOf(100).divide(excedenteDistribuir, 2, BigDecimal.ROUND_HALF_UP);
		    }
		    if (percentual.compareTo(BigDecimal.valueOf(10)) < 0) {
			if (BigDecimal.ONE.compareTo(excedenteDistribuir.multiply(BigDecimal.valueOf(0.01))) > 0) {
			    ajusteReparte = BigInteger.ONE;
			} else {
			    ajusteReparte = excedenteDistribuir.multiply(BigDecimal.valueOf(0.01)).setScale(0, BigDecimal.ROUND_HALF_UP).toBigInteger();
			}
		    } else {
			ajusteReparte = excedenteDistribuir.multiply(BigDecimal.valueOf(0.01)).setScale(0, BigDecimal.ROUND_HALF_UP).toBigInteger();
		    }
		}
		// ExcedenteDistribuir = ExcedenteDistribuir - AjusteReparte
		excedenteDistribuir = excedenteDistribuir.subtract(new BigDecimal(ajusteReparte));
		estudo.setReservaAjuste(ajusteReparte);
		estudo.setReservaAjusteInicial(ajusteReparte);
		estudo.setReparteDistribuir(estudo.getReparteDistribuir().subtract(ajusteReparte));
	    }
	}
    }

    public void calcularPercentualExcedente(EstudoTransient estudo) throws Exception {
	// %Excedente = Excedente / SVendaMédiaFinal
	if (estudo.getSomatoriaVendaMedia().compareTo(BigDecimal.ZERO) > 0) {
	    estudo.setPercentualExcedente(excedenteDistribuir.divide(estudo.getSomatoriaVendaMedia(), 2, BigDecimal.ROUND_HALF_UP));
	}

	if (estudo.getPercentualProporcaoExcedente().isEmpty()) {
	    throw new Exception("Parametros do distribuidor não preenchido, Percentual de Excedente não pode estar vazio.");
	}

	PercentualExcedenteEstudo percentualExcedenteEstudo;
	if (estudo.getPercentualExcedente().compareTo(BigDecimal.valueOf(0.60)) > 0) {
	    percentualExcedenteEstudo = estudo.getPercentualProporcaoExcedente().get("DE_60_100");
	} else if (estudo.getPercentualExcedente().compareTo(BigDecimal.valueOf(0.60)) <= 0
		&& estudo.getPercentualExcedente().compareTo(BigDecimal.valueOf(0.30)) > 0) {
	    percentualExcedenteEstudo = estudo.getPercentualProporcaoExcedente().get("DE_30_60");
	} else {
	    percentualExcedenteEstudo = estudo.getPercentualProporcaoExcedente().get("DE_0_30");
	}

	for (CotaEstudo cota : estudo.getCotas()) {
	    if (estudo.getPercentualExcedente().compareTo(BigDecimal.ZERO) < 0) {
		// RepCalculadoCota = ((RepDistribuir / SVendaMédiaFinal) * VendaMédiaFinalCota) + ReparteMínimo
		BigDecimal temp = new BigDecimal(estudo.getReparteDistribuir()).divide(estudo.getSomatoriaVendaMedia(), 2, BigDecimal.ROUND_HALF_UP);
		cota.setReparteCalculado(temp.multiply(cota.getVendaMedia()).toBigInteger(), estudo);
		// se o reparte mínimo for nulo, simplesmente não o adiciona
		// deixando o cálculo conforme abaixo
		// RepCalculadoCota = ((RepDistribuir / SVendaMédiaFinal) * VendaMédiaFinalCota)
		if (cota.getReparteMinimo() != null) {
		    cota.setReparteCalculado(cota.getReparteCalculado().add(cota.getReparteMinimo()), estudo);
		}
	    } else {
		if (percentualExcedenteEstudo != null && percentualExcedenteEstudo.getPdv() != null && percentualExcedenteEstudo.getVenda() != null) {
		    // ExcedentePDV = ((ExcedenteDistribuir * %PropPDV) / SPDVEstudo) * PDVCota
		    BigDecimal temp = excedenteDistribuir.multiply(percentualExcedenteEstudo.getPdv().divide(BigDecimal.valueOf(100), 4, BigDecimal.ROUND_HALF_UP));
		    BigDecimal excedentePDV = BigDecimal.ZERO;
		    if (estudo.getTotalPDVs().compareTo(BigDecimal.ZERO) > 0 && cota.getQuantidadePDVs() != null) {
			excedentePDV = temp.divide(estudo.getTotalPDVs(), 4, BigDecimal.ROUND_HALF_UP).multiply(cota.getQuantidadePDVs());
		    }

		    // ExcedenteVDA = ((ExcedenteDistribuir * %PropVenda) / SVendaMédiaFinal) * VendaMédiaFinalCota
		    temp = excedenteDistribuir.multiply(percentualExcedenteEstudo.getVenda().divide(BigDecimal.valueOf(100), 4, BigDecimal.ROUND_HALF_UP));
		    if ((percentualExcedenteEstudo.getVenda().compareTo(BigDecimal.ZERO) > 0) &&
			    (estudo.getSomatoriaVendaMedia().compareTo(BigDecimal.ZERO) > 0)) {
			temp = temp.divide(estudo.getSomatoriaVendaMedia(), 4, BigDecimal.ROUND_HALF_UP);
		    }
		    BigDecimal excedenteVenda = temp.multiply(cota.getVendaMedia());

		    // RepCalculadoCota = VMFCota + ExcedPDV + ExcedVda + ReparteMínimo
		    cota.setReparteCalculado(cota.getVendaMedia().add(excedentePDV).add(excedenteVenda).toBigInteger(), estudo);
		    if (cota.getReparteMinimo() != null) {
			cota.setReparteCalculado(cota.getReparteCalculado().add(cota.getReparteMinimo()), estudo);
		    }
		}
	    }
	}
    }

    public void ajustarReparteCalculado(EstudoTransient estudo) {
	if (estudo.isDistribuicaoPorMultiplos() && (estudo.getPacotePadrao() != null)) {
	    for (CotaEstudo cota : estudo.getCotas()) {
		// RepCalculadoCota = ARRED(RepCalculadoCota /Pacote-Padrão; 0) * Pacote-Padrão
		cota.setReparteCalculado(cota.getReparteCalculado().divide(estudo.getPacotePadrao()).multiply(estudo.getPacotePadrao()), estudo);
	    }
	} else {
	    for (CotaEstudo cota : estudo.getCotas()) {
		// Arredondar RepCalculado Cota pelo método inglês
		cota.setReparteCalculado(cota.getReparteCalculado().divide(BigInteger.ONE), estudo);
	    }
	}
    }

    private boolean temEdicaoBaseFechada(EstudoTransient estudo) {
	for (ProdutoEdicaoEstudo edicao : estudo.getEdicoesBase()) {
	    if (!edicao.isEdicaoAberta()) {
		return true;
	    }
	}
	return false;
    }

    public void calcularIndiceSobra(EstudoTransient estudo) {
	// Fim do sub processo
	// Se houver saldo no reparte total distribuido, não considerando-se o total de reparte juramentado:
	// Indice de Sobra ou Falta = ( 'sum'ReparteCalculado Cota / ReparteCalculado) * ReparteCalculado Cota (não

	BigInteger sumReparteCalculadoCota = BigInteger.ZERO;
	for (CotaEstudo cota : estudo.getCotas()) {
	    sumReparteCalculadoCota = sumReparteCalculadoCota.add(cota.getReparteCalculado());
	}

	Collections.sort(estudo.getCotas(), new Comparator<CotaEstudo>() {
	    @Override
	    public int compare(CotaEstudo c1, CotaEstudo c2) {
		return c2.getReparteCalculado().compareTo(c1.getReparteCalculado());
	    }
	});

	for (CotaEstudo cota : estudo.getCotas()) {

	    if (estudo.getReparteDistribuir().compareTo(BigInteger.ZERO) == 0) {
		break;
	    }
	    if (!cota.getClassificacao().equals(ClassificacaoCota.ReparteFixado) && !cota.getClassificacao().equals(ClassificacaoCota.MaximoMinimo)) {

		BigInteger indicedeSobraouFalta = sumReparteCalculadoCota.divide(estudo.getReparteDistribuir()).multiply(
			cota.getReparteCalculado());

		// Se ainda houver saldo, subtrair ou somar 1 exemplar por cota do maior para o menor reparte
		// (exceto repartes fixados (FX), quantidades MAXIMAS E MINIMAS (MM)
		// e bancas com MIX (MX)).
		if (cota.getClassificacao().notIn(ClassificacaoCota.ReparteFixado, ClassificacaoCota.MaximoMinimo,
			ClassificacaoCota.BancaMixSemDeterminadaPublicacao)) {
		    if (indicedeSobraouFalta.compareTo(BigInteger.ZERO) == 1)
			cota.setReparteCalculado(cota.getReparteCalculado().add(BigInteger.ONE), estudo);
		    else if (indicedeSobraouFalta.compareTo(BigInteger.ZERO) == -1)
			cota.setReparteCalculado(cota.getReparteCalculado().subtract(BigInteger.ONE), estudo);
		}
	    }
	}
    }
}
