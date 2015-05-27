package br.com.abril.nds.dto;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ClassificacaoNaoRecebidaDTO extends UsuarioLogDTO {

	private static final long serialVersionUID = -3921631964849282384L;

	private Long idClassificacaoNaoRecebida;

	@Export(label="Classificação", alignment=Alignment.LEFT,exhibitionOrder=1)
	private String nomeClassificacao;
	
	@Export(label="Usuário", alignment=Alignment.LEFT,exhibitionOrder=2)
	@Override
	public String getNomeUsuario() {
		return super.getNomeUsuario();
	}

	@Export(label="Data", alignment=Alignment.LEFT,exhibitionOrder=3)
	@Override
	public String getDataAlteracaoFormatada() {
		return super.getDataAlteracaoFormatada();
	}
	
	@Export(label="Hora", alignment=Alignment.LEFT,exhibitionOrder=4)
	@Override
	public String getHoraAlteracaoFormatada() {
		return super.getHoraAlteracaoFormatada();
	}
	
	public Long getIdClassificacaoNaoRecebida() {
		return idClassificacaoNaoRecebida;
	}

	public void setIdClassificacaoNaoRecebida(Long idClassificacaoNaoRecebida) {
		this.idClassificacaoNaoRecebida = idClassificacaoNaoRecebida;
	}

	public String getNomeClassificacao() {
		return nomeClassificacao;
	}

	public void setNomeClassificacao(String nomeClassificacao) {
		this.nomeClassificacao = nomeClassificacao;
	}

}
