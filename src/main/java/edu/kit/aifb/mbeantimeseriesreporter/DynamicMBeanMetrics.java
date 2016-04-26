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

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

import edu.kit.aifb.mbeantimeseriesreporter.xml.Attribute;
import edu.kit.aifb.mbeantimeseriesreporter.xml.MBean;

/**
 * 
 * @author Steffen Mueller
 * @version 1.0.0
 */
public final class DynamicMBeanMetrics extends MonitorMetricsBase {

	private final MBeanServerConnection connection;
	private final List<MBeanMonitoring<?>> mbeanMonitorings = new ArrayList<MBeanMonitoring<?>>();
	private final MBean mbean;
	private final String hostname;

	public DynamicMBeanMetrics(MetricRegistry metricsRegistry, MBeanServerConnection connection, String hostname,
			MBean mbean) {
		super(metricsRegistry);
		this.connection = connection;
		this.mbean = mbean;
		this.hostname = hostname;

		configure();
	}

	private void configure() {
		for (Attribute attribute : mbean.getAttributes()) {
			try {
				if (attribute.getMetricName() == null || attribute.getMetricName().getValue() == null
						|| attribute.getMetricName().getValue().isEmpty())
					throw new IllegalArgumentException("MetricName must not be null or empty!");

				final String metricName = createMetricName(hostname, attribute.getName());

				MBeanMonitoring<?> monitoring = null;
				switch (attribute.getGaugeType().getValue()) {
				case "Double":
					monitoring = new MBeanMonitoring<Double>(new ObjectName(mbean.getObjectName()),
							attribute.getMetricName().getValue(), metricName);
					break;
				case "Long":
					monitoring = new MBeanMonitoring<Long>(new ObjectName(mbean.getObjectName()),
							attribute.getMetricName().getValue(), metricName);
					break;
				case "Integer":
					monitoring = new MBeanMonitoring<Integer>(new ObjectName(mbean.getObjectName()),
							attribute.getMetricName().getValue(), metricName);
					break;
				case "Boolean":
					monitoring = new MBeanMonitoring<Boolean>(new ObjectName(mbean.getObjectName()),
							attribute.getMetricName().getValue(), metricName);
					break;
				}
				mbeanMonitorings.add(monitoring);
				this.metricsRegistry.register(monitoring.metricName, monitoring.gauge);
				this.metricSet.put(monitoring.metricName, monitoring.gauge);

			} catch (MalformedObjectNameException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void query() throws MBeanTimeseriesReporterException {
		ArrayList<MBeanMonitoring<?>> removes = new ArrayList<MBeanMonitoring<?>>();
		for (MBeanMonitoring<?> mbeanMonitoring : mbeanMonitorings) {
			try {
				mbeanMonitoring
						.setValue(connection.getAttribute(mbeanMonitoring.mbeanName, mbeanMonitoring.attributeName));
			} catch (InstanceNotFoundException e) {
				System.err.println("Instance '" + mbeanMonitoring.mbeanName + "' attribute '"
						+ mbeanMonitoring.attributeName + "' not found! Entry will be removed.");
				e.printStackTrace();
				removes.add(mbeanMonitoring);
			} catch (IOException | ReflectionException | AttributeNotFoundException | MBeanException e) {
				System.err.println("Error while querying '" + mbeanMonitoring.mbeanName + "' attribute '"
						+ mbeanMonitoring.attributeName + "'.");
				e.printStackTrace();
			}
		}
		if (removes.size() > 0)
			for (MBeanMonitoring<?> mbeanMonitoring : removes) {
				System.out.println(
						"Removing monitoring for " + mbeanMonitoring.mbeanName + ", " + mbeanMonitoring.attributeName);
				mbeanMonitorings.remove(mbeanMonitoring);
			}
	}

	@Override
	public String toString() {
		return mbean.getObjectName() + "@" + hostname;
	}

	/**
	 * Internal class for managing {@link com.codahale.metrics.Gauge} instances.
	 * 
	 * @author Steffen Mueller
	 * @version 1.0.0
	 *
	 * @param <T>
	 *            The type of the {@link com.codahale.metrics.Gauge}. E.g.,
	 *            Double, String, Long, ...
	 */
	private class MBeanMonitoring<T> {

		/**
		 * The MBean ObjectName. See e.g.:
		 * http://www.oracle.com/technetwork/java/javase/tech/best-practices-jsp
		 * -136021.html#mozTocId654884.
		 */
		public final ObjectName mbeanName;
		/**
		 * The MBean Attribute Name.
		 */
		public final String attributeName;
		/**
		 * The name of metric. This name is also used for the .csv-filename.
		 */
		public final String metricName;
		public T value;
		public Gauge<T> gauge;

		/**
		 * Constructor.
		 * 
		 * @param mbeanName
		 *            The MBean ObjectName. See e.g.:
		 *            http://www.oracle.com/technetwork/java/javase/tech/best-
		 *            practices-jsp-136021.html#mozTocId654884.
		 * @param attributeName
		 *            The MBean Attribute Name.
		 * @param metricName
		 *            The name of metric. This name is also used for the
		 *            .csv-filename.
		 */
		public MBeanMonitoring(ObjectName mbeanName, String attributeName, String metricName) {
			this.mbeanName = mbeanName;
			this.attributeName = attributeName;
			this.metricName = metricName;
			this.gauge = new Gauge<T>() {
				@Override
				public T getValue() {
					return value;
				}
			};
		}

		@SuppressWarnings("unchecked")
		public void setValue(Object value) {
			this.value = (T) value;
		}
	}
}
