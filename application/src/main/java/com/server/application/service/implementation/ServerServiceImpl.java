package com.server.application.service.implementation;


import com.server.application.dto.ServerDTO;
import com.server.application.model.Server;
import com.server.application.repo.ServerRepo;
import com.server.application.service.ServerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Optional;

import static com.server.application.enumeration.Status.SERVER_DOWN;
import static com.server.application.enumeration.Status.SERVER_UP;


@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServerServiceImpl implements ServerService {
    private final ServerRepo serverRepo;

    @Override
    public ServerDTO create(ServerDTO serverDTO) {
        log.info("Saving new server: {}", serverDTO.getName());
        Server server = new Server();
        copyDtoToEntity(serverDTO, server);
        server.setImageUrl("https://cdn-icons-png.flaticon.com/512/1202/1202760.png?w=740&t=st=1679592213~exp=1679592813~hmac=8f8b26d9157fa742070fe5353cc9693b0ad53ad8624edb63986c85b52a8280e2");
        server = serverRepo.save(server);
        return new ServerDTO(server);
    }

    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("Pinging server IP: {}", ipAddress);
        Server server = serverRepo.findByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(10000) ? SERVER_UP : SERVER_DOWN);
        return serverRepo.save(server);
    }

    @Override
    public Page<ServerDTO> getAllServers(Pageable pageable) {
        log.info("Fetching all servers");
        Page<Server> list = serverRepo.findAll(pageable);
        return list.map(x -> new ServerDTO(x));
    }

    @Override
    public ServerDTO getOne(Long id) {
        log.info("Fetching server by id: {}", id);
        Optional<Server> server = serverRepo.findById(id);
        return new ServerDTO(server.get());
    }

    @Override
    public ServerDTO update(Long id, ServerDTO dto) {
        log.info("Updating server: {}", dto.getName());
        Server server = serverRepo.getReferenceById(id);
        copyDtoToEntity(dto, server);
        server = serverRepo.save(server);
        return new ServerDTO(server);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting server by ID: {}", id);
        serverRepo.deleteById(id);
    }


    public void copyDtoToEntity(ServerDTO dto, Server entity){
        entity.setName(dto.getName());
        entity.setIpAddress(dto.getIpAddress());
        entity.setMemory(dto.getMemory());
        entity.setStatus(dto.getStatus());
        entity.setType(dto.getType());
    }
    private boolean isReachable(String ipAddress, int port, int timeOut) {
        try {
            try(Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(ipAddress, port), timeOut);
            }
            return true;
        }catch (IOException exception){
            return false;
        }
    }
}
