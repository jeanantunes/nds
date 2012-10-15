package br.com.abril.nds.controllers.administracao;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.dto.ConsultaAlteracaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroAlteracaoCotaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.ParametroSistemaService;
import br.com.abril.nds.model.cadastro.BaseCalculo;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomMapJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.AlteracaoCotaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.service.FileService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.HistoricoTitularidadeCotaFinanceiroService;
import br.com.abril.nds.service.ParametroCobrancaCotaService;
import br.com.abril.nds.service.TipoEntregaService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;

@Resource
@Path("/administracao/alteracaoCota")
public class AlteracaoCotaController {

	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private TipoEntregaService tipoEntregaService;
	
	@Autowired
	private EnderecoService enderecoService;
	
	@Autowired
	private AlteracaoCotaService alteracaoCotaService;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private ParametroCobrancaCotaService parametroCobrancaCotaService;

	@Autowired
	private HistoricoTitularidadeCotaFinanceiroService historicoTitularidadeCotaFinanceiroService;
	
	@Autowired
	private FileService fileService; 
	
	@Autowired
	private ParametroSistemaService parametroSistemaService;
	
	public static final FileType[] extensoesAceitas = 
		{FileType.DOC, FileType.DOCX, FileType.BMP, FileType.GIF, FileType.PDF, FileType.JPEG, FileType.JPG, FileType.PNG};
	
	
	private Result result;
	
	public AlteracaoCotaController(Result result) {
		super();
		this.result = result;
	}
	
	@Path("/")
	@Rules(Permissao.ROLE_CADASTRO_ALTERACAO_COTA)
	public void index()
	{
		result.include("listFornecedores", fornecedorService.obterFornecedoresAtivos());
		result.include("listBairros", enderecoService.pesquisarTodosBairros());
		List<String> obterMunicipiosCotas = enderecoService.obterMunicipiosCotas();
		result.include("listMunicipios", obterMunicipiosCotas);
		
		List<Integer> listaVencimento = new ArrayList<Integer>();
		for(int i = 1; i < 31; i++){
			listaVencimento.add(i);
		}
		result.include("listaVencimento", listaVencimento);
		result.include("listTipoEntrega", DescricaoTipoEntrega.values());
		result.include("listTipoDesconto", TipoDesconto.values());
		result.include("listBaseCalculo", BaseCalculo.values());
		result.include("listHistoricoTitularidadeCotaFinanceiro", historicoTitularidadeCotaFinanceiroService.pesquisarTodos());
		
	}
	
	@Path("/pesquisarAlteracaoCota.json")
	public void pesquisarAlteracaoCota(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, String sortname, int page, int rp) {
		
		int startSearch = page * rp - rp;
		
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortname);
		paginacao.setOrdenacao(paginacao.getOrdenacao().ASC);
		filtroAlteracaoCotaDTO.setPaginacao(paginacao);
		
		filtroAlteracaoCotaDTO.setNomeCota(PessoaUtil.removerSufixoDeTipo(filtroAlteracaoCotaDTO.getNomeCota()));
		
