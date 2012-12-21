package br.com.abril.nds.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImageUtil {
	
	public enum FormatoImagem {
		
		JPEG("jpeg"),GIF("gif"),PNG("png"),SVG("svg");
		
		private String formato;
		
		private FormatoImagem(String formato) {
			this.formato = formato;
		}
		
		public String getFormato() {
			return this.formato;
		}
	}
	
	public static byte[] redimensionar(InputStream imagem,int largura, int altura, FormatoImagem formato) {

		byte[] bytesImagem = null;

		Graphics2D graphisImagem = null;

		ByteArrayOutputStream baos = null;

		try {

			// transforma InputStream em uma BufferedImage

			BufferedImage bufImagemLida = ImageIO.read(imagem);

			// cria imagem para

			BufferedImage imagemRedimensionada 
				= new BufferedImage(largura,altura, BufferedImage.TYPE_INT_RGB);

			// realiza o redimensionamento da imagem

			graphisImagem = imagemRedimensionada.createGraphics();

			graphisImagem.drawImage(bufImagemLida, 0, 0, largura, altura, null);

			baos = new ByteArrayOutputStream();

			// escreve a imagem no OutputStream

			ImageIO.write(imagemRedimensionada,formato.getFormato(), baos);

			// transforma o OutputStream em array de bytes e retorna

			bytesImagem = baos.toByteArray();

		} catch (IOException e) {
			
			bytesImagem = new byte[0];

		} finally {

			if (graphisImagem != null) {

				graphisImagem.dispose();
			}

			if (baos != null) {

				try {

					baos.close();

				} catch (IOException e) {
					bytesImagem = new byte[0];
				}
			}
		}

		return bytesImagem;
	}

}
