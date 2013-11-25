package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroVendaProdutoDTO implements Serializable {

	private static final long serialVersionUID = 2433782322485644813L;
	
	@Export(label = "Código")
	private String codigo;
	
	@Export(label = "Produto")
	private String nomeProduto;
	
	@Export(label = "Edição")
	private Long edicao;
	
	private Long idFornecedor;
	
	@Export(label = "Fornecedor")
	private String nomeFornecedor;
	private TipoClassificacaoProduto classifProduto;
	private Integer numeroCota;
	private String nomeCota;
	private Long idClassificacaoProduto;
	
	private PaginacaoVO paginacao;
	
	
	public enum ColunaOrdenacao {

		EDICAO("numEdicao"),
		DATA_LANCAMENTO("dataLancamento"),
		DATA_RECOLHIMENTO("dataRecolhimento"),
		REPARTE("reparte"),
		VENDA("venda"),
		PERCENTUAL_VENDA("percentualVenda"),
		PRECO_CAPA("precoCapa"),
		CHAMADA_CAPA("chamadaCapa"),
		TOTAL("total");
		
		private String nomeColuna;
		
		private ColunaOrdenacao(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
		public static ColunaOrdenacao getPorDescricao(String descricao) {
			for(ColunaOrdenacao coluna: ColunaOrdenacao.values()) {
				if(coluna.toString().equals(descricao))
					return coluna;
			}
			return null;
		}
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	public Long getEdicao() {
		return edicao;
	}
	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}
	public Long getIdFornecedor() {
		return idFornecedor;
	}
	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}
	public PaginacaoVO getPaginacao() {
		return paginacao;
	}
	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}
	public TipoClassificacaoProduto getClassifProduto() {
		return classifProduto;
	}
	public void setClassifProduto(TipoClassificacaoProduto classifProduto) {
		this.classifProduto = classifProduto;
	}
	public Integer getNumeroCota() {
		return numeroCota;
	}
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	public String getNomeCota() {
		return nomeCota;
	}
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
	public Long getIdClassificacaoProduto() {
		return idClassificacaoProduto;
	}
	public void setIdClassificacaoProduto(Long idClassificacaoProduto) {
		this.idClassificacaoProduto = idClassificacaoProduto;
	}
}

