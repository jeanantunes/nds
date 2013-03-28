package br.com.abril.nds.process.calculoreparte;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.dao.MovimentoEstoqueCotaDAO;
import br.com.abril.nds.dao.ProdutoEdicaoDAO;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.ajustefinalreparte.AjusteFinalReparte;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil definido no
 * setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link CalcularReparte}
 * 
 * Processo Anterior: {@link MinimoMaximo} Próximo Processo: {@link AjusteFinalReparte}
 * </p>
 */
@Component
public class GravarReparteJuramentado extends ProcessoAbstrato {

	@Autowired
	private ProdutoEdicaoDAO produtoEdicaoDao;

	@Autowired
	private MovimentoEstoqueCotaDAO movimentoEstoqueCotaDAO;

	@Override
	public void executar(EstudoTransient estudo) {

		if (estudo.getProduto().isParcial()) {
			for (CotaEstudo cota : estudo.getCotas()) {

				int qtdeVezesEnviada = 0;
				for(ProdutoEdicaoEstudo pe :cota.getEdicoesRecebidas()){
					if(estudo.getProduto().getId().equals(pe.getId())){
						qtdeVezesEnviada++;
					}
				}

				if (qtdeVezesEnviada >= 2) {

					// Verificar se tem reparte juramentado A SER FATURADO
					BigInteger reparteJuramentadoAFaturar = movimentoEstoqueCotaDAO
							.retornarReparteJuramentadoAFaturar(cota, estudo.getProduto()).toBigInteger();

					if (reparteJuramentadoAFaturar.compareTo(BigInteger.ZERO) == 1) {
						// Gravar ReparteJura Cota na tabela
						cota.setReparteJuramentadoAFaturar(reparteJuramentadoAFaturar);

						// Se (ReparteCalculadol Cota < ReparteJura Cota) => ReparteCalculado Cota = 0
						if (cota.getReparteCalculado().compareTo(cota.getReparteJuramentadoAFaturar()) == -1) {
							cota.setReparteCalculado(BigInteger.ZERO);
						} else {
							// ReparteCalculado Cota = ReparteCalculado Cota - ReparteJura Cota
							cota.setReparteCalculado(cota.getReparteCalculado().subtract(cota.getReparteJuramentadoAFaturar()));

							// Se Distribuição por Multiplos = SIM
							if (estudo.isDistribuicaoPorMultiplos()) {
								// RepCalculado Cota = ARRED( RepCalculado Cota
								// / Pacote-Padrão ; 0 )* Pacote-Padrão
								BigInteger pacotePadrao = estudo.getPacotePadrao();
								cota.setReparteCalculado(new BigDecimal(cota.getReparteCalculado()).divide(new BigDecimal(pacotePadrao), 0, BigDecimal.ROUND_HALF_UP).toBigInteger());
							}
						}
					}
				}
			}
		}
		// this.fimProcesso();
	}

	public void fimProcesso(EstudoTransient estudo) {
		// Fim do sub processo
		// Se houver saldo no reparte total distribuido, não considerando-se o total de reparte juramentado:
		// Indice de Sobra ou Falta = ( 'sum'ReparteCalculado Cota / ReparteCalculado) * ReparteCalculado Cota (não

		BigInteger sumReparteCalculadoCota = BigInteger.ZERO;
		for (CotaEstudo cota : estudo.getCotas()) {
			sumReparteCalculadoCota = sumReparteCalculadoCota.add(cota.getReparteCalculado());
		}

		Comparator<CotaEstudo> orderCotaDesc = new Comparator<CotaEstudo>() {
			@Override
			public int compare(CotaEstudo c1, CotaEstudo c2) {
				return c2.getReparteCalculado().compareTo(c1.getReparteCalculado());
			}
		};

		Collections.sort(estudo.getCotas(), orderCotaDesc);

		if (estudo.getReparteDistribuir().compareTo(BigInteger.ZERO) == -1 || estudo.getReparteDistribuir().compareTo(BigInteger.ZERO) == 0) {
			return;
		}

		Collections.sort(estudo.getCotas(), orderCotaDesc);

		for (CotaEstudo cota : estudo.getCotas()) {

			if (!cota.getClassificacao().equals(ClassificacaoCota.ReparteFixado) && !cota.getClassificacao().equals(ClassificacaoCota.MaximoMinimo)) {

				BigInteger indicedeSobraouFalta = sumReparteCalculadoCota.divide(estudo.getReparteDistribuir()).multiply(
						cota.getReparteCalculado());

				// Se ainda houver saldo, subtrair ou somar 1 exemplar por cota do maior para o menor reparte
				// (exceto repartes fixados (FX), quantidades MAXIMAS E MINIMAS (MM)
				// e bancas com MIX (MX)).

				if (!cota.getClassificacao().equals(ClassificacaoCota.ReparteFixado)
						&& !cota.getClassificacao().equals(ClassificacaoCota.MaximoMinimo)
						&& !cota.getClassificacao().equals(ClassificacaoCota.BancaMixSemDeterminadaPublicacao)) {

					if (indicedeSobraouFalta.compareTo(BigInteger.ZERO) == 1)
						cota.setReparteCalculado(cota.getReparteCalculado().add(BigInteger.ONE));
					else if (indicedeSobraouFalta.compareTo(BigInteger.ZERO) == -1)
						cota.setReparteCalculado(cota.getReparteCalculado().subtract(BigInteger.ONE));
				}

				else if (indicedeSobraouFalta.compareTo(BigInteger.ZERO) == -1)
					cota.setReparteCalculado(cota.getReparteCalculado().add(BigInteger.ONE));
			}
		}
	}
}
