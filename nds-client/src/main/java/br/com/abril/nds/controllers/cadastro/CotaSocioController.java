package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.SocioCota;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.service.SocioCotaService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.ValidacaoVO;
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
	private SocioCotaService socioCotaService;
	
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

			listaMensagens.addAll(this.obterMensagensValidacaoEnderecoSocio(socioCota.getEndereco()));
			
			listaMensagens.addAll(this.obterMensagensValidacaoTelefoneSocio(socioCota.getTelefone()));
		}
		
		if(!listaMensagens.isEmpty()){

			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, listaMensagens));
		}
	}
	
	private List<String> obterMensagensValidacaoEnderecoSocio(Endereco endereco) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if (endereco == null){
			
			listaMensagens.add("Endereço é obrigatório.");
			
			return listaMensagens;
		}
		
		if (endereco.getCep() == null || endereco.getCep().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [CEP] é obrigatório.");
		}

		if (endereco.getTipoLogradouro() == null || endereco.getTipoLogradouro().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Tipo Logradouro] é obrigatório.");
		}
		
		if (endereco.getLogradouro() == null || endereco.getLogradouro().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Logradouro] é obrigatório.");
		}

		if (endereco.getNumero() == null || endereco.getNumero().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Número] é obrigatório.");
		}
		
		if (endereco.getBairro() == null || endereco.getBairro().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Bairro] é obrigatório.");
		}		

		if (endereco.getCidade() == null || endereco.getCidade().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Cidade] é obrigatório.");
		}
		
		if (endereco.getUf() == null || endereco.getUf().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [UF] é obrigatório.");
		}
		
		return listaMensagens;
	}

	private List<String> obterMensagensValidacaoTelefoneSocio(Telefone telefone) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if (telefone == null) {
			
			listaMensagens.add("Telefone é obrigatório.");
		
			return listaMensagens;
		}
		
		if (telefone.getDdd() == null || telefone.getDdd().trim().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [DDD] é obrigatório.");
		}
		
		if (telefone.getNumero() == null || telefone.getNumero().trim().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Número] é obrigatório.");
		}
		
		return listaMensagens;
	}
	
	@Post
	public void carregarSociosCota(Long idCota){
		
		List<SocioCota> sociosCota = this.socioCotaService.obterSociosCota(idCota);
		
		this.result.use(Results.json()).from(sociosCota, "result").exclude("cota").recursive().serialize();		
	}
	
	@Post
	public void carregarSocioPorId(Long idSocioCota) {
		
		SocioCota socioCota = this.socioCotaService.obterSocioPorId(idSocioCota);
		
		this.result.use(Results.json()).from(socioCota, "result").exclude("cota").recursive().serialize();	
	}
	
	@Post
	public void incluirSocioCota(Long idCota, SocioCota socioCota) {
		
		validarInclusaoSocio(socioCota);
		
		this.socioCotaService.salvarSocioCota(socioCota, idCota);

		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				"result").recursive().serialize();
	}
	
	@Post
	public void removerSocioCota(Long idSocioCota) {
		
		this.socioCotaService.removerSocioCota(idSocioCota);
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				"result").recursive().serialize();
	}
	
	@Post
	public void confirmarSocioCota(Long idCota) {
		
		this.socioCotaService.confirmarCadastroSociosCota(idCota);
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				"result").recursive().serialize();
	}
}
