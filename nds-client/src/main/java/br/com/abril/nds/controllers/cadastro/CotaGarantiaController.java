package br.com.abril.nds.controllers.cadastro;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cheque;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.Imovel;
import br.com.abril.nds.model.cadastro.NotaPromissoria;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.serialization.custom.CustomJson;
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

	@Post
	@Path("/salvaChequeCaucao.json")
	public void salvaChequeCaucao(Cheque chequeCaucao, Long idCota) {
		
		try {
			cotaGarantiaService.salvaChequeCaucao(chequeCaucao, idCota);
		} catch (RelationshipRestrictionException e) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
					e.getMessage()));
		}
		
		result.use(Results.json())
		.from(new ValidacaoVO(TipoMensagem.SUCCESS,
				"Cheque Caução salvo com Sucesso."), "result")
		.recursive().serialize();
	}
	
	@Post
	@Path("/salvaImovel.json")
	public void salvaImovel(List<Imovel> listaImoveis, Long idCota) {
		try {
			cotaGarantiaService.salvaImovel(listaImoveis, idCota);
		} catch (RelationshipRestrictionException e) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
					e.getMessage()));
		}
		
		result.use(Results.json())
		.from(new ValidacaoVO(TipoMensagem.SUCCESS,
				"Imóveis salvos com Sucesso."), "result")
		.recursive().serialize();
	}
	
	@Post("/getByCota.json")
	public void getByCota(Long idCota) {
		CotaGarantia cotaGarantia =	cotaGarantiaService.getByCota(idCota);
		
		if (cotaGarantia != null) {			
			result.use(Results.json()).from(cotaGarantia, "cotaGarantia").exclude("cota").recursive().serialize();		
		}else{			
			result.use(Results.json()).from("OK").serialize();		
		}	
	}
	
	@Get("/impriNotaPromissoria/{id}")
	public void impriNotaPromissoria(Long id){
		//TODO: chamada do relatorio para nota.
		result.nothing();
	}
	
	@Get("/getTiposGarantia.json")
	public void getTiposGarantia(){
		List<TipoGarantia> cotaGarantias = cotaGarantiaService.obtemTiposGarantiasAceitas();		
		result.use(Results.json()). withoutRoot().from(cotaGarantias).recursive().serialize();
	}

	@Post("/buscaFiador.json")
	public void buscaFiador(String nome, int maxResults) {
		List<ItemDTO<Long, String>> listFiador = cotaGarantiaService.buscaFiador(nome, maxResults);		
		result.use(Results.json()).from(listFiador,"items").recursive().serialize();
	}
	
	@Post("/getFiador.json")
	public void getFiador(Long idFiador) {
		Fiador fiador = cotaGarantiaService.getFiador(idFiador);	
		result.use(CustomJson.class).from(fiador).serialize();
	}
	
}
