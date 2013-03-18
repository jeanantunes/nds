package br.com.abril.nds.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.MonthDay;
import org.joda.time.Years;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.dao.DefinicaoBasesDAO;
import br.com.abril.nds.enumerators.DataReferencia;
import br.com.abril.nds.model.ProdutoEdicaoBase;

public class PreparaEstudoService {

	private static final Logger log = LoggerFactory.getLogger(PreparaEstudoService.class);

	private DefinicaoBasesDAO definicaoBasesDAO = new DefinicaoBasesDAO();

	public List<ProdutoEdicaoBase> buscaEdicoesPorLancamento(ProdutoEdicaoBase edicao) {
		log.info("Buscando edições para estudo.");
		return definicaoBasesDAO.listaEdicoesPorLancamento(edicao);
	}

	public List<ProdutoEdicaoBase> buscaEdicoesAnosAnterioresVeraneio(ProdutoEdicaoBase edicao) {
		List<ProdutoEdicaoBase> listaEdicoesAnosAnterioresMesmoMes = definicaoBasesDAO.listaEdicoesAnosAnterioresMesmoMes(edicao);

		if(!listaEdicoesAnosAnterioresMesmoMes.isEmpty()) {
			return listaEdicoesAnosAnterioresMesmoMes;
		}

		return definicaoBasesDAO.listaEdicoesAnosAnterioresVeraneio(edicao, getDatasPeriodoVeraneio(edicao));
	}

	public List<ProdutoEdicaoBase> buscaEdicoesAnosAnterioresSaidaVeraneio(ProdutoEdicaoBase edicao) {
		return definicaoBasesDAO.listaEdicoesAnosAnterioresVeraneio(edicao, getDatasPeriodoSaidaVeraneio(edicao));
	}

	private List<LocalDate> getDatasPeriodoVeraneio(ProdutoEdicaoBase edicao) {
		List<LocalDate> periodoVeraneio = new ArrayList<LocalDate>();
		Date dataLancamento = edicao.getDataLancamento();
		periodoVeraneio.add(parseLocalDate(dataLancamento, Years.ONE, DataReferencia.DEZEMBRO_20));
		periodoVeraneio.add(parseLocalDate(dataLancamento, Years.ZERO, DataReferencia.FEVEREIRO_15));
		periodoVeraneio.add(parseLocalDate(dataLancamento, Years.TWO, DataReferencia.DEZEMBRO_20));
		periodoVeraneio.add(parseLocalDate(dataLancamento, Years.ONE, DataReferencia.FEVEREIRO_15));
		return periodoVeraneio;
	}

	private List<LocalDate> getDatasPeriodoSaidaVeraneio(ProdutoEdicaoBase edicao) {
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
}
