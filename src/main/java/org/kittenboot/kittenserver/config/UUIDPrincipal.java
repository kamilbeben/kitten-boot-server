package org.kittenboot.kittenserver.config;

import lombok.Getter;

import java.security.Principal;
import java.util.Objects;
import java.util.UUID;

@Getter
public class UUIDPrincipal implements Principal {

  private final String name = UUID.randomUUID().toString();

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UUIDPrincipal that = (UUIDPrincipal) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
