package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

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
	private HttpSession httpSession;
	
	@Autowired
	private CotaService cotaService;
	
	public static final String LISTA_SOCIOS_SALVAR_SESSAO = "listaSociosSalvarSessao";
	
	
	private void validarInclusaoSocio(String nome, String cargo){
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if(nome == null || nome.isEmpty()){
			listaMensagens.add("O preenchimento do campo [Nome] é obrigatório!");
		}
		
		if(cargo == null || cargo.isEmpty() ){
			listaMensagens.add("O preenchimento do campo [Cargo] é obrigatório!");
		}
		
		if(!listaMensagens.isEmpty()){

			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, listaMensagens));
		}
		
	}
	@Post
	public void salvarSocioCota(List<SocioCota> sociosCota,Long idCota){
		
		cotaService.salvarSociosCota(sociosCota, idCota);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	@Post
	public void carregarSociosCota(Long idCota){
		
		List<SocioCota> sociosCota = cotaService.obterSociosCota(idCota);
		
		result.use(Results.json()).from(sociosCota, "result").exclude("cota").recursive().serialize();		
	}
	
	@Post
	public void excluirSocioCota(Long idSocio){
		
	}
	
	@Post
	public void incluirSocioCota(String nome,String cargo,boolean principal, Long idSocio){
		
		validarInclusaoSocio(nome,cargo);
		
		SocioCota socioCota = new SocioCota();
		socioCota.setNome(nome);
		socioCota.setCargo(cargo);
		socioCota.setPrincipal(principal);
		
		setSocioCota(socioCota, idSocio);
	}
	
	private void rendereizarSociosCota(){
		
		
	}
	
	private void removeSocioCota(SocioCota socioCota){
		
		Map<Long,SocioCota> socios =  getSociosCota();
		
		if(socios!=null && !socios.isEmpty()){
			
			socios.remove(socioCota);
		}
		
		httpSession.setAttribute(LISTA_SOCIOS_SALVAR_SESSAO,socios);
	}
	
	private void setSocioCota(SocioCota socioCota, Long idSocio){
		
		Map<Long,SocioCota> socios =  getSociosCota();
		
		if(socios!=null && !socios.isEmpty()){
			
			socios.put(idSocio, socioCota);
		}
		
		httpSession.setAttribute(LISTA_SOCIOS_SALVAR_SESSAO,socios);
	}
	
	private SocioCota getSocioCota(Long idSocio){
		
		Map<Long,SocioCota> socios = getSociosCota();
		
		if(socios == null || socios.isEmpty()){
			return null;
		}
		
		if(socios.containsKey(idSocio)){
			return socios.get(idSocio);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private Map<Long,SocioCota> getSociosCota(){
		return  (Map<Long, SocioCota>) httpSession.getAttribute(LISTA_SOCIOS_SALVAR_SESSAO);
	}
	
}
