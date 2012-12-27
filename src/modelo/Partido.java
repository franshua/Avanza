package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Partido implements Serializable {
    @Id
    @GeneratedValue
    long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar fecha;
    private String lugar;
    private String comentarios;
    @ManyToOne
    private Usuario organizador;
    @ManyToMany(cascade=CascadeType.ALL,mappedBy="partidos")
    private List<Usuario> jugador;
    @ManyToMany(cascade=CascadeType.ALL)
    private List<Usuario> jugEspera;
    @ManyToOne
    private Deporte deporte;

    public Partido(){
        organizador = null;
        fecha = Calendar.getInstance();
        jugador = new ArrayList<>();
        jugEspera = new ArrayList<>();
    }
    
    public Partido(Usuario org) throws ObjetoVacio{
        if(org==null){
            throw new ObjetoVacio();
        }else{
            organizador=org;
        }
        fecha = Calendar.getInstance();
        jugador = new ArrayList<>();
        jugEspera = new ArrayList<>();
    }
    
    public boolean buscarJugador(Usuario player) throws ObjetoVacio{
        if(player==null){
            throw new ObjetoVacio();
        }else{
            for(int i=0;i<jugador.size();i++){
                if(jugador.get(i).equals(player)){
                    return true;
                }
            }
            return false;
        }
    }
    
    public void setFecha(Calendar date) throws FechaErronea{
        Calendar c = Calendar.getInstance();
        if(c.after(date)){
            throw new FechaErronea();
        }
        fecha = date;
    }
    
    public void setLugar(String place) throws CadenaVacia{
        if(place.isEmpty()){
            lugar = "Sin definir";
            throw new CadenaVacia();
        }
        lugar = place;
    }
    
    public void setComentarios(String comments){
        comentarios = comments;
    }
    
    public Calendar getFecha(){
        return fecha;
    }
    
    public String getLugar(){
        return lugar;
    }
    
    public String getComentarios(){
        return comentarios;
    }
    
    public Deporte getDeporte(){
        return deporte;
    }
    
    public void setDeporte(Deporte dep) throws ObjetoVacio{
        if(dep==null){
            throw new ObjetoVacio();
        }else{
            deporte=dep;
        }
    }
    
    public int addJugador(Usuario jug) throws ObjetoVacio{
        if(jug==null){
            throw new ObjetoVacio();
        }else{
            if(deporte.getJugadores()*deporte.getEquipos()==jugador.size()){
                jugEspera.add(jug);
                return 0;
            }else{
                jugador.add(jug);
                return 1;
            }
        }
    }
    
    public void delJugador(Usuario jug) throws ObjetoVacio, CadenaVacia{
        if(jug==null){
            throw new ObjetoVacio();
        }else{
            if(jugador.remove(jug)){
                if(!jugEspera.isEmpty()){
                    jugEspera.get(0).inscribirPartido(this);
                    //falta poner asunto y cuerpo
                    //enviarMail(jugEspera.get(0).getEmail(),"Timpik","Has sido añadido al partido "+this.getLugar());
                    jugEspera.remove(jugEspera.get(0));
                }
            }else{
                jugEspera.remove(jug);
            }
        }
    }
    
    public void enviarMail(String mailReceptor, String asunto, String cuerpo) throws CadenaVacia{
        if(mailReceptor.isEmpty() || asunto.isEmpty() || cuerpo.isEmpty()){
            throw new CadenaVacia();
        }
        EnviadorMail mail = new EnviadorMail(mailReceptor,asunto,cuerpo);
    }
}
