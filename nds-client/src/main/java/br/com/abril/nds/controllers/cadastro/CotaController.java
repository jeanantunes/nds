package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.client.vo.CotaVO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/cota")
public class CotaController {
	
	private Result result;

	@Autowired
	private CotaService cotaService;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private FinanceiroController financeiroController;
	
	@Autowired
	private PdvController pdvController;

	public CotaController(Result result) {
		this.result = result;
	}

	
	@Path("/")
	public void index() {
		
		//Pré carregamento da aba "financeiro" 
		this.financeiroController.preCarregamento();
		this.pdvController.preCarregamento();
		
	}
	
	

	@Post
	public void novaCota() { 

		this.session.removeAttribute(Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR);

		this.result.nothing();
	}
	


	@Post
	public void editarCota(Long idCota) { 

		if (idCota == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Ocorreu um erro: Cota inexistente.");
		}

		List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = 
				this.cotaService.obterEnderecosPorIdCota(idCota);
		
		this.session.setAttribute(
			Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR, listaEnderecoAssociacao
		);
		
		List<TelefoneAssociacaoDTO> listaTelefoneAssociacao = 
				this.cotaService.buscarTelefonesCota(idCota, null);
		
		Map<Integer, TelefoneAssociacaoDTO> map = new LinkedHashMap<Integer, TelefoneAssociacaoDTO>();
		
		for (TelefoneAssociacaoDTO telefoneAssociacaoDTO : listaTelefoneAssociacao){
			map.put(telefoneAssociacaoDTO.getReferencia(), telefoneAssociacaoDTO);
		}
		
		this.session.setAttribute(TelefoneController.LISTA_TELEFONES_SALVAR_SESSAO, map);
		
		this.result.nothing();
	}
	
	
	
	@Post
	public void salvarCota(Long idCota) {

		processarEnderecosCota(idCota);
		
		processarTelefonesCota(idCota);
		
		//Persiste os dados da aba "financeiro"
		this.financeiroController.postarFinanceiro(idCota);
		
		this.result.nothing();
	}
	
	
	
	@SuppressWarnings("unchecked")
	private void processarEnderecosCota(Long idCota) {

		List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar = 
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(
						Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR);
		
		List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoRemover = 
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(
						Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_REMOVER);
		
		this.cotaService.processarEnderecos(idCota, listaEnderecoAssociacaoSalvar, listaEnderecoAssociacaoRemover);
	}
	
	
	
	private void processarTelefonesCota(Long idCota){
		Map<Integer, TelefoneAssociacaoDTO> map = this.obterTelefonesSalvarSessao();
		
		List<TelefoneAssociacaoDTO> lista = new ArrayList<TelefoneAssociacaoDTO>();
		for (Integer key : map.keySet()){
			TelefoneAssociacaoDTO telefoneAssociacaoDTO = map.get(key);
			lista.add(telefoneAssociacaoDTO);
		}
		
		Set<Long> telefonesRemover = this.obterTelefonesRemoverSessao();
		this.cotaService.processarTelefones(idCota, lista, telefonesRemover);
		
		this.session.removeAttribute(TelefoneController.LISTA_TELEFONES_SALVAR_SESSAO);
		this.session.removeAttribute(TelefoneController.LISTA_TELEFONES_REMOVER_SESSAO);
	}
	
	
	
	@SuppressWarnings("unchecked")
	private Map<Integer, TelefoneAssociacaoDTO> obterTelefonesSalvarSessao(){
		Map<Integer, TelefoneAssociacaoDTO> telefonesSessao = (Map<Integer, TelefoneAssociacaoDTO>) 
				this.session.getAttribute(TelefoneController.LISTA_TELEFONES_SALVAR_SESSAO);
		
		if (telefonesSessao == null){
			telefonesSessao = new LinkedHashMap<Integer, TelefoneAssociacaoDTO>();
		}
		
		return telefonesSessao;
	}
	
	
	
	@SuppressWarnings("unchecked")
	private Set<Long> obterTelefonesRemoverSessao(){
		Set<Long> telefonesSessao = (Set<Long>) 
				this.session.getAttribute(TelefoneController.LISTA_TELEFONES_REMOVER_SESSAO);
		
		if (telefonesSessao == null){
			telefonesSessao = new HashSet<Long>();
		}
		
		return telefonesSessao;
	}

	
	
	@Post
	public void pesquisarPorNumero(Integer numeroCota) {
		
		if(numeroCota == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Número da cota inválido!");
		}
		
		Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);

		if (cota == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + numeroCota + "\" não encontrada!");
			
		} else {
			
			String nomeExibicao = PessoaUtil.obterNomeExibicaoPeloTipo(cota.getPessoa());
			
			CotaVO cotaVO = new CotaVO(cota.getNumeroCota(), nomeExibicao);
			
			if (cota.getBox() != null) {
			
				cotaVO.setCodigoBox(cota.getBox().getCodigo());
			}
			
			this.result.use(Results.json()).from(cotaVO, "result").recursive().serialize();
		}		
	}

	
	
	@Post
	public void autoCompletarPorNome(String nomeCota) {
		
		nomeCota = PessoaUtil.removerSufixoDeTipo(nomeCota);
		
		List<Cota> listaCotas = this.cotaService.obterCotasPorNomePessoa(nomeCota);
		
		List<ItemAutoComplete> listaCotasAutoComplete = new ArrayList<ItemAutoComplete>();
		
		if (listaCotas != null && !listaCotas.isEmpty()) {
			
			for (Cota cota : listaCotas) {
				
				String nomeExibicao = PessoaUtil.obterNomeExibicaoPeloTipo(cota.getPessoa());
					
				CotaVO cotaVO = new CotaVO(cota.getNumeroCota(), nomeExibicao);
	
				listaCotasAutoComplete.add(new ItemAutoComplete(nomeExibicao, null, cotaVO));
			}
		}
		
		this.result.use(Results.json()).from(listaCotasAutoComplete, "result").include("value", "chave").serialize();
	}
	
	
	
	@Post
	public void pesquisarPorNome(String nomeCota) {
		
		nomeCota = PessoaUtil.removerSufixoDeTipo(nomeCota);
		
		Cota cota = this.cotaService.obterPorNome(nomeCota);
		
		if (cota == null) {
		
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + nomeCota + "\" não encontrada!");
		}
		
		String nomeExibicao = PessoaUtil.obterNomeExibicaoPeloTipo(cota.getPessoa());
				
		CotaVO cotaVO = new CotaVO(cota.getNumeroCota(), nomeExibicao);
			
		this.result.use(Results.json()).from(cotaVO, "result").serialize();
	}
	
	
	
	@Post
	public void cancelar(){
		this.session.removeAttribute(TelefoneController.LISTA_TELEFONES_SALVAR_SESSAO);
		this.session.removeAttribute(TelefoneController.LISTA_TELEFONES_REMOVER_SESSAO);
		
		this.result.use(Results.json()).from("", "result").serialize();
	}
	
	
	
	
}
