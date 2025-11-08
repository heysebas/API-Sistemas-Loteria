package org.konex.sistemaloteria.configuracion;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class StatusController {

    @GetMapping("/api/status")
    public Map<String, Object> status() {
        return Map.of(
                "status", "OK",
                "message", "API SistemaLoteria funcionando correctamente",
                "timestamp", java.time.LocalDateTime.now().toString()
        );
    }
}
