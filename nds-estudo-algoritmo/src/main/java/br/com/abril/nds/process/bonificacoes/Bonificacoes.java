package br.com.abril.nds.process.bonificacoes;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.stereotype.Component;

import br.com.abril.nds.dto.BonificacaoDTO;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.ajustecota.AjusteCota;
import br.com.abril.nds.process.medias.Medias;
import br.com.abril.nds.util.ComponentesPDV;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil definido no
 * setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - N/A
 * 
 * Processo Anterior: {@link Medias} Próximo Processo: {@link AjusteCota}
 * </p>
 */
@Component
public class Bonificacoes extends ProcessoAbstrato {

	@Override
	public void executar(EstudoTransient estudo) {

		if ((estudo.getBonificacoes() != null) && (!estudo.getBonificacoes().isEmpty())) {
			for (BonificacaoDTO bonificacao : estudo.getBonificacoes()) {
				for (CotaEstudo cota : estudo.getCotas()) {
					if ((bonificacao.getComponente().equals(ComponentesPDV.REGIAO)) && (cota.getRegiao().equals(bonificacao.getElemento()))) {
						if (bonificacao.getBonificacaoBigDecimal().compareTo(BigDecimal.ZERO) > 0) {
							BigDecimal indiceBonificacao = BigDecimal.valueOf(100).add(bonificacao.getBonificacaoBigDecimal()).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
							if (indiceBonificacao.compareTo(BigDecimal.ONE) > 0) {
								cota.setIndiceTratamentoRegional(BigDecimal.ONE);
							} else {
								cota.setIndiceTratamentoRegional(indiceBonificacao);
							}
						}
						if ((bonificacao.getReparteMinimoBigInteger().compareTo(BigInteger.ZERO) > 0) && (!bonificacao.isTodasAsCotas())) {
							if (bonificacao.getReparteMinimoBigInteger().compareTo(cota.getReparteMinimo()) > 0) {
								cota.setReparteMinimo(bonificacao.getReparteMinimoBigInteger());
							}
						}
					}
				}
			}
		}
	}
}
