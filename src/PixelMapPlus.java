import java.awt.PageAttributes.ColorType;
import java.lang.Math.*;

/**
 * Classe PixelMapPlus
 * Image de type noir et blanc, tons de gris ou couleurs
 * Peut lire et ecrire des fichiers PNM
 * Implemente les methodes de ImageOperations
 * @author : 
 * @date   : 
 */

public class PixelMapPlus extends PixelMap implements ImageOperations 
{
	/**
	 * Constructeur creant l'image a partir d'un fichier
	 * @param fileName : Nom du fichier image
	 */
	PixelMapPlus(String fileName)
	{
		super( fileName );
	}
	
	/**
	 * Constructeur copie
	 * @param image : source
	 */
	PixelMapPlus(PixelMap image)
	{
		super(image); 
	}
	
	/**
	 * Constructeur copie (sert a changer de format)
	 * @param type : type de l'image a creer (BW/Gray/Color/Transparent)
	 * @param image : source
	 */
	PixelMapPlus(ImageType type, PixelMap image)
	{
		super(type, image); 
	}
	
	/**
	 * Constructeur servant a allouer la memoire de l'image
	 * @param type : type d'image (BW/Gray/Color/Transparent)
	 * @param h : hauteur (height) de l'image 
	 * @param w : largeur (width) de l'image
	 */
	PixelMapPlus(ImageType type, int h, int w)
	{
		super(type, h, w);
	}
	
	/**
	 * Genere le negatif d'une image
	 */
	public void negate() {
		for (int i = 0 ; i < this.height ; i++ ) {
			for (int j = 0 ; j < this.width ; j++ ) {
				this.imageData[i][j] = this.imageData[i][j].Negative();
			}
		}
	}
	
	/**
	 * Convertit l'image vers une image en noir et blanc
	 */
	public void convertToBWImage() {
		this.imageData = this.toBWImage().imageData;
		this.imageType = ImageType.BW;
	}
	
	/**
	 * Convertit l'image vers un format de tons de gris
	 */
	public void convertToGrayImage() {
		this.imageData = this.toGrayImage().imageData;
		this.imageType = ImageType.Gray;
	}
	
	/**
	 * Convertit l'image vers une image en couleurs
	 */
	public void convertToColorImage() {
		this.imageData = this.toColorImage().imageData;
		this.imageType = ImageType.Color;
	}
	
	public void convertToTransparentImage() {
		this.imageData = this.toTransparentImage().imageData;
		this.imageType = ImageType.Transparent;
	}
	
