package kittenserver.required;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.security.Principal;
import java.util.Objects;

@Getter
@Setter
@ToString(exclude = "room")
public abstract class AbstractPlayer<R extends AbstractRoom> {

  protected final Principal principal;
  protected String name;
  protected R room;

  public AbstractPlayer(Principal principal) {
    this.principal = principal;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AbstractPlayer that = (AbstractPlayer) o;
    return Objects.equals(principal, that.principal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(principal);
  }
}
