package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.util.export.Exportable;

/**
 * Classe responsável por armazenar os valores referente aos registros da
 * pesquisa de registra de curva ABC do editor.
 * @author InfoA2
 */
@Exportable
public class  FiltroCurvaABCEditorDTO extends FiltroCurvaABCDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5221185820098445595L;

	public FiltroCurvaABCEditorDTO(Date dataDe, Date dataAte,
			String codigoFornecedor, String codigoProduto, String nomeProduto,
			String edicaoProduto, String codigoEditor, String codigoCota,
			String nomeCota, String municipio) {
		this.setDataDe(dataDe);
		this.setDataAte(dataAte);
		this.setCodigoFornecedor(codigoFornecedor);
		this.setCodigoProduto(codigoProduto);
		this.setNomeProduto(nomeProduto);
		this.setEdicaoProduto(edicaoProduto);
		this.setCodigoEditor(codigoEditor);
		this.setCodigoCota(codigoCota);
		this.setNomeCota(nomeCota);
		this.setMunicipio(municipio);
	}
	
	private List<ColunaOrdenacaoCurvaABCEditor> listaColunaOrdenacao;

	private ColunaOrdenacaoCurvaABCEditor ordenacaoColuna;
	
	public enum ColunaOrdenacaoCurvaABCEditor {

		CODIGO_EDITOR("codigoEditor"),
		NOME_EDITOR("nomeEditor"),
		REPARTE("reparte"),
		VENDA_EXEMPLARES("vendaExemplares"),
		PORCENTAGEM_VENDA_EXEMPLARES("porcentagemVendaExemplares"),
		FATURAMENTO_CAPA("faturamentoCapa"),
		PARTICIPACAO("participacao"),
		PARTICIPACAO_ACUMULADA("participacaoAcumulada");

		private String nomeColuna;
		
		private ColunaOrdenacaoCurvaABCEditor(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
	}

	public List<ColunaOrdenacaoCurvaABCEditor> getListaColunaOrdenacao() {
		return listaColunaOrdenacao;
	}

	public void setListaColunaOrdenacao(List<ColunaOrdenacaoCurvaABCEditor> listaColunaOrdenacao) {
		this.listaColunaOrdenacao = listaColunaOrdenacao;
	}

	public ColunaOrdenacaoCurvaABCEditor getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	public void setOrdenacaoColuna(
			ColunaOrdenacaoCurvaABCEditor ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}
	
}
