package br.com.abril.nds.process.bonificacoes;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.dto.BonificacaoDTO;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.medias.Medias;
import br.com.abril.nds.service.EstudoAlgoritmoService;

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

    @Autowired
    private EstudoAlgoritmoService estudoAlgoritmoService;

    @Override
    public void executar(EstudoTransient estudo) {

	if ((estudo.getBonificacoes() != null) && (!estudo.getBonificacoes().isEmpty())) {
	    for (BonificacaoDTO bonificacao : estudo.getBonificacoes()) {
		// validando reparte minimo
		BigInteger reparteMinimo = BigInteger.ZERO;
		if (bonificacao.getReparteMinimoBigInteger() != null && bonificacao.getReparteMinimoBigInteger().compareTo(BigInteger.ZERO) > 0) {
		    reparteMinimo = bonificacao.getReparteMinimoBigInteger();
		}
		// validando indiceBonificacao
		BigDecimal indiceBonificacao = BigDecimal.ZERO;
		if (bonificacao.getBonificacaoBigDecimal() != null && bonificacao.getBonificacaoBigDecimal().compareTo(BigDecimal.ZERO) > 0) {
		    indiceBonificacao = BigDecimal.valueOf(100).add(bonificacao.getBonificacaoBigDecimal())
			    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
		}

		// inserindo valor nas cotas do estudo
		for (CotaEstudo cota : estudo.getCotas()) {
		    String[] vetor = {bonificacao.getElemento()};
		    if (bonificacao.getComponente() != null && bonificacao.getElemento() != null
			    && estudoAlgoritmoService.isCotaDentroDoComponenteElemento(bonificacao.getComponente(), vetor, cota)) {

			if (indiceBonificacao.compareTo(cota.getIndiceTratamentoRegional()) > 0) {
			    cota.setIndiceTratamentoRegional(indiceBonificacao);
			}

			if (reparteMinimo.compareTo(cota.getReparteMinimoFinal()) > 0) {
			    cota.setReparteMinimoFinal(reparteMinimo);
			}
		    }
		}

		// se todas as cotas estiver selecionado, insere reparte minimo para as bancas SH e VZ fora do estudo
		if (bonificacao.isTodasAsCotas() && bonificacao.getComponente() != null && bonificacao.getElemento() != null) {
		    String[] vetor = {bonificacao.getElemento()};
		    for (CotaEstudo cota : estudo.getCotasExcluidas()) {
			if (cota.getClassificacao().in(ClassificacaoCota.BancaComVendaZero, ClassificacaoCota.BancaSemHistorico) &&
				estudoAlgoritmoService.isCotaDentroDoComponenteElemento(bonificacao.getComponente(), vetor, cota)) {
			    if (reparteMinimo.compareTo(cota.getReparteMinimoFinal()) > 0) {
				cota.setReparteMinimoFinal(reparteMinimo);
				cota.setReparteCalculado(reparteMinimo, estudo);
			    }
			}
		    }
		}
	    }
	}
    }
}
