package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Filtro para consulta da matriz de Distribuição 
 *
 */
@Exportable
public class FiltroDistribuicaoDTO implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3636036624099127400L;

	@Export(label="Fornecedor", exhibitionOrder=0)
	private String nomesFornecedor;
	
	@Export(label="Data da Matriz", exhibitionOrder=1)
	private Date data;
	
	
	private List<Long> idsFornecedores = new ArrayList<Long>();
	private PaginacaoVO paginacao;
	private	ColunaOrdenacao colunaOrdenacao;
	private Integer totalRegistrosEncontrados;
	private Long estudoId;
	
	public FiltroDistribuicaoDTO(){
		
	}
	public FiltroDistribuicaoDTO(Date data, List<Long> idsFornecedores,
			PaginacaoVO paginacao, String sortName) {
		this.data = data;
		this.paginacao = paginacao;
		this.idsFornecedores = idsFornecedores;
		this.colunaOrdenacao = ColunaOrdenacao.getByNomeColuna(sortName);
	}

	public FiltroDistribuicaoDTO(Date data, List<Long> idsFornecedores) {
		this.data = data;
		this.idsFornecedores = idsFornecedores;
	}
	
	public String getNomesFornecedor() {
		return nomesFornecedor;
	}

	public void setNomesFornecedor(String nomesFornecedor) {
		this.nomesFornecedor = nomesFornecedor;
	}
	
	public void setIdsFornecedores(List<Long> idsFornecedores) {
		this.idsFornecedores = idsFornecedores;
	}
	
	public List<Long> getIdsFornecedores() {
		return idsFornecedores;
	}

	public boolean filtraFornecedores() {
		return idsFornecedores != null && !idsFornecedores.isEmpty();
	}
	
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public PaginacaoVO getPaginacao() {
		return paginacao;
	}
	
	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}
	
	public ColunaOrdenacao getColunaOrdenacao() {
		return colunaOrdenacao;
	}
	
	public void setColunaOrdenacao(ColunaOrdenacao colunaOrdenacao) {
		this.colunaOrdenacao = colunaOrdenacao;
	}
	
	public Integer getTotalRegistrosEncontrados() {
		return totalRegistrosEncontrados;
	}

	public void setTotalRegistrosEncontrados(Integer totalRegistrosEncontrados) {
		this.totalRegistrosEncontrados = totalRegistrosEncontrados;
	}

	public enum ColunaOrdenacao {
		CODIGO_PRODUTO("codigoProduto"),
		NOME_PRODUTO("nomeProduto"),
		NUMERO_EDICAO("numEdicao"),
		PERIODO("periodo"),
		PRECO_VENDA("precoVenda"),
		CLASSIFICACAO("classificacao"),
		PCT_PADRAO("pctPadrao"),
		NOME_FORNECEDOR("nomeFornecedor"),
		JURAM("juram"), 
		SUPLEM("suplem"), 
		LANCTO("lancto"),
		PROMO("promo"),
		LIBERADO("liberado"),
		ESTUDO("idEstudo");
		
		private String nomeColuna;
		
		private ColunaOrdenacao(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		public String getNomeColuna() {
			return nomeColuna;
		}
		
		public static ColunaOrdenacao getByNomeColuna(String nomeColuna) {
			for (ColunaOrdenacao coluna : ColunaOrdenacao.values()) {
				if (coluna.getNomeColuna().equals(nomeColuna)) {
					return coluna;
				}
			}
			throw new IllegalArgumentException(
					"Coluna para ordenação não suportada:" + nomeColuna);
		}
	}

	public Long getEstudoId() {
		return estudoId;
	}
	public void setEstudoId(Long estudoId) {
		this.estudoId = estudoId;
	}

	
	
}
