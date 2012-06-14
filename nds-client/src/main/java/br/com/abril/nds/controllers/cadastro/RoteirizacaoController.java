package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.CotaDisponivelRoteirizacaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.LogBairro;
import br.com.abril.nds.model.LogLocalidade;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.CotaService;
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
	private CotaService cotaService;
	
	@Autowired
	private Result result;


	@Path("/")
	public void index() {
		carregarComboBox();
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
	
	@Path("/carregarComboRoteiro")
	public void carregarComboRoteiro(Long boxId) {
		List<Roteiro> roteiros = roteirizacaoService.buscarRoteiroDeBox(boxId);
		result.use(Results.json()).from(roteiros, "result").serialize();
	}
	
	@Path("/carregarComboRota")
	public void carregarComboRota(Long roteiroId) {
		List<Rota> rotas = roteirizacaoService.buscarRotaPorRoteiro(roteiroId);
		result.use(Results.json()).from(rotas, "result").serialize();
	
	}
	
	
	@Path("/carregarComboRoteiroEspecial")
	public void carregarComboRoteiroEspecial() {
		List<Roteiro> roteiros = roteirizacaoService.buscarRoteiroEspecial();
		result.use(Results.json()).from(roteiros, "result").serialize();
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
		
		if(TipoRoteiro.NORMAL.compareTo(roteiro.getTipoRoteiro()) == 0 &&  roteiro.getBox() == null){
			mensagens.add("O campo Box é obrigatório.");
		}
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
	public void iniciaTelaCotas() {
		List<String> uf = roteirizacaoService.buscarUF();
		result.use(Results.json()).from(uf, "result").serialize();
	}
	
	@Path("/buscalistaMunicipio")
	public void buscalistaMunicipio(String uf) {
		List<LogLocalidade> lista = roteirizacaoService.buscarMunicipioPorUf(uf);
		result.use(Results.json()).from(lista, "result").serialize();
	}
	
	@Path("/buscalistaBairro")
	public void buscalistaBairro(String uf, Long municipio) {
		List<LogBairro> bairro = roteirizacaoService.buscarBairroPorMunicipio(municipio, uf);
		result.use(Results.json()).from(bairro, "result").serialize();
	}
	
	@Path("/buscarPvsPorCota")
	public void buscarPvsPorCota(Integer numeroCota,  Long rotaId, Long roteiroId,  String sortname, String sortorder, int rp, int page) {
		List<CotaDisponivelRoteirizacaoDTO> lista = roteirizacaoService.buscarPvsPorCota(numeroCota, rotaId , roteiroId );
		int quantidade = lista.size();
		result.use(FlexiGridJson.class).from(lista).total(quantidade).page(page).serialize();
		
	}
	
	@Path("/confirmaRoteirizacao")
	public void confirmaRoteirizacao(List<CotaDisponivelRoteirizacaoDTO> lista, Long idRota) {
		roteirizacaoService.gravaRoteirizacao(lista, idRota);
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Roteirização cadastrada com sucesso."),"result").recursive().serialize();
		
	}
	@Path("/buscarRotaPorId")
	public void buscarRotaPorId(Long rotaId) {
		Rota rota =  roteirizacaoService.buscarRotaPorId(rotaId);
		result.use(Results.json()).from(rota, "result").serialize();
		
	}
	
	
	@Post
	public void autoCompletarRotaPorDescricao(Long roteiroId, String nomeRota) {
		List<Rota> lista = roteirizacaoService.buscarRotaPorNome(roteiroId, nomeRota, MatchMode.ANYWHERE) ;
		
		List<ItemAutoComplete> listaRotaoAutoComplete = new ArrayList<ItemAutoComplete>();
		
		if (lista != null && !lista.isEmpty()) {
			
			for (Rota rota : lista) {
				rota.setRoteiro(null);
				rota.setRoteirizacao(null);
				listaRotaoAutoComplete.add(new ItemAutoComplete(rota.getDescricaoRota(), null,rota ));
			}
		}
		
		this.result.use(Results.json()).from(listaRotaoAutoComplete, "result").include("chave").serialize();
	}
	
	@Path("/transferirRoteirizacao")
	public void transferirRoteirizacao(List<Long> roteirizacaoId, String rotaNome , Long roteiroId) {
		Rota rota = null;
		if ( rotaNome != null ) {
			List<Rota> listaRotas  = roteirizacaoService.buscarRotaPorNome(roteiroId, rotaNome, MatchMode.EXACT);
			if (!listaRotas.isEmpty() ){
				rota = listaRotas.get(0);
			} 
			
		}
		roteirizacaoService.transferirRoteirizacao(roteirizacaoId, rota);
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Roteirização transferida com sucesso."),"result").recursive().serialize();

	}
	
	@Path("/excluirRoteirizacao")
	public void excluirRoteirizacao(List<Long> roteirizacaoId) {
		roteirizacaoService.excluirRoteirizacao(roteirizacaoId);
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Roteirização excluida com sucesso."),"result").recursive().serialize();

	}	
	
	@Path("/buscarPvsPorEndereco")
	public void buscarPvsPorEndereco(String CEP, String uf, String municipio, String bairro , Long rotaId, Long roteiroId,  String sortname, String sortorder, int rp, int page) {
		List<CotaDisponivelRoteirizacaoDTO> lista = roteirizacaoService.buscarRoteirizacaoPorEndereco(CEP, uf, municipio, bairro,  rotaId , roteiroId );
		int quantidade = lista.size();
		result.use(FlexiGridJson.class).from(lista).total(quantidade).page(page).serialize();
	}
	
	@Path("/pesquisarRoteirizacao")
	public void pesquisarRoteirizacao(Long boxId, Long roteiroId, Long rotaId, TipoRoteiro tipoRoteiro, String sortname, String sortorder, int rp, int page) {
		List<Roteirizacao> lista = roteirizacaoService.buscarRoteirizacao( boxId,  roteiroId,  rotaId,  tipoRoteiro, sortname, Ordenacao.valueOf(sortorder.toUpperCase()), page*rp - rp , rp);
		//String  orderBy, Ordenacao ordenacao, int initialResult, int maxResults
		int quantidade = lista.size();
		result.use(FlexiGridJson.class).from(lista).total(quantidade).page(page).serialize();
	}
	
	@Path("/pesquisarRoteirizacaoPorCota")
	public void pesquisarRoteirizacaoPorCota(Integer numeroCota, Long roteiroId, Long rotaId, TipoRoteiro tipoRoteiro,  String sortname, String sortorder, int rp, int page) {
		List<Roteirizacao> lista = roteirizacaoService.buscarRoteirizacaoPorNumeroCota(numeroCota, tipoRoteiro, sortname, Ordenacao.valueOf(sortorder.toUpperCase()), page*rp - rp , rp);
		if (lista == null || lista.isEmpty() ) {
			result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, "Nenhum registro encontrado."),"result").recursive().serialize();
		} else {
			int quantidade = lista.size();
			result.use(FlexiGridJson.class).from(lista).total(quantidade).page(page).serialize();
		}
	}
	
	@Path("/buscaCotaPorNumero")
	public void buscaCotaPorNumero(Integer numeroCota) {
		if ( numeroCota == null  ) {
			result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, "Informe o numero da cota."),"result").recursive().serialize();
		} else {
			Cota cota =  cotaService.obterPorNumeroDaCota(numeroCota);
			if (cota == null ){
				result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, "Nenhum registro encontrado."),"result").recursive().serialize();
			} else {
				result.use(Results.json()).from(cota, "result").include("pessoa").serialize();
			}		
		}
	}
	

	
	@Path("/transferirRoteirizacaoComNovaRota")
	public void transferirRoteirizacaoComNovaRota(List<Long> roteirizacaoId, String rotaNome , Long roteiroId, Integer ordem) {
		Rota rota = new Rota();
		rota.setDescricaoRota(rotaNome);
		rota.setOrdem(ordem);
		Roteiro roteiro = new Roteiro();
		roteiro.setId(roteiroId);
		rota.setRoteiro(roteiro);
		roteirizacaoService.transferirRoteirizacaoComNovaRota(roteirizacaoId, rota);
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Roteirização transferida com sucesso."),"result").recursive().serialize();

	}
	
	@Path("/atualizaOrdenacaoAsc")
	public void atualizaOrdenacaoAsc(Long roteirizacaoId, Long rotaId , Long roteiroId, Integer ordem, Long pontoVendaId , Ordenacao ordenacao) {
		Rota rota = new Rota();
		rota.setId(rotaId);
		Roteiro roteiro = new Roteiro();
		roteiro.setId(roteiroId);
		rota.setRoteiro(roteiro);
		PDV pdv = new PDV();
		pdv.setId(pontoVendaId);
		Roteirizacao roteirizacao = new Roteirizacao();
		roteirizacao.setOrdem(ordem);
		roteirizacao.setRota(rota);
		roteirizacao.setPdv(pdv);
		roteirizacao.setId(roteirizacaoId);
		if ( Ordenacao.DESC.compareTo(ordenacao) == 0){
			roteirizacaoService.atualizaOrdenacaoDesc(roteirizacao);
		} else {
			roteirizacaoService.atualizaOrdenacaoAsc(roteirizacao);
		}
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Roteirização transferida com sucesso."),"result").recursive().serialize();

	}

}
