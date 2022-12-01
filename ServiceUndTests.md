# Mögliche Implementierung für Service


```java
@Service
public class KlaeffService {

    private KlaeffRepository repo;

    public KlaeffService(KlaeffRepository repo) {
        this.repo = repo;
    }

    public int getNumOfPages(int maxKlaeffsPerPage) {
        return Math.max(
                1, //da wir immer mindestens eine Seite übergeben wollten
                (int) Math.ceil((double) repo.getSize() / maxKlaeffsPerPage)
        );
    }

    public List<List<Klaeff>> getPages(int maxKlaeffsPerPage) {
        List<Klaeff> allSortedKlaeffs = repo.findAll();
        int numOfPages = getNumOfPages(maxKlaeffsPerPage);
        List<List<Klaeff>> allPages = new ArrayList<>();

        for (int page = 0; page < numOfPages; page++) {
            //Wir wollen immer mindestens eine Seite zurückgeben
            allPages.add(new ArrayList<>());
            for(int klaeffNr = 0; klaeffNr < maxKlaeffsPerPage; klaeffNr++) {
                int currentIndex = page*maxKlaeffsPerPage + klaeffNr;
                
                //wenn alle Kläffs verteilt wurden, sind wir hier fertig
                if(currentIndex >= allSortedKlaeffs.getSize()) break;
                
                Klaeff currentKlaeff = allSortedKlaeffs.get(currentIndex);
                allPages(page).add(currentKlaeff);
            }
        }
        
        return allPages;
    }
    
    
    // Falls ihr doch Timestamps in die Kläffs hinzufügen solltet, ist hier ne nützliche Möglichkeit die zu 
    // sortieren (kannst du sonst auch in der Repository Klasse vor dem zurückgeben machen
    public List<Klaeff> sortNewestFirst(List<Klaeff> klaeffs) {
        return klaeffs.stream()
                .sorted(Comparator.comparing(Klaeff::getTimestamp).reversed())
                .toList();
    }
}
```

___
# Mögliche Testbeispiele für Service

## getNumOfPages(int maxKlaeffsPerPage)
- Wenn im Repository 3 Kläffs gespeichert sind, und wir `getNumOfPages(1)` aufrufen, dann sollen wir als Rückgabe 3 
  bekommen zB
  - ```java
    @Test
    @DisplayName("Wenn Repo mit 3 Kläffs, und getNumOfPages(1) aufgerufen wird, wird 3 returned")
    void getNumOfPages1_withThreeElements_returns3() {
        KlaeffRepository repo = mock(KlaeffRepository.class);
        when(repo.getSize()).thenReturn(3);

        KlaeffService service = new KlaeffService(repo);

        int expected = 3;
        int actual = service.getNumOfPages(1);

        assertThat(actual).isEqualTo(expected);
    }
    ```
  
- Wenn das Repository leer ist, gibt `getNumOfPages()` 1 zurück
  - ```java
    @Test
    @DisplayName("getNumOfPages mit leerem Repo gibt 1 zurück")
    void getNumOfPages_withEmptyRepo_returns1() {
        KlaeffRepository repo = mock(KlaeffRepository.class);
        when(repo.getSize()).thenReturn(0);
        KlaeffService service = new KlaeffService(repo);

        int expected = 1;
        int actual = service.getNumOfPages(5); //parameter egal

        assertThat(actual).isEqualTo(expected);
    }
    ```
    
- Wenn `getNumOfPages(x)` mit x > repo.size aufgerufen wird, wird 1 zurückgegeben
  - ```java
    @Test
    @DisplayName("getNumOfPages mit maxKlaeffsPerPage > repo.size gibt 1 zurück")
    void getPageAmount_withMoreKlaeffsThanRepoSize_returns1() {
        KlaeffRepository repo = mock(KlaeffRepository.class);
        when(repo.getSize()).thenReturn(3);
        KlaeffService service = new KlaeffService(repo);

        int expected = 1;
        int actual = service.getNumOfPages(5);

        assertThat(actual).isEqualTo(expected);
    }
    ```
___
  
## getPages(int maxKlaeffsPerPage)
- Wenn das Repository leer ist, gibt `getPages(x)` eine Liste mit einer leeren Liste zurück
  - ```java
    @Test
    @DisplayName("getPaginatedPages mit leerem Repo gibt Liste mit einer leeren Liste zurück")
    void getPages_withEmptyRepo_returnsListOfEmptyList() {
        KlaeffRepository repo = mock(KlaeffRepository.class);
        when(repo.findAll()).thenReturn(new ArrayList<>());
        when(repo.getSize()).thenReturn(0);
        KlaeffService service = new KlaeffService(repo);

        List<List<Klaeff>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());

        List<List<Klaeff>> actual = service.getPages(3);

        assertThat(actual).isEqualTo(expected);
    }
    ```
    
- Wenn das Repository 2 Kläffs hat, gibt `getPages(1)` eine Liste mit 2 Listen zurück
  - ```java
    @Test
    @DisplayName("getPages(1) mit 2 Klaeffs in Repo gibt Liste mit 2 Listen zurück")
    void getPages1_with2KlaeffsInRepo_returnsListOf2() {
        KlaeffRepository repo = mock(KlaeffRepository.class);
        KlaeffService service = new KlaeffService(repo);
        when(repo.getSize()).thenReturn(2);
        when(repo.findAll()).thenReturn(new ArrayList<>(List.of(
                new Klaeff("", ""),
                new Klaeff("", "")
        )));

        List<List<Klaeff>> actual = service.getPages(1);

        assertThat(actual).hasSize(2);
    }
    ```
    
- Wenn das Repository 3 Kläffs hat, dann gibt `getPages(2)` eine Liste mit 2 Listen zurück, von denen die erste 2 
  Elemente und die zweite 1 Element hat
  - ```java
    @Test
    @DisplayName("getPages(2) mit 3 Klaeffs in Repo gibt 2 Listen mit 2 und 1 Element zurück")
    void getPages2_with3KlaeffsInRepo_returnsListOf2AndListOf1() {
        KlaeffRepository repo = mock(KlaeffRepository.class);
        KlaeffService service = new KlaeffService(repo);
        when(repo.getSize()).thenReturn(3);
        when(repo.findAll()).thenReturn(new ArrayList<>(List.of(
                new Klaeff("", ""),
                new Klaeff("", ""),
                new Klaeff("", "")
        )));
        
        List<List<Klaeff>> actual = service.getPages(2);
        List<Klaeff> page1 = actual.get(0);
        List<Klaeff> page2 = actual.get(1);
    
        assertThat(page1).hasSize(2);
        assertThat(page2).hasSize(1);
    }
    ```
