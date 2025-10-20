package com.app.spring.primefaces.datos;

import com.app.spring.primefaces.modelo.Categoria;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ruiz_
 */
@Repository
public interface ICategoriaRepository extends CrudRepository<Categoria, Integer> {
     
    // Buscar por nombre (exacto)
    Optional<Categoria> findByNombre(String nombre);
    
    // Buscar por nombre que contenga (ignorando mayúsculas/minúsculas)
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar por descripción que contenga
    List<Categoria> findByDescripcionContainingIgnoreCase(String descripcion);
    
    // Buscar categorías cuyo nombre o descripción contengan el texto
    // Nota: Con CrudRepository necesitamos @Query para consultas personalizadas
    @Query("SELECT c FROM Categoria c WHERE " +
           "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Categoria> buscarPorNombreODescripcion(@Param("texto") String texto);
    
    // Verificar si existe una categoría con el mismo nombre
    boolean existsByNombre(String nombre);
    
    // Método para obtener todas las categorías ordenadas por nombre
    List<Categoria> findAllByOrderByNombreAsc();

}
