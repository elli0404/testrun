# Hauptteil (Controller)

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
