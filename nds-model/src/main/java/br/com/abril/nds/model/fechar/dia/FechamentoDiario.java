package br.com.abril.nds.model.fechar.dia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "FECHAMENTO_DIARIO")
@SequenceGenerator(name = "FECHAMENTO_DIARIO_SEQ", initialValue = 1, allocationSize = 1)
public class FechamentoDiario implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "FECHAMENTO_DIARIO_SEQ")
    @Column(name = "ID")
    private Long id;
	
	@Column(name="DATA_FECHAMENTO",unique=true)
	@Temporal(TemporalType.DATE)
	private Date dataFechamento; 
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;
	
	@OneToOne(mappedBy = "fechamentoDiario")
	private FechamentoDiarioConsolidadoReparte consolidadoReparte;
	
	@OneToOne(mappedBy = "fechamentoDiario")
	private FechamentoDiarioConsolidadoEncalhe consolidadoEncalhe;
	
	@OneToOne(mappedBy = "fechamentoDiario")
	private FechamentoDiarioConsolidadoSuplementar consolidadoSuplementar;
	
	@OneToMany(mappedBy = "fechamentoDiario")
	private List<FechamentoDiarioConsolidadoDivida> consolidadoDividas;
	
	@OneToOne(mappedBy = "fechamentoDiario")
	private FechamentoDiarioConsolidadoCota consolidadoCota;
	
	@OneToMany(mappedBy = "fechamentoDiario")
	private List<FechamentoDiarioConsignado> consolidadoResumoConsignado;
	
	@OneToMany(mappedBy = "fechamentoDiario")
	private List<FechamentoDiarioResumoEstoque> consolidadoResumoEstoque;
	
    @OneToMany(mappedBy = "fechamentoDiario")
    @Cascade(CascadeType.ALL)
	private List<FechamentoDiarioDiferenca> diferencas;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public FechamentoDiarioConsolidadoReparte getConsolidadoReparte() {
		return consolidadoReparte;
	}

	public void setConsolidadoReparte(FechamentoDiarioConsolidadoReparte consolidadoReparte) {
		this.consolidadoReparte = consolidadoReparte;
	}

	public FechamentoDiarioConsolidadoEncalhe getConsolidadoEncalhe() {
		return consolidadoEncalhe;
	}

	public void setConsolidadoEncalhe(
			FechamentoDiarioConsolidadoEncalhe consolidadoEncalhe) {
		this.consolidadoEncalhe = consolidadoEncalhe;
	}

	public FechamentoDiarioConsolidadoSuplementar getConsolidadoSuplementar() {
		return consolidadoSuplementar;
	}

	public void setConsolidadoSuplementar(
			FechamentoDiarioConsolidadoSuplementar consolidadoSuplementar) {
		this.consolidadoSuplementar = consolidadoSuplementar;
	}

	public List<FechamentoDiarioConsolidadoDivida> getConsolidadoDividas() {
		return consolidadoDividas;
	}

	public void setConsolidadoDividas(
			List<FechamentoDiarioConsolidadoDivida> consolidadoDividas) {
		this.consolidadoDividas = consolidadoDividas;
	}

	public FechamentoDiarioConsolidadoCota getConsolidadoCota() {
		return consolidadoCota;
	}

	public void setConsolidadoCota(
			FechamentoDiarioConsolidadoCota consolidadoCota) {
		this.consolidadoCota = consolidadoCota;
	}

	public List<FechamentoDiarioConsignado> getConsolidadoResumoConsignado() {
		return consolidadoResumoConsignado;
	}

	public void setConsolidadoResumoConsignado(
			List<FechamentoDiarioConsignado> consolidadoResumoConsignado) {
		this.consolidadoResumoConsignado = consolidadoResumoConsignado;
	}

	public List<FechamentoDiarioResumoEstoque> getConsolidadoResumoEstoque() {
		return consolidadoResumoEstoque;
	}

	public void setConsolidadoResumoEstoque(
			List<FechamentoDiarioResumoEstoque> consolidadoResumoEstoque) {
		this.consolidadoResumoEstoque = consolidadoResumoEstoque;
	}

    /**
     * @return the diferencas
     */
    public List<FechamentoDiarioDiferenca> getDiferencas() {
        return diferencas;
    }

    /**
     * @param diferencas the diferencas to set
     */
    public void setDiferencas(List<FechamentoDiarioDiferenca> diferencas) {
        this.diferencas = diferencas;
    }
    
    /**
     * Adiciona uma diferença ao fechamento diário
     * 
     * @param diferenca diferença para inclusão
     */
    public void addDiferenca(FechamentoDiarioDiferenca diferenca) {
        if (diferencas == null) {
            diferencas = new ArrayList<>();
        }
        diferenca.setFechamentoDiario(this);
        diferencas.add(diferenca);
    }
	
}
