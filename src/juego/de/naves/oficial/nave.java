package juego.de.naves.oficial;

public abstract class nave extends movimiento {

 //*******************************************************************
 /** Descrip: Constructor de clase.
 * Crear una nueva entidad para representar las naves
 *
 * @Params: juego Representa la nave que creamos
 * @Params: ref Referencia del sprite para mostrar la nave
 * @Params: x Localización inicial de la nave, variable x
 * @Params: y Localización inicial de la nave, variable y
 *
 ********************************************************************/
        public nave (invader juego, String ref, int x,int y) {
            super (juego,ref,x,y );
        }

    
 }
