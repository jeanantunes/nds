package br.com.abril.nds.util;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
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
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(String.format(REPORTS_PATH, reportName));
            String path = url.toURI().getPath();
            return JasperRunManager.runReportToPdf(path, parameters, dataSource);
        } catch (URISyntaxException | JRException ex) {
            LOG.error("Erro gerando PDF do relatório [" + reportName + "]", ex);
            throw new RuntimeException("Erro gerando PDF do relatório [" + reportName + "]", ex);
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
    

}