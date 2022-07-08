package one.digitalInnovation.labpadroesprojetospring.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.digitalInnovation.labpadroesprojetospring.model.Cliente;
import one.digitalInnovation.labpadroesprojetospring.model.Endereco;
import one.digitalInnovation.labpadroesprojetospring.repository.ClienteRepository;
import one.digitalInnovation.labpadroesprojetospring.repository.EnderecoRepository;
import one.digitalInnovation.labpadroesprojetospring.service.ClienteService;
import one.digitalInnovation.labpadroesprojetospring.service.ViaCepService;

@Service
public class ClienteServiceImpl implements ClienteService {

	@Autowired
	private ClienteRepository repository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private ViaCepService viaCep;
	
	@Override
	public Iterable<Cliente> buscarTodos() {
		return repository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		Optional<Cliente> cliente = repository.findById(id);
		return cliente.get();
	}

	@Override
	public void inserir(Cliente cliente) {
		salvarClienteComCep(cliente);
	}

	@Override
	public void atualizar(Long id, Cliente cliente) {
		Optional<Cliente> clientebd = repository.findById(id);
		if(clientebd.isPresent()) {
			salvarClienteComCep(cliente);
		}
		
	}

	@Override
	public void deletar(Long id) {
		repository.deleteById(id);
		
	}
	
	private void salvarClienteComCep(Cliente cliente) {
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			Endereco novoEndereco = viaCep.consultarCep(cep);
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		cliente.setEndereco(endereco);
		repository.save(cliente);
	}

}
