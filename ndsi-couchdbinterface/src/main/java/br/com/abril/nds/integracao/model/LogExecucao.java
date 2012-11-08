package br.com.abril.nds.integracao.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.com.abril.nds.integracao.model.enums.StatusExecucaoEnum;

@Entity
@Table(name = "LOG_EXECUCAO")
@SequenceGenerator(name="LOG_EXECUCAO_SEQ", initialValue = 1, allocationSize = 1)
public class LogExecucao implements Serializable {

	private static final long serialVersionUID = 2667214042002554933L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "LOG_EXECUCAO_SEQ")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "INTERFACE_EXECUCAO_ID")
	private InterfaceExecucao interfaceExecucao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_INICIO", nullable = false)
	private Date dataInicio;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_FIM", nullable = true)
	private Date dataFim;
	
	@Type(type = "br.com.abril.nds.integracao.persistence.GenericEnumUserType", 
		parameters = {
			@Parameter( name="enumClass", value="br.com.abril.nds.integracao.model.enums.StatusExecucaoEnum" )
		})
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = true, length = 1)
	private StatusExecucaoEnum status;
	
	@Column(name = "NOME_LOGIN_USUARIO", nullable = false, length = 20)
	private String nomeLoginUsuario;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public InterfaceExecucao getInterfaceExecucao() {
		return interfaceExecucao;
	}

	public void setInterfaceExecucao(InterfaceExecucao interfaceExecucao) {
		this.interfaceExecucao = interfaceExecucao;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public StatusExecucaoEnum getStatus() {
		return status;
	}

	public void setStatus(StatusExecucaoEnum status) {
		this.status = status;
	}

	public String getNomeLoginUsuario() {
		return nomeLoginUsuario;
	}

	public void setNomeLoginUsuario(String nomeLoginUsuario) {
		this.nomeLoginUsuario = nomeLoginUsuario;
	}
}
