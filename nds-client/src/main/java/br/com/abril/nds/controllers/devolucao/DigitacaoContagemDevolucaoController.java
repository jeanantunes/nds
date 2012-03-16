package br.com.abril.nds.controllers.devolucao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.DigitacaoContagemDevolucaoVO;
import br.com.abril.nds.client.vo.ResultadoDigitacaoContagemDevolucaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.dto.InfoContagemDevolucaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
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
@Path(value="/devolucao/digitacao/contagem")
public class DigitacaoContagemDevolucaoController  {
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroPesquisa";
	
	@Path("/")
	public void index(){
		
		carregarComboFornecedores();
	}
	
	
	/**
	 * Método responsável por carregar o combo de fornecedores.
	 */
	private void carregarComboFornecedores() {
		
		List<Fornecedor> listaFornecedor =fornecedorService.obterFornecedoresAtivos();
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo = new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(
				new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial()));
		}
		
		result.include("listaFornecedores",listaFornecedoresCombo );
	}
	
	@Post
	@Path("/pesquisar")
	public void pesquisar(String dataPesquisa,Long idFornecedor,String sortorder, String sortname, int page, int rp){
		
		
		validarParametroPesquisa(dataPesquisa);
		
		FiltroDigitacaoContagemDevolucaoDTO filtro = 
				new FiltroDigitacaoContagemDevolucaoDTO(DateUtil.parseData(dataPesquisa, "dd/MM/yyyy"), idFornecedor);
		
		configurarPaginacaoPesquisa(filtro, sortorder, sortname, page, rp);
		
		tratarFiltro(filtro);
		
	}
	
	/**
	 * Executa a pesquisa de digitação de contagem de devolução 
	 * @param filtro
	 */
	public void efetuarPesquisa(FiltroDigitacaoContagemDevolucaoDTO filtro){
		
		Long quantidadeRegistros = 0L; //TODO chamar componente
		
		InfoContagemDevolucaoDTO infoConatege = new InfoContagemDevolucaoDTO();//TODO chamar componente
		
		List<ContagemDevolucaoDTO> listaResultados = infoConatege.getListaContagemDevolucao();
		
		if (listaResultados == null || listaResultados.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		List<DigitacaoContagemDevolucaoVO> listaResultadosVO = new ArrayList<DigitacaoContagemDevolucaoVO>();
		
		DigitacaoContagemDevolucaoVO digitacaoContagemDevolucaoVO = null;
		
		for(ContagemDevolucaoDTO dto: listaResultados){
			
			digitacaoContagemDevolucaoVO = new DigitacaoContagemDevolucaoVO();
			
			
			
			listaResultadosVO.add(digitacaoContagemDevolucaoVO);
		}
		
		TableModel<CellModelKeyValue<DigitacaoContagemDevolucaoVO>> tableModel = new TableModel<CellModelKeyValue<DigitacaoContagemDevolucaoVO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaResultadosVO));

		tableModel.setTotal((quantidadeRegistros!= null)? quantidadeRegistros.intValue():0);

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		String valorTotalFormatado = CurrencyUtil.formatarValor(infoConatege.getValorTotalGeral());
		
		ResultadoDigitacaoContagemDevolucaoVO resultadoPesquisa = new ResultadoDigitacaoContagemDevolucaoVO(tableModel,valorTotalFormatado);

		result.use(Results.json()).withoutRoot().from(resultadoPesquisa).recursive().serialize();
	}
	
	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 * 
	 * @param filtroResumoExpedicao
	 */
	private void tratarFiltro(FiltroDigitacaoContagemDevolucaoDTO filtro) {

		FiltroDigitacaoContagemDevolucaoDTO filtroResumoSession = 
				(FiltroDigitacaoContagemDevolucaoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroResumoSession != null && !filtroResumoSession.equals(filtro)) {

			filtroResumoSession.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}
	
	/**
	 * Configura paginação da lista de resultados
	 * 
	 * @param filtro
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	private void configurarPaginacaoPesquisa(FiltroDigitacaoContagemDevolucaoDTO filtro, 
											String sortorder, String sortname,
											int page, int rp) {

		if (filtro != null) {

			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);

			filtro.setPaginacao(paginacao);

			filtro.setOrdenacaoColuna((Util.getEnumByStringValue(FiltroDigitacaoContagemDevolucaoDTO.OrdenacaoColuna.values(),sortname)));
		}
	}
	
	/**
	 * Verifica se os parametros de pesquisas são validos.
	 * 
	 * @param dataPesquisa
	 */
	private void validarParametroPesquisa(String dataPesquisa){
		
		
		if (dataPesquisa == null || dataPesquisa.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,"O preenchimento do campo \"Data \" é obrigatório." );
		}
		
		if (!DateUtil.isValidDate(dataPesquisa, "dd/MM/yyyy")) {
			
			throw new ValidacaoException(TipoMensagem.ERROR,"Data de pesquisa inválida." );
		}
	}
}
