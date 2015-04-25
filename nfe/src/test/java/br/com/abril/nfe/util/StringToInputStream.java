package br.com.abril.nfe.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * <b>Transforma uma String para um InputStream.</b>
 *
 * @author Dilnei Cunha.
 */
public class StringToInputStream {

    /**
     * <b>Método responsável por transformar uma String em um Stream de
     * dados.</b>
     *
     * @param str
     * @return InputStream
     */
    public InputStream inputStreamStr(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    /**
     * <b>Método responsável por transformar um File em um Stream de dados.</b>
     *
     * @param str
     * @return InputStream
     */
    public InputStream inputStreamStr(File fileStr) throws IOException {
        return new ByteArrayInputStream(FileToString.readFileAsString(fileStr.getAbsolutePath()).getBytes());
    }
}