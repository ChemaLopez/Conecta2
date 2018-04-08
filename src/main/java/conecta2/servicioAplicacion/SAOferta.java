package conecta2.servicioAplicacion;

import java.util.List;

import conecta2.modelo.Empresa;
import conecta2.modelo.Oferta;
import conecta2.transfer.TransferOferta;

public interface SAOferta {
		public void crearOferta(TransferOferta transferOferta);
		public List<Oferta> buscarTodas();
		public Oferta buscarPorId (int id);
		/*
		public Oferta buscarPorNombreAndJornadaAndContratoAndEmpresa(String nombre, JornadaLaboral jornada, Contrato contrato,
				Empresa empresa);
				*/
		public List<Oferta> buscarOfertasPorEmpresa(Empresa empresa);
		public Oferta save(TransferOferta transferEmpresa);
		public Oferta buscarPorIdYEmpresa(int id, Empresa empresa);
		public List<Oferta> buscarOfertasPorNombreYPatron(String nombrePattern);
		public Oferta actualizarOferta(Oferta oferta);
		
}
