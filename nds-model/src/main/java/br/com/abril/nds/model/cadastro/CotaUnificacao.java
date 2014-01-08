package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "COTA_UNIFICACAO")
@SequenceGenerator(name="COTA_UNIFICACAO_SEQ", initialValue = 1, allocationSize = 1)
public class CotaUnificacao implements Serializable {

	private static final long serialVersionUID = 177L;
	
	@Id
	@GeneratedValue(generator = "COTA_UNIFICACAO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@OneToOne
    @JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@OneToMany(mappedBy = "cotaUnificacao")
	private Set<Cota> cotas = new HashSet<Cota>();
	
	@Column(name = "DATA_UNIFICACAO")
	@Temporal(TemporalType.DATE)
	private Date dataUnificacao;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public Set<Cota> getCotas() {
		return cotas;
	}

	public void setCotas(Set<Cota> cotas) {
		this.cotas = cotas;
	}

	public Date getDataUnificacao() {
		return dataUnificacao;
	}

	public void setDataUnificacao(Date dataUnificacao) {
		this.dataUnificacao = dataUnificacao;
	}
	
	public void adicionarCota(Cota cota){
		
        if (this.cotas == null){
			
			this.cotas = new HashSet<Cota>();
		}
        
        this.cotas.add(cota);
	}
}
