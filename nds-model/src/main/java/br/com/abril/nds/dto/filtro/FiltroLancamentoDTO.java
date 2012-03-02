package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Filtro para consulta de lançamentos 
 * @author francisco.garcia
 *
 */
public class FiltroLancamentoDTO implements Serializable {

	private static final long serialVersionUID = -2638836889195236797L;

	private Date data;
	private List<Long> idsFornecedores = new ArrayList<Long>() ;
	private PaginacaoVO paginacao;
	private	ColunaOrdenacao colunaOrdenacao;
	
	public FiltroLancamentoDTO(Date data, List<Long> idsFornecedores,
			PaginacaoVO paginacao, String sortName) {
		this.data = data;
		this.idsFornecedores = idsFornecedores;
		this.paginacao = paginacao;
		this.colunaOrdenacao = ColunaOrdenacao.getByNomeColuna(sortName);
	}

	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public List<Long> getIdsFornecedores() {
		return idsFornecedores;
	}
	
	public void setIdsFornecedores(List<Long> idsFornecedores) {
		this.idsFornecedores = idsFornecedores;
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
	
	public boolean filtraFornecedores() {
		return idsFornecedores != null && !idsFornecedores.isEmpty();
	}
	
	public enum ColunaOrdenacao {
		CODIGO_PRODUTO("codigoProduto"),
		NOME_PRODUTO("nomeProduto"),
		NUMERO_EDICAO("numEdicao"),
		PRECO("preco"),
		PACOTE_PADRAO("pacotePadrao"),
		REPARTE("reparte"),
		FISICO("fisico"),
		ESTUDO_GERADO("estudoGerado"),
		LANCAMENTO("lancamento"), 
		RECOLHIMENTO("dataRecolhimento"), 
		FORNECEDOR("nomeFornecedor"),
		DATA_LANC_DISTRIB("dataMatrizDistrib"),
		DATA_LANC_PREVISTO("dataPrevisto"),
		TOTAL("total");
		
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
	
}
