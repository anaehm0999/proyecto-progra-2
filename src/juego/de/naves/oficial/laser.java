/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juego.de.naves.oficial;


public class laser extends disparo {
/************************************************************
/************************************************************
 *
 * Esta clase representa un laser de la nave defensora
 * Descripción: Constructor de clase. Crea un nuevo laser de la nave defensora
 *
 * @Params: juego Representa el laser que creamos
 * @Params: ref Referencia del sprite para mostrar la nave
 * @Params: x Localización inicial del laser, variable x
 * @Params: y Localización inicial del laser, variable y
 *
 ****************************************************************/
    public laser (invader juego, String sprite, int x,int y) {
        super (juego,sprite, x, y );
            dy = -800; // Velocidad del laser
    }
}
