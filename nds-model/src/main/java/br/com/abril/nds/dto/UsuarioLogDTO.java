package br.com.abril.nds.dto;

import java.util.Date;

import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Exportable;

/**
 * 
 * @author InfoA2 - Samuel Mendes
 * 
 *         <h1>Classe contendo informações de log quando o usuário faz alguma
 *         inserção no sistema e precisa mostrar essa informação no flexGrid.</h1>
 * 
 *         <p>
 *         obs: Quando houver <b>exportação</b> para Excel/PDF fazer as
 *         anotações no override dos atributos.
 *         </p>
 * 
 */

@Exportable
public class UsuarioLogDTO extends FiltroDTO {

	private static final long serialVersionUID = -7763031269118584271L;

	protected String nomeUsuario;
	protected Date dataAlteracao;
	protected String dataAlteracaoFormatada;
	protected String horaAlteracaoFormatada;

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
		this.dataAlteracaoFormatada = DateUtil.formatarDataPTBR(dataAlteracao);
		this.horaAlteracaoFormatada = DateUtil
				.formatarHoraMinuto(dataAlteracao);
	}

	public String getDataAlteracaoFormatada() {
		return dataAlteracaoFormatada;
	}

	public String getHoraAlteracaoFormatada() {
		return horaAlteracaoFormatada;
	}

}
