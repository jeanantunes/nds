package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="TRIBUTOS_ALIQUOTAS")
public class TributoAliquota implements Serializable {

	public enum TipoAliquota {
		PERCENTUAL("Percentual", "%"),
		VALOR("Valor", "$");
		
		String descricao;
		
		String simbolo;
		
		TipoAliquota(String descricao, String simbolo) {
			this.descricao = descricao;
			this.simbolo = simbolo;		
		}

		public String getDescricao() {
			return descricao;
		}

		public String getSimbolo() {
			return simbolo;
		}
		
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@OneToOne
	@JoinColumn(name="tributo_id")
	private Tributo tributo;
	
	@Enumerated(EnumType.STRING)
	@Column(name="tipo_aliquota")
	private TipoAliquota tipoAliquota;
	
	@Column(name="valor")
	private BigDecimal valor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Tributo getTributo() {
		return tributo;
	}

	public void setTributo(Tributo tributo) {
		this.tributo = tributo;
	}

	public TipoAliquota getTipoAliquota() {
		return tipoAliquota;
	}

	public void setTipoAliquota(TipoAliquota tipoAliquota) {
		this.tipoAliquota = tipoAliquota;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public String getNomeTributo() {
		return tributo != null ? tributo.getNome() : "";
	}
}