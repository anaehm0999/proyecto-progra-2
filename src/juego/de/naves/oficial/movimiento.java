/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juego.de.naves.oficial;

import java.awt. Graphics ;
import java.awt. Rectangle ; // para las colisiones


 /********************************************************************
 /*
 /* Esta clase representa algunos elementos que aparecen en el juego, la entidad tiene la responsabilidad de resolver las colisiones y
 * movimientos basados en un conjunto de características definidas en cualquiera de las subclases. Utilizo double para la localización de los pixels para dar más precisión.
 /*
 /********************************************************************
 /********************************************************************/
 public abstract class movimiento implements Runnable {
 /** Localización actual "x" de esta entidad*/
    protected double x;
 /** Localización actual "y" de esta entidad*/
    protected double y;
 /** Sprite que representa esta entidad */
    protected Sprite sprite;
 /** Velocidad horizontal actual de la entidad(pixels/sec) */
    protected double dx;
 /** Velocidad vertical actual de la entidad(pixels/sec) */
    protected double dy;
 /** Rectangulo usado por la entidad durante la colisión */
    private Rectangle yo = new Rectangle();
 /** Rectangulo usado por otra entidad durante la colisión */
    private Rectangle el = new Rectangle();
 /** Declaración del Thread*/
    public Thread th = new Thread (this );
    public boolean esperaTecla = true ;
    public invader juego;

 //******************************************************************
 /** Descripción: Constructora de la clase, referenciada sobre un sprite y la localización de este.
 * @Params: ref Referencia del sprite para mostrar por pantalla
 * @Params: x Localización inicial de la entidad, variable x
 * @Params: y Localización inicial de la entidad, variable y
 * @Return: nada.
 *
 *******************************************************************/
    public movimiento (invader juego, String ref, int x,int y) {
        this .juego = juego;
        this .sprite = SpriteAux. get().obtenerSprite (ref);
        this .x = x;
        this .y = y;
    }

 //******************************************************************
 /** Descripción: Solicita a la entidad el movimiento respecto al tiempo trancurrido
 * @Params: delta. Tiempo transcurrido en ms
 * @Return: nada.
 *******************************************************************/
    public void gestionMovimiento (long delta ) {
 // carga el movimiento de las entidades
        x += (delta * dx) / 1000 ;
        y += (delta * dy) / 1000 ;
    }
//******************************************************************
 /** Descripción: Crea la posición horizontal del sprite
 * @Params: dy. Velocidad horizontal de la entidad (pixels/sec)
 * @Return: nada.
 *******************************************************************/
    public void setHorizontal (double dx) {
        this .dx = dx;
    }

 //******************************************************************
 /** Descripción: Crea la posición vertical del sprite
 * @Params: dy. Velocidad vertical de la entidad (pixels/sec)
 * @Return: nada.
 *******************************************************************/
    public void setVertical (double dy) {
        this .dy = dy;
    }

 //******************************************************************
 /** Descripción: Devuelve la posición del sprite
 * @Params: nada.
 * @Return: Velocidad horizontal de la entidad (pixels/sec)
 *******************************************************************/
    public double getVeloHorizontal () {
        return dx;
    }

 //******************************************************************
 /** Descripción: Devuelve la posición del sprite
 * @Params: nada.
 * @Return: Velocidad vertical de la entidad (pixels/sec)
 *
 *******************************************************************/
    public double getVeloVertical () {
        return dy;
    }

 //******************************************************************
 /** Descripción: Dibuja los gráficos de esta entidad
 * @Params: g. Graficos del dibujo
 * @Return: nada.
 *
 *******************************************************************/
    public void dibujar (Graphics g) {
        sprite. dibujar (g,(int) x,(int) y);
    }

 //******************************************************************
 /** Descripción: Trabaja con la lógica asociada a esta entidad
 * Este método se llama frecuentemente
 * @Params: nada.
 * @Return: nada.
 *
 *******************************************************************/
    public void descender () {
    }

 //******************************************************************
 /** Descripción: Localiza la posición de x
 * @Params: nada.
 * @Return: La situación de la variable x de la entidad
 *
 *******************************************************************/

    public int getX () {
        return (int) x;
    }
 //******************************************************************
 /** Descripción: Localiza la posición de y
 * @Params: nada.
 * @Return: La situación de la variable y de la entidad
 *
 *******************************************************************/
    public int getY () {
        return (int) y;
    }

 //******************************************************************
 /** Descripción: Verifica si la entidad ha colisionado con otra
 * @Params: other. Entidad que verifica la colision
 * @Return: Verdadero si las entidades colisionan unas con otras
 *
 *******************************************************************/
    public boolean colisionaCon (movimiento other ) {
        yo.setBounds((int) x,(int) y,sprite. getAncho(),sprite.getAlto());
        el. setBounds((int)other.x, (int) other.y,other.sprite. getAncho(), other.sprite.getAlto());
        return yo. intersects (el);
    }

 //******************************************************************
 /** Descripción: Aviso a la entidad que ha colisionado con otra
 * @Params: other. entidad con la que colisionamos
 * @Return: Nada
 *
 *******************************************************************/
    public void haColisionado (movimiento other ){
 //si un misil del alien colisiona con
 //la nave guardian se eliminan los dos
        if (other instanceof guardian ) {
            juego. quitarEntidad (this ); // Eliminamos nave guardian
            juego. quitarEntidad (other ); // Eliminamos misil
            juego. avisoDerrota ();
        }
 //si un laser de la nave colisiona con
 //un alien se alnulan los dos.
        if (other instanceof laser ){
            juego. quitarEntidad (this );
            juego. quitarEntidad (other );
            juego. avisoMataAliens ();
        }
    }

 //*******************************************************************
 /** Descripción: El método run() es el corazón de cualquier "Thread" y contiene las tareas de ejecución, la acción sucede dentro del método run().
 * @Params: Nada
 * @Return: Nada
 ********************************************************************/
    public void run() {
        long lastLoopTime = System .currentTimeMillis ();
        while (true ) {
            esperaTecla = juego. getPaused ();
            long delta = System .currentTimeMillis () - lastLoopTime;
            lastLoopTime = System .currentTimeMillis ();
            if (!esperaTecla ) {
                gestionMovimiento (delta );
            }
            try {
                Thread .sleep (10);
            } catch (Exception e) {
                break ;
            }
        }
    }
 }
