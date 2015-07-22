package br.com.abril.nds.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CertificadoNFEDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.fiscal.nota.Certificado;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CertificadoRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.CerfiticadoService;
import br.com.abril.nds.service.FileService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.service.integracao.ParametroSistemaService;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;

/**
 * @author InfoA2
 */
@Service
public class CertificadoServiceImpl implements CerfiticadoService {

	@Autowired
    private FileService fileService;
	
    @Autowired
    private ParametroSistemaService parametroSistemaService;
    
    @Autowired
    private DistribuidorService distribuidorService;
    
    @Autowired
    private CertificadoRepository certificadoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    private static final String DIR_TEMP = "temp/";
    
    private static final String DIR_REAL = "real/";
    
    private static final FileType[] EXTENSOES_ACEITAS = {FileType.JKS, FileType.PFX};
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CertificadoServiceImpl.class);

	@Override
	public String upload(UploadedFile uploadedFile, TipoParametroSistema parametroPath) throws IOException{
		
		LOGGER.debug("Upload do certificado digital.");
		
		String fileName = "";
        
        if (uploadedFile != null) {
            
            fileService.validarArquivo(1, uploadedFile, EXTENSOES_ACEITAS);
            
            final ParametroSistema path = parametroSistemaService.buscarParametroPorTipoParametro(parametroPath);
            
            final String dirBase = (path.getValor()).replace("\\", "/");
            
            fileService.setArquivoTemp(dirBase, uploadedFile.getFileName(), uploadedFile.getFile());
            
            fileName = uploadedFile.getFileName();
            final InputStream inputStream = uploadedFile.getFile();
            
            inputStream.close();
            
        }
		
        return fileName;
	}

	@Override
	public void limparCertificadoTemp(TipoParametroSistema parametroPath) {
		
		final ParametroSistema path = parametroSistemaService.buscarParametroPorTipoParametro(parametroPath);
		
		this.fileService.esvaziarTemp(path.getValor());
        		
	}
	
	@Override
	@Transactional
	public void confirmar(CertificadoNFEDTO filtro, Long idUsuario) {
		
		this.validarCertifcado(filtro);
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		Usuario usuario = this.usuarioRepository.buscarPorId(idUsuario);
		
		Certificado certificado = new Certificado();
		
		certificado.setDataInicio(filtro.getDataFim());
		certificado.setDataFim(filtro.getDataFim());
		certificado.setDataCriacao(new Date());
		certificado.setNomeArquivo(filtro.getNomeArquivo());
		certificado.setExtensao(filtro.getExtensao());
		certificado.setSenha(filtro.getSenha());
		certificado.setAlias(filtro.getAlias());
		certificado.setDistribuidor(distribuidor);
		certificado.setUsuario(usuario);
		
		this.certificadoRepository.adicionar(certificado);
		
		this.persistirTemporario(filtro, idUsuario);
	}

	private void persistirTemporario(CertificadoNFEDTO filtro, Long idUsuario) {
		
         final File file = filtro.getTempFile();
             
         if (file != null) {
             
             final ParametroSistema pathCertificado = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.NFE_PATH_CERTIFICADO);
                              
             final File path = new File(pathCertificado.getValor() + DIR_REAL);
             
             path.mkdirs();
             
             // fileService.limparDiretorio(path);
             
             final File novoArquivo = new File(path, file.getName());
             
             FileOutputStream fos = null;
             
             FileInputStream fis = null;
             
             try {
                 
                 fos = new FileOutputStream(novoArquivo);
                 
                 fis = new FileInputStream(file);
                 
                 IOUtils.copyLarge(fis, fos);
                 
             } catch (final IOException e) {
                 
                 LOGGER.error("Falha ao persistir contrato anexo", e);
                 
                 throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Falha ao persistir certificado anexo."));
                 
             } finally {
                 
                 if(fis != null) {
                     try {
                         fis.close();
                     } catch (final IOException e) {
                         LOGGER.error("Falha ao fechar o arquivo", e);
                     }
                 }
                 
                 if(fos != null) {
                     try {
                         fos.close();
                     } catch (final IOException e) {
                         LOGGER.error("Falha ao fechar o arquivo", e);
                     }
                 }
             }
         }
	}
	
	@SuppressWarnings("rawtypes")
	private void validarCertifcado(CertificadoNFEDTO filtro) {
		
		String nomeArquivo = filtro.getNomeArquivo();
		String ext = filtro.getNomeArquivo();
		
		filtro.setExtensao(Util.retornarExtensao(ext));
		filtro.setNomeArquivo(Util.retornarNomeArquivo(nomeArquivo));
		
		if (this.quantidade(filtro) > 0 ){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Já Existe um Certificado Cadastrado! ");
			
		}
		
		ParametroSistema parametroSistema = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.NFE_PATH_CERTIFICADO);
		
		File file = new File(parametroSistema.getValor() + DIR_TEMP + filtro.getNomeArquivo() + "." + filtro.getExtensao());
        
		X509Certificate certificate = null;

        if (file.exists()) {
        	
        	KeyStore keyStore = null;
        	
        	try {
        		if(filtro.getExtensao().equalsIgnoreCase("jks")) {
        			
        			keyStore = KeyStore.getInstance("JKS");
        			
        		} else {
        			
        			keyStore = KeyStore.getInstance("PKCS12");
        			
        		}
        		
                InputStream is = new FileInputStream(file);
                String senha = filtro.getSenha();
                keyStore.load(is, senha.toCharArray());
                is.close();

                String alias = null;

                PrivateKey privateKey = null;
                Enumeration e = keyStore.aliases();
                
                while (e.hasMoreElements()) {
                    
                	alias = (String) e.nextElement();
                    certificate = (X509Certificate) keyStore.getCertificate(alias);
                    privateKey = (PrivateKey) keyStore.getKey(alias, filtro.getSenha().toCharArray());
                    filtro.setAlias(alias);
                    
                }
                
            } catch (Exception ex) {
            	ex.getStackTrace();
                throw new ValidacaoException(TipoMensagem.WARNING, "Senha não corresponde com o certificado!");
            }
        	
        } else {
        	throw new ValidacaoException(TipoMensagem.WARNING, "Problema no diretorio do arquivo! ");
        }
        
        filtro.setTempFile(file);
	}

	@Override
	@Transactional
	public List<CertificadoNFEDTO> obterCertificado(CertificadoNFEDTO filtro) {
		return this.certificadoRepository.obterCertificado(filtro);
	}

	@Override
	@Transactional
	public Long quantidade(CertificadoNFEDTO filtro) {
		return this.certificadoRepository.quantidade(filtro);
	}

	@Override
	@Transactional
	public CertificadoNFEDTO obterCertificadoId(long id) {
		
		 Certificado certificado = this.certificadoRepository.buscarPorId(id);
		 
		 CertificadoNFEDTO certificadoNFEDTO = new CertificadoNFEDTO();
		 
		 certificadoNFEDTO.setId(certificado.getId());
		 certificadoNFEDTO.setAlias(certificado.getAlias());
		 certificadoNFEDTO.setSenha(certificado.getSenha());
		 certificadoNFEDTO.setDataInicio(certificado.getDataInicio());
		 certificadoNFEDTO.setDataFim(certificado.getDataFim());
		 certificadoNFEDTO.setIdDistribuidor(certificado.getDistribuidor().getId());
		 
		 return certificadoNFEDTO;
	}

	@Override
	@Transactional
	public void remover(long id) {
		
		if(this.deletarCertificado(id)) {
			this.certificadoRepository.removerPorId(id);
		}		
	}	
	
	private boolean deletarCertificado(long id) {
		
		ParametroSistema parametroSistema = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.NFE_PATH_CERTIFICADO);
	    
		Certificado certificado = this.certificadoRepository.buscarPorId(id);
		
		File f = null;
		
	    boolean bool = false;
	    
		try {
			
			f = new File(parametroSistema.getValor() + certificado.getNomeArquivo() + "." + certificado.getExtensao());
			
			bool = f.delete();
			
		} catch (Exception e) {
			// File permission problems are caught here.
			throw new ValidacaoException(TipoMensagem.ERROR, "Problema com a permissão na pasta");
		}
		
		return bool;
	}
}