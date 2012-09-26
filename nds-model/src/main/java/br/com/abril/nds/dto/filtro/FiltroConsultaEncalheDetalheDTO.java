package br.com.abril.nds.dto.filtro;

import java.util.Date;

import br.com.abril.nds.vo.PaginacaoVO;


public class FiltroConsultaEncalheDetalheDTO {

	private Long idProdutoEdicao;
	
	private Long idFornecedor;
	
	private Long idCota;
	
	private Date dataRecolhimento;

	private Date dataMovimento;

	private PaginacaoVO paginacao;
	
	private OrdenacaoColunaDetalhe ordenacaoColunaDetalhe;
	
	/**
	 * Enum para ordenação das colunas de Detalhes do filtro .
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
	 * @return the idProdutoEdicao
	 */
	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	/**
	 * @param idProdutoEdicao the idProdutoEdicao to set
	 */
	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	/**
	 * @return the ordenacaoColunaDetalhe
	 */
	public OrdenacaoColunaDetalhe getOrdenacaoColunaDetalhe() {
		return ordenacaoColunaDetalhe;
	}

	/**
	 * @param ordenacaoColunaDetalhe the ordenacaoColunaDetalhe to set
	 */
	public void setOrdenacaoColunaDetalhe(
			OrdenacaoColunaDetalhe ordenacaoColunaDetalhe) {
		this.ordenacaoColunaDetalhe = ordenacaoColunaDetalhe;
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
	 * @return the idCota
	 */
	public Long getIdCota() {
		return idCota;
	}

	/**
	 * @param idCota the idCota to set
	 */
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
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
	 * @return the dataRecolhimento
	 */
	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	/**
	 * @param dataRecolhimento the dataRecolhimento to set
	 */
	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

	/**
	 * @return the dataMovimento
	 */
	public Date getDataMovimento() {
		return dataMovimento;
	}

	/**
	 * @param dataMovimento the dataMovimento to set
	 */
	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
	}	

	
}
