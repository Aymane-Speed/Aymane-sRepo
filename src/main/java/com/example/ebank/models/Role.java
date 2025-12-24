package com.example.ebank.models;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "roles", uniqueConstraints = @UniqueConstraint(name = "uk_roles_name", columnNames = "name"))
public class Role {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 50)
  private String name;

  public Role() {}
  public Role(String name) { this.name = name; }

  public Long getId() { return id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Role r)) return false;
    return Objects.equals(name, r.name);
  }
  @Override public int hashCode() { return Objects.hash(name); }
}
