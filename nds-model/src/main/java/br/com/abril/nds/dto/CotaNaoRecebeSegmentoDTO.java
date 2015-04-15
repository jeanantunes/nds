package br.com.abril.nds.dto;

import java.util.Date;

import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

/**
 * @author InfoA2 - Samuel Mendes
 * 
 *         <h1>Classe DTO que representa as informações do grid
 *         "Cotas Que Não Recebem Segmento" no menu Distribuição > Segmento Não
 *         Recebido</h1>
 * 
 */
@Exportable
public class CotaNaoRecebeSegmentoDTO {

	private Long segmentoNaoRecebidoId;
	
	@Export(label = "Cota", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private Integer numeroCota;
	
	@Export(label = "Status", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private SituacaoCadastro statusCota;
	
	@Export(label = "Nome", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private String nomeCota;
	
	@Export(label = "Usuário", alignment=Alignment.LEFT, exhibitionOrder = 4)
	private String nomeUsuario;

	private Date dataAlteracao;
	
	@Export(label = "Data Alteração", alignment=Alignment.LEFT, exhibitionOrder = 5)
	private String dataAlteracaoFormatada;
	
	@Export(label = "Hora Alteração", alignment=Alignment.LEFT, exhibitionOrder = 6)
	private String horaAlteracaoFormatada;

	public Long getSegmentoNaoRecebidoId() {
		return segmentoNaoRecebidoId;
	}

	public void setSegmentoNaoRecebidoId(Long segmentoNaoRecebidoId) {
		this.segmentoNaoRecebidoId = segmentoNaoRecebidoId;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public SituacaoCadastro getStatusCota() {
		return statusCota;
	}

	public void setStatusCota(SituacaoCadastro statusCota) {
		this.statusCota = statusCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

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
		this.horaAlteracaoFormatada = DateUtil.formatarHoraMinuto(dataAlteracao);
	}

	public String getDataAlteracaoFormatada() {
		return dataAlteracaoFormatada;
	}

	public String getHoraAlteracaoFormatada() {
		return horaAlteracaoFormatada;
	}

}
