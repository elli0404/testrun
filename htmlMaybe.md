## Mögliches HTML File

```html
<!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd"> 
<!--für Thymeleaf Code Completion, kann aber auch nur <!DOCTYPE html> sein -->
<!-- Ist von der Thymeleaf Seite geklaut btw xD -->
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org" lang="de">
<!-- selber Grund, kann auch nur <html lang="de"> sein -->
<head>
    <meta charset="UTF-8">
<!--  ^^ damit die äs und ös und üs vernünftig angezeigt werden -->
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
