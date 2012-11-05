package br.com.abril.nds.dto.chamadaencalhe;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO com as informações do Resumo da(s) Chamada(s) de Encalhe do Fornecedor 
 * para recolhimento dos produtos do Distribuidor
 * 
 * @author francisco.garcia
 * 
 */
public class ResumoChamadaEncalheFornecedorDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private PessoaJuridicaChamadaEncalheFornecedorDTO fornecedor;
    
    private PessoaJuridicaChamadaEncalheFornecedorDTO distribuidor;
    
    private IdentificacaoChamadaEncalheFornecedorDTO identificacao;
    
    private BigDecimal totalMargemDistribuidor = BigDecimal.ZERO;
    
    private BigDecimal subTotalVendas = BigDecimal.ZERO;
    
    private BigDecimal valorProjetosEspeciais = BigDecimal.ZERO;
    
    private BigDecimal valorPagar = BigDecimal.ZERO;
    
    private List<ItemResumoChamadaEncalheFornecedorDTO> itens = new ArrayList<ItemResumoChamadaEncalheFornecedorDTO>();
 
    ResumoChamadaEncalheFornecedorDTO(PessoaJuridicaChamadaEncalheFornecedorDTO fornecedor,
            PessoaJuridicaChamadaEncalheFornecedorDTO distribuidor, IdentificacaoChamadaEncalheFornecedorDTO identificacao) {
        this.fornecedor = fornecedor;
        this.distribuidor = distribuidor;
        this.identificacao = identificacao;
    }

    /**
     * @return the totalMargemDistribuidor
     */
    public BigDecimal getTotalMargemDistribuidor() {
        return totalMargemDistribuidor;
    }

    /**
     * @param totalMargemDistribuidor the totalMargemDistribuidor to set
     */
    public void setTotalMargemDistribuidor(BigDecimal totalMargemDistribuidor) {
        this.totalMargemDistribuidor = totalMargemDistribuidor;
    }

    /**
     * @return the subTotalVendas
     */
    public BigDecimal getSubTotalVendas() {
        return subTotalVendas;
    }

    /**
     * @param subTotalVendas the subTotalVendas to set
     */
    public void setSubTotalVendas(BigDecimal subTotalVendas) {
        this.subTotalVendas = subTotalVendas;
    }

    /**
     * @return the valorProjetosEspeciais
     */
    public BigDecimal getValorProjetosEspeciais() {
        return valorProjetosEspeciais;
    }

    /**
     * @param valorProjetosEspeciais the valorProjetosEspeciais to set
     */
    public void setValorProjetosEspeciais(BigDecimal valorProjetosEspeciais) {
        this.valorProjetosEspeciais = valorProjetosEspeciais;
    }

    /**
     * @return the valorPagar
     */
    public BigDecimal getValorPagar() {
        return valorPagar;
    }

    /**
     * @param valorPagar the valorPagar to set
     */
    public void setValorPagar(BigDecimal valorPagar) {
        this.valorPagar = valorPagar;
    }

    /**
     * @return the fornecedor
     */
    public PessoaJuridicaChamadaEncalheFornecedorDTO getFornecedor() {
        return fornecedor;
    }

    /**
     * @return the distribuidor
     */
    public PessoaJuridicaChamadaEncalheFornecedorDTO getDistribuidor() {
        return distribuidor;
    }

    /**
     * @return the identificacao
     */
    public IdentificacaoChamadaEncalheFornecedorDTO getIdentificacao() {
        return identificacao;
    }

    /**
     * @return the itens
     */
    public List<ItemResumoChamadaEncalheFornecedorDTO> getItens() {
        return itens;
    }

    void addItem(ItemResumoChamadaEncalheFornecedorDTO item) {
        if (itens == null) {
            itens = new ArrayList<ItemResumoChamadaEncalheFornecedorDTO>();
        }
        int linha = itens.size();
        item.setLinha(++linha);
        itens.add(item);
    }
    
    public ItemResumoChamadaEncalheFornecedorDTO newItem() {
        ItemResumoChamadaEncalheFornecedorDTO item = new ItemResumoChamadaEncalheFornecedorDTO();
        item.setDataVencimento(identificacao.getDataVencimento());
        addItem(item);
        return item;
    }
    

}
