package edu.kit.aifb.mbeantimeseriesreporter;

import java.io.IOException;
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
import java.util.List;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.codahale.metrics.MetricRegistry;
import com.google.common.collect.Lists;

import edu.kit.aifb.mbeantimeseriesreporter.xml.MBean;
import edu.kit.aifb.mbeantimeseriesreporter.xml.MBeans;

/**
 * A client that connects to a JMX Server and queries MBeans.
 * 
 * @author Steffen Mueller
 * @version 1.0.0
 */
public final class JmxQueryClient extends Thread {

	private static final String JMX_SERVICE_URL = "service:jmx:rmi:///jndi/rmi://%s/jmxrmi";
	private final String ipAddress;
	private final MetricRegistry metricsRegistry;
	private boolean run = true;
	private MBeanServerConnection mbeanServerConnection;
	private JMXConnector jmxConnector;
	private MBeans mbeans;
	private final String clientName;
	private final int timeInterval;

	private List<DynamicMBeanMetrics> mbeanMetrics = Lists.newArrayList();

	/**
	 * 
	 * @param metricsRegistry
	 * @param ipAddress
	 * @param mbeans
	 * @param clientName
	 * @throws MBeanTimeseriesReporterException
	 *             If an error occurs.
	 */
	public JmxQueryClient(MetricRegistry metricsRegistry, String ipAddress, MBeans mbeans, String clientName, int timeInterval)
			throws MBeanTimeseriesReporterException {
		this.metricsRegistry = metricsRegistry;
		this.ipAddress = ipAddress;
		this.mbeans = mbeans;
		this.clientName = clientName;
		this.timeInterval = timeInterval;
	}

	/**
	 * Connects to a MBean server.
	 * 
	 * @throws MBeanTimeseriesReporterException
	 *             If an error occurs.
	 */
	public void connect() throws MBeanTimeseriesReporterException {
		try {
			JMXServiceURL url = new JMXServiceURL(String.format(JMX_SERVICE_URL, ipAddress));
			jmxConnector = JMXConnectorFactory.connect(url, null);
			mbeanServerConnection = jmxConnector.getMBeanServerConnection();

			for (MBean mbean : mbeans.getMbeans()) {
				DynamicMBeanMetrics mbeanMetric = new DynamicMBeanMetrics(metricsRegistry, mbeanServerConnection,
						clientName, mbean);
				mbeanMetrics.add(mbeanMetric);
			}
		} catch (IOException e) {
			System.out.println("Error while connecting!" + e.getMessage());
		}
	}

	/**
	 * Queries the MBean attributes from the MBean.
	 * 
	 * @throws MBeanTimeseriesReporterException
	 *             If an error occurs.
	 */
	private void query() throws MBeanTimeseriesReporterException {
		boolean error = false;
		for (DynamicMBeanMetrics mbeanMetric : mbeanMetrics) {
			try {
				mbeanMetric.query();
			} catch (MBeanTimeseriesReporterException e) {
				System.out.println("MBean " + mbeanMetric.toString() + " threw an exception!");
				error = true;
			}
		}
		if (!error)
			System.out.println("All MBeans queried successfully.");
	}

	public void disconnect() throws MBeanTimeseriesReporterException {
		try {
			jmxConnector.close();
		} catch (IOException e) {
			throw new MBeanTimeseriesReporterException(e.getMessage());
		}
	}

	@Override
	public void run() {
		try {
			connect();
			while (run) {
				query();

				Thread.sleep(timeInterval);
			}
			disconnect();
		} catch (MBeanTimeseriesReporterException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void interrupt() {
		try {
			run = false;

			disconnect();
		} catch (MBeanTimeseriesReporterException e) {
			e.printStackTrace();
		}
		super.interrupt();
	}

	public void setRun(boolean run) {
		this.run = run;
	}
}