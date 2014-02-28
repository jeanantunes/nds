package br.com.abril.nds.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.model.integracao.ParametroSistema;

public interface ParametroSistemaRepository extends Repository<ParametroSistema, Long> {

	ParametroSistema buscarParametroPorTipoParametro(TipoParametroSistema tipoParametroSistema);
	
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
	public List<ParametroSistema> buscarParametroSistemaGeral();
	
	/**
	 * Salva os Parâmetros do Sistema.
	 *  
	 * @param parametrosSistema
	 */
	public void salvar(Collection<ParametroSistema> parametrosSistema);
	
	/**
	 * @param string
	 * @return
	 */
	String getParametro(String string);
	
	public Map<String, ParametroSistema> buscarParametroSistemaGeralMap();
	
}
