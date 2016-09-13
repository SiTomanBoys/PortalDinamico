package cl.portaldinamico.ejbs;
import javax.ejb.Remote;

@Remote 
public interface Ejb3PruebaBeanRemote {
	public abstract String prueba();
}
