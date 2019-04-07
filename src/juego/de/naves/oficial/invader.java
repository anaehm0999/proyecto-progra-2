/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juego.de.naves.oficial;

/*******************************************************************
 ********************************************************************
 **  Importaciones generales del sistema
 ******************************************************************** 
 ********************************************************************/
import java.awt.Canvas;
import java.awt.Color; 
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList; 
import javax.swing.JFrame;
import javax.swing.JPanel; 

/*****************************************************************
/*****************************************************************
/* Clase principal del juego, se encarga de coordinar las funciones del programa y coordinar la lógica de juego, la gestión 
/* consiste en ejecutar el bucle del juego moviendo y dibujando las entidades en el lugar apropiado. Será informada cuando 
/* las entidades dentro del juego detecten eventos, como deribar un alien, el derribo de la nave defensora actuando de manera adecuada.
/*****************************************************************
/*****************************************************************/

public class invader extends Canvas implements Runnable {
    /** Estrategia que nos permite utilizar page flipping */   
    private BufferStrategy estrategia;
    /** Verdadero si el juego se está ejecutando */   
    private boolean ejecucionJuego = true; 
    /** Lista de todas las entidades que existen en el juego*/    
    private ArrayList entidades = new ArrayList ();
    /** Lista de entidades que necesitamos eliminar del juego*/   
    private ArrayList quitamosLista = new ArrayList ();
    /** movimiento de la nave defensora*/    
    private movimiento naveDef;
    /** Velocidad a la que se mueve el jugador (pixels/sec)*/    
    private double velocidadMovi = 150;
    /** Tiempo del último laser*/   
    private long ultimoDis = 0;
    /** Intervalo de laser de la nave (ms) */   
    private long intervadoNave = 650; 
    /** Número de aliens que se muestran en pantalla*/    
    private int cuentaAliens;
    /** Mensaje impreso por pantalla, esperando presionar tecla*/   
    private String mensaje = "";
    /** Verdadero si no presionamos ninguna tecla*/   
    private boolean esperaTecla = true; 
    /** Verdadero si presionamos la tecla de izquierda*/   
    private boolean teclaIzd = false; 
    /** Verdadero si presionamos la tecla de derecha*/   
    private boolean teclaDer = false; 
    /** Verdadero si estamos disparando*/   
    private boolean teclaDis = false;
    /** Verdadero si la lógica de juego necesita ser aplicada por lo general como consecuencia de un evento */
    private boolean logicaAplicaJuego = false;
    /** Declaración del Thread*/    
    public Thread th = new Thread (this);
    
   //******************************************************************
    /** Descripción: Constructor de clase y puesta en marcha del juego
    /*  @Params: nada 
    /*  @Return: nada   
    /*******************************************************************/
   public invader() {
       // crea un frame para contener el juego 
       JFrame container = new JFrame ("Space Invaders" ); 
       // Coje el contenido del frame y carga la resolución del juego 
       JPanel panel = (JPanel ) container. getContentPane ();
       panel. setPreferredSize (new Dimension (800,600));
       panel. setLayout (null);       
       // Carga la ventana, (Canvas) y lo pone en el frame 
       setBounds (0,0,800,600); 
       panel. add(this); 
       // Informa AWT no interrumpe a la aceleración gráfica 
       setIgnoreRepaint (true); 
       // finalmente vemos la pantalla
       container. pack();
       //fija la ventana
       container. setResizable (false);
       container. setVisible (true);
       // información para cerrar la ventana suponiendo que queramos salir del juego
       container. addWindowListener (new WindowAdapter () { 
        
       public void windowClosing (WindowEvent e) {
       System .exit(0);}}); 
       // añade las teclas del sistema, definidas más tarde 
       addKeyListener (new KeyInputHandler ());
       requestFocus (); 
       // crea el BufferStrategy        
       // administrar la aceleración de gráficos 
       createBufferStrategy (2);
       estrategia = getBufferStrategy ();
       // inicializa las entidaddes del juego 
       init(); 
   }
   
