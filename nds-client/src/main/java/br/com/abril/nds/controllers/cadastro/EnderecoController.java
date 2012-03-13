package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
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
	private EnderecoService enderecoService;

	@Path("/")
	public void index() { }
	
	/**
	 * Método que realiza a pesquisa dos endereços cadastrados para uma determinada pessoa.
	 */
	@Post
	@SuppressWarnings("unchecked")
	public void pesquisarEnderecos() {

		List<EnderecoAssociacaoDTO> listaEndereco =
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR);

		TableModel<CellModel> tableModelEndereco = new TableModel<CellModel>();
		
		if (listaEndereco == null) {

			listaEndereco = new ArrayList<EnderecoAssociacaoDTO>();

		} else {

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

		List<EnderecoAssociacaoDTO> listaEnderecoAssociacao =
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR);

		if (listaEnderecoAssociacao == null) {

			listaEnderecoAssociacao = new ArrayList<EnderecoAssociacaoDTO>();		
		
		} else if (enderecoAssociacao.isEnderecoPrincipal()) {

			validarExistenciaEnderecoPrincipal(listaEnderecoAssociacao);
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
	 * @param idEndereco
	 */
	@SuppressWarnings("unchecked")
	public void removerEndereco(Long idEndereco) {

		Endereco endereco = enderecoService.obterEnderecoPorId(idEndereco);

		if (endereco == null) {

			endereco = new Endereco();

			endereco.setId(idEndereco);

		} else {

			List<Endereco> listaEnderecosRemover = 
				(List<Endereco>) this.session.getAttribute(Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_REMOVER);

			if (listaEnderecosRemover == null) {

				listaEnderecosRemover = new ArrayList<Endereco>();
			}

			listaEnderecosRemover.add(endereco);

			this.session.setAttribute(Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_REMOVER, listaEnderecosRemover);
		}

		EnderecoAssociacaoDTO enderecoAssociacao = new EnderecoAssociacaoDTO();

		enderecoAssociacao.setEndereco(endereco);

		List<EnderecoAssociacaoDTO> listaEnderecoAssociacao =
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR);

		listaEnderecoAssociacao.remove(enderecoAssociacao);

		TableModel<CellModel> tableModelEndereco = getTableModelListaEndereco(listaEnderecoAssociacao);

		this.session.setAttribute(Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR, listaEnderecoAssociacao);

		this.result.use(Results.json()).from(tableModelEndereco, "result").recursive().serialize();
	}

	/**
	 * Método responsável por preparar um endereço para a edição.
	 * 
	 * @param idEndereco
	 */
	@Post
	@SuppressWarnings("unchecked")
	public void editarEndereco(Long idEndereco) {

		Endereco endereco = new Endereco();

		endereco.setId(idEndereco);

		EnderecoAssociacaoDTO enderecoAssociacao = new EnderecoAssociacaoDTO();

		enderecoAssociacao.setEndereco(endereco);

		List<EnderecoAssociacaoDTO> listaEndereco =
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(Constantes.ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR);

		int index = listaEndereco.indexOf(enderecoAssociacao);

		enderecoAssociacao = listaEndereco.get(index);

		this.result.use(Results.json()).from(enderecoAssociacao, "result").recursive().serialize();
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
		
		for (EnderecoAssociacaoDTO endereco : listaEnderecoAssociacao) {

			endereco.getEndereco().setId(++idCellModel);

			CellModel cellModel = getCellModelEndereco(endereco);

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
				enderecoAssociacao.getEndereco().getId().intValue(),
				enderecoAssociacao.getTipoEndereco().getTipoEndereco(),
				enderecoAssociacao.getEndereco().getLogradouro(), 
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
	private void validarExistenciaEnderecoPrincipal(List<EnderecoAssociacaoDTO> listaEnderecoAssociacao) {

		for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {

			if (enderecoAssociacao.isEnderecoPrincipal()) {

				throw new ValidacaoException(TipoMensagem.ERROR, "Já existe um endereço principal.");
			}
		}
	}
}
