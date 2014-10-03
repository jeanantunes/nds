package br.com.abril.nds.model.planejamento;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

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

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.seguranca.Usuario;

/**
 * Entidade com as chamadas de encalhes das cotas
 * 
 * @author francisco.garcia
 *
 */
@Entity
@Table(name = "CHAMADA_ENCALHE_COTA")
@SequenceGenerator(name="CHAMADA_ENCALHE_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class ChamadaEncalheCota implements Serializable {

	private static final long serialVersionUID = -6783440794882421161L;

	@Id
	@GeneratedValue(generator = "CHAMADA_ENCALHE_COTA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@Column(name = "FECHADO", nullable = false)
	private boolean fechado;
	
	@Column(name = "POSTERGADO", nullable = false)
	private boolean postergado;
	
	@Column(name = "QTDE_PREVISTA", nullable = false)
	private BigInteger qtdePrevista;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "CHAMADA_ENCALHE_ID")
	private ChamadaEncalhe chamadaEncalhe;
	
	@OneToOne(optional = true)
	@JoinColumn(name = "CHAMADA_ENCALHE_COTA_POSTERGADA_ID")
	private ChamadaEncalheCota chamadaEncalheCotaPostergada;

	@OneToMany(mappedBy = "chamadaEncalheCota")
	private Set<ConferenciaEncalhe> conferenciasEncalhe = new HashSet<ConferenciaEncalhe>();
	
	@ManyToOne(optional = true)
    @JoinColumn(name = "USUARIO_ID")
    private Usuario usuario;
	
	public ChamadaEncalheCota() {
		super();
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
	 * @return the cota
	 */
	public Cota getCota() {
		return cota;
	}

	/**
	 * @param cota the cota to set
	 */
	public void setCota(Cota cota) {
		this.cota = cota;
	}
	
	public BigInteger getQtdePrevista() {
		return qtdePrevista;
	}
	
	public void setQtdePrevista(BigInteger qtdePrevista) {
		this.qtdePrevista = qtdePrevista;
	}

	/**
	 * @return the chamadaEncalhe
	 */
	public ChamadaEncalhe getChamadaEncalhe() {
		return chamadaEncalhe;
	}

	/**
	 * @param chamadaEncalhe the chamadaEncalhe to set
	 */
	public void setChamadaEncalhe(ChamadaEncalhe chamadaEncalhe) {
		this.chamadaEncalhe = chamadaEncalhe;
	}
	
	/**
	 * @return
	 */
	public ChamadaEncalheCota getChamadaEncalheCotaPostergada() {
		return chamadaEncalheCotaPostergada;
	}

	/**
	 * @param chamadaEncalheCotaPostergada
	 */
	public void setChamadaEncalheCotaPostergada(ChamadaEncalheCota chamadaEncalheCotaPostergada) {
		this.chamadaEncalheCotaPostergada = chamadaEncalheCotaPostergada;
	}

	/**
	 * @return the fechado
	 */
	public boolean isFechado() {
		return fechado;
	}

	/**
	 * @param fechado the fechado to set
	 */
	public void setFechado(boolean fechado) {
		this.fechado = fechado;
	}

	public boolean isPostergado() {
		return postergado;
	}

	public void setPostergado(boolean postergado) {
		this.postergado = postergado;
	}

	/**
	 * @return the conferenciasEncalhe
	 */
	public Set<ConferenciaEncalhe> getConferenciasEncalhe() {
		return conferenciasEncalhe;
	}

	/**
	 * @param conferenciasEncalhe the conferenciasEncalhe to set
	 */
	public void setConferenciasEncalhe(Set<ConferenciaEncalhe> conferenciasEncalhe) {
		this.conferenciasEncalhe = conferenciasEncalhe;
	}

    public Usuario getUsuario() {
        return usuario;
    }

    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
