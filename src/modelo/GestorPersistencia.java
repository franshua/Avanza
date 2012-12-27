package modelo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class GestorPersistencia {
    EntityManagerFactory emf; EntityManager em;
    static GestorPersistencia instancia = null;

    private GestorPersistencia() throws ErrorConexionBd{
        try{
            emf = Persistence.createEntityManagerFactory("Persistencia");
            em = emf.createEntityManager();
        }catch(Exception e){
            throw new ErrorConexionBd();
        }
    }
    public EntityManager getEntityManager() {
        return em;
    }
    public static void crearConexion() throws ErrorConexionBd {
        if (instancia == null) {
            instancia = new GestorPersistencia();
        }
    }
    public static GestorPersistencia instancia() {
        return instancia;
    }

    public static void desconectar() {
        if (instancia != null) {
            instancia.em.getTransaction().begin(); instancia.em.createNativeQuery("shutdown").executeUpdate(); 
            instancia.em.getTransaction().commit();
            instancia.em.close(); instancia.emf.close();
            instancia = null;
        }
    }
}
