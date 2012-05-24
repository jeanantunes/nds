package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.RotaRoteiroOperacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("cadastro/roteirizacao")
public class RoteirizacaoController {

	@Autowired
	private BoxService boxService;
	
	@Autowired
	private RoteirizacaoService roteirizacaoService;
	
	@Autowired
	private Result result;


	@Path("/")
	public void index() {
		carregarComboBox();
		carregarComboRoteiro();
		carregarComboRota(); 
	}

	private void carregarComboBox() {
		List<Box> boxs = boxService.buscarPorTipo(TipoBox.LANCAMENTO);
		
			List<ItemDTO<Long, String>> lista =
				new ArrayList<ItemDTO<Long,String>>();
		
		for (Box box : boxs) {
			
			lista.add(
				new ItemDTO<Long, String>(box.getId(), box.getNome()));
		}
		
		result.include("listaBox", lista);
	}
	
	private void carregarComboRoteiro() {
		List<Roteiro> roteiros = roteirizacaoService.buscarRoteiro("descricaoRoteiro", Ordenacao.ASC);
		
			List<ItemDTO<Long, String>> lista =
				new ArrayList<ItemDTO<Long,String>>();
		
		for (Roteiro roteiro : roteiros) {
			try{
				roteiro.getRotas();
			} catch (Exception e) {
				e.getMessage();
			}
			
			lista.add(
				new ItemDTO<Long, String>(roteiro.getId(), roteiro.getDescricaoRoteiro()));
		}
		
		result.include("listaRoteiro", lista);
	}
	
	private void carregarComboRota() {
		List<Rota> rotas = roteirizacaoService.buscarRota("descricaoRota", Ordenacao.ASC);
		
			List<ItemDTO<Long, String>> lista =
				new ArrayList<ItemDTO<Long,String>>();
		
		for (Rota rota : rotas) {
			
			lista.add(
				new ItemDTO<Long, String>(rota.getId(), rota.getDescricaoRota()));
		}
		
		result.include("listaRota", lista);
	}
	
	@Path("/pesquisar")
	public void pesquisar(Long idBox, Long idRoteiro, Long idRota,
			String sortname, String sortorder, int rp, int page) {
		List<RotaRoteiroOperacao> lista = roteirizacaoService.busca(null, null,null, null, null, 0, 0);
		int quantidade = lista.size();
		result.use(FlexiGridJson.class).from(lista).total(quantidade).page(page).serialize();

	}
	
	@Path("/incluirRoteiro")
	public void incluirRoteiro(Long idBox, Integer ordem, String nome, TipoRoteiro tipoRoteiro) {
		

		Roteiro roteiro = new Roteiro();
		if ( idBox != null ){
			Box box = new Box();
			box.setId(idBox);
			roteiro.setBox(box);
		}	
		roteiro.setOrdem(ordem);
		roteiro.setDescricaoRoteiro(nome);
		roteiro.setTipoRoteiro(tipoRoteiro);
		validarCampoObrigatoriosRoteiro(roteiro);
		roteirizacaoService.incluirRoteiro(roteiro);
		result.use(Results.json()).from(
		new ValidacaoVO(TipoMensagem.SUCCESS, "Roreito cadastrado com sucesso."),
						"result").recursive().serialize();

	}
	@Path("/iniciaTelaRoteiro")
	public void iniciaTelaRoteiro() {
			
		result.use(Results.json()).from(10).recursive().serialize();
	}
	private void validarCampoObrigatoriosRoteiro(Roteiro roteiro) {
		
		List<String> mensagens = new ArrayList<String>();
		
		if(roteiro.getOrdem() == null){
			mensagens.add("O campo Ordem é obrigatório.");
		}
		if("".equals(roteiro.getDescricaoRoteiro())){
			mensagens.add("O campo Nome é obrigatório.");
		}
	
		
		if (!mensagens.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagens));
		}
	}
	@Post
	public void autoCompletarRoteiroPorDescricao(String descricao) {
		
		
		List<Roteiro> listaRoteiro = roteirizacaoService.buscarRoteiroPorDescricao(descricao, MatchMode.ANYWHERE);
		
		List<ItemAutoComplete> listaRoteiroAutoComplete = new ArrayList<ItemAutoComplete>();
		
		if (listaRoteiro != null && !listaRoteiro.isEmpty()) {
			
			for (Roteiro roteiro : listaRoteiro) {
				roteiro.setRotas(null);
				if ( roteiro.getBox() != null ){ 
					roteiro.getBox().setCotas(null);
				}	
				listaRoteiroAutoComplete.add(new ItemAutoComplete(roteiro.getDescricaoRoteiro(), null,roteiro ));
			}
		}
		
		this.result.use(Results.json()).from(listaRoteiroAutoComplete, "result").include("chave").serialize();
	}
	
	@Post
	public void buscaRoteiroPorDescricao(String descricao) {
		
		
		List<Roteiro> listaRoteiro = roteirizacaoService.buscarRoteiroPorDescricao(descricao, MatchMode.EXACT);
		
		this.result.use(Results.json()).from(listaRoteiro, "result").include("box").serialize();
	}
	
	@Post
	public void buscaRotasPorRoteiro(Long roteiroId, int rp, int page) {
		
		
		List<Rota> listaRota = roteirizacaoService.buscarRotaPorRoteiro(roteiroId);
		
		
		result.use(FlexiGridJson.class).from(listaRota).total(listaRota.size()).page(page).serialize();
	}
	
	@Path("/incluirRota")
	public void incluirRota(Long roteiroId, Integer ordem, String nome) {
		

		Roteiro roteiro = new Roteiro();
		roteiro.setId(roteiroId);
		Rota rota = new Rota();
		rota.setRoteiro(roteiro);
		rota.setOrdem(ordem);
		rota.setDescricaoRota(nome);
		validarCampoObrigatoriosRota(rota);
		roteirizacaoService.incluirRota(rota);
		result.use(Results.json()).from(
		new ValidacaoVO(TipoMensagem.SUCCESS, "Rota cadastrada com sucesso."),
						"result").recursive().serialize();

	}
	@Path("/iniciaTelaRota")
	public void iniciaTelaRota() {
			
		result.use(Results.json()).from(10).recursive().serialize();
	}
	private void validarCampoObrigatoriosRota(Rota rota) {
		
		List<String> mensagens = new ArrayList<String>();
		
		if(rota.getOrdem() == null){
			mensagens.add("O campo Ordem é obrigatório.");
		}
		if("".equals(rota.getDescricaoRota())){
			mensagens.add("O campo Nome é obrigatório.");
		}
	
		
		if (!mensagens.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagens));
		}
	}
	
	
	@Path("/excluiRotas")
	public void excluiRotas(List<Long> rotasId) {
		roteirizacaoService.excluirListaRota(rotasId);
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Rota cadastrada com sucesso."),"result").recursive().serialize();

	}
}
