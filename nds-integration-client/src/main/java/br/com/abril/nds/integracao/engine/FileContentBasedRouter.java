package br.com.abril.nds.integracao.engine;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

import org.apache.commons.io.filefilter.RegexFileFilter;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.engine.data.FileRouteTemplate;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.util.OSUtil;
import br.com.abril.nds.model.integracao.StatusExecucaoEnum;
import br.com.abril.nds.repository.AbstractRepository;


public abstract class FileContentBasedRouter extends AbstractRepository implements ContentBasedRouter {
	private static final Pattern WINDOWS_PATH_NORMALIZER = Pattern.compile("(?i:[a-z][:])");
	
	public abstract void routeFile(FileRouteTemplate fileRouteTemplate, File file);
	
	@Autowired
	NdsiLoggerFactory ndsiLoggerFactory;
	
	public <T extends RouteTemplate> void routeData(T route) {
		FileRouteTemplate fileRoute  = (FileRouteTemplate) route;
		
		File folder = new File(normalizeFileName(fileRoute.getInboundFolder()));
		
		File[] files = folder.listFiles((FilenameFilter) new RegexFileFilter(fileRoute.getFileFilterExpression()));
		
		if(files == null || files.length < 1){
			ndsiLoggerFactory.getLogger().setStatusProcesso(StatusExecucaoEnum.VAZIO);
			return;
		}
		
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			
			routeFile(fileRoute, file);
		}
	}
	
	public static String normalizeFileName(String filename) {
		if (! OSUtil.isWindows() && filename.contains(":")) {
			return WINDOWS_PATH_NORMALIZER.matcher(filename).replaceFirst("");
		}
		
		return filename;
	}
}