package br.com.abril.nds.dto.filtro;

import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroConsultaEncalheDTO {
	
	@Export(label="Período")
	private Date dataRecolhimentoInicial;

	@Export(label="Até")
	private Date dataRecolhimentoFinal;
	
	private Long idFornecedor;
	
	private Long idCota;

	@Export(label="Fornecedor")
	private String nomeFornecedor;
	
	@Export(label="Nº Cota")
	private Integer numCota;
	
	@Export(label="Cota")
	private String nomeCota;
	
	private boolean utilizaPrecoCapa;
	
	private Integer idBox;
	
	private Integer numeroEdicao;

	private Integer codigoProduto;
	
	private Long idProduto;
	
	protected Long idProdutoEdicao;
	
	private PaginacaoVO paginacao;
	
	private OrdenacaoColuna ordenacaoColuna;
	
	private boolean desconsiderarCotaAVista;
	
	public FiltroConsultaEncalheDTO() {
	    
	}
	
	public FiltroConsultaEncalheDTO(Date data) {
        this.dataRecolhimentoFinal = data;
        this.dataRecolhimentoInicial = data;        
    }
	
	/**
	 * Enum para ordenação das colunas do filtro.
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum OrdenacaoColuna {
		
		CODIGO_PRODUTO("codigoProduto"),
		NOME_PRODUTO("nomeProduto"),
		NUMERO_EDICAO("numeroEdicao"),
		PRECO_CAPA("precoVenda"),
		PRECO_COM_DESCONTO("precoComDesconto"),
		REPARTE("reparte"),
		ENCALHE("encalhe"),
		FORNECEDOR("fornecedor"),
		VALOR("total"),
		VALOR_COM_DESCONTO("valorComDesconto"),
		RECOLHIMENTO("recolhimento");
		
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
	 * Enum para ordenação das colunas do filtro.
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum OrdenacaoColunaDetalhe {
		
		NUMERO_COTA("numeroCota"),
		NOME_COTA("nomeCota"),
		OBSERVACAO("observacao");
		
		private String nomeColuna;
		
		private OrdenacaoColunaDetalhe(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
	}	

	/**
	 * Obtém idFornecedor
	 *
	 * @return Long
	 */
	public Long getIdFornecedor() {
		return idFornecedor;
	}

	/**
	 * Atribuí idFornecedor
	 * @param idFornecedor 
	 */
	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}


	/**
	 * Obtém paginacao
	 *
	 * @return PaginacaoVO
	 */
	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	/**
	 * Atribuí paginacao
	 * @param paginacao 
	 */
	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	/**
	 * Obtém ordenacaoColuna
	 *
	 * @return OrdenacaoColuna
	 */
	public OrdenacaoColuna getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	/**
	 * Atribuí ordenacaoColuna
	 * @param ordenacaoColuna 
	 */
	public void setOrdenacaoColuna(OrdenacaoColuna ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}
	
	
	/**
	 * Obtém nomeFornecedor
	 *
	 * @return String
	 */
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	/**
	 * Atribuí nomeFornecedor
	 * @param nomeFornecedor 
	 */
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	/**
	 * Obtém nomeCota
	 *
	 * @return String
	 */
	public String getNomeCota() {
		return nomeCota;
	}

	/**
	 * Atribuí nomeCota
	 * @param nomeCota 
	 */
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	/**
	 * Obtém idCota
	 *
	 * @return Long
	 */
	public Long getIdCota() {
		return idCota;
	}

	/**
	 * Atribuí idCota
	 * @param idCota 
	 */
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	/**
	 * @return the dataRecolhimentoInicial
	 */
	public Date getDataRecolhimentoInicial() {
		return dataRecolhimentoInicial;
	}

	/**
	 * @param dataRecolhimentoInicial the dataRecolhimentoInicial to set
	 */
	public void setDataRecolhimentoInicial(Date dataRecolhimentoInicial) {
		this.dataRecolhimentoInicial = dataRecolhimentoInicial;
	}

	/**
	 * @return the dataRecolhimentoFinal
	 */
	public Date getDataRecolhimentoFinal() {
		return dataRecolhimentoFinal;
	}

	/**
	 * @param dataRecolhimentoFinal the dataRecolhimentoFinal to set
	 */
	public void setDataRecolhimentoFinal(Date dataRecolhimentoFinal) {
		this.dataRecolhimentoFinal = dataRecolhimentoFinal;
	}

	public Integer getNumCota() {
		return numCota;
	}

	public void setNumCota(Integer numCota) {
		this.numCota = numCota;
	}
	
	public boolean isUtilizaPrecoCapa() {
		return utilizaPrecoCapa;
	}

	public void setUtilizaPrecoCapa(boolean utilizaPrecoCapa) {
		this.utilizaPrecoCapa = utilizaPrecoCapa;
	}
	
	public boolean isDesconsiderarCotaAVista() {
		return desconsiderarCotaAVista;
	}

	public void setDesconsiderarCotaAVista(boolean desconsiderarCotaAVista) {
		this.desconsiderarCotaAVista = desconsiderarCotaAVista;
	}
	
	public Integer getIdBox() {
		return idBox;
	}

	public void setIdBox(Integer idBox) {
		this.idBox = idBox;
	}

	public Integer getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Integer numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public Integer getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(Integer codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	
	public Long getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}
	
	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataRecolhimentoFinal == null) ? 0 : dataRecolhimentoFinal.hashCode());
		result = prime * result + ((dataRecolhimentoInicial == null) ? 0 : dataRecolhimentoInicial.hashCode());
		result = prime * result + ((idCota == null) ? 0 : idCota.hashCode());
		result = prime * result + ((idFornecedor == null) ? 0 : idFornecedor.hashCode());
		result = prime * result + ((nomeCota == null) ? 0 : nomeCota.hashCode());
		result = prime * result + ((nomeFornecedor == null) ? 0 : nomeFornecedor.hashCode());
		result = prime * result + ((ordenacaoColuna == null) ? 0 : ordenacaoColuna.hashCode());
		result = prime * result + ((paginacao == null) ? 0 : paginacao.hashCode());
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
		FiltroConsultaEncalheDTO other = (FiltroConsultaEncalheDTO) obj;
		if (dataRecolhimentoFinal == null) {
			if (other.dataRecolhimentoFinal != null)
				return false;
		} else if (!dataRecolhimentoFinal.equals(other.dataRecolhimentoFinal))
			return false;
		if (dataRecolhimentoInicial == null) {
			if (other.dataRecolhimentoInicial != null)
				return false;
		} else if (!dataRecolhimentoInicial
				.equals(other.dataRecolhimentoInicial))
			return false;
		if (idCota == null) {
			if (other.idCota != null)
				return false;
		} else if (!idCota.equals(other.idCota))
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