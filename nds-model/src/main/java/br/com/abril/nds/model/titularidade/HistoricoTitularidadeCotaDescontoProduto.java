package br.com.abril.nds.model.titularidade;

import java.math.BigDecimal;
import java.util.Date;

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
public class HistoricoTitularidadeCotaDescontoProduto extends
        HistoricoTitularidadeCotaDesconto {

    private static final long serialVersionUID = 1L;
    
    public HistoricoTitularidadeCotaDescontoProduto() {
    }

    public HistoricoTitularidadeCotaDescontoProduto(String codigo, String nome,
            Long numeroEdicao, Date atualizacao, BigDecimal desconto) {
        this.codigo = codigo;
        this.nome = nome;
        this.numeroEdicao = numeroEdicao;
        this.atualizacao = atualizacao;
        this.desconto = desconto;
    }

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
    @Column(name = "PRODUTO_NUMERO_EDICAO")
    private Long numeroEdicao;

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
	 * @return the numeroEdicao
	 */
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
}
