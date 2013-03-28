package br.com.abril.nds.process.complementarautomatico;


public class ComplementarAutomaticoTest {
/*
    @Autowired
    private ComplementarAutomatico complementarAutomatico;

    private Estudo criarAmbiente(BigDecimal reparteDistribuir, boolean complementarAutomatico, boolean produtoColecao, BigDecimal vendaMedia) {
	Estudo estudo = new Estudo();
	estudo.setReparteDistribuir(reparteDistribuir);
	estudo.setComplementarAutomatico(complementarAutomatico);
	ProdutoEdicao edicao = new ProdutoEdicao();
	if (produtoColecao) {
	    edicao.setColecao(true);
	    estudo.setProduto(edicao);
	}
	estudo.setEdicoesBase(new LinkedList<ProdutoEdicaoBase>());
	estudo.getEdicoesBase().add(edicao);
	Cota cota = new Cota();
	cota.setVendaMedia(vendaMedia);
	estudo.setCotas(new ArrayList<Cota>());
	estudo.getCotas().add(cota);
	EstudoServiceEstudo.calculate(estudo);

	return estudo;
    }

    @Test
    public void testSemConfiguracao() {
	// Criação do ambiente
	Estudo estudo = criarAmbiente(BigDecimal.valueOf(20), true, false, BigDecimal.ZERO);

	// Execução do processo
	complementarAutomatico.setEstudo(estudo);
	complementarAutomatico.executarProcesso();

	// Validação do teste
	assertEquals(BigDecimal.valueOf(20), complementarAutomatico.getEstudo().getReparteDistribuir());
	for (Cota c : complementarAutomatico.getEstudo().getCotas()) {
	    assertEquals(BigDecimal.ZERO, c.getReparteCalculado());
	}
    }

    @Test
    public void testConfiguracao() {
	// Criação do ambiente
	Estudo estudo = criarAmbiente(BigDecimal.valueOf(100), true, true, BigDecimal.valueOf(20));

	// Execução do processo
	complementarAutomatico.setEstudo(estudo);
	complementarAutomatico.executarProcesso();

	// Validação do teste
	assertEquals(BigDecimal.valueOf(98), complementarAutomatico.getEstudo().getReparteDistribuir());
	for (Cota c : complementarAutomatico.getEstudo().getCotas()) {
	    assertEquals(BigDecimal.ZERO, c.getReparteCalculado());
	}
    }
*/    
}
