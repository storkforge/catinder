package org.example.springboot25.dto;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import java.io.Serializable;

@RedisHash("CachedUser")
@Data
public class CachedUserDTO implements Serializable {
    private Long userId;
    private String userName;
    private String userEmail;
    private String userLocation;
    private String userRole;
    // only include fields you actually need in cache
}
