package com.app.spring.primefaces.controllers;

import com.app.spring.primefaces.entities.Categoria;
import com.app.spring.primefaces.services.ICategoriaServicio;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Named(value = "categoriaBean")
@Component
@ViewScoped
public class CategoriaControlador implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String MENSAJES_COMPONENT_ID = ":forma-categorias:mensajes";
    private static final String TABLA_COMPONENT_ID = ":forma-categorias:tablaCategorias";

    @Autowired
    private ICategoriaServicio categoriaServicio;

    private List<Categoria> categorias;
    private List<Categoria> categoriasFiltradas;
    private Categoria categoriaSeleccionada;
    private String filtro;

    @PostConstruct
    public void iniciar() {
        cargarDatos();
    }

    public void cargarDatos() {
        try {
            List<Categoria> categoriasServicio = this.categoriaServicio.listarCategorias();
            this.categorias = new ArrayList<>(categoriasServicio);
            this.categoriasFiltradas = new ArrayList<>(categoriasServicio);
        } catch (Exception e) {
            e.printStackTrace();
            this.categorias = new ArrayList<>();
            this.categoriasFiltradas = new ArrayList<>();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", "No se pudieron cargar las categorias"));
        }
    }

    public void nuevaCategoria() {
        this.categoriaSeleccionada = new Categoria();
        PrimeFaces.current().executeScript("PF('ventanaModalCategoria').show()");
    }

    public void actualizarLista() {
        cargarDatos();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Actualizado", "Lista de categorias actualizada"));
        PrimeFaces.current().ajax().update(TABLA_COMPONENT_ID, MENSAJES_COMPONENT_ID);
    }

    public void buscar() {
        if (this.categorias == null) {
            this.categorias = new ArrayList<>();
        }

        if (filtro != null && !filtro.trim().isEmpty()) {
            String filtroLower = filtro.toLowerCase(Locale.ROOT).trim();
            this.categoriasFiltradas = this.categorias.stream()
                    .filter(c ->
                            contiene(c.getNombre(), filtroLower) ||
                                    contiene(c.getDescripcion(), filtroLower))
                    .collect(Collectors.toList());
        } else {
            this.categoriasFiltradas = new ArrayList<>(this.categorias);
        }
        PrimeFaces.current().ajax().update(TABLA_COMPONENT_ID);
    }

    public void editarCategoria(Categoria categoria) {
        this.categoriaSeleccionada = copiarCategoria(categoria);
        PrimeFaces.current().executeScript("PF('ventanaModalCategoria').show()");
    }

    public void guardarCategoria() {
        try {
            if (categoriaSeleccionada.getNombre() == null ||
                    categoriaSeleccionada.getNombre().trim().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error", "El nombre de la categoria es requerido"));
                PrimeFaces.current().ajax().update(MENSAJES_COMPONENT_ID);
                return;
            }

            String nombreCategoria = categoriaSeleccionada.getNombre().trim();
            categoriaSeleccionada.setNombre(nombreCategoria);

            if (this.categoriaSeleccionada.getId() == null) {
                if (categoriaServicio.existePorNombre(nombreCategoria)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Error", "Ya existe una categoria con ese nombre"));
                    PrimeFaces.current().ajax().update(MENSAJES_COMPONENT_ID);
                    return;
                }

                this.categoriaServicio.guardarCategoria(this.categoriaSeleccionada);
                cargarDatos();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Categoria Agregada", "Categoria agregada exitosamente"));
            } else {
                Categoria categoriaActual = categoriaServicio.buscarCategoriaPorId(categoriaSeleccionada.getId());
                if (categoriaActual != null
                        && categoriaActual.getNombre() != null
                        && !categoriaActual.getNombre().equalsIgnoreCase(nombreCategoria)
                        && categoriaServicio.existePorNombre(nombreCategoria)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Error", "Ya existe una categoria con ese nombre"));
                    PrimeFaces.current().ajax().update(MENSAJES_COMPONENT_ID);
                    return;
                }

                this.categoriaServicio.guardarCategoria(this.categoriaSeleccionada);
                cargarDatos();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Categoria Actualizada", "Categoria actualizada exitosamente"));
            }

            PrimeFaces.current().executeScript("PF('ventanaModalCategoria').hide()");
            PrimeFaces.current().ajax().update(MENSAJES_COMPONENT_ID, TABLA_COMPONENT_ID);
            this.categoriaSeleccionada = null;

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al guardar categoria", e.getMessage()));
            PrimeFaces.current().ajax().update(MENSAJES_COMPONENT_ID);
        }
    }

    public void eliminarCategoria(Categoria categoria) {
        try {
            this.categoriaSeleccionada = categoria;
            this.categoriaServicio.eliminarCategoria(this.categoriaSeleccionada);
            cargarDatos();
            this.categoriaSeleccionada = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Categoria Eliminada", "Categoria eliminada exitosamente"));
            PrimeFaces.current().ajax().update(MENSAJES_COMPONENT_ID, TABLA_COMPONENT_ID);
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al eliminar categoria", e.getMessage()));
            PrimeFaces.current().ajax().update(MENSAJES_COMPONENT_ID);
        }
    }

    public void eliminarCategoria() {
        if (this.categoriaSeleccionada != null) {
            eliminarCategoria(this.categoriaSeleccionada);
        }
    }

    public String irClientes() {
        return "/views/clientes.xhtml?faces-redirect=true";
    }

    public ICategoriaServicio getCategoriaServicio() {
        return categoriaServicio;
    }

    public void setCategoriaServicio(ICategoriaServicio categoriaServicio) {
        this.categoriaServicio = categoriaServicio;
    }

    public List<Categoria> getCategorias() {
        if (categoriasFiltradas != null) {
            return categoriasFiltradas;
        }
        if (categorias != null) {
            return categorias;
        }
        return Collections.emptyList();
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public List<Categoria> getCategoriasFiltradas() {
        return categoriasFiltradas;
    }

    public void setCategoriasFiltradas(List<Categoria> categoriasFiltradas) {
        this.categoriasFiltradas = categoriasFiltradas;
    }

    public Categoria getCategoriaSeleccionada() {
        if (categoriaSeleccionada == null) {
            categoriaSeleccionada = new Categoria();
        }
        return categoriaSeleccionada;
    }

    public void setCategoriaSeleccionada(Categoria categoriaSeleccionada) {
        this.categoriaSeleccionada = categoriaSeleccionada;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    private boolean contiene(String valor, String filtroBusqueda) {
        return valor != null && valor.toLowerCase(Locale.ROOT).contains(filtroBusqueda);
    }

    private Categoria copiarCategoria(Categoria origen) {
        Categoria copia = new Categoria();
        copia.setId(origen.getId());
        copia.setNombre(origen.getNombre());
        copia.setDescripcion(origen.getDescripcion());
        return copia;
    }
}