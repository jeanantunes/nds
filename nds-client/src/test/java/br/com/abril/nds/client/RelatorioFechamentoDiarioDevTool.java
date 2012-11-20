package br.com.abril.nds.client;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.dto.fechamentodiario.FechamentoDiarioDTO;
import br.com.abril.nds.fixture.DataLoader;
import br.com.abril.nds.service.FecharDiaService;

/**
 * Classe utilitária para desenvolvimento do relatório de fechamento diario
 * 
 * @author francisco.garcia
 *
 */
public class RelatorioFechamentoDiarioDevTool {
    
    private static ClassPathXmlApplicationContext context; 
    
    public static void main(String[] args) throws Exception {
       
        setupEnvironment();
        
        compileReports();

        FechamentoDiarioDTO dto = fecharDia();
        
        gerarRelatorio(dto);
    } 

    private static void gerarRelatorio(FechamentoDiarioDTO dto) throws Exception {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("dataFechamento", dto.getDataFechamento());
       
        runReportPdf(dto, parameters);
    }

    private static FechamentoDiarioDTO fecharDia() {
        FecharDiaService fecharDiaService = context.getBean(FecharDiaService.class);
        
        FechamentoDiarioDTO dto = fecharDiaService.processarFechamentoDoDia(DataLoader.usuarioJoao, DataLoader.distribuidor.getDataOperacao());
        return dto;
    }

    private static void setupEnvironment() {
        DataLoader.main(new String[0]);
        context = new ClassPathXmlApplicationContext("classpath:/applicationContext-test-fechar-dia.xml");
    }

    private static void compileReports() throws URISyntaxException, JRException {
        URL url = Thread.currentThread().getContextClassLoader().getResource("reports/fechamento_diario_sumarizacao.jrxml");
        String path = url.toURI().getPath();
        JasperCompileManager.compileReportToFile(path, "target/test-classes/reports/fechamento_diario_sumarizacao.jasper");
        
        url = Thread.currentThread().getContextClassLoader().getResource("reports/fechamento_diario_sumarizacao_detalhe.jrxml");
        path = url.toURI().getPath();
        JasperCompileManager.compileReportToFile(path, "target/test-classes/reports/fechamento_diario_sumarizacao_detalhe.jasper");
        
        url = Thread.currentThread().getContextClassLoader().getResource("reports/fechamento_diario_sumarizacao_dividas_receber_vencer.jrxml");
        path = url.toURI().getPath();
        JasperCompileManager.compileReportToFile(path, "target/test-classes/reports/fechamento_diario_sumarizacao_dividas_receber_vencer.jasper");
        
        url = Thread.currentThread().getContextClassLoader().getResource("reports/fechamento_diario_lancamento.jrxml");
        path = url.toURI().getPath();
        JasperCompileManager.compileReportToFile(path, "target/test-classes/reports/fechamento_diario_lancamento.jasper");
        
        url = Thread.currentThread().getContextClassLoader().getResource("reports/fechamento_diario_encalhe.jrxml");
        path = url.toURI().getPath();
        JasperCompileManager.compileReportToFile(path, "target/test-classes/reports/fechamento_diario_encalhe.jasper");
        
        url = Thread.currentThread().getContextClassLoader().getResource("reports/fechamento_diario_suplementar.jrxml");
        path = url.toURI().getPath();
        JasperCompileManager.compileReportToFile(path, "target/test-classes/reports/fechamento_diario_suplementar.jasper");
        
        url = Thread.currentThread().getContextClassLoader().getResource("reports/fechamento_diario_faltas_sobras.jrxml");
        path = url.toURI().getPath();
        JasperCompileManager.compileReportToFile(path, "target/test-classes/reports/fechamento_diario_faltas_sobras.jasper");
    }
    
    private static void runReportPdf(FechamentoDiarioDTO dto, Map<String, Object> parameters) throws Exception {
        JasperPrint jpSumarizacao = fillSumarizacao(dto, parameters);
        JasperPrint jpLancamento = fillReparte(dto, parameters);
        JasperPrint jpEncalhe = fillEncalhe(dto, parameters);
        
        URL url = Thread.currentThread().getContextClassLoader().getResource("reports/fechamento_diario_suplementar.jasper");
        String path = url.toURI().getPath();
        JasperPrint jpSuplementar = JasperFillManager.fillReport(path, parameters, new JREmptyDataSource());
        
        url = Thread.currentThread().getContextClassLoader().getResource("reports/fechamento_diario_faltas_sobras.jasper");
        path = url.toURI().getPath();
        JasperPrint jpFaltasSobras = JasperFillManager.fillReport(path, parameters, new JREmptyDataSource());
        
        JRPdfExporter exp = new JRPdfExporter();
        exp.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, Arrays.asList(jpSumarizacao, jpLancamento, jpEncalhe, jpSuplementar, jpFaltasSobras));
        
        exp.setParameter(JRPdfExporterParameter.OUTPUT_FILE, new File("target/fechamentoDiario.pdf"));
        
        exp.exportReport();
    }

    private static JasperPrint fillEncalhe(FechamentoDiarioDTO dto, Map<String, Object> parameters) throws URISyntaxException, JRException {
        URL url = Thread.currentThread().getContextClassLoader().getResource("reports/fechamento_diario_encalhe.jasper");
        String path = url.toURI().getPath();
        JasperPrint jpEncalhe = JasperFillManager.fillReport(path, parameters, new JRBeanCollectionDataSource(dto.getEncalhe()));
        return jpEncalhe;
    }

    private static JasperPrint fillReparte(FechamentoDiarioDTO dto, Map<String, Object> parameters) throws URISyntaxException, JRException {
        URL url = Thread.currentThread().getContextClassLoader().getResource("reports/fechamento_diario_lancamento.jasper");
        String path = url.toURI().getPath();
        JasperPrint jpLancamento = JasperFillManager.fillReport(path, parameters, new JRBeanCollectionDataSource(dto.getReparte()));
        return jpLancamento;
    }

    private static JasperPrint fillSumarizacao(FechamentoDiarioDTO dto, Map<String, Object> parameters) throws URISyntaxException, JRException {
        URL url = Thread.currentThread().getContextClassLoader().getResource("reports/fechamento_diario_sumarizacao.jasper");
        String path = url.toURI().getPath();
       
        Map<String, Object> newParameters = new HashMap<>(parameters);
        newParameters.put("fechamentoDiarioDTO", dto.getSumarizacao());
        JasperPrint jpSumarizacao = JasperFillManager.fillReport(path, newParameters, new JREmptyDataSource());
        return jpSumarizacao;
    }

}
