package br.com.abril.nds.integracao.ems0198.processor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.dto.IPV_recolhimentoDTO;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.ftfutil.FTFParser;
import br.com.abril.nds.integracao.ems0198.outbound.EMS0198Detalhe;
import br.com.abril.nds.integracao.ems0198.outbound.EMS0198Header;
import br.com.abril.nds.integracao.ems0198.outbound.EMS0198Trailer;
import br.com.abril.nds.integracao.ems0198.outbound.EMS198HeaderDTO;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.integracao.DistribuidorService;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

@Component
public class EMS0198MessageProcessor extends AbstractRepository implements MessageProcessor {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EMS0198MessageProcessor.class);

	private static final String ENCALHE_FOLDER = "encalhe";

	private static final String ENCALHE_EXT = ".RCL";

	@Autowired
	private FixedFormatManager fixedFormatManager;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private DescontoService descontoService;
	
	@Autowired
	private DistribuidorService distribuidorService;

	private Date dataLctoDistrib;

	private static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
	
	private int quantidadeArquivosGerados = 0;
	
	
	public void processMessage(Message message) {
		
		this.quantidadeArquivosGerados = 0;
		
		List<EMS198HeaderDTO> cotasRecolhimento = getHeader(message);
		
		for (EMS198HeaderDTO cotaDTO : cotasRecolhimento) {
			
			List<IPV_recolhimentoDTO> cecs = listRecolhimento(message, cotaDTO.getCotaId());
			
			PrintWriter print = geraArquivo(message, cotaDTO.getCodDistribuidor(), cotaDTO.getNumCota(), cotaDTO.getDataRecolhimento());
			
			for (IPV_recolhimentoDTO cec : cecs) {
				
				EMS0198Detalhe outDetalhe = createDetalhes(cec);
				
				print.println(fixedFormatManager.export(outDetalhe));
			}
			print.flush();
			print.close();

		}
		
		this.quantidadeArquivosGerados = cotasRecolhimento == null ? 0 : cotasRecolhimento.size();
	}
	
	
