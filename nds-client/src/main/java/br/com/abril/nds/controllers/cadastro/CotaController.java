package br.com.abril.nds.controllers.cadastro;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.util.Constantes;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;

@Resource
@Path("/cadastro/cota")
public class CotaController {

	@Autowired
	private CotaService cotaService;

	@Autowired
	private HttpSession session;

	@Path("/")
	public void index() {
	}

	public void obterListaEnderecoCota(Long idCota) {

	}

	public void editarCota(Long idCota) {

		List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = this.cotaService.obterEnderecosPorIdCota(idCota);

		this.session.removeAttribute(Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_REMOVER);

		if (listaEnderecoAssociacao != null) {

			this.session.setAttribute(Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR, listaEnderecoAssociacao);
		}
	}
}
