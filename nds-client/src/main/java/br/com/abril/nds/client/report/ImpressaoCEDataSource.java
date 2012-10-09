package br.com.abril.nds.client.report;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import br.com.abril.nds.dto.EnderecoDTO;
import br.com.abril.nds.dto.IdentificacaoImpressaoCEDevolucaoDTO;
import br.com.abril.nds.dto.ImpressaoCEDevolucaoDTO;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Util;

/**
 * Data Source para impressão da CE de Devolução
 * 
 * @author francisco.garcia
 *
 */
public class ImpressaoCEDataSource implements JRDataSource {
    
    private static final String FORMATO_ENDERECO_DISTRIBUIDOR = "%s %s, %s"; 
    
    private static final String FORMATO_ENDERECO_FORNECEDOR = "%s %s"; 

    /**
     * CE de Devolução para impressão
     */
    private ImpressaoCEDevolucaoDTO ceDevolucao;
    
    public boolean hasNext;

    /**
     * @param ceDevolucao CE de Devolução com as informações
     * para geração do relatório
     */
    public ImpressaoCEDataSource(ImpressaoCEDevolucaoDTO ceDevolucao) {
        this.ceDevolucao = ceDevolucao;
        this.hasNext = true;
    }

    @Override
    public boolean next() throws JRException {
        boolean next = this.hasNext;
        if (hasNext) {
            hasNext = false;
        }
        return next;
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        IdentificacaoImpressaoCEDevolucaoDTO distribuidor = ceDevolucao
                .getDistribuidor();
        EnderecoDTO enderecoDistribuidor = distribuidor.getEndereco();
        IdentificacaoImpressaoCEDevolucaoDTO fornecedor = ceDevolucao
                .getFornecedor();
        EnderecoDTO enderecoFornecedor = fornecedor.getEndereco();
        if (jrField.getName().equals("numero")) {
            return ceDevolucao.getNumero();
        } else if (jrField.getName().equals("nomeDistribuidor")) {
            return distribuidor.getNome();
        } else if (jrField.getName().equals("enderecoDistribuidor")) {
            return String.format(FORMATO_ENDERECO_DISTRIBUIDOR,
                    enderecoDistribuidor.getTipoLogradouro(),
                    enderecoDistribuidor.getLogradouro(),
                    enderecoDistribuidor.getNumero());
        } else if (jrField.getName().equals("numeroDistribuidor")) {
            return Util.nvl(enderecoDistribuidor.getNumero(), "");
        } else if (jrField.getName().equals("cidadeDistribuidor")) {
            return enderecoDistribuidor.getCidade();
        } else if (jrField.getName().equals("ufDistribuidor")) {
            return enderecoDistribuidor.getUf();
        } else if (jrField.getName().equals("cepDistribuidor")) {
            return enderecoDistribuidor.getCep();
        } else if (jrField.getName().equals("cnpjDistribuidor")) {
            return distribuidor.getCnpj();
        } else if (jrField.getName().equals("inscricaoEstadualDistribuidor")) {
            return Util.nvl(distribuidor.getInscricaoEstadual(), "");
        } else if (jrField.getName().equals("nomeFornecedor")) {
            return fornecedor.getNome();
        } else if (jrField.getName().equals("cnpjFornecedor")) {
            return fornecedor.getCnpj();
        } else if (jrField.getName().equals("enderecoFornecedor")) {
            return String.format(FORMATO_ENDERECO_FORNECEDOR,
                    enderecoFornecedor.getTipoLogradouro(),
                    enderecoFornecedor.getLogradouro());
        } else if (jrField.getName().equals("numeroFornecedor")) {
            return Util.nvl(enderecoFornecedor.getNumero(), "");
        } else if (jrField.getName().equals("cidadeFornecedor")) {
            return enderecoFornecedor.getCidade();
        } else if (jrField.getName().equals("ufFornecedor")) {
            return enderecoFornecedor.getUf();
        } else if (jrField.getName().equals("cepFornecedor")) {
            return enderecoFornecedor.getCep();
        } else if (jrField.getName().equals("inscricaoEstadualFornecedor")) {
            return fornecedor.getInscricaoEstadual();
        } else if (jrField.getName().equals("dataRecolhimento")) {
            return DateUtil.formatarDataPTBR(ceDevolucao.getDataRecolhimento());
        } else if (jrField.getName().equals("dataEmissao")) {
            return DateUtil.formatarDataPTBR(ceDevolucao.getDataEmissao());
        } else if (jrField.getName().equals("totalBruto")) {
            return CurrencyUtil.formatarValor(ceDevolucao.getTotalBruto());
        } else if (jrField.getName().equals("totalDesconto")) {
            return CurrencyUtil.formatarValor(ceDevolucao.getTotalDesconto());
        } else if (jrField.getName().equals("totalLiquido")) {
            return CurrencyUtil.formatarValor(ceDevolucao.getTotalLiquido());
        } else if (jrField.getName().equals("produtos")) {
            return new JRBeanCollectionDataSource(ceDevolucao.getProdutos());
        }
        return null;
    }
    
}