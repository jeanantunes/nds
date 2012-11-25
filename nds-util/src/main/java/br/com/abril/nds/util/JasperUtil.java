package br.com.abril.nds.util;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe utilitária para manipulação de relatórios JasperReports
 * 
 * @author francisco.garcia
 * 
 */
public class JasperUtil {

    /**
     * Caminho padrão para os relatórios
     */
    public static final String REPORTS_PATH = "/reports/%s";
    
    private static final Logger LOG = LoggerFactory.getLogger(JasperUtil.class);
    
    private JasperUtil() {
    }
    
    /**
     * Método utilitário para exportação de relatório para PDF
     * 
     * @param reportName
     *            nome do relatório para exportação
     * @param dataSource
     *            data source do relatório
     * @param parameters
     *            parâmetros do relatório
     * @return byte[] com o conteúdo do relatório em PDF
     */
    public static byte[] runReportPdf(String reportName, JRDataSource dataSource, Map<String, Object> parameters) {
        String reportPath = getReportAbsolutePath(reportName);
        try {
            return JasperRunManager.runReportToPdf(reportPath, parameters, dataSource);
        } catch (JRException ex) {
            String msg = "Erro gerando PDF do relatório [" + reportName + "]";
            LOG.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }
    }

    
    /**
     * Método utilitário para exportação de relatório para PDF
     * 
     * @param reportName
     *            nome do relatório para exportação
     * @param parameters
     *            parâmetros do relatório
     * @return byte[] com o conteúdo do relatório em PDF
     */
    public static byte[] runReportPdf(String reportName, Map<String, Object> parameters) {
        return runReportPdf(reportName, new JREmptyDataSource(), parameters);
    }
    
    /**
     * Método utilitário para preenchimento do relatório
     * 
     * @param reportName
     *            nome do relatório para preenchimento
     * @param parameters
     *            parâmetros do relatório
     * @param dataSource
     *            data source para preenchimento do relatório
     * 
     * @return relatório preenchido para exportação
     * @throws RuntimeException
     *             caso ocorra algum erro no preenchimento do relatório
     */
    public static JasperPrint fillReport(String reportName, Map<String, Object> parameters, JRDataSource dataSource) {
        String reportPath = getReportAbsolutePath(reportName);
        try {
            return JasperFillManager.fillReport(reportPath, parameters, dataSource);
        } catch (JRException ex) {
            String msg = "Erro preenchendo relatório [" + reportName + "]";
            LOG.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }
    }
    
    /**
     * Retorna o path do relatório de acordo com o padrão de diretório
     * utilizado para armazenamento do relatórios 
     * @param reportName nome do relatório para composição do path
     * @return path completo do relatório
     */
    public static String getReportPath(String reportName) {
        Objects.requireNonNull(reportName, "Nome do relatório não deve ser nulo!");
        return String.format(REPORTS_PATH, reportName);
    }
    
    /**
     * Recupera o caminho abssoluto do relatório
     * 
     * @param reportName
     *            nome do relatório para composição do caminho absoluto
     * @return caminho absoluto do relatório
     * @throws RuntimeException
     *             caso ocorra algum erro ao tentar recuperar o caminho absoluto
     *             do relatório
     */
    private static String getReportAbsolutePath(String reportName) {
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(getReportPath(reportName));
            return url.toURI().getPath();
        } catch (URISyntaxException ex) {
            String msg = "Erro recuperando caminho absoluto do relatório [" + reportName + "]";
            LOG.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }

    }

}