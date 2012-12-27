package modelo;

public class ErrorConexionBd extends Exception {
    public ErrorConexionBd(){}
    public ErrorConexionBd(String msg) {
        super(msg);
    }
}
