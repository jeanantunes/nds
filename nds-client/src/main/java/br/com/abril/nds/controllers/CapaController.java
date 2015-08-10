package br.com.abril.nds.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.persistence.NoResultException;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.CapaService;
import br.com.abril.nds.service.FileService;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/capa")
public class CapaController {
    
    private static final String NO_IMAGE_PATH = "/images/no_image.jpeg";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CapaController.class);
    
    private static final int TAMANHO_MAXIMO = 500 * 1024;
    
    @Autowired
    private Result result;
    
    @Autowired
    private CapaService capaService;
    
    @Autowired
    private ServletContext servletContext;
    
    @Autowired
    private FileService fileService;
    
    @Get("/{idProdutoEdicao}")
    public Download obtemCapaProdutoEdicao(final Long idProdutoEdicao) {
        try {
            final InputStream inputStream = capaService.getCapaInputStream(idProdutoEdicao);
            return new InputStreamDownload(inputStream, null, null);
            
        } catch (final Exception e) {
            LOGGER.warn("Capa nao encontrado para produto_edicao='"+idProdutoEdicao+"' " + e.getMessage(), e);
            result.nothing();
            return null;
        }
        
    }
    
    @Get("tratarNoImage/{idProdutoEdicao}")
    public Download obtemCapaProdutoEdicaoTratarNoImage(final Long idProdutoEdicao) {
        
        try {
            final InputStream inputStream = capaService.getCapaInputStream(idProdutoEdicao);
            return new InputStreamDownload(inputStream, null, null);
            
        } catch (final Exception e) {
            LOGGER.debug(e.getMessage(), e);
            try {
                return new InputStreamDownload(getNoImage(), null, null);
            } catch (final FileNotFoundException e1) {
                LOGGER.error(e1.getMessage(), e1);
                return null;
            }
        }
        
    }
    
    @Post
    public void carregarCapaTemp(final UploadedFile imagemCapa) throws IOException {
        
        try {
            fileService.validarArquivo(500, imagemCapa, FileType.JPEG, FileType.PNG, FileType.GIF);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            if (e instanceof ValidacaoException) {
                throw e;
            } else {
                throw new ValidacaoException(TipoMensagem.WARNING, "Falha ao carregar o arquivo");
            }
        }
        
        final String filePath = fileService.uploadTempFile(imagemCapa, servletContext.getRealPath(""));
        
        result.use(Results.json()).from(filePath, "result").serialize();
    }
    
    public InputStream getNoImage() throws FileNotFoundException {
        
        final File fileDir = new File(servletContext.getRealPath("") + NO_IMAGE_PATH);
        
        final InputStream inputStream = new FileInputStream(fileDir);
        
        return inputStream;
    }
    
    @Post("salvaCapa")
    public void salvaCapa(final Long idProdutoEdicao, final UploadedFile image) throws IOException {
        
        final String contentType = image.getContentType();
        
        if (!FileType.JPEG.getContentType().equalsIgnoreCase(contentType)
                && !FileType.GIF.getContentType().equalsIgnoreCase(contentType)
                && !FileType.PNG.getContentType().equalsIgnoreCase(contentType)) {
            
            result.use(CustomJson.class).from(new ValidacaoVO(TipoMensagem.WARNING, "Apenas Imagens do tipo JPEG"))
            .serialize();
            return;
        }
        
        final InputStream inputStream = image.getFile();
        final int tamanho = inputStream.available();
        if (TAMANHO_MAXIMO < tamanho) {
            result.use(CustomJson.class).from(
                    new ValidacaoVO(TipoMensagem.WARNING, "Apenas Imagens de at&eacute; 20 kbytes")).serialize();
            return;
        }
        capaService.saveCapa(idProdutoEdicao, contentType, inputStream);
        
        result.use(CustomJson.class).from("OK").serialize();
        
    }
    
    /**
     * Exclui a capa da Edição.
     * 
     * @param idProdutoEdicao
     */
    @Post
    @Path("/removerCapa")
    public void removerCapa(final long idProdutoEdicao) {
        
        ValidacaoVO vo = null;
        try {
            
            capaService.deleteCapa(idProdutoEdicao);
            vo = new ValidacaoVO(TipoMensagem.SUCCESS, "Remoção feita com sucesso!");
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            if (e instanceof ValidacaoException) {
                
                vo = ((ValidacaoException) e).getValidacao();
            } else {
                
                if (e instanceof NoResultException) {
                    vo = new ValidacaoVO(TipoMensagem.ERROR, "Não há capa cadastrada!");
                } else {
                    vo = new ValidacaoVO(TipoMensagem.ERROR, e.getMessage() + "");
                }
            }
        } finally {
            result.use(PlainJSONSerialization.class).from(vo, "result").recursive().serialize();
        }
    }
    
    @Path("/getCapaEdicaoJson")
    public File getCapaEdicaoJson(final String codigoProduto, final String numeroEdicao) {
        
        InputStream att = null;
        OutputStream out = null;
        File file = new File("temp" + FileType.JPG.getExtension());
        try {
            att = capaService.getCapaInputStream(codigoProduto, Long.parseLong(numeroEdicao));
            
            out = new FileOutputStream(file);
            
            int read = 0;
            final byte[] bytes = new byte[1024];
            
            while ((read = att.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            
            out.flush();
            
        } catch (final NumberFormatException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            file = new File(servletContext.getRealPath("") + NO_IMAGE_PATH);
        } finally {
            try {
                if (att != null) {
                    att.close();
                }
            } catch (final IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
            
            try {
                if (out != null) {
                    out.close();
                }
            } catch (final IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        
        return file;
    }
    
}
