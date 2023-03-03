package speed.wagon.kpacapp.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import speed.wagon.kpacapp.dao.KpacSetDao;
import speed.wagon.kpacapp.model.KpacSet;
import speed.wagon.kpacapp.service.KpacSetService;

@Service
public class KpacSetServiceImpl implements KpacSetService {
    private final KpacSetDao kpacSetDao;

    public KpacSetServiceImpl(KpacSetDao kpacSetDao) {
        this.kpacSetDao = kpacSetDao;
    }

    @Override
    public KpacSet save(KpacSet kpacSet) {
        return kpacSetDao.add(kpacSet);
    }

    @Override
    public void delete(Long id) {
        kpacSetDao.delete(id);
    }

    @Override
    public KpacSet get(Long id) {
        return kpacSetDao.get(id);
    }

    @Override
    public List<KpacSet> getAll() {
        return kpacSetDao.getAll();
    }
}
