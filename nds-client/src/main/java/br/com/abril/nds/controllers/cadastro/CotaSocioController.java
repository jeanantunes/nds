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
			listaMensagens.add("O preenchimento do Endereço é obrigatório!");
			listaMensagens.add("O preenchimento do Telefone é obrigatório!");
		}
		else{
			
			if(socioCota.getNome() == null || socioCota.getNome().isEmpty()){
				listaMensagens.add("O preenchimento do campo [Nome] é obrigatório!");
			}
			
			if(socioCota.getCargo() == null || socioCota.getCargo().isEmpty() ){
				listaMensagens.add("O preenchimento do campo [Cargo] é obrigatório!");
			}

			if(socioCota.getEndereco() == null){
				listaMensagens.add("O preenchimento do Endereço é obrigatório!");
			}

			if(socioCota.getTelefone() == null){
				listaMensagens.add("O preenchimento do Telefone é obrigatório!");
			}
		}
		
		if(!listaMensagens.isEmpty()){

			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, listaMensagens));
		}
	}
	
	@Post
	public void carregarSociosCota(Long idCota){
		
		List<SocioCota> sociosCota = this.cotaService.obterSociosCota(idCota);
		
		this.result.use(Results.json()).from(sociosCota, "result").exclude("cota").recursive().serialize();		
	}
	
	@Post
	public void carregarSocioPorId(Long idSocioCota) {
		
		SocioCota socioCota = this.cotaService.obterSocioPorId(idSocioCota);
		
		this.result.use(Results.json()).from(socioCota, "result").exclude("cota").recursive().serialize();	
	}
	
	@Post
	public void incluirSocioCota(Long idCota, SocioCota socioCota) {
		
		validarInclusaoSocio(socioCota);
		
		this.cotaService.salvarSocioCota(socioCota, idCota);

		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
						Constantes.PARAM_MSGS).recursive().serialize();
	}
}
