package personal.project.padroesprojeto.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import personal.project.padroesprojeto.model.Cliente;
import personal.project.padroesprojeto.model.ClienteRepository;
import personal.project.padroesprojeto.model.Endereco;
import personal.project.padroesprojeto.model.EnderecoRepository;
import personal.project.padroesprojeto.service.ClienteService;
import org.springframework.stereotype.Service;
import personal.project.padroesprojeto.service.ViaCepService;

import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;

    @Override
    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }

    @Override
    public void inserir(Cliente cliente) {
        salvarClienteComCep(cliente);
    }

    private void salvarClienteComCep(Cliente cliente) {
        //Verificar se o endereço do cliente já existe pelo CEP.
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
        //caso não exista, integrar com o ViaCEP e persistir o retorno.
            Endereco novoEndereco = viaCepService.consultaCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        cliente.setEndereco(endereco);
        //Inserir Cliente vinculando o Endereco (novo ou existente)
        clienteRepository.save(cliente);
    }

    @Override
    public void atualizar(Long id, Cliente cliente) {
        //Buscar cliente por ID, caso exista
        Optional<Cliente> clienteBd = clienteRepository.findById(id);
        if (clienteBd.isPresent()) {
            //Verificar se o Endereco do Cliente já existe (pelo CEP)
            salvarClienteComCep(cliente);
        }
    }

    @Override
    public void deletar(Long id) {
        //Deletar Cliente por ID
        clienteRepository.deleteById(id);
    }
}
