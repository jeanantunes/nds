package br.com.abril.nds.util.export;

import java.io.OutputStream;
import java.util.List;

public interface Exporter {
	
	<T, F, FT> void inOutputStream(String name,
								   NDSFileHeader ndsFileHeader,
								   F filter,
								   FT footer,
								   List<T> dataList, 
								   Class<T> listClass,
								   OutputStream outputStream);

}
