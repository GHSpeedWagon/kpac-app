package speed.wagon.kpacapp.model;

import lombok.Data;
import java.util.List;

@Data
public class KpacSet {
    private Long id;
    private String title;
    private List<Kpac> kpacList;
}
