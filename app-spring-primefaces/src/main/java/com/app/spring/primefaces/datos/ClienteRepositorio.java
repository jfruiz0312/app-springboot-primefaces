package com.app.spring.primefaces.datos;

import com.app.spring.primefaces.modelo.Cliente;
import org.springframework.data.repository.CrudRepository;

public interface ClienteRepositorio extends CrudRepository<Cliente, Integer> {
}
