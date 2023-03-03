package speed.wagon.kpacapp.service;

import speed.wagon.kpacapp.model.KpacSet;
import java.util.List;

public interface KpacSetService {
    KpacSet save(KpacSet kpacSet);

    void delete(Long id);

    KpacSet get(Long id);

    List<KpacSet> getAll();
}
