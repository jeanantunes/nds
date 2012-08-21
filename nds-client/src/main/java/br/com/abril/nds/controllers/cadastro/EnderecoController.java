package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.endereco.vo.EnderecoVO;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.dne.Bairro;
import br.com.abril.nds.model.dne.Localidade;
import br.com.abril.nds.model.dne.Logradouro;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Controller responsável pela lógica de visualização 
 * referentes a funcionalidade de cadastro de endereços. 
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path("/cadastro/endereco")
public class EnderecoController {

	/**
	 * Constante que representa o nome do atributo com a lista de endereços 
	 * armazenado na sessão para serem persistidos na base. 
	 */
	public static  String ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR = "";

	/**
	 * Constante que representa o nome do atributo com a lista de endereços 
	 * armazenado na sessão para serem persistidos na base. 
	 */
	public static  String ATRIBUTO_SESSAO_LISTA_ENDERECOS_REMOVER = "";
	
	public static  String ATRIBUTO_SESSAO_LISTA_ENDERECOS_EXIBIR = "";
	
	@Autowired
	private Result result;

	@Autowired
	private HttpSession session;

	@Autowired
	private EnderecoService enderecoService;
	
	public enum Tela{
		
		ENDERECO_FIADOR,ENDERECO_COTA,ENDERECO_ENTREGADOR,ENDERECO_PDV,ENDERECO_TRANSPORTADOR,ENDERECO_FORNECEDOR;
		
		public void setarParametros(){
			
			switch (this){
				case ENDERECO_FIADOR:
					EnderecoController.setarParametros(
							FiadorController.LISTA_ENDERECOS_SALVAR_SESSAO, 
							FiadorController.LISTA_ENDERECOS_REMOVER_SESSAO, 
							FiadorController.LISTA_ENDERECOS_EXIBICAO);
				break;
				case ENDERECO_COTA:
					EnderecoController.setarParametros(
							CotaController.LISTA_ENDERECOS_SALVAR_SESSAO, 
							CotaController.LISTA_ENDERECOS_REMOVER_SESSAO, 
							CotaController.LISTA_ENDERECOS_EXIBICAO);
				break;
				case ENDERECO_ENTREGADOR:
					EnderecoController.setarParametros(
							EntregadorController.LISTA_ENDERECOS_SALVAR_SESSAO, 
							EntregadorController.LISTA_ENDERECOS_REMOVER_SESSAO, 
							EntregadorController.LISTA_ENDERECOS_EXIBICAO);
				break;

				case ENDERECO_PDV:
					EnderecoController.setarParametros(
							PdvController.LISTA_ENDERECOS_SALVAR_SESSAO, 
							PdvController.LISTA_ENDERECOS_REMOVER_SESSAO, 
							PdvController.LISTA_ENDERECOS_EXIBICAO);
				break;

				case ENDERECO_TRANSPORTADOR:
					EnderecoController.setarParametros(
							TransportadorController.LISTA_ENDERECOS_SALVAR_SESSAO, 
							TransportadorController.LISTA_ENDERECOS_REMOVER_SESSAO, 
							TransportadorController.LISTA_ENDERECOS_EXIBICAO);

				break;
				
				case ENDERECO_FORNECEDOR:
					EnderecoController.setarParametros(
							FornecedorController.LISTA_ENDERECOS_SALVAR_SESSAO, 
							FornecedorController.LISTA_ENDERECOS_REMOVER_SESSAO, 
							FornecedorController.LISTA_ENDERECOS_EXIBICAO);

				break;
			}
		}
	}
	
	@Path("/")
	public void index() {
		
		this.session.removeAttribute(ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR);

		this.session.removeAttribute(ATRIBUTO_SESSAO_LISTA_ENDERECOS_REMOVER);
		
		this.session.removeAttribute(ATRIBUTO_SESSAO_LISTA_ENDERECOS_EXIBIR);
	}
	
	public static void setarParametros(String listaSalvar, String listaRemover, String listaExibir){
		
		ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR = listaSalvar;
		ATRIBUTO_SESSAO_LISTA_ENDERECOS_REMOVER = listaRemover;
		ATRIBUTO_SESSAO_LISTA_ENDERECOS_EXIBIR = listaExibir;
	}

