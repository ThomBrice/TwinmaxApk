package com.example.isen.twinmaxapk.database.interfaces;

import com.example.isen.twinmaxapk.database.historic.Maintenance;
import com.example.isen.twinmaxapk.database.historic.MaintenanceWithList;
import com.example.isen.twinmaxapk.database.historic.Moto;

public interface MotoListener {

    void onViewMaintenance(Moto moto);
    void onViewMoto();
    void dismissPopupMoto();
    void onViewPopupDeleteMoto(Moto moto);
    void addMoto(Moto moto);
    void addMaintenanceView(Moto moto, MaintenanceWithList maintenance);
    void quitHistoricActivity();
}
