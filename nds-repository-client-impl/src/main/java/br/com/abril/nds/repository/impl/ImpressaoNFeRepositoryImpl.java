package br.com.abril.nds.repository.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.BandeirasDTO;
import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.NotasCotasImpressaoNfeDTO;
import br.com.abril.nds.dto.filtro.FiltroImpressaoNFEDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.StatusRetornado;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.ImpressaoNFeRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class ImpressaoNFeRepositoryImpl extends AbstractRepositoryModel<NotaFiscal, Long> implements ImpressaoNFeRepository {

	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	public ImpressaoNFeRepositoryImpl() {
		super(NotaFiscal.class);
	}

	@SuppressWarnings("unchecked")
	public List<NotasCotasImpressaoNfeDTO> buscarCotasParaImpressaoNFe(FiltroImpressaoNFEDTO filtro) {

		StringBuilder sql = new StringBuilder("SELECT new br.com.abril.nds.dto.NotasCotasImpressaoNfeDTO( ")
		.append(" nf.id as idNota")
		.append(", ident.numeroDocumentoFiscal ")
		.append(", nfi.notaImpressa ")
		.append(", cota ")
		.append(", SUM(prd.quantidade) ")
		.append(", SUM(prd.valorTotalBruto) ")
		.append(", coalesce(SUM(prd.valorDesconto), 0)) ");
		
		//Complementa o HQL com as clausulas de filtro
		Query q = montarFiltroConsultaNfeParaImpressao(filtro, sql, filtro.getPaginacao());

		return q.list();
		
	}

	public Integer buscarCotasParaImpressaoNFeQtd(FiltroImpressaoNFEDTO filtro) {

		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) ");
		
		//Complementa o HQL com as clausulas de filtro
		Query q = montarFiltroConsultaNfeParaImpressao(filtro, sql, null);
		
		return q.list().size();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<NotasCotasImpressaoNfeDTO> buscarCotasParaImpressaoNotaEnvio(FiltroImpressaoNFEDTO filtro) {
		StringBuilder sql = new StringBuilder();
		sql.append("select new ")
		   .append(NotasCotasImpressaoNfeDTO.class.getCanonicalName())
		   .append(" (ne.numero, ne.notaImpressa, cota, SUM(nei.reparte), SUM(nei.precoCapa * nei.reparte), SUM(nei.desconto)) ");
		
		//Complementa o HQL com as clausulas de filtro
		Query q = montarFiltroConsultaNotaEnvioParaImpressao(filtro, sql, filtro.getPaginacao());

		return q.list();
	}

	@Override
	public Integer buscarCotasParaImpressaoNotaEnvioQtd(FiltroImpressaoNFEDTO filtro) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) ");
		
		//Complementa o HQL com as clausulas de filtro
		Query q = montarFiltroConsultaNotaEnvioParaImpressao(filtro, sql, null);
		
		return q.list().size();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<NotaFiscal> buscarNotasParaImpressaoNFe(FiltroImpressaoNFEDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select nf ")
		.append(" from NotaFiscal nf ")
		.append(" JOIN nf.notaFiscalInformacoes as nfi ")
		.append(" JOIN nfi.informacaoEletronica as infElet ")
		.append(" JOIN infElet.retornoComunicacaoEletronica retComElet ")
		.append(" JOIN nfi.identificacao as ident ");
		
		
		hql.append(" where")
		.append(" infElet.chaveAcesso is not null ")
		.append(" AND retComElet.protocolo is not null ")
		.append(" AND retComElet.statusRetornado = :statusNFe ")
		.append(" AND nf.id in (:idNotas) ");
		
		if(filtro.getIdBoxInicial() != null && filtro.getIdBoxFinal() != null){
			
			hql.append(" AND box.id between :idBoxInicial and :idBoxFinal ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameterList("idNotas", filtro.getNumerosNotas());
		
		query.setParameter("statusNFe", StatusRetornado.AUTORIZADO );
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<NotaEnvio> buscarNotasEnvioParaImpressaoNFe(FiltroImpressaoNFEDTO filtro) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ne ");
		
		//Complementa o HQL com as clausulas de filtro
		Query q = montarFiltroConsultaNotaEnvioParaImpressao(filtro, sql, filtro.getPaginacao());

		return q.list();
	}
	
	//Torna reaproveitavel a parte de filtro da query
	private Query montarFiltroConsultaNfeParaImpressao(FiltroImpressaoNFEDTO filtro, StringBuilder sql, PaginacaoVO paginacao) {
		
		if(filtro == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "O filtro não pode ser nulo ou estar vazio.");
		}
		
		sql.append("from NotaFiscal nf, Cota cota ")
		.append(" JOIN nf.notaFiscalInformacoes as nfi ")
		.append(" JOIN nf.notaFiscalInformacoes.detalhesNotaFiscal as det ")
		.append(" JOIN nfi.informacaoEletronica as infElet ")
		.append(" JOIN infElet.retornoComunicacaoEletronica retComElet")
		.append(" JOIN nfi.identificacaoEmitente as identEmit ")
		.append(" JOIN nfi.identificacaoDestinatario as identDest")
		.append(" JOIN nfi.identificacao as ident ")
		.append(" JOIN ident.naturezaOperacao as natOp ")
		.append(" LEFT JOIN natOp.processo as proc ")
		.append(" JOIN identEmit.documento as doc ")
		.append(" JOIN identDest.documento as docDest ")
		.append(" JOIN identDest.pessoaDestinatarioReferencia pdest")
		.append(" JOIN det.produtoServico as prd ")
		.append(" JOIN prd.produtoEdicao as pe ");
		
		
		if(filtro.getDataMovimentoInicial() != null || filtro.getDataMovimentoFinal() != null) {
			sql.append(", MovimentoEstoqueCota movEstCota ");
		}
		
		if(filtro.getIdRoteiro() != null && filtro.getIdRoteiro() > -1) {
			sql.append(", Roteiro roteiro ");
		}
		
		if(filtro.getIdRoteiro() != null && filtro.getIdRoteiro() > -1 
				&& filtro.getIdRota() != null && filtro.getIdRota() > -1) {
			sql.append("join roteiro.rotas rota ");
		}
		
		sql.append("WHERE 1 = 1 ");
		sql.append("and pdest.id = cota.pessoa.id ");
		
		if(filtro.getTipoNFe() != null && Long.parseLong(filtro.getTipoNFe()) > -1) {
			sql.append("and nf.identificacao.tipoNotaFiscal.id = :tipoNotaFiscal ");
		}
		
		if(filtro.getDataEmissao() != null) {
			sql.append("and ident.dataEmissao = :dataEmissao ");
		}
		
		sql.append("and retComElet.statusRetornado = :statusNFe ");
		
		//Filtra por datas de movimento de, entre e ate
		if(filtro.getDataMovimentoInicial() != null || filtro.getDataMovimentoFinal() != null) {
			if(filtro.getDataMovimentoInicial() != null && filtro.getDataMovimentoFinal() == null) {
				sql.append("and cota.id = movEstCota.cota.id ");
				sql.append("and movEstCota.data >= :dataInicialMovimento ");
			} else if(filtro.getDataMovimentoInicial() != null && filtro.getDataMovimentoFinal() != null) {
				sql.append("and cota.id = movEstCota.cota.id ");
				sql.append("and movEstCota.data between :dataInicialMovimento and :dataFinalMovimento ");
			} else {
				sql.append("and cota.id = movEstCota.cota.id ");
				sql.append("and movEstCota.data <= :dataFinalMovimento ");
			}
		}
				
		if(filtro.getIdRoteiro() != null && filtro.getIdRoteiro() > -1) {
			sql.append("and roteiro.roteirizacao.box.id = cota.box.id ");
			sql.append("and roteiro.id = :idRoteiro ");
		}
		
		if(filtro.getIdRoteiro() != null && filtro.getIdRoteiro() > -1 
				&& filtro.getIdRota() != null && filtro.getIdRota() > -1) {
			sql.append("and rota.id = :idRota ");
		}
		
			
		if(filtro.getIdCotaInicial() != null || filtro.getIdCotaFinal() != null) {
			if(filtro.getIdCotaInicial() != null && filtro.getIdCotaFinal() == null) {
				sql.append("and cota.numeroCota >= :idCotaInicial ");
			} else if(filtro.getIdCotaInicial() != null && filtro.getIdCotaFinal() != null) {
				sql.append("and cota.numeroCota between :idCotaInicial and :idCotaFinal ");
			} else {
				sql.append("and cota.numeroCota <= :idCotaFinal ");
			}
		}
		
		if(filtro.getIdsCotas() != null && filtro.getIdsCotas().size() > 0) {	
			sql.append("and cota.id in (:idsCotas) ");
		}
		
		if(filtro.getNumerosNotas() != null && filtro.getNumerosNotas().size() > 0) {	
			sql.append("and ident.numeroDocumentoFiscal in (:numerosNotas) ");
		}
		
		if(filtro.getIdBoxInicial() != null || filtro.getIdBoxFinal() != null) {
			if(filtro.getIdBoxInicial() != null && filtro.getIdBoxFinal() == null) {
				sql.append("and cota.box.id >= :idBoxInicial ");
			} else if(filtro.getIdBoxInicial() != null && filtro.getIdBoxFinal() != null) {
				sql.append("and cota.box.id between :idBoxInicial and :idBoxFinal ");
			} else {
				sql.append("and cota.box.id <= :idBoxFinal ");
			}
		}
				
		sql.append("group by nf, cota ");
		
		//Monta a ordenacao
		if (paginacao != null) {

			Map<String, String> columnToSort = new HashMap<String, String>();
			columnToSort.put("numeroNota", "ident.numeroDocumentoFiscal");
			columnToSort.put("idCota", "cota.id");
			columnToSort.put("nomeCota", "cota.pessoa.nome");
			columnToSort.put("vlrTotal", "vlrTotal");
			columnToSort.put("vlrTotalDesconto", "vlrTotalDesconto");
			
			//Verifica a entrada para evitar expressoes SQL
			if (paginacao.getSortColumn() == null 
					|| paginacao.getSortColumn() != null 
					&& !paginacao.getSortColumn().matches("[a-zA-Z0-9\\._]*")) {
				paginacao.setSortColumn("idCota");
	        }
						
			sql.append("order by "+ columnToSort.get(paginacao.getSortColumn())+ " ");

			String orderByColumn = "";

			sql.append(orderByColumn);

			if (paginacao.getOrdenacao() != null) {
				sql.append(paginacao.getOrdenacao().toString());
			}

		}
		
		Query q = getSession().createQuery(sql.toString());
		
		if(filtro.getDataEmissao() != null) {
			q.setParameter("dataEmissao", new java.sql.Date(filtro.getDataEmissao().getTime()));
		}
		
		if(filtro.getTipoNFe() != null && Long.parseLong(filtro.getTipoNFe()) > -1) {
			q.setParameter("tipoNotaFiscal", Long.parseLong(filtro.getTipoNFe()) );
		}
		
		q.setParameter("statusNFe", br.com.abril.nds.model.fiscal.nota.StatusRetornado.AUTORIZADO );
		
		if(filtro.getDataMovimentoInicial() != null || filtro.getDataMovimentoFinal() != null) {
			if(filtro.getDataMovimentoInicial() != null && filtro.getDataMovimentoFinal() == null) {
				q.setParameter("dataInicialMovimento", new java.sql.Date(filtro.getDataMovimentoInicial().getTime()));
			} else if(filtro.getDataMovimentoInicial() != null && filtro.getDataMovimentoFinal() != null) {
				q.setParameter("dataInicialMovimento", new java.sql.Date(filtro.getDataMovimentoInicial().getTime()));
				q.setParameter("dataFinalMovimento", new java.sql.Date(filtro.getDataMovimentoFinal().getTime()));
			} else {
				q.setParameter("dataFinalMovimento", new java.sql.Date(filtro.getDataMovimentoFinal().getTime()));
			}
		}
		
		if(filtro.getIdRoteiro() != null && filtro.getIdRoteiro() > -1) {
			q.setParameter("idRoteiro", filtro.getIdRoteiro() );
		}
		
		if(filtro.getIdRota() != null && filtro.getIdRota() > -1) {
			q.setParameter("idRota", filtro.getIdRota() );
		}
		
		if(filtro.getIdCotaInicial() != null || filtro.getIdCotaFinal() != null) {
			if(filtro.getIdCotaInicial() != null && filtro.getIdCotaFinal() == null) {
				q.setParameter("idCotaInicial", Integer.parseInt(filtro.getIdCotaInicial().toString()));
			} else if(filtro.getIdCotaInicial() != null && filtro.getIdCotaFinal() != null) {
				q.setParameter("idCotaInicial", Integer.parseInt(filtro.getIdCotaInicial().toString()));
				q.setParameter("idCotaFinal", Integer.parseInt(filtro.getIdCotaFinal().toString()));
			} else {
				q.setParameter("idCotaFinal", Integer.parseInt(filtro.getIdCotaFinal().toString()));
			}
		}
		
		if(filtro.getIdsCotas() != null && filtro.getIdsCotas().size() > 0) {
			q.setParameterList("idsCotas", filtro.getIdsCotas());
		}
		
		if(filtro.getNumerosNotas() != null && filtro.getNumerosNotas().size() > 0) {	
			q.setParameterList("numerosNotas", filtro.getNumerosNotas());
		}
		
		if(filtro.getIdBoxInicial() != null || filtro.getIdBoxFinal() != null) {
			if(filtro.getIdBoxInicial() != null && filtro.getIdBoxFinal() == null) {
				q.setParameter("idBoxInicial", filtro.getIdBoxInicial());
			} else if(filtro.getIdBoxInicial() != null && filtro.getIdBoxFinal() != null) {
				q.setParameter("idBoxInicial", filtro.getIdBoxInicial());
				q.setParameter("idBoxFinal", filtro.getIdBoxFinal());
			} else {
				q.setParameter("idBoxFinal", filtro.getIdBoxFinal());
			}
		}
		
		if (paginacao != null && paginacao.getQtdResultadosPorPagina() != null) {
			q.setFirstResult( paginacao.getQtdResultadosPorPagina() * ( (paginacao.getPaginaAtual() - 1 )))
            .setMaxResults( paginacao.getQtdResultadosPorPagina() );
		}
		
		return q;
		
	}
	
	//Torna reaproveitavel a parte de filtro da query
		private Query montarFiltroConsultaNotaEnvioParaImpressao(FiltroImpressaoNFEDTO filtro, StringBuilder sql, PaginacaoVO paginacao) {
			
			boolean obrigacaoFiscal = this.distribuidorRepository.obrigacaoFiscal();
			
			if(filtro == null) {
				throw new ValidacaoException(TipoMensagem.ERROR, "O filtro não pode ser nulo ou estar vazio.");
			}
			
			sql.append("from NotaEnvio ne, Cota cota inner join ne.listaItemNotaEnvio nei ");
			
			if(filtro.getDataMovimentoInicial() != null || filtro.getDataMovimentoFinal() != null) {
				sql.append(", MovimentoEstoqueCota movEstCota ");
			}
			
			if(filtro.getIdRoteiro() != null && filtro.getIdRoteiro() > -1) {
				sql.append(", Roteiro roteiro ");
			}
			
			if(filtro.getIdRoteiro() != null && filtro.getIdRoteiro() > -1 
					&& filtro.getIdRota() != null && filtro.getIdRota() > -1) {
				sql.append("join roteiro.rotas rota ");
			}
					
			sql.append("WHERE 1 = 1 ");
			sql.append("and ne.destinatario.pessoaDestinatarioReferencia.id = cota.pessoa.id ");
			sql.append("and ne.dataEmissao = :dataEmissao ");
									
			//Filtra por datas de movimento de, entre e ate
			if(filtro.getDataMovimentoInicial() != null || filtro.getDataMovimentoFinal() != null) {
				if(filtro.getDataMovimentoInicial() != null && filtro.getDataMovimentoFinal() == null) {
					sql.append("and cota.id = movEstCota.cota.id ");
					sql.append("and movEstCota.data >= :dataInicialMovimento ");
				} else if(filtro.getDataMovimentoInicial() != null && filtro.getDataMovimentoFinal() != null) {
					sql.append("and cota.id = movEstCota.cota.id ");
					sql.append("and movEstCota.data between :dataInicialMovimento and :dataFinalMovimento ");
				} else {
					sql.append("and cota.id = movEstCota.cota.id ");
					sql.append("and movEstCota.data <= :dataFinalMovimento ");
				}
			}
						
			//TODO: Sérgio - Acertar o id do box de roteirizacao
			if(filtro.getIdRoteiro() != null && filtro.getIdRoteiro() > -1) {
				sql.append("and roteiro.roteirizacao.id = cota.box.id ");
				sql.append("and roteiro.id = :idRoteiro ");
			}
			
			if(filtro.getIdRoteiro() != null && filtro.getIdRoteiro() > -1 
					&& filtro.getIdRota() != null && filtro.getIdRota() > -1) {
				sql.append("and rota.id = :idRota ");
			}
			
				
			if(filtro.getIdCotaInicial() != null || filtro.getIdCotaFinal() != null) {
				if(filtro.getIdCotaInicial() != null && filtro.getIdCotaFinal() == null) {
					sql.append("and cota.id >= :idCotaInicial ");
				} else if(filtro.getIdCotaInicial() != null && filtro.getIdCotaFinal() != null) {
					sql.append("and cota.id between :idCotaInicial and :idCotaFinal ");
				} else {
					sql.append("and cota.id <= :idCotaFinal ");
				}
			}
			
			if(filtro.getIdsCotas() != null && filtro.getIdsCotas().size() > 0) {	
				sql.append("and cota.id in (:idsCotas) ");
			}
			
			if(filtro.getNumerosNotas() != null && filtro.getNumerosNotas().size() > 0) {	
				sql.append("and ne.numero in (:numerosNotas) ");
			}
			
			if(filtro.getIdBoxInicial() != null || filtro.getIdBoxFinal() != null) {
				if(filtro.getIdBoxInicial() != null && filtro.getIdBoxFinal() == null) {
					sql.append("and cota.box.id >= :idBoxInicial ");
				} else if(filtro.getIdBoxInicial() != null && filtro.getIdBoxFinal() != null) {
					sql.append("and cota.box.id between :idBoxInicial and :idBoxFinal ");
				} else {
					sql.append("and cota.box.id <= :idBoxFinal ");
				}
			}
					
			sql.append("group by cota ");
			
			//Monta a ordenacao
			if (paginacao != null) {

				Map<String, String> columnToSort = new HashMap<String, String>();
				columnToSort.put("numeroNota", "ne.numero");
				columnToSort.put("idCota", "cota.id");
				columnToSort.put("nomeCota", "cota.pessoa.nome");
				columnToSort.put("vlrTotal", "vlrTotal");
				columnToSort.put("vlrTotalDesconto", "vlrTotalDesconto");
				
				//Verifica a entrada para evitar expressoes SQL
				if (paginacao.getSortColumn() == null 
						|| paginacao.getSortColumn() != null 
						&& !paginacao.getSortColumn().matches("[a-zA-Z0-9\\._]*")) {
					paginacao.setSortColumn("idCota");
		        }
							
				sql.append("order by "+ columnToSort.get(paginacao.getSortColumn())+ " ");

				String orderByColumn = "";

				sql.append(orderByColumn);

				if (paginacao.getOrdenacao() != null) {
					sql.append(paginacao.getOrdenacao().toString());
				}

			}
			
			Query q = getSession().createQuery(sql.toString());
			
			q.setParameter("dataEmissao", new java.sql.Date(filtro.getDataEmissao().getTime()));
			
			if(obrigacaoFiscal) {
				q.setParameter("statusNFe", br.com.abril.nds.model.fiscal.nota.StatusRetornado.AUTORIZADO );
			}
			
			if(filtro.getDataMovimentoInicial() != null || filtro.getDataMovimentoFinal() != null) {
				if(filtro.getDataMovimentoInicial() != null && filtro.getDataMovimentoFinal() == null) {
					q.setParameter("dataInicialMovimento", new java.sql.Date(filtro.getDataMovimentoInicial().getTime()));
				} else if(filtro.getDataMovimentoInicial() != null && filtro.getDataMovimentoFinal() != null) {
					q.setParameter("dataInicialMovimento", new java.sql.Date(filtro.getDataMovimentoInicial().getTime()));
					q.setParameter("dataFinalMovimento", new java.sql.Date(filtro.getDataMovimentoFinal().getTime()));
				} else {
					q.setParameter("dataFinalMovimento", new java.sql.Date(filtro.getDataMovimentoFinal().getTime()));
				}
			}
			
			if(filtro.getIdRoteiro() != null && filtro.getIdRoteiro() > -1) {
				q.setParameter("idRoteiro", filtro.getIdRoteiro() );
			}
			
			if(filtro.getIdRota() != null && filtro.getIdRota() > -1) {
				q.setParameter("idRota", filtro.getIdRota() );
			}
			
			if(filtro.getIdCotaInicial() != null || filtro.getIdCotaFinal() != null) {
				if(filtro.getIdCotaInicial() != null && filtro.getIdCotaFinal() == null) {
					q.setParameter("idCotaInicial", filtro.getIdCotaInicial());
				} else if(filtro.getIdCotaInicial() != null && filtro.getIdCotaFinal() != null) {
					q.setParameter("idCotaInicial", filtro.getIdCotaInicial());
					q.setParameter("idCotaFinal", filtro.getIdCotaFinal());
				} else {
					q.setParameter("idCotaFinal", filtro.getIdCotaFinal());
				}
			}
			
			if(filtro.getIdsCotas() != null && filtro.getIdsCotas().size() > 0) {
				q.setParameterList("idsCotas", filtro.getIdsCotas());
			}
			
			if(filtro.getNumerosNotas() != null && filtro.getNumerosNotas().size() > 0) {	
				q.setParameterList("numerosNotas", filtro.getNumerosNotas());
			}
			
			if(filtro.getIdBoxInicial() != null || filtro.getIdBoxFinal() != null) {
				if(filtro.getIdBoxInicial() != null && filtro.getIdBoxFinal() == null) {
					q.setParameter("idBoxInicial", filtro.getIdBoxInicial());
				} else if(filtro.getIdBoxInicial() != null && filtro.getIdBoxFinal() != null) {
					q.setParameter("idBoxInicial", filtro.getIdBoxInicial());
					q.setParameter("idBoxFinal", filtro.getIdBoxFinal());
				} else {
					q.setParameter("idBoxFinal", filtro.getIdBoxFinal());
				}
			}
			
			if (paginacao != null && paginacao.getQtdResultadosPorPagina() != null) {
				q.setFirstResult( paginacao.getQtdResultadosPorPagina() * ( (paginacao.getPaginaAtual() - 1 )))
	            .setMaxResults( paginacao.getQtdResultadosPorPagina() );
			}
			
			return q;
			
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<Produto> buscarProdutosParaImpressaoNFe(FiltroImpressaoNFEDTO filtro) {

			if(filtro == null) {
				throw new ValidacaoException(TipoMensagem.ERROR, "O filtro não pode ser nulo ou estar vazio.");
			}
			
			StringBuilder sql = new StringBuilder();
			sql.append("select distinct p ");
			sql.append("from Lancamento l join l.produtoEdicao pe join pe.produto p join p.fornecedores f ");
			sql.append("where 1 = 1 ");
			
			if(filtro.getDataMovimentoInicial() != null && filtro.getDataMovimentoFinal() != null) {
				sql.append("and l.dataLancamentoDistribuidor between :dataMovimentoInicial and :dataMovimentoFinal ");
			}
			
			if(filtro.getIdsFornecedores() != null) {
				sql.append("and f.id in (:idsFornecedores) ");
			}
			
			if(filtro.getCodigoProduto() != null) {
				sql.append("and p.codigo like :codigoProduto ");
			}
			
			if(filtro.getNomeProduto() != null) {
				sql.append("and p.nome like :nomeProduto ");
			}
			
			Query q = getSession().createQuery(sql.toString());
			
			if(filtro.getDataMovimentoInicial() != null && filtro.getDataMovimentoFinal() != null) {
				Calendar dataMovimentoInicial = Calendar.getInstance();
				dataMovimentoInicial.setTime(filtro.getDataMovimentoInicial());
				dataMovimentoInicial.set(Calendar.HOUR_OF_DAY, 0);
				dataMovimentoInicial.set(Calendar.MINUTE, 0);
				dataMovimentoInicial.set(Calendar.SECOND, 0);
				dataMovimentoInicial.set(Calendar.MILLISECOND, 0);
				
				Calendar dataMovimentoFinal = Calendar.getInstance();
				dataMovimentoFinal.setTime(filtro.getDataMovimentoFinal());
				dataMovimentoFinal.set(Calendar.HOUR_OF_DAY, 23);
				dataMovimentoFinal.set(Calendar.MINUTE, 59);
				dataMovimentoFinal.set(Calendar.SECOND, 59);
				dataMovimentoFinal.set(Calendar.MILLISECOND, 999);
				
				q.setParameter("dataMovimentoInicial", new java.sql.Date(dataMovimentoInicial.getTime().getTime()));
				q.setParameter("dataMovimentoFinal", new java.sql.Date(dataMovimentoFinal.getTime().getTime()));
			}
		
			q.setParameterList("idsFornecedores", filtro.getIdsFornecedores());
			
			if(filtro.getCodigoProduto() != null) {
				q.setParameter("codigoProduto", "%"+ filtro.getCodigoProduto() +"%");
			}
			
			if(filtro.getNomeProduto() != null) {
				q.setParameter("nomeProduto", "%"+ filtro.getNomeProduto() +"%");
			}

			return q.list();
		}

	@Override
	@SuppressWarnings("unchecked")
	public List<NotasCotasImpressaoNfeDTO> obterNotafiscalImpressao(FiltroImpressaoNFEDTO filtro) {
		
		/**
		 * Long numeroNota, boolean notaImpressa, Cota c, BigInteger totalExemplares, BigDecimal vlrTotal, BigDecimal vlrTotalDesconto
		 */
		StringBuilder hql = new StringBuilder();
		
		switch (filtro.getNaturezaOperacao().getTipoDestinatario()) {
			case COTA:
			case DISTRIBUIDOR:
				
				hql.append("SELECT new br.com.abril.nds.dto.NotasCotasImpressaoNfeDTO( ")
				.append(" notaFiscal.id as idNota")
				.append(", notaFiscal.notaFiscalInformacoes.identificacao.numeroDocumentoFiscal ")
				.append(", notaFiscal.notaFiscalInformacoes.notaImpressa ")
				.append(", cota ")
				.append(", SUM(item.produtoServico.quantidade) ")
				.append(", SUM(item.produtoServico.valorTotalBruto) ")
				.append(", coalesce(SUM(item.produtoServico.valorDesconto), 0)) ");
			break;
	
			case FORNECEDOR:            
				
				hql.append("SELECT new br.com.abril.nds.dto.NotasCotasImpressaoNfeDTO( ")
				.append(" notaFiscal.id as idNota")
				.append(", notaFiscal.notaFiscalInformacoes.identificacao.numeroDocumentoFiscal ")
				.append(", notaFiscal.notaFiscalInformacoes.notaImpressa ")
				.append(", pj.nome ")
				.append(", SUM(item.produtoServico.quantidade) ")
				.append(", SUM(item.produtoServico.valorTotalBruto) ")
				.append(", coalesce(SUM(item.produtoServico.valorDesconto), 0)) ");
				
			break;        
		}
		
		Query query = queryConsultaImpressaoParameters(queryConsultaImpressaoNfe(filtro, hql, true, true, true), filtro);
		
		return query.list();
		
	}
	
	private StringBuilder queryConsultaImpressaoNfe(FiltroImpressaoNFEDTO filtro, StringBuilder hql, boolean isCount, boolean isPagination, boolean isGroup){
		
		montarHqlNaturezaOperacacao(filtro, hql);
		
		// Tipo de Nota:		
		if(filtro.getIdNaturezaOperacao() != null && filtro.getIdNaturezaOperacao() > 0) {
			hql.append(" AND notaFiscal.notaFiscalInformacoes.identificacao.naturezaOperacao.id in (SELECT no.id ")
			.append(" FROM NaturezaOperacao no ")
			.append(" JOIN no.tipoMovimento tm ")
			.append(" WHERE no.id in(:naturezaOperacao)) "); 
		}
		
		// Data Emissão:		
		if(filtro.getDataEmissaoInicial() != null && filtro.getDataEmissaoFinal() != null) {
			hql.append(" AND notaFiscal.notaFiscalInformacoes.identificacao.dataEmissao BETWEEN :dataInicial AND :dataFinal ");
		}
		
		// Intervalo de Cota:
		if(filtro.getIdCotaInicial() != null && filtro.getIdCotaFinal() != null) {
			hql.append(" AND cota.numeroCota BETWEEN :numeroCotaInicial AND :numeroCotaFinal ");
		}
		
		// Roteiro:
		if(filtro.getIdRoteiro() != null && filtro.getIdRoteiro() > 0) {
			hql.append(" AND roteiro.id = :roteiroId ");
		}
		
		// Rota:		
		if(filtro.getIdRota() != null && filtro.getIdRota() > 0) {
			hql.append(" AND rota.id = :rotaId ");
		}
		
		// Cota de:	 Até   
		if(filtro.getIdBoxInicial() != null && filtro.getIdBoxFinal() != null) {
			hql.append(" AND box.codigo between :codigoBoxInicial AND :codigoBoxFinal ");
		}
		
		if(filtro.getIdsFornecedores() != null) {
			hql.append(" AND fornecedor.id in (:fornecedor) ");
		}
		
		if(isGroup) {

			hql.append(" group by notaFiscal.id ");

			if(filtro != null && filtro.getNaturezaOperacao() != null && filtro.getNaturezaOperacao().getTipoDestinatario() != null) {
				
				switch(filtro.getNaturezaOperacao().getTipoDestinatario()) {
				
					case COTA:
					case DISTRIBUIDOR:
						hql.append(", cota.id ");
						break;
					case FORNECEDOR:
						break;
					default:
						break;
				}
			}
				
			if(filtro.getIdBoxInicial() != null || filtro.getIdBoxInicial() != null) {
				
				hql.append(", box.id ");
			}
			
		}
		
		if(isCount && isPagination) {
			if(filtro.getPaginacao() != null && filtro.getPaginacao().getSortOrder() != null && filtro.getPaginacao().getSortColumn() != null) {
				hql.append(" ORDER BY  ").append(filtro.getPaginacao().getSortColumn()).append(" ").append(filtro.getPaginacao().getSortOrder());
			}
		}
		
		return hql;
	}
	
	private StringBuilder montarHqlNaturezaOperacacao(FiltroImpressaoNFEDTO filtro, StringBuilder hql) {
		
		switch (filtro.getNaturezaOperacao().getTipoDestinatario()) {
			case COTA:
			case DISTRIBUIDOR:
				
				hql.append(" FROM NotaFiscal as notaFiscal ")
					.append(" JOIN notaFiscal.notaFiscalInformacoes.identificacaoDestinatario.cota as cota ")
					.append(" JOIN notaFiscal.notaFiscalInformacoes.detalhesNotaFiscal as item ")
					.append(" JOIN notaFiscal.notaFiscalInformacoes.identificacaoDestinatario.pessoaDestinatarioReferencia as pj ")
					.append(" join cota.pdvs pdv ")
					.append(" left outer join pdv.rotas rotaPdv ")
					.append(" left outer join rotaPdv.rota rota ")
					.append(" left outer join rota.roteiro roteiro ")
					.append(" left outer join roteiro.roteirizacao roteirizacao ")
					.append(" left outer join roteirizacao.box box ")
					
					.append(" WHERE cota.pessoa.id = pj.idPessoaOriginal ")
					.append(" AND notaFiscal.notaFiscalInformacoes.informacaoEletronica.retornoComunicacaoEletronica.statusRetornado = :statusNFe ");
			break;
    
			case FORNECEDOR:            
				
				hql.append(" FROM NotaFiscal as notaFiscal ")
					.append(" JOIN notaFiscal.notaFiscalInformacoes.detalhesNotaFiscal as item ")
					.append(" JOIN notaFiscal.notaFiscalInformacoes.identificacaoDestinatario.pessoaDestinatarioReferencia as pj ")
					.append(" WHERE notaFiscal.notaFiscalInformacoes.informacaoEletronica.retornoComunicacaoEletronica.statusRetornado = :statusNFe ");
				
			break;        
		}
		
		return hql;
	}

	private Query queryConsultaImpressaoParameters(StringBuilder hql, FiltroImpressaoNFEDTO filtro) {
		
		// Realizar a consulta e converter ao objeto cota exemplares.
		Query query = this.getSession().createQuery(hql.toString());		
		
		query.setParameter("statusNFe", StatusRetornado.AUTORIZADO );
		
		// Data Emissão:	...  Até   ...
		if(filtro.getDataEmissaoInicial() != null && filtro.getDataEmissaoFinal() != null) {
			query.setParameter("dataInicial", filtro.getDataEmissaoInicial());
			query.setParameter("dataFinal", filtro.getDataEmissaoFinal());
		}
		
		// tipo da nota fiscal		
		if(filtro.getIdNaturezaOperacao() != null && filtro.getIdNaturezaOperacao().longValue() > 0) {
			 query.setParameter("naturezaOperacao", filtro.getIdNaturezaOperacao()); 
		}
		
		// forncedor id		
		if(filtro.getIdsFornecedores() !=null && !filtro.getIdsFornecedores().isEmpty()) {
			query.setParameterList("fornecedor", filtro.getIdsFornecedores());
		}
		
		if(filtro.getIdCotaInicial() != null && filtro.getIdCotaFinal() != null) {
			query.setParameter("numeroCotaInicial", filtro.getIdCotaInicial().intValue());
			query.setParameter("numeroCotaFinal", filtro.getIdCotaFinal().intValue());
		}
		
		// Roteiro:
		if(filtro.getIdRoteiro() != null && filtro.getIdRoteiro() > 0) {
			query.setParameter("roteiroId", filtro.getIdRoteiro());
		}
		
		// Rota:		
		if(filtro.getIdRota() != null && filtro.getIdRota() > 0) {
			query.setParameter("rotaId", filtro.getIdRota());
		}
		
		// Cota de:	 Até   
		if(filtro.getIdBoxInicial() != null && filtro.getIdBoxFinal() != null) {
			query.setParameter("codigoBoxInicial", filtro.getIdBoxInicial());
			query.setParameter("codigoBoxFinal", filtro.getIdBoxFinal());
		}
		
		if(filtro.getIdsFornecedores() != null) {
			query.setParameterList("fornecedor", filtro.getIdsFornecedores());
		}
		
		return query;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<BandeirasDTO> obterNotafiscalImpressaoBandeira(FiltroImpressaoNFEDTO filtro) {
		
		/**
		 * Long numeroNota, boolean notaImpressa, Cota c, BigInteger totalExemplares, BigDecimal vlrTotal, BigDecimal vlrTotalDesconto
		 */
		StringBuilder hql = new StringBuilder();
				
		hql.append("SELECT new br.com.abril.nds.dto.BandeirasDTO( ")
		.append("notaFiscal.notaFiscalInformacoes.identificacao.numeroDocumentoFiscal ")
		.append(", notaFiscal.notaFiscalInformacoes.identificacao.serie ")
		.append(", editor.codigo ")
		.append(", pessoaJuridica.razaoSocial ")
		.append(", notaFiscal.notaFiscalInformacoes.informacaoEletronica.chaveAcesso) ")
		.append(" FROM NotaFiscal as notaFiscal ")
		.append(" JOIN notaFiscal.notaFiscalInformacoes.detalhesNotaFiscal as item ")
		.append(" JOIN item.produtoServico produtoServico ")
		.append(" JOIN produtoServico.produtoEdicao produtoEdicao ")
		.append(" JOIN produtoEdicao.produto produto ")
		.append(" JOIN produto.editor editor ")
		.append(" JOIN editor.pessoaJuridica pessoaJuridica ")
		.append(" JOIN notaFiscal.notaFiscalInformacoes.identificacaoDestinatario.pessoaDestinatarioReferencia as pj ")
		.append(" WHERE notaFiscal.notaFiscalInformacoes.informacaoEletronica.retornoComunicacaoEletronica.statusRetornado = :statusNFe ");	
		// Tipo de Nota:		
		if(filtro.getIdNaturezaOperacao() != null && filtro.getIdNaturezaOperacao() > 0) {
			hql.append(" AND notaFiscal.notaFiscalInformacoes.identificacao.naturezaOperacao.id in (SELECT no.id ")
			.append(" FROM NaturezaOperacao no ")
			.append(" JOIN no.tipoMovimento tm ")
			.append(" WHERE no.id in(:naturezaOperacao)) "); 
		}
		
		// Data Emissão:		
		if(filtro.getDataEmissaoInicial() != null && filtro.getDataEmissaoFinal() != null) {
			hql.append(" AND notaFiscal.notaFiscalInformacoes.identificacao.dataEmissao BETWEEN :dataInicial AND :dataFinal ");
		}
		
		if(filtro.getIdsFornecedores() != null) {
			hql.append(" AND fornecedor.id in (:fornecedor) ");
		}
		// Realizar a consulta e converter ao objeto cota exemplares.
		Query query = this.getSession().createQuery(hql.toString());		
		
//		query.setParameter("dataSaida", new Date() );
		query.setParameter("statusNFe", StatusRetornado.AUTORIZADO );
		
		// Data Emissão:	...  Até   ...
		if(filtro.getDataEmissaoInicial() != null && filtro.getDataEmissaoFinal() != null) {
			query.setParameter("dataInicial", filtro.getDataEmissaoInicial());
			query.setParameter("dataFinal", filtro.getDataEmissaoFinal());
		}
		
		// tipo da nota fiscal		
		if(filtro.getIdNaturezaOperacao() != null && filtro.getIdNaturezaOperacao().longValue() > 0) {
			 query.setParameter("naturezaOperacao", filtro.getIdNaturezaOperacao()); 
		}
		
		// forncedor id		
		if(filtro.getIdsFornecedores() !=null && !filtro.getIdsFornecedores().isEmpty()) {
			query.setParameterList("fornecedor", filtro.getIdsFornecedores());
		}
				
		return query.list();
		
	}

	@Override
	public List<FornecedorDTO> obterDadosFornecedoresParaImpressaoBandeira(Integer semana, Long forncedorId) {
		// TODO Auto-generated method stub
		return null;
	}
}
