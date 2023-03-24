package com.server.application.dto;

import com.server.application.enumeration.Status;
import com.server.application.model.Server;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ServerDTO implements Serializable {
    private final static long serialVersionUID = 1L;


    private Long id;
    private String ipAddress;
    private String name;
    private String memory;
    private String type;
    private String imageUrl;
    private Status status;

    public ServerDTO(Server server){
        this.id = server.getId();
        this.ipAddress = server.getIpAddress();
        this.name = server.getName();
        this.memory = server.getMemory();
        this.type = server.getType();
        this.imageUrl = server.getImageUrl();
        this.status = server.getStatus();
    }

}
