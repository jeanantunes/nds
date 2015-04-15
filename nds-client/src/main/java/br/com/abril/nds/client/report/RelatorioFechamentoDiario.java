package br.com.abril.nds.client.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.dto.fechamentodiario.FechamentoDiarioDTO;
import br.com.abril.nds.util.JasperUtil;

/**
 * Classe utilitária que abstrai a complexidade na geração do relatório de
 * fechamento diário, composto por vários relatórios
 * 
 * @author francisco.garcia
 * 
 */
public class RelatorioFechamentoDiario {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RelatorioFechamentoDiario.class);
   
    /**
     * Nome do parâmetro de data de fechamento
     */
    private static final String PARAMETRO_DATA_FECHAMENTO = "dataFechamento";
    
    /**
     * Nome do parâmetro de logo do relatorio
     */
    private static final String PARAMETRO_LOGO_RELATORIO = "logoRelatorio";

    /**
     * Nome do parâmetro do DTO de fechamento diário
     */
    private static final String PARAMETRO_FECHAMENTO_DIARIO_DTO = "fechamentoDiarioDTO";

    private RelatorioFechamentoDiario() {
    }
    
    /**
     * Exporta o relatório em formato PDF
     * 
     * @param dto dto com as informações do relatório
     * @return byte[] com o relatório exportado
     */
    public static byte[] exportPdf(FechamentoDiarioDTO dto) {
        List<JasperPrint> toPrint = new ArrayList<JasperPrint>(Relatorio.values().length);
        
        for (Relatorio relatorio : Relatorio.values()) {
            toPrint.add(JasperUtil.fillReport(relatorio.getReportName(), relatorio.processParameters(dto), relatorio.createDataSource(dto)));
        }
        
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, toPrint);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, baos);
        
        try {
            exporter.exportReport();
            return baos.toByteArray();
        } catch (JRException ex) {
            String msg = "Erro exportando relatório de Fechamento Diário!";
            LOGGER.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }
        
    }

    /**
     * Enumeração que mantém os relatórios que compõe o relatório de fcehamento
     * diário A ordem dos elementos da enumeração é utilizada como ordem na
     * criação e composição do relatório final
     * 
     * @author francisco.garcia
     * 
     */
    private static enum Relatorio {
        
        SUMARIZACAO("fechamento_diario_sumarizacao.jasper") {
            @Override
            Map<String, Object> processParameters(FechamentoDiarioDTO dto) {
                Map<String, Object> parameters = getDefaultParameters(dto);
                parameters.put(PARAMETRO_FECHAMENTO_DIARIO_DTO, dto.getSumarizacao());
                return parameters;
            }

            @Override
            JRDataSource createDataSource(FechamentoDiarioDTO dto) {
                return new JREmptyDataSource();
            }
        },
        
        LANCAMENTO("fechamento_diario_lancamento.jasper") {
            @Override
            Map<String, Object> processParameters(FechamentoDiarioDTO dto) {
                Map<String, Object> parameters = getDefaultParameters(dto);
                return parameters;
            }

            @Override
            JRDataSource createDataSource(FechamentoDiarioDTO dto) {
                return new JRBeanCollectionDataSource(dto.getReparte());
            }
        }, 
        
        ENCALHE("fechamento_diario_encalhe.jasper") {
            @Override
            Map<String, Object> processParameters(FechamentoDiarioDTO dto) {
                Map<String, Object> parameters = getDefaultParameters(dto);
                return parameters;
            }

            @Override
            JRDataSource createDataSource(FechamentoDiarioDTO dto) {
                return new JRBeanCollectionDataSource(dto.getEncalhe());
            }
        },
        
        SUPLEMENTAR("fechamento_diario_suplementar.jasper") {
            @Override
            Map<String, Object> processParameters(FechamentoDiarioDTO dto) {
                Map<String, Object> parameters = getDefaultParameters(dto);
                return parameters;
            }

            @Override
            JRDataSource createDataSource(FechamentoDiarioDTO dto) {
                return new JRBeanCollectionDataSource(dto.getSuplementar());
            }
        },
        
        FALTAS_SOBRAS("fechamento_diario_faltas_sobras.jasper") {
            @Override
            Map<String, Object> processParameters(FechamentoDiarioDTO dto) {
                Map<String, Object> parameters = getDefaultParameters(dto);
                return parameters;
            }

            @Override
            JRDataSource createDataSource(FechamentoDiarioDTO dto) {
                return new JRBeanCollectionDataSource(dto.getFaltasSobras());
            }
        };
        
        private String reportName;
        
        private Relatorio(String reportName) {
            this.reportName = reportName;
        }
        
        public String getReportName() {
            return reportName;
        }
        
        abstract Map<String, Object> processParameters(FechamentoDiarioDTO dto);

        abstract JRDataSource createDataSource(FechamentoDiarioDTO dto);
        
        protected Map<String, Object> getDefaultParameters(FechamentoDiarioDTO dto) {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put(PARAMETRO_DATA_FECHAMENTO, dto.getDataFechamento());
            parameters.put(PARAMETRO_LOGO_RELATORIO, dto.getImagemLogoDistribuidor());
            return parameters;
        }
             
    }

}