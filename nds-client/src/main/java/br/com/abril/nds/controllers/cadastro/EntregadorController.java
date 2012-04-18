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

import br.com.abril.nds.client.vo.EntregadorPessoaFisicaVO;
import br.com.abril.nds.client.vo.EntregadorPessoaJuridicaVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroEntregadorDTO;
import br.com.abril.nds.dto.filtro.FiltroEntregadorDTO.OrdenacaoColunaEntregador;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProcuracaoEntregador;
import br.com.abril.nds.model.cadastro.TelefoneEntregador;
import br.com.abril.nds.service.EntregadorService;
import br.com.abril.nds.service.PessoaFisicaService;
import br.com.abril.nds.service.PessoaJuridicaService;
import br.com.abril.nds.service.TelefoneService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Controller para a tela de cadastro de entregadores.
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path("/cadastro/entregador")
public class EntregadorController {

	@Autowired
	private Result result;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private EntregadorService entregadorService;

	@Autowired
	private PessoaFisicaService pessoaFisicaService;

	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;
	
	@Path("/")
	public void index() { }

	/**
	 * Método responsável por popular a grid de Entregadores.
	 * 
	 * @param filtroEntregador - Filtro utilizado na pesquisa.
	 *  
	 * @param page - Número da página atual da grid.
	 */
	public void pesquisarEntregadores(
			FiltroEntregadorDTO filtroEntregador, int page, int rp, 
			String sortname, String sortorder) {

		filtroEntregador = prepararFiltroEntregador(filtroEntregador, page, sortname, sortorder, rp);

		List<Entregador> listaEntregador = this.entregadorService.obterEntregadoresPorFiltro(filtroEntregador);
		
		if (listaEntregador == null || listaEntregador.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro foi encontrado.");
		}

		Long quantidadeRegistrosEntregadores = this.entregadorService.obterContagemEntregadoresPorFiltro(filtroEntregador);

		TableModel<CellModel> tableModelEntregador = obterTableModel(listaEntregador);

		tableModelEntregador.setPage(page);

		tableModelEntregador.setTotal(quantidadeRegistrosEntregadores.intValue());

		this.result.use(Results.json()).withoutRoot().from(tableModelEntregador).recursive().serialize();
	}

	/**
	 * Método que realiza o cadastro de um entregador com papel de Pessoa Física.
	 * 
	 * @param entregador - Entregador.
	 * 
	 * @param pessoaFisica - Pessoa Física.
	 * 
	 * @param procuracaoEntregador - Procuração relacionada ao entregador.
	 */
	public void cadastrarEntregadorPessoaFisica(
			Entregador entregador, PessoaFisica pessoaFisica, ProcuracaoEntregador procuracaoEntregador) {

		if (entregador.getId() == null) {

			entregador.setInicioAtividade(new Date());
		}

		pessoaFisica = this.pessoaFisicaService.salvarPessoaFisica(pessoaFisica);

		entregador.setPessoa(pessoaFisica);

		this.entregadorService.salvarEntregador(entregador);

//		procuracaoEntregador.setEn tregador(entregador);

		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, "Cadastro realizado com sucesso.");
		
