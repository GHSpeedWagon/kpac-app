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
import speed.wagon.kpacapp.dto.KpacSetDto;
import speed.wagon.kpacapp.model.Kpac;
import speed.wagon.kpacapp.model.KpacSet;
import speed.wagon.kpacapp.service.KpacService;
import speed.wagon.kpacapp.service.KpacSetMapper;
import speed.wagon.kpacapp.service.KpacSetService;

@Controller
@RequestMapping("/sets")
public class KpacSetsController {
    private final KpacSetService kpacSetService;
    private final KpacService kpacService;
    private final KpacSetMapper kpacSetMapper;

    public KpacSetsController(KpacSetService kpacSetService,
                              KpacService kpacService,
                              KpacSetMapper kpacSetMapper) {
        this.kpacSetService = kpacSetService;
        this.kpacService = kpacService;
        this.kpacSetMapper = kpacSetMapper;
    }

    @GetMapping
    public String getAllKpacSets(Model model) {
        List<KpacSet> allKpacSets = kpacSetService.getAll();
        model.addAttribute("kpacSetList", allKpacSets);
        List<Kpac> allKpacs = kpacService.getAll();
        model.addAttribute("kpacList", allKpacs);
        return "kpac_set/allKpacSets";
    }

    @PostMapping
    public String addNewKpacSet(@RequestBody KpacSetDto kpacSetDto) {
        KpacSet kpacSet = kpacSetMapper.toModel(kpacSetDto);
        kpacSetService.save(kpacSet);
        return "kpac_set/allKpacSets";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("id") Long id) {
        kpacSetService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
