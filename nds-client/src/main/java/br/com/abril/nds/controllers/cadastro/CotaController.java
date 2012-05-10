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

import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.client.vo.CotaVO;
import br.com.abril.nds.client.vo.DadosCotaVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/cota")
public class CotaController {
	
	public static final String LISTA_TELEFONES_SALVAR_SESSAO = "listaTelefonesSalvarSessaoCota";
	
	public static final String LISTA_TELEFONES_REMOVER_SESSAO = "listaTelefonesRemoverSessaoCota";
	
	public static final String LISTA_TELEFONES_EXIBICAO = "listaTelefonesExibicaoCota";

	public static final String LISTA_ENDERECOS_SALVAR_SESSAO = "listaEnderecoSalvarSessaoCota";

	public static final String LISTA_ENDERECOS_REMOVER_SESSAO = "listaEnderecoRemoverSessaoCota";

	public static final String LISTA_ENDERECOS_EXIBICAO = "listaEnderecoExibicaoCota";
	
	@Autowired
	private Result result;

	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private FornecedorService fornecedorService;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private FinanceiroController financeiroController;
	
	@Autowired
	private PdvController pdvController;

	private static final String FILTRO_SESSION_ATTRIBUTE="filtroCadastroCota";

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

		this.session.removeAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);

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
			LISTA_ENDERECOS_SALVAR_SESSAO, listaEnderecoAssociacao
		);
		
		List<TelefoneAssociacaoDTO> listaTelefoneAssociacao = 
				this.cotaService.buscarTelefonesCota(idCota, null);
		
		Map<Integer, TelefoneAssociacaoDTO> map = new LinkedHashMap<Integer, TelefoneAssociacaoDTO>();
		
		for (TelefoneAssociacaoDTO telefoneAssociacaoDTO : listaTelefoneAssociacao){
			map.put(telefoneAssociacaoDTO.getReferencia(), telefoneAssociacaoDTO);
		}
		
		this.session.setAttribute(LISTA_TELEFONES_SALVAR_SESSAO, map);
		
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
						LISTA_ENDERECOS_SALVAR_SESSAO);
		
		List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoRemover = 
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(
						LISTA_ENDERECOS_REMOVER_SESSAO);
		
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
		
		this.session.removeAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		this.session.removeAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
	}
	
	@SuppressWarnings("unchecked")
	private Map<Integer, TelefoneAssociacaoDTO> obterTelefonesSalvarSessao(){
		Map<Integer, TelefoneAssociacaoDTO> telefonesSessao = (Map<Integer, TelefoneAssociacaoDTO>) 
				this.session.getAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		
		if (telefonesSessao == null){
			telefonesSessao = new LinkedHashMap<Integer, TelefoneAssociacaoDTO>();
		}
		
		return telefonesSessao;
	}

	
	@SuppressWarnings("unchecked")
	private Set<Long> obterTelefonesRemoverSessao(){
		Set<Long> telefonesSessao = (Set<Long>) 
				this.session.getAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
		
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
		this.session.removeAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		this.session.removeAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
		
		this.result.use(Results.json()).from("", "result").serialize();
	}
	
	@Post
	@Path("/incluirNovoCNPJ")
	public void prepararDadosInclusaoCotaCNPJ(){
		
		DadosCotaVO dadosCotaVO = new DadosCotaVO();
		
		//TODO obter sugestaao numero de cota
		dadosCotaVO.setNumeroSugestaoCota(1234);
		
		dadosCotaVO.setDataInicioAtividade(DateUtil.formatarDataPTBR(new Date()));
		dadosCotaVO.setStatus(SituacaoCadastro.PENDENTE.toString());
		
		//TODO obter lista de Classificação
		
		List<ItemDTO<String, String>> listaClassificacao = new ArrayList<ItemDTO<String,String>>();
		listaClassificacao.add(new ItemDTO<String, String>("1", "AA - Faturamento - Mais de 1 PDV"));
		listaClassificacao.add(new ItemDTO<String, String>("2", "A - Faturamento Acima de R$ 3.500,00"));
		listaClassificacao.add(new ItemDTO<String, String>("3", "B - Faturamento entre R$ 1.500,00 - R$ 3.499"));
		listaClassificacao.add(new ItemDTO<String, String>("4", "C - Faturamento Abaixo R$ 1.499,00"));
		
		dadosCotaVO.setListaClassificacao(listaClassificacao);
		
		result.use(Results.json()).from(dadosCotaVO, "result").recursive().serialize();
	}
	
	@Post
	@Path("/salvarCotaCNPJ")
	public void salvarCotaPessoaJuridica(CotaDTO cotaDTO){
		
		//TODO salvar as informações de cota
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
	}

	@Post
	@Path("/editar")
	public void editar(Integer numeroCota){
		
		//TODO implementar a parte de edicao dos dados
		
		Boolean pessoaFisica = Boolean.TRUE;
		
		result.use(Results.json()).from(pessoaFisica.toString(),"result").serialize();
	}
	
	
	@Post
	@Path("/excluir")
	public void excluir(Integer numeroCota){
		
		//TODO chamar metodo de exclusão da cota 
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	@Post
	@Path("/salvarFornecedores")
	public void salvarFornecedores(List<Long> fornecedores, Long idCota){
		
		if(fornecedores == null){
			
			List<Fornecedor> list = fornecedorService.obterFornecedoresCota(idCota);
	
			if(list!= null && !list.isEmpty()){
				
				fornecedorService.salvarFornecedorCota(fornecedores, idCota);
				
				result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
						Constantes.PARAM_MSGS).recursive().serialize();
			}
			else{
		
				result.use(Results.json()).from("","result").serialize();
			}	
		}else {
			
			fornecedorService.salvarFornecedorCota(fornecedores, idCota);
			
			result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
					Constantes.PARAM_MSGS).recursive().serialize();
		}
	}
	
	@Post
	@Path("/salvarDescontos")
	public void salvarDescontos(List<Long> descontos){
		
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	@Post
	@Path("/obterFornecedores")
	public void obterFornecedores(Long idCota){
		
		List<Fornecedor> fornecedores =   fornecedorService.obterFornecedores(idCota);
		
		result.use(Results.json()).from(this.getFornecedores(fornecedores),"result").recursive().serialize();
	}
	
	@Post
	@Path("/obterFornecedoresSelecionados")
	public void obterFornecedoresSelecionados(Long idCota){
		
		List<Fornecedor> fornecedores =   fornecedorService.obterFornecedoresCota(idCota);
		
		result.use(Results.json()).from(this.getFornecedores(fornecedores),"result").recursive().serialize();
	
	}
	
	private List<ItemDTO<Long, String>> getFornecedores(List<Fornecedor> fornecedores){
		
		List<ItemDTO<Long, String>> itensFornecedor = new ArrayList<ItemDTO<Long,String>>();
		
		for(Fornecedor fornecedor : fornecedores){
			
			itensFornecedor.add(new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial()));
		}
		
		return itensFornecedor;
	}
	
	@Post
	@Path("/obterDescontos")
	public void obterDescontos(){
		
		//TODO retorna todos os descontos cadastrados
		
		result.use(Results.json()).from(null, "result").recursive().serialize();
	}
	

	@Post
	@Path("/obterDescontosSelecionados")
	public void obterDescontosSelecionados(Long idCota){
		
		//TODO implementar o retorno de desconto da cota informada
		
		result.use(Results.json()).from(null, "result").recursive().serialize();
	}
	
	@Post
	@Path("/pesquisarCotas")
	public void pesquisarCotas(Integer numCota,String nomeCota,String numeroCpfCnpj, String sortorder, 
			 				   String sortname, int page, int rp){
		
		validarParametrosPesquisa(numCota,nomeCota,numeroCpfCnpj);
		
		nomeCota = PessoaUtil.removerSufixoDeTipo(nomeCota);
		
		FiltroCotaDTO filtro = new FiltroCotaDTO( numCota,nomeCota,numeroCpfCnpj );
		
		configurarPaginacaoPesquisa(filtro, sortorder, sortname, page, rp);
		
		tratarFiltro(filtro);
		
		efetuarConsulta(filtro);
	}
	
	/**
	 * Efetua a consulta e monta a estrutura do grid de dividas geradas.
	 * @param filtro
	 */
	private void efetuarConsulta(FiltroCotaDTO filtro) {
		
		List<CotaDTO> listaCotas = cotaService.obterCotas(filtro);
		
		if (listaCotas == null || listaCotas.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		Long totalRegistros = cotaService.obterQuantidadeCotasPesquisadas(filtro);
		
		List<CotaVO> listaCotasVO = getListaCotaVO(listaCotas);

		TableModel<CellModelKeyValue<CotaVO>> tableModel = new TableModel<CellModelKeyValue<CotaVO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotasVO));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal( (totalRegistros == null)?0:totalRegistros.intValue());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private List<CotaVO> getListaCotaVO(List<CotaDTO> listaCotas) {
		
		List<CotaVO> listaRetorno  =  new ArrayList<CotaVO>();
		CotaVO cotaVO = null;
		
		for(CotaDTO dto : listaCotas){
			
			cotaVO = new CotaVO();
			cotaVO.setNumero(dto.getNumeroCota());
			cotaVO.setNome(dto.getNomePessoa());
			cotaVO.setContato( tratarValor( dto.getContato() ));
			cotaVO.setEmail(tratarValor( dto.getEmail()));
			cotaVO.setNumeroCpfCnpj( ( dto.getNumeroCpfCnpj()));
			cotaVO.setStatus( tratarValor(dto.getStatus()));
			cotaVO.setTelefone( tratarValor( dto.getTelefone()));
			
			listaRetorno.add(cotaVO);
		}
		
		return listaRetorno;
	}
	
	private String tratarValor(Object valor){
		
		return (valor == null)?"":valor.toString();
	}

	private void validarParametrosPesquisa(Integer numCota,String nomeCota, String numeroCpfCnpj) {
		
		if(numCota == null 
				&& (nomeCota == null || nomeCota.isEmpty())
				&& (numeroCpfCnpj == null || numeroCpfCnpj.isEmpty())){
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Pelomenos um dos filtros deve ser informado!");
		}
		
	}
	/**
	 * Configura paginação do grid de pesquisa.
	 * @param filtro
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	private void configurarPaginacaoPesquisa(FiltroCotaDTO filtro,String sortorder,String sortname,int page, int rp) {

		if (filtro != null) {
		
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			
			filtro.setPaginacao(paginacao);
			
			filtro.setOrdemColuna(Util.getEnumByStringValue(FiltroCotaDTO.OrdemColuna.values(),sortname));
		}
	}
	
	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 * 
	 * @param filtroResumoExpedicao
	 */
	private void tratarFiltro(FiltroCotaDTO filtro) {

		FiltroCotaDTO filtroSession = (FiltroCotaDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro)) {

			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}
}
