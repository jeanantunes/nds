package br.com.abril.nds.client.vo;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroValeDescontoVO {

	@Export(label="Código", widthPercent=15)
	private String codigo;
	
	@Export(label="Nome")
	private String nome;
	
	@Export(label="Edição")
	private Long numeroEdicao;
	
	private Long edicaoCuponada;
	
	private PaginacaoVO paginacaoVO;

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
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
	 * @return the edicaoCuponada
	 */
	public Long getEdicaoCuponada() {
		return edicaoCuponada;
	}

	/**
	 * @param edicaoCuponada the edicaoCuponada to set
	 */
	public void setEdicaoCuponada(Long edicaoCuponada) {
		this.edicaoCuponada = edicaoCuponada;
	}

	/**
	 * @return the paginacaoVO
	 */
	public PaginacaoVO getPaginacaoVO() {
		return paginacaoVO;
	}

	/**
	 * @param paginacaoVO the paginacaoVO to set
	 */
	public void setPaginacaoVO(PaginacaoVO paginacaoVO) {
		this.paginacaoVO = paginacaoVO;
	}
}
