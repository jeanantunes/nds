package br.com.abril.nds.process.verificartotalfixacoes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaDesenglobada;
import br.com.abril.nds.model.estudo.CotaEnglobada;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
import br.com.abril.nds.process.ProcessoAbstrato;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil definido no
 * setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link VerificarTotalFixacoes}
 * 
 * Processo Anterior: N/A Próximo Processo: N/A
 * </p>
 */
@Component
public class SelecaoBancas extends ProcessoAbstrato {

    private static final BigDecimal BIGDECIMAL_100 = BigDecimal.valueOf(100);

    @Autowired
    private CotaDAO cotaDAO;

    @Override
    public void executar(EstudoTransient estudo) throws Exception {
	
	List<CotaEstudo> cotas = cotaDAO.getCotas(estudo);
	List<Map<Long, CotaEstudo>> historico = new ArrayList<>();
	for (ProdutoEdicaoEstudo edicao : estudo.getEdicoesBase()) {
	    historico.add(cotaDAO.getHistoricoCota(edicao));
	}

	boolean existeCotaComHistorico = false;
	for (CotaEstudo cota : cotas) {
	    cota.setEdicoesRecebidas(new ArrayList<ProdutoEdicaoEstudo>());
	    for (Map<Long, CotaEstudo> item : historico) {
		if (item.get(cota.getId()) != null) {
		    cota.getEdicoesRecebidas().addAll(item.get(cota.getId()).getEdicoesRecebidas());
		}
	    }
	    if (cota.getEdicoesRecebidas().size() > 0) {
		existeCotaComHistorico = true;
	    }
	}

	if (!existeCotaComHistorico) {
	    throw new Exception("Não foram encontradas cotas com historico para estas edições de base.");
	}

	Map<Long, CotaEstudo> cotasComHistoricoMap = new LinkedHashMap<>();
	
	for (CotaEstudo cota : cotas) {
	    calcularTotais(cota, estudo);
	    cotasComHistoricoMap.put(cota.getId(), cota);
	}
	tratarCotasComEnglobacao(cotasComHistoricoMap);
	
	List<Long> idsCotas = new ArrayList<>();
	for (CotaEstudo cota : cotasComHistoricoMap.values()) {
	    if (cota.getClassificacao().equals(ClassificacaoCota.BancaSemHistorico)) {
		idsCotas.add(cota.getId());
	    }
	    if (cota.getClassificacao().in(ClassificacaoCota.BancaComVendaZero, ClassificacaoCota.BancaSemHistorico,
		    ClassificacaoCota.BancaSuspensa, ClassificacaoCota.ReparteFixado)) {
		estudo.getCotasExcluidas().add(cota);
	    }
	}
	for (CotaEstudo cota : estudo.getCotasExcluidas()) {
	    cotasComHistoricoMap.remove(cota.getId());
	}
	
	if (idsCotas.size() > 0) {
	    List<Long> numerosEdicao = new ArrayList<>();
	    if (estudo.getProdutoEdicaoEstudo().getNumeroEdicao() > 1) {
		numerosEdicao.add(estudo.getProdutoEdicaoEstudo().getNumeroEdicao() - 1);
	    }
	    if (estudo.getProdutoEdicaoEstudo().getNumeroEdicao() > 2) {
		numerosEdicao.add(estudo.getProdutoEdicaoEstudo().getNumeroEdicao() - 2);
	    }
	    if (numerosEdicao.size() > 0) {
		List<Long> cotasQueReceberam = cotaDAO.buscarCotasQueReceberamUltimaEdicaoAberta(estudo.getProdutoEdicaoEstudo().getProduto().getCodigo(), idsCotas, numerosEdicao);
		for (CotaEstudo cota : estudo.getCotasExcluidas()) {
		    if (cotasQueReceberam.contains(cota.getId())) {
			cota.setRecebeuUltimaEdicaoAberta(true);
		    }
		}
	    }
	}
	estudo.setCotas(new LinkedList<>(cotasComHistoricoMap.values()));
    }

    private void calcularTotais(CotaEstudo cota, EstudoTransient estudo) {
	BigDecimal totalEdicoes = BigDecimal.ZERO;
	BigDecimal totalVenda = BigDecimal.ZERO;
	BigDecimal totalReparte = BigDecimal.ZERO;
	
	// Verificação se a cota só recebeu edições abertas e somatória delas
	cota.setCotaSoRecebeuEdicaoAberta(true);
	cota.setSomaReparteEdicoesAbertas(BigDecimal.ZERO);
	
	for (ProdutoEdicaoEstudo edicao : cota.getEdicoesRecebidas()) {
	    totalEdicoes = totalEdicoes.add(BigDecimal.ONE);
	    totalVenda   = totalVenda.add(edicao.getVenda());
	    totalReparte = totalReparte.add(edicao.getReparte());
	    if (edicao.isEdicaoAberta()) {
		cota.setSomaReparteEdicoesAbertas(cota.getSomaReparteEdicoesAbertas().add(edicao.getReparte()));
	    } else {
		if (cota.getVendaEdicaoMaisRecenteFechada() == null) {
		    cota.setVendaEdicaoMaisRecenteFechada(edicao.getVenda());
		}
		cota.setCotaSoRecebeuEdicaoAberta(false);
	    }
	}
	if (totalEdicoes.compareTo(BigDecimal.ZERO) != 0) {
	    cota.setVendaMediaNominal(totalVenda.divide(totalEdicoes, 2, BigDecimal.ROUND_HALF_UP));
	    cota.setVendaMedia(cota.getVendaMediaNominal());
	}
	if (!cota.getClassificacao().equals(ClassificacaoCota.CotaNova)) {
	    if (totalReparte.compareTo(BigDecimal.ZERO) == 0 && cota.getReparteMinimo().compareTo(BigInteger.ZERO) == 0) {
		cota.setClassificacao(ClassificacaoCota.BancaSemHistorico);
	    } else if (totalVenda.compareTo(BigDecimal.ZERO) == 0 && cota.getReparteMinimo().compareTo(BigInteger.ZERO) == 0) {
		cota.setClassificacao(ClassificacaoCota.BancaComVendaZero);
	    }
	}
	if (cota.getSituacaoCadastro().equals(SituacaoCadastro.SUSPENSO)) {
	    cota.setClassificacao(ClassificacaoCota.BancaSuspensa);
	}
	if (cota.getReparteFixado() != null) {
	    cota.setClassificacao(ClassificacaoCota.ReparteFixado);
	    cota.setReparteCalculado(cota.getReparteFixado(), estudo);
	}
    }

