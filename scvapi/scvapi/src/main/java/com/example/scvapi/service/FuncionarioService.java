package com.example.scvapi.service;

import com.example.scvapi.exception.RegraNegocioException;
import com.example.scvapi.model.entity.Funcionario;
import com.example.scvapi.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FuncionarioService
{
    private FuncionarioRepository repository ;
    public FuncionarioService(FuncionarioRepository repository)
    {
        this.repository = repository;
    }
    
    public List<Funcionario> getFuncionario()
    {
        return repository.findAll();
    }

    public Optional<Funcionario> getFuncionarioById(Long id)
    {
        return repository.findById(id);
    }

    @Transactional
    public Funcionario salvar(Funcionario funcionario)
    {
        validar(funcionario);
        return repository.save(funcionario);
    }

    @Transactional
    public void excluir(Funcionario funcionario)
    {
        Objects.requireNonNull(funcionario.getId());
        repository.delete(funcionario);
    }

    private void validar(Funcionario funcionario)
    {
        if (funcionario.getNome() == null || funcionario.getNome().trim().equals(""))
        {
            throw new RegraNegocioException("Nome inválido");
        }
        if (funcionario.getEmail() == null || funcionario.getEmail().trim().equals(""))
        {
            throw new RegraNegocioException("E-mail inválido");
        }
        if (funcionario.getCpf() == null || funcionario.getCpf().trim().equals(""))
        {
            throw new RegraNegocioException("CPF inválido");
        }
    }
}
