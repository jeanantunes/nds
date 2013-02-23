package br.com.abril.nds.dto;

import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class CotaQueRecebeExcecaoDTO extends UsuarioLogDTO {

	private static final long serialVersionUID = -8702161919129929636L;

	private Long idExcecaoProdutoCota;
	
	@Export(label="Status", alignment=Alignment.LEFT,exhibitionOrder=1)
	private SituacaoCadastro statusCota;
	
	@Export(label="Cota", alignment=Alignment.LEFT,exhibitionOrder=0)
	private Integer numeroCota;
	
	@Export(label="Nome", alignment=Alignment.LEFT,exhibitionOrder=2)
	private String nomePessoa;

	
	@Export(label="Usuário", alignment=Alignment.LEFT,exhibitionOrder=3)
	@Override
	public String getNomeUsuario() {
		return super.getNomeUsuario();
	}

	@Export(label="Data", alignment=Alignment.LEFT,exhibitionOrder=4)
	@Override
	public String getDataAlteracaoFormatada() {
		return super.getDataAlteracaoFormatada();
	}
	
	@Export(label="Hora", alignment=Alignment.LEFT,exhibitionOrder=5)
	@Override
	public String getHoraAlteracaoFormatada() {
		return super.getHoraAlteracaoFormatada();
	}
	
	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomePessoa() {
		return nomePessoa;
	}

	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
	}

	public Long getIdExcecaoProdutoCota() {
		return idExcecaoProdutoCota;
	}

	public void setIdExcecaoProdutoCota(Long idExcecaoProdutoCota) {
		this.idExcecaoProdutoCota = idExcecaoProdutoCota;
	}

	public SituacaoCadastro getStatusCota() {
		return statusCota;
	}

	public void setStatusCota(SituacaoCadastro statusCota) {
		this.statusCota = statusCota;
	}
	
	
}
