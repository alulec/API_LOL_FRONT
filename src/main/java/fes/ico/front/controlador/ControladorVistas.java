package fes.ico.front.controlador;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import fes.ico.front.modelo.Personaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@RequestMapping("/campeon")
public class ControladorVistas {

    private final WebClient webClient;
    @Autowired
    public ControladorVistas(WebClient webClient){
        this.webClient = webClient;
    }

    // endpoints que me permiten navegar por la pagina
    @GetMapping("/index")
    public String getIndex(Model modelo) throws  JsonProcessingException{
        String uri = "http://localhost:8080/personajes/";
        Flux<Personaje> personajesFlux = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Personaje>() {
                });
        List<Personaje> result = personajesFlux.collectList().block();
        modelo.addAttribute("campeones", result);

        return "index";
    }
    @GetMapping("/borrar")
    public String getActualizar(Model modelo){
        return "deletePersonaje";
    }

    @GetMapping(value = "/crear")
    public String getCrear(Model modelo){
        modelo.addAttribute("nuevoCampeon", new Personaje());
        return "createPersonaje";
    }


    // endpoints que me permiten consumir la api backend
    @PostMapping("/delete")
    public String borrarPersonaje(@ModelAttribute("id") int id, Model modelo){
        String uri = "localhost:8080/personajes/delete/";

        Personaje personaje = new Personaje(id, "", "", "", "");
        Gson gson = new Gson();
        String personajeJSON = gson.toJson(personaje);

        System.out.println(personajeJSON);

        Mono<Personaje> resultadoMono = webClient.post()
                .uri("localhost:8080/personajes/delete/")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(personajeJSON)
                .retrieve()
                .bodyToMono(Personaje.class);
        Personaje per = resultadoMono.block();

        modelo.addAttribute("campeon", per);

        return "readPersonaje";
    }

    @PostMapping("/create")
    public String createCrear(@ModelAttribute Personaje personaje, Model modelo){
        String uri = "http://localhost:8080/personajes/save";
        Gson gson = new Gson();
        String personajeJSON = gson.toJson(personaje);
        System.out.println(personajeJSON);

        Mono<Personaje> resultadoMono = webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(personajeJSON)
                .retrieve()
                .bodyToMono(Personaje.class);
        Personaje per = resultadoMono.block();

        modelo.addAttribute("campeon", per);

        return "readPersonaje";
    }

    @GetMapping("/{id}")
    public String getPersonaje(@PathVariable int id, Model modelo) throws JsonProcessingException {
        String uri = "http://localhost:8080/personajes/" + id;


        Mono<Personaje> personajeMono = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Personaje.class);
        Personaje per = personajeMono.block();

        modelo.addAttribute("campeon", per);
        return "readPersonaje";

    }
}
