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
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.service.TelefoneService;
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

	private static String LISTA_TELEFONES_SALVAR_SESSAO = "";
	
	private static String LISTA_TELEFONES_REMOVER_SESSAO = "";
	
	private static String LISTA_TELEFONES_EXIBICAO = "";
	
	private Result result;
	
	private HttpSession httpSession;
	
	@Autowired
	private TelefoneService telefoneService;
	
	public enum Tela{
		
		FIADOR,
		COTA,
		ENTREGADOR,
		TRANSPORTADOR,
		TELEFONE_PDV;
		
		public void setarParametros(){
			
			switch (this){
				case FIADOR:
					TelefoneController.setarParametros(
							FiadorController.LISTA_TELEFONES_SALVAR_SESSAO, 
							FiadorController.LISTA_TELEFONES_REMOVER_SESSAO, 
							FiadorController.LISTA_TELEFONES_EXIBICAO);
				break;
				case COTA:
					TelefoneController.setarParametros(
							CotaController.LISTA_TELEFONES_SALVAR_SESSAO, 
							CotaController.LISTA_TELEFONES_REMOVER_SESSAO, 
							CotaController.LISTA_TELEFONES_EXIBICAO);
				break;
				case ENTREGADOR:
					TelefoneController.setarParametros(
							EntregadorController.LISTA_TELEFONES_SALVAR_SESSAO, 
							EntregadorController.LISTA_TELEFONES_REMOVER_SESSAO, 
							EntregadorController.LISTA_TELEFONES_EXIBICAO);
				break;
				case TRANSPORTADOR:
					TelefoneController.setarParametros(
							TransportadorController.LISTA_TELEFONES_SALVAR_SESSAO, 
							TransportadorController.LISTA_TELEFONES_REMOVER_SESSAO, 
							TransportadorController.LISTA_TELEFONES_EXIBICAO);
				break;
				case TELEFONE_PDV:
					TelefoneController.setarParametros(
							PdvController.LISTA_TELEFONES_SALVAR_SESSAO, 
							PdvController.LISTA_TELEFONES_REMOVER_SESSAO, 
							PdvController.LISTA_TELEFONES_EXIBICAO);
				break;
			}
		}
	}
	
	public TelefoneController(Result result, HttpSession httpSession){
		this.result = result;
		this.httpSession = httpSession;
	}
	
	public static void setarParametros(String listaSalvar, String listaRemover, String listaExibir){
		
		LISTA_TELEFONES_SALVAR_SESSAO = listaSalvar;
		LISTA_TELEFONES_REMOVER_SESSAO = listaRemover;
		LISTA_TELEFONES_EXIBICAO = listaExibir;
	}
	
	@Path("/")
	public void index(){}
	
	@Post
	public void pesquisarTelefones(Tela tela, String sortname, String sortorder){
		
		tela.setarParametros();
		
		Map<Integer, TelefoneAssociacaoDTO> telefonesSessao = this.obterTelefonesSalvarSessao();
		
		List<TelefoneAssociacaoDTO> listaTelefonesExibir = this.obterTelefonesExibicao();
		
		Set<Long> telefonesRemover = this.obterTelefonesRemoverSessao();
		
		if (listaTelefonesExibir != null){
			
			for (TelefoneAssociacaoDTO tDto : listaTelefonesExibir){
				
				if (!telefonesRemover.contains(new Long(tDto.getReferencia()))){
					
					telefonesSessao.put(tDto.getReferencia(), tDto);
				}
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
	public void adicionarTelefone(Tela tela, Integer referencia, String tipoTelefone, String ddd, 
			String numero, String ramal, boolean principal){
		
		tela.setarParametros();
		
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
		
		if (telefoneAssociacaoDTO == null){
			
			Telefone tel = this.telefoneService.buscarTelefonePorId(referencia.longValue());
			
			telefoneAssociacaoDTO = new TelefoneAssociacaoDTO();
			telefoneAssociacaoDTO.setReferencia(referencia);
			telefoneAssociacaoDTO.setTelefone(tel);
		}
		
		telefoneAssociacaoDTO.setPrincipal(principal);
		telefoneAssociacaoDTO.setTipoTelefone(Util.getEnumByStringValue(TipoTelefone.values(), tipoTelefone));
		
		telefoneAssociacaoDTO.getTelefone().setDdd(ddd);
		telefoneAssociacaoDTO.getTelefone().setNumero(numero);
		telefoneAssociacaoDTO.getTelefone().setRamal(ramal);
		
		telefonesSessao.put(telefoneAssociacaoDTO.getReferencia(), telefoneAssociacaoDTO);
		
		List<TelefoneAssociacaoDTO> listaExibicao = this.obterTelefonesExibicao();
		for (int index = 0 ; index < listaExibicao.size() ; index++){
			
			if (referencia != null && referencia.equals(listaExibicao.get(index).getReferencia())){
				
				listaExibicao.remove(index);
				
				this.httpSession.setAttribute(LISTA_TELEFONES_EXIBICAO, listaExibicao);
				break;
			}
		}
		
		this.httpSession.setAttribute(LISTA_TELEFONES_SALVAR_SESSAO, telefonesSessao);
		
		this.pesquisarTelefones(tela, null, null);
	}

	@Post
	public void removerTelefone(Tela tela, Integer referencia){
		
		tela.setarParametros();
		
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
		
		this.pesquisarTelefones(tela, null, null);
	}
	
	@Post
	public void editarTelefone(Tela tela, Integer referencia){
		
		tela.setarParametros();
		
		Map<Integer, TelefoneAssociacaoDTO> telefonesSessao = this.obterTelefonesSalvarSessao();
		
		TelefoneAssociacaoDTO telefoneAssociacaoDTO = telefonesSessao.get(referencia);
		
		if (telefoneAssociacaoDTO == null){
			
			List<TelefoneAssociacaoDTO> listaExibicao = this.obterTelefonesExibicao();
			
			for (TelefoneAssociacaoDTO t : listaExibicao){
				
				if (referencia.equals(t.getReferencia())){
					
					telefoneAssociacaoDTO = t;
					break;
				}
			}
			
			if (telefoneAssociacaoDTO == null){
				
				Telefone telefone = this.telefoneService.buscarTelefonePorId(referencia.longValue());
				
				if (telefone != null){
					
					telefoneAssociacaoDTO = new TelefoneAssociacaoDTO(false, null, null, telefone);
				}
			}
		}
		
		this.result.use(Results.json()).from(telefoneAssociacaoDTO == null ? "" : telefoneAssociacaoDTO, "result").recursive().exclude("telefone.pessoa").serialize();
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
	private List<TelefoneAssociacaoDTO> obterTelefonesExibicao(){
		
		List<TelefoneAssociacaoDTO> telefonesExibicao = (List<TelefoneAssociacaoDTO>)
				this.httpSession.getAttribute(LISTA_TELEFONES_EXIBICAO);
		
		if (telefonesExibicao == null){
			
			telefonesExibicao = new ArrayList<TelefoneAssociacaoDTO>();
		}
		
		return telefonesExibicao;
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
				
				if (referencia == null){
					
					if (telefones.get(key).isPrincipal()){
						listaValidacao.add("Já existe um telefone principal.");
						break;
					}
					
				} else {
				
					if (telefones.get(key).isPrincipal() && (!referencia.equals(telefones.get(key).getReferencia()))){
						
						listaValidacao.add("Já existe um telefone principal.");
						break;
					}
				}
			}
			
			List<TelefoneAssociacaoDTO> listaExibicao = this.obterTelefonesExibicao();
			
			for (TelefoneAssociacaoDTO dto : listaExibicao){
				
				if (referencia == null){
					
					if (dto.isPrincipal()){
						
						listaValidacao.add("Já existe um telefone principal.");
						break;
					}
				} else {
				
					if (dto.isPrincipal() && (!referencia.equals(dto.getReferencia()))){
						
						listaValidacao.add("Já existe um telefone principal.");
						break;
					}
				}
			}
		}
		
		if (!listaValidacao.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, listaValidacao));
		}
	}
}