package br.com.abril.nds.process.redutorautomatico;

import java.math.BigDecimal;

import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.ajustereparte.AjusteReparte;
import br.com.abril.nds.process.reparteminimo.ReparteMinimo;

/**
 * Processo que efetua cálculos para definir um percentual de excedente que a
 * cota está tendo e efetuar uma redução no reparte dela de acordo com as
 * vendas.
 * <p style="white-space: pre-wrap;">SubProcessos:
 * 		- N/A
 * Processo Pai:
 * 		- N/A
 * 
 * Processo Anterior: {@link AjusteReparte}
 * Próximo Processo: {@link ReparteMinimo}
 * </p>
 */
public class RedutorAutomatico extends ProcessoAbstrato {

	private BigDecimal menorVenda = BigDecimal.ZERO;
	
	public BigDecimal getMenorVenda() {
		return menorVenda;
	}

	public void setMenorVenda(BigDecimal menorVenda) {
		this.menorVenda = menorVenda;
	}

	public RedutorAutomatico(Estudo estudo) {
		super(estudo);
	}

	@Override
	protected void executarProcesso() {

		// TODO: ainda resta efetuar a consulta dos parâmetros que alimentam o método
		calcularMenorVenda();
		calcularRedutorAutomatico();
	}
	
	public void calcularRedutorAutomatico() {
		for (Cota cota : getEstudo().getCotas()) {
			if ((cota.getVendaMedia().doubleValue() <= menorVenda.doubleValue())
					&& (cota.getVendaEdicaoMaisRecenteFechada().equals(BigDecimal.ZERO))) {
				// TODO: verificar se na subtração abaixo deve remover o reparte calculado que
				// já estiver registrado aqui ou se não será removido nenhum valor, pois aqui
				// ele está sendo zerado.
				cota.setReparteCalculado(BigDecimal.ZERO);
				cota.setClassificacao(ClassificacaoCota.RedutorAutomatico);
			}
			if (cota.getClassificacao().equals(ClassificacaoCota.ReparteFixado)
					|| cota.getClassificacao().equals(ClassificacaoCota.BancaSoComEdicaoBaseAberta)
					|| cota.getClassificacao().equals(ClassificacaoCota.RedutorAutomatico))
				getEstudo().setReparteDistribuir(getEstudo().getReparteDistribuir().subtract(cota.getReparteCalculado()));
		}
	}

	public void calcularMenorVenda() {
		BigDecimal excedente = getEstudo().getReparteDistribuir().subtract(getEstudo().getSomatoriaVendaMedia());
		BigDecimal percentualExcedente = BigDecimal.ZERO;
		if (getEstudo().getSomatoriaVendaMedia().doubleValue() > 0) {
			percentualExcedente = excedente.divide(getEstudo().getSomatoriaVendaMedia(), 2, BigDecimal.ROUND_HALF_UP);
		}

		menorVenda = BigDecimal.ZERO;
		if ((percentualExcedente.doubleValue() > 0.4d) && (percentualExcedente.doubleValue() < 0.6d)) {
			menorVenda = new BigDecimal(0.25d);
		} else if (percentualExcedente.doubleValue() < 0.4d) {
			menorVenda = new BigDecimal(0.5d);
		}
	}
}
