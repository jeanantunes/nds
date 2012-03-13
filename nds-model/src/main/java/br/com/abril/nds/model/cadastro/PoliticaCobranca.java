package br.com.abril.nds.model.cadastro;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "POLITICA_COBRANCA")
@SequenceGenerator(name="POLITICA_COBRANCA_SEQ", initialValue = 1, allocationSize = 1)
public class PoliticaCobranca {
	
	@Id
	@GeneratedValue(generator = "POLITICA_COBRANCA_SEQ")
	@Column(name = "ID")
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_COBRANCA", nullable = false)
	private TipoCobranca tipoCobranca;
	@Column(name = "VALOR_MINIMO", nullable = false)
	private BigDecimal valorMinino;
	@Column(name = "NUM_INADIMPLENCIA_SUSP", nullable = false)
	private int inadimplenciasSuspencao;
	@OneToOne(mappedBy = "politicaCobranca")
	private Distribuidor distribuidor;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}
	
	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}
	
	public BigDecimal getValorMinino() {
		return valorMinino;
	}
	
	public void setValorMinino(BigDecimal valorMinino) {
		this.valorMinino = valorMinino;
	}
	
	public int getInadimplenciasSuspencao() {
		return inadimplenciasSuspencao;
	}
	
	public void setInadimplenciasSuspencao(int inadimplenciasSuspencao) {
		this.inadimplenciasSuspencao = inadimplenciasSuspencao;
	}
	
	public Distribuidor getDistribuidor() {
		return distribuidor;
	}
	
	public void setDistribuidor(Distribuidor distribuidor) {
		this.distribuidor = distribuidor;
	}

}