	/**
	 * Método que realiza a pesquisa dos endereços cadastrados para uma determinada pessoa.
	 */
	@Post
	public void pesquisarEnderecos( Tela tela, String sortname, String sortorder) {
		
		tela.setarParametros();
		
		List<EnderecoAssociacaoDTO> listaEndereco = new ArrayList<EnderecoAssociacaoDTO>();
		
		List<EnderecoAssociacaoDTO> listaEnderecoSalvar = this.obterEnderecosSessaoSalvar();
		
		List<EnderecoAssociacaoDTO> enderecosExibir = this.obterEnderecosSessaoExibir();
		
		listaEndereco.addAll(listaEnderecoSalvar);
		
		listaEndereco.addAll(enderecosExibir);
		
		List<EnderecoAssociacaoDTO> enderecosRemovidos = this.obterEnderecosSessaoRemover();
		
		if (enderecosRemovidos != null){
			
			listaEndereco.removeAll(enderecosRemovidos);
		}
		
		TableModel<CellModel> tableModelEndereco = new TableModel<CellModel>();

		if (sortname != null) {

			sortorder = sortorder == null ? "asc" : sortorder;

			Ordenacao ordenacao = Util.getEnumByStringValue(Ordenacao.values(), sortorder);

			PaginacaoUtil.ordenarEmMemoria(listaEndereco, ordenacao, sortname);
		}

		tableModelEndereco = getTableModelListaEndereco(listaEndereco);

		this.result.use(Results.json()).from(tableModelEndereco, "result").recursive().serialize();
	}

	/**
	 * Método responsável por incluir um novo endereço para a pessoa em questão.
	 * 
	 * @param enderecoAssociacao
	 */
	public void incluirNovoEndereco(Tela tela,EnderecoAssociacaoDTO enderecoAssociacao) {
		
		tela.setarParametros();
		
		validarDadosEndereco(enderecoAssociacao);

		validarExistenciaEnderecoPrincipal(enderecoAssociacao);
		
		if (enderecoAssociacao.getEndereco() != null && enderecoAssociacao.getEndereco().getCep() != null) {

			enderecoAssociacao.getEndereco().setCep(retirarFormatacaoCep(enderecoAssociacao.getEndereco().getCep()));
		}
		
		List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = this.obterEnderecosSessaoSalvar();
		
		if (enderecoAssociacao.getId() != null){
			
			for (int index = 0 ; index < listaEnderecoAssociacao.size() ; index++){
				
				if (listaEnderecoAssociacao.get(index).getId().equals(enderecoAssociacao.getId())){
					
					listaEnderecoAssociacao.set(index, enderecoAssociacao);
					break;
				}
			}
			
			List<EnderecoAssociacaoDTO> listaExibir = this.obterEnderecosSessaoExibir();
			
			for (int index = 0 ; index < listaExibir.size() ; index++){
				
				if (listaExibir.get(index).getId().equals(enderecoAssociacao.getId())){
					
					EnderecoAssociacaoDTO removido = listaExibir.remove(index);
					
					this.session.setAttribute(ATRIBUTO_SESSAO_LISTA_ENDERECOS_EXIBIR, listaExibir);
					
					enderecoAssociacao.getEndereco().setId(removido.getEndereco().getId());
					
					listaEnderecoAssociacao.add(enderecoAssociacao);
					break;
				}
			}
		} else {

			if (tela.equals(Tela.ENDERECO_PDV)) {
			
				validarDuplicidadeEnderecoPDV();
			}

			listaEnderecoAssociacao.add(enderecoAssociacao);
		}

		this.session.setAttribute(ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR, listaEnderecoAssociacao);
		
		this.pesquisarEnderecos(tela,null, null);
	}
	
	/**
	 * Método responsável pela obtenção dos dados que irão preencher o combo de UF's.
	 * 
	 * @param tela
	 */
	public void obterDadosComboUF() {
		
		List<String> ufs = this.enderecoService.obterUnidadeFederacaoBrasil();
		
		this.result.use(Results.json()).from(ufs, "result").serialize();
	}

