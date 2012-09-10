package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FORMA_COBRANCA_CAUCAO_LIQUIDA")
@SequenceGenerator(name="FORMA_COBRANCA_CAUCAO_LIQUIDA_SEQ", initialValue = 1, allocationSize = 1)
public class FormaCobrancaCaucaoLiquida implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2405188133540434350L;

	@Id
	@GeneratedValue(generator = "FORMA_COBRANCA_CAUCAO_LIQUIDA_SEQ")
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_FORMA_COBRANCA", nullable = false)
	private TipoFormaCobranca tipoFormaCobranca;
			
	@ElementCollection
	private List<Integer> diasDoMes; 
	
	@OneToMany(mappedBy="formaCobrancaCaucaoLiquida")
	@OrderBy("codigoDiaSemana ASC")
	private Set<ConcentracaoCobrancaCaucaoLiquida> concentracaoCobrancaCaucaoLiquida = new HashSet<ConcentracaoCobrancaCaucaoLiquida>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoFormaCobranca getTipoFormaCobranca() {
		return tipoFormaCobranca;
	}

	public void setTipoFormaCobranca(TipoFormaCobranca tipoFormaCobranca) {
		this.tipoFormaCobranca = tipoFormaCobranca;
	}

	public List<Integer> getDiasDoMes() {
		return diasDoMes;
	}

	public void setDiasDoMes(List<Integer> diasDoMes) {
		this.diasDoMes = diasDoMes;
	}

	public Set<ConcentracaoCobrancaCaucaoLiquida> getConcentracaoCobrancaCaucaoLiquida() {
		return concentracaoCobrancaCaucaoLiquida;
	}

	public void setConcentracaoCobrancaCaucaoLiquida(
			Set<ConcentracaoCobrancaCaucaoLiquida> concentracaoCobrancaCaucaoLiquida) {
		this.concentracaoCobrancaCaucaoLiquida = concentracaoCobrancaCaucaoLiquida;
	}

}
