package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.vo.PaginacaoVO;

public class FiltroCotaAusenteDTO implements Serializable {

	private static final long serialVersionUID = -2816898317696471483L;

	
	private Date data;
	
	private String box;
	
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
		this.data = data;
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
	
		
}
