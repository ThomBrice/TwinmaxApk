package com.example.isen.twinmaxapk.database.interfaces;

import com.example.isen.twinmaxapk.database.historic.Maintenance;
import com.example.isen.twinmaxapk.database.historic.Moto;
import com.github.mikephil.charting.data.LineData;

public interface MaintenanceListener {

    void onViewGraph(LineData lineData);
    void dismissPopupMaintenance();
    void onViewPopupDeleteMaintenance(Maintenance maintenance, Moto moto);
    void onViewMaintenanceForPopup(Moto moto);
    void addMaintenance(Moto moto, Maintenance maintenance);
    void quitHistoricActivityFromMaintenance();
}

