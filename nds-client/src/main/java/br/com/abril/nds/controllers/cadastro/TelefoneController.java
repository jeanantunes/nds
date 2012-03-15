package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TelefoneFornecedor;
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.service.TelefoneService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/telefone")
public class TelefoneController {

	public static final String LISTA_TELEFONES_SALVAR_SESSAO = "listaTelefonesSalvarSessao";
	
	public static final String LISTA_TELEFONES_REMOVER_SESSAO = "listaTelefonesRemoverSessao";
	
	private Result result;
	
	private HttpSession httpSession;
	
	@Autowired
	private TelefoneService telefoneService;
	
	public TelefoneController(Result result, HttpSession httpSession){
		this.result = result;
		this.httpSession = httpSession;
	}
	
	@Path("/")
	public void index(){}
	
	@Post
	public void pesquisarTelefones(){
		Map<Integer, TelefoneAssociacaoDTO> telefonesSessao = this.obterTelefonesSalvarSessao();
		
		this.result.use(Results.json()).from(this.getTableModelListaEndereco(telefonesSessao), "result").recursive().serialize();
	}
	
	@Post
	public void adicionarTelefone(Integer referencia, String tipoTelefone, String ddd, 
			String numero, String ramal, boolean principal){
		
		this.validarDadosEntrada(tipoTelefone, ddd, numero);
		
		Map<Integer, TelefoneAssociacaoDTO> telefonesSessao = this.obterTelefonesSalvarSessao();
		
		TelefoneAssociacaoDTO telefoneAssociacaoDTO = null;
		Telefone telefone = null;
		
		if (referencia == null){
			telefoneAssociacaoDTO = new TelefoneAssociacaoDTO();
			telefone = new Telefone();
			
			telefoneAssociacaoDTO.setReferencia((int) (new Date()).getTime() * -1);
			telefoneAssociacaoDTO.setTelefone(telefone);
		} else {
			telefoneAssociacaoDTO = telefonesSessao.get(referencia);
		}
		
		telefoneAssociacaoDTO.setPrincipal(principal);
		telefoneAssociacaoDTO.setTipoTelefone(Util.getEnumByStringValue(TipoTelefone.values(), tipoTelefone));
		
		telefoneAssociacaoDTO.getTelefone().setDdd(ddd);
		telefoneAssociacaoDTO.getTelefone().setNumero(numero);
		telefoneAssociacaoDTO.getTelefone().setRamal(ramal);
		
		telefonesSessao.put(telefoneAssociacaoDTO.getReferencia(), telefoneAssociacaoDTO);
		
		this.httpSession.setAttribute(LISTA_TELEFONES_SALVAR_SESSAO, telefonesSessao);
		
		this.pesquisarTelefones();
	}

	@Post
	public void removerTelefone(Integer referencia){
		Map<Integer, TelefoneAssociacaoDTO> telefonesSalvarSessao = 
			this.obterTelefonesSalvarSessao();
		
		TelefoneAssociacaoDTO telefoneAssociacaoDTO = telefonesSalvarSessao.remove(referencia);
		
		if (telefoneAssociacaoDTO != null &&
				telefoneAssociacaoDTO.getTelefone() != null &&
				telefoneAssociacaoDTO.getTelefone().getId() != null){
			
			Set<Long> setTelefonesRemover = this.obterTelefonesRemoverSessao();
			setTelefonesRemover.add(telefoneAssociacaoDTO.getTelefone().getId());
			
			this.httpSession.setAttribute(LISTA_TELEFONES_REMOVER_SESSAO, setTelefonesRemover);
		}
		
		this.httpSession.setAttribute(LISTA_TELEFONES_SALVAR_SESSAO, telefonesSalvarSessao);
		
		this.pesquisarTelefones();
	}
	
	@Post
	public void editarTelefone(Integer referencia){
		Map<Integer, TelefoneAssociacaoDTO> telefonesSessao = this.obterTelefonesSalvarSessao();
		
		TelefoneAssociacaoDTO telefoneAssociacaoDTO = telefonesSessao.get(referencia);
		
		if (telefoneAssociacaoDTO != null){
			this.result.use(Results.json()).from(telefoneAssociacaoDTO, "result").recursive().serialize();
		} else {
			this.result.use(Results.json()).from("", "result").recursive().serialize();
		}
	}
	
	@SuppressWarnings("unchecked")
	private Map<Integer, TelefoneAssociacaoDTO> obterTelefonesSalvarSessao(){
		Map<Integer, TelefoneAssociacaoDTO> telefonesSessao = (Map<Integer, TelefoneAssociacaoDTO>) 
				this.httpSession.getAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		
		if (telefonesSessao == null){
			telefonesSessao = new LinkedHashMap<Integer, TelefoneAssociacaoDTO>();
		}
		
		return telefonesSessao;
	}
	
	@SuppressWarnings("unchecked")
	private Set<Long> obterTelefonesRemoverSessao(){
		Set<Long> telefonesSessao = (Set<Long>) 
				this.httpSession.getAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
		
		if (telefonesSessao == null){
			telefonesSessao = new HashSet<Long>();
		}
		
		return telefonesSessao;
	}
	
