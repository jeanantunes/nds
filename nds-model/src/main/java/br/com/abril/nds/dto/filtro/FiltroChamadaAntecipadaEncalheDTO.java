package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

/** 
 * Data Transfer Object para filtro da pesquisa de chamada antecipada de encalhe.
 * 
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class FiltroChamadaAntecipadaEncalheDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1821183862532025765L;
	
	@Export(label = "Código")
	private String codigoProduto;
	
	@Export(label = "Produto")
	private String nomeProduto;
	
	@Export(label = "Edição")
	private Long numeroEdicao;
	
	@Export(label = "Data Programada")
	private String dataProgramada;
	
	@Export(label = "Box")
	private Long box;
	
	@Export(label = "Fornecedor")
	private String nomeFornecedor;

	private Long fornecedor;
	
	private Integer numeroCota;
	
	private Date dataAntecipacao;

	private PaginacaoVO paginacao;
	
	private OrdenacaoColuna ordenacaoColuna;

	/**
	 * Construtor padrão.
	 */
	public FiltroChamadaAntecipadaEncalheDTO(String codigoProduto,Long numeroEdicao,Long box, Long fornecedor) {
		
		this.codigoProduto = codigoProduto;
		this.numeroEdicao = numeroEdicao;
		this.box = box;
		this.fornecedor = fornecedor;
	}
	
	/**
	 * Construtor padrão.
	 */
	public FiltroChamadaAntecipadaEncalheDTO(String codigoProduto,Long numeroEdicao,Long box, Long fornecedor,Integer numeroCota) {
		
		this.codigoProduto = codigoProduto;
		this.numeroEdicao = numeroEdicao;
		this.box = box;
		this.fornecedor = fornecedor;
		this.numeroCota = numeroCota;
	}

	public FiltroChamadaAntecipadaEncalheDTO() {
		
	}
	
	/**
	 * Enum para ordenação das colunas do filtro.
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum OrdenacaoColuna {
		
		BOX("box"),
		NUMERO_COTA(""),
		NOME_COTA(""),
		QNT_EXEMPLARES("");
		
		private String nomeColuna;
		
		private OrdenacaoColuna(String nomeColuna) {
			
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			
			return this.nomeColuna;
		}
	}

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
		this.codigoProduto = codigoProduto;
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
	 * @return the box
	 */
	public Long getBox() {
		return box;
	}

	/**
	 * @param box the box to set
	 */
	public void setBox(Long box) {
		this.box = box;
	}

	/**
	 * @return the fornecedor
	 */
	public Long getFornecedor() {
		return fornecedor;
	}

	/**
	 * @param fornecedor the fornecedor to set
	 */
	public void setFornecedor(Long fornecedor) {
		this.fornecedor = fornecedor;
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
	 * @return the ordenacaoColuna
	 */
	public OrdenacaoColuna getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	/**
	 * @param ordenacaoColuna the ordenacaoColuna to set
	 */
	public void setOrdenacaoColuna(OrdenacaoColuna ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	
	public String getDataProgramada() {
		return dataProgramada;
	}

	public void setDataProgramada(String dataProgramada) {
		this.dataProgramada = dataProgramada;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	
	
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	/**
	 * @return the dataAntecipacao
	 */
	public Date getDataAntecipacao() {
		return dataAntecipacao;
	}

	/**
	 * @param dataAntecipacao the dataAntecipacao to set
	 */
	public void setDataAntecipacao(Date dataAntecipacao) {
		this.dataAntecipacao = dataAntecipacao;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((box == null) ? 0 : box.hashCode());
		result = prime * result
				+ ((codigoProduto == null) ? 0 : codigoProduto.hashCode());
		result = prime * result
				+ ((fornecedor == null) ? 0 : fornecedor.hashCode());
		result = prime * result
				+ ((numeroEdicao == null) ? 0 : numeroEdicao.hashCode());
		result = prime * result
				+ ((ordenacaoColuna == null) ? 0 : ordenacaoColuna.hashCode());
		result = prime * result
				+ ((paginacao == null) ? 0 : paginacao.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FiltroChamadaAntecipadaEncalheDTO other = (FiltroChamadaAntecipadaEncalheDTO) obj;
		if (box == null) {
			if (other.box != null)
				return false;
		} else if (!box.equals(other.box))
			return false;
		if (codigoProduto == null) {
			if (other.codigoProduto != null)
				return false;
		} else if (!codigoProduto.equals(other.codigoProduto))
			return false;
		if (fornecedor == null) {
			if (other.fornecedor != null)
				return false;
		} else if (!fornecedor.equals(other.fornecedor))
			return false;
		if (numeroEdicao == null) {
			if (other.numeroEdicao != null)
				return false;
		} else if (!numeroEdicao.equals(other.numeroEdicao))
			return false;
		if (ordenacaoColuna != other.ordenacaoColuna)
			return false;
		if (paginacao == null) {
			if (other.paginacao != null)
				return false;
		} else if (!paginacao.equals(other.paginacao))
			return false;
		return true;
	}
	
	
}
