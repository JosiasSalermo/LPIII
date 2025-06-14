package com.example.scvapi.api.controller;

import com.example.scvapi.api.dto.CargoDTO;
import com.example.scvapi.model.entity.Cargo;
import com.example.scvapi.model.entity.Funcionario;
import com.example.scvapi.service.CargoService;
import com.example.scvapi.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cargos")
@RequiredArgsConstructor
@CrossOrigin
public class CargoController {
    private final CargoService service;
    private final FuncionarioService funcionarioService;

    @GetMapping()
    public ResponseEntity get(){
        List<Cargo> cargos = service.getCargo();
        return ResponseEntity.ok(cargos.stream().map(CargoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Cargo> cargo = service.getCargoById(id);
        if (!cargo.isPresent()) {
            return new ResponseEntity("Cargo não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(cargo.map(CargoDTO::create));
    }

    public Cargo converter(CargoDTO dto){
        ModelMapper modelMapper = new ModelMapper();
        Cargo cargo = modelMapper.map(dto, Cargo.class);
        if (dto.getFuncionarioId() != null) {
            Optional<Funcionario> funcionario = funcionarioService.getFuncionarioById(dto.getFuncionarioId());
            if(!funcionario.isPresent()) {
                cargo.setFuncionario(null);
        }else{
                cargo.setFuncionario(funcionario.get());
            }
        }

        return cargo;
    }
}
