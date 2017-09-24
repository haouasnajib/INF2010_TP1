import java.awt.PageAttributes.ColorType;
import java.lang.Math.*;

/**
 * Classe PixelMapPlus
 * Image de type noir et blanc, tons de gris ou couleurs
 * Peut lire et ecrire des fichiers PNM
 * Implemente les methodes de ImageOperations
 * @author Xhulio Hasani 1737944 - Mohammed Najib Haouas 1572614 
 * @date : 01 oct 2017
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
		this.imageType = ImageType.BW; // Recording new Image type
	}
	
	/**
	 * Convertit l'image vers un format de tons de gris
	 */
	public void convertToGrayImage() {
		this.imageData = this.toGrayImage().imageData;
		this.imageType = ImageType.Gray; // Recording new Image type
	}
	
	/**
	 * Convertit l'image vers une image en couleurs
	 */
	public void convertToColorImage() {
		this.imageData = this.toColorImage().imageData;
		this.imageType = ImageType.Color; // Recording new Image type
	}
	
	public void convertToTransparentImage() {
		this.imageData = this.toTransparentImage().imageData;
		this.imageType = ImageType.Transparent; // Recording new Image type
	}
	
	/**
	 * Fait pivoter l'image de 10 degres autour du pixel (row,col)=(0, 0)
	 * dans le sens des aiguilles d'une montre (clockWise == true)
	 * ou dans le sens inverse des aiguilles d'une montre (clockWise == false).
	 * Les pixels vides sont blancs.
	 */
	public void rotate(int x, int y, double angleRadian) {
		AbstractPixel[][] temp = new AbstractPixel[height][width];
			// Le pixel est porté vers un nouvel emplacement (new_i, new_j) dans la nouvelle image
		

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Calcul des indices correspondants dans l'image de départ
				int new_i = (int)(Math.cos(angleRadian)*j + Math.sin(angleRadian)*i - Math.cos(angleRadian)*x
						- Math.sin(angleRadian)*y + x);
				int new_j = (int)(- Math.sin(angleRadian)*j + Math.cos(angleRadian)*i + Math.sin(angleRadian)*x
						- Math.cos(angleRadian)*y + y);
				
				// Si ces indices sont à l'extérieur des limites de l'image, remplace les nouveaux AbstractPixel en pixels de type voulu
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
					temp[i][j] = imageData[new_j][new_i]; // Sinon, on porte les données de l'ancienne image dans leur nouvel emplacement
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

		
		// Computing xy scale factor
		float scaleH = (float) w/this.width; // int div loses data here, careful
		float scaleV = (float) h/this.height; // and here

		// Indices declaration
		int i, j;
		int u, v;

		AbstractPixel[][] newImageData = new AbstractPixel[h][w];

		for (u = 0; u < h; u++) {
			for (v = 0; v < w; v++) {
				i = (int) (u/scaleV); // floor here
				j = (int) (v/scaleH); // and here
				newImageData[u][v] = this.imageData[i][j]; // Resize using nearest neighbor method
			}
		}

		// Copying data to this
		this.clearData();
		this.imageData = newImageData;
		this.width = w;
		this.height = h;
	}

	/**
	 * Insert pm dans l'image a la position row0 col0
	 * @param pm : current pixel map
	 * @param row0 : line of insertion
	 * @param col0 : column of insertion
	 */
	public void inset(PixelMap pm, int row0, int col0)
	{
		// Setting scan bounds for pixels to change,
		// We're changing pixels from row0/col0 until either the end of this or the end of pm
		int vbound = Math.min(this.height, row0 + pm.height);
		int hbound = Math.min(this.width, col0 + pm.width);

		// Conversion of pm to destination type
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

		// Insertion
		for (int i = row0; i < vbound; i++) {
			for (int j = col0; j < hbound; j++) {
				this.imageData[i][j] = pm.imageData[i-row0][j-col0];
			}
		}
	}
	
	/**
	 * Decoupe l'image
	 * @param h : new height
	 * @param w : new width
	 */
	public void crop(int h, int w)
	{
		if(w <= 0 || h <= 0)
			throw new IllegalArgumentException();

		PixelMapPlus newImageMap = new PixelMapPlus(this.imageType, h, w); // New blank h-by-w image
		newImageMap.inset(this, 0, 0); // Inserting source image into blank image

		// Copying data to this
		this.imageData = newImageMap.imageData;
		this.height = h;
		this.width = w;
	}
	
	/**
	 * Effectue une translation de l'image
	 */
	public void translate(int rowOffset, int colOffset)
	{
		// New image, abstract
		AbstractPixel[][] tempdata = new AbstractPixel[height][width];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if ((i - rowOffset) < 0 || i -rowOffset >= height || j - colOffset < 0 || j - colOffset >= width){
					// Place white pixels in out of bounds locations
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
					tempdata[i][j] = this.imageData[i-rowOffset][j-colOffset]; // Place source image in valid locations
			}
		}

		// Copying data to this
		this.imageData = tempdata;
	}
	
	/**
	 * Effectue un zoom autour du pixel (x,y) d'un facteur zoomFactor 
	 * @param x : colonne autour de laquelle le zoom sera effectue
	 * @param y : rangee autour de laquelle le zoom sera effectue  
	 * @param zoomFactor : facteur du zoom a effectuer. Doit etre superieur a 1
	 */
	public void zoomIn(int x, int y, double zoomFactor) throws IllegalArgumentException { // Working on the assumption xi horizontal
		if(zoomFactor < 1.0)
			throw new IllegalArgumentException();

		// Recording original attributes
		int originalHeight = this.height;
		int originalWidth = this.width;

		// Recording dimensions of zone to enlarge
		int newHeight = (int) ((float) this.height/zoomFactor);
		int newWidth = (int) ((float) this.width/zoomFactor);

		// Normalizing x and y if they allow the enlargement zone to be outside the canvas
		x = (x < ((int) ((float) newWidth/2))) ? ((int) ((float) newWidth/2)) : x;
		y = (y < ((int) ((float) newHeight/2))) ? ((int) ((float) newHeight/2)) : y;

		x = ((x+((int) ((float) newWidth/2))) > this.width) ? (this.width-((int) ((float) newWidth/2))-1) : x;
		y = ((y+((int) ((float) newHeight/2))) > this.height) ? (this.height-((int) ((float) newHeight/2))-1) : y;

		// Computing translate offsets as defined in method translate to place enlargement zone on top left of file
		int hOffset = -(x-((int) ((float) newWidth/2)));
		int vOffset = -(y-((int) ((float) newHeight/2)));

		// Modifying this
		this.translate(vOffset,hOffset); // Translate to top left
		this.crop(newHeight, newWidth); // Crop excess data
		this.resize(originalWidth, originalHeight); // Resize to original dimensions, thus enlarging
	}

	/**
	 * Effectue un remplacement de tout les pixels dont la valeur est entre min et max 
	 * avec newPixel
	 * @param min : La valeur miniale d'un pixel
	 * @param max : La valeur maximale d'un pixel  
	 * @param newPixel : Le pixel qui remplacera l'ancienne couleur 
	 * (sa valeur est entre min et max)
	 */
	public void replaceColor(AbstractPixel min, AbstractPixel max, AbstractPixel newPixel) {
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				if ((this.imageData[i][j].compareTo(min) == 1) && (this.imageData[i][j].compareTo(max) == -1)) {
					this.imageData[i][j] = newPixel; // this bigger = 1, this smaller = -1
				}
			}
		}
	}

	public void inverser() {
		
		PixelMapPlus newImageObject = new PixelMapPlus(this);
		
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				newImageObject.imageData[i][j] = this.imageData[this.height - i - 1][j]; // vertical flip 
			}
		}

		// Copying data to this
		this.imageData = newImageObject.imageData;
	}
}
