package speed.wagon.kpacapp.dao;

import speed.wagon.kpacapp.model.Kpac;
import java.util.List;

public interface KpacDao {
    Kpac save(Kpac kpac);

    void delete(Long id);

    Kpac get(Long id);

    List<Kpac> getAll();
}
