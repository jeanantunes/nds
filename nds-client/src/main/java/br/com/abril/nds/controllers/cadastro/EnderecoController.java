package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.endereco.vo.EnderecoVO;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.service.ConsultaBaseEnderecoService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.Constantes;
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

	@Autowired
	private Result result;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private ConsultaBaseEnderecoService consultaBaseEnderecoService;
	
	@Path("/")
	public void index() {
		
		this.session.removeAttribute(Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR);

		this.session.removeAttribute(Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_REMOVER);
	}

	/**
	 * Método que realiza a pesquisa dos endereços cadastrados para uma determinada pessoa.
	 */
	@Post
	@SuppressWarnings("unchecked")
	public void pesquisarEnderecos(String sortname, String sortorder) {

		List<EnderecoAssociacaoDTO> listaEndereco =
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR);

		TableModel<CellModel> tableModelEndereco = new TableModel<CellModel>();

		if (listaEndereco == null) {

			listaEndereco = new ArrayList<EnderecoAssociacaoDTO>();

		} else {

			if (sortname != null) {

				sortorder = sortorder == null ? "asc" : sortorder;

				Ordenacao ordenacao = Util.getEnumByStringValue(Ordenacao.values(), sortorder);

				PaginacaoUtil.ordenarEmMemoria(listaEndereco, ordenacao, sortname);
			}

			tableModelEndereco = getTableModelListaEndereco(listaEndereco);
		}

		this.session.setAttribute(Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR, listaEndereco);

		this.result.use(Results.json()).withoutRoot().from(tableModelEndereco).recursive().serialize();
	}

	/**
	 * Método responsável por incluir um novo endereço para a pessoa em questão.
	 * 
	 * @param enderecoAssociacao
	 */
	@SuppressWarnings("unchecked")
	public void incluirNovoEndereco(EnderecoAssociacaoDTO enderecoAssociacao) {

		validarDadosEndereco(enderecoAssociacao);

		List<EnderecoAssociacaoDTO> listaEnderecoAssociacao =
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(
						Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR);

		if (listaEnderecoAssociacao == null) {

			listaEnderecoAssociacao = new ArrayList<EnderecoAssociacaoDTO>();		
		
		} else if (enderecoAssociacao.isEnderecoPrincipal()) {

			validarExistenciaEnderecoPrincipal(listaEnderecoAssociacao, enderecoAssociacao);
		}

		if (listaEnderecoAssociacao.contains(enderecoAssociacao)) {

			int index = listaEnderecoAssociacao.indexOf(enderecoAssociacao);

			listaEnderecoAssociacao.set(index, enderecoAssociacao);

		} else {

			listaEnderecoAssociacao.add(enderecoAssociacao);
		}

		TableModel<CellModel> tableModelEndereco = getTableModelListaEndereco(listaEnderecoAssociacao);

		this.session.setAttribute(Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR, listaEnderecoAssociacao);

		this.result.use(Results.json()).from(tableModelEndereco, "result").recursive().serialize();
	}

	/**
	 * Método que irá remover um endereço a partir de seu ID. 
	 * 
	 * @param idEnderecoAssociacao
	 */
	@Post
	@SuppressWarnings("unchecked")
	public void removerEndereco(Long idEnderecoAssociacao) {

		List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar =
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR);

		EnderecoAssociacaoDTO enderecoAssociacao = new EnderecoAssociacaoDTO();

		enderecoAssociacao.setId(idEnderecoAssociacao);

		listaEnderecoAssociacaoSalvar.remove(enderecoAssociacao);

		this.session.setAttribute(Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR, listaEnderecoAssociacaoSalvar);

		TableModel<CellModel> tableModelEndereco = getTableModelListaEndereco(listaEnderecoAssociacaoSalvar);

		if (idEnderecoAssociacao >= 0) {

			List<EnderecoAssociacaoDTO> listaEnderecosRemover = 
					(List<EnderecoAssociacaoDTO>) this.session.getAttribute(Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_REMOVER);

			if (listaEnderecosRemover == null) {

				listaEnderecosRemover = new ArrayList<EnderecoAssociacaoDTO>();
			}

			listaEnderecosRemover.add(enderecoAssociacao);

			this.session.setAttribute(Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_REMOVER, listaEnderecosRemover);
		}

		this.result.use(Results.json()).from(tableModelEndereco, "result").recursive().serialize();
	}

	/**
	 * Método responsável por preparar um endereço para a edição.
	 * 
	 * @param idEnderecoAssociacao
	 */
	@Post
	@SuppressWarnings("unchecked")
	public void editarEndereco(Long idEnderecoAssociacao) {

		EnderecoAssociacaoDTO enderecoAssociacao = new EnderecoAssociacaoDTO();

		enderecoAssociacao.setId(idEnderecoAssociacao);

		List<EnderecoAssociacaoDTO> listaEndereco =
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR);

		int index = listaEndereco.indexOf(enderecoAssociacao);

		enderecoAssociacao = listaEndereco.get(index);

		this.result.use(Results.json()).from(enderecoAssociacao, "result").recursive().serialize();
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

				idCellModel = (int) System.currentTimeMillis() * -1;

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
				enderecoAssociacao.getTipoEndereco().getTipoEndereco(),
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
	private void validarExistenciaEnderecoPrincipal(List<EnderecoAssociacaoDTO> listaEnderecoAssociacao,
													EnderecoAssociacaoDTO enderecoAssociacaoAtual) {

		for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {

			if (enderecoAssociacao.isEnderecoPrincipal() && !enderecoAssociacao.equals(enderecoAssociacaoAtual)) {

				throw new ValidacaoException(TipoMensagem.ERROR, "Já existe um endereço principal.");
			}
		}
	}
}