		this.result.use(Results.json()).from(validacao, "result").recursive().serialize();
	}

	/**
	 * Método que realiza o cadastro de um entregador com papel de Pessoa Juridica.
	 * 
	 * @param entregador - Entregador.
	 * 
	 * @param pessoaJuridica - Pessoa Jurídica.
	 * 
	 * @param procuracaoEntregador - Procuração relacionada ao entregador.
	 */
	public void cadastrarEntregadorPessoaJuridica(
			Entregador entregador, PessoaJuridica pessoaJuridica, ProcuracaoEntregador procuracaoEntregador) {

		pessoaJuridica = validarPessoaJuridica(pessoaJuridica);

		processarEnderecosEntregador(entregador.getId());

		processarTelefonesCota(entregador.getId());
		
		Entregador entregadorEdicao = null;

		if (entregador.getId() != null) {
			
			entregadorEdicao = this.entregadorService.obterEntregadorPorId(entregador.getId());
		} 

		if (entregadorEdicao != null) {
		
			entregadorEdicao.setCodigo(entregador.getCodigo());
			entregadorEdicao.setComissionado(entregador.isComissionado());
			entregadorEdicao.setPercentualComissao(entregador.getPercentualComissao());
			entregadorEdicao.setPessoa(pessoaJuridica);
			entregadorEdicao.setProcuracao(entregador.isProcuracao());
			entregadorEdicao.setProcuracaoEntregador(procuracaoEntregador);
			
		} else {
			
			entregador.setInicioAtividade(new Date());
		}

		entregador.setPessoa(pessoaJuridica);

		this.entregadorService.salvarEntregador(entregador);

//		procuracaoEntregador.setEn tregador(entregador);

		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, "Cadastro realizado com sucesso.");
		
		this.result.use(Results.json()).from(validacao, "result").recursive().serialize();
	}

	/**
	 * Método que prepara um Entregador para ser editado.
	 * 
	 * @param idEntregador - Id do entregador a ser editado.
	 */
	public void editarEntregador(Long idEntregador) {

		Entregador entregador = this.entregadorService.obterEntregadorPorId(idEntregador);
		
		if (entregador == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Entregador não encontrado.");
		}
		
		List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = 
				this.entregadorService.obterEnderecosPorIdEntregador(idEntregador);

		this.session.setAttribute(
			Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR, listaEnderecoAssociacao
		);
		
		List<TelefoneAssociacaoDTO> listaTelefoneAssociacao = 
				this.entregadorService.buscarTelefonesEntregador(idEntregador);
		
		Map<Integer, TelefoneAssociacaoDTO> map = new LinkedHashMap<Integer, TelefoneAssociacaoDTO>();
		
		for (TelefoneAssociacaoDTO telefoneAssociacaoDTO : listaTelefoneAssociacao){
			map.put(telefoneAssociacaoDTO.getReferencia(), telefoneAssociacaoDTO);
		}
		
		this.session.setAttribute(TelefoneController.LISTA_TELEFONES_SALVAR_SESSAO, map);
		
		if (entregador.getPessoa() instanceof PessoaFisica) {

			EntregadorPessoaFisicaVO entregadorPessoaFisica = new EntregadorPessoaFisicaVO();
			
			entregadorPessoaFisica.setEntregador(entregador);
			
			entregadorPessoaFisica.setPessoaFisica((PessoaFisica) entregador.getPessoa());
			
			this.result.use(Results.json()).from(
					entregadorPessoaFisica, "result").include("entregador", "pessoaFisica").serialize();
		
		} else if (entregador.getPessoa() instanceof PessoaJuridica) {
			
			EntregadorPessoaJuridicaVO entregadorPessoaJuridica = new EntregadorPessoaJuridicaVO();
			
			entregadorPessoaJuridica.setEntregador(entregador);
			
			entregadorPessoaJuridica.setPessoaJuridica((PessoaJuridica) entregador.getPessoa());

			this.result.use(Results.json()).from(
					entregadorPessoaJuridica, "result").include("entregador", "pessoaJuridica").serialize();

		} else {

			this.result.nothing();
		}
	}
	
	/**
	 * Método responsável por remover um entregador a partir de seu ID.
	 * 
	 * @param idEntregador
	 */
	public void removerEntregador(Long idEntregador) {

		if (idEntregador == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id do entregador não pode ser nulo.");
		}
		
		this.entregadorService.removerEntregadorPorId(idEntregador);

		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, "Exclusão realizada com sucesso.");
		
		this.result.use(Results.json()).from(validacao, "result").recursive().serialize();
	}

	/*
	 * Método responsável por processar os endereços do entregador.
	 */
	@SuppressWarnings("unchecked")
	private void processarEnderecosEntregador(Long idEntregador) {

		List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar = 
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(
						Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR);
		
		List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoRemover = 
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(
						Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_REMOVER);

		this.entregadorService.processarEnderecos(idEntregador, 
												  listaEnderecoAssociacaoSalvar, 
												  listaEnderecoAssociacaoRemover);
	}
	
	/*
	 * Método responsável por processar os telefones do entregador.
	 */
	private void processarTelefonesCota(Long idEntregador){
		
		Map<Integer, TelefoneAssociacaoDTO> map = this.obterTelefonesSalvarSessao();
		
		List<TelefoneEntregador> lista = new ArrayList<TelefoneEntregador>();
		
		for (Integer key : map.keySet()){
			
			TelefoneAssociacaoDTO telefoneAssociacaoDTO = map.get(key);
			
			if (telefoneAssociacaoDTO.getTipoTelefone() != null){
				
				TelefoneEntregador telefoneEntregador = new TelefoneEntregador();
				telefoneEntregador.setPrincipal(telefoneAssociacaoDTO.isPrincipal());
				telefoneEntregador.setTelefone(telefoneAssociacaoDTO.getTelefone());
				telefoneEntregador.setTipoTelefone(telefoneAssociacaoDTO.getTipoTelefone());
				
				lista.add(telefoneEntregador);
			}
		}
		
		Set<Long> telefonesRemover = this.obterTelefonesRemoverSessao();
		this.entregadorService.processarTelefones(idEntregador, lista, telefonesRemover);
		
		this.session.removeAttribute(TelefoneController.LISTA_TELEFONES_SALVAR_SESSAO);
		this.session.removeAttribute(TelefoneController.LISTA_TELEFONES_REMOVER_SESSAO);
	}

	/*
	 * Método que obtém os telefones a serem salvos, que estão na sessão.
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer, TelefoneAssociacaoDTO> obterTelefonesSalvarSessao(){
		
		Map<Integer, TelefoneAssociacaoDTO> telefonesSessao = (Map<Integer, TelefoneAssociacaoDTO>) 
				this.session.getAttribute(TelefoneController.LISTA_TELEFONES_SALVAR_SESSAO);
		
		if (telefonesSessao == null){

			telefonesSessao = new LinkedHashMap<Integer, TelefoneAssociacaoDTO>();
		}
		
		return telefonesSessao;
	}
	
	/*
	 * Método que obtém os telefones a serem removidos, que estão na sessão.
	 */
	@SuppressWarnings("unchecked")
	private Set<Long> obterTelefonesRemoverSessao(){
		Set<Long> telefonesSessao = (Set<Long>) 
				this.session.getAttribute(TelefoneController.LISTA_TELEFONES_REMOVER_SESSAO);

		if (telefonesSessao == null){
			telefonesSessao = new HashSet<Long>();
		}

		return telefonesSessao;
	}

	/*
	 * Método que retorna uma pessoa Juridica com suas devidas validações.
	 */
	private PessoaJuridica validarPessoaJuridica(PessoaJuridica pessoaJuridica) {
		
		if (pessoaJuridica == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Pessoa Juridica não pode estar nula.");
		}
		
		if (pessoaJuridica.getCnpj() == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "O CNPJ da Pessoa Juridica não pode ser nulo.");
		}
		
		PessoaJuridica pessoaJuridicaValidada = 
				this.pessoaJuridicaService.buscarPorCnpj(pessoaJuridica.getCnpj());
		
		if (pessoaJuridicaValidada == null) {
			
			pessoaJuridicaValidada = this.pessoaJuridicaService.salvarPessoaJuridica(pessoaJuridica);
		}

		return pessoaJuridicaValidada;
	}
	
	/*
	 * Método que cria um table model baseado no retorno da pesquisa de entregadores.
	 */
	private TableModel<CellModel> obterTableModel(List<Entregador> listaEntregador) {

		TableModel<CellModel> tableModel = new TableModel<CellModel>();

		List<CellModel> listaCellModel = new ArrayList<CellModel>();

		for (Entregador entregador : listaEntregador) {

			String nome = entregador.getPessoa() instanceof PessoaJuridica ? 
					((PessoaJuridica) entregador.getPessoa()).getRazaoSocial() :
						((PessoaFisica) entregador.getPessoa()).getNome();

			String documento = entregador.getPessoa() instanceof PessoaJuridica ? 
					((PessoaJuridica) entregador.getPessoa()).getCnpj() :
						((PessoaFisica) entregador.getPessoa()).getCpf(); 

			String apelido = entregador.getPessoa() instanceof PessoaJuridica ? 
					((PessoaJuridica) entregador.getPessoa()).getNomeFantasia() :
						((PessoaFisica) entregador.getPessoa()).getApelido();

			String email = entregador.getPessoa().getEmail();

			String telefone = "";

			for (TelefoneEntregador telefoneEntregador : entregador.getTelefones()) {

				if (telefoneEntregador.isPrincipal()) {

					telefone = telefoneEntregador.getTelefone().getNumero();
				}
			}

			CellModel cellModel = new CellModel(
					entregador.getId().intValue(),
					formatField(entregador.getCodigo()),
					nome,
					documento,
					apelido,
					telefone,
					email);

			listaCellModel.add(cellModel);
		}

		tableModel.setRows(listaCellModel);

		return tableModel;
	}

	/*
	 * Método que realiza a formatação dos dados a serem apresentados na grid. 
	 */
	private static String formatField(Object obj) {
		
		return obj == null ? "" : String.valueOf(obj);
	}
	
	/*
	 * Método que prepara o filtro para utilização na pesquisa.
	 */
	private FiltroEntregadorDTO prepararFiltroEntregador(
			FiltroEntregadorDTO filtroEntregador, int page, String sortname, String sortorder, int rp) {
		
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
		
		filtroEntregador.setPaginacao(paginacao);
		
		filtroEntregador.setOrdenacaoColunaEntregador(
				Util.getEnumByStringValue(OrdenacaoColunaEntregador.values(), sortname));

		return filtroEntregador;
	}
	
}
