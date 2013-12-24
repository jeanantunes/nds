package br.com.abril.nds.dto.filtro;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroViewNotaFiscalDTO {

	private Integer intervaloBoxInicial;
	
	private Integer intervaloBoxFinal;
	
	private Integer intervalorCotaInicial;
	
	private Integer intervalorCotaFinal;
	
	private Date dataInicial;
	
	private Date dataFinal; 
	
	private Date dataEmissao; 
	
	private Long idTipoNotaFiscal;
	
	private Long idRoteiro; 
	
	private Long idRota; 
	
	private String sortname; 
	
	private String sortorder; 
	
	private Integer rp; 
	
	private Integer page; 
	
	private List<Long> listIdFornecedor; 
	
	private SituacaoCadastro situacaoCadastro;

	public Integer getIntervaloBoxInicial() {
		return intervaloBoxInicial;
	}

	public void setIntervaloBoxInicial(Integer intervaloBoxInicial) {
		this.intervaloBoxInicial = intervaloBoxInicial;
	}

	public Integer getIntervaloBoxFinal() {
		return intervaloBoxFinal;
	}

	public void setIntervaloBoxFinal(Integer intervaloBoxFinal) {
		this.intervaloBoxFinal = intervaloBoxFinal;
	}

	public Integer getIntervalorCotaInicial() {
		return intervalorCotaInicial;
	}

	public void setIntervalorCotaInicial(Integer intervalorCotaInicial) {
		this.intervalorCotaInicial = intervalorCotaInicial;
	}

	public Integer getIntervalorCotaFinal() {
		return intervalorCotaFinal;
	}

	public void setIntervalorCotaFinal(Integer intervalorCotaFinal) {
		this.intervalorCotaFinal = intervalorCotaFinal;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Long getIdTipoNotaFiscal() {
		return idTipoNotaFiscal;
	}

	public void setIdTipoNotaFiscal(Long idTipoNotaFiscal) {
		this.idTipoNotaFiscal = idTipoNotaFiscal;
	}

	public Long getIdRoteiro() {
		return idRoteiro;
	}

	public void setIdRoteiro(Long idRoteiro) {
		this.idRoteiro = idRoteiro;
	}

	public Long getIdRota() {
		return idRota;
	}

	public void setIdRota(Long idRota) {
		this.idRota = idRota;
	}

	public String getSortname() {
		return sortname;
	}

	public void setSortname(String sortname) {
		this.sortname = sortname;
	}

	public String getSortorder() {
		return sortorder;
	}

	public void setSortorder(String sortorder) {
		this.sortorder = sortorder;
	}

	public Integer getRp() {
		return rp;
	}

	public void setRp(Integer rp) {
		this.rp = rp;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public List<Long> getListIdFornecedor() {
		return listIdFornecedor;
	}

	public void setListIdFornecedor(List<Long> listIdFornecedor) {
		this.listIdFornecedor = listIdFornecedor;
	}

	public SituacaoCadastro getSituacaoCadastro() {
		return situacaoCadastro;
	}

	public void setSituacaoCadastro(SituacaoCadastro situacaoCadastro) {
		this.situacaoCadastro = situacaoCadastro;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}
}
