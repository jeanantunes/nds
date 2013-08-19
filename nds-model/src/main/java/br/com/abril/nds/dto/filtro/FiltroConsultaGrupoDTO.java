package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.List;

import br.com.abril.nds.vo.PaginacaoVO;

/**
 * @author InfoA2
 */
public class FiltroConsultaGrupoDTO implements Serializable {

	private static final long serialVersionUID = 5324269089581991865L;
	
	private Long id;
	
	private String nome;

	private List<ColunaOrdenacao> listaColunaOrdenacao;

	private ColunaOrdenacao ordenacaoColuna;

	private PaginacaoVO paginacao;

	public enum ColunaOrdenacao {

		NOME("nome");

		private String nomeColuna;

		private ColunaOrdenacao(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}

		@Override
		public String toString() {
			return this.nomeColuna;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public void setOrdenacaoColuna(ColunaOrdenacao ordenacaoColuna) {
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
		FiltroConsultaGrupoDTO other = (FiltroConsultaGrupoDTO) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		}
		return true;
	}

}
