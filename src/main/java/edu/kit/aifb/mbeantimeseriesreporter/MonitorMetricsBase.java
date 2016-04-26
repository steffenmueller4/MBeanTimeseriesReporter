/**
 * Copyright 2016 Steffen Mueller
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.kit.aifb.mbeantimeseriesreporter;

import java.util.HashMap;
import java.util.Map;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricSet;

/**
 * Abstract class which provides properties for derived metrics.
 * 
 * @author Steffen Mueller
 * @version 1.0.0
 */
public abstract class MonitorMetricsBase implements Queryable, MetricSet {

	/**
	 * The {@link com.codahale.metrics.MetricRegistry} which writes out the metrics.
	 */
	protected MetricRegistry metricsRegistry;
	/**
	 * A set containing all single metrics.
	 */
	protected final Map<String, Metric> metricSet = new HashMap<String, Metric>();

	/**
	 * Constructor.
	 * @param metricsRegistry The {@link com.codahale.metrics.MetricRegistry} which writes out the metrics.
	 */
	public MonitorMetricsBase(MetricRegistry metricsRegistry) {
		this.metricsRegistry = metricsRegistry;
	}

	/**
	 * Creates a unified name based on the parameters.
	 * @param hostname The hostname of the JMX MBean server.
	 * @param metricName The name of JMX MBean attribute used for the CSV-file. 
	 * @return
	 */
	protected String createMetricName(String hostname, String metricName) {
		return hostname + "_" + metricName;
	}

	@Override
	public Map<String, Metric> getMetrics() {
		return metricSet;
	}
}
