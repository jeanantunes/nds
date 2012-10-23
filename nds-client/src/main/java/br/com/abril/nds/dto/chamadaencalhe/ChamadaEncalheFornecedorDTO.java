package br.com.abril.nds.dto.chamadaencalhe;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * DTO com as informações de Chamada de Encalhe do Fornecedor para recolhimento
 * dos produtos do Distribuidor
 * 
 * @author francisco.garcia
 * 
 */
public class ChamadaEncalheFornecedorDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    private Long numeroDocumento;
    
    private IdentificacaoChamadaEncalheFornecedorDTO identificacao;
    
    private PessoaJuridicaChamadaEncalheFornecedorDTO fornecedor;
    
    private PessoaJuridicaChamadaEncalheFornecedorDTO distribuidor;
    
    private Long totalQtdeDevolvido;
    
    private Long totalQtdeVenda;
    
    private BigDecimal totalBruto;
    
    private BigDecimal totalDesconto;
    
    private BigDecimal totalLiquido;
    
    private BigDecimal porcentagemDesconto;
    
    private BigDecimal margemDistribuidor;
    
    private BigDecimal totalMargemDistribuidor;
    
    private List<ItemChamadaEncalheFornecedorDTO> itens = new ArrayList<ItemChamadaEncalheFornecedorDTO>();

    ChamadaEncalheFornecedorDTO(
            PessoaJuridicaChamadaEncalheFornecedorDTO fornecedor,
            PessoaJuridicaChamadaEncalheFornecedorDTO distribuidor,
            IdentificacaoChamadaEncalheFornecedorDTO identificacao) {
        this.fornecedor = fornecedor;
        this.distribuidor = distribuidor;
        this.identificacao = identificacao;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the numeroDocumento
     */
    public Long getNumeroDocumento() {
        return numeroDocumento;
    }

    /**
     * @param numeroDocumento the numeroDocumento to set
     */
    public void setNumeroDocumento(Long numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }
    
    /**
     * @return the identificacao
     */
    public IdentificacaoChamadaEncalheFornecedorDTO getIdentificacao() {
        return identificacao;
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
     * @return the totalQtdeDevolvido
     */
    public Long getTotalQtdeDevolvido() {
        return totalQtdeDevolvido;
    }

    /**
     * @param totalQtdeDevolvido the totalQtdeDevolvido to set
     */
    public void setTotalQtdeDevolvido(Long totalQtdeDevolvido) {
        this.totalQtdeDevolvido = totalQtdeDevolvido;
    }

    /**
     * @return the totalQtdeVenda
     */
    public Long getTotalQtdeVenda() {
        return totalQtdeVenda;
    }

    /**
     * @param totalQtdeVenda the totalQtdeVenda to set
     */
    public void setTotalQtdeVenda(Long totalQtdeVenda) {
        this.totalQtdeVenda = totalQtdeVenda;
    }

    /**
     * @return the totalBruto
     */
    public BigDecimal getTotalBruto() {
        return totalBruto;
    }

    /**
     * @param totalBruto the totalBruto to set
     */
    public void setTotalBruto(BigDecimal totalBruto) {
        this.totalBruto = totalBruto;
    }

    /**
     * @return the totalDesconto
     */
    public BigDecimal getTotalDesconto() {
        return totalDesconto;
    }

    /**
     * @param totalDesconto the totalDesconto to set
     */
    public void setTotalDesconto(BigDecimal totalDesconto) {
        this.totalDesconto = totalDesconto;
    }

    /**
     * @return the totalLiquido
     */
    public BigDecimal getTotalLiquido() {
        return totalLiquido;
    }

    /**
     * @param totalLiquido the totalLiquido to set
     */
    public void setTotalLiquido(BigDecimal totalLiquido) {
        this.totalLiquido = totalLiquido;
    }

    /**
     * @return the porcentagemDesconto
     */
    public BigDecimal getPorcentagemDesconto() {
        return porcentagemDesconto;
    }

    /**
     * @param porcentagemDesconto the porcentagemDesconto to set
     */
    public void setPorcentagemDesconto(BigDecimal porcentagemDesconto) {
        this.porcentagemDesconto = porcentagemDesconto;
    }

    /**
     * @return the margemDistribuidor
     */
    public BigDecimal getMargemDistribuidor() {
        return margemDistribuidor;
    }

    /**
     * @param margemDistribuidor the margemDistribuidor to set
     */
    public void setMargemDistribuidor(BigDecimal margemDistribuidor) {
        this.margemDistribuidor = margemDistribuidor;
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
     * @return the itens
     */
    public List<ItemChamadaEncalheFornecedorDTO> getItens() {
        return itens;
    }

    /**
     * @param itens the itens to set
     */
    public void setItens(List<ItemChamadaEncalheFornecedorDTO> itens) {
        this.itens = itens;
    }
    
    public void addItem(ItemChamadaEncalheFornecedorDTO item) {
        if (itens == null) {
            itens = new ArrayList<>();
        }
        itens.add(item);
    }

}
