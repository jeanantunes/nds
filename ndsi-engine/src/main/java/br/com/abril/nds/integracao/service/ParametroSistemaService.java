package br.com.abril.nds.integracao.service;

import java.io.InputStream;
import java.util.Collection;

import br.com.abril.nds.dto.ParametroSistemaGeralDTO;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;

public interface ParametroSistemaService {

	ParametroSistema buscarParametroPorTipoParametro(TipoParametroSistema pathImagensPdv);

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
	public ParametroSistemaGeralDTO buscarParametroSistemaGeral();
	
	/**
	 * Salva os Parâmetros do Sistema.
	 *  
	 * @param dto
	 * @param imgLogotipo
	 * @param imgContentType
	 */
	public void salvar(ParametroSistemaGeralDTO dto, InputStream imgLogotipo, String imgContentType);

	/**
	 * Salva os Parâmetros do sisetam com uma lista de ParametroSistema
	 * @param collection<ParametroSistema>
	 */
	public void salvar(Collection<ParametroSistema> collectionParametroSistema);
	
}
