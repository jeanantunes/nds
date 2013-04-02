package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.MonthDay;
import org.joda.time.Years;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.dao.DefinicaoBasesDAO;
import br.com.abril.nds.dao.EstudoDAO;
import br.com.abril.nds.dao.ProdutoEdicaoDAO;
import br.com.abril.nds.enumerators.DataReferencia;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
import br.com.abril.nds.process.ajustecota.AjusteCota;
import br.com.abril.nds.process.ajustefinalreparte.AjusteFinalReparte;
import br.com.abril.nds.process.ajustereparte.AjusteReparte;
import br.com.abril.nds.process.bonificacoes.Bonificacoes;
import br.com.abril.nds.process.calculoreparte.CalcularReparte;
import br.com.abril.nds.process.complementarautomatico.ComplementarAutomatico;
import br.com.abril.nds.process.correcaovendas.CorrecaoVendas;
import br.com.abril.nds.process.definicaobases.DefinicaoBases;
import br.com.abril.nds.process.encalhemaximo.EncalheMaximo;
import br.com.abril.nds.process.jornaleirosnovos.JornaleirosNovos;
import br.com.abril.nds.process.medias.Medias;
import br.com.abril.nds.process.redutorautomatico.RedutorAutomatico;
import br.com.abril.nds.process.reparteminimo.ReparteMinimo;
import br.com.abril.nds.process.reparteproporcional.ReparteProporcional;
import br.com.abril.nds.process.vendamediafinal.VendaMediaFinal;
import br.com.abril.nds.process.verificartotalfixacoes.VerificarTotalFixacoes;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil definido no
 * setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - {@link DefinicaoBases} - {@link SomarFixacoes} - {@link VerificarTotalFixacoes} - {@link MontaTabelaEstudos} -
 * {@link CorrecaoVendas} - {@link Medias} - {@link Bonificacoes} - {@link AjusteCota} - {@link JornaleirosNovos} -
 * {@link VendaMediaFinal} - {@link AjusteReparte} - {@link RedutorAutomatico} - {@link ReparteMinimo} -
 * {@link ReparteProporcional} - {@link EncalheMaximo} - {@link ComplementarAutomatico} - {@link CalcularReparte} Processo Pai: -
 * N/A
 * 
 * Processo Anterior: N/A Próximo Processo: N/A
 * </p>
 */
@Service
public class EstudoAlgoritmoService {

	private static final Logger log = LoggerFactory.getLogger(EstudoAlgoritmoService.class);

	@Autowired
	private EstudoDAO estudoDAO;

	@Autowired
	private DefinicaoBasesDAO definicaoBasesDAO;

	@Autowired
	private ProdutoEdicaoDAO produtoEdicaoDAO;

	@Autowired
	private DefinicaoBases definicaoBases;

	@Autowired
	private VerificarTotalFixacoes verificarTotalFixacoes;

	@Autowired
	private AjusteReparte ajusteReparte;

	@Autowired
	private RedutorAutomatico redutorAutomatico;

	@Autowired
	private ReparteMinimo reparteMinimo;

	@Autowired
	private ReparteProporcional reparteProporcional;

	@Autowired
	private EncalheMaximo encalheMaximo;

	@Autowired
	private ComplementarAutomatico complementarAutomatico;

	@Autowired
	private CalcularReparte calcularReparte;

	@Autowired
	private AjusteFinalReparte ajusteFinalReparte;

	@Autowired
	private CorrecaoVendas correcaoVendas;

	@Autowired
	private Medias medias;

	@Autowired
	private VendaMediaFinal vendaMediaFinal;

	@Autowired
	private Bonificacoes bonificacoes;

	@Autowired
	private AjusteCota ajusteCota;

	@Autowired
	private JornaleirosNovos jornaleirosNovos;

	public static void calculate(EstudoTransient estudo) {
		// Somatória da venda média de todas as cotas e
		// Somatória de reparte das edições abertas de todas as cotas
		estudo.setSomatoriaVendaMedia(BigDecimal.ZERO);
		estudo.setSomatoriaReparteEdicoesAbertas(BigDecimal.ZERO);
		estudo.setTotalPDVs(BigDecimal.ZERO);
		for (CotaEstudo cota : estudo.getCotas()) {
			CotaServiceEstudo.calculate(cota);
			if (cota.getClassificacao().notIn(ClassificacaoCota.ReparteFixado, ClassificacaoCota.BancaSoComEdicaoBaseAberta,
					ClassificacaoCota.RedutorAutomatico)) {
				estudo.setSomatoriaVendaMedia(estudo.getSomatoriaVendaMedia().add(cota.getVendaMedia()));
			}
			if (cota.isCotaSoRecebeuEdicaoAberta()) {
				estudo.setSomatoriaReparteEdicoesAbertas(estudo.getSomatoriaReparteEdicoesAbertas().add(cota.getSomaReparteEdicoesAbertas()));
			}
			estudo.setTotalPDVs(estudo.getTotalPDVs().add(cota.getQuantidadePDVs()));
		}
	}

	public void carregarParametros(EstudoTransient estudo) {
		estudo.setProdutoEdicaoEstudo(produtoEdicaoDAO.getLastProdutoEdicaoByIdProduto(estudo.getProdutoEdicaoEstudo().getProduto().getCodigo()));
		if (estudo.getPacotePadrao() == null) {
			estudo.setPacotePadrao(BigInteger.valueOf(estudo.getProdutoEdicaoEstudo().getPacotePadrao()));
		}
		estudo.getProdutoEdicaoEstudo().setPacotePadrao(0);
		estudoDAO.carregarParametrosDistribuidor(estudo);
		estudoDAO.carregarPercentuaisExcedente(estudo);
	}

