package com.app.spring.primefaces.servicio;

import com.app.spring.primefaces.modelo.Cliente;

import java.util.List;

public interface IClienteServicio {
    List<Cliente> listarClientes();
    Cliente buscarClientePorId(Integer idCliente);
    void guardarCliente(Cliente cliente);
    void eliminarCliente(Cliente cliente);
}
