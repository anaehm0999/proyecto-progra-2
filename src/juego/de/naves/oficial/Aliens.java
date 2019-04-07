/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juego.de.naves.oficial;
//Esta clase representa los alien. 

public class Aliens extends nave{
   /** Descripción: Constructor de clase. Crea un nuevo alien
   * @Params: juego Representa el alien que creamos 
   * @Params: ref Referencia del sprite para mostrar del alien 
   * @Params: x Localización inicial del alien, variable x 
   * @Params: y Localización inicial del alien, variable y*/
    public Aliens (invader juego, String ref,int x,int y) { 
            super(juego,ref,x,y );
            dx = -75;  
    }
    //*******************************************************************
    /** Descripción: Actualización de los alien 
    *  @Params: Nada 30 
    *  @Return: Nada 
    //********************************************************************/
    public void descender () {
        // si detectamos que un alien llega al borde
        // cambiamos el sentido y bajamos y += 10; */        
        dx = -dx;        
        y += 10;        
        // si hemos llegado a la parte inferior de la pantalla 
        // la nave defensora es destruida 
        if (y > 570) {
            juego. avisoDerrota (); 
        } 
    }    
       
    /** Descripción: Controla el movimiento de los aliens, evita que el alien se desplace fuera de los lados de la pantalla
    *  @Params: delta. Tiempo transcurrido desde el último movimiento (ms)
    *  @Return: Nada*/   
    public void gestionMovimiento (long delta ) {
        if ((dx < 0) && (x < 10)) { // si alcanza el lado izquierdo
               juego. cargaLogica (); // actualizamos la logica del juego
        } if ((dx > 0) && (x > 750)) { // y viceversa para el derecho 
               juego. cargaLogica (); 
            }
           super.gestionMovimiento (delta);
    }    
}
