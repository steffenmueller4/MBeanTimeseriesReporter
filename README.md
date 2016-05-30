# MBeanTimeseriesReporter
This project provides a quick and dirty option to record timeseries from JMX MBeans to CSV files (see also: [1]). From these CSV files graphs and analyses can be created.

To start MBeanTimeseriesReporter, just build the executable .jar file by running:

1. Check out the source code.
2. Change to the source directory
3. run Maven within the source directory: mvn package

After building the executable .jar file, invoke the executable .jar by, for example:

java -jar MBeanTimeseriesReporter-x.x.x.jar -file xmlMBeanConfigFile.xml -hosts 192.168.0.1:7199 -hosts 192.168.0.2:7199

This starts the MBeanTimeseriesReporter using the configuration file 'xmlMBeanConfigFile.xml' (see also: XML Configuration File) and monitors the hosts '192.168.0.1' and '192.168.0.2' at port 7199 (see also: Start Parameters).
Therefore, the MBeans that should be monitored have to be accessible from the MBeanTimeseriesReporter machine. This may be checked, for instance, via the JConsole (see also: [2]).
Currently, MBeanTimeseriesReporter can not connect to secured MBean servers.

## XML Configuration File

A sample 'xmlMBeanConfigFile.xml' is:

<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<MBeans>
 <MBean name="OperatingSystem" objectname="java.lang:type=OperatingSystem">
  <Attribute name="SystemCpuLoad">
   <GaugeType>Double</GaugeType>
   <MetricName>SystemCpuLoad</MetricName>
  </Attribute>
  <Attribute name="ProcessCpuLoad">
   <GaugeType>Double</GaugeType>
   <MetricName>ProcessCpuLoad</MetricName>
  </Attribute>
 </MBean>
 <MBean name="Runtime" objectname="java.lang:type=Runtime">
  <Attribute name="JREUptime">
   <GaugeType>Long</GaugeType>
   <MetricName>Uptime</MetricName>
  </Attribute>
 </MBean>
</MBeans>

This 'xmlMBeanConfigFile.xml' monitors the Java MBeans 'java.lang:type=OperatingSystem' and 'java.lang:type=Runtime' and their attributes. For the MBean 'java.lang:type=OperatingSystem', two attributes are recorded to CSV files, the attribute 'SystemCpuLoad' and the attribute 'ProcessCpuLoad'. Both attributes are double values. For the MBean 'java.lang:type=Runtime', additionally, the attribute 'JREUptime' is monitored. This attribute is a long value. If you define attributes with numeric values in the configuration file---e.g. all attributes in the sample 'xmlMBeanConfigFile.xml'---, use only numeric wrapper classes.

For every defined attribute, a file is created by MBeanTimeseriesReporter and the values are recorded using a specific time interval (see also: parameter t in Start Parameters).

## Start Parameters

file : The configuration file with the MBeans which should be queried.
hosts : Specifies the hosts that should be queried, for example: -hosts <IP-address:port> -hosts <IP-address:port> ...
t : The time interval for checking and writing the monitored attributes in milliseconds. Default is: 10000.

## References
[1] https://docs.oracle.com/javase/tutorial/jmx/mbeans/index.html
[2] http://openjdk.java.net/tools/svc/jconsole/
