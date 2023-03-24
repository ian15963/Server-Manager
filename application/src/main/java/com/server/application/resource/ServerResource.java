package com.server.application.resource;

import com.server.application.dto.ServerDTO;
import com.server.application.model.Server;
import com.server.application.service.implementation.ServerServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerResource {

    private final ServerServiceImpl serverService;

    @GetMapping("/list")
    public ResponseEntity<Page<ServerDTO>> getServers(Pageable pageable) {

        Page<ServerDTO> servers = serverService.getAllServers(pageable);
        return ResponseEntity.ok().body(servers);
    }

    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<Server> pingServer(@PathVariable("ipAddress") String ipAddress) throws IOException {
        Server server = serverService.ping(ipAddress);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/save")
    public ResponseEntity<ServerDTO> saveServer(@RequestBody @Valid ServerDTO dto) {
        dto = serverService.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ServerDTO> getServer(@PathVariable("id") Long id) {
            ServerDTO dto = serverService.getOne(id);
            return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteServer(@PathVariable("id") Long id) {
        serverService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