// ## Conforme alinhado com César Marracho, há a necessidade de manter a impletação antiga, para uma possível retorno do layout antigo	
//	@Override
//	public void processMessage(Message message) {
//		
//		this.quantidadeArquivosGerados = 0;
//		
//		List<ChamadaEncalheCota> cecs = findListPDV(message);
//		
//		Integer numerCota =  0;
//		
//		int qtdeRegistros = 0;
//		
//		PrintWriter print =  null;
//		
//		for (ChamadaEncalheCota cec: cecs) {
//			
//			if (numerCota == cec.getCota().getNumeroCota()) {
//		
//				qtdeRegistros++;
//			
//			} else {
//			
//				if (!numerCota.equals(0)) {
//		
//					criaTrailer(print, numerCota, qtdeRegistros);
//					
//					print.flush();
//					print.close();					
//				}
//				
//				numerCota = cec.getCota().getNumeroCota();
//				
//				qtdeRegistros = 0;
//				
//				print = geraArquivo(message, this.dataLctoDistrib, cec.getCota().getPessoa().getNome(), numerCota);
//			}			
//			
//			criaDetalhes(print, cec);
//		}
//		if (!numerCota.equals(0)) {
//		
//			criaTrailer(print, numerCota, qtdeRegistros);
//			
//			print.flush();
//			print.close();					
//		}
//		
//	}


	@SuppressWarnings("unchecked")
	private List<IPV_recolhimentoDTO> listRecolhimento(Message message, String idCota){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("  select  ");

		sql.append("       CAST((select d.COD_DISTRIBUIDOR_DINAP from distribuidor d limit 1) as CHAR) codDistribuidor ");
		sql.append("      , CAST(c.PESSOA_ID as CHAR) as codJornaleiro ");
		sql.append("      , CAST(c.NUMERO_COTA as CHAR) as codCota ");
		sql.append("      , CAST(pdv.ID as CHAR) as codPDV ");
		sql.append("      , DATE_FORMAT((mec.DATA),'%Y%m%d') as dataMovimento ");
		sql.append("      , CAST(SUBSTRING(p.CODIGO, -8) as CHAR) as codProduto ");
		sql.append("      , CAST(pe.NUMERO_EDICAO as CHAR) as numEdicao ");
		sql.append("      , CAST(pe.CODIGO_DE_BARRAS as CHAR) as codBarras ");
		sql.append("      , p.NOME as nomeProduto");
		sql.append("      , CAST(ROUND(cec.QTDE_PREVISTA, 0) as CHAR) as reparte ");
		sql.append("      , pes.RAZAO_SOCIAL as nomeEditora ");
		sql.append("      , CAST(ROUND(ROUND(mec.PRECO_VENDA, 2)*100, 0) as CHAR) as precoCapa ");
		sql.append("      , CAST(ROUND(ROUND(mec.PRECO_COM_DESCONTO, 2)*100, 0) as CHAR) as precoCusto ");
		sql.append("      , pe.CHAMADA_CAPA as chamadaCapa ");
		sql.append("      , DATE_FORMAT((ce.DATA_RECOLHIMENTO),'%Y%m%d') as dataLancamento ");

		sql.append("      from chamada_encalhe ce  ");
		
		sql.append("      JOIN chamada_encalhe_cota cec ON cec.CHAMADA_ENCALHE_ID = ce.ID ");
		sql.append(" 		 join produto_edicao pe on ce.PRODUTO_EDICAO_ID = pe.ID ");
		sql.append(" 		 join cota c on cec.COTA_ID = c.ID  ");
		sql.append(" 		 join lancamento l on l.PRODUTO_EDICAO_ID = pe.ID ");
		sql.append(" 		 join produto p on p.id = pe.produto_id  ");
		sql.append(" 		 LEFT join pdv on pdv.COTA_ID = c.ID ");
		sql.append("      LEFT join editor edt ON p.EDITOR_ID = edt.ID ");
		sql.append("      join pessoa pes ON edt.JURIDICA_ID = pes.ID  ");
		sql.append("      join movimento_estoque_cota mec on mec.LANCAMENTO_ID = l.ID and mec.COTA_ID = c.ID ");

		sql.append("      where  ");
		
		sql.append("      pdv.PONTO_PRINCIPAL = true ");
		sql.append("      and ce.DATA_RECOLHIMENTO = :dataRecolhimento ");
		sql.append("      and c.id = :idCota ");
		sql.append("      order by c.NUMERO_COTA, ce.PRODUTO_EDICAO_ID ");
		
		
		SQLQuery query = getSession().createSQLQuery(sql.toString()); 
		
		query.setParameter("dataRecolhimento", this.dataLctoDistrib);
		query.setParameter("idCota", idCota);
		
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
		
		query.setResultTransformer(new AliasToBeanResultTransformer(IPV_recolhimentoDTO.class));
		
		List<IPV_recolhimentoDTO> encalhes = (List<IPV_recolhimentoDTO>) query.list();

		if (encalhes.isEmpty()) {

			message.getHeader().put(MessageHeaderProperties.FILE_NAME.getValue(), "");
			message.getHeader().put(MessageHeaderProperties.LINE_NUMBER.getValue(), 0);

			this.ndsiLoggerFactory.getLogger().logWarning(message,EventoExecucaoEnum.GERACAO_DE_ARQUIVO,"Nenhum registro encontrado!");

			throw new RuntimeException("Nenhum registro encontrado!");
		} else {
			return encalhes;
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<EMS198HeaderDTO> getHeader(Message message){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("  select  ");
		sql.append("     CAST(cec.COTA_ID as CHAR) as cotaId, ");
		sql.append("     CAST(cota.NUMERO_COTA as CHAR) as numCota, ");
		sql.append("     DATE_FORMAT((ce.DATA_RECOLHIMENTO),'%Y%m%d') dataRecolhimento, ");
		sql.append("     CAST(dtb.COD_DISTRIBUIDOR_DINAP as CHAR) as codDistribuidor ");
		sql.append("  from distribuidor dtb, chamada_encalhe ce ");
		sql.append("   join chamada_encalhe_cota cec on cec.CHAMADA_ENCALHE_ID = ce.ID ");
		sql.append("   join cota ON cec.COTA_ID = cota.ID ");
		sql.append("   join pdv on pdv.COTA_ID = cota.ID ");
		sql.append("  where pdv.PONTO_PRINCIPAL = true  ");
		sql.append(" 		and ce.DATA_RECOLHIMENTO = :dataRecolhimento ");
		sql.append(" 		group by cota.NUMERO_COTA ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString()); 
		
		query.setParameter("dataRecolhimento", this.dataLctoDistrib);
		
		query.addScalar("cotaId", StandardBasicTypes.STRING);
		query.addScalar("numCota", StandardBasicTypes.STRING);
		query.addScalar("dataRecolhimento", StandardBasicTypes.STRING);
		query.addScalar("codDistribuidor", StandardBasicTypes.STRING);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EMS198HeaderDTO.class));
		
		List<EMS198HeaderDTO> CotasEncalhes = (List<EMS198HeaderDTO>) query.list();

		if (CotasEncalhes.isEmpty()) {

			message.getHeader().put(MessageHeaderProperties.FILE_NAME.getValue(), "");
			message.getHeader().put(MessageHeaderProperties.LINE_NUMBER.getValue(), 0);

			this.ndsiLoggerFactory.getLogger().logWarning(message,EventoExecucaoEnum.GERACAO_DE_ARQUIVO,"Nenhum registro encontrado!");

			throw new RuntimeException("Nenhum registro encontrado!");
		} else {
			return CotasEncalhes;
		}
	}
	
	private PrintWriter geraArquivo(Message message, String codDistrb, String numCota, String dataRec) {
		
		try {
			 
			String nomeArquivo = ""+codDistrb+"."+numCota+"."+dataRec;

			PrintWriter print = new PrintWriter(new FileWriter(message.getHeader().get(
					TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO.name()) + File.separator + ENCALHE_FOLDER + File.separator + nomeArquivo + ENCALHE_EXT));
			
			return print;
			
		} catch (IOException e) {
			
			LOGGER.error(e.getMessage(), e);
			
			throw new RuntimeException("Falha na geracao do Arquivo!");					

		}
						
	}
	
	private EMS0198Detalhe createDetalhes(IPV_recolhimentoDTO dto) {
		
		EMS0198Detalhe outDetalhe = new EMS0198Detalhe();
		
		String detalhe = "";
		
		FTFParser ftfParser = new FTFParser();
		
		try {
			detalhe = ftfParser.parseArquivo(dto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		outDetalhe.setDetalhes(detalhe);
		
		return outDetalhe;
	}

	
	private List<ChamadaEncalheCota> findListPDV(Message message) {

		StringBuilder sql = new StringBuilder();

		sql.append(" select cec from ");
		sql.append(" ChamadaEncalhe ce ");
		sql.append(" join ce.chamadaEncalheCotas cec ");
		sql.append(" join cec.cota c ");
		sql.append(" join c.pdvs pdv ");


		sql.append(" where pdv.caracteristicas.pontoPrincipal = true ");
		sql.append(" and ce.dataRecolhimento = :dataRecolhimento ");
		sql.append(" order by c.numeroCota");

		Query query = this.getSession().createQuery(sql.toString());
		
		query.setParameter("dataRecolhimento", this.dataLctoDistrib);
		
		@SuppressWarnings("unchecked")
		List<ChamadaEncalheCota> chamadaEncalhe = (List<ChamadaEncalheCota>) query.list();

		if (chamadaEncalhe.isEmpty()) {

			message.getHeader().put(
					MessageHeaderProperties.FILE_NAME.getValue(), "");
			message.getHeader().put(
					MessageHeaderProperties.LINE_NUMBER.getValue(), 0);

			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.GERACAO_DE_ARQUIVO,
					"Nenhum registro encontrado!");

			throw new RuntimeException("Nenhum registro encontrado!");
		} else {

			return chamadaEncalhe;
		}
	}
	
	
	private PrintWriter geraArquivo(Message message, Date data, String nome, int numeroCota) {
		
		try {
			 
			String nomeArquivo = String.format("%1$05d%2$s", numeroCota, sdf.format(dataLctoDistrib));

			PrintWriter print = new PrintWriter(new FileWriter(message.getHeader().get(
					TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO.name()) + File.separator + ENCALHE_FOLDER + File.separator + nomeArquivo + ENCALHE_EXT));
			
			criaHeader(print, numeroCota, nome, data);
			
			return print;
			
		} catch (IOException e) {
			
			LOGGER.error(e.getMessage(), e);
			
			throw new RuntimeException("Falha na geracao do Arquivo!");					

		}
						
	}
	
	
	private void criaHeader(PrintWriter print, Integer numeroCota, String nome, Date data) {

		EMS0198Header outheader = new EMS0198Header();

		outheader.setCodigoCota(numeroCota.toString());
		outheader.setNomeCota(nome);
		outheader.setData(sdf.format(data));

		print.println(fixedFormatManager.export(outheader));
		
	}
	
	private void criaTrailer(PrintWriter print, Integer numeroCota,	Integer quantidadeRegistros) {

		EMS0198Trailer outtrailer = new EMS0198Trailer();

		outtrailer.setCodigoCota(numeroCota.toString());
		outtrailer.setQuantidadeRegistros(quantidadeRegistros.toString());
		
		print.println(fixedFormatManager.export(outtrailer));

		                /*
         * A quantidade de arquivos gerados é incrementado aqui pois
         * considera-se que o arquivo foi gerado corretamente.
         */
		this.quantidadeArquivosGerados++;
	}

//	private void criaDetalhes(PrintWriter print, ChamadaEncalheCota cec) {
//		
//		EMS0198Detalhe outdetalhe = new EMS0198Detalhe();
//
//				
//		outdetalhe.setCodigoCota(cec.getCota().getNumeroCota().toString());
//		outdetalhe.setCodigoProduto(cec.getChamadaEncalhe().getProdutoEdicao().getProduto().getCodigo());
//		outdetalhe.setEdicao(cec.getChamadaEncalhe().getProdutoEdicao().getNumeroEdicao().toString());
//		outdetalhe.setNomePublicacao(cec.getChamadaEncalhe().getProdutoEdicao().getProduto().getNome());
//		outdetalhe.setCodigoDeBarras(cec.getChamadaEncalhe().getProdutoEdicao().getCodigoDeBarras());
//		outdetalhe.setPrecoCusto((cec.getChamadaEncalhe().getProdutoEdicao().getPrecoCusto() != null ? cec.getChamadaEncalhe().getProdutoEdicao().getPrecoCusto().toString():""));
//		outdetalhe.setPrecoVenda((cec.getChamadaEncalhe().getProdutoEdicao().getPrecoVenda() != null ? cec.getChamadaEncalhe().getProdutoEdicao().getPrecoVenda().toString():""));			
//		outdetalhe.setDesconto(descontoService.obterDescontoPorCotaProdutoEdicao(null, cec.getCota().getId(), cec.getChamadaEncalhe().getProdutoEdicao()).toString());												
//		outdetalhe.setQuantidade(cec.getQtdePrevista().toString());
//					
//		outdetalhe.setDataEncalhe(sdf.format(cec.getChamadaEncalhe().getDataRecolhimento()));
//		outdetalhe.setDiaChamada("1");
//		
//		print.println(fixedFormatManager.export(outdetalhe));
//					
//	}

	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		

	}

	@Override
	public void posProcess(Object tempVar) {
		

	}

	public void setDataLctoDistrib(Date data) {
		this.dataLctoDistrib = data;
		
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
