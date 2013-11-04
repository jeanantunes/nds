package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class SuplementarFecharDiaDTO implements Serializable {

	private static final long serialVersionUID = 1712356619355112111L;
	
	private Long idProdutoEdicao;
	
	@Export(label = "Código", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String codigo;
	
	@Export(label = "Produto", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeProduto;
	
	@Export(label = "Edição", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private Long numeroEdicao;
	
	@Export(label = "Preço Capa", alignment=Alignment.RIGHT, exhibitionOrder = 4)
	private BigDecimal precoVenda;
	
	/**
	 * Quantidade(atual) de exemplares do estoque suplementar 
	 */
	@Export(label = "Qtde", alignment=Alignment.RIGHT, exhibitionOrder = 5)
	private BigInteger quantidadeContabil;
	
	/**
	 * Quantidade(anterior) de exemplares do estoque suplementar;
	 */
	private BigInteger quantidadeLogico;
	
	/**
	 *Quantidade de exemplares vendidos do estoque suplementar;
	 */
	private BigInteger quantidadeVenda;
	
	/**
	 * Quantidade de exemplares entrada transferência
	 */
	private BigInteger quantidadeTransferenciaEntrada;
	
	/**
     * Quantidade de exemplares saida transferência
     */
    private BigInteger quantidadeTransferenciaSaida;

	
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

	public BigInteger getQuantidadeContabil() {
		return quantidadeContabil;
	}

	public void setQuantidadeContabil(BigInteger quantidadeContabil) {
		this.quantidadeContabil = quantidadeContabil;
	}	

    /**
     * @return the quantidadeLogico
     */
    public BigInteger getQuantidadeLogico() {
        return quantidadeLogico;
    }

    /**
     * @param quantidadeLogico the quantidadeLogico to set
     */
    public void setQuantidadeLogico(BigInteger quantidadeLogico) {
        this.quantidadeLogico = quantidadeLogico;
    }

    public BigInteger getQuantidadeVenda() {
        return quantidadeVenda;
    }

    public void setQuantidadeVenda(BigInteger quantidadeVenda) {
        this.quantidadeVenda = quantidadeVenda;
    }
    
    public BigInteger getQuantidadeTransferenciaEntrada() {
        return quantidadeTransferenciaEntrada;
    }
    
    public void setQuantidadeTransferenciaEntrada(
            BigInteger quantidadeTransferenciaEntrada) {
        this.quantidadeTransferenciaEntrada = quantidadeTransferenciaEntrada;
    }
    
    public BigInteger getQuantidadeTransferenciaSaida() {
        return quantidadeTransferenciaSaida;
    }
    
    public void setQuantidadeTransferenciaSaida(
            BigInteger quantidadeTransferenciaSaida) {
        this.quantidadeTransferenciaSaida = quantidadeTransferenciaSaida;
    }

	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

}
