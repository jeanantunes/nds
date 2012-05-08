package br.com.abril.nds.controllers.cadastro;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.NotaPromissoria;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.service.CotaGarantiaService;
import br.com.abril.nds.service.exception.RelationshipRestrictionException;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/garantia")
public class CotaGarantiaController {

	@Autowired
	private CotaGarantiaService cotaGarantiaService;

	private Result result;

	public CotaGarantiaController(Result result) {
		super();
		this.result = result;
	}

	@Post
	@Path("/salvaNotaPromissoria.json")
	public void salvaNotaPromissoria(NotaPromissoria notaPromissoria,
			Long idCota) {
		try {
			cotaGarantiaService.salvaNotaPromissoria(notaPromissoria, idCota);
		} catch (RelationshipRestrictionException e) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
					e.getMessage()));
		}
		result.use(Results.json())
				.from(new ValidacaoVO(TipoMensagem.SUCCESS,
						"Nota Promissoria salva com Sucesso."), "result")
				.recursive().serialize();
	}

	@Get
	@Path("/getByCota.json")
	public void getByCota(Long idCota){
		CotaGarantia cotaGarantia =	cotaGarantiaService.getByCota(idCota);
		
		result.use(Results.json()).from(cotaGarantia,"cotaGarantia").exclude("cota").recursive().serialize();
		
		
	}
}
