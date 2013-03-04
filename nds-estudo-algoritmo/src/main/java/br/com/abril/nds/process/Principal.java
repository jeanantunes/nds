package br.com.abril.nds.process;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicaoBase;
import br.com.abril.nds.process.ajustecota.AjusteCota;
import br.com.abril.nds.process.ajustereparte.AjusteReparte;
import br.com.abril.nds.process.bonificacoes.Bonificacoes;
import br.com.abril.nds.process.calculoreparte.AjusteFinalReparte;
import br.com.abril.nds.process.calculoreparte.CalcularReparte;
import br.com.abril.nds.process.calculoreparte.GravarReparteFinalCota;
import br.com.abril.nds.process.complementarautomatico.ComplementarAutomatico;
import br.com.abril.nds.process.correcaovendas.CorrecaoVendas;
import br.com.abril.nds.process.definicaobases.DefinicaoBases;
import br.com.abril.nds.process.encalhemaximo.EncalheMaximo;
import br.com.abril.nds.process.jornaleirosnovos.JornaleirosNovos;
import br.com.abril.nds.process.medias.Medias;
import br.com.abril.nds.process.montatabelaestudos.MontaTabelaEstudos;
import br.com.abril.nds.process.redutorautomatico.RedutorAutomatico;
import br.com.abril.nds.process.reparteminimo.ReparteMinimo;
import br.com.abril.nds.process.reparteproporcional.ReparteProporcional;
import br.com.abril.nds.process.somarfixacoes.SomarFixacoes;
import br.com.abril.nds.process.vendamediafinal.VendaMediaFinal;
import br.com.abril.nds.process.verificartotalfixacoes.VerificarTotalFixacoes;
import br.com.abril.nds.service.EstudoService;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o
 * perfil definido no setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 * 	- {@link DefinicaoBases}
 * 	- {@link SomarFixacoes}
 * 	- {@link VerificarTotalFixacoes}
 * 	- {@link MontaTabelaEstudos}
 * 	- {@link CorrecaoVendas}
 * 	- {@link Medias}
 * 	- {@link Bonificacoes}
 * 	- {@link AjusteCota}
 * 	- {@link JornaleirosNovos}
 * 	- {@link VendaMediaFinal}
 * 	- {@link AjusteReparte}
 * 	- {@link RedutorAutomatico}
 * 	- {@link ReparteMinimo}
 * 	- {@link ReparteProporcional}
 * 	- {@link EncalheMaximo}
 * 	- {@link ComplementarAutomatico}
 * 	- {@link CalcularReparte}
 * Processo Pai:
 * 	- N/A
 * 
 * Processo Anterior: N/A
 * Próximo Processo: N/A
 * </p>
 */
public class Principal {

    @Autowired
    private EstudoService estudoService;
    
    public void gerarEstudoAutomatico(ProdutoEdicaoBase produto, BigDecimal reparte) throws Exception {
	Estudo estudo = new Estudo();
	estudo.setProduto(produto);
	estudo.setReparteDistribuir(reparte);
	estudo.setReparteDistribuirInicial(reparte);
	
	// carregando parâmetros do banco de dados
	estudoService.carregarParametros(estudo);

	DefinicaoBases definicaoBases = new DefinicaoBases(estudo);
	definicaoBases.executar();

	SomarFixacoes somarFixacoes = new SomarFixacoes(estudo);
	somarFixacoes.executar();

	VerificarTotalFixacoes verificarTotalFixacoes = new VerificarTotalFixacoes(estudo);
	verificarTotalFixacoes.executar();

	EstudoService.calculate(estudo);

	MontaTabelaEstudos montaTabelaEstudos = new MontaTabelaEstudos(estudo);
	montaTabelaEstudos.executar();

	for(Cota cota : estudo.getCotas()) {
		CorrecaoVendas correcaoVendas = new CorrecaoVendas(cota);
		correcaoVendas.executar();

		Medias medias = new Medias(cota);
		medias.executar();
	}

	Bonificacoes bonificacoes = new Bonificacoes(estudo);
	bonificacoes.executar();

	AjusteCota ajusteCota = new AjusteCota(estudo);
	ajusteCota.executar();

	for(Cota cota : estudo.getCotas()) {
		VendaMediaFinal vendaMediaFinal = new VendaMediaFinal(cota);
		vendaMediaFinal.executar();
		JornaleirosNovos jornaleirosNovos = new JornaleirosNovos(cota);
		jornaleirosNovos.executar();
	}

	AjusteReparte ajusteReparte = new AjusteReparte(estudo);
	ajusteReparte.executar();

	EstudoService.calculate(estudo);

	RedutorAutomatico redutorAutomatico = new RedutorAutomatico(estudo);
	redutorAutomatico.executar();

	ReparteMinimo reparteMinimo = new ReparteMinimo(estudo);
	reparteMinimo.executar();

	ReparteProporcional reparteProporcional = new ReparteProporcional(estudo);
	reparteProporcional.executar();

	EncalheMaximo encalheMaximo = new EncalheMaximo(estudo);
	encalheMaximo.executar();

	ComplementarAutomatico complementarAutomatico = new ComplementarAutomatico(estudo);
	complementarAutomatico.executar();

	CalcularReparte calcularReparte = new CalcularReparte(estudo);
	calcularReparte.executar();

	AjusteFinalReparte ajusteFinalReparte = new AjusteFinalReparte(estudo);
	ajusteFinalReparte.executar();
	
	GravarReparteFinalCota gravarReparteFinalCota = new GravarReparteFinalCota(estudo);
	gravarReparteFinalCota.executar();
    }
}
