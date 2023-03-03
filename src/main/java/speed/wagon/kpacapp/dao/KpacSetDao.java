package speed.wagon.kpacapp.dao;

import java.util.List;
import speed.wagon.kpacapp.model.KpacSet;

public interface KpacSetDao {
    KpacSet add(KpacSet kpacSet);

    void delete(Long id);

    KpacSet get(Long id);

    List<KpacSet> getAll();
}
