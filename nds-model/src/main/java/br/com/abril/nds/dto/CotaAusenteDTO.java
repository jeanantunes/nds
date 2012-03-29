package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.vo.PaginacaoVO;

public class CotaAusenteDTO implements Serializable{
	
	private static final long serialVersionUID = -5403191577161993585L;
	
	private Date data;
	
	private String box;
	
	private Integer cota;
	
	private String nome;
	
	private BigDecimal valorNe;
	
	private PaginacaoVO paginacao = new PaginacaoVO();
	
	private	ColunaOrdenacao colunaOrdenacao;
	
	
	
	public CotaAusenteDTO(Date data, String box, Integer cota, String nome,
			BigDecimal valorNe, PaginacaoVO paginacao,
			ColunaOrdenacao colunaOrdenacao) {
		super();
		this.data = data;
		this.box = box;
		this.cota = cota;
		this.nome = nome;
		this.valorNe = valorNe;		
	}
	public CotaAusenteDTO(){
		
	}
	
	public enum ColunaOrdenacao {

		data("data"),
		box("box"),
		cota("cota"),
		nome("nome"),
		valorNE("valorNE");	

		private String nomeColuna;
		
		private ColunaOrdenacao(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
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
	public Integer getCota() {
		return cota;
	}
	public void setCota(Integer cota) {
		this.cota = cota;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public BigDecimal getValorNe() {
		return valorNe;
	}
	public void setValorNe(BigDecimal valorNe) {
		this.valorNe = valorNe;
	}

	/*@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((box == null) ? 0 : box.hashCode());
		result = prime * result + ((cota == null) ? 0 : cota.hashCode());
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
		CotaAusenteDTO other = (CotaAusenteDTO) obj;
		if (box == null) {
			if (other.box != null)
				return false;
		} else if (!box.equals(other.box))
			return false;
		if (cota == null) {
			if (other.cota != null)
				return false;
		} else if (!cota.equals(other.cota))
			return false;
		return true;
	}*/
	
	
}
