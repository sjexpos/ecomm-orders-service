/**********
 This project is free software; you can redistribute it and/or modify it under
 the terms of the GNU General Public License as published by the
 Free Software Foundation; either version 3.0 of the License, or (at your
 option) any later version. (See <https://www.gnu.org/licenses/gpl-3.0.html>.)

 This project is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 more details.

 You should have received a copy of the GNU General Public License
 along with this project; if not, write to the Free Software Foundation, Inc.,
 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 **********/
// Copyright (c) 2024-2025 Sergio Exposito.  All rights reserved.              

package io.oigres.ecomm.service.orders.config;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.config.ConfigPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Order(0)
@Slf4j
public class ApplicationReadyInitializer implements ApplicationListener<ApplicationReadyEvent> {

  @Autowired private Environment environment;

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    log.info("");
    log.info("********************************************************************");
    log.info(" ORDERS SERVICE STARTED");
    log.info("********************************************************************");
    log.info("Default Charset: {}", Charset.defaultCharset());
    log.info("File Encoding:   {}", System.getProperty("file.encoding"));
    log.info("Server time:     {}", ZonedDateTime.now());
    MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    log.info("----------------------- JVM memory ----------------------");
    log.info(
        "Max Heap size: {}",
        FileUtils.byteCountToDisplaySize(memoryBean.getHeapMemoryUsage().getMax()));
    log.info(
        "Initial Heap size: {}",
        FileUtils.byteCountToDisplaySize(memoryBean.getHeapMemoryUsage().getInit()));
    log.info(
        "Heap usage: {}",
        FileUtils.byteCountToDisplaySize(memoryBean.getHeapMemoryUsage().getUsed()));
    log.info("------------------------ Postgres -----------------------");
    log.info("Host:     {}", environment.getProperty("spring.datasource.host"));
    log.info("Port:     {}", environment.getProperty("spring.datasource.port"));
    log.info("Schema:   {}", environment.getProperty("spring.datasource.schemaName"));
    log.info("Username: {}", environment.getProperty("spring.datasource.username"));
    String password = environment.getProperty("spring.datasource.password", "");
    password =
        password.substring(0, 1)
            + StringUtils.repeat("*", password.length() - 2)
            + password.substring(password.length() - 1);
    log.info("Password: {}", password);
    log.info("-------------------- Elasticsearch ----------------------");
    log.info(
        "Type: {}", environment.getProperty("spring.jpa.properties.hibernate.search.backend.type"));
    log.info(
        "Hosts: {}",
        environment.getProperty("spring.jpa.properties.hibernate.search.backend.hosts"));
    log.info(
        "Protocol: {}",
        environment.getProperty("spring.jpa.properties.hibernate.search.backend.protocol"));
    log.info("------------------------ Redis --------------------------");
    log.info(
        "Redisson config: {}",
        environment.getProperty("spring.jpa.properties.hibernate.cache.redisson.config"));
    new ConfigPrinter()
        .print(
            log, environment.getProperty("spring.jpa.properties.hibernate.cache.redisson.config"));
    log.info("-------------------------- Tracing ----------------------");
    log.info("URL: {}", environment.getProperty("ecomm.service.tracing.url"));
    log.info("------------------------ Users service ----------------------");
    log.info("URL: {}", environment.getProperty("ecomm.service.users.baseUri"));
    log.info("----------------------- Products service ----------------------");
    log.info("URL: {}", environment.getProperty("ecomm.service.products.baseUri"));
    log.info("********************************************************************");
    log.info("");
  }
}
