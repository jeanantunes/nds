package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.List;

import br.com.abril.nds.dto.FiltroConsolidadoConsignadoCotaDTO;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * @author InfoA2
 */
public class FiltroConsultaPermissaoDTO implements Serializable {

	private static final long serialVersionUID = -435216121608747432L;

	private String nome;
	private String descricao;

	private List<ColunaOrdenacao> listaColunaOrdenacao;

	private ColunaOrdenacao ordenacaoColuna;

	private PaginacaoVO paginacao;

	public enum ColunaOrdenacao {

		NOME("nome"),
		DESCRICAO("descricao");

		private String nomeColuna;
		
		private ColunaOrdenacao(String nomeColuna) {
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

	public List<ColunaOrdenacao> getListaColunaOrdenacao() {
		return listaColunaOrdenacao;
	}

	public void setListaColunaOrdenacao(
			List<ColunaOrdenacao> listaColunaOrdenacao) {
		this.listaColunaOrdenacao = listaColunaOrdenacao;
	}

	public ColunaOrdenacao getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	public void setOrdenacaoColuna(
			ColunaOrdenacao ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FiltroConsultaPermissaoDTO other = (FiltroConsultaPermissaoDTO) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!descricao.equals(other.descricao))
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
