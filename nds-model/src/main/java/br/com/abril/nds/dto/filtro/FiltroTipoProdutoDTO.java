package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroTipoProdutoDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -478416718674122368L;

	@Export(label="Código")
	private Long codigo;
	
	@Export(label="Código NCM")
	private String codigoNCM;

	@Export(label="Código NBM")
	private String codigoNBM;
	
	@Export(label="Tipo de Produto")
	private String descricao;	

	public FiltroTipoProdutoDTO() {
		
	}

	public FiltroTipoProdutoDTO(Long codigo, String codigoNCM, String codigoNBM,
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
	public String getCodigoNCM() {
		return codigoNCM;
	}

	/**
	 * @param codigoNCM the codigoNCM to set
	 */
	public void setCodigoNCM(String codigoNCM) {
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
