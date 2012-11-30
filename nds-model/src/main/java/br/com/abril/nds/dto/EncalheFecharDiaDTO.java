package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

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
	
	@Export(label = "Preço Capa", alignment=Alignment.RIGHT, exhibitionOrder = 4)
	private BigDecimal precoVenda;
	
	/**
	 * Lógico: quantidade total de exemplares de cada produto recebido no encalhe;
	 */
	@Export(label = "Qtde", alignment=Alignment.CENTER, exhibitionOrder = 5)
	private Long qtde;
	
	@Export(label = "Venda Encalhe", alignment=Alignment.LEFT, exhibitionOrder = 6)
	private BigDecimal venda;
	
	@Export(label = "Diferença", alignment=Alignment.LEFT, exhibitionOrder = 7)
	private Integer diferenca;
	
	/**
	 * quantidade de exemplares conferida no Fechamento do Encalhe; 
	 */
	private Long qtdeFisico;
	
	/**
	 * quantidade de exemplares de cada produto juramentado no encalhe;
	 */
	private Long qtdeLogicoJuramentado;
	
	/**
	 * : quantidade de exemplares de cada produto e edição que teve sua venda realizada
	 */
	private Long qtdeVendaEncalhe;
	
	/**
	 * quantidade de exemplares resultantes da subtração de: 
	 * Lógico - Físico - Lógico Juramentado - Venda de Encalhe;
	 */
	private Long diferencas;
	
	private String qtdeFormatado;
	
	private String vendaEncalheFormatado;
	
	private String difencaFormatado;

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

	public Long getQtde() {
		return qtde;
	}

	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}

	public BigDecimal getVenda() {
		return venda;
	}

	public void setVenda(BigDecimal venda) {
		this.venda = venda;
	}

	public Integer getDiferenca() {
		return diferenca;
	}

	public void setDiferenca(Integer diferenca) {
		this.diferenca = diferenca;
	}

	/**
     * @return the qtdeFisico
     */
    public Long getQtdeFisico() {
        return qtdeFisico;
    }

    /**
     * @param qtdeFisico the qtdeFisico to set
     */
    public void setQtdeFisico(Long qtdeFisico) {
        this.qtdeFisico = qtdeFisico;
    }

    /**
     * @return the qtdeLogicoJuramentado
     */
    public Long getQtdeLogicoJuramentado() {
        return qtdeLogicoJuramentado;
    }

    /**
     * @param qtdeLogicoJuramentado the qtdeLogicoJuramentado to set
     */
    public void setQtdeLogicoJuramentado(Long qtdeLogicoJuramentado) {
        this.qtdeLogicoJuramentado = qtdeLogicoJuramentado;
    }

    /**
     * @return the qtdeVendaEncalhe
     */
    public Long getQtdeVendaEncalhe() {
        return qtdeVendaEncalhe;
    }

    /**
     * @param qtdeVendaEncalhe the qtdeVendaEncalhe to set
     */
    public void setQtdeVendaEncalhe(Long qtdeVendaEncalhe) {
        this.qtdeVendaEncalhe = qtdeVendaEncalhe;
    }

    /**
     * @return the diferencas
     */
    public Long getDiferencas() {
        return diferencas;
    }

    /**
     * @param diferencas the diferencas to set
     */
    public void setDiferencas(Long diferencas) {
        this.diferencas = diferencas;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result
				+ ((diferenca == null) ? 0 : diferenca.hashCode());
		result = prime * result
				+ ((nomeProduto == null) ? 0 : nomeProduto.hashCode());
		result = prime * result
				+ ((numeroEdicao == null) ? 0 : numeroEdicao.hashCode());
		result = prime * result
				+ ((precoVenda == null) ? 0 : precoVenda.hashCode());
		result = prime * result + ((qtde == null) ? 0 : qtde.hashCode());
		result = prime * result + ((venda == null) ? 0 : venda.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EncalheFecharDiaDTO other = (EncalheFecharDiaDTO) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (diferenca == null) {
			if (other.diferenca != null)
				return false;
		} else if (!diferenca.equals(other.diferenca))
			return false;
		if (nomeProduto == null) {
			if (other.nomeProduto != null)
				return false;
		} else if (!nomeProduto.equals(other.nomeProduto))
			return false;
		if (numeroEdicao == null) {
			if (other.numeroEdicao != null)
				return false;
		} else if (!numeroEdicao.equals(other.numeroEdicao))
			return false;
		if (precoVenda == null) {
			if (other.precoVenda != null)
				return false;
		} else if (!precoVenda.equals(other.precoVenda))
			return false;
		if (qtde == null) {
			if (other.qtde != null)
				return false;
		} else if (!qtde.equals(other.qtde))
			return false;
		if (venda == null) {
			if (other.venda != null)
				return false;
		} else if (!venda.equals(other.venda))
			return false;
		return true;
	}
	
}
