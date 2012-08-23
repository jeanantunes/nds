package br.com.abril.nds.model.titularidade;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Entidade que representa o desconto do produto no histórico de titularidade da
 * cota
 * 
 * @author francisco.garcia
 * 
 */
@Entity
@DiscriminatorValue("PRODUTO")
public class HistoricoTitularidadeDescontoProduto extends
        HistoricoTitularidadeDescontoCota {

    /**
     * Código do Produto
     */
    @Column(name = "PRODUTO_CODIGO")
    private String codigo;

    /**
     * Nome do produto
     */
    @Column(name = "PRODUTO_NOME")
    private String nome;

    /**
     * Código do produto edição
     */
    @Column(name = "PRODUTO_CODIGO_EDICAO")
    private String codigoEdicao;

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo
     *            the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome
     *            the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the codigoEdicao
     */
    public String getCodigoEdicao() {
        return codigoEdicao;
    }

    /**
     * @param codigoEdicao
     *            the codigoEdicao to set
     */
    public void setCodigoEdicao(String codigoEdicao) {
        this.codigoEdicao = codigoEdicao;
    }

}
