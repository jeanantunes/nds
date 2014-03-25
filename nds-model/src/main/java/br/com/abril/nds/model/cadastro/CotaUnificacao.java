package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
	private Cota cota;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinTable(name = "COTAUNIFICACAO_COTAUNIFICADA", joinColumns = {
			@JoinColumn(name = "COTA_UNIFICACAO_ID")},
			inverseJoinColumns = {@JoinColumn(name="COTA_UNIFICADA_ID")})
	private List<Cota> cotas;
	
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

	public List<Cota> getCotas() {
		return cotas;
	}

	public void setCotas(List<Cota> cotas) {
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
			
			this.cotas = new ArrayList<Cota>();
		}
        
        this.cotas.add(cota);
        
        if (cota.getCotasUnificacao() == null){
        	
        	cota.setCotasUnificacao(new HashSet<CotaUnificacao>());
        }
        
        cota.getCotasUnificacao().add(this);
	}
}
