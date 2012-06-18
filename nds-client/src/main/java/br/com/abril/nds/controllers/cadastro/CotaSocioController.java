package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.SocioCota;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/cota")
public class CotaSocioController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private CotaService cotaService;
	
	private void validarInclusaoSocio(SocioCota socioCota){
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if(socioCota== null){
			listaMensagens.add("O preenchimento do campo [Nome] é obrigatório!");
			listaMensagens.add("O preenchimento do campo [Cargo] é obrigatório!");
		}
		else{
			
			if(socioCota.getNome() == null || socioCota.getNome().isEmpty()){
				listaMensagens.add("O preenchimento do campo [Nome] é obrigatório!");
			}
			
			if(socioCota.getCargo() == null || socioCota.getCargo().isEmpty() ){
				listaMensagens.add("O preenchimento do campo [Cargo] é obrigatório!");
			}
		}
		
		if(!listaMensagens.isEmpty()){

			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, listaMensagens));
		}
	}
	
	@Post
	public void carregarSociosCota(Long idCota){
		
		List<SocioCota> sociosCota = cotaService.obterSociosCota(idCota);
		
		result.use(Results.json()).from(sociosCota, "result").exclude("cota").recursive().serialize();		
	}
	
	@Post
	public void incluirSocioCota(SocioCota socioCota, List<SocioCota> sociosCota){
		
		validarInclusaoSocio(socioCota);
		
		validarInclusaoSocioPrincipal(socioCota,sociosCota);
		
		result.use(Results.json()).from(socioCota,"result").recursive().serialize();
	}
	
	@Post
	public void salvarSocioCota(Long idCota, List<SocioCota> sociosCota){
		
		cotaService.salvarSociosCota(sociosCota, idCota);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
	}

	/**
	 * Verifica se o sócio informado é principal
	 * @param socioCota
	 * @param sociosCota
	 */
	private void validarInclusaoSocioPrincipal(SocioCota socioCota, List<SocioCota> sociosCota) {
		
		if(sociosCota != null && !sociosCota.isEmpty()){
			
			for(SocioCota socio : sociosCota){
				
				if( socio.getId()== null || !(socio.getId().equals(socioCota.getId()))){
					
					if(socio.getPrincipal()!= null && socio.getPrincipal() && socioCota.getPrincipal()!= null && socioCota.getPrincipal()){
						throw new ValidacaoException(TipoMensagem.WARNING,"Socio principal ja foi cadastrado!");
					}
				}
					
			}
		}
	}
	
	/**
	 * Verifica se existe algum sócio cadastrado como principal
	 * @param sociosCota
	 * @return boolean 
	 */
	private boolean isSocioPrincipal(List<SocioCota> sociosCota) {
		
		if(sociosCota != null && !sociosCota.isEmpty()){
			
			for(SocioCota socio : sociosCota){
				if( socio.getPrincipal()!= null && socio.getPrincipal())
					return true;
			}
		}
		return false;	
	}
}
