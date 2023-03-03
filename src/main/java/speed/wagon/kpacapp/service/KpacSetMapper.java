package speed.wagon.kpacapp.service;

import org.springframework.stereotype.Component;
import speed.wagon.kpacapp.dto.KpacSetDto;
import speed.wagon.kpacapp.model.Kpac;
import speed.wagon.kpacapp.model.KpacSet;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class KpacSetMapper {
    private final KpacService kpacService;

    public KpacSetMapper(KpacService kpacService) {
        this.kpacService = kpacService;
    }

    public KpacSet toModel(KpacSetDto kpacSetDto) {
        KpacSet kpacSet = new KpacSet();
        kpacSet.setTitle(kpacSetDto.getTitle());
        String[] split = kpacSetDto.getKpacsIds().split(",");
        List<Kpac> kpacList = Arrays.stream(split)
                .map(i -> kpacService.get(Long.parseLong(i)))
                .collect(Collectors.toList());
        kpacSet.setKpacList(kpacList);
        return kpacSet;
    }
}
