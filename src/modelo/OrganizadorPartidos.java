package modelo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class OrganizadorPartidos{
    private static OrganizadorPartidos INSTANCE;
    public static ArrayList<Deporte> deportes;
    private Usuario logeado;
    private EntityManager em;

    private synchronized static void createInstance() throws CadenaVacia, ObjetoVacio, FechaErronea, NumeroIncorrecto, ErrorConexionBd{
        if (INSTANCE == null) { 
            INSTANCE = new OrganizadorPartidos();
        }
    }
 
    public static OrganizadorPartidos getInstance() throws CadenaVacia, ObjetoVacio, FechaErronea, NumeroIncorrecto, ErrorConexionBd{
        createInstance();
        return INSTANCE;
    }

    private OrganizadorPartidos() throws CadenaVacia, NumeroIncorrecto, ErrorConexionBd{
        deportes = new ArrayList<>();
        logeado = null;
        
        conectar();
        
        getDeportes();

        if(deportes.isEmpty()){
            try{
                em.getTransaction().begin();
                Deporte d=new Deporte("Futbol",11,2);
                deportes.add(d);
                em.persist(d);
                d=new Deporte("Baloncesto",5,2);
                deportes.add(d);
                em.persist(d);
                d=new Deporte("Tenis individual",1,2);
                deportes.add(d);
                em.persist(d);
                d=new Deporte("Tenis dobles",2,2);
                deportes.add(d);
                em.persist(d);
                d=new Deporte("Padel",2,2);
                deportes.add(d);
                em.persist(d);
                em.getTransaction().commit();
            }catch(CadenaVacia | NumeroIncorrecto e){
                throw e;
            }
        }
    }

    public ArrayList<Deporte> getDeportes(){
        long i = 1;
        Deporte d;
        do{
            d = em.find(Deporte.class, i);
            if(d!=null && !deportes.contains(d)){
                deportes.add(d);
            }
            i++;
        }while(d!=null);
        return deportes;
    }

    public boolean crearUsuario(String nom, String clave, String correo){
        try{
            Usuario user = buscarUsuario(correo);
            return false;
        }catch(Exception e){
            if(clave.isEmpty()){
                return false;
            }
            Encriptador crip=new Encriptador();
            String codificada = crip.hash(clave);
            if(!validarEmail(correo)){
                return false;
            }
            try{
                Usuario user2 = new Usuario(nom, codificada, correo);
                em.getTransaction().begin();
                em.persist(user2);
                em.getTransaction().commit();
                return true;
            }catch(Exception e2){
                return false;
            }
        }
    }
    
    public boolean eliminarUsuario() {
        if(logeado!=null){
            em.getTransaction().begin();
            em.remove(logeado);
            em.getTransaction().commit();
            logeado=null;
            return true;
        }else{
            return false;
        }
    }
    
    private Usuario buscarUsuario(String correo) throws NoEncontrado{
        Usuario u;
        u = em.find(Usuario.class,correo);
        if(u!=null){
            return u;
        }
        throw new NoEncontrado();
    }
    
    public Usuario login(String correo,String pass) throws ObjetoVacio, NoEncontrado{
        Usuario user = buscarUsuario(correo);
        
        if(user!=null){
            Encriptador crip=new Encriptador();
            String codificada = crip.hash(pass);
            if(codificada.equals(user.getMD5())){
                logeado=user;
                return logeado;
            }else{
                throw new NoEncontrado();
            }
        }else{
            throw new ObjetoVacio();
        }
    }
    
    public void nuevoPartido(Partido partido) throws ObjetoVacio{
        if(partido==null){
            throw new ObjetoVacio();
        }else{
            em.getTransaction().begin();
            em.persist(partido);
            em.getTransaction().commit();
            logeado.setPartido(partido);
        }
    }
    
    
    public ArrayList<Partido> buscarPartidos(String deporte, Calendar fechaFin) throws ObjetoVacio{
        ArrayList<Partido> lista = new ArrayList<>();
        
        Calendar cal1 = Calendar.getInstance();
        if(fechaFin.before(cal1)){
            return lista;
        }

        int d = -1;
        for(int c=0;c<deportes.size();c++){
            if(deportes.get(c).getNombre().equals(deporte)){
                d = c+1;
                break;
            }
        }
        Query q = em.createQuery("Select p from Partido p Where p.deporte.nombre like :dep");
        q.setParameter("dep", deporte);
        List<Partido> l = q.getResultList();
        
        for(int c=0;c<l.size();c++){
            if(l.get(c).getFecha().after(cal1) && l.get(c).getFecha().before(fechaFin)){
                lista.add(l.get(c));
            }
        }

        return lista;
    }
    
    public ArrayList<Partido> partidosInscritos() throws ObjetoVacio{
        ArrayList<Partido> lista = new ArrayList<>();
        
        Calendar actual = Calendar.getInstance();
        
        for(int i =0;i<logeado.partidosInscrito().size();i++){
            if(logeado.partidosInscrito().get(i).getFecha().after(actual)){
                lista.add(logeado.partidosInscrito().get(i));
            }
        }
        
        return lista;
    }
    
    public int inscribirJugador(Usuario user, Partido match) throws ObjetoVacio{
        if(match==null || user==null){
            throw new ObjetoVacio();
        }else{
            em.getTransaction().begin();
            int i = user.inscribirPartido(match);
            em.getTransaction().commit();
            return i;
        }
    }
    
    public void cancelarJugador(Usuario user, Partido match) throws ObjetoVacio, CadenaVacia{
        if(match==null || user==null){
            throw new ObjetoVacio();
        }else{
            em.getTransaction().begin();
            user.cancelarPartido(match);
            em.getTransaction().commit();
        }
    }
    
    public boolean validarEmail(String mail){
        EmailValidator valida = new EmailValidator();
        return valida.validate(mail);
    }
    
    private void conectar() throws ErrorConexionBd{
        try{
            GestorPersistencia.crearConexion();
        }catch(ErrorConexionBd e){
            throw e;
        }
        em = GestorPersistencia.instancia.getEntityManager();
    }
    
    public Usuario getLogeado(){
        return logeado;
    }
    
    public void logout(){
        GestorPersistencia.desconectar();
    }
}
