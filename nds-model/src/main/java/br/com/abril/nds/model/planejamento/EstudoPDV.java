package br.com.abril.nds.model.planejamento;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.pdv.PDV;

/**
 * Created with IntelliJ IDEA.
 * User: Pedro
 * Date: 27/05/13
 * Time: 14:10
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "ESTUDO_PDV")
@SequenceGenerator(name = "ESTUDO_PDV_SEQ", initialValue = 1, allocationSize = 1)
public class EstudoPDV implements Serializable {

    private static final long serialVersionUID = -6947627816099090543L;

    @Id
    @GeneratedValue(generator = "ESTUDO_PDV_SEQ")
    @Column(name = "ID")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ESTUDO_ID")
    private EstudoGerado estudo;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "COTA_ID")
    private Cota cota;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "PDV_ID")
    private PDV pdv;

    @Column(name = "REPARTE")
    private BigInteger reparte;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstudoGerado getEstudo() {
        return estudo;
    }

    public void setEstudo(EstudoGerado estudo) {
        this.estudo = estudo;
    }

    public Cota getCota() {
        return cota;
    }

    public void setCota(Cota cota) {
        this.cota = cota;
    }

    public PDV getPdv() {
        return pdv;
    }

    public void setPdv(PDV pdv) {
        this.pdv = pdv;
    }

    public BigInteger getReparte() {
        return reparte;
    }

    public void setReparte(BigInteger reparte) {
        this.reparte = reparte;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		result = prime * result + ((this.getCota() == null) ? 0 : this.getCota().hashCode());
		result = prime * result + ((this.getEstudo() == null) ? 0 : this.getEstudo().hashCode());
		result = prime * result + ((this.getPdv() == null) ? 0 : this.getPdv().hashCode());
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
		EstudoPDV other = (EstudoPDV) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		if (this.getCota() == null) {
			if (other.getCota() != null)
				return false;
		} else if (!this.getCota().equals(other.getCota()))
			return false;
		if (this.getEstudo() == null) {
			if (other.getEstudo() != null)
				return false;
		} else if (!this.getEstudo().equals(other.getEstudo()))
			return false;
		if (this.getPdv() == null) {
			if (other.getPdv() != null)
				return false;
		} else if (!this.getPdv().equals(other.getPdv()))
			return false;
		return true;
	}
    
    
}
