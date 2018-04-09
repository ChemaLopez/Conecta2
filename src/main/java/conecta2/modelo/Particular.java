package conecta2.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

@Entity
@Table(name = "particulares")
/**
 * Entidad / Objeto de Negocio de Particular
 * Se utiliza para persistir la información del particular
 */
public class Particular {
	
	/**
	 * Id que genera la base de datos automáticamente, no se debe asignar manualmente
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@NotEmpty
	@Pattern(regexp="^([a-zA-ZáéíóúñÁÉÍÓÚÑ ])*$")
	private String nombre;
	
	@NotEmpty
	@Pattern(regexp="^([a-zA-ZáéíóúñÁÉÍÓÚÑ ])*$")
	private String apellidos;
	
	/**
	 * Filtro para evitar que se introduzcan dnis erróneos
	 */
	@NotEmpty
	@Pattern(regexp="^[0-9]{8}[A-Z]{1}$")
	@Column(unique=true)
	private String dni;
	
	/**
	 * Filtro para evitar que se introduzcan telefonos erróneos
	 */
	@NotEmpty
	@Pattern(regexp="^([0-9]{9})*$")
	private String telefono;
	
	/**
	 * Filtro para evitar que se introduzcan emails erróneos
	 */
	@Email
	@Pattern(regexp="^[^@]+@[^@]+\\.[a-zA-Z]{2,}$")
	@NotEmpty
	@Column(unique=true)
	private String email;
	
	@Length(min = 5)
	@NotEmpty
	@Transient
	private String password;


	private boolean activo;
	
	
	private int puntuacion;
	
	@ManyToMany
	private List<Oferta> ofertas;

	private String descripcion;
	/**
	 * Constructora sin argumentos necesaria para JPA
	 */
	public Particular() {}
	
	/**
	 * Constructora por defecto que se utiliza para crear particulates en los tests
	 * @param nombre
	 * @param apellidos
	 * @param dni
	 * @param email
	 * @param password
	 * @param activo
	 * @param puntuacion
	 */
	public Particular(String nombre, String apellidos, String dni, String telefono, String email, String password, boolean activo, int puntuacion, String descripcion) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.dni = dni;
		this.telefono = telefono;
		this.email = email;
		this.password = password;
		this.activo = activo;
		this.puntuacion = puntuacion;
		this.descripcion = descripcion;
		this.ofertas = new ArrayList<Oferta>();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	
	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public int getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(int puntuacion) {
		this.puntuacion = puntuacion;
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public List<Oferta> getOfertas() {
		return ofertas;
	}

	public void setOfertas(List<Oferta> ofertas) {
		this.ofertas = ofertas;
	}
	
	public void anadirOferta(Oferta oferta) {
		
		if(this.ofertas == null)
			this.ofertas = new ArrayList<Oferta>();
		this.ofertas.add(oferta);		
		
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
	    if (o == this) return true;
	    if (!(o instanceof Particular))return false;
		
	    Particular partObj = (Particular)o;
	    
	    return !(
	    		(this.id != partObj.id) ||
	    		(this.nombre != partObj.nombre) ||
	    		(this.apellidos != partObj.apellidos) ||
	    		(this.dni != partObj.dni) ||
	    		(this.telefono != partObj.telefono) ||
	    		(this.email != partObj.email) ||
	    		(this.password != partObj.password) ||
	    		(this.activo != partObj.activo) ||
	    		(this.puntuacion != partObj.puntuacion) ||
	    		(this.descripcion != partObj.descripcion)
	    		);
	}
}
