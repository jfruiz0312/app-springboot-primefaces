
package com.app.spring.primefaces.controlador;
import com.app.spring.primefaces.modelo.Categoria;
import com.app.spring.primefaces.servicio.ICategoriaServicio;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
@ViewScoped
public class CategoriaControlador implements Serializable {

    @Autowired
    private ICategoriaServicio categoriaServicio;
    
    private List<Categoria> categorias;
    private Categoria categoriaSeleccionada;

    @PostConstruct
    public void iniciar() {
        cargarDatos();
    }

    public void cargarDatos() {
        this.categorias = this.categoriaServicio.listarCategorias();
        this.categorias.forEach(System.out::println);
    }

    public void agregarCategoria() {
        this.categoriaSeleccionada = new Categoria();
    }

    public void guardarCategoria() {
        try {
            // Validar que el nombre no esté vacío
            if (categoriaSeleccionada.getNombre() == null || categoriaSeleccionada.getNombre().trim().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error", "El nombre de la categoría es requerido"));
                PrimeFaces.current().ajax().update("forma-categorias:mensajes");
                return;
            }

            // Caso Agregar
            if (this.categoriaSeleccionada.getId() == null) {
                // Verificar si ya existe una categoría con el mismo nombre
                if (categoriaServicio.existePorNombre(categoriaSeleccionada.getNombre())) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Error", "Ya existe una categoría con ese nombre"));
                    PrimeFaces.current().ajax().update("forma-categorias:mensajes");
                    return;
                }
                
                this.categoriaServicio.guardarCategoria(this.categoriaSeleccionada);
                this.categorias.add(this.categoriaSeleccionada);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Categoría Agregada", "Categoría agregada exitosamente"));
            } else { // Caso Modificar
                this.categoriaServicio.guardarCategoria(this.categoriaSeleccionada);
                cargarDatos(); // Recargar para reflejar cambios
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Categoría Actualizada", "Categoría actualizada exitosamente"));
            }

            // Ocultar la ventana modal
            PrimeFaces.current().executeScript("PF('ventanaModalCategoria').hide()");
            
            // Actualizar componentes
            PrimeFaces.current().ajax().update("forma-categorias:mensajes", "forma-categorias:categorias-tabla");
            
            // Reset del objeto categoria seleccionada
            this.categoriaSeleccionada = null;

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al guardar categoría", e.getMessage()));
            PrimeFaces.current().ajax().update("forma-categorias:mensajes");
        }
    }

    public void eliminarCategoria() {
        try {
            this.categoriaServicio.eliminarCategoria(this.categoriaSeleccionada);
            cargarDatos(); // Recargar datos
            this.categoriaSeleccionada = null;
            
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Categoría Eliminada", "Categoría eliminada exitosamente"));
            PrimeFaces.current().ajax().update("forma-categorias:mensajes", "forma-categorias:categorias-tabla");
                    
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al eliminar categoría", e.getMessage()));
            PrimeFaces.current().ajax().update("forma-categorias:mensajes");
        }
    }

    // Getters y Setters
    public ICategoriaServicio getCategoriaServicio() {
        return categoriaServicio;
    }

    public void setCategoriaServicio(ICategoriaServicio categoriaServicio) {
        this.categoriaServicio = categoriaServicio;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public Categoria getCategoriaSeleccionada() {
        return categoriaSeleccionada;
    }

    public void setCategoriaSeleccionada(Categoria categoriaSeleccionada) {
        this.categoriaSeleccionada = categoriaSeleccionada;
    }
}