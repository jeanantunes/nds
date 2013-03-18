package br.com.abril.nds.process.vendamediafinal;

import java.math.BigDecimal;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.ajustereparte.AjusteReparte;
import br.com.abril.nds.process.jornaleirosnovos.JornaleirosNovos;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - N/A
 * 
 * Processo Anterior: {@link JornaleirosNovos} Próximo Processo: {@link AjusteReparte} </p>
 */
public class VendaMediaFinal extends ProcessoAbstrato {

    private BigDecimal value = BigDecimal.ZERO;

    public VendaMediaFinal(Cota cota) {
	super(cota);
    }

    @Override
    protected void executarProcesso() {

	Cota cota = (Cota) super.genericDTO;

	BigDecimal vendaMedia = cota.getVendaMedia();
	BigDecimal indiceAjusteCota = cota.getIndiceAjusteCota();
	BigDecimal indiceVendaCrescente = cota.getIndiceVendaCrescente();
	BigDecimal indiceTratamentoRegional = cota.getIndiceTratamentoRegional();

	if (vendaMedia != null && indiceAjusteCota != null && indiceVendaCrescente != null && indiceTratamentoRegional != null) {
	    value = cota.getVendaMedia().multiply(cota.getIndiceAjusteCota()).multiply(cota.getIndiceVendaCrescente()).multiply(cota.getIndiceTratamentoRegional());
	}
    }

    public BigDecimal getValue() {
	return value;
    }

}
