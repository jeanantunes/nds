package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroViewPainelMonitorDTO implements Serializable {

	private static final long serialVersionUID = 4377811331164817630L;

	private String tipoDocumento;
	private Integer box;
	private String dataInicial;
	private String dataFinal;
	private String documento;
	private String tipoNfe;
	private Long numeroInicial;
	private Long numeroFinal;
	private String chaveAcesso;
	private String situacaoNfe;
	private Integer serieNfe;
	private PaginacaoVO paginacaoVO;
	
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public Integer getBox() {
		return box;
	}

	public void setBox(Integer box) {
		this.box = box;
	}

	public String getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(String dataInicial) {
		this.dataInicial = dataInicial;
	}

	public String getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getTipoNfe() {
		return tipoNfe;
	}

	public void setTipoNfe(String tipoNfe) {
		this.tipoNfe = tipoNfe;
	}

	public Long getNumeroInicial() {
		return numeroInicial;
	}

	public void setNumeroInicial(Long numeroInicial) {
		this.numeroInicial = numeroInicial;
	}

	public Long getNumeroFinal() {
		return numeroFinal;
	}

	public void setNumeroFinal(Long numeroFinal) {
		this.numeroFinal = numeroFinal;
	}

	public String getChaveAcesso() {
		return chaveAcesso;
	}

	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}

	public String getSituacaoNfe() {
		return situacaoNfe;
	}

	public void setSituacaoNfe(String situacaoNfe) {
		this.situacaoNfe = situacaoNfe;
	}

	public Integer getSerieNfe() {
		return serieNfe;
	}

	public void setSerieNfe(Integer serieNfe) {
		this.serieNfe = serieNfe;
	}

	public PaginacaoVO getPaginacaoVO() {
		return paginacaoVO;
	}

	public void setPaginacaoVO(PaginacaoVO paginacaoVO) {
		this.paginacaoVO = paginacaoVO;
	}
}
