package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FornecedorExemplaresDTO implements Serializable {
	
	private static final long serialVersionUID = -5054002962389418662L;
	
	public FornecedorExemplaresDTO() {
		
	}
	
	public FornecedorExemplaresDTO(Long idFornecedor, Integer numeroFornecedor, String nomeFornecedor, BigInteger exemplares, BigInteger total, BigInteger totalDesconto, boolean inativo) {
		super();
		this.idFornecedor = idFornecedor;
		this.numeroFornecedor = numeroFornecedor;
		this.nomeFornecedor = nomeFornecedor;
		this.exemplares = exemplares;
		this.total = total;
		this.totalDesconto = totalDesconto;
		this.inativo = inativo;
	}

	private Long idFornecedor;
	
	@Export(label="Fornecedor", alignment=Alignment.LEFT)
	private Integer numeroFornecedor;
	
	@Export(label="Nome", alignment=Alignment.LEFT)
	private String nomeFornecedor;
	
	@Export(label="Total Exemplares", alignment=Alignment.CENTER)
	private BigInteger exemplares;
	
	@Export(label="Total R$", alignment=Alignment.RIGHT)
	private BigInteger total;
	
	@Export(label="Total Desconto R$", alignment=Alignment.RIGHT)
	private BigInteger totalDesconto;
	
	private boolean inativo;

	/**
	 * @return the idFornecedor
	 */
	public Long getIdFornecedor() {
		return idFornecedor;
	}

	/**
	 * @param idFornecedor the idFornecedor to set
	 */
	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	/**
	 * @return the nomeFornecedor
	 */
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	/**
	 * @param nomeFornecedor the nomeFornecedor to set
	 */
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	/**
	 * @return the inativo
	 */
	public boolean isInativo() {
		return inativo;
	}

	/**
	 * @param inativo the inativo to set
	 */
	public void setInativo(boolean inativo) {
		this.inativo = inativo;
	}

	public BigInteger getTotal() {
		return total;
	}

	public void setTotal(BigInteger total) {
		this.total = total;
	}

	public BigInteger getTotalDesconto() {
		return totalDesconto;
	}

	public void setTotalDesconto(BigInteger totalDesconto) {
		this.totalDesconto = totalDesconto;
	}
	
	/**
	 * @return the exemplares
	 */
	public void setExemplares(BigInteger exemplares) {
		this.exemplares = exemplares;
	}
	
	/**
	 * @param exemplares the exemplares to set
	 */
	public BigInteger getExemplares() {
		return exemplares;
	}

	public Integer getNumeroFornecedor() {
		return numeroFornecedor;
	}

	public void setNumeroFornecedor(Integer numeroFornecedor) {
		this.numeroFornecedor = numeroFornecedor;
	}
}