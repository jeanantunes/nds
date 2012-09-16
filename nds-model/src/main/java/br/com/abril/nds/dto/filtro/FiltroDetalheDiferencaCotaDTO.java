package br.com.abril.nds.dto.filtro;

import java.math.BigInteger;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

/**
 * Filtro utilizado na consulta de detalhes da diferença por cota.
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class FiltroDetalheDiferencaCotaDTO extends FiltroDTO {

	private static final long serialVersionUID = 6771818300987153961L;

	private Long idDiferenca;

	@Export(label = "Código", exhibitionOrder = 1)
	private String codigoProduto;
	
	@Export(label = "Produto", exhibitionOrder = 2)
	private String descricaoProduto;
	
	@Export(label = "Edição", exhibitionOrder = 3)
	private String numeroEdicao;
	
	@Export(label = "Tipo de Diferença", exhibitionOrder = 4)
	private String nomeFornecedor;

	@Export(label = "Tipo de Diferença", exhibitionOrder = 5)
	private String tipoDiferenca;

	@Export(label = "Exemplar", exhibitionOrder = 6)
	private BigInteger quantidade;

	private ColunaOrdenacao colunaOrdenacao;
	
	public enum ColunaOrdenacao {

		DATA("data"),
		COTA("numeroCota"),
		NOME("nomeCota"),
		BOX("codigoBox"),
		EXEMPLARES("exemplares"),
		PRECO_DESCONTO("precoDesconto"),
		TOTAL_APROVADAS("totalAprovadas"),
		TOTAL_REJEITADAS("totalRejeitadas"),
		TOTAL("valorTotal");

		private String ordenacao;
		
		ColunaOrdenacao(String ordenacao) {

			this.ordenacao = ordenacao;
		}
		
		@Override
		public String toString() {

			return this.ordenacao;
		}
	}
	
	/**
	 * @return the idDiferenca
	 */
	public Long getIdDiferenca() {
		return idDiferenca;
	}

	/**
	 * @param idDiferenca the idDiferenca to set
	 */
	public void setIdDiferenca(Long idDiferenca) {
		this.idDiferenca = idDiferenca;
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
	 * @return the descricaoProduto
	 */
	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	/**
	 * @param descricaoProduto the descricaoProduto to set
	 */
	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	/**
	 * @return the numeroEdicao
	 */
	public String getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(String numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
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
	 * @return the tipoDiferenca
	 */
	public String getTipoDiferenca() {
		return tipoDiferenca;
	}

	/**
	 * @param tipoDiferenca the tipoDiferenca to set
	 */
	public void setTipoDiferenca(String tipoDiferenca) {
		this.tipoDiferenca = tipoDiferenca;
	}

	/**
	 * @return the quantidade
	 */
	public BigInteger getQuantidade() {
		return quantidade;
	}

	/**
	 * @param quantidade the quantidade to set
	 */
	public void setQuantidade(BigInteger quantidade) {
		this.quantidade = quantidade;
	}

	/**
	 * @return the colunaOrdenacao
	 */
	public ColunaOrdenacao getColunaOrdenacao() {
		return colunaOrdenacao;
	}

	/**
	 * @param colunaOrdenacao the colunaOrdenacao to set
	 */
	public void setColunaOrdenacao(ColunaOrdenacao colunaOrdenacao) {
		this.colunaOrdenacao = colunaOrdenacao;
	}
}

