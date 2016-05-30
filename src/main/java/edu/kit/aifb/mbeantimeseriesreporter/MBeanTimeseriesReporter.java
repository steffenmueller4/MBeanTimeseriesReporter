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

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.management.MalformedObjectNameException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.codahale.metrics.CsvReporter;
import com.codahale.metrics.MetricRegistry;
import com.google.common.collect.Maps;

import edu.kit.aifb.mbeantimeseriesreporter.xml.MBeans;

/**
 * The Main class of the MBean Timeseries Reporter.
 * 
 * @author Steffen Mueller
 * @version 1.0.0
 */
public class MBeanTimeseriesReporter {

	private static final Map<String, JmxQueryClient> CONNECTED_HOST_LIST = Maps.newHashMap();
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
	static final int TIME_INTERVAL_IN_MS_DEFAULTVALUE = 10000;

	private MetricRegistry metricsRegistry;
	private CsvReporter reporter;

	@Parameter(names = "-hosts", converter = HostPortConverter.class, description = "Specifies the hosts that should be queried, for example: -hosts <IP-address:port> -hosts <IP-address:port> ...", required = true)
	private List<HostPort> ipAddressList;
	@Parameter(converter = FileConverter.class, description = "The configuration file with the MBeans which should be queried.", required = true, names = "-file")
	private File mbeanConfigurationFile;
	@Parameter(names = "-t", description = "The time interval for checking and writing the monitored attributes in milliseconds. Default is: 10000.", arity = 1)
	private int timeInterval = TIME_INTERVAL_IN_MS_DEFAULTVALUE;

	private MBeanTimeseriesReporter() {

	}

	public static void main(String[] args) {
		try {
			MBeanTimeseriesReporter monitor = new MBeanTimeseriesReporter();
			new JCommander(monitor, args);
			monitor.run();
		} catch (IOException | MalformedObjectNameException | MBeanTimeseriesReporterException | InterruptedException
				| JAXBException e) {
			e.printStackTrace();
		}
	}

	private void run() throws IOException, MalformedObjectNameException, MBeanTimeseriesReporterException,
			InterruptedException, JAXBException {
		// Parse MBean configuration
		JAXBContext jaxbContext = JAXBContext.newInstance(MBeans.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		MBeans mbeans = (MBeans) jaxbUnmarshaller.unmarshal(mbeanConfigurationFile);

		// Prepare MetricsRegistry
		metricsRegistry = new MetricRegistry();
		File metricsDir = new File("" + DATE_FORMAT.format(new Date()) + "/");
		metricsDir.mkdir();
		reporter = CsvReporter.forRegistry(metricsRegistry).formatFor(Locale.GERMANY).convertRatesTo(TimeUnit.SECONDS)
				.convertDurationsTo(TimeUnit.MILLISECONDS).build(metricsDir);
		reporter.start(timeInterval, TimeUnit.MILLISECONDS);

		// Create JMX Clients
		for (HostPort hostPort : ipAddressList) {
			String ipAddress = hostPort.host + ":" + hostPort.port;
			String name = "ip-" + hostPort.host.replace(".", "-");

			JmxQueryClient t = new JmxQueryClient(metricsRegistry, ipAddress, mbeans, name, timeInterval);
			CONNECTED_HOST_LIST.put(name + "Jmx", t);
			t.start();
		}

		System.out.println("Press enter to stop!");
		System.in.read();

		System.out.println("Stopping, please wait...");
		for (JmxQueryClient t : CONNECTED_HOST_LIST.values()) {
			t.setRun(false);
			t.join();
		}

		System.out.println();
		System.out.println("Bye! Bye!");
	}
}
