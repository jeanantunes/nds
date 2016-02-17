package br.com.abril.nds.dto.filtro;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroRankingSegmentoDTO extends FiltroCurvaABCDTO {
	
	private static final long serialVersionUID = -3796675565765372133L;
	
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
		this.setDataDe(de);
		this.setDataAte(ate);
		this.idTipoSegmento=idTipoSegmento;
		this.setPaginacao(new PaginacaoVO(page, rp, sortOrder, sortColumn)); 
	}
	
	public FiltroRankingSegmentoDTO(Date de, Date ate, Long idTipoSegmento, Long codigoFornecedor, String codigoProduto, 
									String nomeProduto, List<Long> edicaoProduto, Long codigoEditor, Integer codigoCota,
									String nomeCota, String municipio, Long regiaoID, Integer page, Integer rp, 
									String sortOrder, String sortColumn) {
		this.de=de;
		this.ate=ate;
		this.idTipoSegmento=idTipoSegmento;
		this.setDataDe(de);
		this.setDataAte(ate);
		this.setCodigoFornecedor(codigoFornecedor != null ? codigoFornecedor.toString() : null);
		this.setCodigoProduto(codigoProduto);
		this.setNomeProduto(nomeProduto);
		this.setEdicaoProduto(edicaoProduto);
		this.setCodigoEditor(codigoEditor != null ? codigoEditor.toString():null);
		this.setCodigoCota(codigoCota);
		this.setNomeCota(nomeCota);
		this.setMunicipio(municipio);
		this.setRegiaoID(regiaoID);
		this.setPaginacao(new PaginacaoVO(page, rp, sortOrder, sortColumn)); 
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
