package integration.efacil;

import br.com.abril.nds.dto.EfacilDTO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConectionEfacil {

        public String conectar(EfacilDTO efacilDTO){
            String strResponse = null;
            try {
                String privatekey= "jytCqej8a50GlPp0Esio4mk1qrc39g8pBSURCeZepsD5vtgXREr31XMR5kevvtXhSEnCGxyIqLPCLX1dFxH7yMTLoigdZV32LZOL/dSX5qwytqas+qyr95JRfCRxBpeb+g2sTiO9WB+kxUyvVIqBRuw+8883XX68gDGYP17CcHU=";
                efacilDTO.setDados(new String(efacilDTO.getDados().getBytes(), "UTF-8"));


                SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

                SecretKeySpec privateKey = new SecretKeySpec(
                        javax.xml.bind.DatatypeConverter.parseBase64Binary(privatekey), "HmacSHA256");

                JsonObject payload = new JsonObject();
                payload.addProperty("token", efacilDTO.getToken());

                JwtBuilder builder = Jwts.builder();
                builder.signWith(signatureAlgorithm, privateKey);
                builder.setPayload(payload.toString());

                String authorization = builder.compact();
                URL url = new URL(efacilDTO.getURL());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestProperty("Authorization", "Bearer " + authorization);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept-Charset", "UTF-8");
               conn.setRequestProperty("Content-Length", String.valueOf(efacilDTO.getDados().getBytes().length));

                conn.setRequestMethod(efacilDTO.getMetodo());

                if ("POST".equals(efacilDTO.getMetodo())) {
                    conn.setDoOutput(true);
                    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                    writer.write(efacilDTO.getDados());
                    writer.flush();
                }

                boolean erro = false;
                InputStream in = null;
                System.out.println(conn.getResponseCode());
                if (conn.getResponseCode() == 200) {
                    in = conn.getInputStream();
                } else {
                    erro = true;
                    in = conn.getErrorStream();
                }


                BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String line;

                while ((line = br.readLine()) != null)
                    strResponse += line;

                if (erro) {
                    if (strResponse.contains("\"erro\":")) {
                        Gson o = new Gson();
                        JsonObject objErro = o.fromJson(strResponse, JsonObject.class);

                        throw new Exception(objErro.get("erro").getAsString());
                    } else {
                        throw new Exception(strResponse);
                    }
                }


            }catch(Exception e){
                e.printStackTrace();
            }finally {
                return strResponse;
            }

        }



}
