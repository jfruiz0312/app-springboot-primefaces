package com.app.spring.primefaces.services;

import com.app.spring.primefaces.repositoy.ICategoriaRepository;
import com.app.spring.primefaces.entities.Categoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author ruiz_
 */
@Service
@Transactional
public class CategoriaServicioImpl extends GenericServicioImpl<Categoria, Integer> implements ICategoriaServicio {
  @Autowired
    private ICategoriaRepository categoriaRepository;

    @Override
    protected CrudRepository<Categoria, Integer> getRepository() {
        return categoriaRepository;
    }

    // Sobrescribir listarTodos para usar ordenamiento
    @Override
    @Transactional(readOnly = true)
    public List<Categoria> listarTodos() {
        return categoriaRepository.findAllByOrderByNombreAsc();
    }

    @Override
    public Optional<Categoria> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    @Override
    public List<Categoria> buscarPorNombreConteniendo(String nombre) {
        return categoriaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Categoria> buscarPorDescripcionConteniendo(String descripcion) {
        return categoriaRepository.findByDescripcionContainingIgnoreCase(descripcion);
    }

    @Override
    public List<Categoria> buscarPorNombreODescripcion(String texto) {
        return categoriaRepository.buscarPorNombreODescripcion(texto);
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return categoriaRepository.existsByNombre(nombre);
    }

    // Métodos de compatibilidad
    @Override
    public Categoria buscarCategoriaPorId(Integer idCategoria) {
        return buscarPorId(idCategoria).orElse(null);
    }

    @Override
    public void guardarCategoria(Categoria categoria) {
        guardar(categoria);
    }

    @Override
    public void eliminarCategoria(Categoria categoria) {
        eliminar(categoria);
    }

    @Override
    public List<Categoria> listarCategorias() {
        return listarTodos();
    }
}