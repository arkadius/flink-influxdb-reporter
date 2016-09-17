/*
 * Copyright 2015 the original author or authors.
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
package com.github.arkadius.flinkinflux

import java.util.concurrent.TimeUnit

import com.codahale.metrics.{MetricFilter, ScheduledReporter}
import metrics_influxdb.InfluxdbReporter
import metrics_influxdb.api.measurements.CategoriesMetricMeasurementTransformer
import metrics_influxdb.api.protocols.InfluxdbProtocols
import org.apache.flink.dropwizard.ScheduledDropwizardReporter
import org.apache.flink.metrics.MetricConfig

/**
  * InfluxDB Metrics Reporter for Apache Flink
  *
  * To use this add the following configuration to your flink-conf.yaml file and place the JAR containing
  * this class in your flink/lib directory:
  *
  * #==========================================
    # Metrics
    #==========================================
    metrics.reporters: influxdb
    metrics.reporter.influxdb.server: localhost
    metrics.reporter.influxdb.port: 8086
    metrics.reporter.influxdb.user: admin
    metrics.reporter.influxdb.password: admin
    metrics.reporter.influxdb.db: flink
    metrics.reporter.influxdb.class: com.jgrier.flinkstuff.metrics.InfluxDbReporter
    metrics.reporter.influxdb.interval: 10 SECONDS

    # metrics format: host.process_type.tm_id.job_name.task_name.subtask_index

    metrics.scope.jm: <host>.jobmanager.na.na.na.na
    metrics.scope.jm.job: <host>.jobmanager.na.<job_name>.na.na
    metrics.scope.tm: <host>.taskmanager.<tm_id>.na.na.na
    metrics.scope.tm.job: <host>.taskmanager.<tm_id>.<job_name>.na.na
    metrics.scope.tm.task: <host>.taskmanager.<tm_id>.<job_name>.<task_name>.<subtask_index>
    metrics.scope.tm.operator: <host>.taskmanager.<tm_id>.<job_name>.<task_name>.<subtask_index>  */
class InfluxDbReporter extends ScheduledDropwizardReporter {
  override def getReporter(metricConfig: MetricConfig): ScheduledReporter = {

    val server = metricConfig.getString("server", "localhost")
    val port = metricConfig.getInteger("port", 8086)
    val user = metricConfig.getString("user", "admin")
    val password = metricConfig.getString("password", "admin")
    val db = metricConfig.getString("db", "flink")

    InfluxdbReporter.forRegistry(registry)
      .protocol(InfluxdbProtocols.http(server, port, user, password, db))
      .convertRatesTo(TimeUnit.SECONDS)
      .convertDurationsTo(TimeUnit.MILLISECONDS)
      .filter(MetricFilter.ALL)
      .skipIdleMetrics(false)
      .transformer(new CategoriesMetricMeasurementTransformer("host", "process_type", "tm_id", "job_name", "task_name", "subtask_index"))
      .build()
  }
}