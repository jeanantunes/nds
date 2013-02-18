package br.com.abril.nds.process.calculoreparte;

import java.math.BigDecimal;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;
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

	BigDecimal excedente = BigDecimal.ZERO;
	BigDecimal excedenteDistribuir = BigDecimal.ZERO;
	BigDecimal ajusteReparte = BigDecimal.ZERO;
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

	private void calcularReservaAjuste() {
		if (getEstudo().getReparteDistribuir().doubleValue() > getEstudo().getSomatoriaVendaMedia().doubleValue()) {
			excedenteDistribuir = getEstudo().getReparteDistribuir().subtract(getEstudo().getSomatoriaVendaMedia());
			boolean temEdicaoBaseFechada = temEdicaoBaseFechada();

			if (temEdicaoBaseFechada) {
				// ReservaAjuste = Excedente * 1%
				BigDecimal reservaAjuste = BigDecimal.ZERO;
				if (getEstudo().isDistribuicaoPorMultiplos()) {
					reservaAjuste = getEstudo().getPacotePadrao();
				} else {
					if (new BigDecimal(100).divide(excedente, 2, BigDecimal.ROUND_FLOOR).doubleValue() < 10) {
						if (BigDecimal.ONE.doubleValue() > excedente.multiply(new BigDecimal(0.01)).doubleValue()) {
							reservaAjuste = BigDecimal.ONE;
						} else {
							reservaAjuste = excedente.multiply(new BigDecimal(0.01));
						}
					} else {
						reservaAjuste = excedente.multiply(new BigDecimal(0.01));
					}
				}
				// ExcedenteDistribuir = ExcedenteDistribuir - AjusteReparte
				excedenteDistribuir = excedenteDistribuir.subtract(ajusteReparte);
				getEstudo().setReservaAjuste(reservaAjuste);
			}
		}
	}

	private void calcularPercentualExcedente() {
		// %Excedente = Excedente / SVendaMédiaFinal
		percentualExcedente = excedente.divide(getEstudo().getSomatoriaVendaMedia(), 2, BigDecimal.ROUND_FLOOR);
		for (Cota cota : getEstudo().getCotas()) {
			if (percentualExcedente.doubleValue() < 0) {
				// RepCalculadoCota = ((RepDistribuir / SVendaMédiaFinal) * VendaMédiaFinalCota) + ReparteMínimo
				BigDecimal temp = getEstudo().getReparteDistribuir()
						.divide(getEstudo().getSomatoriaVendaMedia(), 2, BigDecimal.ROUND_FLOOR);
				cota.setReparteCalculado(temp.multiply(cota.getVendaMedia()).add(cota.getReparteMinimo()));
			} else {
				// ExcedentePDV = ((ExcedenteDistribuir * %PropPDV) / SPDVEstudo) * PDVCota
				BigDecimal temp = excedenteDistribuir.multiply(getEstudo().getPercentualProporcaoExcedentePDV());
				BigDecimal excedentePDV = temp.divide(getEstudo().getTotalPDVs(), 2, BigDecimal.ROUND_FLOOR).multiply(
						cota.getQuantidadePDVs());

				// ExcedenteVDA = ((ExcedenteDistribuir * %PropVenda) / SVendaMédiaFinal) * VendaMédiaFinalCota
				temp = excedenteDistribuir.multiply(getEstudo().getPercentualProporcaoExcedenteVenda());
				temp = temp.divide(getEstudo().getPercentualProporcaoExcedenteVenda(), 2, BigDecimal.ROUND_FLOOR);
				BigDecimal excedenteVenda = temp.multiply(cota.getVendaMedia());

				// RepCalculadoCota = VMFCota + ExcedPDV + ExcedVda + ReparteMínimo
				cota.setReparteCalculado(cota.getVendaMedia().add(excedentePDV).add(excedenteVenda).add(cota.getReparteMinimo()));
			}
		}
	}

	private void ajustarReparteCalculado() {
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
		for (ProdutoEdicao edicao : getEstudo().getEdicoesBase()) {
			if (!edicao.isEdicaoAberta()) {
				return true;
			}
		}
		return false;
	}
}
