package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class TipoProdutoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7731033664102090282L;

	@Export(label="Código")
	private Long codigo;
	
	@Export(label="Código NCM")
	private Long codigoNCM;

	@Export(label="Código NBM")
	private String codigoNBM;
	
	@Export(label="Tipo de Produto")
	private String descricao;	

	public TipoProdutoDTO() {
		
	}

	public TipoProdutoDTO(Long codigo, Long codigoNCM, String codigoNBM,
			String descricao) {
		super();
		this.codigo = codigo;
		this.codigoNCM = codigoNCM;
		this.codigoNBM = codigoNBM;
		this.descricao = descricao;
	}

	/**
	 * @return the codigo
	 */
	public Long getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the codigoNCM
	 */
	public Long getCodigoNCM() {
		return codigoNCM;
	}

	/**
	 * @param codigoNCM the codigoNCM to set
	 */
	public void setCodigoNCM(Long codigoNCM) {
		this.codigoNCM = codigoNCM;
	}

	/**
	 * @return the codigoNBM
	 */
	public String getCodigoNBM() {
		return codigoNBM;
	}

	/**
	 * @param codigoNBM the codigoNBM to set
	 */
	public void setCodigoNBM(String codigoNBM) {
		this.codigoNBM = codigoNBM;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
