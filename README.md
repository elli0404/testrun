# Zusammenfassung

Ich benenne btw die Wuffs mal in Kläffs um, damit es zur Domain der Übung passt :D

Auf der README sind alle Sachen auf einer Seite. Sonst hast du hier Links zu den einzelnen Abschnitten
- [mögliche Vorarbeit, also KläffRepository und Kläff](/KlaeffUndKlaeffRepo.md)
- [möglicher KläffService mit möglichen Tests](/ServiceUndTests.md)
- [möglicher Controller mit möglichen Tests](/ControllerUndTests.md)
- [mögliches HTML File](/htmlMaybe.md)


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


# mögliche Testbeispiele für Service
___
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
    
___ 

# Hauptteil (Controller)
___
## Mögliche Implementierung 

```java
@Controller
public class KlaeffController {
    @Autowired //bei dem Autowired bin ich mir unsicher
    private KlaeffService service;
    
    public KlaeffController(KlaeffService service) {
        this.service = service;
    }
    
    
    @GetMapping("/")
    public String index() { return "redirect:/page/1"; }
    
    @PostMapping("/page/{nummer}")
    public String showPage(@PathVariable int nummer, Model model, @ModelAttribute("form") Form form) {
        int maxKlaeffsPerPage = 5;
        if(nummer > service.getNumOfPages(maxKlaeffsPerPage))... //irgendeinen Fehler oder Defaultverhalten oder so
        
        List<List<Klaeff>> allPages = service.getPages(maxKlaeffsPerPage);
        model.addAttribute("form", new Form(form.username(), ""));
        model.addAttribute("currentPage", allPages.get(nummer - 1));
        model.addAttribute("numOfPages", service.getNumOfPages(maxKlaeffsPerPage));
        
        return "index";
    }
    
    
    @PostMapping("/") //oder ("/sendKlaeff") oder so
    public String sendKlaeff(Form form, RedirectAttributes redirect) {
        service.addKlaeff(new Klaeff(form.username(), form.text()));
        
        redirectAttributes("form", new Form(form.username(), form.text()));
        
        return "redirect:/page/1";
    }
}
```

___

## Mögliche ControllerTests
```java
@WebMvcTest
class KlaeffControllerTest {
    @Autowired
    MockMvc mvc;
    
    @MockBean
    private KlaeffService service;
    
            .
            .
            .
}
```

- Ein `GET` Request auf `/` gibt den `Status 302` (passiert iwie automatisch beim Redirect) mit der `redirectUrl 
  /page/1` 
  - ```java
    @Test
    @DisplayName("GET / redirects to /page/1 with status 302")
    void getHome_redirectsToPage1_with302() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/page/1"));
    }
    ```
    
- Ein `POST` auf `/` (oder /sendKlaeff oder wo auch immer ihr es macht) mit `username=Peter` und `text=hallo` hat das 
  flashAttribute `"form"` mit `new Form("Peter", "")`
  - ```java
    @Test
    @DisplayName("POST / with username=Peter, text=Hallo has flashAttribute form with username only")
    void postHome_withUsernameAndText_hasAttributeUsername throws Exception {
        mvc.perform(post("/")
            .param("username", "Peter")
            .param("text", "hallo"))
        .andExpect(status().is(302))
        .andExpect(flash().attribute("username", "Peter"));
    }
    ```
    
- Ein `GET` auf `/page/{nummer}` mit `nummer <= numOfPages` ist `OK` mit der View `index` (oder wie ihr es nennt) 
    - ```java
      @Test
      @DisplayName("GET /page/{nummer} with nummer < numOfPages is OK with index view")
      void postPageNum_withNumLessThanNumOfPages_isOKWithIndexView throws Exception {
          when(service.getNumOfPages(anyInt())).thenReturn(5);
          mvc.perform(get("/page/1"))
              .andExpect(status().isOk())
              .andExpect(view().name("index"));
      }
      ```
      
- Und was auch immer wir vorhin gemacht haben, hab es nicht mehr ganz auf dem Schirm xD

___

