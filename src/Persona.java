
/*
 * @author Sergio Jimenez Roncero
 */

public class Persona {
	
	private String nombre;
	private boolean vivo;
	
	public Persona(String nombre) {
		this.nombre = nombre;
		this.vivo = true;
	}
	
	public boolean getVivo() {
		return vivo;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setVivo(boolean vivo) {
		this.vivo = vivo;
	}
	
}
