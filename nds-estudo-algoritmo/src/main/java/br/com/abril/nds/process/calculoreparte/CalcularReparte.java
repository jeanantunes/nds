package br.com.abril.nds.process.calculoreparte;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.dao.EstudoDAO;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
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
	BigDecimal percentualExcedente = BigDecimal.ZERO;

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
			}
		}
	}

	public void calcularPercentualExcedente(EstudoTransient estudo) {
		// %Excedente = Excedente / SVendaMédiaFinal
		if (estudo.getSomatoriaVendaMedia().compareTo(BigDecimal.ZERO) > 0) {
		    // FIXME TROCAR VARIAVEL DO EXCEDENTE
			percentualExcedente = excedenteDistribuir.divide(estudo.getSomatoriaVendaMedia(), 2, BigDecimal.ROUND_HALF_UP);
		}

		for (CotaEstudo cota : estudo.getCotas()) {
		    cota.setReparteMinimo(BigInteger.ZERO);
			if (percentualExcedente.compareTo(BigDecimal.ZERO) < 0) {
				// RepCalculadoCota = ((RepDistribuir / SVendaMédiaFinal) *
				// VendaMédiaFinalCota) + ReparteMínimo
				BigDecimal temp = new BigDecimal(estudo.getReparteDistribuir()).divide(estudo.getSomatoriaVendaMedia(), 2, BigDecimal.ROUND_HALF_UP);
				cota.setReparteCalculado(temp.multiply(cota.getVendaMedia()).toBigInteger());
				// se o reparte mínimo for nulo, simplesmente não o adiciona
				// deixando o cálculo conforme abaixo
				// RepCalculadoCota = ((RepDistribuir / SVendaMédiaFinal) *
				// VendaMédiaFinalCota)
				if (cota.getReparteMinimo() != null) {
					cota.setReparteCalculado(cota.getReparteCalculado().add(cota.getReparteMinimo()));
				}
			} else {
				estudoDAO.carregarPercentuaisProporcao(estudo);

				if ((estudo.getPercentualProporcaoExcedentePDV() != null) && (estudo.getPercentualProporcaoExcedenteVenda() != null)) {
					// ExcedentePDV = ((ExcedenteDistribuir * %PropPDV) / SPDVEstudo) * PDVCota
					BigDecimal temp = excedenteDistribuir.multiply(estudo.getPercentualProporcaoExcedentePDV());
					BigDecimal excedentePDV = BigDecimal.ZERO;
					if (estudo.getTotalPDVs().compareTo(BigDecimal.ZERO) > 0) {
						excedentePDV = temp.divide(estudo.getTotalPDVs(), 2, BigDecimal.ROUND_HALF_UP).multiply(cota.getQuantidadePDVs());
					}

					// ExcedenteVDA = ((ExcedenteDistribuir * %PropVenda) / SVendaMédiaFinal) * VendaMédiaFinalCota
					temp = excedenteDistribuir.multiply(estudo.getPercentualProporcaoExcedenteVenda());
					if (estudo.getPercentualProporcaoExcedenteVenda().compareTo(BigDecimal.ZERO) > 0) {
						temp = temp.divide(estudo.getSomatoriaVendaMedia(), 2, BigDecimal.ROUND_FLOOR);
					}
					BigDecimal excedenteVenda = temp.multiply(cota.getVendaMedia());

					// RepCalculadoCota = VMFCota + ExcedPDV + ExcedVda +
					// ReparteMínimo
					cota.setReparteCalculado(cota.getVendaMedia().add(excedentePDV).add(excedenteVenda).add(new BigDecimal(cota.getReparteMinimo())).toBigInteger());
				}
			}
		}
	}

	public void ajustarReparteCalculado(EstudoTransient estudo) {
		if (estudo.isDistribuicaoPorMultiplos() && (estudo.getPacotePadrao() != null)) {
			for (CotaEstudo cota : estudo.getCotas()) {
				// RepCalculadoCota = ARRED(RepCalculadoCota /Pacote-Padrão; 0)
				// * Pacote-Padrão
				cota.setReparteCalculado(cota.getReparteCalculado().divide(estudo.getPacotePadrao()).multiply(estudo.getPacotePadrao()));
			}
		} else {
			for (CotaEstudo cota : estudo.getCotas()) {
				// Arredondar RepCalculado Cota pelo método inglês
				cota.setReparteCalculado(cota.getReparteCalculado().divide(BigInteger.ONE));
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
}
