package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.dto.filtro.FiltroRomaneioDTO.ColunaOrdenacaoRomaneio;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroConsultaConsignadoCotaDTO implements Serializable {

	private static final long serialVersionUID = -642561468775306010L;
	
	private Long idCota;
	
	private Long idFornecedor;
	
	@Export(label="Cota")
	private String nomeCota;
	
	@Export(label="Fornecedor")
	private String nomeFornecedor;
	
	private PaginacaoVO paginacao;
	
	private ColunaOrdenacaoConsultaConsignadoCota ordenacaoColuna;
	
	public enum ColunaOrdenacaoConsultaConsignadoCota {

		COTA("cota");
		
		private String nomeColuna;
		
		private ColunaOrdenacaoConsultaConsignadoCota(String nomeColuna) {
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

	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public Long getIdFornecedor() {
		return idFornecedor;
	}

	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	public ColunaOrdenacaoConsultaConsignadoCota getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	public void setOrdenacaoColuna(
			ColunaOrdenacaoConsultaConsignadoCota ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	
}
