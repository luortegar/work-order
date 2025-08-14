package cl.creando.skappserver.workorder.controller;


import cl.creando.skappserver.workorder.entity.TypeOfPurchase;
import cl.creando.skappserver.workorder.response.TypeOfPurchaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/private/v1/type-of-purchase")
public class TypeOfPurchaseController {

    @GetMapping("/autocomplete")
    public List<TypeOfPurchaseResponse> autocomplete(@RequestParam(name = "searchTerm", required = false, defaultValue = "") String searchTerm) {
        return Arrays.stream(TypeOfPurchase.values())
                .filter(type -> type.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .map(TypeOfPurchaseResponse::new)
                .collect(Collectors.toList());
    }
}