	private void validarDuplicidadeEnderecoPDV() {
		
		List<EnderecoAssociacaoDTO> listaSalvar = this.obterEnderecosSessaoSalvar();
		
		for (int index = 0 ; index < listaSalvar.size() ; index++){
			
			if (listaSalvar.get(index).getTipoEndereco() != null){
				throw new ValidacaoException(TipoMensagem.WARNING,"Já existe associação de endereço para este PDV.");
			}
		}
		
		List<EnderecoAssociacaoDTO> listaExibir = this.obterEnderecosSessaoExibir();
			
		for (int index = 0 ; index < listaExibir.size() ; index++){
			
			if (listaExibir.get(index).getTipoEndereco() != null){
				
				List<EnderecoAssociacaoDTO> listaExclusao = this.obterEnderecosSessaoRemover();
				
				for (int i = 0 ; i < listaExclusao.size() ; i++){
					
					boolean encontrado = false;
					
					if (listaExclusao.get(index).getTipoEndereco() != null){
						encontrado = true;						
					}
					
					if(encontrado == false) 
						throw new ValidacaoException(TipoMensagem.WARNING,"Já existe associação de endereço para este PDV.");
				}	
			}
		}			
	}

	/**
	 * Método responsável pela obtenção dos dados que irão preencher o combo de Cidades.
	 * 
	 * @param tela
	 * 
	 * @param siglaUF
	 */
	public void autoCompletarLocalidadePorNome(String nomeLocalidade, String siglaUF) {

		if (siglaUF == null || siglaUF.isEmpty()) {
			
			this.result.use(Results.json()).from("", "result").serialize();
			
			return;
		}
		
		List<Localidade> localidades = this.enderecoService.obterLocalidadesPorUFNome(nomeLocalidade, siglaUF);
		
		List<ItemAutoComplete> listaAutoComplete = new ArrayList<ItemAutoComplete>();
		
		if (localidades != null && !localidades.isEmpty()) {
			
			for (Localidade localidade : localidades) {
				
				String nomeExibicao = localidade.getNome();
				
				Long chave = localidade.getCodigoMunicipioIBGE();
				
				listaAutoComplete.add(new ItemAutoComplete(nomeExibicao, null, chave));
			}
		}
		
		this.result.use(Results.json()).from(listaAutoComplete, "result").include("value", "chave").serialize();
	}
	
	public void autoCompletarBairroPorNome(Long codigoIBGE, String nomeBairro) {

		if (codigoIBGE == null) {
			
			this.result.use(Results.json()).from("", "result").serialize();
			
			return;
		}
		
		List<Bairro> bairros = this.enderecoService.obterBairrosPorCodigoIBGENome(nomeBairro, codigoIBGE);
		
		List<ItemAutoComplete> listaAutoComplete = new ArrayList<ItemAutoComplete>();
		
		if (bairros != null && !bairros.isEmpty()) {
			
			for (Bairro bairro : bairros) {
				
				String nomeExibicao = bairro.getNome();
				
				Long chave = bairro.getId();
				
				listaAutoComplete.add(new ItemAutoComplete(nomeExibicao, null, chave));
			}
		}

		this.result.use(Results.json()).from(listaAutoComplete, "result").include("value", "chave").serialize();
	}

	public void autoCompletarLogradourosPorNome(Long codigoBairro, String nomeLogradouro) {

		if (codigoBairro == null) {
			
			this.result.use(Results.json()).from("", "result").serialize();
			
			return;
		}
		
		List<Logradouro> logradouros = this.enderecoService.obterLogradourosPorCodigoBairroNome(codigoBairro, nomeLogradouro);
		
		List<ItemAutoComplete> listaAutoComplete = new ArrayList<ItemAutoComplete>();
		
		if (logradouros != null && !logradouros.isEmpty()) {
			
			for (Logradouro logradouro : logradouros) {
				
				String nomeExibicao = logradouro.getNome();
				
				Long chave = logradouro.getId();
				
				listaAutoComplete.add(new ItemAutoComplete(nomeExibicao, null, chave));
			}
		}

		this.result.use(Results.json()).from(listaAutoComplete, "result").include("value", "chave").serialize();
	}