   //*****************************************************************
   /** Descripción: Comienza el juego, debe de partir de cero con todos los aliens y la nave defensora                                                   
   *  @Params: nada 
   *  @Return: nada
   ******************************************************************/
    private void empezar () {
       // limpiar si se ha inicializado alguna entidad
       entidades. clear(); 
       init();
       // inicializa las teclas 
       teclaIzd = false;
       teclaDer = false;
       teclaDis = false;
    }
       
    public boolean getPaused () {
       return esperaTecla;
    }
    
    //****************************************************************** 
    /** Descripción: Inicializa las entidades, cada entidad debe de ser añadida en el conjunto del juego
    *  @Params: nada
    *  @Return: nada
    *******************************************************************/     
    private void init() {
       th = new Thread (this); //Crea un nuevo hilo
       // se crea la nave defensora en el centro de la pantalla 
       naveDef = new guardian (this,"nave.gif" ,370,550);
       entidades. add(naveDef );
       naveDef.th. start();
       // Grupo inicial de aliens en este caso 5 filas y 9 columnas 
       cuentaAliens = 0; 
       for (int row=0;row<5;row++) {
           for (int x=0;x<9;x++) { 
               // Distancia lateral entre ellos 100+(x*80)
               // Distancia vertical entre ellos (50)+row*30  
               movimiento alien = new Aliens(this,"alien.gif" ,100+(x*65),(50)+row*30); 
                entidades. add(alien);
                cuentaAliens ++;
           }
       }
    }
   
 //******************************************************************
 /** Descripción: Inicializa la lógica del juego, que comenzará a funcionar cuando lo requiera algún evento
 *  @Params: nada 
 *  @Return: nada
 *******************************************************************/
    public void cargaLogica () {
       logicaAplicaJuego = true;
    }     
 
//******************************************************************
 /** Descripción: Quita la entidad del juego
 *  @Params: entity. La entidad debe ser quitada
 *  @Return: nada
 *******************************************************************/  
    public void quitarEntidad (movimiento entity ) {
       quitamosLista. add(entity );
    } 

 //******************************************************************
 /** Descripción: Aviso que la nave defensora es destruida 
 *  @Params: nada 
 *  @Return: nada
 *******************************************************************/  
    public void avisoDerrota () {
       mensaje ="¡Has Perdido!  -->  ¿Otra partida?" ;
       esperaTecla = true; 
    }

 //******************************************************************
 /** Descripción: Aviso que la nave defensora destruye todos los alien 
 *  @Params: nada 
 *  @Return: nada
 *******************************************************************/ 
    public void avisoVictoria () {
       mensaje ="¡¡¡FELICIDADES HAS GANADO!!!";
       esperaTecla = true; 
    }

 //******************************************************************
 /** Descripción: Aviso que un alien ha sido destruid 
 *  @Params: nada 
 *  @Return: nada
 *******************************************************************/     
    public void avisoMataAliens () {
       // decrementa los alien, si no quedan más, el jugador ha ganado 
        cuentaAliens --;
       if (cuentaAliens == 0) {
           avisoVictoria ();
       }
       // si todavía hay algunos alien entonces acelerar todos los que quedan
       for (int i=0;i<entidades. size();i++) {
            movimiento entity = (movimiento ) entidades. get(i);
            if (entity instanceof Aliens ) { 
                entity. setHorizontal (entity. getVeloHorizontal () * 1.03); 
            }
        }
    }
    
//******************************************************************
 /** Descripción: laser del jugador
 *  @Params: nada 
 *  @Return: nada
 *******************************************************************/ 
    public void verificaDisparo () {
       // verifica tiempo de espera del disparo
        if (System .currentTimeMillis () - ultimoDis < intervadoNave ) {
           return ;
       }
       // si esperamos lo suficiente, creamos un Nuevo disparo y tomamos el tiempo 
       ultimoDis = System .currentTimeMillis ();
       // Posicion inicial disparo de nave naveDef.getX()+13,naveDef.getY()-10 
       laser shot = new laser(this,"laser.gif" ,naveDef. getX()+13,naveDef. getY()-10);
       shot.th. start();
       entidades. add(shot);
    }

