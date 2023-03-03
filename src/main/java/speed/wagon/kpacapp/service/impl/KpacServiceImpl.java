package speed.wagon.kpacapp.service.impl;

import org.springframework.stereotype.Service;
import speed.wagon.kpacapp.dao.KpacDao;
import speed.wagon.kpacapp.model.Kpac;
import speed.wagon.kpacapp.service.KpacService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class KpacServiceImpl implements KpacService {
    private KpacDao kpacDao;

    public KpacServiceImpl(KpacDao kpacDao) {
        this.kpacDao = kpacDao;
    }

    @Override
    public Kpac save(Kpac kpac) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            kpac.setCreationDate(LocalDate.parse(kpac.getCreationDate(), dateFormat).toString());
        } catch (Exception e) {
            throw new RuntimeException("Date is not valid", e);
        }
        return kpacDao.save(kpac);
    }

    @Override
    public void delete(Long id) {
        kpacDao.delete(id);
    }

    @Override
    public Kpac get(Long id) {
        return kpacDao.get(id);
    }

    @Override
    public List<Kpac> getAll() {
        return kpacDao.getAll();
    }
}
