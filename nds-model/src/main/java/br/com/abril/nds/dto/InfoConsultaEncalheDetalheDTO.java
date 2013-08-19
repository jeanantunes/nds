package br.com.abril.nds.dto;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

public class InfoConsultaEncalheDetalheDTO {

	private List<ConsultaEncalheDetalheDTO> listaConsultaEncalheDetalhe;
	
	private Integer qtdeConsultaEncalheDetalhe;
	
	private ProdutoEdicao produtoEdicao;
	
	private Date dataOperacao;
	
	/**
	 * @return the produtoEdicao
	 */
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	/**
	 * @param produtoEdicao the produtoEdicao to set
	 */
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	/**
	 * @return the qtdeConsultaEncalheDetalhe
	 */
	public Integer getQtdeConsultaEncalheDetalhe() {
		return qtdeConsultaEncalheDetalhe;
	}

	/**
	 * @param qtdeConsultaEncalheDetalhe the qtdeConsultaEncalheDetalhe to set
	 */
	public void setQtdeConsultaEncalheDetalhe(Integer qtdeConsultaEncalheDetalhe) {
		this.qtdeConsultaEncalheDetalhe = qtdeConsultaEncalheDetalhe;
	}

	/**
	 * @return the listaConsultaEncalheDetalhe
	 */
	public List<ConsultaEncalheDetalheDTO> getListaConsultaEncalheDetalhe() {
		return listaConsultaEncalheDetalhe;
	}

	/**
	 * @param listaConsultaEncalheDetalhe the listaConsultaEncalheDetalhe to set
	 */
	public void setListaConsultaEncalheDetalhe(
			List<ConsultaEncalheDetalheDTO> listaConsultaEncalheDetalhe) {
		this.listaConsultaEncalheDetalhe = listaConsultaEncalheDetalhe;
	}

	/**
	 * @return the dataOperacao
	 */
	public Date getDataOperacao() {
		return dataOperacao;
	}

	/**
	 * @param dataOperacao the dataOperacao to set
	 */
	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
	}
	
	
}
