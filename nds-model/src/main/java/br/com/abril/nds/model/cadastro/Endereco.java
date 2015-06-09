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
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "ENDERECO")
@SequenceGenerator(name="ENDERECO_SEQ", initialValue = 1, allocationSize = 1)
public class Endereco implements Serializable, Cloneable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 321277267597116429L;
	
	@Id
	@GeneratedValue(generator = "ENDERECO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "BAIRRO", length=60)
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_EMITENTE, export = @NFEExport(secao=TipoSecao.C05, posicao=3, tamanho=60)),
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_DESTINATARIO, export = @NFEExport(secao=TipoSecao.E05, posicao=3 , tamanho=60))
	})
	private String bairro;
	
	@Column(name = "CEP", length=9)
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_EMITENTE, export = @NFEExport(secao=TipoSecao.C05, posicao=7, tamanho=8)),
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_DESTINATARIO, export = @NFEExport(secao=TipoSecao.E05, posicao=7 , tamanho=8))
	})
	private String cep;
	
	@Column(name = "CODIGO_CIDADE_IBGE", nullable = true, length=7)
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_EMITENTE, export = @NFEExport(secao = TipoSecao.B, posicao = 11)),
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_EMITENTE, export = @NFEExport(secao = TipoSecao.C05, posicao = 4)),
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_DESTINATARIO, export = @NFEExport(secao = TipoSecao.E05, posicao = 4))
	})
	private Integer codigoCidadeIBGE;
	
	@Column(name = "CIDADE", length=60)
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_EMITENTE, export = @NFEExport(secao=TipoSecao.C05, posicao=5, tamanho=60)),
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_DESTINATARIO, export = @NFEExport(secao=TipoSecao.E05, posicao=5 , tamanho=60))
	})
	private String cidade;
	
	@Column(name = "COMPLEMENTO", length=60)
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_EMITENTE, export = @NFEExport(secao=TipoSecao.C05, posicao=2, tamanho=60)),
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_DESTINATARIO, export = @NFEExport(secao=TipoSecao.E05, posicao=2 , tamanho=60))
	})
	private String complemento;
	
	@Column(name = "TIPO_LOGRADOURO")
	private String tipoLogradouro;
	
	@Column(name = "LOGRADOURO", length=60)
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_EMITENTE, export = @NFEExport(secao = TipoSecao.C05, posicao = 0, tamanho = 60)),
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_DESTINATARIO, export = @NFEExport(secao = TipoSecao.E05, posicao = 0, tamanho = 60))
	})
	private String logradouro;
	
	@Column(name = "NUMERO", nullable = true, length=60)
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_EMITENTE, export = @NFEExport(secao=TipoSecao.C05, posicao=1, tamanho=60)),
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_DESTINATARIO, export = @NFEExport(secao=TipoSecao.E05, posicao=1 , tamanho=60))
	})
	private String numero;
	
	@Column(name = "UF", length=2)
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_EMITENTE, export = @NFEExport(secao = TipoSecao.C05, posicao = 6, tamanho = 2)),
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_DESTINATARIO, export = @NFEExport(secao = TipoSecao.E05, posicao = 6, tamanho = 2))
	})
	private String uf;
	
	@Column(name = "CODIGO_UF", length=2)
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_EMITENTE, export = @NFEExport(secao = TipoSecao.B, posicao = 0))
	})
	private Integer codigoUf;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "PESSOA_ID")
	private Pessoa pessoa;
	
	public Endereco() {
	}

	public Endereco(String bairro, String cep,
            Integer codigoCidadeIBGE, String cidade, String complemento,
            String tipoLogradouro, String logradouro, String numero, String uf,
            Integer codigoUf, Pessoa pessoa) {
        this.bairro = bairro;
        this.cep = cep;
        this.codigoCidadeIBGE = codigoCidadeIBGE;
        this.cidade = cidade;
        this.complemento = complemento;
        this.tipoLogradouro = tipoLogradouro;
        this.logradouro = logradouro;
        this.numero = numero;
        this.uf = uf;
        this.codigoUf = codigoUf;
        this.pessoa = pessoa;
    }



    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	
	/**
	 * @return the tipoLogradouro
	 */
	public String getTipoLogradouro() {
		return tipoLogradouro;
	}

	/**
	 * @param tipoLogradouro the tipoLogradouro to set
	 */
	public void setTipoLogradouro(String tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}
	
	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Integer getCodigoCidadeIBGE() {
		return codigoCidadeIBGE;
	}

	public void setCodigoCidadeIBGE(Integer codigoCidadeIBGE) {
		this.codigoCidadeIBGE = codigoCidadeIBGE;
	}

	/**
	 * @return the codigoUf
	 */
	public Integer getCodigoUf() {
		return codigoUf;
	}

	/**
	 * @param codigoUf the codigoUf to set
	 */
	public void setCodigoUf(Integer codigoUf) {
		this.codigoUf = codigoUf;
	}
	
	@Override
	public Endereco clone() throws CloneNotSupportedException {
		return (Endereco) super.clone();
	}


	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Endereco other = (Endereco) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}