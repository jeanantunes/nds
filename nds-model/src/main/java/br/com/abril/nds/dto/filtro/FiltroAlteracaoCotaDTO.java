package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroAlteracaoCotaDTO extends FiltroDTO implements Serializable {

	private Long idCota;
	private Integer numeroCota; 
	private String nomeCota; 
	private Long idFornecedor;
	private Integer idBairro;
	private String idMunicipio; 
	private String idTpDesconto; 
	private TipoDesconto idTpEntrega;
	private Long idVrMinimo;
	
	private FiltroModalFornecedor filtroModalFornecedor = new FiltroModalFornecedor();
	private FiltroModalFinanceiro filtroModalFinanceiro = new FiltroModalFinanceiro();
	private FiltroModalDistribuicao filtroModalDistribuicao = new FiltroModalDistribuicao();
	
	private List<String> listaLinhaSelecao = new ArrayList<String>(); 
	
	public Integer getNumeroCota() {
		return numeroCota;
	}
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	public String getNomeCota() {
		return nomeCota;
	}
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
	public Long getIdFornecedor() {
		return idFornecedor;
	}
	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}
	public Integer getIdBairro() {
		return idBairro;
	}
	public void setIdBairro(Integer idBairro) {
		this.idBairro = idBairro;
	}
	public String getIdMunicipio() {
		return idMunicipio;
	}
	public void setIdMunicipio(String idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	public String getIdTpDesconto() {
		return idTpDesconto;
	}
	public void setIdTpDesconto(String idTpDesconto) {
		this.idTpDesconto = idTpDesconto;
	}
	public TipoDesconto getIdTpEntrega() {
		return idTpEntrega;
	}
	public void setIdTpEntrega(TipoDesconto idTpEntrega) {
		this.idTpEntrega = idTpEntrega;
	}
	public List<String> getListaLinhaSelecao() {
		return listaLinhaSelecao;
	}
	public void setListaLinhaSelecao(List<String> listaLinhaSelecao) {
		this.listaLinhaSelecao = listaLinhaSelecao;
	}
	
	public FiltroModalFornecedor getFiltroModalFornecedor() {
		return filtroModalFornecedor;
	}
	public void setFiltroModalFornecedor(FiltroModalFornecedor filtroModalFornecedor) {
		this.filtroModalFornecedor = filtroModalFornecedor;
	}

	public FiltroModalFinanceiro getFiltroModalFinanceiro() {
		return filtroModalFinanceiro;
	}
	public void setFiltroModalFinanceiro(FiltroModalFinanceiro filtroModalFinanceiro) {
		this.filtroModalFinanceiro = filtroModalFinanceiro;
	}
	public FiltroModalDistribuicao getFiltroModalDistribuicao() {
		return filtroModalDistribuicao;
	}
	public void setFiltroModalDistribuicao(
			FiltroModalDistribuicao filtroModalDistribuicao) {
		this.filtroModalDistribuicao = filtroModalDistribuicao;
	}
	public Long getIdCota() {
		return idCota;
	}
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}
	public Long getIdVrMinimo() {
		return idVrMinimo;
	}
	public void setIdVrMinimo(Long idVrMinimo) {
		this.idVrMinimo = idVrMinimo;
	}
	
	
}
