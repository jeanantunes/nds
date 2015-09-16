package br.com.abril.nds.model.cadastro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.estoque.CobrancaControleConferenciaEncalheCota;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;
import br.com.abril.nds.model.seguranca.Usuario;

/**
 * Entidade  usada para gerar relatorios de vendas/reparte agregados por uma cota master (
 *  (caso caruso )
 * 
 * 
 * @author Odemir
 */


@Entity
@Table(name = "CONTROLE_COTA")
@SequenceGenerator(name = "CONTROLE_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class ControleCota {

	@Id
	@GeneratedValue(generator = "CONTROLE_COTA_SEQ")
	@Column(name = "ID")
	private Long id;

	

	@Column(name="NUMERO_COTA_MASTER")
	private Integer numeroCotaMaster;
	
	@Column(name="NUMERO_COTA")
	private Integer numeroCota;
	
	@Column(name="SITUACAO")
	private String situacao;
	
	
	
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	
	public String getSituacao() {
		return situacao;
	}

	
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	
	public Integer getNumeroCotaMaster() {
		return numeroCotaMaster;
	}

	public void setNumeroCotaMaster(Integer numeroCotaMaster) {
		this.numeroCotaMaster= numeroCotaMaster;
	}
	
	
	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota= numeroCota;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : getId().hashCode());
		result = prime * result + ((this.getNumeroCotaMaster() == null) ? 0 : this.getNumeroCotaMaster().hashCode());
		result = prime * result + ((this.getNumeroCota() == null) ? 0 : this.getNumeroCota().hashCode());
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
		Cota other = (Cota) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		
		
		return true;
	}


	
}
