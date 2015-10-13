package br.com.abril.nds.controllers.administracao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.DiretorioTransferenciaArquivoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.TransferenciaArquivoDownloadDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiretorioTransferenciaArquivo;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.TransferenciaArquivoService;
import br.com.abril.nds.service.integracao.ParametroSistemaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.FileDownload;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/administracao/transferenciaArquivos")
@Rules(Permissao.ROLE_ADMINISTRACAO_REPOSITORIO)
public class TransferenciaArquivosController extends BaseController {

	private Result result;
	
	public TransferenciaArquivosController(Result result) {
		this.result = result;
	}
	
	@Autowired
	private TransferenciaArquivoService transferenciaArquivoService;
	
	@Autowired
	private ParametroSistemaService parametroSistemaService; 
	
	@Path("/")
	public void index(){
		this.carregarComboDiretorios();
	}
	
	@Post
	@Path("/adicionarDiretorio")
    @Rules(Permissao.ROLE_DISTRIBUICAO_REGIAO_ALTERACAO)
	public void adicionarDiretorio (String nomeDiretorio, String pastaDiretorio){

		DiretorioTransferenciaArquivo diretorio = new DiretorioTransferenciaArquivo();
		
		ParametroSistema path = this.parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_TRANSFERENCIA_ARQUIVO);
		
		String pathFile = path.getValor();
		
		if(!pastaDiretorio.substring(0, 1).equals("/")){
			pastaDiretorio = "/"+pastaDiretorio;
		}

		if(!pathFile.endsWith("/")){
			pathFile = pathFile+pastaDiretorio;
		}else{
			pathFile = pathFile+pastaDiretorio.substring(1);
		}
		
		diretorio.setNomeDiretorio(nomeDiretorio);
		diretorio.setEnderecoDiretorio(pathFile);
		
		transferenciaArquivoService.salvarDiretorio(diretorio);
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Diretório inserido com sucesso!"),"result").recursive().serialize();
		
	}
	
	@Post
	@Path("/excluirDiretorio")
    @Rules(Permissao.ROLE_DISTRIBUICAO_REGIAO_ALTERACAO)
	public void excluirDiretorio (Long idDiretorio){

		transferenciaArquivoService.excluirDiretorio(idDiretorio);
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Diretório excluído com sucesso!"),"result").recursive().serialize();
		
	}
	
	@Post
	public void carregarComboDiretorios() {

		List<ItemDTO<Long,String>> comboDiretorios =  new ArrayList<ItemDTO<Long,String>>();
		List<DiretorioTransferenciaArquivoDTO> listaDiretorioTransferenciaArquivoDTO = transferenciaArquivoService.obterTodosDiretorios(null);

		for (DiretorioTransferenciaArquivoDTO diretorio : listaDiretorioTransferenciaArquivoDTO) {
			comboDiretorios.add(new ItemDTO<Long, String>(diretorio.getIdDiretorio(), diretorio.getNomeDiretorio()));
		}
		
		result.include("listaDiretorios",comboDiretorios);
	}
	
	@Post
	@Path("/carregarDiretorios")
	public void carregarDiretorios (String sortorder, String sortname, int page, int rp){

		DiretorioTransferenciaArquivoDTO dto = new DiretorioTransferenciaArquivoDTO();
		
		dto.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		TableModel<CellModelKeyValue<DiretorioTransferenciaArquivoDTO>> tableModel = efetuarConsultaDiretorios(dto);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();

	}
	
	private TableModel<CellModelKeyValue<DiretorioTransferenciaArquivoDTO>> efetuarConsultaDiretorios (DiretorioTransferenciaArquivoDTO filtro) {
		
		List<DiretorioTransferenciaArquivoDTO> listaDiretorioTransferenciaArquivoDTO = transferenciaArquivoService.obterTodosDiretorios(filtro);
		
		TableModel<CellModelKeyValue<DiretorioTransferenciaArquivoDTO>> tableModel = new TableModel<CellModelKeyValue<DiretorioTransferenciaArquivoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDiretorioTransferenciaArquivoDTO));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());

		return tableModel;
	}
	
	@Post
	@Path("/uploadArquivo")
	public void uploadArquivo(UploadedFile file, Long idDiretorio) throws FileNotFoundException, IOException{
		
		if(idDiretorio == null || idDiretorio.compareTo(0L) <= 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "Diretório inválido, selecione outro diretório.");
		}
			
			DiretorioTransferenciaArquivo diretorio = transferenciaArquivoService.buscarPorId(idDiretorio);
			
			String nomeArquivo = transferenciaArquivoService.upload(file, diretorio.getEnderecoDiretorio());
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Arquivo ["+nomeArquivo+"] enviado com sucesso!"),"result").recursive().serialize();
	}
	
	@Post
	@Path("/excluirArquivo")
	public void excluirArquivo(String pathFile, String nomeArquivo){
		
		File diretorioFile = new File(pathFile, nomeArquivo);
		
		if(diretorioFile.getName() != null){
			diretorioFile.setWritable(true);
			diretorioFile.delete();
		}else{
			throw new ValidacaoException(TipoMensagem.WARNING, "Erro ao excluir o arquivo.");
		}
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Arquivo excluído com sucesso!"),"result").recursive().serialize();
	}
	
	@Get
	@Path("/downloadArquivo")
	public Download downloadArquivo(String pathFile, String nomeArquivo)throws Exception {
		
		File diretorioFile = new File(pathFile, nomeArquivo);
		
		String contentType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(diretorioFile);
		String filename = diretorioFile.getName();
		
		return new FileDownload(diretorioFile, contentType, filename);
					
	}
	
	@Post
	@Path("/buscarArquivosDownload")
	public void buscarArquivosDownload(Long idDiretorio) throws FileNotFoundException, IOException{
		
		if(idDiretorio == null || idDiretorio.compareTo(0L) <= 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "Diretório inválido, selecione outro diretório.");
		}
			DiretorioTransferenciaArquivo diretorio = transferenciaArquivoService.buscarPorId(idDiretorio);
			
			File diretorioFile = new File(diretorio.getEnderecoDiretorio());
			
			if(diretorioFile.listFiles() == null || diretorioFile.listFiles().length == 0){
				throw new ValidacaoException(TipoMensagem.WARNING, "Não há arquivos no diretório.");
			}

			List<TransferenciaArquivoDownloadDTO> listArquivos = new ArrayList<>();
			
			for (File file : diretorioFile.listFiles()) {
				
				TransferenciaArquivoDownloadDTO novoArquivo = new TransferenciaArquivoDownloadDTO();
				
				novoArquivo.setNomeArquivo(file.getName());
				novoArquivo.setPathAbsolutoArquivo(file.getPath());
				novoArquivo.setPathArquivo(file.getParent());
				novoArquivo.setTamanhoArquivo((file.length()/1024));
				novoArquivo.setUltimaModificacao(file.lastModified());

				listArquivos.add(novoArquivo);
		 	}

			TableModel<CellModelKeyValue<TransferenciaArquivoDownloadDTO>> tableModel = formatarArquivosDiretoriosDownload(listArquivos);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}

	private TableModel<CellModelKeyValue<TransferenciaArquivoDownloadDTO>> formatarArquivosDiretoriosDownload(List<TransferenciaArquivoDownloadDTO> listArquivos) {
		
		TableModel<CellModelKeyValue<TransferenciaArquivoDownloadDTO>> tableModel = new TableModel<CellModelKeyValue<TransferenciaArquivoDownloadDTO>>();
	
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listArquivos));
	
		tableModel.setPage(1);
	
		tableModel.setTotal(listArquivos.size());
	
		return tableModel;
	}
}
