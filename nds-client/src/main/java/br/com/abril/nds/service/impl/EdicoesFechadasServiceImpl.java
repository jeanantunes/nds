package br.com.abril.nds.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ResultadoEdicoesFechadasVO;
import br.com.abril.nds.service.EdicoesFechadasService;

@Service
public class EdicoesFechadasServiceImpl implements EdicoesFechadasService {

	@Override
	@Transactional(readOnly = true)
	public ResultadoEdicoesFechadasVO obterResultadoEdicoesFechadas(Date dataDe, Date dateAte, String codigoFornecedor) {
		return null;
	}
	
}
