package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.DiaSemana;

@Entity
@Table(name = "PARAMETRO_COBRANCA_TRANSPORTADOR")
@SequenceGenerator(name="PARAMETRO_COBRANCA_TRANSPORTADOR_SEQ", initialValue = 1, allocationSize = 1)
public class ParametroCobrancaTransportador implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5150610869351056930L;

	public enum PeriodicidadeCobranca{
		
		DIARIO("D", "Diário"),
		SEMANAL("S", "Semanal"),
		QUINZENAL("Q", "Semanal"),
		MENSAL("M", "Mensal");
		
		private String codigo;
		
		private String descricao;
		
		private PeriodicidadeCobranca(String codigo, String descricao){
			
			this.codigo = codigo;
			this.descricao = descricao;
		}

		public String getCodigo() {
			return codigo;
		}

		public void setCodigo(String codigo) {
			this.codigo = codigo;
		}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
	}
	
	public enum ModelidadeCobranca{
		
		TAXA_FIXA("TF", "Taxa Fixa"),
		PERCENTUAL("P", "Percentual");
		
		private String codigo;
		
		private String descricao;
		
		private ModelidadeCobranca(String codigo, String descricao){
			
			this.codigo = codigo;
			this.descricao = descricao;
		}

		public String getCodigo() {
			return codigo;
		}

		public void setCodigo(String codigo) {
			this.codigo = codigo;
		}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
	}
	
	@Id
	@GeneratedValue(generator = "PARAMETRO_COBRANCA_TRANSPORTADOR_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "MODELIDADE_COBRANCA")
	private ModelidadeCobranca modelidadeCobranca;
	
	@Column(name = "VALOR")
	private BigDecimal valor;
	
	@Column(name = "POR_ENTREGA")
	private boolean porEntrega;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "PERIODICIDADE_COBRANCA")
	private PeriodicidadeCobranca periodicidadeCobranca;
	
	@ElementCollection(targetClass = DiaSemana.class) 
	@CollectionTable(name = "DIA_COBRANCA_TRANSPORTADOR", joinColumns = @JoinColumn(name = "PARAMETRO_COBRANCA_TRANSPORTADOR_ID"))
	@Column(name = "DIA_ID")
	private List<DiaSemana> diasSemanaCobranca;
	
	@Column(name = "DIA_COBRANCA")
	private Integer diaCobranca;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ModelidadeCobranca getModelidadeCobranca() {
		return modelidadeCobranca;
	}

	public void setModelidadeCobranca(ModelidadeCobranca modelidadeCobranca) {
		this.modelidadeCobranca = modelidadeCobranca;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public boolean isPorEntrega() {
		return porEntrega;
	}

	public void setPorEntrega(boolean porEntrega) {
		this.porEntrega = porEntrega;
	}

	public PeriodicidadeCobranca getPeriodicidadeCobranca() {
		return periodicidadeCobranca;
	}

	public void setPeriodicidadeCobranca(PeriodicidadeCobranca periodicidadeCobranca) {
		this.periodicidadeCobranca = periodicidadeCobranca;
	}

	public List<DiaSemana> getDiasSemanaCobranca() {
		return diasSemanaCobranca;
	}

	public void setDiasSemanaCobranca(List<DiaSemana> diasSemanaCobranca) {
		this.diasSemanaCobranca = diasSemanaCobranca;
	}

	public Integer getDiaCobranca() {
		return diaCobranca;
	}

	public void setDiaCobranca(Integer diaCobranca) {
		this.diaCobranca = diaCobranca;
	}
}