package com.niule.znxj.web.model.taskcontent;

import com.niule.znxj.web.model.Areainfo;
import com.niule.znxj.web.model.Equipmentinfo;

import java.util.List;

/**
 * Created by administor on 2017/4/21.
 */
public class TaskArea {
    private Areainfo area;
    private List<TaskEquipment> equipments;

    public Areainfo getArea() {
        return area;
    }

    public void setArea(Areainfo area) {
        this.area = area;
    }

    public List<TaskEquipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<TaskEquipment> equipments) {
        this.equipments = equipments;
    }
}
