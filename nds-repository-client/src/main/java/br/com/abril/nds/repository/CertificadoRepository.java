package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.CertificadoNFEDTO;
import br.com.abril.nds.model.fiscal.nota.Certificado;

public interface CertificadoRepository extends Repository<Certificado, Long> {

	List<CertificadoNFEDTO> obterCertificado(CertificadoNFEDTO filtro);

	Long quantidade(CertificadoNFEDTO filtro);
	
}
