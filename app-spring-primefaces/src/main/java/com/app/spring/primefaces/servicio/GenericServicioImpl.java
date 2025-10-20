package com.app.spring.primefaces.servicio;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author ruiz_
 */
public abstract class GenericServicioImpl<T, ID> implements IGenericServicio<T, ID> {

    protected abstract CrudRepository<T, ID> getRepository();

    @Override
    @Transactional(readOnly = true)
    public List<T> listarTodos() {
        // Convertir Iterable a List
        return StreamSupport.stream(getRepository().findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<T> buscarPorId(ID id) {
        return getRepository().findById(id);
    }

    @Override
    @Transactional
    public T guardar(T entidad) {
        return getRepository().save(entidad);
    }

    @Override
    @Transactional
    public void eliminarPorId(ID id) {
        getRepository().deleteById(id);
    }

    @Override
    @Transactional
    public void eliminar(T entidad) {
        getRepository().delete(entidad);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorId(ID id) {
        return getRepository().existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long contar() {
        return getRepository().count();
    }

    @Override
    @Transactional
    public Iterable<T> guardarTodos(Iterable<T> entidades) {
        return getRepository().saveAll(entidades);
    }

    @Override
    @Transactional
    public void eliminarTodos() {
        getRepository().deleteAll();
    }

    @Override
    @Transactional
    public void eliminarTodos(Iterable<T> entidades) {
        getRepository().deleteAll(entidades);
    }

}
