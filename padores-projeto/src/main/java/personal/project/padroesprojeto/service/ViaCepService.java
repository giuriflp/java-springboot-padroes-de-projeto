package personal.project.padroesprojeto.service;

import org.springframework.web.bind.annotation.GetMapping;
import personal.project.padroesprojeto.model.Endereco;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viacep", url = "https://viacep.com.br/ws")
public interface ViaCepService {

    @GetMapping("/{cep}/json/")
    Endereco consultaCep(@PathVariable("cep") String cep);
}
