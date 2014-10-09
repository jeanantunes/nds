package br.com.abril.nds.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CalendarioFeriadoDTO;
import br.com.abril.nds.dto.CalendarioFeriadoWrapper;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.Feriado;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.TipoFeriado;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FeriadoRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.DistribuicaoFornecedorService;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.JasperUtil;
import br.com.abril.nds.util.SemanaUtil;
import br.com.abril.nds.util.export.FileExporter.FileType;

/**
 * Classe de implementação de serviços referentes funcionalidades de calendário.
 * 
 * @author Discover Technology
 */
@Service
public class CalendarioServiceImpl implements CalendarioService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CalendarioServiceImpl.class);
    
    @Autowired
    protected FeriadoRepository feriadoRepository;
    
    @Autowired
    private EnderecoService enderecoService;
    
    @Autowired
    private DistribuidorRepository distribuidorRepository;
    
    @Autowired
    private DistribuicaoFornecedorService distribuicaoFornecedorService;
    
    @Override
    @Transactional(readOnly = true)
    public Date adicionarDiasUteis(final Date data, final int numDias) {
        return this.adicionarDiasUteis(data, numDias, null);
       
    }
    
    @Override
    @Transactional(readOnly = true)
    public Date adicionarDiasUteis(final Date data, final int numDias, String localidade){
        
    	final Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        
        String localidadeDistribuidor = distribuidorRepository.obter().getEnderecoDistribuidor().getEndereco().getCidade();
        
        if (numDias == 0) {
            
            // Verifica se o dia informado é util.
            // Caso não seja, incrementa até encontrar o primeiro dia útil.
            while (DateUtil.isSabadoDomingo(cal) || isFeriado(cal, localidadeDistribuidor)) {
                cal.setTime(DateUtil.adicionarDias(cal.getTime(), 1));
            }
            
        } else {
            
            // Adiciona o número de dias úteis informado.
            for (int i = 0; i < numDias; i++) {
                
                cal.setTime(DateUtil.adicionarDias(cal.getTime(), 1));
                
                while (DateUtil.isSabadoDomingo(cal) || (localidade != null && isFeriado(cal, localidade)) || isFeriado(cal, localidadeDistribuidor)) {
                    cal.setTime(DateUtil.adicionarDias(cal.getTime(), 1));
                }
            }
        }
        
        return cal.getTime();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Date adicionarDiasRetornarDiaUtil(final Date data, final int numDias) {
        
        final Calendar cal = Calendar.getInstance();
        
        if (numDias == 0) {
            
            cal.setTime(this.obterProximaDataDiaUtil(data));
        } else {
            
            cal.setTime(DateUtil.adicionarDias(data, numDias));
        }
        
        return cal.getTime();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Date subtrairDiasUteis(final Date data, final int numDias) {
        
        final Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        
        for (int i = 0; i < numDias; i++) {
            
            cal.setTime(DateUtil.subtrairDias(cal.getTime(), 1));
            
            while (DateUtil.isSabadoDomingo(cal) || isFeriado(cal, null)) {
                cal.setTime(DateUtil.subtrairDias(cal.getTime(), 1));
            }
        }
        
        return cal.getTime();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Date subtrairDiasUteisComOperacao(final Date data, final int numDias) {
        
        final Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        
        for (int i = 0; i < numDias; i++) {
            
            cal.setTime(DateUtil.subtrairDias(cal.getTime(), 1));
            
            while (DateUtil.isSabadoDomingo(cal) || isFeriadoSemOperacao(cal.getTime())
                    || isFeriadoMunicipalSemOperacao(cal.getTime())) {
                cal.setTime(DateUtil.subtrairDias(cal.getTime(), 1));
            }
        }
        
        return cal.getTime();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isDiaOperante(final Date data, final Long idFornecedor, final OperacaoDistribuidor operacaoDistribuidor) {
        
        final Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        
        final List<Integer> dias = distribuicaoFornecedorService.obterCodigosDiaDistribuicaoFornecedor(idFornecedor, operacaoDistribuidor);
        
        final int codigoDiaCorrente = cal.get(Calendar.DAY_OF_WEEK);
        
        if (dias.contains(codigoDiaCorrente)) {
        
        	final String localidadeDistribuidor = distribuidorRepository.cidadeDistribuidor();
            
        	return feriadoRepository.isNaoOpera(data, localidadeDistribuidor);
        	
        }
        
        return false;
    }
    
    @Override
    @Transactional
    public boolean isDiaUtil(final Date data) {
        
        if (data == null) {
            
            return false;
        }
        
        final Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        
        return !(DateUtil.isSabadoDomingo(cal) || isFeriado(cal, null));
    }

    @Override
    @Transactional
    public boolean isFeriado(final Date data) {
        
        if (data == null) {
            
            return false;
        }
        
        final Calendar cal = Calendar.getInstance();
        
        cal.setTime(data);
        
        return isFeriado(cal, null);
    }

    protected boolean isFeriado(final Calendar cal) {
        return isFeriado(cal, null);
    }

    protected boolean isFeriado(final Calendar cal, String localidade) {
        
        if (cal != null) {
            if(localidade == null) {
                return feriadoRepository.isFeriado(cal.getTime());
            } else {
                return feriadoRepository.isFeriado(cal.getTime(), localidade);
            }
        }
        
        return false;
    }
    
    private void tratarTipoFeriado(final CalendarioFeriadoDTO calendarioFeriado) {
        
        final TipoFeriado tipoFeriado = calendarioFeriado.getTipoFeriado();
        
        if (TipoFeriado.FEDERAL.equals(tipoFeriado)) {
            calendarioFeriado.setUfSigla(null);
            calendarioFeriado.setLocalidade(null);
        }
        
        if (TipoFeriado.ESTADUAL.equals(tipoFeriado)) {
            calendarioFeriado.setLocalidade(null);
        }
        
        if (TipoFeriado.MUNICIPAL.equals(tipoFeriado)) {
            calendarioFeriado.setUfSigla(null);
        }
        
    }
    
    /**
     * Cadastra ou atualiza registro de feriado.
     * 
     * @param feriadoDTO
     */
    @Override
    @Transactional
    public void cadastrarFeriado(final CalendarioFeriadoDTO feriadoDTO) {
        
        tratarTipoFeriado(feriadoDTO);
        
        final Date data = feriadoDTO.getDataFeriado();
        final String descricao = feriadoDTO.getDescricaoFeriado();
        final TipoFeriado tipoFeriado = feriadoDTO.getTipoFeriado();
        final boolean indOpera = feriadoDTO.isIndOpera();
        final boolean indRepeteAnualmente = feriadoDTO.isIndRepeteAnualmente();
        final boolean indEfetuaCobranca = feriadoDTO.isIndEfetuaCobranca();
        final String localidade = feriadoDTO.getLocalidade();
        
        Feriado feriado = null;
        String uf = null;
        
        if (TipoFeriado.ESTADUAL.equals(tipoFeriado)) {
            uf = obterUfDistribuidor();
        }
        
        if (feriadoDTO.getIdFeriado() != null) {
            
            feriado = feriadoRepository.buscarPorId(feriadoDTO.getIdFeriado());
            
            if (Origem.INTERFACE.equals(feriado.getOrigem())
                    && !feriado.getDescricao().equals(feriadoDTO.getDescricaoFeriado())) {
                
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Não é permitido alterar descrição de feriado da Interface.");
                
            }
            
            feriado.setDescricao(descricao);
            feriado.setTipoFeriado(tipoFeriado);
            feriado.setLocalidade(localidade);
            feriado.setIndEfetuaCobranca(indEfetuaCobranca);
            feriado.setIndOpera(indOpera);
            feriado.setIndRepeteAnualmente(indRepeteAnualmente);
            verificarFeriadoAnualExistente(feriado);
            
            feriadoRepository.alterar(feriado);
            
        } else {
            
            feriado = new Feriado();
            
            feriado.setData(data);
            feriado.setDescricao(descricao);
            
            feriado.setIndEfetuaCobranca(indEfetuaCobranca);
            feriado.setIndOpera(indOpera);
            feriado.setIndRepeteAnualmente(indRepeteAnualmente);
            
            feriado.setLocalidade(localidade);
            feriado.setTipoFeriado(tipoFeriado);
            feriado.setUnidadeFederacao(uf);
            
            feriado.setOrigem(Origem.MANUAL);
            verificarFeriadoAnualExistente(feriado);
            feriadoRepository.adicionar(feriado);
        }
        
    }
    
    /**
     * Verifica se já existe um feriado anual cadastrado com os mesmo dia e nmês
     * e tipo do feriado recebido como parâmetro
     * 
     * @param feriado feriado para verificação de feriado com repetição anual já
     *            cadastrado com as caracteristicas do feriado recebido
     * 
     * @throws ValidacaoException caso já exista um feriado com repetição anual
     *             com o mesmo tipo dia e mês já cadastrado
     */
    private void verificarFeriadoAnualExistente(final Feriado feriado) {
        
        final Date data = feriado.getData();
        
        final TipoFeriado tipoFeriado = feriado.getTipoFeriado();
        
        Feriado existente;
        
        if (tipoFeriado.equals(TipoFeriado.MUNICIPAL)) {
            existente = feriadoRepository.obterFeriadoAnualLocalidade(data, feriado.getLocalidade());
            
        } else {
            existente = feriadoRepository.obterFeriadoAnualTipo(data, tipoFeriado);
        }
        
        if (existente != null && !feriado.equals(existente)) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Feriado anual com o tipo " + tipoFeriado
                    + " já cadastrado para a data " + DateUtil.formatarDataPTBR(data));
        }
    }
    
    @Override
    @Transactional
    public void excluirFeriado(final Long idFeriado) {
        
        final Feriado feriado = feriadoRepository.buscarPorId(idFeriado);
        
        this.validarExclusaoFeriado(feriado);
        
        feriadoRepository.remover(feriado);
        
    }
    
    private void validarExclusaoFeriado(final Feriado feriado) {
        
        if (feriado == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum feriado encontrado");
        }
        
        if (Origem.CARGA_INICIAL.equals(feriado.getOrigem())) {
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Feriados cadastrados automaticamente não podem ser excluidos!");
        }
    }
    
    private String obterUfDistribuidor() {
        
        final EnderecoDistribuidor endDistribuidor = distribuidorRepository.obterEnderecoPrincipal();
        
        if (endDistribuidor == null || endDistribuidor.getEndereco() == null
                || endDistribuidor.getEndereco().getUf() == null || endDistribuidor.getEndereco().getUf().isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Uf não foi encontrada");
            
        }
        
        return endDistribuidor.getEndereco().getUf();
        
    }
    
    @Override
    @Transactional
    public List<CalendarioFeriadoDTO> obterListaCalendarioFeriadoDataEspecifica(final Date dataFeriado) {
        
        final Calendar dataPesquisa = Calendar.getInstance();
        dataPesquisa.setTime(dataFeriado);
        final int ano = dataPesquisa.get(Calendar.YEAR);
        
        final List<CalendarioFeriadoDTO> listaCalendarioFeriado = feriadoRepository
                .obterListaCalendarioFeriadoDataEspecifica(dataFeriado);
        
        if (listaCalendarioFeriado != null && !listaCalendarioFeriado.isEmpty()) {
            for (final CalendarioFeriadoDTO feriado : listaCalendarioFeriado) {
                feriado.setDataFeriado(obterDataComAnoPesquisa(feriado.getDataFeriado(), ano));
            }
        }
        
        return listaCalendarioFeriado;
        
    }
    
    @Override
    @Transactional
    public List<CalendarioFeriadoDTO> obterListaCalendarioFeriadoMensal(final int mes, final int ano) {
        
        final List<CalendarioFeriadoDTO> listaFeriado = feriadoRepository.obterListaCalendarioFeriadoMensal(mes, ano);
        
        if (listaFeriado == null || listaFeriado.isEmpty()) {
            return listaFeriado;
        }
        
        for (final CalendarioFeriadoDTO feriado : listaFeriado) {
            feriado.setDataFeriado(obterDataComAnoPesquisa(feriado.getDataFeriado(), ano));
        }
        
        return listaFeriado;
        
    }
    
    private Date obterDataComAnoPesquisa(final Date data, final int anoPesquisa) {
        
        if (data == null) {
            return null;
        }
        
        final Calendar novaData = Calendar.getInstance();
        
        novaData.setTime(data);
        
        novaData.set(Calendar.YEAR, anoPesquisa);
        
        return novaData.getTime();
        
    }
    
    public void validarAlteracaoFeriado(final Feriado feriado) {
        
        if (Origem.INTERFACE.equals(feriado.getOrigem())) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Dados do feriado não podem ser alterados");
            
        }
        
    }
    
    @Override
    @Transactional
    public Map<Date, String> obterListaDataFeriado(final int anoVigencia) {
        
        final Calendar calendarInicial = Calendar.getInstance();
        calendarInicial.clear();
        calendarInicial.set(anoVigencia, Calendar.JANUARY, 1);
        
        final Calendar calendarFinal = Calendar.getInstance();
        calendarFinal.clear();
        calendarFinal.set(anoVigencia, Calendar.DECEMBER, 31);
        
        final Date dataInicial = calendarInicial.getTime();
        
        final Date dataFinal = calendarFinal.getTime();
        
        final List<CalendarioFeriadoDTO> listaDataFeriado = feriadoRepository.obterListaCalendarioFeriadoPeriodo(dataInicial,
                dataFinal);
        
        final Map<Date, String> mapaFeriado = new HashMap<Date, String>();
        
        final Calendar calendarNovo = Calendar.getInstance();
        final Calendar calendarIterado = Calendar.getInstance();
        
        for (final CalendarioFeriadoDTO calendario : listaDataFeriado) {
            
            calendarIterado.clear();
            calendarIterado.setTime(calendario.getDataFeriado());
            
            calendarNovo.clear();
            calendarNovo.set(anoVigencia, calendarIterado.get(Calendar.MONTH),
                    calendarIterado.get(Calendar.DAY_OF_MONTH));
            
            mapaFeriado.put(calendarNovo.getTime(), calendario.getDescricaoFeriado());
            
        }
        
        return mapaFeriado;
        
    }
    
    protected URL obterDiretorioReports() {
        
        final URL urlDanfe = Thread.currentThread().getContextClassLoader().getResource("reports/");
        
        return urlDanfe;
    }
    
    private byte[] gerarDocumentoExcel(final List<CalendarioFeriadoWrapper> listaCalendarioFeriadoWrapper, final int anoFeriado,
            final InputStream logoDistribuidor) throws URISyntaxException, JRException {
        
        final JRDataSource jrDataSource = new JRBeanCollectionDataSource(listaCalendarioFeriadoWrapper);
        
        final URL diretorioReports = obterDiretorioReports();
        
        final String path = diretorioReports.toURI().getPath() + "/relatorio_calendario_feriado.jasper";
        
        final String nomeDistribuidor = distribuidorRepository.obterRazaoSocialDistribuidor();
        
        final Map<String, Object> parameters = new HashMap<String, Object>();
        
        parameters.put("SUBREPORT_DIR", diretorioReports.toURI().getPath());
        parameters.put("ANO_FERIADO", String.valueOf(anoFeriado));
        parameters.put("LOGO", JasperUtil.getImagemRelatorio(logoDistribuidor));
        parameters.put("NOME_DISTRIBUIDOR", nomeDistribuidor);
        
        final JasperPrint jasperPrint = JasperFillManager.fillReport(path, parameters, jrDataSource);
        
        final JRXlsExporter exporter = new JRXlsExporter();
        
        final ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
        
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);
        
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "relatorio_cadastro_feriado.xls");
        
        exporter.exportReport();
        
        return xlsReport.toByteArray();
        
    }
    
    private byte[] gerarDocumentoPDF(final List<CalendarioFeriadoWrapper> listaCalendarioFeriadoWrapper, final int anoFeriado,
            final InputStream logoDistribuidor) throws JRException, URISyntaxException {
        
        final JRDataSource jrDataSource = new JRBeanCollectionDataSource(listaCalendarioFeriadoWrapper);
        
        final URL diretorioReports = obterDiretorioReports();
        
        final String path = diretorioReports.toURI().getPath() + "/relatorio_calendario_feriado.jasper";
        
        final String nomeDistribuidor = distribuidorRepository.obterRazaoSocialDistribuidor();
        
        final Map<String, Object> parameters = new HashMap<String, Object>();
        
        parameters.put("SUBREPORT_DIR", diretorioReports.toURI().getPath());
        parameters.put("ANO_FERIADO", String.valueOf(anoFeriado));
        parameters.put("LOGO", JasperUtil.getImagemRelatorio(logoDistribuidor));
        parameters.put("NOME_DISTRIBUIDOR", nomeDistribuidor);
        
        return JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
    }
    
    private List<CalendarioFeriadoWrapper> obterListaCalendarioFeriadoWrapper(final TipoPesquisaFeriado tipoPesquisaFeriado,
            final int mes, final int ano) {
        
        List<CalendarioFeriadoDTO> listaCalendarioFeriado = null;
        
        if (TipoPesquisaFeriado.FERIADO_ANUAL.equals(tipoPesquisaFeriado)) {
            
            listaCalendarioFeriado = obterFeriadosPorAno(ano);
            
            if (listaCalendarioFeriado != null && !listaCalendarioFeriado.isEmpty()) {
                for (final CalendarioFeriadoDTO feriado : listaCalendarioFeriado) {
                    feriado.setDataFeriado(obterDataComAnoPesquisa(feriado.getDataFeriado(), ano));
                }
                // Ordena pela data do feriado
                Collections.sort(listaCalendarioFeriado);
            }
            
        }
        
        if (TipoPesquisaFeriado.FERIADO_MENSAL.equals(tipoPesquisaFeriado)) {
            
            listaCalendarioFeriado = feriadoRepository.obterListaCalendarioFeriadoMensal(mes, ano);
            
            if (!listaCalendarioFeriado.isEmpty()) {
                for (final CalendarioFeriadoDTO feriado : listaCalendarioFeriado) {
                    feriado.setDataFeriado(obterDataComAnoPesquisa(feriado.getDataFeriado(), ano));
                }
            }
            
        }
        
        final List<CalendarioFeriadoWrapper> listaCalendarioFeriadoWrapper = new LinkedList<CalendarioFeriadoWrapper>();
        
        final Map<Integer, List<CalendarioFeriadoDTO>> mapaFeriadosPorMes = new LinkedHashMap<Integer, List<CalendarioFeriadoDTO>>();
        
        final Calendar novaData = Calendar.getInstance();
        
        for (final CalendarioFeriadoDTO calendario : listaCalendarioFeriado) {
            
            final Date dataFeriado = calendario.getDataFeriado();
            
            novaData.setTime(dataFeriado);
            
            final int mesFeriado = novaData.get(Calendar.MONTH);
            
            final int diaSemana = novaData.get(Calendar.DAY_OF_WEEK);
            
            calendario.setDiaSemana(SemanaUtil.obterDiaSemana(diaSemana));
            
            if (mapaFeriadosPorMes.get(mesFeriado) != null) {
                
                mapaFeriadosPorMes.get(mesFeriado).add(calendario);
                
            } else {
                
                mapaFeriadosPorMes.put(mesFeriado, new LinkedList<CalendarioFeriadoDTO>());
                
                mapaFeriadosPorMes.get(mesFeriado).add(calendario);
                
            }
            
        }
        
        for (final Entry<Integer, List<CalendarioFeriadoDTO>> entry : mapaFeriadosPorMes.entrySet()) {
            
            final String descricaoMes = DateUtil.obterDecricaoMes(entry.getKey());
            
            final CalendarioFeriadoWrapper cFeriadoWrapper = new CalendarioFeriadoWrapper();
            
            cFeriadoWrapper.setDescricaoMes(descricaoMes);
            
            cFeriadoWrapper.setListaCalendarioFeriado(entry.getValue());
            
            listaCalendarioFeriadoWrapper.add(cFeriadoWrapper);
            
        }
        
        return listaCalendarioFeriadoWrapper;
    }
    
    @Override
    @Transactional
    public List<CalendarioFeriadoDTO> obterFeriadosPorAno(final int ano) {
        
        List<CalendarioFeriadoDTO> listaCalendarioFeriado;
        
        final Calendar calendarInicial = Calendar.getInstance();
        calendarInicial.clear();
        calendarInicial.set(ano, Calendar.JANUARY, 1);
        
        final Calendar calendarFinal = Calendar.getInstance();
        calendarFinal.clear();
        calendarFinal.set(ano, Calendar.DECEMBER, 31);
        
        final Date dataInicial = calendarInicial.getTime();
        
        final Date dataFinal = calendarFinal.getTime();
        
        listaCalendarioFeriado = feriadoRepository.obterListaCalendarioFeriadoPeriodo(dataInicial, dataFinal);
        
        return listaCalendarioFeriado;
    }
    
    @Override
    @Transactional
    public byte[] obterRelatorioCalendarioFeriado(final FileType fileType, final TipoPesquisaFeriado tipoPesquisaFeriado, final int mes,
            final int ano, final InputStream logoDistribuidor) {
        
        try {
            
            if (FileType.PDF.equals(fileType)) {
                
                return gerarDocumentoPDF(obterListaCalendarioFeriadoWrapper(tipoPesquisaFeriado, mes, ano), ano,
                        logoDistribuidor);
                
            } else if (FileType.XLS.equals(fileType)) {
                
                return gerarDocumentoExcel(obterListaCalendarioFeriadoWrapper(tipoPesquisaFeriado, mes, ano), ano,
                        logoDistribuidor);
                
            }
            
            return null;
            
        } catch (final Exception e) {
            LOGGER.error("Falha ao gerar relatorio de feriados", e);
            throw new ValidacaoException(TipoMensagem.WARNING, "Falha ao gerar relatorio de feriados");
            
        }
        
    }
    
    @Override
    @Transactional
    public List<String> obterListaLocalidadePdv() {
        
        return enderecoService.obterListaLocalidadePdv();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isFeriadoSemOperacao(final Date data) {
        
        if (data == null) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Data inválida!");
        }
        
        return feriadoRepository.isNaoOpera(data);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isFeriadoComOperacao(final Date data) {
        
        if (data == null) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Data inválida!");
        }
        
        return feriadoRepository.isOpera(data);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isFeriadoMunicipalSemOperacao(final Date data) {
        
        if (data == null) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Data inválida!");
        }
        
       final String localidadeDistribuidor = distribuidorRepository.cidadeDistribuidor();
        
       return feriadoRepository.isNaoOpera(data, localidadeDistribuidor);
        
    }
    
    /**
     * Obtem a proxima data com dia util, considerando Feriados, Sabados e
     * Domingos
     * 
     * @param data
     * @return Date
     */
    @Override
    @Transactional
    public Date obterProximaDataDiaUtil(Date data) {
        
        final Calendar c = Calendar.getInstance();
        
        c.setTime(data);
        
        if (feriadoRepository.isFeriado(data) || DateUtil.isSabadoDomingo(c)) {
            
            data = this.obterProximaDataDiaUtil(DateUtil.adicionarDias(data, 1));
        }
        
        return data;
    }
}