## Mögliches HTML File

```html
<!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd">
<!--für Thymeleaf Code Completion, kann aber auch nur <!DOCTYPE html> sein -->
<!-- Ist von der Thymeleaf Seite geklaut btw xD -->
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org" lang="de">
<!-- selber Grund, kann auch nur <html lang="de"> sein -->
<head>
  <title>Kläffer</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
        crossorigin="anonymous">
  <!-- Bootstrap CSS -->
</head>
<body class="container"> <!-- Nimmt als Container nicht das ganze Fenster ein bei Vollbild -->

                         <!-- Du kannst die header, main und footer tags auch durch zB <section> oder <div> ersetzen -->
<header>
  <h2>Vergnüge die Welt mit deinem Kläff!</h2>
  <form method="post" th:object="${form}" th:action="@{/}">
    <!--         Username Input -->
    <div class="form-floating"> <!-- für das Label im Input -->
      <input type="text"
             name="username"
             id="username"
             th:field="*{username}"
             class="form-control" > <!-- wie oben -->
      <label for="username"
             class="form-label">Username</label> <!-- wie oben -->
    </div>


    <!--         Text Input -->
    <div class="form-floating input-group">
      <!-- input-group für den Button direkt an der textarea dran -->
      <!-- die textarea ist bisschen weird, idk, vllt hat ja jemand Tipps lol -->
      <input type="text"
             class="form-control"
             th:field="*{text}"
             style="height: 100px"> <!-- zum Anpassen der Höhe der Textarea -->
      <label for="message">Kläffe etwas in die Welt hinaus</label>
      <button type="submit" class="input-group-text btn btn-primary">kläffen</button>
      <!-- input-group-text, damit der Button auch tatsächlich an der input area hängt -->
    </div>


    </div>
  </form>
</header>



<main class="pt-5"> <!-- padding-top 5, damit die Kläffs etwas Abstand zu dem Formular haben -->
  <h2>Schaue dir die aktuellen Kläffer an</h2>
  <div class="card my-3" th:each="klaeff : ${currentPage}">
    <!--card, damit die Kläffs einen Header und einen Body haben können und stylish aussehen xD -->
    <!-- my-3 ist margin-y, damit die Kläffs nicht aneinander kleben sondern etwas Abstand haben -->
    <div class="card-header">
      <!-- card-header damit der Username oben im Header stehen kann -->
      <span th:text="${klaeff.username}">elli0404</span>
    </div>


    <div class="card-body">
      <!-- card-body, damit die Nachricht im Body stehen kann -->
      <span th:text="${klaeff.message}">i need coffee</span>
    </div>
  </div>


</main>



<footer>
  <div class="d-flex justify-content-center" th:remove="all-but-first" >
    <!--   d-flex, damit die Links nebeneinander sind und justify-content-center, damit sie mittig platziert werden     -->
    <a href=""
       class="px-1"
       th:href="@{/page/{nr}(nr=${num})}"
       th:text="${num}"
       th:each="num : ${#numbers.sequence(1, pageAmount)}">1
    </a>
    <!--       px-1 damit die Links etwas Abstand voneinander haben -->
    <!--       das th:each ist quasi ein for(num = 1; num<=pageAmount; num++) -->

    <!-- vvvvvvvvvvvvvvvvvvv dummy Daten  vvvvvvvvvvvvvvvvvvv -->
    <a href="" class="px-1" th:href="@{/page/{nr}(nr=${num})}" th:text="${num}">2</a>
    <a href="" class="px-1" th:href="@{/page/{nr}(nr=${num})}" th:text="${num}">3</a>
    <a href="" class="px-1" th:href="@{/page/{nr}(nr=${num})}" th:text="${num}">4</a>
    <a href="" class="px-1" th:href="@{/page/{nr}(nr=${num})}" th:text="${num}">5</a>
  </div>
</footer>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4"
        crossorigin="anonymous"></script>
                         <!-- Bootstrap JavaScript, kann man eig auch weglassen wenn mans nicht nutzt -->
</body>
</html>
```