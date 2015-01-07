package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.enums.OpcoesFiltro;
import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroAlteracaoCotaDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = -9068411972805292601L;
	
	private Long idCota;
	
	private Integer numeroCota; 
	
	private String nomeCota; 
	
	private Long idFornecedor;
	
	private String idBairro;
	
	private String idMunicipio; 
	
	private DescricaoTipoEntrega descricaoTipoEntrega; 
	
	private TipoDesconto tipoDesconto;
	
	private OpcoesFiltro utilizaParametroCobrancaDistribuidor;
	
	private BigDecimal idVrMinimo;
	
	private Integer idVencimento;
	
	private FiltroModalFornecedor filtroModalFornecedor = new FiltroModalFornecedor();
	
	private FiltroModalFinanceiro filtroModalFinanceiro = new FiltroModalFinanceiro();
	
	private FiltroModalDistribuicao filtroModalDistribuicao = new FiltroModalDistribuicao();
	
	private List<Long> listaLinhaSelecao = new ArrayList<Long>(); 
	
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
	public String getIdBairro() {
		return idBairro;
	}
	public void setIdBairro(String bairro) {
		this.idBairro = bairro;
	}
	public String getIdMunicipio() {
		return idMunicipio;
	}
	public void setIdMunicipio(String idMunicipio) {
		this.idMunicipio = idMunicipio;
	}

	public List<Long> getListaLinhaSelecao() {
		return listaLinhaSelecao;
	}
	public void setListaLinhaSelecao(List<Long> listaLinhaSelecao) {
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
	public BigDecimal getIdVrMinimo() {
		return idVrMinimo;
	}
	public void setIdVrMinimo(BigDecimal idVrMinimo) {
		this.idVrMinimo = idVrMinimo;
	}
	public DescricaoTipoEntrega getDescricaoTipoEntrega() {
		return descricaoTipoEntrega;
	}
	public void setDescricaoTipoEntrega(DescricaoTipoEntrega descricaoTipoEntrega) {
		this.descricaoTipoEntrega = descricaoTipoEntrega;
	}
	public TipoDesconto getTipoDesconto() {
		return tipoDesconto;
	}
	public void setTipoDesconto(TipoDesconto tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}
	public OpcoesFiltro getUtilizaParametroCobrancaDistribuidor() {
		return utilizaParametroCobrancaDistribuidor;
	}
	public void setUtilizaParametroCobrancaDistribuidor(OpcoesFiltro utilizaParametroCobrancaDistribuidor) {
		this.utilizaParametroCobrancaDistribuidor = utilizaParametroCobrancaDistribuidor;
	}
	public Integer getIdVencimento() {
		return idVencimento;
	}
	public void setIdVencimento(Integer idVencimento) {
		this.idVencimento = idVencimento;
	}

	
	
}
