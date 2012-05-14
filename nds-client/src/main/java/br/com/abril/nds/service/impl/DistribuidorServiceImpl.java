package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.service.DistribuidorService;

@Service
public class DistribuidorServiceImpl implements DistribuidorService {

	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Override
	@Transactional
	public Distribuidor obter() {
		return distribuidorRepository.obter();
	}

	@Override
	@Transactional
	public List<RegistroCurvaABCDistribuidorVO> obterCurvaABCDistribuidor(
			Date dataDe, Date dataAte, String codigoFornecedor,
			String codigoProduto, String nomeProduto, String edicaoProduto,
			String codigoEditor, String codigoCota, String nomeCota,
			String municipio) {
		return distribuidorRepository.obterCurvaABCDistribuidor(dataDe, dataAte, codigoFornecedor, codigoProduto, nomeProduto, edicaoProduto, codigoEditor, codigoCota, nomeCota, municipio);
	}
	
}
