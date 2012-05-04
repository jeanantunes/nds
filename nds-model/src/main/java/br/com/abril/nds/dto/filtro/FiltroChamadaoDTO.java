package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

/**
 * Data Transfer Object para filtro da pesquisa de 
 * chamadão de publicações.
 * 
 * @author Discover Technology
 */
@Exportable
public class FiltroChamadaoDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = -6084623822454374488L;

	@Export(label = "Cota", exhibitionOrder = 1)
	private Integer numeroCota;
	
	@Export(label = "Nome", exhibitionOrder = 2)
	private String nomeCota;
	
	@Export(label = "Data Chamadão", exhibitionOrder = 3)
	private Date dataChamadao;
	
	private Long idFornecedor;
	
	@Export(label = "Fornecedor", exhibitionOrder = 4)
	private String nomeFornecedor;
	
	private OrdenacaoColunaChamadao ordenacaoColuna;
	
	/**
	 * Construtor padrão.
	 */
	public FiltroChamadaoDTO() {
		
	}
	
	/**
	 * Construtor.
	 * 
	 * @param numeroCota - número da cota
	 * @param dataChamadao - data do chamadão
	 * @param idFornecedor - id do fornecedor
	 */
	public FiltroChamadaoDTO(Integer numeroCota, Date dataChamadao, Long idFornecedor) {
		
		this.numeroCota = numeroCota;
		this.dataChamadao = dataChamadao;
		this.idFornecedor = idFornecedor;
	}

	/**
	 * Enum para ordenação das colunas do filtro.
	 * 
	 * @author Discover Technology
	 */
	public enum OrdenacaoColunaChamadao {
		
		CODIGO_PRODUTO("codigo"),
		NOME_PRODUTO("produto"),
		EDICAO("edicao"),
		PRECO_VENDA("precoVenda"),
		PRECO_DESCONTO("precoDesconto"),
		REPARTE("reparte"),
		FORNECEDOR("fornecedor"),
		RECOLHIMENTO("dataRecolhimento"),
		VALOR_TOTAL("valorTotal");
		
		private String nomeColuna;
		
		private OrdenacaoColunaChamadao(String nomeColuna) {
			
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			
			return this.nomeColuna;
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
	 * @return the dataChamadao
	 */
	public Date getDataChamadao() {
		return dataChamadao;
	}

	/**
	 * @param dataChamadao the dataChamadao to set
	 */
	public void setDataChamadao(Date dataChamadao) {
		this.dataChamadao = dataChamadao;
	}

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
	 * @return the ordenacaoColuna
	 */
	public OrdenacaoColunaChamadao getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	/**
	 * @param ordenacaoColuna the ordenacaoColuna to set
	 */
	public void setOrdenacaoColuna(OrdenacaoColunaChamadao ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((dataChamadao == null) ? 0 : dataChamadao.hashCode());
		result = prime * result
				+ ((idFornecedor == null) ? 0 : idFornecedor.hashCode());
		result = prime * result
				+ ((nomeCota == null) ? 0 : nomeCota.hashCode());
		result = prime * result
				+ ((nomeFornecedor == null) ? 0 : nomeFornecedor.hashCode());
		result = prime * result
				+ ((numeroCota == null) ? 0 : numeroCota.hashCode());
		result = prime * result
				+ ((ordenacaoColuna == null) ? 0 : ordenacaoColuna.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FiltroChamadaoDTO other = (FiltroChamadaoDTO) obj;
		if (dataChamadao == null) {
			if (other.dataChamadao != null)
				return false;
		} else if (!dataChamadao.equals(other.dataChamadao))
			return false;
		if (idFornecedor == null) {
			if (other.idFornecedor != null)
				return false;
		} else if (!idFornecedor.equals(other.idFornecedor))
			return false;
		if (nomeCota == null) {
			if (other.nomeCota != null)
				return false;
		} else if (!nomeCota.equals(other.nomeCota))
			return false;
		if (nomeFornecedor == null) {
			if (other.nomeFornecedor != null)
				return false;
		} else if (!nomeFornecedor.equals(other.nomeFornecedor))
			return false;
		if (numeroCota == null) {
			if (other.numeroCota != null)
				return false;
		} else if (!numeroCota.equals(other.numeroCota))
			return false;
		if (ordenacaoColuna != other.ordenacaoColuna)
			return false;
		return true;
	}

}
