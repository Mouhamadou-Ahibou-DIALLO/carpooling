package api.carpooling.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "user_refresh_tokens")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRefreshTokens {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
}
