package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonBackReference;

import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEConditions;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;
import br.com.abril.nds.util.export.fiscal.nota.NFEWhen;
import br.com.abril.nds.util.export.fiscal.nota.NFEWhens;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
@Entity
@Table(name = "TELEFONE")
@SequenceGenerator(name="TELEFONE_SEQ", initialValue = 1, allocationSize = 1)
public class Telefone implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3716908928267452396L;

	@Id
	@GeneratedValue(generator = "TELEFONE_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NUMERO", nullable = false)
	private String numero;
	
	@Column(name = "RAMAL", nullable = true)
	private String ramal;
	
	@Column(name = "DDD")
	private String ddd;
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "PESSOA_ID")
	private Pessoa pessoa;
	
	public Telefone() {
    }

	public Telefone(Long id, String numero, String ramal, String ddd,
            Pessoa pessoa) {
        this.id = id;
        this.numero = numero;
        this.ramal = ramal;
        this.ddd = ddd;
        this.pessoa = pessoa;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getRamal() {
		return ramal;
	}

	public void setRamal(String ramal) {
		this.ramal = ramal;
	}

	public String getDdd() {
		return ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}
	
	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_EMITENTE, export = @NFEExport(secao=TipoSecao.C05, posicao=10, tamanho=10)),
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_DESTINATARIO, export = @NFEExport(secao=TipoSecao.E05, posicao=10 , tamanho=10))
	})
	public String getDddNumero() {
		return ddd+numero;
	}

	public String toString(){
		
		return ddd + " - " + numero;
	}

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Telefone other = (Telefone) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }
	
	
	
	
}