	private TableModel<CellModel> getTableModelListaEndereco(Map<Integer, TelefoneAssociacaoDTO> listaEnderecoAssociacao) {

		TableModel<CellModel> tableModel = new TableModel<CellModel>();

		List<CellModel> listaCellModel = new ArrayList<CellModel>();

		for (Integer key : listaEnderecoAssociacao.keySet()) {
			
			TelefoneAssociacaoDTO telefoneAssociacao = listaEnderecoAssociacao.get(key);

			CellModel cellModel = new CellModel(
				telefoneAssociacao.getReferencia(),
				telefoneAssociacao.getTipoTelefone() == null ? "" : 
					TipoTelefone.getDescricao(telefoneAssociacao.getTipoTelefone()),
				telefoneAssociacao.getTelefone().getDdd(),
				telefoneAssociacao.getTelefone().getNumero(),
				telefoneAssociacao.getTelefone().getRamal(),
				String.valueOf(telefoneAssociacao.isPrincipal())
			);

			listaCellModel.add(cellModel);
		}

		tableModel.setPage(1);
		tableModel.setRows(listaCellModel);
		tableModel.setTotal(listaCellModel.size()); 

		return tableModel;
	}
	
	private void validarDadosEntrada(String tipoTelefone, String ddd, String numero) {
		List<String> listaValidacao = new ArrayList<String>();
		
		if (Util.getEnumByStringValue(TipoTelefone.values(), tipoTelefone) == null){
			listaValidacao.add("Tipo de telefone é obrigatório");
		}
		
		if (ddd == null || ddd.trim().isEmpty()){
			listaValidacao.add("DDD é obrigatório");
		}
		
		if (numero == null || numero.trim().isEmpty()){
			listaValidacao.add("Número é obrigatório");
		}
		
		if (!listaValidacao.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, listaValidacao));
		}
	}
	
	//OS MÉTODOS A BAIXO FORAM CRIADOS APENAS PARA TESTE, JA QUE ESSA TELA SERÁ INCLUDE PARA OUTRAS
	@Post
	public void cadastrar(Long idCota, Long idFornecedor){
		List<TelefoneAssociacaoDTO> lista = null;
		
		if (idCota != null){
			lista = this.telefoneService.buscarTelefonesCota(idCota, null);
		} else if (idFornecedor != null){
			lista = this.telefoneService.buscarTelefonesFornecedor(idFornecedor, null);
		}
		
		Map<Integer, TelefoneAssociacaoDTO> map = new LinkedHashMap<Integer, TelefoneAssociacaoDTO>();
		
		for (TelefoneAssociacaoDTO telefoneAssociacaoDTO : lista){
			map.put(telefoneAssociacaoDTO.getReferencia(), telefoneAssociacaoDTO);
		}
		
		this.httpSession.removeAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
		
		this.httpSession.setAttribute(LISTA_TELEFONES_SALVAR_SESSAO, map);
		
		this.pesquisarTelefones();
	}
	
	@Post
	public void salvar(Long idCota, Long idFornecedor){
		Map<Integer, TelefoneAssociacaoDTO> map = this.obterTelefonesSalvarSessao();
		
		if (idCota != null){
			Cota cota = new Cota();
			cota.setId(idCota);
			List<TelefoneCota> lista = new ArrayList<TelefoneCota>();
			for (Integer key : map.keySet()){
				TelefoneAssociacaoDTO telefoneAssociacaoDTO = map.get(key);
				if (telefoneAssociacaoDTO.getTipoTelefone() != null){
					TelefoneCota telefoneCota = new TelefoneCota();
					telefoneCota.setCota(cota);
					telefoneCota.setPrincipal(telefoneAssociacaoDTO.isPrincipal());
					telefoneCota.setTelefone(telefoneAssociacaoDTO.getTelefone());
					telefoneCota.setTipoTelefone(telefoneAssociacaoDTO.getTipoTelefone());
					
					lista.add(telefoneCota);
				}
			}
			
			Set<Long> telefonesRemover = this.obterTelefonesRemoverSessao();
			
			this.telefoneService.cadastrarTelefonesCota(lista, telefonesRemover);
		} else if (idFornecedor != null){
			Fornecedor fornecedor = new Fornecedor();
			fornecedor.setId(idFornecedor);
			List<TelefoneFornecedor> lista = new ArrayList<TelefoneFornecedor>();
			for (Integer key : map.keySet()){
				TelefoneAssociacaoDTO telefoneAssociacaoDTO = map.get(key);
				if (telefoneAssociacaoDTO.getTipoTelefone() != null){
					TelefoneFornecedor telefoneFornecedor = new TelefoneFornecedor();
					telefoneFornecedor.setFornecedor(fornecedor);
					telefoneFornecedor.setPrincipal(telefoneAssociacaoDTO.isPrincipal());
					telefoneFornecedor.setTelefone(telefoneAssociacaoDTO.getTelefone());
					telefoneFornecedor.setTipoTelefone(telefoneAssociacaoDTO.getTipoTelefone());
					
					lista.add(telefoneFornecedor);
				}
			}
			
			Set<Long> telefonesRemover = this.obterTelefonesRemoverSessao();
			
			this.telefoneService.cadastrarTelefonesFornecedor(lista, telefonesRemover);
		}
		
		this.httpSession.removeAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		this.httpSession.removeAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
		
		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."), "result").recursive().serialize();
		
		//this.pesquisarTelefones();
	}
}