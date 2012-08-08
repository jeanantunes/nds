package br.com.abril.nds.service.impl;

import java.io.ByteArrayOutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CalendarioFeriadoDTO;
import br.com.abril.nds.dto.CalendarioFeriadoWrapper;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.Feriado;
import br.com.abril.nds.model.cadastro.TipoFeriado;
import br.com.abril.nds.model.dne.Localidade;
import br.com.abril.nds.model.dne.UnidadeFederacao;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FeriadoRepository;
import br.com.abril.nds.repository.LocalidadeRepository;
import br.com.abril.nds.repository.UnidadeFederacaoRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter.FileType;

/**
 * Classe de implementação de serviços referentes funcionalidades de calendário.
 * 
 * @author Discover Technology
 */
@Service
public class CalendarioServiceImpl implements CalendarioService {

	@Autowired
	protected FeriadoRepository feriadoRepository;

	@Autowired
	private LocalidadeRepository localidadeRepository;

	@Autowired
	private UnidadeFederacaoRepository unidadeFederacaoRepository;

	@Autowired
	private DistribuidorRepository distribuidorRepository;

	@Override
	@Transactional(readOnly = true)
	public Date adicionarDiasUteis(Date data, int numDias) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(data);

		if (numDias == 0) {

			// Verifica se o dia informado é util.
			// Caso não seja, incrementa até encontrar o primeiro dia útil.
			while (DateUtil.isSabadoDomingo(cal) || isFeriado(cal)) {
				cal.setTime(DateUtil.adicionarDias(cal.getTime(), 1));
			}

		} else {

			// Adiciona o número de dias úteis informado.
			for (int i = 0; i < numDias; i++) {

				cal.setTime(DateUtil.adicionarDias(cal.getTime(), 1));

				while (DateUtil.isSabadoDomingo(cal) || isFeriado(cal)) {
					cal.setTime(DateUtil.adicionarDias(cal.getTime(), 1));
				}
			}
		}

