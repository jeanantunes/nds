package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.fiscal.NotaFiscalTipoEmissaoRegimeEspecial;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroNFeDTO implements Serializable {
	
	private static final long serialVersionUID = 4377811331164817630L;

	private Integer intervaloBoxInicial;
	
	private Integer intervaloBoxFinal;
	
	private Integer intervalorCotaInicial;
	
	private Integer intervalorCotaFinal;
	
	private Boolean notaFiscalDevolucaoSimbolica;
	
	private Boolean notaFiscalVendaConsignado;
	
	private Date dataInicial;
	
	private Date dataFinal; 
	
	private Date dataEmissao; 
	
	private Long idNaturezaOperacao;
	
	private Long idRoteiro; 
	
	private Long idRota;  
	
	private Long idCota;
	
	private Long idFornecedor;

	private Boolean emissaoPorEditor;
	
	private Boolean emissaoPorDestinacaoEncalhe;
	
	private Set<Long> idsCota;
	
	private List<Long> listIdFornecedor; 
	
	private NotaFiscalTipoEmissaoRegimeEspecial notaFiscalTipoEmissaoRegimeEspecial;
	
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

	public Boolean isNotaFiscalDevolucaoSimbolica() {
		return notaFiscalDevolucaoSimbolica;
	}

	public void setNotaFiscalDevolucaoSimbolica(Boolean notaFiscalDevolucaoSimbolica) {
		this.notaFiscalDevolucaoSimbolica = notaFiscalDevolucaoSimbolica;
	}

	public Boolean isNotaFiscalVendaConsignado() {
		return notaFiscalVendaConsignado;
	}

	public void setNotaFiscalVendaConsignado(Boolean notaFiscalVendaConsignado) {
		this.notaFiscalVendaConsignado = notaFiscalVendaConsignado;
	}

	public NotaFiscalTipoEmissaoRegimeEspecial getNotaFiscalTipoEmissaoRegimeEspecial() {
		return notaFiscalTipoEmissaoRegimeEspecial;
	}

	public void setNotaFiscalTipoEmissaoRegimeEspecial(
			NotaFiscalTipoEmissaoRegimeEspecial notaFiscalTipoEmissaoRegimeEspecial) {
		this.notaFiscalTipoEmissaoRegimeEspecial = notaFiscalTipoEmissaoRegimeEspecial;
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

	public Set<Long> getIdsCota() {
		return idsCota;
	}

	public void setIdsCota(Set<Long> idsCota) {
		this.idsCota = idsCota;
	}

	public Long getIdFornecedor() {
		return idFornecedor;
	}

	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	public Boolean isEmissaoPorEditor() {
		return emissaoPorEditor;
	}

	public void setEmissaoPorEditor(Boolean emissaoPorEditor) {
		this.emissaoPorEditor = emissaoPorEditor;
	}

	public Boolean isEmissaoPorDestinacaoEncalhe() {
		return emissaoPorDestinacaoEncalhe;
	}

	public void setEmissaoPorDestinacaoEncalhe(Boolean emissaoPorDestinacaoEncalhe) {
		this.emissaoPorDestinacaoEncalhe = emissaoPorDestinacaoEncalhe;
	}

	public NotaFiscalTipoEmissaoRegimeEspecial getNotaFiscalTipoEmissao() {
		return notaFiscalTipoEmissaoRegimeEspecial;
	}

	public void setNotaFiscalTipoEmissao(NotaFiscalTipoEmissaoRegimeEspecial notaFiscalTipoEmissaoRegimeEspecial) {
		this.notaFiscalTipoEmissaoRegimeEspecial = notaFiscalTipoEmissaoRegimeEspecial;
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
