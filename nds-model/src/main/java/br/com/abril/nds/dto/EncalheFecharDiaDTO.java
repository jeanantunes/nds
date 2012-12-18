package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class EncalheFecharDiaDTO implements Serializable {

	private static final long serialVersionUID = 1712356619355112111L;
	
	@Export(label = "Código", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String codigo;
	
	@Export(label = "Produto", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeProduto;
	
	@Export(label = "Edição", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private Long numeroEdicao;

	private BigDecimal precoVenda;
	
	@Export(label = "Preço Capa", alignment=Alignment.RIGHT, exhibitionOrder = 4)
	private String precoVendaFormatado;
	
	/**
	 * Lógico: Quantidade total de exemplares de cada produto recebido no encalhe;
	 */
	@Export(label = "Lógico", alignment=Alignment.CENTER, exhibitionOrder = 5)
	private BigInteger qtdeLogico;
	
	@Export(label = "Venda Encalhe", alignment=Alignment.CENTER, exhibitionOrder = 8)
	private BigDecimal venda;
	
	@Export(label = "Diferenças", alignment=Alignment.CENTER, exhibitionOrder = 9)
	private BigInteger qtdeDiferenca;
	
	/**
	 * Quantidade de exemplares conferida no Fechamento do Encalhe; 
	 */
	@Export(label = "Físico", alignment=Alignment.CENTER, exhibitionOrder = 6)
	private BigInteger qtdeFisico;
	
	/**
	 * Quantidade de exemplares de cada produto juramentado no encalhe;
	 */
	@Export(label = "Lógico Juramentado", alignment=Alignment.CENTER, exhibitionOrder = 7)
	private BigInteger qtdeLogicoJuramentado;
	
	/**
	 * Quantidade de exemplares de cada produto e edição que vendidos do
	 * encalhe
	 */
	private BigInteger qtdeVendaEncalhe;
	
	private String qtdeFormatado;
	
	private String vendaEncalheFormatado;
	
	private String difencaFormatado;
	
	public EncalheFecharDiaDTO() {
    }

    public EncalheFecharDiaDTO(String codigo, String nomeProduto,
            Long numeroEdicao, BigDecimal precoVenda, BigInteger qtdeLogico,
            BigInteger qtdeLogicoJuramentado, BigInteger qtdeFisico,
            BigInteger qtdeVendaEncalhe, BigInteger qtdeDiferenca) {
        this.codigo = codigo;
        this.nomeProduto = nomeProduto;
        this.numeroEdicao = numeroEdicao;
        this.precoVenda = precoVenda;
        this.precoVendaFormatado = CurrencyUtil.formatarValor(precoVenda);
        this.qtdeLogico = qtdeLogico;
        this.qtdeFisico = qtdeFisico;
        this.qtdeLogicoJuramentado = qtdeLogicoJuramentado;
        this.qtdeVendaEncalhe = qtdeVendaEncalhe;
        this.qtdeDiferenca = qtdeDiferenca;
    }

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
	
	public String getPrecoVendaFormatado() {
	    return precoVendaFormatado;
	}

	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	public BigInteger getQtdeLogico() {
		return qtdeLogico;
	}


	public BigDecimal getVenda() {
		return venda;
	}

	public void setVenda(BigDecimal venda) {
		this.venda = venda;
	}

	public BigInteger getQtdeDiferenca() {
		return qtdeDiferenca;
	}

	/**
     * @return the qtdeFisico
     */
    public BigInteger getQtdeFisico() {
        return qtdeFisico;
    }

    /**
     * @return the qtdeLogicoJuramentado
     */
    public BigInteger getQtdeLogicoJuramentado() {
        return qtdeLogicoJuramentado;
    }


    /**
     * @return the qtdeVendaEncalhe
     */
    public BigInteger getQtdeVendaEncalhe() {
        return qtdeVendaEncalhe;
    }

    public String getQtdeFormatado() {
		return qtdeFormatado;
	}

	public void setQtdeFormatado(String qtdeFormatado) {
		this.qtdeFormatado = qtdeFormatado;
	}

	public String getVendaEncalheFormatado() {
		return vendaEncalheFormatado;
	}

	public void setVendaEncalheFormatado(String vendaEncalheFormatado) {
		this.vendaEncalheFormatado = vendaEncalheFormatado;
	}

	public String getDifencaFormatado() {
		return difencaFormatado;
	}

	public void setDifencaFormatado(String difencaFormatado) {
		this.difencaFormatado = difencaFormatado;
	}

		
    @Override
    public String toString() {
        return new StringBuilder("Produto:[").append(nomeProduto).append("], ")
                .append("Qtde:[").append(qtdeLogico).append("]").toString();
    }
	
}
