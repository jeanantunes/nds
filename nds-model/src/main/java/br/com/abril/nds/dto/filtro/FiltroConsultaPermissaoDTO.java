package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.List;

import br.com.abril.nds.vo.PaginacaoVO;

/**
 * @author InfoA2
 */
public class FiltroConsultaPermissaoDTO implements Serializable {

	private static final long serialVersionUID = -435216121608747432L;

	private String nome;
	private String descricao;

	private List<ColunaOrdenacaoPesquisarHistoricoEditorDTO> listaColunaOrdenacao;

	private ColunaOrdenacaoPesquisarHistoricoEditorDTO ordenacaoColuna;

	private PaginacaoVO paginacao;

	public enum ColunaOrdenacaoPesquisarHistoricoEditorDTO {

		NOME("nome"),
		DESCRICAO("descricao");

		private String nomeColuna;
		
		private ColunaOrdenacaoPesquisarHistoricoEditorDTO(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

}
