package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.model.estoque.Semaforo;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.DateUtil;

public class StatusProcessoEncalheVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5629848975071215707L;
	
	private Integer numeroCota;
	
	private String horaInicio;
	
	private String horaFim;
	
	private String mensagem;
	
	private String status;
	
	private String usuario;
	
	public StatusProcessoEncalheVO() {
		
	}
	
	public StatusProcessoEncalheVO(Semaforo semaforo) {
		
		this.numeroCota = semaforo.getNumeroCota();
		this.horaInicio = DateUtil.formatarHoraMinuto(semaforo.getDataInicio());
		this.horaFim = DateUtil.formatarHoraMinuto(semaforo.getDataFim());
		this.mensagem = semaforo.getErrorLog();
		this.status = semaforo.getStatusProcessoEncalhe().toString();
		
		Usuario usuario = semaforo.getUsuario();
		
		if (usuario != null) {
			
			StringBuilder nomeUsuario = new StringBuilder(usuario.getNome());
			
			if (usuario.getSobrenome() != null) {
				
				nomeUsuario.append(" ").append(usuario.getSobrenome());
			}
			
			this.usuario = nomeUsuario.toString();
		}
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	public String getHoraFim() {
		return horaFim;
	}

	public void setHoraFim(String horaFim) {
		this.horaFim = horaFim;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}