	/**
	 * Método que irá remover um endereço a partir de seu ID. 
	 * 
	 * @param idEnderecoAssociacao
	 */
	@Post
	public void removerEndereco(Tela tela,Long idEnderecoAssociacao) {
		
		tela.setarParametros();
		
		EnderecoAssociacaoDTO enderecoRemover = null;
		
		List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = this.obterEnderecosSessaoSalvar();
			
		for (int index = 0 ; index < listaEnderecoAssociacao.size() ; index++){
			
			if (listaEnderecoAssociacao.get(index).getId().equals(idEnderecoAssociacao)){
				
				enderecoRemover = listaEnderecoAssociacao.remove(index);
				break;
			}
		}
		
		List<EnderecoAssociacaoDTO> listaExibir = this.obterEnderecosSessaoExibir();
		
		for (int index = 0 ; index < listaExibir.size() ; index++){
			
			if (listaExibir.get(index).getId().equals(idEnderecoAssociacao)){
				
				if (enderecoRemover == null){
					enderecoRemover = listaExibir.remove(index);
				} else {
					listaExibir.remove(index);
				}
				break;
			}
		}
		
		List<EnderecoAssociacaoDTO> listaRemover = this.obterEnderecosSessaoRemover();
		
		listaRemover.add(enderecoRemover);
		
		this.session.setAttribute(ATRIBUTO_SESSAO_LISTA_ENDERECOS_REMOVER, listaRemover);
		
		this.pesquisarEnderecos(tela,null, null);
	}

	/**
	 * Método responsável por preparar um endereço para a edição.
	 * 
	 * @param idEnderecoAssociacao
	 */
	@Post
	public void editarEndereco(Tela tela,Long idEnderecoAssociacao) {
		
		tela.setarParametros();
		
		EnderecoAssociacaoDTO enderecoAssociacao = null;
		
		List<EnderecoAssociacaoDTO> listaEndereco =	this.obterEnderecosSessaoSalvar();
		
		for (EnderecoAssociacaoDTO dto : listaEndereco){
			
			if (dto.getId().equals(idEnderecoAssociacao)){
				
				enderecoAssociacao = dto;
				break;
			}
		}
		
		if (enderecoAssociacao == null){
			
			List<EnderecoAssociacaoDTO> listaEnderecoExibir =	this.obterEnderecosSessaoExibir();
			
			for (EnderecoAssociacaoDTO dto : listaEnderecoExibir){
				
				if (dto.getId().equals(idEnderecoAssociacao)){
					
					enderecoAssociacao = dto;
					break;
				}
			}
		}
		
		if (enderecoAssociacao == null){
			
			Endereco endereco = this.enderecoService.buscarEnderecoPorId(idEnderecoAssociacao);

			if (endereco.getCep() != null) {
			
				endereco.setCep(retirarFormatacaoCep(endereco.getCep()));
			}
			
			if (endereco != null){
				
				enderecoAssociacao = new EnderecoAssociacaoDTO(
						false, 
						endereco, 
						null,
						null);
				
				enderecoAssociacao.setId(System.currentTimeMillis());
				
				List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = this.obterEnderecosSessaoSalvar();
				
				listaEnderecoAssociacao.add(enderecoAssociacao);
				
				this.session.setAttribute(ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR, listaEnderecoAssociacao);
			}
		}

		this.result.use(Results.json()).from(enderecoAssociacao, "result").recursive().exclude("endereco.pessoa").serialize();
	}

	/**
	 * Método que realiza a busca do endereço pelo CEP 
	 * 
	 * @param cep
	 */
	@Post
	public void obterEnderecoPorCep(String cep) {
		
		cep = retirarFormatacaoCep(cep);
		
		EnderecoVO enderecoRetornado = this.enderecoService.obterEnderecoPorCep(cep);
		
		if (enderecoRetornado == null) {
			
			this.result.use(Results.json()).from("", "result").recursive().serialize();
		
		} else {

			this.result.use(Results.json()).from(enderecoRetornado, "result").recursive().serialize();
		}
	}
	
