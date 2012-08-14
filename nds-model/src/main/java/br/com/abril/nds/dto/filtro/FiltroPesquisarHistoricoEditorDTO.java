package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Classe responsável por armazenar os valores referente aos filtros de
 * registros da pesquisa de registros de histórico de editor.
 * 
 * @author InfoA2
 */
@Exportable
public class FiltroPesquisarHistoricoEditorDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9024485757148061039L;

	private Date dataDe;
	private Date dataAte;
	private String numeroEditor;

	private List<ColunaOrdenacaoPesquisarHistoricoEditorDTO> listaColunaOrdenacao;

	private ColunaOrdenacaoPesquisarHistoricoEditorDTO ordenacaoColuna;

	private PaginacaoVO paginacao;

	public FiltroPesquisarHistoricoEditorDTO(Date dataDe, Date dataAte,
			String numeroEditor) {
		this.dataDe = dataDe;
		this.dataAte = dataAte;
		this.numeroEditor = numeroEditor;
	}

	public enum ColunaOrdenacaoPesquisarHistoricoEditorDTO {

		CODIGO_PRODUTO("codigoProduto"), NOME_PRODUTO("nomeProduto"), EDICAO_PRODUTO(
				"edicaoProduto"), REPARTE("reparte"), VENDA_EXEMPLARES(
				"vendaExemplares"), PORCENTAGEM_VENDA_EXEMPLARES(
				"porcentagemVenda");

		private String nomeColuna;

		private ColunaOrdenacaoPesquisarHistoricoEditorDTO(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}

		@Override
		public String toString() {
			return this.nomeColuna;
		}
	}

	public List<ColunaOrdenacaoPesquisarHistoricoEditorDTO> getListaColunaOrdenacao() {
		return listaColunaOrdenacao;
	}

	public void setListaColunaOrdenacao(
			List<ColunaOrdenacaoPesquisarHistoricoEditorDTO> listaColunaOrdenacao) {
		this.listaColunaOrdenacao = listaColunaOrdenacao;
	}

	public ColunaOrdenacaoPesquisarHistoricoEditorDTO getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	public void setOrdenacaoColuna(
			ColunaOrdenacaoPesquisarHistoricoEditorDTO ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}

	public Date getDataDe() {
		return dataDe;
	}

	public void setDataDe(Date dataDe) {
		this.dataDe = dataDe;
	}

	public Date getDataAte() {
		return dataAte;
	}

	public void setDataAte(Date dataAte) {
		this.dataAte = dataAte;
	}

	public String getNumeroEditor() {
		return numeroEditor;
	}

	public void setNumeroEditor(String numeroEditor) {
		this.numeroEditor = numeroEditor;
	}

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

}
