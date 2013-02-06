package br.com.abril.nds.process.correcaovendas;

import java.math.BigDecimal;
import java.util.Iterator;

import br.com.abril.nds.dao.EstoqueProdutoCotaDAO;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.EstoqueProdutoCota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.ProcessoAbstrato;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link CorrecaoVendas}
 * 
 * Processo Anterior: {@link CorrecaoIndividual} Próximo Processo:
 * {@link VendaCrescente}
 * </p>
 */
public class CorrecaoTendencia extends ProcessoAbstrato {

    public CorrecaoTendencia(Estudo estudo) {
	super(estudo);
    }

    
    @Override
    protected void executarProcesso() {
	Iterator<Cota> itCota = super.estudo.getCotas().iterator();

	while (itCota.hasNext()) {

	    BigDecimal indiceCorrecaoTendencia = BigDecimal.ONE;

	    Cota cota = itCota.next();
	    // TODO retirar esse trecho de código
	    // FIXME Essa consulta no DAO é somente para teste.
	    cota.setEstoqueProdutoCotas(new EstoqueProdutoCotaDAO()
		    .getByCotaId(cota.getId()));

	    BigDecimal totalReparte = BigDecimal.ZERO;
	    BigDecimal totalVenda = BigDecimal.ZERO;

	    Iterator<EstoqueProdutoCota> itEdicaobase = cota
		    .getEstoqueProdutoCotas().iterator();
	    while (itEdicaobase.hasNext()) {
		EstoqueProdutoCota estoqueProdutoCota = itEdicaobase.next();
		totalReparte = totalReparte.add(estoqueProdutoCota
			.getQuantidadeDevolvida());
		totalVenda = totalVenda.add(estoqueProdutoCota
			.getQuantidadeRecebida());
	    }

	    if (!totalVenda.equals(BigDecimal.ZERO)) {

		BigDecimal percentualVenda = totalReparte.divide(totalVenda, 1,
			BigDecimal.ROUND_FLOOR);

		BigDecimal oneCompare = BigDecimal.ONE;
		oneCompare = oneCompare.divide(new BigDecimal(1), 1,
			BigDecimal.ROUND_FLOOR);

		if (percentualVenda.compareTo(oneCompare) == 0) {
		    indiceCorrecaoTendencia = indiceCorrecaoTendencia
			    .add(new BigDecimal(0.2));
		} else {

		    BigDecimal decimalCompare = new BigDecimal(0.9);
		    decimalCompare = decimalCompare.divide(new BigDecimal(1),
			    1, BigDecimal.ROUND_FLOOR);

		    if (percentualVenda.compareTo(decimalCompare) >= 0) {
			indiceCorrecaoTendencia = indiceCorrecaoTendencia
				.add(new BigDecimal(0.1));
		    }
		}
	    }

	    indiceCorrecaoTendencia = indiceCorrecaoTendencia.divide(
		    new BigDecimal(1), 1, BigDecimal.ROUND_FLOOR);

	    System.out.println("indiceCorrecaoTendencia :: "
		    + indiceCorrecaoTendencia);
	}
    }

}