 //******************************************************************
 /** Descripción:  El método run() es el corazón de cualquier "Thread" y contiene las tareas de ejecución, la acción sucede 
 *  dentro del método run(), además es el bucle del juego, este bucle se ejecuta durante todo el juego siendo reponsable de 
 *  todas las actividades Calcular el tiempo desde el último bucle Procesar las entradas del jugador 270 Mover todo basado en 
 *  el tiempo desde el último bucle Dibujar todo lo que hay en pantalla Actualizar buffers para que la nueva imagen sea visible 
 *  Verifica entradas Actualiza los eventos 
 *  
 * @Params: nada 
 *  @Return: nada
 *******************************************************************/ 
    public void run() {
        
        long lastLoopTime = System .currentTimeMillis ();
        // Inicializa y mantiene el bucle hasta el fin del juego
        while (ejecucionJuego ) { 
           //se utiliza para calcular en qué medida las entidades deberán mover este bucle
            long delta = System .currentTimeMillis () - lastLoopTime; 
            lastLoopTime = System .currentTimeMillis ();
            // Encuentra el contexo para la aceleración de gráficos
            Graphics2D g = (Graphics2D ) estrategia. getDrawGraphics (); 
            g. setColor (Color.black );
            g. fillRect (0,0,800,600); 
            if (!esperaTecla ) {
               for (int i=0;i<entidades. size();i++) {
                    movimiento entity = (movimiento ) entidades. get(i);
                    entity. gestionMovimiento (delta);
                }
            }
            
            // Dibuja las enidades del juego
            for (int i=0;i<entidades. size();i++) {         
                movimiento entity = (movimiento ) entidades. get(i);
                entity. dibujar (g);  
            }
            
       // fuerza bruta colisiones, comparar cada entidad con las demás. Si cualquiera de ellas chocan notifica que ambas entidades han colisionado 
            for (int p=0;p<entidades. size();p++) {                
                for (int s=p+1;s<entidades. size();s++) {
                    movimiento yo = (movimiento ) entidades. get(p);
                    movimiento el = (movimiento ) entidades. get(s);
                
                    if (yo.colisionaCon (el)) { 
                        yo. haColisionado (el);
                        el. haColisionado (yo);
                    }
                }
            }
           
            // Eliminar entidades marcadas
            entidades. removeAll (quitamosLista );
           quitamosLista. clear();
           
           // Si un evento ha indicado que debería ejecutarse la lógica del juego entonces se solicita a cada entidad 
            if (logicaAplicaJuego ) { 
               for (int i=0;i<entidades. size();i++) {
                   movimiento entity = (movimiento ) entidades. get(i);
                   entity. descender ();
               }
             logicaAplicaJuego = false; 
            }
           
           // Esperamos que se presione una tecla para comenzar el juego nos lo muestra el mensaje mensaje
            if (esperaTecla ) {                
                g. setColor (Color.white );                
                g. drawString (mensaje, (800-g.getFontMetrics ().stringWidth (mensaje ))/2,250);        
                g. drawString ("Presionar una tecla" ,(800-g.getFontMetrics().stringWidth("Presionar una tecla" ))/2,300);
            }
            g. dispose ();
            estrategia. show();
            // Resolver el movimiento de la nave En primer lugar asumir que la nave no se mueve. Si una tecla de cursor se
            // presiona entonces actualizar el movimiento
            naveDef. setHorizontal (0);
            if ((teclaIzd ) && (!teclaDer )) {
                naveDef. setHorizontal (-velocidadMovi );           
            } else if ((teclaDer ) && (!teclaIzd )) {
                 naveDef. setHorizontal (velocidadMovi );
            }
            //Si estamos presionando fuego, entoces dispara
            if (teclaDis ) {                
                verificaDisparo ();    
            } 
       
            try { Thread .sleep(10); } catch (Exception e) {}
            // Cuenta el numero de disparos de los aliens
            int numAliensTiro = 0; 
            for (int i=0;i<entidades. size();i++) { 
                movimiento entity = (movimiento ) entidades. get(i);
                if (entity instanceof misil ) {
                    numAliensTiro ++; 
                }    
            }
            
            // Cuando comienza el juego los aliens empiezan a disparar aleatoriamente 
            if (Math.random ()<0.03) {
                for (int i=0;i<entidades. size();i++) { 
                    movimiento entity = (movimiento ) entidades. get(i);
                    if (entity instanceof Aliens ) { 
                        // Frecuencia de disparo 
                        if (Math.random ()<0.02 && numAliensTiro <=5) { 
                            // Posicion inicial disparo de los aliens: 
                            misil alienTiro = new misil
                            (this, "misil.gif" , entity. getX()+13, entity. getY()+25);
                                    entidades. add(alienTiro );
                                    alienTiro.th. start(); 
                        }
                    }
                }    
            }
        }        
    } 
    
