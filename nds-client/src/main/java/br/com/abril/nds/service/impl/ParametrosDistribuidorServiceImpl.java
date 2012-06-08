package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ParametrosDistribuidorVO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.ParametrosDistribuidorService;

/**
 * Implementação da interface de serviços do parametrosDistribuidorVO
 * @author InfoA2
 */
@Service
public class ParametrosDistribuidorServiceImpl implements ParametrosDistribuidorService {

	@Autowired
	DistribuidorService distribuidorService;
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.ParametrosDistribuidorService#getParametrosDistribuidor()
	 */
	@Transactional(readOnly = true)
	@Override
	public ParametrosDistribuidorVO getParametrosDistribuidor() {
		ParametrosDistribuidorVO parametrosDistribuidor = new ParametrosDistribuidorVO();
		
		parametrosDistribuidor.setConferenciaCegaEncalhe("checked");
		parametrosDistribuidor.setConferenciaCegaRecebimento("");
		
		return parametrosDistribuidor;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.ParametrosDistribuidorService#getDistribuidor(br.com.abril.nds.client.vo.ParametrosDistribuidorVO)
	 */
	@Transactional(readOnly = true)
	@Override
	public Distribuidor getDistribuidor(ParametrosDistribuidorVO parametrosDistribuidor) {
		Distribuidor distribuidor = distribuidorService.obter();
		// TODO: setar os atributos aqui
		//distribuidor.set
		return distribuidor;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.ParametrosDistribuidorService#getListaParametrosSistema(br.com.abril.nds.client.vo.ParametrosDistribuidorVO)
	 */
	@Transactional(readOnly = true)
	@Override
	public List<ParametroSistema> getListaParametrosSistema(ParametrosDistribuidorVO parametrosDistribuidor) {
		List<ParametroSistema> parametrosSistema = new ArrayList<ParametroSistema>();
		// TODO: adicionar a lista de parâmetros do sistema
		//parametrosSistema.add(
		return parametrosSistema;
	}

}
