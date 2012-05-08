package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.vo.PaginacaoVO;

public class FiltroCotaDTO implements Serializable {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	private Integer numeroCota;
	
	private String numeroCpfCnpj;
	
	private PaginacaoVO paginacao;
	
	private OrdemColuna ordemColuna;
	
	public enum OrdemColuna{
		
		NUMERO_COTA(""),
		NOME_PESSOA(""),
		NUMERO_CPF_CNPJ(""),
		CONTATO(""),
		TELEFONE(""),
		EMAIL(""),
		STATUS("");
		
		private String descricao;
		
		private OrdemColuna(String descricao) {
			this.descricao = descricao;
		}
		
		public String getDescricao(){
			return this.descricao;
		}
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
	 * @return the numeroCpfCnpj
	 */
	public String getNumeroCpfCnpj() {
		return numeroCpfCnpj;
	}

	/**
	 * @param numeroCpfCnpj the numeroCpfCnpj to set
	 */
	public void setNumeroCpfCnpj(String numeroCpfCnpj) {
		this.numeroCpfCnpj = numeroCpfCnpj;
	}

	

	/**
	 * @return the paginacao
	 */
	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	/**
	 * @param paginacao the paginacao to set
	 */
	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	/**
	 * @return the ordemColuna
	 */
	public OrdemColuna getOrdemColuna() {
		return ordemColuna;
	}

	/**
	 * @param ordemColuna the ordemColuna to set
	 */
	public void setOrdemColuna(OrdemColuna ordemColuna) {
		this.ordemColuna = ordemColuna;
	}
	
	
}