    private void tratarCotasComEnglobacao(Map<Long, CotaEstudo> cotasComHistoricoMap) {

	for (CotaDesenglobada cotaDesenglobada : cotaDAO.buscarCotasDesenglobadas()) {

	    if (cotasComHistoricoMap.containsKey(cotaDesenglobada.getId())) {
		cotasComHistoricoMap.get(cotaDesenglobada.getId()).setClassificacao(ClassificacaoCota.EnglobaDesengloba);

		if (cotasComHistoricoMap.get(cotaDesenglobada.getId()).getEdicoesRecebidas() != null) {
		    for (ProdutoEdicaoEstudo edicaoCotaDesenglobada : cotasComHistoricoMap.get(cotaDesenglobada.getId()).getEdicoesRecebidas()) {
			BigDecimal reparteInicial = edicaoCotaDesenglobada.getReparte();
			BigDecimal vendaInicial = edicaoCotaDesenglobada.getVenda();

			for (CotaEnglobada cotaEnglobada : cotaDesenglobada.getCotasEnglobadas()) {
			    BigDecimal porcentualEnglobacao = BigDecimal.valueOf(cotaEnglobada.getPorcentualEnglobacao()).divide(BIGDECIMAL_100);

			    if (validaEnglobacaoComPeriodoVigente(cotaEnglobada.getDataInclusao())) {
				if (cotasComHistoricoMap.containsKey(cotaEnglobada.getId())) {
				    distribuiEnglobacao(reparteInicial, vendaInicial, porcentualEnglobacao, edicaoCotaDesenglobada,
					    cotasComHistoricoMap.get(cotaEnglobada.getId()));
				} else {
				    CotaEstudo cota = cotaDAO.getCotaById(cotaEnglobada.getId());
				    distribuiEnglobacao(reparteInicial, vendaInicial, porcentualEnglobacao, edicaoCotaDesenglobada, cota);
				    cotasComHistoricoMap.put(cota.getId(), cota);
				}
			    }
			}
		    }
		}
	    }
	}
    }

    private boolean validaEnglobacaoComPeriodoVigente(Date dataInclusao) {
	return LocalDate.fromDateFields(dataInclusao).plus(Years.ONE).isAfter(LocalDate.now());
    }

    private void distribuiEnglobacao(BigDecimal reparteInicial, BigDecimal vendaInicial, BigDecimal porcentualEnglobacao,
	    ProdutoEdicaoEstudo edicaoCotaDesenglobada, CotaEstudo cotaEnglobada) {

	cotaEnglobada.setClassificacao(ClassificacaoCota.EnglobaDesengloba);
	ProdutoEdicaoEstudo edicaoCotaEnglobada = buscaEdicaoPorNumeroLancamento(edicaoCotaDesenglobada, cotaEnglobada.getEdicoesRecebidas());

	if (edicaoCotaDesenglobada != null) {
	    BigDecimal reparteTransferir = reparteInicial.multiply(porcentualEnglobacao);
	    edicaoCotaDesenglobada.setReparte(edicaoCotaDesenglobada.getReparte().subtract(reparteTransferir));
	    edicaoCotaEnglobada.setReparte(edicaoCotaEnglobada.getReparte().add(reparteTransferir));

	    BigDecimal vendaTransferir = vendaInicial.multiply(porcentualEnglobacao);
	    edicaoCotaDesenglobada.setVenda(edicaoCotaDesenglobada.getVenda().subtract(vendaTransferir));
	    edicaoCotaEnglobada.setVenda(edicaoCotaEnglobada.getVenda().add(vendaTransferir));
	}
    }

    private ProdutoEdicaoEstudo buscaEdicaoPorNumeroLancamento(ProdutoEdicaoEstudo edicaoCotaDesenglobada, List<ProdutoEdicaoEstudo> edicoesRecebidas) {
	if (edicoesRecebidas != null) {
	    for (ProdutoEdicaoEstudo produtoEdicao : edicoesRecebidas) {
		if (produtoEdicao.getNumeroEdicao().equals(edicaoCotaDesenglobada.getNumeroEdicao())) {
		    return produtoEdicao;
		}
	    }
	}
	return atualizaListaCotaEnglobadaComEdicaoClonada(edicaoCotaDesenglobada, edicoesRecebidas);
    }

    private ProdutoEdicaoEstudo atualizaListaCotaEnglobadaComEdicaoClonada(ProdutoEdicaoEstudo edicaoCotaDesenglobada,
	    List<ProdutoEdicaoEstudo> edicoesRecebidas) {
	ProdutoEdicaoEstudo produtoEdicao = new ProdutoEdicaoEstudo();
	BeanUtils.copyProperties(edicaoCotaDesenglobada, produtoEdicao);
	produtoEdicao.setReparte(BigDecimal.ZERO);
	produtoEdicao.setVenda(BigDecimal.ZERO);
	return produtoEdicao;
    }
}
