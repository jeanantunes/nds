package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.ls.LSInput;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.CotaDisponivelRoteirizacaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EnderecoService;
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
		
		//TODO alteração de roteirização - foi deletado do sistema a entidade RotaRoteiroOperacao
	}
	
	@Path("/incluirRoteiro")
	public void incluirRoteiro(Long idBox, Integer ordem, String nome, TipoRoteiro tipoRoteiro) {
		

		Roteiro roteiro = populaRoteiro(idBox, ordem, nome, tipoRoteiro);
		validarCampoObrigatoriosRoteiro(roteiro);
		roteirizacaoService.incluirRoteiro(roteiro);
		result.use(Results.json()).from(
		new ValidacaoVO(TipoMensagem.SUCCESS, "Roreito cadastrado com sucesso."),
						"result").recursive().serialize();

	}
	@Path("/iniciaTelaRoteiro")
	public void iniciaTelaRoteiro() {
		Integer ordem = roteirizacaoService.buscarMaiorOrdemRoteiro();
		ordem++;
		result.use(Results.json()).from(ordem).recursive().serialize();
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
					roteiro.getBox().setRoteiros(null);
				}	
				listaRoteiroAutoComplete.add(new ItemAutoComplete(roteiro.getDescricaoRoteiro(), null,roteiro ));
			}
		}
		
		this.result.use(Results.json()).from(listaRoteiroAutoComplete, "result").include("chave").serialize();
	}
	
	@Post
	public void buscaRoteiroPorDescricao(String descricao) {
		
		
		List<Roteiro> listaRoteiro = roteirizacaoService.buscarRoteiroPorDescricao(descricao, MatchMode.EXACT);
		if (listaRoteiro.isEmpty()){
			//throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Não existe roteiro"));
			result.use(Results.json()).from(
					new ValidacaoVO(TipoMensagem.ERROR, "Roteiro inexistente."),
									"result").recursive().serialize();
		} else {
			this.result.use(Results.json()).from(listaRoteiro, "result").include("box").serialize();
		}	
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
	public void iniciaTelaRota(Long idRoteiro) {
		Integer ordem = roteirizacaoService.buscarMaiorOrdemRota(idRoteiro);
		if (ordem == null){
			ordem = 0;
		}
		ordem++;
		result.use(Results.json()).from(ordem).recursive().serialize();
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
	public void excluiRotas(List<Long> rotasId, Long roteiroId) {
		roteirizacaoService.excluirListaRota(rotasId, roteiroId);
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Rotas excluidas com sucesso."),"result").recursive().serialize();

	}
	
	@Path("/transferirRotas")
	public void transferirRotas(List<Long> rotasId, Long roteiroId, String roteiroNome) {
		if ( roteiroId == null ) {
			List<Roteiro> listaRoteiros  = roteirizacaoService.buscarRoteiroPorDescricao(roteiroNome, MatchMode.EXACT);
			if (!listaRoteiros.isEmpty() ){
				roteiroId = listaRoteiros.get(0).getId();
			} 
			
		}
		roteirizacaoService.transferirListaRota(rotasId, roteiroId) ;
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Rotas transferidas com sucesso."),"result").recursive().serialize();

	}
	
	@Path("/transferirRotasComNovoRoteiro")
	public void transferirRotasComNovoRoteiro(List<Long> rotasId, Long idBox, Integer ordem, String roteiroNome, TipoRoteiro tipoRoteiro) {
		Roteiro roteiro = populaRoteiro(idBox, ordem, roteiroNome, tipoRoteiro);
		validarCampoObrigatoriosRoteiro(roteiro);
		roteirizacaoService.transferirListaRotaComNovoRoteiro(rotasId, roteiro);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Rotas transferidas com sucesso."),"result").recursive().serialize();

	}

	private Roteiro populaRoteiro(Long idBox, Integer ordem,
			String roteiroNome, TipoRoteiro tipoRoteiro) {
		Roteiro roteiro = new Roteiro();
		if ( idBox != null ){
			Box box = new Box();
			box.setId(idBox);
			roteiro.setBox(box);
		}	
		roteiro.setOrdem(ordem);
		roteiro.setDescricaoRoteiro(roteiroNome);
		roteiro.setTipoRoteiro(tipoRoteiro);
		return roteiro;
	}
	
	@Path("/pesquisarRotaPorNome")
	public void pesquisarRotaPorNome(Long roteiroId, String nomeRota,
			String sortname, String sortorder, int rp, int page) {
		List<Rota> lista = roteirizacaoService.buscarRotaPorNome(roteiroId, nomeRota, MatchMode.ANYWHERE) ;
		int quantidade = lista.size();
		result.use(FlexiGridJson.class).from(lista).total(quantidade).page(page).serialize();

	}
	
	@Path("/buscarRoterizacaoPorRota")
	public void buscarRoterizacaoPorRota(Long rotaId,
			String sortname, String sortorder, int rp, int page) {
		List<CotaDisponivelRoteirizacaoDTO>lista = roteirizacaoService.buscarRoterizacaoPorRota(rotaId) ;
		int quantidade = lista.size();
		result.use(FlexiGridJson.class).from(lista).total(quantidade).page(page).serialize();

	}
	
	@Path("/iniciaTelaCotas")
	public void buscarRoterizacaoPorRota() {
		//List<String> uf = enderecoService.obterUF();
		result.use(Results.json()).from("", "result").serialize();
	}
	
	@Path("/buscalistaMunicipio")
	public void buscalistaMunicipio(String uf) {
		//List<String> municipios = enderecoService.obterMunicipioPorUf(uf);
		result.use(Results.json()).from("", "result").serialize();
	}
	
	@Path("/buscalistaBairro")
	public void buscalistaBairro(String uf, String municipio) {
		//List<String> bairro = enderecoService.obterBairroPorMunicipio(municipio) ;
		result.use(Results.json()).from("", "result").serialize();
	}
	
	@Path("/buscarPvsPorCota")
	public void buscarPvsPorCota(Integer numeroCota, String sortname, String sortorder, int rp, int page) {
		List<CotaDisponivelRoteirizacaoDTO> lista = roteirizacaoService.buscarPvsPorCota(numeroCota);
		int quantidade = lista.size();
		result.use(FlexiGridJson.class).from(lista).total(quantidade).page(page).serialize();
		
	}
	
	@Path("/confirmaRoteirizacao")
	public void confirmaRoteirizacao(List<CotaDisponivelRoteirizacaoDTO> lista, Long idRota) {
		roteirizacaoService.gravaRoteirizacao(lista, idRota);
		
	}
	@Path("/buscarRotaPorId")
	public void buscarRotaPorId(Long rotaId) {
		 Rota rota =  roteirizacaoService.buscarRotaPorId(rotaId);
		result.use(Results.json()).from(rota, "result").serialize();
		
		
	}
	
	
	
}
