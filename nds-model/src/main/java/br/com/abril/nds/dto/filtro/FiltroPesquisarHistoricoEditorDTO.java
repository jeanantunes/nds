package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
	
	public FiltroPesquisarHistoricoEditorDTO(Date dataDe, Date dataAte, String numeroEditor) {
		this.dataDe = dataDe;
		this.dataAte = dataAte;
		this.numeroEditor = numeroEditor;
	}
	
	public enum ColunaOrdenacaoPesquisarHistoricoEditorDTO {

		CODIGO_PRODUTO("codigoProduto"),
		NOME_PRODUTO("nomeProduto"),
		EDICAO_PRODUTO("edicaoProduto"),
		REPARTE("reparte"),
		VENDA_EXEMPLARES("vendaExemplares"),
		PORCENTAGEM_VENDA_EXEMPLARES("porcentagemVendaExemplares");

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

	public void setListaColunaOrdenacao(List<ColunaOrdenacaoPesquisarHistoricoEditorDTO> listaColunaOrdenacao) {
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
	
}
