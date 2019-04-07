package juego.de.naves.oficial;

import java.awt. Canvas;
import java.awt. Color; 
import java.awt. Dimension;
import java.awt. Graphics2D;
import java.awt.event. KeyAdapter;
import java.awt.event. KeyEvent;
import java.awt.event. WindowAdapter;
import java.awt.event. WindowEvent;
import java.awt.image. BufferStrategy;
import java.util. ArrayList; 
import javax.swing. JFrame;
import javax.swing. JPanel ; 



public class invader extends Canvas implements Runnable {
       private BufferStrategy estrategia;
       private boolean ejecucionJuego = true; 
       private ArrayList entidades = new ArrayList ();
       private ArrayList quitamosLista = new ArrayList ();
       private movimiento naveDef;
       private double velocidadMovi = 150;
       private long ultimoDis = 0;
       private long intervadoNave = 650; 
       private int cuentaAliens;
       private String mensaje = "";
       private boolean esperaTecla = true; 
       private boolean teclaIzd = false; 
       private boolean teclaDer = false; 
       private boolean teclaDis = false;
       private boolean logicaAplicaJuego = false;
       public Thread th = new Thread (this);
    
   
   public invader() {
       JFrame container = new JFrame ("Space Invaders" ); 
       JPanel panel = (JPanel ) container. getContentPane ();
       panel. setPreferredSize (new Dimension (800,600));
       panel. setLayout (null);       
       setBounds (0,0,800,600); 
       panel. add(this); 
       setIgnoreRepaint (true); 
       container. pack();
       container. setResizable (false);
       container. setVisible (true);
       container. addWindowListener (new WindowAdapter () { 
        public void windowClosing (WindowEvent e) {
           System .exit(0);}}); 
       
       addKeyListener (new KeyInputHandler ());
       requestFocus (); 
       
       createBufferStrategy (2);
       estrategia = getBufferStrategy ();
       
       init(); 
   }
   
   private void empezar () { 
       entidades. clear(); 
       init();
       
       teclaIzd = false;
       teclaDer = false;
       teclaDis = false;
    }
   
   public boolean getPaused () {
       return esperaTecla;
   }
   
   private void init() {
       th = new Thread (this); 
       naveDef = new guardian (this,"nave.gif" ,370,550);
       entidades. add(naveDef );
       naveDef.th. start();
       cuentaAliens = 0; 
       for (int row=0;row<5;row++) {
           for (int x=0;x<9;x++) { 
                movimiento alien = new Aliens(this,"alien.gif" ,100+(x*65),(50)+row*30); 
                entidades. add(alien);
                cuentaAliens ++;
           }
       }
   }
   
   public void cargaLogica () {
       logicaAplicaJuego = true;
   } 
   
   public void quitarEntidad (movimiento entity ) {
       quitamosLista. add(entity );
   } 
   
   public void avisoDerrota () {
       mensaje ="¿Otra partida?" ;
       esperaTecla = true; 
   }
   
   public void avisoVictoria () {
       mensaje ="FELICIDADES HAS GANADO";
       esperaTecla = true; 
   }
   
   public void avisoMataAliens () {
       cuentaAliens --;
       if (cuentaAliens == 0) {
           avisoVictoria ();
       }
       
       for (int i=0;i<entidades. size();i++) {
            movimiento entity = (movimiento ) entidades. get(i);
            if (entity instanceof Aliens ) { 
                entity. setHorizontal (entity. getVeloHorizontal () * 1.03); 
            }
        }
   }
 
   public void verificaDisparo () {
       if (System .currentTimeMillis () - ultimoDis < intervadoNave ) {
           return ;
       }
       
       ultimoDis = System .currentTimeMillis ();
       
       laser shot = new laser(this,"laser.gif" ,naveDef. getX()+13,naveDef. getY()-10);
       shot.th. start();
       entidades. add(shot);
   }
   
   public void run() {
       long lastLoopTime = System .currentTimeMillis ();
       while (ejecucionJuego ) { 
           long delta = System .currentTimeMillis () - lastLoopTime; 
           lastLoopTime = System .currentTimeMillis ();
           Graphics2D g = (Graphics2D ) estrategia. getDrawGraphics (); 
           g. setColor (Color.black );
           g. fillRect (0,0,800,600); 
           if (!esperaTecla ) {
               for (int i=0;i<entidades. size();i++) {
                    movimiento entity = (movimiento ) entidades. get(i);
                    entity. gestionMovimiento (delta);
                }
           }
           for (int i=0;i<entidades. size();i++) {         
                movimiento entity = (movimiento ) entidades. get(i);
                entity. dibujar (g);  
           } 
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
           
           entidades. removeAll (quitamosLista );
           quitamosLista. clear();
           
           if (logicaAplicaJuego ) { 
               for (int i=0;i<entidades. size();i++) {
                   movimiento entity = (movimiento ) entidades. get(i);
                   entity. descender ();
               }
            logicaAplicaJuego = false; 
           }
           
           if (esperaTecla ) {                
                g. setColor (Color.white );                
                g. drawString (mensaje, (800-g.getFontMetrics ().stringWidth (mensaje ))/2,250);        
                g. drawString ("Presionar una tecla" ,(800-g.getFontMetrics().stringWidth("Presionar una tecla" ))/2,300);
           }
           g. dispose ();
           estrategia. show();
           
           naveDef. setHorizontal (0);
            if ((teclaIzd ) && (!teclaDer )) {
                naveDef. setHorizontal (-velocidadMovi );           
            } else if ((teclaDer ) && (!teclaIzd )) {
                 naveDef. setHorizontal (velocidadMovi );
            }
       
            if (teclaDis ) {                
                verificaDisparo ();    
            } 
       
            try { Thread .sleep(10); } catch (Exception e) {}
       
            int numAliensTiro = 0; 
            for (int i=0;i<entidades. size();i++) { 
                movimiento entity = (movimiento ) entidades. get(i);
                if (entity instanceof misil ) {
                    numAliensTiro ++; 
                }    
            }
       
            if (Math.random ()<0.03) {
                for (int i=0;i<entidades. size();i++) { 
                    movimiento entity = (movimiento ) entidades. get(i);
                    if (entity instanceof Aliens ) { 
                        if (Math.random ()<0.02 && numAliensTiro <=5) { 
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
   
   private class KeyInputHandler extends KeyAdapter {
       private int pressCount = 1;
       public void keyPressed (KeyEvent e) { 
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
       
       public void keyReleased (KeyEvent e) {
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
/** Descrip: Una tecla ha sido pulsada
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
 /** Descrip: Método stop()
 * @Params: Nada
 * @Return: Nada
 *
 **********************************************************************/
        public void stop (){
            if (th!=null ) th. stop ();
        }

//******************************************************************
 /** Descrip: Punto de entrada al juego, se crea una instancia de
 * clase que inicializa la ventana de juego y el bucle
 * @Params: argv. Argumentos pasados al juego
 * @Return: nada.
 *
 *******************************************************************/
            public static void main (String argv []) {
                   invader g =new invader ();
                   // Comienzo del juego, el método no devuelve nada hasta
                   // que finaliza el juego, de ahí que utilicemos el
                   // principal thread para ejecutar el juego
                   g. run();
            }     
        
}
      

