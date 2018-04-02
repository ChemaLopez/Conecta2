package conecta2.controlador;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import conecta2.modelo.Empresa;
import conecta2.modelo.Particular;
import conecta2.modelo.Rol;
import conecta2.servicioAplicacion.SAEmail;
import conecta2.servicioAplicacion.SAEmpresa;
import conecta2.servicioAplicacion.SAParticular;
import conecta2.transfer.TransferParticular;
import conecta2.transfer.TransferEmpresa;
/**
 * Controlador de la aplicación, en él se mapean las diferentes peticiones (GET, POST...),
 * @author ferlo
 * Se redirige entre vistas y hace uso de los Servicios de Aplicación
 */
@Controller
public class ControladorPrincipal {	
	@Autowired	
	private SAParticular saParticular;
	@Autowired
	private SAEmpresa saEmpresa;
	@Autowired
	private SAEmail saEmail;
	

	/**
	 * Método que captura las peticiones GET de /login
	 * @return devuelve la vista de Inicio de sesion
	 */
	@RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
	public ModelAndView login(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("index");
		return modelAndView;
	}
	
	/**
	 * Método que captura las peticiones GET de /crear-cuenta
	 * @return devuelve la vista para Crear una cuenta, mostrando por defecto el registro de empresa
	 */
	@RequestMapping(value="/crear-cuenta", method = RequestMethod.GET)
	public ModelAndView registration(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("transferParticular", new TransferParticular());
		modelAndView.addObject("transferEmpresa", new TransferEmpresa());
		modelAndView.setViewName("crearCuenta");
	
		return modelAndView;
	}
	
	/**
	 * Método que captura las peticiones POST de /crear-empresa
	 * @param transferEmpresa que recibe para insertar la empresa con los datos
	 * @param bindingResult clase para controlar los errores producidos al introducir los datos
	 * @return redirige a inicio si no ha habido fallos, en caso contrario notifica sin cambiar de pagina
	 */
	@RequestMapping(value = "/crear-empresa", method = RequestMethod.POST)
	public ModelAndView crearEmpresa (@ModelAttribute ("transferEmpresa") @Valid TransferEmpresa transferEmpresa, BindingResult bindingResult) {
		ModelAndView modelAndView = null;
		Empresa empresa = saEmpresa.buscarPorEmail(transferEmpresa.getEmail());
		Empresa cif = saEmpresa.buscarPorCif(transferEmpresa.getCif());
		
		TransferParticular transferParticular = new TransferParticular();
		
		if (!transferEmpresa.getPassword().equals(transferEmpresa.getPasswordConfirmacion())) {
			bindingResult.rejectValue("password", "error.transferEmpresa", "* Las contraseñas no coinciden");
		}
		if (empresa != null)
			bindingResult.rejectValue("email", "error.transferEmpresa", "* Ya existe una empresa con este e-mail");	
		
		if (cif != null)
			bindingResult.rejectValue("cif", "error.transferEmpresa", "* Ya existe una empresa con este CIF");
		
		if (bindingResult.hasErrors()) {
			modelAndView = new ModelAndView("crearCuenta", bindingResult.getModel());
			modelAndView.addObject("transferEmpresa", transferEmpresa);
			modelAndView.addObject("transferParticular", transferParticular);
			
		}		
		else {
			saEmpresa.crearEmpresa(transferEmpresa);
			modelAndView = new ModelAndView("redirect:/");
		}
		
		return modelAndView;
	}
	
	/**
	 * Método que captura las peticiones POST de /crear-particular
	 * @param transferParticular que recibe para insertar el particular con los datos
	 * @param bindingResult clase para controlar los errores producidos al introducir los datos
	 * @return redirige a inicio si no ha habido fallos, en caso contrario notifica sin cambiar de pagina
	 */
	@RequestMapping(value = "/crear-particular", method = RequestMethod.POST)
	public ModelAndView crearParticular(@ModelAttribute("transferParticular") @Valid TransferParticular transferParticular, BindingResult bindingResult) {
		ModelAndView modelAndView = null;
		Particular particular = saParticular.buscarPorEmail(transferParticular.getEmail());
		
		TransferEmpresa transferEmpresa = new TransferEmpresa();
		
		if (!transferParticular.getPassword().equals(transferParticular.getPasswordConfirmacion())) {
			bindingResult.rejectValue("password", "error.transferParticular", "* Las contraseñas no coinciden");
		}
		if (particular != null)
			bindingResult.rejectValue("email", "error.dtoUsuario", "* Ya existe un particular con este e-mail");		
		
		if (bindingResult.hasErrors()) {
			modelAndView = new ModelAndView("crearCuenta", bindingResult.getModel());
			modelAndView.addObject("transferParticular", transferParticular);
			modelAndView.addObject("transferEmpresa", transferEmpresa);
		}			
		else {
			saParticular.crearParticular(transferParticular);
			modelAndView = new ModelAndView("redirect:/");
		}
		
		modelAndView.addObject("roles", Rol.values());
		
		return modelAndView;
	}
	
