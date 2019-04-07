/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juego.de.naves.oficial;

/**
 *
 * @author AnaHernández
 */
/**********************************************************************
 ***********************************************************************
 **
 ** Importaciones generales del sistema
 **
 ***********************************************************************
 ***********************************************************************/
 import java.awt. GraphicsConfiguration ;
 import java.awt. GraphicsEnvironment ;
 import java.awt. Image ;
 import java.awt. Transparency ;
 import java.awt.image. BufferedImage ;
 import java.io. IOException ;
 import java.net. URL;
 import java.util. HashMap ;
 import javax.imageio. ImageIO ; // carga la imagen

 //*********************************************************************
 /** Descrip: Clase SpriteAux. Se encarga de los recursos para los
 * Sprites en el juego. Es importante el cómo y dónde
 * utilizamos los recursos
 * Responsable carga y cache de los sprites
 *
 **********************************************************************/
public class SpriteAux {
 /** Instancia simple de esta clase */
    private static SpriteAux single = new SpriteAux ();

 //*********************************************************************
 /** Descrip: Crea una instancia simple de la clase
 *
 * @Params: nada
 * @Return: Una instancia simple de la clase
 *
 **********************************************************************/
    public static SpriteAux get() {
        return single;
    }
 /** cache del sprite */
    private HashMap sprites = new HashMap ();

 //*********************************************************************
 /** Descrip: Recupera un sprite
 *
 * @Params: ref. Referencia a la imagen usada por el sprite
 * @Return: Una instancia que contiene la peticion de la
 * aceleracion grafica del sprite
 *
 **********************************************************************/
    public Sprite obtenerSprite (String ref ) {
 // si ya hemos conseguido un sprite en la caché
 // entonces podemos devolverlo
        if (sprites. get(ref) != null ) {
            return (Sprite ) sprites. get(ref);
        }
        BufferedImage sourceImage = null ;
        try {
 // ClassLoader.getResource() nos asegura el sprite
 // en el sitio adecuado.
            URL url = this .getClass ().getClassLoader ().getResource (ref);
            if (url == null ) {
                fallo ("No se puede encontrar la referencia: " +ref);
            }
 // utiliza ImageIO para leer la imagen
            sourceImage = ImageIO .read (url);
        } catch (IOException e) {
            fallo ("Fallo al cargar: " +ref);
        }
 // crea una aceleración gráfica del sprite
        GraphicsConfiguration gc = GraphicsEnvironment .
        getLocalGraphicsEnvironment ().getDefaultScreenDevice().getDefaultConfiguration ();
        Image image = gc. createCompatibleImage (sourceImage.getWidth(),sourceImage. getHeight (),Transparency .BITMASK );
 // dibuja la imagen con aceleración gráfica
        image. getGraphics ().drawImage (sourceImage, 0,0,null );
 // crear un sprite y lo añade al caché
        Sprite sprite = new Sprite (image );
        sprites. put(ref,sprite );
        return sprite;
    }

 //*********************************************************************
 /** Descrip: Tratamiento de recursos
 *
 * @Params: message. mensaje lanzado a la ventana
 * @Return: nada
 *
 **********************************************************************/
    private void fallo (String message ) {
        System .err. println (message );
        System .exit (0);
    }
 }