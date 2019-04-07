/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juego.de.naves.oficial;

import java.awt.Graphics ;
import java.awt.Image ;


/** Descrip: Clase Sprite. Un sprite se visualiza en pantalla. Un sprite no da información, únicamente la imagen y no la localización.
* Esto nos permite usar un sprite simple en diferentes partes del juego sin tener que almacenar muchas copias de esa imagen*/

 public class Sprite {
 /** imagen dibujada por el sprite */
    private Image imagen;

 //*********************************************************************
 /** Descripción: Constructor de la clase. Se crea un nuevo sprite basado en una imagen
 * @Params: imagen. imagen del sprite
 **********************************************************************/
        public Sprite (Image imagen ) {
            this .imagen = imagen;
        }

 //*********************************************************************
 /** Descripción: getWidth determina anchura del sprite. Si no se conoce devuelve -1
 * @Params: nada.
 * @Return: anchura en pixeles de este sprite
 **********************************************************************/
        public int getAncho () {
            return imagen. getWidth (null );
        }

 //*********************************************************************
 /** Descripción: getHeight determina la altura del sprite Si no se conoce devuelve -1
 * @Params: nada.
 * @Return: altura en pixeles de este sprite
 **********************************************************************/
        public int getAlto () {
            return imagen. getHeight (null );
        }

 //*********************************************************************
 /** Descrip: dibuja el frame en pantalla
 * @Params: g dibuja el sprite
 * @Params: x Localización inicial del sprite, variable x
 * @Params: y Localización inicial del sprite, variable y
 * @Return: nada.
 **********************************************************************/
        public void dibujar (Graphics g,int x,int y) {
            g. drawImage (imagen,x,y, null );
        }
 }
