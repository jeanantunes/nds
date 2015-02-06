package br.com.abril.nds.process.calculoreparte;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.dao.EstudoDAO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
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
import br.com.abril.nds.service.EstudoAlgoritmoService;
import br.com.abril.nds.vo.ValidacaoVO;

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

    @Override
    public void executar(EstudoTransient estudo) throws Exception {

	// Cálculo da variável AjusteReparte
	calcularAjusteReparte(estudo);
	// Cálculos do percentual de excedente
	calcularPercentualExcedente(estudo);

	if(estudo.isUsarMix()){
		minimoMaximo.executar(estudo);
	}
	

	// Ajustar reparte calculado ao pacote padrão ou simplesmente arredondar reparte calculado
	ajustarReparteCalculado(estudo);

	gravarReparteJuramentado.executar(estudo);

	calcularIndiceSobra(estudo);
    }

	public void calcularAjusteReparte(EstudoTransient estudo) {
		
		BigDecimal reparteDistribuir = new BigDecimal(estudo.getReparteDistribuir());
		estudo.setExcedenteDistribuir(reparteDistribuir.subtract(estudo.getSomatoriaVendaMedia()));
		
		if (reparteDistribuir.compareTo(estudo.getSomatoriaVendaMedia()) > 0) {
			
			boolean temEdicaoBaseFechada = temEdicaoBaseFechada(estudo);
			
			BigInteger reservaAjuste = BigInteger.ZERO;
			
			if (temEdicaoBaseFechada) {
				// Variável AjusteReparte modificada no faq FAQF2-53
				// AjusteReparte = Excedente * 1%
				// ou 1 exemplar (o que for maior, desde que 1 exemplar não
				// ultrapasse a 10% do excedente)
				// ou 1 pacote-padrão se for distribuição por múltiplos

				// Calculo 1 - Pacote padrao
				BigInteger calculo1 = BigInteger.ZERO;
				
				if (estudo.isDistribuicaoPorMultiplos()
						&& estudo.getPacotePadrao() != null) {
					calculo1 = estudo.getPacotePadrao();
				}
				
				// Calculo 2 - Excedente * 1%
				BigInteger calculo2 = estudo.getExcedenteDistribuir()
						.multiply(BigDecimal.valueOf(0.01))
						.setScale(0, BigDecimal.ROUND_HALF_UP).toBigInteger();
				// Calculo 3 - 1 Exemplar
				BigInteger calculo3 = BigInteger.ZERO;
				// checando se 1 exemplar nao e maior que 10%
				BigDecimal percentual = BigDecimal.ZERO;
				
				if (estudo.getExcedenteDistribuir().compareTo(BigDecimal.ZERO) > 0) {
					percentual = BigDecimal.valueOf(100).divide(
							estudo.getExcedenteDistribuir(), 2,
							BigDecimal.ROUND_HALF_UP);
				}
				
				if (percentual.compareTo(BigDecimal.valueOf(10)) < 0) {
					calculo3 = BigInteger.ONE;
				}

				reservaAjuste = calculo1.max(calculo2);
				reservaAjuste = reservaAjuste.max(calculo3);
				reservaAjuste = EstudoAlgoritmoService.arredondarPacotePadrao(estudo, new BigDecimal(reservaAjuste));

				// ExcedenteDistribuir = ExcedenteDistribuir - AjusteReparte
				estudo.setExcedenteDistribuir(estudo.getExcedenteDistribuir().subtract(new BigDecimal(reservaAjuste)));
				estudo.setReservaAjuste(reservaAjuste);
				estudo.setReservaAjusteInicial(reservaAjuste);
				estudo.setReparteDistribuir(estudo.getReparteDistribuir().subtract(reservaAjuste));
			}
		}
	}

    public void calcularPercentualExcedente(EstudoTransient estudo) throws Exception {
	// %Excedente = Excedente / SVendaMédiaFinal
	if (estudo.getSomatoriaVendaMedia().compareTo(BigDecimal.ZERO) > 0) {
	    estudo.setPercentualExcedente(estudo.getExcedenteDistribuir().divide(estudo.getSomatoriaVendaMedia(), 2, BigDecimal.ROUND_HALF_UP));
	}

	if (estudo.getPercentualProporcaoExcedente().isEmpty()) {
	    throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Parametros do distribuidor não preenchido, Percentual de Excedente não pode estar vazio."));
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

	BigDecimal indiceReparte = BigDecimal.ZERO;
	if (estudo.getSomatoriaVendaMedia().compareTo(BigDecimal.ZERO) > 0) {
	    indiceReparte = new BigDecimal(estudo.getReparteDistribuir()).divide(estudo.getSomatoriaVendaMedia(), 2, BigDecimal.ROUND_HALF_UP);
	}
	BigDecimal excedentePDV = BigDecimal.ZERO;
	BigDecimal excedenteVenda = BigDecimal.ZERO;
	if (percentualExcedenteEstudo != null && percentualExcedenteEstudo.getPdv() != null && percentualExcedenteEstudo.getVenda() != null) {
	    // ExcedentePDV = ((ExcedenteDistribuir * %PropPDV) / SPDVEstudo)
	    BigDecimal pdv = estudo.getExcedenteDistribuir().multiply(percentualExcedenteEstudo.getPdv().divide(BigDecimal.valueOf(100), 4, BigDecimal.ROUND_HALF_UP));
	    if (estudo.getTotalPDVs().compareTo(BigDecimal.ZERO) > 0) {
		excedentePDV = pdv.divide(estudo.getTotalPDVs(), 4, BigDecimal.ROUND_HALF_UP);
	    }

	    // ExcedenteVDA = ((ExcedenteDistribuir * %PropVenda) / SVendaMédiaFinal) 
	    excedenteVenda = estudo.getExcedenteDistribuir().multiply(percentualExcedenteEstudo.getVenda().divide(BigDecimal.valueOf(100), 4, BigDecimal.ROUND_HALF_UP));
	    if ((percentualExcedenteEstudo.getVenda().compareTo(BigDecimal.ZERO) > 0) &&
		    (estudo.getSomatoriaVendaMedia().compareTo(BigDecimal.ZERO) > 0)) {
		excedenteVenda = excedenteVenda.divide(estudo.getSomatoriaVendaMedia(), 4, BigDecimal.ROUND_HALF_UP);
	    }
	}

	for (CotaEstudo cota : estudo.getCotas()) {
	    // calculando reparte minimo para a cota
	    BigDecimal reparteMinimo = BigDecimal.ZERO;
	    if (cota.getReparteMinimo() != null) {
		reparteMinimo = new BigDecimal(cota.getReparteMinimo()).divide(BigDecimal.valueOf(2), 3, BigDecimal.ROUND_HALF_UP);
	    }

	    if (estudo.getPercentualExcedente().compareTo(BigDecimal.ZERO) < 0) {
			// RepCalculadoCota = ((RepDistribuir / SVendaMédiaFinal) * VendaMédiaFinalCota) + ReparteMínimo
			cota.setReparteCalculado(indiceReparte.multiply(cota.getVendaMedia()).add(reparteMinimo).setScale(0, BigDecimal.ROUND_HALF_UP).toBigInteger());
			
			// ajuste no reparteDistribuir do estudo para nao remover novamente o valor do reparte minimo do reparteDistribuir
			BigInteger reparteSemMinimo = indiceReparte.multiply(cota.getVendaMedia()).setScale(0, BigDecimal.ROUND_HALF_UP).toBigInteger();
			estudo.setReparteDistribuir(estudo.getReparteDistribuir().add(cota.getReparteCalculado().subtract(reparteSemMinimo)));
	    } else {
	    	// ExcPDV = ExcedentePDV * PDVCota
			BigDecimal excPDV = BigDecimal.ZERO;
			
			if (cota.getQuantidadePDVs() != null) {
			    excPDV = excedentePDV.multiply(cota.getQuantidadePDVs());
			}
			
			// ExcVDA = ExcedenteVDA * VendaMédiaFinalCota		    
			BigDecimal excVenda = excedenteVenda.multiply(cota.getVendaMedia());
	
			// RepCalculadoCota = VMFCota + ExcedPDV + ExcedVda + ReparteMínimo
			cota.setReparteCalculado(cota.getVendaMedia().add(excPDV).add(excVenda).add(reparteMinimo).setScale(0, BigDecimal.ROUND_HALF_UP).toBigInteger(), estudo);
			
			// ajuste no reparteDistribuir do estudo para nao remover novamente o valor do reparte minimo do reparteDistribuir
			BigInteger reparteSemMinimo = cota.getVendaMedia().add(excPDV).add(excVenda).setScale(0, BigDecimal.ROUND_HALF_UP).toBigInteger();
			estudo.setReparteDistribuir(estudo.getReparteDistribuir().add(cota.getReparteCalculado().subtract(reparteSemMinimo)));
	    }
	}
    }

    public void ajustarReparteCalculado(EstudoTransient estudo) {
		for (CotaEstudo cota : estudo.getCotas()) {
			
			BigInteger reparteArredondado = EstudoAlgoritmoService.arredondarPacotePadrao(estudo, new BigDecimal(cota.getReparteCalculado()));
			
			if(reparteArredondado.compareTo(cota.getReparteMinimo()) < 0){
				cota.setReparteCalculado(cota.getReparteMinimo(), estudo);
			}else{
				cota.setReparteCalculado(reparteArredondado, estudo);
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

	private BigInteger getSomaReparteCalculadoCotas(EstudoTransient estudo) {
		
		List<CotaEstudo> cotas = new ArrayList<>();
		
		cotas.addAll(estudo.getCotas());
		cotas.addAll(estudo.getCotasExcluidas());
		cotas.addAll(estudo.getCotasSoComEdicaoAberta());
		cotas.addAll(estudo.getCotasComReparteJaCalculado());
		
		BigInteger somaReparteCalculadoCota = BigInteger.ZERO;

		for (CotaEstudo cota : cotas) {
			somaReparteCalculadoCota = somaReparteCalculadoCota.add(cota.getReparteCalculado());
		}
		
		return somaReparteCalculadoCota;
	}

    public void calcularIndiceSobra(EstudoTransient estudo) {
	// Fim do sub processo
	// Se houver saldo no reparte total distribuido, não considerando-se o total de reparte juramentado:
	// Indice de Sobra ou Falta = ( 'sum'ReparteCalculado Cota / ReparteCalculado) * ReparteCalculado Cota (não
	List<CotaEstudo> cotas = estudo.getCotas();

	Collections.sort(cotas, new Comparator<CotaEstudo>() {
	    @Override
	    public int compare(CotaEstudo c1, CotaEstudo c2) {
	    	return c2.getReparteCalculado().compareTo(c1.getReparteCalculado());
	    }
	});

	BigDecimal somaRepartes = new BigDecimal(getSomaReparteCalculadoCotas(estudo));
	if (estudo.getReparteComplementar() != null) {
	    somaRepartes = somaRepartes.add(new BigDecimal(estudo.getReparteComplementar()));
	}
	if (estudo.getReservaAjuste() != null) {
	    somaRepartes = somaRepartes.add(new BigDecimal(estudo.getReservaAjuste()));
	}
	estudo.setReparteDistribuir(estudo.getReparteDistribuirInicial().subtract(somaRepartes.toBigInteger()));
	// indice que sera aplicado para todas as cotas na distribuicao da sobra
	BigDecimal indicedeSobraouFalta = BigDecimal.ZERO;
	if (somaRepartes.compareTo(BigDecimal.ZERO) > 0) {
	    indicedeSobraouFalta = new BigDecimal(estudo.getReparteDistribuirInicial()).divide(somaRepartes, 3, BigDecimal.ROUND_HALF_UP);
	}

	// aplicacao do indice de sobra nas cotas
	for (CotaEstudo cota : cotas) {
	    if (estudo.getReparteDistribuir().compareTo(BigInteger.ZERO) == 0) {
		break;
	    }
	    if (cota.getClassificacao().notIn(ClassificacaoCota.ReparteFixado, ClassificacaoCota.MaximoMinimo,
		    ClassificacaoCota.BancaMixSemDeterminadaPublicacao, ClassificacaoCota.CotaMix)) {
		
	    	BigDecimal indiceCalculado = new BigDecimal(cota.getReparteCalculado()).multiply(indicedeSobraouFalta);
		
	    	BigDecimal reparteMinimoCota = new BigDecimal(cota.getReparteMinimoFinal());
		
		if(indiceCalculado.compareTo(reparteMinimoCota) > 0){
			cota.setReparteCalculado(EstudoAlgoritmoService.arredondarPacotePadrao(estudo, indiceCalculado), estudo);
		}
		
	    }
	}

	// Se ainda houver saldo, subtrair ou somar 1 exemplar por cota do maior para o menor reparte
	// (exceto repartes fixados (FX), quantidades MAXIMAS E MINIMAS (MM) e bancas com MIX (MX)).
	BigInteger reparte = BigInteger.ONE;
	if (estudo.isDistribuicaoPorMultiplos() && estudo.getPacotePadrao() != null && estudo.getPacotePadrao().compareTo(BigInteger.ZERO) > 0) {
	    reparte = estudo.getPacotePadrao();
	}
		while (estudo.getReparteDistribuir().compareTo(reparte) >= 0 || estudo.getReparteDistribuir().compareTo(reparte.negate()) <= 0) {
		    for (CotaEstudo cota : cotas) {
				if (cota.getClassificacao().notIn(ClassificacaoCota.ReparteFixado, ClassificacaoCota.MaximoMinimo,
					ClassificacaoCota.BancaMixSemDeterminadaPublicacao, ClassificacaoCota.CotaMix)) {
					    if (estudo.getReparteDistribuir().compareTo(reparte) >= 0) {
						cota.setReparteCalculado(cota.getReparteCalculado().add(reparte), estudo);
					    } else if ((estudo.getReparteDistribuir().compareTo(reparte.negate()) <= 0)) {
					    	if(cota.getReparteCalculado().compareTo(cota.getReparteMinimoFinal()) > 0){
					    		cota.setReparteCalculado(cota.getReparteCalculado().subtract(reparte), estudo);
					    	}
					    } else {
					    	break;
					    }
				}
				if (estudo.getReparteDistribuir().compareTo(BigInteger.ZERO) == 0) {
				    break;
				}
		    }
		    
		    if(cotas.size() == 0){
		    	break;
		    }
		    
		}
    }
}
