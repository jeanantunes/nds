package br.com.dgb.nfe;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.List;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERString;

/**
 *
 * @author Murilo
 */
public class TestMain {

    private void testAlheio() throws CertificateException, IOException {
        String x509content = "C:/Users/wrpaiva/certificadoAR/certificado.pfx";
        CertificateFactory fact = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream bais = new ByteArrayInputStream(x509content.getBytes());
        while (bais.available() > 0) {
            X509Certificate x509 = (X509Certificate) fact.generateCertificate(bais);
            exibeCertificado(x509);
        }
        
        System.out.println("Certificate Type: "+fact.getType());
		System.out.println("Public Key: \n"+fact.getProvider().getName());
		
        
        bais.close();
    }

    private void exibeCertificado(X509Certificate certificate) throws CertificateParsingException, IOException {
        for (List<?> subjectAlternativeName : certificate.getSubjectAlternativeNames()) {
            String email;
            Pair<DERObjectIdentifier, String> otherName;
            // O primeiro elemento é um Integer com o valor 0 = otherName, 1 = rfc822name etc.
            // O segundo valor é um byte array ou uma String. Veja o javadoc de
            // getSubjectAlternativeNames.
            switch (((Number) subjectAlternativeName.get(0)).intValue()) {
                case 0: // OtherName - contém CPF, CNPJ etc.
                    otherName = getOtherName((byte[]) subjectAlternativeName.get(1));
                    System.out.println("First : " + otherName.first);
                    System.out.println("Second : " + otherName.second);
                    // o OID fica em otherName.first
                    break;
                case 1: // rfc822Name - usado para email
                    email = (String) subjectAlternativeName.get(1);
                    System.out.println("email : "+email);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Uma classe que encapsula pares de objetos.
     * Inspirado no template pair<> da STL.
     *
     * @param <T> O primeiro tipo.
     * @param <U> O segundo tipo.
     */
    class Pair<T, U> {

        /** O primeiro objeto. */
        public T first;
        /** O segundo objeto. */
        public U second;

        /** Construtor */
        public Pair(T t, U u) {
            first = t;
            second = u;
        }
    }

    /**
     * Interpreta um dado do tipo otherName.
     * Obs. O JDK 5.0 não tem classes que lidem com um dado do tipo OtherName.
     * É necessário usar o BouncyCastle.
     * @param encoded O dado em ASN.1.
     * @return Um par contendo o OID e o conteúdo.
     */
    private Pair<DERObjectIdentifier, String> getOtherName(byte[] encoded) throws IOException {
        // O JDK 5.0 não tem classes que lidem com um dado do tipo OtherName.
        // É necessário usar o BouncyCastle.
        ASN1InputStream inps = new ASN1InputStream(encoded);
        DERSequence seq = null;
        DERObjectIdentifier oid = null;
        String conteudo = "";
        seq = (DERSequence) inps.readObject();
        inps.close();
        Enumeration en = seq.getObjects();
        oid = (DERObjectIdentifier) en.nextElement();
        DERObject obj = ((ASN1TaggedObject) ((ASN1TaggedObject) en.nextElement()).getObject()).getObject();
        if (obj instanceof DERString) { // Certificados antigos SERASA - incorretos
            conteudo = ((DERString) obj).getString();
        } else if (obj instanceof DEROctetString) { // Certificados corretos
            conteudo = new String(((DEROctetString) obj).getOctets(), "ISO-8859-1");
        }
        return new Pair<DERObjectIdentifier, String>(oid, conteudo);
    }

//
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws CertificateParsingException, IOException, CertificateException {
        new TestMain().test();
    }

    private void test() throws CertificateParsingException, IOException {
        File file = new File("C:/Users/wrpaiva/certificadoAR/certificado.pfx");
        X509Certificate certificate = null;

        if (file.exists()) {

//            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
//            InputStream is = new FileInputStream(file);
//            Certificate certificate = certificateFactory.generateCertificate(is);
//            System.out.println("Tipo de certificado "+certificate.getType());
//            is.close();
            try {
                KeyStore keyStore = KeyStore.getInstance("PKCS12");
                InputStream is = new FileInputStream(file);
                String senha = "arsp15";
                keyStore.load(is, senha.toCharArray());
                is.close();

                String alias = "{3ed46e23-51f5-462f-ab68-b5bea536993d}";

                PrivateKey privateKey = null;
                Enumeration e = keyStore.aliases();
                while (e.hasMoreElements()) {
                    alias = (String) e.nextElement();
                    certificate = (X509Certificate) keyStore.getCertificate(alias);
                    privateKey = (PrivateKey) keyStore.getKey(alias, senha.toCharArray());
                    System.out.println(certificate + " " + privateKey.getAlgorithm());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(ex.getCause());
            }
        } else {
            System.out.println("Certificado nao encontrado em " + file.getAbsolutePath());
        }
        exibeCertificado(certificate);
    }
}

