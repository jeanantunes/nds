package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class TipoDescontoProdutoDTO implements Serializable {

	private static final long serialVersionUID = 8061615658824921559L;
	
	@Export(label = "Código", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String codigoProduto;
	
	@Export(label = "Produto", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeProduto;
	
	@Export(label = "Edição", alignment=Alignment.CENTER, exhibitionOrder = 3)
	private Long numeroEdicao;
	
	@Export(label = "Desconto %", alignment=Alignment.CENTER, exhibitionOrder = 4)
	private BigDecimal desconto;
	
	@Export(label = "Data Alteração", alignment=Alignment.CENTER, exhibitionOrder = 5)
	private Date dataAlteracao;
	
	@Export(label = "Usuário", alignment=Alignment.LEFT, exhibitionOrder = 6)
	private String nomeUsuario;
	
	private Long idTipoDesconto;
	
	public TipoDescontoProdutoDTO() {
    }
	
	public TipoDescontoProdutoDTO(String codigoProduto, String nomeProduto,
            Long numeroEdicao, BigDecimal desconto, Date dataAlteracao) {
        this.codigoProduto = codigoProduto;
        this.nomeProduto = nomeProduto;
        this.numeroEdicao = numeroEdicao;
        this.desconto = desconto;
        this.dataAlteracao = dataAlteracao;
    }

    /**
	 * @return the codigoProduto
	 */
	public String getCodigoProduto() {
		return codigoProduto;
	}

	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	/**
	 * @return the nomeProduto
	 */
	public String getNomeProduto() {
		return nomeProduto;
	}

	/**
	 * @param nomeProduto the nomeProduto to set
	 */
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
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

	/**
	 * @return the desconto
	 */
	public BigDecimal getDesconto() {
		return desconto;
	}

	/**
	 * @param desconto the desconto to set
	 */
	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	/**
	 * @return the dataAlteracao
	 */
	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	/**
	 * @param dataAlteracao the dataAlteracao to set
	 */
	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	/**
	 * @return the nomeUsuario
	 */
	public String getNomeUsuario() {
		return nomeUsuario;
	}

	/**
	 * @param nomeUsuario the nomeUsuario to set
	 */
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}


	/**
	 * @return the idTipoDesconto
	 */
	public Long getIdTipoDesconto() {
		return idTipoDesconto;
	}

	/**
	 * @param idTipoDesconto the idTipoDesconto to set
	 */
	public void setIdTipoDesconto(Long idTipoDesconto) {
		this.idTipoDesconto = idTipoDesconto;
	}

}
