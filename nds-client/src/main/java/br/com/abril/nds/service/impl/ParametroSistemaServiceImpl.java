package br.com.abril.nds.service.impl;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ParametroSistemaGeralDTO;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.service.ParametroSistemaService;

@Service
public class ParametroSistemaServiceImpl implements ParametroSistemaService{

	@Autowired
	private ParametroSistemaRepository parametroSistemaRepository;
	
	@Transactional
	public ParametroSistema buscarParametroPorTipoParametro(TipoParametroSistema tipoParametroSistema) {
		return parametroSistemaRepository.buscarParametroPorTipoParametro(tipoParametroSistema);
	}

	/**
	 * Busca os parâmetros gerais do sistema.<br>
	 * Atualmente são considerados como parãmetros gerais do sistema:
	 * <ul>
	 * <li>Logo;</li>
	 * <li>CNPJ;</li>
	 * <li>Razão Social;</li>
	 * <li>E-mail;</li>
	 * <li>UF;</li>
	 * <li>Código Distribuidor Dinap;</li>
	 * <li>Código Distribuidor FC;</li>
	 * <li>Login;</li>
	 * <li>Senha;</li>
	 * <li>Versão Sistema;</li>
	 * <li>Inteface CE Exportação;</li>
	 * <li>Inteface PRODIN Importação;</li>
	 * <li>Inteface PRODIN Exportação;</li>
	 * <li>Inteface MDC Importação;</li>
	 * <li>Inteface MDC Exportação;</li>
	 * <li>Inteface Bancas Exportação;</li>
	 * <li>Inteface GFS Importação;</li>
	 * <li>Inteface GFS Exportação;</li>
	 * <li>Inteface NF-e Importação;</li>
	 * <li>Inteface NF-e Exportação;</li>
	 * <li>NF-e em DPEC;</li>
	 * <li>Data de Operação Corrente;</li>
	 * </ul>
	 * 
	 * @return Lista dos parâmetros do sistema que são considerados gerais. 
	 */
	@Transactional
	public ParametroSistemaGeralDTO buscarParametroSistemaGeral() {
		
		List<ParametroSistema> lst = parametroSistemaRepository.buscarParametroSistemaGeral();
		
		ParametroSistemaGeralDTO dto = new ParametroSistemaGeralDTO();
		dto.setParametrosSistema(lst);
		
		return dto;
	}
	
	@Transactional
	public void salvar(ParametroSistemaGeralDTO dto, InputStream imgLogotipo) {
		
		
		// TODO: validar CNPJ;
		// TODO: validar email;
		// TODO: validar Uf;
		
		// TODO: salvar imgLogotipo
		
		// TODO: salvar dto;
	}
}
