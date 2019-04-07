/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juego.de.naves.oficial;

public class misil extends disparo {
/************************************************************
 /************************************************************
 /*
 /*Esta clase representa un disparo del alien
 
 /*Descripción: Constructor de clase. Crea un nuevo disparo del alien
 *
 * @Params: juego Representa el misil que creamos
 * @Params: ref Referencia del sprite para mostrar del alien
 * @Params: x Localización inicial del disparo del alien, variable x
 * @Params: y Localización inicial del disparo del alien, variable y
 *
 *****************************************************************/
    public misil (invader juego, String sprite, int x, int y) {
        super (juego,sprite, x, y );
        dy = 100; // Velocidad del misil
    }

 //****************************************************************
 /** Descripción: Controla la trayectoria del disparo
 * @Params: delta. Tiempo transcurrido desde el último movimiento (ms)
 * @Return: Nada
 *****************************************************************/
    public void gestionMovimiento (long delta ) {
        super .gestionMovimiento (delta ); // movimiento normal
        if (y > 600) { // si sale fuera de la pantalla,
            juego. quitarEntidad (this ); //lo quitamos
        }
    }    
}
