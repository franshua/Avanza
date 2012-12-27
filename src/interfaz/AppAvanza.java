package interfaz;

import javax.swing.*;
import modelo.CadenaVacia;
import modelo.ErrorConexionBd;
import modelo.FechaErronea;
import modelo.NoEncontrado;
import modelo.NumeroIncorrecto;
import modelo.ObjetoVacio;
import modelo.OrganizadorPartidos;

public class AppAvanza {

    public static void main(String[] args) throws ObjetoVacio, NoEncontrado, ErrorConexionBd{
        try{
            // Set System L&F 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
        }
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) { 
           // handle exception 
        }
    
        Conectando car = new Conectando();
        car.setVisible(true);
        try{
            OrganizadorPartidos.getInstance();
        }catch(CadenaVacia | ObjetoVacio | FechaErronea | NumeroIncorrecto e){
        }
        car.setVisible(false);
        try{
            Login l = new Login();
            l.setVisible(true);
        }catch(CadenaVacia | ErrorConexionBd | FechaErronea | NumeroIncorrecto | ObjetoVacio e){
            
        }
    }
}
