package br.com.abril.nds.model.estoque;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;

@Entity
@Table(name = "COBRANCA_CONTROLE_CONFERENCIA_ENCALHE_COTA")
@SequenceGenerator(name="COBRANCA_CONTROLE_CONFERENCIA_ENCALHE_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class CobrancaControleConferenciaEncalheCota {
	
    @Id
    @GeneratedValue(generator = "COBRANCA_CONTROLE_CONFERENCIA_ENCALHE_COTA_SEQ")
    @Column(name = "ID")
	private Long id;

    @ManyToOne
    @JoinColumn(name = "COBRANCA_ID")
	private Cobranca cobranca;
    
    @ManyToOne
    @JoinColumn(name = "CONTROLE_CONF_ENCALHE_COTA_ID")
	private ControleConferenciaEncalheCota controleConferenciaEncalheCota;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cobranca getCobranca() {
		return cobranca;
	}

	public void setCobranca(Cobranca cobranca) {
		this.cobranca = cobranca;
	}

	public ControleConferenciaEncalheCota getControleConferenciaEncalheCota() {
		return controleConferenciaEncalheCota;
	}

	public void setControleConferenciaEncalheCota(
			ControleConferenciaEncalheCota controleConferenciaEncalheCota) {
		this.controleConferenciaEncalheCota = controleConferenciaEncalheCota;
	}
	
    
    
	
}
