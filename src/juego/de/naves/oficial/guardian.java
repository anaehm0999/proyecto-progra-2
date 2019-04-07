package juego.de.naves.oficial;

public class guardian extends nave {
    
    public guardian (invader juego, String ref,int x,int y) {
        super(juego,ref,x,y);
    }
    
    public void gestionMovimiento (long delta ) {
        if ((dx < 0) && (x < 10)) { 
            return;
        }
        
        if ((dx > 0) && (x > 750)){
        return ; 
        }
        super.gestionMovimiento (delta); 
    }  
}
