package br.com.abril.nds.process.calculoreparte;


public class CalcularReparteTest {
/*
	@Autowired
	private CalcularReparte calcularReparte;

	private EstudoTransient criarAmbiente(boolean configurado, boolean distribuicaoPorMultiplos, BigInteger pacotePadrao, BigDecimal vendaMedia,
			BigInteger reparteCalculado, BigInteger reparteDistribuir, boolean temEdicaoBaseFechada) {
		EstudoTransient estudo = new EstudoTransient();
		estudo.setProduto(new ProdutoEdicaoEstudo());
		estudo.setEdicoesBase(new LinkedList<ProdutoEdicaoEstudo>());
		CotaEstudo cota = new CotaEstudo();
		if (configurado) {
			if (temEdicaoBaseFechada) {
				ProdutoEdicaoEstudo edicao = new ProdutoEdicaoEstudo();
				edicao.setEdicaoAberta(false);
				estudo.getEdicoesBase().add(edicao);
			}
			estudo.setDistribuicaoPorMultiplos(distribuicaoPorMultiplos?1:0);
			estudo.setPacotePadrao(pacotePadrao);
			estudo.setReparteDistribuir(reparteDistribuir);
			ProdutoEdicaoEstudo base = new ProdutoEdicaoEstudo();
			base.setVenda(vendaMedia);
			cota.setEdicoesRecebidas(new ArrayList<ProdutoEdicaoEstudo>());
			cota.getEdicoesRecebidas().add(base);
			cota.setReparteCalculado(reparteCalculado);
		}
		estudo.setCotas(new ArrayList<CotaEstudo>());
		estudo.getCotas().add(cota);
		EstudoServiceEstudo.calculate(estudo);
		estudo.setExcedente(new BigDecimal(estudo.getReparteDistribuir().subtract(estudo.getSomatoriaVendaMedia().toBigInteger())));
		return estudo;
	}

	@Test
	public void testSemConfiguracao() throws Exception {
		// Criação do ambiente
		EstudoTransient estudo = criarAmbiente(false, false, null, null, null, null, false);

		// Execução do Processo
		calcularReparte.executar(estudo);

		// Validação do teste
		assertEquals(BigDecimal.ZERO, estudo.getReparteDistribuir());
		for (CotaEstudo cota : estudo.getCotas()) {
			assertEquals(ClassificacaoCota.SemClassificacao, cota.getClassificacao());
		}
	}

	@Test
	public void testAjustarReparteCalculadoComMultiplos() {
		// Criação do ambiente
		EstudoTransient estudo = criarAmbiente(true, true,  BigInteger(5), new BigDecimal(15), new BigDecimal(10), new BigDecimal(100), true);

		// Execução do Processo
		calcularReparte.setEstudo(estudo);
		calcularReparte.calcularAjusteReparte();

		// Validação do teste
		assertEquals(new BigDecimal(100), calcularReparte.getEstudo().getReparteDistribuir());
		assertEquals(new BigDecimal(15), calcularReparte.getEstudo().getAjusteReparte());
		for (Cota cota : calcularReparte.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(10), cota.getReparteCalculado());
			assertEquals(ClassificacaoCota.SemClassificacao, cota.getClassificacao());
		}
	}

	@Test
	public void testAjustarReparteCalculadoSemMultiplos() {
		// Criação do ambiente
		Estudo estudo = criarAmbiente(true, false, new BigDecimal(5), new BigDecimal(15), new BigDecimal(10), new BigDecimal(100), true);

		// Execução do Processo
		calcularReparte.setEstudo(estudo);
		calcularReparte.calcularAjusteReparte();

		// Validação do teste
		assertEquals(new BigDecimal(100), calcularReparte.getEstudo().getReparteDistribuir());
		assertEquals(new BigDecimal(15), calcularReparte.getEstudo().getAjusteReparte());
		for (Cota cota : calcularReparte.getEstudo().getCotas()) {
			assertEquals(new BigDecimal(10), cota.getReparteCalculado());
			assertEquals(ClassificacaoCota.SemClassificacao, cota.getClassificacao());
		}
	}
*/
}
