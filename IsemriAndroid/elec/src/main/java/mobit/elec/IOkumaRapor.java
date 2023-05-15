package mobit.elec;

import java.util.List;

/**
 * Created by Genel on 5.12.2018.
 */

public interface IOkumaRapor {

    List<IIsemri> getOkunanlar();
    List<IIsemri> getOkunmayanlar();
    List<IIsemri> getKuyruktaOlanlar();
    List<IIsemri> getUyariliOkunanlar();
    List<IIsemri> getHataliOkunanlar();

}