	/*
	 * Método para popular autocomplete de logradouros da tela de pesquisa de cotas
	 */
	@Post
	public void pesquisarLogradouros(String nomeLogradouro){
		
		List<ItemAutoComplete> autoCompleteLogradouros = 
				new ArrayList<ItemAutoComplete>();
		
		List<Logradouro> logradouros = 
				this.enderecoService.pesquisarLogradouros(nomeLogradouro);
		
		for (Logradouro logradouro : logradouros){
			
			ItemAutoComplete itemAutoComplete = 
					new ItemAutoComplete(logradouro.getNome(), logradouro.getNome(), logradouro.getId());
			
			autoCompleteLogradouros.add(itemAutoComplete);
		}
		
		this.result.use(Results.json()).from(autoCompleteLogradouros, "result").include("value", "chave").serialize();
	}
	
	/*
	 * Método para popular autocomplete de bairros da tela de pesquisa de cotas
	 */
	@Post
	public void pesquisarBairros(String nomeBairro){
		
		List<ItemAutoComplete> autoCompleteBairros = 
				new ArrayList<ItemAutoComplete>();
		
		List<Bairro> bairros = 
				this.enderecoService.pesquisarBairros(nomeBairro);
		
		for (Bairro bairro : bairros){
			
			ItemAutoComplete itemAutoComplete = 
					new ItemAutoComplete(bairro.getNome(), bairro.getNome(), bairro.getId());
			
			autoCompleteBairros.add(itemAutoComplete);
		}
		
		this.result.use(Results.json()).from(autoCompleteBairros, "result").include("value", "chave").serialize();
	}
	
	/*
	 * Método para popular autocomplete de municipios da tela de pesquisa de cotas
	 */
	@Post
	public void pesquisarLocalidades(String nomeLocalidade){
		
		List<ItemAutoComplete> autoCompleteLocalidades = 
				new ArrayList<ItemAutoComplete>();
		
		List<Localidade> localidades = 
				this.enderecoService.pesquisarLocalidades(nomeLocalidade);
		
		for (Localidade localidade : localidades){
			
			ItemAutoComplete itemAutoComplete = 
					new ItemAutoComplete(localidade.getNome(), localidade.getNome(), localidade.getId());
			
			autoCompleteLocalidades.add(itemAutoComplete);
		}
		
		this.result.use(Results.json()).from(autoCompleteLocalidades, "result").include("value", "chave").serialize();
	}

	private String retirarFormatacaoCep(String cep) {

		return cep.replaceAll("-", "");
	}
	
	/**
	 * 
	 * @param enderecoAssociacao
	 */
	private void validarDadosEndereco(EnderecoAssociacaoDTO enderecoAssociacao) {
	
		Endereco endereco = enderecoAssociacao.getEndereco();
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if (enderecoAssociacao.getTipoEndereco() == null) {
			
			listaMensagens.add("O preenchimento do campo [Tipo Endereco] é obrigatório.");
		}

		if (endereco.getCep() == null || endereco.getCep().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [CEP] é obrigatório.");
		}

		if (endereco.getTipoLogradouro() == null || endereco.getTipoLogradouro().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Tipo Logradouro] é obrigatório.");
		}
		
