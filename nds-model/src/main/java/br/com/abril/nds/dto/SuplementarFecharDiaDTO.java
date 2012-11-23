package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class SuplementarFecharDiaDTO implements Serializable {

	private static final long serialVersionUID = 1712356619355112111L;
	
	@Export(label = "Código", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String codigo;
	
	@Export(label = "Produto", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeProduto;
	
	@Export(label = "Edição", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private Long numeroEdicao;
	
	@Export(label = "Preço Capa", alignment=Alignment.RIGHT, exhibitionOrder = 4)
	private BigDecimal precoVenda;
	
	private Integer quantidadeContabil;
	
	private Integer quantidadeFisico;
	
	private Integer diferenca;
	
	/**
	 * quantidade total de exemplares de cada produto;
	 */
	private Long quantidadeLogico;
	
	/**
	 *quantidade de exemplares destinada às cotas;
	 */
	private Long quantidadedeVenda;
	
	/**
	 * saldo do estoque, gerado pela subtração de Lógico – Venda;
	 */
	private Long saldo;
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	public Integer getQuantidadeContabil() {
		return quantidadeContabil;
	}

	public void setQuantidadeContabil(Integer quantidadeContabil) {
		this.quantidadeContabil = quantidadeContabil;
	}

	public Integer getQuantidadeFisico() {
		return quantidadeFisico;
	}

	public void setQuantidadeFisico(Integer quantidadeFisico) {
		this.quantidadeFisico = quantidadeFisico;
	}

	public Integer getDiferenca() {
		return diferenca;
	}

	public void setDiferenca(Integer diferenca) {
		this.diferenca = diferenca;
	}

    /**
     * @return the quantidadeLogico
     */
    public Long getQuantidadeLogico() {
        return quantidadeLogico;
    }

    /**
     * @param quantidadeLogico the quantidadeLogico to set
     */
    public void setQuantidadeLogico(Long quantidadeLogico) {
        this.quantidadeLogico = quantidadeLogico;
    }

    /**
     * @return the quantidadedeVenda
     */
    public Long getQuantidadedeVenda() {
        return quantidadedeVenda;
    }

    /**
     * @param quantidadedeVenda the quantidadedeVenda to set
     */
    public void setQuantidadedeVenda(Long quantidadedeVenda) {
        this.quantidadedeVenda = quantidadedeVenda;
    }

    /**
     * @return the saldo
     */
    public Long getSaldo() {
        return saldo;
    }

    /**
     * @param saldo the saldo to set
     */
    public void setSaldo(Long saldo) {
        this.saldo = saldo;
    }

}
