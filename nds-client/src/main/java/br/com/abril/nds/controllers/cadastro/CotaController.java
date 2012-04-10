package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.CotaVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
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
	
	private static final String SUFIXO_PESSOA_FISICA = " (PF)";
	
	private static final String SUFIXO_PESSOA_JURIDICA = " (PJ)";

	@Autowired
	private CotaService cotaService;

	@Autowired
	private HttpSession session;
	
	public CotaController(Result result) {

		this.result = result;
	}

	@Path("/")
	public void index() { }

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
		
		Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);

		if (cota == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + numeroCota + "\" não encontrada!");
			
		} else {
			
			String nomeExibicao =
				this.obterNomeExibicaoCotaPeloTipoPessoa(cota.getPessoa());
			
			CotaVO cotaVO = new CotaVO(cota.getNumeroCota(), nomeExibicao);
			
			if (cota.getBox() != null) {
			
				cotaVO.setCodigoBox(cota.getBox().getCodigo());
			}
			
			this.result.use(Results.json()).from(cotaVO, "result").recursive().serialize();
		}		
	}

	@Post
	public void autoCompletarPorNome(String nomeCota) {
		
		nomeCota = this.removerSufixoTipoPessoa(nomeCota);
		
		List<Cota> listaCotas = this.cotaService.obterCotasPorNomePessoa(nomeCota);
		
		List<ItemAutoComplete> listaCotasAutoComplete = new ArrayList<ItemAutoComplete>();
		
		if (listaCotas != null && !listaCotas.isEmpty()) {
			
			for (Cota cota : listaCotas) {
				
				String nomeExibicao =
					this.obterNomeExibicaoCotaPeloTipoPessoa(cota.getPessoa());
					
				CotaVO cotaVO = new CotaVO(cota.getNumeroCota(), nomeExibicao);
	
				listaCotasAutoComplete.add(new ItemAutoComplete(nomeExibicao, null, cotaVO));
			}
		}
		
		this.result.use(Results.json()).from(listaCotasAutoComplete, "result").include("value", "chave").serialize();
	}
	
	@Post
	public void pesquisarPorNome(String nomeCota) {
		
		nomeCota = this.removerSufixoTipoPessoa(nomeCota);
		
		Cota cota = this.cotaService.obterPorNome(nomeCota);
		
		if (cota == null) {
		
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + nomeCota + "\" não encontrada!");
		}
		
		String nomeExibicao =
			this.obterNomeExibicaoCotaPeloTipoPessoa(cota.getPessoa());
				
		CotaVO cotaVO = new CotaVO(cota.getNumeroCota(), nomeExibicao);
			
		this.result.use(Results.json()).from(cotaVO, "result").serialize();
	}
	
	@Post
	public void cancelar(){
		this.session.removeAttribute(TelefoneController.LISTA_TELEFONES_SALVAR_SESSAO);
		this.session.removeAttribute(TelefoneController.LISTA_TELEFONES_REMOVER_SESSAO);
		
		this.result.use(Results.json()).from("", "result").serialize();
	}
	
	/*
	 * Obtém o nome de exibição da cota de acordo com o tipo de pessoa.
	 * 
	 * Se for Física retorna o nome e se for jurídica retorna a Razão Social.
	 * 
	 * @param pessoa - pessoa
	 * 
	 * @return Nome para exibição
	 */
	private String obterNomeExibicaoCotaPeloTipoPessoa(Pessoa pessoa) {

		String nomeExibicao = "";
		
		if (pessoa != null) {
			
			if (pessoa instanceof PessoaJuridica) {
				
				String razaoSocial = ((PessoaJuridica) pessoa).getRazaoSocial();

				nomeExibicao = razaoSocial != null ? razaoSocial + SUFIXO_PESSOA_JURIDICA : razaoSocial;

				
			} else if (pessoa instanceof PessoaFisica) {
				
				String nome = ((PessoaFisica) pessoa).getNome();

				nomeExibicao = nome != null ? nome + SUFIXO_PESSOA_FISICA : nome;
			}
		}
		
		return nomeExibicao;
	}
	
	/*
	 * Remove o sufixo do tipo de pessoa.
	 * 
	 * @param nomeCota - nome da cota
	 * 
	 * @return Nome da cota sem sufixo
	 */
	private String removerSufixoTipoPessoa(String nomeCota) {
		
		if (nomeCota != null && !nomeCota.trim().isEmpty()) {
			
			if (nomeCota.contains(SUFIXO_PESSOA_FISICA)) {
				
				return nomeCota.replace(SUFIXO_PESSOA_FISICA, "");
				
			} else if (nomeCota.contains(SUFIXO_PESSOA_JURIDICA)) {
				
				return nomeCota.replace(SUFIXO_PESSOA_JURIDICA, "");
			}
		}
		
		return nomeCota;
	}
}
