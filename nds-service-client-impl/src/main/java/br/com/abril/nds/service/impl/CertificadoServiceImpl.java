package br.com.abril.nds.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

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
import br.com.abril.nds.util.export.FileExporter.FileType;
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
		certificado.setSenha(filtro.getSenha());
		certificado.setAlias(filtro.getAlias());
		certificado.setDistribuidor(distribuidor);
		certificado.setUsuario(usuario);
		
		this.certificadoRepository.adicionar(certificado);
		
	}

	private void validarCertifcado(CertificadoNFEDTO filtro) {

		ParametroSistema parametroSistema = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.NFE_PATH_CERTIFICADO);
       
		File file = new File(parametroSistema.getValor()+ "temp/" + filtro.getNomeArquivo());
        
		X509Certificate certificate = null;

        if (file.exists()) {
        	try {
                KeyStore keyStore = KeyStore.getInstance("PKCS12");
                InputStream is = new FileInputStream(file);
                String senha = "arsp15";
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
                    System.out.println(certificate + " " + privateKey.getAlgorithm());
                }
            } catch (Exception ex) {
                throw new ValidacaoException(TipoMensagem.WARNING, "Senha não corresponde com o certificado!");
            }
        } else {
        	throw new ValidacaoException(TipoMensagem.WARNING, "Problema no diretorio do arquivo! ");
        }
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
	    
		File f = null;
		
	    boolean bool = false;
	    
		try {
			
			f = new File(parametroSistema.getValor()+ "temp/" + "certificado" + ".jks");
			
			bool = f.delete();
			
		} catch (Exception e) {
			// File permission problems are caught here.
			throw new ValidacaoException(TipoMensagem.ERROR, "Problema com a permissão na pasta");
		}
		
		return bool;
	}
}