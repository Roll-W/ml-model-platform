/*
 * Copyright (C) 2022 RollW
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.rollw.machlearn.configuration;

import tech.rollw.machlearn.data.database.MachSystemDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import space.lingu.light.DatasourceConfig;
import space.lingu.light.Light;
import space.lingu.light.connect.simple.DisposableConnectionPool;
import space.lingu.light.log.LightSlf4jLogger;
import space.lingu.light.sql.SQLiteDialectProvider;

/**
 * @author RollW
 */
@Configuration
public class DatabaseConfiguration {

    @Bean
    public MachSystemDatabase machSystemDatabase() {
        return Light.databaseBuilder(MachSystemDatabase.class, SQLiteDialectProvider.class)
                .setLogger(LightSlf4jLogger.createLogger(MachSystemDatabase.class))
                .setConnectionPool(DisposableConnectionPool.class)
                .datasource(new DatasourceConfig(
                        "jdbc:sqlite:mach_system.db",
                        "org.sqlite.JDBC",
                        null, null))
                .build();
    }
}
