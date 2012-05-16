package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.vo.PaginacaoVO;

public class FiltroCotaDTO implements Serializable {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	private Integer numeroCota;
	
	private String nomeCota;
	
	private String numeroCpfCnpj;
	
	private PaginacaoVO paginacao;
	
	private OrdemColuna ordemColuna;
	
	public FiltroCotaDTO() {}
	
	public FiltroCotaDTO(Integer numeroCota, String nomeCota ,String numeroCpfCnpj) {
		this.numeroCota = numeroCota;
		this.numeroCpfCnpj = numeroCpfCnpj;
		this.nomeCota = nomeCota;
	}
	
	public enum OrdemColuna{
		
		NUMERO_COTA("numero"),
		NOME_PESSOA("nome"),
		NUMERO_CPF_CNPJ("numeroCpfCnpj"),
		CONTATO("contato"),
		TELEFONE("telefone"),
		EMAIL("email"),
		STATUS("status");
		
		private String descricao;
		
		private OrdemColuna(String descricao) {
			this.descricao = descricao;
		}
		
		public String getDescricao(){
			return this.descricao;
		}
		
		@Override
		public String toString() {
			
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((nomeCota == null) ? 0 : nomeCota.hashCode());
		result = prime * result
				+ ((numeroCota == null) ? 0 : numeroCota.hashCode());
		result = prime * result
				+ ((numeroCpfCnpj == null) ? 0 : numeroCpfCnpj.hashCode());
		result = prime * result
				+ ((ordemColuna == null) ? 0 : ordemColuna.hashCode());
		result = prime * result
				+ ((paginacao == null) ? 0 : paginacao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FiltroCotaDTO other = (FiltroCotaDTO) obj;
		if (nomeCota == null) {
			if (other.nomeCota != null)
				return false;
		} else if (!nomeCota.equals(other.nomeCota))
			return false;
		if (numeroCota == null) {
			if (other.numeroCota != null)
				return false;
		} else if (!numeroCota.equals(other.numeroCota))
			return false;
		if (numeroCpfCnpj == null) {
			if (other.numeroCpfCnpj != null)
				return false;
		} else if (!numeroCpfCnpj.equals(other.numeroCpfCnpj))
			return false;
		if (ordemColuna != other.ordemColuna)
			return false;
		if (paginacao == null) {
			if (other.paginacao != null)
				return false;
		} else if (!paginacao.equals(other.paginacao))
			return false;
		return true;
	}
	
	
	
	
}
