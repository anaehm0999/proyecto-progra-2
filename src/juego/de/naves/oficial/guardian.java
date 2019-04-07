/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juego.de.naves.oficial;
/************************************************************
/************************************************************
/*  Esta clase representa la nave guardian
/************************************************************
/************************************************************/
public class guardian extends nave {
    
    //***************************************************************
    /** Descripción: Constructor de clase. Crear una nueva entidad para representar la nave defensora
    * @Params: juego Representa la nave que creamos
    * @Params: ref Referencia del sprite para mostrar la nave
    * @Params: x Localización inicial de la nave, variable x
    * @Params: y Localización inicial de la nave, variable y
    ****************************************************************/ 
    
    public guardian (invader juego, String ref,int x,int y) {
        super(juego,ref,x,y);
    }
    
    //******************************************************************
    /** Descripción: Controla el movimiento de la nave, evita que la nave se desplace fuera de los lados de la pantalla
    /*  @Params: delta. Tiempo transcurrido desde el último movimiento (ms) 
    /*  @Return: nada   
    /*******************************************************************/
    public void gestionMovimiento (long delta ) {
        //si nos movemos y llegado al final de la parte la izquierda no podemos seguir
        if ((dx < 0) && (x < 10)) { 
            return;
        }
        //si nos movemos y llegado al final de la parte la derecha no podemos seguir 
        if ((dx > 0) && (x > 750)){
        return ; 
        }
        super.gestionMovimiento (delta); 
    }  
}
