package com.app.spring.primefaces.repositoy;

import com.app.spring.primefaces.entities.Cliente;
import org.springframework.data.repository.CrudRepository;

public interface IClienteRepositorio extends CrudRepository<Cliente, Integer> {
}
