package speed.wagon.kpacapp.service;

import speed.wagon.kpacapp.model.Kpac;
import java.util.List;

public interface KpacService {
    Kpac save(Kpac kpac);

    void delete(Long id);

    Kpac get(Long id);

    List<Kpac> getAll();
}
