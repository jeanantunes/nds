package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.PersistenceException;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.repository.AbstractRepositoryModel;
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
		lst.add(TipoParametroSistema.DATA_OPERACAO_CORRENTE);
		lst.add(TipoParametroSistema.EMAIL_REMETENTE);
		lst.add(TipoParametroSistema.EMAIL_HOST);
		lst.add(TipoParametroSistema.EMAIL_PORTA);
		lst.add(TipoParametroSistema.EMAIL_PROTOCOLO);		
		lst.add(TipoParametroSistema.EMAIL_SENHA);
		lst.add(TipoParametroSistema.EMAIL_USUARIO);
		lst.add(TipoParametroSistema.EMAIL_AUTENTICAR);	
		lst.add(TipoParametroSistema.FREQUENCIA_EXPURGO);
		lst.add(TipoParametroSistema.NFE_DPEC);
		lst.add(TipoParametroSistema.PATH_IMAGENS_CAPA);
		lst.add(TipoParametroSistema.PATH_IMAGENS_PDV);
		lst.add(TipoParametroSistema.PATH_IMPORTACAO_CONTRATO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_CE_EXPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_GFS_IMPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_GFS_EXPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_MDC_IMPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_MDC_EXPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_MDC_BACKUP);
		lst.add(TipoParametroSistema.PATH_INTERFACE_NFE_IMPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_NFE_EXPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_PRODIN_IMPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_PRODIN_EXPORTACAO);
		lst.add(TipoParametroSistema.PATH_INTERFACE_PICKING_EXPORTACAO);
		//lst.add(TipoParametroSistema.VERSAO_SISTEMA);

		Query query = this.getSession().createQuery(hql);
		query.setParameterList("listaTipoParametroSistema", lst);

		return query.list();
	}

	@Override
	public void salvar(Collection<ParametroSistema> parametrosSistema) {

		String hql = "from ParametroSistema p where p.tipoParametroSistema = :tipoParametroSistema ";
		
		Query query = this.getSession().createQuery(hql);
		
		query.setMaxResults(1);

		for (ParametroSistema novoPS : parametrosSistema) {
			
			if (isInterface(novoPS)) {
				
				continue;
			}
			
			query.setParameter("tipoParametroSistema", novoPS.getTipoParametroSistema());
			
			ParametroSistema psAtual = (ParametroSistema) query.uniqueResult();
			
			if (psAtual == null) {

				adicionar(novoPS);
				
			} else {

				psAtual.setValor(novoPS.getValor());
				
				alterar(psAtual);
			}
		}
	}

	public String getParametro(String tipoParametro) {

		try {
			
			String sql = "SELECT a.valor FROM ParametroSistema a WHERE a.tipoParametroSistema = :tipoParametro";
			Query query = getSession().createQuery(sql);
			query.setParameter("tipoParametro", TipoParametroSistema.valueOf(tipoParametro));
			return (String) query.uniqueResult();
			
		} catch (PersistenceException e) {
			return null;
		}
	}

	/**
	 * @param itemPS
	 * @return
	 */
	private boolean isInterface(ParametroSistema itemPS) {
		return TipoParametroSistema.TIPOS_INTERFACE.contains(itemPS.getTipoParametroSistema());
	}

}