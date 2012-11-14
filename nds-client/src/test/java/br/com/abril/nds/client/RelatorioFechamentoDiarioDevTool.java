package br.com.abril.nds.client;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperRunManager;

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
        JRDataSource dataSource = new JREmptyDataSource();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("dataFechamento", dto.getDataFechamento());
        parameters.put("fechamentoDiarioDTO", dto);
        
        runReportPdf(dataSource, parameters);
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
    }
    
    private static void runReportPdf(JRDataSource dataSource, Map<String, Object> parameters) throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource("reports/fechamento_diario_sumarizacao.jasper");
        String path = url.toURI().getPath();
        JasperRunManager.runReportToPdfFile(path, "target/fechamentoDiario.pdf", parameters, dataSource);
    }

}
