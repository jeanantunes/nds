package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.repository.ParametroSistemaRepository;

@Repository
public class ParametroSistemaRepositoryImpl extends AbstractRepository<ParametroSistema, Long>
		implements ParametroSistemaRepository {

	public ParametroSistemaRepositoryImpl() {
		super(ParametroSistema.class);
	}
	
	public ParametroSistema buscarParametroPorTipoParametro(TipoParametroSistema tipoParametroSistema){
		String hql = "from ParametroSistema p where p.tipoParametroSistema = :tipoParametroSistema";
		
		Query query = this.getSession().createQuery(hql);
		query.setParameter("tipoParametroSistema", tipoParametroSistema);
		
		query.setMaxResults(1);
		
		return (ParametroSistema) query.uniqueResult();
	}
	
	/**
	 * Busca os parâmetros gerais do sistema.
	 * 
	 * @return Lista dos parâmetros do sistema que são considerados gerais. 
	 */
	@SuppressWarnings("unchecked")
	public List<ParametroSistema> buscarParametroSistemaGeral() {
		
		String hql = "from ParametroSistema p where p.tipoParametroSistema in (:listaTipoParametroSistema) ";
		
		List<TipoParametroSistema> lst = new ArrayList<TipoParametroSistema>();
		lst.add(TipoParametroSistema.PATH_LOGO_SISTEMA);
		lst.add(TipoParametroSistema.CNPJ);
		lst.add(TipoParametroSistema.RAZAO_SOCIAL);
		lst.add(TipoParametroSistema.EMAIL_USUARIO);
		lst.add(TipoParametroSistema.UF);
		lst.add(TipoParametroSistema.CODIGO_DISTRIBUIDOR_DINAP);
		lst.add(TipoParametroSistema.CODIGO_DISTRIBUIDOR_FC);
		lst.add(TipoParametroSistema.LOGIN_DISTRIBUIDOR);
		lst.add(TipoParametroSistema.SENHA_DISTRIBUIDOR);
		lst.add(TipoParametroSistema.VERSAO_SISTEMA);
		lst.add(TipoParametroSistema.PATH_INTERFACE_CE_EXPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_PRODIN_IMPORTCAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_PRODIN_EXPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_MDC_IMPORTCAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_MDC_EXPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_GFS_IMPORTCAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_GFS_EXPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_NFE_IMPORTCAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_NFE_EXPORTACAO);
		lst.add(TipoParametroSistema.NFE_DPEC);
		lst.add(TipoParametroSistema.DATA_OPERACAO_CORRENTE);
		
		Query query = this.getSession().createQuery(hql);
		query.setParameter("listaTipoParametroSistema", lst);
		
		return query.list();
	}
	
}