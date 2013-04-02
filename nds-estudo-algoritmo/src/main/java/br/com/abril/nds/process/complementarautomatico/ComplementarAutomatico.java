package br.com.abril.nds.process.complementarautomatico;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.ajustefinalreparte.ReparteComplementarPorCota;
import br.com.abril.nds.process.calculoreparte.CalcularReparte;
import br.com.abril.nds.process.encalhemaximo.EncalheMaximo;

/**
 * Este processo tem como objetivo calcular o reparteComplementar que será
 * distribuído posteriormente no SubProcesso {@link ReparteComplementarPorCota}
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - N/A
 * 
 * Processo Anterior: {@link EncalheMaximo} Próximo Processo:
 * {@link CalcularReparte}
 * </p>
 */
@Component
public class ComplementarAutomatico extends ProcessoAbstrato {

	@Override
	public void executar(EstudoTransient estudo) {
		if ((estudo.getProdutoEdicaoEstudo() != null) && (estudo.getEdicoesBase() != null)) {
			if ((estudo.isComplementarAutomatico()) && ((estudo.getProdutoEdicaoEstudo().getNumeroEdicao() == 1) || (!estudo.getProdutoEdicaoEstudo().isColecao()))) {
				estudo.setExcedente(new BigDecimal(estudo.getReparteDistribuir()).subtract(estudo.getSomatoriaVendaMedia()));
				BigDecimal percentualExcedente = BigDecimal.ZERO;
				if (estudo.getSomatoriaVendaMedia().compareTo(BigDecimal.ZERO) > 0) {
					percentualExcedente = estudo.getExcedente().divide(estudo.getSomatoriaVendaMedia(), 2, BigDecimal.ROUND_HALF_UP);
				}
				estudo.setReparteComplementar(BigInteger.ZERO);
				if (percentualExcedente.compareTo(BigDecimal.ONE) > 0) {
					// Este cálculo sofreu alterações no trac FAQF2-57
					// %Abrangência =
					// (QtdeDeBancasDoEstudo(SemLegendaDeExclusão) /
					// TotalCotasAtivas) * 100'
					int contadorAtivas = 0;
					for (CotaEstudo cota : estudo.getCotas()) {
						if (!cota.getClassificacao().equals(ClassificacaoCota.BancaSuspensa)) {
							contadorAtivas++;
						}
					}
					BigDecimal percentualAbrangencia = BigDecimal.valueOf(estudo.getCotas().size() / contadorAtivas);
					BigDecimal excedenteAMais = estudo.getExcedente().subtract(estudo.getSomatoriaVendaMedia());

					// RepComplementar = ExcedenteAmais * (1 – (((0,6 *
					// %Abrangência) + 40) / 100))
					BigDecimal temp = BigDecimal.valueOf(0.6).multiply(percentualAbrangencia).add(BigDecimal.valueOf(40));
					temp = excedenteAMais.multiply(BigDecimal.ONE.subtract(temp)).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);

					// RepComplementar = ExcedenteAmais * (1 – (((0,6 *
					// %Abrangência) + 40) / 100))
					if (estudo.isDistribuicaoPorMultiplos()) {
						estudo.setReparteComplementar(estudo.getPacotePadrao());
					} else {
						if (BigDecimal.valueOf(200).divide(estudo.getExcedente(), 2, BigDecimal.ROUND_FLOOR).doubleValue() < 10) {
							if (2 > estudo.getExcedente().multiply(BigDecimal.valueOf(0.02)).doubleValue()) {
								estudo.setReparteComplementar(BigInteger.valueOf(2));
							} else {
								estudo.setReparteComplementar(estudo.getExcedente().multiply(BigDecimal.valueOf(0.02)).toBigInteger());
							}
						} else {
							estudo.setReparteComplementar(estudo.getExcedente().multiply(BigDecimal.valueOf(0.02)).toBigInteger());
						}
					}
				} else if (percentualExcedente.doubleValue() > BigDecimal.valueOf(0.6).doubleValue()) {
					if (estudo.isDistribuicaoPorMultiplos()) {
						estudo.setReparteComplementar(estudo.getPacotePadrao());
					} else {
						// RepComplementar = Excedente * 2%`
					    // 2 / EXCEDENTE > 0.1
						if (BigDecimal.valueOf(2).divide(estudo.getExcedente(), 2, BigDecimal.ROUND_FLOOR).compareTo(BigDecimal.valueOf(0.1)) < 0) {
							if (2 > estudo.getExcedente().multiply(BigDecimal.valueOf(0.02)).doubleValue()) {
								estudo.setReparteComplementar(BigInteger.valueOf(2));
							} else {
								estudo.setReparteComplementar(estudo.getExcedente().multiply(BigDecimal.valueOf(0.02)).toBigInteger());
							}
						} else {
							estudo.setReparteComplementar(estudo.getExcedente().multiply(BigDecimal.valueOf(0.02)).toBigInteger());
						}
					}
				}
				estudo.setReparteDistribuir(estudo.getReparteDistribuir().subtract(estudo.getReparteComplementar()));
			}
		}
	}
}
