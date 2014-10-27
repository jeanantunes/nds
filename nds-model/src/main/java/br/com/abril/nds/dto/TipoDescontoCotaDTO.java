package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class TipoDescontoCotaDTO implements Serializable {

	private static final long serialVersionUID = -792066273850207153L;
	
	private Long descontoId;
	
	@Export(label = "Cota", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private Integer numeroCota;
	
	@Export(label = "Nome", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeCota;
	
	@Export(label = "Desconto %", alignment=Alignment.CENTER, exhibitionOrder = 3)
	private BigDecimal desconto;
	
	@Export(label = "Fornecedor(es)", alignment=Alignment.RIGHT, exhibitionOrder = 4)
	private String fornecedor;
	
	@Export(label = "Data Alteração", alignment=Alignment.CENTER, exhibitionOrder = 5)
	private Date dataAlteracao;
	
	@Export(label = "Usuário", alignment=Alignment.LEFT, exhibitionOrder = 6)
	private String nomeUsuario;
	
	private String descTipoDesconto;
	
	private Long idTipoDesconto;
	
	public TipoDescontoCotaDTO() {}
	
    public TipoDescontoCotaDTO(BigDecimal desconto, String fornecedor,
            Date dataAlteracao, String descTipoDesconto) {
        this.desconto = desconto;
        this.fornecedor = fornecedor;
        this.dataAlteracao = dataAlteracao;
        this.descTipoDesconto = descTipoDesconto;
    }

    public Long getDescontoId() {
		return descontoId;
	}

	public void setDescontoId(Long descontoId) {
		this.descontoId = descontoId;
	}

	/**
	 * @return the numeroCota
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the nomeCota
	 */
	public String getNomeCota() {
		return nomeCota;
	}

	/**
	 * @param nomeCota the nomeCota to set
	 */
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
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
	 * @return the fornecedor
	 */
	public String getFornecedor() {
		return fornecedor;
	}

	/**
	 * @param fornecedor the fornecedor to set
	 */
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
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

	/**
	 * @return the descTipoDesconto
	 */
	public String getDescTipoDesconto() {
		return descTipoDesconto;
	}

	/**
	 * @param descTipoDesconto the descTipoDesconto to set
	 */
	public void setDescTipoDesconto(String descTipoDesconto) {
		this.descTipoDesconto = descTipoDesconto;
	}
		
}
