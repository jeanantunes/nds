package br.com.abril.nds.service;

import java.io.IOException;
import java.util.List;

import br.com.abril.nds.dto.CertificadoNFEDTO;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;

public interface CerfiticadoService {
    
	public String upload(final UploadedFile uploadedFile, final TipoParametroSistema parametroPath) throws IOException;

	public void confirmar(CertificadoNFEDTO filtro, Long idUsuario);

	List<CertificadoNFEDTO> obterCertificado(CertificadoNFEDTO filtro);

	Long quantidade(CertificadoNFEDTO filtro);

	public CertificadoNFEDTO obterCertificadoId(long id);

	public void remover(long id);
	
}
