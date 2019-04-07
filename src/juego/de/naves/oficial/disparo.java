/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juego.de.naves.oficial;
/***************************************************************
 * /************************************************************ 
 * /*  Esta clase representa los disparos del juego
 * /************************************************************
 **************************************************************/

public abstract class disparo extends movimiento {
//****************************************************************
/** Descripción: Constructor de clase. Crea un nuevo disparo
/* @Params: juego Representa el disparo que creamos
/* @Params: ref Referencia del sprite para mostrar la nave
/* @Params: x Localización inicial del laser, variable x 
/* @Params: y Localización inicial del laser, variable y
/****************************************************************/ 
    public disparo (invader juego, String sprite, int x,int y) {
         super(juego,sprite,x,y );
     }
}
