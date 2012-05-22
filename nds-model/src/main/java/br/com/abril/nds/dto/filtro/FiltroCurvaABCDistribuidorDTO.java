package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroCurvaABCDistribuidorDTO extends FiltroCurvaABCDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1308372670097421023L;

	public FiltroCurvaABCDistribuidorDTO(Date dataDe, Date dataAte,
			Long codigoFornecedor, String codigoProduto, String nomeProduto,
			String edicaoProduto, Long codigoEditor, String codigoCota,
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

		COTA("numeroCota"),
		NOME("nomeCota"),
		MUNICIPIO("municipio"),
		QTDEPDV("quantidadePdvs"),
		VENDA_EXEMPLARES("vendaExemplares"),
		FATURAMENTO("faturamentoCapa"),
		PARTICIPACAO("participacao"),
		PARTICIPACAO_ACUMULADA("participacaoAcumulada");

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

}
