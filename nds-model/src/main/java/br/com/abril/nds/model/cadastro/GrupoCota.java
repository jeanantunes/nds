package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.TipoGrupo;
import br.com.abril.nds.model.cadastro.pdv.TipoCaracteristicaSegmentacaoPDV;
import br.com.abril.nds.model.dne.Localidade;

@Entity
@Table(name = "GRUPO_COTA")
@SequenceGenerator(name="GRUPO_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class GrupoCota implements Serializable {

	private static final long serialVersionUID = -1108598683189774129L;

	@Id
	@GeneratedValue(generator = "GRUPO_COTA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name="NOME")
	private String nome;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_GRUPO")
	private TipoGrupo tipoGrupo;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_COTA")
	private TipoCaracteristicaSegmentacaoPDV tipoCota;
	
	@ManyToMany
	@JoinTable(name = "GRUPO_MUNICIPIO", joinColumns = {@JoinColumn(name = "GRUPO_COTA_ID")}, 
	inverseJoinColumns = {@JoinColumn(name = "LOCALIDADE_ID")})
	private Set<Localidade> municipios;
	
	@ElementCollection(targetClass = DiaSemana.class) 
	@CollectionTable(name = "DIA_RECOLHIMENTO_GRUPO_COTA",
	    joinColumns = @JoinColumn(name = "GRUPO_ID"))
	@Column(name = "DIA_ID")
	protected Set<DiaSemana> diasRecolhimento; 
	
	@ManyToMany
	@JoinTable(name = "COTA_GRUPO", joinColumns = {@JoinColumn(name = "GRUPO_COTA_ID")}, 
	inverseJoinColumns = {@JoinColumn(name = "COTA_ID")})
	private Set<Cota> cotas;
	
	public GrupoCota() {
		
	}
	
	public GrupoCota(Long id, String nome, TipoGrupo tipoGrupo,
			Set<DiaSemana> diasRecolhimento,
			TipoCaracteristicaSegmentacaoPDV tipoCota,
			Set<Localidade> municipios, Set<Cota> cotas) {
		super();
		this.id = id;
		this.nome = nome;
		this.tipoGrupo = tipoGrupo;
		this.diasRecolhimento = diasRecolhimento;
		this.tipoCota = tipoCota;
		this.municipios = municipios;
		this.cotas = cotas;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the diasRecolhimento
	 */
	public Set<DiaSemana> getDiasRecolhimento() {
		return diasRecolhimento;
	}

	/**
	 * @param diasRecolhimento the diasRecolhimento to set
	 */
	public void setDiasRecolhimento(Set<DiaSemana> diasRecolhimento) {
		this.diasRecolhimento = diasRecolhimento;
	}

	/**
	 * @return the municipios
	 */
	public Set<Localidade> getMunicipios() {
		return municipios;
	}

	/**
	 * @param municipios the municipios to set
	 */
	public void setMunicipios(Set<Localidade> municipios) {
		this.municipios = municipios;
	}
	
	/**
	 * @return the tipoGrupo
	 */
	public TipoGrupo getTipoGrupo() {
		return tipoGrupo;
	}

	/**
	 * @param tipoGrupo the tipoGrupo to set
	 */
	public void setTipoGrupo(TipoGrupo tipoGrupo) {
		this.tipoGrupo = tipoGrupo;
	}

	/**
	 * @return the tipoCota
	 */
	public TipoCaracteristicaSegmentacaoPDV getTipoCota() {
		return tipoCota;
	}

	/**
	 * @param tipoCota the tipoCota to set
	 */
	public void setTipoCota(TipoCaracteristicaSegmentacaoPDV tipoCota) {
		this.tipoCota = tipoCota;
	}

	/**
	 * @return the cotas
	 */
	public Set<Cota> getCotas() {
		return cotas;
	}

	/**
	 * @param cotas the cotas to set
	 */
	public void setCotas(Set<Cota> cotas) {
		this.cotas = cotas;
	}

	
	
}
