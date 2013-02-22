package br.com.abril.nds.process.calculoreparte;

import java.math.BigDecimal;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicaoBase;
import br.com.abril.nds.process.ProcessoAbstrato;
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
public class CalcularReparte extends ProcessoAbstrato {

	BigDecimal excedenteDistribuir = BigDecimal.ZERO;
	BigDecimal percentualExcedente = BigDecimal.ZERO;

	public CalcularReparte(Estudo estudo) {
		super(estudo);
	}

	@Override
	public void executarProcesso() throws Exception {

		// Cálculo da variável Reserva Ajuste
		calcularReservaAjuste();
		// Cálculos do percentual de excedente
		calcularPercentualExcedente();

		MinimoMaximo minimoMaximo = new MinimoMaximo(getEstudo());
		minimoMaximo.executar();

		// Ajustar reparte calculado ao pacote padrão ou simplesmente arredondar reparte calculado
		ajustarReparteCalculado();

		GravarReparteJuramentado gravarReparteJuramentado = new GravarReparteJuramentado(getEstudo());
		gravarReparteJuramentado.executar();

		AjusteFinalReparte ajusteFinalReparte = new AjusteFinalReparte(getEstudo());
		ajusteFinalReparte.executar();

		ReparteComplementarPorCota reparteComplementarPorCota = new ReparteComplementarPorCota(getEstudo());
		reparteComplementarPorCota.executar();

		GravarReparteFinalCota gravarReparteFinalCota = new GravarReparteFinalCota(getEstudo());
		gravarReparteFinalCota.executar();
	}

	public void calcularReservaAjuste() {
		if (getEstudo().getReparteDistribuir().doubleValue() > getEstudo().getSomatoriaVendaMedia().doubleValue()) {
			excedenteDistribuir = getEstudo().getReparteDistribuir().subtract(getEstudo().getSomatoriaVendaMedia());
			boolean temEdicaoBaseFechada = temEdicaoBaseFechada();

			BigDecimal ajusteReparte = BigDecimal.ZERO;
			if (temEdicaoBaseFechada) {
				// ReservaAjuste = Excedente * 1%
				// ou 1 exemplar (o que for maior, desde que 1 exemplar não ultrapasse a 10% do excedente)
				// ou 1 pacote-padrão se for distribuição por múltiplos
				if (getEstudo().isDistribuicaoPorMultiplos()) {
					ajusteReparte = getEstudo().getPacotePadrao();
				} else {
					BigDecimal percentual = BigDecimal.ZERO;
					if (!getEstudo().getExcedente().equals(BigDecimal.ZERO)) {
						percentual = BigDecimal.valueOf(100).divide(getEstudo().getExcedente(), 2, BigDecimal.ROUND_HALF_UP);
					}
					if (percentual.doubleValue() < 10) {
						if (BigDecimal.ONE.doubleValue() > getEstudo().getExcedente().multiply(BigDecimal.valueOf(0.01)).doubleValue()) {
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
		if (!getEstudo().getSomatoriaVendaMedia().equals(BigDecimal.ZERO)) {
			percentualExcedente = getEstudo().getExcedente().divide(getEstudo().getSomatoriaVendaMedia(), 2, BigDecimal.ROUND_HALF_UP);
		}
		for (Cota cota : getEstudo().getCotas()) {
			if (percentualExcedente.doubleValue() < 0) {
				// RepCalculadoCota = ((RepDistribuir / SVendaMédiaFinal) * VendaMédiaFinalCota) + ReparteMínimo
				BigDecimal temp = getEstudo().getReparteDistribuir()
						.divide(getEstudo().getSomatoriaVendaMedia(), 2, BigDecimal.ROUND_HALF_UP);
				cota.setReparteCalculado(temp.multiply(cota.getVendaMedia()).add(cota.getReparteMinimo()));
			} else {
				// ExcedentePDV = ((ExcedenteDistribuir * %PropPDV) / SPDVEstudo) * PDVCota
				BigDecimal temp = excedenteDistribuir.multiply(getEstudo().getPercentualProporcaoExcedentePDV());
				BigDecimal excedentePDV = BigDecimal.ZERO;
				if (!getEstudo().getTotalPDVs().equals(BigDecimal.ZERO)) {
					excedentePDV = temp.divide(getEstudo().getTotalPDVs(), 2, BigDecimal.ROUND_HALF_UP).multiply(
						cota.getQuantidadePDVs());
				}

				// ExcedenteVDA = ((ExcedenteDistribuir * %PropVenda) / SVendaMédiaFinal) * VendaMédiaFinalCota
				temp = excedenteDistribuir.multiply(getEstudo().getPercentualProporcaoExcedenteVenda());
				if (!getEstudo().getPercentualProporcaoExcedenteVenda().equals(BigDecimal.ZERO)) {
					temp = temp.divide(getEstudo().getPercentualProporcaoExcedenteVenda(), 2, BigDecimal.ROUND_FLOOR);
				}
				BigDecimal excedenteVenda = temp.multiply(cota.getVendaMedia());

				// RepCalculadoCota = VMFCota + ExcedPDV + ExcedVda + ReparteMínimo
				cota.setReparteCalculado(cota.getVendaMedia().add(excedentePDV).add(excedenteVenda).add(cota.getReparteMinimo()));
			}
		}
	}

	public void ajustarReparteCalculado() {
		if (getEstudo().isDistribuicaoPorMultiplos()) {
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
