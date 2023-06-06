package fes.ico.front.modelo;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Personaje {
    private int id;
    private String nombre;
    private String region;
    private String lore;
    private String urlImagen;

}
