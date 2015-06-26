package br.com.abril.nds.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CertificadoNFEDTO;
import br.com.abril.nds.enums.TipoParametroSistema;
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
}