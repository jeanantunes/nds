package br.com.abril.nds.controllers.devolucao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.DigitacaoContagemDevolucaoVO;
import br.com.abril.nds.client.vo.ResultadoDigitacaoContagemDevolucaoVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
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
import br.com.abril.nds.vo.PeriodoVO;
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
	public void pesquisar(String dataDe,String dataAte,Long idFornecedor,String sortorder, String sortname, int page, int rp){
		
		PeriodoVO periodo =  obterPeriodoValidado(dataDe, dataAte);
		
		FiltroDigitacaoContagemDevolucaoDTO filtro = new FiltroDigitacaoContagemDevolucaoDTO(periodo,idFornecedor);
		
		configurarPaginacaoPesquisa(filtro, sortorder, sortname, page, rp);
		
		tratarFiltro(filtro);
		
		efetuarPesquisa(filtro);
	}
	
	private List<ContagemDevolucaoDTO> getMock(){
		
		List<ContagemDevolucaoDTO> list = new ArrayList<ContagemDevolucaoDTO>();
		
		for (int i = 0; i < 10; i++) {
		
			ContagemDevolucaoDTO xx = new ContagemDevolucaoDTO();
			xx.setCodigoProduto("100");
			xx.setIdConferenciaEncParcial(10L);
			xx.setIdMovimentoEstoqueCota(1L);
			xx.setNomeProduto("Nome");
			xx.setNumeroEdicao(10L);
			xx.setPrecoVenda(new BigDecimal(20));
			xx.setQtdDevolucao(new BigDecimal(25));
			xx.setQtdNota(new BigDecimal(85));
			xx.setValorTotal(new BigDecimal(52));
			
			list.add(xx);
		}
		return list;
		
	}
	
	/**
	 * Executa a pesquisa de digitação de contagem de devolução 
	 * @param filtro
	 */
	public void efetuarPesquisa(FiltroDigitacaoContagemDevolucaoDTO filtro){
		
		Long quantidadeRegistros = 10L; //TODO chamar componente
		
		InfoContagemDevolucaoDTO infoConatege = new InfoContagemDevolucaoDTO();//TODO chamar componente
		
		//List<ContagemDevolucaoDTO> listaResultados = infoConatege.getListaContagemDevolucao();
		
		List<ContagemDevolucaoDTO> listaResultados =  getMock();
		
		if (listaResultados == null || listaResultados.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		List<DigitacaoContagemDevolucaoVO> listaResultadosVO = new ArrayList<DigitacaoContagemDevolucaoVO>();
		
		DigitacaoContagemDevolucaoVO digitacaoContagemDevolucaoVO = null;
		
		for(ContagemDevolucaoDTO dto: listaResultados){
			
			digitacaoContagemDevolucaoVO = new DigitacaoContagemDevolucaoVO();
			
			digitacaoContagemDevolucaoVO.setCodigoProduto(dto.getCodigoProduto());
			digitacaoContagemDevolucaoVO.setIdConferenciaEncParcial(String.valueOf(dto.getIdConferenciaEncParcial()) );
			digitacaoContagemDevolucaoVO.setIdMovimentoEstoqueCota(String.valueOf(dto.getIdMovimentoEstoqueCota()));
			digitacaoContagemDevolucaoVO.setNomeProduto(dto.getNomeProduto());
			digitacaoContagemDevolucaoVO.setNumeroEdicao(String.valueOf(dto.getNumeroEdicao()));
			digitacaoContagemDevolucaoVO.setPrecoVenda(CurrencyUtil.formatarValor(dto.getPrecoVenda()));
			digitacaoContagemDevolucaoVO.setQntDiferenca(null);
			digitacaoContagemDevolucaoVO.setQtdDevolucao(String.valueOf(dto.getQtdDevolucao()));
			digitacaoContagemDevolucaoVO.setQtdNota(String.valueOf(dto.getQtdNota()));
			digitacaoContagemDevolucaoVO.setValorTotal(CurrencyUtil.formatarValor(dto.getValorTotal()));
			
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
	 * Valida o periodo da consulta e retorna um objeto com os valores. 
	 * @param dataInicial
	 * @param dataFinal
	 * @return PeriodoVO
	 */
	private PeriodoVO obterPeriodoValidado(String dataInicial, String dataFinal) {
				
		tratarErroDatas(validarPreenchimentoObrigatorio(dataInicial, dataFinal));

		tratarErroDatas(validarFormatoDatas(dataInicial, dataFinal));		
		
		validarPeriodo(dataInicial, dataFinal);		
		
		PeriodoVO periodo = new PeriodoVO(DateUtil.parseData(dataInicial, "dd/MM/yyyy"), 
										  DateUtil.parseData(dataFinal, "dd/MM/yyyy"));

		return periodo; 
	}
	
	/**
	 * Trata mensagens de erro, caso tenha mensagem lança exceção de erro.
	 * @param mensagensErro
	 */
	private void tratarErroDatas(List<String> mensagensErro){
		
		ValidacaoVO validacao = new ValidacaoVO();
		
		validacao.setTipoMensagem(TipoMensagem.ERROR);
		
		if(!mensagensErro.isEmpty()){
			
			validacao.setListaMensagens(mensagensErro);
			throw new ValidacaoException(validacao);
		}
	}
	
	/**
	 * Valida o período informado para consulta 
	 * @param dataInicial
	 * @param dataFinal
	 */
	private void validarPeriodo(String dataInicial,String dataFinal){

		if (DateUtil.isDataInicialMaiorDataFinal(DateUtil.parseDataPTBR(dataInicial),
				 								 DateUtil.parseDataPTBR(dataFinal))) {

			throw new ValidacaoException(TipoMensagem.ERROR, "O campo [Período de] não pode ser maior que o campo [Até]!");
		}
		
		if (!DateUtil.isDataInicialMaiorDataFinal(DateUtil.parseDataPTBR(dataInicial),
						 						 DateUtil.parseDataPTBR(dataFinal))) {
		
			throw new ValidacaoException(TipoMensagem.ERROR, "O campo [Até] não pode ser menor que o campo [Período de]!");
		}
		
	}
	
	/**
	 * Valida o formato  das datas informadas  em um determinado período.
	 * @param dataInicial
	 * @param dataFinal
	 * @return List<String>
	 */
	private List<String> validarFormatoDatas(String dataInicial,String dataFinal){
		
		List<String> mensagens = new ArrayList<String>();
		
		if (!DateUtil.isValidDate(dataInicial, "dd/MM/yyyy")) {
			
			mensagens.add("O campo [Período de] é inválido");
		} 
		
		if (!DateUtil.isValidDate(dataFinal, "dd/MM/yyyy")) {
			
			mensagens.add ("O campo [Até] é inválido");
		}
		
		return mensagens;
	}
	
	/**
	 * Valida o preenchimento obrigatório do período informado.
	 * @param dataInicial
	 * @param dataFinal
	 * @return List<String>
	 */
	private List<String> validarPreenchimentoObrigatorio(String dataInicial,String dataFinal){
		
		 List<String> mensagens = new ArrayList<String>();
		
		if (dataInicial == null || dataInicial.isEmpty()) {
			
			mensagens.add("O preenchimento do campo [Período de] é obrigatório");
		} 
		
		if (dataFinal == null || dataFinal.isEmpty()) {
			
			mensagens.add("O preenchimento do campo [Até] é obrigatório");
		} 
		
		return mensagens;
	}
}
