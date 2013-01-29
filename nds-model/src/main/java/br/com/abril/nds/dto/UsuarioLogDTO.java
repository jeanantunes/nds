package br.com.abril.nds.dto;

import java.util.Date;

import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Exportable;

/**
 * 
 * @author InfoA2 - Samuel Mendes
 * 
 *         <h1>Classe contendo informações de log quando o usuário faz alguma
 *         inserção no sistema e precisa mostrar essa informação no flexGrid. </h1>
 *         
 *         <p>obs: Quando houver <b>exportação</b> para Excel/PDF fazer as anotações no override dos métodos GET.</p>
 * 
 */

@Exportable
public abstract class UsuarioLogDTO {

	private String nomeUsuario;

	private Date dataAlteracao;

	private String dataAlteracaoFormatada;

	private String horaAlteracaoFormatada;

	protected String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	protected Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
		this.dataAlteracaoFormatada = DateUtil.formatarDataPTBR(dataAlteracao);
		this.horaAlteracaoFormatada = DateUtil.formatarHoraMinuto(dataAlteracao);
	}

	protected String getDataAlteracaoFormatada() {
		return dataAlteracaoFormatada;
	}

	protected String getHoraAlteracaoFormatada() {
		return horaAlteracaoFormatada;
	}

}
