package br.com.abril.nds.process.calculoreparte;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;

public class AjusteFinalReparteTest {

	Estudo estudo = null;
	AjusteFinalReparte ajusteFinalReparte=null;
		 
	@Before
	public void setUp() throws Exception {
		estudo = new Estudo();
		estudo.setCotas(new ArrayList<Cota>());
		
		ProdutoEdicao pe = new ProdutoEdicao();
		pe.setId(133786l);
		
		Cota cota = new Cota();
		cota.setId(212l);
		
		estudo.getCotas().add(cota);
		estudo.setProduto(pe);
		
		estudo.setReservaAjuste(new BigDecimal(10));
		
		ajusteFinalReparte= new AjusteFinalReparte(estudo);
	}

	@Test
	public void testExecutar() {
		try {
			ajusteFinalReparte.executar();
		} catch (Exception e) {
			fail("Exceção inválida.");
		}
	}

}