		List<ConsultaAlteracaoCotaDTO> lista = this.alteracaoCotaService.pesquisarAlteracaoCota(filtroAlteracaoCotaDTO);
		
		
		this.result.use(FlexiGridJson.class).from(lista).total(lista.size()).page(page).serialize();
		
	}
	
	@Post
	public void carregarCamposAlteracao(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, String sortname, int page, int rp) {
		
		List<Fornecedor> listaFornecedoresAtivos = fornecedorService.obterFornecedoresAtivos();
		
		
		//Carregará os dados apenas se o usuário selecionar uma linha do grid p/ alteração.
		if(filtroAlteracaoCotaDTO != null && filtroAlteracaoCotaDTO.getListaLinhaSelecao() != null && filtroAlteracaoCotaDTO.getListaLinhaSelecao().size() == 1){
			
			List<Fornecedor> listFornecedoresCota = new ArrayList<Fornecedor>();
			
			String idCotaStr = filtroAlteracaoCotaDTO.getListaLinhaSelecao().get(0);
			
			Cota cota = cotaService.obterPorId(new Long(idCotaStr));
			
			if(idCotaStr != null && !"".equals(idCotaStr)){
				listFornecedoresCota.addAll(fornecedorService.obterFornecedoresCota(new Long(idCotaStr)));
				removerFornecedorAssociadoLista(listFornecedoresCota, listaFornecedoresAtivos);
			}
			
			filtroAlteracaoCotaDTO.getFiltroModalFornecedor().setListFornecedores(listaFornecedoresAtivos);
			filtroAlteracaoCotaDTO.getFiltroModalFornecedor().setListaFornecedorAssociado(listFornecedoresCota);
			
			filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setIsSugereSuspensao(cota.isSugereSuspensao());
			
			preencherFiltroFinanceiro(filtroAlteracaoCotaDTO, cota);
			preencherFiltroDistribuicao(filtroAlteracaoCotaDTO, cota);

			
		}else{
			filtroAlteracaoCotaDTO.getFiltroModalFornecedor().setListFornecedores(listaFornecedoresAtivos);
		}

		
		result.use(CustomMapJson.class).put("filtroAlteracaoCotaDTO", filtroAlteracaoCotaDTO).serialize();
	}
	
	@Post
	public void salvarAlteracao(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, String sortname, int page, int rp) {
		
		for(String idCota : filtroAlteracaoCotaDTO.getListaLinhaSelecao()){
			//****FORNECEDORES****//
			//Encontra Cota a Ser Alterada
			Cota cota = cotaService.obterPorId(new Long(idCota));
			
			//Altera Fornecedores da Cota
			Set<Fornecedor> fornecedoresCota = new HashSet<Fornecedor>();
			//fornecedoresCota.add(fornecedorService.obterFornecedorPorId(new Long(2)));
			for (Long  id : filtroAlteracaoCotaDTO.getFiltroModalFornecedor().getListaFornecedoresSelecionados()){
				fornecedoresCota.add(fornecedorService.obterFornecedorPorId(id));
			}
			cota.setFornecedores(fornecedoresCota);

			//****FINANCEIRO****//
			//Sugere Suspensao
			cota.setSugereSuspensao(filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().getIsSugereSuspensao());
			//Fator Vencimento
			cota.getParametroCobranca().setFatorVencimento(filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().getIdVencimento());
			try {
			//Valor Minimo
			cota.getParametroCobranca().setValorMininoCobranca(new BigDecimal(filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().getVrMinimo()));
			//Suspensao = true -> Cria Politica de Suspensao
			
			if (cota.isSugereSuspensao()){
				PoliticaSuspensao politicaSuspensao = new PoliticaSuspensao();
				politicaSuspensao.setNumeroAcumuloDivida(filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().getQtdDividaEmAberto());	
				politicaSuspensao.setValor(new BigDecimal(filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().getVrDividaEmAberto()));	
				cota.getParametroCobranca().setPoliticaSuspensao(politicaSuspensao);
			}
			
			} catch (NumberFormatException e) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Valor inválido");
			}
			//****DISTRIBUICAO****//
			cota.getParametroDistribuicao().setAssistenteComercial(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getNmAssitPromoComercial());
			cota.getParametroDistribuicao().setGerenteComercial(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getNmGerenteComercial());
			cota.getParametroDistribuicao().setObservacao(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getObservacao());
			cota.getParametroDistribuicao().setDescricaoTipoEntrega(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getDescricaoTipoEntrega());

			cota.getParametroDistribuicao().setRepartePorPontoVenda(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getIsRepartePontoVenda());
			cota.getParametroDistribuicao().setSolicitaNumAtras(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getIsSolicitacaoNumAtrasoInternet());
			cota.getParametroDistribuicao().setRecebeRecolheParciais(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getIsRecebeRecolheProdutosParciais());
			
			//--Emissao Documentos
			cota.getParametroDistribuicao().setSlipEmail(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsSlipEmail());
			cota.getParametroDistribuicao().setSlipImpresso(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsSlipImpresso());
			cota.getParametroDistribuicao().setBoletoEmail(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsBoletoEmail());
			cota.getParametroDistribuicao().setBoletoImpresso(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsBoletoImpresso());
			cota.getParametroDistribuicao().setBoletoSlipEmail(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsBoletoSlipEmail());
			cota.getParametroDistribuicao().setBoletoSlipImpresso(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsBoletoSlipImpresso());
			cota.getParametroDistribuicao().setReciboEmail(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsReciboEmail());
			cota.getParametroDistribuicao().setReciboImpresso(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsReciboImpresso());
			cota.getParametroDistribuicao().setNotaEnvioEmail(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsNotaEnvioEmail());
			cota.getParametroDistribuicao().setNotaEnvioImpresso(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsNotaEnvioImpresso());
			cota.getParametroDistribuicao().setChamadaEncalheEmail(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsChamdaEncalheEmail());
			cota.getParametroDistribuicao().setChamadaEncalheImpresso(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsChamdaEncalheImpresso());
			
			
			
			
			cotaService.alterarCota(cota);
			parametroCobrancaCotaService.alterarParametro(cota.getParametroCobranca());
		}
		
		throw new ValidacaoException(TipoMensagem.SUCCESS, "Cota alterada com sucesso.");
		
	}
	
	
	public void preencherFiltroFinanceiro(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, Cota cota){
		
		//FINANCEIRO
		if(cota.getParametroCobranca().getFatorVencimento() != null)
			filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setIdVencimento(cota.getParametroCobranca().getFatorVencimento());
		
		if(cota.getParametroCobranca().getValorMininoCobranca() != null)
			filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setVrMinimo(String.valueOf(cota.getParametroCobranca().getValorMininoCobranca()));
		if(cota.getParametroCobranca().getPoliticaSuspensao() != null){
			if(cota.getParametroCobranca().getPoliticaSuspensao().getNumeroAcumuloDivida() != null)
				filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setQtdDividaEmAberto(cota.getParametroCobranca().getPoliticaSuspensao().getNumeroAcumuloDivida());
		
			if(cota.getParametroCobranca().getPoliticaSuspensao().getValor() != null)
				filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setVrDividaEmAberto(String.valueOf(cota.getParametroCobranca().getPoliticaSuspensao().getValor()));
		}
	}
	
	public void preencherFiltroDistribuicao(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, Cota cota){
		
		if (cota.getParametroDistribuicao() == null){
			return;
		}
		
		//DISTRIBUICAO
		if(cota.getParametroDistribuicao().getAssistenteComercial() != null)
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setNmAssitPromoComercial(cota.getParametroDistribuicao().getAssistenteComercial());
		
		if(cota.getParametroDistribuicao().getGerenteComercial() != null)
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setNmGerenteComercial(cota.getParametroDistribuicao().getGerenteComercial());
		
		if(cota.getParametroDistribuicao().getObservacao() != null)
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setObservacao(cota.getParametroDistribuicao().getObservacao());
		
		if(cota.getParametroDistribuicao().getRepartePorPontoVenda() != null)
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setIsRepartePontoVenda(cota.getParametroDistribuicao().getRepartePorPontoVenda());
		
		if(cota.getParametroDistribuicao().getSolicitaNumAtras() != null)
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setIsSolicitacaoNumAtrasoInternet(cota.getParametroDistribuicao().getSolicitaNumAtras());
		
		if(cota.getParametroDistribuicao().getRecebeRecolheParciais() != null)
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setIsRecebeRecolheProdutosParciais(cota.getParametroDistribuicao().getRecebeRecolheParciais());
		
		if(cota.getParametroDistribuicao().getDescricaoTipoEntrega() != null)
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setDescricaoTipoEntrega(cota.getParametroDistribuicao().getDescricaoTipoEntrega());
		
		//--Emissao Documentos
		if(cota.getParametroDistribuicao().getSlipImpresso() != null)
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsSlipImpresso(cota.getParametroDistribuicao().getSlipImpresso());
		
		if(cota.getParametroDistribuicao().getSlipEmail() != null)
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsSlipEmail(cota.getParametroDistribuicao().getSlipEmail());
		
		if(cota.getParametroDistribuicao().getBoletoImpresso() != null)
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsBoletoImpresso(cota.getParametroDistribuicao().getBoletoImpresso());
		
		if(cota.getParametroDistribuicao().getBoletoEmail() != null)
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsBoletoEmail(cota.getParametroDistribuicao().getBoletoEmail());
		
		if(cota.getParametroDistribuicao().getBoletoSlipImpresso() != null)
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsBoletoSlipImpresso(cota.getParametroDistribuicao().getBoletoSlipImpresso());
		
		if(cota.getParametroDistribuicao().getBoletoSlipEmail() != null)
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsBoletoSlipEmail(cota.getParametroDistribuicao().getBoletoSlipEmail());
		
		if(cota.getParametroDistribuicao().getReciboImpresso() != null)
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsReciboImpresso(cota.getParametroDistribuicao().getReciboImpresso());
		
		if(cota.getParametroDistribuicao().getReciboEmail() != null)
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsReciboEmail(cota.getParametroDistribuicao().getReciboEmail());
		
		if(cota.getParametroDistribuicao().getNotaEnvioImpresso() != null)
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsNotaEnvioImpresso(cota.getParametroDistribuicao().getNotaEnvioImpresso());
		
		if(cota.getParametroDistribuicao().getNotaEnvioEmail() != null)
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsNotaEnvioEmail(cota.getParametroDistribuicao().getNotaEnvioEmail());
		
		if(cota.getParametroDistribuicao().getChamadaEncalheImpresso() != null)
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsChamdaEncalheImpresso(cota.getParametroDistribuicao().getChamadaEncalheImpresso());
		
		if(cota.getParametroDistribuicao().getChamadaEncalheEmail() != null)
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsChamdaEncalheEmail(cota.getParametroDistribuicao().getChamadaEncalheEmail());
		
	}
	

	public static void main(String[] args) {
		Fornecedor f = new Fornecedor();
		f.setId(1l);
		
		Fornecedor f2 = new Fornecedor();
		f2.setId(2l);

		Fornecedor f3 = new Fornecedor();
		f3.setId(3l);
		
		List<Fornecedor> listaFornecedor = new ArrayList<Fornecedor>();
		listaFornecedor.add(f);
		listaFornecedor.add(f2);
		listaFornecedor.add(f3);
		
		List<Fornecedor> listaFornecedorCota = new ArrayList<Fornecedor>();
		listaFornecedorCota.add(f);
		listaFornecedorCota.add(f2);
		
		removerFornecedorAssociadoLista(listaFornecedorCota, listaFornecedor);
		
		System.out.println("OK");
	}
	
	private static void removerFornecedorAssociadoLista(List<Fornecedor> listFornecedoresCota, List<Fornecedor> listFornecedores) {
		
		if(listFornecedoresCota != null && listFornecedoresCota.size() > 0 && listFornecedores != null && listFornecedores.size() > 0){
			
			for(int i = 0; i < listFornecedoresCota.size(); i++){
				Fornecedor fornecedorCota = listFornecedoresCota.get(i);
				if(fornecedorCota!= null){
					
					if(listFornecedores.size() == 0)
						break;
					
					for(int j = 0; j < listFornecedores.size(); j++){
						Fornecedor fornecedor = listFornecedores.get(j);
						if(fornecedor!= null){
							
							if(fornecedor.getId().compareTo(fornecedorCota.getId()) == 0){
								listFornecedores.remove(j);
								break;
							}
						}
					}
				}
			}
		}
	}
	
	
	@Post
	public void uploadTermoAdesao(UploadedFile uploadedFileTermo, Integer numCotaUpload) throws IOException {		
		upload(uploadedFileTermo, numCotaUpload, TipoParametroSistema.PATH_TERMO_ADESAO);
	}
	
	@Post
	public void uploadProcuracao(UploadedFile uploadedFileProcuracao, Integer numCotaUpload) throws IOException {		
		upload(uploadedFileProcuracao, numCotaUpload, TipoParametroSistema.PATH_PROCURACAO);
	}
	
	private void upload(UploadedFile uploadedFile, Integer numCota, TipoParametroSistema parametroPath ) throws IOException {		
		
		String fileName = "";
		
		if(uploadedFile != null) {
			
			this.fileService.validarArquivo(1, uploadedFile, extensoesAceitas);
			
			ParametroSistema raiz = 
					this.parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_ARQUIVOS_DISTRIBUICAO_COTA);
			
			ParametroSistema path = 
					this.parametroSistemaService.buscarParametroPorTipoParametro(parametroPath);								
			
			String dirBase = (raiz.getValor() + path.getValor() + numCota.toString() ).replace("\\", "/");
			
			fileService.setArquivoTemp(dirBase, uploadedFile.getFileName(), uploadedFile.getFile());
			
			fileName = uploadedFile.getFileName();
		}
		
		this.result.use(PlainJSONSerialization.class)
			.from(fileName, "result").recursive().serialize();
	}
}
