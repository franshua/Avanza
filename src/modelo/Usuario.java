package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Usuario implements Serializable {
    @Id
    protected String email;
    protected String nombre;
    protected String md5;
    @OneToMany(cascade=CascadeType.ALL, mappedBy="organizador")
    private List<Partido> partidoOrganizado;
    @ManyToMany(cascade=CascadeType.ALL)
    private List<Partido> partidos;

    public Usuario(){
        partidoOrganizado = new ArrayList<>();
        partidos = new ArrayList<>();
    }
    
    public Usuario(String nom, String codificada, String correo) throws CadenaVacia{
        if(nom.isEmpty() || codificada.isEmpty() || correo.isEmpty()){
            throw new CadenaVacia();
        }
        nombre = nom;
        md5 = codificada;
        email = correo;
        partidoOrganizado = new ArrayList<>();
        partidos = new ArrayList<>();
    }
    
    public void setNombre(String nom) throws CadenaVacia{
        if(nom.isEmpty()){
            throw new CadenaVacia();
        }
        nombre = nom;
    }
    
    public void setMD5(String cod) throws CadenaVacia{
        if(cod.isEmpty()){
            throw new CadenaVacia();
        }
        md5 = cod;
    }
    
    public void setEmail(String correo) throws CadenaVacia{
        if(correo.isEmpty()){
            throw new CadenaVacia();
        }
        email = correo;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public String getMD5(){
        return md5;
    }
    
    public String getEmail(){
        return email;
    }
    
    public void setPartido(Partido par)throws ObjetoVacio{
        if(par==null){
            throw new ObjetoVacio();
        }
        partidoOrganizado.add(par);
    }
    
    public int inscribirPartido(Partido p) throws ObjetoVacio{
        if(p==null){
            throw new ObjetoVacio();
        }
        int i = p.addJugador(this);
        if(i==1){
            partidos.add(p);
        }
        return i;
    }

    public void cancelarPartido(Partido p) throws ObjetoVacio, CadenaVacia{
        if(p==null){
            throw new ObjetoVacio();
        }
        p.delJugador(this);
        partidos.remove(p);
    }

    public List<Partido> partidosInscrito(){
        return partidos;
    }
}
