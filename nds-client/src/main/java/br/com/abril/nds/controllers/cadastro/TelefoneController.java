package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneFiador;
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.service.FiadorService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
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
	private FiadorService fiadorService;
	
	public TelefoneController(Result result, HttpSession httpSession){
		this.result = result;
		this.httpSession = httpSession;
	}
	
	@Path("/")
	public void index(){}
	
	@Post
	public void pesquisarTelefones(String sortname, String sortorder){
		Map<Integer, TelefoneAssociacaoDTO> telefonesSessao = this.obterTelefonesSalvarSessao();
		
		List<TelefoneAssociacaoDTO> listaTelefones = null;
		
		Long idFiador = (Long) this.httpSession.getAttribute(FiadorController.ID_FIADOR_EDICAO);
		
		if (idFiador != null){
			
			Set<Long> idsTelefonesSessao = new HashSet<Long>();
			
			if (!telefonesSessao.isEmpty()){
				
				for (TelefoneAssociacaoDTO dto : telefonesSessao.values()){
					
					idsTelefonesSessao.add(dto.getTelefone().getId());
				}
			}
			
			idsTelefonesSessao.addAll(this.obterTelefonesRemoverSessao());
			
			listaTelefones = this.fiadorService.buscarTelefonesFiador(idFiador, idsTelefonesSessao);
		}
		
		if (listaTelefones != null){
			for (TelefoneAssociacaoDTO tDto : listaTelefones){
				telefonesSessao.put(tDto.getReferencia(), tDto);
			}
		}
		
		if (sortname != null) {

			sortorder = sortorder == null ? "asc" : sortorder;

			Ordenacao ordenacao = Util.getEnumByStringValue(Ordenacao.values(), sortorder);
			
			LinkedList<TelefoneAssociacaoDTO> lista = new LinkedList<TelefoneAssociacaoDTO>();
			for (Integer key : telefonesSessao.keySet()){
				lista.add(telefonesSessao.get(key));
			}
			
			telefonesSessao = new LinkedHashMap<Integer, TelefoneAssociacaoDTO>();
			
			for (Object dto : PaginacaoUtil.ordenarEmMemoria(lista, ordenacao, sortname)){
				TelefoneAssociacaoDTO ref = (TelefoneAssociacaoDTO) dto;
				telefonesSessao.put(ref.getReferencia(), ref);
			}
		}

		this.result.use(Results.json()).from(this.getTableModelListaEndereco(telefonesSessao), "result").recursive().serialize();
	}
	
	@Post
	public void adicionarTelefone(Integer referencia, String tipoTelefone, String ddd, 
			String numero, String ramal, boolean principal){
		
		this.validarDadosEntrada(tipoTelefone, ddd, numero, principal, referencia);
		
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
		
		this.pesquisarTelefones(null, null);
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
		} else {
			
			Set<Long> setTelefonesRemover = this.obterTelefonesRemoverSessao();
			setTelefonesRemover.add(referencia.longValue());
			
			this.httpSession.setAttribute(LISTA_TELEFONES_REMOVER_SESSAO, setTelefonesRemover);
		}
		
		this.httpSession.setAttribute(LISTA_TELEFONES_SALVAR_SESSAO, telefonesSalvarSessao);
		
		this.pesquisarTelefones(null, null);
	}
	
	@Post
	public void editarTelefone(Integer referencia){
		Map<Integer, TelefoneAssociacaoDTO> telefonesSessao = this.obterTelefonesSalvarSessao();
		
		TelefoneAssociacaoDTO telefoneAssociacaoDTO = telefonesSessao.get(referencia);
		
		
		
		if (telefoneAssociacaoDTO != null){
			
			this.result.use(Results.json()).from(telefoneAssociacaoDTO, "result").recursive().serialize();
		} else {
			
			telefoneAssociacaoDTO = this.buscarAssociacaoTelefone(referencia);
			
			if (telefoneAssociacaoDTO != null){
				
				this.obterTelefonesSalvarSessao().put(referencia, telefoneAssociacaoDTO);
				
				this.httpSession.setAttribute(LISTA_TELEFONES_SALVAR_SESSAO, this.obterTelefonesSalvarSessao());
				
				this.result.use(Results.json()).from(telefoneAssociacaoDTO, "result").recursive().serialize();
			} else {
				this.result.use(Results.json()).from("", "result").serialize();
			}
		}
	}
	
	private TelefoneAssociacaoDTO buscarAssociacaoTelefone(Integer referencia) {
		
		Long idFiador = (Long) this.httpSession.getAttribute(FiadorController.ID_FIADOR_EDICAO);
		
		if (idFiador != null){
			
			TelefoneFiador telefoneFiador = this.fiadorService.buscarTelefonePorTelefoneFiador(idFiador, referencia.longValue());
			
			TelefoneAssociacaoDTO dto = new TelefoneAssociacaoDTO();
			dto.setPrincipal(telefoneFiador.isPrincipal());
			dto.setReferencia(referencia);
			dto.setTelefone(telefoneFiador.getTelefone());
			dto.setTipoTelefone(telefoneFiador.getTipoTelefone());
			
			return dto;
		}
		
		return null;
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
	
	private void validarDadosEntrada(String tipoTelefone, String ddd, String numero, boolean principal, Integer referencia) {
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
		
		if (principal){
			Map<Integer, TelefoneAssociacaoDTO> telefones = this.obterTelefonesSalvarSessao();
			
			for (Integer key : telefones.keySet()){
				
				if (telefones.get(key).isPrincipal() && (referencia != null && !referencia.equals(telefones.get(key).getReferencia()))){
					
					listaValidacao.add("Já existe um telefone principal.");
					break;
				}
			}
		}
		
		if (!listaValidacao.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, listaValidacao));
		}
	}
}