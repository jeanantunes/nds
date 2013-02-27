package br.com.abril.nds.process.jornaleirosnovos;

import java.math.BigDecimal;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.dao.ProdutoEdicaoDAO;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.ajustecota.AjusteCota;
import br.com.abril.nds.process.correcaovendas.CorrecaoIndividual;
import br.com.abril.nds.process.medias.Medias;
import br.com.abril.nds.process.vendamediafinal.VendaMediaFinal;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - N/A
 * 
 * Processo Anterior: {@link AjusteCota} Próximo Processo: {@link VendaMediaFinal} </p>
 */
public class JornaleirosNovos extends ProcessoAbstrato {

    public JornaleirosNovos(Cota cota) {
	super(cota);
    }

    @Override
    protected void executarProcesso() throws Exception {

	Cota cota = (Cota) super.genericDTO;
	cota = new CotaDAO().getCotaEquivalenteByCota(cota);

	if (cota.isNova()) {

	    if (cota.getEdicoesRecebidas() != null && cota.getEdicoesRecebidas().size() <= 3) {

		BigDecimal vendaMediaCorrigidaNovo = BigDecimal.ZERO;
		BigDecimal totalVendaMediaCorrigidaEquivalente = BigDecimal.ZERO;

		int iEquivalente = 0;
		while (iEquivalente < cota.getEquivalente().size()) {

		    Cota cotaEquivalente = cota.getEquivalente().get(iEquivalente);

		    cotaEquivalente.setEdicoesRecebidas(new ProdutoEdicaoDAO().getEdicaoRecebidas(cotaEquivalente));

		    int iProdutoEdicaoEquivalente = 0;
		    while (iProdutoEdicaoEquivalente < cotaEquivalente.getEdicoesRecebidas().size()) {

			ProdutoEdicao produtoEdicaoEquivalente = cotaEquivalente.getEdicoesRecebidas().get(iProdutoEdicaoEquivalente);

			CorrecaoIndividual correcaoIndividual = new CorrecaoIndividual(produtoEdicaoEquivalente);
			correcaoIndividual.executar();

			iProdutoEdicaoEquivalente++;
		    }

		    Medias medias = new Medias(cotaEquivalente);
		    medias.executar();

		    totalVendaMediaCorrigidaEquivalente = totalVendaMediaCorrigidaEquivalente.add(cotaEquivalente.getVendaMedia());

		    iEquivalente++;
		}

		if (totalVendaMediaCorrigidaEquivalente.compareTo(BigDecimal.ZERO) == 1) {

		    BigDecimal qtdeEquivalente = new BigDecimal(cota.getEquivalente().size());
		    vendaMediaCorrigidaNovo = totalVendaMediaCorrigidaEquivalente.divide(qtdeEquivalente, 2, BigDecimal.ROUND_FLOOR);
		    vendaMediaCorrigidaNovo = vendaMediaCorrigidaNovo.multiply(cota.getIndiceAjusteEquivalente()).divide(BigDecimal.ONE, 2, BigDecimal.ROUND_FLOOR);

		    cota.setVendaMedia(vendaMediaCorrigidaNovo);
		}
	    }

	}
    }
}
