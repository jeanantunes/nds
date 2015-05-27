package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroRomaneioDTO.ColunaOrdenacaoRomaneio;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroEntradaNFETerceiros implements Serializable{

	private static final long serialVersionUID = -1398883028867830199L;
	
	@Export(label = "Fornecedor", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private Fornecedor fornecedor;

	@Export(label = "Cota", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private Cota cota;

	@Export(label = "Período", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private Date dataInicial;

	@Export(label = "Até", alignment=Alignment.LEFT, exhibitionOrder = 4)
	private Date dataFinal;

	private StatusNotaFiscalEntrada statusNotaFiscalEntrada;
	
	private TipoNota tipoNota;
	
	private PaginacaoVO paginacao;
	
	private ColunaOrdenacaoRomaneio ordenacaoColuna;
	
	private List<Fornecedor> listIdFornecedor;
	
	public enum ColunaOrdenacaoConsultaNFEEncalheTramamento {

		COTA("cota");
		
		private String nomeColuna;
		
		private ColunaOrdenacaoConsultaNFEEncalheTramamento(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
		public static ColunaOrdenacaoRomaneio getPorDescricao(String descricao) {
			for(ColunaOrdenacaoRomaneio coluna: ColunaOrdenacaoRomaneio.values()) {
				if(coluna.toString().equals(descricao))
					return coluna;
			}
			return null;
		}
	}
	
	public enum TipoNota {
		
		TODAS("Todas"),
		ENTRADA("Entrada"),
		SAIDA("Saida");
		
		private String descricao;
		
		private TipoNota(String descricao) {
			this.descricao = descricao;
		}

		/**
		 * @return the descricao
		 */
		public String getDescricao() {
			return descricao;
		}
		
	}

	public StatusNotaFiscalEntrada getStatusNotaFiscalEntrada() {
		return statusNotaFiscalEntrada;
	}

	@Export(label = "Status", alignment=Alignment.LEFT, exhibitionOrder = 5)
	public String getStatusNotaFiscalEntradaDescricao() {
		return statusNotaFiscalEntrada.getDescricao();
	}

	public void setStatusNotaFiscalEntrada(
			StatusNotaFiscalEntrada statusNotaFiscalEntrada) {
		this.statusNotaFiscalEntrada = statusNotaFiscalEntrada;
	}

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	public ColunaOrdenacaoRomaneio getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	public void setOrdenacaoColuna(ColunaOrdenacaoRomaneio ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}

	/**
	 * @return the dataInicial
	 */
	public Date getDataInicial() {
		return dataInicial;
	}

	/**
	 * @param dataInicial the dataInicial to set
	 */
	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	/**
	 * @return the dataFinal
	 */
	public Date getDataFinal() {
		return dataFinal;
	}

	/**
	 * @param dataFinal the dataFinal to set
	 */
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	/**
	 * @return the fornecedor
	 */
	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	/**
	 * @param fornecedor the fornecedor to set
	 */
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	/**
	 * @return the cota
	 */
	public Cota getCota() {
		return cota;
	}

	/**
	 * @param cota the cota to set
	 */
	public void setCota(Cota cota) {
		this.cota = cota;
	}

	/**
	 * @return the tipoNota
	 */
	public TipoNota getTipoNota() {
		return tipoNota;
	}

	/**
	 * @param tipoNota the tipoNota to set
	 */
	public void setTipoNota(TipoNota tipoNota) {
		this.tipoNota = tipoNota;
	}

	public List<Fornecedor> getListIdFornecedor() {
		return listIdFornecedor;
	}

	public void setListIdFornecedor(List<Fornecedor> listIdFornecedor) {
		this.listIdFornecedor = listIdFornecedor;
	}
}
