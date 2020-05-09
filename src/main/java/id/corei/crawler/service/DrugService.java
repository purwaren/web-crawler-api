package id.corei.crawler.service;

import id.corei.crawler.alodokter.model.DrugItem;
import id.corei.crawler.model.Drug;
import id.corei.crawler.model.DrugExample;
import id.corei.crawler.model.mapper.DrugMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrugService {
    @Autowired
    DrugMapper drugMapper;

    public void save(Drug drug) {
        if (drug.getId() == null) {
            drugMapper.insert(drug);
        }
        else {
            drugMapper.updateByPrimaryKey(drug);
        }
    }

    public Drug getByPermalink(String permalink) {
        DrugExample ex = new DrugExample();
        ex.createCriteria().andPermalinkEqualTo(permalink);
        List<Drug> drugList = drugMapper.selectByExample(ex);
        return drugList.size() > 0 ? drugList.get(0) : null;
    }
}
