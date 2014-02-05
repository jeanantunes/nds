package br.com.abril.nds.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
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
        
        if (numDias == 0) {
            
            cal.setTime(this.obterProximaDataDiaUtil(data));
        } else {
            
            cal.setTime(DateUtil.adicionarDias(data, numDias));
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
    
    @Override
    @Transactional(readOnly = true)
    public Date subtrairDiasUteisComOperacao(Date data, int numDias) {
        
        Calendar cal = Calendar.getInstance();
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
    public boolean isDiaOperante(Date data, Long idFornecedor, OperacaoDistribuidor operacaoDistribuidor) {
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        
        List<Integer> dias = this.distribuicaoFornecedorService
                .obterCodigosDiaDistribuicaoFornecedor(idFornecedor, operacaoDistribuidor);
        
        int codigoDiaCorrente = cal.get(Calendar.DAY_OF_WEEK);
        
        if (dias.contains(codigoDiaCorrente)) {
            return !this.feriadoRepository.isNaoOpera(data);
        }
        return false;
        
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
    
    protected boolean isFeriado(Calendar cal) {
        
        if (cal != null) {
            
            return this.feriadoRepository.isFeriado(cal.getTime());
        }
        
        return false;
    }
    
    private void tratarTipoFeriado(CalendarioFeriadoDTO calendarioFeriado) {
        
        TipoFeriado tipoFeriado = calendarioFeriado.getTipoFeriado();
        
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
    @Transactional
    public void cadastrarFeriado(CalendarioFeriadoDTO feriadoDTO) {
        
        tratarTipoFeriado(feriadoDTO);
        
        Date data = feriadoDTO.getDataFeriado();
        String descricao = feriadoDTO.getDescricaoFeriado();
        TipoFeriado tipoFeriado = feriadoDTO.getTipoFeriado();
        boolean indOpera = feriadoDTO.isIndOpera();
        boolean indRepeteAnualmente = feriadoDTO.isIndRepeteAnualmente();
        boolean indEfetuaCobranca = feriadoDTO.isIndEfetuaCobranca();
        String localidade = feriadoDTO.getLocalidade();
        
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
    private void verificarFeriadoAnualExistente(Feriado feriado) {
        
        Date data = feriado.getData();
        
        TipoFeriado tipoFeriado = feriado.getTipoFeriado();
        
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
    
    @Transactional
    public void excluirFeriado(Long idFeriado) {
        
        Feriado feriado = this.feriadoRepository.buscarPorId(idFeriado);
        
        this.validarExclusaoFeriado(feriado);
        
        feriadoRepository.remover(feriado);
        
    }
    
    private void validarExclusaoFeriado(Feriado feriado) {
        
        if (feriado == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum feriado encontrado");
        }
        
        if (Origem.CARGA_INICIAL.equals(feriado.getOrigem())) {
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Feriados cadastrados automaticamente não podem ser excluidos!");
        }
    }
    
    private String obterUfDistribuidor() {
        
        EnderecoDistribuidor endDistribuidor = distribuidorRepository.obterEnderecoPrincipal();
        
        if (endDistribuidor == null || endDistribuidor.getEndereco() == null
                || endDistribuidor.getEndereco().getUf() == null || endDistribuidor.getEndereco().getUf().isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Uf não foi encontrada");
            
        }
        
        return endDistribuidor.getEndereco().getUf();
        
    }
    
    @Transactional
    public List<CalendarioFeriadoDTO> obterListaCalendarioFeriadoDataEspecifica(Date dataFeriado) {
        
        Calendar dataPesquisa = Calendar.getInstance();
        dataPesquisa.setTime(dataFeriado);
        int ano = dataPesquisa.get(Calendar.YEAR);
        
        List<CalendarioFeriadoDTO> listaCalendarioFeriado = feriadoRepository
                .obterListaCalendarioFeriadoDataEspecifica(dataFeriado);
        
        if (listaCalendarioFeriado != null && !listaCalendarioFeriado.isEmpty()) {
            for (CalendarioFeriadoDTO feriado : listaCalendarioFeriado) {
                feriado.setDataFeriado(obterDataComAnoPesquisa(feriado.getDataFeriado(), ano));
            }
        }
        
        return listaCalendarioFeriado;
        
    }
    
    @Transactional
    public List<CalendarioFeriadoDTO> obterListaCalendarioFeriadoMensal(int mes, int ano) {
        
        List<CalendarioFeriadoDTO> listaFeriado = feriadoRepository.obterListaCalendarioFeriadoMensal(mes, ano);
        
        if (listaFeriado == null || listaFeriado.isEmpty()) {
            return listaFeriado;
        }
        
        for (CalendarioFeriadoDTO feriado : listaFeriado) {
            feriado.setDataFeriado(obterDataComAnoPesquisa(feriado.getDataFeriado(), ano));
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
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Dados do feriado não podem ser alterados");
            
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
        
        List<CalendarioFeriadoDTO> listaDataFeriado = feriadoRepository.obterListaCalendarioFeriadoPeriodo(dataInicial,
                dataFinal);
        
        Map<Date, String> mapaFeriado = new HashMap<Date, String>();
        
        Calendar calendarNovo = Calendar.getInstance();
        Calendar calendarIterado = Calendar.getInstance();
        
        for (CalendarioFeriadoDTO calendario : listaDataFeriado) {
            
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
        
        URL urlDanfe = Thread.currentThread().getContextClassLoader().getResource("reports/");
        
        return urlDanfe;
    }
    
    private byte[] gerarDocumentoExcel(List<CalendarioFeriadoWrapper> listaCalendarioFeriadoWrapper, int anoFeriado,
            InputStream logoDistribuidor) throws URISyntaxException, JRException {
        
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(listaCalendarioFeriadoWrapper);
        
        URL diretorioReports = obterDiretorioReports();
        
        String path = diretorioReports.toURI().getPath() + "/relatorio_calendario_feriado.jasper";
        
        String nomeDistribuidor = this.distribuidorRepository.obterRazaoSocialDistribuidor();
        
        Map<String, Object> parameters = new HashMap<String, Object>();
        
        parameters.put("SUBREPORT_DIR", diretorioReports.toURI().getPath());
        parameters.put("ANO_FERIADO", String.valueOf(anoFeriado));
        parameters.put("LOGO", JasperUtil.getImagemRelatorio(logoDistribuidor));
        parameters.put("NOME_DISTRIBUIDOR", nomeDistribuidor);
        
        JasperPrint jasperPrint = JasperFillManager.fillReport(path, parameters, jrDataSource);
        
        JRXlsExporter exporter = new JRXlsExporter();
        
        ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
        
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);
        // exporter.setParameter(JRExporterParameter.OUTPUT_FILE, "C:JSP");
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "relatorio_cadastro_feriado.xls");
        
        exporter.exportReport();
        
        return xlsReport.toByteArray();
        
    }
    
    private byte[] gerarDocumentoPDF(List<CalendarioFeriadoWrapper> listaCalendarioFeriadoWrapper, int anoFeriado,
            InputStream logoDistribuidor) throws JRException, URISyntaxException {
        
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(listaCalendarioFeriadoWrapper);
        
        URL diretorioReports = obterDiretorioReports();
        
        String path = diretorioReports.toURI().getPath() + "/relatorio_calendario_feriado.jasper";
        
        String nomeDistribuidor = this.distribuidorRepository.obterRazaoSocialDistribuidor();
        
        Map<String, Object> parameters = new HashMap<String, Object>();
        
        parameters.put("SUBREPORT_DIR", diretorioReports.toURI().getPath());
        parameters.put("ANO_FERIADO", String.valueOf(anoFeriado));
        parameters.put("LOGO", JasperUtil.getImagemRelatorio(logoDistribuidor));
        parameters.put("NOME_DISTRIBUIDOR", nomeDistribuidor);
        
        return JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
    }
    
    private List<CalendarioFeriadoWrapper> obterListaCalendarioFeriadoWrapper(TipoPesquisaFeriado tipoPesquisaFeriado,
            int mes, int ano) {
        
        List<CalendarioFeriadoDTO> listaCalendarioFeriado = null;
        
        if (TipoPesquisaFeriado.FERIADO_ANUAL.equals(tipoPesquisaFeriado)) {
            
            listaCalendarioFeriado = obterFeriadosPorAno(ano);
            
            if (listaCalendarioFeriado != null && !listaCalendarioFeriado.isEmpty()) {
                for (CalendarioFeriadoDTO feriado : listaCalendarioFeriado) {
                    feriado.setDataFeriado(obterDataComAnoPesquisa(feriado.getDataFeriado(), ano));
                }
                // Ordena pela data do feriado
                Collections.sort(listaCalendarioFeriado);
            }
            
        }
        
        if (TipoPesquisaFeriado.FERIADO_MENSAL.equals(tipoPesquisaFeriado)) {
            
            listaCalendarioFeriado = feriadoRepository.obterListaCalendarioFeriadoMensal(mes, ano);
            
            if (listaCalendarioFeriado != null && !listaCalendarioFeriado.isEmpty()) {
                for (CalendarioFeriadoDTO feriado : listaCalendarioFeriado) {
                    feriado.setDataFeriado(obterDataComAnoPesquisa(feriado.getDataFeriado(), ano));
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
            
            calendario.setDiaSemana(SemanaUtil.obterDiaSemana(diaSemana));
            
            if (mapaFeriadosPorMes.get(mesFeriado) != null) {
                
                mapaFeriadosPorMes.get(mesFeriado).add(calendario);
                
            } else {
                
                mapaFeriadosPorMes.put(mesFeriado, new LinkedList<CalendarioFeriadoDTO>());
                
                mapaFeriadosPorMes.get(mesFeriado).add(calendario);
                
            }
            
        }
        
        for (Entry<Integer, List<CalendarioFeriadoDTO>> entry : mapaFeriadosPorMes.entrySet()) {
            
            String descricaoMes = DateUtil.obterDecricaoMes(entry.getKey());
            
            CalendarioFeriadoWrapper cFeriadoWrapper = new CalendarioFeriadoWrapper();
            
            cFeriadoWrapper.setDescricaoMes(descricaoMes);
            
            cFeriadoWrapper.setListaCalendarioFeriado(entry.getValue());
            
            listaCalendarioFeriadoWrapper.add(cFeriadoWrapper);
            
        }
        
        return listaCalendarioFeriadoWrapper;
    }
    
    @Transactional
    public List<CalendarioFeriadoDTO> obterFeriadosPorAno(int ano) {
        
        List<CalendarioFeriadoDTO> listaCalendarioFeriado;
        
        Calendar calendarInicial = Calendar.getInstance();
        calendarInicial.clear();
        calendarInicial.set(ano, Calendar.JANUARY, 1);
        
        Calendar calendarFinal = Calendar.getInstance();
        calendarFinal.clear();
        calendarFinal.set(ano, Calendar.DECEMBER, 31);
        
        Date dataInicial = calendarInicial.getTime();
        
        Date dataFinal = calendarFinal.getTime();
        
        listaCalendarioFeriado = feriadoRepository.obterListaCalendarioFeriadoPeriodo(dataInicial, dataFinal);
        
        return listaCalendarioFeriado;
    }
    
    @Transactional
    public byte[] obterRelatorioCalendarioFeriado(FileType fileType, TipoPesquisaFeriado tipoPesquisaFeriado, int mes,
            int ano, InputStream logoDistribuidor) {
        
        try {
            
            if (FileType.PDF.equals(fileType)) {
                
                return gerarDocumentoPDF(obterListaCalendarioFeriadoWrapper(tipoPesquisaFeriado, mes, ano), ano,
                        logoDistribuidor);
                
            } else if (FileType.XLS.equals(fileType)) {
                
                return gerarDocumentoExcel(obterListaCalendarioFeriadoWrapper(tipoPesquisaFeriado, mes, ano), ano,
                        logoDistribuidor);
                
            }
            
            return null;
            
        } catch (Exception e) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Falha ao gerar relatorio de feriados");
            
        }
        
    }
    
    @Transactional
    public List<String> obterListaLocalidadePdv() {
        
        return enderecoService.obterListaLocalidadePdv();
        
    }
    
    @Transactional(readOnly = true)
    public boolean isFeriadoSemOperacao(Date data) {
        
        if (data == null) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Data inválida!");
        }
        
        List<TipoFeriado> tiposFeriado = Arrays.asList(TipoFeriado.ESTADUAL, TipoFeriado.FEDERAL);
        
        List<Feriado> feriados = this.feriadoRepository.obterFeriados(data, tiposFeriado, false);
        
        return !feriados.isEmpty();
    }
    
    @Transactional(readOnly = true)
    public boolean isFeriadoMunicipalSemOperacao(Date data) {
        
        if (data == null) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Data inválida!");
        }
        
        boolean feriadoMunicipalSemOperacao = false;
        
        List<TipoFeriado> tiposFeriado = Arrays.asList(TipoFeriado.MUNICIPAL);
        
        List<Feriado> feriados = this.feriadoRepository.obterFeriados(data, tiposFeriado, false);
        
        if (!feriados.isEmpty()) {
            
            String localidadeDistribuidor = this.distribuidorRepository.cidadeDistribuidor();
            
            for (Feriado feriado : feriados) {
                
                if (localidadeDistribuidor != null && feriado.getLocalidade() != null
                        && feriado.getLocalidade().toUpperCase().equals(localidadeDistribuidor.toUpperCase())) {
                    
                    feriadoMunicipalSemOperacao = true;
                    
                    break;
                }
            }
        }
        return feriadoMunicipalSemOperacao;
    }
    
    /**
     * Obtem a proxima data com dia util, considerando Feriados, Sabados e
     * Domingos
     * 
     * @param data
     * @return Date
     */
    @Override
    public Date obterProximaDataDiaUtil(Date data) {
        
        Calendar c = Calendar.getInstance();
        
        c.setTime(data);
        
        if (this.feriadoRepository.isFeriado(data) || DateUtil.isSabadoDomingo(c)) {
            
            data = this.obterProximaDataDiaUtil(DateUtil.adicionarDias(data, 1));
        }
        
        return data;
    }
}
