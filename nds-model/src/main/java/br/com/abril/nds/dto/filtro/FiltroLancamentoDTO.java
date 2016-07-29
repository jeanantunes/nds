package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Filtro para consulta de lançamentos 
 * @author francisco.garcia
 *
 */
@Exportable
public class FiltroLancamentoDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2638836889195236797L;

	@Export(label="Fornecedor", exhibitionOrder=0)
	private String nomesFornecedor;
	
	@Export(label="Data de Lançamento Matriz/Distribuidor", exhibitionOrder=1)
	private Date data;
	
	private List<Long> idsFornecedores = new ArrayList<Long>() ;
	
	private PaginacaoVO paginacao;
	private	ColunaOrdenacao colunaOrdenacao;
	private Integer totalRegistrosEncontrados;
	
	private Integer filtroFisico;
	
	public FiltroLancamentoDTO(Date data, List<Long> idsFornecedores,
			PaginacaoVO paginacao, String sortName) {
		this.data = data;
		this.idsFornecedores = idsFornecedores;
		this.paginacao = paginacao;
		this.colunaOrdenacao = ColunaOrdenacao.getByNomeColuna(sortName);
	}

	public FiltroLancamentoDTO(Date data, List<Long> idsFornecedores) {
		this.data = data;
		this.idsFornecedores = idsFornecedores;
	}
	
	public String getNomesFornecedor() {
		return nomesFornecedor;
	}

	public void setNomesFornecedor(String nomesFornecedor) {
		this.nomesFornecedor = nomesFornecedor;
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
	
	public Integer getTotalRegistrosEncontrados() {
		return totalRegistrosEncontrados;
	}

	public void setTotalRegistrosEncontrados(Integer totalRegistrosEncontrados) {
		this.totalRegistrosEncontrados = totalRegistrosEncontrados;
	}

	public Integer getFiltroFisico() {
		return filtroFisico;
	}

	public void setFiltroFisico(Integer filtroFisico) {
		this.filtroFisico = filtroFisico;
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