	@RequestMapping(value="/menu", method = RequestMethod.GET)
	public ModelAndView mostrarMenu(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("menu");
		return modelAndView;
	}
	
	@RequestMapping("/informacion")
    public String mostrarInformacion() {
        return "informacion";
    }
	
	/**
	 * Método que autentica al usuario capturando la petición GET de /authorization
	 * @param val usuario que se autentica
	 * @return redirige a la página principal si no ha habido fallos, en caso contrario notifica sin cambiar de pagina
	 */
	@RequestMapping(value="/authorization", method = RequestMethod.GET, params = {"val"})
	public ModelAndView autorizacion(@RequestParam("val") String val){ 
	

		Object obj = saEmail.validaUsuario(val);
		
		ModelAndView modelAndView = null;
		
		
		if(obj==null) {
			//MOSTRAR MENSAJE DE ERROR
			
		}else{
			//ES UNA EMPRESA
			if(obj.getClass()== (new TransferEmpresa()).getClass()){
				TransferEmpresa myTransf= (TransferEmpresa) obj;
				modelAndView = new ModelAndView();
				modelAndView.addObject("transferEmpresa", myTransf);
				modelAndView.setViewName("modificarEmpresa");
				//modelAndView = new ModelAndView("redirect:/verPerfilEmpresa?val="+ myTransf.getEmail());
				
				
			}else {
				//ES UN PARTICULAR
				TransferParticular myTransf= (TransferParticular) obj;
				modelAndView = new ModelAndView();
				modelAndView.addObject("transferParticular", myTransf);
				modelAndView.setViewName("perfilParticular");
				//modelAndView = new ModelAndView("redirect:/verPerfilParticular?val="+ myTransf.getEmail());
				
				
			}
			
		}
		return modelAndView;
	}
	
	/**
	 * Método que captura la petición GET de /verPerfilEmpresa
	 * @param val usuario que se autentica
	 * @return redirige a la página que muestra el perfil si no ha habido fallos,
	 *  en caso contrario redirige al login y notifica
	 */
	@RequestMapping(value ="/verPerfilEmpresa", method = RequestMethod.GET, params = {"val"})
    public ModelAndView mostrarPerfilEmpresa(@RequestParam("val") String val) { 
		
		Empresa empresa = saEmpresa.buscarPorEmail(val);
		
		ModelAndView modelAndView = new ModelAndView();
		
		TransferEmpresa tEmpresa = new TransferEmpresa(empresa.getNombreEmpresa(),empresa.getCif(),empresa.getEmail(),"0","0",true);
		
		modelAndView.addObject("transferEmpresa", tEmpresa);
		modelAndView.setViewName("perfilEmpresa");
		
		return modelAndView;
    }
	
	@RequestMapping(value ="/verPerfilParticular", method = RequestMethod.GET, params = {"val"})
    public ModelAndView mostrarPerfilParticular(@RequestParam("val") String val) { 
		
		Particular particular = saParticular.buscarPorEmail(val);
		
		ModelAndView modelAndView = new ModelAndView();
		
		TransferParticular tParticular = new TransferParticular(particular.getNombre(),particular.getApellidos(), particular.getDni(),
				particular.getEmail(),"0",true, particular.getPuntuacion());
		
		/*modelAndView.addObject("nombre", tParticular.getNombre());
		modelAndView.addObject("apellidos", tParticular.getApellidos());
		modelAndView.addObject("email", tParticular.getEmail());
		modelAndView.addObject("dni", tParticular.getDni());*/
		modelAndView.addObject("transferParticular", tParticular);
		modelAndView.setViewName("perfilParticular");
		
		return modelAndView;
    }
	
	@RequestMapping("/paginaMenuEmpresa")
	public String paginaMenuEmpresa(){
		return "paginaMenuEmpresa";
	}
	
	
	/**
	 * Método que añade al particular como variable permanente para el modelo
	 * @param model modelo al que se le inserta el particular
	 */
	//Esta anotación nos permite establecer variables permanentes para el modelo
	@ModelAttribute
	public void addAttributes(Model model) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		Particular particular = saParticular.buscarPorEmail(auth.getName());
		
		model.addAttribute("particular", particular); //En este caso el objeto usuario estará permanentemente en todas las vistas por el @ModelAttribute 
	}
	
}