	public LinkedList<ProdutoEdicaoEstudo> buscaEdicoesPorLancamento(ProdutoEdicaoEstudo edicao) {
		log.info("Buscando edições para estudo.");
		return definicaoBasesDAO.listaEdicoesPorLancamento(edicao);
	}

	public List<ProdutoEdicaoEstudo> buscaEdicoesAnosAnterioresVeraneio(ProdutoEdicaoEstudo edicao) throws Exception {
		List<ProdutoEdicaoEstudo> listaEdicoesBase = definicaoBasesDAO.listaEdicoesAnosAnterioresMesmoMes(edicao);

		if (!listaEdicoesBase.isEmpty()) {
			return listaEdicoesBase;
		}

		listaEdicoesBase = definicaoBasesDAO.listaEdicoesAnosAnterioresVeraneio(edicao, getDatasPeriodoVeraneio(edicao));
		if (listaEdicoesBase.isEmpty()) {
			throw new Exception("Não foram encontradas edições de veraneio, favor inserir as bases manualmente.");
		}
		return listaEdicoesBase;
	}

	public List<ProdutoEdicaoEstudo> buscaEdicoesAnosAnterioresSaidaVeraneio(ProdutoEdicaoEstudo edicao) {
		return definicaoBasesDAO.listaEdicoesAnosAnterioresVeraneio(edicao, getDatasPeriodoSaidaVeraneio(edicao));
	}

	private List<LocalDate> getDatasPeriodoVeraneio(ProdutoEdicaoEstudo edicao) {
		List<LocalDate> periodoVeraneio = new ArrayList<LocalDate>();
		Date dataLancamento = edicao.getDataLancamento();
		periodoVeraneio.add(parseLocalDate(dataLancamento, Years.ONE, DataReferencia.DEZEMBRO_20));
		periodoVeraneio.add(parseLocalDate(dataLancamento, Years.ZERO, DataReferencia.FEVEREIRO_15));
		periodoVeraneio.add(parseLocalDate(dataLancamento, Years.TWO, DataReferencia.DEZEMBRO_20));
		periodoVeraneio.add(parseLocalDate(dataLancamento, Years.ONE, DataReferencia.FEVEREIRO_15));
		return periodoVeraneio;
	}

	private List<LocalDate> getDatasPeriodoSaidaVeraneio(ProdutoEdicaoEstudo edicao) {
		List<LocalDate> periodoSaidaVeraneio = new ArrayList<LocalDate>();
		Date dataLancamento = edicao.getDataLancamento();
		periodoSaidaVeraneio.add(parseLocalDate(dataLancamento, Years.ONE, DataReferencia.FEVEREIRO_16));
		periodoSaidaVeraneio.add(parseLocalDate(dataLancamento, Years.ONE, DataReferencia.DEZEMBRO_19));
		periodoSaidaVeraneio.add(parseLocalDate(dataLancamento, Years.TWO, DataReferencia.FEVEREIRO_16));
		periodoSaidaVeraneio.add(parseLocalDate(dataLancamento, Years.TWO, DataReferencia.DEZEMBRO_19));
		return periodoSaidaVeraneio;
	}

	private LocalDate parseLocalDate(Date dataLancamento, Years anosSubtrair, DataReferencia dataReferencia) {
		return MonthDay.parse(dataReferencia.getData()).toLocalDate(LocalDate.fromDateFields(dataLancamento).minus(anosSubtrair).getYear());
	}

	public void gravarEstudo(EstudoTransient estudo) {
		estudoDAO.gravarEstudo(estudo);
	}

	public EstudoTransient gerarEstudoAutomatico(ProdutoEdicaoEstudo produto, BigInteger reparte) throws Exception {
		return gerarEstudoAutomatico(null, false, null, null, produto, reparte);
	}

    public EstudoTransient gerarEstudoAutomatico(List<ProdutoEdicaoEstudo> edicoesBase, boolean distribuicaoPorMultiplos, BigInteger _reparteMinimo,
			BigInteger pacotePadrao, ProdutoEdicaoEstudo produto, BigInteger reparte) throws Exception {
		log.debug("Iniciando execução do estudo.");
		EstudoTransient estudo = new EstudoTransient();
		estudo.setDataCadastro(new Date());
		estudo.setStatusEstudo("ESTUDO_FECHADO");
		estudo.setProdutoEdicaoEstudo(produto);
		estudo.setReparteDistribuir(reparte);
		estudo.setReparteDistribuirInicial(reparte);

		estudo.setDistribuicaoPorMultiplos(distribuicaoPorMultiplos ? 1 : 0);
		estudo.setReparteMinimo(_reparteMinimo);
		estudo.setPacotePadrao(pacotePadrao);

		// carregando parâmetros do banco de dados
		carregarParametros(estudo);

		definicaoBases.executar(estudo);

		verificarTotalFixacoes.executar(estudo);

		calculate(estudo);

		for (CotaEstudo cota : estudo.getCotas()) {
			ajusteCota.executar(cota);
			
			correcaoVendas.executar(cota);

			medias.executar(cota);

			vendaMediaFinal.executar(cota);
			
			jornaleirosNovos.executar(cota);
		}
		bonificacoes.executar(estudo);
		
		ajusteReparte.executar(estudo);

		redutorAutomatico.executar(estudo);

		reparteMinimo.executar(estudo);

		reparteProporcional.executar(estudo);

		encalheMaximo.executar(estudo);

		complementarAutomatico.executar(estudo);

		calcularReparte.executar(estudo);

		// processo que faz os ajustes finais e grava as informações no banco de dados
		ajusteFinalReparte.executar(estudo);
		
		log.debug("Execução do estudo concluída");
		return estudo;
	}
}
