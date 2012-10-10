package br.com.abril.nds.client.report;

import java.util.Collection;
import java.util.Iterator;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import br.com.abril.nds.dto.ImpressaoCEDevolucaoDTO;


/**
 * Data Source para impressão de lote de CE de Devolução
 * 
 * @author francisco.garcia
 * 
 */
public class ImpressaoCECollectionDataSource implements JRDataSource {

    /**
     * Coleção de CE's de Devolução para impressão
     */
    private Collection<ImpressaoCEDevolucaoDTO> cesImpressao;
    
    private Iterator<ImpressaoCEDevolucaoDTO> iterator;
   
    private ImpressaoCEDevolucaoDTO atual;
    
    /**
     * @param cesImpressao Coleção de CE's de Devolução para impressão
     */
    public ImpressaoCECollectionDataSource(
            Collection<ImpressaoCEDevolucaoDTO> cesImpressao) {
        this.cesImpressao = cesImpressao;
        if (this.cesImpressao != null) {
            iterator = this.cesImpressao.iterator();
        }
    }

    @Override
    public boolean next() throws JRException {
        boolean hasNext = false;
        if (this.iterator != null) {
            hasNext = this.iterator.hasNext();
            if (hasNext) {
                this.atual = this.iterator.next();
            }
        }
        return hasNext;
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        if (jrField.getName().equals("ceImpressao")) {
            return new ImpressaoCEDataSource(atual);
        }
        return null;
    }

}