		if (endereco.getLogradouro() == null || endereco.getLogradouro().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Logradouro] é obrigatório.");
		}

		if (endereco.getNumero() == null) {
			
			listaMensagens.add("O preenchimento do campo [Número] é obrigatório.");
		}
		
		if (endereco.getBairro() == null || endereco.getBairro().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Bairro] é obrigatório.");
		}		

		if (endereco.getCidade() == null || endereco.getCidade().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Cidade] é obrigatório.");
		}
		
		if (endereco.getUf() == null || endereco.getUf().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [UF] é obrigatório.");
		}
		
		if (!listaMensagens.isEmpty()) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, listaMensagens));
		}
	}
	
	/**
	 * Método que retorna um Table Model de acordo com a lista de Endereços desejada.
	 * 
	 * @param listaEndereco
	 * @return TableModel<CellModel>
	 */
	private TableModel<CellModel> getTableModelListaEndereco(List<EnderecoAssociacaoDTO> listaEnderecoAssociacao) {

		TableModel<CellModel> tableModel = new TableModel<CellModel>();

		List<CellModel> listaCellModel = new ArrayList<CellModel>();

		long idCellModel = 0;
		
		for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {

			if (enderecoAssociacao.getId() == null) {

				idCellModel = enderecoAssociacao.getEndereco().getId() == null ? (int) System.currentTimeMillis() * -1 : enderecoAssociacao.getEndereco().getId().intValue();

				enderecoAssociacao.setId(idCellModel);
			}

			CellModel cellModel = getCellModelEndereco(enderecoAssociacao);

			listaCellModel.add(cellModel);
		}

		tableModel.setPage(1);
		tableModel.setRows(listaCellModel);
		tableModel.setTotal(listaCellModel.size()); 

		return tableModel;
	}

	/**
	 * Método que retorna um Cell Model de acordo com o Endereço desejado.
	 * 
	 * @param endereco
	 * @return CellModel
	 */
	private CellModel getCellModelEndereco(EnderecoAssociacaoDTO enderecoAssociacao) {

		return new CellModel(
				enderecoAssociacao.getId().intValue(),
				enderecoAssociacao.getTipoEndereco() == null ? "": enderecoAssociacao.getTipoEndereco().getTipoEndereco(),
				enderecoAssociacao.getEndereco().getLogradouro() 
					+ ", nº: " + enderecoAssociacao.getEndereco().getNumero(), 
				enderecoAssociacao.getEndereco().getBairro(),
				Util.adicionarMascaraCEP(enderecoAssociacao.getEndereco().getCep()), 
				enderecoAssociacao.getEndereco().getCidade(), 
				String.valueOf(enderecoAssociacao.isEnderecoPrincipal())
			);
	}
	
	/**
	 * Método que realiza a verificação de endereço principal na lista corrente. 
	 * 
	 * @param listaEnderecoAssociacao
	 */
	private void validarExistenciaEnderecoPrincipal(EnderecoAssociacaoDTO enderecoAssociacaoAtual) {
		
		List<EnderecoAssociacaoDTO> listaEnderecos = new ArrayList<EnderecoAssociacaoDTO>();
		
		List<EnderecoAssociacaoDTO> listaEnderecosSalvar = this.obterEnderecosSessaoSalvar();
		
		List<EnderecoAssociacaoDTO> listaEnderecosExibir = this.obterEnderecosSessaoExibir();
		
		listaEnderecos.addAll(listaEnderecosExibir);
		listaEnderecos.addAll(listaEnderecosSalvar);
		
		boolean hasPrincipal = enderecoAssociacaoAtual.isEnderecoPrincipal();
		
		for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecos) {
				
			if (enderecoAssociacao.isEnderecoPrincipal()) {
				
				hasPrincipal = enderecoAssociacao.isEnderecoPrincipal();
				
				if (!enderecoAssociacao.equals(enderecoAssociacaoAtual) && enderecoAssociacaoAtual.isEnderecoPrincipal()) {
					
					throw new ValidacaoException(TipoMensagem.WARNING, "Já existe um endereço principal.");
				}
			}
		}
		
		if (!hasPrincipal) {
			throw new ValidacaoException(TipoMensagem.WARNING, "É necessário pelo menos um endereço principal.");
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<EnderecoAssociacaoDTO> obterEnderecosSessaoSalvar(){
		
		List<EnderecoAssociacaoDTO> listaEndereco =
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR);
		
		if (listaEndereco == null) {
			
			listaEndereco = new ArrayList<EnderecoAssociacaoDTO>();
		}
		
		return listaEndereco;
	}
	
	@SuppressWarnings("unchecked")
	private List<EnderecoAssociacaoDTO> obterEnderecosSessaoRemover(){
		
		List<EnderecoAssociacaoDTO> lista = (List<EnderecoAssociacaoDTO>) 
				this.session.getAttribute(ATRIBUTO_SESSAO_LISTA_ENDERECOS_REMOVER);
		
		if (lista == null){
			
			lista = new ArrayList<EnderecoAssociacaoDTO>();
		}
		
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private List<EnderecoAssociacaoDTO> obterEnderecosSessaoExibir(){
		
		List<EnderecoAssociacaoDTO> lista = (List<EnderecoAssociacaoDTO>)
			this.session.getAttribute(ATRIBUTO_SESSAO_LISTA_ENDERECOS_EXIBIR);
		
		if (lista == null){
			
			lista = new ArrayList<EnderecoAssociacaoDTO>();
		}
		
		return lista;
	}
}
