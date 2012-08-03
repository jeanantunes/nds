package br.com.abril.nds.dto;

public class ProdutoServicoDTO {
	
	private Long idNotaFiscal;
	
	private Integer sequencia;
	
	public ProdutoServicoDTO() {}
	
	public ProdutoServicoDTO(Long idNotaFiscal, Integer sequencia) {
		
		this.idNotaFiscal = idNotaFiscal;
		this.sequencia = sequencia;
		
	}

	public Long getIdNotaFiscal() {
		return idNotaFiscal;
	}

	public void setIdNotaFiscal(Long idNotaFiscal) {
		this.idNotaFiscal = idNotaFiscal;
	}

	public Integer getSequencia() {
		return sequencia;
	}

	public void setSequencia(Integer sequencia) {
		this.sequencia = sequencia;
	}
	
}
