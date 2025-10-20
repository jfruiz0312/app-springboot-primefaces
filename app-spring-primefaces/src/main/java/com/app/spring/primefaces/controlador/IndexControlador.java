package com.app.spring.primefaces.controlador;

import com.app.spring.primefaces.modelo.Cliente;
import com.app.spring.primefaces.servicio.IClienteServicio;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import java.io.Serializable;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.ArrayList;

@Component
@ViewScoped
public class IndexControlador implements Serializable{

    @Autowired
    IClienteServicio clienteServicio;
    private List<Cliente> clientes;
    private Cliente clienteSeleccionado;

    @PostConstruct
    public void iniciar(){
        cargarDatos();
    }

    public void cargarDatos(){
        // Crear una nueva ArrayList mutable
        List<Cliente> clientesServicio = this.clienteServicio.listarClientes();
        this.clientes = new ArrayList<>(clientesServicio);
        this.clientes.forEach(System.out::println);
    }

    public void agregarCliente(){
        this.clienteSeleccionado = new Cliente();
    }

    public void guardarCliente(){
        try {
            // Caso Agregar
            if(this.clienteSeleccionado.getId() == null){
                // CORREGIDO: El servicio retorna void, así que guardamos y luego recargamos
                this.clienteServicio.guardarCliente(this.clienteSeleccionado);
                // Recargar datos para obtener el cliente con ID generado
                cargarDatos();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, 
                        "Cliente Agregado", "Cliente agregado exitosamente"));
            }
            else{ // Caso Modificar
                this.clienteServicio.guardarCliente(this.clienteSeleccionado);
                // Recargar datos para asegurar consistencia
                cargarDatos();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Cliente Actualizado", "Cliente actualizado exitosamente"));
            }
            
            // Ocultar la ventana modal
            PrimeFaces.current().executeScript("PF('ventanaModalCliente').hide()");
            
            // Actualizar componentes
            PrimeFaces.current().ajax().update("forma-clientes:mensajes",
                    "forma-clientes:clientes-tabla");
            
            // Reset del objeto cliente seleccionado
            this.clienteSeleccionado = null;
            
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al guardar cliente", e.getMessage()));
            PrimeFaces.current().ajax().update("forma-clientes:mensajes");
        }
    }

    public void eliminarCliente(){
        try {
            this.clienteServicio.eliminarCliente(this.clienteSeleccionado);
            // Recargar datos desde la base de datos
            cargarDatos();
            // Reset del objeto de cliente seleccionado
            this.clienteSeleccionado = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Cliente Eliminado", "Cliente eliminado exitosamente"));
            PrimeFaces.current().ajax().update("forma-clientes:mensajes",
                    "forma-clientes:clientes-tabla");
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al eliminar cliente", e.getMessage()));
            PrimeFaces.current().ajax().update("forma-clientes:mensajes");
        }
    }

    public IClienteServicio getClienteServicio() {
        return clienteServicio;
    }

    public void setClienteServicio(IClienteServicio clienteServicio) {
        this.clienteServicio = clienteServicio;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public Cliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    public void setClienteSeleccionado(Cliente clienteSeleccionado) {
        this.clienteSeleccionado = clienteSeleccionado;
    }
}