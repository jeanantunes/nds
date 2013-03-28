package br.com.abril.nds.process.ajustereparte;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;

public class AjusteReparteTest {

	@Autowired
	private AjusteReparte ajusteReparte;

	private EstudoTransient criarAmbiente(boolean configurado, BigInteger pacotePadrao, BigInteger vendaMediaMaisN, BigDecimal vendaMedia) {
		EstudoTransient estudo = new EstudoTransient();
		CotaEstudo cota = new CotaEstudo();
		if (configurado) {
			estudo.setPacotePadrao(pacotePadrao);
			cota.setVendaMediaMaisN(vendaMediaMaisN);
			cota.setVendaMedia(vendaMedia);
		}
		estudo.setCotas(new ArrayList<CotaEstudo>());
		estudo.getCotas().add(cota);
		// calculate() não esta sendo chamado porque a venda média já esta sendo informada
		// estudo.calculate();
		return estudo;
	}

	@Test
	public void testCotaComParametroZerado() throws Exception {
		// Criação do ambiente
		EstudoTransient estudo = criarAmbiente(true, BigInteger.ZERO, BigInteger.ZERO, BigDecimal.ZERO);

		// Execução do processo
		ajusteReparte.executar(estudo);

		// Validação do teste
		for (CotaEstudo c : estudo.getCotas()) {
			assertEquals(BigDecimal.ZERO, c.getReparteCalculado());
		}
	}

	@Test
	public void testCotaComParametro100() throws Exception {
		// Criação do ambiente
		EstudoTransient estudo = criarAmbiente(true, BigInteger.valueOf(10), BigInteger.valueOf(100), BigDecimal.ZERO);

		// Execução do processo
		ajusteReparte.executar(estudo);

		// Validação do teste
		for (CotaEstudo c : estudo.getCotas()) {
			assertEquals(BigDecimal.valueOf(100), c.getReparteCalculado());
		}
	}

	@Test
	public void testCotaComParametro20EVendaMedia50() throws Exception {
		// Criação do ambiente
		EstudoTransient estudo = criarAmbiente(true, BigInteger.valueOf(5), BigInteger.valueOf(20), BigDecimal.valueOf(50));

		// Execução do processo
		ajusteReparte.executar(estudo);

		// Validação do teste
		for (CotaEstudo c : estudo.getCotas()) {
			assertEquals(BigDecimal.valueOf(70), c.getReparteCalculado());
		}
	}

	@Test
	public void testCotaComParametro20VendaMedia50EPacotePadrao25() throws Exception {
		// Criação do ambiente
		EstudoTransient estudo = criarAmbiente(true, BigInteger.valueOf(25), BigInteger.valueOf(20), BigDecimal.valueOf(50));

		// Execução do processo
		ajusteReparte.executar(estudo);

		// Validação do teste
		for (CotaEstudo c : estudo.getCotas()) {
			assertEquals(BigDecimal.valueOf(75), c.getReparteCalculado());
		}
	}

	@Test
	public void testCotaSemConfiguracao() throws Exception {
		// Criação do ambiente
		EstudoTransient estudo = criarAmbiente(false, null, null, null);

		// Execução do processo
		ajusteReparte.executar(estudo);

		// Validação do teste
		for (CotaEstudo c : estudo.getCotas()) {
			assertEquals(BigDecimal.ZERO, c.getReparteCalculado());
		}
	}

	@Test
	public void testCotaSemPacotePadrao() throws Exception {
		// Criação do ambiente
		EstudoTransient estudo = criarAmbiente(true, BigInteger.ZERO, BigInteger.valueOf(30), BigDecimal.valueOf(10));

		// Execução do processo
		ajusteReparte.executar(estudo);

		// Validação do teste
		for (CotaEstudo c : estudo.getCotas()) {
			assertEquals(BigDecimal.valueOf(40), c.getReparteCalculado());
		}
	}

	@Test
	public void testVerificaClassificacaoCotaComParametroConfigurado() throws Exception {
		// Criação do ambiente
		EstudoTransient estudo = criarAmbiente(true, BigInteger.ZERO, BigInteger.valueOf(30), BigDecimal.valueOf(10));

		// Execução do processo
		ajusteReparte.executar(estudo);

		// Validação do teste
		for (CotaEstudo c : estudo.getCotas()) {
			assertEquals(ClassificacaoCota.ReparteFixado, c.getClassificacao());
		}
	}

	@Test
	public void testVerificaClassificacaoCotaSemParametroConfigurado() throws Exception {
		// Criação do ambiente
		EstudoTransient estudo = criarAmbiente(false, null, null, null);

		// Execução do processo
		ajusteReparte.executar(estudo);

		// Validação do teste
		for (CotaEstudo c : estudo.getCotas()) {
			assertEquals(ClassificacaoCota.SemClassificacao, c.getClassificacao());
		}
	}
}
