package modelo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Deporte implements Serializable {
    @Id
    @GeneratedValue
    long id;
    private String nombre;
    private int jugEquipo;
    private int numEquipos;

    public Deporte(){
        jugEquipo = 0;
        numEquipos = 0;
    }

    public Deporte(String nom, int jug, int num) throws CadenaVacia, NumeroIncorrecto{
        if(jug<1){
            throw new NumeroIncorrecto();
        }
        if(num<0){
            throw new NumeroIncorrecto();
        }
        if(nom.isEmpty()){
            throw new CadenaVacia();
        }
        nombre = nom;
        jugEquipo = jug;
        numEquipos = num;
    }

    public void setNombre(String nom) throws CadenaVacia{
        if(nom.isEmpty()){
            throw new CadenaVacia();
        }
        nombre = nom;
    }
    
    public void setJugadores(int jug) throws NumeroIncorrecto{
        if(jug<1){
            throw new NumeroIncorrecto();
        }
        jugEquipo = jug;
    }
    
    public void setEquipos(int eq)throws NumeroIncorrecto{
        if(eq<0){
            throw new NumeroIncorrecto();
        }
        numEquipos = eq;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public int getJugadores(){
        return jugEquipo;
    }
    
    public int getEquipos(){
        return numEquipos;
    }
}
