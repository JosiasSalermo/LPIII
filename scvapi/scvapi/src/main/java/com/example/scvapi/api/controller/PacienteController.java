package com.example.scvapi.api.controller;

import com.example.scvapi.api.dto.PacienteDTO;
import com.example.scvapi.exception.RegraNegocioException;
import com.example.scvapi.model.entity.Paciente;
import com.example.scvapi.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pacientes")
@RequiredArgsConstructor
@CrossOrigin
public class PacienteController {
    private final PacienteService service;

    @GetMapping()
    public ResponseEntity get(){
        List<Paciente> pacientes = service.getPaciente();
        return ResponseEntity.ok(pacientes.stream().map(PacienteDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Paciente> paciente = service.getPacienteById(id);
        if (!paciente.isPresent()) {
            return new ResponseEntity("Paciente não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(paciente.map(PacienteDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody PacienteDTO dto) {
        try {
            Paciente paciente = converter(dto);
            paciente = service.salvar(paciente);
            return new ResponseEntity(paciente, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody PacienteDTO dto) {
        if (!service.getPacienteById(id).isPresent()) {
            return new ResponseEntity("Paciente não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Paciente paciente = converter(dto);
            paciente.setId(id);
            service.salvar(paciente);
            return ResponseEntity.ok(paciente);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Paciente> paciente = service.getPacienteById(id);
        if (!paciente.isPresent()) {
            return new ResponseEntity("Paciente não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(paciente.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Paciente converter(PacienteDTO dto){
        ModelMapper modelMapper = new ModelMapper();
        Paciente paciente = modelMapper.map(dto, Paciente.class);
        return paciente;
    }
}
