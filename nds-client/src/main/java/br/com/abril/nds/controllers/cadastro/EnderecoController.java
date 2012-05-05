package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.endereco.vo.EnderecoVO;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.service.ConsultaBaseEnderecoService;
import br.com.abril.nds.service.EnderecoService;
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
	private ConsultaBaseEnderecoService consultaBaseEnderecoService;
	
	@Autowired
	private EnderecoService enderecoService;
	
	public enum Tela{
		
		ENDERECO_FIADOR,ENDERECO_COTA,ENDERECO_ENTREGADOR,ENDERECO_PDV,ENDERECO_TRANSPORTADOR;
		
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

		if (enderecoAssociacao.isEnderecoPrincipal()) {

			validarExistenciaEnderecoPrincipal(enderecoAssociacao);
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
			
			listaEnderecoAssociacao.add(enderecoAssociacao);
		}

		this.session.setAttribute(ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR, listaEnderecoAssociacao);
		
		this.pesquisarEnderecos(tela,null, null);
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
		
		EnderecoVO enderecoRetornado = this.consultaBaseEnderecoService.buscarPorCep(cep);
		
		if (enderecoRetornado == null) {
			
			this.result.nothing();
		
		} else {

			Endereco endereco = parseEndereco(enderecoRetornado);
			
			endereco.setCep(cep);

			EnderecoAssociacaoDTO enderecoAssociacao = new EnderecoAssociacaoDTO();

			enderecoAssociacao.setEndereco(endereco);

			enderecoAssociacao.setEnderecoPrincipal(false);

			this.result.use(Results.json()).from(enderecoAssociacao, "result").recursive().serialize();
		}
	}

	private Endereco parseEndereco(EnderecoVO enderecoRetornado) {
		
		String bairro = enderecoRetornado.getBairro() == null 
					? "" : enderecoRetornado.getBairro().getNome();
		
		String cidade = enderecoRetornado.getLocalidade() == null 
					? "" : enderecoRetornado.getLocalidade().getNome();
		
		String tipoLogradouro = enderecoRetornado.getTipoLogradouro() == null 
				? "" : enderecoRetornado.getTipoLogradouro().getNome();
		
		String logradouro = enderecoRetornado.getLogradouro() == null 
					? "" : enderecoRetornado.getLogradouro().getNome();
		
		String uf = enderecoRetornado.getUnidadeFederecao() == null 
					? "" : enderecoRetornado.getUnidadeFederecao().getSigla();
		
		Endereco endereco = new Endereco();

		endereco.setBairro(bairro);

		endereco.setCidade(cidade);

		endereco.setTipoLogradouro(tipoLogradouro);
		
		endereco.setLogradouro(logradouro);

		endereco.setUf(uf);
		
		return endereco;
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
		
		if (endereco.getCep() == null || endereco.getCep().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [CEP] é obrigatório.");
		}

		if (endereco.getTipoLogradouro() == null || endereco.getTipoLogradouro().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Tipo Logradouro] é obrigatório.");
		}
		
		if (endereco.getLogradouro() == null || endereco.getLogradouro().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Logradouro] é obrigatório.");
		}

		if (endereco.getNumero() <= 0) {
			
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
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, listaMensagens));
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
				(enderecoAssociacao.getTipoEndereco() == null)?"":enderecoAssociacao.getTipoEndereco().getTipoEndereco(),
				enderecoAssociacao.getEndereco().getLogradouro() 
					+ ", nº: " + enderecoAssociacao.getEndereco().getNumero(), 
				enderecoAssociacao.getEndereco().getBairro(),
				enderecoAssociacao.getEndereco().getCep(), 
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

		List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = this.obterEnderecosSessaoSalvar();
		
		for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {

			if (enderecoAssociacao.isEnderecoPrincipal() && !enderecoAssociacao.equals(enderecoAssociacaoAtual)) {

				throw new ValidacaoException(TipoMensagem.ERROR, "Já existe um endereço principal.");
			}
		}
		
		List<EnderecoAssociacaoDTO> listaExibir = this.obterEnderecosSessaoExibir();
		
		for (EnderecoAssociacaoDTO enderecoAssociacao : listaExibir) {

			if (enderecoAssociacao.isEnderecoPrincipal() && !enderecoAssociacao.equals(enderecoAssociacaoAtual)) {

				throw new ValidacaoException(TipoMensagem.ERROR, "Já existe um endereço principal.");
			}
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