		return cal.getTime();
	}

	@Override
	@Transactional(readOnly = true)
	public Date adicionarDiasRetornarDiaUtil(Date data, int numDias) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(data);

		if (numDias == 0) {

			// Verifica se o dia informado é util.
			// Caso não seja, incrementa até encontrar o primeiro dia útil.
			while (DateUtil.isSabadoDomingo(cal) || isFeriado(cal)) {
				cal.setTime(DateUtil.adicionarDias(cal.getTime(), 1));
			}

		} else {

			// Adiciona o número de dias úteis informado.
			for (int i = 0; i < numDias; i++) {

				cal.setTime(DateUtil.adicionarDias(cal.getTime(), 1));
			}
		}

		return cal.getTime();
	}

	@Override
	@Transactional(readOnly = true)
	public Date subtrairDiasUteis(Date data, int numDias) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(data);

		for (int i = 0; i < numDias; i++) {

			cal.setTime(DateUtil.subtrairDias(cal.getTime(), 1));

			while (DateUtil.isSabadoDomingo(cal) || isFeriado(cal)) {
				cal.setTime(DateUtil.subtrairDias(cal.getTime(), 1));
			}
		}

		return cal.getTime();
	}

	@Transactional
	public boolean isDiaUtil(Date data) {

		if (data == null) {

			return false;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(data);

		return !(DateUtil.isSabadoDomingo(cal) || isFeriado(cal));
	}

	@Override
	public Date adicionarDiasUteis(Date data, int numDias,
			List<Integer> diasSemanaConcentracaoCobranca,
			Integer diaMesConcentracaoCobranca) {

		if (diasSemanaConcentracaoCobranca == null
				|| diasSemanaConcentracaoCobranca.isEmpty()
				&& (diasSemanaConcentracaoCobranca == null)) {

			return this.adicionarDiasUteis(data, numDias);
		}

		if (diasSemanaConcentracaoCobranca != null
				&& !diasSemanaConcentracaoCobranca.isEmpty()) {

			Calendar dataBase = Calendar.getInstance();
			dataBase.setTime(data);
			dataBase.add(Calendar.DAY_OF_MONTH, numDias);

			boolean dataValida = false;

			while (!dataValida) {
				while (!diasSemanaConcentracaoCobranca.contains(dataBase
						.get(Calendar.DAY_OF_WEEK))) {
					dataBase.add(Calendar.DAY_OF_MONTH, 1);
				}

				dataBase.setTime(this.adicionarDiasUteis(dataBase.getTime(), 0));

				dataValida = diasSemanaConcentracaoCobranca.contains(dataBase
						.get(Calendar.DAY_OF_WEEK));
			}

			return dataBase.getTime();
		} else if (diaMesConcentracaoCobranca != null) {

			if (Calendar.getInstance().getLeastMaximum(Calendar.DAY_OF_MONTH) > diaMesConcentracaoCobranca) {

				diaMesConcentracaoCobranca = Calendar.getInstance()
						.getLeastMaximum(Calendar.DAY_OF_MONTH);
			}

			Calendar dataVencimento = Calendar.getInstance();

			while (dataVencimento.get(Calendar.DAY_OF_MONTH) < diaMesConcentracaoCobranca) {

				dataVencimento.setTime(this.adicionarDiasUteis(
						dataVencimento.getTime(), 1));
			}

			return dataVencimento.getTime();
		}

		return Calendar.getInstance().getTime();
	}

	private boolean isFeriado(Calendar cal) {

		Feriado feriado = null;

		if (cal != null) {

			List<Feriado> feriados = feriadoRepository.obterFeriados(
					cal.getTime(), TipoFeriado.FEDERAL, null, null);

			if (feriados == null || feriados.isEmpty()) {
				return false;
			}

			feriado = feriados.get(0);
		}

		return (feriado != null) ? true : false;
	}

	private void tratarTipoFeriado(CalendarioFeriadoDTO calendarioFeriado) {

		TipoFeriado tipoFeriado = calendarioFeriado.getTipoFeriado();

		if (TipoFeriado.FEDERAL.equals(tipoFeriado)) {
			calendarioFeriado.setUfSigla(null);
			calendarioFeriado.setIdLocalidade(null);
		}

		if (TipoFeriado.ESTADUAL.equals(tipoFeriado)) {
			calendarioFeriado.setIdLocalidade(null);
		}

		if (TipoFeriado.MUNICIPAL.equals(tipoFeriado)) {
			calendarioFeriado.setUfSigla(null);
		}

	}

	/**
	 * Cadastra ou atualiza registro de feriado.
	 * 
	 * @param calendarioFeriado
	 */
	@Transactional
	public void cadastrarFeriado(CalendarioFeriadoDTO calendarioFeriado) {

		tratarTipoFeriado(calendarioFeriado);

		Date data = calendarioFeriado.getDataFeriado();
		String descricao = calendarioFeriado.getDescricaoFeriado();
		TipoFeriado tipoFeriado = calendarioFeriado.getTipoFeriado();
		boolean indOpera = calendarioFeriado.isIndOpera();
		boolean indRepeteAnualmente = calendarioFeriado.isIndRepeteAnualmente();
		boolean indEfetuaCobranca = calendarioFeriado.isIndEfetuaCobranca();
		Long idLocalidade = calendarioFeriado.getIdLocalidade();

		Feriado feriado = null;
		UnidadeFederacao unidadeFederacao = null;

		if (calendarioFeriado.isIndRepeteAnualmente()) {
			feriado = feriadoRepository
					.obterFeriadoAnualTipo(data, tipoFeriado);
		} else {

			String uf = null;

			if (TipoFeriado.ESTADUAL.equals(tipoFeriado)) {
				unidadeFederacao = obterUfDistribuidor();
				uf = unidadeFederacao.getSigla();
			}

			List<Feriado> listaFeriado = feriadoRepository.obterFeriados(data,
					tipoFeriado, uf, idLocalidade);

			if (listaFeriado != null && !listaFeriado.isEmpty()) {
				feriado = listaFeriado.get(0);
			}
		}
		
		if (feriado != null) {

			if (Origem.INTERFACE.equals(feriado.getOrigem())
					&& !feriado.getDescricao().equals(descricao)) {

				throw new ValidacaoException(TipoMensagem.WARNING,
						"Não é permitido alterar descrição de feriado da Interface.");

			}

			feriado.setDescricao(descricao);
			feriado.setIndEfetuaCobranca(indEfetuaCobranca);
			feriado.setIndOpera(indOpera);
			feriado.setIndRepeteAnualmente(indRepeteAnualmente);
			verificarFeriadoAnualExistente(feriado);
			feriadoRepository.alterar(feriado);

		} else {

			Localidade localidade = null;

			if (idLocalidade != null) {

				localidade = localidadeRepository.buscarPorId(idLocalidade);

				if (localidade == null) {
					throw new ValidacaoException(TipoMensagem.WARNING,
							"Localidade não foi encontrada");
				}

			}

			feriado = new Feriado();

			feriado.setData(data);
			feriado.setDescricao(descricao);

			feriado.setIndEfetuaCobranca(indEfetuaCobranca);
			feriado.setIndOpera(indOpera);
			feriado.setIndRepeteAnualmente(indRepeteAnualmente);

			feriado.setLocalidade(localidade);
			feriado.setTipoFeriado(tipoFeriado);
			feriado.setUnidadeFederacao(unidadeFederacao);

			feriado.setOrigem(Origem.MANUAL);
			verificarFeriadoAnualExistente(feriado);
			feriadoRepository.adicionar(feriado);
		}

	}
	
	/**
	 * Verifica se já existe um feriado anual cadastrado com 
	 * os mesmo dia e nmês e tipo do feriado recebido como parâmetro
	 * @param feriado feriado para verificação de feriado com repetição anual 
	 * já cadastrado com as caracteristicas do feriado recebido
	 * @throws ValidacaoException caso já exista um feriado com repetição
	 * anual com o mesmo tipo dia e mês já cadastrado 
	 */
	private void verificarFeriadoAnualExistente(Feriado feriado) {
		Date data = feriado.getData();
		TipoFeriado tipoFeriado = feriado.getTipoFeriado();
		Feriado existente = feriadoRepository.obterFeriadoAnualTipo(data, tipoFeriado);
		if (existente != null && !feriado.equals(existente)) {
			throw new ValidacaoException(TipoMensagem.WARNING,
					"Feriado anual com o tipo " + tipoFeriado
					+ " já cadastrado para a data " + DateUtil.formatarDataPTBR(data));
		}
	}

	@Transactional
	public void excluirFeriado(CalendarioFeriadoDTO calendarioFeriado) {

		Feriado feriado = validarExclusaoFeriado(calendarioFeriado);

		feriadoRepository.remover(feriado);

	}

	private Feriado validarExclusaoFeriado(
			CalendarioFeriadoDTO calendarioFeriado) {

		Date dataFeriado = calendarioFeriado.getDataFeriado();
		TipoFeriado tipoFeriado = calendarioFeriado.getTipoFeriado();
		Long idLocalidade = calendarioFeriado.getIdLocalidade();

		Feriado feriado = null;

		if (calendarioFeriado.isIndRepeteAnualmente()) {
			feriado = feriadoRepository.obterFeriadoAnualTipo(dataFeriado,
					tipoFeriado);
		} else {
			String uf = null;

			if (TipoFeriado.ESTADUAL.equals(tipoFeriado)) {
				UnidadeFederacao unidadeFederacao = obterUfDistribuidor();
				uf = unidadeFederacao.getSigla();
			}

			List<Feriado> feriados = feriadoRepository.obterFeriados(
					dataFeriado, tipoFeriado, uf, idLocalidade);

			if (feriados == null || feriados.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,
						"Nenhum feriado encontrado");
			}

			feriado = feriados.get(0);
		}
		if (Origem.INTERFACE.equals(feriado.getOrigem())) {
			throw new ValidacaoException(TipoMensagem.WARNING,
					"Feriado não pode ser excluido.");
		}

		return feriado;
	}

	private UnidadeFederacao obterUfDistribuidor() {

		EnderecoDistribuidor endDistribuidor = distribuidorRepository
				.obterEnderecoPrincipal();

		if (endDistribuidor == null || endDistribuidor.getEndereco() == null
				|| endDistribuidor.getEndereco().getUf() == null
				|| endDistribuidor.getEndereco().getUf().isEmpty()) {

			throw new ValidacaoException(TipoMensagem.WARNING,
					"Uf não foi encontrada");

		}

		UnidadeFederacao unidadeFederacao = unidadeFederacaoRepository
				.buscarPorId(endDistribuidor.getEndereco().getUf());

		if (unidadeFederacao == null) {
			throw new ValidacaoException(TipoMensagem.WARNING,
					"Uf não foi encontrada");
		}

		return unidadeFederacao;

	}

	@Transactional
	public List<CalendarioFeriadoDTO> obterListaCalendarioFeriadoDataEspecifica(
			Date dataFeriado) {

		Calendar dataPesquisa = Calendar.getInstance();
		dataPesquisa.setTime(dataFeriado);
		int ano = dataPesquisa.get(Calendar.YEAR);

		List<CalendarioFeriadoDTO> listaCalendarioFeriado = feriadoRepository
				.obterListaCalendarioFeriadoDataEspecifica(dataFeriado);

		if (listaCalendarioFeriado != null && !listaCalendarioFeriado.isEmpty()) {
			for (CalendarioFeriadoDTO feriado : listaCalendarioFeriado) {
				feriado.setDataFeriado(obterDataComAnoPesquisa(
						feriado.getDataFeriado(), ano));
			}
		}

		return listaCalendarioFeriado;

	}

	@Transactional
	public List<CalendarioFeriadoDTO> obterListaCalendarioFeriadoMensal(
			int mes, int ano) {

		List<CalendarioFeriadoDTO> listaFeriado = feriadoRepository
				.obterListaCalendarioFeriadoMensal(mes, ano);

		if (listaFeriado == null || listaFeriado.isEmpty()) {
			return listaFeriado;
		}

		for (CalendarioFeriadoDTO feriado : listaFeriado) {
			feriado.setDataFeriado(obterDataComAnoPesquisa(
					feriado.getDataFeriado(), ano));
		}

		return listaFeriado;

	}

	private Date obterDataComAnoPesquisa(Date data, int anoPesquisa) {

		if (data == null) {
			return null;
		}

		Calendar novaData = Calendar.getInstance();

		novaData.setTime(data);

		novaData.set(Calendar.YEAR, anoPesquisa);

		return novaData.getTime();

	}

	public void validarAlteracaoFeriado(Feriado feriado) {

		if (Origem.INTERFACE.equals(feriado.getOrigem())) {

			throw new ValidacaoException(TipoMensagem.WARNING,
					"Dados do feriado não podem ser alterados");

		}

	}

	@Transactional
	public Map<Date, String> obterListaDataFeriado(int anoVigencia) {

		Calendar calendarInicial = Calendar.getInstance();
		calendarInicial.clear();
		calendarInicial.set(anoVigencia, Calendar.JANUARY, 1);

		Calendar calendarFinal = Calendar.getInstance();
		calendarFinal.clear();
		calendarFinal.set(anoVigencia, Calendar.DECEMBER, 31);

		Date dataInicial = calendarInicial.getTime();

		Date dataFinal = calendarFinal.getTime();

		List<CalendarioFeriadoDTO> listaDataFeriado = feriadoRepository
				.obterListaCalendarioFeriadoPeriodo(dataInicial, dataFinal);

		Map<Date, String> mapaFeriado = new HashMap<Date, String>();

		Calendar calendarNovo = Calendar.getInstance();
		Calendar calendarIterado = Calendar.getInstance();

		for (CalendarioFeriadoDTO calendario : listaDataFeriado) {

			calendarIterado.clear();
			calendarIterado.setTime(calendario.getDataFeriado());

			calendarNovo.clear();
			calendarNovo.set(anoVigencia, calendarIterado.get(Calendar.MONTH),
					calendarIterado.get(Calendar.DAY_OF_MONTH));

			mapaFeriado.put(calendarNovo.getTime(),
					calendario.getDescricaoFeriado());

		}

		return mapaFeriado;

	}

	protected URL obterDiretorioReports() {

		URL urlDanfe = Thread.currentThread().getContextClassLoader()
				.getResource("/reports/");

		return urlDanfe;
	}

	private byte[] gerarDocumentoExcel(
			List<CalendarioFeriadoWrapper> listaCalendarioFeriadoWrapper,
			int anoFeriado) throws URISyntaxException, JRException {

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(
				listaCalendarioFeriadoWrapper);

		URL diretorioReports = obterDiretorioReports();

		String path = diretorioReports.toURI().getPath()
				+ "/relatorio_calendario_feriado.jasper";

		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("SUBREPORT_DIR", diretorioReports.toURI().getPath());
		parameters.put("ANO_FERIADO", String.valueOf(anoFeriado));

		JasperPrint jasperPrint = JasperFillManager.fillReport(path,
				parameters, jrDataSource);

		JRXlsExporter exporter = new JRXlsExporter();

		ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();

		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);
		// exporter.setParameter(JRExporterParameter.OUTPUT_FILE, "C:JSP");
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
				"relatorio_cadastro_feriado.xls");

		exporter.exportReport();

		return xlsReport.toByteArray();

	}

	private byte[] gerarDocumentoPDF(
			List<CalendarioFeriadoWrapper> listaCalendarioFeriadoWrapper,
			int anoFeriado) throws JRException, URISyntaxException {

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(
				listaCalendarioFeriadoWrapper);

		URL diretorioReports = obterDiretorioReports();

		String path = diretorioReports.toURI().getPath()
				+ "/relatorio_calendario_feriado.jasper";

		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("SUBREPORT_DIR", diretorioReports.toURI().getPath());
		parameters.put("ANO_FERIADO", String.valueOf(anoFeriado));

		return JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
	}

	private List<CalendarioFeriadoWrapper> obterListaCalendarioFeriadoWrapper(
			TipoPesquisaFeriado tipoPesquisaFeriado, int mes, int ano) {

		List<CalendarioFeriadoDTO> listaCalendarioFeriado = null;

		if (TipoPesquisaFeriado.FERIADO_ANUAL.equals(tipoPesquisaFeriado)) {

			Calendar calendarInicial = Calendar.getInstance();
			calendarInicial.clear();
			calendarInicial.set(ano, Calendar.JANUARY, 1);

			Calendar calendarFinal = Calendar.getInstance();
			calendarFinal.clear();
			calendarFinal.set(ano, Calendar.DECEMBER, 31);

			Date dataInicial = calendarInicial.getTime();

			Date dataFinal = calendarFinal.getTime();

			listaCalendarioFeriado = feriadoRepository
					.obterListaCalendarioFeriadoPeriodo(dataInicial, dataFinal);

			if (listaCalendarioFeriado != null
					&& !listaCalendarioFeriado.isEmpty()) {
				for (CalendarioFeriadoDTO feriado : listaCalendarioFeriado) {
					feriado.setDataFeriado(obterDataComAnoPesquisa(
							feriado.getDataFeriado(), ano));
				}
			}

		}

		if (TipoPesquisaFeriado.FERIADO_MENSAL.equals(tipoPesquisaFeriado)) {

			listaCalendarioFeriado = feriadoRepository
					.obterListaCalendarioFeriadoMensal(mes, ano);

			if (listaCalendarioFeriado != null
					&& !listaCalendarioFeriado.isEmpty()) {
				for (CalendarioFeriadoDTO feriado : listaCalendarioFeriado) {
					feriado.setDataFeriado(obterDataComAnoPesquisa(
							feriado.getDataFeriado(), ano));
				}
			}

		}

		List<CalendarioFeriadoWrapper> listaCalendarioFeriadoWrapper = new LinkedList<CalendarioFeriadoWrapper>();

		Map<Integer, List<CalendarioFeriadoDTO>> mapaFeriadosPorMes = new LinkedHashMap<Integer, List<CalendarioFeriadoDTO>>();

		Calendar novaData = Calendar.getInstance();

		for (CalendarioFeriadoDTO calendario : listaCalendarioFeriado) {

			Date dataFeriado = calendario.getDataFeriado();

			novaData.setTime(dataFeriado);

			int mesFeriado = novaData.get(Calendar.MONTH);

			int diaSemana = novaData.get(Calendar.DAY_OF_WEEK);

			calendario.setDiaSemana(DateUtil.obterDiaSemana(diaSemana));

			if (mapaFeriadosPorMes.get(mesFeriado) != null) {

				mapaFeriadosPorMes.get(mesFeriado).add(calendario);

			} else {

				mapaFeriadosPorMes.put(mesFeriado,
						new LinkedList<CalendarioFeriadoDTO>());

				mapaFeriadosPorMes.get(mesFeriado).add(calendario);

			}

		}

		for (Integer codigoMes : mapaFeriadosPorMes.keySet()) {

			String descricaoMes = DateUtil.obterDecricaoMes(codigoMes);

			CalendarioFeriadoWrapper cFeriadoWrapper = new CalendarioFeriadoWrapper();

			cFeriadoWrapper.setDescricaoMes(descricaoMes);

			cFeriadoWrapper.setListaCalendarioFeriado(mapaFeriadosPorMes
					.get(codigoMes));

			listaCalendarioFeriadoWrapper.add(cFeriadoWrapper);

		}

		return listaCalendarioFeriadoWrapper;
	}

	@Transactional
	public byte[] obterRelatorioCalendarioFeriado(FileType fileType,
			TipoPesquisaFeriado tipoPesquisaFeriado, int mes, int ano) {

		try {

			if (FileType.PDF.equals(fileType)) {

				return gerarDocumentoPDF(
						obterListaCalendarioFeriadoWrapper(tipoPesquisaFeriado,
								mes, ano), ano);

			} else if (FileType.XLS.equals(fileType)) {

				return gerarDocumentoExcel(
						obterListaCalendarioFeriadoWrapper(tipoPesquisaFeriado,
								mes, ano), ano);

			}

			return null;

		} catch (Exception e) {

			throw new ValidacaoException(TipoMensagem.WARNING,
					"Falha ao gerar relatorio de feriados");

		}

	}

	@Transactional
	public List<Localidade> obterListaLocalidadeCotas() {

		return localidadeRepository.obterListaLocalidadeCotas();

	}

}
