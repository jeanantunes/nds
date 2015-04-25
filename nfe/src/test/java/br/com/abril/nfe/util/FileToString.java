package br.com.abril.nfe.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <pre><b>Classe responsável por trasnformar um instância de File
 * em uma String.</b></pre>
 *
 * @author Dilnei Cunha.
 */
public class FileToString {

    /**
     * <b>Lê um file e tranforma o conteúdo em uma String.</b>
     * <pre>Retorna 'null' caso não consiga transformar o
     * caminho em String</pre>
     *
     * @param fullyQualifiedNamePathFile - nome do arquivo com o caminho
     * completo.
     *
     * @return String
     */
    public static String readFileAsString(String fullyQualifiedNamePathFile) throws IOException {

        if (fullyQualifiedNamePathFile == null || fullyQualifiedNamePathFile.isEmpty()) {
            return null;
        }
        // Verifica byte order mark (BOM).
        UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(new FileInputStream(new File(fullyQualifiedNamePathFile)));
        ubis.skipBOM();

        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(ubis);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        char[] buf = new char[1024];
        int numRead = 0;
        try {
            while ((numRead = bufferedReader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                stringBuilder.append(readData);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        try {
            bufferedReader.close();
            inputStreamReader.close();
            ubis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return stringBuilder.toString().trim();
    }
}