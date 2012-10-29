package br.com.abril.nds.repository.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotasImpressaoNfeDTO;
import br.com.abril.nds.dto.filtro.FiltroImpressaoNFEDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.ImpressaoNFeRepository;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class ImpressaoNFeRepositoryImpl extends AbstractRepositoryModel<NotaFiscal, Long> implements ImpressaoNFeRepository {

	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	public ImpressaoNFeRepositoryImpl() {
		super(NotaFiscal.class);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<CotasImpressaoNfeDTO> buscarCotasParaImpressaoNFe(FiltroImpressaoNFEDTO filtro) {

		StringBuilder sql = new StringBuilder();
		sql.append("select new br.com.abril.nds.dto.CotasImpressaoNfeDTO(cota, SUM(nf.informacaoValoresTotais.valorProdutos) as vlrTotal, SUM(nf.informacaoValoresTotais.valorDesconto) as vlrTotalDesconto) ");
		
		//Complementa o HQL com as clausulas de filtro
		Query q = montarFiltroConsultaNfeParaImpressao(filtro, sql, filtro.getPaginacao());

		return q.list();
		
	}

	@Transactional(readOnly=true)
	public Integer buscarCotasParaImpressaoNFeQtd(FiltroImpressaoNFEDTO filtro) {

		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) ");
		
		//Complementa o HQL com as clausulas de filtro
		Query q = montarFiltroConsultaNfeParaImpressao(filtro, sql, null);
		
		return q.list().size();
	}
	
	@Override
	public List<CotasImpressaoNfeDTO> buscarCotasParaImpressaoNotaEnvio(FiltroImpressaoNFEDTO filtro) {
		StringBuilder sql = new StringBuilder();
		sql.append("select new br.com.abril.nds.dto.CotasImpressaoNfeDTO(cota, SUM(nei.precoCapa), SUM(nei.desconto)) ");
		
		//Complementa o HQL com as clausulas de filtro
		Query q = montarFiltroConsultaNotaEnvioParaImpressao(null, filtro, sql, filtro.getPaginacao());

		return q.list();
	}

	@Override
	public Integer buscarCotasParaImpressaoNotaEnvioQtd(FiltroImpressaoNFEDTO filtro) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) ");
		
		//Complementa o HQL com as clausulas de filtro
		Query q = montarFiltroConsultaNotaEnvioParaImpressao(null, filtro, sql, null);
		
		return q.list().size();
	}
	
	@Override
	public List<NotaFiscal> buscarNotasPorCotaParaImpressaoNFe(Cota cota, FiltroImpressaoNFEDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select nf ");
		
		//Complementa o HQL com as clausulas de filtro
		Query q = montarFiltroConsultaNfeParaImpressao(filtro, sql, filtro.getPaginacao());

		return q.list();
	}
	
	@Override
	public List<NotaEnvio> buscarNotasEnvioPorCotaParaImpressaoNFe(Cota cota, FiltroImpressaoNFEDTO filtro) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ne ");
		
		//Complementa o HQL com as clausulas de filtro
		Query q = montarFiltroConsultaNotaEnvioParaImpressao(cota, filtro, sql, filtro.getPaginacao());

		return q.list();
	}
	
	//Torna reaproveitavel a parte de filtro da query
	private Query montarFiltroConsultaNfeParaImpressao(FiltroImpressaoNFEDTO filtro, StringBuilder sql, PaginacaoVO paginacao) {
		
		Distribuidor distribuidor = distribuidorRepository.obter();
		
		if(filtro == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "O filtro não pode ser nulo ou estar vazio.");
		}
		
		sql.append("from NotaFiscal nf, Cota cota ");
		
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
		sql.append("and nf.identificacaoDestinatario.pessoaDestinatarioReferencia.id = cota.pessoa.id ");
		sql.append("and nf.identificacao.dataEmissao = :dataEmissao ");
		
		if(distribuidor.getObrigacaoFiscal() != null) {
			sql.append("and nf.informacaoEletronica.retornoComunicacaoEletronica.status = :statusNFe ");
		}
		
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
		
		if(distribuidor.getObrigacaoFiscal() != null) {
			q.setParameter("statusNFe", br.com.abril.nds.model.fiscal.nota.Status.AUTORIZADO );
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
		private Query montarFiltroConsultaNotaEnvioParaImpressao(Cota cota, FiltroImpressaoNFEDTO filtro, StringBuilder sql, PaginacaoVO paginacao) {
			
			Distribuidor distribuidor = distribuidorRepository.obter();
			
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
			
			if(cota != null && cota.getId() != null) {
				sql.append("and cota.id = :idCota ");
			}
			
			if(filtro.getIdsCotas() != null && filtro.getIdsCotas().size() > 0) {	
				sql.append("and cota.id in (:idsCotas) ");
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
			
			if(filtro.getTipoNFe() != null && Long.parseLong(filtro.getTipoNFe()) > -1) {
				q.setParameter("tipoNotaFiscal", Long.parseLong(filtro.getTipoNFe()) );
			}
			
			if(distribuidor.getObrigacaoFiscal() != null) {
				q.setParameter("statusNFe", br.com.abril.nds.model.fiscal.nota.Status.AUTORIZADO );
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
			
			if(cota != null && cota.getId() != null) {
				q.setParameter("idCota", cota.getId());
			}
			
			if(filtro.getIdsCotas() != null && filtro.getIdsCotas().size() > 0) {
				q.setParameterList("idsCotas", filtro.getIdsCotas());
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

		@Override
		public List<Produto> buscarProdutosParaImpressaoNFe(FiltroImpressaoNFEDTO filtro) {

			if(filtro == null) {
				throw new ValidacaoException(TipoMensagem.ERROR, "O filtro não pode ser nulo ou estar vazio.");
			}
			
			StringBuilder sql = new StringBuilder();
			sql.append("select distinct p ");
			sql.append("from Lancamento l join l.produtoEdicao pe join pe.produto p join p.fornecedores f ");
			sql.append("where l.dataLancamentoDistribuidor between :dataMovimentoInicial and :dataMovimentoFinal ");
			
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
			q.setParameterList("idsFornecedores", filtro.getIdsFornecedores());
			
			if(filtro.getCodigoProduto() != null) {
				q.setParameter("codigoProduto", "%"+ filtro.getCodigoProduto() +"%");
			}
			
			if(filtro.getNomeProduto() != null) {
				q.setParameter("nomeProduto", "%"+ filtro.getNomeProduto() +"%");
			}

			return q.list();
		}

}
