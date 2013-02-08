package br.com.abril.nds.process;

import java.util.Iterator;

import org.testng.annotations.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.EstoqueProdutoCota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.correcaovendas.CorrecaoVendas;

public class CorrecaoVendasTest {

    @Test
    public void main() {

	try {
	    CorrecaoVendas correcaoVendas = new CorrecaoVendas();

	    correcaoVendas.executar();
	    /*
	     * Medias medias = new Medias(correcaoVendas.getEstudo());
	     * medias.executar();
	     */
	    Estudo estudoReturn = (Estudo) correcaoVendas.getGenericDTO();

	    Iterator<Cota> itCota = estudoReturn.getCotas().iterator();

	    while (itCota.hasNext()) {

		Cota cota = itCota.next();

		System.out.println();
		System.out.println("<< Cota " + cota.getId() + " >> ");
		System.out.println("\tIndiceCorrecaoTendencia : "
			+ cota.getIndiceCorrecaoTendencia());

		Iterator<EstoqueProdutoCota> itEstoqueProdutoCota = cota
			.getEstoqueProdutoCotas().iterator();
		while (itEstoqueProdutoCota.hasNext()) {

		    System.out.println();

		    EstoqueProdutoCota estoqueProdutoCota = itEstoqueProdutoCota
			    .next();

		    System.out.println("\t\t<< EstoqueProdutoCota "
			    + estoqueProdutoCota.getId() + " >> ");
		    System.out.println("\t\t\tIndiceCorrecao : "
			    + estoqueProdutoCota.getIndiceCorrecao());

		}
	    }
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }
}
