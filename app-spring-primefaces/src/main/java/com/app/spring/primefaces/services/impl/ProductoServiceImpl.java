package com.app.spring.primefaces.services;

import com.app.spring.primefaces.entities.Product;
import com.app.spring.primefaces.repositoy.IProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoServiceImpl  implements IGenericServicio<Product, Integer> {
    private final IProductoRepository productoRepository;

    public ProductoServiceImpl(IProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Product> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Product> buscarPorId(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Product guardar(Product entidad) {
        return null;
    }

    @Override
    public void eliminarPorId(Integer integer) {

    }

    @Override
    public void eliminar(Product entidad) {

    }

    @Override
    public boolean existePorId(Integer integer) {
        return false;
    }

    @Override
    public long contar() {
        return 0;
    }

    @Override
    public Iterable<Product> guardarTodos(Iterable<Product> entidades) {
        return null;
    }

    @Override
    public void eliminarTodos() {

    }

    @Override
    public void eliminarTodos(Iterable<Product> entidades) {

    }
}
