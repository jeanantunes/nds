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
	
	private String tipoDesconto;
	
	private Integer vencimento;
	
	private String valorMinimo;
	
	private String tipoEntrega;
	
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
			this.tipoDesconto = tipoDesconto.getDescricao();
		this.vencimento = vencimento;
		if(financeiro != null)
			this.valorMinimo = financeiro.toString();
		if(tipoEntrega != null)
			this.tipoEntrega = tipoEntrega.getValue();
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

	public String getTipoDesconto() {
		return tipoDesconto;
	}

	public void setTipoDesconto(String tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}

	public String getTipoEntrega() {
		return tipoEntrega;
	}

	public void setTipoEntrega(String tipoEntrega) {
		this.tipoEntrega = tipoEntrega;
	}

	
}
