package com.app.spring.primefaces.services;

import com.app.spring.primefaces.entities.Categoria;
import java.util.List;
import java.util.Optional;


public interface ICategoriaServicio extends IGenericServicio<Categoria, Integer> {

    // Métodos específicos para Categoria
    Optional<Categoria> buscarPorNombre(String nombre);

    List<Categoria> buscarPorNombreConteniendo(String nombre);

    List<Categoria> buscarPorDescripcionConteniendo(String descripcion);

    List<Categoria> buscarPorNombreODescripcion(String texto);

    boolean existePorNombre(String nombre);

    // Mantener compatibilidad con nombres similares a Cliente
    Categoria buscarCategoriaPorId(Integer idCategoria);

    void guardarCategoria(Categoria categoria);

    void eliminarCategoria(Categoria categoria);

    List<Categoria> listarCategorias();
}
