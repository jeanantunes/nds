package br.com.abril.nds.controllers.distribuicao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.DesenglobaDTO;
import br.com.abril.nds.dto.filtro.FiltroDesenglobacaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.DesenglobacaoService;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/distribuicao/desenglobacao")
public class DesenglobacaoController extends BaseController {
	
	@Autowired
	private Result result;
	
	private DesenglobacaoService service;
	
	@Rules(Permissao.ROLE_DISTRIBUICAO_DESENGLOBACAO)
	@Path("/index")
	public void desenglobacao(){
		
	}
	
	@Post
	public void pesquisaPrincipal(FiltroDesenglobacaoDTO filtro, String sortorder, String sortname, int page, int rp ){
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		validarFiltroDesenglobacao(filtro);
		result.nothing();
	}

	private void validarFiltroDesenglobacao(FiltroDesenglobacaoDTO filtro) {
		if((filtro.getCotaDto().getNumeroCota() == null || filtro.getCotaDto().getNumeroCota() == 0) && 
				(filtro.getCotaDto().getNomePessoa() == null || filtro.getCotaDto().getNomePessoa().trim().isEmpty()))
			throw new ValidacaoException(TipoMensagem.WARNING, "Informe um código ou nome.");
	}
	
	@Post
	@Path("/inserirEnglobacao")
	public void inserirEnglobacao(List<DesenglobaDTO> desenglobaDTO) {
		service.inserirDesenglobacao(null);
		result.nothing();
	}
}
