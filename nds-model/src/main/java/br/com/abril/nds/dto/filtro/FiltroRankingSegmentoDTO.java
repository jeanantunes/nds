package br.com.abril.nds.dto.filtro;

import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroRankingSegmentoDTO extends FiltroDTO {
	
	@Export(label="De",widthPercent=10)
	private Date de;
	@Export(label="Até",widthPercent=10)
	private Date ate;
	private Long idTipoSegmento;
	@Export(label="Tipo Segmento",widthPercent=50)
	private String descricaoTipoSegmento;
	private BigDecimal totalFaturamento;
	
	public FiltroRankingSegmentoDTO() { }
	
	public FiltroRankingSegmentoDTO(Date de, Date ate, Long idTipoSegmento, Integer page, Integer rp, String sortOrder, String sortColumn) {
		this.de=de;
		this.ate=ate;
		this.idTipoSegmento=idTipoSegmento;
		this.paginacao = new PaginacaoVO(page, rp, sortOrder, sortColumn);
	}

	public Date getDe() {
		return de;
	}
	public void setDe(Date de) {
		this.de = de;
	}
	public Date getAte() {
		return ate;
	}
	public void setAte(Date ate) {
		this.ate = ate;
	}
	public Long getIdTipoSegmento() {
		return idTipoSegmento;
	}
	public void setIdTipoSegmento(Long idTipoSegmento) {
		this.idTipoSegmento = idTipoSegmento;
	}
	public String getDescricaoTipoSegmento() {
		return descricaoTipoSegmento;
	}
	public void setDescricaoTipoSegmento(String descricaoTipoSegmento) {
		this.descricaoTipoSegmento = descricaoTipoSegmento;
	}
	public BigDecimal getTotalFaturamento() {
		return totalFaturamento;
	}
	public void setTotalFaturamento(BigDecimal totalFaturamento) {
		this.totalFaturamento = totalFaturamento;
	}	
}
