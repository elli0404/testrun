## Vorarbeit

```java
public record Klaeff(
        String username,
        String text
) {}
```

```java
@Repository
public class KlaeffRepository {

    private List<Klaeff> repo = new ArrayList<>();

    public void save(Klaeff klaeff);

    public List<Klaeff> findAll() {
        //Falls du es wie gestern sortiert zurückgeben willst
        List<Klaeff> repoClone = new ArrayList<>(repo);
        Collections.reverse(clone);
        return repoClone;
    }

    public int getSize();
}
```