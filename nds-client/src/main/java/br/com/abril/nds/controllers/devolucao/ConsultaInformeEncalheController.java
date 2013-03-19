package br.com.abril.nds.controllers.devolucao;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.lightcouch.NoDocumentException;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ColunaRelatorioInformeEncalhe;
import br.com.abril.nds.dto.InformeEncalheDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.TipoImpressaoInformeEncalheDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CapaService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

/**
 * 
 * Classe responsável por controlas as ações da pagina de Informe de
 * Recolhimentos.
 * 
 * @author Discover Technology
 * 
 */
@Resource
@Path(value = "/devolucao/informeEncalhe")
public class ConsultaInformeEncalheController extends BaseController {

	@Autowired
	private Result result;

	@Autowired
	private LancamentoService lancamentoService;

	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired 
	private CapaService capaService;	
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private CalendarioService calendarioService;
	
	private DiaSemana inicioDaSemana;
	
	
	private ConsultaInformeEncalheController(DistribuidorService distribuidorService){
		inicioDaSemana = distribuidorService.inicioSemana();
	}

	@Get("/")
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONSULTA_INFORME_ENCALHE)
	public void index() {
		result.include("fornecedores", fornecedorService
				.obterFornecedoresIdNome(SituacaoCadastro.ATIVO, true));
	}

	@Post("/busca.json")
	public void busca(Long idFornecedor, Integer semanaRecolhimento,
			Calendar dataRecolhimento, String sortname, String sortorder,
			int rp, int page) {
		Calendar dataInicioRecolhimento = null, dataFimRecolhimento = null;

		if ((semanaRecolhimento == null) && (dataRecolhimento == null)) {

			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,
					"Informe [Semana] ou [Data Recolhimento]"));
		}

		if (semanaRecolhimento != null) {
			dataInicioRecolhimento = Calendar.getInstance();

			if (semanaRecolhimento > dataInicioRecolhimento
					.getMaximum(Calendar.WEEK_OF_YEAR)) {
				throw new ValidacaoException(new ValidacaoVO(
						TipoMensagem.WARNING, "Semana inválida."));
			}

			dataInicioRecolhimento.set(Calendar.WEEK_OF_YEAR,
					semanaRecolhimento);
			
			dataInicioRecolhimento.set(Calendar.DAY_OF_WEEK, inicioDaSemana.getCodigoDiaSemana());
			dataFimRecolhimento = (Calendar) dataInicioRecolhimento.clone();
			dataFimRecolhimento.add(Calendar.DAY_OF_MONTH, 6);

		} else if (dataRecolhimento != null) {
			dataInicioRecolhimento = dataRecolhimento;
			dataFimRecolhimento = obterDataFimRecolhimento(dataInicioRecolhimento);
			
		}
			
		Long quantidade = lancamentoService
				.quantidadeLancamentoInformeRecolhimento(idFornecedor,
						dataInicioRecolhimento, dataFimRecolhimento);
		if (quantidade > 0) {
			List<InformeEncalheDTO> informeEncalheDTOs = lancamentoService
					.obterLancamentoInformeRecolhimento(idFornecedor,
							dataInicioRecolhimento, dataFimRecolhimento,
							sortname,
							Ordenacao.valueOf(sortorder.toUpperCase()), page
									* rp - rp, rp);
			
			result.use(FlexiGridJson.class).from(informeEncalheDTOs)
					.total(quantidade.intValue()).page(page).serialize();
		}else{
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,
					"Registros não encontrados."));
		}
	}

	private Calendar obterDataFimRecolhimento(Calendar dataInicioRecolhimento) {
		Calendar dataFimRecolhimento;
		dataFimRecolhimento = Calendar.getInstance();
		int maxDiaRecolhimento = obterMaxDiaRecolhimentoDistribuidor();
		dataFimRecolhimento.setTime(this.calendarioService.adicionarDiasUteis(dataInicioRecolhimento.getTime(), maxDiaRecolhimento));
		return dataFimRecolhimento;
	}

	
	@Post
	public void relatorioInformeEncalhe(Long idFornecedor, Integer semanaRecolhimento,
			Calendar dataRecolhimento,
			TipoImpressaoInformeEncalheDTO tipoImpressao, String sortorder){

		final String sortname = "sequenciaMatriz";
		
		Calendar dataInicioRecolhimento = null, dataFimRecolhimento = null;
		
		if (tipoImpressao == null || tipoImpressao.getColunas() == null || tipoImpressao.getColunas().isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Parâmetro inválido para geração do relaório.");
		}
		
		if ((semanaRecolhimento == null) && (dataRecolhimento == null)) {

			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,
					"Informe [Semana] ou [Data Recolhimento]"));
		}
			
		int maxDiaSemanaRecolhimento = obterMaxDiaRecolhimentoDistribuidor();
		
		Calendar dataImpressãoFimRecolhimento = Calendar.getInstance();

		
		if (semanaRecolhimento != null) {
			dataInicioRecolhimento = Calendar.getInstance();

			if (semanaRecolhimento > dataInicioRecolhimento
					.getMaximum(Calendar.WEEK_OF_YEAR)) {
				throw new ValidacaoException(new ValidacaoVO(
						TipoMensagem.WARNING, "Semana inválida."));
			}

			dataInicioRecolhimento.set(Calendar.WEEK_OF_YEAR,
					semanaRecolhimento);
			
			dataInicioRecolhimento.set(Calendar.DAY_OF_WEEK, inicioDaSemana.getCodigoDiaSemana());
			dataFimRecolhimento = Calendar.getInstance();
			dataFimRecolhimento.setTime(this.calendarioService.adicionarDiasUteis(dataInicioRecolhimento.getTime(), (maxDiaSemanaRecolhimento-1)));
			dataImpressãoFimRecolhimento = dataFimRecolhimento;
			
		} else if (dataRecolhimento != null) {
			dataInicioRecolhimento = dataRecolhimento;
			dataFimRecolhimento = dataInicioRecolhimento;
			dataImpressãoFimRecolhimento = obterDataFimRecolhimento(dataInicioRecolhimento);
		}

		List<InformeEncalheDTO> dados = lancamentoService
				.obterLancamentoInformeRecolhimento(idFornecedor,
						dataInicioRecolhimento, dataFimRecolhimento, sortname,
						Ordenacao.valueOf(sortorder.toUpperCase()), null, null);
		
		this.result.include("diaMesInicioRecolhimento", 1);
		this.result.include("dataInicioRecolhimento", new SimpleDateFormat("dd/MM").format(dataInicioRecolhimento.getTime()));
		this.result.include("diaSemanaInicioRecolhimento", DateUtil.obterDiaSemana(dataInicioRecolhimento.get(Calendar.DAY_OF_WEEK)));
		
		this.result.include("diaMesFimRecolhimento", maxDiaSemanaRecolhimento);
		this.result.include("dataFimRecolhimento", new SimpleDateFormat("dd/MM").format(dataImpressãoFimRecolhimento.getTime()));
		this.result.include("diaSemanaFimRecolhimento", DateUtil.obterDiaSemana(dataImpressãoFimRecolhimento.get(Calendar.DAY_OF_WEEK)));
		
		List<ColunaRelatorioInformeEncalhe> colunas = new ArrayList<ColunaRelatorioInformeEncalhe>();
		
		int qtdColunas = tipoImpressao.getColunas().size() == 0 ? 1 : tipoImpressao.getColunas().size();
		
		if (tipoImpressao != null && tipoImpressao.getColunas() != null){
			
			if (tipoImpressao.getColunas().contains("sequenciaMatriz")){
				
				colunas.add(new ColunaRelatorioInformeEncalhe("Sequência", 
						this.calcularTamanhoColunaRelatorio(qtdColunas, 4), 
						"sequenciaMatriz"));
			}
			
			if (tipoImpressao.getColunas().contains("codigoProduto")){
				
				colunas.add(new ColunaRelatorioInformeEncalhe("Código", 
						this.calcularTamanhoColunaRelatorio(qtdColunas, 4), 
						"codigoProduto"));
			}
			
			if (tipoImpressao.getColunas().contains("nomeProduto")){
				
				colunas.add(new ColunaRelatorioInformeEncalhe("Produto", 
						this.calcularTamanhoColunaRelatorio(qtdColunas, 13), 
						"nomeProduto"));
			}
			
			if (tipoImpressao.getColunas().contains("numeroEdicao")){
				
				colunas.add(new ColunaRelatorioInformeEncalhe("ED.", 
						this.calcularTamanhoColunaRelatorio(qtdColunas, 4),
						"numeroEdicao"));
			}
			
			if (tipoImpressao.getColunas().contains("chamadaCapa")){
				
				colunas.add(new ColunaRelatorioInformeEncalhe("Chamada de Capa", 
						this.calcularTamanhoColunaRelatorio(qtdColunas, 10), 
						"chamadaCapa"));
			}
			
			if (tipoImpressao.getColunas().contains("codigoDeBarras")){
				
				colunas.add(new ColunaRelatorioInformeEncalhe("Código de Barras", 
						this.calcularTamanhoColunaRelatorio(qtdColunas, 9),
						"codigoDeBarras"));
			}
			
			if (tipoImpressao.getColunas().contains("precoVenda")){
				
				colunas.add(new ColunaRelatorioInformeEncalhe("R$ Capa", 
						this.calcularTamanhoColunaRelatorio(qtdColunas, 4),
						"precoVenda"));
			}
			
			if (tipoImpressao.getColunas().contains("nomeEditor")){
				
				colunas.add(new ColunaRelatorioInformeEncalhe("Editor", 
						this.calcularTamanhoColunaRelatorio(qtdColunas, 14), 
						"nomeEditor"));
			}
			
			if (tipoImpressao.getColunas().contains("brinde")){
				
				colunas.add(new ColunaRelatorioInformeEncalhe("Brinde", 
						this.calcularTamanhoColunaRelatorio(qtdColunas, 4), 
						"brinde"));
			}
			
			if (tipoImpressao.getColunas().contains("dataLancamento")){
				
				colunas.add(new ColunaRelatorioInformeEncalhe("Lanc.", 
						this.calcularTamanhoColunaRelatorio(qtdColunas, 4),
						"dataLancamento"));
			}
			
			if (tipoImpressao.getColunas().contains("dataRecolhimento")){
				
				colunas.add(new ColunaRelatorioInformeEncalhe("Rec.",
						this.calcularTamanhoColunaRelatorio(qtdColunas, 4),
						"dataRecolhimento"));
			}
			
			if (tipoImpressao.getColunas().contains("tipoLancamentoParcial")){
				
				colunas.add(new ColunaRelatorioInformeEncalhe("TR",
						this.calcularTamanhoColunaRelatorio(qtdColunas, 3), 
						"tipoLancamentoParcial"));
			}
		}
		
		String nomeDistribuidor = this.distribuidorService.obterRazaoSocialDistribuidor();
		
		this.result.include("nomeDistribuidor", nomeDistribuidor);
		
		this.result.include("colunas", colunas);
		
		int qtdReg = 0;
		
		int quebra = 30;
		
		int indexImg = 0;
		
		int qtdImg = 30;
		
		int imgAdd = 0;
		
		List<InformeEncalheDTO> listaResult = new ArrayList<InformeEncalheDTO>();
		
		for (InformeEncalheDTO info : dados){
			
			listaResult.add(info);
			qtdReg++;
			
			if (qtdReg == quebra){
				
				qtdReg = 0;
								
				switch (tipoImpressao.getCapas()) {
				
					case NAO:
						
						InformeEncalheDTO dto = new InformeEncalheDTO();
						dto.setImagem(true);
						dto.setSequenciaMatriz(info.getSequenciaMatriz());
						listaResult.add(dto);					
						break;
										
					case PAR:
						
						for (int i = indexImg ; i < indexImg + qtdImg ; i++){
							
							InformeEncalheDTO img = new InformeEncalheDTO();
							img.setImagem(true);
							
							if (i < dados.size()){
							
								img.setIdProdutoEdicao(dados.get(i).getIdProdutoEdicao());
								img.setSequenciaMatriz(dados.get(i).getSequenciaMatriz());
							}
							
							listaResult.add(img);
							
							imgAdd++;
						}
						
						indexImg += qtdImg;
						break;
				}
				
			}
		}
		
		while (qtdReg != quebra && qtdReg > 0){
			
			InformeEncalheDTO inDto = new InformeEncalheDTO();
			listaResult.add(inDto);
			qtdReg++;
		}
		
		if (!TipoImpressaoInformeEncalheDTO.Capas.NAO.equals(tipoImpressao.getCapas())){
			
			if (TipoImpressaoInformeEncalheDTO.Capas.PAR.equals(tipoImpressao.getCapas())){
			
				for (int i = indexImg ; i < indexImg + qtdImg ; i++){
					
					InformeEncalheDTO img = new InformeEncalheDTO();
					img.setImagem(true);
					
					if (i < dados.size()){
					
						img.setIdProdutoEdicao(dados.get(i).getIdProdutoEdicao());
						img.setSequenciaMatriz(dados.get(i).getSequenciaMatriz());
					}
					
					listaResult.add(img);
					
					imgAdd++;
				}
				
				indexImg += qtdImg;
			}
		} else {
			
			InformeEncalheDTO dt = new InformeEncalheDTO();
			dt.setImagem(true);
			listaResult.add(dt);
		}
		
		if(!TipoImpressaoInformeEncalheDTO.Capas.NAO.equals(tipoImpressao.getCapas())){
		
			if (TipoImpressaoInformeEncalheDTO.Capas.PAR.equals(tipoImpressao.getCapas())){
			
				while (imgAdd % qtdImg != 0){
					
					InformeEncalheDTO inDto = new InformeEncalheDTO();
					inDto.setImagem(true);
					listaResult.add(inDto);
					
					imgAdd++;
				}
			
			} else {
				
				int sizeList = listaResult.size();
				
				for (int index = 0 ; index < sizeList ; index++){
					
					if (listaResult.get(index).getIdProdutoEdicao() == null){
						
						continue;
					}
					
					InformeEncalheDTO inDto = new InformeEncalheDTO();
					inDto.setImagem(true);
					inDto.setIdProdutoEdicao(listaResult.get(index).getIdProdutoEdicao());
					inDto.setSequenciaMatriz(listaResult.get(index).getSequenciaMatriz());
					listaResult.add(inDto);
					
					imgAdd++;
					
					if (imgAdd % qtdImg == 0){
						
						listaResult.add(null);
					}
				}
			}
		}
		
		this.result.include("dados", listaResult);
	}
	
	private int calcularTamanhoColunaRelatorio(int qtdColunas, int porcentual){
		
		int tamanhoTotalTable = 1000;
		
		return tamanhoTotalTable / qtdColunas * porcentual / 100;
	}

	private int obterMaxDiaRecolhimentoDistribuidor() {
		return this.distribuidorService.obterOrdinalUltimoDiaRecolhimento();
	}
	
	/**
	 * Prepara lista de capas com sequencia na lista de InformeEncalheDTO
	 * @param informeEncalheDTOs
	 * @throws IOException
	 */
	private List<List<ItemDTO<Integer, byte[]>>> preparaDadosImpressao(
			List<InformeEncalheDTO> informeEncalheDTOs) throws IOException {
		List<List<ItemDTO<Integer, byte[]>>> capas = new ArrayList<List<ItemDTO<Integer, byte[]>>>();
		int quantidadeLinha = 5;
		List<ItemDTO<Integer, byte[]>> linha = null;
		int seqCapa = 0;
		for (InformeEncalheDTO informeEncalheDTO : informeEncalheDTOs) {			
			if(linha == null  ||  linha.size() == quantidadeLinha){
				linha = new ArrayList<ItemDTO<Integer, byte[]>>(quantidadeLinha);
				capas.add(linha);
			}
			
			try {
				InputStream inputStream = capaService.getCapaInputStream(
						informeEncalheDTO.getCodigoProduto(),
						informeEncalheDTO.getNumeroEdicao());
				byte[] foto = IOUtils.toByteArray(inputStream);
				
				seqCapa++;
				linha.add(new ItemDTO<Integer, byte[]>(seqCapa, foto));
				informeEncalheDTO.setSeqCapa(seqCapa);
			} catch (NoDocumentException e) {
				continue;
			}
			
			
		}
		
		return capas;
	}

}

