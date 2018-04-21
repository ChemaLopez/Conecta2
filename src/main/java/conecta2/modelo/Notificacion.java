package conecta2.modelo;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "notificaciones")
public class Notificacion {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String descripcion;
	
	private boolean leida;
	
	@ManyToOne (fetch=FetchType.EAGER)
	private Empresa empresa;
	
	@ManyToOne (fetch=FetchType.EAGER)
	private Particular particular;
	
	public Notificacion() {}
	
	public Notificacion(String descripcion, boolean leida, Empresa empresa, Particular particular) {
		
		this.descripcion = descripcion;
		this.leida = leida;
		this.empresa = empresa;
		this.particular = particular;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isLeida() {
		return leida;
	}

	public void setLeida(boolean leida) {
		this.leida = leida;
	}
	
}