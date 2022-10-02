package com.example.authauthorization.entities;

import com.example.authauthorization.utils.RoleName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;


@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "Roles")
public class Role {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    private RoleName roleName;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "roles")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<User> users = new HashSet<>();
}
