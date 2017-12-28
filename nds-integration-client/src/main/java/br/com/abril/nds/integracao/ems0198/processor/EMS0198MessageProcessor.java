package br.com.abril.nds.integracao.ems0198.processor;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.dto.CotaCouchDTO;
import br.com.abril.nds.dto.IpvRecolhimentoDTO;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.ftfutil.FTFParser;
import br.com.abril.nds.integracao.ems0198.outbound.EMS0198Detalhe;
import br.com.abril.nds.integracao.ems0198.outbound.EMS198HeaderDTO;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.service.ControleCotaService;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.ExporteCouch;
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
	private CotaRepository cotaRepository;
	
	@Autowired
	private ControleCotaService controleCotaService;
	
	@Autowired
	private ExporteCouch exporteCouch;

	private Date dataLctoDistrib;

	private int quantidadeArquivosGerados = 0;
	
	
	public void processMessage(Message message) {
		
		this.quantidadeArquivosGerados = 0;
		
		List<EMS198HeaderDTO> cotasRecolhimento = getHeader(message);
		
		for (EMS198HeaderDTO cotaDTO : cotasRecolhimento) {
			
			List<IpvRecolhimentoDTO> cecs = listRecolhimento(message, cotaDTO.getCotaId());
			
			PrintWriter print = geraArquivo(message, cotaDTO.getCodDistribuidor(), cotaDTO.getNumCota(), cotaDTO.getDataRecolhimento());
			
			for (IpvRecolhimentoDTO cec : cecs) {
				
				EMS0198Detalhe outDetalhe = createDetalhes(cec);
				
				print.write(fixedFormatManager.export(outDetalhe), 0, 196);
				print.print("\r\n");
			}
			print.flush();
			print.close();

		}

		if(!cotasRecolhimento.isEmpty()) {

			this.quantidadeArquivosGerados = cotasRecolhimento == null ? 0 : cotasRecolhimento.size();

			try {

				Iterator it = FileUtils.iterateFiles(new File(message.getHeader().get(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO.name())
						+ File.separator + ENCALHE_FOLDER), new String[]{"RCL"}, false);


				while (it.hasNext()) {
					File file = (File) it.next();
					String name = file.getName(); // nome neste formato 0757350.00023.20151005.RCL
					Integer cota = Integer.parseInt(name.split("\\.")[1]);
					Integer cotaMaster = controleCotaService.buscarCotaMaster(cota);
					if (cotaMaster != null && !cotaMaster.equals(cota)) { // tem cota master, appendar no arquivo
						String nomeArquivoMaster = "" + name.split("\\.")[0] + "." + StringUtils.leftPad(cotaMaster.toString(), 5, '0') + "." + (name.split("\\.")[2]);

						File fileMaster = new File(
								message.getHeader().get(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO.name())
										+ File.separator + ENCALHE_FOLDER + File.separator + nomeArquivoMaster + ENCALHE_EXT);
						if (!fileMaster.exists())
							this.quantidadeArquivosGerados++;
						cat(file, fileMaster);
						file.delete();
					}


				}
			} catch (Exception ee) {

				LOGGER.error("Falha ao gerar arquivo caruso.", ee);
			}

			compactarArquivos(message);
		}
		
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			List<CotaCouchDTO> lista = cotaRepository.getCotaRecolhimento(dataLctoDistrib);

			if(!lista.isEmpty()) {
				for (CotaCouchDTO reparte : lista) {
					reparte.setProdutos(cotaRepository.getProdutoRecolhimento(reparte.getIdCota(), simpleDateFormat.parse(reparte.getDataMovimento())));
				}
				exporteCouch.exportarLancamentoRecolhimento(lista, "Recolhimento");
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
	}

	private void compactarArquivos(Message message) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dir = message.getHeader().get(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO.name()) + File.separator + ENCALHE_FOLDER +File.separator;
		File diretorio = new File(dir); 
		FileOutputStream fos = null;
		ZipOutputStream zipOut = null;
		FileInputStream fis = null;
		try {
			
			fos = new FileOutputStream(message.getHeader().get(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO.name()) 
						+ File.separator + ENCALHE_FOLDER + File.separator +"zip"+ File.separator +"encalhe-"+ sdf.format(dataLctoDistrib) +".zip");
			zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
			for(File input : diretorio.listFiles()) {
				
				if(input.isDirectory()) {
					continue;
				}
				
				fis = new FileInputStream(input);
				ZipEntry ze = new ZipEntry(input.getName());
			
				zipOut.putNextEntry(ze);
				byte[] tmp = new byte[4 * 1024];
				int size = 0;
				while((size = fis.read(tmp)) != -1) {
					
					zipOut.write(tmp, 0, size);
				}
				zipOut.flush();
				fis.close();
			}
			
			zipOut.close();
			
			for(File input : diretorio.listFiles()) {
				
				if(input.isDirectory()) {
					continue;
				}
				
				input.delete();
			}
			
		} catch (FileNotFoundException e) {
			
			LOGGER.error("Falha ao obter arquivo.", e);
		} catch (IOException e) {
			
			LOGGER.error("IOException", e);
		} finally {
			try {
				
				if(fos != null) { 
					fos.close();
				}
			} catch(Exception ex) {

				LOGGER.error("Falha ao fechar arquivo.", ex);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private List<IpvRecolhimentoDTO> listRecolhimento(Message message, String idCota){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("  select  ");

		sql.append("       CAST((select if (d.COD_DISTRIBUIDOR_DINAP != 0,d.COD_DISTRIBUIDOR_DINAP,d.COD_DISTRIBUIDOR_FC) from distribuidor d limit 1) as CHAR) codDistribuidor ");
		sql.append("      , CAST(coalesce(c.numero_jornaleiro_ipv,c.PESSOA_ID) AS CHAR) AS codJornaleiro ");
		sql.append("      , CAST(c.NUMERO_COTA as CHAR) as codCota ");
		sql.append("      , CAST(coalesce(pdv.numero_pdv,pdv.ID) AS CHAR )  AS codPDV ");
		sql.append("      , DATE_FORMAT((ce.DATA_RECOLHIMENTO),'%Y%m%d') as dataMovimento ");
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
		sql.append(" 		 LEFT join pdv on pdv.COTA_ID = c.ID and pdv.PONTO_PRINCIPAL is true ");
		sql.append("      LEFT join editor edt ON p.EDITOR_ID = edt.ID ");
		sql.append("      join pessoa pes ON edt.JURIDICA_ID = pes.ID  ");
		sql.append("      join movimento_estoque_cota mec on mec.LANCAMENTO_ID = l.ID and mec.COTA_ID = c.ID ");

		sql.append("      where  ");
		sql.append("      ce.DATA_RECOLHIMENTO = :dataRecolhimento ");
		sql.append("      and c.id = :idCota ");
		sql.append("      and c.UTILIZA_IPV = :true ");
		sql.append("      group by c.NUMERO_COTA,ce.PRODUTO_EDICAO_ID");
		sql.append("      order by ce.SEQUENCIA, c.NUMERO_COTA, ce.PRODUTO_EDICAO_ID  ");
		
		
		SQLQuery query = getSession().createSQLQuery(sql.toString()); 
		
		query.setParameter("dataRecolhimento", this.dataLctoDistrib);
		query.setParameter("idCota", idCota);
		query.setParameter("true", true);
		
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
		
		query.setResultTransformer(new AliasToBeanResultTransformer(IpvRecolhimentoDTO.class));
		
		List<IpvRecolhimentoDTO> encalhes = (List<IpvRecolhimentoDTO>) query.list();

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
		sql.append("     CAST((if (dtb.COD_DISTRIBUIDOR_DINAP != 0,dtb.COD_DISTRIBUIDOR_DINAP,dtb.COD_DISTRIBUIDOR_FC)) as CHAR) as codDistribuidor ");
		sql.append("  from distribuidor dtb, chamada_encalhe ce ");
		sql.append("   join chamada_encalhe_cota cec on cec.CHAMADA_ENCALHE_ID = ce.ID ");
		sql.append("   join cota ON cec.COTA_ID = cota.ID ");
		sql.append("   join pdv on pdv.COTA_ID = cota.ID ");
		sql.append("  where pdv.PONTO_PRINCIPAL = true  ");
		sql.append(" 		and ce.DATA_RECOLHIMENTO = :dataRecolhimento ");
		sql.append(" 		and cota.UTILIZA_IPV = true ");
		sql.append(" 		and cota.tipo_transmissao = 'TXT'");
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

			return Collections.emptyList();
		} else {
			return CotasEncalhes;
		}
	}
	
	private PrintWriter geraArquivo(Message message, String codDistrb, String numCota, String dataRec) {
		
		try {
			 
			String nomeArquivo = ""+codDistrb+"."+StringUtils.leftPad(numCota, 5, '0')+"."+dataRec;

			PrintWriter print = new PrintWriter(new FileWriter(Paths.get(message.getHeader().get(
					TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO.name()) + File.separator + ENCALHE_FOLDER + File.separator + nomeArquivo + ENCALHE_EXT).toString()));
			
			return print;
			
		} catch (IOException e) {
			
			LOGGER.error(e.getMessage(), e);
			
			throw new RuntimeException("Falha na geracao do Arquivo!");					

		}
						
	}
	
	private EMS0198Detalhe createDetalhes(IpvRecolhimentoDTO dto) {
		
		EMS0198Detalhe outDetalhe = new EMS0198Detalhe();
		
		String detalhe = "";
		
		FTFParser ftfParser = new FTFParser();
		
		try {
			detalhe = ftfParser.parseArquivo(dto);
		} catch (Exception e) {
			
			LOGGER.error("Erro no parse do arquivo.", e);
		}
		
		outDetalhe.setDetalhes(detalhe);
		
		return outDetalhe;
	}

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
	
    public void cat (File origem, File destino) throws IOException {
		
		FileInputStream fis = new FileInputStream(origem);
		BufferedReader in = new BufferedReader(new InputStreamReader(fis));
		               
 
		FileWriter fstream = new FileWriter(destino, true);
		BufferedWriter out = new BufferedWriter(fstream);
 
		String aLine = null;
		while ((aLine = in.readLine()) != null) {
			//Process each line and add output to Dest.txt file
			out.write(aLine);
			out.newLine();
		}
 
	
		in.close();

		out.close();
	}
	
}