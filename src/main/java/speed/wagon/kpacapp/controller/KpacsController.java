package speed.wagon.kpacapp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import speed.wagon.kpacapp.model.Kpac;
import speed.wagon.kpacapp.service.KpacService;

@Controller
@RequestMapping("/kpacs")
public class KpacsController {
    private final KpacService kpacService;

    public KpacsController(KpacService kpacService) {
        this.kpacService = kpacService;
    }

    @GetMapping
    public String getAllKpacs(Model model) {
        List<Kpac> allKpacs = kpacService.getAll();
        model.addAttribute("kpacList", allKpacs);
        return "kpac/allkpacs";
    }

    @PostMapping()
    public String addNewKpac(@RequestBody Kpac kpac) {
        kpacService.save(kpac);
        return "kpac/allkpacs";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("id") Long id) {
        kpacService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
