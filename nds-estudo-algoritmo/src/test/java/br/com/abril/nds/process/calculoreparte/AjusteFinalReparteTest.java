package br.com.abril.nds.process.calculoreparte;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
import br.com.abril.nds.process.ajustefinalreparte.AjusteFinalReparte;

public class AjusteFinalReparteTest {

	EstudoTransient estudo = null;

	@Autowired
	private AjusteFinalReparte ajusteFinalReparte;

	@Before
	public void setUp() throws Exception {
		estudo = new EstudoTransient();
		estudo.setCotas(new ArrayList<CotaEstudo>());

		ProdutoEdicaoEstudo pe = new ProdutoEdicaoEstudo();
		pe.setId(133786l);

		CotaEstudo cota = new CotaEstudo();
		cota.setId(212l);

		estudo.getCotas().add(cota);
		estudo.setProduto(pe);

		estudo.setAjusteReparte(BigInteger.valueOf(10));
	}

	@Test
	public void testExecutar() {
		try {
			ajusteFinalReparte.executar(estudo);
		} catch (Exception e) {
			fail("Exceção inválida.");
		}
	}

	@Test
	public void testExecutarReservaAjusteZero() {

		estudo.setAjusteReparte(BigInteger.ONE);
		try {
			ajusteFinalReparte.executar(estudo);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exceção inválida.");
		}

		assertEquals(BigInteger.ZERO, estudo.getAjusteReparte());
	}

	@Test
	public void testExecutarReparteCalculadoDois() {

		estudo.setAjusteReparte(BigInteger.valueOf(2));
		try {
			ajusteFinalReparte.executar(estudo);
		} catch (Exception e) {
			fail("Exceção inválida.");
		}

		assertEquals(BigInteger.valueOf(2), estudo.getCotas().get(0).getReparteCalculado());
	}
}