 //******************************************************************
 /** Descripción: Clase que recoje las pulsaciones del teclado, las habituales izquierda, derecha y disparo. Se impementa como una clase interna 
 *  @Params: nada 
 *  @Return: nada
 *******************************************************************/ 
    private class KeyInputHandler extends KeyAdapter {
        // Número de teclas presionadas mientras esperamos presionar una tecla 
        private int pressCount = 1;
        //******************************************************************
        /** Descripción: Una tecla ha sido presionad
        *  @Params: e. Detalles de la tecla presionad
        *  @Return: nada
        *******************************************************************/ 
        public void keyPressed (KeyEvent e) { 
            // Esperamos presionar una tecla 
            if (esperaTecla ) {
               return ;
            }
            if (e.getKeyCode () == KeyEvent .VK_O) {
               teclaIzd = true;
            }
            if (e.getKeyCode () == KeyEvent .VK_P) {
               teclaDer = true;
            }
            if (e.getKeyCode () == KeyEvent .VK_SPACE ) {
               teclaDis = true;
            }    
        }

 //******************************************************************
 /** Descripción: Una tecla ha sido liberad
 *  @Params: e. Detalles de la tecla liberada 
 *  @Return: nada
 *******************************************************************/        
        public void keyReleased (KeyEvent e) {
            // Esperamos liberar una tecla
            if (esperaTecla ) { 
               return; 
            }
            if (e.getKeyCode () == KeyEvent .VK_O) {
               teclaIzd = false;
            }
            if (e.getKeyCode () == KeyEvent .VK_P) { 
               teclaDer = false;
            }
            if (e.getKeyCode () == KeyEvent .VK_SPACE ) { 
               teclaDis = false;
            }
        }
       
//******************************************************************
/** Descripción: Una tecla ha sido pulsada
 * @Params: e. Detalles de la tecla pulsada
 * @Return: nada.
 *
 *******************************************************************/
        public void keyTyped (KeyEvent e) {
            if (esperaTecla ) {
                if (pressCount == 1) {
                    esperaTecla = false ;
                    empezar ();
                    pressCount = 0;
                } else {
                    pressCount ++;
                }  
            }
 // si pulsamos escape, salimos del juego
            if (e.getKeyChar () == 27) {
                System .exit (0);
            }
        }
    } 
//*********************************************************************
 /** Descripción: Método stop()
 * @Params: Nada
 * @Return: Nada
 *
 **********************************************************************/
        public void stop (){
            if (th!= null ) th. stop ();
        }

//******************************************************************
 /** Descripción: Punto de entrada al juego, se crea una instancia de clase que inicializa la ventana de juego y el bucle
 * @Params: argv. Argumentos pasados al juego
 * @Return: nada.
 *
 *******************************************************************/
            public static void main (String argv []) {
                   invader g = new invader ();
                   // Comienzo del juego, el método no devuelve nada hasta
                   // que finaliza el juego, de ahí que utilicemos el
                   // principal thread para ejecutar el juego
                   g. run();
            }     
        
}
