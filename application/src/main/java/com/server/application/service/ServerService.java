package com.server.application.service;

import com.server.application.dto.ServerDTO;
import com.server.application.model.Server;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface ServerService {
    ServerDTO create(ServerDTO server);
    Server ping(String ipAddress) throws IOException;
    Page<ServerDTO> getAllServers(Pageable pageable);
    ServerDTO getOne(Long id);
    ServerDTO update(Long id, ServerDTO server);
    void delete(Long id);
}
