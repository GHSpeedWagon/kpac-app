package speed.wagon.kpacapp.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import speed.wagon.kpacapp.model.Kpac;
import speed.wagon.kpacapp.model.KpacSet;
import speed.wagon.kpacapp.service.KpacSetService;

@Controller
@RequestMapping("/set")
public class KpacSetController {
    private final KpacSetService kpacSetService;

    public KpacSetController(KpacSetService kpacSetService) {
        this.kpacSetService = kpacSetService;
    }

    @GetMapping("/{id}")
    public String getKpacsBySetId(@PathVariable Long id, Model model) {
        KpacSet kpacSet = kpacSetService.get(id);
        List<Kpac> kpacList = kpacSet.getKpacList();
        model.addAttribute("kpacList", kpacList);
        return "kpac/kpacsBySetId";
    }
}
