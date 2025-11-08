package org.konex.sistemaloteria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SistemaLoteriaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaLoteriaApplication.class, args);

        System.out.println("=============================================");
        System.out.println("   API SistemaLoteria iniciada correctamente ");
        System.out.println("   Servidor disponible en: http://localhost:8080");
        System.out.println("   Endpoints principales: /api/sorteos, /api/billetes, /api/clientes, /api/ventas");
        System.out.println("   Fecha y hora de inicio: " + java.time.LocalDateTime.now());
        System.out.println("=============================================");
    }
}
