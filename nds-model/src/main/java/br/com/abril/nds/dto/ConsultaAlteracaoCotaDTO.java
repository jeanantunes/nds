package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;

public class ConsultaAlteracaoCotaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long idCota;
	
	private Integer numeroCota;

	private String nomeRazaoSocial;

	private String nomeFornecedor;
	
	private TipoDesconto tipoDesconto;
	
	private Integer vencimento;
	
	private String valorMinimo;
	
	private DescricaoTipoEntrega tipoEntrega;
	
	private String box;

	
	public ConsultaAlteracaoCotaDTO(Long idCota, Integer numeroCota, String nomeRazaoSocial,
			String nomeFornecedor, TipoDesconto tipoDesconto, Integer vencimento,
			BigDecimal financeiro, DescricaoTipoEntrega tipoEntrega, String box) {
		super();
		this.idCota = idCota;
		this.numeroCota = numeroCota;
		this.nomeRazaoSocial = nomeRazaoSocial;
		this.nomeFornecedor = nomeFornecedor;
		if(tipoDesconto!= null)
			this.tipoDesconto = tipoDesconto;
		this.vencimento = vencimento;
		if(financeiro != null)
			this.valorMinimo = financeiro.toString();
		if(tipoEntrega != null)
			this.tipoEntrega = tipoEntrega;
		this.box = box;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeRazaoSocial() {
		return nomeRazaoSocial;
	}

	public void setNomeRazaoSocial(String nomeRazaoSocial) {
		this.nomeRazaoSocial = nomeRazaoSocial;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public TipoDesconto getVrDesconto() {
		return tipoDesconto;
	}

	public void setVrDesconto(TipoDesconto vrDesconto) {
		this.tipoDesconto = vrDesconto;
	}

	public Integer getVencimento() {
		return vencimento;
	}

	public void setVencimento(Integer vencimento) {
		this.vencimento = vencimento;
	}

	public String getValorMinimo() {
		return valorMinimo;
	}

	public void setValorMinimo(String valorMinimo) {
		this.valorMinimo = valorMinimo;
	}

	public DescricaoTipoEntrega getTipoEntrega() {
		return tipoEntrega;
	}

	public void setTipoEntrega(DescricaoTipoEntrega tipoEntrega) {
		this.tipoEntrega = tipoEntrega;
	}

	public String getBox() {
		return box;
	}

	public void setBox(String box) {
		this.box = box;
	}

	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public TipoDesconto getTipoDesconto() {
		return tipoDesconto;
	}

	public void setTipoDesconto(TipoDesconto tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}
	
}
