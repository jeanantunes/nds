package br.com.abril.nds.util;

import java.util.HashMap;
import java.util.Map;

public class TirarAcento {

    private static Map<Character, Character> mapCharSubstituir = new HashMap<Character, Character>();

    static {
        mapCharSubstituir.put('á', 'a');
        mapCharSubstituir.put('Á', 'A');
        mapCharSubstituir.put('à', 'a');
        mapCharSubstituir.put('À', 'A');
        mapCharSubstituir.put('â', 'a');
        mapCharSubstituir.put('Â', 'A');
        mapCharSubstituir.put('ã', 'a');
        mapCharSubstituir.put('Ã', 'A');
        mapCharSubstituir.put('é', 'e');
        mapCharSubstituir.put('É', 'E');
        mapCharSubstituir.put('è', 'e');
        mapCharSubstituir.put('È', 'E');
        mapCharSubstituir.put('ê', 'e');
        mapCharSubstituir.put('Ê', 'E');
        mapCharSubstituir.put('í', 'i');
        mapCharSubstituir.put('Í', 'I');
        mapCharSubstituir.put('ì', 'i');
        mapCharSubstituir.put('Ì', 'I');
        mapCharSubstituir.put('î', 'i');
        mapCharSubstituir.put('Î', 'I');
        mapCharSubstituir.put('Ó', 'O');
        mapCharSubstituir.put('ò', 'o');
        mapCharSubstituir.put('Ò', 'O');
        mapCharSubstituir.put('õ', 'o');
        mapCharSubstituir.put('Õ', 'O');
        mapCharSubstituir.put('ô', 'o');
        mapCharSubstituir.put('Ô', 'O');
        mapCharSubstituir.put('ú', 'u');
        mapCharSubstituir.put('Ú', 'U');
        mapCharSubstituir.put('ù', 'u');
        mapCharSubstituir.put('Ù', 'U');
        mapCharSubstituir.put('ü', 'u');
        mapCharSubstituir.put('Ü', 'U');
        mapCharSubstituir.put('û', 'u');
        mapCharSubstituir.put('Û', 'U');
        mapCharSubstituir.put('ç', 'c');
        mapCharSubstituir.put('Ç', 'C');
        mapCharSubstituir.put('ñ', 'n');
        mapCharSubstituir.put('Ñ', 'N');
        mapCharSubstituir.put('º', ' ');
        mapCharSubstituir.put('°', ' ');
        
    }

    public static String removerAcentuacao(String texto) {
        StringBuilder sb = new StringBuilder(texto);
        {
            for (int i = 0; i < sb.length(); i++) {
                Character c = mapCharSubstituir.get(sb.charAt(i));
                if (c != null) {
                    sb.setCharAt(i, c);
                }
            }
            return sb.toString();
        }
    }
}
