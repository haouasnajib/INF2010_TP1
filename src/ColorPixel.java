/**
 * Classe de pixel en couleurs 
 * @author :
 * @date : 
 */

public class ColorPixel extends AbstractPixel
{
	public int[] rgb; // donnees de l'image
	
	/**
	 * Constructeur par defaut (pixel blanc)
	 */
	ColorPixel() {
		rgb = new int[3];
		rgb[0] = 255;
		rgb[1] = 255;
		rgb[2] = 255;
	}
	
	/**
	 * Assigne une valeur au pixel
	 * @param rgb: valeurs a assigner 
	 */
	ColorPixel(int[] rgb) {
		this.rgb = new int[3];
		this.rgb[0] = rgb[0];
		this.rgb[1] = rgb[1];
		this.rgb[2] = rgb[2];
	}
	
	/**
	 * Renvoie un pixel copie de type noir et blanc
	 */
	public BWPixel toBWPixel() {
		int moyenne = (rgb[0] + rgb[1] + rgb[2])/3;

		return new BWPixel((moyenne > 127));
	}
	
	/**
	 * Renvoie un pixel copie de type tons de gris
	 */
	public GrayPixel toGrayPixel() {
		int moyenne = (rgb[0] + rgb[1] + rgb[2])/3;

		return new GrayPixel(moyenne);
	}
	
	/**
	 * Renvoie un pixel copie de type couleurs
	 */
	public ColorPixel toColorPixel() {
		return new ColorPixel(this.rgb);
	}
	
	public TransparentPixel toTransparentPixel() {
		int[] rgba = new int[4];
		rgba[0] = rgb[0];
		rgba[1] = rgb[1];
		rgba[2] = rgb[2];
		rgba[3] = 255;

		return new TransparentPixel(rgba);
	}
	
	/**
	 * Renvoie le negatif du pixel (255-pixel)
	 */
	public AbstractPixel Negative() { // POURQUOI ABSTRACT PIXEL?
		int[] nrgb = new int[3];
		nrgb[0] = 255-rgb[0];
		nrgb[1] = 255-rgb[1];
		nrgb[2] = 255-rgb[2];

		return new ColorPixel(nrgb);
	}
	
	public void setAlpha(int alpha) {
		//ne fait rien
	}
	
	/**
	 * Convertit le pixel en String (sert a ecrire dans un fichier 
	 * (avec un espace supplémentaire en fin)s
	 */
	public String toString() {
		return  ((Integer)rgb[0]).toString() + " " + 
				((Integer)rgb[1]).toString() + " " +
				((Integer)rgb[2]).toString() + " ";
	}
	
	public int compareTo(AbstractPixel p) {
		if (rgb[0] < ((ColorPixel) p).rgb[0]
				&& rgb[1] < ((ColorPixel) p).rgb[1]
				&& rgb[2] < ((ColorPixel) p).rgb[2]) {
			return -1;
		} else {
			if (rgb[0] == ((ColorPixel) p).rgb[0]
					&& rgb[1] == ((ColorPixel) p).rgb[1]
					&& rgb[2] == ((ColorPixel) p).rgb[2]) {
				return 0;
			} else {
				return 1;
			}
		}
	}

}