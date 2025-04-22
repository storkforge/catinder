package org.example.springboot25.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class CachedUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private String userName;
    private String userEmail;
    private String userLocation;
    private String userRole;
}
