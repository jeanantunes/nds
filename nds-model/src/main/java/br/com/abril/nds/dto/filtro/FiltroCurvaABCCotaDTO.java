package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.util.export.Exportable;

/**
 * Classe responsável por armazenar os valores de filtro referente aos registros da
 * pesquisa de registra de curva ABC da cota.
 * @author InfoA2
 */
@Exportable
public class FiltroCurvaABCCotaDTO extends FiltroCurvaABCDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7127227181583728607L;

	public FiltroCurvaABCCotaDTO(Date dataDe, Date dataAte,
			String codigoFornecedor, String codigoProduto, String nomeProduto,
			String edicaoProduto, String codigoEditor, Integer codigoCota,
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
	
	private List<ColunaOrdenacaoCurvaABCCota> listaColunaOrdenacao;

	private ColunaOrdenacaoCurvaABCCota ordenacaoColuna;
	
	public enum ColunaOrdenacaoCurvaABCCota {
		
		RANKING("rkProduto"),
		CODIGO_PRODUTO("codigoProduto"),
		NOME_PRODUTO("nomeProduto"),
		EDICAO_PRODUTO("edicaoProduto"),
		REPARTE("reparteFormatado"),
		VENDA_EXEMPLARES("vendaExemplaresFormatado"),
		FATURAMENTO("faturamentoFormatado"),
		PORCENTAGEM_VENDA("porcentagemVendaFormatado"),
		PARTICIPACAO("participacaoFormatado"),
		PARTICIPACAO_ACUMULADA("participacaoAcumuladaFormatado");

		private String nomeColuna;
		
		private ColunaOrdenacaoCurvaABCCota(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
	}

	public List<ColunaOrdenacaoCurvaABCCota> getListaColunaOrdenacao() {
		return listaColunaOrdenacao;
	}

	public void setListaColunaOrdenacao(List<ColunaOrdenacaoCurvaABCCota> listaColunaOrdenacao) {
		this.listaColunaOrdenacao = listaColunaOrdenacao;
	}

	public ColunaOrdenacaoCurvaABCCota getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	public void setOrdenacaoColuna(
			ColunaOrdenacaoCurvaABCCota ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}
	
}
