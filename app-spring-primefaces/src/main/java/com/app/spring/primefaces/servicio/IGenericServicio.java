package com.app.spring.primefaces.servicio;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author ruiz_
 */
public interface IGenericServicio<T, ID> {

    // Operaciones CRUD básicas
    List<T> listarTodos();
    Optional<T> buscarPorId(ID id);
    T guardar(T entidad);
    
    // Métodos de eliminación
    void eliminarPorId(ID id);
    void eliminar(T entidad);
    
    // Operaciones adicionales
    boolean existePorId(ID id);
    long contar();
    Iterable<T> guardarTodos(Iterable<T> entidades);
    void eliminarTodos();
    void eliminarTodos(Iterable<T> entidades);
}
