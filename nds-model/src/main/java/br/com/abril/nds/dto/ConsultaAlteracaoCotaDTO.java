package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;

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

	public ConsultaAlteracaoCotaDTO(){
		
	}
	public ConsultaAlteracaoCotaDTO(Long idCota, Integer numeroCota, String clazz, String nomeRazaoSocial, String nome, Integer vencimento,
			BigDecimal financeiro, DescricaoTipoEntrega tipoEntrega, String box) {
		super();
		this.idCota = idCota;
		this.numeroCota = numeroCota;
		this.nomeRazaoSocial = (clazz.equals("J") ? nomeRazaoSocial : nome) ;
		this.vencimento = vencimento;
		if(financeiro != null){
			this.valorMinimo = financeiro.toString();
		} else {
			this.valorMinimo = BigDecimal.ZERO.toString();
		}	
		if(tipoEntrega != null) {
			this.tipoEntrega = tipoEntrega.getValue();
		} else {
			this.tipoEntrega ="";
		}	
		
		this.box = box;
	}
	
		
	public ConsultaAlteracaoCotaDTO(Long idCota, String nomeFornecedor, String tipoDesconto){
		this.idCota = idCota;
		this.nomeFornecedor = nomeFornecedor;
		if(tipoDesconto!= null){
			this.tipoDesconto = tipoDesconto;
		} else {
			this.tipoDesconto = "";
		}	
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
		if (tipoEntrega !=null) {
			this.tipoEntrega = DescricaoTipoEntrega.valueOf(tipoEntrega).getValue();
		}else{
			this.tipoEntrega = null;
		}
	}
	
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idCota == null) ? 0 : idCota.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConsultaAlteracaoCotaDTO other = (ConsultaAlteracaoCotaDTO) obj;
		if (idCota == null) {
			if (other.idCota != null)
				return false;
		} else if (!idCota.equals(other.idCota))
			return false;
		return true;
	}

	
}
