package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class TipoDescontoProdutoDTO implements Serializable {

	private static final long serialVersionUID = 8061615658824921559L;
	
	private Long descontoId;
	
	@Export(label = "Código", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String codigoProduto;
	
	@Export(label = "Produto", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeProduto;
	
	@Export(label = "Edição", alignment=Alignment.CENTER, exhibitionOrder = 3)
	private Long numeroEdicao;
	
	@Export(label = "Desconto %", alignment=Alignment.CENTER, exhibitionOrder = 4)
	private BigDecimal desconto;
	
	@Export(label = "Predominante", alignment=Alignment.CENTER, exhibitionOrder = 5)
	private boolean predominante;
	
	@Export(label = "Data Alteração", alignment=Alignment.CENTER, exhibitionOrder = 6)
	private Date dataAlteracao;
	
	@Export(label = "Usuário", alignment=Alignment.LEFT, exhibitionOrder = 7)
	private String nomeUsuario;
	
	private Long idTipoDesconto;
	
	private Integer qtdeProxLcmt;
	
	private Integer qtdeProxLcmtAtual;
	
	private Integer qtdeCotas;
	
	private boolean excluivel;
	
	public TipoDescontoProdutoDTO() {
    }
	
	public TipoDescontoProdutoDTO(String codigoProduto, String nomeProduto,
            Long numeroEdicao, BigDecimal desconto, Date dataAlteracao, String nomeUsuario, boolean excluivel) {
        this.codigoProduto = codigoProduto;
        this.nomeProduto = nomeProduto;
        this.numeroEdicao = numeroEdicao;
        this.desconto = desconto;
        this.dataAlteracao = dataAlteracao;
        this.nomeUsuario = nomeUsuario; 
        this.excluivel = excluivel; 
    }

    public Long getDescontoId() {
		return descontoId;
	}

	public void setDescontoId(Number descontoId) {
		this.descontoId = descontoId != null ? descontoId.longValue() : null;
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
	public void setNumeroEdicao(Number numeroEdicao) {
		this.numeroEdicao = numeroEdicao != null ? numeroEdicao.longValue() : null;
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
	 * @return
	 */
	public boolean isPredominante() {
		return predominante;
	}

	/**
	 * @param predominante
	 */
	public void setPredominante(boolean predominante) {
		this.predominante = predominante;
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
	public void setIdTipoDesconto(Number idTipoDesconto) {
		this.idTipoDesconto = idTipoDesconto != null ? idTipoDesconto.longValue() : null;
	}

	public boolean isExcluivel() {
		return excluivel;
	}

	public void setExcluivel(boolean excluivel) {
		this.excluivel = excluivel;
	}

	public Integer getQtdeProxLcmt() {
		return qtdeProxLcmt;
	}

	public void setQtdeProxLcmt(Integer qtdeProxLcmt) {
		this.qtdeProxLcmt = qtdeProxLcmt;
	}

	public Integer getQtdeCotas() {
		return qtdeCotas;
	}

	public void setQtdeCotas(BigInteger qtdeCotas) {
		this.qtdeCotas = (qtdeCotas == null) ? null : qtdeCotas.intValue();
	}

    public Integer getQtdeProxLcmtAtual() {
        return qtdeProxLcmtAtual;
    }

    public void setQtdeProxLcmtAtual(Integer qtdeProxLcmtAtual) {
        this.qtdeProxLcmtAtual = qtdeProxLcmtAtual;
    }

}
