package br.com.abril.nds.integracao.ems0197.processor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.dto.DetalhesPickingDTO;
import br.com.abril.nds.dto.IpvLancamentoDTO;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.ftfutil.FTFParser;
import br.com.abril.nds.integracao.ems0197.outbound.EMS0197Detalhe;
import br.com.abril.nds.integracao.ems0197.outbound.EMS0197Header;
import br.com.abril.nds.integracao.ems0197.outbound.EMS0197Trailer;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.integracao.DistribuidorService;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

@Component
public class EMS0197MessageProcessor extends AbstractRepository implements MessageProcessor {

	private static final String REPARTE_FOLDER = "reparte";

	private static final String REPARTE_EXT = ".LCT";

	@Autowired
	private FixedFormatManager fixedFormatManager;

	@Autowired
	private DistribuidorService distribuidorService;

	@Autowired
	private DescontoService descontoService; 	
	
	private Date dataLctoDistrib;

	/** Quantidade de arquivos processados. */
	private int quantidadeArquivosGerados = 0;
	
	@Override
	public void processMessage(Message message) {
		
		this.quantidadeArquivosGerados = 0;
		
		List<EMS0197Header> listHeaders = this.criarHeader(dataLctoDistrib);
		
		for (EMS0197Header outheader : listHeaders){
		
		try{

			String nomeArquivo = ""+outheader.getCodDistribuidor()+"."+outheader.getNumeroCota()+"."+outheader.getDataLctoDistrib();
			
			PrintWriter print = new PrintWriter(new FileWriter(
					message.getHeader().get(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO.name())
					+ File.separator + REPARTE_FOLDER +File.separator + nomeArquivo + REPARTE_EXT));
			
			List<IpvLancamentoDTO> listDetalhes = getDetalhesPickingLancamento(outheader.getIdCota(), this.dataLctoDistrib);
			
			for (IpvLancamentoDTO dto : listDetalhes) {
				
				EMS0197Detalhe outDetalhe = createDetalhes(dto);

				print.println(fixedFormatManager.export(outDetalhe));
			}
				
//			EMS0197Trailer outTrailer = createTrailer(outheader.getNumeroCota(), listDetalhes.size());
			
//			print.println(fixedFormatManager.export(outTrailer));
				
			print.flush();
			print.close();
					
			this.quantidadeArquivosGerados++;
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
		
		
		
//		List<EMS0197Header> listHeaders = this.criarHeader(dataLctoDistrib);
		
//		for (EMS0197Header outheader : listHeaders){
//
//			try{
//				
//				String nomeArquivo = String.format("%1$05d%2$s", 
//						Integer.parseInt(outheader.getNumeroCota()), outheader.getFormatedDate()); 												
//				
//				PrintWriter print = new PrintWriter(new FileWriter(
//						message.getHeader().get(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO.name())
//						+ File.separator + REPARTE_FOLDER +File.separator + nomeArquivo + REPARTE_EXT));
//				
//				print.println(fixedFormatManager.export(outheader));
//			
//				List<DetalhesPickingDTO> listDetalhes = getDetalhesPicking(outheader.getIdCota(), this.dataLctoDistrib);
//				
//				for(DetalhesPickingDTO pickingDTO : listDetalhes){
//					
//					EMS0197Detalhe outDetalhe = createDetalhes(pickingDTO);
//
//					print.println(fixedFormatManager.export(outDetalhe));
//				}
//					
//				EMS0197Trailer outTrailer = createTrailer(outheader.getNumeroCota(), listDetalhes.size());
//				
//				print.println(fixedFormatManager.export(outTrailer));
//					
//				print.flush();
//				print.close();
//						
//				this.quantidadeArquivosGerados++;
//			}
//			catch(IOException e){
//				
//			}
//		}
	}

	/**
	 * Cria o trailer do arquivo
	 * 
	 * @param jornaleiro
	 * @return
	 */
	private EMS0197Trailer createTrailer(String numeroCota, Integer qtdRegistros) {
		
		EMS0197Trailer outTrailer = new EMS0197Trailer();
		
		outTrailer.setNumeroCota(numeroCota);
		outTrailer.setQtdeRegTipo2(qtdRegistros);
		
		return outTrailer;
	}

	/**
	 * Cria os detalhes do arquivo
	 * 
	 * @param jornaleiro
	 * @param movimentoEstoqueCota
	 * @return
	 */
	private EMS0197Detalhe createDetalhes(IpvLancamentoDTO dto) {
		
		EMS0197Detalhe outDetalhe = new EMS0197Detalhe();
		
		String detalhe = "";
		
		FTFParser ftfParser = new FTFParser();
		
		try {
			detalhe = ftfParser.parseArquivo(dto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		outDetalhe.setDetalhes(detalhe);
		
//		outDetalhe.setCodigoCota(pickingDTO.getNumeroCota().toString());
//		
//		outDetalhe.setCodProduto(pickingDTO.getCodigoProduto());
//		
//		outDetalhe.setNumEdicao(pickingDTO.getCodigoEdicao().toString());
//		
//		outDetalhe.setNomeProduto(pickingDTO.getNomeProduto());
//		
//		outDetalhe.setCodigoDeBarrasPE(pickingDTO.getCodigoDeBarrasProdutoEdicao());
//		
//		outDetalhe.setPrecoCustoPE(pickingDTO.getPrecoCustoProdutoEdicao().toString());
//		
//		outDetalhe.setPrecoVendaPE(pickingDTO.getPrecoVendaProdutoEdicao().toString());												
//		
//		outDetalhe.setDescontoPE(pickingDTO.getValorDescontoMEC().toString());
//		
//		outDetalhe.setQtdeMEC(pickingDTO.getQtdeMEC().toString());
		
		return outDetalhe;
	}

	
	@SuppressWarnings("unchecked")
	public List<EMS0197Header> criarHeader(Date data) {

		StringBuilder sql = new StringBuilder();
		
		sql.append(" select distinct(c.id) as idCota, c.numero_cota as numeroCota");
		sql.append(" , pdv.NOME as nomePDV "); 
		sql.append(" , mec.data as dataLctoDistrib ");
		sql.append(" ,(select d.COD_DISTRIBUIDOR_DINAP from distribuidor d limit 1) as codDistribuidor  ");
		sql.append(" from cota c ");
		sql.append(" join pdv pdv on pdv.cota_id = c.id  ");
		sql.append(" join movimento_estoque_cota mec on mec.cota_id = c.ID ");
		sql.append(" join tipo_movimento tm on tm.id = mec.TIPO_MOVIMENTO_ID ");
		sql.append(" where mec.DATA = :data ");
		sql.append(" and pdv.PONTO_PRINCIPAL = true ");
		sql.append(" and tm.GRUPO_MOVIMENTO_ESTOQUE in (:grupos) ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("data", data);
		query.setParameterList("grupos", 
				Arrays.asList( 
						GrupoMovimentoEstoque.RECEBIMENTO_JORNALEIRO_JURAMENTADO.name(),
						GrupoMovimentoEstoque.SOBRA_DE_COTA.name(), 
						GrupoMovimentoEstoque.SOBRA_EM_COTA.name(),
						GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name(), 
						GrupoMovimentoEstoque.RATEIO_REPARTE_COTA_AUSENTE.name(),
						GrupoMovimentoEstoque.RESTAURACAO_REPARTE_COTA_AUSENTE.name()
				));
		
		query.addScalar("idCota", StandardBasicTypes.LONG);
		query.addScalar("dataLctoDistrib", StandardBasicTypes.DATE);
		query.addScalar("numeroCota", StandardBasicTypes.STRING );
		query.addScalar("nomePDV", StandardBasicTypes.STRING );
		query.addScalar("codDistribuidor", StandardBasicTypes.STRING );
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EMS0197Header.class));
		
		return (List<EMS0197Header>) query.list();
	}
	
	/**
	 * 
	 * @param idCota
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DetalhesPickingDTO> getDetalhesPicking(Long idCota, Date data) {
		
		StringBuilder sql = new StringBuilder();

		sql.append(" select c.id as idCota ");
		sql.append(" ,c.numero_cota as numeroCota ");
		sql.append(" ,mec.qtde as qtdeMEC ");
		sql.append(" ,CAST(p.CODIGO as UNSIGNED) as codigoProduto ");
		sql.append(" ,p.nome as nomeProduto ");
		sql.append(" ,l.sequencia_matriz as sequenciaMatriz ");
		sql.append(" ,pe.numero_edicao as codigoEdicao ");
		sql.append(" ,pe.preco_custo as precoCustoProdutoEdicao ");
		sql.append(" ,pe.preco_venda as precoVendaProdutoEdicao ");
		sql.append(" ,mec.valor_desconto as valorDescontoMEC ");
		sql.append(" ,pe.codigo_de_barras as codigoDeBarrasProdutoEdicao ");
		sql.append(" from movimento_estoque_cota mec ");
		sql.append(" join lancamento l on l.id = mec.lancamento_id ");
		sql.append(" join cota c on mec.COTA_ID = c.ID  ");
		sql.append(" join produto_edicao pe on pe.id = mec.produto_edicao_id ");
		sql.append(" join produto p on p.id = pe.produto_id ");
		sql.append(" join tipo_movimento tm on tm.id = mec.TIPO_MOVIMENTO_ID ");
		sql.append(" where mec.data = :data ");
		sql.append(" and c.id = :idCota ");
		sql.append(" and tm.GRUPO_MOVIMENTO_ESTOQUE in (:grupos) ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("data", data);
		query.setParameter("idCota", idCota);
		query.setParameterList("grupos", 
				Arrays.asList( 
						GrupoMovimentoEstoque.RECEBIMENTO_JORNALEIRO_JURAMENTADO.name(),
						GrupoMovimentoEstoque.SOBRA_DE_COTA.name(), 
						GrupoMovimentoEstoque.SOBRA_EM_COTA.name(),
						GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name(), 
						GrupoMovimentoEstoque.RATEIO_REPARTE_COTA_AUSENTE.name(),
						GrupoMovimentoEstoque.RESTAURACAO_REPARTE_COTA_AUSENTE.name()
				));
		
		query.addScalar("idCota", StandardBasicTypes.LONG);
		query.addScalar("numeroCota", StandardBasicTypes.INTEGER);
		query.addScalar("qtdeMEC", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("codigoProduto", StandardBasicTypes.STRING);
		query.addScalar("codigoEdicao", StandardBasicTypes.LONG);
		query.addScalar("codigoDeBarrasProdutoEdicao", StandardBasicTypes.STRING);
		query.addScalar("nomeProduto", StandardBasicTypes.STRING);
		query.addScalar("sequenciaMatriz", StandardBasicTypes.INTEGER);
		query.addScalar("precoCustoProdutoEdicao", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("precoVendaProdutoEdicao", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("valorDescontoMEC", StandardBasicTypes.BIG_DECIMAL);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(DetalhesPickingDTO.class));

		return (List<DetalhesPickingDTO>) query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<IpvLancamentoDTO> getDetalhesPickingLancamento(Long idCota, Date data) {
		
		StringBuilder sql = new StringBuilder();

		sql.append(" select                                                                                            ");
		sql.append("      	CAST((select d.COD_DISTRIBUIDOR_DINAP from distribuidor d limit 1) as CHAR) as codDistribuidor               ");
		sql.append("      , CAST(c.PESSOA_ID as CHAR) as codJornaleiro                                                              ");
		sql.append("      , CAST(c.NUMERO_COTA as CHAR) as codCota                                                                  ");
		sql.append("      , CAST(pdv.ID as CHAR) as codPDV                                                                          ");
		sql.append("      , DATE_FORMAT((mec.DATA),'%Y%m%d') as dataMovimento                                       ");
		sql.append("      , CAST(SUBSTRING(p.CODIGO, -8) as CHAR) as codProduto                                                                 ");
		sql.append("      , CAST(pe.NUMERO_EDICAO as CHAR) as numEdicao                                                                 ");
		sql.append("      , CAST(pe.CODIGO_DE_BARRAS as CHAR) as codBarras                                                      ");
		sql.append("      , p.NOME as nomeProduto                                                                                ");
		sql.append("      , CAST(ROUND(mec.QTDE, 0) as CHAR) as reparte                                                                              ");
		sql.append("      , pes.RAZAO_SOCIAL as nomeEditora                                                                       ");
		sql.append("      , CAST(ROUND(ROUND(mec.PRECO_VENDA, 2)*100, 0) as CHAR)  as precoCapa                                                   ");
		sql.append("      , CAST(ROUND(ROUND(mec.PRECO_COM_DESCONTO, 2)*100, 0) as CHAR) as precoCusto                                         ");
		sql.append("      , pe.CHAMADA_CAPA  as  chamadaCapa                                                                          ");
		sql.append("      , DATE_FORMAT((mec.DATA),'%Y%m%d') as dataLancamento                                            ");
		sql.append("      , DATE_FORMAT((select me.DATA from movimento_estoque_cota me                                             ");
		sql.append("           where me.PRODUTO_EDICAO_ID = mec.PRODUTO_EDICAO_ID and me.COTA_ID = mec.COTA_ID         ");
		sql.append("           group by me.LANCAMENTO_ID order by me.DATA asc limit 1),'%Y%m%d') as dataPrimeiroLancamentoParcial     ");
		sql.append("                                                                                                   ");
		
		sql.append("   from movimento_estoque_cota mec                                                                 ");
		sql.append("	 join lancamento l on l.id = mec.lancamento_id                                                 ");
		sql.append("	 join cota c on mec.COTA_ID = c.ID                                                             ");
		sql.append("	 join produto_edicao pe on pe.id = mec.produto_edicao_id                                       ");
		sql.append("	 join produto p on p.id = pe.produto_id                                                        ");
		sql.append("	 join tipo_movimento tm on tm.id = mec.TIPO_MOVIMENTO_ID                                       ");
		sql.append(" 	 join pdv on pdv.COTA_ID = c.ID                                                                ");
		sql.append(" 	 join editor edt ON p.EDITOR_ID = edt.ID                                                       ");
		sql.append(" 	 join pessoa pes ON edt.JURIDICA_ID = pes.ID                                                   ");
		sql.append(" where                                                                                             ");
		sql.append(" 	 mec.data = :data                                                                       	   ");
		sql.append(" 	 and c.id = :idCota                                                                            ");
		sql.append("	 and tm.GRUPO_MOVIMENTO_ESTOQUE in (:grupos)												   ");
		
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("data", data);
		query.setParameter("idCota", idCota);
		query.setParameterList("grupos", 
				Arrays.asList( 
						GrupoMovimentoEstoque.RECEBIMENTO_JORNALEIRO_JURAMENTADO.name(),
						GrupoMovimentoEstoque.SOBRA_DE_COTA.name(), 
						GrupoMovimentoEstoque.SOBRA_EM_COTA.name(),
						GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name(), 
						GrupoMovimentoEstoque.RATEIO_REPARTE_COTA_AUSENTE.name(),
						GrupoMovimentoEstoque.RESTAURACAO_REPARTE_COTA_AUSENTE.name()
				));
		
		query.addScalar("codDistribuidor", StandardBasicTypes.STRING);
		query.addScalar("codJornaleiro", StandardBasicTypes.STRING);
		query.addScalar("codCota", StandardBasicTypes.STRING);
		query.addScalar("codPDV", StandardBasicTypes.STRING);
		query.addScalar("dataMovimento", StandardBasicTypes.STRING);
		query.addScalar("codProduto", StandardBasicTypes.STRING);
		query.addScalar("numEdicao", StandardBasicTypes.STRING);
		query.addScalar("codBarras", StandardBasicTypes.STRING);
		query.addScalar("nomeProduto", StandardBasicTypes.STRING);
		query.addScalar("reparte", StandardBasicTypes.STRING);
		query.addScalar("nomeEditora", StandardBasicTypes.STRING);
		query.addScalar("precoCapa", StandardBasicTypes.STRING);
		query.addScalar("precoCusto", StandardBasicTypes.STRING);
		query.addScalar("chamadaCapa", StandardBasicTypes.STRING);
		query.addScalar("dataLancamento", StandardBasicTypes.STRING);
		query.addScalar("dataPrimeiroLancamentoParcial", StandardBasicTypes.STRING);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(IpvLancamentoDTO.class));

		return (List<IpvLancamentoDTO>) query.list();
	}
						
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
		
	}

	public void setDataLctoDistrib(Date dataLctoDistrib) {
		this.dataLctoDistrib = dataLctoDistrib;		
	}
				

	/**
	 * Retorna a quantidade de arquivos gerados apos o processamento.
	 * 
	 * @return
	 */
	public int getQuantidadeArquivosGerados() {
		return quantidadeArquivosGerados;
	}
	
}

