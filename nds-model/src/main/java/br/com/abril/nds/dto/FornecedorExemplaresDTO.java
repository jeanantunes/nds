package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FornecedorExemplaresDTO implements Serializable {
	
	private static final long serialVersionUID = -5054002962389418662L;
	
	public FornecedorExemplaresDTO() {
		
	}
	
	public FornecedorExemplaresDTO(Long idFornecedor, Long numeroFornecedor, String nomeFornecedor, BigInteger exemplares
			, BigDecimal total, BigDecimal totalDesconto, boolean inativo) {
		super();
		this.idFornecedor = idFornecedor;
		this.numeroFornecedor = numeroFornecedor;
		this.nomeFornecedor = nomeFornecedor;
		this.exemplares = exemplares;
		this.total = total;
		this.totalDesconto = totalDesconto;
		this.inativo = inativo;
	}
	
	public FornecedorExemplaresDTO(Long idFornecedor, Long numeroFornecedor, String nomeFornecedor, BigInteger exemplares
			, Long idEditor, String nomeEditor, Long codigoEditor
			, BigDecimal total, BigDecimal totalDesconto, boolean inativo) {
		super();
		this.idFornecedor = idFornecedor;
		this.numeroFornecedor = numeroFornecedor;
		this.nomeFornecedor = nomeFornecedor;
		this.idEditor = idEditor;
		this.codigoEditor = codigoEditor;
		this.nomeEditor = nomeEditor;
		this.exemplares = exemplares;
		this.total = total;
		this.totalDesconto = totalDesconto;
		this.inativo = inativo;
	}

	private Long idFornecedor;
	
	@Export(label="Fornecedor", alignment=Alignment.LEFT)
	private Long numeroFornecedor;
	
	@Export(label="Nome", alignment=Alignment.LEFT)
	private String nomeFornecedor;
	
	private Long idEditor;
	
	@Export(label="Editor", alignment=Alignment.LEFT)
	private Long codigoEditor;
	
	@Export(label="Nome Editor", alignment=Alignment.LEFT)
	private String nomeEditor;
	
	@Export(label="Total Exemplares", alignment=Alignment.CENTER)
	private BigInteger exemplares;
	
	@Export(label="Total R$", alignment=Alignment.RIGHT)
	private BigDecimal total;
	
	@Export(label="Total Desconto R$", alignment=Alignment.RIGHT)
	private BigDecimal totalDesconto;
	
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

	public Long getIdEditor() {
		return idEditor;
	}

	public void setIdEditor(Long idEditor) {
		this.idEditor = idEditor;
	}

	public Long getCodigoEditor() {
		return codigoEditor;
	}

	public void setCodigoEditor(Long codigoEditor) {
		this.codigoEditor = codigoEditor;
	}

	public String getNomeEditor() {
		return nomeEditor;
	}

	public void setNomeEditor(String nomeEditor) {
		this.nomeEditor = nomeEditor;
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

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getTotalDesconto() {
		return totalDesconto;
	}

	public void setTotalDesconto(BigDecimal totalDesconto) {
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

	public Long getNumeroFornecedor() {
		return numeroFornecedor;
	}

	public void setNumeroFornecedor(Long numeroFornecedor) {
		this.numeroFornecedor = numeroFornecedor;
	}
}