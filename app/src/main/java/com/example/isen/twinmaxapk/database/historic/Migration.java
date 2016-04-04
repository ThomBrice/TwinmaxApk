package com.example.isen.twinmaxapk.database.historic;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class Migration implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();

        if (oldVersion == 0) {
            RealmObjectSchema motoSchema = schema.get("Moto");

            motoSchema
                    .addField("name", String.class, FieldAttribute.REQUIRED);

        }
    }
}
