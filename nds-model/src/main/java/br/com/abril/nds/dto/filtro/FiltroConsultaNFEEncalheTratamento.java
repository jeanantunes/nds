package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.dto.filtro.FiltroRomaneioDTO.ColunaOrdenacaoRomaneio;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroConsultaNFEEncalheTratamento implements Serializable{

	private static final long serialVersionUID = -1398883028867830199L;
	
	private String codigoCota;
	private Date data;
	private StatusNotaFiscalEntrada statusNotaFiscalEntrada;
	private Long codigoNota;
	
	private PaginacaoVO paginacao;
	
	private ColunaOrdenacaoRomaneio ordenacaoColuna;
	
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

	public String getCodigoCota() {
		return codigoCota;
	}

	public void setCodigoCota(String codigoCota) {
		this.codigoCota = codigoCota;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public StatusNotaFiscalEntrada getStatusNotaFiscalEntrada() {
		return statusNotaFiscalEntrada;
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

	public Long getCodigoNota() {
		return codigoNota;
	}

	public void setCodigoNota(Long codigoNota) {
		this.codigoNota = codigoNota;
	}
	
}
