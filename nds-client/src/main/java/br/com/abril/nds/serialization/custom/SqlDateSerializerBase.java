package br.com.abril.nds.serialization.custom;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.std.SerializerBase;


public class SqlDateSerializerBase extends SerializerBase<java.sql.Date> {
	
	
	private DateFormat df;
	protected SqlDateSerializerBase(DateFormat df) {
		super(java.sql.Date.class);
		this.df = df;
	}

	@Override
	public void serialize(Date value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonGenerationException {
		
		jgen.writeString(df.format(value));
	}

}
