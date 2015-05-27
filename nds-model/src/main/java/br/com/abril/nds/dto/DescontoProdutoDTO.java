package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.Constantes;

public class DescontoProdutoDTO implements Serializable {

	private static final long serialVersionUID = -2744214706916950981L;

	private String codigoProduto;
	
	private Long edicaoProduto;
	
	private Integer quantidadeEdicoes; 
	
	private Boolean hasCotaEspecifica;
	
	private Boolean isTodasCotas;
	
	private BigDecimal descontoProduto;
	
	private List<Integer> cotas;
	
	private Boolean descontoPredominante;

	private Usuario usuario;
	
	private Boolean indProdutoEdicao;
	
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
		this.codigoProduto = StringUtils.leftPad(codigoProduto, Constantes.TAMANHO_CAMPO_PRODUTO_CONSULTA, '0');
	}

	/**
	 * @return the edicaoProduto
	 */
	public Long getEdicaoProduto() {
		return edicaoProduto;
	}

	/**
	 * @param edicaoProduto the edicaoProduto to set
	 */
	public void setEdicaoProduto(Long edicaoProduto) {
		this.edicaoProduto = edicaoProduto;
	}

	/**
	 * @return the quantidadeEdicoes
	 */
	public Integer getQuantidadeEdicoes() {
		return quantidadeEdicoes;
	}

	/**
	 * @param quantidadeEdicoes the quantidadeEdicoes to set
	 */
	public void setQuantidadeEdicoes(Integer quantidadeEdicoes) {
		this.quantidadeEdicoes = quantidadeEdicoes;
	}
	
	/**
	 * @return the hasCotaEspecifica
	 */
	public Boolean isHasCotaEspecifica() {
		return hasCotaEspecifica;
	}

	/**
	 * @param hasCotaEspecifica the hasCotaEspecifica to set
	 */
	public void setHasCotaEspecifica(Boolean hasCotaEspecifica) {
		this.hasCotaEspecifica = hasCotaEspecifica;
	}

	/**
	 * @return the isTodasCotas
	 */
	public Boolean isTodasCotas() {
		return isTodasCotas;
	}

	/**
	 * @param isTodasCotas the isTodasCotas to set
	 */
	public void setIsTodasCotas(Boolean isTodasCotas) {
		this.isTodasCotas = isTodasCotas;
	}

	/**
	 * @return the descontoProduto
	 */
	public BigDecimal getDescontoProduto() {
		return descontoProduto;
	}

	/**
	 * @param descontoProduto the descontoProduto to set
	 */
	public void setDescontoProduto(BigDecimal descontoProduto) {
		this.descontoProduto = descontoProduto;
	}

	/**
	 * @return the cotas
	 */
	public List<Integer> getCotas() {
		return cotas;
	}

	/**
	 * @param cotas the cotas to set
	 */
	public void setCotas(List<Integer> cotas) {
		this.cotas = cotas;
	}

	/**
	 * @return the descontoPredominante
	 */
	public Boolean isDescontoPredominante() {
		return descontoPredominante;
	}

	/**
	 * @param descontoPredominante the descontoPredominante to set
	 */
	public void setDescontoPredominante(Boolean descontoPredominante) {
		this.descontoPredominante = descontoPredominante;
	}

	/**
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * Obtém indProdutoEdicao
	 *
	 * @return Boolean
	 */
	public Boolean isIndProdutoEdicao() {
		return indProdutoEdicao;
	}

	/**
	 * Atribuí indProdutoEdicao
	 * @param indProdutoEdicao 
	 */
	public void setIndProdutoEdicao(Boolean indProdutoEdicao) {
		this.indProdutoEdicao = indProdutoEdicao;
	}
	
}
