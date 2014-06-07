package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.cadastro.NotaFiscalTipoEmissao.NotaFiscalTipoEmissaoRegimeEspecial;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroNFeDTO implements Serializable {
	
	private static final long serialVersionUID = 4377811331164817630L;

	private Integer intervaloBoxInicial;
	
	private Integer intervaloBoxFinal;
	
	private Integer intervalorCotaInicial;
	
	private Integer intervalorCotaFinal;
	
	private Date dataInicial;
	
	private Date dataFinal; 
	
	private Date dataEmissao; 
	
	private Long idNaturezaOperacao;
	
	private Long idRoteiro; 
	
	private Long idRota;  
	
	private Long idCota;
	
	private Long idFornecedor;
	
	private NotaFiscalTipoEmissaoRegimeEspecial notaFiscalTipoEmissao;
	
	private List<Long> listIdFornecedor; 
	
	private SituacaoCadastro situacaoCadastro;

	private PaginacaoVO paginacaoVO;
	
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

	public NotaFiscalTipoEmissaoRegimeEspecial getNotaFiscalTipoEmissao() {
		return notaFiscalTipoEmissao;
	}

	public void setNotaFiscalTipoEmissao(NotaFiscalTipoEmissaoRegimeEspecial notaFiscalTipoEmissao) {
		this.notaFiscalTipoEmissao = notaFiscalTipoEmissao;
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

	public Long getIdNaturezaOperacao() {
		return idNaturezaOperacao;
	}

	public void setIdNaturezaOperacao(Long idNaturezaOperacao) {
		this.idNaturezaOperacao = idNaturezaOperacao;
	}
	
	public PaginacaoVO getPaginacaoVO() {
		return paginacaoVO;
	}

	public void setPaginacaoVO(PaginacaoVO paginacaoVO) {
		this.paginacaoVO = paginacaoVO;
	}
}
