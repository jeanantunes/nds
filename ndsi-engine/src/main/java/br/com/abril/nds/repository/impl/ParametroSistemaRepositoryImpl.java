package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.repository.ParametroSistemaRepository;

@Repository
public class ParametroSistemaRepositoryImpl extends AbstractRepositoryModel<ParametroSistema, Long>
		implements ParametroSistemaRepository {

	public ParametroSistemaRepositoryImpl() {
		super(ParametroSistema.class);
	}
	
	@Override
	public ParametroSistema buscarParametroPorTipoParametro(TipoParametroSistema tipoParametroSistema){
		String hql = "from ParametroSistema p where p.tipoParametroSistema = :tipoParametroSistema";
		
		Query query = this.getSession().createQuery(hql);
		query.setParameter("tipoParametroSistema", tipoParametroSistema);
		
		query.setMaxResults(1);
		
		return (ParametroSistema) query.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ParametroSistema> buscarParametroSistemaGeral() {
		
		String hql = "from ParametroSistema p where p.tipoParametroSistema in (:listaTipoParametroSistema) ";
		
		List<TipoParametroSistema> lst = new ArrayList<TipoParametroSistema>();
		lst.add(TipoParametroSistema.EMAIL_USUARIO);
		lst.add(TipoParametroSistema.VERSAO_SISTEMA);
		lst.add(TipoParametroSistema.PATH_INTERFACE_CE_EXPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_PRODIN_IMPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_PRODIN_EXPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_MDC_IMPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_MDC_EXPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_MDC_BACKUP);
		lst.add(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_GFS_IMPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_GFS_EXPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_NFE_IMPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_NFE_EXPORTACAO);
		lst.add(TipoParametroSistema.NFE_DPEC);
		lst.add(TipoParametroSistema.DATA_OPERACAO_CORRENTE);
		lst.add(TipoParametroSistema.PATH_IMAGENS_CAPA);
		lst.add(TipoParametroSistema.PATH_IMAGENS_PDV);
		lst.add(TipoParametroSistema.FREQUENCIA_EXPURGO);
		
		Query query = this.getSession().createQuery(hql);
		query.setParameterList("listaTipoParametroSistema", lst);
		
		return query.list();
	}
	
	@Override
	public void salvar(Collection<ParametroSistema> parametrosSistema) {

		String hql = "from ParametroSistema p where p.tipoParametroSistema = :tipoParametroSistema ";
		Query query = this.getSession().createQuery(hql);
		query.setMaxResults(1);
		
		for (ParametroSistema itemPS : parametrosSistema) {
			query.setParameter("tipoParametroSistema", itemPS.getTipoParametroSistema());
			ParametroSistema ps = (ParametroSistema) query.uniqueResult();
			if (ps == null) {
				
				// Parâmetro não existe no banco - create:
				adicionar(itemPS);
			} else {
				
				// Atualiza o valor de um parâmetro existente:
				if (!ps.getValor().equals(itemPS.getValor())) {
					ps.setValor(itemPS.getValor());
					alterar(ps);
				}
			}
		}
	}
	
}