	/**
	 * Fait pivoter l'image de 10 degres autour du pixel (row,col)=(0, 0)
	 * dans le sens des aiguilles d'une montre (clockWise == true)
	 * ou dans le sens inverse des aiguilles d'une montre (clockWise == false).
	 * Les pixels vides sont blancs.
	 */
	public void rotate(int x, int y, double angleRadian) { // NN working on the assumption xui is horizontal, yvj vertical, correct assumption ? Verify calculation of matrix
		// int u, v; // indices in new transformed matrix, this.
		// int i, j; // indices in former matrix, copy of this.
		// AbstractPixel[][] temporaryImageData = new AbstractPixel[height][width];

		// for (u=0; u < this.height; u++) {
		// 	for (v=0; v < this.width; v++) {
		// 		// Compute corresponding i and j in temporary copy PMP
		// 		j = (int) (Math.cos(angleRadian)*v + Math.sin(angleRadian)*u - Math.cos(angleRadian)*y - Math.sin(angleRadian)*x + y);
		// 		i = (int) (-Math.sin(angleRadian)*v + Math.cos(angleRadian)*u + Math.sin(angleRadian)*y - Math.cos(angleRadian)*x + x);

		// 		if (i < 0 || i >= this.height || j < 0 || j >= this.width)

		// 			switch (imageType){
		// 			case BW:
		// 				this.imageData[u][v] = new BWPixel();
		// 				break;
		// 			case Gray:
		// 				this.imageData[u][v] = new GrayPixel();
		// 				break;
		// 			case Color:
		// 				this.imageData[u][v] = new ColorPixel();
		// 				break;
		// 			case Transparent:
		// 				this.imageData[u][v] = new TransparentPixel();
		// 				break;
		// 			}
					
		// 		else
		// 			this.imageData[u][v] = temporaryImageData[i][j];
		// 	}
		// }
		AbstractPixel[][] temp = new AbstractPixel[height][width];
			// Le pixel est porté vers un nouvel emplacement (new_i, new_j) dans la nouvelle image
			
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					int new_i = (int)(Math.cos(angleRadian)*j + Math.sin(angleRadian)*i - Math.cos(angleRadian)*x
							- Math.sin(angleRadian)*y + x);
					int new_j = (int)(- Math.sin(angleRadian)*j + Math.cos(angleRadian)*i + Math.sin(angleRadian)*x
							- Math.cos(angleRadian)*y + y);
					
					if(new_i < 0|| new_i >= width || new_j < 0 || new_j >= height){
						switch (imageType){
						case BW:
							temp[i][j] = new BWPixel();
							break;
						case Gray:
							temp[i][j] = new GrayPixel();
							break;
						case Color:
							temp[i][j] = new ColorPixel();
							break;
						case Transparent:
							temp[i][j] = new TransparentPixel();
							break;
						}
					}
					else {
						temp[i][j] = imageData[new_j][new_i];
					}
				}
			}
			
			imageData = temp;

	}
	
	/**
	 * Modifie la longueur et la largeur de l'image 
	 * @param w : nouvelle largeur
	 * @param h : nouvelle hauteur
	 */
	public void resize(int w, int h) throws IllegalArgumentException
	{
		if(w <= 0 || h <= 0)
			throw new IllegalArgumentException();

		
		float scaleH = (float) w/this.width;
		float scaleV = (float) h/this.height; // NN getHeight ??

		int i, j;
		int u, v;

		AbstractPixel[][] newImageData = new AbstractPixel[h][w];

		for (u = 0; u < h; u++) {
			for (v = 0; v < w; v++) {
				i = (int) (u/scaleV); // NN floor here
				j = (int) (v/scaleH);
				newImageData[u][v] = this.imageData[i][j];
			}
		}

		this.imageData = newImageData;
		this.width = w;
		this.height = h;
	}

	/**
	 * Insert pm dans l'image a la position row0 col0
	 */
	public void inset(PixelMap pm, int row0, int col0)
	{
		int vbound = Math.min(this.height, row0 + pm.height);
		int hbound = Math.min(this.width, col0 + pm.width);

		switch (this.imageType) {
			case Transparent:
				pm = pm.toTransparentImage();
				break;
			case Color:
				pm = pm.toColorImage();
				break;
			case Gray:
				pm = pm.toGrayImage();
				break;
			case BW:
				pm = pm.toBWImage();
				break;
		}

		for (int i = row0; i < vbound; i++) {
			for (int j = col0; j < hbound; j++) {
				this.imageData[i][j] = pm.imageData[i-row0][j-col0]; // NN needs conversion?
			}
		}
	}
	
	/**
	 * Decoupe l'image 
	 */
	public void crop(int h, int w)
	{
		if(w <= 0 || h <= 0)
			throw new IllegalArgumentException();

		PixelMapPlus newImageMap = new PixelMapPlus(this.imageType, h, w);
		newImageMap.inset(this, 0, 0);

		// AbstractPixel[][] newImageData = new AbstractPixel[h][w];
		// for (int i = 0 ; i < h ; i++ ) {
		// 	for (int j = 0 ; j < w ; j++ ) {
		// 		newImageData[i][j] = imageData[i][j];				
		// 	}
			
		// }

		this.imageData = newImageMap.imageData;
		this.height = h;
		this.width = w;
	}
	
	/**
	 * Effectue une translation de l'image 
	 */
	public void translate(int rowOffset, int colOffset)
	{
		// PixelMapPlus newImageObject = new PixelMapPlus(this);

		// int vStartIndex = (rowOffset < 0) ? 0 : rowOffset;
		// int vEndIndex = (rowOffset > 0) ? this.height : this.height + rowOffset;
		// int hStartIndex = (colOffset < 0) ? 0 : colOffset;
		// int hEndIndex = (colOffset > 0) ? this.width : this.width + colOffset;


		// for (int i = vStartIndex; i < vEndIndex; i++) {
		// 	for (int j = hStartIndex; j < hEndIndex; j++) {
		// 		newImageObject.imageData[i][j] = this.imageData[i-rowOffset][j-colOffset];
		// 	}
		// }


		// this.imageData = newImageObject.imageData;

		AbstractPixel[][] tempdata = new AbstractPixel[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if ((i - rowOffset) < 0 || i -rowOffset >= height || j - colOffset < 0 || j - colOffset >= width){
					switch (this.imageType){
					case BW:
						tempdata[i][j] = new BWPixel();
						break;
					case Gray:
						tempdata[i][j] = new GrayPixel();
						break;
					case Color:
						tempdata[i][j] = new ColorPixel();
						break;
					case Transparent:
						tempdata[i][j] = new TransparentPixel();
						break;
					}
				}
				else
					tempdata[i][j] = this.imageData[i-rowOffset][j-colOffset];
			}
		}
		this.imageData = tempdata;
	}
	
	/**
	 * Effectue un zoom autour du pixel (x,y) d'un facteur zoomFactor 
	 * @param x : colonne autour de laquelle le zoom sera effectue
	 * @param y : rangee autour de laquelle le zoom sera effectue  
	 * @param zoomFactor : facteur du zoom a effectuer. Doit etre superieur a 1
	 */
	public void zoomIn(int x, int y, double zoomFactor) throws IllegalArgumentException { // NN Working on the assumption xi horizontal
		if(zoomFactor < 1.0)
			throw new IllegalArgumentException();

		int originalHeight = this.height;
		int originalWidth = this.width;

		int newHeight = (int) ((float) this.height/zoomFactor);
		int newWidth = (int) ((float) this.width/zoomFactor);

		x = (x < ((int) ((float) newWidth/2))) ? ((int) ((float) newWidth/2)) : x;
		y = (y < ((int) ((float) newHeight/2))) ? ((int) ((float) newHeight/2)) : y;

		x = ((x+((int) ((float) newWidth/2))) > this.width) ? (this.width-((int) ((float) newWidth/2))-1) : x;
		y = ((y+((int) ((float) newHeight/2))) > this.height) ? (this.height-((int) ((float) newHeight/2))-1) : y;

		int hOffset = -(x-((int) ((float) newWidth/2)));
		int vOffset = -(y-((int) ((float) newHeight/2)));


		this.translate(vOffset,hOffset);
		this.crop(newHeight, newWidth);
		this.resize(originalWidth, originalHeight);
	}

	/**
	 * Effectue un remplacement de tout les pixels dont la valeur est entre min et max 
	 * avec newPixel
	 * @param min : La valeur miniale d'un pixel
	 * @param max : La valeur maximale d'un pixel  
	 * @param newPixel : Le pixel qui remplacera l'ancienne couleur 
	 * (sa valeur est entre min et max)
	 */
	public void replaceColor(AbstractPixel min, AbstractPixel max, AbstractPixel newPixel) { // NN Not sure
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				if ((this.imageData[i][j].compareTo(min) == 1) && (this.imageData[i][j].compareTo(max) == -1)) {
					this.imageData[i][j] = newPixel;
				}
			}
		}
	}

	public void inverser() {
		
		PixelMapPlus newImageObject = new PixelMapPlus(this);
		
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				newImageObject.imageData[i][j] = this.imageData[this.height - i - 1][j];
			}
		}

		this.imageData = newImageObject.imageData;
	}
}
