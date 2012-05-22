package br.com.abril.nds.integracao.engine;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

import org.apache.commons.io.filefilter.RegexFileFilter;

import br.com.abril.nds.integracao.engine.data.FileRouteTemplate;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.util.OSUtil;


public abstract class FileContentBasedRouter implements ContentBasedRouter {
	private static final Pattern WINDOWS_PATH_NORMALIZER = Pattern.compile("(?i:[a-z][:])");
	
	public abstract void routeFile(FileRouteTemplate fileRouteTemplate, File file);
	
	public <T extends RouteTemplate> void routeData(T route) {
		FileRouteTemplate fileRoute  = (FileRouteTemplate) route;
		
		File folder = new File(normalizeFileName(fileRoute.getInboundFolder()));
		
		File[] files = folder.listFiles((FilenameFilter) new RegexFileFilter(fileRoute.getFileFilterExpression()));
		
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