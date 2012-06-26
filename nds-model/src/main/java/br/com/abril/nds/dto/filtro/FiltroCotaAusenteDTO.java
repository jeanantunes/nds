package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroCotaAusenteDTO implements Serializable {

	private static final long serialVersionUID = -2816898317696471483L;

	@Export(label = "Data")
	private Date data;
	@Export(label = "Box")
	
	private String box;
	@Export(label = "Cota")
	private Integer numCota;
	
	private PaginacaoVO paginacao;
	
	private	ColunaOrdenacao colunaOrdenacao;
	
	public FiltroCotaAusenteDTO(){
		
	}
	
	public FiltroCotaAusenteDTO(Date data, String box, Integer numCota,
			PaginacaoVO paginacao, ColunaOrdenacao colunaOrdenacao) {
		super();
		this.data = data;
		this.box = box;
		this.numCota = numCota;
		this.paginacao = paginacao;
		this.colunaOrdenacao = colunaOrdenacao;
	}

	public enum ColunaOrdenacao {

		data("data"),
		box("box"),
		cota("cota"),
		nome("nome"),
		valorNe("valorNe");	

		private String nomeColuna;
		
		private ColunaOrdenacao(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
			
		this.data = DateUtil.removerTimestamp(data);
	}

	public String getBox() {
		return box;
	}

	public void setBox(String box) {
		this.box = box;
	}

	public Integer getNumCota() {
		return numCota;
	}

	public void setNumCota(Integer numCota) {
		this.numCota = numCota;
	}

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	public ColunaOrdenacao getColunaOrdenacao() {
		return colunaOrdenacao;
	}

	public void setColunaOrdenacao(ColunaOrdenacao colunaOrdenacao) {
		this.colunaOrdenacao = colunaOrdenacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((box == null) ? 0 : box.hashCode());
		result = prime * result
				+ ((colunaOrdenacao == null) ? 0 : colunaOrdenacao.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((numCota == null) ? 0 : numCota.hashCode());
		result = prime * result
				+ ((paginacao == null) ? 0 : paginacao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FiltroCotaAusenteDTO other = (FiltroCotaAusenteDTO) obj;
		if (box == null) {
			if (other.box != null)
				return false;
		} else if (!box.equals(other.box))
			return false;
		if (colunaOrdenacao != other.colunaOrdenacao)
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (numCota == null) {
			if (other.numCota != null)
				return false;
		} else if (!numCota.equals(other.numCota))
			return false;
		if (paginacao == null) {
			if (other.paginacao != null)
				return false;
		} else if (!paginacao.equals(other.paginacao))
			return false;
		return true;
	}
	
		
}
