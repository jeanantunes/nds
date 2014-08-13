package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.util.export.Exportable;

/**
 * Classe responsável por armazenar os valores referente aos registros da
 * pesquisa de registra de curva ABC do distribuidor.
 * @author InfoA2
 */
@Exportable
public class FiltroCurvaABCDistribuidorDTO extends FiltroCurvaABCDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1308372670097421023L;
	
	public enum TipoConsultaCurvaABC {
		PRODUTO,DISTRIBUIDOR
	}

	private TipoConsultaCurvaABC tipoConsultaCurvaABC; 
	
	public FiltroCurvaABCDistribuidorDTO() {
	
	}

	public FiltroCurvaABCDistribuidorDTO(Date dataDe, Date dataAte,
			String codigoFornecedor, String codigoProduto, String nomeProduto,
			List<Long> edicaoProduto, String codigoEditor, Integer codigoCota,
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
	
	private List<ColunaOrdenacaoCurvaABCDistribuidor> listaColunaOrdenacao;

	private ColunaOrdenacaoCurvaABCDistribuidor ordenacaoColuna;
	
	public enum ColunaOrdenacaoCurvaABCDistribuidor {
		
		RANKING_COTA("rkCota"),
		RANKING_PRODUTO("rkProduto"),
		COTA("numeroCota"),
		NOME("nomeCota"),
		MUNICIPIO("municipio"),
		QTDEPDV("quantidadePdvs"),
		VENDA_EXEMPLARES("vendaExemplaresFormatado"),
		FATURAMENTO("faturamentoCapaFormatado"),
		PARTICIPACAO("participacaoFormatado"),
		PARTICIPACAO_ACUMULADA("participacaoAcumuladaFormatado");

		private String nomeColuna;
		
		private ColunaOrdenacaoCurvaABCDistribuidor(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
	}

	public List<ColunaOrdenacaoCurvaABCDistribuidor> getListaColunaOrdenacao() {
		return listaColunaOrdenacao;
	}

	public void setListaColunaOrdenacao(List<ColunaOrdenacaoCurvaABCDistribuidor> listaColunaOrdenacao) {
		this.listaColunaOrdenacao = listaColunaOrdenacao;
	}

	public ColunaOrdenacaoCurvaABCDistribuidor getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	public void setOrdenacaoColuna(
			ColunaOrdenacaoCurvaABCDistribuidor ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}

	public TipoConsultaCurvaABC getTipoConsultaCurvaABC() {
		return tipoConsultaCurvaABC;
	}

	public void setTipoConsultaCurvaABC(TipoConsultaCurvaABC tipoConsultaCurvaABC) {
		this.tipoConsultaCurvaABC = tipoConsultaCurvaABC;
	}

	

}
